package com.tencent.bugly.proguard;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class w {

    /* renamed from: a, reason: collision with root package name */
    private static w f207a;
    private ScheduledExecutorService b;

    protected w() {
        this.b = null;
        this.b = Executors.newScheduledThreadPool(3, new ThreadFactory(this) { // from class: com.tencent.bugly.proguard.w.1
            @Override // java.util.concurrent.ThreadFactory
            public final Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName("BUGLY_THREAD");
                return thread;
            }
        });
        if (this.b == null || this.b.isShutdown()) {
            x.d("[AsyncTaskHandler] ScheduledExecutorService is not valiable!", new Object[0]);
        }
    }

    public static synchronized w a() {
        if (f207a == null) {
            f207a = new w();
        }
        return f207a;
    }

    public final synchronized boolean a(Runnable runnable, long j) {
        boolean z = false;
        synchronized (this) {
            if (!c()) {
                x.d("[AsyncTaskHandler] Async handler was closed, should not post task.", new Object[0]);
            } else if (runnable == null) {
                x.d("[AsyncTaskHandler] Task input is null.", new Object[0]);
            } else {
                if (j <= 0) {
                    j = 0;
                }
                x.c("[AsyncTaskHandler] Post a delay(time: %dms) task: %s", Long.valueOf(j), runnable.getClass().getName());
                try {
                    this.b.schedule(runnable, j, TimeUnit.MILLISECONDS);
                    z = true;
                } catch (Throwable th) {
                    if (com.tencent.bugly.b.c) {
                        th.printStackTrace();
                    }
                }
            }
        }
        return z;
    }

    public final synchronized boolean a(Runnable runnable) {
        boolean z = false;
        synchronized (this) {
            if (!c()) {
                x.d("[AsyncTaskHandler] Async handler was closed, should not post task.", new Object[0]);
            } else if (runnable == null) {
                x.d("[AsyncTaskHandler] Task input is null.", new Object[0]);
            } else {
                x.c("[AsyncTaskHandler] Post a normal task: %s", runnable.getClass().getName());
                try {
                    this.b.execute(runnable);
                    z = true;
                } catch (Throwable th) {
                    if (com.tencent.bugly.b.c) {
                        th.printStackTrace();
                    }
                }
            }
        }
        return z;
    }

    public final synchronized void b() {
        if (this.b != null && !this.b.isShutdown()) {
            x.c("[AsyncTaskHandler] Close async handler.", new Object[0]);
            this.b.shutdownNow();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0010  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final synchronized boolean c() {
        /*
            r1 = this;
            monitor-enter(r1)
            java.util.concurrent.ScheduledExecutorService r0 = r1.b     // Catch: java.lang.Throwable -> L12
            if (r0 == 0) goto L10
            java.util.concurrent.ScheduledExecutorService r0 = r1.b     // Catch: java.lang.Throwable -> L12
            boolean r0 = r0.isShutdown()     // Catch: java.lang.Throwable -> L12
            if (r0 != 0) goto L10
            r0 = 1
        Le:
            monitor-exit(r1)
            return r0
        L10:
            r0 = 0
            goto Le
        L12:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.w.c():boolean");
    }
}
