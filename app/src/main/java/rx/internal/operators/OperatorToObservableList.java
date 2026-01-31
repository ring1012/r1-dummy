package rx.internal.operators;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.internal.producers.SingleDelayedProducer;

/* loaded from: classes.dex */
public final class OperatorToObservableList<T> implements Observable.Operator<List<T>, T> {

    private static final class Holder {
        static final OperatorToObservableList<Object> INSTANCE = new OperatorToObservableList<>();

        private Holder() {
        }
    }

    public static <T> OperatorToObservableList<T> instance() {
        return (OperatorToObservableList<T>) Holder.INSTANCE;
    }

    private OperatorToObservableList() {
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super List<T>> subscriber) {
        final SingleDelayedProducer singleDelayedProducer = new SingleDelayedProducer(subscriber);
        Subscriber<T> subscriber2 = new Subscriber<T>() { // from class: rx.internal.operators.OperatorToObservableList.1
            boolean completed = false;
            List<T> list = new LinkedList();

            @Override // rx.Subscriber
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            @Override // rx.Observer
            public void onCompleted() {
                if (!this.completed) {
                    this.completed = true;
                    try {
                        ArrayList arrayList = new ArrayList(this.list);
                        this.list = null;
                        singleDelayedProducer.setValue(arrayList);
                    } catch (Throwable t) {
                        Exceptions.throwOrReport(t, this);
                    }
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onNext(T value) {
                if (!this.completed) {
                    this.list.add(value);
                }
            }
        };
        subscriber.add(subscriber2);
        subscriber.setProducer(singleDelayedProducer);
        return subscriber2;
    }
}
