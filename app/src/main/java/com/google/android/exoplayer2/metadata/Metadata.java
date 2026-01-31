package com.google.android.exoplayer2.metadata;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public final class Metadata implements Parcelable {
    public static final Parcelable.Creator<Metadata> CREATOR = new Parcelable.Creator<Metadata>() { // from class: com.google.android.exoplayer2.metadata.Metadata.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Metadata createFromParcel(Parcel in) {
            return new Metadata(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Metadata[] newArray(int size) {
            return new Metadata[0];
        }
    };
    private final Entry[] entries;

    public interface Entry extends Parcelable {
    }

    public Metadata(Entry... entries) {
        this.entries = entries == null ? new Entry[0] : entries;
    }

    public Metadata(List<? extends Entry> entries) {
        if (entries != null) {
            this.entries = new Entry[entries.size()];
            entries.toArray(this.entries);
        } else {
            this.entries = new Entry[0];
        }
    }

    Metadata(Parcel in) {
        this.entries = new Entry[in.readInt()];
        for (int i = 0; i < this.entries.length; i++) {
            this.entries[i] = (Entry) in.readParcelable(Entry.class.getClassLoader());
        }
    }

    public int length() {
        return this.entries.length;
    }

    public Entry get(int index) {
        return this.entries[index];
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Metadata other = (Metadata) obj;
        return Arrays.equals(this.entries, other.entries);
    }

    public int hashCode() {
        return Arrays.hashCode(this.entries);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.entries.length);
        for (Entry entry : this.entries) {
            dest.writeParcelable(entry, 0);
        }
    }
}
