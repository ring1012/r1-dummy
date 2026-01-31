package com.unisound.sdk;

import cn.yunzhisheng.tts.offline.lib.YzsTts;
import com.unisound.client.ErrorCode;
import java.text.DecimalFormat;

/* loaded from: classes.dex */
public class bt extends bs {
    private String c;
    private YzsTts d;
    private bz e;
    private bw f;
    private boolean g;

    public bt(String str, bw bwVar) {
        super(bwVar.r().booleanValue(), bwVar.m());
        this.d = YzsTts.b();
        this.g = false;
        this.c = str;
        this.f = bwVar;
    }

    private void a(float f) {
        this.d.a(f);
    }

    private void a(Boolean bool) {
        this.d.b(bool);
    }

    private void a(byte[] bArr, int i) {
        bz bzVar = this.e;
        if (bzVar != null) {
            bzVar.a(bArr, i);
        }
    }

    private void b(float f) {
        this.d.b(f);
    }

    private void c(float f) {
        this.d.d(f);
    }

    private void c(int i) {
        bz bzVar = this.e;
        if (bzVar != null) {
            bzVar.a(i);
        }
    }

    private void d(float f) {
        this.d.c(f);
    }

    private void d(int i) {
        this.d.a(i);
    }

    private void e(int i) {
        this.d.b(i);
    }

    private void f(int i) {
    }

    private boolean g(int i) {
        return this.d.d(i);
    }

    private void h(int i) {
        this.d.f(i);
    }

    private void i() {
        bz bzVar = this.e;
        if (bzVar != null) {
            bzVar.a();
        }
    }

    private void i(int i) {
        this.d.g(i);
    }

    private void j() {
        bz bzVar = this.e;
        if (bzVar != null) {
            bzVar.b();
        }
    }

    private void k() {
        bz bzVar = this.e;
        if (bzVar != null) {
            bzVar.c();
        }
    }

    protected void a(bw bwVar) {
        this.f = bwVar;
    }

    public void a(bz bzVar) {
        this.e = bzVar;
    }

    @Override // com.unisound.sdk.bs
    public void b() {
        super.b();
        if (this.d != null) {
            this.d.e();
        }
    }

    public void b(int i) {
        if (isAlive()) {
            if (this.d != null) {
                this.d.e();
            }
            try {
                super.join(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void b(boolean z) {
        this.g = z;
    }

    public void g() {
        if (this.d != null) {
            this.d.c();
            this.d = null;
        }
        k();
    }

    public void h() {
        a((bz) null);
    }

    @Override // com.unisound.common.f, java.lang.Thread, java.lang.Runnable
    public void run() throws InterruptedException {
        super.run();
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("TTSOfflineSynthesizerThread -> ThreadName = ", Thread.currentThread().getName(), ", ThreadId = ", Long.valueOf(Thread.currentThread().getId()));
        }
        try {
        } catch (Exception e) {
            e.printStackTrace();
            com.unisound.common.y.a("TTSOfflineSynthesizerThread run(): Exception error");
        }
        if (this.d.a() == 0) {
            c(ErrorCode.TTS_ERROR_OFFLINE_ENGINE_NOT_INIT);
            com.unisound.common.y.a("TTSOfflineSynthesizerThread run(): 离线tts引擎未初始化，请确认执行init并接收init回调！ ");
            return;
        }
        int iQ = this.f.q();
        if (iQ != 0) {
            com.unisound.common.y.b("TTSOfflineSynthesizerThread run(): _LogLevel=", Integer.valueOf(iQ));
            a(iQ);
        }
        float fU = this.f.u();
        if (fU != 50.0f) {
            com.unisound.common.y.b("TTSOfflineSynthesizerThread run(): _VoiceSpeed=", Float.valueOf(fU));
            b(fU);
        }
        float fV = this.f.v();
        if (fV != 50.0f) {
            com.unisound.common.y.b("TTSOfflineSynthesizerThread run(): _VoicePitch=", Float.valueOf(fV));
            c(fV);
        }
        float fW = this.f.w();
        if (fW != 50.0f) {
            com.unisound.common.y.b("TTSOfflineSynthesizerThread run(): _VoiceVolume=", Float.valueOf(fW));
            d(fW);
        }
        boolean zBooleanValue = this.f.n().booleanValue();
        if (zBooleanValue) {
            com.unisound.common.y.b("TTSOfflineSynthesizerThread run(): _ReadEnglishInPinyin=", Boolean.valueOf(zBooleanValue));
            a(Boolean.valueOf(zBooleanValue));
        }
        int iO = this.f.o();
        if (iO != 100) {
            com.unisound.common.y.b("TTSOfflineSynthesizerThread run(): _FrontSilence=", Integer.valueOf(iO));
            d(iO);
        }
        int iP = this.f.p();
        if (iP != 100) {
            com.unisound.common.y.b("TTSOfflineSynthesizerThread run(): _BackSilence=", Integer.valueOf(iP));
            e(iP);
        }
        if (this.d.setText(this.d.a(), this.c) != 0) {
            c(ErrorCode.TTS_ERROR_OFFLINE_SYNTHESIZER_SET_TEXT);
            com.unisound.common.y.a("TTSOfflineSynthesizerThread run(): setText error ");
            return;
        }
        int iF = 6400;
        if (this.f.G()) {
            iF = this.f.F() * this.f.y() * 2;
            this.d.e(iF);
            com.unisound.common.y.c("TTSOfflineSynthesizerThread bufferLength = " + iF);
        }
        int iJ = this.f.J();
        if (iJ != 50) {
            com.unisound.common.y.b("TTSOfflineSynthesizerThread run(): _Voiceloud=", Integer.valueOf(iJ));
            h(iJ);
        }
        int iK = this.f.K();
        if (iK != -1) {
            com.unisound.common.y.b("TTSOfflineSynthesizerThread run(): _Domain=", Integer.valueOf(iK));
            i(iK);
        }
        byte[] bArr = new byte[iF];
        this.d.a((Boolean) true);
        i();
        long j = 0;
        long j2 = 0;
        int iA = 1;
        while (iA != 0 && !a()) {
            com.unisound.common.y.f("TTSOfflineSythesizer run : receiveSamples before");
            com.unisound.common.y.d("TTSOfflineSythesizer run : receiveSamples before");
            if (this.f.G()) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                iA = this.d.b(this.d.a(), bArr);
                long jCurrentTimeMillis2 = System.currentTimeMillis();
                if (this.f.I()) {
                    j += jCurrentTimeMillis2 - jCurrentTimeMillis;
                    j2 += iA;
                }
            } else {
                long jCurrentTimeMillis3 = System.currentTimeMillis();
                iA = this.d.a(this.d.a(), bArr);
                long jCurrentTimeMillis4 = System.currentTimeMillis();
                if (this.f.I()) {
                    j += jCurrentTimeMillis4 - jCurrentTimeMillis3;
                    j2 += iA;
                }
            }
            if (com.unisound.common.y.l) {
                com.unisound.common.y.d("TTSOfflineSynthesizerThread -> receiveData handle = ", Integer.valueOf(iA), " thread = ", Long.valueOf(Thread.currentThread().getId()));
            }
            com.unisound.common.y.d("TTSOfflineSythesizer run : receiveSamples after");
            com.unisound.common.y.f("TTSOfflineSythesizer run : receiveSamples after");
            if (bArr != null && iA > 1) {
                a(bArr, iA);
            }
            while (!a() && iA != 0 && this.g) {
                if (com.unisound.common.y.l) {
                    com.unisound.common.y.d("TTSOfflineSynthesizerThread -> pause handle = ", Integer.valueOf(iA), " thread = ", Long.valueOf(Thread.currentThread().getId()));
                }
                Thread.sleep(50L);
            }
        }
        if (this.f.I()) {
            com.unisound.common.y.a("TTSOfflineSythesizer", "synthesizerTime = " + j + ",synthesizerBufferLength = " + j2 + ", RTF = " + new DecimalFormat("#.000").format(j / (j2 / ((this.f.y() / 1000) * 2))));
        }
        j();
        this.d.a((Boolean) false);
        com.unisound.common.y.c("TTSOfflineSynthesizerThread run(): synthesizer end");
    }
}
