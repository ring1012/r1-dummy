package com.unisound.ant.device.service;

import android.support.annotation.Nullable;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.phicomm.speaker.device.custom.udid.DeviceIdProcessor;
import com.unisound.ant.device.profile.AgentProfileInfo;
import com.unisound.ant.device.profile.DeviceProfileInfo;
import com.unisound.ant.device.profile.DstServiceProfile;
import com.unisound.ant.device.profile.EnvironmentProfileInfo;
import com.unisound.ant.device.profile.UserProfileInfo;
import com.unisound.ant.device.sessionlayer.DialogProfile;
import com.unisound.sdk.ca;

/* loaded from: classes.dex */
public class DeviceRequest {

    @SerializedName("agentProfile")
    @Nullable
    @JSONField(name = "agentProfile")
    AgentProfileInfo agentProfile;

    @SerializedName(ca.c)
    @Nullable
    @JSONField(name = ca.c)
    String appkey;

    @SerializedName(DeviceIdProcessor.SP_DEVICE_ID)
    @Nullable
    @JSONField(name = DeviceIdProcessor.SP_DEVICE_ID)
    String deviceId;

    @SerializedName("deviceProfile")
    @Nullable
    @JSONField(name = "deviceProfile")
    DeviceProfileInfo deviceProfile;

    @SerializedName("dialogProfile")
    @Nullable
    @JSONField(name = "dialogProfile")
    DialogProfile dialogProfile;

    @SerializedName("enviromentProfile")
    @Nullable
    @JSONField(name = "enviromentProfile")
    EnvironmentProfileInfo enviromentProfile;

    @SerializedName("messageType")
    @Nullable
    @JSONField(name = "messageType")
    String messageType;

    @SerializedName("reqId")
    @Nullable
    @JSONField(name = "reqId")
    String reqId;

    @SerializedName("serviceProfile")
    @Nullable
    @JSONField(name = "serviceProfile")
    DstServiceProfile serviceProfile;

    @SerializedName("userProfile")
    @Nullable
    @JSONField(name = "userProfile")
    UserProfileInfo userProfile;

    @SerializedName("version")
    @Nullable
    @JSONField(name = "version")
    String version;

    @Nullable
    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(@Nullable String deviceId) {
        this.deviceId = deviceId;
    }

    @Nullable
    public String getAppkey() {
        return this.appkey;
    }

    public void setAppkey(@Nullable String appkey) {
        this.appkey = appkey;
    }

    @Nullable
    public String getVersion() {
        return this.version;
    }

    public void setVersion(@Nullable String version) {
        this.version = version;
    }

    @Nullable
    public String getReqId() {
        return this.reqId;
    }

    public void setReqId(@Nullable String reqId) {
        this.reqId = reqId;
    }

    @Nullable
    public String getMessageType() {
        return this.messageType;
    }

    public void setMessageType(@Nullable String messageType) {
        this.messageType = messageType;
    }

    @Nullable
    public DeviceProfileInfo getDeviceProfile() {
        return this.deviceProfile;
    }

    public void setDeviceProfile(@Nullable DeviceProfileInfo deviceProfile) {
        this.deviceProfile = deviceProfile;
    }

    @Nullable
    public DstServiceProfile getServiceProfile() {
        return this.serviceProfile;
    }

    public void setServiceProfile(@Nullable DstServiceProfile serviceProfile) {
        this.serviceProfile = serviceProfile;
    }

    @Nullable
    public DialogProfile getDialogProfile() {
        return this.dialogProfile;
    }

    public void setDialogProfile(@Nullable DialogProfile dialogProfile) {
        this.dialogProfile = dialogProfile;
    }

    @Nullable
    public EnvironmentProfileInfo getEnviromentProfile() {
        return this.enviromentProfile;
    }

    public void setEnviromentProfile(@Nullable EnvironmentProfileInfo enviromentProfile) {
        this.enviromentProfile = enviromentProfile;
    }

    @Nullable
    public AgentProfileInfo getAgentProfile() {
        return this.agentProfile;
    }

    public void setAgentProfile(@Nullable AgentProfileInfo agentProfile) {
        this.agentProfile = agentProfile;
    }

    @Nullable
    public UserProfileInfo getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(@Nullable UserProfileInfo userProfile) {
        this.userProfile = userProfile;
    }
}
