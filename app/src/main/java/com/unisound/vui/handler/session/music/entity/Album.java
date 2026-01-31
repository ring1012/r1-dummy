package com.unisound.vui.handler.session.music.entity;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class Album implements Parcelable {
    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() { // from class: com.unisound.vui.handler.session.music.entity.Album.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Album createFromParcel(Parcel source) {
            Album album = new Album();
            album.readFromParcel(source);
            return album;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    @SerializedName("albumId")
    @JSONField(name = "albumId")
    private String albumId;

    @SerializedName("albumLogo")
    @JSONField(name = "albumLogo")
    private String albumLogo;

    @SerializedName("albumName")
    @JSONField(name = "albumName")
    private String albumName;

    @SerializedName("description")
    @JSONField(name = "description")
    private String description;

    @SerializedName("musicCount")
    @JSONField(name = "musicCount")
    private int musicCount;

    @SerializedName("publishTime")
    @JSONField(name = "publishTime")
    private long publishTime;

    public Album() {
    }

    public Album(String albumName, String albumId, int musicCount, String albumLogo, String description, long publishTime) {
        this.albumName = albumName;
        this.albumId = albumId;
        this.musicCount = musicCount;
        this.albumLogo = albumLogo;
        this.description = description;
        this.publishTime = publishTime;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getMusicCount() {
        return this.musicCount;
    }

    public void setMusicCount(int musicCount) {
        this.musicCount = musicCount;
    }

    public String getAlbumLogo() {
        return this.albumLogo;
    }

    public void setAlbumLogo(String albumLogo) {
        this.albumLogo = albumLogo;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPublishTime() {
        return this.publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.albumName);
        dest.writeString(this.albumId);
        dest.writeInt(this.musicCount);
        dest.writeString(this.albumLogo);
        dest.writeString(this.description);
        dest.writeLong(this.publishTime);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readFromParcel(Parcel in) {
        this.albumName = in.readString();
        this.albumId = in.readString();
        this.musicCount = in.readInt();
        this.albumLogo = in.readString();
        this.description = in.readString();
        this.publishTime = in.readLong();
    }

    public String toString() {
        return "Album [albumName=" + this.albumName + ", albumId=" + this.albumId + ", musicCount=" + this.musicCount + ", albumLogo=" + this.albumLogo + "description=" + this.description + ", publishTime=" + this.publishTime + "]";
    }
}
