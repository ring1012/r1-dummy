package com.unisound.vui.handler.session.music.playitem;

/* loaded from: classes.dex */
public class XmItem {
    private String album;
    private String artist;
    private String title;
    private int opr = -1;
    private int playStatus = -1;
    private int duration = -1;
    private int currentDuration = -1;

    public int getCurrentDuration() {
        return this.currentDuration;
    }

    public void setCurrentDuration(int currentDuration) {
        this.currentDuration = currentDuration;
    }

    public int getPlayStatus() {
        return this.playStatus;
    }

    public void setPlayStatus(int playStatus) {
        this.playStatus = playStatus;
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

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getOpr() {
        return this.opr;
    }

    public void setOpr(int opr) {
        this.opr = opr;
    }
}
