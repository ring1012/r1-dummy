package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/* loaded from: classes.dex */
public class CookBookResult implements Result {

    @SerializedName("cookBooks")
    @JSONField(name = "cookBooks")
    List<CookBook> cookBooks;

    @SerializedName("count")
    @JSONField(name = "count")
    String count;

    @SerializedName("dataSourceName")
    @JSONField(name = "dataSourceName")
    String dataSourceName;

    @SerializedName("errorCode")
    @JSONField(name = "errorCode")
    int errorCode;

    @SerializedName("source")
    @JSONField(name = "source")
    String source;

    @SerializedName("totalTime")
    @JSONField(name = "totalTime")
    int totalTime;

    public static class CookBook {
        private String content;
        private String dishName;
        private int id;
        private String imgAddress;
        private String webAddress;

        public String getContent() {
            return this.content;
        }

        public String getDishName() {
            return this.dishName;
        }

        public int getId() {
            return this.id;
        }

        public String getImgAddress() {
            return this.imgAddress;
        }

        public String getWebAddress() {
            return this.webAddress;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setDishName(String dishName) {
            this.dishName = dishName;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setImgAddress(String imgAddress) {
            this.imgAddress = imgAddress;
        }

        public void setWebAddress(String webAddress) {
            this.webAddress = webAddress;
        }
    }

    public List<CookBook> getCookBooks() {
        return this.cookBooks;
    }

    public String getCount() {
        return this.count;
    }

    public String getDataSourceName() {
        return this.dataSourceName;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getSource() {
        return this.source;
    }

    public int getTotalTime() {
        return this.totalTime;
    }

    public void setCookBooks(List<CookBook> cookBooks) {
        this.cookBooks = cookBooks;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
}
