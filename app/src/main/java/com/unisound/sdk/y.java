package com.unisound.sdk;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class y extends z {
    private static final int f = 50;
    private BlockingQueue<byte[]> e;

    public y(cn.yunzhisheng.asr.a aVar, au auVar) {
        super(aVar, auVar, true);
        this.e = new LinkedBlockingQueue();
    }

    @Override // com.unisound.sdk.z
    protected boolean a() {
        this.e.clear();
        return true;
    }

    @Override // com.unisound.sdk.z
    protected void b() {
    }

    @Override // com.unisound.sdk.z
    protected byte[] c() throws InterruptedException {
        byte[] bArrPoll;
        while (!e() && !f()) {
            try {
                bArrPoll = this.e.poll(50L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                bArrPoll = null;
            }
            if (bArrPoll != null) {
                byte[] bArr = new byte[bArrPoll.length];
                return bArrPoll;
            }
        }
        return null;
    }
}
