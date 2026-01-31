package com.kaolafm.sdk.client;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class Radio implements Parcelable {
    public static final Parcelable.Creator<Radio> CREATOR = new Parcelable.Creator<Radio>() { // from class: com.kaolafm.sdk.client.Radio.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Radio createFromParcel(Parcel in) {
            return new Radio(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Radio[] newArray(int size) {
            return new Radio[size];
        }
    };
    public Long categoryId;
    public Long id;
    public String name;

    public Radio() {
    }

    protected Radio(Parcel in) {
        this.id = Long.valueOf(in.readLong());
        this.name = in.readString();
        this.categoryId = Long.valueOf(in.readLong());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id.longValue());
        dest.writeString(this.name);
        dest.writeLong(this.categoryId.longValue());
    }
}
