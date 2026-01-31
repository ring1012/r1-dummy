package com.unisound.vui.engine;

import android.content.Context;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.d;
import com.unisound.vui.util.internal.ObjectUtil;
import java.util.List;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public abstract class AbstractANTHandlerContext extends d implements ANTHandlerContext {
    private static final String TAG = "AbstractANTHandlerContext";
    private final ANTEngine antEngine;
    private final boolean inbound;
    private final String name;
    protected AbstractANTHandlerContext next;
    private final boolean outbound;
    private final DefaultANTPipeline pipeline;
    protected AbstractANTHandlerContext prev;

    AbstractANTHandlerContext(DefaultANTPipeline pipeline, boolean inbound, boolean outbound, String name) {
        ObjectUtil.checkNotNull(name, Const.TableSchema.COLUMN_NAME);
        this.antEngine = pipeline.antEngine;
        this.pipeline = pipeline;
        this.name = name;
        this.inbound = inbound;
        this.outbound = outbound;
    }

    private AbstractANTHandlerContext findNextContextInbound() {
        do {
            this = this.next;
        } while (!this.inbound);
        return this;
    }

    private AbstractANTHandlerContext findPreContextOutbound() {
        do {
            this = this.prev;
        } while (!this.outbound);
        return this;
    }

    private static boolean inExceptionCaught(Throwable cause) {
        do {
            StackTraceElement[] stackTrace = cause.getStackTrace();
            if (stackTrace != null) {
                for (StackTraceElement stackTraceElement : stackTrace) {
                    if (stackTraceElement == null) {
                        break;
                    }
                    if ("exceptionCaught".equals(stackTraceElement.getMethodName())) {
                        return true;
                    }
                }
            }
            cause = cause.getCause();
        } while (cause != null);
        return false;
    }

    private void invokeExceptionCaught(Throwable cause) {
        try {
            handler().exceptionCaught(this, cause);
        } catch (Throwable th) {
            LogMgr.e(TAG, cause + "An exception was thrown by a user handler's exceptionCaught() method while handling the following exception:");
        }
    }

    private void notifyHandlerException(Throwable cause) {
        if (inExceptionCaught(cause)) {
            LogMgr.e(TAG, cause + "An exception was thrown by a user handler while handling an exceptionCaught event");
        } else {
            invokeExceptionCaught(cause);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public Context androidContext() {
        return this.antEngine.androidContext();
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void cancelASR() {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).cancelASR(abstractANTHandlerContextFindPreContextOutbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void cancelEngine() {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).cancelEngine(abstractANTHandlerContextFindPreContextOutbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void cancelTTS() {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).cancelTTS(abstractANTHandlerContextFindPreContextOutbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void doPPTAction() {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).doPPTAction(abstractANTHandlerContextFindPreContextOutbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTEngine engine() {
        return this.antEngine;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void enterASR() {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).enterASR(abstractANTHandlerContextFindPreContextOutbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void enterWakeup(boolean playBeep) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).enterWakeup(abstractANTHandlerContextFindPreContextOutbound, playBeep);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTHandlerContext fireASRError(int type, String error) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindNextContextInbound = findNextContextInbound();
            ((ANTInboundHandler) abstractANTHandlerContextFindNextContextInbound.handler()).onASRError(type, error, abstractANTHandlerContextFindNextContextInbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTHandlerContext fireASREvent(int type) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindNextContextInbound = findNextContextInbound();
            ((ANTInboundHandler) abstractANTHandlerContextFindNextContextInbound.handler()).onASREvent(type, abstractANTHandlerContextFindNextContextInbound);
        } catch (Throwable th) {
            th.printStackTrace();
            notifyHandlerException(th);
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTHandlerContext fireASRResult(int type, String result) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindNextContextInbound = findNextContextInbound();
            ((ANTInboundHandler) abstractANTHandlerContextFindNextContextInbound.handler()).onASRResult(type, result, abstractANTHandlerContextFindNextContextInbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTHandlerContext fireExceptionCaught(Throwable cause) {
        if (cause != null) {
            ObjectUtil.checkNotNull(cause, "cause");
            this.next.invokeExceptionCaught(cause);
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTHandlerContext fireNLUError(int type, String error) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindNextContextInbound = findNextContextInbound();
            ((ANTInboundHandler) abstractANTHandlerContextFindNextContextInbound.handler()).onNLUError(type, error, abstractANTHandlerContextFindNextContextInbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTHandlerContext fireNLUEvent(int type) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindNextContextInbound = findNextContextInbound();
            ((ANTInboundHandler) abstractANTHandlerContextFindNextContextInbound.handler()).onNLUEvent(type, abstractANTHandlerContextFindNextContextInbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTHandlerContext fireNLUResult(int type, String result) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindNextContextInbound = findNextContextInbound();
            ((ANTInboundHandler) abstractANTHandlerContextFindNextContextInbound.handler()).onNLUResult(type, result, abstractANTHandlerContextFindNextContextInbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTHandlerContext fireTTSError(int type, String error) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindNextContextInbound = findNextContextInbound();
            ((ANTInboundHandler) abstractANTHandlerContextFindNextContextInbound.handler()).onTTSError(type, error, abstractANTHandlerContextFindNextContextInbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTHandlerContext fireTTSEvent(int type) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindNextContextInbound = findNextContextInbound();
            ((ANTInboundHandler) abstractANTHandlerContextFindNextContextInbound.handler()).onTTSEvent(type, abstractANTHandlerContextFindNextContextInbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTHandlerContext fireUserEventTriggered(Object event) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindNextContextInbound = findNextContextInbound();
            ((ANTInboundHandler) abstractANTHandlerContextFindNextContextInbound.handler()).userEventTriggered(event, abstractANTHandlerContextFindNextContextInbound);
        } catch (Exception e) {
            e.printStackTrace();
            notifyHandlerException(e);
        }
        return this;
    }

    public AbstractANTHandlerContext getNext() {
        return this.next;
    }

    public AbstractANTHandlerContext getPrev() {
        return this.prev;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void initializeMode() {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).initializeMode(abstractANTHandlerContextFindPreContextOutbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void initializeSdk() {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).initializeSdk(abstractANTHandlerContextFindPreContextOutbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public String name() {
        return this.name;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public ANTPipeline pipeline() {
        return this.pipeline;
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void playBuffer(byte[] data) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).playBuffer(abstractANTHandlerContextFindPreContextOutbound, data);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void playTTS(String text) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).playTTS(abstractANTHandlerContextFindPreContextOutbound, text);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void setWakeupWord(List<String> wakeup, boolean playBeep) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).setWakeupWord(abstractANTHandlerContextFindPreContextOutbound, wakeup, playBeep);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void stopASR() {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).stopASR(abstractANTHandlerContextFindPreContextOutbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void stopWakeup() {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).stopWakeup(abstractANTHandlerContextFindPreContextOutbound);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void updateWakeupWord(List<String> wakeup) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).updateWakeupWord(abstractANTHandlerContextFindPreContextOutbound, wakeup);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }

    @Override // com.unisound.vui.engine.ANTHandlerContext
    public void write(Object msg) {
        try {
            AbstractANTHandlerContext abstractANTHandlerContextFindPreContextOutbound = findPreContextOutbound();
            ((ANTOutboundHandler) abstractANTHandlerContextFindPreContextOutbound.handler()).write(abstractANTHandlerContextFindPreContextOutbound, msg);
        } catch (Throwable th) {
            notifyHandlerException(th);
        }
    }
}
