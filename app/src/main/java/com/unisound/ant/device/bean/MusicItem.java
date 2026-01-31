package com.unisound.ant.device.bean;

/* loaded from: classes.dex */
public class MusicItem {
    private String album;
    private String artist;
    private String itemId;
    private int itemType;
    private String title;
    private String url;

    public MusicItem(String title, String url, String itemId, int itemType, String album, String artist) {
        this.title = title;
        this.url = url;
        this.itemId = itemId;
        this.itemType = itemType;
        this.album = album;
        this.artist = artist;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemType() {
        return this.itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String toString() {
        return "title=" + this.title + "&url=" + this.url + "&itemId=" + this.itemId + "&itemType=" + this.itemType + "&album=" + this.album + "&artist=" + this.artist;
    }
}
