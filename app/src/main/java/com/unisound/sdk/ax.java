package com.unisound.sdk;

import com.unisound.client.IAudioSource;
import java.util.Arrays;

/* loaded from: classes.dex */
public class ax extends z {
    protected static ax e;
    private static Object g = new Object();
    int f;
    private IAudioSource h;
    private cn.yunzhisheng.asr.a i;
    private byte[] j;

    public ax(cn.yunzhisheng.asr.a aVar, au auVar, IAudioSource iAudioSource) {
        super(aVar, auVar);
        this.h = null;
        this.f = 0;
        this.i = aVar;
        this.h = iAudioSource;
        e = this;
    }

    public static void n() {
        if (e != null) {
            e.k();
        }
    }

    @Override // com.unisound.sdk.z
    protected boolean a() {
        if (this.c.c == null || !this.c.c.a()) {
            if (this.c.av()) {
                this.j = new byte[this.c.ai() * 2];
            } else {
                this.j = new byte[this.c.ai()];
            }
        } else if (!this.c.c.s()) {
            this.j = new byte[this.c.ai() * 2];
        } else if (this.c.c.t()) {
            this.j = new byte[this.c.ai()];
        } else {
            this.j = new byte[this.c.ai() * 2];
        }
        if (this.h.openAudioIn() != 0) {
            return false;
        }
        b(true);
        c(true);
        return true;
    }

    @Override // com.unisound.sdk.z
    protected void b() {
        synchronized (g) {
            b(false);
            this.h.closeAudioIn();
            this.j = null;
            if (e == this) {
                e = null;
            }
        }
    }

    @Override // com.unisound.sdk.z
    protected byte[] c() {
        int length = this.j.length;
        com.unisound.common.y.f("Record Read     ");
        int data = this.h.readData(this.j, length);
        if (this.i.c != null && this.i.c.a() && this.i.c.l()) {
            if (this.i.c.s()) {
                if (this.i.c.k() != 1) {
                    com.unisound.common.y.c("4mic getOneShotReady != 1");
                    return null;
                }
            } else if (this.i.c.j() != 1) {
                com.unisound.common.y.c("4mic getOneShotReady\t != 1");
                return null;
            }
        }
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("RecordingThread:: read = ", Integer.valueOf(data), " size = ", Integer.valueOf(this.j.length), " first byte ", Byte.valueOf(this.j[0]));
        }
        if (data > 0) {
            return Arrays.copyOfRange(this.j, 0, data);
        }
        if (data == -9) {
            if (com.unisound.common.y.l) {
                com.unisound.common.y.d("RecordingThread:: stop signal received ");
            }
            this.f356a = true;
            d();
            com.unisound.common.y.c("RecordingThread", "stop signal received");
            return Arrays.copyOfRange(this.d, 0, 1);
        }
        if (data >= 0) {
            return null;
        }
        this.f356a = true;
        d();
        i();
        com.unisound.common.y.a("RecordingThread :: read error");
        return Arrays.copyOfRange(this.d, 0, 1);
    }
}
