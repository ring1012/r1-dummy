package com.unisound.vui.handler;

import com.unisound.vui.engine.ANTDuplexHandler;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.util.ExoConstants;
import com.unisound.vui.util.LogMgr;

/* loaded from: classes.dex */
public class SimpleSessionManager extends ANTDuplexHandler {
    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void userEventTriggered(Object evt, ANTHandlerContext ctx) throws Exception {
        if (evt instanceof ExoConstants.a) {
            SessionRegister.getInstance().recycle();
        }
        super.userEventTriggered(evt, ctx);
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.engine.ANTOutboundHandler
    public void write(ANTHandlerContext ctx, Object msg) throws Exception {
        if (ExoConstants.FIRE_ACTIVE_INTERRUPT.equals(msg)) {
            LogMgr.d("SimpleSessionManager", "fireActiveInterrupt %s", ctx.handler().getClass().getName());
            ctx.fireUserEventTriggered(ExoConstants.DO_ACTIVE_INTERRUPT);
            return;
        }
        if (ExoConstants.DO_ONE_SHOT_INTERRUPT.equals(msg)) {
            LogMgr.d("SimpleSessionManager", "fireOneShotInterrupt %s", ctx.handler().getClass().getName());
            ctx.fireUserEventTriggered(ExoConstants.DO_ONE_SHOT_INTERRUPT);
            return;
        }
        if (ExoConstants.FIRE_ASR_INTERRUPT.equals(msg)) {
            LogMgr.d("SimpleSessionManager", "fireAsrInterrupt %s", ctx.handler().getClass().getName());
            ctx.fireUserEventTriggered(ExoConstants.DO_ASR_INTERRUPT);
            return;
        }
        if (ExoConstants.FIRE_PASSIVE_INTERRUPT.equals(msg)) {
            LogMgr.d("SimpleSessionManager", "firePassiveInterrupt %s", ctx.handler().getClass().getName());
            ctx.fireUserEventTriggered(ExoConstants.DO_PASSIVE_INTERRUPT);
        } else if (ExoConstants.FIRE_RESUME.equals(msg)) {
            LogMgr.d("SimpleSessionManager", "fireResume %s", ctx.handler().getClass().getName());
            ctx.fireUserEventTriggered(ExoConstants.DO_RESUME);
        } else if (!ExoConstants.FIRE_ENGINE_INIT_DONE.equals(msg)) {
            super.write(ctx, msg);
        } else {
            LogMgr.d("SimpleSessionManager", "fireEngineInitDone %s", ctx.handler().getClass().getName());
            ctx.fireUserEventTriggered(ExoConstants.DO_ENGINE_INIT_DONE);
        }
    }
}
