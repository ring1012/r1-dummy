package rx.internal.operators;

import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;
import rx.Producer;
import rx.Subscriber;

/* loaded from: classes.dex */
public final class OperatorElementAt<T> implements Observable.Operator<T, T> {
    private final T defaultValue;
    private final boolean hasDefault;
    private final int index;

    public OperatorElementAt(int index) {
        this(index, null, false);
    }

    public OperatorElementAt(int index, T defaultValue) {
        this(index, defaultValue, true);
    }

    private OperatorElementAt(int index, T defaultValue, boolean hasDefault) {
        if (index < 0) {
            throw new IndexOutOfBoundsException(index + " is out of bounds");
        }
        this.index = index;
        this.defaultValue = defaultValue;
        this.hasDefault = hasDefault;
    }

    @Override // rx.functions.Func1
    public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
        Subscriber<T> subscriber2 = new Subscriber<T>() { // from class: rx.internal.operators.OperatorElementAt.1
            private int currentIndex = 0;

            @Override // rx.Observer
            public void onNext(T value) {
                int i = this.currentIndex;
                this.currentIndex = i + 1;
                if (i == OperatorElementAt.this.index) {
                    subscriber.onNext(value);
                    subscriber.onCompleted();
                    unsubscribe();
                }
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // rx.Observer
            public void onCompleted() {
                if (this.currentIndex <= OperatorElementAt.this.index) {
                    if (OperatorElementAt.this.hasDefault) {
                        subscriber.onNext(OperatorElementAt.this.defaultValue);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new IndexOutOfBoundsException(OperatorElementAt.this.index + " is out of bounds"));
                    }
                }
            }

            @Override // rx.Subscriber
            public void setProducer(Producer p) {
                subscriber.setProducer(new InnerProducer(p));
            }
        };
        subscriber.add(subscriber2);
        return subscriber2;
    }

    static class InnerProducer extends AtomicBoolean implements Producer {
        private static final long serialVersionUID = 1;
        final Producer actual;

        public InnerProducer(Producer actual) {
            this.actual = actual;
        }

        @Override // rx.Producer
        public void request(long n) {
            if (n < 0) {
                throw new IllegalArgumentException("n >= 0 required");
            }
            if (n > 0 && compareAndSet(false, true)) {
                this.actual.request(Long.MAX_VALUE);
            }
        }
    }
}
