package com.unisound.vui.engine;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Keep;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.unisound.client.IAudioSource;
import com.unisound.client.SpeechConstants;
import com.unisound.vui.bootstrap.ANTELocalConfiguration;
import com.unisound.vui.bootstrap.DefaultLocalConfigurationProvider;
import com.unisound.vui.common.config.ANTConfigPreference;
import com.unisound.vui.common.file.FileHelper;
import com.unisound.vui.common.network.NetUtil;
import com.unisound.vui.d;
import com.unisound.vui.data.tts.TTSContent;
import com.unisound.vui.e;
import com.unisound.vui.engine.ANTEngine;
import com.unisound.vui.engine.AbstractANTEngine;
import com.unisound.vui.engine.a;
import com.unisound.vui.transport.out.VocabContent;
import com.unisound.vui.util.ExoConstants;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.StringUtils;
import com.unisound.vui.util.SystemUitls;
import com.unisound.vui.util.UserPerferenceUtil;
import com.unisound.vui.util.entity.ExoAsrTag;
import com.unisound.vui.util.entity.VocabSlotTag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

/* loaded from: classes.dex */
public class NativeANTEngine extends AbstractANTEngine {

    /* renamed from: a, reason: collision with root package name */
    private static final String f412a = "oneshot:" + ExoAsrTag.getMainTag(ExoAsrTag.TAG_UNIDRIVE_MAIN);
    private static String b;
    private static String c;
    private List<String> d;
    private String e;
    private final ANTEngineConfig h;
    private final com.unisound.vui.a j;
    private final e k;
    private final com.unisound.vui.c l;
    private String m;
    private ANTELocalConfiguration n;
    private b p;
    private TTSContent q;
    private List<String> f = new ArrayList();
    private List<String> g = new ArrayList();
    private Handler i = new Handler(Looper.getMainLooper()) { // from class: com.unisound.vui.engine.NativeANTEngine.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    ArrayList arrayList = new ArrayList();
                    UserPerferenceUtil.setWakeupWord(NativeANTEngine.this.context, NativeANTEngine.this.d);
                    arrayList.addAll(UserPerferenceUtil.getMainWakeupWord(NativeANTEngine.this.context));
                    NativeANTEngine.this.setWakeupWord0(arrayList, false);
                    NativeANTEngine.this.insertVocab(NativeANTEngine.this.f(), ExoAsrTag.getMainTag(ExoAsrTag.TAG_UNIDRIVE_MAIN));
                    break;
                case 1002:
                    NativeANTEngine.this.pipeline().fireUserEventTriggered(Integer.valueOf(EventType.WAKEUP_EVENT_UPDATEWAKEUPWORD_FAIL));
                    break;
                case 1003:
                    NativeANTEngine.this.setWakeupWord0(NativeANTEngine.this.d, false);
                    break;
            }
        }
    };
    private Gson o = new Gson();
    private Handler r = new Handler() { // from class: com.unisound.vui.engine.NativeANTEngine.2
        @Override // android.os.Handler
        public void handleMessage(Message msg) throws JSONException {
            LogMgr.d("NativeANTEngine", "try to initSpeechUnderstanderWithDeviceId again");
            NativeANTEngine.this.c();
        }
    };

    private final class a extends AbstractANTEngine.AbstractUnsafe {
        private a() {
            super();
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public Object getOption(int key) {
            return NativeANTEngine.this.j.a(key);
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public String getVersion() {
            return NativeANTEngine.this.j.c();
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public int loadCompiledJsgf(String grammarTag, String jsgfPath) {
            return NativeANTEngine.this.j.a(grammarTag, jsgfPath);
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public int loadGrammar(String grammarName, String grammarPath) {
            return NativeANTEngine.this.j.b(grammarName, grammarPath);
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void release(int type, String dataName) {
            NativeANTEngine.this.j.a(type, dataName);
            NativeANTEngine.this.l.a(type, dataName);
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void setASROption(int key, Object value) {
            NativeANTEngine.this.j.a(key, value);
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void setMainTag(String tag) {
            if (NativeANTEngine.this.n == null) {
                NativeANTEngine.this.n = NativeANTEngine.this.config().getLocalConfiguration();
            }
            if (NativeANTEngine.this.n.contentTag(tag)) {
                NativeANTEngine.this.m = tag;
                if (((Boolean) NativeANTEngine.this.config().getOption(ANTEngineOption.OPT_SET_FIX_RESULT_NLU)).booleanValue()) {
                    NativeANTEngine.this.j.a(6, (Object) NativeANTEngine.this.n.getLocalRecognition(NativeANTEngine.this.m).getLocalNluPath());
                }
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void setNLUOption(int key, Object value) throws NumberFormatException {
            NativeANTEngine.this.k.a(key, value);
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void setOption(String key, Object value) {
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void setTTSOption(int key, Object value) {
            NativeANTEngine.this.l.a(key, value);
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public int unloadGrammar(String grammarTag) {
            return NativeANTEngine.this.j.c(grammarTag);
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void uploadUserData(Map<Integer, List<String>> userData) {
            NativeANTEngine.this.j.a(userData);
        }
    }

    final class b implements com.unisound.vui.b {
        private final ANTPipeline b;

        b(ANTPipeline aNTPipeline) {
            this.b = aNTPipeline;
        }

        @Override // com.unisound.vui.b, com.unisound.client.SpeechUnderstanderListener
        public void onError(int type, String errorMSG) {
            LogMgr.d("NativeANTEngine", "type:" + type + ";onError:" + errorMSG);
            if (ANTConfigPreference.isAsrRecognitionTest) {
                return;
            }
            this.b.fireASRError(type, errorMSG);
        }

        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:29:0x0194 -> B:53:0x015c). Please report as a decompilation issue!!! */
        @Override // com.unisound.vui.b, com.unisound.client.SpeechUnderstanderListener
        public void onEvent(int type, int timeMs) {
            if (type == 1129) {
                LogMgr.d("NativeANTEngine", "--->>engine init done vesion " + NativeANTEngine.this.j.c());
                NativeANTEngine.this.j.a(5001, Float.valueOf(5000000.0f));
                NativeANTEngine.this.j.a(5002, Float.valueOf(5000000.0f));
                NativeANTEngine.this.j.a(5003, Float.valueOf(-0.8f));
                NativeANTEngine.this.j.a(5004, (Object) 3);
                NativeANTEngine.this.j.a(5005, (Object) 100);
                NativeANTEngine.this.j.a(5008, (Object) 30);
                NativeANTEngine.this.j.a(5012, Float.valueOf(0.8f));
                NativeANTEngine.this.j.a(5013, (Object) 1);
                NativeANTEngine.this.j.a(5017, (Object) 3);
                NativeANTEngine.this.j.a(1019, (Object) "athenaAppService");
            }
            if (type == 3105) {
                LogMgr.d("NativeANTEngine", "wakeUpEventSetWakeUpWordDone");
                NativeANTEngine.this.f.clear();
                NativeANTEngine.this.f.addAll(NativeANTEngine.this.g);
                NativeANTEngine.this.setNeedRecoveryWakeUp(false);
                NativeANTEngine.this.setInSetWakeUpWord(false);
                boolean zStartWakeupAfterSetWakeupWord = UserPerferenceUtil.startWakeupAfterSetWakeupWord(NativeANTEngine.this.context);
                LogMgr.d("NativeANTEngine", "set wakeup word done, need start wakeup = " + zStartWakeupAfterSetWakeupWord + ", current thread is " + Thread.currentThread().getName());
                if (NativeANTEngine.this.isASR() || NativeANTEngine.this.isRecognition() || !zStartWakeupAfterSetWakeupWord) {
                    return;
                }
                if (NativeANTEngine.this.getAecEnable() || !NativeANTEngine.this.isTTSPlaying()) {
                    try {
                        if (NativeANTEngine.this.isTTSPlaying()) {
                            NativeANTEngine.this.doEnterWakeup(false);
                        } else {
                            NativeANTEngine.this.doEnterWakeup(NativeANTEngine.this.isPlayBeep());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (type == 1160) {
                LogMgr.d("NativeANTEngine", "upload online wake up word success");
                Message messageObtainMessage = NativeANTEngine.this.i.obtainMessage();
                messageObtainMessage.what = 1001;
                NativeANTEngine.this.i.sendMessage(messageObtainMessage);
            } else if (type != 1117) {
                if (type == 3103) {
                    LogMgr.d("NativeANTEngine", "WAKEUP_EVENT_RECOGNITION_SUCCESS ");
                    return;
                }
                if (type == 1129) {
                    LogMgr.d("AutoStart", "engine init done");
                } else if (type == 1101) {
                    LogMgr.d("NativeANTEngine", "onAsrEngine recording start:" + SystemUitls.getCurrentTime());
                } else if (type == 1102) {
                    LogMgr.d("NativeANTEngine", "onAsrEngine recording stop:" + SystemUitls.getCurrentTime());
                    NativeANTEngine.this.j.a(1058, (Object) "");
                } else if (type == 1107) {
                    LogMgr.d("NativeANTEngine", "onAsrEngine recording end:" + SystemUitls.getCurrentTime());
                    NativeANTEngine.this.setEngineState(0);
                } else if (type == 1103) {
                    LogMgr.d("NativeANTEngine", "onAsrEngine vad timeout:" + SystemUitls.getCurrentTime());
                    NativeANTEngine.this.doStopASR();
                }
            }
            if (ANTConfigPreference.isAsrRecognitionTest) {
                LogMgr.d("NativeANTEngine", "asr is recognise test:" + ANTConfigPreference.isAsrRecognitionTest);
                if (type == 1102) {
                    try {
                        NativeANTEngine.this.doEnterASR();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            this.b.fireASREvent(type);
        }

        @Override // com.unisound.vui.b, com.unisound.client.SpeechUnderstanderListener
        public void onResult(int type, String jsonResult) {
            LogMgr.d("NativeANTEngine", "type:" + type + ";onResult:" + jsonResult);
            if (!ANTConfigPreference.isAsrRecognitionTest) {
                this.b.fireASRResult(type, jsonResult);
            } else if (type == 3201) {
                try {
                    NativeANTEngine.this.doEnterASR();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    final class c implements d {
        private final ANTPipeline b;

        private c(ANTPipeline aNTPipeline) {
            this.b = aNTPipeline;
        }

        @Override // com.unisound.vui.d, com.unisound.client.SpeechSynthesizerListener
        public void onError(int type, String errorMSG) {
            this.b.fireTTSError(type, errorMSG);
        }

        @Override // com.unisound.vui.d, com.unisound.client.SpeechSynthesizerListener
        public void onEvent(int type) throws JSONException {
            LogMgr.d("NativeANTEngine", "onEvent : " + type);
            if (type == 2107) {
                LogMgr.d("NativeANTEngine", "tts play end ,isInSetWakeUpWord:" + NativeANTEngine.this.isInSetWakeUpWord());
                NativeANTEngine.this.setTtsPlaying(false);
            }
            if (type == 2101) {
                LogMgr.d("AutoStart", "tts int done");
                NativeANTEngine.this.b();
                NativeANTEngine.this.l.a(1, Integer.valueOf(cn.yunzhisheng.asr.a.U));
            }
            this.b.fireTTSEvent(type);
        }
    }

    @Keep
    public NativeANTEngine(a.InterfaceC0011a provider, IAudioSource audioSource) {
        LogMgr.d("NativeANTEngine", "NativeANTEngine");
        a();
        this.h = new com.unisound.vui.engine.b(this);
        com.unisound.vui.engine.a aNTBuilder = provider.getANTBuilder();
        this.context = provider.context();
        UserPerferenceUtil.setStartWakeupAfterSetWakeupWord(this.context, true);
        this.j = aNTBuilder.createASRManager();
        this.k = aNTBuilder.createNluManager();
        ANTPipeline aNTPipelinePipeline = pipeline();
        this.p = new b(aNTPipelinePipeline);
        this.j.a(this.p);
        this.l = aNTBuilder.createTTSManager();
        this.l.a(new c(aNTPipelinePipeline));
        this.q = new TTSContent(this.context);
        this.q.init();
    }

    private void a() {
        LogMgr.d("NativeANTEngine", "initReqAddress is versionType:" + ANTConfigPreference.sVersionType);
        c = ANTConfigPreference.getTRUrl();
        if (ANTConfigPreference.isDev()) {
            b = "http://106.38.55.5:19999/tr/dataProcess";
        }
        LogMgr.d("NativeANTEngine", "current asr_server " + c);
    }

    private void a(int i) {
        if (i != 0 || ANTConfigPreference.saveWakeUpRecording) {
            if (i != 1 || ANTConfigPreference.saveRecognizerRecording) {
                String aSRSavedRecordingPath = FileHelper.getASRSavedRecordingPath(i);
                LogMgr.d("NativeANTEngine", "-setSaveRecordingOption-" + i);
                if (aSRSavedRecordingPath != null) {
                    this.j.a(1058, (Object) aSRSavedRecordingPath);
                }
            }
        }
    }

    private boolean a(String str) {
        return (UserPerferenceUtil.getUserTTSModelType(this.context).equals("SWEET") || this.l.a(2005).equals("sweet")) && this.q.isLocalAnswer(str);
    }

    private String b(String str) {
        return "<WAV>/system/unisound/audio/tts_answer/" + str + "</WAV>";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() throws JSONException {
        LogMgr.d("AutoStart", "understander init");
        this.j.a(SpeechConstants.ASR_INIT_MODE, (Object) 0);
        this.j.a(1001, (Object) 0);
        this.j.a(SpeechConstants.ASR_FOURMIC_IS_RK_PLATFORM, (Object) true);
        this.j.a(SpeechConstants.ASR_FOURMIC_IS_RK_SINGALCHANEL, (Object) false);
        c();
        if (ANTConfigPreference.sdkDebug) {
            this.j.a(20120629, (Object) true);
        }
        d();
        this.k.a("");
        initOneshotState();
        initAecEnableState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() throws JSONException {
        this.e = UserPerferenceUtil.getDeviceId(this.context);
        if (TextUtils.isEmpty(this.e)) {
            LogMgr.d("NativeANTEngine", "jsonStr is null, post delay 1000 ms");
            this.r.sendEmptyMessageDelayed(0, 1000L);
        } else {
            String str = "{\"activate\":\"true\",\"deviceSn\":\"" + this.e + "\"}";
            this.j.a(str);
            LogMgr.d("NativeANTEngine", "speechUnderstander init with jsonStr : " + str);
        }
    }

    private void d() {
        if (this.j == null) {
            return;
        }
        this.j.a(1095, (Object) true);
        if (ANTConfigPreference.isDev()) {
            this.j.a(SpeechConstants.ASR_OPT_FILTER_URL, (Object) b);
        }
        LogMgr.d("NativeANTEngine", "set token:" + UserPerferenceUtil.getValidSdk(this.context));
        if (TextUtils.isEmpty(UserPerferenceUtil.getValidSdk(this.context))) {
            return;
        }
        this.j.a(1097, (Object) ExoConstants.getServiceParam());
    }

    private void e() {
        LogMgr.d("AutoStart", "tts init");
        this.l.a(2022, (Object) 150);
        this.l.a(2003, (Object) 100);
        if (ANTConfigPreference.sdkDebug) {
            this.l.a(20120629, (Object) true);
        }
        if (ANTConfigPreference.saveTTSRecording) {
            this.l.a(2014, (Object) true);
            this.l.a(2015, (Object) FileHelper.getASRSavedRecordingPath(2));
        }
        this.l.a("");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<String, List<String>> f() {
        HashMap map = new HashMap();
        List<String> mainWakeupWord = UserPerferenceUtil.getMainWakeupWord(this.context);
        if (isOneshot()) {
            ArrayList arrayList = new ArrayList();
            if (UserPerferenceUtil.getOneshotEnableV1(this.context)) {
                for (String str : mainWakeupWord) {
                    arrayList.add(str);
                    arrayList.add(str + " <unk>");
                }
            }
            map.put(VocabSlotTag.asr_wakeup_words_slot.toString(), arrayList);
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add("cccc");
            map.put(VocabSlotTag.Domain_wakeup_words_slot.toString(), arrayList2);
        } else {
            LogMgr.d("NativeANTEngine", "no oneshot");
            ArrayList arrayList3 = new ArrayList();
            arrayList3.addAll(mainWakeupWord);
            if (arrayList3.size() > 0) {
                map.put(VocabSlotTag.Domain_wakeup_words_slot.toString(), arrayList3);
            }
        }
        LogMgr.d("NativeANTEngine", "insertWakeupWordVocab wakeupWordsMap size : %d", Integer.valueOf(map.size()));
        return map;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public ANTEngineConfig config() {
        return this.h;
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doCancelASR() {
        LogMgr.d("NativeANTEngine", "doCancelASR state:" + this.engineState);
        if (isASR() || isRecognition()) {
            this.j.b();
            setEngineState(0);
            this.j.a(1058, (Object) "");
        }
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doCancelEngine() {
        if (!isIdle() || isInSetWakeUpWord()) {
            LogMgr.d("NativeANTEngine", "doCancelEngine start");
            this.j.b();
            LogMgr.d("NativeANTEngine", "doCancelEngine end");
            this.j.a(1058, (Object) "");
            setEngineState(0);
        }
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doCancelTTS() {
        LogMgr.d("NativeANTEngine", "doCancelTTS");
        setTtsPlaying(false);
        this.l.b();
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doCloseReleaseStatus() {
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doEnterASR() {
        if (ANTConfigPreference.sdkDebug && ANTConfigPreference.isAsrRecognitionTest) {
            this.j.a(10198, (Object) true);
        }
        doCancelEngine();
        LogMgr.d("NativeANTEngine", "doEnterASR isWakeUp:" + isWakeup());
        a(1);
        this.j.a(1070, Integer.valueOf(ANTConfigPreference.beepSize));
        LogMgr.d("NativeANTEngine", "doEnterASR tag - %s", this.m);
        this.j.a(1011, Integer.valueOf(ANTConfigPreference.asrVadTimeoutBackSil));
        this.j.a(5008, (Object) 50);
        if (ANTConfigPreference.isDev()) {
            this.j.a(SpeechConstants.ASR_OPT_FILTER_URL, (Object) b);
        }
        this.j.a(1009, (Object) c);
        this.j.a(10100, (Object) false);
        this.j.a(SpeechConstants.ASR_OPT_RECORDING_PAC_SIZE, Integer.valueOf(SpeechConstants.ASR_BEST_RESULT_RETURN));
        if (ANTConfigPreference.onlineVad) {
            this.j.a(SpeechConstants.ASR_OPT_RECOGNIZE_VAD_ENABLE, (Object) true);
        }
        List<String> mainWakeupWord = UserPerferenceUtil.getMainWakeupWord(this.context);
        mainWakeupWord.removeAll(UserPerferenceUtil.getDefaultWakeupWord(this.context));
        String str = (mainWakeupWord.size() == 0 || TextUtils.isEmpty(mainWakeupWord.get(0))) ? UserPerferenceUtil.getDefaultWakeupWord(this.context).get(0) : mainWakeupWord.get(0);
        this.j.a(1008, (Object) "song");
        this.j.a(1017, (Object) ("returnType=json;city=" + ((String) this.h.getOption(ANTEngineOption.GENERAL_CITY)) + ";gps=" + ((String) this.h.getOption(ANTEngineOption.GENERAL_GPS)) + ";time=" + SystemUitls.getTime() + ";screen=;dpi=;history=;udid=" + ((String) this.h.getOption(ANTEngineOption.GENERAL_UDID)) + ";ver=3.0;appver=;oneshotKeyProperty=wakeup;filterName=nlu2;additionalService=athenaAppService;scenario=musicDefault" + (ANTConfigPreference.isDev() ? ";filterUrl=" + b + ";" : "") + ";wakeupword=" + str + ";appendLength=2;audioUrl=true;mac=" + this.e + ";"));
        if (isWakeupRecord()) {
            setWakeupRecord(false);
        }
        this.j.b(this.m);
        setEngineState(2);
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected synchronized void doEnterWakeup(boolean playBeep) {
        if (isInSetWakeUpWord()) {
            LogMgr.e("NativeANTEngine", "doEnterWakeup: now is setting wakeup words,cannot start wakeup");
        } else {
            if (ANTConfigPreference.sdkDebug && (ANTConfigPreference.isWakeupTest || ANTConfigPreference.isWakeupAecTest)) {
                this.j.a(10198, (Object) true);
            }
            if (!isWakeup()) {
                doCancelEngine();
            }
            LogMgr.d("NativeANTEngine", "doEnterWakeup isIdle:" + isIdle() + ", current thread = " + Thread.currentThread().getName());
            this.lastPlayBeep = playBeep;
            if (isIdle()) {
                this.j.a(10100, (Object) false);
                a(0);
                if (isOneshot()) {
                    this.j.a(1070, (Object) 0);
                    this.j.a(1011, Integer.valueOf(ANTConfigPreference.oneshotWakeupVadTimeoutBackSil));
                    setWakeupRecord(true);
                    this.j.a(SpeechConstants.ASR_OPT_RECORDING_PAC_SIZE, (Object) 3840);
                    this.j.b(f412a);
                } else {
                    this.j.a(10199, (Object) true);
                    this.j.a(1070, (Object) 0);
                    this.j.a(1011, Integer.valueOf(ANTConfigPreference.wakeupVadTimeoutBackSil));
                    this.j.a(5008, (Object) 30);
                    setWakeupRecord(true);
                    LogMgr.d("NativeANTEngine", "ASR_OPT_RECORDING_PAC_SIZE : 640");
                    this.j.a(SpeechConstants.ASR_OPT_RECORDING_PAC_SIZE, (Object) 640);
                    this.j.b("wakeup");
                }
                setEngineState(1);
            }
        }
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doInitEngine() throws JSONException {
        c();
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doInitializeMode() {
        DefaultLocalConfigurationProvider defaultLocalConfigurationProvider = new DefaultLocalConfigurationProvider(this.context);
        config().setOption(ANTEngineOption.TTS_KEY_FRONTEND_MODEL_PATH, defaultLocalConfigurationProvider.getTTSFrontendPath());
        String userTTSModelType = UserPerferenceUtil.getUserTTSModelType(this.context);
        LogMgr.d("NativeANTEngine", "get history speaker:" + userTTSModelType);
        if ("FEMALE".equals(userTTSModelType)) {
            config().setOption(ANTEngineOption.TTS_KEY_BACKEND_MODEL_PATH, defaultLocalConfigurationProvider.getTTSBackendStandarPath());
        } else if ("MALE".equals(userTTSModelType)) {
            config().setOption(ANTEngineOption.TTS_KEY_BACKEND_MODEL_PATH, defaultLocalConfigurationProvider.getTTSBackendMalePath());
        } else if ("CHILDREN".equals(userTTSModelType)) {
            config().setOption(ANTEngineOption.TTS_KEY_BACKEND_MODEL_PATH, defaultLocalConfigurationProvider.getTTSBackendChildPath());
        } else if ("SWEET".equals(userTTSModelType)) {
            config().setOption(ANTEngineOption.TTS_KEY_BACKEND_MODEL_PATH, defaultLocalConfigurationProvider.getTTSBackendSweetPath());
        } else {
            config().setOption(ANTEngineOption.TTS_KEY_BACKEND_MODEL_PATH, defaultLocalConfigurationProvider.getTTSBackendSweetPath());
        }
        e();
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doInitializeSdk() {
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doPPTAction0() {
        LogMgr.d("NativeANTEngine", "doPPTAction0");
        doCancelASR();
        doEnterASR();
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doPlayBuffer(byte[] bytes) {
        setTtsPlaying(true);
        this.l.a(bytes);
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doPlayTTS(String text) {
        if (isTTSPlaying()) {
            doCancelTTS();
        }
        LogMgr.d("NativeANTEngine", "doPlayTTS:" + text);
        if (!getAecEnable()) {
            doStopWakeup();
        } else if (!isInSetWakeUpWord()) {
            doEnterWakeup(false);
        }
        if (TextUtils.isEmpty(text)) {
            return;
        }
        setTtsPlaying(true);
        if (a(text)) {
            this.l.b(b(this.q.getLocalAnswerPath(text)));
        } else {
            this.l.b(text);
        }
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doReleaseAudioRecord() {
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doStopASR() {
        LogMgr.d("NativeANTEngine", "doStopASR isAsr:" + isASR());
        if (isASR()) {
            if (this.l.a()) {
                this.l.c();
            }
            this.j.a();
            setEngineState(3);
        }
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doStopWakeup() {
        LogMgr.d("NativeANTEngine", "doStopWakeup isWakeUp:" + isWakeup());
        if (isWakeup()) {
            this.j.b();
            setEngineState(0);
        }
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void doWrite(Object msg) {
        if (msg instanceof VocabContent) {
            VocabContent vocabContent = (VocabContent) msg;
            insertVocab0(vocabContent.getVocabContent(), vocabContent.getGrammarTag());
        }
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine, com.unisound.vui.engine.ANTEngine
    public void enableOneshot(boolean enable) {
        super.enableOneshot(enable);
        this.j.a(1018, UserPerferenceUtil.getOneShotWakeupWord(this.context));
        insertVocab(f(), ExoAsrTag.getMainTag(ExoAsrTag.TAG_UNIDRIVE_MAIN));
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void insertVocab0(Map<String, List<String>> vocabcontent, String grammarTag) {
        LogMgr.d("NativeANTEngine", "insertVocab0");
        this.j.a(vocabcontent, grammarTag);
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected AbstractANTEngine.AbstractUnsafe newUnsafe() {
        return new a();
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void setWakeupWord0(List<String> wakeup, boolean playBeep) {
        if (wakeup == null) {
            LogMgr.e("NativeANTEngine", "setWakeupWord error, wakeup word is null");
            return;
        }
        if (isInSetWakeUpWord()) {
            LogMgr.d("NativeANTEngine", "now is setting wakeup word, delay 100ms");
            this.d = wakeup;
            this.i.sendEmptyMessageDelayed(1003, 100L);
            return;
        }
        setInSetWakeUpWord(true);
        if (isASR() || isRecognition()) {
            try {
                doCancelEngine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.listStringEquals(this.f, wakeup)) {
            LogMgr.d("NativeANTEngine", "old wake up equals new wake up");
            this.p.onEvent(3105, 0);
            return;
        }
        try {
            doCancelEngine();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        LogMgr.d("NativeANTEngine", "setWakeupWord0,words=" + this.o.toJson(wakeup) + "playBeep:" + playBeep);
        setNeedRecoveryWakeUp(true);
        setPlayBeep(playBeep);
        this.g.clear();
        this.g.addAll(wakeup);
        this.f.clear();
        LogMgr.d("NativeANTEngine", "setWakeupWord0");
        HashMap map = new HashMap();
        map.put("wakeup", wakeup);
        this.j.b(map);
        UserPerferenceUtil.setEffectiveWakeupword(wakeup, this.context);
        this.j.a(1018, UserPerferenceUtil.getOneShotWakeupWord(this.context));
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine, com.unisound.vui.engine.ANTEngine
    public ANTEngine.Unsafe unsafe() {
        return super.unsafe();
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void updateWakeupWord0(final List<String> wakeup) {
        this.d = wakeup;
        LogMgr.d("NativeANTEngine", "updateWakeupWord0");
        if (NetUtil.getInstante(this.context).getConnectState()) {
            new Thread(new Runnable() { // from class: com.unisound.vui.engine.NativeANTEngine.3
                @Override // java.lang.Runnable
                public void run() {
                    NativeANTEngine.this.j.a(wakeup);
                }
            }).start();
        } else {
            pipeline().fireUserEventTriggered(Integer.valueOf(EventType.WAKEUP_EVENT_UPDATEWAKEUPWORD_FAIL));
        }
    }

    @Override // com.unisound.vui.engine.AbstractANTEngine
    protected void updateWakeupWord1(final List<String> wakeup) {
        this.d = wakeup;
        LogMgr.d("NativeANTEngine", "updateWakeupWord1");
        if (NetUtil.getInstante(this.context).getConnectState()) {
            new Thread(new Runnable() { // from class: com.unisound.vui.engine.NativeANTEngine.4
                @Override // java.lang.Runnable
                public void run() {
                    NativeANTEngine.this.j.a(wakeup);
                }
            }).start();
        } else {
            pipeline().fireUserEventTriggered(Integer.valueOf(EventType.WAKEUP_EVENT_UPDATEWAKEUPWORD_FAIL));
        }
    }
}
