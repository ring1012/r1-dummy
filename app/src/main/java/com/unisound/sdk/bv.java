package com.unisound.sdk;

import cn.yunzhisheng.tts.JniClient;
import java.util.TimerTask;

/* loaded from: classes.dex */
class bv extends TimerTask {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JniClient f324a;
    final /* synthetic */ bu b;

    bv(bu buVar, JniClient jniClient) {
        this.b = buVar;
        this.f324a = jniClient;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
        if (this.b.c == null) {
            this.f324a.c();
            this.b.j();
            this.b.b(true);
        }
    }
}
