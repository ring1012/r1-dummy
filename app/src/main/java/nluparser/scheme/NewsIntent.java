package nluparser.scheme;

import java.io.Serializable;

/* loaded from: classes.dex */
public class NewsIntent implements Serializable, Intent {
    private String keyword;

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
