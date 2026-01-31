package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class Speaker {
    public static final String GENDER_CHILDREN = "CHILDREN";
    public static final String GENDER_FEMALE = "FEMALE";
    public static final String GENDER_LZL = "LZL";
    public static final String GENDER_MALE = "MALE";
    public static final String GENDER_SWEET = "SWEET";

    @SerializedName("age")
    @JSONField(name = "age")
    int age;

    @SerializedName("gender")
    @JSONField(name = "gender")
    String gender;

    @SerializedName(Const.TableSchema.COLUMN_NAME)
    @JSONField(name = Const.TableSchema.COLUMN_NAME)
    String name;

    public int getAge() {
        return this.age;
    }

    public String getGender() {
        return this.gender;
    }

    public String getName() {
        return this.name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }
}
