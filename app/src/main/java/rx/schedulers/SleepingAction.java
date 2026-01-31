package rx.schedulers;

import rx.Scheduler;
import rx.functions.Action0;

/* loaded from: classes.dex */
class SleepingAction implements Action0 {
    private final long execTime;
    private final Scheduler.Worker innerScheduler;
    private final Action0 underlying;

    public SleepingAction(Action0 underlying, Scheduler.Worker scheduler, long execTime) {
        this.underlying = underlying;
        this.innerScheduler = scheduler;
        this.execTime = execTime;
    }

    @Override // rx.functions.Action0
    public void call() throws InterruptedException {
        if (!this.innerScheduler.isUnsubscribed()) {
            if (this.execTime > this.innerScheduler.now()) {
                long delay = this.execTime - this.innerScheduler.now();
                if (delay > 0) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                }
            }
            if (!this.innerScheduler.isUnsubscribed()) {
                this.underlying.call();
            }
        }
    }
}
