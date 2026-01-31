package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.internal.producers.SingleDelayedProducer;

/* loaded from: classes.dex */
public final class OperatorAll<T> implements Observable.Operator<Boolean, T> {
    private final Func1<? super T, Boolean> predicate;

    public OperatorAll(Func1<? super T, Boolean> predicate) {
        this.predicate = predicate;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super Boolean> subscriber) {
        final SingleDelayedProducer singleDelayedProducer = new SingleDelayedProducer(subscriber);
        Subscriber<T> subscriber2 = new Subscriber<T>() { // from class: rx.internal.operators.OperatorAll.1
            boolean done;

            @Override // rx.Observer
            public void onNext(T t) {
                try {
                    Boolean result = (Boolean) OperatorAll.this.predicate.call(t);
                    if (!result.booleanValue() && !this.done) {
                        this.done = true;
                        singleDelayedProducer.setValue(false);
                        unsubscribe();
                    }
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, this, t);
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    singleDelayedProducer.setValue(true);
                }
            }
        };
        subscriber.add(subscriber2);
        subscriber.setProducer(singleDelayedProducer);
        return subscriber2;
    }
}
