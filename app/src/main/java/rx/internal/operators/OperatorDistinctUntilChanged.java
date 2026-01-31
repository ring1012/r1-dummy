package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.internal.util.UtilityFunctions;

/* loaded from: classes.dex */
public final class OperatorDistinctUntilChanged<T, U> implements Observable.Operator<T, T> {
    final Func1<? super T, ? extends U> keySelector;

    private static class Holder {
        static final OperatorDistinctUntilChanged<?, ?> INSTANCE = new OperatorDistinctUntilChanged<>(UtilityFunctions.identity());

        private Holder() {
        }
    }

    public static <T> OperatorDistinctUntilChanged<T, T> instance() {
        return (OperatorDistinctUntilChanged<T, T>) Holder.INSTANCE;
    }

    public OperatorDistinctUntilChanged(Func1<? super T, ? extends U> keySelector) {
        this.keySelector = keySelector;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorDistinctUntilChanged.1
            boolean hasPrevious;
            U previousKey;

            @Override // rx.Observer
            public void onNext(T t) {
                U currentKey = this.previousKey;
                U key = OperatorDistinctUntilChanged.this.keySelector.call(t);
                this.previousKey = key;
                if (this.hasPrevious) {
                    if (currentKey != key && (key == null || !key.equals(currentKey))) {
                        subscriber.onNext(t);
                        return;
                    } else {
                        request(1L);
                        return;
                    }
                }
                this.hasPrevious = true;
                subscriber.onNext(t);
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onCompleted() {
                subscriber.onCompleted();
            }
        };
    }
}
