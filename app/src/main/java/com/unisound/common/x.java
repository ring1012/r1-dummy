package com.unisound.common;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class x {

    /* renamed from: a, reason: collision with root package name */
    public static final String f272a = "appKey";
    public static final String b = "imei";
    public static final String c = "sessionID";
    public static final String d = "sessionId";
    public static final String e = "timeStamp";
    public static final String f = "packageName";
    public static final String g = "status";
    public static final String h = "errString";
    public static final String i = "javaSDKLog";
    public static final String j = "cSDKLog";
    private static final String k = "http://log.hivoice.cn/trace/basicService/";
    private String l;
    private String m;
    private String n;
    private String o;
    private String p;
    private int q;
    private String r;
    private String s;
    private String t;

    public x() {
        this.l = "0";
        this.m = "0";
        this.n = "0";
        this.o = "0";
        this.p = "";
        this.q = 0;
        this.r = "";
        this.s = "";
        this.t = "";
    }

    public x(String str, String str2, String str3, String str4, String str5, int i2, String str6, String str7, String str8) {
        this.l = "0";
        this.m = "0";
        this.n = "0";
        this.o = "0";
        this.p = "";
        this.q = 0;
        this.r = "";
        this.s = "";
        this.t = "";
        this.l = str;
        this.m = str2;
        this.n = str3;
        this.o = str4;
        this.p = str5;
        this.q = i2;
        this.r = str6;
        this.s = str7;
        this.t = str8;
    }

    public String a() {
        return k;
    }

    public void a(int i2) {
        this.q = i2;
    }

    public void a(String str) {
        this.l = str;
    }

    public String b() {
        return this.l;
    }

    public void b(String str) {
        this.m = str;
    }

    public String c() {
        return this.m;
    }

    public void c(String str) {
        this.n = str;
    }

    public String d() {
        return this.n;
    }

    public void d(String str) {
        this.o = str;
    }

    public String e() {
        return this.o;
    }

    public void e(String str) {
        this.p = str;
    }

    public String f() {
        return this.p;
    }

    public void f(String str) {
        this.r = str;
    }

    public int g() {
        return this.q;
    }

    public void g(String str) {
        this.s = str;
    }

    public String h() {
        return this.r;
    }

    public void h(String str) {
        this.t = str;
    }

    public String i() {
        return this.s;
    }

    public String j() {
        return this.t;
    }

    public String k() throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(d, this.n);
            jSONObject.put(f, this.p);
            jSONObject.put("status", this.q);
            jSONObject.put(h, this.r);
            byte[] bytes = this.s.getBytes();
            JSONArray jSONArray = new JSONArray();
            for (int i2 = 0; i2 < bytes.length; i2++) {
                jSONArray.put(i2, (int) bytes[i2]);
            }
            jSONObject.put(i, jSONArray);
            JSONArray jSONArray2 = new JSONArray();
            byte[] bytes2 = this.t.getBytes();
            for (int i3 = 0; i3 < bytes2.length; i3++) {
                jSONArray2.put(i3, (int) bytes2[i3]);
            }
            jSONObject.put(j, jSONArray2);
            return jSONObject.toString();
        } catch (JSONException e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public String l() {
        HashMap map = new HashMap();
        map.put(f272a, this.l);
        map.put(b, this.m);
        map.put(c, this.n);
        y.c("BASEURL = ", k);
        return aj.a(k, map);
    }
}
