package com.unisound.vui.message;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class HandlerSelectData<T> {
    private List<T> data;
    private String extra;
    private HandlerSelectType handlerSelectType;
    private int index;

    public enum HandlerSelectType {
        CONFIRM,
        CANCEL,
        PAGE,
        ITEM,
        LIST,
        DEFAULT
    }

    public static HandlerSelectData getCancelData() {
        HandlerSelectData handlerSelectData = new HandlerSelectData();
        handlerSelectData.setHandlerSelectType(HandlerSelectType.CANCEL);
        return handlerSelectData;
    }

    public static HandlerSelectData getConfirmData() {
        HandlerSelectData handlerSelectData = new HandlerSelectData();
        handlerSelectData.setHandlerSelectType(HandlerSelectType.CONFIRM);
        return handlerSelectData;
    }

    public static HandlerSelectData getSelectItemData(int index) {
        HandlerSelectData handlerSelectData = new HandlerSelectData();
        handlerSelectData.setHandlerSelectType(HandlerSelectType.ITEM);
        handlerSelectData.setIndex(index);
        return handlerSelectData;
    }

    public static <E> HandlerSelectData getSelectListData(E data) {
        HandlerSelectData handlerSelectData = new HandlerSelectData();
        handlerSelectData.setHandlerSelectType(HandlerSelectType.LIST);
        ArrayList arrayList = new ArrayList();
        arrayList.add(data);
        handlerSelectData.setData(arrayList);
        return handlerSelectData;
    }

    public static <E> HandlerSelectData getSelectListData(List<E> data) {
        HandlerSelectData handlerSelectData = new HandlerSelectData();
        handlerSelectData.setHandlerSelectType(HandlerSelectType.LIST);
        handlerSelectData.setData(data);
        return handlerSelectData;
    }

    public static HandlerSelectData getSelectPageData(int index) {
        HandlerSelectData handlerSelectData = new HandlerSelectData();
        handlerSelectData.setHandlerSelectType(HandlerSelectType.PAGE);
        handlerSelectData.setIndex(index);
        return handlerSelectData;
    }

    public List<T> getData() {
        return this.data;
    }

    public String getExtra() {
        return this.extra;
    }

    public HandlerSelectType getHandlerSelectType() {
        return this.handlerSelectType;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isCancelData() {
        return this.handlerSelectType == HandlerSelectType.CANCEL;
    }

    public boolean isConfirmData() {
        return this.handlerSelectType == HandlerSelectType.CONFIRM;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setHandlerSelectType(HandlerSelectType handlerSelectType) {
        this.handlerSelectType = handlerSelectType;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
