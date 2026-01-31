package rx.internal.util.unsafe;

import java.util.Iterator;

/* loaded from: classes.dex */
public abstract class ConcurrentCircularArrayQueue<E> extends ConcurrentCircularArrayQueueL0Pad<E> {
    protected static final int BUFFER_PAD = 32;
    private static final long REF_ARRAY_BASE;
    private static final int REF_ELEMENT_SHIFT;
    protected static final int SPARSE_SHIFT = Integer.getInteger("sparse.shift", 0).intValue();
    protected final E[] buffer;
    protected final long mask;

    static {
        int scale = UnsafeAccess.UNSAFE.arrayIndexScale(Object[].class);
        if (4 == scale) {
            REF_ELEMENT_SHIFT = SPARSE_SHIFT + 2;
        } else if (8 == scale) {
            REF_ELEMENT_SHIFT = SPARSE_SHIFT + 3;
        } else {
            throw new IllegalStateException("Unknown pointer size");
        }
        REF_ARRAY_BASE = UnsafeAccess.UNSAFE.arrayBaseOffset(Object[].class) + (32 << (REF_ELEMENT_SHIFT - SPARSE_SHIFT));
    }

    public ConcurrentCircularArrayQueue(int i) {
        int iRoundToPowerOfTwo = Pow2.roundToPowerOfTwo(i);
        this.mask = iRoundToPowerOfTwo - 1;
        this.buffer = (E[]) new Object[(iRoundToPowerOfTwo << SPARSE_SHIFT) + 64];
    }

    protected final long calcElementOffset(long index) {
        return calcElementOffset(index, this.mask);
    }

    protected final long calcElementOffset(long index, long mask) {
        return REF_ARRAY_BASE + ((index & mask) << REF_ELEMENT_SHIFT);
    }

    protected final void spElement(long offset, E e) {
        spElement(this.buffer, offset, e);
    }

    protected final void spElement(E[] buffer, long offset, E e) {
        UnsafeAccess.UNSAFE.putObject(buffer, offset, e);
    }

    protected final void soElement(long offset, E e) {
        soElement(this.buffer, offset, e);
    }

    protected final void soElement(E[] buffer, long offset, E e) {
        UnsafeAccess.UNSAFE.putOrderedObject(buffer, offset, e);
    }

    protected final E lpElement(long offset) {
        return lpElement(this.buffer, offset);
    }

    protected final E lpElement(E[] eArr, long j) {
        return (E) UnsafeAccess.UNSAFE.getObject(eArr, j);
    }

    protected final E lvElement(long offset) {
        return lvElement(this.buffer, offset);
    }

    protected final E lvElement(E[] eArr, long j) {
        return (E) UnsafeAccess.UNSAFE.getObjectVolatile(eArr, j);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        while (true) {
            if (poll() == null && isEmpty()) {
                return;
            }
        }
    }
}
