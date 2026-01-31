package rx.internal.util;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.internal.schedulers.EventLoopsScheduler;

/* loaded from: classes.dex */
public final class ScalarSynchronousObservable<T> extends Observable<T> {
    private final T t;

    public static final <T> ScalarSynchronousObservable<T> create(T t) {
        return new ScalarSynchronousObservable<>(t);
    }

    protected ScalarSynchronousObservable(final T t) {
        super(new Observable.OnSubscribe<T>() { // from class: rx.internal.util.ScalarSynchronousObservable.1
            @Override // rx.functions.Action1
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onNext((Object) t);
                subscriber.onCompleted();
            }
        });
        this.t = t;
    }

    public T get() {
        return this.t;
    }

    public Observable<T> scalarScheduleOn(Scheduler scheduler) {
        if (!(scheduler instanceof EventLoopsScheduler)) {
            return create((Observable.OnSubscribe) new NormalScheduledEmission(scheduler, this.t));
        }
        EventLoopsScheduler es = (EventLoopsScheduler) scheduler;
        return create((Observable.OnSubscribe) new DirectScheduledEmission(es, this.t));
    }

    static final class DirectScheduledEmission<T> implements Observable.OnSubscribe<T> {
        private final EventLoopsScheduler es;
        private final T value;

        DirectScheduledEmission(EventLoopsScheduler es, T value) {
            this.es = es;
            this.value = value;
        }

        @Override // rx.functions.Action1
        public void call(Subscriber<? super T> child) {
            child.add(this.es.scheduleDirect(new ScalarSynchronousAction(child, this.value)));
        }
    }

    static final class NormalScheduledEmission<T> implements Observable.OnSubscribe<T> {
        private final Scheduler scheduler;
        private final T value;

        NormalScheduledEmission(Scheduler scheduler, T value) {
            this.scheduler = scheduler;
            this.value = value;
        }

        @Override // rx.functions.Action1
        public void call(Subscriber<? super T> subscriber) {
            Scheduler.Worker worker = this.scheduler.createWorker();
            subscriber.add(worker);
            worker.schedule(new ScalarSynchronousAction(subscriber, this.value));
        }
    }

    static final class ScalarSynchronousAction<T> implements Action0 {
        private final Subscriber<? super T> subscriber;
        private final T value;

        private ScalarSynchronousAction(Subscriber<? super T> subscriber, T value) {
            this.subscriber = subscriber;
            this.value = value;
        }

        @Override // rx.functions.Action0
        public void call() {
            try {
                this.subscriber.onNext(this.value);
                this.subscriber.onCompleted();
            } catch (Throwable th) {
                this.subscriber.onError(th);
            }
        }
    }

    public <R> Observable<R> scalarFlatMap(final Func1<? super T, ? extends Observable<? extends R>> func) {
        return create((Observable.OnSubscribe) new Observable.OnSubscribe<R>() { // from class: rx.internal.util.ScalarSynchronousObservable.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // rx.functions.Action1
            public void call(final Subscriber<? super R> subscriber) {
                Observable observable = (Observable) func.call(ScalarSynchronousObservable.this.t);
                if (observable.getClass() == ScalarSynchronousObservable.class) {
                    subscriber.onNext((Object) ((ScalarSynchronousObservable) observable).t);
                    subscriber.onCompleted();
                } else {
                    observable.unsafeSubscribe(new Subscriber<R>(subscriber) { // from class: rx.internal.util.ScalarSynchronousObservable.2.1
                        @Override // rx.Observer
                        public void onNext(R v) {
                            subscriber.onNext(v);
                        }

                        @Override // rx.Observer
                        public void onError(Throwable e) {
                            subscriber.onError(e);
                        }

                        @Override // rx.Observer
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }
                    });
                }
            }
        });
    }
}
