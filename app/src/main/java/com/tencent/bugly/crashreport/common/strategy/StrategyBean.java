package com.tencent.bugly.crashreport.common.strategy;

import android.os.Parcel;
import android.os.Parcelable;
import com.tencent.bugly.proguard.z;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class StrategyBean implements Parcelable {
    public static String d;
    public long e;
    public long f;
    public boolean g;
    public boolean h;
    public boolean i;
    public boolean j;
    public boolean k;
    public boolean l;
    public boolean m;
    public boolean n;
    public boolean o;
    public long p;
    public long q;
    public String r;
    public String s;
    public String t;
    public String u;
    public Map<String, String> v;
    public int w;
    public long x;
    public long y;

    /* renamed from: a, reason: collision with root package name */
    public static String f143a = "http://rqd.uu.qq.com/rqd/sync";
    public static String b = "http://android.bugly.qq.com/rqd/async";
    public static String c = "http://android.bugly.qq.com/rqd/async";
    public static final Parcelable.Creator<StrategyBean> CREATOR = new Parcelable.Creator<StrategyBean>() { // from class: com.tencent.bugly.crashreport.common.strategy.StrategyBean.1
        @Override // android.os.Parcelable.Creator
        public final /* synthetic */ StrategyBean createFromParcel(Parcel parcel) {
            return new StrategyBean(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public final /* bridge */ /* synthetic */ StrategyBean[] newArray(int i) {
            return new StrategyBean[i];
        }
    };

    public StrategyBean() {
        this.e = -1L;
        this.f = -1L;
        this.g = true;
        this.h = true;
        this.i = true;
        this.j = true;
        this.k = false;
        this.l = true;
        this.m = true;
        this.n = true;
        this.o = true;
        this.q = 30000L;
        this.r = b;
        this.s = c;
        this.t = f143a;
        this.w = 10;
        this.x = 300000L;
        this.y = -1L;
        this.f = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("S(@L@L").append("@)");
        d = sb.toString();
        sb.setLength(0);
        sb.append("*^@K#K").append("@!");
        this.u = sb.toString();
    }

    public StrategyBean(Parcel parcel) {
        this.e = -1L;
        this.f = -1L;
        this.g = true;
        this.h = true;
        this.i = true;
        this.j = true;
        this.k = false;
        this.l = true;
        this.m = true;
        this.n = true;
        this.o = true;
        this.q = 30000L;
        this.r = b;
        this.s = c;
        this.t = f143a;
        this.w = 10;
        this.x = 300000L;
        this.y = -1L;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("S(@L@L").append("@)");
            d = sb.toString();
            this.f = parcel.readLong();
            this.g = parcel.readByte() == 1;
            this.h = parcel.readByte() == 1;
            this.i = parcel.readByte() == 1;
            this.r = parcel.readString();
            this.s = parcel.readString();
            this.u = parcel.readString();
            this.v = z.b(parcel);
            this.j = parcel.readByte() == 1;
            this.k = parcel.readByte() == 1;
            this.n = parcel.readByte() == 1;
            this.o = parcel.readByte() == 1;
            this.q = parcel.readLong();
            this.l = parcel.readByte() == 1;
            this.m = parcel.readByte() == 1;
            this.p = parcel.readLong();
            this.w = parcel.readInt();
            this.x = parcel.readLong();
            this.y = parcel.readLong();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.f);
        parcel.writeByte((byte) (this.g ? 1 : 0));
        parcel.writeByte((byte) (this.h ? 1 : 0));
        parcel.writeByte((byte) (this.i ? 1 : 0));
        parcel.writeString(this.r);
        parcel.writeString(this.s);
        parcel.writeString(this.u);
        z.b(parcel, this.v);
        parcel.writeByte((byte) (this.j ? 1 : 0));
        parcel.writeByte((byte) (this.k ? 1 : 0));
        parcel.writeByte((byte) (this.n ? 1 : 0));
        parcel.writeByte((byte) (this.o ? 1 : 0));
        parcel.writeLong(this.q);
        parcel.writeByte((byte) (this.l ? 1 : 0));
        parcel.writeByte((byte) (this.m ? 1 : 0));
        parcel.writeLong(this.p);
        parcel.writeInt(this.w);
        parcel.writeLong(this.x);
        parcel.writeLong(this.y);
    }
}
