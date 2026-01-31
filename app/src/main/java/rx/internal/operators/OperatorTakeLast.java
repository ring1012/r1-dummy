package rx.internal.operators;

import java.util.ArrayDeque;
import rx.Observable;
import rx.Subscriber;

/* loaded from: classes.dex */
public final class OperatorTakeLast<T> implements Observable.Operator<T, T> {
    private final int count;

    public OperatorTakeLast(int count) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count cannot be negative");
        }
        this.count = count;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        final ArrayDeque arrayDeque = new ArrayDeque();
        final NotificationLite notificationLiteInstance = NotificationLite.instance();
        final TakeLastQueueProducer takeLastQueueProducer = new TakeLastQueueProducer(notificationLiteInstance, arrayDeque, subscriber);
        subscriber.setProducer(takeLastQueueProducer);
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorTakeLast.1
            @Override // rx.Subscriber
            public void onStart() {
                request(Long.MAX_VALUE);
            }

            @Override // rx.Observer
            public void onCompleted() {
                arrayDeque.offer(notificationLiteInstance.completed());
                takeLastQueueProducer.startEmitting();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                arrayDeque.clear();
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onNext(T value) {
                if (OperatorTakeLast.this.count != 0) {
                    if (arrayDeque.size() == OperatorTakeLast.this.count) {
                        arrayDeque.removeFirst();
                    }
                    arrayDeque.offerLast(notificationLiteInstance.next(value));
                }
            }
        };
    }
}
