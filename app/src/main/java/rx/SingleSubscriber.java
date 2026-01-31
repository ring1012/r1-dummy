package rx;

import rx.annotations.Beta;
import rx.internal.util.SubscriptionList;

@Beta
/* loaded from: classes.dex */
public abstract class SingleSubscriber<T> implements Subscription {
    private final SubscriptionList cs = new SubscriptionList();

    public abstract void onError(Throwable th);

    public abstract void onSuccess(T t);

    public final void add(Subscription s) {
        this.cs.add(s);
    }

    @Override // rx.Subscription
    public final void unsubscribe() throws Throwable {
        this.cs.unsubscribe();
    }

    @Override // rx.Subscription
    public final boolean isUnsubscribed() {
        return this.cs.isUnsubscribed();
    }
}
