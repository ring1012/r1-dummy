package rx.internal.operators;

import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.internal.producers.ProducerArbiter;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.SerialSubscription;

/* loaded from: classes.dex */
public final class OperatorOnErrorResumeNextViaFunction<T> implements Observable.Operator<T, T> {
    private final Func1<Throwable, ? extends Observable<? extends T>> resumeFunction;

    public OperatorOnErrorResumeNextViaFunction(Func1<Throwable, ? extends Observable<? extends T>> f) {
        this.resumeFunction = f;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        final ProducerArbiter producerArbiter = new ProducerArbiter();
        final SerialSubscription serialSubscription = new SerialSubscription();
        Subscriber<T> subscriber2 = new Subscriber<T>() { // from class: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.1
            private boolean done = false;

            @Override // rx.Observer
            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    subscriber.onCompleted();
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                if (this.done) {
                    Exceptions.throwIfFatal(e);
                    return;
                }
                this.done = true;
                try {
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
                    unsubscribe();
                    Subscriber<T> next = new Subscriber<T>() { // from class: rx.internal.operators.OperatorOnErrorResumeNextViaFunction.1.1
                        @Override // rx.Observer
                        public void onNext(T t) {
                            subscriber.onNext(t);
                        }

                        @Override // rx.Observer
                        public void onError(Throwable e2) {
                            subscriber.onError(e2);
                        }

                        @Override // rx.Observer
                        public void onCompleted() {
                            subscriber.onCompleted();
                        }

                        @Override // rx.Subscriber
                        public void setProducer(Producer producer) {
                            producerArbiter.setProducer(producer);
                        }
                    };
                    serialSubscription.set(next);
                    Observable<? extends T> resume = (Observable) OperatorOnErrorResumeNextViaFunction.this.resumeFunction.call(e);
                    resume.unsafeSubscribe(next);
                } catch (Throwable e2) {
                    Exceptions.throwOrReport(e2, subscriber);
                }
            }

            @Override // rx.Observer
            public void onNext(T t) {
                if (!this.done) {
                    subscriber.onNext(t);
                }
            }

            @Override // rx.Subscriber
            public void setProducer(Producer producer) {
                producerArbiter.setProducer(producer);
            }
        };
        subscriber.add(serialSubscription);
        serialSubscription.set(subscriber2);
        subscriber.setProducer(producerArbiter);
        return subscriber2;
    }
}
