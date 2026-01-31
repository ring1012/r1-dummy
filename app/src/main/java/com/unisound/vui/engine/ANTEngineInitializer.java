package com.unisound.vui.engine;

import com.unisound.vui.handler.ANTEventDispatcher;
import com.unisound.vui.util.LogMgr;

/* loaded from: classes.dex */
public abstract class ANTEngineInitializer extends ANTEventDispatcher {
    private static final String TAG = "ANTEngineInitializer";

    @Override // com.unisound.vui.engine.ANTHandlerAdapter, com.unisound.vui.engine.ANTHandler
    public void exceptionCaught(ANTHandlerContext ctx, Throwable cause) throws Exception {
        LogMgr.e(TAG, cause + ":Failed to initialize the ant engine");
        ANTPipeline aNTPipelinePipeline = ctx.pipeline();
        if (aNTPipelinePipeline.context(this) != null) {
            aNTPipelinePipeline.remove(this);
        }
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventEngineInitDone(ANTHandlerContext ctx) {
        onEngineInitDone(ctx.engine());
        ctx.pipeline().remove(this);
        return false;
    }

    protected abstract void onEngineInitDone(ANTEngine aNTEngine);
}
