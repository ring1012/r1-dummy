package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/* loaded from: classes.dex */
public final class OperatorFilter<T> implements Observable.Operator<T, T> {
    private final Func1<? super T, Boolean> predicate;

    public OperatorFilter(Func1<? super T, Boolean> predicate) {
        this.predicate = predicate;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorFilter.1
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
                try {
                    if (((Boolean) OperatorFilter.this.predicate.call(t)).booleanValue()) {
                        subscriber.onNext(t);
                    } else {
                        request(1L);
                    }
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, subscriber, t);
                }
            }
        };
    }
}
