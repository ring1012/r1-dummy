package com.unisound.ant.device.service;

import com.unisound.vui.util.AppGlobalConstant;
import com.unisound.vui.util.SystemUitls;

/* loaded from: classes.dex */
public final class CommonHeaderUtils {
    private CommonHeaderUtils() {
    }

    public static MessageHeader buildUpdateReqHeader(String messageType) {
        String version = SystemUitls.getAppVersion(AppGlobalConstant.getContext());
        return new MessageHeader(messageType, version);
    }

    public static MessageHeader buildUpdateReqHeader() {
        String version = SystemUitls.getAppVersion(AppGlobalConstant.getContext());
        return new MessageHeader(BaseRequest.MESSAGE_TYPE_GD_REQUEST, version);
    }

    public static MessageHeader buildUploadReqHeader() {
        String version = SystemUitls.getAppVersion(AppGlobalConstant.getContext());
        return new MessageHeader(BaseRequest.MESSAGE_TYPE_PD_REQUEST, version);
    }
}
