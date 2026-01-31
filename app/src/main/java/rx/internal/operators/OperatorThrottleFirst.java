package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;

/* loaded from: classes.dex */
public final class OperatorThrottleFirst<T> implements Observable.Operator<T, T> {
    private final Scheduler scheduler;
    private final long timeInMilliseconds;

    public OperatorThrottleFirst(long windowDuration, TimeUnit unit, Scheduler scheduler) {
        this.timeInMilliseconds = unit.toMillis(windowDuration);
        this.scheduler = scheduler;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorThrottleFirst.1
            private long lastOnNext = 0;

            @Override // rx.Subscriber
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            @Override // rx.Observer
            public void onNext(T v) {
                long now = OperatorThrottleFirst.this.scheduler.now();
                if (this.lastOnNext == 0 || now - this.lastOnNext >= OperatorThrottleFirst.this.timeInMilliseconds) {
                    this.lastOnNext = now;
                    subscriber.onNext(v);
                }
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
