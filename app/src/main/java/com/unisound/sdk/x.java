package com.unisound.sdk;

import android.media.AudioRecord;
import com.unisound.client.ErrorCode;
import com.unisound.client.FourMicAudioManagerListener;
import com.unisound.client.SpeechConstants;
import com.unisound.jni.Uni4micHalJNI;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class x extends Thread {
    private static int r;
    private Uni4micHalJNI h;
    private FourMicAudioManagerListener u;
    private static int q = 16000;

    /* renamed from: a, reason: collision with root package name */
    protected static int f355a = 16;
    protected static int b = 16;
    protected static int c = 12;
    protected static int d = 2;
    protected static int e = 4;
    protected static int f = 1;
    private static int t = q;
    private long i = -1;
    private boolean j = true;
    private int k = 1;
    private int l = 2;
    private int m = 1;
    private int n = 0;
    private int o = 0;
    private AudioRecord p = null;
    int g = 0;
    private int s = SpeechConstants.ASR_BEST_RESULT_RETURN;
    private AtomicBoolean v = new AtomicBoolean(false);

    static {
        r = 6400;
        int minBufferSize = AudioRecord.getMinBufferSize(t, f355a, d);
        if (r < minBufferSize) {
            r = minBufferSize;
        }
    }

    public x(FourMicAudioManagerListener fourMicAudioManagerListener) {
        this.u = null;
        this.u = fourMicAudioManagerListener;
    }

    private int a() throws IllegalStateException {
        if (!this.j) {
            this.p = new AudioRecord(this.g, q, b, d, r);
            if (this.p.getState() != 1) {
                return -1;
            }
            this.p.startRecording();
            return 0;
        }
        if (this.h == null) {
            return -1;
        }
        this.i = this.h.openAudioIn(this.l);
        int iStartRecorder = this.h.startRecorder(this.i);
        com.unisound.common.y.c("FourMicAudioManagerInterface", "openIn uni4micHalJNI status = " + iStartRecorder);
        return iStartRecorder == 0 ? 0 : -1;
    }

    private int a(byte[] bArr, int i) {
        if (this.j) {
            if (this.h != null) {
                return this.h.readData(this.i, bArr, i);
            }
            return 0;
        }
        if (this.p != null) {
            return this.p.read(bArr, 0, i);
        }
        return 0;
    }

    private void b() throws IllegalStateException {
        if (this.j) {
            if (this.h != null) {
                com.unisound.common.y.c("FourMicAudioManagerInterface", "closeIn uni4micHalJNI stop = " + this.h.stopRecorder(this.i) + ", close = " + this.h.closeAudioIn(this.i));
                return;
            }
            return;
        }
        if (this.p != null) {
            com.unisound.common.y.c("FourMicAudioManagerInterface::close audioRecord.stop()");
            if (this.p.getState() == 1) {
                this.p.stop();
            }
            com.unisound.common.y.c("FourMicAudioManagerInterface::close audioRecord.release()");
            this.p.release();
            this.p = null;
            com.unisound.common.y.c("FourMicAudioManagerInterface::close ok");
        }
    }

    private int c() {
        this.h = Uni4micHalJNI.getInstance();
        int iInit = this.h.init(this.k);
        com.unisound.common.y.c("FourMicAudioManagerInterface initFourMic status = " + iInit);
        this.h.set4MicDebugMode(this.m);
        this.h.close4MicAlgorithm(this.n);
        this.h.set4MicWakeUpStatus(this.o);
        return iInit;
    }

    private int d() {
        return this.h.release();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IllegalStateException {
        this.u.onEvent(1032);
        try {
            try {
                if (this.j) {
                    c();
                }
                a();
                this.v.set(true);
                while (this.v.get()) {
                    byte[] bArr = this.j ? new byte[this.s * 2] : new byte[this.s];
                    int iA = a(bArr, bArr.length);
                    if (iA > 0) {
                        this.u.onRecordingData(bArr, iA);
                    }
                }
                b();
                if (this.j) {
                    d();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                this.u.onError(ErrorCode.toMessage(ErrorCode.ASR_FOURMIC_RECORDING_ERROR));
                b();
                if (this.j) {
                    d();
                }
            }
            this.u.onEvent(1033);
        } catch (Throwable th) {
            b();
            if (this.j) {
                d();
            }
            throw th;
        }
    }

    protected void setOption(int i, Object obj) {
        switch (i) {
            case 1044:
                try {
                    q = ((Integer) obj).intValue();
                    break;
                } catch (Exception e2) {
                    com.unisound.common.y.a("set asr_sampling_rate Error.");
                    return;
                }
            case SpeechConstants.ASR_OPT_RECORDING_PAC_SIZE /* 1080 */:
                try {
                    this.s = ((Integer) obj).intValue();
                    break;
                } catch (Exception e3) {
                    com.unisound.common.y.a("set recordingPacSize Error.");
                    return;
                }
            case 1111:
                try {
                    if (((Boolean) obj).booleanValue()) {
                        this.k = 1;
                    } else {
                        this.k = 0;
                    }
                    break;
                } catch (Exception e4) {
                    com.unisound.common.y.a("set use4mic Error.");
                    return;
                }
            case 2222:
                try {
                    this.l = ((Integer) obj).intValue();
                    break;
                } catch (Exception e5) {
                    com.unisound.common.y.a("set 4mic_ch_num Error.");
                    return;
                }
            case 3333:
                try {
                    this.g = ((Integer) obj).intValue();
                    break;
                } catch (Exception e6) {
                    com.unisound.common.y.a("set asr_sampling_rate Error.");
                    return;
                }
            case 4444:
                try {
                    this.o = ((Integer) obj).intValue();
                    break;
                } catch (Exception e7) {
                    com.unisound.common.y.a("set wakeupStatus Error.");
                    return;
                }
            case 10100:
                try {
                    if (((Boolean) obj).booleanValue()) {
                        this.n = 1;
                    } else {
                        this.n = 0;
                    }
                    break;
                } catch (Exception e8) {
                    com.unisound.common.y.a("set ASR_FOURMIC_CLOSE_4MICALGORITHM Error.");
                    return;
                }
            case 10198:
                try {
                    boolean zBooleanValue = ((Boolean) obj).booleanValue();
                    if (zBooleanValue) {
                        this.m = 1;
                    } else {
                        this.m = 0;
                    }
                    com.unisound.common.y.k = zBooleanValue;
                    break;
                } catch (Exception e9) {
                    com.unisound.common.y.a("set ASR_FOURMIC_ISDEBUG Error.");
                    return;
                }
            case 10199:
                try {
                    this.j = ((Boolean) obj).booleanValue();
                    break;
                } catch (Exception e10) {
                    com.unisound.common.y.a("set is4micMode Error.");
                    return;
                }
        }
    }

    protected void startRecord() {
        waitEnd();
        start();
    }

    protected void stopRecord() {
        waitEnd();
    }

    public void waitEnd() {
        this.v.set(false);
        if (isAlive()) {
            try {
                join(3900L);
                com.unisound.common.y.c("FourMicAudioManagerInferface::waitEnd()");
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }
}
