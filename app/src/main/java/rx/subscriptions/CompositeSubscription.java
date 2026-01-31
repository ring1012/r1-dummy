package rx.subscriptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import rx.Subscription;
import rx.exceptions.Exceptions;

/* loaded from: classes.dex */
public final class CompositeSubscription implements Subscription {
    private Set<Subscription> subscriptions;
    private volatile boolean unsubscribed;

    public CompositeSubscription() {
    }

    public CompositeSubscription(Subscription... subscriptions) {
        this.subscriptions = new HashSet(Arrays.asList(subscriptions));
    }

    @Override // rx.Subscription
    public boolean isUnsubscribed() {
        return this.unsubscribed;
    }

    public void add(Subscription s) {
        if (!s.isUnsubscribed()) {
            if (!this.unsubscribed) {
                synchronized (this) {
                    if (!this.unsubscribed) {
                        if (this.subscriptions == null) {
                            this.subscriptions = new HashSet(4);
                        }
                        this.subscriptions.add(s);
                        return;
                    }
                }
            }
            s.unsubscribe();
        }
    }

    public void remove(Subscription s) {
        if (!this.unsubscribed) {
            synchronized (this) {
                if (!this.unsubscribed && this.subscriptions != null) {
                    boolean unsubscribe = this.subscriptions.remove(s);
                    if (unsubscribe) {
                        s.unsubscribe();
                    }
                }
            }
        }
    }

    public void clear() throws Throwable {
        if (!this.unsubscribed) {
            synchronized (this) {
                if (!this.unsubscribed && this.subscriptions != null) {
                    Collection<Subscription> unsubscribe = this.subscriptions;
                    this.subscriptions = null;
                    unsubscribeFromAll(unsubscribe);
                }
            }
        }
    }

    @Override // rx.Subscription
    public void unsubscribe() throws Throwable {
        if (!this.unsubscribed) {
            synchronized (this) {
                if (!this.unsubscribed) {
                    this.unsubscribed = true;
                    Collection<Subscription> unsubscribe = this.subscriptions;
                    this.subscriptions = null;
                    unsubscribeFromAll(unsubscribe);
                }
            }
        }
    }

    private static void unsubscribeFromAll(Collection<Subscription> subscriptions) throws Throwable {
        if (subscriptions != null) {
            List<Throwable> es = null;
            for (Subscription s : subscriptions) {
                try {
                    s.unsubscribe();
                } catch (Throwable e) {
                    if (es == null) {
                        es = new ArrayList<>();
                    }
                    es.add(e);
                }
            }
            Exceptions.throwIfAny(es);
        }
    }

    public boolean hasSubscriptions() {
        boolean z = false;
        if (!this.unsubscribed) {
            synchronized (this) {
                if (!this.unsubscribed && this.subscriptions != null && !this.subscriptions.isEmpty()) {
                    z = true;
                }
            }
        }
        return z;
    }
}
