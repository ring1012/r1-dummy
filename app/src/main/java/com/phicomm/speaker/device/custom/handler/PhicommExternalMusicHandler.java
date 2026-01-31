package com.phicomm.speaker.device.custom.handler;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import com.phicomm.speaker.device.R;
import com.phicomm.speaker.device.custom.status.PhicommDeviceStatusProcessor;
import com.unisound.ant.device.controlor.DefaultVolumeOperator;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.handler.SimpleUserEventInboundHandler;
import com.unisound.vui.handler.session.music.CommonPlayer;
import com.unisound.vui.util.AttributeKey;
import com.unisound.vui.util.ExoConstants;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.SpeakerTTSUtil;
import com.unisound.vui.util.UserPerferenceUtil;
import com.unisound.vui.util.internal.RandomHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import nlucreator.MusicNluCreator;
import nlucreator.SettingNluCreator;
import nluparser.scheme.Intent;
import nluparser.scheme.LocalASR;
import nluparser.scheme.NLU;
import nluparser.scheme.Operands;
import nluparser.scheme.Operator;
import nluparser.scheme.Result;
import nluparser.scheme.SName;
import nluparser.scheme.SettingExtIntent;
import nluparser.scheme.SettingIntent;

/* loaded from: classes.dex */
public class PhicommExternalMusicHandler extends SimpleUserEventInboundHandler<NLU> implements PhicommDeviceStatusProcessor.OnDeviceStatusChangedListener {
    private static final String TAG = PhicommExternalMusicHandler.class.getSimpleName();
    private CommonPlayer mCommonPlayer;
    private Context mContext;
    private boolean mIsExternMode;
    private HashMap<String, Integer> settingMap;
    private final AttributeKey<Boolean> EMPTY_RESULT = AttributeKey.valueOf(PhicommExternalMusicHandler.class, "EMPTY_RESULT");
    private final AttributeKey<Boolean> MUSIC_STATUS_PAUSE = AttributeKey.valueOf(PhicommExternalMusicHandler.class, "MUSIC_STATUS_PAUSE");
    private final AttributeKey<Boolean> MUSIC_STATUS_STOP = AttributeKey.valueOf(PhicommExternalMusicHandler.class, "MUSIC_STATUS_STOP");
    private final int SETTING_MP = 3;
    private final int PREV = 0;
    private final int NEXT = 1;
    private final int PLAY = 2;
    private final int PAUSE = 3;
    private final int STOP = 4;
    private final int CLOSE = 10;
    private int serviceType = 0;
    private List<String> currentWakeupList = new ArrayList();
    private final PhicommDeviceStatusProcessor mDeviceStatusProcessor = PhicommDeviceStatusProcessor.getInstance();

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    protected void initPriority() {
        setPriority(300);
    }

    public PhicommExternalMusicHandler(CommonPlayer commonPlayer, Context context) {
        this.mContext = context;
        this.mCommonPlayer = commonPlayer;
        this.mDeviceStatusProcessor.addDeviceStatusChangedListener(this);
        this.settingMap = new HashMap<>();
        this.settingMap.put(Operator.ACT_PREV, 0);
        this.settingMap.put(Operator.ACT_NEXT, 1);
        this.settingMap.put("ACT_PLAY", 2);
        this.settingMap.put(Operator.ACT_PAUSE, 3);
        this.settingMap.put(Operator.ACT_STOP, 4);
        this.settingMap.put("ACT_CLOSE", 10);
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventEngineInitDone(ANTHandlerContext ctx) {
        LogMgr.d(TAG, "DefaultMusicHandler onASREventEngineInitDone :");
        this.currentWakeupList = UserPerferenceUtil.getWakeupWord(ctx.androidContext());
        return super.onASREventEngineInitDone(ctx);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    public boolean acceptInboundEvent0(NLU evt) throws Exception {
        if (this.mIsExternMode) {
            String service = evt.getService();
            LogMgr.d(TAG, "acceptInboundEvent0 service:" + service);
            if (SName.SETTING_MP.equals(service) && !isRemotePlayMusic((SettingExtIntent) evt.getSemantic().getIntent())) {
                setPriority(300);
                this.serviceType = 3;
                return true;
            }
        }
        return false;
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void userEventTriggered(Object evt, ANTHandlerContext ctx) throws Exception {
        if (this.mIsExternMode && (evt instanceof LocalASR)) {
            String wakeupWord = ((LocalASR) evt).getRecognitionResult().replaceAll("\\s", "");
            LogMgr.d(TAG, "-->>userEventTriggered wakeupWord:" + wakeupWord);
            if (isIntervalWakeupWord(wakeupWord)) {
                firNluMusicWakeupOperate(wakeupWord);
                return;
            }
        }
        super.userEventTriggered(evt, ctx);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    public void eventReceived(NLU evt, ANTHandlerContext ctx) throws Exception {
        LogMgr.d(TAG, "DefaultMusicHandler evt :" + evt);
        String prompt = "";
        switch (this.serviceType) {
            case 3:
                SettingExtIntent intent = (SettingExtIntent) evt.getSemantic().getIntent();
                if (evt.getGeneral() != null) {
                    intent.setVoiceTip(evt.getGeneral().getText());
                }
                prompt = dealMediaSettingService(intent);
                LogMgr.d(TAG, "eventReceived:SETTING_MP " + prompt);
                break;
            default:
                LogMgr.d(TAG, "eventReceived: default");
                break;
        }
        if (TextUtils.isEmpty(prompt)) {
            LogMgr.d(TAG, "--->>eventReceived get prompt is null");
            if (ctx.hasAttr(this.MUSIC_STATUS_PAUSE) && ((Boolean) ctx.attr(this.MUSIC_STATUS_PAUSE).get()).booleanValue()) {
                exitSession(false, false);
                ctx.enterWakeup(false);
            } else {
                ctx.enterWakeup(false);
            }
            ctx.pipeline().fireTTSEvent(2107);
            return;
        }
        String noSongResult = this.mContext.getString(R.string.tts_music_no_find_song);
        if (prompt.equals(noSongResult)) {
            ctx.attr(this.EMPTY_RESULT).set(true);
        }
        ctx.playTTS(prompt);
    }

    @Override // com.unisound.vui.handler.SimpleSessionManagementHandler
    protected void doInterrupt(ANTHandlerContext ctx, String type) {
        LogMgr.e(TAG, "-->>doInterrupt type:" + type + ",eventReceived:" + this.eventReceived);
        if (this.mIsExternMode) {
            ctx.cancelTTS();
            if (ExoConstants.DO_FINISH_ALL_INTERRUPT.equals(type)) {
                ctx.attr(this.MUSIC_STATUS_STOP).set(true);
                this.mCommonPlayer.stop();
                this.shouldResume = false;
            } else {
                this.mCommonPlayer.pause();
                this.shouldResume = true;
            }
        }
    }

    @Override // com.unisound.vui.handler.SimpleSessionManagementHandler
    protected void doResume(ANTHandlerContext ctx) throws Resources.NotFoundException {
        LogMgr.d(TAG, "-->>doResume shouldResume:" + this.shouldResume);
        if (this.mIsExternMode) {
            if (this.shouldResume && (!ctx.hasAttr(this.MUSIC_STATUS_PAUSE) || !((Boolean) ctx.attr(this.MUSIC_STATUS_PAUSE).get()).booleanValue())) {
                LogMgr.d(TAG, "doResume: =========继续播放");
                mark(true);
                this.mCommonPlayer.resume();
                ctx.attr(this.MUSIC_STATUS_PAUSE).set(false);
                ctx.attr(this.MUSIC_STATUS_STOP).set(false);
                exitSession(false, false);
                setOperateWakeupWord(ctx);
                reset();
                this.shouldResume = false;
                return;
            }
            if (ctx.hasAttr(this.MUSIC_STATUS_PAUSE) && ((Boolean) ctx.attr(this.MUSIC_STATUS_PAUSE).get()).booleanValue()) {
                setOperateWakeupWord(ctx);
            }
        }
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPlayingEnd(ANTHandlerContext ctx) throws Resources.NotFoundException {
        if (this.eventReceived) {
            LogMgr.d(TAG, "-->>onTTSEventPlayingEnd serviceType=" + this.serviceType);
            switch (this.serviceType) {
                case 3:
                    if (ctx.hasAttr(this.MUSIC_STATUS_STOP) && ((Boolean) ctx.attr(this.MUSIC_STATUS_STOP).getAndRemove()).booleanValue()) {
                        exitSession(true, true);
                        break;
                    } else if (ctx.hasAttr(this.MUSIC_STATUS_PAUSE) && ((Boolean) ctx.attr(this.MUSIC_STATUS_PAUSE).get()).booleanValue()) {
                        exitSession(false, false);
                        break;
                    } else {
                        exitSession(false, true);
                        break;
                    }
                    break;
                default:
                    setOperateWakeupWord(ctx);
                    break;
            }
            return true;
        }
        return super.onTTSEventPlayingEnd(ctx);
    }

    private void firNluMusicWakeupOperate(String wakeupWord) {
        if (this.mContext.getString(R.string.music_volume_increase).equals(wakeupWord)) {
            DefaultVolumeOperator.getInstance(this.mContext).setVolumeRaise();
            return;
        }
        if (this.mContext.getString(R.string.music_volume_decrease).equals(wakeupWord)) {
            DefaultVolumeOperator.getInstance(this.mContext).setVolumeLower();
            return;
        }
        LogMgr.d(TAG, "-->>firNluMusicWakeupOperate simulation music operate command nlu");
        NLU<Intent, Result> nlu = null;
        Context context = this.ctx.androidContext();
        this.mCommonPlayer.getCurrentItem();
        if (context.getString(R.string.music_wakeup_prev).equals(wakeupWord)) {
            nlu = MusicNluCreator.createMusicPrevNlu();
        } else if (context.getString(R.string.music_wakeup_next).equals(wakeupWord)) {
            nlu = MusicNluCreator.createMusicNextNlu();
        } else if (context.getString(R.string.music_wakeup_play).equals(wakeupWord)) {
            nlu = MusicNluCreator.createMusicPlayNlu();
        } else if (context.getString(R.string.music_wakeup_pause).equals(wakeupWord)) {
            nlu = MusicNluCreator.createMusicPauseNlu();
        } else if (context.getString(R.string.music_wakeup_stop).equals(wakeupWord)) {
            nlu = MusicNluCreator.createMusicCloseNlu();
        } else if (context.getString(R.string.music_volume_increase).equals(wakeupWord)) {
            nlu = SettingNluCreator.createVolumeIncreaseNlu();
        } else if (context.getString(R.string.music_volume_decrease).equals(wakeupWord)) {
            nlu = SettingNluCreator.createVolumeDecreaseNlu();
        }
        if (nlu != null) {
            this.ctx.pipeline().fireUserEventTriggered(nlu);
        }
    }

    private void exitSession(boolean isNeedRecoveryWakeup, boolean isNeedFireResume) {
        LogMgr.d(TAG, "--->>exitSession isNeedRecoveryWakeup:" + isNeedRecoveryWakeup + ",isNeedFireResume:" + isNeedFireResume);
        reset();
        if (isNeedRecoveryWakeup) {
            this.ctx.cancelEngine();
            recoveryDefaultWakeupWord(this.ctx);
        }
        if (isNeedFireResume) {
            fireResume(this.ctx);
        }
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    protected void reset() {
        super.reset();
        this.serviceType = 0;
    }

    private boolean isIntervalWakeupWord(String wakeupWord) {
        String[] words = SpeakerTTSUtil.getTTSStringArray(R.array.music_running_wakeup_words, this.ctx.androidContext());
        if (!Arrays.asList(words).contains(wakeupWord)) {
            return false;
        }
        String avoidWakeNext = this.mContext.getString(R.string.music_avoid_wakeup_next);
        String avoidWakePre = this.mContext.getString(R.string.music_avoid_wakeup_prev);
        return (avoidWakeNext.equals(wakeupWord) || avoidWakePre.equals(wakeupWord)) ? false : true;
    }

    protected void setOperateWakeupWord(ANTHandlerContext ctx) throws Resources.NotFoundException {
        String[] musicWakeup = ctx.androidContext().getResources().getStringArray(R.array.music_running_wakeup_words);
        List<String> listWakeup = Arrays.asList(musicWakeup);
        this.currentWakeupList = UserPerferenceUtil.getEffectWakeupword(this.mContext);
        boolean isContain = this.currentWakeupList.containsAll(listWakeup);
        LogMgr.d(TAG, "-->>setOperateWakeupWord isContain:" + isContain);
        if (!isContain) {
            this.currentWakeupList.addAll(listWakeup);
            ctx.setWakeupWord(this.currentWakeupList, false);
        }
        LogMgr.d(TAG, "setOperateWakeupWord wakupWords:" + this.currentWakeupList);
    }

    protected void recoveryDefaultWakeupWord(ANTHandlerContext ctx) {
        this.currentWakeupList = UserPerferenceUtil.getWakeupWord(ctx.androidContext());
        ctx.setWakeupWord(this.currentWakeupList, false);
        LogMgr.d(TAG, "recoveryDefaultWakeupWord wakupWords:" + this.currentWakeupList);
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupEventSetWakeupwordDone(ANTHandlerContext ctx) {
        if (this.eventReceived) {
            return true;
        }
        return super.onWakeupEventSetWakeupwordDone(ctx);
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupEventUpdateWakeupWordFail(ANTHandlerContext ctx) {
        if (this.eventReceived) {
            return true;
        }
        return super.onWakeupEventUpdateWakeupWordFail(ctx);
    }

    private boolean isRemotePlayMusic(SettingExtIntent intent) {
        if (intent.getOperations().iterator().hasNext()) {
            SettingIntent settingIntent = intent.getOperations().iterator().next();
            String operands = settingIntent.getOperands();
            if (Operands.ATTR_BUTTON.equals(operands)) {
                return true;
            }
        }
        return false;
    }

    private String dealMediaSettingService(SettingExtIntent settingExtIntent) throws Resources.NotFoundException {
        String prompt = "";
        if (settingExtIntent.getOperations().iterator().hasNext()) {
            SettingIntent settingIntent = settingExtIntent.getOperations().iterator().next();
            String operator = settingIntent.getOperator();
            String operands = settingIntent.getOperands();
            LogMgr.d(TAG, "-->>dealMediaSettingService operator11:" + operator);
            if (this.settingMap.containsKey(operator)) {
                switch (this.settingMap.get(operator).intValue()) {
                    case 0:
                        if (this.ctx.hasAttr(this.MUSIC_STATUS_STOP) && ((Boolean) this.ctx.attr(this.MUSIC_STATUS_STOP).get()).booleanValue()) {
                            return this.mContext.getString(R.string.tts_music_not_playing);
                        }
                        this.mCommonPlayer.playPrev();
                        prompt = RandomHelper.getRandom(R.array.tts_action_music_play_answer, this.ctx.androidContext());
                        break;
                        break;
                    case 1:
                        if (this.ctx.hasAttr(this.MUSIC_STATUS_STOP) && ((Boolean) this.ctx.attr(this.MUSIC_STATUS_PAUSE).get()).booleanValue()) {
                            return this.mContext.getString(R.string.tts_music_not_playing);
                        }
                        this.mCommonPlayer.playNext();
                        prompt = RandomHelper.getRandom(R.array.tts_action_music_play_answer, this.ctx.androidContext());
                        break;
                        break;
                    case 2:
                        LogMgr.d(TAG, "dealMediaSettingService: " + operands);
                        if ("OBJ_FAV_MUSIC_LIST".equals(operands)) {
                            prompt = settingExtIntent.getVoiceTip();
                            this.mCommonPlayer.resume();
                            break;
                        } else if (!this.mCommonPlayer.isPlaying()) {
                            this.mCommonPlayer.resume();
                            setOperateWakeupWord(this.ctx);
                            this.ctx.attr(this.MUSIC_STATUS_PAUSE).set(false);
                            this.ctx.attr(this.MUSIC_STATUS_STOP).set(false);
                            break;
                        } else {
                            prompt = this.mContext.getString(R.string.tts_music_is_already_playing);
                            break;
                        }
                    case 3:
                    case 4:
                        if (!this.ctx.hasAttr(this.MUSIC_STATUS_PAUSE) || !((Boolean) this.ctx.attr(this.MUSIC_STATUS_PAUSE).get()).booleanValue()) {
                            this.mCommonPlayer.pause();
                            this.shouldResume = false;
                            prompt = String.format(this.mContext.getString(R.string.tts_music_control_pause), new Object[0]);
                            this.ctx.attr(this.MUSIC_STATUS_PAUSE).set(true);
                            break;
                        }
                        break;
                    case 10:
                        this.ctx.attr(this.MUSIC_STATUS_STOP).set(true);
                        this.mCommonPlayer.stop();
                        this.shouldResume = false;
                        this.ctx.attr(this.MUSIC_STATUS_STOP).set(true);
                        prompt = settingExtIntent.getVoiceTip();
                        if (TextUtils.isEmpty(prompt)) {
                        }
                        break;
                }
            } else {
                prompt = this.mContext.getString(R.string.tts_music_change_no_supported);
            }
        }
        return prompt;
    }

    @Override // com.phicomm.speaker.device.custom.status.PhicommDeviceStatusProcessor.OnDeviceStatusChangedListener
    public void onDeviceStatusChanged(int prevStatus, int status) throws Resources.NotFoundException {
        if (status == 3) {
            this.mIsExternMode = true;
            if (this.ctx != null) {
                setOperateWakeupWord(this.ctx);
                return;
            }
            return;
        }
        if (prevStatus == 3) {
            this.mIsExternMode = false;
            this.shouldResume = false;
            if (this.ctx != null) {
                this.ctx.attr(this.MUSIC_STATUS_PAUSE).remove();
                this.ctx.attr(this.MUSIC_STATUS_STOP).remove();
                if (status != 1) {
                    recoveryDefaultWakeupWord(this.ctx);
                }
            }
        }
    }
}
