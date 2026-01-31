package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
public final class UrlLinkFrame extends Id3Frame {
    public static final Parcelable.Creator<UrlLinkFrame> CREATOR = new Parcelable.Creator<UrlLinkFrame>() { // from class: com.google.android.exoplayer2.metadata.id3.UrlLinkFrame.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UrlLinkFrame createFromParcel(Parcel in) {
            return new UrlLinkFrame(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UrlLinkFrame[] newArray(int size) {
            return new UrlLinkFrame[size];
        }
    };
    public final String description;
    public final String url;

    public UrlLinkFrame(String id, String description, String url) {
        super(id);
        this.description = description;
        this.url = url;
    }

    UrlLinkFrame(Parcel in) {
        super(in.readString());
        this.description = in.readString();
        this.url = in.readString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UrlLinkFrame other = (UrlLinkFrame) obj;
        return this.id.equals(other.id) && Util.areEqual(this.description, other.description) && Util.areEqual(this.url, other.url);
    }

    public int hashCode() {
        int result = this.id.hashCode() + 527;
        return (((result * 31) + (this.description != null ? this.description.hashCode() : 0)) * 31) + (this.url != null ? this.url.hashCode() : 0);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.description);
        dest.writeString(this.url);
    }
}
