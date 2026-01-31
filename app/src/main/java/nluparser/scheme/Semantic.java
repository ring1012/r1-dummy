package nluparser.scheme;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import nluparser.scheme.Intent;

/* loaded from: classes.dex */
public class Semantic<T extends Intent> {

    @SerializedName("intent")
    @NonNull
    @JSONField(name = "intent")
    private T intent;

    @SerializedName("noDataHeader")
    @Nullable
    @JSONField(name = "noDataHeader")
    private String noDataHeader;

    @SerializedName("noDataHeaderTts")
    @Nullable
    @JSONField(name = "noDataHeaderTts")
    private String noDataHeaderTts;

    @SerializedName("normalHeader")
    @Nullable
    @JSONField(name = "normalHeader")
    private String normalHeader;

    @SerializedName("normalHeaderTts")
    @Nullable
    @JSONField(name = "normalHeaderTts")
    private String normalHeaderTts;

    @NonNull
    public T getIntent() {
        return this.intent;
    }

    @Nullable
    public String getNoDataHeader() {
        return this.noDataHeader;
    }

    @Nullable
    public String getNoDataHeaderTts() {
        return this.noDataHeaderTts;
    }

    @Nullable
    public String getNormalHeader() {
        return this.normalHeader;
    }

    @Nullable
    public String getNormalHeaderTts() {
        return this.normalHeaderTts;
    }

    public void setIntent(@NonNull T intent) {
        this.intent = intent;
    }

    public void setNoDataHeader(@Nullable String noDataHeader) {
        this.noDataHeader = noDataHeader;
    }

    public void setNoDataHeaderTts(@Nullable String noDataHeaderTts) {
        this.noDataHeaderTts = noDataHeaderTts;
    }

    public void setNormalHeader(@Nullable String normalHeader) {
        this.normalHeader = normalHeader;
    }

    public void setNormalHeaderTts(@Nullable String normalHeaderTts) {
        this.normalHeaderTts = normalHeaderTts;
    }
}
