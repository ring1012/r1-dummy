package com.unisound.vui.handler.session.music.network.body;

/* loaded from: classes.dex */
public class AudioBody {
    private String albumId;
    private String code;
    private String keyword;
    private String pageNo;
    private String pageSize = "30";
    private String udid;

    public AudioBody(String udid, String albumId, String pageNo) {
        this.udid = udid;
        this.albumId = albumId;
        this.pageNo = pageNo;
    }

    public AudioBody(String udid, String code, String keyword, String pageNo) {
        this.udid = udid;
        this.code = code;
        this.keyword = keyword;
        this.pageNo = pageNo;
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

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
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
}
