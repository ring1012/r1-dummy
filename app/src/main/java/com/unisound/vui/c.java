package com.unisound.vui;

import android.content.Context;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;
import com.unisound.sdk.bw;
import com.unisound.vui.util.LogMgr;

/* loaded from: classes.dex */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private static final String f362a = c.class.getSimpleName();
    private final SpeechSynthesizerListener b = new SpeechSynthesizerListener() { // from class: com.unisound.vui.c.1
        @Override // com.unisound.client.SpeechSynthesizerListener
        public void onError(int type, String errorMSG) {
            if (c.this.d != null) {
                c.this.d.onError(type, errorMSG);
            }
        }

        @Override // com.unisound.client.SpeechSynthesizerListener
        public void onEvent(int type) {
            if (c.this.d != null) {
                c.this.d.onEvent(type);
            }
        }
    };
    private final SpeechSynthesizer c;
    private d d;

    public c(Context context, String str, String str2) {
        this.c = new SpeechSynthesizer(context, str, str2);
        this.c.setTTSListener(this.b);
    }

    private void c(String str) {
        String str2;
        if (str.contains("lingling")) {
            str2 = "lzl";
        } else if (str.contains("tangtang")) {
            str2 = "tangtang";
        } else if (str.contains("tiantian")) {
            str2 = "boy";
        } else if (str.contains("xiaofeng")) {
            str2 = "xiaoming";
        } else if (str.contains("xiaowen")) {
            str2 = bw.j;
        } else if (str.contains("xuanxuan")) {
            str2 = "sweet";
        } else {
            LogMgr.w(f362a, "Local tts speaker name : '" + str + "' is invalid, 'sweet' tts speaker is set for online");
            str2 = "sweet";
        }
        this.c.setOption(2005, str2);
    }

    private boolean d(String str) {
        return e(str) && !f(str);
    }

    private boolean e(String str) {
        return str.matches(".*[a-zA-Z]{2,}.*");
    }

    private boolean f(String str) {
        return str.startsWith("<PCM>") || str.startsWith("<WAV>");
    }

    public int a(String str) {
        return this.c.init(str);
    }

    public Object a(int i) {
        return this.c.getOption(i);
    }

    public void a(int i, Object obj) {
        if (i == 2031 || i == 2032) {
            LogMgr.d(f362a, "Setting local tts speaker, switch online tts speaker also");
            c(obj.toString());
        }
        this.c.setOption(i, obj);
    }

    public void a(int i, String str) {
        this.c.release(i, str);
    }

    public void a(d dVar) {
        this.d = dVar;
    }

    public void a(byte[] bArr) {
        this.c.playBuffer(bArr);
    }

    public boolean a() {
        return this.c.isPlaying();
    }

    public int b() {
        return this.c.cancel();
    }

    public int b(String str) {
        int iIntValue = ((Integer) this.c.getOption(2020)).intValue();
        LogMgr.d(f362a, "ttsServiceMode=" + iIntValue);
        if (d(str)) {
            if (iIntValue != 3) {
                this.c.setOption(2020, 3);
            }
        } else if (iIntValue != 1) {
            this.c.setOption(2020, 1);
        }
        return this.c.playText(str);
    }

    public void c() {
        this.c.stop();
    }
}
