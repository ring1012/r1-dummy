package com.unisound.ant.device.bean;

/* loaded from: classes.dex */
public class RequestInfo {
    private String businessType;
    private String command;
    private PageInfo data;
    private ClientInfo tcl;
    private String version;

    public RequestInfo(String businessType, String command, PageInfo data, ClientInfo tcl, String version) {
        this.businessType = businessType;
        this.command = command;
        this.data = data;
        this.tcl = tcl;
        this.version = version;
    }

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

    public PageInfo getData() {
        return this.data;
    }

    public void setData(PageInfo data) {
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

    public static class PageInfo {
        private String albumId;
        private String pageNo;
        private String pageSize;
        private String timeAsc;
        private String udid;

        public PageInfo(String pageNo, String pageSize, String udid) {
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.udid = udid;
        }

        public PageInfo(String pageNo, String pageSize, String udid, String albumId, String timeAsc) {
            this.pageNo = pageNo;
            this.pageSize = pageSize;
            this.udid = udid;
            this.albumId = albumId;
            this.timeAsc = timeAsc;
        }

        public String getPageNo() {
            return this.pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }

        public String getPageSize() {
            return this.pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getUdid() {
            return this.udid;
        }

        public void setUdid(String udid) {
            this.udid = udid;
        }

        public String getAlbumId() {
            return this.albumId;
        }

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public String getTimeAsc() {
            return this.timeAsc;
        }

        public void setTimeAsc(String timeAsc) {
            this.timeAsc = timeAsc;
        }
    }

    public static class ClientInfo {
        private String clientId;
        private int subSysId;
        private String token;

        public ClientInfo(String clientId, int subSysId, String token) {
            this.clientId = clientId;
            this.subSysId = subSysId;
            this.token = token;
        }

        public ClientInfo(int subSysId) {
            this.subSysId = subSysId;
        }

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
