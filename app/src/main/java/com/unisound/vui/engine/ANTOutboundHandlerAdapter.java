package com.unisound.vui.engine;

import java.util.List;

/* loaded from: classes.dex */
public class ANTOutboundHandlerAdapter extends ANTHandlerAdapter implements ANTOutboundHandler {
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

    @Override // com.unisound.vui.engine.ANTOutboundHandler
    public void updateWakeupWord(ANTHandlerContext ctx, List<String> wakeup) throws Exception {
        ctx.updateWakeupWord(wakeup);
    }

    public void write(ANTHandlerContext ctx, Object msg) throws Exception {
        ctx.write(msg);
    }
}
