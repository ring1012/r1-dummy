package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public final class OnSubscribeRefCount<T> implements Observable.OnSubscribe<T> {
    private final ConnectableObservable<? extends T> source;
    private volatile CompositeSubscription baseSubscription = new CompositeSubscription();
    private final AtomicInteger subscriptionCount = new AtomicInteger(0);
    private final ReentrantLock lock = new ReentrantLock();

    public OnSubscribeRefCount(ConnectableObservable<? extends T> source) {
        this.source = source;
    }

    @Override // rx.functions.Action1
    public void call(Subscriber<? super T> subscriber) {
        boolean z;
        this.lock.lock();
        if (this.subscriptionCount.incrementAndGet() == 1) {
            AtomicBoolean writeLocked = new AtomicBoolean(true);
            try {
                this.source.connect(onSubscribe(subscriber, writeLocked));
                if (z) {
                    return;
                } else {
                    return;
                }
            } finally {
                if (writeLocked.get()) {
                }
            }
        }
        try {
            doSubscribe(subscriber, this.baseSubscription);
        } finally {
            this.lock.unlock();
        }
    }

    private Action1<Subscription> onSubscribe(final Subscriber<? super T> subscriber, final AtomicBoolean writeLocked) {
        return new Action1<Subscription>() { // from class: rx.internal.operators.OnSubscribeRefCount.1
            @Override // rx.functions.Action1
            public void call(Subscription subscription) {
                try {
                    OnSubscribeRefCount.this.baseSubscription.add(subscription);
                    OnSubscribeRefCount.this.doSubscribe(subscriber, OnSubscribeRefCount.this.baseSubscription);
                } finally {
                    OnSubscribeRefCount.this.lock.unlock();
                    writeLocked.set(false);
                }
            }
        };
    }

    void doSubscribe(final Subscriber<? super T> subscriber, final CompositeSubscription compositeSubscription) {
        subscriber.add(disconnect(compositeSubscription));
        this.source.unsafeSubscribe(new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OnSubscribeRefCount.2
            @Override // rx.Observer
            public void onError(Throwable e) {
                cleanup();
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onNext(T t) {
                subscriber.onNext(t);
            }

            @Override // rx.Observer
            public void onCompleted() {
                cleanup();
                subscriber.onCompleted();
            }

            void cleanup() {
                OnSubscribeRefCount.this.lock.lock();
                try {
                    if (OnSubscribeRefCount.this.baseSubscription == compositeSubscription) {
                        OnSubscribeRefCount.this.baseSubscription.unsubscribe();
                        OnSubscribeRefCount.this.baseSubscription = new CompositeSubscription();
                        OnSubscribeRefCount.this.subscriptionCount.set(0);
                    }
                } finally {
                    OnSubscribeRefCount.this.lock.unlock();
                }
            }
        });
    }

    private Subscription disconnect(final CompositeSubscription current) {
        return Subscriptions.create(new Action0() { // from class: rx.internal.operators.OnSubscribeRefCount.3
            @Override // rx.functions.Action0
            public void call() {
                OnSubscribeRefCount.this.lock.lock();
                try {
                    if (OnSubscribeRefCount.this.baseSubscription == current && OnSubscribeRefCount.this.subscriptionCount.decrementAndGet() == 0) {
                        OnSubscribeRefCount.this.baseSubscription.unsubscribe();
                        OnSubscribeRefCount.this.baseSubscription = new CompositeSubscription();
                    }
                } finally {
                    OnSubscribeRefCount.this.lock.unlock();
                }
            }
        });
    }
}
