package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.observers.Subscribers;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public class OperatorDoOnUnsubscribe<T> implements Observable.Operator<T, T> {
    private final Action0 unsubscribe;

    public OperatorDoOnUnsubscribe(Action0 unsubscribe) {
        this.unsubscribe = unsubscribe;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> child) {
        child.add(Subscriptions.create(this.unsubscribe));
        return Subscribers.wrap(child);
    }
}
