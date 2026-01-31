package rx.internal.operators;

import java.util.HashSet;
import java.util.Set;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.internal.util.UtilityFunctions;

/* loaded from: classes.dex */
public final class OperatorDistinct<T, U> implements Observable.Operator<T, T> {
    final Func1<? super T, ? extends U> keySelector;

    private static class Holder {
        static final OperatorDistinct<?, ?> INSTANCE = new OperatorDistinct<>(UtilityFunctions.identity());

        private Holder() {
        }
    }

    public static <T> OperatorDistinct<T, T> instance() {
        return (OperatorDistinct<T, T>) Holder.INSTANCE;
    }

    public OperatorDistinct(Func1<? super T, ? extends U> keySelector) {
        this.keySelector = keySelector;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorDistinct.1
            Set<U> keyMemory = new HashSet();

            @Override // rx.Observer
            public void onNext(T t) {
                U key = OperatorDistinct.this.keySelector.call(t);
                if (this.keyMemory.add(key)) {
                    subscriber.onNext(t);
                } else {
                    request(1L);
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                this.keyMemory = null;
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onCompleted() {
                this.keyMemory = null;
                subscriber.onCompleted();
            }
        };
    }
}
