package com.unisound.vui.engine;

import android.content.Context;
import android.text.TextUtils;
import com.unisound.vui.common.file.FileHelper;
import com.unisound.vui.engine.ANTEngine;
import com.unisound.vui.transport.out.VocabContent;
import com.unisound.vui.util.AttributeKey;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.StringUtils;
import com.unisound.vui.util.UserPerferenceUtil;
import com.unisound.vui.util.d;
import java.io.File;
import java.util.List;
import java.util.Map;
import nluparser.scheme.SCode;

/* loaded from: classes.dex */
public abstract class AbstractANTEngine extends d implements ANTEngine {
    protected static final String TAG = "NativeANTEngine";
    private static final String TTS_PCM_END_TAG = "</PCM>";
    private static final String TTS_PCM_START_TAG = "<PCM>";
    private volatile boolean aecEnable;
    protected Context context;
    protected volatile int engineState;
    private volatile boolean isOneshot;
    private volatile boolean isWakeupRecord;
    private static final AttributeKey<Context> ANDROID_CONTEXT = AttributeKey.newInstance("ANDROID_CONTEXT");
    private static final AttributeKey<List<String>> WAKEUP_WORD = AttributeKey.newInstance(SCode.WAKEUP_WORD);
    private static final AttributeKey<Boolean> MODE_INITIALIZED = AttributeKey.newInstance("MODE_INITIALIZED");
    private static final AttributeKey<Boolean> NEED_RECOVERY_WAKE_UP = AttributeKey.newInstance("NEED_RECOVERY_WAKE_UP");
    protected volatile boolean isTtsPlaying = false;
    protected volatile boolean lastPlayBeep = false;
    private volatile boolean inSetWakeUpWord = false;
    private volatile boolean playBeep = false;
    private final ANTEngine.Unsafe unsafe = newUnsafe();
    private final DefaultANTPipeline pipeline = new DefaultANTPipeline(this);

    protected abstract class AbstractUnsafe implements ANTEngine.Unsafe {
        protected AbstractUnsafe() {
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void cancelASR() {
            try {
                AbstractANTEngine.this.doCancelASR();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void cancelEngine() {
            try {
                AbstractANTEngine.this.doCancelEngine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void cancelTTS() {
            try {
                AbstractANTEngine.this.doCancelTTS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void enterASR() {
            try {
                AbstractANTEngine.this.doEnterASR();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void enterWakeup(boolean playBeep) {
            try {
                AbstractANTEngine.this.doEnterWakeup(playBeep);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void initEngine() {
            try {
                AbstractANTEngine.this.doInitEngine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void initializeMode() {
            try {
                AbstractANTEngine.this.doInitializeMode();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void initializeSdk() {
            try {
                AbstractANTEngine.this.doInitializeSdk();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void insertVocab(Map<String, List<String>> vocabcontent, String grammarTag) {
            try {
                AbstractANTEngine.this.insertVocab0(vocabcontent, grammarTag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void markModeInitialized() {
            if (!AbstractANTEngine.this.attr(AbstractANTEngine.MODE_INITIALIZED).compareAndSet(false, true)) {
                throw new Error("markModeInitialized");
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void playBuffer(byte[] data) {
            try {
                AbstractANTEngine.this.doPlayBuffer(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void playTTS(String text) {
            try {
                if (TextUtils.isEmpty(text) || !text.startsWith(UserPerferenceUtil.PCM_TAG)) {
                    AbstractANTEngine.this.doPlayTTS(text);
                    return;
                }
                String strMatchFind = StringUtils.matchFind(text, UserPerferenceUtil.FILE_TAG);
                if (!TextUtils.isEmpty(strMatchFind)) {
                    String str = strMatchFind + UserPerferenceUtil.getPcmFileSuffix();
                    String path = FileHelper.getTTSPCMFile(str).getPath();
                    File file = new File(path);
                    if (file.isFile() && file.exists()) {
                        AbstractANTEngine.this.doPlayTTS(AbstractANTEngine.TTS_PCM_START_TAG + FileHelper.getTTSPCMFile(str).getPath() + AbstractANTEngine.TTS_PCM_END_TAG);
                        return;
                    }
                    LogMgr.d(AbstractANTEngine.TAG, "pcm file not exist:" + path);
                }
                AbstractANTEngine.this.doPlayTTS(StringUtils.matchFind(text, UserPerferenceUtil.TXT_TAG));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void setAndroidContext(Context context) {
            AbstractANTEngine.this.attr(AbstractANTEngine.ANDROID_CONTEXT).setIfAbsent(context);
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void setWakeupWord(List<String> wakeup, boolean playBeep) {
            try {
                AbstractANTEngine.this.attr(AbstractANTEngine.WAKEUP_WORD).set(wakeup);
                AbstractANTEngine.this.setWakeupWord0(wakeup, playBeep);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void stopASR() {
            try {
                AbstractANTEngine.this.doStopASR();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void stopWakeup() {
            try {
                AbstractANTEngine.this.doStopWakeup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void updateCustomWakeupWord(List<String> wakeup) {
            try {
                AbstractANTEngine.this.updateWakeupWord1(wakeup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void updateWakeupWord(List<String> wakeup) {
            try {
                AbstractANTEngine.this.attr(AbstractANTEngine.WAKEUP_WORD).set(wakeup);
                AbstractANTEngine.this.updateWakeupWord0(wakeup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override // com.unisound.vui.engine.ANTEngine.Unsafe
        public void write(Object msg) {
            try {
                AbstractANTEngine.this.doWrite(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected AbstractANTEngine() {
        attr(MODE_INITIALIZED).set(false);
        attr(NEED_RECOVERY_WAKE_UP).set(false);
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public Context androidContext() {
        return (Context) attr(ANDROID_CONTEXT).get();
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void cancelASR() {
        this.pipeline.cancelASR();
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void cancelTTS() {
        this.pipeline.cancelTTS();
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void closeReleaseStatus() {
        try {
            doCloseReleaseStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void doCancelASR() throws Exception;

    protected abstract void doCancelEngine() throws Exception;

    protected abstract void doCancelTTS() throws Exception;

    protected abstract void doCloseReleaseStatus() throws Exception;

    protected abstract void doEnterASR() throws Exception;

    protected abstract void doEnterWakeup(boolean z) throws Exception;

    protected abstract void doInitEngine() throws Exception;

    protected abstract void doInitializeMode() throws Exception;

    protected abstract void doInitializeSdk() throws Exception;

    @Override // com.unisound.vui.engine.ANTEngine
    public void doPPTAction() {
        this.pipeline.doPPTAction();
    }

    protected abstract void doPPTAction0() throws Exception;

    protected abstract void doPlayBuffer(byte[] bArr) throws Exception;

    protected abstract void doPlayTTS(String str) throws Exception;

    protected abstract void doReleaseAudioRecord() throws Exception;

    protected abstract void doStopASR() throws Exception;

    protected abstract void doStopWakeup() throws Exception;

    protected abstract void doWrite(Object obj) throws Exception;

    @Override // com.unisound.vui.engine.ANTEngine
    public void enableOneshot(boolean enable) {
        UserPerferenceUtil.setOneshotEnable(androidContext(), enable);
        this.isOneshot = enable;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void enterASR() {
        this.pipeline.enterASR();
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void enterWakeup(boolean playBeep) {
        this.pipeline.enterWakeup(playBeep);
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public boolean getAecEnable() {
        return this.aecEnable;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public int getEngineState() {
        return this.engineState;
    }

    void initAecEnableState() {
        this.aecEnable = UserPerferenceUtil.getAecEnable(androidContext());
    }

    void initOneshotState() {
        this.isOneshot = UserPerferenceUtil.getOneshotEnable(androidContext());
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void initializeMode() {
        this.pipeline.initializeMode();
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void initializeSdk() {
        this.pipeline.initializeSdk();
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void insertVocab(Map<String, List<String>> vocabcontent, String grammarTag) {
        this.pipeline.write(new VocabContent(vocabcontent, grammarTag));
    }

    protected abstract void insertVocab0(Map<String, List<String>> map, String str) throws Exception;

    @Override // com.unisound.vui.engine.ANTEngine
    public boolean isASR() {
        return this.engineState == 2;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public boolean isIdle() {
        return this.engineState == 0;
    }

    public boolean isInSetWakeUpWord() {
        return this.inSetWakeUpWord;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public boolean isModeInitialized() {
        return ((Boolean) attr(MODE_INITIALIZED).get()).booleanValue();
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public boolean isNeedRecoveryWakeUp() {
        return ((Boolean) attr(NEED_RECOVERY_WAKE_UP).get()).booleanValue();
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public boolean isOneshot() {
        return this.isOneshot;
    }

    public boolean isPlayBeep() {
        return this.playBeep;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public boolean isRecognition() {
        return this.engineState == 3;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public boolean isShowVolume() {
        return isASR() || (isWakeup() && this.lastPlayBeep);
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public boolean isTTSPlaying() {
        return this.isTtsPlaying;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public boolean isWakeup() {
        return this.engineState == 1;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public boolean isWakeupRecord() {
        return this.isWakeupRecord;
    }

    protected abstract AbstractUnsafe newUnsafe();

    @Override // com.unisound.vui.engine.ANTEngine
    public ANTPipeline pipeline() {
        return this.pipeline;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void playTTS(String text) {
        this.pipeline.playTTS(text);
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void releaseAudioRecord() {
        try {
            doReleaseAudioRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final void setEngineState(int state) {
        LogMgr.d(TAG, "setState:" + state);
        if (this.engineState == state) {
            return;
        }
        this.engineState = state;
    }

    public void setInSetWakeUpWord(boolean inSetWakeUpWord) {
        this.inSetWakeUpWord = inSetWakeUpWord;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void setNeedRecoveryWakeUp(boolean intercept) {
        attr(NEED_RECOVERY_WAKE_UP).set(Boolean.valueOf(intercept));
    }

    public void setPlayBeep(boolean playBeep) {
        LogMgr.d(TAG, "setPlayBeep:" + playBeep);
        this.playBeep = playBeep;
    }

    protected void setTtsPlaying(boolean ttsPlaying) {
        this.isTtsPlaying = ttsPlaying;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void setWakeupRecord(boolean enable) {
        this.isWakeupRecord = enable;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void setWakeupWord(List<String> wakeup, boolean playBeep) {
        this.pipeline.setWakeupWord(wakeup, playBeep);
    }

    protected abstract void setWakeupWord0(List<String> list, boolean z) throws Exception;

    @Override // com.unisound.vui.engine.ANTEngine
    public void stopASR() {
        this.pipeline.stopASR();
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void stopWakeup() {
        this.pipeline.stopWakeup();
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public ANTEngine.Unsafe unsafe() {
        return this.unsafe;
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void updateWakeupWord(List<String> wakeup) {
        this.pipeline.updateWakeupWord(wakeup);
    }

    protected abstract void updateWakeupWord0(List<String> list) throws Exception;

    protected abstract void updateWakeupWord1(List<String> list) throws Exception;

    @Override // com.unisound.vui.engine.ANTEngine
    public List<String> wakeupWord() {
        return (List) attr(WAKEUP_WORD).get();
    }

    @Override // com.unisound.vui.engine.ANTEngine
    public void write(Object msg) {
        this.pipeline.write(msg);
    }
}
