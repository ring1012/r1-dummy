package rx.android;

import android.os.Looper;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/* loaded from: classes.dex */
public abstract class MainThreadSubscription implements Subscription {
    private final AtomicBoolean unsubscribed = new AtomicBoolean();

    protected abstract void onUnsubscribe();

    public static void verifyMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("Expected to be called on the main thread but was " + Thread.currentThread().getName());
        }
    }

    @Override // rx.Subscription
    public final boolean isUnsubscribed() {
        return this.unsubscribed.get();
    }

    @Override // rx.Subscription
    public final void unsubscribe() {
        if (this.unsubscribed.compareAndSet(false, true)) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                onUnsubscribe();
            } else {
                AndroidSchedulers.mainThread().createWorker().schedule(new Action0() { // from class: rx.android.MainThreadSubscription.1
                    @Override // rx.functions.Action0
                    public void call() {
                        MainThreadSubscription.this.onUnsubscribe();
                    }
                });
            }
        }
    }
}
