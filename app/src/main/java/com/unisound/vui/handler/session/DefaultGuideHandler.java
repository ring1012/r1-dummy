package com.unisound.vui.handler.session;

import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.handler.SimpleUserEventInboundHandler;
import nluparser.scheme.NLU;
import nluparser.scheme.SName;

/* loaded from: classes.dex */
public class DefaultGuideHandler extends SimpleUserEventInboundHandler<NLU> {
    private void a() {
        this.ctx.pipeline().fireASRResult(3201, "{\"local_asr\":[{\"engine_mode\":\"wakeup\",\"result_type\":\"full\",\"recognition_result\":\"  小讯小讯   \",\"score\":6.08,\"utteranceTime\":1230,\"outRecordingTime\":212210,\"delayTime\":280}]}");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void eventReceived(NLU nlu, ANTHandlerContext aNTHandlerContext) throws Exception {
        super.eventReceived(nlu, aNTHandlerContext);
        a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public boolean acceptInboundEvent0(NLU nlu) throws Exception {
        return SName.WAKEUP_WORD.equals(nlu.getService());
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    protected void initPriority() {
        setPriority(300);
    }
}
