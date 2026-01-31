package com.phicomm.speaker.device;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import android.support.multidex.MultiDexApplication;
import com.phicomm.speaker.device.custom.message.MessageSenderDelegate;
import com.phicomm.speaker.device.custom.mqtt.PhicommMQTTStatausChange;
import com.phicomm.speaker.device.custom.status.PhicommDeviceStatusProcessor;
import com.phicomm.speaker.device.custom.udid.DeviceIdProcessor;
import com.phicomm.speaker.device.ui.service.WindowsService;
import com.unisound.ant.device.DeviceCenterHandler;
import com.unisound.ant.device.controlor.DefaultVolumeOperator;
import com.unisound.vui.common.media.UniMediaPlayer;
import com.unisound.vui.util.AppGlobalConstant;
import com.unisound.vui.util.LogMgr;
import com.unisound.vui.util.SharedPreferencesHelper;
import org.litepal.LitePalApplication;

import java.io.File;


public class ExampleApp extends MultiDexApplication {
    public static Context context;
    public static  String PROCESS_NAME = "com.phicomm.speaker.device";
    public static  String VERSION_NAME = "1.2.1";
    public static  int VERSION_CODE = 11;
    private static final String TAG = "ExampleApp";

    private String getMyProcessName(Context context) {
        int pid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
//                return appProcess.processName;
                return ExampleApp.PROCESS_NAME;
            }
        }
        return "";
    }

    public void onCreate() {
        super.onCreate();
        String processName = getMyProcessName(this);
        LogMgr.e(TAG, "processName:" + processName);
//        if (processName.equals(PROCESS_NAME)) {
            init();
            startWindowsService();
//        }
    }

    private void init() {
        Log.i(TAG, "init: start");
        SharedPreferencesHelper.init(this);
        AppGlobalConstant.setContext(this);
        DefaultVolumeOperator.init(this);
        LitePalApplication.initialize(this);
        UniMediaPlayer.init(this);
        PhicommDeviceStatusProcessor.init(this);
        MessageSenderDelegate.init(this);
        DeviceCenterHandler.init(this, new PhicommMQTTStatausChange(this));
        initCustomDevicesIdProcess();

        initService();
    }


    /**
     * 初始化服务
     */
    private void initService() {
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

    @Override
    protected void attachBaseContext(Context newBase) {
//        ServiceManagerWraper.hookPMS(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    public File getFilesDir(){
        File file = new File(super.getFilesDir(),PROCESS_NAME);
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }
}
