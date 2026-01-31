package com.unisound.ant.device.bean;

import java.util.List;

/* loaded from: classes.dex */
public class PhicommMusicInfo {
    private String asrResult;
    private int index;
    private List<MusicItem> itemList;
    private int pageIndex;
    private int totalPage;

    public PhicommMusicInfo(int index, List<MusicItem> itemList) {
        this.index = index;
        this.itemList = itemList;
    }

    public PhicommMusicInfo(int index, List<MusicItem> itemList, int pageIndex, int totalPage) {
        this.index = index;
        this.itemList = itemList;
        this.pageIndex = pageIndex;
        this.totalPage = totalPage;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<MusicItem> getItemList() {
        return this.itemList;
    }

    public void setItemList(List<MusicItem> itemList) {
        this.itemList = itemList;
    }

    public String getAsrResult() {
        return this.asrResult;
    }

    public void setAsrResult(String asrResult) {
        this.asrResult = asrResult;
    }

    public int getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
