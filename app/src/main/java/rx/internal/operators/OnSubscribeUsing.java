package rx.internal.operators;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.observers.Subscribers;

/* loaded from: classes.dex */
public final class OnSubscribeUsing<T, Resource> implements Observable.OnSubscribe<T> {
    private final Action1<? super Resource> dispose;
    private final boolean disposeEagerly;
    private final Func1<? super Resource, ? extends Observable<? extends T>> observableFactory;
    private final Func0<Resource> resourceFactory;

    public OnSubscribeUsing(Func0<Resource> resourceFactory, Func1<? super Resource, ? extends Observable<? extends T>> observableFactory, Action1<? super Resource> dispose, boolean disposeEagerly) {
        this.resourceFactory = resourceFactory;
        this.observableFactory = observableFactory;
        this.dispose = dispose;
        this.disposeEagerly = disposeEagerly;
    }

    @Override // rx.functions.Action1
    public void call(Subscriber<? super T> subscriber) {
        Observable<? extends T> observable;
        try {
            Resource resource = this.resourceFactory.call();
            DisposeAction<Resource> disposeOnceOnly = new DisposeAction<>(this.dispose, resource);
            subscriber.add(disposeOnceOnly);
            Observable<? extends T> source = this.observableFactory.call(resource);
            if (this.disposeEagerly) {
                observable = source.doOnTerminate(disposeOnceOnly);
            } else {
                observable = source;
            }
            try {
                observable.unsafeSubscribe(Subscribers.wrap(subscriber));
            } catch (Throwable e) {
                Throwable disposeError = disposeEagerlyIfRequested(disposeOnceOnly);
                Exceptions.throwIfFatal(e);
                Exceptions.throwIfFatal(disposeError);
                if (disposeError != null) {
                    subscriber.onError(new CompositeException(Arrays.asList(e, disposeError)));
                } else {
                    subscriber.onError(e);
                }
            }
        } catch (Throwable e2) {
            Exceptions.throwOrReport(e2, subscriber);
        }
    }

    private Throwable disposeEagerlyIfRequested(Action0 disposeOnceOnly) {
        if (!this.disposeEagerly) {
            return null;
        }
        try {
            disposeOnceOnly.call();
            return null;
        } catch (Throwable th) {
            return th;
        }
    }

    private static final class DisposeAction<Resource> extends AtomicBoolean implements Action0, Subscription {
        private static final long serialVersionUID = 4262875056400218316L;
        private Action1<? super Resource> dispose;
        private Resource resource;

        private DisposeAction(Action1<? super Resource> dispose, Resource resource) {
            this.dispose = dispose;
            this.resource = resource;
            lazySet(false);
        }

        /* JADX WARN: Type inference failed for: r2v0, types: [Resource, rx.functions.Action1<? super Resource>] */
        @Override // rx.functions.Action0
        public void call() {
            ?? r2 = (Resource) null;
            if (compareAndSet(false, true)) {
                try {
                    this.dispose.call(this.resource);
                } finally {
                    this.resource = null;
                    this.dispose = null;
                }
            }
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return get();
        }

        @Override // rx.Subscription
        public void unsubscribe() {
            call();
        }
    }
}
