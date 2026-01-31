package rx.observables;

import java.util.concurrent.atomic.AtomicLong;
import rx.Observable;
import rx.Observer;
import rx.Producer;
import rx.Subscriber;
import rx.Subscription;
import rx.annotations.Experimental;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func2;
import rx.internal.operators.BackpressureUtils;
import rx.plugins.RxJavaPlugins;

@Experimental
/* loaded from: classes.dex */
public abstract class SyncOnSubscribe<S, T> implements Observable.OnSubscribe<T> {
    protected abstract S generateState();

    protected abstract S next(S s, Observer<? super T> observer);

    @Override // rx.functions.Action1
    public final void call(Subscriber<? super T> subscriber) {
        S state = generateState();
        SubscriptionProducer<S, T> p = new SubscriptionProducer<>(subscriber, this, state);
        subscriber.add(p);
        subscriber.setProducer(p);
    }

    protected void onUnsubscribe(S state) {
    }

    @Experimental
    public static <S, T> Observable.OnSubscribe<T> createSingleState(Func0<? extends S> generator, final Action2<? super S, ? super Observer<? super T>> next) {
        Func2<S, ? super Observer<? super T>, S> nextFunc = new Func2<S, Observer<? super T>, S>() { // from class: rx.observables.SyncOnSubscribe.1
            @Override // rx.functions.Func2
            public /* bridge */ /* synthetic */ Object call(Object x0, Object x1) {
                return call((AnonymousClass1) x0, (Observer) x1);
            }

            public S call(S state, Observer<? super T> subscriber) {
                next.call(state, subscriber);
                return state;
            }
        };
        return new SyncOnSubscribeImpl(generator, nextFunc);
    }

    @Experimental
    public static <S, T> Observable.OnSubscribe<T> createSingleState(Func0<? extends S> generator, final Action2<? super S, ? super Observer<? super T>> next, Action1<? super S> onUnsubscribe) {
        Func2<S, Observer<? super T>, S> nextFunc = new Func2<S, Observer<? super T>, S>() { // from class: rx.observables.SyncOnSubscribe.2
            @Override // rx.functions.Func2
            public /* bridge */ /* synthetic */ Object call(Object x0, Object x1) {
                return call((AnonymousClass2) x0, (Observer) x1);
            }

            public S call(S state, Observer<? super T> subscriber) {
                next.call(state, subscriber);
                return state;
            }
        };
        return new SyncOnSubscribeImpl(generator, nextFunc, onUnsubscribe);
    }

    @Experimental
    public static <S, T> Observable.OnSubscribe<T> createStateful(Func0<? extends S> generator, Func2<? super S, ? super Observer<? super T>, ? extends S> next, Action1<? super S> onUnsubscribe) {
        return new SyncOnSubscribeImpl(generator, next, onUnsubscribe);
    }

    @Experimental
    public static <S, T> Observable.OnSubscribe<T> createStateful(Func0<? extends S> generator, Func2<? super S, ? super Observer<? super T>, ? extends S> next) {
        return new SyncOnSubscribeImpl(generator, next);
    }

    @Experimental
    public static <T> Observable.OnSubscribe<T> createStateless(final Action1<? super Observer<? super T>> next) {
        Func2<Void, Observer<? super T>, Void> nextFunc = new Func2<Void, Observer<? super T>, Void>() { // from class: rx.observables.SyncOnSubscribe.3
            @Override // rx.functions.Func2
            public Void call(Void state, Observer<? super T> subscriber) {
                next.call(subscriber);
                return state;
            }
        };
        return new SyncOnSubscribeImpl(nextFunc);
    }

    @Experimental
    public static <T> Observable.OnSubscribe<T> createStateless(final Action1<? super Observer<? super T>> next, final Action0 onUnsubscribe) {
        Func2<Void, Observer<? super T>, Void> nextFunc = new Func2<Void, Observer<? super T>, Void>() { // from class: rx.observables.SyncOnSubscribe.4
            @Override // rx.functions.Func2
            public Void call(Void state, Observer<? super T> subscriber) {
                next.call(subscriber);
                return null;
            }
        };
        Action1<? super Void> wrappedOnUnsubscribe = new Action1<Void>() { // from class: rx.observables.SyncOnSubscribe.5
            @Override // rx.functions.Action1
            public void call(Void t) {
                onUnsubscribe.call();
            }
        };
        return new SyncOnSubscribeImpl(nextFunc, wrappedOnUnsubscribe);
    }

    private static final class SyncOnSubscribeImpl<S, T> extends SyncOnSubscribe<S, T> {
        private final Func0<? extends S> generator;
        private final Func2<? super S, ? super Observer<? super T>, ? extends S> next;
        private final Action1<? super S> onUnsubscribe;

        @Override // rx.observables.SyncOnSubscribe, rx.functions.Action1
        public /* bridge */ /* synthetic */ void call(Object x0) {
            super.call((Subscriber) x0);
        }

        private SyncOnSubscribeImpl(Func0<? extends S> generator, Func2<? super S, ? super Observer<? super T>, ? extends S> next, Action1<? super S> onUnsubscribe) {
            this.generator = generator;
            this.next = next;
            this.onUnsubscribe = onUnsubscribe;
        }

        public SyncOnSubscribeImpl(Func0<? extends S> generator, Func2<? super S, ? super Observer<? super T>, ? extends S> next) {
            this(generator, next, null);
        }

        public SyncOnSubscribeImpl(Func2<S, Observer<? super T>, S> next, Action1<? super S> onUnsubscribe) {
            this(null, next, onUnsubscribe);
        }

        public SyncOnSubscribeImpl(Func2<S, Observer<? super T>, S> nextFunc) {
            this(null, nextFunc, null);
        }

        @Override // rx.observables.SyncOnSubscribe
        protected S generateState() {
            if (this.generator == null) {
                return null;
            }
            return this.generator.call();
        }

        @Override // rx.observables.SyncOnSubscribe
        protected S next(S state, Observer<? super T> observer) {
            return this.next.call(state, observer);
        }

        @Override // rx.observables.SyncOnSubscribe
        protected void onUnsubscribe(S state) {
            if (this.onUnsubscribe != null) {
                this.onUnsubscribe.call(state);
            }
        }
    }

    private static class SubscriptionProducer<S, T> extends AtomicLong implements Producer, Subscription, Observer<T> {
        private static final long serialVersionUID = -3736864024352728072L;
        private final Subscriber<? super T> actualSubscriber;
        private boolean hasTerminated;
        private boolean onNextCalled;
        private final SyncOnSubscribe<S, T> parent;
        private S state;

        private SubscriptionProducer(Subscriber<? super T> subscriber, SyncOnSubscribe<S, T> parent, S state) {
            this.actualSubscriber = subscriber;
            this.parent = parent;
            this.state = state;
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return get() < 0;
        }

        @Override // rx.Subscription
        public void unsubscribe() {
            long requestCount;
            do {
                requestCount = get();
                if (compareAndSet(0L, -1L)) {
                    doUnsubscribe();
                    return;
                }
            } while (!compareAndSet(requestCount, -2L));
        }

        private boolean tryUnsubscribe() {
            if (!this.hasTerminated && get() >= -1) {
                return false;
            }
            set(-1L);
            doUnsubscribe();
            return true;
        }

        private void doUnsubscribe() {
            this.parent.onUnsubscribe(this.state);
        }

        @Override // rx.Producer
        public void request(long n) {
            if (n <= 0 || BackpressureUtils.getAndAddRequest(this, n) != 0) {
                return;
            }
            if (n == Long.MAX_VALUE) {
                fastpath();
            } else {
                slowPath(n);
            }
        }

        private void fastpath() {
            SyncOnSubscribe<S, T> p = this.parent;
            Subscriber<? super T> a2 = this.actualSubscriber;
            do {
                try {
                    this.onNextCalled = false;
                    nextIteration(p);
                } catch (Throwable ex) {
                    handleThrownError(a2, ex);
                    return;
                }
            } while (!tryUnsubscribe());
        }

        private void handleThrownError(Subscriber<? super T> a2, Throwable ex) {
            if (this.hasTerminated) {
                RxJavaPlugins.getInstance().getErrorHandler().handleError(ex);
                return;
            }
            this.hasTerminated = true;
            a2.onError(ex);
            unsubscribe();
        }

        private void slowPath(long n) {
            SyncOnSubscribe<S, T> p = this.parent;
            Subscriber<? super T> a2 = this.actualSubscriber;
            long numRequested = n;
            do {
                long numRemaining = numRequested;
                do {
                    try {
                        this.onNextCalled = false;
                        nextIteration(p);
                        if (!tryUnsubscribe()) {
                            if (this.onNextCalled) {
                                numRemaining--;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable ex) {
                        handleThrownError(a2, ex);
                        return;
                    }
                } while (numRemaining != 0);
                numRequested = addAndGet(-numRequested);
            } while (numRequested > 0);
            tryUnsubscribe();
        }

        private void nextIteration(SyncOnSubscribe<S, T> parent) {
            this.state = parent.next(this.state, this);
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            if (!this.actualSubscriber.isUnsubscribed()) {
                this.actualSubscriber.onCompleted();
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            if (!this.actualSubscriber.isUnsubscribed()) {
                this.actualSubscriber.onError(e);
            }
        }

        @Override // rx.Observer
        public void onNext(T value) {
            if (this.onNextCalled) {
                throw new IllegalStateException("onNext called multiple times!");
            }
            this.onNextCalled = true;
            this.actualSubscriber.onNext(value);
        }
    }
}
