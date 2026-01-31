package rx.singles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import rx.Single;
import rx.SingleSubscriber;
import rx.annotations.Experimental;
import rx.internal.operators.BlockingOperatorToFuture;
import rx.internal.util.BlockingUtils;

@Experimental
/* loaded from: classes.dex */
public class BlockingSingle<T> {
    private final Single<? extends T> single;

    private BlockingSingle(Single<? extends T> single) {
        this.single = single;
    }

    @Experimental
    public static <T> BlockingSingle<T> from(Single<? extends T> single) {
        return new BlockingSingle<>(single);
    }

    /* JADX WARN: Type inference failed for: r5v2, types: [T, java.lang.Object] */
    @Experimental
    public T value() throws InterruptedException {
        final AtomicReference atomicReference = new AtomicReference();
        final AtomicReference atomicReference2 = new AtomicReference();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        BlockingUtils.awaitForComplete(countDownLatch, this.single.subscribe((SingleSubscriber<? super Object>) new SingleSubscriber<T>() { // from class: rx.singles.BlockingSingle.1
            @Override // rx.SingleSubscriber
            public void onSuccess(T value) {
                atomicReference.set(value);
                countDownLatch.countDown();
            }

            @Override // rx.SingleSubscriber
            public void onError(Throwable error) {
                atomicReference2.set(error);
                countDownLatch.countDown();
            }
        }));
        Throwable th = (Throwable) atomicReference2.get();
        if (th != null) {
            if (th instanceof RuntimeException) {
                throw ((RuntimeException) th);
            }
            throw new RuntimeException(th);
        }
        return atomicReference.get();
    }

    @Experimental
    public Future<T> toFuture() {
        return BlockingOperatorToFuture.toFuture(this.single.toObservable());
    }
}
