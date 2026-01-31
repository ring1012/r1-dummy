package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

/* loaded from: classes.dex */
public final class OperatorSkipUntil<T, U> implements Observable.Operator<T, T> {
    final Observable<U> other;

    public OperatorSkipUntil(Observable<U> other) {
        this.other = other;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        Subscriber<U> subscriber2 = new Subscriber<U>() { // from class: rx.internal.operators.OperatorSkipUntil.1
            @Override // rx.Observer
            public void onNext(U t) {
                atomicBoolean.set(true);
                unsubscribe();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                serializedSubscriber.onError(e);
                serializedSubscriber.unsubscribe();
            }

            @Override // rx.Observer
            public void onCompleted() {
                unsubscribe();
            }
        };
        subscriber.add(subscriber2);
        this.other.unsafeSubscribe(subscriber2);
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorSkipUntil.2
            @Override // rx.Observer
            public void onNext(T t) {
                if (atomicBoolean.get()) {
                    serializedSubscriber.onNext(t);
                } else {
                    request(1L);
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                serializedSubscriber.onError(e);
                unsubscribe();
            }

            @Override // rx.Observer
            public void onCompleted() {
                serializedSubscriber.onCompleted();
                unsubscribe();
            }
        };
    }
}
