package rx.internal.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes.dex */
public final class RxThreadFactory extends AtomicLong implements ThreadFactory {
    final String prefix;

    public RxThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override // java.util.concurrent.ThreadFactory
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, this.prefix + incrementAndGet());
        t.setDaemon(true);
        return t;
    }
}
