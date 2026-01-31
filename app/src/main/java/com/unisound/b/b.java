package com.unisound.b;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.phicomm.speaker.device.BuildConfig;
import com.unisound.common.x;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class b {
    private static final String C = "systemResponese";
    private static String z = "";
    private Context A;
    private a B;
    private String c;

    /* renamed from: a, reason: collision with root package name */
    private String f217a = "http://dc.hivoice.cn/rest/v2/device/activate";
    private String b = "http://dc.hivoice.cn/rest/v2/token/refresh";
    private String d = "";
    private String e = "";
    private String f = "";
    private String g = "";
    private String h = "";
    private String i = "";
    private String j = "";
    private String k = "";
    private String l = "";
    private String m = "";
    private String n = "";
    private String o = "";
    private String p = "";
    private String q = "";
    private String r = "Android";
    private String s = "";
    private String t = "";
    private String u = "";
    private String v = "";
    private String w = "";
    private String x = "";
    private String y = "";
    private c D = c.FINISH;

    public b(Context context) {
        this.A = null;
        this.A = context;
        this.B = new a(context.getMainLooper());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<String, String> A() {
        HashMap map = new HashMap();
        map.put("udid", this.d != null ? this.d : "");
        map.put("deviceSn", this.e != null ? this.e : "");
        map.put(x.f272a, this.f != null ? this.f : "");
        map.put("timestamp", E());
        map.put("appVersion", this.h != null ? this.h : "");
        map.put("pkgName", this.i != null ? this.i : "");
        map.put(x.b, this.j != null ? this.j : "");
        map.put("macAddress", this.k != null ? this.k : "");
        map.put("wifiSsid", this.l != null ? this.l : "");
        map.put("telecomOperator", this.m != null ? this.m : "");
        map.put("bssId", this.n != null ? this.n : "");
        map.put("productName", this.o != null ? this.o : "");
        map.put("productModel", this.p != null ? this.p : "");
        map.put("productMfr", this.q != null ? this.q : "");
        map.put("productOs", this.r != null ? this.r : "");
        map.put("productOsVersion", this.s != null ? this.s : "");
        map.put("hardwareSn", this.t != null ? this.t : "");
        map.put("memo", this.u != null ? this.u : "");
        map.put("signature", C());
        return map;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<String, String> B() {
        HashMap map = new HashMap();
        map.put("udid", this.d != null ? this.d : "");
        map.put(x.f272a, this.f != null ? this.f : "");
        map.put("token", this.x != null ? this.x : "");
        map.put("timestamp", E());
        map.put("signature", D());
        return map;
    }

    private String C() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.d != null ? this.d : "");
        arrayList.add(this.e != null ? this.e : "");
        arrayList.add(this.f != null ? this.f : "");
        arrayList.add(this.g != null ? this.g : "");
        arrayList.add(this.h != null ? this.h : "");
        arrayList.add(this.i != null ? this.i : "");
        arrayList.add(this.j != null ? this.j : "");
        arrayList.add(this.k != null ? this.k : "");
        arrayList.add(this.l != null ? this.l : "");
        arrayList.add(this.m != null ? this.m : "");
        arrayList.add(this.n != null ? this.n : "");
        arrayList.add(this.o != null ? this.o : "");
        arrayList.add(this.p != null ? this.p : "");
        arrayList.add(this.q != null ? this.q : "");
        arrayList.add(this.r != null ? this.r : "");
        arrayList.add(this.s != null ? this.s : "");
        arrayList.add(this.t != null ? this.t : "");
        arrayList.add(this.u != null ? this.u : "");
        arrayList.add(this.y != null ? this.y : "");
        return k.a(arrayList);
    }

    private String D() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.d != null ? this.d : "");
        arrayList.add(this.f != null ? this.f : "");
        arrayList.add(this.g != null ? this.g : "");
        arrayList.add(this.x != null ? this.x : "");
        arrayList.add(this.y != null ? this.y : "");
        return k.a(arrayList);
    }

    private String E() {
        long jF = F();
        if (jF != 0) {
            this.g = String.valueOf(k.a(jF + System.currentTimeMillis()));
            return this.g;
        }
        this.g = String.valueOf(k.a(System.currentTimeMillis()));
        return this.g;
    }

    private long F() {
        return this.A.getSharedPreferences(C, 0).getLong("timeDelay", 0L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(long j) {
        SharedPreferences.Editor editorEdit = this.A.getSharedPreferences(C, 0).edit();
        editorEdit.putLong("timeDelay", j);
        editorEdit.commit();
    }

    protected static void a(String str) {
        if (TextUtils.isEmpty(str)) {
            i.a("ActivatroInterface setJsonString error!");
        } else {
            z = str;
        }
    }

    private void r(String str) throws JSONException {
        JSONObject jSONObjectA = h.a(str);
        String strB = h.b(jSONObjectA, "deviceSn");
        if (strB.equals("")) {
            i.c("ActivatorInterface _deviceSn= null");
        } else {
            l(strB);
            i.c("ActivatorInterface _deviceSn= " + strB);
        }
        String strB2 = h.b(jSONObjectA, x.f272a);
        if (strB2.equals("")) {
            i.a("ActivatorInterface _appkey= null");
        } else {
            c(strB2);
            i.c("ActivatorInterface _appkey= " + strB2);
        }
        String strB3 = h.b(jSONObjectA, "appSecret");
        if (strB3.equals("")) {
            i.a("ActivatorInterface _secret= null");
        } else {
            d(strB3);
            i.c("ActivatorInterface _secret= " + strB3);
        }
        String strB4 = h.b(jSONObjectA, "appVersion");
        if (strB4.equals("")) {
            i.c("ActivatorInterface _appVersion= null");
        } else {
            f(strB4);
            i.c("ActivatorInterface _appVersion= " + strB4);
        }
        String strB5 = h.b(jSONObjectA, x.f);
        if (strB5.equals("")) {
            i.c("ActivatorInterface _packageName= null");
        } else {
            g(strB5);
            i.c("ActivatorInterface _packageName= " + strB5);
        }
        String strB6 = h.b(jSONObjectA, x.b);
        if (strB6.equals("")) {
            i.c("ActivatorInterface _imei= null");
        } else {
            e(strB6);
            i.c("ActivatorInterface _imei= " + strB6);
        }
        String strB7 = h.b(jSONObjectA, "macAddress");
        if (strB7.equals("")) {
            i.c("ActivatorInterface _macAddress= null");
        } else {
            h(strB7);
            i.c("ActivatorInterface _macAddress= " + strB7);
        }
        String strB8 = h.b(jSONObjectA, "wifiSsid");
        if (strB8.equals("")) {
            i.c("ActivatorInterface _wifiSsid= null");
        } else {
            i(strB8);
            i.c("ActivatorInterface _wifiSsid= " + strB8);
        }
        String strB9 = h.b(jSONObjectA, "telecomOperator");
        if (strB9.equals("")) {
            i.c("ActivatorInterface _telecomOperator= null");
        } else {
            j(strB9);
            i.c("ActivatorInterface _telecomOperator= " + strB9);
        }
        String strB10 = h.b(jSONObjectA, "memo");
        if (strB10.equals("")) {
            i.c("ActivatorInterface _memo= null");
        } else {
            k(strB10);
            i.c("ActivatorInterface _memo= " + strB10);
        }
        String strB11 = h.b(jSONObjectA, "token");
        if (strB11.equals("")) {
            i.c("ActivatorInterface _token= null");
        } else {
            q(strB11);
            i.c("ActivatorInterface __token= " + strB11);
        }
        boolean zA = h.a(jSONObjectA, BuildConfig.BUILD_TYPE, false);
        if (zA) {
            a(zA);
            i.c("ActivatorInterface _debug= " + zA);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void y() {
        if (this.h == "") {
            g();
        }
        if (this.j == "") {
            e();
        }
        if (this.i == "") {
            i();
        }
        if (this.k == "") {
            k();
        }
        if (this.l == "") {
            m();
        }
        if (this.m == "") {
            o();
        }
        z();
    }

    private void z() {
        this.n = "";
        this.o = "";
        this.p = "";
        this.q = "";
        this.s = "";
        this.t = "";
        try {
            this.n = com.unisound.a.a.e(this.A);
            this.o = com.unisound.a.a.d();
            this.p = com.unisound.a.a.e();
            this.q = com.unisound.a.a.f();
            this.s = com.unisound.a.a.g();
            this.t = com.unisound.a.a.h();
        } catch (Exception e) {
            i.d("activate cant createDeviceInfo");
        }
    }

    protected Context a() {
        return this.A;
    }

    public void a(int i) throws JSONException {
        if (this.D == c.RUNNING) {
            this.B.sendEmptyMessage(108);
        }
        this.D = c.RUNNING;
        switch (i) {
            case 0:
            case 1:
                r(z);
                new d(this, i).start();
                break;
            default:
                this.B.sendEmptyMessage(110);
                this.D = c.FINISH;
                break;
        }
    }

    protected void a(Context context) {
        this.A = context;
    }

    public void a(com.unisound.b.a.c cVar) {
        this.B.a(cVar);
    }

    protected void a(boolean z2) {
        i.f223a = z2;
        com.unisound.a.a.b(z2);
    }

    protected String b() {
        return this.f;
    }

    protected void b(String str) {
        this.c = str;
        this.f217a = "http://" + str + "/rest/v1/device/activate";
        this.b = "http://" + str + "/rest/v1/token/refresh";
    }

    protected String c() {
        return this.y;
    }

    protected void c(String str) {
        this.f = str;
    }

    protected String d() {
        return this.j;
    }

    protected void d(String str) {
        this.y = str;
    }

    protected void e() {
        try {
            this.j = com.unisound.a.a.b(this.A);
        } catch (Exception e) {
            this.B.sendEmptyMessage(g.g);
            i.d("activate HttpResponse result is null");
        }
    }

    protected void e(String str) {
        this.j = str;
    }

    protected String f() {
        return this.h;
    }

    protected void f(String str) {
        this.h = str;
    }

    protected void g() {
        this.h = com.unisound.a.a.f(this.A);
    }

    protected void g(String str) {
        this.i = str;
    }

    protected String h() {
        return this.i;
    }

    protected void h(String str) {
        this.k = str;
    }

    protected void i() {
        this.i = com.unisound.a.a.g(this.A);
    }

    protected void i(String str) {
        this.l = str;
    }

    protected String j() {
        return this.k;
    }

    protected void j(String str) {
        this.m = str;
    }

    protected void k() {
        try {
            this.k = com.unisound.a.a.h(this.A);
        } catch (Exception e) {
            i.d("activate setMacAddress error");
        }
    }

    protected void k(String str) {
        this.u = str;
    }

    protected String l() {
        return this.l;
    }

    public void l(String str) {
        this.e = str;
        com.unisound.a.a.b(this.e);
    }

    protected void m() {
        try {
            this.l = com.unisound.a.a.i(this.A);
        } catch (Exception e) {
            i.d("activate setWifiSsid error");
        }
    }

    public void m(String str) {
        this.g = str;
    }

    protected String n() {
        return this.m;
    }

    protected void n(String str) {
        this.d = str;
    }

    protected void o() {
        try {
            this.m = com.unisound.a.a.j(this.A);
        } catch (Exception e) {
            i.d("activate setTelecomOperator error");
        }
    }

    protected void o(String str) {
        this.v = str;
    }

    protected String p() {
        return this.u;
    }

    protected void p(String str) {
        this.w = str;
    }

    protected String q() {
        return e.a();
    }

    public void q(String str) {
        this.x = str;
    }

    public String r() {
        return this.e;
    }

    public String s() {
        return this.g;
    }

    protected String t() {
        return this.d;
    }

    public String u() {
        return this.v;
    }

    public String v() {
        return this.w;
    }

    public String w() {
        return this.x;
    }
}
