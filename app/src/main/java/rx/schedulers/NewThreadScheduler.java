package rx.schedulers;

import rx.Scheduler;
import rx.internal.schedulers.NewThreadWorker;
import rx.internal.util.RxThreadFactory;

/* loaded from: classes.dex */
public final class NewThreadScheduler extends Scheduler {
    private static final String THREAD_NAME_PREFIX = "RxNewThreadScheduler-";
    private static final RxThreadFactory THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX);
    private static final NewThreadScheduler INSTANCE = new NewThreadScheduler();

    static NewThreadScheduler instance() {
        return INSTANCE;
    }

    private NewThreadScheduler() {
    }

    @Override // rx.Scheduler
    public Scheduler.Worker createWorker() {
        return new NewThreadWorker(THREAD_FACTORY);
    }
}
