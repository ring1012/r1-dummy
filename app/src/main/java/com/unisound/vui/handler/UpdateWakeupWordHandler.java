package com.unisound.vui.handler;

import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.transport.R;
import com.unisound.vui.util.AttributeKey;
import com.unisound.vui.util.SpeakerTTSUtil;
import java.util.List;

/* loaded from: classes.dex */
public class UpdateWakeupWordHandler extends SimpleSessionManagementHandler {

    /* renamed from: a, reason: collision with root package name */
    private static final AttributeKey<Boolean> f422a = AttributeKey.valueOf(UpdateWakeupWordHandler.class, "UPDATE_WAKEUPWORD");

    @Override // com.unisound.vui.handler.SimpleSessionManagementHandler
    protected void doInterrupt(ANTHandlerContext ctx, String interruptType) {
        if (ctx.hasAttr(f422a) && ((Boolean) ctx.attr(f422a).getAndRemove()).booleanValue()) {
            ctx.cancelTTS();
        }
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPlayingEnd(ANTHandlerContext ctx) {
        if (!ctx.hasAttr(f422a) || !((Boolean) ctx.attr(f422a).getAndRemove()).booleanValue()) {
            return super.onTTSEventPlayingEnd(ctx);
        }
        ctx.enterWakeup(false);
        fireResume(ctx);
        return true;
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupEventSetWakeupwordDone(ANTHandlerContext ctx) {
        if (!ctx.hasAttr(f422a) || !((Boolean) ctx.attr(f422a).get()).booleanValue()) {
            return super.onWakeupEventSetWakeupwordDone(ctx);
        }
        ctx.playTTS(ctx.androidContext().getString(R.string.tts_update_wakeup_word_succ));
        return true;
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupEventUpdateWakeupWordFail(ANTHandlerContext ctx) {
        if (!ctx.hasAttr(f422a) || !((Boolean) ctx.attr(f422a).get()).booleanValue()) {
            return super.onWakeupEventUpdateWakeupWordFail(ctx);
        }
        ctx.playTTS(SpeakerTTSUtil.getTTSString(R.string.tts_update_wakeup_word_fail, -1, ctx.engine().androidContext()));
        return true;
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.engine.ANTOutboundHandler
    public void updateWakeupWord(ANTHandlerContext ctx, List<String> wakeup) throws Exception {
        fireActiveInterrupt(ctx);
        ctx.attr(f422a).set(true);
        ctx.updateWakeupWord(wakeup);
    }
}
