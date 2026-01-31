package com.baidu.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import cn.yunzhisheng.asr.JniUscClient;
import com.baidu.location.a;
import com.unisound.common.y;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class c implements Parcelable {
    public static final Parcelable.Creator<c> CREATOR = new j();
    private int A;
    private String B;
    private int C;
    private String D;
    private int E;
    private int F;
    private int G;
    private int H;
    private String I;
    private String J;
    private String K;
    private List<i> L;
    private String M;
    private String N;
    private HashMap<String, String> O;
    private int P;
    private int Q;

    /* renamed from: a, reason: collision with root package name */
    private int f98a;
    private String b;
    private double c;
    private double d;
    private boolean e;
    private double f;
    private boolean g;
    private float h;
    private boolean i;
    private float j;
    private boolean k;
    private int l;
    private float m;
    private String n;
    private boolean o;
    private String p;
    private String q;
    private String r;
    private String s;
    private boolean t;
    private a u;
    private String v;
    private String w;
    private String x;
    private boolean y;
    private int z;

    public c() {
        this.f98a = 0;
        this.b = null;
        this.c = Double.MIN_VALUE;
        this.d = Double.MIN_VALUE;
        this.e = false;
        this.f = Double.MIN_VALUE;
        this.g = false;
        this.h = 0.0f;
        this.i = false;
        this.j = 0.0f;
        this.k = false;
        this.l = -1;
        this.m = -1.0f;
        this.n = null;
        this.o = false;
        this.p = null;
        this.q = null;
        this.r = null;
        this.s = null;
        this.t = false;
        this.u = new a.C0004a().a();
        this.v = null;
        this.w = null;
        this.x = null;
        this.y = false;
        this.z = 0;
        this.A = 1;
        this.B = null;
        this.D = "";
        this.E = -1;
        this.F = 0;
        this.G = 2;
        this.H = 0;
        this.I = null;
        this.J = null;
        this.K = null;
        this.L = null;
        this.M = null;
        this.N = null;
        this.O = new HashMap<>();
        this.P = 0;
        this.Q = 0;
    }

    private c(Parcel parcel) {
        this.f98a = 0;
        this.b = null;
        this.c = Double.MIN_VALUE;
        this.d = Double.MIN_VALUE;
        this.e = false;
        this.f = Double.MIN_VALUE;
        this.g = false;
        this.h = 0.0f;
        this.i = false;
        this.j = 0.0f;
        this.k = false;
        this.l = -1;
        this.m = -1.0f;
        this.n = null;
        this.o = false;
        this.p = null;
        this.q = null;
        this.r = null;
        this.s = null;
        this.t = false;
        this.u = new a.C0004a().a();
        this.v = null;
        this.w = null;
        this.x = null;
        this.y = false;
        this.z = 0;
        this.A = 1;
        this.B = null;
        this.D = "";
        this.E = -1;
        this.F = 0;
        this.G = 2;
        this.H = 0;
        this.I = null;
        this.J = null;
        this.K = null;
        this.L = null;
        this.M = null;
        this.N = null;
        this.O = new HashMap<>();
        this.P = 0;
        this.Q = 0;
        this.f98a = parcel.readInt();
        this.b = parcel.readString();
        this.c = parcel.readDouble();
        this.d = parcel.readDouble();
        this.f = parcel.readDouble();
        this.h = parcel.readFloat();
        this.j = parcel.readFloat();
        this.l = parcel.readInt();
        this.m = parcel.readFloat();
        this.v = parcel.readString();
        this.z = parcel.readInt();
        this.w = parcel.readString();
        this.x = parcel.readString();
        this.B = parcel.readString();
        String string = parcel.readString();
        String string2 = parcel.readString();
        String string3 = parcel.readString();
        String string4 = parcel.readString();
        String string5 = parcel.readString();
        String string6 = parcel.readString();
        parcel.readString();
        String string7 = parcel.readString();
        String string8 = parcel.readString();
        this.u = new a.C0004a().a(string7).c(string8).d(string).e(string2).f(string6).g(string3).h(string4).i(string5).b(parcel.readString()).a();
        boolean[] zArr = new boolean[7];
        this.C = parcel.readInt();
        this.D = parcel.readString();
        this.q = parcel.readString();
        this.r = parcel.readString();
        this.s = parcel.readString();
        this.A = parcel.readInt();
        this.M = parcel.readString();
        this.E = parcel.readInt();
        this.F = parcel.readInt();
        this.G = parcel.readInt();
        this.H = parcel.readInt();
        this.I = parcel.readString();
        this.J = parcel.readString();
        this.K = parcel.readString();
        this.P = parcel.readInt();
        this.N = parcel.readString();
        this.Q = parcel.readInt();
        try {
            parcel.readBooleanArray(zArr);
            this.e = zArr[0];
            this.g = zArr[1];
            this.i = zArr[2];
            this.k = zArr[3];
            this.o = zArr[4];
            this.t = zArr[5];
            this.y = zArr[6];
        } catch (Exception e) {
        }
        ArrayList arrayList = new ArrayList();
        parcel.readList(arrayList, i.class.getClassLoader());
        if (arrayList.size() == 0) {
            this.L = null;
        } else {
            this.L = arrayList;
        }
    }

    /* synthetic */ c(Parcel parcel, j jVar) {
        this(parcel);
    }

    public c(c cVar) {
        int i = 0;
        this.f98a = 0;
        this.b = null;
        this.c = Double.MIN_VALUE;
        this.d = Double.MIN_VALUE;
        this.e = false;
        this.f = Double.MIN_VALUE;
        this.g = false;
        this.h = 0.0f;
        this.i = false;
        this.j = 0.0f;
        this.k = false;
        this.l = -1;
        this.m = -1.0f;
        this.n = null;
        this.o = false;
        this.p = null;
        this.q = null;
        this.r = null;
        this.s = null;
        this.t = false;
        this.u = new a.C0004a().a();
        this.v = null;
        this.w = null;
        this.x = null;
        this.y = false;
        this.z = 0;
        this.A = 1;
        this.B = null;
        this.D = "";
        this.E = -1;
        this.F = 0;
        this.G = 2;
        this.H = 0;
        this.I = null;
        this.J = null;
        this.K = null;
        this.L = null;
        this.M = null;
        this.N = null;
        this.O = new HashMap<>();
        this.P = 0;
        this.Q = 0;
        this.f98a = cVar.f98a;
        this.b = cVar.b;
        this.c = cVar.c;
        this.d = cVar.d;
        this.e = cVar.e;
        this.f = cVar.f;
        this.g = cVar.g;
        this.h = cVar.h;
        this.i = cVar.i;
        this.j = cVar.j;
        this.k = cVar.k;
        this.l = cVar.l;
        this.m = cVar.m;
        this.n = cVar.n;
        this.o = cVar.o;
        this.p = cVar.p;
        this.t = cVar.t;
        this.u = new a.C0004a().a(cVar.u.f52a).c(cVar.u.b).d(cVar.u.c).e(cVar.u.d).f(cVar.u.e).g(cVar.u.f).h(cVar.u.g).i(cVar.u.h).b(cVar.u.j).a();
        this.v = cVar.v;
        this.w = cVar.w;
        this.x = cVar.x;
        this.A = cVar.A;
        this.z = cVar.z;
        this.y = cVar.y;
        this.B = cVar.B;
        this.C = cVar.C;
        this.D = cVar.D;
        this.q = cVar.q;
        this.r = cVar.r;
        this.s = cVar.s;
        this.E = cVar.E;
        this.F = cVar.F;
        this.G = cVar.F;
        this.H = cVar.H;
        this.I = cVar.I;
        this.J = cVar.J;
        this.K = cVar.K;
        this.P = cVar.P;
        this.N = cVar.N;
        if (cVar.L == null) {
            this.L = null;
        } else {
            ArrayList arrayList = new ArrayList();
            while (true) {
                int i2 = i;
                if (i2 >= cVar.L.size()) {
                    break;
                }
                i iVar = cVar.L.get(i2);
                arrayList.add(new i(iVar.a(), iVar.c(), iVar.b()));
                i = i2 + 1;
            }
            this.L = arrayList;
        }
        this.M = cVar.M;
        this.O = cVar.O;
        this.Q = cVar.Q;
    }

    public c(String str) throws JSONException, NumberFormatException {
        JSONObject jSONObject;
        String string;
        String string2;
        String string3;
        String string4;
        String string5;
        String string6;
        String string7;
        String string8;
        this.f98a = 0;
        this.b = null;
        this.c = Double.MIN_VALUE;
        this.d = Double.MIN_VALUE;
        this.e = false;
        this.f = Double.MIN_VALUE;
        this.g = false;
        this.h = 0.0f;
        this.i = false;
        this.j = 0.0f;
        this.k = false;
        this.l = -1;
        this.m = -1.0f;
        this.n = null;
        this.o = false;
        this.p = null;
        this.q = null;
        this.r = null;
        this.s = null;
        this.t = false;
        this.u = new a.C0004a().a();
        this.v = null;
        this.w = null;
        this.x = null;
        this.y = false;
        this.z = 0;
        this.A = 1;
        this.B = null;
        this.D = "";
        this.E = -1;
        this.F = 0;
        this.G = 2;
        this.H = 0;
        this.I = null;
        this.J = null;
        this.K = null;
        this.L = null;
        this.M = null;
        this.N = null;
        this.O = new HashMap<>();
        this.P = 0;
        this.Q = 0;
        if (str == null || str.equals("")) {
            return;
        }
        try {
            try {
                JSONObject jSONObject2 = new JSONObject(str);
                JSONObject jSONObject3 = jSONObject2.getJSONObject("result");
                int i = Integer.parseInt(jSONObject3.getString(y.I));
                d(i);
                b(jSONObject3.getString("time"));
                if (i == 61) {
                    JSONObject jSONObject4 = jSONObject2.getJSONObject("content");
                    JSONObject jSONObject5 = jSONObject4.getJSONObject("point");
                    a(Double.parseDouble(jSONObject5.getString("y")));
                    b(Double.parseDouble(jSONObject5.getString("x")));
                    b(Float.parseFloat(jSONObject4.getString("radius")));
                    a(Float.parseFloat(jSONObject4.getString("s")));
                    c(Float.parseFloat(jSONObject4.getString("d")));
                    e(Integer.parseInt(jSONObject4.getString("n")));
                    if (jSONObject4.has("h")) {
                        try {
                            c(jSONObject4.getDouble("h"));
                        } catch (Exception e) {
                        }
                    }
                    try {
                        if (jSONObject4.has("in_cn")) {
                            f(Integer.parseInt(jSONObject4.getString("in_cn")));
                        } else {
                            f(1);
                        }
                    } catch (Exception e2) {
                    }
                    if (this.A == 0) {
                        d("wgs84");
                        return;
                    } else {
                        d("gcj02");
                        return;
                    }
                }
                if (i != 161) {
                    if (i != 66 && i != 68) {
                        if (i == 167) {
                            f(2);
                            return;
                        }
                        return;
                    }
                    JSONObject jSONObject6 = jSONObject2.getJSONObject("content");
                    JSONObject jSONObject7 = jSONObject6.getJSONObject("point");
                    a(Double.parseDouble(jSONObject7.getString("y")));
                    b(Double.parseDouble(jSONObject7.getString("x")));
                    b(Float.parseFloat(jSONObject6.getString("radius")));
                    a(Boolean.valueOf(Boolean.parseBoolean(jSONObject6.getString("isCellChanged"))));
                    d("gcj02");
                    return;
                }
                JSONObject jSONObject8 = jSONObject2.getJSONObject("content");
                JSONObject jSONObject9 = jSONObject8.getJSONObject("point");
                a(Double.parseDouble(jSONObject9.getString("y")));
                b(Double.parseDouble(jSONObject9.getString("x")));
                b(Float.parseFloat(jSONObject8.getString("radius")));
                if (jSONObject8.has("sema")) {
                    JSONObject jSONObject10 = jSONObject8.getJSONObject("sema");
                    if (jSONObject10.has("aptag")) {
                        String string9 = jSONObject10.getString("aptag");
                        if (TextUtils.isEmpty(string9)) {
                            this.q = "";
                        } else {
                            this.q = string9;
                        }
                    }
                    if (jSONObject10.has("aptagd")) {
                        JSONArray jSONArray = jSONObject10.getJSONObject("aptagd").getJSONArray("pois");
                        ArrayList arrayList = new ArrayList();
                        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                            JSONObject jSONObject11 = jSONArray.getJSONObject(i2);
                            arrayList.add(new i(jSONObject11.getString("pid"), jSONObject11.getString("pname"), jSONObject11.getDouble("pr")));
                        }
                        this.L = arrayList;
                    }
                    if (jSONObject10.has("poiregion")) {
                        String string10 = jSONObject10.getString("poiregion");
                        if (!TextUtils.isEmpty(string10)) {
                            this.r = string10;
                        }
                    }
                    if (jSONObject10.has("regular")) {
                        String string11 = jSONObject10.getString("regular");
                        if (!TextUtils.isEmpty(string11)) {
                            this.s = string11;
                        }
                    }
                }
                if (jSONObject8.has("addr")) {
                    String string12 = null;
                    String string13 = jSONObject8.getString("addr");
                    try {
                        jSONObject = new JSONObject(string13);
                    } catch (Exception e3) {
                        jSONObject = null;
                    }
                    if (jSONObject != null) {
                        string12 = "";
                        string2 = jSONObject.has("city") ? jSONObject.getString("city") : "";
                        string6 = jSONObject.has("city_code") ? jSONObject.getString("city_code") : "";
                        string7 = jSONObject.has("country") ? jSONObject.getString("country") : "";
                        string8 = jSONObject.has("country_code") ? jSONObject.getString("country_code") : "";
                        string = jSONObject.has("province") ? jSONObject.getString("province") : "";
                        string3 = jSONObject.has("district") ? jSONObject.getString("district") : "";
                        string4 = jSONObject.has("street") ? jSONObject.getString("street") : "";
                        string5 = jSONObject.has("street_number") ? jSONObject.getString("street_number") : "";
                        if (jSONObject.has("adcode")) {
                            string12 = jSONObject.getString("adcode");
                        }
                    } else {
                        String[] strArrSplit = string13.split(",");
                        int length = strArrSplit.length;
                        string = length > 0 ? strArrSplit[0] : null;
                        string2 = length > 1 ? strArrSplit[1] : null;
                        string3 = length > 2 ? strArrSplit[2] : null;
                        string4 = length > 3 ? strArrSplit[3] : null;
                        string5 = length > 4 ? strArrSplit[4] : null;
                        string6 = length > 5 ? strArrSplit[5] : null;
                        string7 = length > 6 ? strArrSplit[6] : null;
                        string8 = length > 7 ? strArrSplit[7] : null;
                        if (length > 8) {
                            string12 = strArrSplit[8];
                        }
                    }
                    this.u = new a.C0004a().a(string7).c(string8).d(string).e(string2).f(string6).g(string3).h(string4).i(string5).b(string12).a();
                    this.o = true;
                } else {
                    this.o = false;
                    e((String) null);
                }
                if (jSONObject8.has("floor")) {
                    this.v = jSONObject8.getString("floor");
                    if (TextUtils.isEmpty(this.v)) {
                        this.v = null;
                    }
                }
                if (jSONObject8.has("indoor")) {
                    String string14 = jSONObject8.getString("indoor");
                    if (!TextUtils.isEmpty(string14)) {
                        a(Integer.valueOf(string14).intValue());
                    }
                }
                if (jSONObject8.has("loctp")) {
                    this.B = jSONObject8.getString("loctp");
                    if (TextUtils.isEmpty(this.B)) {
                        this.B = null;
                    }
                }
                if (jSONObject8.has("bldgid")) {
                    this.w = jSONObject8.getString("bldgid");
                    if (TextUtils.isEmpty(this.w)) {
                        this.w = null;
                    }
                }
                if (jSONObject8.has("bldg")) {
                    this.x = jSONObject8.getString("bldg");
                    if (TextUtils.isEmpty(this.x)) {
                        this.x = null;
                    }
                }
                if (jSONObject8.has("ibav")) {
                    String string15 = jSONObject8.getString("ibav");
                    if (TextUtils.isEmpty(string15) || string15.equals("0")) {
                        this.z = 0;
                    } else {
                        this.z = Integer.valueOf(string15).intValue();
                    }
                }
                if (jSONObject8.has("indoorflags")) {
                    try {
                        JSONObject jSONObject12 = jSONObject8.getJSONObject("indoorflags");
                        if (jSONObject12.has("area")) {
                            int iIntValue = Integer.valueOf(jSONObject12.getString("area")).intValue();
                            if (iIntValue == 0) {
                                b(2);
                            } else if (iIntValue == 1) {
                                b(1);
                            }
                        }
                        if (jSONObject12.has("support")) {
                            c(Integer.valueOf(jSONObject12.getString("support")).intValue());
                        }
                        if (jSONObject12.has("inbldg")) {
                            this.I = jSONObject12.getString("inbldg");
                        }
                        if (jSONObject12.has("inbldgid")) {
                            this.J = jSONObject12.getString("inbldgid");
                        }
                        if (jSONObject12.has("polygon")) {
                            a(jSONObject12.getString("polygon"));
                        }
                        if (jSONObject12.has("ret_fields")) {
                            try {
                                String[] strArrSplit2 = jSONObject12.getString("ret_fields").split("\\|");
                                for (String str2 : strArrSplit2) {
                                    String[] strArrSplit3 = str2.split("=");
                                    this.O.put(strArrSplit3[0], strArrSplit3[1]);
                                }
                            } catch (Exception e4) {
                                e4.printStackTrace();
                            }
                        }
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                }
                if (jSONObject8.has("gpscs")) {
                    h(jSONObject8.getInt("gpscs"));
                } else {
                    h(0);
                }
                try {
                    if (jSONObject8.has("in_cn")) {
                        f(Integer.parseInt(jSONObject8.getString("in_cn")));
                    } else {
                        f(1);
                    }
                } catch (Exception e6) {
                }
                if (this.A == 0) {
                    d("wgs84");
                } else {
                    d("gcj02");
                }
            } catch (Error e7) {
                e7.printStackTrace();
                this.f98a = 0;
                this.o = false;
            }
        } catch (Exception e8) {
            e8.printStackTrace();
            this.f98a = 0;
            this.o = false;
        }
    }

    private void a(Boolean bool) {
        this.t = bool.booleanValue();
    }

    public List<i> a() {
        return this.L;
    }

    public void a(double d) {
        this.c = d;
    }

    public void a(float f) {
        this.h = f;
        this.g = true;
    }

    public void a(int i) {
        this.E = i;
    }

    public void a(a aVar) {
        if (aVar != null) {
            this.u = aVar;
            this.o = true;
        }
    }

    public void a(String str) {
        this.K = str;
    }

    public void a(List<i> list) {
        this.L = list;
    }

    public int b() {
        return this.E;
    }

    public void b(double d) {
        this.d = d;
    }

    public void b(float f) {
        this.j = f;
        this.i = true;
    }

    public void b(int i) {
        this.F = i;
    }

    public void b(String str) {
        this.b = str;
        c(com.baidu.location.d.j.a(str));
    }

    public String c() {
        return this.b;
    }

    public void c(double d) {
        this.f = d;
        this.e = true;
    }

    public void c(float f) {
        this.m = f;
    }

    public void c(int i) {
        this.H = i;
    }

    public void c(String str) {
        this.N = str;
    }

    public double d() {
        return this.c;
    }

    public void d(int i) {
        this.f98a = i;
        switch (i) {
            case JniUscClient.ax /* 61 */:
                f("GPS location successful!");
                a(0);
                break;
            case JniUscClient.ay /* 62 */:
                f("Location failed beacuse we can not get any loc information!");
                break;
            case 63:
            case 67:
                f("Offline location failed, please check the net (wifi/cell)!");
                break;
            case 66:
                f("Offline location successful!");
                break;
            case 161:
                f("NetWork location successful!");
                break;
            case 162:
                f("NetWork location failed because baidu location service can not decrypt the request query, please check the so file !");
                break;
            case 167:
                f("NetWork location failed because baidu location service can not caculate the location!");
                break;
            case 505:
                f("NetWork location failed because baidu location service check the key is unlegal, please check the key in AndroidManifest.xml !");
                break;
            default:
                f("UnKnown!");
                break;
        }
    }

    public void d(String str) {
        this.n = str;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public double e() {
        return this.d;
    }

    public void e(int i) {
        this.l = i;
    }

    public void e(String str) {
        this.p = str;
        if (str == null) {
            this.o = false;
        } else {
            this.o = true;
        }
    }

    public float f() {
        return this.j;
    }

    public void f(int i) {
        this.A = i;
    }

    public void f(String str) {
        this.M = str;
    }

    public String g() {
        return this.n;
    }

    public void g(int i) {
        this.C = i;
    }

    public void g(String str) {
        this.q = str;
    }

    public int h() {
        return this.f98a;
    }

    public void h(int i) {
        this.Q = i;
    }

    public String i() {
        return this.M;
    }

    public boolean j() {
        return this.o;
    }

    public a k() {
        return this.u;
    }

    public String l() {
        return this.u.i;
    }

    public String m() {
        return this.u.c;
    }

    public String n() {
        return this.u.d;
    }

    public String o() {
        return this.u.e;
    }

    public String p() {
        return this.u.f52a;
    }

    public String q() {
        return this.u.f;
    }

    public String r() {
        return this.q;
    }

    public String s() {
        return this.B;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.f98a);
        parcel.writeString(this.b);
        parcel.writeDouble(this.c);
        parcel.writeDouble(this.d);
        parcel.writeDouble(this.f);
        parcel.writeFloat(this.h);
        parcel.writeFloat(this.j);
        parcel.writeInt(this.l);
        parcel.writeFloat(this.m);
        parcel.writeString(this.v);
        parcel.writeInt(this.z);
        parcel.writeString(this.w);
        parcel.writeString(this.x);
        parcel.writeString(this.B);
        parcel.writeString(this.u.c);
        parcel.writeString(this.u.d);
        parcel.writeString(this.u.f);
        parcel.writeString(this.u.g);
        parcel.writeString(this.u.h);
        parcel.writeString(this.u.e);
        parcel.writeString(this.u.i);
        parcel.writeString(this.u.f52a);
        parcel.writeString(this.u.b);
        parcel.writeString(this.u.j);
        parcel.writeInt(this.C);
        parcel.writeString(this.D);
        parcel.writeString(this.q);
        parcel.writeString(this.r);
        parcel.writeString(this.s);
        parcel.writeInt(this.A);
        parcel.writeString(this.M);
        parcel.writeInt(this.E);
        parcel.writeInt(this.F);
        parcel.writeInt(this.G);
        parcel.writeInt(this.H);
        parcel.writeString(this.I);
        parcel.writeString(this.J);
        parcel.writeString(this.K);
        parcel.writeInt(this.P);
        parcel.writeString(this.N);
        parcel.writeInt(this.Q);
        parcel.writeBooleanArray(new boolean[]{this.e, this.g, this.i, this.k, this.o, this.t, this.y});
        parcel.writeList(this.L);
    }
}
