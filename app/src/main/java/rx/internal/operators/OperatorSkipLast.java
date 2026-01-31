package rx.internal.operators;

import java.util.ArrayDeque;
import java.util.Deque;
import rx.Observable;
import rx.Subscriber;

/* loaded from: classes.dex */
public class OperatorSkipLast<T> implements Observable.Operator<T, T> {
    private final int count;

    public OperatorSkipLast(int count) {
        if (count < 0) {
            throw new IndexOutOfBoundsException("count could not be negative");
        }
        this.count = count;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        return new Subscriber<T>(subscriber) { // from class: rx.internal.operators.OperatorSkipLast.1
            private final NotificationLite<T> on = NotificationLite.instance();
            private final Deque<Object> deque = new ArrayDeque();

            @Override // rx.Observer
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override // rx.Observer
            public void onNext(T value) {
                if (OperatorSkipLast.this.count != 0) {
                    if (this.deque.size() == OperatorSkipLast.this.count) {
                        subscriber.onNext(this.on.getValue(this.deque.removeFirst()));
                    } else {
                        request(1L);
                    }
                    this.deque.offerLast(this.on.next(value));
                    return;
                }
                subscriber.onNext(value);
            }
        };
    }
}
