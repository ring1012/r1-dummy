package com.unisound.vui.handler.session.memo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import com.unisound.vui.handler.session.memo.entity.LocalMemo;
import com.unisound.vui.handler.session.memo.utils.MemoConstants;
import com.unisound.vui.handler.session.memo.utils.MemoUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import nluparser.scheme.AlarmIntent;
import nluparser.scheme.Intent;
import nluparser.scheme.NLU;
import nluparser.scheme.Result;
import nluparser.scheme.SName;

/* loaded from: classes.dex */
public class DefaultAlarmHandler extends AbstractMemoHandler {
    private static final String TAG = "memolog-AlarmHandler";

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    protected /* bridge */ /* synthetic */ boolean acceptInboundEvent0(NLU<Intent, Result.NullResult> nlu) throws Exception {
        return acceptInboundEvent02((NLU) nlu);
    }

    public DefaultAlarmHandler(Context context) {
        super(context);
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    protected void initPriority() {
        setPriority(300);
    }

    /* renamed from: acceptInboundEvent0, reason: avoid collision after fix types in other method */
    protected boolean acceptInboundEvent02(NLU evt) throws Exception {
        return SName.ALARM.equals(evt.getService());
    }

    @Override // com.unisound.vui.handler.session.memo.AbstractMemoHandler
    @SuppressLint({"StringFormatInvalid", "StringFormatMatches"})
    protected String dealWithSetMemo(Intent intent, Context context) {
        LocalMemo memo;
        AlarmIntent alarmIntent = (AlarmIntent) intent;
        if (alarmIntent.getType() == 0) {
            if (!checkMemoCount("alarm")) {
                return this.ctx.androidContext().getString(R.string.tts_alarm_count_max);
            }
        } else if (!checkMemoCount(MemoConstants.MEMO_TYPE_COUNT_DOWN)) {
            return this.ctx.androidContext().getString(R.string.tts_count_down_count_max);
        }
        if (!isValidIntent(alarmIntent)) {
            return "";
        }
        if (alarmIntent.getType() == 1) {
            memo = MemoUtils.generateCountdownMemo(alarmIntent);
        } else {
            memo = MemoUtils.generateAlarmMemo(alarmIntent);
            if (memo == null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
                String dateStr = dateFormat.format(new Date());
                return context.getString(R.string.tts_memo_set_result_outofdate_exception, dateStr);
            }
        }
        this.mMemoInfoMgr.addMemo(memo);
        return memo.isAlarm() ? context.getString(R.string.tts_alarm_set_result_ok, getMemoTimeNlu(context, memo)) : context.getString(R.string.tts_count_down_set_result_ok);
    }

    private boolean isValidIntent(AlarmIntent intent) {
        if (intent == null) {
            return false;
        }
        if (intent.getType() == 0) {
            if ("MONTH".equals(intent.getRepeatDate()) || "YEAR".equals(intent.getRepeatDate())) {
                return false;
            }
            return (TextUtils.isEmpty(intent.getDate()) && TextUtils.isEmpty(intent.getTime())) ? false : true;
        }
        return intent.getCountDown() >= 10000;
    }

    @Override // com.unisound.vui.handler.session.memo.AbstractMemoHandler
    protected void recoveryHandlerPriority() {
        setPriority(300);
    }
}
