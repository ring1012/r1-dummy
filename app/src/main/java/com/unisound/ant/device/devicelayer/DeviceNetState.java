package com.unisound.ant.device.devicelayer;

import android.support.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public class DeviceNetState {

    @SerializedName("inNetIp")
    @Nullable
    @JSONField(name = "inNetIp")
    String inNetIp;

    @SerializedName("netType")
    @Nullable
    @JSONField(name = "netType")
    String netType;

    @SerializedName("outNetIp")
    @Nullable
    @JSONField(name = "outNetIp")
    String outNetIp;

    @Nullable
    public String getNetType() {
        return this.netType;
    }

    public void setNetType(@Nullable String netType) {
        this.netType = netType;
    }

    @Nullable
    public String getInNetIp() {
        return this.inNetIp;
    }

    public void setInNetIp(@Nullable String inNetIp) {
        this.inNetIp = inNetIp;
    }

    @Nullable
    public String getOutNetIp() {
        return this.outNetIp;
    }

    public void setOutNetIp(@Nullable String outNetIp) {
        this.outNetIp = outNetIp;
    }
}
