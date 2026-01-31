package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class WechatIntent implements Intent {

    @SerializedName("content")
    @JSONField(name = "content")
    String content;

    @SerializedName(Const.TableSchema.COLUMN_NAME)
    @JSONField(name = Const.TableSchema.COLUMN_NAME)
    String name;

    @SerializedName("ttsInfo")
    @JSONField(name = "ttsInfo")
    MessageTtsInfo ttsInfo;

    public String getContent() {
        return this.content;
    }

    public String getName() {
        return this.name;
    }

    public MessageTtsInfo getTtsInfo() {
        return this.ttsInfo;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTtsInfo(MessageTtsInfo ttsInfo) {
        this.ttsInfo = ttsInfo;
    }
}
