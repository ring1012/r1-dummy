package com.tencent.bugly.crashreport.common.info;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Process;
import cn.yunzhisheng.common.PinyinConverter;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class a {
    private static a ad = null;
    public SharedPreferences E;
    private final Context F;
    private String G;
    private String H;
    private String X;
    private boolean aj;
    public String c;
    public final String d;
    public final String f;
    public final String g;
    public final String h;
    public long i;
    public String j;
    public String k;
    public String l;
    public List<String> o;
    public boolean u;
    public String v;
    public String w;
    public String x;
    public boolean z;
    public boolean e = true;
    private String I = "unknown";
    private String J = "unknown";
    private String K = "";
    private String L = null;
    private String M = null;
    private String N = null;
    private String O = null;
    private long P = -1;
    private long Q = -1;
    private long R = -1;
    private String S = null;
    private String T = null;
    private Map<String, PlugInBean> U = null;
    private boolean V = true;
    private String W = null;
    private Boolean Y = null;
    private String Z = null;
    private String aa = null;
    private String ab = null;
    public String m = null;
    public String n = null;
    private Map<String, PlugInBean> ac = null;
    private int ae = -1;
    private int af = -1;
    private Map<String, String> ag = new HashMap();
    private Map<String, String> ah = new HashMap();
    private Map<String, String> ai = new HashMap();
    public String p = "unknown";
    public long q = 0;
    public long r = 0;
    public long s = 0;
    public long t = 0;
    public boolean y = false;
    public HashMap<String, String> A = new HashMap<>();
    private String ak = null;
    private String al = null;
    private String am = null;
    private String an = null;
    private String ao = null;
    public boolean B = true;
    public List<String> C = new ArrayList();
    public com.tencent.bugly.crashreport.a D = null;
    private final Object ap = new Object();
    private final Object aq = new Object();
    private final Object ar = new Object();
    private final Object as = new Object();
    private final Object at = new Object();
    private final Object au = new Object();
    private final Object av = new Object();

    /* renamed from: a, reason: collision with root package name */
    public final long f141a = System.currentTimeMillis();
    public final byte b = 1;

    private a(Context context) {
        this.j = null;
        this.k = null;
        this.X = null;
        this.l = null;
        this.o = null;
        this.u = false;
        this.v = null;
        this.w = null;
        this.x = null;
        this.z = false;
        this.F = z.a(context);
        PackageInfo packageInfoB = AppInfo.b(context);
        if (packageInfoB != null) {
            try {
                this.j = packageInfoB.versionName;
                this.v = this.j;
                this.w = Integer.toString(packageInfoB.versionCode);
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
            }
        }
        this.c = AppInfo.a(context);
        this.d = AppInfo.a(Process.myPid());
        this.f = b.l();
        this.g = b.a();
        this.k = AppInfo.c(context);
        this.h = "Android " + b.b() + ",level " + b.c();
        String str = this.g + ";" + this.h;
        Map<String, String> mapD = AppInfo.d(context);
        if (mapD != null) {
            try {
                this.o = AppInfo.a(mapD);
                String str2 = mapD.get("BUGLY_APPID");
                if (str2 != null) {
                    this.X = str2;
                }
                String str3 = mapD.get("BUGLY_APP_VERSION");
                if (str3 != null) {
                    this.j = str3;
                }
                String str4 = mapD.get("BUGLY_APP_CHANNEL");
                if (str4 != null) {
                    this.l = str4;
                }
                String str5 = mapD.get("BUGLY_ENABLE_DEBUG");
                if (str5 != null) {
                    this.u = str5.equalsIgnoreCase("true");
                }
                String str6 = mapD.get("com.tencent.rdm.uuid");
                if (str6 != null) {
                    this.x = str6;
                }
            } catch (Throwable th2) {
                if (!x.a(th2)) {
                    th2.printStackTrace();
                }
            }
        }
        try {
            if (!context.getDatabasePath("bugly_db_").exists()) {
                this.z = true;
                x.c("App is first time to be installed on the device.", new Object[0]);
            }
        } catch (Throwable th3) {
            if (com.tencent.bugly.b.c) {
                th3.printStackTrace();
            }
        }
        this.E = z.a("BUGLY_COMMON_VALUES", context);
        x.c("com info create end", new Object[0]);
    }

    public final boolean a() {
        return this.aj;
    }

    public final void a(boolean z) {
        this.aj = z;
        if (this.D != null) {
            this.D.setNativeIsAppForeground(z);
        }
    }

    public static synchronized a a(Context context) {
        if (ad == null) {
            ad = new a(context);
        }
        return ad;
    }

    public static synchronized a b() {
        return ad;
    }

    public static String c() {
        return "2.6.6";
    }

    public final void d() {
        synchronized (this.ap) {
            this.G = UUID.randomUUID().toString();
        }
    }

    public final String e() {
        if (this.G == null) {
            synchronized (this.ap) {
                if (this.G == null) {
                    this.G = UUID.randomUUID().toString();
                }
            }
        }
        return this.G;
    }

    public final String f() {
        if (z.a((String) null)) {
            return this.X;
        }
        return null;
    }

    public final void a(String str) {
        this.X = str;
    }

    public final String g() {
        String str;
        synchronized (this.au) {
            str = this.I;
        }
        return str;
    }

    public final void b(String str) {
        synchronized (this.au) {
            if (str == null) {
                str = "10000";
            }
            this.I = str;
        }
    }

    public final String h() {
        if (this.H != null) {
            return this.H;
        }
        this.H = k() + PinyinConverter.PINYIN_EXCLUDE + m() + PinyinConverter.PINYIN_EXCLUDE + n();
        return this.H;
    }

    public final void c(String str) {
        this.H = str;
        synchronized (this.av) {
            this.ah.put("E8", str);
        }
    }

    public final synchronized String i() {
        return this.J;
    }

    public final synchronized void d(String str) {
        this.J = str;
    }

    public final synchronized String j() {
        return this.K;
    }

    public final synchronized void e(String str) {
        this.K = str;
    }

    public final String k() {
        if (!this.V) {
            return "";
        }
        if (this.L == null) {
            this.L = b.a(this.F);
        }
        return this.L;
    }

    public final String l() {
        if (!this.V) {
            return "";
        }
        if (this.M == null || !this.M.contains(":")) {
            this.M = b.d(this.F);
        }
        return this.M;
    }

    public final String m() {
        if (!this.V) {
            return "";
        }
        if (this.N == null) {
            this.N = b.b(this.F);
        }
        return this.N;
    }

    public final String n() {
        if (!this.V) {
            return "";
        }
        if (this.O == null) {
            this.O = b.c(this.F);
        }
        return this.O;
    }

    public final long o() {
        if (this.P <= 0) {
            this.P = b.e();
        }
        return this.P;
    }

    public final long p() {
        if (this.Q <= 0) {
            this.Q = b.g();
        }
        return this.Q;
    }

    public final long q() {
        if (this.R <= 0) {
            this.R = b.i();
        }
        return this.R;
    }

    public final String r() {
        if (this.S == null) {
            this.S = b.a(true);
        }
        return this.S;
    }

    public final String s() {
        if (this.T == null) {
            this.T = b.h(this.F);
        }
        return this.T;
    }

    public final void a(String str, String str2) {
        if (str != null && str2 != null) {
            synchronized (this.aq) {
                this.A.put(str, str2);
            }
        }
    }

    public final String t() {
        try {
            Map<String, ?> all = this.F.getSharedPreferences("BuglySdkInfos", 0).getAll();
            if (!all.isEmpty()) {
                synchronized (this.aq) {
                    for (Map.Entry<String, ?> entry : all.entrySet()) {
                        try {
                            this.A.put(entry.getKey(), entry.getValue().toString());
                        } catch (Throwable th) {
                            x.a(th);
                        }
                    }
                }
            }
        } catch (Throwable th2) {
            x.a(th2);
        }
        if (this.A.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry2 : this.A.entrySet()) {
            sb.append("[");
            sb.append(entry2.getKey());
            sb.append(",");
            sb.append(entry2.getValue());
            sb.append("] ");
        }
        c("SDK_INFO", sb.toString());
        return sb.toString();
    }

    public final String u() {
        if (this.ao == null) {
            this.ao = AppInfo.e(this.F);
        }
        return this.ao;
    }

    public final synchronized Map<String, PlugInBean> v() {
        return null;
    }

    public final String w() {
        if (this.W == null) {
            this.W = b.k();
        }
        return this.W;
    }

    public final Boolean x() {
        if (this.Y == null) {
            this.Y = Boolean.valueOf(b.i(this.F));
        }
        return this.Y;
    }

    public final String y() {
        if (this.Z == null) {
            this.Z = b.g(this.F);
            x.a("ROM ID: %s", this.Z);
        }
        return this.Z;
    }

    public final String z() {
        if (this.aa == null) {
            this.aa = b.e(this.F);
            x.a("SIM serial number: %s", this.aa);
        }
        return this.aa;
    }

    public final String A() {
        if (this.ab == null) {
            this.ab = b.d();
            x.a("Hardware serial number: %s", this.ab);
        }
        return this.ab;
    }

    public final Map<String, String> B() {
        HashMap map;
        synchronized (this.ar) {
            map = this.ag.size() <= 0 ? null : new HashMap(this.ag);
        }
        return map;
    }

    public final String f(String str) {
        String strRemove;
        if (z.a(str)) {
            x.d("key should not be empty %s", str);
            return null;
        }
        synchronized (this.ar) {
            strRemove = this.ag.remove(str);
        }
        return strRemove;
    }

    public final void C() {
        synchronized (this.ar) {
            this.ag.clear();
        }
    }

    public final String g(String str) {
        String str2;
        if (z.a(str)) {
            x.d("key should not be empty %s", str);
            return null;
        }
        synchronized (this.ar) {
            str2 = this.ag.get(str);
        }
        return str2;
    }

    public final void b(String str, String str2) {
        if (z.a(str) || z.a(str2)) {
            x.d("key&value should not be empty %s %s", str, str2);
            return;
        }
        synchronized (this.ar) {
            this.ag.put(str, str2);
        }
    }

    public final int D() {
        int size;
        synchronized (this.ar) {
            size = this.ag.size();
        }
        return size;
    }

    public final Set<String> E() {
        Set<String> setKeySet;
        synchronized (this.ar) {
            setKeySet = this.ag.keySet();
        }
        return setKeySet;
    }

    public final Map<String, String> F() {
        HashMap map;
        synchronized (this.av) {
            map = this.ah.size() <= 0 ? null : new HashMap(this.ah);
        }
        return map;
    }

    public final void c(String str, String str2) {
        if (z.a(str) || z.a(str2)) {
            x.d("server key&value should not be empty %s %s", str, str2);
            return;
        }
        synchronized (this.as) {
            this.ai.put(str, str2);
        }
    }

    public final Map<String, String> G() {
        HashMap map;
        synchronized (this.as) {
            map = this.ai.size() <= 0 ? null : new HashMap(this.ai);
        }
        return map;
    }

    public final void a(int i) {
        synchronized (this.at) {
            int i2 = this.ae;
            if (i2 != i) {
                this.ae = i;
                x.a("user scene tag %d changed to tag %d", Integer.valueOf(i2), Integer.valueOf(this.ae));
            }
        }
    }

    public final int H() {
        int i;
        synchronized (this.at) {
            i = this.ae;
        }
        return i;
    }

    public final void b(int i) {
        int i2 = this.af;
        if (i2 != 24096) {
            this.af = 24096;
            x.a("server scene tag %d changed to tag %d", Integer.valueOf(i2), Integer.valueOf(this.af));
        }
    }

    public final int I() {
        return this.af;
    }

    public final boolean J() {
        return AppInfo.f(this.F);
    }

    public final synchronized Map<String, PlugInBean> K() {
        return null;
    }

    public static int L() {
        return b.c();
    }

    public final String M() {
        if (this.ak == null) {
            this.ak = b.m();
        }
        return this.ak;
    }

    public final String N() {
        if (this.al == null) {
            this.al = b.j(this.F);
        }
        return this.al;
    }

    public final String O() {
        if (this.am == null) {
            this.am = b.k(this.F);
        }
        return this.am;
    }

    public final String P() {
        Context context = this.F;
        return b.n();
    }

    public final String Q() {
        if (this.an == null) {
            this.an = b.l(this.F);
        }
        return this.an;
    }

    public final long R() {
        Context context = this.F;
        return b.o();
    }
}
