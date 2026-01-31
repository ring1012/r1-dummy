package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
public final class CommentFrame extends Id3Frame {
    public static final Parcelable.Creator<CommentFrame> CREATOR = new Parcelable.Creator<CommentFrame>() { // from class: com.google.android.exoplayer2.metadata.id3.CommentFrame.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CommentFrame createFromParcel(Parcel in) {
            return new CommentFrame(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CommentFrame[] newArray(int size) {
            return new CommentFrame[size];
        }
    };
    public static final String ID = "COMM";
    public final String description;
    public final String language;
    public final String text;

    public CommentFrame(String language, String description, String text) {
        super(ID);
        this.language = language;
        this.description = description;
        this.text = text;
    }

    CommentFrame(Parcel in) {
        super(ID);
        this.language = in.readString();
        this.description = in.readString();
        this.text = in.readString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CommentFrame other = (CommentFrame) obj;
        return Util.areEqual(this.description, other.description) && Util.areEqual(this.language, other.language) && Util.areEqual(this.text, other.text);
    }

    public int hashCode() {
        int result = (this.language != null ? this.language.hashCode() : 0) + 527;
        return (((result * 31) + (this.description != null ? this.description.hashCode() : 0)) * 31) + (this.text != null ? this.text.hashCode() : 0);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.language);
        dest.writeString(this.text);
    }
}
