package com.phicomm.speaker.device.custom.handler;

import com.phicomm.speaker.device.R;
import com.phicomm.speaker.device.custom.status.PhicommDeviceStatusProcessor;
import com.phicomm.speaker.device.utils.MediaPlayerUtils;
import com.unisound.vui.common.network.NetUtil;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.handler.ANTEventDispatcher;
import com.unisound.vui.handler.session.light.LightListener;
import com.unisound.vui.util.ExoConstants;
import com.unisound.vui.util.UserPerferenceUtil;

/* loaded from: classes.dex */
public class PhicommInterceptHandler extends ANTEventDispatcher {
    private LightListener mLightListener;
    private boolean playTTS = false;

    public PhicommInterceptHandler(LightListener lightListener) {
        this.mLightListener = lightListener;
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupResult(ANTHandlerContext ctx, String result) throws IllegalStateException {
        if (PhicommDeviceStatusProcessor.getInstance().getDeviceStatus() == 3) {
            return false;
        }
        if (!NetUtil.isNetworkConnected(ctx.androidContext())) {
            ctx.fireUserEventTriggered(ExoConstants.DO_FINISH_ALL_INTERRUPT);
            this.playTTS = true;
            this.mLightListener.onRecognizeStart();
            ctx.playTTS(ctx.androidContext().getString(R.string.tts_net_is_weak));
            ctx.enterWakeup(false);
            return true;
        }
        if (!UserPerferenceUtil.getDeviceBindState(ctx.androidContext())) {
            ctx.fireUserEventTriggered(ExoConstants.DO_FINISH_ALL_INTERRUPT);
            this.playTTS = true;
            this.mLightListener.onRecognizeStart();
            ctx.playTTS(ctx.androidContext().getString(R.string.tts_not_bind));
            ctx.enterWakeup(false);
            return true;
        }
        if (!MediaPlayerUtils.getInstance().isPlaying()) {
            return false;
        }
        MediaPlayerUtils.getInstance().stop();
        return true;
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPlayingEnd(ANTHandlerContext ctx) {
        if (this.playTTS) {
            this.mLightListener.onRecognizeStop();
            this.playTTS = false;
        }
        return super.onTTSEventPlayingEnd(ctx);
    }
}
