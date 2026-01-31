package rx.internal.operators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.internal.util.atomic.SpscAtomicArrayQueue;
import rx.internal.util.unsafe.SpscArrayQueue;
import rx.internal.util.unsafe.UnsafeAccess;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public final class OperatorEagerConcatMap<T, R> implements Observable.Operator<R, T> {
    final int bufferSize;
    final Func1<? super T, ? extends Observable<? extends R>> mapper;

    public OperatorEagerConcatMap(Func1<? super T, ? extends Observable<? extends R>> mapper, int bufferSize) {
        this.mapper = mapper;
        this.bufferSize = bufferSize;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super R> t) {
        EagerOuterSubscriber<T, R> outer = new EagerOuterSubscriber<>(this.mapper, this.bufferSize, t);
        outer.init();
        return outer;
    }

    static final class EagerOuterProducer extends AtomicLong implements Producer {
        private static final long serialVersionUID = -657299606803478389L;
        final EagerOuterSubscriber<?, ?> parent;

        public EagerOuterProducer(EagerOuterSubscriber<?, ?> parent) {
            this.parent = parent;
        }

        @Override // rx.Producer
        public void request(long n) {
            if (n < 0) {
                throw new IllegalStateException("n >= 0 required but it was " + n);
            }
            if (n > 0) {
                BackpressureUtils.getAndAddRequest(this, n);
                this.parent.drain();
            }
        }
    }

    static final class EagerOuterSubscriber<T, R> extends Subscriber<T> {
        final Subscriber<? super R> actual;
        final int bufferSize;
        volatile boolean cancelled;
        volatile boolean done;
        Throwable error;
        final Func1<? super T, ? extends Observable<? extends R>> mapper;
        private EagerOuterProducer sharedProducer;
        final LinkedList<EagerInnerSubscriber<R>> subscribers = new LinkedList<>();
        final AtomicInteger wip = new AtomicInteger();

        public EagerOuterSubscriber(Func1<? super T, ? extends Observable<? extends R>> mapper, int bufferSize, Subscriber<? super R> actual) {
            this.mapper = mapper;
            this.bufferSize = bufferSize;
            this.actual = actual;
        }

        void init() {
            this.sharedProducer = new EagerOuterProducer(this);
            add(Subscriptions.create(new Action0() { // from class: rx.internal.operators.OperatorEagerConcatMap.EagerOuterSubscriber.1
                @Override // rx.functions.Action0
                public void call() {
                    EagerOuterSubscriber.this.cancelled = true;
                    if (EagerOuterSubscriber.this.wip.getAndIncrement() == 0) {
                        EagerOuterSubscriber.this.cleanup();
                    }
                }
            }));
            this.actual.add(this);
            this.actual.setProducer(this.sharedProducer);
        }

        void cleanup() {
            List<Subscription> list;
            synchronized (this.subscribers) {
                list = new ArrayList<>(this.subscribers);
                this.subscribers.clear();
            }
            for (Subscription s : list) {
                s.unsubscribe();
            }
        }

        @Override // rx.Observer
        public void onNext(T t) {
            try {
                Observable<? extends R> observable = this.mapper.call(t);
                EagerInnerSubscriber<R> inner = new EagerInnerSubscriber<>(this, this.bufferSize);
                if (!this.cancelled) {
                    synchronized (this.subscribers) {
                        if (!this.cancelled) {
                            this.subscribers.add(inner);
                            if (!this.cancelled) {
                                observable.unsafeSubscribe(inner);
                                drain();
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                Exceptions.throwOrReport(e, this.actual, t);
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            drain();
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.done = true;
            drain();
        }

        /* JADX WARN: Removed duplicated region for block: B:58:0x00c8  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void drain() {
            /*
                Method dump skipped, instructions count: 233
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: rx.internal.operators.OperatorEagerConcatMap.EagerOuterSubscriber.drain():void");
        }
    }

    static final class EagerInnerSubscriber<T> extends Subscriber<T> {
        volatile boolean done;
        Throwable error;
        final EagerOuterSubscriber<?, T> parent;
        final Queue<T> queue;

        public EagerInnerSubscriber(EagerOuterSubscriber<?, T> parent, int bufferSize) {
            Queue<T> q;
            this.parent = parent;
            if (UnsafeAccess.isUnsafeAvailable()) {
                q = new SpscArrayQueue<>(bufferSize);
            } else {
                q = new SpscAtomicArrayQueue<>(bufferSize);
            }
            this.queue = q;
            request(bufferSize);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.queue.offer(t);
            this.parent.drain();
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            this.parent.drain();
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.done = true;
            this.parent.drain();
        }

        void requestMore(long n) {
            request(n);
        }
    }
}
