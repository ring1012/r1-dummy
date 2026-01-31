package com.phicomm.speaker.device.ui.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import com.phicomm.speaker.device.R;
import com.phicomm.speaker.device.ui.MainActivity;
import com.unisound.vui.common.config.ANTConfigPreference;
import com.unisound.vui.message.HandlerActions;
import com.unisound.vui.message.HandlerSelectData;
import com.unisound.vui.message.MessageBeanHandlerGui;
import com.unisound.vui.message.TransferHandlerWithGui;
import com.unisound.vui.util.LogMgr;
import java.io.IOException;

/* loaded from: classes.dex */
public class WindowsService extends Service implements TransferHandlerWithGui {
    private static final int NOTIFICATION_ID = 1;
    private static final String TAG = "WindowsService";
    private static ANTServiceBinder antServiceBinder;

    @Override // android.app.Service
    @TargetApi(16)
    public void onCreate() throws IOException {
        super.onCreate();
        LogMgr.d(TAG, "onCreate");
        ANTConfigPreference.init(getApplicationContext());
        showNotificationForAliveBackground();
        if (antServiceBinder == null) {
            antServiceBinder = new ANTServiceBinder(getApplication(), null);
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        stopForegroundCompat(1);
        Process.killProcess(Process.myPid());
    }

    void startForegroundCompat(int id, Notification notification) {
        startForeground(id, notification);
    }

    void stopForegroundCompat(int id) {
        stopForeground(true);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogMgr.d(TAG, "Windows Service Start");
        return 1;
    }

    @Override // android.app.Service
    @Nullable
    public IBinder onBind(Intent intent) {
        LogMgr.d(TAG, "-->>onBind");
        return antServiceBinder;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        stopSelf();
        return true;
    }

    @Override // android.app.Service
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        LogMgr.d(TAG, "onRebind");
    }

    @Override // com.unisound.vui.message.TransferHandlerWithGui
    public void sendMsg(MessageBeanHandlerGui message, TransferHandlerWithGui receiver) {
        LogMgr.d(TAG, "sendMsg to handler:" + receiver.getClass().getSimpleName());
        if (message.getData() != null && (message.getData() instanceof HandlerSelectData)) {
            LogMgr.d(TAG, "sendMsg to handler data:" + ((HandlerSelectData) message.getData()).getHandlerSelectType().toString());
        }
        if (receiver != null) {
            receiver.preReceiveMsg(message);
        }
    }

    @Override // com.unisound.vui.message.TransferHandlerWithGui
    public void receiveMsg(MessageBeanHandlerGui message) {
        if (message == null) {
            LogMgr.e(TAG, "receiveMsg message is null");
        } else if (!message.getAction().equals(HandlerActions.VOLOUME_CHANGE)) {
            LogMgr.d(TAG, "receiveMsg:" + message.getAction());
        }
    }

    @Override // com.unisound.vui.message.TransferHandlerWithGui
    public void registerMessageHandlerWithGui(TransferHandlerWithGui transferHandlerWithGui) {
    }

    @Override // com.unisound.vui.message.TransferHandlerWithGui
    public void preReceiveMsg(MessageBeanHandlerGui<?> messageBeanHandlerGui) {
        receiveMsg(messageBeanHandlerGui);
    }

    private void showNotificationForAliveBackground() {
        Notification.Builder builder = new Notification.Builder(this);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, (Class<?>) MainActivity.class), 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker("Foreground Service Start");
        builder.setContentTitle("Foreground Service");
        builder.setContentText("Make this service run in the foreground.");
        Notification notification = builder.build();
        startForegroundCompat(1, notification);
    }
}
