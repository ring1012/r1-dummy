package rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.exceptions.MissingBackpressureException;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.internal.producers.ProducerArbiter;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;

/* loaded from: classes.dex */
public final class OperatorMapNotification<T, R> implements Observable.Operator<R, T> {
    private final Func0<? extends R> onCompleted;
    private final Func1<? super Throwable, ? extends R> onError;
    private final Func1<? super T, ? extends R> onNext;

    public OperatorMapNotification(Func1<? super T, ? extends R> onNext, Func1<? super Throwable, ? extends R> onError, Func0<? extends R> onCompleted) {
        this.onNext = onNext;
        this.onError = onError;
        this.onCompleted = onCompleted;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super R> o) {
        ProducerArbiter pa = new ProducerArbiter();
        OperatorMapNotification<T, R>.MapNotificationSubscriber subscriber = new MapNotificationSubscriber(pa, o);
        o.add(subscriber);
        subscriber.init();
        return subscriber;
    }

    final class MapNotificationSubscriber extends Subscriber<T> {
        final SingleEmitter<R> emitter;
        private final Subscriber<? super R> o;
        private final ProducerArbiter pa;

        private MapNotificationSubscriber(ProducerArbiter pa, Subscriber<? super R> o) {
            this.pa = pa;
            this.o = o;
            this.emitter = new SingleEmitter<>(o, pa, this);
        }

        void init() {
            this.o.setProducer(this.emitter);
        }

        @Override // rx.Subscriber
        public void setProducer(Producer producer) {
            this.pa.setProducer(producer);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // rx.Observer
        public void onCompleted() {
            try {
                this.emitter.offerAndComplete(OperatorMapNotification.this.onCompleted.call());
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, this.o);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // rx.Observer
        public void onError(Throwable th) {
            try {
                this.emitter.offerAndComplete(OperatorMapNotification.this.onError.call(th));
            } catch (Throwable th2) {
                Exceptions.throwOrReport(th2, this.o);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // rx.Observer
        public void onNext(T t) {
            try {
                this.emitter.offer(OperatorMapNotification.this.onNext.call(t));
            } catch (Throwable th) {
                Exceptions.throwOrReport(th, this.o, t);
            }
        }
    }

    static final class SingleEmitter<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = -249869671366010660L;
        final Subscription cancel;
        final Subscriber<? super T> child;
        volatile boolean complete;
        boolean emitting;
        boolean missed;
        final NotificationLite<T> nl;
        final Producer producer;
        final Queue<Object> queue;

        public SingleEmitter(Subscriber<? super T> child, Producer producer, Subscription cancel) {
            this.child = child;
            this.producer = producer;
            this.cancel = cancel;
            this.queue = UnsafeAccess.isUnsafeAvailable() ? new SpscArrayQueue<>(2) : new ConcurrentLinkedQueue<>();
            this.nl = NotificationLite.instance();
        }

        @Override // rx.Producer
        public void request(long n) {
            long r;
            long u;
            do {
                r = get();
                if (r >= 0) {
                    u = r + n;
                    if (u < 0) {
                        u = Long.MAX_VALUE;
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(r, u));
            this.producer.request(n);
            drain();
        }

        void produced(long n) {
            long r;
            long u;
            do {
                r = get();
                if (r >= 0) {
                    u = r - n;
                    if (u < 0) {
                        throw new IllegalStateException("More produced (" + n + ") than requested (" + r + ")");
                    }
                } else {
                    return;
                }
            } while (!compareAndSet(r, u));
        }

        public void offer(T value) {
            if (!this.queue.offer(value)) {
                this.child.onError(new MissingBackpressureException());
                unsubscribe();
            } else {
                drain();
            }
        }

        public void offerAndComplete(T value) {
            if (!this.queue.offer(value)) {
                this.child.onError(new MissingBackpressureException());
                unsubscribe();
            } else {
                this.complete = true;
                drain();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:36:0x0059, code lost:
        
            r4 = true;
            r8.emitting = false;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void drain() {
            /*
                r8 = this;
                monitor-enter(r8)
                boolean r6 = r8.emitting     // Catch: java.lang.Throwable -> L33
                if (r6 == 0) goto La
                r6 = 1
                r8.missed = r6     // Catch: java.lang.Throwable -> L33
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L33
            L9:
                return
            La:
                r6 = 1
                r8.emitting = r6     // Catch: java.lang.Throwable -> L33
                r6 = 0
                r8.missed = r6     // Catch: java.lang.Throwable -> L33
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L33
                r4 = 0
            L12:
                long r2 = r8.get()     // Catch: java.lang.Throwable -> L84
                boolean r0 = r8.complete     // Catch: java.lang.Throwable -> L84
                java.util.Queue<java.lang.Object> r6 = r8.queue     // Catch: java.lang.Throwable -> L84
                boolean r1 = r6.isEmpty()     // Catch: java.lang.Throwable -> L84
                if (r0 == 0) goto L36
                if (r1 == 0) goto L36
                rx.Subscriber<? super T> r6 = r8.child     // Catch: java.lang.Throwable -> L84
                r6.onCompleted()     // Catch: java.lang.Throwable -> L84
                r4 = 1
                if (r4 != 0) goto L9
                monitor-enter(r8)
                r6 = 0
                r8.emitting = r6     // Catch: java.lang.Throwable -> L30
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L30
                goto L9
            L30:
                r6 = move-exception
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L30
                throw r6
            L33:
                r6 = move-exception
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L33
                throw r6
            L36:
                r6 = 0
                int r6 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
                if (r6 <= 0) goto L54
                java.util.Queue<java.lang.Object> r6 = r8.queue     // Catch: java.lang.Throwable -> L84
                java.lang.Object r5 = r6.poll()     // Catch: java.lang.Throwable -> L84
                if (r5 == 0) goto L69
                rx.Subscriber<? super T> r6 = r8.child     // Catch: java.lang.Throwable -> L84
                rx.internal.operators.NotificationLite<T> r7 = r8.nl     // Catch: java.lang.Throwable -> L84
                java.lang.Object r7 = r7.getValue(r5)     // Catch: java.lang.Throwable -> L84
                r6.onNext(r7)     // Catch: java.lang.Throwable -> L84
                r6 = 1
                r8.produced(r6)     // Catch: java.lang.Throwable -> L84
            L54:
                monitor-enter(r8)     // Catch: java.lang.Throwable -> L84
                boolean r6 = r8.missed     // Catch: java.lang.Throwable -> L81
                if (r6 != 0) goto L7c
                r4 = 1
                r6 = 0
                r8.emitting = r6     // Catch: java.lang.Throwable -> L81
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L81
                if (r4 != 0) goto L9
                monitor-enter(r8)
                r6 = 0
                r8.emitting = r6     // Catch: java.lang.Throwable -> L66
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L66
                goto L9
            L66:
                r6 = move-exception
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L66
                throw r6
            L69:
                if (r0 == 0) goto L54
                rx.Subscriber<? super T> r6 = r8.child     // Catch: java.lang.Throwable -> L84
                r6.onCompleted()     // Catch: java.lang.Throwable -> L84
                r4 = 1
                if (r4 != 0) goto L9
                monitor-enter(r8)
                r6 = 0
                r8.emitting = r6     // Catch: java.lang.Throwable -> L79
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L79
                goto L9
            L79:
                r6 = move-exception
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L79
                throw r6
            L7c:
                r6 = 0
                r8.missed = r6     // Catch: java.lang.Throwable -> L81
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L81
                goto L12
            L81:
                r6 = move-exception
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L81
                throw r6     // Catch: java.lang.Throwable -> L84
            L84:
                r6 = move-exception
                if (r4 != 0) goto L8c
                monitor-enter(r8)
                r7 = 0
                r8.emitting = r7     // Catch: java.lang.Throwable -> L8d
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L8d
            L8c:
                throw r6
            L8d:
                r6 = move-exception
                monitor-exit(r8)     // Catch: java.lang.Throwable -> L8d
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorMapNotification.SingleEmitter.drain():void");
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return get() < 0;
        }

        @Override // rx.Subscription
        public void unsubscribe() {
            long r = get();
            if (r != Long.MIN_VALUE) {
                long r2 = getAndSet(Long.MIN_VALUE);
                if (r2 != Long.MIN_VALUE) {
                    this.cancel.unsubscribe();
                }
            }
        }
    }
}
