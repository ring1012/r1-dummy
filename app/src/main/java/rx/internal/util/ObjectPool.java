package rx.internal.util;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import rx.Scheduler;
import rx.functions.Action0;
import rx.internal.schedulers.SchedulerLifecycle;
import rx.internal.util.unsafe.MpmcArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.schedulers.Schedulers;

/* loaded from: classes.dex */
public abstract class ObjectPool<T> implements SchedulerLifecycle {
    private final int maxSize;
    private final int minSize;
    private Queue<T> pool;
    private final AtomicReference<Scheduler.Worker> schedulerWorker;
    private final long validationInterval;

    protected abstract T createObject();

    public ObjectPool() {
        this(0, 0, 67L);
    }

    private ObjectPool(int min, int max, long validationInterval) {
        this.minSize = min;
        this.maxSize = max;
        this.validationInterval = validationInterval;
        this.schedulerWorker = new AtomicReference<>();
        initialize(min);
        start();
    }

    public T borrowObject() {
        T object = this.pool.poll();
        if (object == null) {
            return createObject();
        }
        return object;
    }

    public void returnObject(T object) {
        if (object != null) {
            this.pool.offer(object);
        }
    }

    @Override // rx.internal.schedulers.SchedulerLifecycle
    public void shutdown() {
        Scheduler.Worker w = this.schedulerWorker.getAndSet(null);
        if (w != null) {
            w.unsubscribe();
        }
    }

    @Override // rx.internal.schedulers.SchedulerLifecycle
    public void start() {
        Scheduler.Worker w = Schedulers.computation().createWorker();
        if (this.schedulerWorker.compareAndSet(null, w)) {
            w.schedulePeriodically(new Action0() { // from class: rx.internal.util.ObjectPool.1
                @Override // rx.functions.Action0
                public void call() {
                    int size = ObjectPool.this.pool.size();
                    if (size < ObjectPool.this.minSize) {
                        int sizeToBeAdded = ObjectPool.this.maxSize - size;
                        for (int i = 0; i < sizeToBeAdded; i++) {
                            ObjectPool.this.pool.add(ObjectPool.this.createObject());
                        }
                        return;
                    }
                    if (size > ObjectPool.this.maxSize) {
                        int sizeToBeRemoved = size - ObjectPool.this.maxSize;
                        for (int i2 = 0; i2 < sizeToBeRemoved; i2++) {
                            ObjectPool.this.pool.poll();
                        }
                    }
                }
            }, this.validationInterval, this.validationInterval, TimeUnit.SECONDS);
        } else {
            w.unsubscribe();
        }
    }

    private void initialize(int min) {
        if (UnsafeAccess.isUnsafeAvailable()) {
            this.pool = new MpmcArrayQueue(Math.max(this.maxSize, 1024));
        } else {
            this.pool = new ConcurrentLinkedQueue();
        }
        for (int i = 0; i < min; i++) {
            this.pool.add(createObject());
        }
    }
}
