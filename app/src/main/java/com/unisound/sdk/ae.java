package com.unisound.sdk;

import cn.yunzhisheng.asr.JniUscClient;
import com.unisound.vui.handler.session.memo.utils.MemoConstants;
import java.text.SimpleDateFormat;

/* loaded from: classes.dex */
public class ae {

    /* renamed from: a, reason: collision with root package name */
    public static final String f294a = "filterName";
    public static final String b = "returnType";
    public static final String c = "city";
    public static final String d = "gps";
    public static final String e = "time";
    public static final String f = "scenario";
    public static final String g = "screen";
    public static final String h = "dpi";
    public static final String i = "history";
    public static final String j = "udid";
    public static final String k = "ver";
    public static final String l = "appver";
    public static final String m = "oneshotKeyProperty";
    public static String n = "http://scv2.hivoice.cn/service/iss";
    private String A;
    private String B;
    private String C;
    private String D;
    private String E;
    private String F;
    private final String o;
    private String p;
    private String q;
    private String r;
    private String s;
    private String t;
    private String u;
    private String v;
    private String w;
    private String x;
    private String y;
    private String z;

    public ae() {
        this.o = "iss.getTalk";
        this.p = "2.0";
        this.q = "";
        this.r = "";
        this.s = "";
        this.t = "";
        this.u = "";
        this.v = "";
        this.w = a(System.currentTimeMillis());
        this.x = "";
        this.y = "";
        this.z = "";
        this.A = "";
        this.B = "";
        this.C = "";
        this.D = "";
        this.E = "";
        this.F = JniUscClient.aA;
    }

    public ae(String str, String str2) {
        this.o = "iss.getTalk";
        this.p = "2.0";
        this.q = "";
        this.r = "";
        this.s = "";
        this.t = "";
        this.u = "";
        this.v = "";
        this.w = a(System.currentTimeMillis());
        this.x = "";
        this.y = "";
        this.z = "";
        this.A = "";
        this.B = "";
        this.C = "";
        this.D = "";
        this.E = "";
        this.F = JniUscClient.aA;
        this.q = str;
        this.r = str2;
    }

    public ae(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13) {
        this.o = "iss.getTalk";
        this.p = "2.0";
        this.q = "";
        this.r = "";
        this.s = "";
        this.t = "";
        this.u = "";
        this.v = "";
        this.w = a(System.currentTimeMillis());
        this.x = "";
        this.y = "";
        this.z = "";
        this.A = "";
        this.B = "";
        this.C = "";
        this.D = "";
        this.E = "";
        this.F = JniUscClient.aA;
        this.q = str;
        this.r = str2;
        this.t = str3;
        this.s = str4;
        this.u = str5;
        this.x = str6;
        this.y = str7;
        this.v = str8;
        this.w = str9;
        this.z = str10;
        this.A = str11;
        this.B = str12;
        this.C = str13;
    }

    public static String a(long j2) {
        return new SimpleDateFormat(MemoConstants.DATE_FORMATE_YMDHMS).format(Long.valueOf(j2));
    }

    public String a() {
        return n;
    }

    public void a(ae aeVar) {
        this.q = aeVar.b();
        this.s = aeVar.g();
        this.v = aeVar.j();
        this.C = aeVar.p();
        this.u = aeVar.f();
        this.y = aeVar.i();
        this.D = aeVar.q();
        this.A = aeVar.n();
        this.B = aeVar.o();
        this.r = aeVar.c();
        this.x = aeVar.h();
        this.w = aeVar.k();
        this.t = aeVar.e();
        this.p = aeVar.d();
        this.s = aeVar.g();
        this.E = aeVar.r();
        this.z = aeVar.m();
    }

    public void a(String str) {
        n = str;
    }

    public String b() {
        return this.q;
    }

    public void b(long j2) {
        this.w = a(j2);
    }

    public void b(String str) {
        this.q = str;
    }

    public String c() {
        return this.r;
    }

    public void c(String str) {
        this.r = str;
    }

    public String d() {
        return this.p;
    }

    public void d(String str) {
        this.p = str;
    }

    public String e() {
        return com.unisound.c.a.a(this.q);
    }

    public void e(String str) {
        this.u = str;
    }

    public String f() {
        return this.u;
    }

    public void f(String str) {
        this.s = str;
    }

    public String g() {
        return this.s;
    }

    public void g(String str) {
        this.x = str;
    }

    public String h() {
        return this.x;
    }

    public void h(String str) {
        this.y = str;
    }

    public String i() {
        return this.y;
    }

    public void i(String str) {
        this.v = str;
    }

    public String j() {
        return this.v;
    }

    public void j(String str) {
        this.z = str;
    }

    public String k() {
        return this.w;
    }

    public void k(String str) {
        this.A = str;
    }

    public String l() {
        return "iss.getTalk";
    }

    public void l(String str) {
        this.B = str;
    }

    public String m() {
        return this.z;
    }

    public void m(String str) {
        this.C = str;
    }

    public String n() {
        return this.A;
    }

    public void n(String str) {
        this.D = str;
    }

    public String o() {
        return this.B;
    }

    public void o(String str) {
        this.E = str;
    }

    public String p() {
        return this.C;
    }

    public void p(String str) {
        this.F = str;
    }

    public String q() {
        return this.D;
    }

    public String r() {
        return this.E;
    }

    public String s() {
        return this.F;
    }
}
