package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.util.Util;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class GeobFrame extends Id3Frame {
    public static final Parcelable.Creator<GeobFrame> CREATOR = new Parcelable.Creator<GeobFrame>() { // from class: com.google.android.exoplayer2.metadata.id3.GeobFrame.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GeobFrame createFromParcel(Parcel in) {
            return new GeobFrame(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GeobFrame[] newArray(int size) {
            return new GeobFrame[size];
        }
    };
    public static final String ID = "GEOB";
    public final byte[] data;
    public final String description;
    public final String filename;
    public final String mimeType;

    public GeobFrame(String mimeType, String filename, String description, byte[] data) {
        super(ID);
        this.mimeType = mimeType;
        this.filename = filename;
        this.description = description;
        this.data = data;
    }

    GeobFrame(Parcel in) {
        super(ID);
        this.mimeType = in.readString();
        this.filename = in.readString();
        this.description = in.readString();
        this.data = in.createByteArray();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GeobFrame other = (GeobFrame) obj;
        return Util.areEqual(this.mimeType, other.mimeType) && Util.areEqual(this.filename, other.filename) && Util.areEqual(this.description, other.description) && Arrays.equals(this.data, other.data);
    }

    public int hashCode() {
        int result = (this.mimeType != null ? this.mimeType.hashCode() : 0) + 527;
        return (((((result * 31) + (this.filename != null ? this.filename.hashCode() : 0)) * 31) + (this.description != null ? this.description.hashCode() : 0)) * 31) + Arrays.hashCode(this.data);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mimeType);
        dest.writeString(this.filename);
        dest.writeString(this.description);
        dest.writeByteArray(this.data);
    }
}
