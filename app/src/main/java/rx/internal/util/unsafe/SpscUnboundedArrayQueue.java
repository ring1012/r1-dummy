package rx.internal.util.unsafe;

import java.lang.reflect.Field;
import java.util.Iterator;

/* loaded from: classes.dex */
public class SpscUnboundedArrayQueue<E> extends SpscUnboundedArrayQueueConsumerField<E> implements QueueProgressIndicators {
    private static final long C_INDEX_OFFSET;
    private static final long P_INDEX_OFFSET;
    private static final long REF_ARRAY_BASE;
    private static final int REF_ELEMENT_SHIFT;
    static final int MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096).intValue();
    private static final Object HAS_NEXT = new Object();

    static {
        int scale = UnsafeAccess.UNSAFE.arrayIndexScale(Object[].class);
        if (4 == scale) {
            REF_ELEMENT_SHIFT = 2;
        } else if (8 == scale) {
            REF_ELEMENT_SHIFT = 3;
        } else {
            throw new IllegalStateException("Unknown pointer size");
        }
        REF_ARRAY_BASE = UnsafeAccess.UNSAFE.arrayBaseOffset(Object[].class);
        try {
            Field iField = SpscUnboundedArrayQueueProducerFields.class.getDeclaredField("producerIndex");
            P_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(iField);
            try {
                Field iField2 = SpscUnboundedArrayQueueConsumerField.class.getDeclaredField("consumerIndex");
                C_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(iField2);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchFieldException e2) {
            throw new RuntimeException(e2);
        }
    }

    public SpscUnboundedArrayQueue(int i) {
        int iRoundToPowerOfTwo = Pow2.roundToPowerOfTwo(i);
        long j = iRoundToPowerOfTwo - 1;
        E[] eArr = (E[]) new Object[iRoundToPowerOfTwo + 1];
        this.producerBuffer = eArr;
        this.producerMask = j;
        adjustLookAheadStep(iRoundToPowerOfTwo);
        this.consumerBuffer = eArr;
        this.consumerMask = j;
        this.producerLookAhead = j - 1;
        soProducerIndex(0L);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Queue
    public final boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException("Null is not a valid element");
        }
        E[] buffer = this.producerBuffer;
        long index = this.producerIndex;
        long mask = this.producerMask;
        long offset = calcWrappedOffset(index, mask);
        if (index < this.producerLookAhead) {
            return writeToQueue(buffer, e, index, offset);
        }
        int lookAheadStep = this.producerLookAheadStep;
        long lookAheadElementOffset = calcWrappedOffset(lookAheadStep + index, mask);
        if (lvElement(buffer, lookAheadElementOffset) == null) {
            this.producerLookAhead = (lookAheadStep + index) - 1;
            return writeToQueue(buffer, e, index, offset);
        }
        if (lvElement(buffer, calcWrappedOffset(1 + index, mask)) != null) {
            return writeToQueue(buffer, e, index, offset);
        }
        resize(buffer, index, offset, e, mask);
        return true;
    }

    private boolean writeToQueue(E[] buffer, E e, long index, long offset) {
        soProducerIndex(1 + index);
        soElement(buffer, offset, e);
        return true;
    }

    private void resize(E[] eArr, long j, long j2, E e, long j3) {
        E[] eArr2 = (E[]) new Object[eArr.length];
        this.producerBuffer = eArr2;
        this.producerLookAhead = (j + j3) - 1;
        soProducerIndex(j + 1);
        soElement(eArr2, j2, e);
        soNext(eArr, eArr2);
        soElement(eArr, j2, HAS_NEXT);
    }

    private void soNext(E[] curr, E[] next) {
        soElement(curr, calcDirectOffset(curr.length - 1), next);
    }

    private E[] lvNext(E[] eArr) {
        return (E[]) ((Object[]) lvElement(eArr, calcDirectOffset(eArr.length - 1)));
    }

    @Override // java.util.Queue
    public final E poll() {
        E[] eArr = this.consumerBuffer;
        long j = this.consumerIndex;
        long j2 = this.consumerMask;
        long jCalcWrappedOffset = calcWrappedOffset(j, j2);
        E e = (E) lvElement(eArr, jCalcWrappedOffset);
        boolean z = e == HAS_NEXT;
        if (e != null && !z) {
            soConsumerIndex(1 + j);
            soElement(eArr, jCalcWrappedOffset, null);
            return e;
        }
        if (z) {
            return newBufferPoll(lvNext(eArr), j, j2);
        }
        return null;
    }

    private E newBufferPoll(E[] eArr, long j, long j2) {
        this.consumerBuffer = eArr;
        long jCalcWrappedOffset = calcWrappedOffset(j, j2);
        E e = (E) lvElement(eArr, jCalcWrappedOffset);
        if (e == null) {
            return null;
        }
        soConsumerIndex(1 + j);
        soElement(eArr, jCalcWrappedOffset, null);
        return e;
    }

    @Override // java.util.Queue
    public final E peek() {
        E[] eArr = this.consumerBuffer;
        long j = this.consumerIndex;
        long j2 = this.consumerMask;
        E e = (E) lvElement(eArr, calcWrappedOffset(j, j2));
        if (e == HAS_NEXT) {
            return newBufferPeek(lvNext(eArr), j, j2);
        }
        return e;
    }

    private E newBufferPeek(E[] eArr, long j, long j2) {
        this.consumerBuffer = eArr;
        return (E) lvElement(eArr, calcWrappedOffset(j, j2));
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final int size() {
        long before;
        long currentProducerIndex;
        long after = lvConsumerIndex();
        do {
            before = after;
            currentProducerIndex = lvProducerIndex();
            after = lvConsumerIndex();
        } while (before != after);
        return (int) (currentProducerIndex - after);
    }

    private void adjustLookAheadStep(int capacity) {
        this.producerLookAheadStep = Math.min(capacity / 4, MAX_LOOK_AHEAD_STEP);
    }

    private long lvProducerIndex() {
        return UnsafeAccess.UNSAFE.getLongVolatile(this, P_INDEX_OFFSET);
    }

    private long lvConsumerIndex() {
        return UnsafeAccess.UNSAFE.getLongVolatile(this, C_INDEX_OFFSET);
    }

    private void soProducerIndex(long v) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, P_INDEX_OFFSET, v);
    }

    private void soConsumerIndex(long v) {
        UnsafeAccess.UNSAFE.putOrderedLong(this, C_INDEX_OFFSET, v);
    }

    private static final long calcWrappedOffset(long index, long mask) {
        return calcDirectOffset(index & mask);
    }

    private static final long calcDirectOffset(long index) {
        return REF_ARRAY_BASE + (index << REF_ELEMENT_SHIFT);
    }

    private static final void soElement(Object[] buffer, long offset, Object e) {
        UnsafeAccess.UNSAFE.putOrderedObject(buffer, offset, e);
    }

    private static final <E> Object lvElement(E[] buffer, long offset) {
        return UnsafeAccess.UNSAFE.getObjectVolatile(buffer, offset);
    }

    @Override // rx.internal.util.unsafe.QueueProgressIndicators
    public long currentProducerIndex() {
        return lvProducerIndex();
    }

    @Override // rx.internal.util.unsafe.QueueProgressIndicators
    public long currentConsumerIndex() {
        return lvConsumerIndex();
    }
}
