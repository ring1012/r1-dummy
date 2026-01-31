package com.phicomm.speaker.device.custom.handler;

import android.content.Context;
import com.phicomm.speaker.device.custom.ipc.PhicommLightController;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.handler.session.memo.DefaultReminderHandler;

/* loaded from: classes.dex */
public class PhicommReminderHandler extends DefaultReminderHandler {
    private PhicommLightController mLightController;

    public PhicommReminderHandler(Context context) {
        super(context);
        this.mLightController = new PhicommLightController(context);
    }

    @Override // com.unisound.vui.handler.session.memo.AbstractMemoHandler, com.unisound.vui.handler.SimpleUserEventInboundHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPlayingEnd(ANTHandlerContext ctx) {
        if (!this.eventReceived) {
            return false;
        }
        if (ctx.hasAttr(NEED_SUPPLEMENT) && ((Boolean) ctx.attr(NEED_SUPPLEMENT).getAndRemove()).booleanValue()) {
            reset();
            ctx.enterASR();
            this.mLightController.turnOnWakeupLastLight();
            return false;
        }
        return super.onTTSEventPlayingEnd(ctx);
    }
}
