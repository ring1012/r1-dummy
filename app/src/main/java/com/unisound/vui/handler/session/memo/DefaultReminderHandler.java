package com.unisound.vui.handler.session.memo;

import android.content.Context;
import android.text.TextUtils;
import com.unisound.vui.handler.session.memo.entity.LocalMemo;
import com.unisound.vui.handler.session.memo.utils.MemoUtils;
import com.unisound.vui.util.LogMgr;
import java.text.SimpleDateFormat;
import java.util.Date;
import nluparser.scheme.Intent;
import nluparser.scheme.NLU;
import nluparser.scheme.ReminderIntent;
import nluparser.scheme.Result;
import nluparser.scheme.SName;

/* loaded from: classes.dex */
public class DefaultReminderHandler extends AbstractMemoHandler {
    private static final String TAG = "memolog-ReminderHandler";

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    protected /* bridge */ /* synthetic */ boolean acceptInboundEvent0(NLU<Intent, Result.NullResult> nlu) throws Exception {
        return acceptInboundEvent02((NLU) nlu);
    }

    public DefaultReminderHandler(Context context) {
        super(context);
    }

    @Override // com.unisound.vui.handler.SimpleUserEventInboundHandler
    protected void initPriority() {
        setPriority(300);
    }

    /* renamed from: acceptInboundEvent0, reason: avoid collision after fix types in other method */
    protected boolean acceptInboundEvent02(NLU evt) throws Exception {
        return SName.REMINDER.equals(evt.getService());
    }

    @Override // com.unisound.vui.handler.session.memo.AbstractMemoHandler
    protected String dealWithSetMemo(Intent intent, Context context) {
        if (!checkMemoCount("reminder")) {
            return context.getString(R.string.tts_reminder_count_max);
        }
        ReminderIntent reminderIntent = (ReminderIntent) intent;
        if (!isValidReminder(reminderIntent)) {
            LogMgr.d(TAG, "dealWithSetMemo, reminder intent is invalid");
            return "";
        }
        if (TextUtils.isEmpty(reminderIntent.getContent())) {
            LogMgr.d(TAG, "dealWithSetMemo, reminder content is empty");
            return context.getString(R.string.tts_memo_set_result_withoutcontent_exception);
        }
        LocalMemo memo = MemoUtils.generateReminderMemo(reminderIntent);
        if (memo == null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
            String dateStr = dateFormat.format(new Date());
            return context.getString(R.string.tts_memo_set_result_outofdate_exception, dateStr);
        }
        this.mMemoInfoMgr.addMemo(memo);
        return context.getString(R.string.tts_reminder_set_result_ok, getMemoTimeNlu(context, memo), memo.getMemoContent());
    }

    private boolean isValidReminder(ReminderIntent intent) {
        return (intent == null || TextUtils.isEmpty(intent.getDateTime())) ? false : true;
    }

    @Override // com.unisound.vui.handler.session.memo.AbstractMemoHandler
    protected void recoveryHandlerPriority() {
        setPriority(300);
    }
}
