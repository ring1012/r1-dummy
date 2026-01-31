package com.unisound.ant.device.profile;

import com.unisound.ant.device.bean.DeviceCapability;
import com.unisound.ant.device.bean.GeneralInfo;
import com.unisound.ant.device.bean.Parameter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class DeviceProfileInfo extends Parameter {
    private String aiChip;
    private List<DeviceCapability> capabilites = new ArrayList();
    private String category;
    private String deviceModel;
    private String deviceType;
    private GeneralInfo generalInfo;
    private String udid;

    public String getUdid() {
        return this.udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceModel() {
        return this.deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAiChip() {
        return this.aiChip;
    }

    public void setAiChip(String aiChip) {
        this.aiChip = aiChip;
    }

    public List<DeviceCapability> getCapabilites() {
        return this.capabilites;
    }

    public void setCapabilites(List<DeviceCapability> capabilites) {
        this.capabilites = capabilites;
    }

    public GeneralInfo getGeneralInfo() {
        return this.generalInfo;
    }

    public void setGeneralInfo(GeneralInfo generalInfo) {
        this.generalInfo = generalInfo;
    }
}
