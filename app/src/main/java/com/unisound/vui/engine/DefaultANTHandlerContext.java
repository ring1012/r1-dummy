package com.unisound.vui.engine;

import com.unisound.vui.util.internal.ObjectUtil;

/* loaded from: classes.dex */
final class DefaultANTHandlerContext extends AbstractANTHandlerContext {
    private final ANTHandler handler;

    DefaultANTHandlerContext(DefaultANTPipeline pipeline, String name, ANTHandler handler) {
        super(pipeline, isInbound(handler), isOutbound(handler), name);
        ObjectUtil.checkNotNull(handler, "handler");
        this.handler = handler;
    }

    private static boolean isInbound(ANTHandler handler) {
        return handler instanceof ANTInboundHandler;
    }

    private static boolean isOutbound(ANTHandler handler) {
        return handler instanceof ANTOutboundHandler;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTHandler handler() {
        return this.handler;
    }
}
