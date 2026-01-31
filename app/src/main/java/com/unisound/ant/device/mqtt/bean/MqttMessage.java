package com.unisound.ant.device.mqtt.bean;

import java.util.List;

/* loaded from: classes.dex */
public class MqttMessage {
    private int costTime;
    private String message;
    private Result result;
    private String returnCode;

    public int getCostTime() {
        return this.costTime;
    }

    public void setCostTime(int costTime) {
        this.costTime = costTime;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReturnCode() {
        return this.returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public Result getResult() {
        return this.result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        private String clientId;
        private MqttConnection connection;
        private List<ChangeMessage> msgList;
        private int totalCount;

        public String getClientId() {
            return this.clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public MqttConnection getConnection() {
            return this.connection;
        }

        public void setConnection(MqttConnection connection) {
            this.connection = connection;
        }

        public int getTotalCount() {
            return this.totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public List<ChangeMessage> getMsgList() {
            return this.msgList;
        }

        public void setMsgList(List<ChangeMessage> msgList) {
            this.msgList = msgList;
        }

        public String toString() {
            return "Result{clientId='" + this.clientId + "', connection=" + this.connection + ", totalCount=" + this.totalCount + ", msgList=" + this.msgList + '}';
        }
    }

    public String toString() {
        return "MqttMessage{costTime=" + this.costTime + ", message='" + this.message + "', returnCode='" + this.returnCode + "', result=" + this.result + '}';
    }
}
