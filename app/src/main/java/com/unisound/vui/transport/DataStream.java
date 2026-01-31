package com.unisound.vui.transport;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class DataStream implements Parcelable {
    public static final Parcelable.Creator<DataStream> CREATOR = new Parcelable.Creator<DataStream>() { // from class: com.unisound.vui.transport.DataStream.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public DataStream createFromParcel(Parcel parcel) {
            return new DataStream(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a, reason: merged with bridge method [inline-methods] */
        public DataStream[] newArray(int i) {
            return new DataStream[i];
        }
    };
    private byte[] dataBytes;

    private DataStream(Parcel source) {
        readFromParcel(source);
    }

    public DataStream(byte[] bytes) {
        this.dataBytes = bytes;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public byte[] getDataBytes() {
        return this.dataBytes;
    }

    public void readFromParcel(Parcel source) {
        this.dataBytes = new byte[source.readInt()];
        source.readByteArray(this.dataBytes);
    }

    public void setDataBytes(byte[] dataBytes) {
        this.dataBytes = dataBytes;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dataBytes.length);
        dest.writeByteArray(this.dataBytes);
    }
}
