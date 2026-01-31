package rx.internal.operators;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Timestamped;

/* loaded from: classes.dex */
public final class OperatorTimestamp<T> implements Observable.Operator<Timestamped<T>, T> {
    private final Scheduler scheduler;

    public OperatorTimestamp(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super Timestamped<T>> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorTimestamp.1
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
                subscriber.onNext(new Timestamped(OperatorTimestamp.this.scheduler.now(), t));
            }
        };
    }
}
