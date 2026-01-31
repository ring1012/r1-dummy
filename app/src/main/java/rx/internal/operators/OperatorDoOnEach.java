package rx.internal.operators;

import java.util.Arrays;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.exceptions.CompositeException;
import rx.exceptions.Exceptions;

/* loaded from: classes.dex */
public class OperatorDoOnEach<T> implements Observable.Operator<T, T> {
    private final Observer<? super T> doOnEachObserver;

    public OperatorDoOnEach(Observer<? super T> doOnEachObserver) {
        this.doOnEachObserver = doOnEachObserver;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorDoOnEach.1
            private boolean done = false;

            @Override // rx.Observer
            public void onCompleted() {
                if (!this.done) {
                    try {
                        OperatorDoOnEach.this.doOnEachObserver.onCompleted();
                        this.done = true;
                        subscriber.onCompleted();
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, this);
                    }
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                Exceptions.throwIfFatal(e);
                if (!this.done) {
                    this.done = true;
                    try {
                        OperatorDoOnEach.this.doOnEachObserver.onError(e);
                        subscriber.onError(e);
                    } catch (Throwable e2) {
                        Exceptions.throwIfFatal(e2);
                        subscriber.onError(new CompositeException(Arrays.asList(e, e2)));
                    }
                }
            }

            @Override // rx.Observer
            public void onNext(T value) {
                if (!this.done) {
                    try {
                        OperatorDoOnEach.this.doOnEachObserver.onNext(value);
                        subscriber.onNext(value);
                    } catch (Throwable e) {
                        Exceptions.throwOrReport(e, this, value);
                    }
                }
            }
        };
    }
}
