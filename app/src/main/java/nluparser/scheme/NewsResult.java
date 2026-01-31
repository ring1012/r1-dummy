package nluparser.scheme;

import java.io.Serializable;
import java.util.List;

/* loaded from: classes.dex */
public class NewsResult implements Serializable, Result {
    private int count;
    private String dataSourceName;
    private int errorCode;
    private List<NewsBean> news;
    private String searchType;
    private int totalTime;
    private String tts;

    public static class NewsBean {
        private String audioUrl;
        private String createdTime;
        private double duration;
        private String humanTime;
        private String id;
        private String imageUrl;
        private String summary;
        private String tags;
        private String text;
        private String title;
        private String updatedTime;

        public String getAudioUrl() {
            return this.audioUrl;
        }

        public String getCreatedTime() {
            return this.createdTime;
        }

        public double getDuration() {
            return this.duration;
        }

        public String getHumanTime() {
            return this.humanTime;
        }

        public String getId() {
            return this.id;
        }

        public String getImageUrl() {
            return this.imageUrl;
        }

        public String getSummary() {
            return this.summary;
        }

        public String getTags() {
            return this.tags;
        }

        public String getText() {
            return this.text;
        }

        public String getTitle() {
            return this.title;
        }

        public String getUpdatedTime() {
            return this.updatedTime;
        }

        public void setAudioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public void setDuration(double duration) {
            this.duration = duration;
        }

        public void setHumanTime(String humanTime) {
            this.humanTime = humanTime;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setUpdatedTime(String updatedTime) {
            this.updatedTime = updatedTime;
        }

        public String toString() {
            return "NewsBean{id='" + this.id + "', summary='" + this.summary + "', tags='" + this.tags + "', audioUrl='" + this.audioUrl + "', duration=" + this.duration + ", text='" + this.text + "', title='" + this.title + "', createdTime='" + this.createdTime + "', imageUrl='" + this.imageUrl + "', humanTime='" + this.humanTime + "', updatedTime='" + this.updatedTime + "'}";
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

    public List<NewsBean> getNews() {
        return this.news;
    }

    public String getSearchType() {
        return this.searchType;
    }

    public int getTotalTime() {
        return this.totalTime;
    }

    public String getTts() {
        return this.tts;
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

    public void setNews(List<NewsBean> news) {
        this.news = news;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }
}
