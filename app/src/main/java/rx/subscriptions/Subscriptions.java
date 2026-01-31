package rx.subscriptions;

import java.util.concurrent.Future;
import rx.Subscription;
import rx.functions.Action0;

/* loaded from: classes.dex */
public final class Subscriptions {
    private static final Unsubscribed UNSUBSCRIBED = new Unsubscribed();

    private Subscriptions() {
        throw new IllegalStateException("No instances!");
    }

    public static Subscription empty() {
        return BooleanSubscription.create();
    }

    public static Subscription unsubscribed() {
        return UNSUBSCRIBED;
    }

    public static Subscription create(Action0 unsubscribe) {
        return BooleanSubscription.create(unsubscribe);
    }

    public static Subscription from(Future<?> f) {
        return new FutureSubscription(f);
    }

    private static final class FutureSubscription implements Subscription {
        final Future<?> f;

        public FutureSubscription(Future<?> f) {
            this.f = f;
        }

        @Override // rx.Subscription
        public void unsubscribe() {
            this.f.cancel(true);
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return this.f.isCancelled();
        }
    }

    public static CompositeSubscription from(Subscription... subscriptions) {
        return new CompositeSubscription(subscriptions);
    }

    private static final class Unsubscribed implements Subscription {
        private Unsubscribed() {
        }

        @Override // rx.Subscription
        public void unsubscribe() {
        }

        @Override // rx.Subscription
        public boolean isUnsubscribed() {
            return true;
        }
    }
}
