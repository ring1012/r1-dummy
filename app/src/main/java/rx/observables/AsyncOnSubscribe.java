package rx.observables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.annotations.Experimental;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Action3;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func3;
import rx.internal.operators.BufferUntilSubscriber;
import rx.observers.SerializedObserver;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;

@Experimental
/* loaded from: classes.dex */
public abstract class AsyncOnSubscribe<S, T> implements Observable.OnSubscribe<T> {
    protected abstract S generateState();

    protected abstract S next(S s, long j, Observer<Observable<? extends T>> observer);

    protected void onUnsubscribe(S state) {
    }

    @Experimental
    public static <S, T> Observable.OnSubscribe<T> createSingleState(Func0<? extends S> generator, final Action3<? super S, Long, ? super Observer<Observable<? extends T>>> next) {
        Func3<S, Long, ? super Observer<Observable<? extends T>>, S> nextFunc = new Func3<S, Long, Observer<Observable<? extends T>>, S>() { // from class: rx.observables.AsyncOnSubscribe.1
            @Override // rx.functions.Func3
            public /* bridge */ /* synthetic */ Object call(Object x0, Long l, Object x2) {
                return call((AnonymousClass1) x0, l, (Observer) x2);
            }

            public S call(S state, Long requested, Observer<Observable<? extends T>> subscriber) {
                next.call(state, requested, subscriber);
                return state;
            }
        };
        return new AsyncOnSubscribeImpl(generator, nextFunc);
    }

    @Experimental
    public static <S, T> Observable.OnSubscribe<T> createSingleState(Func0<? extends S> generator, final Action3<? super S, Long, ? super Observer<Observable<? extends T>>> next, Action1<? super S> onUnsubscribe) {
        Func3<S, Long, Observer<Observable<? extends T>>, S> nextFunc = new Func3<S, Long, Observer<Observable<? extends T>>, S>() { // from class: rx.observables.AsyncOnSubscribe.2
            @Override // rx.functions.Func3
            public /* bridge */ /* synthetic */ Object call(Object x0, Long l, Object x2) {
                return call((AnonymousClass2) x0, l, (Observer) x2);
            }

            public S call(S state, Long requested, Observer<Observable<? extends T>> subscriber) {
                next.call(state, requested, subscriber);
                return state;
            }
        };
        return new AsyncOnSubscribeImpl(generator, nextFunc, onUnsubscribe);
    }

    @Experimental
    public static <S, T> Observable.OnSubscribe<T> createStateful(Func0<? extends S> generator, Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> next, Action1<? super S> onUnsubscribe) {
        return new AsyncOnSubscribeImpl(generator, next, onUnsubscribe);
    }

    @Experimental
    public static <S, T> Observable.OnSubscribe<T> createStateful(Func0<? extends S> generator, Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> next) {
        return new AsyncOnSubscribeImpl(generator, next);
    }

    @Experimental
    public static <T> Observable.OnSubscribe<T> createStateless(final Action2<Long, ? super Observer<Observable<? extends T>>> next) {
        Func3<Void, Long, Observer<Observable<? extends T>>, Void> nextFunc = new Func3<Void, Long, Observer<Observable<? extends T>>, Void>() { // from class: rx.observables.AsyncOnSubscribe.3
            @Override // rx.functions.Func3
            public Void call(Void state, Long requested, Observer<Observable<? extends T>> subscriber) {
                next.call(requested, subscriber);
                return state;
            }
        };
        return new AsyncOnSubscribeImpl(nextFunc);
    }

    @Experimental
    public static <T> Observable.OnSubscribe<T> createStateless(final Action2<Long, ? super Observer<Observable<? extends T>>> next, final Action0 onUnsubscribe) {
        Func3<Void, Long, Observer<Observable<? extends T>>, Void> nextFunc = new Func3<Void, Long, Observer<Observable<? extends T>>, Void>() { // from class: rx.observables.AsyncOnSubscribe.4
            @Override // rx.functions.Func3
            public Void call(Void state, Long requested, Observer<Observable<? extends T>> subscriber) {
                next.call(requested, subscriber);
                return null;
            }
        };
        Action1<? super Void> wrappedOnUnsubscribe = new Action1<Void>() { // from class: rx.observables.AsyncOnSubscribe.5
            @Override // rx.functions.Action1
            public void call(Void t) {
                onUnsubscribe.call();
            }
        };
        return new AsyncOnSubscribeImpl(nextFunc, wrappedOnUnsubscribe);
    }

    private static final class AsyncOnSubscribeImpl<S, T> extends AsyncOnSubscribe<S, T> {
        private final Func0<? extends S> generator;
        private final Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> next;
        private final Action1<? super S> onUnsubscribe;

        @Override // rx.observables.AsyncOnSubscribe, rx.functions.Action1
        public /* bridge */ /* synthetic */ void call(Object x0) {
            super.call((Subscriber) x0);
        }

        private AsyncOnSubscribeImpl(Func0<? extends S> generator, Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> next, Action1<? super S> onUnsubscribe) {
            this.generator = generator;
            this.next = next;
            this.onUnsubscribe = onUnsubscribe;
        }

        public AsyncOnSubscribeImpl(Func0<? extends S> generator, Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> next) {
            this(generator, next, null);
        }

        public AsyncOnSubscribeImpl(Func3<S, Long, Observer<Observable<? extends T>>, S> next, Action1<? super S> onUnsubscribe) {
            this(null, next, onUnsubscribe);
        }

        public AsyncOnSubscribeImpl(Func3<S, Long, Observer<Observable<? extends T>>, S> nextFunc) {
            this(null, nextFunc, null);
        }

        @Override // rx.observables.AsyncOnSubscribe
        protected S generateState() {
            if (this.generator == null) {
                return null;
            }
            return this.generator.call();
        }

        @Override // rx.observables.AsyncOnSubscribe
        protected S next(S state, long requested, Observer<Observable<? extends T>> observer) {
            return this.next.call(state, Long.valueOf(requested), observer);
        }

        @Override // rx.observables.AsyncOnSubscribe
        protected void onUnsubscribe(S state) {
            if (this.onUnsubscribe != null) {
                this.onUnsubscribe.call(state);
            }
        }
    }

    @Override // rx.functions.Action1
    public final void call(final Subscriber<? super T> actualSubscriber) {
        try {
            S state = generateState();
            UnicastSubject<Observable<T>> subject = UnicastSubject.create();
            final AsyncOuterManager<S, T> outerProducer = new AsyncOuterManager<>(this, state, subject);
            Subscriber<T> concatSubscriber = new Subscriber<T>() { // from class: rx.observables.AsyncOnSubscribe.6
                @Override // rx.Observer
                public void onNext(T t) {
                    actualSubscriber.onNext(t);
                }

                @Override // rx.Observer
                public void onError(Throwable e) {
                    actualSubscriber.onError(e);
                }

                @Override // rx.Observer
                public void onCompleted() {
                    actualSubscriber.onCompleted();
                }

                @Override // rx.Subscriber
                public void setProducer(Producer p) {
                    outerProducer.setConcatProducer(p);
                }
            };
            subject.onBackpressureBuffer().concatMap(new Func1<Observable<T>, Observable<T>>() { // from class: rx.observables.AsyncOnSubscribe.7
                @Override // rx.functions.Func1
                public Observable<T> call(Observable<T> v) {
                    return v.onBackpressureBuffer();
                }
            }).unsafeSubscribe(concatSubscriber);
            actualSubscriber.add(concatSubscriber);
            actualSubscriber.add(outerProducer);
            actualSubscriber.setProducer(outerProducer);
        } catch (Throwable ex) {
            actualSubscriber.onError(ex);
        }
    }

    static final class AsyncOuterManager<S, T> implements Producer, Subscription, Observer<Observable<? extends T>> {
        private static final AtomicIntegerFieldUpdater<AsyncOuterManager> IS_UNSUBSCRIBED = AtomicIntegerFieldUpdater.newUpdater(AsyncOuterManager.class, "isUnsubscribed");
        Producer concatProducer;
        boolean emitting;
        long expectedDelivery;
        private boolean hasTerminated;
        private volatile int isUnsubscribed;
        private final UnicastSubject<Observable<T>> merger;
        private boolean onNextCalled;
        private final AsyncOnSubscribe<S, T> parent;
        List<Long> requests;
        private S state;
        private final CompositeSubscription subscriptions = new CompositeSubscription();
        private final SerializedObserver<Observable<? extends T>> serializedSubscriber = new SerializedObserver<>(this);

        public AsyncOuterManager(AsyncOnSubscribe<S, T> parent, S initialState, UnicastSubject<Observable<T>> merger) {
            this.parent = parent;
            this.state = initialState;
            this.merger = merger;
        }

        @Override // rx.Subscription
        public void unsubscribe() throws Throwable {
            if (IS_UNSUBSCRIBED.compareAndSet(this, 0, 1)) {
                synchronized (this) {
                    if (this.emitting) {
                        this.requests = new ArrayList();
                        this.requests.add(0L);
                    } else {
                        this.emitting = true;
                        cleanup();
                    }
                }
            }
        }

        void setConcatProducer(Producer p) {
            if (this.concatProducer != null) {
                throw new IllegalStateException("setConcatProducer may be called at most once!");
            }
            this.concatProducer = p;
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return this.isUnsubscribed != 0;
        }

        public void nextIteration(long requestCount) {
            this.state = this.parent.next(this.state, requestCount, this.serializedSubscriber);
        }

        void cleanup() throws Throwable {
            this.subscriptions.unsubscribe();
            try {
                this.parent.onUnsubscribe(this.state);
            } catch (Throwable ex) {
                handleThrownError(ex);
            }
        }

        @Override // rx.Producer
        public void request(long n) {
            if (n == 0) {
                return;
            }
            if (n < 0) {
                throw new IllegalStateException("Request can't be negative! " + n);
            }
            boolean quit = false;
            synchronized (this) {
                if (this.emitting) {
                    List<Long> q = this.requests;
                    if (q == null) {
                        q = new ArrayList<>();
                        this.requests = q;
                    }
                    q.add(Long.valueOf(n));
                    quit = true;
                } else {
                    this.emitting = true;
                }
            }
            this.concatProducer.request(n);
            if (quit || tryEmit(n)) {
                return;
            }
            while (true) {
                synchronized (this) {
                    List<Long> q2 = this.requests;
                    if (q2 == null) {
                        this.emitting = false;
                        return;
                    }
                    this.requests = null;
                    Iterator i$ = q2.iterator();
                    while (i$.hasNext()) {
                        long r = i$.next().longValue();
                        if (tryEmit(r)) {
                            return;
                        }
                    }
                }
            }
        }

        public void requestRemaining(long n) {
            if (n == 0) {
                return;
            }
            if (n < 0) {
                throw new IllegalStateException("Request can't be negative! " + n);
            }
            synchronized (this) {
                if (this.emitting) {
                    List<Long> q = this.requests;
                    if (q == null) {
                        q = new ArrayList<>();
                        this.requests = q;
                    }
                    q.add(Long.valueOf(n));
                    return;
                }
                this.emitting = true;
                if (tryEmit(n)) {
                    return;
                }
                while (true) {
                    synchronized (this) {
                        List<Long> q2 = this.requests;
                        if (q2 == null) {
                            this.emitting = false;
                            return;
                        }
                        this.requests = null;
                        Iterator i$ = q2.iterator();
                        while (i$.hasNext()) {
                            long r = i$.next().longValue();
                            if (tryEmit(r)) {
                                return;
                            }
                        }
                    }
                }
            }
        }

        boolean tryEmit(long n) throws Throwable {
            boolean z = true;
            if (isUnsubscribed()) {
                cleanup();
            } else {
                try {
                    this.onNextCalled = false;
                    this.expectedDelivery = n;
                    nextIteration(n);
                    if (this.hasTerminated || isUnsubscribed()) {
                        cleanup();
                    } else if (this.onNextCalled) {
                        z = false;
                    } else {
                        handleThrownError(new IllegalStateException("No events emitted!"));
                    }
                } catch (Throwable ex) {
                    handleThrownError(ex);
                }
            }
            return z;
        }

        private void handleThrownError(Throwable ex) throws Throwable {
            if (this.hasTerminated) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(ex);
                return;
            }
            this.hasTerminated = true;
            this.merger.onError(ex);
            cleanup();
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            this.merger.onCompleted();
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            this.merger.onError(e);
        }

        @Override // rx.Observer
        public void onNext(Observable<? extends T> t) {
            if (this.onNextCalled) {
                throw new IllegalStateException("onNext called multiple times!");
            }
            this.onNextCalled = true;
            if (!this.hasTerminated) {
                subscribeBufferToObservable(t);
            }
        }

        private void subscribeBufferToObservable(Observable<? extends T> t) {
            final BufferUntilSubscriber<T> buffer = BufferUntilSubscriber.create();
            final long expected = this.expectedDelivery;
            final Subscriber<T> s = new Subscriber<T>() { // from class: rx.observables.AsyncOnSubscribe.AsyncOuterManager.1
                long remaining;

                {
                    this.remaining = expected;
                }

                @Override // rx.Observer
                public void onNext(T t2) {
                    this.remaining--;
                    buffer.onNext(t2);
                }

                @Override // rx.Observer
                public void onError(Throwable e) {
                    buffer.onError(e);
                }

                @Override // rx.Observer
                public void onCompleted() {
                    buffer.onCompleted();
                    long r = this.remaining;
                    if (r > 0) {
                        AsyncOuterManager.this.requestRemaining(r);
                    }
                }
            };
            this.subscriptions.add(s);
            t.doOnTerminate(new Action0() { // from class: rx.observables.AsyncOnSubscribe.AsyncOuterManager.2
                @Override // rx.functions.Action0
                public void call() {
                    AsyncOuterManager.this.subscriptions.remove(s);
                }
            }).subscribe((Subscriber<? super Object>) s);
            this.merger.onNext(buffer);
        }
    }

    static final class UnicastSubject<T> extends Observable<T> implements Observer<T> {
        private State<T> state;

        public static <T> UnicastSubject<T> create() {
            return new UnicastSubject<>(new State());
        }

        protected UnicastSubject(State<T> state) {
            super(state);
            this.state = state;
        }

        @Override // rx.Observer
        public void onCompleted() {
            ((State) this.state).subscriber.onCompleted();
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            ((State) this.state).subscriber.onError(e);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            ((State) this.state).subscriber.onNext(t);
        }

        static final class State<T> implements Observable.OnSubscribe<T> {
            private Subscriber<? super T> subscriber;

            State() {
            }

            @Override // rx.functions.Action1
            public void call(Subscriber<? super T> s) {
                synchronized (this) {
                    if (this.subscriber == null) {
                        this.subscriber = s;
                    } else {
                        s.onError(new IllegalStateException("There can be only one subscriber"));
                    }
                }
            }
        }
    }
}
