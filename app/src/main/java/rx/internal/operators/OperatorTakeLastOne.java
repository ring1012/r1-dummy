package rx.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;

/* loaded from: classes.dex */
public class OperatorTakeLastOne<T> implements Observable.Operator<T, T> {

    private static class Holder {
        static final OperatorTakeLastOne<Object> INSTANCE = new OperatorTakeLastOne<>();

        private Holder() {
        }
    }

    public static <T> OperatorTakeLastOne<T> instance() {
        return (OperatorTakeLastOne<T>) Holder.INSTANCE;
    }

    private OperatorTakeLastOne() {
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(Subscriber<? super T> child) {
        final ParentSubscriber<T> parent = new ParentSubscriber<>(child);
        child.setProducer(new Producer() { // from class: rx.internal.operators.OperatorTakeLastOne.1
            @Override // rx.Producer
            public void request(long n) {
                parent.requestMore(n);
            }
        });
        child.add(parent);
        return parent;
    }

    private static class ParentSubscriber<T> extends Subscriber<T> {
        private static final Object ABSENT = new Object();
        private static final int NOT_REQUESTED_COMPLETED = 1;
        private static final int NOT_REQUESTED_NOT_COMPLETED = 0;
        private static final int REQUESTED_COMPLETED = 3;
        private static final int REQUESTED_NOT_COMPLETED = 2;
        private final Subscriber<? super T> child;
        private T last = (T) ABSENT;
        private final AtomicInteger state = new AtomicInteger(0);

        ParentSubscriber(Subscriber<? super T> subscriber) {
            this.child = subscriber;
        }

        void requestMore(long n) {
            if (n <= 0) {
                return;
            }
            while (true) {
                int s = this.state.get();
                if (s == 0) {
                    if (this.state.compareAndSet(0, 2)) {
                        return;
                    }
                } else if (s == 1) {
                    if (this.state.compareAndSet(1, 3)) {
                        emit();
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        @Override // rx.Observer
        public void onCompleted() {
            if (this.last == ABSENT) {
                this.child.onCompleted();
                return;
            }
            while (true) {
                int s = this.state.get();
                if (s == 0) {
                    if (this.state.compareAndSet(0, 1)) {
                        return;
                    }
                } else if (s == 2) {
                    if (this.state.compareAndSet(2, 3)) {
                        emit();
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        private void emit() {
            if (isUnsubscribed()) {
                this.last = null;
                return;
            }
            T t = this.last;
            this.last = null;
            if (t != ABSENT) {
                try {
                    this.child.onNext(t);
                } catch (Throwable e) {
                    Exceptions.throwOrReport(e, this.child);
                    return;
                }
            }
            if (!isUnsubscribed()) {
                this.child.onCompleted();
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.child.onError(e);
        }

        @Override // rx.Observer
        public void onNext(T t) {
            this.last = t;
        }
    }
}
