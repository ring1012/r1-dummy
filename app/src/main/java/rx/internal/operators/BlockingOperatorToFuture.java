package rx.internal.operators;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/* loaded from: classes.dex */
public final class BlockingOperatorToFuture {
    private BlockingOperatorToFuture() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Future<T> toFuture(Observable<? extends T> observable) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final AtomicReference atomicReference = new AtomicReference();
        final AtomicReference atomicReference2 = new AtomicReference();
        final Subscription subscriptionSubscribe = observable.single().subscribe((Subscriber<? super Object>) new Subscriber<T>() { // from class: rx.internal.operators.BlockingOperatorToFuture.1
            @Override // rx.Observer
            public void onCompleted() {
                countDownLatch.countDown();
            }

            @Override // rx.Observer
            public void onError(Throwable e) {
                atomicReference2.compareAndSet(null, e);
                countDownLatch.countDown();
            }

            @Override // rx.Observer
            public void onNext(T v) {
                atomicReference.set(v);
            }
        });
        return new Future<T>() { // from class: rx.internal.operators.BlockingOperatorToFuture.2
            private volatile boolean cancelled = false;

            @Override // java.util.concurrent.Future
            public boolean cancel(boolean mayInterruptIfRunning) {
                if (countDownLatch.getCount() <= 0) {
                    return false;
                }
                this.cancelled = true;
                subscriptionSubscribe.unsubscribe();
                countDownLatch.countDown();
                return true;
            }

            @Override // java.util.concurrent.Future
            public boolean isCancelled() {
                return this.cancelled;
            }

            @Override // java.util.concurrent.Future
            public boolean isDone() {
                return countDownLatch.getCount() == 0;
            }

            @Override // java.util.concurrent.Future
            public T get() throws ExecutionException, InterruptedException {
                countDownLatch.await();
                return getValue();
            }

            @Override // java.util.concurrent.Future
            public T get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                if (countDownLatch.await(timeout, unit)) {
                    return getValue();
                }
                throw new TimeoutException("Timed out after " + unit.toMillis(timeout) + "ms waiting for underlying Observable.");
            }

            private T getValue() throws ExecutionException {
                Throwable th = (Throwable) atomicReference2.get();
                if (th != null) {
                    throw new ExecutionException("Observable onError", th);
                }
                if (this.cancelled) {
                    throw new CancellationException("Subscription unsubscribed");
                }
                return (T) atomicReference.get();
            }
        };
    }
}
