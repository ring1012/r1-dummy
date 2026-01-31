package rx.internal.util.atomic;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
abstract class BaseLinkedAtomicQueue<E> extends AbstractQueue<E> {
    private final AtomicReference<LinkedQueueNode<E>> producerNode = new AtomicReference<>();
    private final AtomicReference<LinkedQueueNode<E>> consumerNode = new AtomicReference<>();

    protected final LinkedQueueNode<E> lvProducerNode() {
        return this.producerNode.get();
    }

    protected final LinkedQueueNode<E> lpProducerNode() {
        return this.producerNode.get();
    }

    protected final void spProducerNode(LinkedQueueNode<E> node) {
        this.producerNode.lazySet(node);
    }

    protected final LinkedQueueNode<E> xchgProducerNode(LinkedQueueNode<E> node) {
        return this.producerNode.getAndSet(node);
    }

    protected final LinkedQueueNode<E> lvConsumerNode() {
        return this.consumerNode.get();
    }

    protected final LinkedQueueNode<E> lpConsumerNode() {
        return this.consumerNode.get();
    }

    protected final void spConsumerNode(LinkedQueueNode<E> node) {
        this.consumerNode.lazySet(node);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final int size() {
        LinkedQueueNode<E> next;
        LinkedQueueNode<E> chaserNode = lvConsumerNode();
        LinkedQueueNode<E> producerNode = lvProducerNode();
        int size = 0;
        while (chaserNode != producerNode && size < Integer.MAX_VALUE) {
            do {
                next = chaserNode.lvNext();
            } while (next == null);
            chaserNode = next;
            size++;
        }
        return size;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public final boolean isEmpty() {
        return lvConsumerNode() == lvProducerNode();
    }
}
