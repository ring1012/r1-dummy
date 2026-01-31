package rx.schedulers;

import java.util.concurrent.Executor;
import rx.Scheduler;
import rx.internal.schedulers.EventLoopsScheduler;
import rx.internal.schedulers.GenericScheduledExecutorService;
import rx.internal.schedulers.SchedulerLifecycle;
import rx.internal.util.RxRingBuffer;
import rx.plugins.RxJavaPlugins;

/* loaded from: classes.dex */
public final class Schedulers {
    private static final Schedulers INSTANCE = new Schedulers();
    private final Scheduler computationScheduler;
    private final Scheduler ioScheduler;
    private final Scheduler newThreadScheduler;

    private Schedulers() {
        Scheduler c = RxJavaPlugins.getInstance().getSchedulersHook().getComputationScheduler();
        if (c != null) {
            this.computationScheduler = c;
        } else {
            this.computationScheduler = new EventLoopsScheduler();
        }
        Scheduler io = RxJavaPlugins.getInstance().getSchedulersHook().getIOScheduler();
        if (io != null) {
            this.ioScheduler = io;
        } else {
            this.ioScheduler = new CachedThreadScheduler();
        }
        Scheduler nt = RxJavaPlugins.getInstance().getSchedulersHook().getNewThreadScheduler();
        if (nt != null) {
            this.newThreadScheduler = nt;
        } else {
            this.newThreadScheduler = NewThreadScheduler.instance();
        }
    }

    public static Scheduler immediate() {
        return ImmediateScheduler.instance();
    }

    public static Scheduler trampoline() {
        return TrampolineScheduler.instance();
    }

    public static Scheduler newThread() {
        return INSTANCE.newThreadScheduler;
    }

    public static Scheduler computation() {
        return INSTANCE.computationScheduler;
    }

    public static Scheduler io() {
        return INSTANCE.ioScheduler;
    }

    public static TestScheduler test() {
        return new TestScheduler();
    }

    public static Scheduler from(Executor executor) {
        return new ExecutorScheduler(executor);
    }

    static void start() {
        Schedulers s = INSTANCE;
        synchronized (s) {
            if (s.computationScheduler instanceof SchedulerLifecycle) {
                ((SchedulerLifecycle) s.computationScheduler).start();
            }
            if (s.ioScheduler instanceof SchedulerLifecycle) {
                ((SchedulerLifecycle) s.ioScheduler).start();
            }
            if (s.newThreadScheduler instanceof SchedulerLifecycle) {
                ((SchedulerLifecycle) s.newThreadScheduler).start();
            }
            GenericScheduledExecutorService.INSTANCE.start();
            RxRingBuffer.SPSC_POOL.start();
            RxRingBuffer.SPMC_POOL.start();
        }
    }

    public static void shutdown() {
        Schedulers s = INSTANCE;
        synchronized (s) {
            if (s.computationScheduler instanceof SchedulerLifecycle) {
                ((SchedulerLifecycle) s.computationScheduler).shutdown();
            }
            if (s.ioScheduler instanceof SchedulerLifecycle) {
                ((SchedulerLifecycle) s.ioScheduler).shutdown();
            }
            if (s.newThreadScheduler instanceof SchedulerLifecycle) {
                ((SchedulerLifecycle) s.newThreadScheduler).shutdown();
            }
            GenericScheduledExecutorService.INSTANCE.shutdown();
            RxRingBuffer.SPSC_POOL.shutdown();
            RxRingBuffer.SPMC_POOL.shutdown();
        }
    }
}
