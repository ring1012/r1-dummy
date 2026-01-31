package com.baidu.location;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class i implements Parcelable {
    public static final Parcelable.Creator<i> CREATOR = new m();

    /* renamed from: a, reason: collision with root package name */
    private final double f120a;
    private final String b;
    private final String c;

    public i(String str, String str2, double d) {
        this.b = str;
        this.c = str2;
        this.f120a = d;
    }

    public String a() {
        return this.b;
    }

    public double b() {
        return this.f120a;
    }

    public String c() {
        return this.c;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.b);
        parcel.writeString(this.c);
        parcel.writeDouble(this.f120a);
    }
}
