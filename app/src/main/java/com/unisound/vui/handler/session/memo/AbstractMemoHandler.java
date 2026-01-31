package com.unisound.vui.handler.session.memo;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import com.unisound.vui.common.config.ANTConfigPreference;
import com.unisound.vui.engine.ANTHandlerContext;
import com.unisound.vui.handler.SimpleUserEventInboundHandler;
import com.unisound.vui.handler.session.memo.entity.LocalMemo;
import com.unisound.vui.handler.session.memo.schulding.MemoDataManager;
import com.unisound.vui.handler.session.memo.utils.MemoUtils;
import com.unisound.vui.priority.PriorityMap;
import com.unisound.vui.util.AttributeKey;
import com.unisound.vui.util.ExoConstants;
import com.unisound.vui.util.LogMgr;
import nluparser.scheme.AlarmIntent;
import nluparser.scheme.General;
import nluparser.scheme.Intent;
import nluparser.scheme.NLU;
import nluparser.scheme.Result;

/* loaded from: classes.dex */
public abstract class AbstractMemoHandler extends SimpleUserEventInboundHandler<NLU<Intent, Result.NullResult>> {
    protected static final AttributeKey<Boolean> NEED_SUPPLEMENT = AttributeKey.valueOf(DefaultAlarmHandler.class, "NEED_SUPPLEMENT");
    private static final String TAG = "AbstractMemoHandler";
    private Context mContext;
    protected MemoDataManager mMemoInfoMgr;

    protected abstract String dealWithSetMemo(Intent intent, Context context);

    protected abstract void recoveryHandlerPriority();

    public AbstractMemoHandler(Context context) {
        this.mContext = context;
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onASREventEngineInitDone(ANTHandlerContext ctx) {
        this.mMemoInfoMgr = MemoDataManager.getInstance();
        return super.onASREventEngineInitDone(ctx);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    public void eventReceived(NLU<Intent, Result.NullResult> evt, ANTHandlerContext ctx) throws Exception {
        String playAnswer;
        super.eventReceived((AbstractMemoHandler) evt, ctx);
        String code = evt.getCode();
        LogMgr.d(TAG, "-->> eventReceived code:" + code);
        playAnswer = "";
        switch (code) {
            case "ALARM_REMOVE_ALL":
            case "REMINDER_REMOVE_ALL":
                playAnswer = this.mContext.getString(R.string.tts_memo_cancel_disallowed);
                break;
            case "ALARM_CANCEL":
            case "REMINDER_CANCEL":
                playAnswer = this.mContext.getString(R.string.tts_memo_cancel_disallowed);
                break;
            case "ALARM_REMOVE":
            case "REMINDER_REMOVE":
                playAnswer = this.mContext.getString(R.string.tts_memo_cancel_disallowed);
                break;
            case "ANSWER":
                playAnswer = dealWithAnswer(ctx, evt.getGeneral());
                break;
            case "ALARM_SET":
            case "REMINDER_SET":
                if (evt.getSemantic() != null) {
                    playAnswer = dealWithSetMemo(evt.getSemantic().getIntent(), this.mContext);
                    break;
                }
                break;
            case "ALARM_SET_TIMING":
                if (evt.getSemantic() != null) {
                    AlarmIntent intent = (AlarmIntent) evt.getSemantic().getIntent();
                    intent.setType(1);
                    playAnswer = dealWithSetMemo(intent, this.mContext);
                    break;
                }
                break;
        }
        if (TextUtils.isEmpty(playAnswer)) {
            playAnswer = this.mContext.getResources().getString(R.string.tts_unsupport);
        }
        ctx.playTTS(playAnswer);
        sendFullLogToDeviceCenter(evt, playAnswer);
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler, com.unisound.vui.engine.ANTDuplexHandler, com.unisound.vui.handler.ANTEventDispatcher
    protected boolean onTTSEventPlayingEnd(ANTHandlerContext ctx) {
        if (this.eventReceived) {
            reset();
            if (ctx.hasAttr(NEED_SUPPLEMENT) && ((Boolean) ctx.attr(NEED_SUPPLEMENT).getAndRemove()).booleanValue()) {
                ctx.enterASR();
            } else {
                recoveryHandlerPriority();
                ctx.enterWakeup(false);
                ctx.fireUserEventTriggered(ExoConstants.DO_RESUME);
            }
        }
        return super.onTTSEventPlayingEnd(ctx);
    }

    @Override // com.unisound.vui.handler.SimpleSessionManagementHandler
    protected void doInterrupt(ANTHandlerContext ctx, String interruptType) {
        if (this.eventReceived) {
            ctx.cancelTTS();
            reset();
            recoveryHandlerPriority();
        }
    }

    protected String dealWithAnswer(ANTHandlerContext ctx, General answerGeneral) {
        if (answerGeneral == null) {
            return null;
        }
        ctx.attr(NEED_SUPPLEMENT).set(true);
        setPriority(PriorityMap.PRIORITY_MAX);
        return answerGeneral.getText();
    }

    boolean checkMemoCount(String memoType) {
        switch (memoType) {
            case "alarm":
                int alarmCount = this.mMemoInfoMgr.getAlarmsCount();
                LogMgr.d(TAG, "checkAlarmCount, alarmCount:" + alarmCount);
                if (alarmCount > ANTConfigPreference.sMaxCountAlarm) {
                    return false;
                }
                break;
            case "countDown":
                int countDownCount = this.mMemoInfoMgr.getCountDownCount();
                LogMgr.d(TAG, "checkAlarmCount, countDownCount:" + countDownCount);
                if (countDownCount > ANTConfigPreference.sMaxCountCountDown) {
                    return false;
                }
                break;
            case "reminder":
                int reminderCount = this.mMemoInfoMgr.getReminderCount();
                if (reminderCount > ANTConfigPreference.sMaxCountReminder) {
                    LogMgr.d(TAG, "checkReminderCount:" + reminderCount + ", max:" + ANTConfigPreference.sMaxCountAlarm);
                    return false;
                }
                break;
        }
        return true;
    }

    String getMemoTimeNlu(Context mAndroidContext, LocalMemo memo) throws Resources.NotFoundException {
        String[] memoTimeDayNlus = mAndroidContext.getResources().getStringArray(R.array.memo_time_day_nlu);
        String[] memoTimeDurationNlus = mAndroidContext.getResources().getStringArray(R.array.memo_time_duration_nlu);
        return MemoUtils.getSetMemoNluTime(memo, memoTimeDayNlus, memoTimeDurationNlus);
    }
}
