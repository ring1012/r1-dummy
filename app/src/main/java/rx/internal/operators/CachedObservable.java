package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.internal.util.LinkedArrayList;
import rx.subscriptions.SerialSubscription;

/* loaded from: classes.dex */
public final class CachedObservable<T> extends Observable<T> {
    private final CacheState<T> state;

    public static <T> CachedObservable<T> from(Observable<? extends T> source) {
        return from(source, 16);
    }

    public static <T> CachedObservable<T> from(Observable<? extends T> source, int capacityHint) {
        if (capacityHint < 1) {
            throw new IllegalArgumentException("capacityHint > 0 required");
        }
        CacheState<T> state = new CacheState<>(source, capacityHint);
        CachedSubscribe<T> onSubscribe = new CachedSubscribe<>(state);
        return new CachedObservable<>(onSubscribe, state);
    }

    private CachedObservable(Observable.OnSubscribe<T> onSubscribe, CacheState<T> state) {
        super(onSubscribe);
        this.state = state;
    }

    boolean isConnected() {
        return this.state.isConnected;
    }

    boolean hasObservers() {
        return this.state.producers.length != 0;
    }

    static final class CacheState<T> extends LinkedArrayList implements Observer<T> {
        static final ReplayProducer<?>[] EMPTY = new ReplayProducer[0];
        final SerialSubscription connection;
        volatile boolean isConnected;
        final NotificationLite<T> nl;
        volatile ReplayProducer<?>[] producers;
        final Observable<? extends T> source;
        boolean sourceDone;

        public CacheState(Observable<? extends T> source, int capacityHint) {
            super(capacityHint);
            this.source = source;
            this.producers = EMPTY;
            this.nl = NotificationLite.instance();
            this.connection = new SerialSubscription();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void addProducer(ReplayProducer<T> p) {
            synchronized (this.connection) {
                ReplayProducer<?>[] a2 = this.producers;
                int n = a2.length;
                ReplayProducer<?>[] replayProducerArr = new ReplayProducer[n + 1];
                System.arraycopy(a2, 0, replayProducerArr, 0, n);
                replayProducerArr[n] = p;
                this.producers = replayProducerArr;
            }
        }

        public void removeProducer(ReplayProducer<T> p) {
            synchronized (this.connection) {
                ReplayProducer<?>[] a2 = this.producers;
                int n = a2.length;
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    }
                    if (!a2[i].equals(p)) {
                        i++;
                    } else {
                        j = i;
                        break;
                    }
                }
                if (j >= 0) {
                    if (n == 1) {
                        this.producers = EMPTY;
                        return;
                    }
                    ReplayProducer<?>[] b = new ReplayProducer[n - 1];
                    System.arraycopy(a2, 0, b, 0, j);
                    System.arraycopy(a2, j + 1, b, j, (n - j) - 1);
                    this.producers = b;
                }
            }
        }

        public void connect() {
            Subscriber<T> subscriber = new Subscriber<T>() { // from class: rx.internal.operators.CachedObservable.CacheState.1
                @Override // rx.Observer
                public void onNext(T t) {
                    CacheState.this.onNext(t);
                }

                @Override // rx.Observer
                public void onError(Throwable e) {
                    CacheState.this.onError(e);
                }

                @Override // rx.Observer
                public void onCompleted() {
                    CacheState.this.onCompleted();
                }
            };
            this.connection.set(subscriber);
            this.source.unsafeSubscribe(subscriber);
            this.isConnected = true;
        }

        @Override // rx.Observer
        public void onNext(T t) {
            if (!this.sourceDone) {
                Object o = this.nl.next(t);
                add(o);
                dispatch();
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            if (!this.sourceDone) {
                this.sourceDone = true;
                Object o = this.nl.error(e);
                add(o);
                this.connection.unsubscribe();
                dispatch();
            }
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (!this.sourceDone) {
                this.sourceDone = true;
                Object o = this.nl.completed();
                add(o);
                this.connection.unsubscribe();
                dispatch();
            }
        }

        void dispatch() {
            ReplayProducer<?>[] a2 = this.producers;
            for (ReplayProducer<?> rp : a2) {
                rp.replay();
            }
        }
    }

    static final class CachedSubscribe<T> extends AtomicBoolean implements Observable.OnSubscribe<T> {
        private static final long serialVersionUID = -2817751667698696782L;
        final CacheState<T> state;

        public CachedSubscribe(CacheState<T> state) {
            this.state = state;
        }

        @Override // rx.functions.Action1
        public void call(Subscriber<? super T> t) {
            ReplayProducer<T> rp = new ReplayProducer<>(t, this.state);
            this.state.addProducer(rp);
            t.add(rp);
            t.setProducer(rp);
            if (!get() && compareAndSet(false, true)) {
                this.state.connect();
            }
        }
    }

    static final class ReplayProducer<T> extends AtomicLong implements Producer, Subscription {
        private static final long serialVersionUID = -2557562030197141021L;
        final Subscriber<? super T> child;
        Object[] currentBuffer;
        int currentIndexInBuffer;
        boolean emitting;
        int index;
        boolean missed;
        final CacheState<T> state;

        public ReplayProducer(Subscriber<? super T> child, CacheState<T> state) {
            this.child = child;
            this.state = state;
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
            replay();
        }

        public long produced(long n) {
            return addAndGet(-n);
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return get() < 0;
        }

        @Override // rx.Subscription
        public void unsubscribe() {
            long r = get();
            if (r >= 0) {
                long r2 = getAndSet(-1L);
                if (r2 >= 0) {
                    this.state.removeProducer(this);
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:124:0x018a, code lost:
        
            r20.emitting = false;
            r15 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:193:?, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:194:?, code lost:
        
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:82:0x00fc, code lost:
        
            r15 = true;
            unsubscribe();
         */
        /* JADX WARN: Code restructure failed: missing block: B:83:0x0100, code lost:
        
            if (1 != 0) goto L193;
         */
        /* JADX WARN: Code restructure failed: missing block: B:84:0x0102, code lost:
        
            monitor-enter(r20);
         */
        /* JADX WARN: Code restructure failed: missing block: B:86:0x0105, code lost:
        
            r20.emitting = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:87:0x010b, code lost:
        
            monitor-exit(r20);
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void replay() {
            /*
                Method dump skipped, instructions count: 452
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.CachedObservable.ReplayProducer.replay():void");
        }
    }
}
