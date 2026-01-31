package com.tencent.bugly.crashreport.crash;

import android.content.Context;
import com.tencent.bugly.crashreport.common.strategy.StrategyBean;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.y;
import com.tencent.bugly.proguard.z;
import java.util.LinkedHashMap;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class d {

    /* renamed from: a, reason: collision with root package name */
    private static d f162a = null;
    private com.tencent.bugly.crashreport.common.strategy.a b;
    private com.tencent.bugly.crashreport.common.info.a c;
    private b d;
    private Context e;

    static /* synthetic */ void a(d dVar) {
        x.c("[ExtraCrashManager] Trying to notify Bugly agents.", new Object[0]);
        try {
            Class<?> cls = Class.forName("com.tencent.bugly.agent.GameAgent");
            dVar.c.getClass();
            z.a(cls, "sdkPackageName", "".equals("") ? "com.tencent.bugly" : "com.tencent.bugly.", (Object) null);
            x.c("[ExtraCrashManager] Bugly game agent has been notified.", new Object[0]);
        } catch (Throwable th) {
            x.a("[ExtraCrashManager] no game agent", new Object[0]);
        }
    }

    static /* synthetic */ void a(d dVar, Thread thread, int i, String str, String str2, String str3, Map map) {
        String str4;
        String str5;
        String str6;
        switch (i) {
            case 4:
                str4 = "Unity";
                break;
            case 5:
            case 6:
                str4 = "Cocos";
                break;
            case 7:
            default:
                x.d("[ExtraCrashManager] Unknown extra crash type: %d", Integer.valueOf(i));
                return;
            case 8:
                str4 = "H5";
                break;
        }
        x.e("[ExtraCrashManager] %s Crash Happen", str4);
        try {
            try {
                if (!dVar.b.b()) {
                    x.e("waiting for remote sync", new Object[0]);
                    int i2 = 0;
                    while (!dVar.b.b()) {
                        z.b(500L);
                        i2 += cn.yunzhisheng.asr.a.U;
                        if (i2 >= 3000) {
                        }
                    }
                }
                if (!dVar.b.b()) {
                    x.d("[ExtraCrashManager] There is no remote strategy, but still store it.", new Object[0]);
                }
                StrategyBean strategyBeanC = dVar.b.c();
                if (!strategyBeanC.g && dVar.b.b()) {
                    x.e("[ExtraCrashManager] Crash report was closed by remote , will not upload to Bugly , print local for helpful!", new Object[0]);
                    b.a(str4, z.a(), dVar.c.d, thread, str + "\n" + str2 + "\n" + str3, null);
                    x.e("[ExtraCrashManager] Successfully handled.", new Object[0]);
                    return;
                }
                switch (i) {
                    case 5:
                    case 6:
                        if (!strategyBeanC.l) {
                            x.e("[ExtraCrashManager] %s report is disabled.", str4);
                            x.e("[ExtraCrashManager] Successfully handled.", new Object[0]);
                            return;
                        }
                        break;
                    case 8:
                        if (!strategyBeanC.m) {
                            x.e("[ExtraCrashManager] %s report is disabled.", str4);
                            x.e("[ExtraCrashManager] Successfully handled.", new Object[0]);
                            return;
                        }
                        break;
                }
                if (i == 8) {
                    i = 5;
                }
                CrashDetailBean crashDetailBean = new CrashDetailBean();
                crashDetailBean.B = com.tencent.bugly.crashreport.common.info.b.h();
                crashDetailBean.C = com.tencent.bugly.crashreport.common.info.b.f();
                crashDetailBean.D = com.tencent.bugly.crashreport.common.info.b.j();
                crashDetailBean.E = dVar.c.p();
                crashDetailBean.F = dVar.c.o();
                crashDetailBean.G = dVar.c.q();
                crashDetailBean.w = z.a(dVar.e, c.e, (String) null);
                crashDetailBean.b = i;
                crashDetailBean.e = dVar.c.h();
                crashDetailBean.f = dVar.c.j;
                crashDetailBean.g = dVar.c.w();
                crashDetailBean.m = dVar.c.g();
                crashDetailBean.n = str;
                crashDetailBean.o = str2;
                if (str3 != null) {
                    String[] strArrSplit = str3.split("\n");
                    str5 = strArrSplit.length > 0 ? strArrSplit[0] : "";
                    str6 = str3;
                } else {
                    str5 = "";
                    str6 = "";
                }
                crashDetailBean.p = str5;
                crashDetailBean.q = str6;
                crashDetailBean.r = System.currentTimeMillis();
                crashDetailBean.u = z.b(crashDetailBean.q.getBytes());
                crashDetailBean.y = z.a(c.f, false);
                crashDetailBean.z = dVar.c.d;
                crashDetailBean.A = thread.getName() + "(" + thread.getId() + ")";
                crashDetailBean.H = dVar.c.y();
                crashDetailBean.h = dVar.c.v();
                crashDetailBean.L = dVar.c.f141a;
                crashDetailBean.M = dVar.c.a();
                crashDetailBean.O = dVar.c.H();
                crashDetailBean.P = dVar.c.I();
                crashDetailBean.Q = dVar.c.B();
                crashDetailBean.R = dVar.c.G();
                dVar.d.c(crashDetailBean);
                crashDetailBean.x = y.a();
                if (crashDetailBean.N == null) {
                    crashDetailBean.N = new LinkedHashMap();
                }
                if (map != null) {
                    crashDetailBean.N.putAll(map);
                }
                if (crashDetailBean == null) {
                    x.e("[ExtraCrashManager] Failed to package crash data.", new Object[0]);
                    x.e("[ExtraCrashManager] Successfully handled.", new Object[0]);
                } else {
                    b.a(str4, z.a(), dVar.c.d, thread, str + "\n" + str2 + "\n" + str3, crashDetailBean);
                    if (!dVar.d.a(crashDetailBean)) {
                        dVar.d.a(crashDetailBean, 3000L, false);
                    }
                    x.e("[ExtraCrashManager] Successfully handled.", new Object[0]);
                }
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                x.e("[ExtraCrashManager] Successfully handled.", new Object[0]);
            }
        } catch (Throwable th2) {
            x.e("[ExtraCrashManager] Successfully handled.", new Object[0]);
            throw th2;
        }
    }

    private d(Context context) {
        c cVarA = c.a();
        if (cVarA != null) {
            this.b = com.tencent.bugly.crashreport.common.strategy.a.a();
            this.c = com.tencent.bugly.crashreport.common.info.a.a(context);
            this.d = cVarA.o;
            this.e = context;
            w.a().a(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.d.1
                @Override // java.lang.Runnable
                public final void run() {
                    d.a(d.this);
                }
            });
        }
    }

    public static d a(Context context) {
        if (f162a == null) {
            f162a = new d(context);
        }
        return f162a;
    }

    public static void a(final Thread thread, final int i, final String str, final String str2, final String str3, final Map<String, String> map) {
        w.a().a(new Runnable() { // from class: com.tencent.bugly.crashreport.crash.d.2
            @Override // java.lang.Runnable
            public final void run() {
                try {
                    if (d.f162a != null) {
                        d.a(d.f162a, thread, i, str, str2, str3, map);
                    } else {
                        x.e("[ExtraCrashManager] Extra crash manager has not been initialized.", new Object[0]);
                    }
                } catch (Throwable th) {
                    if (!x.b(th)) {
                        th.printStackTrace();
                    }
                    x.e("[ExtraCrashManager] Crash error %s %s %s", str, str2, str3);
                }
            }
        });
    }
}
