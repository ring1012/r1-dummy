package com.unisound.vui.transport.out;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class OptionContent {

    @SerializedName("optionKey")
    @JSONField(name = "optionKey")
    private String optionKey;

    @SerializedName("optionValue")
    @JSONField(name = "optionValue")
    private Object optionValue;

    public OptionContent() {
    }

    public OptionContent(String optionKey, Object optionValue) {
        this.optionKey = optionKey;
        this.optionValue = optionValue;
    }

    public String getOptionKey() {
        return this.optionKey;
    }

    public Object getOptionValue() {
        return this.optionValue;
    }

    public void setOptionKey(String optionKey) {
        this.optionKey = optionKey;
    }

    public void setOptionValue(Object optionValue) {
        this.optionValue = optionValue;
    }
}
