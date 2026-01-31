package com.kaolafm.sdk.client;

import android.content.ComponentName;

/* loaded from: classes.dex */
public interface IServiceConnection {
    void onServiceConnected(ComponentName componentName);

    void onServiceDisconnected(ComponentName componentName);
}
