package nluparser.scheme;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.unisound.common.y;
import com.unisound.sdk.i;
import nluparser.scheme.Intent;
import nluparser.scheme.Result;

/* loaded from: classes.dex */
public class NLU<I extends Intent, R extends Result> implements Comparable<NLU> {

    @SerializedName("asrResult")
    @JSONField(name = "asrResult")
    String asrResult;

    @SerializedName("code")
    @NonNull
    @JSONField(name = "code")
    String code;

    @SerializedName("data")
    @Nullable
    @JSONField(name = "data")
    Data<R> data;

    @SerializedName(y.I)
    @Nullable
    @JSONField(name = y.I)
    Error error;

    @SerializedName("extraFlag")
    @JSONField(name = "extraFlag")
    String extraFlag;

    @SerializedName("gender")
    @Nullable
    @JSONField(name = "gender")
    String gender;

    @SerializedName(i.i)
    @Nullable
    @JSONField(name = i.i)
    General general;

    @SerializedName("history")
    @NonNull
    @JSONField(name = "history")
    String history;

    @SerializedName("html5Url")
    @Nullable
    @JSONField(name = "html5Url")
    String html5Url;

    @SerializedName("iotUniJson")
    @JSONField(name = "iotUniJson")
    JsonObject iotUniJson;

    @SerializedName("isLocalNLU")
    @JSONField(name = "isLocalNLU")
    boolean isLocalNLU;

    @SerializedName("isNeedAbandonNlu")
    @JSONField(name = "isNeedAbandonNlu")
    boolean isNeedAbandonNlu;

    @SerializedName("isQueenMessage")
    @JSONField(name = "isQueenMessage")
    boolean isQueenMessage;

    @SerializedName("rc")
    @NonNull
    @JSONField(name = "rc")
    int responseCode;

    @SerializedName("responseId")
    @Nullable
    @JSONField(name = "responseId")
    String responseId;

    @SerializedName("semantic")
    @Nullable
    @JSONField(name = "semantic")
    Semantic<I> semantic;

    @SerializedName("service")
    @NonNull
    @JSONField(name = "service")
    String service;

    @SerializedName("text")
    @NonNull
    @JSONField(name = "text")
    String text;

    @SerializedName("time")
    @JSONField(name = "time")
    long time;

    @Override // java.lang.Comparable
    public int compareTo(NLU another) {
        return (int) (getTime() - another.getTime());
    }

    public String getAsrResult() {
        return this.asrResult;
    }

    @NonNull
    public String getCode() {
        return this.code;
    }

    @Nullable
    public Data<R> getData() {
        return this.data;
    }

    @Nullable
    public Error getError() {
        return this.error;
    }

    public String getExtraFlag() {
        return this.extraFlag;
    }

    @Nullable
    public String getGender() {
        return this.gender;
    }

    @Nullable
    public General getGeneral() {
        return this.general;
    }

    @NonNull
    public String getHistory() {
        return this.history;
    }

    @Nullable
    public String getHtml5Url() {
        return this.html5Url;
    }

    public JsonObject getIotUniJson() {
        return this.iotUniJson;
    }

    @NonNull
    public int getResponseCode() {
        return this.responseCode;
    }

    @Nullable
    public String getResponseId() {
        return this.responseId;
    }

    @Nullable
    public Semantic<I> getSemantic() {
        return this.semantic;
    }

    @NonNull
    public String getService() {
        return this.service;
    }

    @NonNull
    public String getText() {
        return this.text;
    }

    public long getTime() {
        return this.time;
    }

    public boolean isLocalNLU() {
        return this.isLocalNLU;
    }

    public boolean isNeedAbandonNlu() {
        return this.isNeedAbandonNlu;
    }

    public boolean isQueenMessage() {
        return this.isQueenMessage;
    }

    public void setAsrResult(String asrResult) {
        this.asrResult = asrResult;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    public void setData(@Nullable Data<R> data) {
        this.data = data;
    }

    public void setError(@Nullable Error error) {
        this.error = error;
    }

    public void setExtraFlag(String extraFlag) {
        this.extraFlag = extraFlag;
    }

    public void setGender(@Nullable String gender) {
        this.gender = gender;
    }

    public void setGeneral(@Nullable General general) {
        this.general = general;
    }

    public void setHistory(@NonNull String history) {
        this.history = history;
    }

    public void setHtml5Url(@Nullable String html5Url) {
        this.html5Url = html5Url;
    }

    public void setIotUniJson(@Nullable JsonObject iotUniJson) {
        this.iotUniJson = iotUniJson;
    }

    public void setLocalNLU(boolean localNLU) {
        this.isLocalNLU = localNLU;
    }

    public void setNeedAbandonNlu(boolean needAbandonNlu) {
        this.isNeedAbandonNlu = needAbandonNlu;
    }

    public void setQueenMessage(boolean queenMessage) {
        this.isQueenMessage = queenMessage;
    }

    public void setResponseCode(@NonNull int responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseId(@Nullable String responseId) {
        this.responseId = responseId;
    }

    public void setSemantic(@Nullable Semantic<I> semantic) {
        this.semantic = semantic;
    }

    public void setService(@NonNull String service) {
        this.service = service;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
