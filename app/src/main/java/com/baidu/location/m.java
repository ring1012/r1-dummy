package com.baidu.location;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class m implements Parcelable.Creator<i> {
    m() {
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public i createFromParcel(Parcel parcel) {
        return new i(parcel.readString(), parcel.readString(), parcel.readDouble());
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public i[] newArray(int i) {
        return new i[i];
    }
}
