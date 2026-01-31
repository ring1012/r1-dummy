package cn.kuwo.autosdk;

/* loaded from: classes.dex */
final class t extends Thread {

    /* renamed from: a, reason: collision with root package name */
    private volatile Runnable f21a;
    private volatile int b;
    private volatile boolean c;

    private t() {
    }

    /* synthetic */ t(t tVar) {
        this();
    }

    public void a(Runnable runnable, int i) {
        this.f21a = runnable;
        this.b = i;
        if (this.c) {
            synchronized (this) {
                notify();
            }
        } else {
            start();
            this.c = true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x003b, code lost:
    
        wait();
     */
    @Override // java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() throws java.lang.SecurityException, java.lang.IllegalArgumentException {
        /*
            r4 = this;
            r3 = 5
        L1:
            int r0 = r4.b
            android.os.Process.setThreadPriority(r0)
            java.lang.Runnable r0 = r4.f21a
            r0.run()
            int r0 = cn.kuwo.autosdk.r.a()
            if (r0 < r3) goto L15
        L11:
            r0 = 0
            r4.c = r0
            return
        L15:
            monitor-enter(r4)
            cn.kuwo.autosdk.t[] r1 = cn.kuwo.autosdk.r.b()     // Catch: java.lang.Throwable -> L24
            monitor-enter(r1)     // Catch: java.lang.Throwable -> L24
            int r0 = cn.kuwo.autosdk.r.a()     // Catch: java.lang.Throwable -> L40
            if (r0 < r3) goto L27
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L40
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L24
            goto L11
        L24:
            r0 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L24
            throw r0
        L27:
            cn.kuwo.autosdk.t[] r0 = cn.kuwo.autosdk.r.b()     // Catch: java.lang.Throwable -> L40
            int r2 = cn.kuwo.autosdk.r.a()     // Catch: java.lang.Throwable -> L40
            r0[r2] = r4     // Catch: java.lang.Throwable -> L40
            int r0 = cn.kuwo.autosdk.r.a()     // Catch: java.lang.Throwable -> L40
            int r0 = r0 + 1
            cn.kuwo.autosdk.r.a(r0)     // Catch: java.lang.Throwable -> L40
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L40
            r4.wait()     // Catch: java.lang.Throwable -> L24 java.lang.InterruptedException -> L43
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L24
            goto L1
        L40:
            r0 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L40
            throw r0     // Catch: java.lang.Throwable -> L24
        L43:
            r0 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L24
            goto L11
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.kuwo.autosdk.t.run():void");
    }
}
