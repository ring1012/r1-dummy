package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;

/* loaded from: classes.dex */
public final class OperatorOnBackpressureLatest<T> implements Observable.Operator<T, T> {

    static final class Holder {
        static final OperatorOnBackpressureLatest<Object> INSTANCE = new OperatorOnBackpressureLatest<>();

        Holder() {
        }
    }

    public static <T> OperatorOnBackpressureLatest<T> instance() {
        return (OperatorOnBackpressureLatest<T>) Holder.INSTANCE;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> child) {
        LatestEmitter<T> producer = new LatestEmitter<>(child);
        LatestSubscriber<? super T> latestSubscriber = new LatestSubscriber<>(producer);
        producer.parent = latestSubscriber;
        child.add(latestSubscriber);
        child.add(producer);
        child.setProducer(producer);
        return latestSubscriber;
    }

    static final class LatestEmitter<T> extends AtomicLong implements Producer, Subscription, Observer<T> {
        static final Object EMPTY = new Object();
        static final long NOT_REQUESTED = -4611686018427387904L;
        private static final long serialVersionUID = -1364393685005146274L;
        final Subscriber<? super T> child;
        volatile boolean done;
        boolean emitting;
        boolean missed;
        LatestSubscriber<? super T> parent;
        Throwable terminal;
        final AtomicReference<Object> value = new AtomicReference<>(EMPTY);

        public LatestEmitter(Subscriber<? super T> child) {
            this.child = child;
            lazySet(NOT_REQUESTED);
        }

        @Override // rx.Producer
        public void request(long n) {
            long r;
            long u;
            if (n >= 0) {
                do {
                    r = get();
                    if (r == Long.MIN_VALUE) {
                        return;
                    }
                    if (r == NOT_REQUESTED) {
                        u = n;
                    } else {
                        u = r + n;
                        if (u < 0) {
                            u = Long.MAX_VALUE;
                        }
                    }
                } while (!compareAndSet(r, u));
                if (r == NOT_REQUESTED) {
                    this.parent.requestMore(Long.MAX_VALUE);
                }
                emit();
            }
        }

        long produced(long n) {
            long r;
            long u;
            do {
                r = get();
                if (r >= 0) {
                    u = r - n;
                } else {
                    return r;
                }
            } while (!compareAndSet(r, u));
            return u;
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return get() == Long.MIN_VALUE;
        }

        @Override // rx.Subscription
        public void unsubscribe() {
            if (get() >= 0) {
                getAndSet(Long.MIN_VALUE);
            }
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.value.lazySet(t);
            emit();
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.terminal = e;
            this.done = true;
            emit();
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.done = true;
            emit();
        }

        /* JADX WARN: Code restructure failed: missing block: B:42:0x0065, code lost:
        
            r8.emitting = false;
            r1 = true;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void emit() {
            /*
                r8 = this;
                monitor-enter(r8)
                boolean r6 = r8.emitting     // Catch: java.lang.Throwable -> L28
                if (r6 == 0) goto La
                r6 = 1
                r8.missed = r6     // Catch: java.lang.Throwable -> L28
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L28
            L9:
                return
            La:
                r6 = 1
                r8.emitting = r6     // Catch: java.lang.Throwable -> L28
                r6 = 0
                r8.missed = r6     // Catch: java.lang.Throwable -> L28
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L28
                r1 = 0
            L12:
                long r2 = r8.get()     // Catch: java.lang.Throwable -> L6e
                r6 = -9223372036854775808
                int r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
                if (r6 != 0) goto L2b
                r1 = 1
            L1d:
                if (r1 != 0) goto L9
                monitor-enter(r8)
                r6 = 0
                r8.emitting = r6     // Catch: java.lang.Throwable -> L25
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L25
                goto L9
            L25:
                r6 = move-exception
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L25
                throw r6
            L28:
                r6 = move-exception
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L28
                throw r6
            L2b:
                java.util.concurrent.atomic.AtomicReference<java.lang.Object> r6 = r8.value     // Catch: java.lang.Throwable -> L6e
                java.lang.Object r4 = r6.get()     // Catch: java.lang.Throwable -> L6e
                r6 = 0
                int r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
                if (r6 <= 0) goto L4f
                java.lang.Object r6 = rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.EMPTY     // Catch: java.lang.Throwable -> L6e
                if (r4 == r6) goto L4f
                r5 = r4
                rx.Subscriber<? super T> r6 = r8.child     // Catch: java.lang.Throwable -> L6e
                r6.onNext(r5)     // Catch: java.lang.Throwable -> L6e
                java.util.concurrent.atomic.AtomicReference<java.lang.Object> r6 = r8.value     // Catch: java.lang.Throwable -> L6e
                java.lang.Object r7 = rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.EMPTY     // Catch: java.lang.Throwable -> L6e
                r6.compareAndSet(r4, r7)     // Catch: java.lang.Throwable -> L6e
                r6 = 1
                r8.produced(r6)     // Catch: java.lang.Throwable -> L6e
                java.lang.Object r4 = rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.EMPTY     // Catch: java.lang.Throwable -> L6e
            L4f:
                java.lang.Object r6 = rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.EMPTY     // Catch: java.lang.Throwable -> L6e
                if (r4 != r6) goto L60
                boolean r6 = r8.done     // Catch: java.lang.Throwable -> L6e
                if (r6 == 0) goto L60
                java.lang.Throwable r0 = r8.terminal     // Catch: java.lang.Throwable -> L6e
                if (r0 == 0) goto L77
                rx.Subscriber<? super T> r6 = r8.child     // Catch: java.lang.Throwable -> L6e
                r6.onError(r0)     // Catch: java.lang.Throwable -> L6e
            L60:
                monitor-enter(r8)     // Catch: java.lang.Throwable -> L6e
                boolean r6 = r8.missed     // Catch: java.lang.Throwable -> L6b
                if (r6 != 0) goto L7d
                r6 = 0
                r8.emitting = r6     // Catch: java.lang.Throwable -> L6b
                r1 = 1
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L6b
                goto L1d
            L6b:
                r6 = move-exception
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L6b
                throw r6     // Catch: java.lang.Throwable -> L6e
            L6e:
                r6 = move-exception
                if (r1 != 0) goto L76
                monitor-enter(r8)
                r7 = 0
                r8.emitting = r7     // Catch: java.lang.Throwable -> L82
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L82
            L76:
                throw r6
            L77:
                rx.Subscriber<? super T> r6 = r8.child     // Catch: java.lang.Throwable -> L6e
                r6.onCompleted()     // Catch: java.lang.Throwable -> L6e
                goto L60
            L7d:
                r6 = 0
                r8.missed = r6     // Catch: java.lang.Throwable -> L6b
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L6b
                goto L12
            L82:
                r6 = move-exception
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L82
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorOnBackpressureLatest.LatestEmitter.emit():void");
        }
    }

    static final class LatestSubscriber<T> extends Subscriber<T> {
        private final LatestEmitter<T> producer;

        private LatestSubscriber(LatestEmitter<T> producer) {
            this.producer = producer;
        }

        @Override // rx.Subscriber
        public void onStart() {
            request(0L);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.producer.onNext(t);
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.producer.onError(e);
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.producer.onCompleted();
        }

        void requestMore(long n) {
            request(n);
        }
    }
}
