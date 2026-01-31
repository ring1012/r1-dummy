package nluparser.scheme;

/* loaded from: classes.dex */
public class AudioIntentBuilder {
    private String album;
    private String artist;
    private String category;
    private String episode;
    private String keyword;
    private String name;
    private String tag;

    public AudioIntent build() {
        AudioIntent audioIntent = new AudioIntent();
        audioIntent.episode = this.episode;
        audioIntent.category = this.category;
        audioIntent.tag = this.tag;
        audioIntent.album = this.album;
        audioIntent.keyword = this.keyword;
        audioIntent.artist = this.artist;
        return audioIntent;
    }

    public AudioIntentBuilder setAlbum(String album) {
        this.album = album;
        return this;
    }

    public AudioIntentBuilder setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public AudioIntentBuilder setCategory(String category) {
        this.category = category;
        return this;
    }

    public AudioIntentBuilder setEpisode(String episode) {
        this.episode = episode;
        return this;
    }

    public AudioIntentBuilder setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public AudioIntentBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public AudioIntentBuilder setTag(String tag) {
        this.tag = tag;
        return this;
    }
}
