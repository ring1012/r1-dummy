package rx.internal.util.unsafe;

/* compiled from: SpscUnboundedArrayQueue.java */
/* loaded from: classes.dex */
abstract class SpscUnboundedArrayQueueConsumerColdField<E> extends SpscUnboundedArrayQueueL2Pad<E> {
    protected E[] consumerBuffer;
    protected long consumerMask;

    SpscUnboundedArrayQueueConsumerColdField() {
    }
}
