package com.unisound.ant.device.bean;

import java.util.List;
import nluparser.scheme.AudioResult;

/* loaded from: classes.dex */
public class AudioListResult {
    private ControlInfo controlInfo;
    private String detailInfo;
    private int status;

    public ControlInfo getControlInfo() {
        return this.controlInfo;
    }

    public void setControlInfo(ControlInfo controlInfo) {
        this.controlInfo = controlInfo;
    }

    public String getDetailInfo() {
        return this.detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class ControlInfo {
        private int pageCount;
        private List<AudioResult.Music> result;

        public int getPageCount() {
            return this.pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public List<AudioResult.Music> getResult() {
            return this.result;
        }

        public void setResult(List<AudioResult.Music> result) {
            this.result = result;
        }
    }
}
