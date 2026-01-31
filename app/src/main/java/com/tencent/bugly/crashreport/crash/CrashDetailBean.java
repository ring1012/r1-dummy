package com.tencent.bugly.crashreport.crash;

import android.os.Parcel;
import android.os.Parcelable;
import com.tencent.bugly.crashreport.common.info.PlugInBean;
import com.tencent.bugly.proguard.z;
import java.util.Map;
import java.util.UUID;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class CrashDetailBean implements Parcelable, Comparable<CrashDetailBean> {
    public static final Parcelable.Creator<CrashDetailBean> CREATOR = new Parcelable.Creator<CrashDetailBean>() { // from class: com.tencent.bugly.crashreport.crash.CrashDetailBean.1
        @Override // android.os.Parcelable.Creator
        public final /* synthetic */ CrashDetailBean createFromParcel(Parcel parcel) {
            return new CrashDetailBean(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final /* bridge */ /* synthetic */ CrashDetailBean[] newArray(int i) {
            return new CrashDetailBean[i];
        }
    };
    public String A;
    public long B;
    public long C;
    public long D;
    public long E;
    public long F;
    public long G;
    public String H;
    public String I;
    public String J;
    public String K;
    public long L;
    public boolean M;
    public Map<String, String> N;
    public int O;
    public int P;
    public Map<String, String> Q;
    public Map<String, String> R;
    public byte[] S;
    public String T;
    public String U;
    private String V;

    /* renamed from: a, reason: collision with root package name */
    public long f148a;
    public int b;
    public String c;
    public boolean d;
    public String e;
    public String f;
    public String g;
    public Map<String, PlugInBean> h;
    public Map<String, PlugInBean> i;
    public boolean j;
    public boolean k;
    public int l;
    public String m;
    public String n;
    public String o;
    public String p;
    public String q;
    public long r;
    public String s;
    public int t;
    public String u;
    public String v;
    public String w;
    public byte[] x;
    public Map<String, String> y;
    public String z;

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(CrashDetailBean crashDetailBean) {
        CrashDetailBean crashDetailBean2 = crashDetailBean;
        if (crashDetailBean2 != null) {
            long j = this.r - crashDetailBean2.r;
            if (j <= 0) {
                return j < 0 ? -1 : 0;
            }
        }
        return 1;
    }

    public CrashDetailBean() {
        this.f148a = -1L;
        this.b = 0;
        this.c = UUID.randomUUID().toString();
        this.d = false;
        this.e = "";
        this.f = "";
        this.g = "";
        this.h = null;
        this.i = null;
        this.j = false;
        this.k = false;
        this.l = 0;
        this.m = "";
        this.n = "";
        this.o = "";
        this.p = "";
        this.q = "";
        this.r = -1L;
        this.s = null;
        this.t = 0;
        this.u = "";
        this.v = "";
        this.w = null;
        this.x = null;
        this.y = null;
        this.z = "";
        this.A = "";
        this.B = -1L;
        this.C = -1L;
        this.D = -1L;
        this.E = -1L;
        this.F = -1L;
        this.G = -1L;
        this.H = "";
        this.V = "";
        this.I = "";
        this.J = "";
        this.K = "";
        this.L = -1L;
        this.M = false;
        this.N = null;
        this.O = -1;
        this.P = -1;
        this.Q = null;
        this.R = null;
        this.S = null;
        this.T = null;
        this.U = null;
    }

    public CrashDetailBean(Parcel parcel) {
        this.f148a = -1L;
        this.b = 0;
        this.c = UUID.randomUUID().toString();
        this.d = false;
        this.e = "";
        this.f = "";
        this.g = "";
        this.h = null;
        this.i = null;
        this.j = false;
        this.k = false;
        this.l = 0;
        this.m = "";
        this.n = "";
        this.o = "";
        this.p = "";
        this.q = "";
        this.r = -1L;
        this.s = null;
        this.t = 0;
        this.u = "";
        this.v = "";
        this.w = null;
        this.x = null;
        this.y = null;
        this.z = "";
        this.A = "";
        this.B = -1L;
        this.C = -1L;
        this.D = -1L;
        this.E = -1L;
        this.F = -1L;
        this.G = -1L;
        this.H = "";
        this.V = "";
        this.I = "";
        this.J = "";
        this.K = "";
        this.L = -1L;
        this.M = false;
        this.N = null;
        this.O = -1;
        this.P = -1;
        this.Q = null;
        this.R = null;
        this.S = null;
        this.T = null;
        this.U = null;
        this.b = parcel.readInt();
        this.c = parcel.readString();
        this.d = parcel.readByte() == 1;
        this.e = parcel.readString();
        this.f = parcel.readString();
        this.g = parcel.readString();
        this.j = parcel.readByte() == 1;
        this.k = parcel.readByte() == 1;
        this.l = parcel.readInt();
        this.m = parcel.readString();
        this.n = parcel.readString();
        this.o = parcel.readString();
        this.p = parcel.readString();
        this.q = parcel.readString();
        this.r = parcel.readLong();
        this.s = parcel.readString();
        this.t = parcel.readInt();
        this.u = parcel.readString();
        this.v = parcel.readString();
        this.w = parcel.readString();
        this.y = z.b(parcel);
        this.z = parcel.readString();
        this.A = parcel.readString();
        this.B = parcel.readLong();
        this.C = parcel.readLong();
        this.D = parcel.readLong();
        this.E = parcel.readLong();
        this.F = parcel.readLong();
        this.G = parcel.readLong();
        this.H = parcel.readString();
        this.V = parcel.readString();
        this.I = parcel.readString();
        this.J = parcel.readString();
        this.K = parcel.readString();
        this.L = parcel.readLong();
        this.M = parcel.readByte() == 1;
        this.N = z.b(parcel);
        this.h = z.a(parcel);
        this.i = z.a(parcel);
        this.O = parcel.readInt();
        this.P = parcel.readInt();
        this.Q = z.b(parcel);
        this.R = z.b(parcel);
        this.S = parcel.createByteArray();
        this.x = parcel.createByteArray();
        this.T = parcel.readString();
        this.U = parcel.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.b);
        parcel.writeString(this.c);
        parcel.writeByte((byte) (this.d ? 1 : 0));
        parcel.writeString(this.e);
        parcel.writeString(this.f);
        parcel.writeString(this.g);
        parcel.writeByte((byte) (this.j ? 1 : 0));
        parcel.writeByte((byte) (this.k ? 1 : 0));
        parcel.writeInt(this.l);
        parcel.writeString(this.m);
        parcel.writeString(this.n);
        parcel.writeString(this.o);
        parcel.writeString(this.p);
        parcel.writeString(this.q);
        parcel.writeLong(this.r);
        parcel.writeString(this.s);
        parcel.writeInt(this.t);
        parcel.writeString(this.u);
        parcel.writeString(this.v);
        parcel.writeString(this.w);
        z.b(parcel, this.y);
        parcel.writeString(this.z);
        parcel.writeString(this.A);
        parcel.writeLong(this.B);
        parcel.writeLong(this.C);
        parcel.writeLong(this.D);
        parcel.writeLong(this.E);
        parcel.writeLong(this.F);
        parcel.writeLong(this.G);
        parcel.writeString(this.H);
        parcel.writeString(this.V);
        parcel.writeString(this.I);
        parcel.writeString(this.J);
        parcel.writeString(this.K);
        parcel.writeLong(this.L);
        parcel.writeByte((byte) (this.M ? 1 : 0));
        z.b(parcel, this.N);
        z.a(parcel, this.h);
        z.a(parcel, this.i);
        parcel.writeInt(this.O);
        parcel.writeInt(this.P);
        z.b(parcel, this.Q);
        z.b(parcel, this.R);
        parcel.writeByteArray(this.S);
        parcel.writeByteArray(this.x);
        parcel.writeString(this.T);
        parcel.writeString(this.U);
    }
}
