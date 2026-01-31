package com.kaolafm.sdk.client;

import android.os.Parcel;
import android.os.Parcelable;
import com.unisound.common.y;

/* loaded from: classes.dex */
public class ErrorInfo implements Parcelable {
    public static final Parcelable.Creator<ErrorInfo> CREATOR = new Parcelable.Creator<ErrorInfo>() { // from class: com.kaolafm.sdk.client.ErrorInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ErrorInfo createFromParcel(Parcel source) {
            return new ErrorInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ErrorInfo[] newArray(int size) {
            return new ErrorInfo[size];
        }
    };
    public int errCode;
    public String info;

    public ErrorInfo(int errCode) {
        this.info = y.I;
        this.errCode = errCode;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.errCode);
        dest.writeString(this.info);
    }

    protected ErrorInfo(Parcel in) {
        this.info = y.I;
        this.errCode = in.readInt();
        this.info = in.readString();
    }
}
