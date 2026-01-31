package com.tencent.bugly.crashreport.common.strategy;

import android.content.Context;
import com.tencent.bugly.crashreport.biz.b;
import com.tencent.bugly.proguard.ap;
import com.tencent.bugly.proguard.o;
import com.tencent.bugly.proguard.p;
import com.tencent.bugly.proguard.r;
import com.tencent.bugly.proguard.w;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.List;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class a {

    /* renamed from: a, reason: collision with root package name */
    public static int f144a = 1000;
    private static a b = null;
    private static String h = null;
    private final List<com.tencent.bugly.a> c;
    private Context g;
    private StrategyBean f = null;
    private final StrategyBean e = new StrategyBean();
    private final w d = w.a();

    static /* synthetic */ String e() {
        return null;
    }

    private a(Context context, List<com.tencent.bugly.a> list) {
        this.g = context;
        this.c = list;
    }

    public static synchronized a a(Context context, List<com.tencent.bugly.a> list) {
        if (b == null) {
            b = new a(context, list);
        }
        return b;
    }

    public final void a(long j) {
        this.d.a(new Thread() { // from class: com.tencent.bugly.crashreport.common.strategy.a.1
            @Override // java.lang.Thread, java.lang.Runnable
            public final void run() {
                try {
                    Map<String, byte[]> mapA = p.a().a(a.f144a, (o) null, true);
                    if (mapA != null) {
                        byte[] bArr = mapA.get("key_imei");
                        byte[] bArr2 = mapA.get("key_ip");
                        if (bArr != null) {
                            com.tencent.bugly.crashreport.common.info.a.a(a.this.g).e(new String(bArr));
                        }
                        if (bArr2 != null) {
                            com.tencent.bugly.crashreport.common.info.a.a(a.this.g).d(new String(bArr2));
                        }
                    }
                    a aVar = a.this;
                    a aVar2 = a.this;
                    aVar.f = a.d();
                    if (a.this.f != null && !z.a(a.e()) && z.c(a.e())) {
                        a.this.f.r = a.e();
                        a.this.f.s = a.e();
                    }
                } catch (Throwable th) {
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                }
                a.this.a(a.this.f, false);
            }
        }, j);
    }

    public static synchronized a a() {
        return b;
    }

    public final synchronized boolean b() {
        return this.f != null;
    }

    public final StrategyBean c() {
        return this.f != null ? this.f : this.e;
    }

    protected final void a(StrategyBean strategyBean, boolean z) {
        x.c("[Strategy] Notify %s", b.class.getName());
        b.a(strategyBean, z);
        for (com.tencent.bugly.a aVar : this.c) {
            try {
                x.c("[Strategy] Notify %s", aVar.getClass().getName());
                aVar.onServerStrategyChanged(strategyBean);
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
            }
        }
    }

    public final void a(ap apVar) throws NumberFormatException {
        if (apVar != null) {
            if (this.f == null || apVar.h != this.f.p) {
                StrategyBean strategyBean = new StrategyBean();
                strategyBean.g = apVar.f184a;
                strategyBean.i = apVar.c;
                strategyBean.h = apVar.b;
                if (z.a((String) null) || !z.c((String) null)) {
                    if (z.c(apVar.d)) {
                        x.c("[Strategy] Upload url changes to %s", apVar.d);
                        strategyBean.r = apVar.d;
                    }
                    if (z.c(apVar.e)) {
                        x.c("[Strategy] Exception upload url changes to %s", apVar.e);
                        strategyBean.s = apVar.e;
                    }
                }
                if (apVar.f != null && !z.a(apVar.f.f183a)) {
                    strategyBean.u = apVar.f.f183a;
                }
                if (apVar.h != 0) {
                    strategyBean.p = apVar.h;
                }
                if (apVar.g != null && apVar.g.size() > 0) {
                    strategyBean.v = apVar.g;
                    String str = apVar.g.get("B11");
                    if (str != null && str.equals("1")) {
                        strategyBean.j = true;
                    } else {
                        strategyBean.j = false;
                    }
                    String str2 = apVar.g.get("B3");
                    if (str2 != null) {
                        strategyBean.y = Long.valueOf(str2).longValue();
                    }
                    strategyBean.q = apVar.i;
                    strategyBean.x = apVar.i;
                    String str3 = apVar.g.get("B27");
                    if (str3 != null && str3.length() > 0) {
                        try {
                            int i = Integer.parseInt(str3);
                            if (i > 0) {
                                strategyBean.w = i;
                            }
                        } catch (Exception e) {
                            if (!x.a(e)) {
                                e.printStackTrace();
                            }
                        }
                    }
                    String str4 = apVar.g.get("B25");
                    if (str4 != null && str4.equals("1")) {
                        strategyBean.l = true;
                    } else {
                        strategyBean.l = false;
                    }
                }
                x.a("[Strategy] enableCrashReport:%b, enableQuery:%b, enableUserInfo:%b, enableAnr:%b, enableBlock:%b, enableSession:%b, enableSessionTimer:%b, sessionOverTime:%d, enableCocos:%b, strategyLastUpdateTime:%d", Boolean.valueOf(strategyBean.g), Boolean.valueOf(strategyBean.i), Boolean.valueOf(strategyBean.h), Boolean.valueOf(strategyBean.j), Boolean.valueOf(strategyBean.k), Boolean.valueOf(strategyBean.n), Boolean.valueOf(strategyBean.o), Long.valueOf(strategyBean.q), Boolean.valueOf(strategyBean.l), Long.valueOf(strategyBean.p));
                this.f = strategyBean;
                p.a().b(2);
                r rVar = new r();
                rVar.b = 2;
                rVar.f200a = strategyBean.e;
                rVar.e = strategyBean.f;
                rVar.g = z.a(strategyBean);
                p.a().a(rVar);
                a(strategyBean, true);
            }
        }
    }

    public static StrategyBean d() {
        List<r> listA = p.a().a(2);
        if (listA != null && listA.size() > 0) {
            r rVar = listA.get(0);
            if (rVar.g != null) {
                return (StrategyBean) z.a(rVar.g, StrategyBean.CREATOR);
            }
        }
        return null;
    }
}
