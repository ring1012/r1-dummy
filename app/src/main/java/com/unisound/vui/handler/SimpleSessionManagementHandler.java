package com.unisound.vui.handler;

import com.unisound.vui.engine.ANTDuplexHandler;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.util.ExoConstants;
import com.unisound.vui.util.LogMgr;

/* loaded from: classes.dex */
public abstract class SimpleSessionManagementHandler extends ANTDuplexHandler {
    private static final String TAG = "SimpleSessionManagementHandler";

    protected void doInterrupt(ANTHandlerContext ctx, String interruptType) {
    }

    protected void doResume(ANTHandlerContext ctx) {
    }

    protected void fireActiveInterrupt(ANTHandlerContext ctx) {
        LogMgr.d(TAG, "fireActiveInterrupt:" + getClass().getSimpleName());
        ctx.write(ExoConstants.FIRE_ACTIVE_INTERRUPT);
    }

    protected void fireAsrInterrupt(ANTHandlerContext ctx) {
        LogMgr.d(TAG, "fireAsrInterrupt:" + getClass().getSimpleName());
        ctx.write(ExoConstants.FIRE_ASR_INTERRUPT);
    }

    protected void fireEngineInitDone(ANTHandlerContext ctx) {
        ctx.write(ExoConstants.FIRE_ENGINE_INIT_DONE);
    }

    protected void fireOneShotInterrupt(ANTHandlerContext ctx) {
        LogMgr.d(TAG, "fireOneShotInterrupt:" + getClass().getSimpleName());
        ctx.write(ExoConstants.FIRE_ONE_SHOT_INTERRUPT);
    }

    protected void firePassiveInterrupt(ANTHandlerContext ctx) {
        LogMgr.d(TAG, "firePassiveInterrupt:" + getClass().getSimpleName());
        ctx.write(ExoConstants.FIRE_PASSIVE_INTERRUPT);
    }

    protected void fireResume(ANTHandlerContext ctx) {
        LogMgr.d(TAG, "fireResume:" + getClass().getSimpleName());
        ctx.write(ExoConstants.FIRE_RESUME);
    }

    protected void manageState(Object evt, ANTHandlerContext ctx) {
        if (ExoConstants.DO_ACTIVE_INTERRUPT.equals(evt)) {
            doInterrupt(ctx, ExoConstants.DO_ACTIVE_INTERRUPT);
        } else if (ExoConstants.DO_PASSIVE_INTERRUPT.equals(evt)) {
            doInterrupt(ctx, ExoConstants.DO_PASSIVE_INTERRUPT);
        } else if (ExoConstants.DO_ASR_INTERRUPT.equals(evt)) {
            doInterrupt(ctx, ExoConstants.DO_ASR_INTERRUPT);
        } else if (ExoConstants.DO_ONE_SHOT_INTERRUPT.equals(evt)) {
            doInterrupt(ctx, ExoConstants.DO_ONE_SHOT_INTERRUPT);
        } else if (ExoConstants.DO_FINISH_ALL_INTERRUPT.equals(evt)) {
            doInterrupt(ctx, ExoConstants.DO_FINISH_ALL_INTERRUPT);
        }
        if (ExoConstants.DO_RESUME.equals(evt)) {
            doResume(ctx);
        }
    }

    protected void onEngineInitDone(ANTHandlerContext ctx) {
    }
}
