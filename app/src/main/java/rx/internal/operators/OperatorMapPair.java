package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.functions.Func2;

/* loaded from: classes.dex */
public final class OperatorMapPair<T, U, R> implements Observable.Operator<Observable<? extends R>, T> {
    final Func1<? super T, ? extends Observable<? extends U>> collectionSelector;
    final Func2<? super T, ? super U, ? extends R> resultSelector;

    public static <T, U> Func1<T, Observable<U>> convertSelector(final Func1<? super T, ? extends Iterable<? extends U>> selector) {
        return new Func1<T, Observable<U>>() { // from class: rx.internal.operators.OperatorMapPair.1
            @Override // rx.functions.Func1
            public /* bridge */ /* synthetic */ Object call(Object x0) {
                return call((AnonymousClass1) x0);
            }

            @Override // rx.functions.Func1
            public Observable<U> call(T t1) {
                return Observable.from((Iterable) selector.call(t1));
            }
        };
    }

    public OperatorMapPair(Func1<? super T, ? extends Observable<? extends U>> collectionSelector, Func2<? super T, ? super U, ? extends R> resultSelector) {
        this.collectionSelector = collectionSelector;
        this.resultSelector = resultSelector;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super Observable<? extends R>> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorMapPair.2
            @Override // rx.Observer
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onNext(final T t) {
                try {
                    subscriber.onNext(OperatorMapPair.this.collectionSelector.call(t).map(new Func1<U, R>() { // from class: rx.internal.operators.OperatorMapPair.2.1
                        @Override // rx.functions.Func1
                        public R call(U u) {
                            return OperatorMapPair.this.resultSelector.call((Object) t, u);
                        }
                    }));
                } catch (Throwable th) {
                    Exceptions.throwOrReport(th, subscriber, t);
                }
            }
        };
    }
}
