package com.unisound.common;

import android.os.Process;

/* loaded from: classes.dex */
public class f extends Thread {

    /* renamed from: a, reason: collision with root package name */
    protected int f259a = 0;

    public f(boolean z) throws SecurityException, IllegalArgumentException {
        if (z) {
            setPriority(10);
            Process.setThreadPriority(-19);
        } else {
            setPriority(5);
            Process.setThreadPriority(-16);
        }
    }

    public void a(int i) {
        this.f259a = i;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
    }
}
