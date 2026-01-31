package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/* loaded from: classes.dex */
public class AudioResult implements Result {

    @SerializedName("cover")
    @JSONField(name = "cover")
    String cover;

    @SerializedName("cover_large")
    @JSONField(name = "cover_large")
    String coverLarge;

    @SerializedName("description")
    @JSONField(name = "description")
    String description;

    @SerializedName(TtmlNode.ATTR_ID)
    @JSONField(name = TtmlNode.ATTR_ID)
    String id;

    @SerializedName("page")
    @JSONField(name = "page")
    String page;

    @SerializedName("pagesize")
    @JSONField(name = "pagesize")
    String pageSize;

    @SerializedName("playlist")
    @JSONField(name = "playlist")
    List<Music> playlist;

    @SerializedName("source")
    @JSONField(name = "source")
    String source;

    @SerializedName("title")
    @JSONField(name = "title")
    String title;

    @SerializedName("total")
    @JSONField(name = "total")
    String total;

    public static class Music {

        @SerializedName("cover")
        @JSONField(name = "cover")
        String cover;

        @SerializedName("cover_large")
        @JSONField(name = "cover_large")
        String coverLarge;

        @SerializedName("duration")
        @JSONField(name = "duration")
        String duration;

        @SerializedName(TtmlNode.ATTR_ID)
        @JSONField(name = TtmlNode.ATTR_ID)
        String id;

        @SerializedName("isCollected")
        @JSONField(name = "isCollected")
        boolean isCollected;

        @SerializedName("title")
        @JSONField(name = "title")
        String title;

        @SerializedName("update_time")
        @JSONField(name = "update_time")
        String updateTime;

        @SerializedName("url")
        @JSONField(name = "url")
        String url;

        @SerializedName("url_high")
        @JSONField(name = "url_high")
        String urlHigh;

        public String getCover() {
            return this.cover;
        }

        public String getCoverLarge() {
            return this.coverLarge;
        }

        public String getDuration() {
            return this.duration;
        }

        public String getId() {
            return this.id;
        }

        public String getTitle() {
            return this.title;
        }

        public String getUpdateTime() {
            return this.updateTime;
        }

        public String getUrl() {
            return this.url;
        }

        public String getUrlHigh() {
            return this.urlHigh;
        }

        public boolean isCollected() {
            return this.isCollected;
        }

        public void setCollected(boolean collected) {
            this.isCollected = collected;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setCoverLarge(String coverLarge) {
            this.coverLarge = coverLarge;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setUrlHigh(String urlHigh) {
            this.urlHigh = urlHigh;
        }
    }

    public String getCover() {
        return this.cover;
    }

    public String getCoverLarge() {
        return this.coverLarge;
    }

    public String getDescription() {
        return this.description;
    }

    public String getId() {
        return this.id;
    }

    public String getPage() {
        return this.page;
    }

    public String getPageSize() {
        return this.pageSize;
    }

    public List<Music> getPlaylist() {
        return this.playlist;
    }

    public String getSource() {
        return this.source;
    }

    public String getTitle() {
        return this.title;
    }

    public String getTotal() {
        return this.total;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setCoverLarge(String coverLarge) {
        this.coverLarge = coverLarge;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public void setPlaylist(List<Music> playlist) {
        this.playlist = playlist;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
