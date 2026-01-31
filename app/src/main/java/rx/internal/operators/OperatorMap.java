package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;

/* loaded from: classes.dex */
public final class OperatorMap<T, R> implements Observable.Operator<R, T> {
    private final Func1<? super T, ? extends R> transformer;

    public OperatorMap(Func1<? super T, ? extends R> transformer) {
        this.transformer = transformer;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super R> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorMap.1
            @Override // rx.Observer
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // rx.Observer
            public void onNext(T t) {
                try {
                    subscriber.onNext(OperatorMap.this.transformer.call(t));
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, this, t);
                }
            }
        };
    }
}
