package com.unisound.vui.handler;

import com.unisound.vui.engine.ANTEngineOption;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.engine.ANTInboundHandlerAdapter;
import com.unisound.vui.engine.EventType;
import nluparser.scheme.LocalASR;

/* loaded from: classes.dex */
public abstract class ANTEventDispatcher extends ANTInboundHandlerAdapter {
    @Override // com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onASRError(int type, String error, ANTHandlerContext ctx) throws Exception {
        if (onASRError(ctx, error)) {
            return;
        }
        ctx.fireASRError(type, error);
    }

    protected boolean onASRError(ANTHandlerContext ctx, String error) {
        return false;
    }

    @Override // com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onASREvent(int type, ANTHandlerContext ctx) throws Exception {
        boolean zOnWakeupEventUpdateWakeupWordFail;
        switch (type) {
            case 1101:
                zOnWakeupEventUpdateWakeupWordFail = onASREventRecordingStart(ctx);
                break;
            case 1102:
                zOnWakeupEventUpdateWakeupWordFail = onASREventRecordingStop(ctx);
                break;
            case 1103:
                zOnWakeupEventUpdateWakeupWordFail = onASREventVadTimeout(ctx);
                break;
            case 1104:
                zOnWakeupEventUpdateWakeupWordFail = onASREventSpeechDetected(ctx);
                break;
            case 1105:
                zOnWakeupEventUpdateWakeupWordFail = onASREventSpeechEnd(ctx);
                break;
            case 1107:
                zOnWakeupEventUpdateWakeupWordFail = onASREventRecognitionEnd(ctx);
                break;
            case 1108:
                zOnWakeupEventUpdateWakeupWordFail = onASREventUserdataUploaded(ctx);
                break;
            case 1109:
                zOnWakeupEventUpdateWakeupWordFail = onASREventGrammarCompiled(ctx);
                break;
            case 1110:
                zOnWakeupEventUpdateWakeupWordFail = onASREventGrammarLoaded(ctx);
                break;
            case 1111:
                zOnWakeupEventUpdateWakeupWordFail = onASREventGrammarInserted(ctx);
                break;
            case 1112:
                zOnWakeupEventUpdateWakeupWordFail = onASREventVocabInserted(ctx);
                break;
            case 1113:
                zOnWakeupEventUpdateWakeupWordFail = onASREventFxAbnormalTooLoud(ctx);
                break;
            case 1114:
                zOnWakeupEventUpdateWakeupWordFail = onASREventFxAbnormalTooQuiet(ctx);
                break;
            case 1115:
                zOnWakeupEventUpdateWakeupWordFail = onASREventFxAbnormalSnrBad(ctx);
                break;
            case 1116:
                zOnWakeupEventUpdateWakeupWordFail = onASREventFxAbnormalNoLeadingsilence(ctx);
                break;
            case 1117:
                zOnWakeupEventUpdateWakeupWordFail = onASREventCancel(ctx);
                break;
            case 1118:
                zOnWakeupEventUpdateWakeupWordFail = onASREventLocalEnd(ctx);
                break;
            case 1119:
                zOnWakeupEventUpdateWakeupWordFail = onASREventNetEnd(ctx);
                break;
            case 1120:
                zOnWakeupEventUpdateWakeupWordFail = onASREventEnd(ctx);
                break;
            case 1121:
                zOnWakeupEventUpdateWakeupWordFail = onASRNluEventEnd(ctx);
                break;
            case 1122:
                zOnWakeupEventUpdateWakeupWordFail = onASREventVolumeChange(ctx, ((Integer) ctx.engine().config().getOption(ANTEngineOption.GENERAL_UPDATE_VOLUME)).intValue());
                break;
            case 1123:
                zOnWakeupEventUpdateWakeupWordFail = onASREventCompileDone(ctx);
                break;
            case 1129:
                zOnWakeupEventUpdateWakeupWordFail = onASREventEngineInitDone(ctx);
                break;
            case 1130:
                zOnWakeupEventUpdateWakeupWordFail = onASREventLoadGrammarDone(ctx);
                break;
            case 1131:
                zOnWakeupEventUpdateWakeupWordFail = onASREventRecordingPrepared(ctx);
                break;
            case 1150:
                zOnWakeupEventUpdateWakeupWordFail = onASREventModelLoadSuccess(ctx);
                break;
            case 1151:
                zOnWakeupEventUpdateWakeupWordFail = onASREventModelLoadFail(ctx);
                break;
            case EventType.ASR_EVENT_ONESHOT_VAD_TIMEOUT /* 1800 */:
                zOnWakeupEventUpdateWakeupWordFail = onASREventOneshotVadTimeout(ctx);
                break;
            case 3105:
                zOnWakeupEventUpdateWakeupWordFail = onWakeupEventSetWakeupwordDone(ctx);
                break;
            case EventType.WAKEUP_EVENT_UPDATEWAKEUPWORD_FAIL /* 3107 */:
                zOnWakeupEventUpdateWakeupWordFail = onWakeupEventUpdateWakeupWordFail(ctx);
                break;
            default:
                zOnWakeupEventUpdateWakeupWordFail = false;
                break;
        }
        if (zOnWakeupEventUpdateWakeupWordFail) {
            return;
        }
        ctx.fireASREvent(type);
    }

    protected boolean onASREventCancel(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventCompileDone(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventEnd(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventEngineInitDone(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventFxAbnormalNoLeadingsilence(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventFxAbnormalSnrBad(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventFxAbnormalTooLoud(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventFxAbnormalTooQuiet(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventGrammarCompiled(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventGrammarInserted(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventGrammarLoaded(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventLoadGrammarDone(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventLocalEnd(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventModelLoadFail(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventModelLoadSuccess(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventNetEnd(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventOneshotVadTimeout(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventRecognitionEnd(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventRecordingPrepared(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventRecordingStart(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventRecordingStop(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventSpeechDetected(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventSpeechEnd(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventUserdataUploaded(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventVadTimeout(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventVocabInserted(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onASREventVolumeChange(ANTHandlerContext ctx, int volume) {
        return false;
    }

    protected boolean onASRNluEventEnd(ANTHandlerContext ctx) {
        return false;
    }

    @Override // com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onASRResult(int type, String result, ANTHandlerContext ctx) throws Exception {
        boolean zOnWakeupResult;
        switch (type) {
            case 1201:
                zOnWakeupResult = onASRResultNet(ctx, result);
                break;
            case 1202:
                zOnWakeupResult = onASRResultLocal(ctx, result);
                break;
            case 1210:
                zOnWakeupResult = onASRResultRecognition(ctx, result);
                break;
            case 3201:
                zOnWakeupResult = onWakeupResult(ctx, result);
                break;
            default:
                zOnWakeupResult = false;
                break;
        }
        if (zOnWakeupResult) {
            return;
        }
        ctx.fireASRResult(type, result);
    }

    protected boolean onASRResultLocal(ANTHandlerContext ctx, String result) {
        return false;
    }

    protected boolean onASRResultNet(ANTHandlerContext ctx, String result) {
        return false;
    }

    protected boolean onASRResultRecognition(ANTHandlerContext ctx, String result) {
        return false;
    }

    @Override // com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onNLUError(int type, String error, ANTHandlerContext ctx) throws Exception {
        ctx.fireNLUError(type, error);
    }

    @Override // com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onNLUEvent(int type, ANTHandlerContext ctx) throws Exception {
        ctx.fireNLUEvent(type);
    }

    @Override // com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onNLUResult(int type, String result, ANTHandlerContext ctx) throws Exception {
        ctx.fireNLUResult(type, result);
    }

    @Override // com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onTTSError(int type, String error, ANTHandlerContext ctx) throws Exception {
        ctx.fireTTSError(type, error);
    }

    @Override // com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onTTSEvent(int type, ANTHandlerContext ctx) throws Exception {
        boolean zOnTTSEventSwitchBackendModelSuccess;
        switch (type) {
            case 2101:
                zOnTTSEventSwitchBackendModelSuccess = onTTSEventInit(ctx);
                break;
            case 2102:
                zOnTTSEventSwitchBackendModelSuccess = onTTSEventSynthesizerStart(ctx);
                break;
            case 2103:
                zOnTTSEventSwitchBackendModelSuccess = onTTSEventSynthesizerEnd(ctx);
                break;
            case 2104:
                zOnTTSEventSwitchBackendModelSuccess = onTTSEventBufferBegin(ctx);
                break;
            case 2105:
                zOnTTSEventSwitchBackendModelSuccess = onTTSEventBufferReady(ctx);
                break;
            case 2106:
                zOnTTSEventSwitchBackendModelSuccess = onTTSEventPlayingStart(ctx);
                break;
            case 2107:
                zOnTTSEventSwitchBackendModelSuccess = onTTSEventPlayingEnd(ctx);
                break;
            case 2108:
                zOnTTSEventSwitchBackendModelSuccess = onTTSEventPause(ctx);
                break;
            case 2109:
                zOnTTSEventSwitchBackendModelSuccess = onTTSEventResume(ctx);
                break;
            case 2110:
            case 2113:
            default:
                zOnTTSEventSwitchBackendModelSuccess = false;
                break;
            case 2111:
                zOnTTSEventSwitchBackendModelSuccess = onTTSEventStop(ctx);
                break;
            case 2112:
                zOnTTSEventSwitchBackendModelSuccess = onTTSEventRelease(ctx);
                break;
            case 2114:
                zOnTTSEventSwitchBackendModelSuccess = onTTSEventSwitchBackendModelSuccess(ctx);
                break;
        }
        if (zOnTTSEventSwitchBackendModelSuccess) {
            return;
        }
        ctx.fireTTSEvent(type);
    }

    protected boolean onTTSEventBufferBegin(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onTTSEventBufferReady(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onTTSEventInit(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onTTSEventPause(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onTTSEventPlayingEnd(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onTTSEventPlayingStart(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onTTSEventRelease(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onTTSEventResume(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onTTSEventStop(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onTTSEventSwitchBackendModelSuccess(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onTTSEventSynthesizerEnd(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onTTSEventSynthesizerStart(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onWakeupEventSetWakeupwordDone(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onWakeupEventUpdateWakeupWordFail(ANTHandlerContext ctx) {
        return false;
    }

    protected boolean onWakeupResult(ANTHandlerContext ctx, String result) {
        return false;
    }

    protected boolean onWakeupResult(ANTHandlerContext ctx, LocalASR localASR) {
        return false;
    }
}
