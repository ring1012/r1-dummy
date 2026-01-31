package com.unisound.ant.device.sessionlayer;

import android.text.TextUtils;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.unisound.ant.device.bean.SessionData;
import com.unisound.ant.device.bean.UnisoundDeviceCommand;
import com.unisound.ant.device.profile.DstServiceProfile;
import com.unisound.ant.device.service.CloudResponse;
import com.unisound.ant.device.service.ServiceProtocolUtil;
import com.unisound.vui.util.LogMgr;
import java.lang.reflect.Type;

/* loaded from: classes.dex */
public class NluSessionLayer {
    private static final String TAG = "NluSessionLayer";

    public static UnisoundDeviceCommand parseNluSessionContent(String message) {
        LogMgr.d(TAG, "--->>dispatcherMessage message:" + message);
        if (TextUtils.isEmpty(message)) {
            return null;
        }
        Type classType = new TypeToken<CloudResponse<SessionData<JsonObject>>>() { // from class: com.unisound.ant.device.sessionlayer.NluSessionLayer.1
        }.getType();
        CloudResponse<SessionData<JsonObject>> cloudResponse = ServiceProtocolUtil.getResponseHeader(message, classType);
        String messageType = cloudResponse.getMessageType();
        SessionData sessionData = cloudResponse.getMessageBody();
        if (sessionData == null) {
            LogMgr.e(TAG, "--parse sessionData is null and this session is not effective");
            return null;
        }
        if (!CloudResponse.MESSAGE_TYPE_INTENT_ACTION.equals(messageType)) {
            return null;
        }
        sessionData.getDstService().getUniCommand().getOperation();
        return dispatcherServiceMessage(sessionData.getDialog().getDstService(), sessionData.getDstService());
    }

    public static UnisoundDeviceCommand dispatcherServiceMessage(String dstServiceName, DstServiceProfile dstServiceProfile) {
        LogMgr.d(TAG, "--->>dispatcherServiceMessage dstServiceName:" + dstServiceName);
        SessionExecuteHandler handler = SessionRegister.getSessionExecuter(dstServiceName);
        UnisoundDeviceCommand command = dstServiceProfile.getUniCommand();
        if (handler == null || command == null) {
            LogMgr.d(TAG, "--->>dispatcherServiceMessage handler may be null OR not register");
            return null;
        }
        return command;
    }
}
