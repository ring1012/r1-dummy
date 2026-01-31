package com.unisound.sdk;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class i {
    private static final String A = "sampleRate";
    private static final String B = "oneshot";
    private static final String C = "oneshot_key";
    private static final String D = "alread_awpe";
    private static final String E = "productLine";
    private static final String F = "punctuated";

    /* renamed from: a, reason: collision with root package name */
    public static final int f345a = 8000;
    public static final int b = 16000;
    public static final int c = 80000;
    public static final String d = "far";
    public static final String e = "near";
    public static final String f = "8k";
    public static final String g = "16k";
    public static final String h = "16kto8k";
    public static final String i = "general";
    public static final String j = "poi";
    public static final String k = "food";
    public static final String l = "medical";
    public static final String m = "movietv";
    public static final String n = "textFormat";
    public static final String o = "serverVad";
    public static final String p = "best_result_return";
    public static final String q = "false_alarm";
    public static final String r = "domains_penalty";
    public static final String s = "SGN_Setting";
    public static final String t = "en";
    public static final String u = "co";
    public static final String v = "cn";
    private static final String w = "modelType";
    private static final String x = "subModel";
    private static final String y = "voiceField";
    private static final String z = "lang";
    private Map<String, String> G = new HashMap();
    private StringBuffer H = new StringBuffer();
    private boolean I = true;
    private int J = 16000;
    private boolean K = true;
    private boolean L = true;

    public i() {
        d(e);
        a(16000);
        d(false);
        f("");
        g("json");
    }

    private void g() {
        this.I = true;
    }

    private void h() {
        if (this.I) {
            this.I = false;
            this.H.delete(0, this.H.length());
            for (String str : this.G.keySet()) {
                this.H.append(str).append(":");
                this.H.append(this.G.get(str)).append("\n");
            }
        }
    }

    public void a(String str) {
        this.G.put(z, str);
        g();
    }

    public void a(String str, String str2) {
        this.G.put(str, String.valueOf(str2));
        g();
    }

    public void a(boolean z2) {
        this.K = z2;
    }

    public boolean a() {
        return this.K;
    }

    public boolean a(int i2) {
        switch (i2) {
            case 8000:
                b();
                return true;
            case 16000:
                d();
                return true;
            case c /* 80000 */:
                c();
                return true;
            default:
                com.unisound.common.y.a(toString() + ".setSampleRate param error " + i2);
                return false;
        }
    }

    public void b() {
        e(h);
        this.J = 8000;
    }

    public void b(int i2) {
        this.G.put(q, String.valueOf(i2));
        g();
    }

    public void b(boolean z2) {
        this.L = z2;
    }

    public boolean b(String str) {
        this.G.put(w, str);
        g();
        return true;
    }

    public void c() {
        e(f);
        this.J = c;
    }

    public void c(int i2) {
        this.G.put(r, String.valueOf(i2));
        g();
    }

    public boolean c(String str) {
        this.G.put(x, str);
        g();
        return true;
    }

    public boolean c(boolean z2) {
        return this.L;
    }

    public void d() {
        e(g);
        this.J = 16000;
    }

    public void d(String str) {
        this.G.put(y, str);
        g();
    }

    public void d(boolean z2) {
        this.G.put(B, String.valueOf(z2));
        g();
    }

    public void e() {
        this.H.delete(0, this.H.length());
        this.G.clear();
    }

    public void e(String str) {
        this.G.put(A, str);
        g();
    }

    public void e(boolean z2) {
        this.G.put(D, String.valueOf(z2));
        g();
    }

    public int f() {
        return this.J;
    }

    public void f(String str) {
        this.G.put(C, str);
        g();
    }

    public void f(boolean z2) {
        this.G.put(o, String.valueOf(z2));
        g();
    }

    public void g(String str) {
        this.G.put(n, str);
        g();
    }

    public void g(boolean z2) {
        this.G.put(F, String.valueOf(z2));
        g();
    }

    public void h(String str) {
        this.G.put(s, String.valueOf(str));
        g();
    }

    public void h(boolean z2) {
        this.G.put(p, String.valueOf(z2));
        g();
    }

    public void i(String str) {
        this.G.put(E, str);
        g();
    }

    public String toString() {
        h();
        return this.H.toString();
    }
}
