package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;

/* loaded from: classes.dex */
public final class OperatorSkipTimed<T> implements Observable.Operator<T, T> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    public OperatorSkipTimed(long time, TimeUnit unit, Scheduler scheduler) {
        this.time = time;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        Scheduler.Worker workerCreateWorker = this.scheduler.createWorker();
        subscriber.add(workerCreateWorker);
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        workerCreateWorker.schedule(new Action0() { // from class: rx.internal.operators.OperatorSkipTimed.1
            @Override // rx.functions.Action0
            public void call() {
                atomicBoolean.set(true);
            }
        }, this.time, this.unit);
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorSkipTimed.2
            @Override // rx.Observer
            public void onNext(T t) {
                if (atomicBoolean.get()) {
                    subscriber.onNext(t);
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                try {
                    subscriber.onError(e);
                } finally {
                    unsubscribe();
                }
            }

            @Override // rx.Observer
            public void onCompleted() {
                try {
                    subscriber.onCompleted();
                } finally {
                    unsubscribe();
                }
            }
        };
    }
}
