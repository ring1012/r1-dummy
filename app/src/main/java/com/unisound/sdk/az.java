package com.unisound.sdk;

import android.content.Context;
import android.os.HandlerThread;
import android.text.TextUtils;
import cn.yunzhisheng.tts.offline.lib.YzsTts;
import com.unisound.client.ErrorCode;
import com.unisound.client.IAudioSource;
import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizerListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class az {
    private static Object t = new Object();
    private YzsTts b;
    private bt c;
    private bu d;
    private bx e;
    private SpeechSynthesizerListener f;
    private Context h;
    private IAudioSource l;
    private be m;
    private HandlerThread o;
    private com.unisound.common.ab p;
    private com.unisound.common.ag q;
    private String g = "16000";
    private int i = 0;
    private bw j = bw.a();
    private String k = "";
    private Integer n = 2;
    private bz r = new ba(this);
    private by s = new bb(this);

    /* renamed from: a, reason: collision with root package name */
    cf f306a = new cf(new bc(this));

    public az(Context context, String str, String str2) {
        this.h = context;
        this.j.b(str);
        this.j.c(str2);
        this.p = new bd(this, context.getMainLooper());
        this.q = new com.unisound.common.ag(context);
    }

    private int a(String str, be beVar) {
        this.m = beVar;
        if (this.p == null) {
            this.f.onError(2301, ErrorCode.toJsonMessage(ErrorCode.GENERAL_INIT_ERROR));
            return -1;
        }
        if (TextUtils.isEmpty(str)) {
            com.unisound.common.y.a("SpeechSynthesizerInterface beginTts: text is unusable");
            sendMsg(this.p, 202, ErrorCode.toJsonMessage(ErrorCode.TTS_ERROR_TEXT_UNUSEABLE));
            return -1;
        }
        h();
        c();
        f();
        i();
        d();
        g();
        if (this.c != null) {
            this.c.b(10000);
        }
        if (this.e != null) {
            this.e.c(10000);
        }
        if (this.n.intValue() == 1 || k()) {
            this.j.s("22050");
            this.c = null;
            this.c = new bt(str, this.j);
            this.c.setName("TTSOfflineSynthesizerThread");
            this.c.a(this.r);
        } else {
            this.j.s(this.g);
            try {
                this.j.f325a = com.unisound.c.a.b();
            } catch (Exception e) {
                com.unisound.common.y.c("SpeechSynthesizerInterface getNetType error");
            }
            this.d = null;
            this.d = new bu(str, this.j);
            this.d.setName("TTSOnlineSynthesizerThread");
            this.d.a(this.r);
        }
        a();
        if (this.n.intValue() == 1 || k()) {
            this.c.start();
            com.unisound.common.y.b("SpeechSynthesizerInterface beginTts: mOfflineSynthesizeThread.start()");
        } else {
            this.d.start();
            com.unisound.common.y.b("SpeechSynthesizerInterface beginTts: mOnlineSynthesizerThread.start(text)");
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Object a(Object... objArr) {
        if (this.b.a((String) objArr[0], (String) objArr[1], (String) objArr[2], (String) objArr[3])) {
            sendEmptyMsg(this.p, 101);
            return true;
        }
        sendMsg(this.p, 201, ErrorCode.toJsonMessage(ErrorCode.TTS_ERROR_LOAD_MODEL));
        return false;
    }

    private void a() {
        this.e = null;
        this.e = new bx(this.j);
        this.e.setName("TTSPlayThread");
        this.e.a(this.s);
        this.e.a(this.l);
        if (this.m == be.onlySyn) {
            this.e.a((Boolean) false);
            com.unisound.common.y.b("SpeechSynthesizerInterface beginTts: onlySynthesize executed");
        }
        this.e.start();
        com.unisound.common.y.b("SpeechSynthesizerInterface beginTts: mTTSPlayThread.start()");
    }

    private void b() {
        if (this.q.a(SpeechConstants.PERMISSION_ACCESS_FINE_LOCATION) != 0) {
            com.unisound.common.y.a("no ACCESS_FINE_LOCATION permission");
            if (this.f != null) {
                this.f.onEvent(SpeechConstants.GENERAL_EVENT_REQUIRE_ACCESS_FINE_LOCATION_PERMISSION);
            }
        }
        if (this.q.a(SpeechConstants.PERMISSION_READ_PHONE_STATE) != 0) {
            com.unisound.common.y.a("no READ_PHONE_STATE permission");
            if (this.f != null) {
                this.f.onEvent(1151);
            }
        }
        if (this.q.a(SpeechConstants.PERMISSION_WRITE_EXTERNAL_STORAGE) != 0) {
            com.unisound.common.y.a("no WRITE_EXTERNAL_STORAGE permission");
            if (this.f != null) {
                this.f.onEvent(SpeechConstants.GENERAL_EVENT_REQUIRE_WRITE_EXTERNAL_STORAGE_PERMISSION);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str) {
        this.j.s("22050");
        h();
        f();
        i();
        g();
        this.c = null;
        this.c = new bt(str, this.j);
        this.c.setName("TTSOfflineSynthesizerThread");
        this.c.a(this.r);
        a();
        this.c.start();
        com.unisound.common.y.b("SpeechSynthesizerInterface changeTts: mOfflineSynthesizeThread.start()");
    }

    private int c() {
        if (this.c == null) {
            return -1;
        }
        this.c.h();
        return 0;
    }

    private int d() {
        if (this.c == null) {
            return -1;
        }
        this.c.b();
        return 0;
    }

    private int e() {
        if (this.c == null) {
            return -1;
        }
        this.c.g();
        return 0;
    }

    private int f() {
        if (this.d == null) {
            return -1;
        }
        this.d.g();
        return 0;
    }

    private int g() {
        if (this.d == null) {
            return -1;
        }
        this.d.b();
        return 0;
    }

    private int h() {
        if (this.e == null) {
            return -1;
        }
        this.e.j();
        return 0;
    }

    private int i() {
        if (this.e == null) {
            return -1;
        }
        this.e.b();
        return 0;
    }

    private void j() {
        if (this.p.hasMessages(107)) {
            com.unisound.common.y.b("SpeechSynthesizerInterface removeMessage : remvoeMessage = ", 107);
            this.p.removeMessages(107);
        }
        if (this.p.hasMessages(103)) {
            com.unisound.common.y.b("SpeechSynthesizerInterface removeMessage : remvoeMessage = ", 103);
            this.p.removeMessages(103);
        }
        if (this.p.hasMessages(102)) {
            com.unisound.common.y.b("SpeechSynthesizerInterface removeMessage : remvoeMessage = ", 102);
            this.p.removeMessages(102);
        }
        if (this.p.hasMessages(104)) {
            com.unisound.common.y.b("SpeechSynthesizerInterface removeMessage : remvoeMessage = ", 104);
            this.p.removeMessages(104);
        }
        if (this.p.hasMessages(105)) {
            com.unisound.common.y.b("SpeechSynthesizerInterface removeMessage : remvoeMessage = ", 105);
            this.p.removeMessages(105);
        }
        if (this.p.hasMessages(106)) {
            com.unisound.common.y.b("SpeechSynthesizerInterface removeMessage : remvoeMessage = ", 106);
            this.p.removeMessages(106);
        }
        if (this.p.hasMessages(101)) {
            com.unisound.common.y.b("SpeechSynthesizerInterface removeMessage : remvoeMessage = ", 101);
            this.p.removeMessages(101);
        }
        if (this.p.hasMessages(108)) {
            com.unisound.common.y.b("SpeechSynthesizerInterface removeMessage : remvoeMessage = ", 108);
            this.p.removeMessages(108);
        }
        if (this.p.hasMessages(109)) {
            com.unisound.common.y.b("SpeechSynthesizerInterface removeMessage : remvoeMessage = ", 109);
            this.p.removeMessages(109);
        }
        if (this.p.hasMessages(com.unisound.common.ad.r)) {
            com.unisound.common.y.b("SpeechSynthesizerInterface removeMessage : remvoeMessage = ", Integer.valueOf(com.unisound.common.ad.r));
            this.p.removeMessages(com.unisound.common.ad.r);
        }
        if (this.p.hasMessages(com.unisound.common.ad.s)) {
            com.unisound.common.y.b("SpeechSynthesizerInterface removeMessage : remvoeMessage = ", Integer.valueOf(com.unisound.common.ad.s));
            this.p.removeMessages(com.unisound.common.ad.s);
        }
    }

    private boolean k() {
        return this.j.D().intValue() == 3 && !com.unisound.common.ae.b(this.h);
    }

    private void l() {
        com.unisound.common.y.b("SpeechSynthesizerInterface switchSpeeker begin");
        if (this.b == null) {
            sendMsg(this.p, com.unisound.common.ad.g, ErrorCode.toJsonMessage(ErrorCode.TTS_ERROR_OFFLINE_ENGINE_NOT_INIT));
            return;
        }
        int iA = this.b.a(this.j.g());
        switch (iA) {
            case ErrorCode.TTS_ERROR_OFFLINE_CHANGE_SPEAKER_FAIL /* -73310 */:
                sendMsg(this.p, com.unisound.common.ad.g, ErrorCode.toJsonMessage(iA));
                break;
            case ErrorCode.TTS_ERROR_OFFLINE_ENGINE_IS_PROCESSING /* -73309 */:
                sendMsg(this.p, com.unisound.common.ad.g, ErrorCode.toJsonMessage(iA));
                break;
            case ErrorCode.TTS_ERROR_OFFLINE_ENGINE_NOT_INIT /* -73308 */:
                sendMsg(this.p, com.unisound.common.ad.g, ErrorCode.toJsonMessage(iA));
                break;
            case com.unisound.common.ad.u /* 114 */:
                sendEmptyMsg(this.p, iA);
                break;
        }
        com.unisound.common.y.b("SpeechSynthesizerInterface switchSpeeker end");
    }

    private void m() {
        List<Integer> listZ = this.j.z();
        if (listZ.size() > 0) {
            for (Integer num : listZ) {
                this.b.d(num.intValue());
                com.unisound.common.y.c("setTtsField....", num);
            }
            listZ.clear();
        }
    }

    public static void sendEmptyMsg(com.unisound.common.ab abVar, int i) {
        synchronized (t) {
            if (com.unisound.common.y.l) {
                com.unisound.common.y.d("SpeechSynthesizerInterface ->sendEmptyMsg what = ", Integer.valueOf(i));
            }
            abVar.sendEmptyMessage(i);
        }
    }

    public static void sendMsg(com.unisound.common.ab abVar, int i, String str) {
        synchronized (t) {
            if (com.unisound.common.y.l) {
                com.unisound.common.y.d("SpeechSynthesizerInterface ->sendMsg what = ", Integer.valueOf(i), " obj = ", str);
            }
            abVar.sendMessage(i, str);
        }
    }

    protected int a(String str) {
        return 0;
    }

    protected int a(String str, String str2) {
        return 0;
    }

    protected int cancel() {
        int iH;
        int iC;
        int iF;
        com.unisound.common.y.c("SpeechSynthesizerInterface cancel begin");
        synchronized (t) {
            iH = h();
            iC = c();
            iF = f();
            j();
        }
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("SpeechSynthesizerInterface->cancel:stop() start");
        }
        stop();
        if (com.unisound.common.y.l) {
            com.unisound.common.y.d("SpeechSynthesizerInterface->cancel:stop() end");
        }
        if ((iF == iC || iH == -1) && com.unisound.common.y.l) {
            com.unisound.common.y.d("SpeechSynthesizerInterface cancel end -1");
        }
        com.unisound.common.y.b("SpeechSynthesizerInterface cancel end");
        return 0;
    }

    protected Object getOption(int i) {
        switch (i) {
            case 1036:
                return com.unisound.c.a.a(this.j.B());
            case 2001:
                return Integer.valueOf(this.j.u());
            case 2002:
                return Integer.valueOf(this.j.v());
            case 2003:
                return Integer.valueOf(this.j.w());
            case 2004:
                return Integer.valueOf(this.j.y());
            case 2005:
                return this.j.t();
            case 2011:
                return this.j.f().a();
            case 2012:
                return Integer.valueOf(this.j.x());
            case 2013:
                return Integer.valueOf(this.j.A());
            case 2014:
                return this.j.r();
            case 2020:
                return this.j.D();
            case 2033:
                if (this.b != null) {
                    return this.b.f();
                }
                sendMsg(this.p, com.unisound.common.ad.g, ErrorCode.toJsonMessage(ErrorCode.TTS_ERROR_GET_ENGINE_INFO));
                return "";
            case SpeechConstants.TTS_KEY_VOICE_LOUD /* 2040 */:
                return Integer.valueOf(this.j.J());
            default:
                return null;
        }
    }

    public bw getParams() {
        return this.j;
    }

    public String getSessionId() {
        return this.k;
    }

    protected int getStatus() {
        return this.i;
    }

    protected String getVersion() {
        return com.unisound.common.an.f242a;
    }

    protected int init(String str) {
        b();
        try {
            com.unisound.c.a.a(this.h);
        } catch (Exception e) {
            e.printStackTrace();
            if (this.f != null) {
                this.f.onError(2301, ErrorCode.toJsonMessage(ErrorCode.GENERAL_NO_READ_PHONE_STATE_PERMISSION));
            }
        }
        if (str != null && !str.equals("")) {
            Map<Integer, Object> mapA = com.unisound.common.v.a(str, this.j.E());
            Iterator<Integer> it = mapA.keySet().iterator();
            while (it.hasNext()) {
                int iIntValue = it.next().intValue();
                if (mapA.get(Integer.valueOf(iIntValue)) != null) {
                    setOption(iIntValue, mapA.get(Integer.valueOf(iIntValue)));
                }
            }
            com.unisound.common.y.b("SpeechSynthesizerInterface init: jsonString init param executed");
        }
        if (this.j.l()) {
            this.o = new HandlerThread("ht_outer");
            this.o.start();
            this.p = new bd(this, this.o.getLooper());
        }
        if (this.n.intValue() == 1 || this.n.intValue() == 3) {
            com.unisound.common.y.b("SpeechSynthesizerInterface init: TTS_SERVICE_MODE_LOCAL");
            this.b = YzsTts.b();
            com.unisound.common.y.b("getDicModelPath= ", this.j.h(), " getSpeakerModelPath= ", this.j.g(), " getAnnotationFilePath = ", this.j.i(), " getUserDictFilePath = ", this.j.j());
            if (!this.f306a.c() && !this.b.d()) {
                this.f306a.a(this.j.h(), this.j.g(), this.j.i(), this.j.j());
                this.f306a.a();
                com.unisound.common.y.b("SpeechSynthesizerInterface init: asyncTask.start()");
            } else if (this.b.d()) {
                sendEmptyMsg(this.p, 101);
                com.unisound.common.y.b("SpeechSynthesizerInterface init: mTts.isInit()");
            }
        } else if (this.n.intValue() == 2) {
            com.unisound.common.y.b("SpeechSynthesizerInterface init: TTS_SERVICE_MODE_NET");
        }
        return 0;
    }

    public boolean isPlaying() {
        if (this.e != null) {
            return this.e.k();
        }
        return false;
    }

    protected void pause() {
        com.unisound.common.y.b("SpeechSynthesizerInterface pause begin");
        if (this.e != null) {
            this.e.d();
        }
        com.unisound.common.y.b("SpeechSynthesizerInterface pause end");
    }

    protected void playBuffer(byte[] bArr) {
        if (this.e != null) {
            this.e.c(10000);
        }
        this.e = null;
        this.e = new bx(this.j);
        this.e.setName("TTSPlayThread");
        this.e.a(this.s);
        this.e.a(this.l);
        this.e.start();
        this.e.a(bArr);
        this.e.h();
    }

    protected void playSynWav() {
        if (this.e != null) {
            this.e.a((Boolean) true);
        }
    }

    protected int playText(String str) {
        return a(str, be.synAndPlay);
    }

    protected int release(int i, String str) {
        int iE;
        com.unisound.common.y.b("SpeechSynthesizerInterface release begin");
        switch (i) {
            case 2401:
                stop();
                if (this.c != null) {
                    iE = e();
                } else {
                    if (this.b != null) {
                        this.b.c();
                        this.b = null;
                        sendEmptyMsg(this.p, com.unisound.common.ad.s);
                    }
                    iE = 0;
                }
                if (this.c != null) {
                    this.c.b(10000);
                }
                if (this.e != null) {
                    this.e.c(10000);
                    break;
                }
                break;
            default:
                com.unisound.common.y.a("SpeechSynthesizerInterface release : release type error");
                iE = 0;
                break;
        }
        com.unisound.common.y.b("SpeechSynthesizerInterface release end");
        return iE;
    }

    protected void resume() {
        com.unisound.common.y.b("SpeechSynthesizerInterface resume begin");
        if (this.e != null) {
            this.e.f();
        }
        com.unisound.common.y.b("SpeechSynthesizerInterface resume end");
    }

    protected int setAudioSource(IAudioSource iAudioSource) {
        this.l = iAudioSource;
        return 0;
    }

    protected void setOption(int i, Object obj) {
        switch (i) {
            case 1:
                this.j.E(obj);
                break;
            case 2001:
                this.j.n(obj);
                break;
            case 2002:
                this.j.o(obj);
                break;
            case 2003:
                this.j.p(obj);
                break;
            case 2004:
                this.j.s(obj);
                this.g = (String) obj;
                break;
            case 2005:
                this.j.m(obj);
                break;
            case 2011:
                this.j.u(obj);
                break;
            case 2012:
                this.j.q(obj);
                break;
            case 2013:
                this.j.v(obj);
                break;
            case 2014:
                this.j.k(obj);
                com.unisound.common.y.k = this.j.r().booleanValue();
                com.unisound.c.a.b(((Boolean) obj).booleanValue());
                break;
            case 2015:
                this.j.l(obj);
                break;
            case 2016:
                this.j.j(obj);
                break;
            case 2020:
                this.n = (Integer) obj;
                this.j.x(obj);
                break;
            case 2021:
                this.j.g(obj);
                break;
            case 2022:
                this.j.h(obj);
                break;
            case 2023:
                this.j.i(obj);
                break;
            case 2024:
                this.j.f(obj);
                break;
            case 2025:
                this.j.e(obj);
                break;
            case 2030:
                this.j.b(obj);
                break;
            case 2031:
                this.j.a(obj);
                break;
            case 2032:
                cancel();
                com.unisound.common.y.b("SpeechSynthesizerInterface setOption switch backend_model: ", obj);
                this.j.a(obj);
                l();
                break;
            case SpeechConstants.TTS_KEY_ANNOTATION_FILE_PATH /* 2034 */:
                this.j.c(obj);
                break;
            case 2035:
                this.j.d(obj);
                break;
            case SpeechConstants.TTS_KEY_USE_SENTENCE_MODE /* 2036 */:
                this.j.z(obj);
                break;
            case SpeechConstants.TTS_KEY_SENTENCE_MAX_TIME /* 2037 */:
                this.j.y(obj);
                break;
            case SpeechConstants.TTS_KEY_SENTENCE_BUFFER_TIMES /* 2038 */:
                this.j.A(obj);
                break;
            case SpeechConstants.TTS_KEY_DEBUG_RTF /* 2039 */:
                this.j.B(obj);
                break;
            case SpeechConstants.TTS_KEY_VOICE_LOUD /* 2040 */:
                this.j.C(obj);
                break;
            case SpeechConstants.TTS_KEY_DOMAIN /* 2041 */:
                this.j.D(obj);
                break;
        }
    }

    public void setServer(String str, short s) {
        this.j.a(new String(str), s);
    }

    protected void setTTSListener(SpeechSynthesizerListener speechSynthesizerListener) {
        this.f = speechSynthesizerListener;
    }

    protected void stop() {
        com.unisound.common.y.c("SpeechSynthesizerInterface stop begin");
        i();
        d();
        g();
        com.unisound.common.y.c("SpeechSynthesizerInterface stop end");
    }

    protected void synthesizeText(String str) {
        a(str, be.onlySyn);
    }
}
