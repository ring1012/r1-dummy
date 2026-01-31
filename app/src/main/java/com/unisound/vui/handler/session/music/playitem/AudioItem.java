package com.unisound.vui.handler.session.music.playitem;

import com.unisound.vui.handler.session.music.playitem.PlayItem;
import nluparser.scheme.AudioResult;

/* loaded from: classes.dex */
public final class AudioItem implements PlayItem {
    static final String EMPTY = "";
    String album;
    String artist;
    AudioResult.Music audio;

    AudioItem(AudioResult.Music audio, String album, String artist) {
        this.audio = null;
        this.audio = audio;
        this.album = album == null ? "" : album;
        this.artist = artist == null ? "" : artist;
    }

    public AudioItem() {
        this.audio = null;
    }

    public void setAudio(AudioResult.Music audio) {
        this.audio = audio;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public String getId() {
        return this.audio.getId();
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public String getListId() {
        return null;
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public void setListId(String listId) {
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public String getTitle() {
        return this.audio.getTitle();
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public String getArtist() {
        return this.artist;
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public String getAlbum() {
        return this.album;
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public String getUrl() {
        return this.audio.getUrlHigh();
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public int getDuration() {
        String duration = this.audio.getDuration();
        if (duration == null) {
            return 0;
        }
        return Double.valueOf(duration).intValue();
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public String getImgUrl() {
        return this.audio.getCover();
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public String getHdImgUrl() {
        return this.audio.getCoverLarge();
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public String getmLyric() {
        return "";
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public PlayItem.ItemType getType() {
        return PlayItem.ItemType.TYPE_AUDIO;
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public void setCollected(boolean isCollected) {
        this.audio.setCollected(isCollected);
    }

    @Override // com.unisound.vui.handler.session.music.playitem.PlayItem
    public boolean isCollected() {
        return this.audio.isCollected();
    }

    public String toString() {
        return "id=" + getId() + "&title=" + getTitle() + "&artist=" + getArtist() + "&album=" + getAlbum() + "&url=" + getUrl() + "&duration=" + getDuration() + "&imgUrl=" + getImgUrl() + "&hdImgUrl=" + getHdImgUrl() + "&mLyric=" + getmLyric() + "&isCollected=" + isCollected();
    }
}
