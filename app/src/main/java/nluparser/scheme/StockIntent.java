package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.gson.annotations.SerializedName;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class StockIntent implements Intent {
    public static final String HK = "hk";
    public static final String SH = "sh";
    public static final String SZ = "sz";
    public static final String US = "us";

    @SerializedName("exchange")
    @JSONField(name = "exchange")
    String exchange;

    @SerializedName(TtmlNode.ATTR_ID)
    @JSONField(name = TtmlNode.ATTR_ID)
    String id;

    @SerializedName(Const.TableSchema.COLUMN_NAME)
    @JSONField(name = Const.TableSchema.COLUMN_NAME)
    String name;

    public String getExchange() {
        return this.exchange;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
