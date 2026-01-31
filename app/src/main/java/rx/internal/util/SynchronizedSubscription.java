package rx.internal.util;

import rx.Subscription;

/* loaded from: classes.dex */
public class SynchronizedSubscription implements Subscription {
    private final Subscription s;

    public SynchronizedSubscription(Subscription s) {
        this.s = s;
    }

    @Override // rx.Subscription
    public synchronized void unsubscribe() {
        this.s.unsubscribe();
    }

    @Override // rx.Subscription
    public synchronized boolean isUnsubscribed() {
        return this.s.isUnsubscribed();
    }
}
