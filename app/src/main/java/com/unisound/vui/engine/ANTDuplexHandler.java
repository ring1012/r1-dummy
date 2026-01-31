package com.unisound.vui.engine;

import com.unisound.vui.handler.ANTEventDispatcher;
import java.util.List;
import nluparser.scheme.LocalASR;

/* loaded from: classes.dex */
public class ANTDuplexHandler extends ANTEventDispatcher implements ANTOutboundHandler {
    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void cancelASR(ANTHandlerContext ctx) throws Exception {
        ctx.cancelASR();
    }

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void cancelEngine(ANTHandlerContext ctx) throws Exception {
        ctx.cancelEngine();
    }

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void cancelTTS(ANTHandlerContext ctx) throws Exception {
        ctx.cancelTTS();
    }

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void doPPTAction(ANTHandlerContext ctx) throws Exception {
        ctx.doPPTAction();
    }

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void enterASR(ANTHandlerContext ctx) throws Exception {
        ctx.enterASR();
    }

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void enterWakeup(ANTHandlerContext ctx, boolean playBeep) throws Exception {
        ctx.enterWakeup(playBeep);
    }

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void initializeMode(ANTHandlerContext ctx) throws Exception {
        ctx.initializeMode();
    }

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void initializeSdk(ANTHandlerContext ctx) throws Exception {
        ctx.initializeSdk();
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onASRError(int type, String error, ANTHandlerContext ctx) throws Exception {
        super.onASRError(type, error, ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASRError(ANTHandlerContext ctx, String error) {
        return super.onASRError(ctx, error);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventCancel(ANTHandlerContext ctx) {
        return super.onASREventCancel(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventCompileDone(ANTHandlerContext ctx) {
        return super.onASREventCompileDone(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventEnd(ANTHandlerContext ctx) {
        return super.onASREventEnd(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventEngineInitDone(ANTHandlerContext ctx) {
        return super.onASREventEngineInitDone(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventFxAbnormalNoLeadingsilence(ANTHandlerContext ctx) {
        return super.onASREventFxAbnormalNoLeadingsilence(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventFxAbnormalSnrBad(ANTHandlerContext ctx) {
        return super.onASREventFxAbnormalSnrBad(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventFxAbnormalTooLoud(ANTHandlerContext ctx) {
        return super.onASREventFxAbnormalTooLoud(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventFxAbnormalTooQuiet(ANTHandlerContext ctx) {
        return super.onASREventFxAbnormalTooQuiet(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventGrammarCompiled(ANTHandlerContext ctx) {
        return super.onASREventGrammarCompiled(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventGrammarInserted(ANTHandlerContext ctx) {
        return super.onASREventGrammarInserted(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventGrammarLoaded(ANTHandlerContext ctx) {
        return super.onASREventGrammarLoaded(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventLoadGrammarDone(ANTHandlerContext ctx) {
        return super.onASREventLoadGrammarDone(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventLocalEnd(ANTHandlerContext ctx) {
        return super.onASREventLocalEnd(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventModelLoadFail(ANTHandlerContext ctx) {
        return super.onASREventModelLoadFail(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventModelLoadSuccess(ANTHandlerContext ctx) {
        return super.onASREventModelLoadSuccess(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventNetEnd(ANTHandlerContext ctx) {
        return super.onASREventNetEnd(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventOneshotVadTimeout(ANTHandlerContext ctx) {
        return super.onASREventOneshotVadTimeout(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventRecognitionEnd(ANTHandlerContext ctx) {
        return super.onASREventRecognitionEnd(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventRecordingPrepared(ANTHandlerContext ctx) {
        return super.onASREventRecordingPrepared(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventRecordingStart(ANTHandlerContext ctx) {
        return super.onASREventRecordingStart(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventRecordingStop(ANTHandlerContext ctx) {
        return super.onASREventRecordingStop(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventSpeechDetected(ANTHandlerContext ctx) {
        return super.onASREventSpeechDetected(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventSpeechEnd(ANTHandlerContext ctx) {
        return super.onASREventSpeechEnd(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventUserdataUploaded(ANTHandlerContext ctx) {
        return super.onASREventUserdataUploaded(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventVadTimeout(ANTHandlerContext ctx) {
        return super.onASREventVadTimeout(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventVocabInserted(ANTHandlerContext ctx) {
        return super.onASREventVocabInserted(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventVolumeChange(ANTHandlerContext ctx, int volume) {
        return super.onASREventVolumeChange(ctx, volume);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASRNluEventEnd(ANTHandlerContext ctx) {
        return super.onASRNluEventEnd(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASRResultLocal(ANTHandlerContext ctx, String result) {
        return super.onASRResultLocal(ctx, result);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASRResultNet(ANTHandlerContext ctx, String result) {
        return super.onASRResultNet(ctx, result);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASRResultRecognition(ANTHandlerContext ctx, String result) {
        return super.onASRResultRecognition(ctx, result);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onNLUError(int type, String error, ANTHandlerContext ctx) throws Exception {
        super.onNLUError(type, error, ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onNLUEvent(int type, ANTHandlerContext ctx) throws Exception {
        super.onNLUEvent(type, ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onNLUResult(int type, String result, ANTHandlerContext ctx) throws Exception {
        super.onNLUResult(type, result, ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onTTSError(int type, String error, ANTHandlerContext ctx) throws Exception {
        super.onTTSError(type, error, ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onTTSEvent(int type, ANTHandlerContext ctx) throws Exception {
        super.onTTSEvent(type, ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventBufferBegin(ANTHandlerContext ctx) {
        return super.onTTSEventBufferBegin(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventBufferReady(ANTHandlerContext ctx) {
        return super.onTTSEventBufferReady(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventInit(ANTHandlerContext ctx) {
        return super.onTTSEventInit(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPause(ANTHandlerContext ctx) {
        return super.onTTSEventPause(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPlayingEnd(ANTHandlerContext ctx) {
        return super.onTTSEventPlayingEnd(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPlayingStart(ANTHandlerContext ctx) {
        return super.onTTSEventPlayingStart(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventRelease(ANTHandlerContext ctx) {
        return super.onTTSEventRelease(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventResume(ANTHandlerContext ctx) {
        return super.onTTSEventResume(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventStop(ANTHandlerContext ctx) {
        return super.onTTSEventStop(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventSwitchBackendModelSuccess(ANTHandlerContext ctx) {
        return super.onTTSEventSwitchBackendModelSuccess(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventSynthesizerEnd(ANTHandlerContext ctx) {
        return super.onTTSEventSynthesizerEnd(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventSynthesizerStart(ANTHandlerContext ctx) {
        return super.onTTSEventSynthesizerStart(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupEventSetWakeupwordDone(ANTHandlerContext ctx) {
        return super.onWakeupEventSetWakeupwordDone(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupEventUpdateWakeupWordFail(ANTHandlerContext ctx) {
        return super.onWakeupEventUpdateWakeupWordFail(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupResult(ANTHandlerContext ctx, String result) {
        return super.onWakeupResult(ctx, result);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupResult(ANTHandlerContext ctx, LocalASR localASR) {
        return super.onWakeupResult(ctx, localASR);
    }

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void playBuffer(ANTHandlerContext ctx, byte[] data) throws Exception {
        ctx.playBuffer(data);
    }

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void playTTS(ANTHandlerContext ctx, String text) throws Exception {
        ctx.playTTS(text);
    }

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void setWakeupWord(ANTHandlerContext ctx, List<String> wakeup, boolean playBeep) throws Exception {
        ctx.setWakeupWord(wakeup, playBeep);
    }

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void stopASR(ANTHandlerContext ctx) throws Exception {
        ctx.stopASR();
    }

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void stopWakeup(ANTHandlerContext ctx) throws Exception {
        ctx.stopWakeup();
    }

    public void updateWakeupWord(ANTHandlerContext ctx, List<String> wakeup) throws Exception {
        ctx.updateWakeupWord(wakeup);
    }

    @Override // com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void userEventTriggered(Object evt, ANTHandlerContext ctx) throws Exception {
        super.userEventTriggered(evt, ctx);
    }

    public void write(ANTHandlerContext ctx, Object msg) throws Exception {
        ctx.write(msg);
    }
}
