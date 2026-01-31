package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class APPIntent implements Intent {
    public static final String FUNC_AUDIO_CAPTURE = "FUNC_AUDIO_CAPTURE";
    public static final String FUNC_IMAGE_CAPTURE = "FUNC_IMAGE_CAPTURE";
    public static final String FUNC_IMAGE_VIEW = "FUNC_IMAGE_VIEW";
    public static final String FUNC_MUSIC_PLAY = "FUNC_MUSIC_PLAY";
    public static final String FUNC_OPEN_CALENDAR = "FUNC_OPEN_CALENDAR";
    public static final String FUNC_OPEN_CONTACT = "FUNC_OPEN_CONTACT";
    public static final String FUNC_OPEN_MAIL = "FUNC_OPEN_MAIL";
    public static final String FUNC_OPEN_MAP = "FUNC_OPEN_MAP";
    public static final String FUNC_OPEN_RADIO = "FUNC_OPEN_RADIO";
    public static final String FUNC_OPEN_WEB = "FUNC_OPEN_WEB";
    public static final String FUNC_SEARCH = "FUNC_SEARCH";
    public static final String FUNC_SETTING = "FUNC_SETTING";
    public static final String FUNC_VIDEO_CAPTURE = "FUNC_VIDEO_CAPTURE";

    @SerializedName("function")
    @JSONField(name = "function")
    String function;

    @SerializedName(Const.TableSchema.COLUMN_NAME)
    @JSONField(name = Const.TableSchema.COLUMN_NAME)
    String name;

    @SerializedName("url")
    @JSONField(name = "url")
    String url;

    public String getFunction() {
        return this.function;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
