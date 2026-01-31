package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public class MusicResult implements Result {

    @SerializedName("count")
    @JSONField(name = "count")
    int count;

    @SerializedName("dataSourceName")
    @JSONField(name = "dataSourceName")
    String dataSourceName;

    @SerializedName("errorCode")
    @JSONField(name = "errorCode")
    int errorCode;

    @SerializedName("musicinfo")
    @JSONField(name = "musicinfo")
    List<Music> musicinfo = Collections.emptyList();

    @SerializedName("searchType")
    @JSONField(name = "searchType")
    String searchType;

    @SerializedName("updateTime")
    @JSONField(name = "updateTime")
    String updateTime;

    public static class Music {

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

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Music music = (Music) o;
            if (this.title.equals(music.title)) {
                return this.artist.equals(music.artist);
            }
            return false;
        }

        public String getAlbum() {
            return this.album;
        }

        public String getArtist() {
            return this.artist;
        }

        public int getDuration() {
            return this.duration;
        }

        public int getErrorCode() {
            return this.errorCode;
        }

        public String getHdImgUrl() {
            return this.hdImgUrl;
        }

        public String getId() {
            return this.id;
        }

        public String getImgUrl() {
            return this.imgUrl;
        }

        public String getMusicListId() {
            return this.musicListId;
        }

        public String getTitle() {
            return this.title;
        }

        public String getUrl() {
            return this.url;
        }

        public String getmLyric() {
            return this.mLyric;
        }

        public int hashCode() {
            return getTitle().hashCode() & getArtist().hashCode();
        }

        public boolean isCollected() {
            return this.isCollected;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public void setCollected(boolean isCollected) {
            this.isCollected = isCollected;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public void setHdImgUrl(String hdImgUrl) {
            this.hdImgUrl = hdImgUrl;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public void setMusicListId(String musicListId) {
            this.musicListId = musicListId;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setmLyric(String mLyric) {
            this.mLyric = mLyric;
        }
    }

    public int getCount() {
        return this.count;
    }

    public String getDataSourceName() {
        return this.dataSourceName;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public List<Music> getMusicinfo() {
        return this.musicinfo;
    }

    public String getSearchType() {
        return this.searchType;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setMusicinfo(List<Music> musicinfo) {
        this.musicinfo = musicinfo;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
