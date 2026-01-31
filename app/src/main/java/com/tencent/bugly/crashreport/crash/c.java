package com.tencent.bugly.crashreport.crash;

import android.content.Context;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.crashreport.crash.jni.NativeCrashHandler;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.r;
import com.tencent.bugly.proguard.u;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class c {

    /* renamed from: a, reason: collision with root package name */
    public static int f159a = 0;
    public static boolean b = false;
    public static int c = 2;
    public static boolean d = true;
    public static int e = cn.yunzhisheng.asr.a.b;
    public static int f = cn.yunzhisheng.asr.a.b;
    public static long g = 604800000;
    public static String h = null;
    public static boolean i = false;
    public static String j = null;
    public static int k = 5000;
    public static boolean l = true;
    public static String m = null;
    public static String n = null;
    private static c q;
    public final b o;
    private final Context p;
    private final e r;
    private final NativeCrashHandler s;
    private com.tencent.bugly.crashreport.common.strategy.a t;
    private w u;
    private final com.tencent.bugly.crashreport.crash.anr.b v;
    private Boolean w;

    private c(int i2, Context context, w wVar, boolean z, BuglyStrategy.a aVar, o oVar, String str) {
        f159a = i2;
        Context contextA = z.a(context);
        this.p = contextA;
        this.t = com.tencent.bugly.crashreport.common.strategy.a.a();
        this.u = wVar;
        this.o = new b(i2, contextA, u.a(), p.a(), this.t, aVar, oVar);
        com.tencent.bugly.crashreport.common.info.a aVarA = com.tencent.bugly.crashreport.common.info.a.a(contextA);
        this.r = new e(contextA, this.o, this.t, aVarA);
        this.s = NativeCrashHandler.getInstance(contextA, aVarA, this.o, this.t, wVar, z, str);
        aVarA.D = this.s;
        this.v = new com.tencent.bugly.crashreport.crash.anr.b(contextA, this.t, aVarA, wVar, this.o);
    }

    public static synchronized void a(int i2, Context context, boolean z, BuglyStrategy.a aVar, o oVar, String str) {
        if (q == null) {
            q = new c(1004, context, w.a(), z, aVar, null, null);
        }
    }

    public static synchronized c a() {
        return q;
    }

    public final void a(StrategyBean strategyBean) {
        this.r.a(strategyBean);
        this.s.onStrategyChanged(strategyBean);
        this.v.a(strategyBean);
        w.a().a(new AnonymousClass2(), 0L);
    }

    public final boolean b() {
        Boolean bool = this.w;
        if (bool != null) {
            return bool.booleanValue();
        }
        String str = com.tencent.bugly.crashreport.common.info.a.b().d;
        List<r> listA = p.a().a(1);
        ArrayList arrayList = new ArrayList();
        if (listA != null && listA.size() > 0) {
            for (r rVar : listA) {
                if (str.equals(rVar.c)) {
                    this.w = true;
                    arrayList.add(rVar);
                }
            }
            if (arrayList.size() > 0) {
                p.a().a(arrayList);
            }
            return true;
        }
        this.w = false;
        return false;
    }

    public final synchronized void c() {
        this.r.a();
        this.s.setUserOpened(true);
        this.v.a(true);
    }

    public final synchronized void d() {
        this.r.b();
        this.s.setUserOpened(false);
        this.v.a(false);
    }

    public final void e() {
        this.r.a();
    }

    public final void f() {
        this.s.setUserOpened(false);
    }

    public final void g() {
        this.s.setUserOpened(true);
    }

    public final void h() {
        this.v.a(true);
    }

    public final void i() {
        this.v.a(false);
    }

    public final synchronized void j() {
        this.s.testNativeCrash();
    }

    public final synchronized void k() {
        int i2 = 0;
        synchronized (this) {
            com.tencent.bugly.crashreport.crash.anr.b bVar = this.v;
            while (true) {
                int i3 = i2 + 1;
                if (i2 >= 30) {
                    break;
                }
                try {
                    x.a("try main sleep for make a test anr! try:%d/30 , kill it if you don't want to wait!", Integer.valueOf(i3));
                    z.b(DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
                    i2 = i3;
                } catch (Throwable th) {
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                }
            }
        }
    }

    public final boolean l() {
        return this.v.a();
    }

    public final void a(final Thread thread, final Throwable th, boolean z, String str, byte[] bArr, final boolean z2) {
        final String str2 = null;
        w wVar = this.u;
        final boolean z3 = false;
        final byte b2 = 0 == true ? 1 : 0;
        wVar.a(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.c.1
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    x.c("post a throwable %b", Boolean.valueOf(z3));
                    c.this.r.a(thread, th, false, str2, b2);
                    if (z2) {
                        x.a("clear user datas", new Object[0]);
                        com.tencent.bugly.crashreport.common.info.a.a(c.this.p).C();
                    }
                } catch (Throwable th2) {
                    if (!x.b(th2)) {
                        th2.printStackTrace();
                    }
                    x.e("java catch error: %s", th.toString());
                }
            }
        });
    }

    public final void a(CrashDetailBean crashDetailBean) {
        this.o.d(crashDetailBean);
    }

    /* compiled from: BUGLY */
    /* renamed from: com.tencent.bugly.crashreport.crash.c$2, reason: invalid class name */
    final class AnonymousClass2 extends Thread {
        AnonymousClass2() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public final void run() throws Throwable {
            List<CrashDetailBean> arrayList;
            if (z.a(c.this.p, "local_crash_lock", 10000L)) {
                List<CrashDetailBean> listA = c.this.o.a();
                if (listA != null && listA.size() > 0) {
                    int size = listA.size();
                    if (size > 100) {
                        arrayList = new ArrayList<>();
                        Collections.sort(listA);
                        for (int i = 0; i < 100; i++) {
                            arrayList.add(listA.get((size - 1) - i));
                        }
                    } else {
                        arrayList = listA;
                    }
                    c.this.o.a(arrayList, 0L, false, false, false);
                }
                z.b(c.this.p, "local_crash_lock");
            }
        }
    }

    public final void a(long j2) {
        w.a().a(new AnonymousClass2(), 0L);
    }
}
