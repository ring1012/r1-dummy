package cn.yunzhisheng.asrfix;

import cn.yunzhisheng.common.PinyinConverter;
import com.unisound.client.ErrorCode;
import com.unisound.common.y;
import com.unisound.sdk.w;

/* loaded from: classes.dex */
class a extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JniAsrFix f29a;
    private w b;

    public a(JniAsrFix jniAsrFix, w wVar) {
        this.f29a = jniAsrFix;
        this.b = wVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        y.g("Recognizer.loadModel Reset thread start");
        y.c("Recognizer.loadModel Reset thread start");
        this.f29a.T = true;
        synchronized (JniAsrFix.D) {
            this.f29a.S = false;
            while (!this.f29a.S) {
                if (!this.f29a.N) {
                    this.f29a.O = 1503;
                    this.f29a.P.a(this.f29a.O);
                    while (!this.f29a.B.isEmpty()) {
                        String strPoll = this.f29a.B.poll();
                        String strPoll2 = this.f29a.B.poll();
                        String strPoll3 = this.f29a.B.poll();
                        y.c("Recognizer.loadModel reseting ", strPoll, PinyinConverter.PINYIN_SEPARATOR, strPoll2);
                        int iReset = this.f29a.reset(strPoll, "");
                        this.f29a.O = 1501;
                        if (iReset == 0) {
                            this.f29a.P.a(this.f29a.O);
                            if ("wakeup" == strPoll2) {
                                this.f29a.P.a(3105, (int) System.currentTimeMillis());
                            } else if ("command" == strPoll2) {
                                this.b.a(strPoll3, true);
                                this.f29a.P.a(1130, (int) System.currentTimeMillis());
                            } else {
                                y.a("Recognizer.loadModel no cmd type error");
                            }
                            y.c("Recognizer.loadModel reset ok");
                        } else {
                            if ("command" == strPoll2) {
                                this.b.a(strPoll3, false);
                            }
                            this.f29a.P.b(1300, ErrorCode.ASR_SDK_FIX_LOADGRAMMAR_ERROR);
                            y.a("Recognizer.loadModel reset error:" + iReset);
                        }
                    }
                    this.f29a.T = false;
                    this.f29a.S = true;
                    this.f29a.O = 1501;
                }
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        y.c("Recognizer.loadModel Reset thread stop");
        y.g("Recognizer.loadModel Reset thread stop");
    }
}
