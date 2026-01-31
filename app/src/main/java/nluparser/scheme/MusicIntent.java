package nluparser.scheme;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class MusicIntent implements Intent {

    @SerializedName("album")
    @Nullable
    @JSONField(name = "album")
    String album;

    @SerializedName("artist")
    @Nullable
    @JSONField(name = "artist")
    String artist;

    @SerializedName("artistType")
    @Nullable
    @JSONField(name = "artistType")
    String artistType;

    @SerializedName("billboard")
    @Nullable
    @JSONField(name = "billboard")
    String billboard;

    @SerializedName("episode")
    @Nullable
    @JSONField(name = "episode")
    String episode;

    @SerializedName("genre")
    @Nullable
    @JSONField(name = "genre")
    String genre;

    @SerializedName("keyword")
    @NonNull
    @JSONField(name = "keyword")
    String keyword;

    @SerializedName("language")
    @Nullable
    @JSONField(name = "language")
    String language;

    @SerializedName("lyrics")
    @Nullable
    @JSONField(name = "lyrics")
    String lyrics;

    @SerializedName("mood")
    @Nullable
    @JSONField(name = "mood")
    String mood;

    @SerializedName("musicTag")
    @Nullable
    @JSONField(name = "musicTag")
    String musicTag;

    @SerializedName("scene")
    @Nullable
    @JSONField(name = "scene")
    String scene;

    @SerializedName("song")
    @Nullable
    @JSONField(name = "song")
    String song;

    @SerializedName("songlist")
    @Nullable
    @JSONField(name = "songlist")
    String songlist;

    @Nullable
    public String getAlbum() {
        return this.album;
    }

    @Nullable
    public String getArtist() {
        return this.artist;
    }

    @Nullable
    public String getArtistType() {
        return this.artistType;
    }

    @Nullable
    public String getBillboard() {
        return this.billboard;
    }

    @Nullable
    public String getEpisode() {
        return this.episode;
    }

    @Nullable
    public String getGenre() {
        return this.genre;
    }

    @NonNull
    public String getKeyword() {
        return this.keyword;
    }

    @Nullable
    public String getLanguage() {
        return this.language;
    }

    @Nullable
    public String getLyrics() {
        return this.lyrics;
    }

    @Nullable
    public String getMood() {
        return this.mood;
    }

    @Nullable
    public String getMusicTag() {
        return this.musicTag;
    }

    @Nullable
    public String getScene() {
        return this.scene;
    }

    @Nullable
    public String getSong() {
        return this.song;
    }

    @Nullable
    public String getSonglist() {
        return this.songlist;
    }

    public void setAlbum(@Nullable String album) {
        this.album = album;
    }

    public void setArtist(@Nullable String artist) {
        this.artist = artist;
    }

    public void setArtistType(@Nullable String artistType) {
        this.artistType = artistType;
    }

    public void setBillboard(@Nullable String billboard) {
        this.billboard = billboard;
    }

    public void setEpisode(@Nullable String episode) {
        this.episode = episode;
    }

    public void setGenre(@Nullable String genre) {
        this.genre = genre;
    }

    public void setKeyword(@NonNull String keyword) {
        this.keyword = keyword;
    }

    public void setLanguage(@Nullable String language) {
        this.language = language;
    }

    public void setLyrics(@Nullable String lyrics) {
        this.lyrics = lyrics;
    }

    public void setMood(@Nullable String mood) {
        this.mood = mood;
    }

    public void setMusicTag(@Nullable String musicTag) {
        this.musicTag = musicTag;
    }

    public void setScene(@Nullable String scene) {
        this.scene = scene;
    }

    public void setSong(@Nullable String song) {
        this.song = song;
    }

    public void setSonglist(@Nullable String songlist) {
        this.songlist = songlist;
    }
}
