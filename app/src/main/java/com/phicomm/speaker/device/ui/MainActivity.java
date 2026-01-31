package com.phicomm.speaker.device.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import com.baidu.mobstat.StatService;
import com.phicomm.speaker.device.R;
import com.tencent.bugly.crashreport.CrashReport;
import com.unisound.vui.util.LogMgr;


public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    Button scrcpyBtn;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogMgr.d(TAG, "onCreate");
        setContentView(R.layout.layout_welcome);
        StatService.setDebugOn(false);
        StatService.setAppKey("efe5be0e5e");
        StatService.setAppChannel(this,"tdre",true);
        StatService.setOn(this, 1);
        StatService.setSessionTimeOut(30);
        StatService.enableDeviceMac(this, true);
        StatService.setForTv(this, true) ;
        StatService.autoTrace(this, true, false);
//        initService();
    }

    /**
     * 初始化服务
     */
    private void initService() {
    }


    public void onResume() {
        super.onResume();
        LogMgr.d(TAG, "onResume");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LogMgr.d(TAG, "--->>onDestroy");
        super.onDestroy();
    }
}
