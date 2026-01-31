package rx.schedulers;

import java.util.concurrent.TimeUnit;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.BooleanSubscription;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public final class ImmediateScheduler extends Scheduler {
    private static final ImmediateScheduler INSTANCE = new ImmediateScheduler();

    static ImmediateScheduler instance() {
        return INSTANCE;
    }

    ImmediateScheduler() {
    }

    @Override // rx.Scheduler
    public Scheduler.Worker createWorker() {
        return new InnerImmediateScheduler();
    }

    private class InnerImmediateScheduler extends Scheduler.Worker implements Subscription {
        final BooleanSubscription innerSubscription;

        private InnerImmediateScheduler() {
            this.innerSubscription = new BooleanSubscription();
        }

        @Override // rx.Scheduler.Worker
        public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
            long execTime = ImmediateScheduler.this.now() + unit.toMillis(delayTime);
            return schedule(new SleepingAction(action, this, execTime));
        }

        @Override // rx.Scheduler.Worker
        public Subscription schedule(Action0 action) {
            action.call();
            return Subscriptions.unsubscribed();
        }

        @Override // rx.Subscription
        public void unsubscribe() {
            this.innerSubscription.unsubscribe();
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return this.innerSubscription.isUnsubscribed();
        }
    }
}
