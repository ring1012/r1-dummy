package com.unisound.vui.handler.session.memo.syncloud;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.google.gson.JsonObject;
import com.unisound.ant.device.bean.DstServiceName;
import com.unisound.ant.device.bean.NoteInfo;
import com.unisound.ant.device.bean.UnisoundDeviceCommand;
import com.unisound.ant.device.sessionlayer.SessionExecuteHandler;
import com.unisound.ant.device.sessionlayer.SessionRegister;
import com.unisound.vui.util.JsonTool;
import com.unisound.vui.util.LogMgr;

/* loaded from: classes.dex */
public class NoteSateMgr extends SessionExecuteHandler {
    private static final String TAG = "NoteSateMgr";
    private NoteStateListener controlStateListener;
    private Handler reportHandler;

    public interface NoteStateListener {
        void remoteAddNote(NoteInfo noteInfo);

        void remoteDeleteNote(NoteInfo noteInfo);

        void remoteUpdateNote(NoteInfo noteInfo);
    }

    public NoteSateMgr() {
        HandlerThread thread = new HandlerThread(TAG);
        thread.start();
        this.reportHandler = new Handler(thread.getLooper());
        SessionRegister.associateSessionCenter(DstServiceName.DST_SERVICE_NOTE_MANAGER, this);
    }

    public void setControlStateListener(NoteStateListener controlStateListener) {
        this.controlStateListener = controlStateListener;
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                UnisoundDeviceCommand command = (UnisoundDeviceCommand) msg.obj;
                dispatchNoteControlCommand(command);
                break;
        }
    }

    private void dispatchNoteControlCommand(UnisoundDeviceCommand command) {
        if (command == null) {
            LogMgr.d(TAG, "dispatchNoteControlCommand command is null");
            return;
        }
        String operation = command.getOperation();
        JsonObject parameter = (JsonObject) command.getParameter();
        LogMgr.d(TAG, "--->>dispatchNoteControlCommand operate:" + operation);
        NoteInfo noteInfo = (NoteInfo) JsonTool.fromJson(parameter, NoteInfo.class);
        if (noteInfo == null) {
            LogMgr.d(TAG, "dispatchNoteControlCommand get alarmReminder is null");
            return;
        }
        if ("add".equals(operation)) {
            if (this.controlStateListener != null) {
                this.controlStateListener.remoteAddNote(noteInfo);
            }
        } else if ("delete".equals(operation)) {
            if (this.controlStateListener != null) {
                this.controlStateListener.remoteDeleteNote(noteInfo);
            }
        } else if ("update".equals(operation) && this.controlStateListener != null) {
            this.controlStateListener.remoteUpdateNote(noteInfo);
        }
    }

    public void reportNoteStatus(final String commandValue, final NoteInfo content) {
        this.reportHandler.post(new Runnable() { // from class: com.unisound.vui.handler.session.memo.syncloud.NoteSateMgr.1
            @Override // java.lang.Runnable
            public void run() {
                LogMgr.d(NoteSateMgr.TAG, "--->>reportAlarmReminder content:" + content);
                if (SessionRegister.getUpDownMessageManager() == null) {
                    LogMgr.d(NoteSateMgr.TAG, "--->>messgaeMonitor is null");
                } else {
                    SessionRegister.getUpDownMessageManager().onReportNoteStatus(null, commandValue, content);
                }
            }
        });
    }
}
