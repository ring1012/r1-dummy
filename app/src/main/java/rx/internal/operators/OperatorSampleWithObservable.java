package rx.internal.operators;

import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Subscriber;
import rx.observers.SerializedSubscriber;

/* loaded from: classes.dex */
public final class OperatorSampleWithObservable<T, U> implements Observable.Operator<T, T> {
    static final Object EMPTY_TOKEN = new Object();
    final Observable<U> sampler;

    public OperatorSampleWithObservable(Observable<U> sampler) {
        this.sampler = sampler;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        final SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        final AtomicReference atomicReference = new AtomicReference(EMPTY_TOKEN);
        Subscriber<U> subscriber2 = new Subscriber<U>(subscriber) { // from class: rx.internal.operators.OperatorSampleWithObservable.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // rx.Observer
            public void onNext(U t) {
                Object localValue = atomicReference.getAndSet(OperatorSampleWithObservable.EMPTY_TOKEN);
                if (localValue != OperatorSampleWithObservable.EMPTY_TOKEN) {
                    serializedSubscriber.onNext(localValue);
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
        Subscriber<T> subscriber3 = new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorSampleWithObservable.2
            @Override // rx.Observer
            public void onNext(T t) {
                atomicReference.set(t);
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
        this.sampler.unsafeSubscribe(subscriber2);
        return subscriber3;
    }
}
