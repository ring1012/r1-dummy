package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.gson.annotations.SerializedName;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class General {

    @SerializedName("audio")
    @JSONField(name = "audio")
    String audio;

    @SerializedName("code")
    @JSONField(name = "code")
    String code;

    @SerializedName("emoji")
    @JSONField(name = "emoji")
    String emoji;

    @SerializedName("imgAlt")
    @JSONField(name = "imgAlt")
    String imgAlt;

    @SerializedName("imgUrl")
    @JSONField(name = "imgUrl")
    String imgUrl;

    @SerializedName("mood")
    @JSONField(name = "mood")
    String mood;

    @SerializedName("speaker")
    @JSONField(name = "speaker")
    Speaker speaker;

    @SerializedName(TtmlNode.TAG_STYLE)
    @JSONField(name = TtmlNode.TAG_STYLE)
    String style;

    @SerializedName("text")
    @JSONField(name = "text")
    String text;

    @SerializedName("textTts")
    @JSONField(name = "textTts")
    String textTts;

    @SerializedName(Const.TableSchema.COLUMN_TYPE)
    @JSONField(name = Const.TableSchema.COLUMN_TYPE)
    String type;

    @SerializedName("url")
    @JSONField(name = "url")
    String url;

    @SerializedName("urlAlt")
    @JSONField(name = "urlAlt")
    String urlAlt;

    public String getAudio() {
        return this.audio;
    }

    public String getCode() {
        return this.code;
    }

    public String getEmoji() {
        return this.emoji;
    }

    public String getImgAlt() {
        return this.imgAlt;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public String getMood() {
        return this.mood;
    }

    public Speaker getSpeaker() {
        return this.speaker;
    }

    public String getStyle() {
        return this.style;
    }

    public String getText() {
        return this.text;
    }

    public String getTextTts() {
        return this.textTts;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUrlAlt() {
        return this.urlAlt;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public void setImgAlt(String imgAlt) {
        this.imgAlt = imgAlt;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextTts(String textTts) {
        this.textTts = textTts;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUrlAlt(String urlAlt) {
        this.urlAlt = urlAlt;
    }
}
