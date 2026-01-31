package com.unisound.vui.handler.session.light;

import android.content.Context;
import com.unisound.ant.device.event.TurnOffWakeLightEvent;
import com.unisound.ant.device.netmodule.NetChangeReceiver;
import com.unisound.vui.engine.ANTEngineOption;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.handler.SimpleSessionManagementHandler;
import com.unisound.vui.util.JsonTool;
import com.unisound.vui.util.ThreadUtils;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DefaultLightsHandler extends SimpleSessionManagementHandler implements NetChangeReceiver.NetStateListener {
    private static final String TAG = DefaultLightsHandler.class.getSimpleName();
    private LightListener mLightListener;
    private NetChangeReceiver mNetChangeReceiver = new NetChangeReceiver();

    public DefaultLightsHandler(LightListener lightListener, Context context) {
        this.mLightListener = lightListener;
        this.mNetChangeReceiver.setStateListener(this);
        this.mNetChangeReceiver.registerNetReceiverAndCheck(context);
    }

    @Override // com.unisound.vui.handler.SimpleSessionManagementHandler
    protected void doInterrupt(ANTHandlerContext ctx, String interruptType) {
        this.mLightListener.onInterrupt();
        super.doInterrupt(ctx, interruptType);
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupResult(final ANTHandlerContext ctx, String result) {
        ThreadUtils.execute(new Runnable() { // from class: com.unisound.vui.handler.session.light.DefaultLightsHandler.1
            @Override // java.lang.Runnable
            public void run() {
                int angle = ((Integer) ctx.engine().config().getOption(ANTEngineOption.ASR_FOURMIC_DOA_RESULT)).intValue();
                DefaultLightsHandler.this.mLightListener.onWakeupSuccess(angle);
            }
        });
        return false;
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void userEventTriggered(Object evt, ANTHandlerContext ctx) throws Exception {
        super.userEventTriggered(evt, ctx);
        if (evt instanceof TurnOffWakeLightEvent) {
            this.mLightListener.onInterrupt();
        }
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventRecordingStop(ANTHandlerContext ctx) {
        this.mLightListener.onRecognizeStart();
        return super.onASREventRecordingStop(ctx);
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventRecognitionEnd(ANTHandlerContext ctx) {
        this.mLightListener.onRecognizeStop();
        return super.onASREventRecognitionEnd(ctx);
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onASRError(int type, String error, ANTHandlerContext ctx) throws Exception {
        JSONObject mError = JsonTool.parseToJSONObject(error);
        String mErrorCode = JsonTool.getJsonValue(mError, "errorCode");
        if (!"-90002".equals(mErrorCode) && !"-90005".equals(mErrorCode)) {
            this.mLightListener.onRecognizeStop();
        }
        super.onASRError(type, error, ctx);
    }

    @Override // com.unisound.ant.device.netmodule.NetChangeReceiver.NetStateListener
    public void onNetConnecting() {
        this.mLightListener.onNetConnected();
    }

    @Override // com.unisound.ant.device.netmodule.NetChangeReceiver.NetStateListener
    public void onNetConnected() {
        this.mLightListener.onNetDisconnected();
    }

    @Override // com.unisound.ant.device.netmodule.NetChangeReceiver.NetStateListener
    public void onNetDisconnected() {
        this.mLightListener.onNetDisconnected();
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPlayingEnd(ANTHandlerContext ctx) {
        this.mLightListener.onTTSEnd();
        return false;
    }
}
