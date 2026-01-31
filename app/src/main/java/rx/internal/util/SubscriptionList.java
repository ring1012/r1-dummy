package rx.internal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import rx.Subscription;
import rx.exceptions.Exceptions;

/* loaded from: classes.dex */
public final class SubscriptionList implements Subscription {
    private LinkedList<Subscription> subscriptions;
    private volatile boolean unsubscribed;

    public SubscriptionList() {
    }

    public SubscriptionList(Subscription... subscriptions) {
        this.subscriptions = new LinkedList<>(Arrays.asList(subscriptions));
    }

    public SubscriptionList(Subscription s) {
        this.subscriptions = new LinkedList<>();
        this.subscriptions.add(s);
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
                        LinkedList<Subscription> subs = this.subscriptions;
                        if (subs == null) {
                            subs = new LinkedList<>();
                            this.subscriptions = subs;
                        }
                        subs.add(s);
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
                LinkedList<Subscription> subs = this.subscriptions;
                if (!this.unsubscribed && subs != null) {
                    boolean unsubscribe = subs.remove(s);
                    if (unsubscribe) {
                        s.unsubscribe();
                    }
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
                    List<Subscription> list = this.subscriptions;
                    this.subscriptions = null;
                    unsubscribeFromAll(list);
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

    public void clear() throws Throwable {
        List<Subscription> list;
        if (!this.unsubscribed) {
            synchronized (this) {
                list = this.subscriptions;
                this.subscriptions = null;
            }
            unsubscribeFromAll(list);
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
