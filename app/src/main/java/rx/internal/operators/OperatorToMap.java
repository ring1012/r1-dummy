package rx.internal.operators;

import java.util.HashMap;
import java.util.Map;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;

/* loaded from: classes.dex */
public final class OperatorToMap<T, K, V> implements Observable.Operator<Map<K, V>, T> {
    private final Func1<? super T, ? extends K> keySelector;
    private final Func0<? extends Map<K, V>> mapFactory;
    private final Func1<? super T, ? extends V> valueSelector;

    public static final class DefaultToMapFactory<K, V> implements Func0<Map<K, V>> {
        @Override // rx.functions.Func0, java.util.concurrent.Callable
        public Map<K, V> call() {
            return new HashMap();
        }
    }

    public OperatorToMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector) {
        this(keySelector, valueSelector, new DefaultToMapFactory());
    }

    public OperatorToMap(Func1<? super T, ? extends K> keySelector, Func1<? super T, ? extends V> valueSelector, Func0<? extends Map<K, V>> mapFactory) {
        this.keySelector = keySelector;
        this.valueSelector = valueSelector;
        this.mapFactory = mapFactory;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super Map<K, V>> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorToMap.1
            private Map<K, V> map;

            {
                this.map = (Map) OperatorToMap.this.mapFactory.call();
            }

            @Override // rx.Subscriber
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // rx.Observer
            public void onNext(T t) {
                this.map.put(OperatorToMap.this.keySelector.call(t), OperatorToMap.this.valueSelector.call(t));
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                this.map = null;
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onCompleted() {
                Map<K, V> map0 = this.map;
                this.map = null;
                subscriber.onNext(map0);
                subscriber.onCompleted();
            }
        };
    }
}
