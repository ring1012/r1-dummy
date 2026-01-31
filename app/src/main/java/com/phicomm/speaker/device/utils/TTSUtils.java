package com.phicomm.speaker.device.utils;

import android.content.Context;
import com.unisound.vui.bootstrap.DefaultLocalConfigurationProvider;
import com.unisound.vui.engine.ANTEngine;
import com.unisound.vui.engine.ANTEngineOption;
import com.unisound.vui.util.ExoConstants;
import com.unisound.vui.util.UserPerferenceUtil;

/* loaded from: classes.dex */
public class TTSUtils {
    public static void switchSpeaker(Context context, ANTEngine engine, String speaker) {
        DefaultLocalConfigurationProvider provider;
        String ttsModelFile;
        provider = new DefaultLocalConfigurationProvider(context);
        switch (speaker) {
            case "FEMALE":
                ttsModelFile = provider.getTTSBackendStandarPath();
                break;
            case "MALE":
                ttsModelFile = provider.getTTSBackendMalePath();
                break;
            case "CHILDREN":
                ttsModelFile = provider.getTTSBackendChildPath();
                break;
            case "SWEET":
                ttsModelFile = provider.getTTSBackendSweetPath();
                break;
            case "LZL":
                ttsModelFile = provider.getTTSBackendLZLPath();
                break;
            default:
                ttsModelFile = provider.getTTSBackendSweetPath();
                break;
        }
        UserPerferenceUtil.setUserTTSModelType(context, speaker);
        engine.config().setOption(ANTEngineOption.TTS_KEY_SWITCH_BACKEND_MODEL_PATH, ttsModelFile);
        engine.pipeline().fireUserEventTriggered(ExoConstants.SWITCH_TTS_PLAYER);
    }
}
