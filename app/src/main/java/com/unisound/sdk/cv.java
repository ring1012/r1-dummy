package com.unisound.sdk;

import com.unisound.client.SpeechConstants;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class cv {
    public Map<String, Integer> a() {
        HashMap map = new HashMap();
        map.put(SpeechConstants.VPR_OPT_SERVER_ADDR_JSONKEY, Integer.valueOf(SpeechConstants.VPR_SERVER_ADDR));
        map.put(SpeechConstants.VPR_OPT_INPUT_8K_JSONKEY, Integer.valueOf(SpeechConstants.VPR_INPUT_8K));
        map.put(SpeechConstants.VPR_OPT_SCENE_ID_JSONKEY, Integer.valueOf(SpeechConstants.VPR_SCENE_ID));
        map.put(SpeechConstants.VPR_OPT_LOG_LISTNER_JSONKEY, Integer.valueOf(SpeechConstants.VPR_LOG_LISTNER));
        map.put(SpeechConstants.VPR_OPT_SAVE_RECORDING_DATA_JSONKEY, Integer.valueOf(SpeechConstants.VPR_SAVE_RECORDING_DATA));
        map.put(SpeechConstants.VPR_OPT_FRONT_VAD_ENABLED_JSONKEY, Integer.valueOf(SpeechConstants.VPR_FRONT_VAD_ENABLED));
        map.put(SpeechConstants.VPR_OPT_SAMPLE_RATE_JSONKEY, Integer.valueOf(SpeechConstants.VPR_SAMPLE_RATE));
        map.put(SpeechConstants.VPR_OPT_BLUETOOTH_ENABLED_JSONKEY, Integer.valueOf(SpeechConstants.VPR_BLUETOOTH_ENABLED));
        map.put(SpeechConstants.VPR_OPT_VAD_TIMEOUT_JSONKEY, Integer.valueOf(SpeechConstants.VPR_VAD_TIMEOUT));
        map.put(SpeechConstants.VPR_OPT_STOP_TIMEOUT_JSONKEY, Integer.valueOf(SpeechConstants.VPR_STOP_TIMEOUT));
        map.put(SpeechConstants.VPR_OPT_REQUEST_AUDIO_SERVER_JSONKEY, Integer.valueOf(SpeechConstants.VPR_REQUEST_AUDIO_SERVER));
        map.put(SpeechConstants.VPR_OPT_FARFILED_ENABLED_JSONKEY, Integer.valueOf(SpeechConstants.VPR_FARFILED_ENABLED));
        map.put(SpeechConstants.VPR_OPT_TYPE_JSONKEY, Integer.valueOf(SpeechConstants.VPR_TYPE));
        map.put(SpeechConstants.VPR_OPT_RECORDING_ENABLED_JSONKEY, Integer.valueOf(SpeechConstants.VPR_RECORDING_ENABLED));
        map.put(SpeechConstants.VPR_OPT_GET_WAVE_DATA_ENABLED_JSONKEY, Integer.valueOf(SpeechConstants.VPR_GET_WAVE_DATA_ENABLED));
        map.put(SpeechConstants.VPR_OPT_USERNAME_JSONKEY, Integer.valueOf(SpeechConstants.VPR_USERNAME));
        return map;
    }
}
