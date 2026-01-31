package com.unisound.sdk;

/* loaded from: classes.dex */
class cl extends ae {
    private String o;
    private boolean p;
    private String q;
    private String r;
    private boolean s;
    private int t;

    public cl() {
        this.o = "search";
        this.p = true;
        this.q = "json";
        this.r = n;
        this.s = false;
        this.t = -1;
    }

    public cl(String str, String str2) {
        super(str, str2);
        this.o = "search";
        this.p = true;
        this.q = "json";
        this.r = n;
        this.s = false;
        this.t = -1;
    }

    public String A() {
        return "returnType=" + v() + ";city=" + j() + ";gps=" + f() + ";time=" + k() + ";scenario=" + n() + ";screen=" + o() + ";dpi=" + p() + ";history=" + i() + ";udid=" + e() + ";ver=" + d() + ";appver=" + g() + ";" + ae.m + "=" + s() + ";";
    }

    public void a(int i) {
        this.t = i;
    }

    public void a(cl clVar) {
        super.a((ae) clVar);
        this.o = clVar.u();
        this.p = clVar.w();
        this.r = clVar.t();
        this.q = clVar.v();
    }

    public void a(boolean z) {
        this.p = z;
    }

    public boolean a(String str, int i) {
        if (str == null) {
            return false;
        }
        this.r = "http://" + str + ":" + i + "/service/iss";
        return true;
    }

    public void b(boolean z) {
        this.s = z;
    }

    public void q(String str) {
        this.r = str;
    }

    public boolean r(String str) {
        return false;
    }

    public void s(String str) {
        this.o = str;
    }

    public String t() {
        return this.r;
    }

    public void t(String str) {
        this.q = str;
    }

    public String u() {
        return this.o;
    }

    public String v() {
        return this.q;
    }

    public boolean w() {
        return this.p;
    }

    public cl x() {
        return this;
    }

    public boolean y() {
        return this.s;
    }

    public int z() {
        return this.t;
    }
}
