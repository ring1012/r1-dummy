package rx.internal.operators;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.internal.util.UtilityFunctions;

/* loaded from: classes.dex */
public final class OperatorSequenceEqual {
    private static final Object LOCAL_ONCOMPLETED = new Object();

    private OperatorSequenceEqual() {
        throw new IllegalStateException("No instances!");
    }

    static <T> Observable<Object> materializeLite(Observable<T> observable) {
        return Observable.concat(observable.map(new Func1<T, Object>() { // from class: rx.internal.operators.OperatorSequenceEqual.1
            @Override // rx.functions.Func1
            public Object call(T t1) {
                return t1;
            }
        }), Observable.just(LOCAL_ONCOMPLETED));
    }

    public static <T> Observable<Boolean> sequenceEqual(Observable<? extends T> first, Observable<? extends T> second, final Func2<? super T, ? super T, Boolean> equality) {
        Observable<Object> firstObservable = materializeLite(first);
        Observable<Object> secondObservable = materializeLite(second);
        return Observable.zip(firstObservable, secondObservable, new Func2<Object, Object, Boolean>() { // from class: rx.internal.operators.OperatorSequenceEqual.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // rx.functions.Func2
            public Boolean call(Object t1, Object t2) {
                boolean c1 = t1 == OperatorSequenceEqual.LOCAL_ONCOMPLETED;
                boolean c2 = t2 == OperatorSequenceEqual.LOCAL_ONCOMPLETED;
                if (c1 && c2) {
                    return true;
                }
                if (c1 || c2) {
                    return false;
                }
                return (Boolean) equality.call(t1, t2);
            }
        }).all(UtilityFunctions.identity());
    }
}
