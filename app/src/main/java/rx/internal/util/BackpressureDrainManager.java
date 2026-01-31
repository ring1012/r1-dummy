package rx.internal.util;

import java.util.concurrent.atomic.AtomicLong;
import rx.Producer;
import rx.annotations.Experimental;

@Experimental
/* loaded from: classes.dex */
public final class BackpressureDrainManager extends AtomicLong implements Producer {
    protected final BackpressureQueueCallback actual;
    protected boolean emitting;
    protected Throwable exception;
    protected volatile boolean terminated;

    public interface BackpressureQueueCallback {
        boolean accept(Object obj);

        void complete(Throwable th);

        Object peek();

        Object poll();
    }

    public BackpressureDrainManager(BackpressureQueueCallback actual) {
        this.actual = actual;
    }

    public final boolean isTerminated() {
        return this.terminated;
    }

    public final void terminate() {
        this.terminated = true;
    }

    public final void terminate(Throwable error) {
        if (!this.terminated) {
            this.exception = error;
            this.terminated = true;
        }
    }

    public final void terminateAndDrain() {
        this.terminated = true;
        drain();
    }

    public final void terminateAndDrain(Throwable error) {
        if (!this.terminated) {
            this.exception = error;
            this.terminated = true;
            drain();
        }
    }

    @Override // rx.Producer
    public final void request(long n) {
        long r;
        boolean mayDrain;
        long u;
        if (n != 0) {
            do {
                r = get();
                mayDrain = r == 0;
                if (r == Long.MAX_VALUE) {
                    break;
                }
                if (n == Long.MAX_VALUE) {
                    u = n;
                    mayDrain = true;
                } else if (r > Long.MAX_VALUE - n) {
                    u = Long.MAX_VALUE;
                } else {
                    u = r + n;
                }
            } while (!compareAndSet(r, u));
            if (mayDrain) {
                drain();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:124:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:?, code lost:
    
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x003f, code lost:
    
        monitor-enter(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0040, code lost:
    
        r8 = r14.terminated;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0046, code lost:
    
        if (r0.peek() == null) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0048, code lost:
    
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0054, code lost:
    
        if (get() != Long.MAX_VALUE) goto L81;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0056, code lost:
    
        if (r3 != false) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0058, code lost:
    
        if (r8 != false) goto L68;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x005a, code lost:
    
        r14.emitting = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x005e, code lost:
    
        monitor-exit(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x005f, code lost:
    
        if (1 != 0) goto L124;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0061, code lost:
    
        monitor-enter(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0063, code lost:
    
        r14.emitting = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0065, code lost:
    
        monitor-exit(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0088, code lost:
    
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x008a, code lost:
    
        r4 = Long.MAX_VALUE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x008f, code lost:
    
        monitor-exit(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x009f, code lost:
    
        r4 = addAndGet(-r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x00a7, code lost:
    
        if (r4 == 0) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x00a9, code lost:
    
        if (r3 != false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x00ab, code lost:
    
        if (r8 == false) goto L115;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x00ad, code lost:
    
        if (r3 == false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x00af, code lost:
    
        r14.emitting = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x00b3, code lost:
    
        monitor-exit(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x00b4, code lost:
    
        if (1 != 0) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x00b6, code lost:
    
        monitor-enter(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x00b8, code lost:
    
        r14.emitting = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x00ba, code lost:
    
        monitor-exit(r14);
     */
    /* JADX WARN: Removed duplicated region for block: B:52:0x006a A[Catch: all -> 0x0094, TRY_ENTER, TryCatch #3 {all -> 0x0094, blocks: (B:10:0x0012, B:33:0x003f, B:73:0x0093, B:16:0x001f, B:18:0x0025, B:52:0x006a, B:54:0x0070, B:34:0x0040, B:37:0x0049, B:41:0x005a, B:42:0x005e, B:69:0x008f, B:82:0x009f, B:87:0x00af, B:88:0x00b3), top: B:106:0x0012, inners: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void drain() {
        /*
            Method dump skipped, instructions count: 195
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: rx.internal.util.BackpressureDrainManager.drain():void");
    }
}
