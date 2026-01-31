package com.unisound.vui.handler.filter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import com.google.gson.reflect.TypeToken;
import com.unisound.vui.common.config.ANTConfigPreference;
import com.unisound.vui.common.network.NetUtil;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.handler.ANTEventDispatcher;
import com.unisound.vui.transport.R;
import com.unisound.vui.util.AttributeKey;
import com.unisound.vui.util.JsonTool;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.UserPerferenceUtil;
import com.unisound.vui.util.e;
import java.util.List;
import nluparser.MixtureProcessor;
import nluparser.NluProcessor;
import nluparser.scheme.Intent;
import nluparser.scheme.LocalASR;
import nluparser.scheme.Mixture;
import nluparser.scheme.NLU;
import nluparser.scheme.Result;
import nluparser.scheme.SName;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class NLUDispatcher extends ANTEventDispatcher {

    /* renamed from: a, reason: collision with root package name */
    private boolean f423a;
    private boolean b;
    private final MixtureProcessor c;
    private Runnable f;
    private Handler e = new Handler();
    private NluProcessor d = new NluProcessor.Builder().registerTypeMapper(SName.ERROR_REPORT, new TypeToken<NLU<Intent.NullIntent, Result.NullResult>>() { // from class: com.unisound.vui.handler.filter.NLUDispatcher.1
    }.getType()).build();

    public NLUDispatcher(MixtureProcessor mixtureProcessor) {
        this.c = mixtureProcessor;
    }

    private void a() {
        if (this.f != null) {
            LogMgr.d("NLUDispatcher", "stop asr or nlu result timeout task");
            this.e.removeCallbacks(this.f);
            this.f = null;
        }
    }

    private void a(ANTHandlerContext aNTHandlerContext, boolean z) {
        aNTHandlerContext.engine().attr(AttributeKey.valueOf(NLUDispatcher.class, "RECOGNITION_HANDLED")).set(Boolean.valueOf(z));
    }

    private boolean a(float f) {
        return f > ANTConfigPreference.FUNCTION_WAKEUP_BENCHMARK;
    }

    private boolean a(Context context, String str) {
        return UserPerferenceUtil.getCmopetitionWord(context).contains(str);
    }

    private boolean a(ANTHandlerContext aNTHandlerContext) {
        if (b(aNTHandlerContext)) {
            this.f423a = true;
            NLU nluC = c("bluetooth_error");
            nluC.setText(aNTHandlerContext.androidContext().getString(R.string.tts_music_change_no_supported));
            c(aNTHandlerContext, nluC);
            return true;
        }
        if (c(aNTHandlerContext) && !this.b) {
            return false;
        }
        this.f423a = true;
        NLU nluC2 = c("-90002");
        nluC2.setText("no network");
        c(aNTHandlerContext, nluC2);
        return true;
    }

    private boolean a(ANTHandlerContext aNTHandlerContext, String str, Mixture<Intent, Result> mixture) {
        NLU nlu = (NLU) mixture.getNluList().get(0);
        if (a(mixture, nlu)) {
            LogMgr.d("NLUDispatcher", "handleNetFilterService return filter service ...");
            return false;
        }
        this.f423a = true;
        nlu.setLocalNLU(false);
        nlu.setAsrResult(str);
        b(aNTHandlerContext, nlu);
        return true;
    }

    private boolean a(ANTHandlerContext aNTHandlerContext, String str, NLU nlu) {
        if (a(aNTHandlerContext, nlu)) {
            return false;
        }
        this.f423a = true;
        nlu.setLocalNLU(true);
        nlu.setAsrResult(str);
        c(aNTHandlerContext, nlu);
        return true;
    }

    private boolean a(ANTHandlerContext aNTHandlerContext, LocalASR localASR) {
        aNTHandlerContext.fireUserEventTriggered(localASR);
        return true;
    }

    private boolean a(ANTHandlerContext aNTHandlerContext, NLU nlu) {
        return (!a(nlu.getService()) || this.b || b(aNTHandlerContext)) ? false : true;
    }

    private boolean a(String str) {
        return a.a(str);
    }

    private boolean a(LocalASR localASR, NLU nlu) {
        return nlu.getResponseCode() == 0 && localASR.getScore() > ANTConfigPreference.recognizerScore;
    }

    private boolean a(Mixture<Intent, Result> mixture) {
        return mixture.getNetASRList() != null && "full".equals(mixture.getNetASRList().get(0).getResultType());
    }

    private boolean a(Mixture<Intent, Result> mixture, NLU nlu) {
        return a(nlu.getService()) && !a(mixture);
    }

    private void b(ANTHandlerContext aNTHandlerContext, NLU nlu) {
        LogMgr.d("NLUDispatcher", "preHandleNetNlu nlu :" + nlu);
        if (TextUtils.isEmpty(nlu.getText())) {
            c(aNTHandlerContext, c("-63551"));
        } else {
            c(aNTHandlerContext, nlu);
        }
    }

    private boolean b(float f) {
        return f > ANTConfigPreference.effectWakeupBenchmark;
    }

    private boolean b(Context context, String str) {
        return !UserPerferenceUtil.getMainWakeupWord(context).contains(str);
    }

    private boolean b(ANTHandlerContext aNTHandlerContext) {
        return ((Integer) aNTHandlerContext.engine().unsafe().getOption(1001)).intValue() == 2;
    }

    private boolean b(String str) {
        return e.a(str);
    }

    private boolean b(Mixture<Intent, Result> mixture) {
        List<NLU<I, R>> nluList = mixture.getNluList();
        return (nluList == 0 || nluList.size() == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public NLU c(String str) {
        NLU nlu = new NLU();
        nlu.setService(SName.ERROR_REPORT);
        nlu.setCode(str);
        return nlu;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(ANTHandlerContext aNTHandlerContext, NLU nlu) {
        a();
        aNTHandlerContext.pipeline().fireASREvent(1102);
        a(aNTHandlerContext, true);
        aNTHandlerContext.cancelEngine();
        aNTHandlerContext.fireUserEventTriggered(nlu);
        this.b = false;
        this.f423a = false;
    }

    private boolean c(Context context, String str) {
        return UserPerferenceUtil.getMainWakeupWord(context).contains(str);
    }

    private boolean c(ANTHandlerContext aNTHandlerContext) {
        return NetUtil.isNetworkConnected(aNTHandlerContext.androidContext());
    }

    private void d(final ANTHandlerContext aNTHandlerContext) {
        if (this.f == null) {
            LogMgr.d("NLUDispatcher", "start asr or nlu result timeout task");
            this.f = new Runnable() { // from class: com.unisound.vui.handler.filter.NLUDispatcher.2
                @Override // java.lang.Runnable
                public void run() {
                    LogMgr.d("NLUDispatcher", "process asr or nlu result timeout, dispatch network error nlu ");
                    NLU nluC = NLUDispatcher.this.c("-90002");
                    nluC.setText("no network");
                    NLUDispatcher.this.c(aNTHandlerContext, nluC);
                }
            };
            this.e.postDelayed(this.f, 15000L);
        }
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASRError(ANTHandlerContext ctx, String error) throws JSONException {
        d(ctx);
        LogMgr.i("NLUDispatcher", "-onASRError-" + error);
        JSONObject toJSONObject = JsonTool.parseToJSONObject(error);
        String jsonValue = JsonTool.getJsonValue(toJSONObject, "errorCode");
        String jsonValue2 = JsonTool.getJsonValue(toJSONObject, "errorMsg");
        if (b(jsonValue)) {
            this.b = true;
            return false;
        }
        this.f423a = true;
        NLU nluC = c(jsonValue);
        nluC.setText(jsonValue2);
        c(ctx, nluC);
        return false;
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventCancel(ANTHandlerContext ctx) {
        a();
        return false;
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventRecordingStart(ANTHandlerContext ctx) {
        LogMgr.i("NLUDispatcher", "onASREventRecordingStart");
        a();
        a(ctx, false);
        return super.onASREventRecordingStart(ctx);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASRResultLocal(ANTHandlerContext ctx, String result) {
        LogMgr.d("NLUDispatcher", "onASRResultLocal:" + result);
        if (this.f423a) {
            LogMgr.e("NLUDispatcher", "result has handled, local nlu handle return");
            return true;
        }
        d(ctx);
        LocalASR localASR = this.c.from(result).getLocalASRList().get(0);
        NLU nluFrom = this.d.from(localASR.getRecognitionResult());
        if (!a(localASR, nluFrom)) {
            return a(ctx);
        }
        if (a(ctx.androidContext(), nluFrom.getText())) {
            return false;
        }
        return a(ctx, result, nluFrom);
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASRResultNet(ANTHandlerContext ctx, String result) {
        String str;
        LogMgr.d("NLUDispatcher", "onASRResultNet:" + result);
        if (this.f423a) {
            LogMgr.e("NLUDispatcher", "result has handled, net nlu handle return");
            return true;
        }
        d(ctx);
        Mixture<Intent, Result> mixtureFrom = this.c.from(result);
        if (mixtureFrom != null) {
            if (b(mixtureFrom)) {
                return a(ctx, result, mixtureFrom);
            }
            return false;
        }
        try {
            str = (String) ((JSONObject) JsonTool.parseToJSONObject(result).getJSONArray("net_nlu").get(0)).get("text");
        } catch (JSONException e) {
            LogMgr.e("NLUDispatcher", "unsupported domain 解析用户说的话出错 : " + e.getMessage());
            str = result;
        }
        LogMgr.w("NLUDispatcher", "unsupported domain " + result);
        this.f423a = true;
        NLU nluC = c("unsupportedDomain");
        nluC.setText(str);
        b(ctx, nluC);
        return true;
    }

    @Override // com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onWakeupResult(ANTHandlerContext ctx, String result) {
        LogMgr.d("NLUDispatcher", "onWakeupResult:" + result);
        LocalASR localASR = this.c.from(result).getLocalASRList().get(0);
        String strTrim = localASR.getRecognitionResult().trim();
        if (a(ctx.androidContext(), strTrim)) {
            LogMgr.d("NLUDispatcher", strTrim + " is CompetitionWord, return");
            return true;
        }
        if (b(ctx.androidContext(), strTrim) && a(localASR.getScore())) {
            LogMgr.d("NLUDispatcher", "isFunctionWakeupWord : wakeupResult:" + strTrim + ";success core:" + localASR.getScore());
            return a(ctx, localASR);
        }
        if (!c(ctx.androidContext(), strTrim) || !b(localASR.getScore())) {
            return true;
        }
        LogMgr.d("NLUDispatcher", "isMainWakeupWord : wakeupResult:" + strTrim + ";success core:" + localASR.getScore());
        return false;
    }

    @Override // com.unisound.vui.engine.ANTInboundHandlerAdapter, com.unisound.vui.engine.ANTInboundHandler
    public void userEventTriggered(Object evt, ANTHandlerContext ctx) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }
}
