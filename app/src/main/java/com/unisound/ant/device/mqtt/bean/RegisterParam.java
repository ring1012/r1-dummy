package com.unisound.ant.device.mqtt.bean;

import android.text.TextUtils;
import java.net.URLEncoder;

/* loaded from: classes.dex */
public class RegisterParam {
    public static final String ENCODING_UTF8 = "UTF-8";
    private static final String dataVersion = "v1";
    public static final int subsystemId = 4;
    private String appKey;
    private String appSecret;
    private String extras = "extras params";
    private String tcDeviceId;
    private String token;
    private String udid;

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public void setTcDeviceId(String tcDeviceId) {
        this.tcDeviceId = tcDeviceId;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getSubsystemId() {
        try {
            return URLEncoder.encode("4", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDataVersion() {
        try {
            return URLEncoder.encode(dataVersion, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAppKey() {
        try {
            return URLEncoder.encode(this.appKey, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAppSecret() {
        try {
            return URLEncoder.encode(this.appSecret, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTcDeviceId() {
        try {
            return URLEncoder.encode(this.tcDeviceId, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUdid() {
        try {
            return URLEncoder.encode(this.udid, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getToken() {
        if (TextUtils.isEmpty(this.token)) {
            return "";
        }
        try {
            return URLEncoder.encode(this.token, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getExtras() {
        try {
            return URLEncoder.encode(this.extras, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString() {
        return "RegisterParam{appKey='" + this.appKey + "', appSecret='" + this.appSecret + "', tcDeviceId='" + this.tcDeviceId + "', udid='" + this.udid + "', token='" + this.token + "'}";
    }
}
