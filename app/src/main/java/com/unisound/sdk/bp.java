package com.unisound.sdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import com.unisound.client.ErrorCode;
import com.unisound.client.SpeechConstants;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/* loaded from: classes.dex */
class bp extends Handler {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bg f321a;

    public bp(bg bgVar) {
        this.f321a = bgVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public bp(bg bgVar, Looper looper) {
        super(looper);
        this.f321a = bgVar;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onEvent(3103, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 5:
                com.unisound.common.y.c("recognition End");
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onEvent(1107, (int) System.currentTimeMillis());
                }
                this.f321a.u.clear();
                this.f321a.v.clear();
                this.f321a.w.clear();
                com.unisound.common.v.a();
                this.f321a.au = false;
                break;
            case 6:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onEvent(1118, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 7:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onEvent(1119, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 11:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onEvent(1101, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 12:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onEvent(1102, (int) System.currentTimeMillis());
                    this.f321a.av = true;
                    if (this.f321a.au) {
                        this.f321a.z.onEvent(1107, (int) System.currentTimeMillis());
                        break;
                    }
                }
                break;
            case 13:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onEvent(1104, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 14:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onEvent(1131, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 15:
                if (this.f321a.z != null) {
                    this.f321a.z.onEvent(1117, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 16:
                if (this.f321a.z != null) {
                    this.f321a.z.onEvent(1108, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 17:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onEvent(1122, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 18:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onEvent(1103, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 20:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onEvent(1105, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 21:
                if (this.f321a.z != null) {
                    this.f321a.z.onEvent(1131, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 22:
                if (this.f321a.z != null) {
                    this.f321a.z.onEvent(1123, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 23:
                if (this.f321a.z != null) {
                    this.f321a.z.onEvent(SpeechConstants.ASR_EVENT_COMPILE_WAKEUP_WORD_DONE, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 24:
                if (this.f321a.z != null) {
                    this.f321a.z.onEvent(SpeechConstants.ASR_EVENT_COMPILER_INIT_DONE, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case MqttConnectOptions.CONNECTION_TIMEOUT_DEFAULT /* 30 */:
                if (this.f321a.z != null) {
                    this.f321a.z.onEvent(SpeechConstants.ASR_EVENT_SESSIONID_CHANGED, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case MotionEventCompat.AXIS_GENERIC_9 /* 40 */:
                if (this.f321a.z != null) {
                    this.f321a.z.onEvent(SpeechConstants.ASR_EVENT_ACTIVATE_SUCCESS, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 50:
                if (this.f321a.z != null) {
                    this.f321a.z.onEvent(SpeechConstants.ASR_EVENT_UPLOAD_ONLINE_ONESHOT_WAKEUPWORD_SUCCESS, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 53:
                if (this.f321a.z != null) {
                    this.f321a.z.onError(3301, ErrorCode.toJsonMessage(((Integer) message.obj).intValue()));
                    break;
                }
                break;
            case 54:
                if (this.f321a.z != null) {
                    this.f321a.z.onError(1300, ErrorCode.toJsonMessage(((Integer) message.obj).intValue()));
                    break;
                }
                break;
            case 1129:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onEvent(1129, (int) System.currentTimeMillis());
                    break;
                }
                break;
            case 1201:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onResult(1201, (String) message.obj);
                    break;
                }
                break;
            case 1202:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onResult(1202, (String) message.obj);
                    break;
                }
                break;
            case 1210:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onResult(1210, (String) message.obj);
                    break;
                }
                break;
            case 3201:
                if (this.f321a.z != null && !this.f321a.as) {
                    this.f321a.z.onResult(3201, (String) message.obj);
                    break;
                }
                break;
            default:
                if (this.f321a.z != null) {
                    this.f321a.z.onEvent(message.what, (int) System.currentTimeMillis());
                    break;
                }
                break;
        }
    }
}
