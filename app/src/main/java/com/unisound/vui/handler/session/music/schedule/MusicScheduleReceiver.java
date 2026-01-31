package com.unisound.vui.handler.session.music.schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.unisound.vui.util.LogMgr;

/* loaded from: classes.dex */
public class MusicScheduleReceiver extends BroadcastReceiver {
    private ScheduleTriggeredListener mScheduleTriggeredListener;

    public interface ScheduleTriggeredListener {
        void onScheduleTriggered();
    }

    public MusicScheduleReceiver() {
    }

    public MusicScheduleReceiver(ScheduleTriggeredListener mScheduleTriggeredListener) {
        this.mScheduleTriggeredListener = mScheduleTriggeredListener;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        LogMgr.d("MusicScheduleReceiver", "onReceive, " + intent.getLongExtra("stop_time", 0L));
        if (this.mScheduleTriggeredListener != null) {
            this.mScheduleTriggeredListener.onScheduleTriggered();
        }
    }
}
