package rx.internal.schedulers;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.util.RxThreadFactory;
import rx.internal.util.SubscriptionList;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public class EventLoopsScheduler extends Scheduler implements SchedulerLifecycle {
    static final String KEY_MAX_THREADS = "rx.scheduler.max-computation-threads";
    static final int MAX_THREADS;
    static final FixedSchedulerPool NONE;
    static final PoolWorker SHUTDOWN_WORKER;
    final AtomicReference<FixedSchedulerPool> pool = new AtomicReference<>(NONE);
    private static final String THREAD_NAME_PREFIX = "RxComputationThreadPool-";
    private static final RxThreadFactory THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX);

    static {
        int max;
        int maxThreads = Integer.getInteger(KEY_MAX_THREADS, 0).intValue();
        int ncpu = Runtime.getRuntime().availableProcessors();
        if (maxThreads <= 0 || maxThreads > ncpu) {
            max = ncpu;
        } else {
            max = maxThreads;
        }
        MAX_THREADS = max;
        SHUTDOWN_WORKER = new PoolWorker(new RxThreadFactory("RxComputationShutdown-"));
        SHUTDOWN_WORKER.unsubscribe();
        NONE = new FixedSchedulerPool(0);
    }

    static final class FixedSchedulerPool {
        final int cores;
        final PoolWorker[] eventLoops;
        long n;

        FixedSchedulerPool(int maxThreads) {
            this.cores = maxThreads;
            this.eventLoops = new PoolWorker[maxThreads];
            for (int i = 0; i < maxThreads; i++) {
                this.eventLoops[i] = new PoolWorker(EventLoopsScheduler.THREAD_FACTORY);
            }
        }

        public PoolWorker getEventLoop() {
            int c = this.cores;
            if (c == 0) {
                return EventLoopsScheduler.SHUTDOWN_WORKER;
            }
            PoolWorker[] poolWorkerArr = this.eventLoops;
            long j = this.n;
            this.n = 1 + j;
            return poolWorkerArr[(int) (j % c)];
        }

        public void shutdown() {
            PoolWorker[] arr$ = this.eventLoops;
            for (PoolWorker w : arr$) {
                w.unsubscribe();
            }
        }
    }

    public EventLoopsScheduler() {
        start();
    }

    @Override // rx.Scheduler
    public Scheduler.Worker createWorker() {
        return new EventLoopWorker(this.pool.get().getEventLoop());
    }

    @Override // rx.internal.schedulers.SchedulerLifecycle
    public void start() {
        FixedSchedulerPool update = new FixedSchedulerPool(MAX_THREADS);
        if (!this.pool.compareAndSet(NONE, update)) {
            update.shutdown();
        }
    }

    @Override // rx.internal.schedulers.SchedulerLifecycle
    public void shutdown() {
        FixedSchedulerPool curr;
        do {
            curr = this.pool.get();
            if (curr == NONE) {
                return;
            }
        } while (!this.pool.compareAndSet(curr, NONE));
        curr.shutdown();
    }

    public Subscription scheduleDirect(Action0 action) {
        PoolWorker pw = this.pool.get().getEventLoop();
        return pw.scheduleActual(action, -1L, TimeUnit.NANOSECONDS);
    }

    private static class EventLoopWorker extends Scheduler.Worker {
        private final PoolWorker poolWorker;
        private final SubscriptionList serial = new SubscriptionList();
        private final CompositeSubscription timed = new CompositeSubscription();
        private final SubscriptionList both = new SubscriptionList(this.serial, this.timed);

        EventLoopWorker(PoolWorker poolWorker) {
            this.poolWorker = poolWorker;
        }

        @Override // rx.Subscription
        public void unsubscribe() throws Throwable {
            this.both.unsubscribe();
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return this.both.isUnsubscribed();
        }

        @Override // rx.Scheduler.Worker
        public Subscription schedule(Action0 action) {
            return isUnsubscribed() ? Subscriptions.unsubscribed() : this.poolWorker.scheduleActual(action, 0L, (TimeUnit) null, this.serial);
        }

        @Override // rx.Scheduler.Worker
        public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
            return isUnsubscribed() ? Subscriptions.unsubscribed() : this.poolWorker.scheduleActual(action, delayTime, unit, this.timed);
        }
    }

    private static final class PoolWorker extends NewThreadWorker {
        PoolWorker(ThreadFactory threadFactory) {
            super(threadFactory);
        }
    }
}
