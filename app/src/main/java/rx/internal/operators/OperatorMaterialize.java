package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import rx.Notification;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.plugins.RxJavaPlugins;

/* loaded from: classes.dex */
public final class OperatorMaterialize<T> implements Observable.Operator<Notification<T>, T> {

    private static final class Holder {
        static final OperatorMaterialize<Object> INSTANCE = new OperatorMaterialize<>();

        private Holder() {
        }
    }

    public static <T> OperatorMaterialize<T> instance() {
        return (OperatorMaterialize<T>) Holder.INSTANCE;
    }

    private OperatorMaterialize() {
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super Notification<T>> child) {
        final ParentSubscriber<T> parent = new ParentSubscriber<>(child);
        child.add(parent);
        child.setProducer(new Producer() { // from class: rx.internal.operators.OperatorMaterialize.1
            @Override // rx.Producer
            public void request(long n) {
                if (n > 0) {
                    parent.requestMore(n);
                }
            }
        });
        return parent;
    }

    private static class ParentSubscriber<T> extends Subscriber<T> {
        private final Subscriber<? super Notification<T>> child;
        private volatile Notification<T> terminalNotification;
        private boolean busy = false;
        private boolean missed = false;
        private final AtomicLong requested = new AtomicLong();

        ParentSubscriber(Subscriber<? super Notification<T>> child) {
            this.child = child;
        }

        @Override // rx.Subscriber
        public void onStart() {
            request(0L);
        }

        void requestMore(long n) {
            BackpressureUtils.getAndAddRequest(this.requested, n);
            request(n);
            drain();
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.terminalNotification = Notification.createOnCompleted();
            drain();
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.terminalNotification = Notification.createOnError(e);
            RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
            drain();
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.child.onNext(Notification.createOnNext(t));
            decrementRequested();
        }

        private void decrementRequested() {
            long r;
            AtomicLong localRequested = this.requested;
            do {
                r = localRequested.get();
                if (r == Long.MAX_VALUE) {
                    return;
                }
            } while (!localRequested.compareAndSet(r, r - 1));
        }

        private void drain() {
            synchronized (this) {
                if (this.busy) {
                    this.missed = true;
                    return;
                }
                AtomicLong localRequested = this.requested;
                while (!this.child.isUnsubscribed()) {
                    Notification<T> tn = this.terminalNotification;
                    if (tn != null && localRequested.get() > 0) {
                        this.terminalNotification = null;
                        this.child.onNext(tn);
                        if (!this.child.isUnsubscribed()) {
                            this.child.onCompleted();
                            return;
                        }
                        return;
                    }
                    synchronized (this) {
                        if (!this.missed) {
                            this.busy = false;
                            return;
                        }
                    }
                }
            }
        }
    }
}
