package com.phicomm.speaker.device;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import com.phicomm.speaker.device.custom.message.MessageSenderDelegate;
import com.phicomm.speaker.device.custom.mqtt.PhicommMQTTStatausChange;
import com.phicomm.speaker.device.custom.status.PhicommDeviceStatusProcessor;
import com.phicomm.speaker.device.custom.udid.DeviceIdProcessor;
import com.phicomm.speaker.device.ui.service.WindowsService;
import com.tencent.bugly.crashreport.CrashReport;
import com.unisound.ant.device.DeviceCenterHandler;
import com.unisound.ant.device.controlor.DefaultVolumeOperator;
import com.unisound.vui.common.media.UniMediaPlayer;
import com.unisound.vui.util.AppGlobalConstant;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.SharedPreferencesHelper;
import org.litepal.LitePalApplication;

/* loaded from: classes.dex */
public class ExampleApp extends MultiDexApplication {
    private static final String PROCESS_NAME = "com.phicomm.speaker.device";
    private static final String TAG = "ExampleApp";

    private String getMyProcessName(Context context) {
        int pid = Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        String processName = getMyProcessName(this);
        LogMgr.e(TAG, "processName:" + processName);
        if (processName.equals("com.phicomm.speaker.device")) {
            init();
            startWindowsService();
        }
    }

    private void init() {
        SharedPreferencesHelper.init(this);
        AppGlobalConstant.setContext(this);
        DefaultVolumeOperator.init(this);
        LitePalApplication.initialize(this);
        UniMediaPlayer.init(this);
        CrashReport.initCrashReport(this, "747a0abbf5", BuildConfig.DEBUG);
        PhicommDeviceStatusProcessor.init(this);
        MessageSenderDelegate.init(this);
        DeviceCenterHandler.init(this, new PhicommMQTTStatausChange(this));
        initCustomDevicesIdProcess();
    }

    private void initCustomDevicesIdProcess() {
        DeviceIdProcessor deviceIdProcessor = new DeviceIdProcessor(this);
        if (TextUtils.isEmpty(deviceIdProcessor.getDeviceId())) {
            deviceIdProcessor.fetchDeviceId();
        }
    }

    private void startWindowsService() {
        Intent intent = new Intent();
        intent.setClass(this, WindowsService.class);
        startService(intent);
    }
}
