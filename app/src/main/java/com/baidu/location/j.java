package com.baidu.location;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class j implements Parcelable.Creator<c> {
    j() {
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public c createFromParcel(Parcel parcel) {
        return new c(parcel, null);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public c[] newArray(int i) {
        return new c[i];
    }
}
