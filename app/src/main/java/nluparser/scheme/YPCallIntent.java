package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import java.util.Collections;
import java.util.List;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class YPCallIntent implements Intent {

    @SerializedName("contacts")
    @JSONField(name = "contacts")
    List<Contacts> contacts = Collections.emptyList();

    public static class Contacts {

        @SerializedName(Const.TableSchema.COLUMN_NAME)
        @JSONField(name = Const.TableSchema.COLUMN_NAME)
        String name;

        @SerializedName("numbers")
        @JSONField(name = "numbers")
        String numbers;

        public String getName() {
            return this.name;
        }

        public String getNumbers() {
            return this.numbers;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNumbers(String numbers) {
            this.numbers = numbers;
        }
    }

    public List<Contacts> getContacts() {
        return this.contacts;
    }

    public void setContacts(List<Contacts> contacts) {
        this.contacts = contacts;
    }
}
