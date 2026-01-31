package rx.internal.operators;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public class OperatorUnsubscribeOn<T> implements Observable.Operator<T, T> {
    private final Scheduler scheduler;

    public OperatorUnsubscribeOn(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        final Subscriber<T> subscriber2 = new Subscriber<T>() { // from class: rx.internal.operators.OperatorUnsubscribeOn.1
            @Override // rx.Observer
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onNext(T t) {
                subscriber.onNext(t);
            }
        };
        subscriber.add(Subscriptions.create(new Action0() { // from class: rx.internal.operators.OperatorUnsubscribeOn.2
            @Override // rx.functions.Action0
            public void call() {
                final Scheduler.Worker inner = OperatorUnsubscribeOn.this.scheduler.createWorker();
                inner.schedule(new Action0() { // from class: rx.internal.operators.OperatorUnsubscribeOn.2.1
                    @Override // rx.functions.Action0
                    public void call() throws Throwable {
                        subscriber2.unsubscribe();
                        inner.unsubscribe();
                    }
                });
            }
        }));
        return subscriber2;
    }
}
