package rx.internal.util.unsafe;

/* loaded from: classes.dex */
public final class SpmcArrayQueue<E> extends SpmcArrayQueueL3Pad<E> {
    public SpmcArrayQueue(int capacity) {
        super(capacity);
    }

    @Override // java.util.Queue, rx.internal.util.unsafe.MessagePassingQueue
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException("Null is not a valid element");
        }
        E[] lb = this.buffer;
        long lMask = this.mask;
        long currProducerIndex = lvProducerIndex();
        long offset = calcElementOffset(currProducerIndex);
        if (lvElement(lb, offset) != null) {
            long size = currProducerIndex - lvConsumerIndex();
            if (size > lMask) {
                return false;
            }
            while (lvElement(lb, offset) != null) {
            }
        }
        spElement(lb, offset, e);
        soTail(1 + currProducerIndex);
        return true;
    }

    @Override // java.util.Queue, rx.internal.util.unsafe.MessagePassingQueue
    public E poll() {
        long currentConsumerIndex;
        long currProducerIndexCache = lvProducerIndexCache();
        do {
            currentConsumerIndex = lvConsumerIndex();
            if (currentConsumerIndex >= currProducerIndexCache) {
                long currProducerIndex = lvProducerIndex();
                if (currentConsumerIndex >= currProducerIndex) {
                    return null;
                }
                svProducerIndexCache(currProducerIndex);
            }
        } while (!casHead(currentConsumerIndex, 1 + currentConsumerIndex));
        long offset = calcElementOffset(currentConsumerIndex);
        E[] lb = this.buffer;
        E eLpElement = lpElement(lb, offset);
        soElement(lb, offset, null);
        return eLpElement;
    }

    @Override // java.util.Queue, rx.internal.util.unsafe.MessagePassingQueue
    public E peek() {
        E e;
        long currProducerIndexCache = lvProducerIndexCache();
        do {
            long currentConsumerIndex = lvConsumerIndex();
            if (currentConsumerIndex >= currProducerIndexCache) {
                long currProducerIndex = lvProducerIndex();
                if (currentConsumerIndex >= currProducerIndex) {
                    return null;
                }
                svProducerIndexCache(currProducerIndex);
            }
            e = lvElement(calcElementOffset(currentConsumerIndex));
        } while (e == null);
        return e;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, rx.internal.util.unsafe.MessagePassingQueue
    public int size() {
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

    @Override // java.util.AbstractCollection, java.util.Collection, rx.internal.util.unsafe.MessagePassingQueue
    public boolean isEmpty() {
        return lvConsumerIndex() == lvProducerIndex();
    }
}
