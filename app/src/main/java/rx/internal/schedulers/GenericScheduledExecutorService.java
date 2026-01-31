package rx.internal.schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;
import rx.internal.util.RxThreadFactory;

/* loaded from: classes.dex */
public final class GenericScheduledExecutorService implements SchedulerLifecycle {
    private final AtomicReference<ScheduledExecutorService> executor = new AtomicReference<>(NONE);
    private static final String THREAD_NAME_PREFIX = "RxScheduledExecutorPool-";
    private static final RxThreadFactory THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX);
    public static final GenericScheduledExecutorService INSTANCE = new GenericScheduledExecutorService();
    static final ScheduledExecutorService NONE = Executors.newScheduledThreadPool(0);

    static {
        NONE.shutdownNow();
    }

    private GenericScheduledExecutorService() {
        start();
    }

    @Override // rx.internal.schedulers.SchedulerLifecycle
    public void start() {
        int count = Runtime.getRuntime().availableProcessors();
        if (count > 4) {
            count /= 2;
        }
        if (count > 8) {
            count = 8;
        }
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(count, THREAD_FACTORY);
        if (this.executor.compareAndSet(NONE, exec)) {
            if (!NewThreadWorker.tryEnableCancelPolicy(exec) && (exec instanceof ScheduledThreadPoolExecutor)) {
                NewThreadWorker.registerExecutor((ScheduledThreadPoolExecutor) exec);
                return;
            }
            return;
        }
        exec.shutdownNow();
    }

    @Override // rx.internal.schedulers.SchedulerLifecycle
    public void shutdown() {
        ScheduledExecutorService exec;
        do {
            exec = this.executor.get();
            if (exec == NONE) {
                return;
            }
        } while (!this.executor.compareAndSet(exec, NONE));
        NewThreadWorker.deregisterExecutor(exec);
        exec.shutdownNow();
    }

    public static ScheduledExecutorService getInstance() {
        return INSTANCE.executor.get();
    }
}
