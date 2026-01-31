package com.tencent.bugly.crashreport.crash.anr;

import android.content.Context;
import android.os.FileObserver;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.crash.CrashDetailBean;
import com.tencent.bugly.crashreport.crash.c;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class b {
    private final Context c;
    private final com.tencent.bugly.crashreport.common.info.a d;
    private final w e;
    private final com.tencent.bugly.crashreport.common.strategy.a f;
    private final String g;
    private final com.tencent.bugly.crashreport.crash.b h;
    private FileObserver i;

    /* renamed from: a, reason: collision with root package name */
    private AtomicInteger f154a = new AtomicInteger(0);
    private long b = -1;
    private boolean j = true;

    public b(Context context, com.tencent.bugly.crashreport.common.strategy.a aVar, com.tencent.bugly.crashreport.common.info.a aVar2, w wVar, com.tencent.bugly.crashreport.crash.b bVar) {
        this.c = z.a(context);
        this.g = context.getDir("bugly", 0).getAbsolutePath();
        this.d = aVar2;
        this.e = wVar;
        this.f = aVar;
        this.h = bVar;
    }

    private CrashDetailBean a(a aVar) {
        CrashDetailBean crashDetailBean = new CrashDetailBean();
        try {
            crashDetailBean.B = com.tencent.bugly.crashreport.common.info.b.h();
            crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.f();
            crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.j();
            crashDetailBean.E = this.d.p();
            crashDetailBean.F = this.d.o();
            crashDetailBean.G = this.d.q();
            crashDetailBean.w = z.a(this.c, c.e, (String) null);
            crashDetailBean.b = 3;
            crashDetailBean.e = this.d.h();
            crashDetailBean.f = this.d.j;
            crashDetailBean.g = this.d.w();
            crashDetailBean.m = this.d.g();
            crashDetailBean.n = "ANR_EXCEPTION";
            crashDetailBean.o = aVar.f;
            crashDetailBean.q = aVar.g;
            crashDetailBean.N = new HashMap();
            crashDetailBean.N.put("BUGLY_CR_01", aVar.e);
            int iIndexOf = -1;
            if (crashDetailBean.q != null) {
                iIndexOf = crashDetailBean.q.indexOf("\n");
            }
            crashDetailBean.p = iIndexOf > 0 ? crashDetailBean.q.substring(0, iIndexOf) : "GET_FAIL";
            crashDetailBean.r = aVar.c;
            if (crashDetailBean.q != null) {
                crashDetailBean.u = z.b(crashDetailBean.q.getBytes());
            }
            crashDetailBean.y = aVar.b;
            crashDetailBean.z = this.d.d;
            crashDetailBean.A = "main(1)";
            crashDetailBean.H = this.d.y();
            crashDetailBean.h = this.d.v();
            crashDetailBean.i = this.d.K();
            crashDetailBean.v = aVar.d;
            crashDetailBean.K = this.d.n;
            crashDetailBean.L = this.d.f141a;
            crashDetailBean.M = this.d.a();
            crashDetailBean.O = this.d.H();
            crashDetailBean.P = this.d.I();
            crashDetailBean.Q = this.d.B();
            crashDetailBean.R = this.d.G();
            this.h.c(crashDetailBean);
            crashDetailBean.x = y.a();
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
        }
        return crashDetailBean;
    }

    /* JADX WARN: Removed duplicated region for block: B:79:0x01d6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean a(java.lang.String r12, java.lang.String r13, java.lang.String r14) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 492
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.anr.b.a(java.lang.String, java.lang.String, java.lang.String):boolean");
    }

    public final boolean a() {
        return this.f154a.get() != 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:112:0x02ed A[LOOP:0: B:37:0x00c6->B:112:0x02ed, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0113 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void a(java.lang.String r13) {
        /*
            Method dump skipped, instructions count: 755
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.crash.anr.b.a(java.lang.String):void");
    }

    private synchronized void c() {
        if (e()) {
            x.d("start when started!", new Object[0]);
        } else {
            this.i = new FileObserver("/data/anr/", 8) { // from class: com.tencent.bugly.crashreport.crash.anr.b.1
                @Override // android.os.FileObserver
                public final void onEvent(int i, String str) {
                    if (str != null) {
                        String str2 = "/data/anr/" + str;
                        if (!str2.contains("trace")) {
                            x.d("not anr file %s", str2);
                        } else {
                            b.this.a(str2);
                        }
                    }
                }
            };
            try {
                this.i.startWatching();
                x.a("start anr monitor!", new Object[0]);
                this.e.a(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.anr.b.2
                    @Override // java.lang.Runnable
                    public final void run() {
                        b.this.b();
                    }
                });
            } catch (Throwable th) {
                this.i = null;
                x.d("start anr monitor failed!", new Object[0]);
                if (!x.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    private synchronized void d() {
        if (!e()) {
            x.d("close when closed!", new Object[0]);
        } else {
            try {
                this.i.stopWatching();
                this.i = null;
                x.d("close anr monitor!", new Object[0]);
            } catch (Throwable th) {
                x.d("stop anr monitor failed!", new Object[0]);
                if (!x.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    private synchronized boolean e() {
        return this.i != null;
    }

    private synchronized void b(boolean z) {
        if (z) {
            c();
        } else {
            d();
        }
    }

    private synchronized boolean f() {
        return this.j;
    }

    private synchronized void c(boolean z) {
        if (this.j != z) {
            x.a("user change anr %b", Boolean.valueOf(z));
            this.j = z;
        }
    }

    public final void a(boolean z) {
        c(z);
        boolean zF = f();
        com.tencent.bugly.crashreport.common.strategy.a aVarA = com.tencent.bugly.crashreport.common.strategy.a.a();
        if (aVarA != null) {
            zF = zF && aVarA.c().g;
        }
        if (zF != e()) {
            x.a("anr changed to %b", Boolean.valueOf(zF));
            b(zF);
        }
    }

    protected final void b() {
        File[] fileArrListFiles;
        int iIndexOf;
        long jB = z.b() - c.g;
        File file = new File(this.g);
        if (file.exists() && file.isDirectory() && (fileArrListFiles = file.listFiles()) != null && fileArrListFiles.length != 0) {
            int length = "bugly_trace_".length();
            int i = 0;
            for (File file2 : fileArrListFiles) {
                String name = file2.getName();
                if (name.startsWith("bugly_trace_")) {
                    try {
                        iIndexOf = name.indexOf(".txt");
                    } catch (Throwable th) {
                        x.e("tomb format error delete %s", name);
                    }
                    if (iIndexOf <= 0 || Long.parseLong(name.substring(length, iIndexOf)) < jB) {
                        if (file2.delete()) {
                            i++;
                        }
                    }
                }
            }
            x.c("clean tombs %d", Integer.valueOf(i));
        }
    }

    public final synchronized void a(StrategyBean strategyBean) {
        synchronized (this) {
            if (strategyBean != null) {
                if (strategyBean.j != e()) {
                    x.d("server anr changed to %b", Boolean.valueOf(strategyBean.j));
                }
                boolean z = strategyBean.j && f();
                if (z != e()) {
                    x.a("anr changed to %b", Boolean.valueOf(z));
                    b(z);
                }
            }
        }
    }
}
