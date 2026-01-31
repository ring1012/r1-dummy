package rx.internal.util.unsafe;

/* loaded from: classes.dex */
public class MpmcArrayQueue<E> extends MpmcArrayQueueConsumerField<E> {
    long p30;
    long p31;
    long p32;
    long p33;
    long p34;
    long p35;
    long p36;
    long p37;
    long p40;
    long p41;
    long p42;
    long p43;
    long p44;
    long p45;
    long p46;

    public MpmcArrayQueue(int capacity) {
        super(Math.max(2, capacity));
    }

    @Override // java.util.Queue, rx.internal.util.unsafe.MessagePassingQueue
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException("Null is not a valid element");
        }
        long capacity = this.mask + 1;
        long[] lSequenceBuffer = this.sequenceBuffer;
        long cIndex = Long.MAX_VALUE;
        while (true) {
            long currentProducerIndex = lvProducerIndex();
            long seqOffset = calcSequenceOffset(currentProducerIndex);
            long seq = lvSequence(lSequenceBuffer, seqOffset);
            long delta = seq - currentProducerIndex;
            if (delta == 0) {
                if (casProducerIndex(currentProducerIndex, 1 + currentProducerIndex)) {
                    long elementOffset = calcElementOffset(currentProducerIndex);
                    spElement(elementOffset, e);
                    soSequence(lSequenceBuffer, seqOffset, 1 + currentProducerIndex);
                    return true;
                }
            } else if (delta < 0 && currentProducerIndex - capacity <= cIndex) {
                long j = currentProducerIndex - capacity;
                cIndex = lvConsumerIndex();
                if (j <= cIndex) {
                    return false;
                }
            }
        }
    }

    @Override // java.util.Queue, rx.internal.util.unsafe.MessagePassingQueue
    public E poll() {
        long[] lSequenceBuffer = this.sequenceBuffer;
        long pIndex = -1;
        while (true) {
            long currentConsumerIndex = lvConsumerIndex();
            long seqOffset = calcSequenceOffset(currentConsumerIndex);
            long seq = lvSequence(lSequenceBuffer, seqOffset);
            long delta = seq - (1 + currentConsumerIndex);
            if (delta == 0) {
                if (casConsumerIndex(currentConsumerIndex, 1 + currentConsumerIndex)) {
                    long offset = calcElementOffset(currentConsumerIndex);
                    E e = lpElement(offset);
                    spElement(offset, null);
                    soSequence(lSequenceBuffer, seqOffset, this.mask + currentConsumerIndex + 1);
                    return e;
                }
            } else if (delta < 0 && currentConsumerIndex >= pIndex) {
                pIndex = lvProducerIndex();
                if (currentConsumerIndex == pIndex) {
                    return null;
                }
            }
        }
    }

    @Override // java.util.Queue, rx.internal.util.unsafe.MessagePassingQueue
    public E peek() {
        long currConsumerIndex;
        E e;
        do {
            currConsumerIndex = lvConsumerIndex();
            e = lpElement(calcElementOffset(currConsumerIndex));
            if (e != null) {
                break;
            }
        } while (currConsumerIndex != lvProducerIndex());
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
