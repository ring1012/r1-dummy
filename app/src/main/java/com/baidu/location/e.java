package com.baidu.location;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/* loaded from: classes.dex */
public interface e {
    double a();

    void a(Context context);

    boolean a(Intent intent);

    IBinder onBind(Intent intent);

    void onDestroy();

    int onStartCommand(Intent intent, int i, int i2);

    void onTaskRemoved(Intent intent);
}
