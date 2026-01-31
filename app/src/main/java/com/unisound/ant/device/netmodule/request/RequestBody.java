package com.unisound.ant.device.netmodule.request;

/* loaded from: classes.dex */
public class RequestBody<T> {
    private String businessType;
    private String command;
    private T data;
    private ClientInfo tcl;
    private String version = "2.0.0";

    public String getBusinessType() {
        return this.businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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

    public ClientInfo getTcl() {
        return this.tcl;
    }

    public void setTcl(ClientInfo tcl) {
        this.tcl = tcl;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static class ClientInfo {
        private String clientId;
        private int subSysId = 9;
        private String token;

        public String getClientId() {
            return this.clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public int getSubSysId() {
            return this.subSysId;
        }

        public void setSubSysId(int subSysId) {
            this.subSysId = subSysId;
        }

        public String getToken() {
            return this.token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
