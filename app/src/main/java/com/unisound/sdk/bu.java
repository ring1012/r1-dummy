package com.unisound.sdk;

import cn.yunzhisheng.tts.JniClient;
import com.unisound.client.ErrorCode;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;

/* loaded from: classes.dex */
public class bu extends bs {
    byte[] c;
    private bz d;
    private bw e;
    private String f;

    public bu(String str, bw bwVar) {
        super(bwVar.r().booleanValue(), bwVar.m());
        this.d = null;
        this.f = str;
        this.e = bwVar;
    }

    private String a(bw bwVar) {
        StringBuilder sb = new StringBuilder();
        if (bwVar != null) {
            int iV = bwVar.v();
            if (iV != 50) {
                sb.append("pit=").append(iV).append(";");
            }
            int iU = bwVar.u();
            if (iU != 50) {
                sb.append("spd=").append(iU).append(";");
            }
            int iW = bwVar.w();
            if (iW != 50) {
                sb.append("vol=").append(iW).append(";");
            }
            String strT = bwVar.t();
            if (strT != bw.j) {
                sb.append("vcn=").append(strT).append(";");
            }
            int iO = bwVar.o();
            if (iO != 100) {
                sb.append("smt=").append(iO).append(";");
            }
            int iP = bwVar.p();
            if (iP != 100) {
                sb.append("emt=").append(iP).append(";");
            }
            boolean zBooleanValue = bwVar.n().booleanValue();
            if (zBooleanValue) {
                sb.append("e2c=").append(zBooleanValue).append(";");
            }
            int iY = bwVar.y();
            if (iY != 22050) {
                sb.append("sampleRate=").append(iY).append(";");
            }
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }

    public static String a(String str) {
        if (str != null) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private void a(byte[] bArr) {
        bz bzVar = this.d;
        if (bzVar != null) {
            bzVar.a(bArr, bArr.length);
        }
    }

    private void b(int i) {
        bz bzVar = this.d;
        if (bzVar != null) {
            bzVar.a(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b(boolean z) {
        if (this.e.D().intValue() != 3 || !z) {
            return false;
        }
        k();
        return true;
    }

    private void i() {
        bz bzVar = this.d;
        if (bzVar != null) {
            bzVar.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        bz bzVar = this.d;
        if (bzVar != null) {
            bzVar.b();
        }
    }

    private void k() {
        bz bzVar = this.d;
        if (bzVar != null) {
            bzVar.a(this.f);
        }
    }

    public void a(bz bzVar) {
        this.d = bzVar;
    }

    @Override // com.unisound.sdk.bs
    public void b() {
        super.b();
    }

    public void b(bz bzVar) {
        this.d = bzVar;
    }

    public void g() {
        this.d = null;
    }

    public String h() {
        return null;
    }

    @Override // com.unisound.common.f, java.lang.Thread, java.lang.Runnable
    public void run() {
        com.unisound.common.a aVarF;
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("TTSOnlineSynthesizerThread ->run ThreadName = ", Thread.currentThread().getName(), ", ThreadId = ", Long.valueOf(Thread.currentThread().getId()));
        }
        super.run();
        com.unisound.common.y.b("TTSOnlineSynthesizerThread run()：synthesizer start");
        JniClient jniClient = new JniClient();
        try {
            aVarF = this.e.f();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jniClient.b();
        }
        if (!jniClient.a(this.e.B(), aVarF.b(), aVarF.c())) {
            com.unisound.common.y.a("TTSOnlineSynthesizerThread getTTSData: TTSThread:create error appkey: " + this.e.B() + " server:" + aVarF.b() + " ip:" + aVarF.a() + " port: " + aVarF.c());
            b(ErrorCode.TTS_ERROR_ONLINE_SYNTHESIZER_INIT);
            return;
        }
        jniClient.a(8, com.unisound.c.a.q);
        jniClient.a(14, com.unisound.c.a.a(this.e.B()));
        jniClient.a(22, com.unisound.c.a.a(this.e.B()));
        StringBuilder sb = new StringBuilder();
        sb.append(com.unisound.c.a.s).append(":");
        sb.append(0).append(":");
        sb.append(com.unisound.c.a.r).append(":");
        sb.append(this.e.f325a).append(":");
        sb.append(com.unisound.c.a.t).append(":");
        sb.append(com.unisound.common.an.f242a);
        sb.append("\t" + com.unisound.common.y.x + ":" + JniClient.c + ":" + JniClient.d);
        com.unisound.common.y.b("TTSOnlineSynthesizerThread getTTSData: TTS_OPT_CLIENT_INFO: ", sb.toString());
        jniClient.a(15, sb.toString());
        String strA = a(this.e);
        if (strA != null) {
            com.unisound.common.y.b("TTSOnlineSynthesizerThread getTTSData: ParamString(): ", strA);
            jniClient.a(104, strA);
        }
        if (this.e.k() != null) {
            jniClient.a(203, this.e.k());
        }
        if (this.e.C() != null) {
            jniClient.a(204, this.e.C());
        }
        c cVarB = this.e.b();
        int iA = jniClient.a(cVarB.b(), cVarB.a());
        if (iA != 0) {
            com.unisound.common.y.a("TTSOnlineSynthesizerThread getTTSData: jni.start error " + iA + " audioFormat.toParamString(): " + cVarB.b() + " audioFormat.getEncode(): " + cVarB.a());
            b(iA);
            return;
        }
        int iB = jniClient.b(this.f);
        if (iB != 0) {
            com.unisound.common.y.a("TTSOnlineSynthesizerThread getTTSData: jni.textPut error " + iB);
            b(iB);
            return;
        }
        i();
        System.currentTimeMillis();
        int i = 0;
        while (!a() && jniClient.m.b != 2 && i < 10) {
            com.unisound.common.y.f("TTSOnlineSynthesizerThread run : jni.getResult() before");
            Timer timer = new Timer();
            if (this.e.D().intValue() == 3) {
                timer.schedule(new bv(this, jniClient), this.e.L());
            }
            this.c = jniClient.d();
            timer.cancel();
            com.unisound.common.y.f("TTSOnlineSynthesizerThread run : jni.getResult() after");
            if (this.c == null) {
                i++;
                int i2 = jniClient.m.c;
                if (i2 == 0) {
                    continue;
                } else if (b(true)) {
                    break;
                } else {
                    com.unisound.common.y.a("TTSOnlineSynthesizerThread getTTSData: jni.getResult() error" + i2);
                }
            } else {
                System.currentTimeMillis();
                a(this.c);
                i = 0;
            }
        }
        jniClient.c();
        j();
        com.unisound.common.y.c("TTSOnlineSynthesizerThread run()：synthesizer end");
    }
}
