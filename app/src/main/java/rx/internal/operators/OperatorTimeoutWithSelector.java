package rx.internal.operators;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.internal.operators.OperatorTimeoutBase;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public class OperatorTimeoutWithSelector<T, U, V> extends OperatorTimeoutBase<T> {
    @Override // rx.internal.operators.OperatorTimeoutBase
    public /* bridge */ /* synthetic */ Subscriber call(Subscriber x0) {
        return super.call(x0);
    }

    public OperatorTimeoutWithSelector(final Func0<? extends Observable<U>> firstTimeoutSelector, final Func1<? super T, ? extends Observable<V>> timeoutSelector, Observable<? extends T> other) {
        super(new OperatorTimeoutBase.FirstTimeoutStub<T>() { // from class: rx.internal.operators.OperatorTimeoutWithSelector.1
            @Override // rx.functions.Func3
            public Subscription call(final OperatorTimeoutBase.TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, Scheduler.Worker inner) {
                if (firstTimeoutSelector != null) {
                    try {
                        Observable<U> o = (Observable) firstTimeoutSelector.call();
                        return o.unsafeSubscribe(new Subscriber<U>() { // from class: rx.internal.operators.OperatorTimeoutWithSelector.1.1
                            @Override // rx.Observer
                            public void onCompleted() {
                                timeoutSubscriber.onTimeout(seqId.longValue());
                            }

                            @Override // rx.Observer
                            public void onError(Throwable e) {
                                timeoutSubscriber.onError(e);
                            }

                            @Override // rx.Observer
                            public void onNext(U t) {
                                timeoutSubscriber.onTimeout(seqId.longValue());
                            }
                        });
                    } catch (Throwable t) {
                        Exceptions.throwOrReport(t, timeoutSubscriber);
                        return Subscriptions.unsubscribed();
                    }
                }
                return Subscriptions.unsubscribed();
            }
        }, new OperatorTimeoutBase.TimeoutStub<T>() { // from class: rx.internal.operators.OperatorTimeoutWithSelector.2
            @Override // rx.functions.Func4
            public /* bridge */ /* synthetic */ Subscription call(Object x0, Long l, Object x2, Scheduler.Worker worker) {
                return call((OperatorTimeoutBase.TimeoutSubscriber<Long>) x0, l, (Long) x2, worker);
            }

            public Subscription call(final OperatorTimeoutBase.TimeoutSubscriber<T> timeoutSubscriber, final Long seqId, T value, Scheduler.Worker inner) {
                try {
                    Observable<V> o = (Observable) timeoutSelector.call(value);
                    return o.unsafeSubscribe(new Subscriber<V>() { // from class: rx.internal.operators.OperatorTimeoutWithSelector.2.1
                        @Override // rx.Observer
                        public void onCompleted() {
                            timeoutSubscriber.onTimeout(seqId.longValue());
                        }

                        @Override // rx.Observer
                        public void onError(Throwable e) {
                            timeoutSubscriber.onError(e);
                        }

                        @Override // rx.Observer
                        public void onNext(V t) {
                            timeoutSubscriber.onTimeout(seqId.longValue());
                        }
                    });
                } catch (Throwable t) {
                    Exceptions.throwOrReport(t, timeoutSubscriber);
                    return Subscriptions.unsubscribed();
                }
            }
        }, other, Schedulers.immediate());
    }
}
