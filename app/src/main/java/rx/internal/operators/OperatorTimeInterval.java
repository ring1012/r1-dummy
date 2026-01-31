package rx.internal.operators;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.TimeInterval;

/* loaded from: classes.dex */
public final class OperatorTimeInterval<T> implements Observable.Operator<TimeInterval<T>, T> {
    private final Scheduler scheduler;

    public OperatorTimeInterval(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super TimeInterval<T>> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorTimeInterval.1
            private long lastTimestamp;

            {
                this.lastTimestamp = OperatorTimeInterval.this.scheduler.now();
            }

            @Override // rx.Observer
            public void onNext(T args) {
                long nowTimestamp = OperatorTimeInterval.this.scheduler.now();
                subscriber.onNext(new TimeInterval(nowTimestamp - this.lastTimestamp, args));
                this.lastTimestamp = nowTimestamp;
            }

            @Override // rx.Observer
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }
        };
    }
}
