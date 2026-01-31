package com.unisound.ant.device.mqtt.bean;

/* loaded from: classes.dex */
public class LconResponse {
    LconData data;
    String detailInfo;
    String statusCode;

    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDetailInfo() {
        return this.detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public LconData getData() {
        return this.data;
    }

    public void setData(LconData data) {
        this.data = data;
    }

    public static class LconData {
        String connAccessId;
        String queryIP;
        String queryPort;

        public String getQueryIP() {
            return this.queryIP;
        }

        public void setQueryIP(String queryIP) {
            this.queryIP = queryIP;
        }

        public String getQueryPort() {
            return this.queryPort;
        }

        public void setQueryPort(String queryPort) {
            this.queryPort = queryPort;
        }

        public String getConnAccessId() {
            return this.connAccessId;
        }

        public void setConnAccessId(String connAccessId) {
            this.connAccessId = connAccessId;
        }
    }
}
