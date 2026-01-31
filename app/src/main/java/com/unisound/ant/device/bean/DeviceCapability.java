package com.unisound.ant.device.bean;

import java.util.List;

/* loaded from: classes.dex */
public class DeviceCapability {
    String capability;
    Detail detailInfo;
    List<String> operations;

    public String getCapability() {
        return this.capability;
    }

    public void setCapability(String capability) {
        this.capability = capability;
    }

    public List<String> getOperations() {
        return this.operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public Detail getDetailInfo() {
        return this.detailInfo;
    }

    public void setDetailInfo(Detail detailInfo) {
        this.detailInfo = detailInfo;
    }

    public static final class Detail {
        List<Vendors> supportVendors;

        public List<Vendors> getSupportVendors() {
            return this.supportVendors;
        }

        public void setSupportVendors(List<Vendors> supportVendors) {
            this.supportVendors = supportVendors;
        }
    }

    public static final class Vendors {
        String extraInfo;
        String vendorName;

        public String getVendorName() {
            return this.vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public String getExtraInfo() {
            return this.extraInfo;
        }

        public void setExtraInfo(String extraInfo) {
            this.extraInfo = extraInfo;
        }
    }
}
