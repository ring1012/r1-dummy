package nluparser.scheme;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class ContactIntent extends CallIntent {

    @SerializedName("receiverName")
    @JSONField(name = "receiverName")
    String receiverName;

    @SerializedName("receiverNumber")
    @JSONField(name = "receiverNumber")
    String receiverNumber;

    public String getReceiverName() {
        return this.receiverName;
    }

    public String getReceiverNumber() {
        return this.receiverNumber;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }
}
