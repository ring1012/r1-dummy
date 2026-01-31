package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
public final class TextInformationFrame extends Id3Frame {
    public static final Parcelable.Creator<TextInformationFrame> CREATOR = new Parcelable.Creator<TextInformationFrame>() { // from class: com.google.android.exoplayer2.metadata.id3.TextInformationFrame.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextInformationFrame createFromParcel(Parcel in) {
            return new TextInformationFrame(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextInformationFrame[] newArray(int size) {
            return new TextInformationFrame[size];
        }
    };
    public final String description;
    public final String value;

    public TextInformationFrame(String id, String description, String value) {
        super(id);
        this.description = description;
        this.value = value;
    }

    TextInformationFrame(Parcel in) {
        super(in.readString());
        this.description = in.readString();
        this.value = in.readString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TextInformationFrame other = (TextInformationFrame) obj;
        return this.id.equals(other.id) && Util.areEqual(this.description, other.description) && Util.areEqual(this.value, other.value);
    }

    public int hashCode() {
        int result = this.id.hashCode() + 527;
        return (((result * 31) + (this.description != null ? this.description.hashCode() : 0)) * 31) + (this.value != null ? this.value.hashCode() : 0);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.value);
    }
}
