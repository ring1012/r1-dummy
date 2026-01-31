package com.unisound.vui.handler.session.music.entity;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import nluparser.scheme.ASR;

/* loaded from: classes.dex */
public class Music implements Parcelable {
    private static final String TAG = "Music";

    @SerializedName("album")
    @JSONField(name = "album")
    private Album album;

    @SerializedName("announcer")
    @JSONField(name = "announcer")
    private Announcer announcer;

    @SerializedName("artist")
    @JSONField(name = "artist")
    private String artist;

    @SerializedName("bufferPercent")
    @JSONField(name = "bufferPercent")
    private int bufferPercent;

    @SerializedName("curPostion")
    @JSONField(name = "curPostion")
    private long curPostion;

    @SerializedName("dataSource")
    @JSONField(name = "dataSource")
    private String dataSource;

    @SerializedName("errorCode")
    @JSONField(name = "errorCode")
    private int errorCode;

    @SerializedName("hdImgUrl")
    @JSONField(name = "hdImgUrl")
    private String hdImgUrl;

    @SerializedName("imgUrl")
    @JSONField(name = "imgUrl")
    private String imgUrl;

    @SerializedName(ASR.LOCAL)
    @JSONField(name = ASR.LOCAL)
    private boolean local;

    @SerializedName("lyric")
    @JSONField(name = "lyric")
    private String lyric;

    @SerializedName("mediaId")
    @JSONField(name = "mediaId")
    private String mediaId;

    @SerializedName("musicType")
    @JSONField(name = "musicType")
    private MusicType musicType;

    @SerializedName("size")
    @JSONField(name = "size")
    private long size;

    @SerializedName("time")
    @JSONField(name = "time")
    private long time;

    @SerializedName("title")
    @JSONField(name = "title")
    private String title;

    @SerializedName("url")
    @JSONField(name = "url")
    private String url;
    public static final Music NONE = new Music();
    public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() { // from class: com.unisound.vui.handler.session.music.entity.Music.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Music createFromParcel(Parcel source) {
            Music music = new Music();
            music.readFromParcel(source);
            return music;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    public enum MusicType {
        MUSIC,
        AUDIO,
        RADIO
    }

    public Music() {
    }

    public Music(String mediaId, String title, String artist, Album album, Announcer announcer, String url, long size, long time, int errorCode, boolean local, long curPostion, int bufferPercent, String imgUrl, String hdImgUrl, String lyric, String dataSource, MusicType musicType) {
        this.mediaId = mediaId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.announcer = announcer;
        this.url = url;
        this.size = size;
        this.time = time;
        this.errorCode = errorCode;
        this.local = local;
        this.curPostion = curPostion;
        this.bufferPercent = bufferPercent;
        this.imgUrl = imgUrl;
        this.hdImgUrl = hdImgUrl;
        this.lyric = lyric;
        this.dataSource = dataSource;
        this.musicType = musicType;
    }

    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Album getAlbum() {
        return this.album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Announcer getAnnouncer() {
        return this.announcer;
    }

    public void setAnnouncer(Announcer announcer) {
        this.announcer = announcer;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isLocal() {
        return this.local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public long getCurPostion() {
        return this.curPostion;
    }

    public void setCurPostion(long curPostion) {
        this.curPostion = curPostion;
    }

    public int getBufferPercent() {
        return this.bufferPercent;
    }

    public void setBufferPercent(int bufferPercent) {
        this.bufferPercent = bufferPercent;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHdImgUrl() {
        return this.hdImgUrl;
    }

    public void setHdImgUrl(String hdImgUrl) {
        this.hdImgUrl = hdImgUrl;
    }

    public String getLyric() {
        return this.lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public MusicType getMusicType() {
        return this.musicType;
    }

    public void setMusicType(MusicType musicType) {
        this.musicType = musicType;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mediaId);
        dest.writeString(this.title);
        dest.writeString(this.artist);
        this.album.writeToParcel(dest, flags);
        this.announcer.writeToParcel(dest, flags);
        dest.writeString(this.url);
        dest.writeLong(this.size);
        dest.writeLong(this.time);
        dest.writeInt(this.errorCode);
        dest.writeBooleanArray(new boolean[]{this.local});
        dest.writeLong(this.curPostion);
        dest.writeInt(this.bufferPercent);
        dest.writeString(this.imgUrl);
        dest.writeString(this.hdImgUrl);
        dest.writeString(this.lyric);
        dest.writeString(this.dataSource);
        dest.writeInt(this.musicType.ordinal());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readFromParcel(Parcel in) {
        this.mediaId = in.readString();
        this.title = in.readString();
        this.artist = in.readString();
        this.album = Album.CREATOR.createFromParcel(in);
        this.announcer = Announcer.CREATOR.createFromParcel(in);
        this.url = in.readString();
        this.size = in.readLong();
        this.time = in.readLong();
        this.errorCode = in.readInt();
        boolean[] temp = new boolean[1];
        in.readBooleanArray(temp);
        this.local = temp[0];
        this.curPostion = in.readLong();
        this.bufferPercent = in.readInt();
        this.imgUrl = in.readString();
        this.hdImgUrl = in.readString();
        this.lyric = in.readString();
        this.dataSource = in.readString();
        this.musicType = MusicType.values()[in.readInt()];
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Music [title=").append(this.title).append(", artist=").append(this.artist).append(", album=").append(this.album).append(", albumId=").append(this.album.getAlbumId()).append(", url=").append(this.url).append(", size=").append(this.size).append(", time=").append(this.time).append(", errorCode=").append(this.errorCode).append(", local=").append(this.local).append(", curPostion=").append(this.curPostion).append(", bufferPercent=").append(this.bufferPercent).append(", imgUrl=").append(this.imgUrl).append(", hdImgUrl=").append(this.hdImgUrl).append(", lyric=").append(this.lyric).append(", dataSource=").append(this.dataSource).append("]");
        return builder.toString();
    }
}
