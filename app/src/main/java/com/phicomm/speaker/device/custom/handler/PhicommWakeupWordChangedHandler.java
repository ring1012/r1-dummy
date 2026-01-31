package com.phicomm.speaker.device.custom.handler;

import com.phicomm.speaker.device.R;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.handler.UpdateWakeupWordHandler;
import com.unisound.vui.util.AttributeKey;
import com.unisound.vui.util.SpeakerTTSUtil;
import com.unisound.vui.util.UserPerferenceUtil;
import java.util.List;

/* loaded from: classes.dex */
public class PhicommWakeupWordChangedHandler extends UpdateWakeupWordHandler {
    public static final AttributeKey<Boolean> UPDATE_WAKEUPWORD = AttributeKey.valueOf(UpdateWakeupWordHandler.class, "UPDATE_WAKEUPWORD");

    @Override // com.unisound.vui.handler.UpdateWakeupWordHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.engine.ANTOutboundHandler
    public void updateWakeupWord(ANTHandlerContext ctx, List<String> wakeup) throws Exception {
        fireActiveInterrupt(ctx);
        ctx.attr(UPDATE_WAKEUPWORD).set(true);
        ctx.updateWakeupWord(wakeup);
    }

    @Override // com.unisound.vui.handler.UpdateWakeupWordHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupEventSetWakeupwordDone(ANTHandlerContext ctx) {
        if (ctx.hasAttr(UPDATE_WAKEUPWORD) && ((Boolean) ctx.attr(UPDATE_WAKEUPWORD).get()).booleanValue()) {
            if (!UserPerferenceUtil.getDeviceBindState(ctx.androidContext())) {
                return true;
            }
            ctx.playTTS(ctx.androidContext().getString(R.string.tts_update_wakeup_word_succ));
            return true;
        }
        return super.onWakeupEventSetWakeupwordDone(ctx);
    }

    @Override // com.unisound.vui.handler.UpdateWakeupWordHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupEventUpdateWakeupWordFail(ANTHandlerContext ctx) {
        if (!ctx.hasAttr(UPDATE_WAKEUPWORD) || !((Boolean) ctx.attr(UPDATE_WAKEUPWORD).get()).booleanValue()) {
            return super.onWakeupEventUpdateWakeupWordFail(ctx);
        }
        ctx.playTTS(SpeakerTTSUtil.getTTSString(R.string.tts_update_wakeup_word_fail, -1, ctx.engine().androidContext()));
        return true;
    }

    @Override // com.unisound.vui.handler.UpdateWakeupWordHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPlayingEnd(ANTHandlerContext ctx) {
        if (!ctx.hasAttr(UPDATE_WAKEUPWORD) || !((Boolean) ctx.attr(UPDATE_WAKEUPWORD).getAndRemove()).booleanValue()) {
            return super.onTTSEventPlayingEnd(ctx);
        }
        ctx.enterWakeup(false);
        fireResume(ctx);
        return true;
    }

    @Override // com.unisound.vui.handler.UpdateWakeupWordHandler, com.unisound.vui.handler.SimpleSessionManagementHandler
    protected void doInterrupt(ANTHandlerContext ctx, String interruptType) {
        if (ctx.hasAttr(UPDATE_WAKEUPWORD) && ((Boolean) ctx.attr(UPDATE_WAKEUPWORD).getAndRemove()).booleanValue()) {
            ctx.cancelTTS();
        }
    }
}
