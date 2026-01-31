package rx.internal.util.unsafe;

/* compiled from: SpscArrayQueue.java */
/* loaded from: classes.dex */
abstract class SpscArrayQueueColdField<E> extends ConcurrentCircularArrayQueue<E> {
    private static final Integer MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096);
    protected final int lookAheadStep;

    public SpscArrayQueueColdField(int capacity) {
        super(capacity);
        this.lookAheadStep = Math.min(capacity / 4, MAX_LOOK_AHEAD_STEP.intValue());
    }
}
