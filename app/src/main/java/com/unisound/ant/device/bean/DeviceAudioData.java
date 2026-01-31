package com.unisound.ant.device.bean;

import java.util.List;
import nluparser.scheme.AudioResult;

/* loaded from: classes.dex */
public class DeviceAudioData {
    private String albumId;
    private List<AudioResult.Music> audioList;
    private int index;
    private int pageCount;
    private int pageNo;
    private int pageSize;

    public DeviceAudioData(int index, String albumId, List<AudioResult.Music> audioList) {
        this.pageNo = 1;
        this.pageSize = 1;
        this.pageCount = 2;
        this.index = index;
        this.albumId = albumId;
        this.audioList = audioList;
    }

    public DeviceAudioData(int index, String albumId, List<AudioResult.Music> audioList, int pageNo, int pageSize, int pageCount) {
        this.pageNo = 1;
        this.pageSize = 1;
        this.pageCount = 2;
        this.index = index;
        this.albumId = albumId;
        this.audioList = audioList;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<AudioResult.Music> getAudioList() {
        return this.audioList;
    }

    public void setAudioList(List<AudioResult.Music> audioList) {
        this.audioList = audioList;
    }
}
