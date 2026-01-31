package rx.internal.util.atomic;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceArray;
import rx.internal.util.unsafe.Pow2;

/* loaded from: classes.dex */
public final class SpscUnboundedAtomicArrayQueue<T> implements Queue<T> {
    protected AtomicReferenceArray<Object> consumerBuffer;
    protected volatile long consumerIndex;
    protected int consumerMask;
    protected AtomicReferenceArray<Object> producerBuffer;
    protected volatile long producerIndex;
    protected long producerLookAhead;
    protected int producerLookAheadStep;
    protected int producerMask;
    static final int MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096).intValue();
    static final AtomicLongFieldUpdater<SpscUnboundedAtomicArrayQueue> PRODUCER_INDEX = AtomicLongFieldUpdater.newUpdater(SpscUnboundedAtomicArrayQueue.class, "producerIndex");
    static final AtomicLongFieldUpdater<SpscUnboundedAtomicArrayQueue> CONSUMER_INDEX = AtomicLongFieldUpdater.newUpdater(SpscUnboundedAtomicArrayQueue.class, "consumerIndex");
    private static final Object HAS_NEXT = new Object();

    public SpscUnboundedAtomicArrayQueue(int bufferSize) {
        int p2capacity = Pow2.roundToPowerOfTwo(Math.max(8, bufferSize));
        int mask = p2capacity - 1;
        AtomicReferenceArray<Object> buffer = new AtomicReferenceArray<>(p2capacity + 1);
        this.producerBuffer = buffer;
        this.producerMask = mask;
        adjustLookAheadStep(p2capacity);
        this.consumerBuffer = buffer;
        this.consumerMask = mask;
        this.producerLookAhead = mask - 1;
        soProducerIndex(0L);
    }

    @Override // java.util.Queue
    public final boolean offer(T e) {
        if (e == null) {
            throw new NullPointerException();
        }
        AtomicReferenceArray<Object> buffer = this.producerBuffer;
        long index = lpProducerIndex();
        int mask = this.producerMask;
        int offset = calcWrappedOffset(index, mask);
        if (index < this.producerLookAhead) {
            return writeToQueue(buffer, e, index, offset);
        }
        int lookAheadStep = this.producerLookAheadStep;
        int lookAheadElementOffset = calcWrappedOffset(lookAheadStep + index, mask);
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

    private boolean writeToQueue(AtomicReferenceArray<Object> buffer, T e, long index, int offset) {
        soProducerIndex(1 + index);
        soElement(buffer, offset, e);
        return true;
    }

    private void resize(AtomicReferenceArray<Object> oldBuffer, long currIndex, int offset, T e, long mask) {
        int capacity = oldBuffer.length();
        AtomicReferenceArray<Object> newBuffer = new AtomicReferenceArray<>(capacity);
        this.producerBuffer = newBuffer;
        this.producerLookAhead = (currIndex + mask) - 1;
        soProducerIndex(currIndex + 1);
        soElement(newBuffer, offset, e);
        soNext(oldBuffer, newBuffer);
        soElement(oldBuffer, offset, HAS_NEXT);
    }

    private void soNext(AtomicReferenceArray<Object> curr, AtomicReferenceArray<Object> next) {
        soElement(curr, calcDirectOffset(curr.length() - 1), next);
    }

    private AtomicReferenceArray<Object> lvNext(AtomicReferenceArray<Object> curr) {
        return (AtomicReferenceArray) lvElement(curr, calcDirectOffset(curr.length() - 1));
    }

    @Override // java.util.Queue
    public final T poll() {
        AtomicReferenceArray<Object> atomicReferenceArray = this.consumerBuffer;
        long jLpConsumerIndex = lpConsumerIndex();
        int i = this.consumerMask;
        int iCalcWrappedOffset = calcWrappedOffset(jLpConsumerIndex, i);
        T t = (T) lvElement(atomicReferenceArray, iCalcWrappedOffset);
        boolean z = t == HAS_NEXT;
        if (t != null && !z) {
            soConsumerIndex(1 + jLpConsumerIndex);
            soElement(atomicReferenceArray, iCalcWrappedOffset, null);
            return t;
        }
        if (z) {
            return newBufferPoll(lvNext(atomicReferenceArray), jLpConsumerIndex, i);
        }
        return null;
    }

    private T newBufferPoll(AtomicReferenceArray<Object> atomicReferenceArray, long j, int i) {
        this.consumerBuffer = atomicReferenceArray;
        int iCalcWrappedOffset = calcWrappedOffset(j, i);
        T t = (T) lvElement(atomicReferenceArray, iCalcWrappedOffset);
        if (t == null) {
            return null;
        }
        soConsumerIndex(1 + j);
        soElement(atomicReferenceArray, iCalcWrappedOffset, null);
        return t;
    }

    @Override // java.util.Queue
    public final T peek() {
        AtomicReferenceArray<Object> atomicReferenceArray = this.consumerBuffer;
        long jLpConsumerIndex = lpConsumerIndex();
        int i = this.consumerMask;
        T t = (T) lvElement(atomicReferenceArray, calcWrappedOffset(jLpConsumerIndex, i));
        if (t == HAS_NEXT) {
            return newBufferPeek(lvNext(atomicReferenceArray), jLpConsumerIndex, i);
        }
        return t;
    }

    @Override // java.util.Collection
    public void clear() {
        while (true) {
            if (poll() == null && isEmpty()) {
                return;
            }
        }
    }

    private T newBufferPeek(AtomicReferenceArray<Object> atomicReferenceArray, long j, int i) {
        this.consumerBuffer = atomicReferenceArray;
        return (T) lvElement(atomicReferenceArray, calcWrappedOffset(j, i));
    }

    @Override // java.util.Collection
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

    @Override // java.util.Collection
    public boolean isEmpty() {
        return lvProducerIndex() == lvConsumerIndex();
    }

    private void adjustLookAheadStep(int capacity) {
        this.producerLookAheadStep = Math.min(capacity / 4, MAX_LOOK_AHEAD_STEP);
    }

    private long lvProducerIndex() {
        return this.producerIndex;
    }

    private long lvConsumerIndex() {
        return this.consumerIndex;
    }

    private long lpProducerIndex() {
        return this.producerIndex;
    }

    private long lpConsumerIndex() {
        return this.consumerIndex;
    }

    private void soProducerIndex(long v) {
        PRODUCER_INDEX.lazySet(this, v);
    }

    private void soConsumerIndex(long v) {
        CONSUMER_INDEX.lazySet(this, v);
    }

    private static final int calcWrappedOffset(long index, int mask) {
        return calcDirectOffset(((int) index) & mask);
    }

    private static final int calcDirectOffset(int index) {
        return index;
    }

    private static final void soElement(AtomicReferenceArray<Object> buffer, int offset, Object e) {
        buffer.lazySet(offset, e);
    }

    private static final <E> Object lvElement(AtomicReferenceArray<Object> buffer, int offset) {
        return buffer.get(offset);
    }

    @Override // java.util.Collection, java.lang.Iterable
    public final Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public <E> E[] toArray(E[] a2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Queue, java.util.Collection
    public boolean add(T e) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Queue
    public T remove() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Queue
    public T element() {
        throw new UnsupportedOperationException();
    }
}
