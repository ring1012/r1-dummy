package rx.internal.operators;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/* loaded from: classes.dex */
public final class BackpressureUtils {
    private BackpressureUtils() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> long getAndAddRequest(AtomicLongFieldUpdater<T> requested, T object, long n) {
        long current;
        long next;
        do {
            current = requested.get(object);
            next = addCap(current, n);
        } while (!requested.compareAndSet(object, current, next));
        return current;
    }

    public static long getAndAddRequest(AtomicLong requested, long n) {
        long current;
        long next;
        do {
            current = requested.get();
            next = addCap(current, n);
        } while (!requested.compareAndSet(current, next));
        return current;
    }

    public static long multiplyCap(long a2, long b) {
        long u = a2 * b;
        if (((a2 | b) >>> 31) != 0 && b != 0 && u / b != a2) {
            return Long.MAX_VALUE;
        }
        return u;
    }

    public static long addCap(long a2, long b) {
        long u = a2 + b;
        if (u < 0) {
            return Long.MAX_VALUE;
        }
        return u;
    }
}
