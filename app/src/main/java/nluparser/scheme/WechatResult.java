package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/* loaded from: classes.dex */
public class WechatResult implements Result {

    @SerializedName("contactList")
    @JSONField(name = "contactList")
    List<String> contactList;

    public List<String> getContactList() {
        return this.contactList;
    }

    public void setContactList(List<String> contactList) {
        this.contactList = contactList;
    }
}
