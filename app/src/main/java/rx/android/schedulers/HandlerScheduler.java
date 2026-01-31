package rx.android.schedulers;

import android.os.Handler;
import java.util.concurrent.TimeUnit;
import rx.Scheduler;
import rx.Subscription;
import rx.android.plugins.RxAndroidPlugins;
import rx.functions.Action0;
import rx.internal.schedulers.ScheduledAction;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public final class HandlerScheduler extends Scheduler {
    private final Handler handler;

    public static HandlerScheduler from(Handler handler) {
        if (handler == null) {
            throw new NullPointerException("handler == null");
        }
        return new HandlerScheduler(handler);
    }

    HandlerScheduler(Handler handler) {
        this.handler = handler;
    }

    @Override // rx.Scheduler
    public Scheduler.Worker createWorker() {
        return new HandlerWorker(this.handler);
    }

    static class HandlerWorker extends Scheduler.Worker {
        private final CompositeSubscription compositeSubscription = new CompositeSubscription();
        private final Handler handler;

        HandlerWorker(Handler handler) {
            this.handler = handler;
        }

        @Override // rx.Subscription
        public void unsubscribe() throws Throwable {
            this.compositeSubscription.unsubscribe();
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return this.compositeSubscription.isUnsubscribed();
        }

        @Override // rx.Scheduler.Worker
        public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
            if (this.compositeSubscription.isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            final ScheduledAction scheduledAction = new ScheduledAction(RxAndroidPlugins.getInstance().getSchedulersHook().onSchedule(action));
            scheduledAction.addParent(this.compositeSubscription);
            this.compositeSubscription.add(scheduledAction);
            this.handler.postDelayed(scheduledAction, unit.toMillis(delayTime));
            scheduledAction.add(Subscriptions.create(new Action0() { // from class: rx.android.schedulers.HandlerScheduler.HandlerWorker.1
                @Override // rx.functions.Action0
                public void call() {
                    HandlerWorker.this.handler.removeCallbacks(scheduledAction);
                }
            }));
            return scheduledAction;
        }

        @Override // rx.Scheduler.Worker
        public Subscription schedule(Action0 action) {
            return schedule(action, 0L, TimeUnit.MILLISECONDS);
        }
    }
}
