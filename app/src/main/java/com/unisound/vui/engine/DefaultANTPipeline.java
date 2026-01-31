package com.unisound.vui.engine;

import com.unisound.vui.engine.ANTEngine;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.internal.ObjectUtil;
import com.unisound.vui.util.internal.c;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import org.litepal.util.Const;

/* loaded from: classes.dex */
final class DefaultANTPipeline implements ANTPipeline {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final WeakHashMap<Class<?>, String>[] NAME_CACHES;
    private static final String TAG = "DefaultANTPipeline";
    final ANTEngine antEngine;
    final AbstractANTHandlerContext head;
    private final Map<String, ANTHandlerContext> name2ctx = new HashMap(4);
    final AbstractANTHandlerContext tail;

    static final class a extends AbstractANTHandlerContext implements ANTOutboundHandler {

        /* renamed from: a, reason: collision with root package name */
        private static final String f410a = DefaultANTPipeline.generateName0(a.class);
        private final ANTEngine.Unsafe b;

        a(DefaultANTPipeline defaultANTPipeline) {
            super(defaultANTPipeline, false, true, f410a);
            this.b = defaultANTPipeline.engine().unsafe();
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void cancelASR(ANTHandlerContext ctx) throws Exception {
            this.b.cancelASR();
        }

        @Override // com.unisound.vui.engine.AbstractANTHandlerContext, com.unisound.vui.engine.ANTHandlerContext
        public void cancelEngine() {
            this.b.cancelEngine();
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void cancelEngine(ANTHandlerContext ctx) throws Exception {
            this.b.cancelEngine();
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void cancelTTS(ANTHandlerContext ctx) throws Exception {
            this.b.cancelTTS();
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void doPPTAction(ANTHandlerContext ctx) throws Exception {
            this.b.cancelASR();
            this.b.enterASR();
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void enterASR(ANTHandlerContext ctx) throws Exception {
            this.b.enterASR();
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void enterWakeup(ANTHandlerContext ctx, boolean playBeep) throws Exception {
            this.b.enterWakeup(playBeep);
        }

        @Override // com.unisound.vui.engine.ANTHandler
        public void exceptionCaught(ANTHandlerContext ctx, Throwable cause) throws Exception {
            ctx.fireExceptionCaught(cause);
        }

        @Override // com.unisound.vui.engine.ANTHandlerContext
        public ANTHandler handler() {
            return this;
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void initializeMode(ANTHandlerContext ctx) throws Exception {
            this.b.initializeMode();
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void initializeSdk(ANTHandlerContext ctx) throws Exception {
            this.b.initializeSdk();
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void playBuffer(ANTHandlerContext ctx, byte[] data) throws Exception {
            this.b.playBuffer(data);
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void playTTS(ANTHandlerContext ctx, String text) throws Exception {
            this.b.playTTS(text);
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void setWakeupWord(ANTHandlerContext ctx, List<String> wakeup, boolean playBeep) throws Exception {
            this.b.setWakeupWord(wakeup, playBeep);
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void stopASR(ANTHandlerContext ctx) throws Exception {
            this.b.stopASR();
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void stopWakeup(ANTHandlerContext ctx) throws Exception {
            this.b.stopWakeup();
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void updateWakeupWord(ANTHandlerContext ctx, List<String> wakeup) throws Exception {
            this.b.updateWakeupWord(wakeup);
        }

        @Override // com.unisound.vui.engine.ANTOutboundHandler
        public void write(ANTHandlerContext ctx, Object msg) throws Exception {
            this.b.write(msg);
        }
    }

    static final class b extends AbstractANTHandlerContext implements ANTInboundHandler {

        /* renamed from: a, reason: collision with root package name */
        private static final String f411a = DefaultANTPipeline.generateName0(b.class);

        b(DefaultANTPipeline defaultANTPipeline) {
            super(defaultANTPipeline, true, false, f411a);
        }

        @Override // com.unisound.vui.engine.ANTHandler
        public void exceptionCaught(ANTHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            LogMgr.e(DefaultANTPipeline.TAG, "exceptionCaught:" + cause.toString());
        }

        @Override // com.unisound.vui.engine.ANTHandlerContext
        public ANTHandler handler() {
            return this;
        }

        @Override // com.unisound.vui.engine.ANTInboundHandler
        public void onASRError(int type, String error, ANTHandlerContext ctx) throws Exception {
        }

        @Override // com.unisound.vui.engine.ANTInboundHandler
        public void onASREvent(int type, ANTHandlerContext ctx) throws Exception {
        }

        @Override // com.unisound.vui.engine.ANTInboundHandler
        public void onASRResult(int type, String result, ANTHandlerContext ctx) throws Exception {
        }

        @Override // com.unisound.vui.engine.ANTInboundHandler
        public void onNLUError(int type, String error, ANTHandlerContext ctx) throws Exception {
        }

        @Override // com.unisound.vui.engine.ANTInboundHandler
        public void onNLUEvent(int type, ANTHandlerContext ctx) throws Exception {
        }

        @Override // com.unisound.vui.engine.ANTInboundHandler
        public void onNLUResult(int type, String result, ANTHandlerContext ctx) throws Exception {
        }

        @Override // com.unisound.vui.engine.ANTInboundHandler
        public void onTTSError(int type, String error, ANTHandlerContext ctx) throws Exception {
        }

        @Override // com.unisound.vui.engine.ANTInboundHandler
        public void onTTSEvent(int type, ANTHandlerContext ctx) throws Exception {
        }

        @Override // com.unisound.vui.engine.ANTInboundHandler
        public void userEventTriggered(Object evt, ANTHandlerContext ctx) throws Exception {
        }
    }

    static {
        $assertionsDisabled = !DefaultANTPipeline.class.desiredAssertionStatus();
        NAME_CACHES = new WeakHashMap[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < NAME_CACHES.length; i++) {
            NAME_CACHES[i] = new WeakHashMap<>();
        }
    }

    public DefaultANTPipeline(ANTEngine antEngine) {
        if (antEngine == null) {
            throw new NullPointerException("antEngine");
        }
        this.antEngine = antEngine;
        this.head = new a(this);
        this.tail = new b(this);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    private void addAfter0(String name, AbstractANTHandlerContext ctx, AbstractANTHandlerContext newCtx) {
        checkDuplicateName(name);
        newCtx.prev = ctx;
        newCtx.next = ctx.next;
        ctx.next.prev = newCtx;
        ctx.next = newCtx;
        this.name2ctx.put(name, newCtx);
    }

    private void addBefore0(String name, AbstractANTHandlerContext ctx, AbstractANTHandlerContext newCtx) {
        newCtx.prev = ctx.prev;
        newCtx.next = ctx;
        ctx.prev.next = newCtx;
        ctx.prev = newCtx;
        this.name2ctx.put(name, newCtx);
    }

    private void addFirst0(String name, AbstractANTHandlerContext newCtx) {
        AbstractANTHandlerContext abstractANTHandlerContext = this.head.next;
        newCtx.prev = this.head;
        newCtx.next = abstractANTHandlerContext;
        this.head.next = newCtx;
        abstractANTHandlerContext.prev = newCtx;
        this.name2ctx.put(name, newCtx);
    }

    private void addLast0(String name, AbstractANTHandlerContext newCtx) {
        AbstractANTHandlerContext abstractANTHandlerContext = this.tail.prev;
        newCtx.prev = abstractANTHandlerContext;
        newCtx.next = this.tail;
        abstractANTHandlerContext.next = newCtx;
        this.tail.prev = newCtx;
        this.name2ctx.put(name, newCtx);
    }

    private void checkDuplicateName(String name) {
        if (this.name2ctx.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate handler name: " + name);
        }
    }

    private String generateName(ANTHandler handler) {
        String strGenerateName0;
        WeakHashMap<Class<?>, String> weakHashMap = NAME_CACHES[(int) (Thread.currentThread().getId() % NAME_CACHES.length)];
        Class<?> cls = handler.getClass();
        synchronized (weakHashMap) {
            strGenerateName0 = weakHashMap.get(cls);
            if (strGenerateName0 == null) {
                strGenerateName0 = generateName0(cls);
                weakHashMap.put(cls, strGenerateName0);
            }
        }
        synchronized (this) {
            if (this.name2ctx.containsKey(strGenerateName0)) {
                String strSubstring = strGenerateName0.substring(0, strGenerateName0.length() - 1);
                int i = 1;
                while (true) {
                    int i2 = i;
                    strGenerateName0 = strSubstring + i2;
                    if (!this.name2ctx.containsKey(strGenerateName0)) {
                        break;
                    }
                    i = i2 + 1;
                }
            }
        }
        return strGenerateName0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String generateName0(Class<?> handlerType) {
        return c.a(handlerType) + "#0";
    }

    private AbstractANTHandlerContext getContextOrDie(ANTHandler handler) {
        AbstractANTHandlerContext abstractANTHandlerContext = (AbstractANTHandlerContext) context(handler);
        if (abstractANTHandlerContext == null) {
            throw new NoSuchElementException(handler.getClass().getName());
        }
        return abstractANTHandlerContext;
    }

    private AbstractANTHandlerContext getContextOrDie(String name) {
        AbstractANTHandlerContext abstractANTHandlerContext = (AbstractANTHandlerContext) context(name);
        if (abstractANTHandlerContext == null) {
            throw new NoSuchElementException(name);
        }
        return abstractANTHandlerContext;
    }

    private AbstractANTHandlerContext remove(AbstractANTHandlerContext ctx) {
        if (!$assertionsDisabled && (ctx == this.head || ctx == this.tail)) {
            throw new AssertionError();
        }
        synchronized (this) {
            remove0(ctx);
        }
        return ctx;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline addAfter(String baseName, String name, ANTHandler handler) {
        synchronized (this) {
            AbstractANTHandlerContext contextOrDie = getContextOrDie(baseName);
            checkDuplicateName(baseName);
            addAfter0(name, contextOrDie, new DefaultANTHandlerContext(this, name, handler));
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline addBefore(String baseName, String name, ANTHandler handler) {
        synchronized (this) {
            AbstractANTHandlerContext contextOrDie = getContextOrDie(baseName);
            checkDuplicateName(name);
            addBefore0(name, contextOrDie, new DefaultANTHandlerContext(this, name, handler));
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline addFirst(String name, ANTHandler handler) {
        synchronized (this) {
            checkDuplicateName(name);
            addFirst0(name, new DefaultANTHandlerContext(this, name, handler));
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline addFirst(ANTHandler... handlers) {
        ObjectUtil.checkNotNull(handlers, "handlers");
        if (handlers.length != 0 && handlers[0] != null) {
            int i = 1;
            while (i < handlers.length && handlers[i] != null) {
                i++;
            }
            for (int i2 = i - 1; i2 >= 0; i2--) {
                ANTHandler aNTHandler = handlers[i2];
                addFirst(generateName(aNTHandler), aNTHandler);
            }
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline addLast(String name, ANTHandler handler) {
        synchronized (this) {
            checkDuplicateName(name);
            addLast0(name, new DefaultANTHandlerContext(this, name, handler));
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline addLast(ANTHandler... handlers) {
        ObjectUtil.checkNotNull(handlers, "handlers");
        for (ANTHandler aNTHandler : handlers) {
            if (aNTHandler == null) {
                break;
            }
            addLast(generateName(aNTHandler), aNTHandler);
        }
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void cancelASR() {
        this.tail.cancelASR();
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void cancelTTS() {
        this.tail.cancelTTS();
    }

    public void clearPipeline() {
        for (AbstractANTHandlerContext abstractANTHandlerContext = this.tail.prev; abstractANTHandlerContext != this.head; abstractANTHandlerContext = abstractANTHandlerContext.prev) {
        }
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTHandlerContext context(ANTHandler handler) {
        ObjectUtil.checkNotNull(handler, "handler");
        for (AbstractANTHandlerContext abstractANTHandlerContext = this.head.next; abstractANTHandlerContext != null; abstractANTHandlerContext = abstractANTHandlerContext.next) {
            if (abstractANTHandlerContext.handler() == handler) {
                return abstractANTHandlerContext;
            }
        }
        return null;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTHandlerContext context(String name) {
        ANTHandlerContext aNTHandlerContext;
        ObjectUtil.checkNotNull(name, Const.TableSchema.COLUMN_NAME);
        synchronized (this) {
            aNTHandlerContext = this.name2ctx.get(name);
        }
        return aNTHandlerContext;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void doPPTAction() {
        this.tail.doPPTAction();
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTEngine engine() {
        return this.antEngine;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void enterASR() {
        this.tail.enterASR();
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void enterWakeup(boolean playBeep) {
        this.tail.enterWakeup(playBeep);
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline fireASRError(int type, String error) {
        this.head.fireASRError(type, error);
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline fireASREvent(int type) {
        this.head.fireASREvent(type);
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline fireASRResult(int type, String result) {
        this.head.fireASRResult(type, result);
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline fireNLUError(int type, String error) {
        this.head.fireNLUError(type, error);
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline fireNLUEvent(int type) {
        this.head.fireNLUEvent(type);
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline fireNLUResult(int type, String result) {
        this.head.fireNLUResult(type, result);
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline fireTTSError(int type, String error) {
        this.head.fireTTSError(type, error);
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline fireTTSEvent(int type) {
        this.head.fireTTSEvent(type);
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline fireUserEventTriggered(Object event) {
        this.head.fireUserEventTriggered(event);
        return this;
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void initializeMode() {
        this.tail.initializeMode();
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void initializeSdk() {
        this.tail.initializeSdk();
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void playTTS(String text) {
        this.tail.playTTS(text);
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTHandler remove(String name) {
        return remove(getContextOrDie(name)).handler();
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public ANTPipeline remove(ANTHandler handler) {
        remove(getContextOrDie(handler));
        return this;
    }

    void remove0(AbstractANTHandlerContext ctx) {
        AbstractANTHandlerContext abstractANTHandlerContext = ctx.prev;
        AbstractANTHandlerContext abstractANTHandlerContext2 = ctx.next;
        abstractANTHandlerContext.next = abstractANTHandlerContext2;
        abstractANTHandlerContext2.prev = abstractANTHandlerContext;
        this.name2ctx.remove(ctx.name());
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void setWakeupWord(List<String> wakeup, boolean playBeep) {
        this.tail.setWakeupWord(wakeup, playBeep);
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void stopASR() {
        this.tail.stopASR();
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void stopWakeup() {
        this.tail.stopWakeup();
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void updateWakeupWord(List<String> wakeup) {
        this.tail.updateWakeupWord(wakeup);
    }

    @Override // com.unisound.vui.engine.ANTPipeline
    public void write(Object msg) {
        this.tail.write(msg);
    }
}
