package rx.internal.util;

import java.util.concurrent.CountDownLatch;
import rx.Subscription;
import rx.annotations.Experimental;

@Experimental
/* loaded from: classes.dex */
public final class BlockingUtils {
    private BlockingUtils() {
    }

    @Experimental
    public static void awaitForComplete(CountDownLatch latch, Subscription subscription) throws InterruptedException {
        if (latch.getCount() != 0) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                subscription.unsubscribe();
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for subscription to complete.", e);
            }
        }
    }
}
