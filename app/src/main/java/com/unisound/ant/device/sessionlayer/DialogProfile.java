package com.unisound.ant.device.sessionlayer;

/* loaded from: classes.dex */
public class DialogProfile {
    public static final String DIALOG_FINISH = "2";
    public static final String DIALOG_IDLE = "0";
    public static final String DIALOG_START = "1";
    public static final String SCENE_FLAG_END = "end";
    public static final String SCENE_FLAG_PROCESSING = "processing";
    public static final String SCENE_FLAG_START = "start";
    public String dstService;
    public String dstState;
    public String sceneFlag;
    public TerminalResponse sendToCloudResponse;
    public TerminalResponse sendToTerminalResponse;

    public String getDstState() {
        return this.dstState;
    }

    public void setDstState(String dstState) {
        this.dstState = dstState;
    }

    public String getDstService() {
        return this.dstService;
    }

    public void setDstService(String dstService) {
        this.dstService = dstService;
    }

    public TerminalResponse getSendToCloudResponse() {
        return this.sendToCloudResponse;
    }

    public void setSendToCloudResponse(TerminalResponse sendToCloudResponse) {
        this.sendToCloudResponse = sendToCloudResponse;
    }

    public TerminalResponse getSendToTerminalResponse() {
        return this.sendToTerminalResponse;
    }

    public void setSendToTerminalResponse(TerminalResponse sendToTerminalResponse) {
        this.sendToTerminalResponse = sendToTerminalResponse;
    }

    public static class TerminalResponse {
        private String actionDstServiceId;
        private String actionResponseId;
        private long actionTimestamp = System.currentTimeMillis();

        public TerminalResponse(String actionResponseId) {
            this.actionResponseId = actionResponseId;
        }

        public String getActionResponseId() {
            return this.actionResponseId;
        }

        public void setActionResponseId(String actionResponseId) {
            this.actionResponseId = actionResponseId;
        }

        public long getActionTimestamp() {
            return this.actionTimestamp;
        }

        public void setActionTimestamp(long actionTimestamp) {
            this.actionTimestamp = actionTimestamp;
        }

        public String getActionDstServiceId() {
            return this.actionDstServiceId;
        }

        public void setActionDstServiceId(String actionDstServiceId) {
            this.actionDstServiceId = actionDstServiceId;
        }
    }

    public String getSceneFlag() {
        return this.sceneFlag;
    }

    public void setSceneFlag(String sceneFlag) {
        this.sceneFlag = sceneFlag;
    }
}
