package com.unisound.ant.device.mqtt.bean;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/* loaded from: classes.dex */
public class ChannelParams {
    private String appKey;
    private String appSecret;
    public String connAccessId;
    public String mHBOwnerId;
    public String mHBServerIp;
    public int mHBServerPort;
    public String mSRDeviceId;
    public String mSROwnerId;
    public String mSRServerIp = "10.10.11.2";
    public int mSRServerPort = 8090;
    private String tcDeviceId = "kDzBV2xaZnShsidc6FD+swkDpKV54renxqDEH+OmemY=";
    private String token = null;
    public String udid;

    public ChannelParams(Context context, String udid) {
        if (TextUtils.isEmpty(udid)) {
            TelephonyManager phoneManager = (TelephonyManager) context.getSystemService("phone");
            udid = phoneManager.getDeviceId();
        }
        this.udid = udid;
        this.mHBOwnerId = udid + "_owner";
        this.mSRDeviceId = udid;
        this.mSROwnerId = udid + "_owner";
    }

    public String getUdid() {
        return this.udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getConnAccessId() {
        return this.connAccessId;
    }

    public void setConnAccessId(String connAccessId) {
        this.connAccessId = connAccessId;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getTcDeviceId() {
        return this.tcDeviceId;
    }

    public void setTcDeviceId(String tcDeviceId) {
        this.tcDeviceId = tcDeviceId;
    }

    public String getToken() throws UnsupportedEncodingException {
        if (TextUtils.isEmpty(this.token)) {
            return "";
        }
        try {
            URLEncoder.encode(this.token, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String toString() {
        return "ChannelParams{mHBServerIp='" + this.mHBServerIp + "', mHBServerPort=" + this.mHBServerPort + ", udid='" + this.udid + "', mHBOwnerId='" + this.mHBOwnerId + "', mSRServerIp='" + this.mSRServerIp + "', mSRServerPort=" + this.mSRServerPort + ", mSRDeviceId='" + this.mSRDeviceId + "', mSROwnerId='" + this.mSROwnerId + "', connAccessId='" + this.connAccessId + "', appKey='" + this.appKey + "', appSecret='" + this.appSecret + "', tcDeviceId='" + this.tcDeviceId + "', token='" + this.token + "'}";
    }
}
