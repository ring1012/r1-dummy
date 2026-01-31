package rx.internal.util.unsafe;

/* loaded from: classes.dex */
public abstract class ConcurrentSequencedCircularArrayQueue<E> extends ConcurrentCircularArrayQueue<E> {
    private static final long ARRAY_BASE;
    private static final int ELEMENT_SHIFT;
    protected final long[] sequenceBuffer;

    static {
        int scale = UnsafeAccess.UNSAFE.arrayIndexScale(long[].class);
        if (8 == scale) {
            ELEMENT_SHIFT = SPARSE_SHIFT + 3;
            ARRAY_BASE = UnsafeAccess.UNSAFE.arrayBaseOffset(long[].class) + (32 << (ELEMENT_SHIFT - SPARSE_SHIFT));
            return;
        }
        throw new IllegalStateException("Unexpected long[] element size");
    }

    public ConcurrentSequencedCircularArrayQueue(int capacity) {
        super(capacity);
        int actualCapacity = (int) (this.mask + 1);
        this.sequenceBuffer = new long[(actualCapacity << SPARSE_SHIFT) + 64];
        for (long i = 0; i < actualCapacity; i++) {
            soSequence(this.sequenceBuffer, calcSequenceOffset(i), i);
        }
    }

    protected final long calcSequenceOffset(long index) {
        return ARRAY_BASE + ((this.mask & index) << ELEMENT_SHIFT);
    }

    protected final void soSequence(long[] buffer, long offset, long e) {
        UnsafeAccess.UNSAFE.putOrderedLong(buffer, offset, e);
    }

    protected final long lvSequence(long[] buffer, long offset) {
        return UnsafeAccess.UNSAFE.getLongVolatile(buffer, offset);
    }
}
