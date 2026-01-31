package com.google.android.exoplayer2.source.dash.manifest;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/* loaded from: classes.dex */
public final class RepresentationKey implements Parcelable, Comparable<RepresentationKey> {
    public static final Parcelable.Creator<RepresentationKey> CREATOR = new Parcelable.Creator<RepresentationKey>() { // from class: com.google.android.exoplayer2.source.dash.manifest.RepresentationKey.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RepresentationKey createFromParcel(Parcel in) {
            return new RepresentationKey(in.readInt(), in.readInt(), in.readInt());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RepresentationKey[] newArray(int size) {
            return new RepresentationKey[size];
        }
    };
    public final int adaptationSetIndex;
    public final int periodIndex;
    public final int representationIndex;

    public RepresentationKey(int periodIndex, int adaptationSetIndex, int representationIndex) {
        this.periodIndex = periodIndex;
        this.adaptationSetIndex = adaptationSetIndex;
        this.representationIndex = representationIndex;
    }

    public String toString() {
        return this.periodIndex + "." + this.adaptationSetIndex + "." + this.representationIndex;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.periodIndex);
        dest.writeInt(this.adaptationSetIndex);
        dest.writeInt(this.representationIndex);
    }

    @Override // java.lang.Comparable
    public int compareTo(@NonNull RepresentationKey o) {
        int result = this.periodIndex - o.periodIndex;
        if (result == 0) {
            int result2 = this.adaptationSetIndex - o.adaptationSetIndex;
            if (result2 == 0) {
                return this.representationIndex - o.representationIndex;
            }
            return result2;
        }
        return result;
    }
}
