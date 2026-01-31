package com.kaolafm.sdk.client;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class Music implements Parcelable {
    public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() { // from class: com.kaolafm.sdk.client.Music.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Music createFromParcel(Parcel source) {
            return new Music(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };
    public long albumId;
    public String albumName;
    public long audioId;
    public String audioName;
    public String authorName;
    public long categoryId;
    public String categoryName;
    public String describe;
    public long duration;
    public String picUrl;
    public String playUrl;
    public long progress;

    public String toString() {
        return this.audioName + "(" + this.audioId + ")";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.audioId);
        dest.writeString(this.audioName);
        dest.writeLong(this.albumId);
        dest.writeString(this.albumName);
        dest.writeLong(this.categoryId);
        dest.writeString(this.categoryName);
        dest.writeString(this.playUrl);
        dest.writeString(this.picUrl);
        dest.writeLong(this.duration);
        dest.writeLong(this.progress);
        dest.writeString(this.authorName);
        dest.writeString(this.describe);
    }

    public Music() {
    }

    protected Music(Parcel in) {
        this.audioId = in.readLong();
        this.audioName = in.readString();
        this.albumId = in.readLong();
        this.albumName = in.readString();
        this.categoryId = in.readLong();
        this.categoryName = in.readString();
        this.playUrl = in.readString();
        this.picUrl = in.readString();
        this.duration = in.readLong();
        this.progress = in.readLong();
        this.authorName = in.readString();
        this.describe = in.readString();
    }
}
