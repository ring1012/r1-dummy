package rx.internal.operators;

import rx.Observable;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

/* loaded from: classes.dex */
public final class OperatorTakeUntil<T, E> implements Observable.Operator<T, T> {
    private final Observable<? extends E> other;

    public OperatorTakeUntil(Observable<? extends E> other) {
        this.other = other;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber, false);
        final Subscriber<T> subscriber2 = new Subscriber<T>(serializedSubscriber, false) { // from class: rx.internal.operators.OperatorTakeUntil.1
            @Override // rx.Observer
            public void onNext(T t) {
                serializedSubscriber.onNext(t);
            }

            @Override // rx.Observer
            public void onError(Throwable e) throws Throwable {
                try {
                    serializedSubscriber.onError(e);
                } finally {
                    serializedSubscriber.unsubscribe();
                }
            }

            @Override // rx.Observer
            public void onCompleted() throws Throwable {
                try {
                    serializedSubscriber.onCompleted();
                } finally {
                    serializedSubscriber.unsubscribe();
                }
            }
        };
        Subscriber<E> subscriber3 = new Subscriber<E>() { // from class: rx.internal.operators.OperatorTakeUntil.2
            @Override // rx.Subscriber
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            @Override // rx.Observer
            public void onCompleted() {
                subscriber2.onCompleted();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber2.onError(e);
            }

            @Override // rx.Observer
            public void onNext(E t) {
                onCompleted();
            }
        };
        serializedSubscriber.add(subscriber2);
        serializedSubscriber.add(subscriber3);
        subscriber.add(serializedSubscriber);
        this.other.unsafeSubscribe(subscriber3);
        return subscriber2;
    }
}
