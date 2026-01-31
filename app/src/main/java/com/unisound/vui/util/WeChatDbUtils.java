package com.unisound.vui.util;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class WeChatDbUtils {
    private static Context mContext;
    private static com.unisound.vui.data.f.a weChatDbCallBack;

    private WeChatDbUtils() {
    }

    private static void callbackMessage(com.unisound.vui.data.entity.a.d weChatMessage) {
        if (weChatDbCallBack != null) {
            weChatDbCallBack.a(weChatMessage);
        }
    }

    public static void clearAll() {
        com.unisound.vui.data.a.a.a(mContext).c();
    }

    public static List<com.unisound.vui.data.entity.a.d> getAllWeChatMessageByUser(String userName) {
        List<com.unisound.vui.data.entity.a.d> listA = com.unisound.vui.data.a.a.a(mContext).a(userName);
        return listA == null ? new ArrayList() : listA;
    }

    public static void init(Context mContext2) {
        mContext = mContext2;
    }

    public static void saveWechatMessage(com.unisound.vui.data.entity.a.d weChatMessage) {
        com.unisound.vui.data.a.a.a(mContext).a(weChatMessage);
        callbackMessage(weChatMessage);
    }

    public static void setWeChatDbCallBack(com.unisound.vui.data.f.a weChatDbCallBack2) {
        weChatDbCallBack = weChatDbCallBack2;
    }

    public static void upWechatMessage(com.unisound.vui.data.entity.a.d weChatMessage) {
        com.unisound.vui.data.a.a.a(mContext).b(weChatMessage);
        callbackMessage(weChatMessage);
    }
}
