package rx.internal.operators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;

/* loaded from: classes.dex */
public final class BlockingOperatorLatest {
    private BlockingOperatorLatest() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Iterable<T> latest(final Observable<? extends T> source) {
        return new Iterable<T>() { // from class: rx.internal.operators.BlockingOperatorLatest.1
            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                LatestObserverIterator<T> lio = new LatestObserverIterator<>();
                source.materialize().subscribe((Subscriber<? super Notification<T>>) lio);
                return lio;
            }
        };
    }

    static final class LatestObserverIterator<T> extends Subscriber<Notification<? extends T>> implements Iterator<T> {
        Notification<? extends T> iNotif;
        final Semaphore notify = new Semaphore(0);
        final AtomicReference<Notification<? extends T>> value = new AtomicReference<>();

        LatestObserverIterator() {
        }

        @Override // rx.Observer
        public void onNext(Notification<? extends T> args) {
            boolean wasntAvailable = this.value.getAndSet(args) == null;
            if (wasntAvailable) {
                this.notify.release();
            }
        }

        @Override // rx.Observer
        public void onError(Throwable e) {
        }

        @Override // rx.Observer
        public void onCompleted() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() throws InterruptedException {
            if (this.iNotif != null && this.iNotif.isOnError()) {
                throw Exceptions.propagate(this.iNotif.getThrowable());
            }
            if ((this.iNotif == null || !this.iNotif.isOnCompleted()) && this.iNotif == null) {
                try {
                    this.notify.acquire();
                    Notification<? extends T> n = this.value.getAndSet(null);
                    this.iNotif = n;
                    if (this.iNotif.isOnError()) {
                        throw Exceptions.propagate(this.iNotif.getThrowable());
                    }
                } catch (InterruptedException ex) {
                    unsubscribe();
                    Thread.currentThread().interrupt();
                    this.iNotif = Notification.createOnError(ex);
                    throw Exceptions.propagate(ex);
                }
            }
            return !this.iNotif.isOnCompleted();
        }

        @Override // java.util.Iterator
        public T next() {
            if (hasNext() && this.iNotif.isOnNext()) {
                T v = this.iNotif.getValue();
                this.iNotif = null;
                return v;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Read-only iterator.");
        }
    }
}
