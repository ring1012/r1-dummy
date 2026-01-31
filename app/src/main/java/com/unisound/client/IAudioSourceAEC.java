package com.unisound.client;

import android.os.Environment;
import com.unisound.common.y;
import com.unisound.jni.AEC;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public abstract class IAudioSourceAEC implements IAudioSource {
    private static final String i = "AudioSourceImpl";
    private static final int k = 5;
    AEC h;
    private static boolean j = false;
    private static int l = SpeechConstants.ASR_BEST_RESULT_RETURN;

    /* renamed from: a, reason: collision with root package name */
    byte[] f228a = new byte[l];
    byte[] b = {100};
    int c = 0;
    private boolean m = false;
    private boolean n = false;
    byte[] d = null;
    private BlockingQueue<byte[]> o = new LinkedBlockingQueue();
    private BlockingQueue<byte[]> p = new LinkedBlockingQueue();
    byte[] e = null;
    String f = Environment.getExternalStorageDirectory().getPath() + "/YunZhiSheng/aec/";
    String g = "";
    private float q = 0.0f;
    private float r = 0.0f;
    private int s = 0;
    private boolean t = false;
    private boolean u = false;

    class AECThread extends Thread {
        private AECThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws Throwable {
            super.run();
            while (IAudioSourceAEC.this.isRecordingStart() && !IAudioSourceAEC.this.m) {
                try {
                    byte[] bArr = (byte[]) IAudioSourceAEC.this.p.poll(5L, TimeUnit.MILLISECONDS);
                    if (bArr != null) {
                        if (bArr.length == 1 && bArr[0] == 100) {
                            IAudioSourceAEC.this.m = true;
                        } else {
                            IAudioSourceAEC.saveRecordingData(bArr, IAudioSourceAEC.this.f + IAudioSourceAEC.this.g + "_out_mic.pcm");
                            IAudioSourceAEC.this.e = IAudioSourceAEC.this.h.process(bArr, null);
                        }
                        IAudioSourceAEC.this.a(IAudioSourceAEC.this.e);
                    } else {
                        Thread.sleep(5L);
                    }
                } catch (InterruptedException e) {
                    y.c(IAudioSourceAEC.i, "IAudioSourceAEC runAEC interrupt");
                }
            }
            IAudioSourceAEC.this.e = IAudioSourceAEC.this.h.getlast();
            IAudioSourceAEC.this.a(IAudioSourceAEC.this.e);
            IAudioSourceAEC.this.n = true;
            IAudioSourceAEC.this.a(IAudioSourceAEC.this.b);
        }
    }

    class recordingThread extends Thread {
        private recordingThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            while (IAudioSourceAEC.this.isRecordingStart()) {
                int dataPro = IAudioSourceAEC.this.readDataPro(IAudioSourceAEC.this.f228a, IAudioSourceAEC.this.f228a.length);
                if (dataPro > 0) {
                    if (IAudioSourceAEC.this.p != null) {
                        IAudioSourceAEC.this.p.add(Arrays.copyOfRange(IAudioSourceAEC.this.f228a, 0, dataPro));
                    }
                } else if (dataPro < 0) {
                    IAudioSourceAEC.this.p.add(Arrays.copyOfRange(IAudioSourceAEC.this.b, 0, IAudioSourceAEC.this.b.length));
                }
            }
            IAudioSourceAEC.this.setFirstStartRecording(true);
        }
    }

    public IAudioSourceAEC() {
        this.h = null;
        y.c(i, "IAudioSourceAEC");
        this.h = new AEC(16000, 1);
        this.h.setOptionInt(0, 600);
        this.h.setOptionInt(2, 1);
        this.h.setOptionInt(3, this.s);
    }

    private int a(int i2, int i3) {
        return i2 % i3 == 0 ? i2 / i3 : (i2 / i3) + 1;
    }

    private int a(byte[] bArr, int i2) throws InterruptedException {
        int i3;
        try {
            byte[] bArrPoll = this.o.poll(5L, TimeUnit.MILLISECONDS);
            if (bArrPoll != null) {
                int length = bArrPoll.length;
                try {
                    if (length > l) {
                        length = l;
                    }
                    System.arraycopy(bArrPoll, 0, bArr, 0, length);
                    i3 = length;
                } catch (InterruptedException e) {
                    i3 = length;
                    y.c(i, "IAudioSourceAEC readBuffer interrupt");
                    return i3;
                }
            } else {
                i3 = this.n ? -9 : 0;
            }
        } catch (InterruptedException e2) {
            i3 = 0;
        }
        return i3;
    }

    private void a() {
        this.n = false;
        this.h.reset(c(), d());
        AECThread aECThread = new AECThread();
        aECThread.setPriority(10);
        aECThread.start();
    }

    private static void a(String str) {
        int iLastIndexOf;
        if (str != null && (iLastIndexOf = str.lastIndexOf(47)) >= 0) {
            new File(str.substring(0, iLastIndexOf)).mkdirs();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(byte[] bArr) throws Throwable {
        if (bArr != null) {
            saveRecordingData(bArr, this.f + this.g + "_out_aec.pcm");
            this.o.add(Arrays.copyOfRange(bArr, 0, bArr.length));
        }
    }

    private void b() {
        recordingThread recordingthread = new recordingThread();
        recordingthread.setPriority(10);
        recordingthread.start();
    }

    private float c() {
        return this.q;
    }

    private float d() {
        return this.r;
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x003a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean saveRecordingData(byte[] r4, java.lang.String r5) throws java.lang.Throwable {
        /*
            boolean r0 = com.unisound.client.IAudioSourceAEC.j
            if (r0 == 0) goto L2f
            a(r5)
            r2 = 0
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L36
            java.lang.String r0 = "rw"
            r1.<init>(r5, r0)     // Catch: java.lang.Exception -> L25 java.lang.Throwable -> L36
            long r2 = r1.length()     // Catch: java.lang.Throwable -> L43 java.lang.Exception -> L45
            r1.seek(r2)     // Catch: java.lang.Throwable -> L43 java.lang.Exception -> L45
            r1.write(r4)     // Catch: java.lang.Throwable -> L43 java.lang.Exception -> L45
            r0 = 1
            if (r1 == 0) goto L1f
            r1.close()     // Catch: java.io.IOException -> L20
        L1f:
            return r0
        L20:
            r1 = move-exception
            r1.printStackTrace()
            goto L1f
        L25:
            r0 = move-exception
            r1 = r2
        L27:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L43
            if (r1 == 0) goto L2f
            r1.close()     // Catch: java.io.IOException -> L31
        L2f:
            r0 = 0
            goto L1f
        L31:
            r0 = move-exception
            r0.printStackTrace()
            goto L2f
        L36:
            r0 = move-exception
            r1 = r2
        L38:
            if (r1 == 0) goto L3d
            r1.close()     // Catch: java.io.IOException -> L3e
        L3d:
            throw r0
        L3e:
            r1 = move-exception
            r1.printStackTrace()
            goto L3d
        L43:
            r0 = move-exception
            goto L38
        L45:
            r0 = move-exception
            goto L27
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.client.IAudioSourceAEC.saveRecordingData(byte[], java.lang.String):boolean");
    }

    public boolean isFirstStartRecording() {
        return this.u;
    }

    public boolean isRecordingStart() {
        return this.t;
    }

    @Override // com.unisound.client.IAudioSource
    public int readData(byte[] bArr, int i2) {
        if (isFirstStartRecording()) {
            setFirstStartRecording(false);
            this.g = String.valueOf(System.currentTimeMillis());
            if (this.p != null) {
                this.p.clear();
            }
            if (this.o != null) {
                this.o.clear();
            }
            b();
            this.m = false;
            a();
        }
        return a(bArr, i2);
    }

    public abstract int readDataPro(byte[] bArr, int i2);

    public void release() {
        this.h.release();
        this.h = null;
    }

    public void setDebug(boolean z) {
        j = z;
    }

    public void setEngineDebug(boolean z) {
        if (z) {
            if (this.h != null) {
                this.h.setOptionInt(4, 1);
            }
        } else if (this.h != null) {
            this.h.setOptionInt(4, 0);
        }
    }

    public void setFirstStartRecording(boolean z) {
        this.u = z;
    }

    public int setFrameAjustUnit(int i2) {
        if (this.h != null) {
            return this.h.setOptionInt(5, i2);
        }
        return 0;
    }

    public void setMicChannel(int i2) {
        if (this.h != null) {
            this.h.setOptionInt(3, i2);
        }
    }

    public void setRecordingStart(boolean z) {
        this.t = z;
    }

    @Override // com.unisound.client.IAudioSource
    public int writeData(byte[] bArr, int i2) {
        int length = bArr.length;
        if (length > 0) {
            return writeDataPro(Arrays.copyOfRange(bArr, 0, length), i2);
        }
        return 0;
    }

    public abstract int writeDataPro(byte[] bArr, int i2);
}
