package com.unisound.ant.device.mqtt.bean;

import com.unisound.ant.device.mqtt.MsgUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class MessageParam {
    private int pageNo;
    private int pageSize;
    private String signature;
    private String timestamp = MsgUtil.getCurrentUnixTimestamp() + "";
    private String udid;

    public String getUdid() {
        return this.udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getSignature() {
        List<String> params = new ArrayList<>();
        params.add(getUdid());
        params.add(getPageNo() + "");
        if (getPageSize() != 0) {
            params.add(getPageSize() + "");
        }
        params.add(getTimestamp());
        this.signature = MsgUtil.buildSignature(params);
        return this.signature;
    }

    public String formateParam() {
        StringBuffer urlParams = new StringBuffer();
        urlParams.append("&udid=").append(this.udid).append("&pageNo=").append(this.pageNo).append("&pageSize=").append(this.pageSize).append("&timestamp=").append(this.timestamp).append("&timestamp=").append(this.timestamp).append("&signature=").append(getSignature());
        return urlParams.toString();
    }
}
