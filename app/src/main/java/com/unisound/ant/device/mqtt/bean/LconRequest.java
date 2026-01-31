package com.unisound.ant.device.mqtt.bean;

/* loaded from: classes.dex */
public class LconRequest<T> {
    String command;
    T data;
    EffectiveToken tcl;
    String version;

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public EffectiveToken getTcl() {
        return this.tcl;
    }

    public void setTcl(EffectiveToken tcl) {
        this.tcl = tcl;
    }

    public static class EffectiveToken {
        private String clientId;
        private String token;

        public EffectiveToken(String clientId, String token) {
            this.clientId = clientId;
            this.token = token;
        }

        public String getClientId() {
            return this.clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getToken() {
            return this.token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
