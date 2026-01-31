package rx.observables;

import rx.Observable;
import rx.Subscriber;

/* loaded from: classes.dex */
public class GroupedObservable<K, T> extends Observable<T> {
    private final K key;

    public static <K, T> GroupedObservable<K, T> from(K key, final Observable<T> o) {
        return new GroupedObservable<>(key, new Observable.OnSubscribe<T>() { // from class: rx.observables.GroupedObservable.1
            @Override // rx.functions.Action1
            public void call(Subscriber<? super T> s) {
                o.unsafeSubscribe(s);
            }
        });
    }

    public static final <K, T> GroupedObservable<K, T> create(K key, Observable.OnSubscribe<T> f) {
        return new GroupedObservable<>(key, f);
    }

    protected GroupedObservable(K key, Observable.OnSubscribe<T> onSubscribe) {
        super(onSubscribe);
        this.key = key;
    }

    public K getKey() {
        return this.key;
    }
}
