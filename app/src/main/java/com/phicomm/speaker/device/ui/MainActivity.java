package com.phicomm.speaker.device.ui;

import android.app.Activity;
import android.os.Bundle;
import com.phicomm.speaker.device.R;
import com.unisound.vui.util.LogMgr;

/* loaded from: classes.dex */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogMgr.d(TAG, "onCreate");
        setContentView(R.layout.layout_welcome);
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        LogMgr.d(TAG, "onResume");
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        LogMgr.d(TAG, "--->>onDestroy");
        super.onDestroy();
    }
}
