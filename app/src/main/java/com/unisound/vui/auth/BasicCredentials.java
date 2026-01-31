package com.unisound.vui.auth;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes.dex */
public final class BasicCredentials implements UNIOSCredentials {

    @SerializedName("accessKey")
    @JSONField(name = "accessKey")
    private String accessKey;

    @SerializedName("secretKey")
    @JSONField(name = "secretKey")
    private String secretKey;

    public BasicCredentials() {
    }

    public BasicCredentials(String accessKey, String secretKey) {
        if (accessKey == null) {
            throw new IllegalArgumentException("Access key cannot be null.");
        }
        if (secretKey == null) {
            throw new IllegalArgumentException("Secret key cannot be null.");
        }
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Override // com.unisound.vui.auth.UNIOSCredentials
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BasicCredentials basicCredentials = (BasicCredentials) o;
        return this.accessKey.equals(basicCredentials.accessKey) && !this.secretKey.equals(basicCredentials.secretKey);
    }

    @Override // com.unisound.vui.auth.UNIOSCredentials
    public String getAccessKey() {
        return this.accessKey;
    }

    @Override // com.unisound.vui.auth.UNIOSCredentials
    public String getSecretKey() {
        return this.secretKey;
    }

    @Override // com.unisound.vui.auth.UNIOSCredentials
    public int hashCode() {
        return (this.accessKey.hashCode() * 31) + this.secretKey.hashCode();
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
