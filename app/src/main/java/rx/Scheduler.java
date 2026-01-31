package rx;

import java.util.concurrent.TimeUnit;
import rx.functions.Action0;
import rx.subscriptions.MultipleAssignmentSubscription;

/* loaded from: classes.dex */
public abstract class Scheduler {
    public abstract Worker createWorker();

    public static abstract class Worker implements Subscription {
        public abstract Subscription schedule(Action0 action0);

        public abstract Subscription schedule(Action0 action0, long j, TimeUnit timeUnit);

        public Subscription schedulePeriodically(final Action0 action, long initialDelay, long period, TimeUnit unit) {
            final long periodInNanos = unit.toNanos(period);
            final long startInNanos = TimeUnit.MILLISECONDS.toNanos(now()) + unit.toNanos(initialDelay);
            final MultipleAssignmentSubscription mas = new MultipleAssignmentSubscription();
            Action0 recursiveAction = new Action0() { // from class: rx.Scheduler.Worker.1
                long count = 0;

                @Override // rx.functions.Action0
                public void call() {
                    if (!mas.isUnsubscribed()) {
                        action.call();
                        long j = startInNanos;
                        long j2 = this.count + 1;
                        this.count = j2;
                        long nextTick = j + (j2 * periodInNanos);
                        mas.set(Worker.this.schedule(this, nextTick - TimeUnit.MILLISECONDS.toNanos(Worker.this.now()), TimeUnit.NANOSECONDS));
                    }
                }
            };
            MultipleAssignmentSubscription s = new MultipleAssignmentSubscription();
            mas.set(s);
            s.set(schedule(recursiveAction, initialDelay, unit));
            return mas;
        }

        public long now() {
            return System.currentTimeMillis();
        }
    }

    public long now() {
        return System.currentTimeMillis();
    }
}
