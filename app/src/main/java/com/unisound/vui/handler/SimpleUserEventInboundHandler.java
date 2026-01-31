package com.unisound.vui.handler;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.util.ExoConstants;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.NaviConfig;
import com.unisound.vui.util.UserPerferenceUtil;
import com.unisound.vui.util.internal.e;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import logreport.FullLog;
import nluparser.scheme.LocalASR;
import nluparser.scheme.NLU;

/* loaded from: classes.dex */
public abstract class SimpleUserEventInboundHandler<I> extends SimpleSessionManagementHandler {
    private static final String TAG = "SimpleUserEvent";
    private static final int WAKEUP_TIMEOUT_MESSAGE = 0;
    private static Set<SimpleUserEventInboundHandler> activeHandlers = new HashSet();
    protected ANTHandlerContext ctx;
    protected int errorTime;
    protected boolean eventReceived;
    private Handler handler;
    private final e matcher;
    protected boolean playCancelTTS;
    private int priority;
    protected String sessionName;
    protected boolean shouldResume;

    protected SimpleUserEventInboundHandler() {
        this.playCancelTTS = false;
        this.shouldResume = false;
        this.matcher = e.a(this, SimpleUserEventInboundHandler.class, "I");
        initPriority();
        initHandler();
    }

    protected SimpleUserEventInboundHandler(Class<? extends I> parserResultType) {
        this.playCancelTTS = false;
        this.shouldResume = false;
        this.matcher = e.a((Class<?>) parserResultType);
        initPriority();
        initHandler();
    }

    public static boolean hasActiveHandlers() {
        if (activeHandlers == null || activeHandlers.size() <= 0) {
            return false;
        }
        Iterator<SimpleUserEventInboundHandler> it = activeHandlers.iterator();
        while (it.hasNext()) {
            LogMgr.d(TAG, "active handler:" + it.next().getClass().getSimpleName());
        }
        return true;
    }

    public boolean acceptInboundEvent(Object evt) throws Exception {
        return this.matcher.a(evt);
    }

    protected boolean acceptInboundEvent0(I evt) throws Exception {
        return false;
    }

    public void cancelAsrEventTriggered(LocalASR localASR, ANTHandlerContext ctx) {
    }

    protected void eventReceived(I evt, ANTHandlerContext ctx) throws Exception {
        setDomainWakeUpWordList(ctx);
    }

    protected int getErrorTime() {
        return this.errorTime;
    }

    protected String getInterruptType(I evt) {
        return ExoConstants.DO_ACTIVE_INTERRUPT;
    }

    public int getPriority() {
        return this.priority;
    }

    protected void increaseErrorTime() {
        this.errorTime++;
    }

    protected void initHandler() {
        this.handler = new Handler() { // from class: com.unisound.vui.handler.SimpleUserEventInboundHandler.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        SimpleUserEventInboundHandler.this.onWakeupTimeOut();
                        break;
                }
            }
        };
    }

    protected abstract void initPriority();

    public boolean localAsrEventTriggered(LocalASR localASR, ANTHandlerContext ctx) {
        return false;
    }

    protected void mark(boolean eventReceived) {
        LogMgr.d(TAG, "mark true,hanler:" + getClass().getSimpleName());
        if (this.eventReceived) {
            return;
        }
        this.eventReceived = eventReceived;
        activeHandlers.add(this);
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventCancel(ANTHandlerContext ctx) {
        return super.onASREventCancel(ctx);
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventEngineInitDone(ANTHandlerContext ctx) {
        LogMgr.d(TAG, "onASREventEngineInitDone:" + getClass().getSimpleName());
        this.ctx = ctx;
        NaviConfig.setContext(ctx.androidContext());
        return super.onASREventEngineInitDone(ctx);
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventRecordingStart(ANTHandlerContext ctx) {
        return super.onASREventRecordingStart(ctx);
    }

    protected void onDestroy(ANTHandlerContext ctx) {
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void onTTSError(int type, String error, ANTHandlerContext ctx) throws Exception {
        if (this.eventReceived) {
            reset();
            ctx.enterWakeup(false);
        }
        ctx.fireTTSError(type, error);
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPlayingEnd(ANTHandlerContext ctx) {
        LogMgr.d(TAG, "onTTSEventPlayingEnd:" + getClass().getSimpleName());
        return super.onTTSEventPlayingEnd(ctx);
    }

    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupEventSetWakeupwordDone(ANTHandlerContext ctx) {
        LogMgr.d(TAG, "onWakeupEventSetWakeupwordDone:" + getClass().getSimpleName());
        return super.onWakeupEventSetWakeupwordDone(ctx);
    }

    protected void onWakeupTimeOut() {
        LogMgr.i(TAG, "-onWakeupTimeOut-2");
    }

    protected void removeTimeoutMessage() {
        if (this.handler.hasMessages(0)) {
            this.handler.removeMessages(0);
        }
    }

    protected void reset() {
        LogMgr.d(TAG, "reset,hanler:" + getClass().getSimpleName());
        if (this.eventReceived) {
            this.eventReceived = false;
            activeHandlers.remove(this);
        }
        resetErrorTime();
    }

    protected void resetErrorTime() {
        LogMgr.d(TAG, "-resetErrorTime-");
        this.errorTime = 0;
    }

    public void sendFullLogToDeviceCenter(NLU evt, String ttsData) {
        this.ctx.pipeline().fireUserEventTriggered(new FullLog(evt, ttsData));
    }

    public void sendTimeoutMessage(int time) {
        this.handler.removeMessages(0);
        this.handler.sendEmptyMessageDelayed(0, time);
    }

    protected void setDomainWakeUpWordList(ANTHandlerContext ctx) {
        ctx.setWakeupWord(UserPerferenceUtil.getWakeupWord(ctx.androidContext()), false);
    }

    protected void setPriority(int priority) {
        this.priority = priority;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void userEventTriggered(Object obj, ANTHandlerContext ctx) throws Exception {
        if (acceptInboundEvent(obj) && acceptInboundEvent0(obj)) {
            if (!this.eventReceived) {
                ctx.cancelTTS();
                ctx.cancelEngine();
                String interruptType = getInterruptType(obj);
                if (!TextUtils.isEmpty(interruptType)) {
                    ctx.pipeline().fireUserEventTriggered(interruptType);
                }
                mark(true);
            }
            eventReceived(obj, ctx);
            return;
        }
        if (obj instanceof ExoConstants.a) {
            onDestroy(ctx);
        } else if (!(obj instanceof LocalASR)) {
            manageState(obj, ctx);
        } else if (UserPerferenceUtil.getGlobalCancelWakeupWord(ctx.androidContext()).contains(((LocalASR) obj).getRecognitionResult())) {
            cancelAsrEventTriggered((LocalASR) obj, ctx);
        } else if (localAsrEventTriggered((LocalASR) obj, ctx)) {
            return;
        }
        ctx.fireUserEventTriggered(obj);
    }
}
