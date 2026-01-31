package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;

/* loaded from: classes.dex */
public final class OperatorFinally<T> implements Observable.Operator<T, T> {
    final Action0 action;

    public OperatorFinally(Action0 action) {
        if (action == null) {
            throw new NullPointerException("Action can not be null");
        }
        this.action = action;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorFinally.1
            @Override // rx.Observer
            public void onNext(T t) {
                subscriber.onNext(t);
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                try {
                    subscriber.onError(e);
                } finally {
                    OperatorFinally.this.action.call();
                }
            }

            @Override // rx.Observer
            public void onCompleted() {
                try {
                    subscriber.onCompleted();
                } finally {
                    OperatorFinally.this.action.call();
                }
            }
        };
    }
}
