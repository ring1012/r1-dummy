package com.unisound.vui.handler.session.music.syncloud;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/* loaded from: classes.dex */
public class SyncMusicListBean {
    private List<MusicInfo> musicList;
    private String udid;

    public String getUdid() {
        return this.udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public List<MusicInfo> getMusicList() {
        return this.musicList;
    }

    public void setMusicList(List<MusicInfo> musicList) {
        this.musicList = musicList;
    }

    public static class MusicInfo {

        @SerializedName("album")
        @JSONField(name = "album")
        String album;

        @SerializedName("artist")
        @JSONField(name = "artist")
        String artist;

        @SerializedName("duration")
        @JSONField(name = "duration")
        int duration;

        @SerializedName("errorCode")
        @JSONField(name = "errorCode")
        int errorCode;

        @SerializedName("hdImgUrl")
        @JSONField(name = "hdImgUrl")
        String hdImgUrl;

        @SerializedName(TtmlNode.ATTR_ID)
        @JSONField(name = TtmlNode.ATTR_ID)
        String id;

        @SerializedName("imgUrl")
        @JSONField(name = "imgUrl")
        String imgUrl;

        @SerializedName("isCollected")
        @JSONField(name = "isCollected")
        boolean isCollected;

        @SerializedName("lyric")
        @JSONField(name = "lyric")
        String mLyric;

        @SerializedName("musicListId")
        @JSONField(name = "musicListId")
        String musicListId;

        @SerializedName("title")
        @JSONField(name = "title")
        String title;

        @SerializedName("url")
        @JSONField(name = "url")
        String url;

        public String getId() {
            return this.id;
        }

        public String getMusicListId() {
            return this.musicListId;
        }

        public String getTitle() {
            return this.title;
        }

        public String getArtist() {
            return this.artist;
        }

        public String getAlbum() {
            return this.album;
        }

        public String getUrl() {
            return this.url;
        }

        public int getDuration() {
            return this.duration;
        }

        public int getErrorCode() {
            return this.errorCode;
        }

        public String getImgUrl() {
            return this.imgUrl;
        }

        public String getHdImgUrl() {
            return this.hdImgUrl;
        }

        public String getmLyric() {
            return this.mLyric;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setMusicListId(String musicListId) {
            this.musicListId = musicListId;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public void setHdImgUrl(String hdImgUrl) {
            this.hdImgUrl = hdImgUrl;
        }

        public void setmLyric(String mLyric) {
            this.mLyric = mLyric;
        }

        public boolean isCollected() {
            return this.isCollected;
        }

        public void setCollected(boolean isCollected) {
            this.isCollected = isCollected;
        }
    }
}
