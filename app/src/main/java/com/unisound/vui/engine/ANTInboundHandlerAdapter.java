package com.unisound.vui.engine;

/* loaded from: classes.dex */
public class ANTInboundHandlerAdapter extends ANTHandlerAdapter implements ANTInboundHandler {
    public void onASRError(int type, String error, ANTHandlerContext ctx) throws Exception {
        ctx.fireASRError(type, error);
    }

    @Override // com.unisound.vui.engine.ANTInboundHandler
    public void onASREvent(int type, ANTHandlerContext ctx) throws Exception {
        ctx.fireASREvent(type);
    }

    @Override // com.unisound.vui.engine.ANTInboundHandler
    public void onASRResult(int type, String result, ANTHandlerContext ctx) throws Exception {
        ctx.fireASRResult(type, result);
    }

    public void onNLUError(int type, String error, ANTHandlerContext ctx) throws Exception {
        ctx.fireNLUError(type, error);
    }

    public void onNLUEvent(int type, ANTHandlerContext ctx) throws Exception {
        ctx.fireNLUEvent(type);
    }

    public void onNLUResult(int type, String result, ANTHandlerContext ctx) throws Exception {
        ctx.fireNLUResult(type, result);
    }

    public void onTTSError(int type, String error, ANTHandlerContext ctx) throws Exception {
        ctx.fireTTSError(type, error);
    }

    public void onTTSEvent(int type, ANTHandlerContext ctx) throws Exception {
        ctx.fireTTSEvent(type);
    }

    public void userEventTriggered(Object evt, ANTHandlerContext ctx) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }
}
