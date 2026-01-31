package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.MissingBackpressureException;
import rx.functions.Action0;
import rx.internal.util.RxRingBuffer;
import rx.internal.util.SynchronizedQueue;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.schedulers.ImmediateScheduler;
import rx.schedulers.TrampolineScheduler;

/* loaded from: classes.dex */
public final class OperatorObserveOn<T> implements Observable.Operator<T, T> {
    private final Scheduler scheduler;

    public OperatorObserveOn(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> child) {
        if (!(this.scheduler instanceof ImmediateScheduler) && !(this.scheduler instanceof TrampolineScheduler)) {
            ObserveOnSubscriber<T> parent = new ObserveOnSubscriber<>(this.scheduler, child);
            parent.init();
            return parent;
        }
        return child;
    }

    private static final class ObserveOnSubscriber<T> extends Subscriber<T> {
        final Subscriber<? super T> child;
        volatile Throwable error;
        final Queue<Object> queue;
        final Scheduler.Worker recursiveScheduler;
        final ScheduledUnsubscribe scheduledUnsubscribe;
        final NotificationLite<T> on = NotificationLite.instance();
        volatile boolean finished = false;
        final AtomicLong requested = new AtomicLong();
        final AtomicLong counter = new AtomicLong();
        final Action0 action = new Action0() { // from class: rx.internal.operators.OperatorObserveOn.ObserveOnSubscriber.2
            @Override // rx.functions.Action0
            public void call() {
                ObserveOnSubscriber.this.pollQueue();
            }
        };

        public ObserveOnSubscriber(Scheduler scheduler, Subscriber<? super T> child) {
            this.child = child;
            this.recursiveScheduler = scheduler.createWorker();
            if (UnsafeAccess.isUnsafeAvailable()) {
                this.queue = new SpscArrayQueue(RxRingBuffer.SIZE);
            } else {
                this.queue = new SynchronizedQueue(RxRingBuffer.SIZE);
            }
            this.scheduledUnsubscribe = new ScheduledUnsubscribe(this.recursiveScheduler);
        }

        void init() {
            this.child.add(this.scheduledUnsubscribe);
            this.child.setProducer(new Producer() { // from class: rx.internal.operators.OperatorObserveOn.ObserveOnSubscriber.1
                @Override // rx.Producer
                public void request(long n) {
                    BackpressureUtils.getAndAddRequest(ObserveOnSubscriber.this.requested, n);
                    ObserveOnSubscriber.this.schedule();
                }
            });
            this.child.add(this.recursiveScheduler);
            this.child.add(this);
        }

        @Override // rx.Subscriber
        public void onStart() {
            request(RxRingBuffer.SIZE);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            if (!isUnsubscribed()) {
                if (!this.queue.offer(this.on.next(t))) {
                    onError(new MissingBackpressureException());
                } else {
                    schedule();
                }
            }
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (!isUnsubscribed() && !this.finished) {
                this.finished = true;
                schedule();
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            if (!isUnsubscribed() && !this.finished) {
                this.error = e;
                unsubscribe();
                this.finished = true;
                schedule();
            }
        }

        protected void schedule() {
            if (this.counter.getAndIncrement() == 0) {
                this.recursiveScheduler.schedule(this.action);
            }
        }

        void pollQueue() {
            Object objPoll;
            int i = 0;
            AtomicLong atomicLong = this.requested;
            AtomicLong atomicLong2 = this.counter;
            do {
                atomicLong2.set(1L);
                long j = 0;
                long j2 = atomicLong.get();
                while (!this.child.isUnsubscribed()) {
                    if (this.finished) {
                        Throwable th = this.error;
                        if (th != null) {
                            this.queue.clear();
                            this.child.onError(th);
                            return;
                        } else if (this.queue.isEmpty()) {
                            this.child.onCompleted();
                            return;
                        }
                    }
                    if (j2 > 0 && (objPoll = this.queue.poll()) != null) {
                        this.child.onNext(this.on.getValue(objPoll));
                        j2--;
                        i++;
                        j++;
                    } else if (j > 0 && atomicLong.get() != Long.MAX_VALUE) {
                        atomicLong.addAndGet(-j);
                    }
                }
                return;
            } while (atomicLong2.decrementAndGet() > 0);
            if (i > 0) {
                request(i);
            }
        }
    }

    static final class ScheduledUnsubscribe extends AtomicInteger implements Subscription {
        volatile boolean unsubscribed = false;
        final Scheduler.Worker worker;

        public ScheduledUnsubscribe(Scheduler.Worker worker) {
            this.worker = worker;
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return this.unsubscribed;
        }

        @Override // rx.Subscription
        public void unsubscribe() {
            if (getAndSet(1) == 0) {
                this.worker.schedule(new Action0() { // from class: rx.internal.operators.OperatorObserveOn.ScheduledUnsubscribe.1
                    @Override // rx.functions.Action0
                    public void call() {
                        ScheduledUnsubscribe.this.worker.unsubscribe();
                        ScheduledUnsubscribe.this.unsubscribed = true;
                    }
                });
            }
        }
    }
}
