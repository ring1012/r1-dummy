package nluparser.scheme;

/* loaded from: classes.dex */
public class MusicIntentBuilder {
    private String album;
    private String artist;
    private String artistType;
    private String billboard;
    private String episode;
    private String genre;
    private String keyword;
    private String language;
    private String lyrics;
    private String mood;
    private String musicTag;
    private String scene;
    private String song;
    private String songlist;

    public MusicIntent build() {
        MusicIntent musicIntent = new MusicIntent();
        musicIntent.song = this.song;
        musicIntent.artist = this.artist;
        musicIntent.genre = this.genre;
        musicIntent.musicTag = this.musicTag;
        musicIntent.mood = this.mood;
        musicIntent.scene = this.scene;
        musicIntent.billboard = this.billboard;
        musicIntent.language = this.language;
        musicIntent.lyrics = this.lyrics;
        musicIntent.songlist = this.songlist;
        musicIntent.keyword = this.keyword;
        musicIntent.album = this.album;
        musicIntent.artistType = this.artistType;
        musicIntent.episode = this.episode;
        return musicIntent;
    }

    public MusicIntentBuilder setAlbum(String album) {
        this.album = album;
        return this;
    }

    public MusicIntentBuilder setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public MusicIntentBuilder setArtistType(String artistType) {
        this.artistType = artistType;
        return this;
    }

    public MusicIntentBuilder setBillboard(String billboard) {
        this.billboard = billboard;
        return this;
    }

    public MusicIntentBuilder setEpisode(String episode) {
        this.episode = episode;
        return this;
    }

    public MusicIntentBuilder setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public MusicIntentBuilder setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public MusicIntentBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }

    public MusicIntentBuilder setLyrics(String lyrics) {
        this.lyrics = lyrics;
        return this;
    }

    public MusicIntentBuilder setMood(String mood) {
        this.mood = mood;
        return this;
    }

    public MusicIntentBuilder setMusicTag(String musicTag) {
        this.musicTag = musicTag;
        return this;
    }

    public MusicIntentBuilder setScene(String scene) {
        this.scene = scene;
        return this;
    }

    public MusicIntentBuilder setSong(String song) {
        this.song = song;
        return this;
    }

    public MusicIntentBuilder setSonglist(String songlist) {
        this.songlist = songlist;
        return this;
    }
}
