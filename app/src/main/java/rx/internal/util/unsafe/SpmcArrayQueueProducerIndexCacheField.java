package rx.internal.util.unsafe;

/* compiled from: SpmcArrayQueue.java */
/* loaded from: classes.dex */
abstract class SpmcArrayQueueProducerIndexCacheField<E> extends SpmcArrayQueueMidPad<E> {
    private volatile long producerIndexCache;

    public SpmcArrayQueueProducerIndexCacheField(int capacity) {
        super(capacity);
    }

    protected final long lvProducerIndexCache() {
        return this.producerIndexCache;
    }

    protected final void svProducerIndexCache(long v) {
        this.producerIndexCache = v;
    }
}
