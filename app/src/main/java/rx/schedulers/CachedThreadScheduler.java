package rx.schedulers;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.schedulers.NewThreadWorker;
import rx.internal.schedulers.ScheduledAction;
import rx.internal.schedulers.SchedulerLifecycle;
import rx.internal.util.RxThreadFactory;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
final class CachedThreadScheduler extends Scheduler implements SchedulerLifecycle {
    private static final long KEEP_ALIVE_TIME = 60;
    static final CachedWorkerPool NONE;
    final AtomicReference<CachedWorkerPool> pool = new AtomicReference<>(NONE);
    private static final String WORKER_THREAD_NAME_PREFIX = "RxCachedThreadScheduler-";
    private static final RxThreadFactory WORKER_THREAD_FACTORY = new RxThreadFactory(WORKER_THREAD_NAME_PREFIX);
    private static final String EVICTOR_THREAD_NAME_PREFIX = "RxCachedWorkerPoolEvictor-";
    private static final RxThreadFactory EVICTOR_THREAD_FACTORY = new RxThreadFactory(EVICTOR_THREAD_NAME_PREFIX);
    private static final TimeUnit KEEP_ALIVE_UNIT = TimeUnit.SECONDS;
    static final ThreadWorker SHUTDOWN_THREADWORKER = new ThreadWorker(new RxThreadFactory("RxCachedThreadSchedulerShutdown-"));

    static {
        SHUTDOWN_THREADWORKER.unsubscribe();
        NONE = new CachedWorkerPool(0L, null);
        NONE.shutdown();
    }

    private static final class CachedWorkerPool {
        private final CompositeSubscription allWorkers;
        private final ScheduledExecutorService evictorService;
        private final Future<?> evictorTask;
        private final ConcurrentLinkedQueue<ThreadWorker> expiringWorkerQueue;
        private final long keepAliveTime;

        CachedWorkerPool(long keepAliveTime, TimeUnit unit) throws IllegalAccessException, SecurityException, IllegalArgumentException, InvocationTargetException {
            this.keepAliveTime = unit != null ? unit.toNanos(keepAliveTime) : 0L;
            this.expiringWorkerQueue = new ConcurrentLinkedQueue<>();
            this.allWorkers = new CompositeSubscription();
            ScheduledExecutorService evictor = null;
            Future<?> task = null;
            if (unit != null) {
                evictor = Executors.newScheduledThreadPool(1, CachedThreadScheduler.EVICTOR_THREAD_FACTORY);
                NewThreadWorker.tryEnableCancelPolicy(evictor);
                task = evictor.scheduleWithFixedDelay(new Runnable() { // from class: rx.schedulers.CachedThreadScheduler.CachedWorkerPool.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CachedWorkerPool.this.evictExpiredWorkers();
                    }
                }, this.keepAliveTime, this.keepAliveTime, TimeUnit.NANOSECONDS);
            }
            this.evictorService = evictor;
            this.evictorTask = task;
        }

        ThreadWorker get() {
            if (this.allWorkers.isUnsubscribed()) {
                return CachedThreadScheduler.SHUTDOWN_THREADWORKER;
            }
            while (!this.expiringWorkerQueue.isEmpty()) {
                ThreadWorker threadWorker = this.expiringWorkerQueue.poll();
                if (threadWorker != null) {
                    return threadWorker;
                }
            }
            ThreadWorker w = new ThreadWorker(CachedThreadScheduler.WORKER_THREAD_FACTORY);
            this.allWorkers.add(w);
            return w;
        }

        void release(ThreadWorker threadWorker) {
            threadWorker.setExpirationTime(now() + this.keepAliveTime);
            this.expiringWorkerQueue.offer(threadWorker);
        }

        void evictExpiredWorkers() {
            if (!this.expiringWorkerQueue.isEmpty()) {
                long currentTimestamp = now();
                Iterator i$ = this.expiringWorkerQueue.iterator();
                while (i$.hasNext()) {
                    ThreadWorker threadWorker = i$.next();
                    if (threadWorker.getExpirationTime() <= currentTimestamp) {
                        if (this.expiringWorkerQueue.remove(threadWorker)) {
                            this.allWorkers.remove(threadWorker);
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        long now() {
            return System.nanoTime();
        }

        void shutdown() throws Throwable {
            try {
                if (this.evictorTask != null) {
                    this.evictorTask.cancel(true);
                }
                if (this.evictorService != null) {
                    this.evictorService.shutdownNow();
                }
            } finally {
                this.allWorkers.unsubscribe();
            }
        }
    }

    public CachedThreadScheduler() throws Throwable {
        start();
    }

    @Override // rx.internal.schedulers.SchedulerLifecycle
    public void start() throws Throwable {
        CachedWorkerPool update = new CachedWorkerPool(KEEP_ALIVE_TIME, KEEP_ALIVE_UNIT);
        if (!this.pool.compareAndSet(NONE, update)) {
            update.shutdown();
        }
    }

    @Override // rx.internal.schedulers.SchedulerLifecycle
    public void shutdown() throws Throwable {
        CachedWorkerPool curr;
        do {
            curr = this.pool.get();
            if (curr == NONE) {
                return;
            }
        } while (!this.pool.compareAndSet(curr, NONE));
        curr.shutdown();
    }

    @Override // rx.Scheduler
    public Scheduler.Worker createWorker() {
        return new EventLoopWorker(this.pool.get());
    }

    private static final class EventLoopWorker extends Scheduler.Worker {
        static final AtomicIntegerFieldUpdater<EventLoopWorker> ONCE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(EventLoopWorker.class, "once");
        private final CompositeSubscription innerSubscription = new CompositeSubscription();
        volatile int once;
        private final CachedWorkerPool pool;
        private final ThreadWorker threadWorker;

        EventLoopWorker(CachedWorkerPool pool) {
            this.pool = pool;
            this.threadWorker = pool.get();
        }

        @Override // rx.Subscription
        public void unsubscribe() throws Throwable {
            if (ONCE_UPDATER.compareAndSet(this, 0, 1)) {
                this.pool.release(this.threadWorker);
            }
            this.innerSubscription.unsubscribe();
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return this.innerSubscription.isUnsubscribed();
        }

        @Override // rx.Scheduler.Worker
        public Subscription schedule(Action0 action) {
            return schedule(action, 0L, null);
        }

        @Override // rx.Scheduler.Worker
        public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
            if (this.innerSubscription.isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            ScheduledAction s = this.threadWorker.scheduleActual(action, delayTime, unit);
            this.innerSubscription.add(s);
            s.addParent(this.innerSubscription);
            return s;
        }
    }

    private static final class ThreadWorker extends NewThreadWorker {
        private long expirationTime;

        ThreadWorker(ThreadFactory threadFactory) {
            super(threadFactory);
            this.expirationTime = 0L;
        }

        public long getExpirationTime() {
            return this.expirationTime;
        }

        public void setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
        }
    }
}
