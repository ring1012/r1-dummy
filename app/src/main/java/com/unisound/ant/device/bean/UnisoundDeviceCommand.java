package com.unisound.ant.device.bean;

import android.text.TextUtils;

/* loaded from: classes.dex */
public class UnisoundDeviceCommand<T> {
    private String capability;
    private String operation;
    private T parameter;
    private String version;

    public UnisoundDeviceCommand(String version, String capability, String operation, T parameter) {
        this.version = version;
        this.capability = capability;
        this.operation = operation;
        this.parameter = parameter;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCapability() {
        return this.capability;
    }

    public void setCapability(String capability) {
        this.capability = capability;
    }

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public T getParameter() {
        return this.parameter;
    }

    public static class Builder<T> {
        private String capability;
        private String operation;
        private T parameter;
        private String version;

        public Builder setCapability(String capability) {
            this.capability = capability;
            return this;
        }

        public Builder setOperation(String operation) {
            this.operation = operation;
            return this;
        }

        public Builder setVersion(String version) {
            this.version = version;
            return this;
        }

        public Builder setParameter(T parameter) {
            this.parameter = parameter;
            return this;
        }

        void check() {
            if (TextUtils.isEmpty(this.capability)) {
                throw new IllegalArgumentException("capability may not be null.");
            }
            if (TextUtils.isEmpty(this.operation)) {
                throw new IllegalArgumentException("operation may not be null.");
            }
        }

        public UnisoundDeviceCommand build() {
            check();
            return new UnisoundDeviceCommand(this.version, this.capability, this.operation, this.parameter);
        }
    }
}
