package com.unisound.ant.platform.illeagechat;

import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.handler.SimpleUserEventInboundHandler;
import com.unisound.vui.util.SpeakerTTSUtil;
import nluparser.scheme.NLU;
import nluparser.scheme.SCode;
import nluparser.scheme.SName;

/* loaded from: classes.dex */
public class DefaultIlleageChatHandler extends SimpleUserEventInboundHandler<NLU> {
    private static final String TAG = "DefaultIlleageChatHandler";

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    protected void initPriority() {
        setPriority(300);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    public boolean acceptInboundEvent0(NLU evt) throws Exception {
        return SName.CHAT_ILLEGAL.equals(evt.getService());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    public void eventReceived(NLU evt, ANTHandlerContext ctx) throws Exception {
        super.eventReceived((DefaultIlleageChatHandler) evt, ctx);
        String playContent = SpeakerTTSUtil.getTTSString(R.string.tts_unsupport, -1, ctx.androidContext());
        if ((SCode.ILLEGAL_DIRTY.equals(evt.getCode()) || SCode.ILLEGAL_YELLOW.equals(evt.getCode()) || SCode.ILLEGAL_SENSITIVE.equals(evt.getCode())) && evt.getGeneral() != null) {
            playContent = evt.getGeneral().getText();
        }
        ctx.playTTS(playContent);
        sendFullLogToDeviceCenter(evt, playContent);
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPlayingEnd(ANTHandlerContext ctx) {
        if (this.eventReceived) {
            exitSession();
        }
        return super.onTTSEventPlayingEnd(ctx);
    }

    private void exitSession() {
        reset();
    }
}
