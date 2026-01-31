package nluparser.scheme;

import android.support.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class AudioIntent extends MusicIntent {

    @SerializedName("category")
    @Nullable
    @JSONField(name = "category")
    String category;

    @SerializedName(Const.TableSchema.COLUMN_NAME)
    @Nullable
    @JSONField(name = Const.TableSchema.COLUMN_NAME)
    String name;

    @SerializedName("tag")
    @Nullable
    @JSONField(name = "tag")
    String tag;

    @Nullable
    public String getCategory() {
        return this.category;
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    @Nullable
    public String getTag() {
        return this.tag;
    }

    public void setCategory(@Nullable String category) {
        this.category = category;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setTag(@Nullable String tag) {
        this.tag = tag;
    }
}
