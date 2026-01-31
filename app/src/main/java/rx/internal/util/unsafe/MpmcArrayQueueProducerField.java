package rx.internal.util.unsafe;

/* compiled from: MpmcArrayQueue.java */
/* loaded from: classes.dex */
abstract class MpmcArrayQueueProducerField<E> extends MpmcArrayQueueL1Pad<E> {
    private static final long P_INDEX_OFFSET = UnsafeAccess.addressOf(MpmcArrayQueueProducerField.class, "producerIndex");
    private volatile long producerIndex;

    public MpmcArrayQueueProducerField(int capacity) {
        super(capacity);
    }

    protected final long lvProducerIndex() {
        return this.producerIndex;
    }

    protected final boolean casProducerIndex(long expect, long newValue) {
        return UnsafeAccess.UNSAFE.compareAndSwapLong(this, P_INDEX_OFFSET, expect, newValue);
    }
}
