package com.tencent.bugly.crashreport.crash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class BuglyBroadcastRecevier extends BroadcastReceiver {
    private static BuglyBroadcastRecevier d = null;
    private Context b;
    private String c;
    private boolean e = true;

    /* renamed from: a, reason: collision with root package name */
    private IntentFilter f146a = new IntentFilter();

    public static synchronized BuglyBroadcastRecevier getInstance() {
        if (d == null) {
            d = new BuglyBroadcastRecevier();
        }
        return d;
    }

    public synchronized void addFilter(String str) {
        if (!this.f146a.hasAction(str)) {
            this.f146a.addAction(str);
        }
        x.c("add action %s", str);
    }

    public synchronized void register(Context context) {
        this.b = context;
        z.a(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.BuglyBroadcastRecevier.1
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    x.a(BuglyBroadcastRecevier.d.getClass(), "Register broadcast receiver of Bugly.", new Object[0]);
                    synchronized (this) {
                        BuglyBroadcastRecevier.this.b.registerReceiver(BuglyBroadcastRecevier.d, BuglyBroadcastRecevier.this.f146a);
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }

    public synchronized void unregister(Context context) {
        try {
            x.a(getClass(), "Unregister broadcast receiver of Bugly.", new Object[0]);
            context.unregisterReceiver(this);
            this.b = context;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
        }
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        try {
            a(context, intent);
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x0015  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private synchronized boolean a(android.content.Context r11, android.content.Intent r12) {
        /*
            r10 = this;
            r8 = 30000(0x7530, double:1.4822E-319)
            r0 = 1
            r1 = 0
            monitor-enter(r10)
            if (r11 == 0) goto L15
            if (r12 == 0) goto L15
            java.lang.String r2 = r12.getAction()     // Catch: java.lang.Throwable -> L20
            java.lang.String r3 = "android.net.conn.CONNECTIVITY_CHANGE"
            boolean r2 = r2.equals(r3)     // Catch: java.lang.Throwable -> L20
            if (r2 != 0) goto L18
        L15:
            r0 = r1
        L16:
            monitor-exit(r10)
            return r0
        L18:
            boolean r1 = r10.e     // Catch: java.lang.Throwable -> L20
            if (r1 == 0) goto L23
            r1 = 0
            r10.e = r1     // Catch: java.lang.Throwable -> L20
            goto L16
        L20:
            r0 = move-exception
            monitor-exit(r10)
            throw r0
        L23:
            android.content.Context r1 = r10.b     // Catch: java.lang.Throwable -> L20
            java.lang.String r1 = com.tencent.bugly.crashreport.common.info.b.f(r1)     // Catch: java.lang.Throwable -> L20
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L20
            java.lang.String r3 = "is Connect BC "
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L20
            java.lang.StringBuilder r2 = r2.append(r1)     // Catch: java.lang.Throwable -> L20
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L20
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L20
            com.tencent.bugly.proguard.x.c(r2, r3)     // Catch: java.lang.Throwable -> L20
            java.lang.String r2 = "network %s changed to %s"
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L20
            r4 = 0
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L20
            r5.<init>()     // Catch: java.lang.Throwable -> L20
            java.lang.String r6 = r10.c     // Catch: java.lang.Throwable -> L20
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch: java.lang.Throwable -> L20
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L20
            r3[r4] = r5     // Catch: java.lang.Throwable -> L20
            r4 = 1
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L20
            r5.<init>()     // Catch: java.lang.Throwable -> L20
            java.lang.StringBuilder r5 = r5.append(r1)     // Catch: java.lang.Throwable -> L20
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L20
            r3[r4] = r5     // Catch: java.lang.Throwable -> L20
            com.tencent.bugly.proguard.x.a(r2, r3)     // Catch: java.lang.Throwable -> L20
            if (r1 != 0) goto L6e
            r1 = 0
            r10.c = r1     // Catch: java.lang.Throwable -> L20
            goto L16
        L6e:
            java.lang.String r2 = r10.c     // Catch: java.lang.Throwable -> L20
            r10.c = r1     // Catch: java.lang.Throwable -> L20
            long r4 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L20
            com.tencent.bugly.crashreport.common.strategy.a r3 = com.tencent.bugly.crashreport.common.strategy.a.a()     // Catch: java.lang.Throwable -> L20
            com.tencent.bugly.proguard.u r6 = com.tencent.bugly.proguard.u.a()     // Catch: java.lang.Throwable -> L20
            com.tencent.bugly.crashreport.common.info.a r7 = com.tencent.bugly.crashreport.common.info.a.a(r11)     // Catch: java.lang.Throwable -> L20
            if (r3 == 0) goto L88
            if (r6 == 0) goto L88
            if (r7 != 0) goto L91
        L88:
            java.lang.String r1 = "not inited BC not work"
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L20
            com.tencent.bugly.proguard.x.d(r1, r2)     // Catch: java.lang.Throwable -> L20
            goto L16
        L91:
            boolean r1 = r1.equals(r2)     // Catch: java.lang.Throwable -> L20
            if (r1 != 0) goto L16
            int r1 = com.tencent.bugly.crashreport.crash.c.f159a     // Catch: java.lang.Throwable -> L20
            long r2 = r6.a(r1)     // Catch: java.lang.Throwable -> L20
            long r2 = r4 - r2
            int r1 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r1 <= 0) goto Lb6
            java.lang.String r1 = "try to upload crash on network changed."
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L20
            com.tencent.bugly.proguard.x.a(r1, r2)     // Catch: java.lang.Throwable -> L20
            com.tencent.bugly.crashreport.crash.c r1 = com.tencent.bugly.crashreport.crash.c.a()     // Catch: java.lang.Throwable -> L20
            if (r1 == 0) goto Lb6
            r2 = 0
            r1.a(r2)     // Catch: java.lang.Throwable -> L20
        Lb6:
            r1 = 1001(0x3e9, float:1.403E-42)
            long r2 = r6.a(r1)     // Catch: java.lang.Throwable -> L20
            long r2 = r4 - r2
            int r1 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r1 <= 0) goto L16
            java.lang.String r1 = "try to upload userinfo on network changed."
            r2 = 0
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.Throwable -> L20
            com.tencent.bugly.proguard.x.a(r1, r2)     // Catch: java.lang.Throwable -> L20
            com.tencent.bugly.crashreport.biz.a r1 = com.tencent.bugly.crashreport.biz.b.f137a     // Catch: java.lang.Throwable -> L20
            r1.b()     // Catch: java.lang.Throwable -> L20
            goto L16
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.BuglyBroadcastRecevier.a(android.content.Context, android.content.Intent):boolean");
    }
}
