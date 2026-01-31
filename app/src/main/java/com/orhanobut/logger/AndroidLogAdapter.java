package com.orhanobut.logger;

import android.util.Log;

/* loaded from: classes.dex */
class AndroidLogAdapter implements LogAdapter {
    AndroidLogAdapter() {
    }

    @Override // com.orhanobut.logger.LogAdapter
    public void d(String tag, String message) {
        Log.d(tag, message);
    }

    @Override // com.orhanobut.logger.LogAdapter
    public void e(String tag, String message) {
        Log.e(tag, message);
    }

    @Override // com.orhanobut.logger.LogAdapter
    public void w(String tag, String message) {
        Log.w(tag, message);
    }

    @Override // com.orhanobut.logger.LogAdapter
    public void i(String tag, String message) {
        Log.i(tag, message);
    }

    @Override // com.orhanobut.logger.LogAdapter
    public void v(String tag, String message) {
        Log.v(tag, message);
    }

    @Override // com.orhanobut.logger.LogAdapter
    public void wtf(String tag, String message) {
        Log.wtf(tag, message);
    }
}
