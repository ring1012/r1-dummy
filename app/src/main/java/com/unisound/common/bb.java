package com.unisound.common;

import android.content.Context;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes.dex */
class bb extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BlockingQueue f253a;
    final /* synthetic */ Context b;
    final /* synthetic */ bd c;
    final /* synthetic */ ba d;

    bb(ba baVar, BlockingQueue blockingQueue, Context context, bd bdVar) {
        this.d = baVar;
        this.f253a = blockingQueue;
        this.b = context;
        this.c = bdVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        y.c("WakeupWordCacheAndUpload uploadWakeup");
        try {
            LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
            linkedBlockingQueue.addAll(this.f253a);
            if (this.d.c(this.b, this.c, this.f253a)) {
                y.c("WakeupWordCacheAndUpload uploadWakeup success!");
            } else {
                y.a("WakeupWordCacheAndUpload uploadWakeup error occur,save to cache!");
                this.d.b(this.b, this.c, (BlockingQueue<byte[]>) linkedBlockingQueue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
