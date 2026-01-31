package rx.internal.operators;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.observers.SerializedSubscriber;

/* loaded from: classes.dex */
public final class OperatorSampleWithTime<T> implements Observable.Operator<T, T> {
    final Scheduler scheduler;
    final long time;
    final TimeUnit unit;

    public OperatorSampleWithTime(long time, TimeUnit unit, Scheduler scheduler) {
        this.time = time;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> child) {
        SerializedSubscriber<T> s = new SerializedSubscriber<>(child);
        Scheduler.Worker worker = this.scheduler.createWorker();
        child.add(worker);
        SamplerSubscriber<T> sampler = new SamplerSubscriber<>(s);
        child.add(sampler);
        worker.schedulePeriodically(sampler, this.time, this.time, this.unit);
        return sampler;
    }

    static final class SamplerSubscriber<T> extends Subscriber<T> implements Action0 {
        private static final Object EMPTY_TOKEN = new Object();
        private final Subscriber<? super T> subscriber;
        final AtomicReference<Object> value = new AtomicReference<>(EMPTY_TOKEN);

        public SamplerSubscriber(Subscriber<? super T> subscriber) {
            this.subscriber = subscriber;
        }

        @Override // rx.Subscriber
        public void onStart() {
            request(Long.MAX_VALUE);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.value.set(t);
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.subscriber.onError(e);
            unsubscribe();
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.subscriber.onCompleted();
            unsubscribe();
        }

        @Override // rx.functions.Action0
        public void call() {
            Object localValue = this.value.getAndSet(EMPTY_TOKEN);
            if (localValue != EMPTY_TOKEN) {
                try {
                    this.subscriber.onNext(localValue);
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, this);
                }
            }
        }
    }
}
