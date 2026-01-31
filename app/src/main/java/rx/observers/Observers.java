package rx.observers;

import rx.Observer;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.functions.Action1;

/* loaded from: classes.dex */
public final class Observers {
    private static final Observer<Object> EMPTY = new Observer<Object>() { // from class: rx.observers.Observers.1
        @Override // rx.Observer
        public final void onCompleted() {
        }

        @Override // rx.Observer
        public final void onError(Throwable e) {
            throw new OnErrorNotImplementedException(e);
        }

        @Override // rx.Observer
        public final void onNext(Object args) {
        }
    };

    private Observers() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Observer<T> empty() {
        return (Observer<T>) EMPTY;
    }

    public static final <T> Observer<T> create(final Action1<? super T> onNext) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }
        return new Observer<T>() { // from class: rx.observers.Observers.2
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

    public static final <T> Observer<T> create(final Action1<? super T> onNext, final Action1<Throwable> onError) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }
        if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        }
        return new Observer<T>() { // from class: rx.observers.Observers.3
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

    public static final <T> Observer<T> create(final Action1<? super T> onNext, final Action1<Throwable> onError, final Action0 onComplete) {
        if (onNext == null) {
            throw new IllegalArgumentException("onNext can not be null");
        }
        if (onError == null) {
            throw new IllegalArgumentException("onError can not be null");
        }
        if (onComplete == null) {
            throw new IllegalArgumentException("onComplete can not be null");
        }
        return new Observer<T>() { // from class: rx.observers.Observers.4
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
}
