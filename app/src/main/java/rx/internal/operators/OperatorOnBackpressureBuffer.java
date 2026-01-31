package rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.MissingBackpressureException;
import rx.functions.Action0;
import rx.internal.util.BackpressureDrainManager;

/* loaded from: classes.dex */
public class OperatorOnBackpressureBuffer<T> implements Observable.Operator<T, T> {
    private final Long capacity;
    private final Action0 onOverflow;

    private static class Holder {
        static final OperatorOnBackpressureBuffer<?> INSTANCE = new OperatorOnBackpressureBuffer<>();

        private Holder() {
        }
    }

    public static <T> OperatorOnBackpressureBuffer<T> instance() {
        return (OperatorOnBackpressureBuffer<T>) Holder.INSTANCE;
    }

    private OperatorOnBackpressureBuffer() {
        this.capacity = null;
        this.onOverflow = null;
    }

    public OperatorOnBackpressureBuffer(long capacity) {
        this(capacity, null);
    }

    public OperatorOnBackpressureBuffer(long capacity, Action0 onOverflow) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Buffer capacity must be > 0");
        }
        this.capacity = Long.valueOf(capacity);
        this.onOverflow = onOverflow;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> child) {
        BufferSubscriber<T> parent = new BufferSubscriber<>(child, this.capacity, this.onOverflow);
        child.add(parent);
        child.setProducer(parent.manager());
        return parent;
    }

    private static final class BufferSubscriber<T> extends Subscriber<T> implements BackpressureDrainManager.BackpressureQueueCallback {
        private final Long baseCapacity;
        private final AtomicLong capacity;
        private final Subscriber<? super T> child;
        private final BackpressureDrainManager manager;
        private final Action0 onOverflow;
        private final ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();
        private final AtomicBoolean saturated = new AtomicBoolean(false);
        private final NotificationLite<T> on = NotificationLite.instance();

        public BufferSubscriber(Subscriber<? super T> child, Long capacity, Action0 onOverflow) {
            this.child = child;
            this.baseCapacity = capacity;
            this.capacity = capacity != null ? new AtomicLong(capacity.longValue()) : null;
            this.onOverflow = onOverflow;
            this.manager = new BackpressureDrainManager(this);
        }

        @Override // rx.Subscriber
        public void onStart() {
            request(Long.MAX_VALUE);
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (!this.saturated.get()) {
                this.manager.terminateAndDrain();
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            if (!this.saturated.get()) {
                this.manager.terminateAndDrain(e);
            }
        }

        @Override // rx.Observer
        public void onNext(T t) {
            if (assertCapacity()) {
                this.queue.offer(this.on.next(t));
                this.manager.drain();
            }
        }

        @Override // rx.internal.util.BackpressureDrainManager.BackpressureQueueCallback
        public boolean accept(Object value) {
            return this.on.accept(this.child, value);
        }

        @Override // rx.internal.util.BackpressureDrainManager.BackpressureQueueCallback
        public void complete(Throwable exception) {
            if (exception != null) {
                this.child.onError(exception);
            } else {
                this.child.onCompleted();
            }
        }

        @Override // rx.internal.util.BackpressureDrainManager.BackpressureQueueCallback
        public Object peek() {
            return this.queue.peek();
        }

        @Override // rx.internal.util.BackpressureDrainManager.BackpressureQueueCallback
        public Object poll() {
            Object value = this.queue.poll();
            if (this.capacity != null && value != null) {
                this.capacity.incrementAndGet();
            }
            return value;
        }

        private boolean assertCapacity() {
            long currCapacity;
            if (this.capacity == null) {
                return true;
            }
            do {
                currCapacity = this.capacity.get();
                if (currCapacity <= 0) {
                    if (this.saturated.compareAndSet(false, true)) {
                        unsubscribe();
                        this.child.onError(new MissingBackpressureException("Overflowed buffer of " + this.baseCapacity));
                        if (this.onOverflow != null) {
                            this.onOverflow.call();
                        }
                    }
                    return false;
                }
            } while (!this.capacity.compareAndSet(currCapacity, currCapacity - 1));
            return true;
        }

        protected Producer manager() {
            return this.manager;
        }
    }
}
