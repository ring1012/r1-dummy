package rx.observers;

import rx.Observer;
import rx.Subscriber;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.functions.Action1;

/* loaded from: classes.dex */
public final class Subscribers {
    private Subscribers() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Subscriber<T> empty() {
        return from(Observers.empty());
    }

    public static <T> Subscriber<T> from(final Observer<? super T> o) {
        return new Subscriber<T>() { // from class: rx.observers.Subscribers.1
            @Override // rx.Observer
            public void onCompleted() {
                o.onCompleted();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                o.onError(e);
            }

            @Override // rx.Observer
            public void onNext(T t) {
                o.onNext(t);
            }
        };
    }

    public static final <T> Subscriber<T> create(final Action1<? super T> onNext) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }
        return new Subscriber<T>() { // from class: rx.observers.Subscribers.2
            @Override // rx.Observer
            public final void onCompleted() {
            }

            @Override // rx.Observer
            public final void onError(Throwable e) {
                throw new OnErrorNotImplementedException(e);
            }

            @Override // rx.Observer
            public final void onNext(T args) {
                onNext.call(args);
            }
        };
    }

    public static final <T> Subscriber<T> create(final Action1<? super T> onNext, final Action1<Throwable> onError) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }
        if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        }
        return new Subscriber<T>() { // from class: rx.observers.Subscribers.3
            @Override // rx.Observer
            public final void onCompleted() {
            }

            @Override // rx.Observer
            public final void onError(Throwable e) {
                onError.call(e);
            }

            @Override // rx.Observer
            public final void onNext(T args) {
                onNext.call(args);
            }
        };
    }

    public static final <T> Subscriber<T> create(final Action1<? super T> onNext, final Action1<Throwable> onError, final Action0 onComplete) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }
        if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        }
        if (onComplete == null) {
            throw new IllegalArgumentException("onComplete can not be null");
        }
        return new Subscriber<T>() { // from class: rx.observers.Subscribers.4
            @Override // rx.Observer
            public final void onCompleted() {
                onComplete.call();
            }

            @Override // rx.Observer
            public final void onError(Throwable e) {
                onError.call(e);
            }

            @Override // rx.Observer
            public final void onNext(T args) {
                onNext.call(args);
            }
        };
    }

    public static <T> Subscriber<T> wrap(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.observers.Subscribers.5
            @Override // rx.Observer
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onNext(T t) {
                subscriber.onNext(t);
            }
        };
    }
}
