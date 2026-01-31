package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

/* loaded from: classes.dex */
public final class BlockingOperatorNext {
    private BlockingOperatorNext() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> next(final Observable<? extends T> items) {
        return new Iterable<T>() { // from class: rx.internal.operators.BlockingOperatorNext.1
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                NextObserver<T> nextObserver = new NextObserver<>();
                return new NextIterator(items, nextObserver);
            }
        };
    }

    static final class NextIterator<T> implements Iterator<T> {
        private Throwable error;
        private boolean hasNext;
        private boolean isNextConsumed;
        private final Observable<? extends T> items;
        private T next;
        private final NextObserver<T> observer;
        private boolean started;

        private NextIterator(Observable<? extends T> items, NextObserver<T> observer) {
            this.hasNext = true;
            this.isNextConsumed = true;
            this.error = null;
            this.started = false;
            this.items = items;
            this.observer = observer;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.error != null) {
                throw Exceptions.propagate(this.error);
            }
            if (!this.hasNext) {
                return false;
            }
            if (!this.isNextConsumed) {
                return true;
            }
            return moveToNext();
        }

        private boolean moveToNext() {
            try {
                if (!this.started) {
                    this.started = true;
                    this.observer.setWaiting(1);
                    this.items.materialize().subscribe((Subscriber<? super Notification<? extends T>>) this.observer);
                }
                Notification<? extends T> nextNotification = this.observer.takeNext();
                if (nextNotification.isOnNext()) {
                    this.isNextConsumed = false;
                    this.next = nextNotification.getValue();
                    return true;
                }
                this.hasNext = false;
                if (nextNotification.isOnCompleted()) {
                    return false;
                }
                if (nextNotification.isOnError()) {
                    this.error = nextNotification.getThrowable();
                    throw Exceptions.propagate(this.error);
                }
                throw new IllegalStateException("Should not reach here");
            } catch (InterruptedException e) {
                this.observer.unsubscribe();
                Thread.currentThread().interrupt();
                this.error = e;
                throw Exceptions.propagate(this.error);
            }
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.error != null) {
                throw Exceptions.propagate(this.error);
            }
            if (hasNext()) {
                this.isNextConsumed = true;
                return this.next;
            }
            throw new NoSuchElementException("No more elements");
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Read only iterator");
        }
    }

    private static class NextObserver<T> extends Subscriber<Notification<? extends T>> {
        private final BlockingQueue<Notification<? extends T>> buf;
        final AtomicInteger waiting;

        private NextObserver() {
            this.buf = new ArrayBlockingQueue(1);
            this.waiting = new AtomicInteger();
        }

        @Override // rx.Observer
        public void onCompleted() {
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
        }

        @Override // rx.Observer
        public void onNext(Notification<? extends T> args) {
            if (this.waiting.getAndSet(0) == 1 || !args.isOnNext()) {
                Notification<? extends T> toOffer = args;
                while (!this.buf.offer(toOffer)) {
                    Notification<? extends T> concurrentItem = this.buf.poll();
                    if (concurrentItem != null && !concurrentItem.isOnNext()) {
                        toOffer = concurrentItem;
                    }
                }
            }
        }

        public Notification<? extends T> takeNext() throws InterruptedException {
            setWaiting(1);
            return this.buf.take();
        }

        void setWaiting(int value) {
            this.waiting.set(value);
        }
    }
}
