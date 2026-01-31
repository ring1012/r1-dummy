package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

/* loaded from: classes.dex */
public final class BlockingOperatorMostRecent {
    private BlockingOperatorMostRecent() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> mostRecent(final Observable<? extends T> source, final T initialValue) {
        return new Iterable<T>() { // from class: rx.internal.operators.BlockingOperatorMostRecent.1
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                MostRecentObserver<T> mostRecentObserver = new MostRecentObserver<>(initialValue);
                source.subscribe((Subscriber) mostRecentObserver);
                return mostRecentObserver.getIterable();
            }
        };
    }

    private static final class MostRecentObserver<T> extends Subscriber<T> {
        final NotificationLite<T> nl;
        volatile Object value;

        private MostRecentObserver(T value) {
            this.nl = NotificationLite.instance();
            this.value = this.nl.next(value);
        }

        @Override // rx.Observer
        public void onCompleted() {
            this.value = this.nl.completed();
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
            this.value = this.nl.error(e);
        }

        @Override // rx.Observer
        public void onNext(T args) {
            this.value = this.nl.next(args);
        }

        public Iterator<T> getIterable() {
            return new Iterator<T>() { // from class: rx.internal.operators.BlockingOperatorMostRecent.MostRecentObserver.1
                private Object buf = null;

                @Override // java.util.Iterator
                public boolean hasNext() {
                    this.buf = MostRecentObserver.this.value;
                    return !MostRecentObserver.this.nl.isCompleted(this.buf);
                }

                @Override // java.util.Iterator
                public T next() {
                    try {
                        if (this.buf == null) {
                            this.buf = MostRecentObserver.this.value;
                        }
                        if (MostRecentObserver.this.nl.isCompleted(this.buf)) {
                            throw new NoSuchElementException();
                        }
                        if (MostRecentObserver.this.nl.isError(this.buf)) {
                            throw Exceptions.propagate(MostRecentObserver.this.nl.getError(this.buf));
                        }
                        return MostRecentObserver.this.nl.getValue(this.buf);
                    } finally {
                        this.buf = null;
                    }
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException("Read only iterator");
                }
            };
        }
    }
}
