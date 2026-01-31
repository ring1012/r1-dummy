package com.unisound.vui.engine;

import com.unisound.vui.bootstrap.ANTELocalConfiguration;
import com.unisound.vui.bootstrap.DefaultUserANTEOptionProvider;
import com.unisound.vui.engine.ANTEngine;
import com.unisound.vui.transport.DefaultMessageCodec;
import com.unisound.vui.transport.MessageCodec;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.internal.ObjectUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class b implements ANTEngineConfig {
    private static final MessageCodec c = new DefaultMessageCodec();

    /* renamed from: a, reason: collision with root package name */
    protected final ANTEngine f420a;
    protected final ANTEngine.Unsafe b;
    private String e;
    private String f;
    private String g;
    private Map<ANTEngineOption<?>, Object> h;
    private ANTELocalConfiguration i;
    private Map<String, List<String>> j;
    private volatile MessageCodec d = c;
    private volatile boolean k = false;

    public b(ANTEngine aNTEngine) {
        ObjectUtil.checkNotNull(aNTEngine, "antEngine");
        this.f420a = aNTEngine;
        this.b = aNTEngine.unsafe();
    }

    private static <T> T a(T t, T t2, String str) {
        if (t2 == null) {
            throw new NullPointerException(str);
        }
        if (t != null) {
            throw new IllegalStateException(str + "set already");
        }
        return t2;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public b setMainTag(String str) {
        this.e = str;
        this.b.setMainTag(this.e);
        return this;
    }

    protected <T> void a(ANTEngineOption<T> aNTEngineOption, T t) {
        if (aNTEngineOption == null) {
            throw new NullPointerException("option");
        }
        aNTEngineOption.validate(t);
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    /* renamed from: b, reason: merged with bridge method [inline-methods] */
    public b setWakeupTag(String str) {
        this.f = (String) a(this.f, str, "wakeupTag");
        return this;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    /* renamed from: c, reason: merged with bridge method [inline-methods] */
    public b setWakeupOneshotTag(String str) {
        this.g = (String) a(this.g, str, "wakeupOneshotTag");
        return this;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public ANTELocalConfiguration getLocalConfiguration() {
        return this.i;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public String getMainTag() {
        return this.e;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public Map<String, List<String>> getMainVocab() {
        return this.j;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public MessageCodec getMessageCodec() {
        return this.d;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public <T> T getOption(ANTEngineOption<T> aNTEngineOption) {
        ObjectUtil.checkNotNull(aNTEngineOption, "option");
        return aNTEngineOption == ANTEngineOption.MAIN_TAG ? (T) getMainTag() : aNTEngineOption == ANTEngineOption.WAKEUP_TAG ? (T) getWakeupTag() : aNTEngineOption == ANTEngineOption.WAKEUP_ONESHOT_TAG ? (T) getWakeupOneshotTag() : aNTEngineOption == ANTEngineOption.MESSAGE_CODEC ? (T) getMessageCodec() : aNTEngineOption == ANTEngineOption.GENERAL_UPDATE_VOLUME ? (T) this.b.getOption(1045) : aNTEngineOption == ANTEngineOption.ASR_OPT_TIMEOUT_STATUS ? (T) this.b.getOption(1078) : aNTEngineOption == ANTEngineOption.ASR_SAMPLING_RATE ? (T) this.b.getOption(1044) : aNTEngineOption == ANTEngineOption.ASR_DOMAIN ? (T) this.b.getOption(1008) : aNTEngineOption == ANTEngineOption.ASR_VOICE_FIELD ? (T) this.b.getOption(1003) : aNTEngineOption == ANTEngineOption.ASR_LANGUAGE ? (T) this.b.getOption(1004) : aNTEngineOption == ANTEngineOption.ASR_VAD_TIMEOUT_FRONTSIL ? (T) this.b.getOption(1010) : aNTEngineOption == ANTEngineOption.ASR_VAD_TIMEOUT_BACKSIL ? (T) this.b.getOption(1011) : aNTEngineOption == ANTEngineOption.ASR_NET_TIMEOUT ? (T) this.b.getOption(1014) : aNTEngineOption == ANTEngineOption.WAKEUP_OPT_THRESHOLD_VALUE ? (T) this.b.getOption(3150) : aNTEngineOption == ANTEngineOption.NLU_ENABLE ? (T) this.b.getOption(1020) : aNTEngineOption == ANTEngineOption.NLU_SCENARIO ? (T) this.b.getOption(1021) : aNTEngineOption == ANTEngineOption.GENERAL_HISTORY ? (T) this.b.getOption(1030) : aNTEngineOption == ANTEngineOption.GENERAL_CITY ? (T) this.b.getOption(1031) : aNTEngineOption == ANTEngineOption.GENERAL_VOICEID ? (T) this.b.getOption(1032) : aNTEngineOption == ANTEngineOption.ASR_OPT_RECOGNIZE_SCENE ? (T) this.b.getOption(1082) : aNTEngineOption == ANTEngineOption.ASR_SUBDOMAIN ? (T) this.b.getOption(1087) : aNTEngineOption == ANTEngineOption.ASR_VAD_TIMEOUT_FRONTSIL ? (T) this.b.getOption(1010) : aNTEngineOption == ANTEngineOption.ASR_VAD_TIMEOUT_BACKSIL ? (T) this.b.getOption(1011) : aNTEngineOption == ANTEngineOption.ASR_FOURMIC_DOA_RESULT ? (T) this.b.getOption(10196) : aNTEngineOption == ANTEngineOption.ASR_FOURMIC_BOARD_VERSION ? (T) this.b.getOption(10197) : aNTEngineOption == ANTEngineOption.ASR_FOURMIC_CLOSE_4MICALGORITHM ? (T) this.b.getOption(10100) : aNTEngineOption == ANTEngineOption.GENERAL_UDID ? (T) this.b.getOption(1036) : (T) this.h.get(aNTEngineOption);
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public Map<ANTEngineOption<?>, Object> getOptions() {
        return this.h;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public String getWakeupOneshotTag() {
        return this.g;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public String getWakeupTag() {
        return this.f;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public boolean isPrintEngineLog() {
        return this.k;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public ANTEngineConfig setLocalConfiguration(ANTELocalConfiguration localConfiguration) {
        this.i = localConfiguration;
        return this;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public ANTEngineConfig setMainVocab(Map<String, List<String>> mainVocab) {
        this.j = (Map) a(this.j, mainVocab, "mainVocab");
        return this;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public ANTEngineConfig setMessageCodec(MessageCodec messageCodec) {
        if (messageCodec == null) {
            throw new NullPointerException("messageCodec");
        }
        this.d = messageCodec;
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.unisound.vui.engine.ANTEngineConfig
    public <T> boolean setOption(ANTEngineOption<T> option, T t) {
        a(option, t);
        LogMgr.d("NativeANTEngineConfig", "==>>setOption option:" + option + ",value:" + t);
        if (option == ANTEngineOption.MAIN_TAG) {
            setMainTag((String) t);
        } else if (option == ANTEngineOption.WAKEUP_TAG) {
            setWakeupTag((String) t);
        } else if (option == ANTEngineOption.WAKEUP_ONESHOT_TAG) {
            setWakeupOneshotTag((String) t);
        } else if (option == ANTEngineOption.MESSAGE_CODEC) {
            setMessageCodec((MessageCodec) t);
        } else if (option == ANTEngineOption.ASR_SAMPLING_RATE) {
            this.b.setASROption(1044, t);
        } else if (option == ANTEngineOption.ASR_DOMAIN) {
            this.b.setASROption(1008, t);
        } else if (option == ANTEngineOption.ASR_VOICE_FIELD) {
            this.b.setASROption(1003, t);
        } else if (option == ANTEngineOption.ASR_LANGUAGE) {
            this.b.setASROption(1004, t);
        } else if (option == ANTEngineOption.ASR_NET_TIMEOUT) {
            this.b.setASROption(1014, t);
        } else if (option == ANTEngineOption.GENERAL_UPDATE_VOLUME) {
            this.b.setASROption(1045, t);
        } else if (option == ANTEngineOption.ASR_OPT_FRONT_CACHE_TIME) {
            this.b.setASROption(1060, t);
        } else if (option == ANTEngineOption.ASR_OPT_VAD_ENABLED) {
            this.b.setASROption(1061, t);
        } else if (option == ANTEngineOption.WAKEUP_OPT_THRESHOLD_VALUE) {
            this.b.setASROption(3150, t);
        } else if (option == ANTEngineOption.NLU_ENABLE) {
            this.b.setASROption(1020, t);
        } else if (option == ANTEngineOption.NLU_SCENARIO) {
            this.b.setASROption(1021, t);
        } else if (option == ANTEngineOption.GENERAL_GPS) {
            this.b.setASROption(1033, t);
        } else if (option == ANTEngineOption.GENERAL_HISTORY) {
            this.b.setASROption(1030, t);
        } else if (option == ANTEngineOption.GENERAL_CITY) {
            this.b.setASROption(1031, t);
        } else if (option == ANTEngineOption.GENERAL_VOICEID) {
            this.b.setASROption(1032, t);
        } else if (option == ANTEngineOption.NLU_VER) {
            this.b.setASROption(1024, t);
        } else if (option == ANTEngineOption.NLU_APPVER) {
            this.b.setASROption(1025, t);
        } else if (option == ANTEngineOption.ASR_OPT_FRONT_RESET_CACHE_BYTE_TIME) {
            this.b.setASROption(1070, t);
        } else if (option == ANTEngineOption.ASR_OPT_DEBUG_SAVELOG) {
            this.b.setASROption(1071, t);
        } else if (option == ANTEngineOption.ASR_OPT_DEBUG_POSTLOG) {
            this.b.setASROption(1072, t);
        } else if (option == ANTEngineOption.ASR_OPT_SAVE_AFTERVAD_RECORDING_DATA) {
            this.b.setASROption(1074, t);
        } else if (option == ANTEngineOption.ASR_OPT_ONESHOT_VADBACKSIL_TIME) {
            this.b.setASROption(1081, t);
        } else if (option == ANTEngineOption.ASR_OPT_RECOGNIZE_SCENE) {
            this.b.setASROption(1082, t);
        } else if (option == ANTEngineOption.ASR_OPT_RECOGNIZE_MODEL_ID) {
            if (this.f420a.isModeInitialized()) {
                LogMgr.d("NativeANTEngineConfig", "ASR_OPT_RECOGNIZE_MODEL_ID:" + t);
                this.b.setASROption(1083, t);
            }
        } else if (option == ANTEngineOption.ASR_OPT_WAKEUP_MODEL_ID) {
            if (this.f420a.isModeInitialized()) {
                LogMgr.d("NativeANTEngineConfig", "ASR_OPT_WAKEUP_MODEL_ID:" + t);
                this.b.setASROption(1084, t);
            }
        } else if (option == ANTEngineOption.ASR_OPT_ALREAD_AWPE) {
            this.b.setASROption(1085, t);
        } else if (option == ANTEngineOption.ASR_SUBDOMAIN) {
            this.b.setASROption(1087, t);
        } else if (option == ANTEngineOption.ASR_OPT_RESULT_FILTER) {
            this.b.setASROption(1051, t);
        } else if (option == ANTEngineOption.ASR_OPT_RECORDING_ENABLED) {
            this.b.setASROption(1053, t);
        } else if (option == ANTEngineOption.ASR_OPT_PRINT_LOG) {
            this.b.setASROption(1054, t);
        } else if (option == ANTEngineOption.ASR_OPT_PRINT_TIME_LOG) {
            this.b.setASROption(1063, t);
        } else if (option == ANTEngineOption.ASR_OPT_SAVE_RECORDING_DATA) {
            this.b.setASROption(1058, t);
        } else if (option == ANTEngineOption.ASR_VAD_TIMEOUT_FRONTSIL) {
            this.b.setASROption(1010, t);
        } else if (option == ANTEngineOption.ASR_VAD_TIMEOUT_BACKSIL) {
            this.b.setASROption(1011, t);
        } else if (option == ANTEngineOption.ASR_FOURMIC) {
            this.b.setASROption(10199, t);
        } else if (option == ANTEngineOption.ASR_FOURMIC_ISDEBUG) {
            this.b.setASROption(10198, t);
        } else if (option == ANTEngineOption.ASR_FOURMIC_CHANGE_CHANNEL) {
            this.b.setASROption(10194, t);
        } else if (option == ANTEngineOption.ASR_FOURMIC_CLOSE_4MICALGORITHM) {
            this.b.setASROption(10100, t);
        } else if (option == ANTEngineOption.OPT_SET_FIX_RESULT_NLU) {
            this.b.setASROption(5, t);
        } else if (option == ANTEngineOption.OPT_SET_FIX_RESULT_NLU_CONFIGPATH) {
            this.b.setASROption(6, t);
        } else if (option == ANTEngineOption.ASR_OPT_PRINT_ENGINE_LOG) {
            this.b.setASROption(1062, t);
        } else if (option == ANTEngineOption.TTS_KEY_VOICE_SPEED) {
            this.b.setTTSOption(2001, t);
        } else if (option == ANTEngineOption.TTS_KEY_VOICE_PITCH) {
            this.b.setTTSOption(2002, t);
        } else if (option == ANTEngineOption.TTS_KEY_VOICE_VOLUME) {
            this.b.setTTSOption(2003, t);
        } else if (option == ANTEngineOption.TTS_KEY_VOICE_NAME) {
            this.b.setTTSOption(2005, t);
        } else if (option == ANTEngineOption.TTS_KEY_PLAY_START_BUFFER_TIME) {
            this.b.setTTSOption(2012, t);
        } else if (option == ANTEngineOption.TTS_KEY_SAMPLE_RATE) {
            this.b.setTTSOption(2004, t);
        } else if (option == ANTEngineOption.TTS_KEY_STREAM_TYPE) {
            this.b.setTTSOption(2013, t);
        } else if (option == ANTEngineOption.TTS_SERVICE_MODE) {
            this.b.setTTSOption(2020, t);
        } else if (option == ANTEngineOption.TTS_KEY_IS_DEBUG) {
            this.b.setTTSOption(2014, t);
        } else if (option == ANTEngineOption.TTS_KEY_IS_READ_ENLISH_IN_PINYIN) {
            this.b.setTTSOption(2021, t);
        } else if (option == ANTEngineOption.TTS_KEY_FRONT_SILENCE) {
            this.b.setTTSOption(2022, t);
        } else if (option == ANTEngineOption.TTS_KEY_BACK_SILENCE) {
            this.b.setTTSOption(2023, t);
        } else if (option == ANTEngineOption.TTS_KEY_IS_URGENT_AUDIO) {
            this.b.setTTSOption(2024, t);
        } else if (option == ANTEngineOption.TTS_KEY_FRONTEND_MODEL_PATH) {
            this.b.setTTSOption(2030, t);
        } else if (option == ANTEngineOption.TTS_KEY_BACKEND_MODEL_PATH) {
            this.b.setTTSOption(2031, t);
        } else if (option == ANTEngineOption.TTS_KEY_SWITCH_BACKEND_MODEL_PATH) {
            this.b.setTTSOption(2032, t);
        } else if (option == ANTEngineOption.TTS_KEY_USER_DICT_FILE_PATH) {
            this.b.setTTSOption(2035, t);
        } else if (option == ANTEngineOption.ASR_OPT_WX_SERVICE) {
            LogMgr.d("NativeANTEngineConfig", "ASR_OPT_WX_SERVICE:" + t);
            this.b.setASROption(1095, t);
        } else if (option == ANTEngineOption.ASR_OPT_SUB_SERVICE_PARAM) {
            LogMgr.d("NativeANTEngineConfig", "ASR_OPT_SUB_SERVICE_PARAM:" + t);
            this.b.setASROption(1097, t);
        } else if (option == ANTEngineOption.ASR_OPT_RECOGNITION_FRONT_VAD) {
            this.b.setASROption(1079, t);
        } else {
            if (option != ANTEngineOption.ASR_OPT_TEMP_RESULT_ENABLE) {
                return false;
            }
            this.b.setASROption(1076, t);
        }
        return true;
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public boolean setOptions(Map<ANTEngineOption<?>, ?> options) {
        if (options == null) {
            throw new NullPointerException("options");
        }
        if (this.h == null) {
            this.h = new DefaultUserANTEOptionProvider().options();
        }
        boolean z = true;
        Iterator<Map.Entry<ANTEngineOption<?>, ?>> it = options.entrySet().iterator();
        while (true) {
            boolean z2 = z;
            if (!it.hasNext()) {
                return z2;
            }
            Map.Entry<ANTEngineOption<?>, ?> next = it.next();
            this.h.put(next.getKey(), next.getValue());
            z = !setOption(next.getKey(), next.getValue()) ? false : z2;
        }
    }

    @Override // com.unisound.vui.engine.ANTEngineConfig
    public ANTEngineConfig setPrintEngineLog(boolean printEngineLog) {
        this.k = printEngineLog;
        this.b.setASROption(1062, Boolean.valueOf(printEngineLog));
        return this;
    }
}
