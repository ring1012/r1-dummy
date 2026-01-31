package com.unisound.b;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class a extends Handler {

    /* renamed from: a, reason: collision with root package name */
    private com.unisound.b.a.c f214a;

    public a(Looper looper) {
        super(looper);
    }

    private String a(int i) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("errorCode", i);
            jSONObject.put("registerCode", "");
            switch (i) {
                case 1011:
                    jSONObject.put("message", "没有网络连接错误");
                    break;
                case 1012:
                    jSONObject.put("message", "异常错误");
                    break;
                case 1013:
                    jSONObject.put("message", "返回结果为空错误");
                    break;
                case 1014:
                    jSONObject.put("message", "无效激活类型");
                    break;
                case 1015:
                    jSONObject.put("message", "激活状态错误，已经有激活操作正在执行");
                    break;
                default:
                    jSONObject.put("message", "未知错误");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public void a(com.unisound.b.a.c cVar) {
        this.f214a = cVar;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (this.f214a == null) {
            i.b("ActivatorHandler listener == null");
        }
        switch (message.what) {
            case 102:
                i.c("ActivatorHandler GET_RESULT");
                this.f214a.a((String) message.obj);
                break;
            case 106:
                i.a("ActivatorHandler RESPONSE_IS_NULL_ERROR");
                this.f214a.a(a(1013));
                break;
            case 107:
                i.a("ActivatorHandler EXCEPTION_ERROR");
                this.f214a.a(a(1012));
                break;
            case 108:
                i.a("ActivatorHandler ACTIVATOR_STATUS_ERROR_MESSAGE");
                this.f214a.a(a(1015));
                break;
            case 109:
                i.a("ActivatorHandler NO_NETWORK_ERROR");
                this.f214a.a(a(1011));
                break;
            case 110:
                i.a("ActivatorHandler INVALID_URL_TYPE_ERROR");
                this.f214a.a(a(1014));
                break;
            case g.g /* 120 */:
                i.c("ActivatorHandler NO_READ_PHONE_STATE_PERMISSION");
                this.f214a.a((String) message.obj);
                break;
        }
    }
}
