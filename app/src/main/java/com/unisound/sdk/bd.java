package com.unisound.sdk;

import android.os.Looper;
import android.os.Message;
import com.unisound.client.SpeechSynthesizerListener;

/* loaded from: classes.dex */
class bd extends com.unisound.common.ab {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ az f311a;

    public bd(az azVar) {
        this.f311a = azVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public bd(az azVar, Looper looper) {
        super(looper);
        this.f311a = azVar;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.unisound.common.ab
    public boolean a(Message message) {
        SpeechSynthesizerListener speechSynthesizerListener = this.f311a.f;
        if (speechSynthesizerListener == null) {
            return false;
        }
        switch (message.what) {
            case 101:
                speechSynthesizerListener.onEvent(2101);
                return true;
            case 102:
                speechSynthesizerListener.onEvent(2102);
                return true;
            case 103:
                speechSynthesizerListener.onEvent(2103);
                return true;
            case 104:
                speechSynthesizerListener.onEvent(2104);
                return true;
            case 105:
                speechSynthesizerListener.onEvent(2105);
                return true;
            case 106:
                speechSynthesizerListener.onEvent(2106);
                return true;
            case 107:
                speechSynthesizerListener.onEvent(2107);
                return true;
            case 108:
                speechSynthesizerListener.onEvent(2108);
                return true;
            case 109:
                speechSynthesizerListener.onEvent(2109);
                return true;
            case 110:
                return true;
            case com.unisound.common.ad.r /* 111 */:
                speechSynthesizerListener.onEvent(2111);
                return true;
            case com.unisound.common.ad.s /* 112 */:
                speechSynthesizerListener.onEvent(2112);
                return true;
            case com.unisound.common.ad.u /* 114 */:
                speechSynthesizerListener.onEvent(2114);
                return true;
            case 200:
                speechSynthesizerListener.onError(2301, (String) message.obj);
                return true;
            case 201:
                speechSynthesizerListener.onError(2301, (String) message.obj);
                return true;
            case 202:
                speechSynthesizerListener.onError(2301, (String) message.obj);
                return true;
            case com.unisound.common.ad.d /* 210 */:
                speechSynthesizerListener.onError(2301, (String) message.obj);
                return true;
            case com.unisound.common.ad.e /* 211 */:
                speechSynthesizerListener.onError(2301, (String) message.obj);
                return true;
            case com.unisound.common.ad.f /* 212 */:
                speechSynthesizerListener.onError(2301, (String) message.obj);
                return true;
            case com.unisound.common.ad.g /* 225 */:
                speechSynthesizerListener.onError(2301, (String) message.obj);
                return true;
            default:
                return false;
        }
    }

    @Override // com.unisound.common.ab
    public void sendMessage(int i) {
        super.sendMessage(i);
    }
}
