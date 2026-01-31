package com.unisound.vui.handler.session.memo.syncloud;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.google.gson.JsonObject;
import com.unisound.ant.device.bean.AlarmReminder;
import com.unisound.ant.device.bean.DstServiceName;
import com.unisound.ant.device.bean.UnisoundDeviceCommand;
import com.unisound.ant.device.sessionlayer.SessionExecuteHandler;
import com.unisound.ant.device.sessionlayer.SessionRegister;
import com.unisound.vui.handler.session.memo.entity.LocalMemo;
import com.unisound.vui.handler.session.memo.utils.MemoUtils;
import com.unisound.vui.util.JsonTool;
import com.unisound.vui.util.LogMgr;

/* loaded from: classes.dex */
public class MemoryStateMgr extends SessionExecuteHandler {
    private static final String TAG = "memolog-MemoryStateMgr";
    private ControlStateListener mControlStateListener;
    private Handler mReportHandler;

    public interface ControlStateListener {
        void remoteAddMemo(LocalMemo localMemo);

        void remoteDeleteMemo(LocalMemo localMemo);

        void remoteUpdateMemo(LocalMemo localMemo);
    }

    public MemoryStateMgr() {
        HandlerThread thread = new HandlerThread(TAG);
        thread.start();
        this.mReportHandler = new Handler(thread.getLooper());
        SessionRegister.associateSessionCenter(DstServiceName.DST_SERVICE_MEMORY_MANAGER, this);
    }

    public void setControlStateListener(ControlStateListener controlStateListener) {
        this.mControlStateListener = controlStateListener;
    }

    @Override // android.os.Handler
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                if (msg.obj != null && (msg.obj instanceof UnisoundDeviceCommand)) {
                    UnisoundDeviceCommand command = (UnisoundDeviceCommand) msg.obj;
                    dispatchMemoControlCommand(command);
                    break;
                }
                break;
        }
    }

    private void dispatchMemoControlCommand(UnisoundDeviceCommand command) {
        String operation = command.getOperation();
        JsonObject parameter = (JsonObject) command.getParameter();
        LogMgr.d(TAG, "dispatchMemoControlCommand operate:" + operation);
        AlarmReminder alarmReminder = (AlarmReminder) JsonTool.fromJson(parameter, AlarmReminder.class);
        if (alarmReminder == null) {
            LogMgr.d(TAG, "dispatchMemoControlCommand get alarmReminder is null");
            return;
        }
        LocalMemo localMemo = MemoUtils.getLocalMemo(alarmReminder);
        if (localMemo == null) {
            LogMgr.d(TAG, "dispatchMemoControlCommand get localMemo is null");
            return;
        }
        localMemo.setLocalCreateUpDdate(false);
        if ("add".equals(operation)) {
            if (this.mControlStateListener != null) {
                this.mControlStateListener.remoteAddMemo(localMemo);
            }
        } else if ("delete".equals(operation)) {
            if (this.mControlStateListener != null) {
                this.mControlStateListener.remoteDeleteMemo(localMemo);
            }
        } else if ("update".equals(operation) && this.mControlStateListener != null) {
            this.mControlStateListener.remoteUpdateMemo(localMemo);
        }
    }

    public void reportMemoStatus(final String commandValue, LocalMemo memo) {
        final AlarmReminder content;
        LogMgr.d(TAG, "reportMemoStatus, commandValue:" + commandValue + ", " + memo);
        switch (commandValue) {
            case "add":
                content = MemoUtils.getAlarmReminder("start", memo);
                break;
            case "delete":
                content = MemoUtils.getAlarmReminder("cancel", memo);
                break;
            case "update":
                if (memo.isEnabled()) {
                    content = MemoUtils.getAlarmReminder("start", memo);
                    break;
                } else {
                    content = MemoUtils.getAlarmReminder("cancel", memo);
                    break;
                }
            default:
                content = null;
                break;
        }
        if (content != null) {
            this.mReportHandler.post(new Runnable() { // from class: com.unisound.vui.handler.session.memo.syncloud.MemoryStateMgr.1
                @Override // java.lang.Runnable
                public void run() {
                    if (SessionRegister.getUpDownMessageManager() == null) {
                        LogMgr.w(MemoryStateMgr.TAG, "reportMemoStatus, UpDownMessageManager is null");
                    } else {
                        SessionRegister.getUpDownMessageManager().onReportAlarmReminderStatus(null, commandValue, content);
                    }
                }
            });
        }
    }
}
