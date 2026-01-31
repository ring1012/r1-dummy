package rx.internal.operators;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/* loaded from: classes.dex */
public final class OnSubscribeToObservableFuture {
    private OnSubscribeToObservableFuture() {
        throw new IllegalStateException("No instances!");
    }

    static class ToObservableFuture<T> implements Observable.OnSubscribe<T> {
        private final Future<? extends T> that;
        private final long time;
        private final TimeUnit unit;

        public ToObservableFuture(Future<? extends T> that) {
            this.that = that;
            this.time = 0L;
            this.unit = null;
        }

        public ToObservableFuture(Future<? extends T> that, long time, TimeUnit unit) {
            this.that = that;
            this.time = time;
            this.unit = unit;
        }

        @Override // rx.functions.Action1
        public void call(Subscriber<? super T> subscriber) {
            subscriber.add(Subscriptions.create(new Action0() { // from class: rx.internal.operators.OnSubscribeToObservableFuture.ToObservableFuture.1
                @Override // rx.functions.Action0
                public void call() {
                    ToObservableFuture.this.that.cancel(true);
                }
            }));
            try {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(this.unit == null ? this.that.get() : this.that.get(this.time, this.unit));
                    subscriber.onCompleted();
                }
            } catch (Throwable th) {
                if (!subscriber.isUnsubscribed()) {
                    Exceptions.throwOrReport(th, subscriber);
                }
            }
        }
    }

    public static <T> Observable.OnSubscribe<T> toObservableFuture(Future<? extends T> that) {
        return new ToObservableFuture(that);
    }

    public static <T> Observable.OnSubscribe<T> toObservableFuture(Future<? extends T> that, long time, TimeUnit unit) {
        return new ToObservableFuture(that, time, unit);
    }
}
