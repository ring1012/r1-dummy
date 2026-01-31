package rx.internal.util.unsafe;

import rx.internal.util.atomic.LinkedQueueNode;

/* compiled from: BaseLinkedQueue.java */
/* loaded from: classes.dex */
abstract class BaseLinkedQueueConsumerNodeRef<E> extends BaseLinkedQueuePad1<E> {
    protected static final long C_NODE_OFFSET = UnsafeAccess.addressOf(BaseLinkedQueueConsumerNodeRef.class, "consumerNode");
    protected LinkedQueueNode<E> consumerNode;

    BaseLinkedQueueConsumerNodeRef() {
    }

    protected final void spConsumerNode(LinkedQueueNode<E> node) {
        this.consumerNode = node;
    }

    protected final LinkedQueueNode<E> lvConsumerNode() {
        return (LinkedQueueNode) UnsafeAccess.UNSAFE.getObjectVolatile(this, C_NODE_OFFSET);
    }

    protected final LinkedQueueNode<E> lpConsumerNode() {
        return this.consumerNode;
    }
}
