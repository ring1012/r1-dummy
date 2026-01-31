package rx.internal.operators;

import a.a.a.a;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;
import rx.functions.Func2;
import rx.internal.util.atomic.SpscLinkedAtomicQueue;
import rx.internal.util.unsafe.SpscLinkedQueue;
import rx.internal.util.unsafe.UnsafeAccess;

/* loaded from: classes.dex */
public final class OperatorScan<R, T> implements Observable.Operator<R, T> {
    private static final Object NO_INITIAL_VALUE = new Object();
    private final Func2<R, ? super T, R> accumulator;
    private final Func0<R> initialValueFactory;

    public OperatorScan(final R initialValue, Func2<R, ? super T, R> accumulator) {
        this((Func0) new Func0<R>() { // from class: rx.internal.operators.OperatorScan.1
            @Override // rx.functions.Func0, java.util.concurrent.Callable
            public R call() {
                return (R) initialValue;
            }
        }, (Func2) accumulator);
    }

    public OperatorScan(Func0<R> initialValueFactory, Func2<R, ? super T, R> accumulator) {
        this.initialValueFactory = initialValueFactory;
        this.accumulator = accumulator;
    }

    public OperatorScan(Func2<R, ? super T, R> accumulator) {
        this(NO_INITIAL_VALUE, accumulator);
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super R> subscriber) {
        final R rCall = this.initialValueFactory.call();
        if (rCall == NO_INITIAL_VALUE) {
            return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorScan.2
                boolean once;
                R value;

                @Override // rx.Observer
                public void onNext(T t) {
                    T t2;
                    if (!this.once) {
                        this.once = true;
                        t2 = (R) t;
                    } else {
                        try {
                            t2 = (R) OperatorScan.this.accumulator.call(this.value, t);
                        } catch (Throwable th) {
                            Exceptions.throwIfFatal(th);
                            subscriber.onError(OnErrorThrowable.addValueAsLastCause(th, t));
                            return;
                        }
                    }
                    this.value = (R) t2;
                    subscriber.onNext(t2);
                }

                @Override // rx.Observer
                public void onError(Throwable e) {
                    subscriber.onError(e);
                }

                @Override // rx.Observer
                public void onCompleted() {
                    subscriber.onCompleted();
                }
            };
        }
        final InitialProducer initialProducer = new InitialProducer(rCall, subscriber);
        Subscriber<T> subscriber2 = new Subscriber<T>() { // from class: rx.internal.operators.OperatorScan.3
            private R value;

            {
                this.value = (R) rCall;
            }

            @Override // rx.Observer
            public void onNext(T t) {
                try {
                    R r = (R) OperatorScan.this.accumulator.call(this.value, t);
                    this.value = r;
                    initialProducer.onNext(r);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    onError(OnErrorThrowable.addValueAsLastCause(th, t));
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                initialProducer.onError(e);
            }

            @Override // rx.Observer
            public void onCompleted() {
                initialProducer.onCompleted();
            }

            @Override // rx.Subscriber
            public void setProducer(Producer producer) {
                initialProducer.setProducer(producer);
            }
        };
        subscriber.add(subscriber2);
        subscriber.setProducer(initialProducer);
        return subscriber2;
    }

    static final class InitialProducer<R> implements Producer, Observer<R> {
        final Subscriber<? super R> child;
        volatile boolean done;
        boolean emitting;
        Throwable error;
        boolean missed;
        long missedRequested;
        volatile Producer producer;
        final Queue<Object> queue;
        final AtomicLong requested;

        public InitialProducer(R initialValue, Subscriber<? super R> child) {
            Queue<Object> q;
            this.child = child;
            if (UnsafeAccess.isUnsafeAvailable()) {
                q = new SpscLinkedQueue<>();
            } else {
                q = new SpscLinkedAtomicQueue<>();
            }
            this.queue = q;
            q.offer(NotificationLite.instance().next(initialValue));
            this.requested = new AtomicLong();
        }

        @Override // rx.Observer
        public void onNext(R t) {
            this.queue.offer(NotificationLite.instance().next(t));
            emit();
        }

        boolean checkTerminated(boolean d, boolean empty, Subscriber<? super R> child) {
            if (child.isUnsubscribed()) {
                return true;
            }
            if (d) {
                Throwable err = this.error;
                if (err != null) {
                    child.onError(err);
                    return true;
                }
                if (empty) {
                    child.onCompleted();
                    return true;
                }
            }
            return false;
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            emit();
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.done = true;
            emit();
        }

        @Override // rx.Producer
        public void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n >= required but it was " + n);
            }
            if (n != 0) {
                BackpressureUtils.getAndAddRequest(this.requested, n);
                Producer p = this.producer;
                if (p == null) {
                    synchronized (this.requested) {
                        p = this.producer;
                        if (p == null) {
                            long mr = this.missedRequested;
                            this.missedRequested = BackpressureUtils.addCap(mr, n);
                        }
                    }
                }
                if (p != null) {
                    p.request(n);
                }
                emit();
            }
        }

        public void setProducer(Producer p) {
            long mr;
            if (p == null) {
                throw new NullPointerException();
            }
            synchronized (this.requested) {
                if (this.producer != null) {
                    throw new IllegalStateException("Can't set more than one Producer!");
                }
                mr = this.missedRequested - 1;
                this.missedRequested = 0L;
                this.producer = p;
            }
            if (mr > 0) {
                p.request(mr);
            }
            emit();
        }

        void emit() {
            synchronized (this) {
                if (this.emitting) {
                    this.missed = true;
                } else {
                    this.emitting = true;
                    emitLoop();
                }
            }
        }

        void emitLoop() {
            Subscriber<? super R> subscriber = this.child;
            Queue<Object> queue = this.queue;
            NotificationLite notificationLiteInstance = NotificationLite.instance();
            AtomicLong atomicLong = this.requested;
            long jAddAndGet = atomicLong.get();
            while (true) {
                boolean z = jAddAndGet == Long.MAX_VALUE;
                if (!checkTerminated(this.done, queue.isEmpty(), subscriber)) {
                    long j = 0;
                    while (jAddAndGet != 0) {
                        boolean z2 = this.done;
                        Object objPoll = queue.poll();
                        boolean z3 = objPoll == null;
                        if (!checkTerminated(z2, z3, subscriber)) {
                            if (z3) {
                                break;
                            }
                            a aVar = (R) notificationLiteInstance.getValue(objPoll);
                            try {
                                subscriber.onNext(aVar);
                                jAddAndGet--;
                                j--;
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                subscriber.onError(OnErrorThrowable.addValueAsLastCause(th, aVar));
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                    if (j != 0 && !z) {
                        jAddAndGet = atomicLong.addAndGet(j);
                    }
                    synchronized (this) {
                        if (!this.missed) {
                            this.emitting = false;
                            return;
                        }
                        this.missed = false;
                    }
                } else {
                    return;
                }
            }
        }
    }
}
