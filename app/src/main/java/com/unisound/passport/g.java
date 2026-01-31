package com.unisound.passport;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
class g extends Handler {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f287a;

    public g(d dVar) {
        this.f287a = dVar;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g(d dVar, Looper looper) {
        super(looper);
        this.f287a = dVar;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
            case 1201:
                if (this.f287a.f284a != null) {
                    this.f287a.f284a.onEvent(1201, System.currentTimeMillis());
                    break;
                }
                break;
            case 1202:
                if (this.f287a.f284a != null) {
                    this.f287a.f284a.onEvent(1202, System.currentTimeMillis());
                    break;
                }
                break;
            case 1203:
                if (this.f287a.f284a != null) {
                    this.f287a.f284a.onEvent(1203, System.currentTimeMillis());
                    break;
                }
                break;
            case 1204:
                if (this.f287a.f284a != null) {
                    this.f287a.f284a.onEvent(1204, System.currentTimeMillis());
                    break;
                }
                break;
            case c.e /* 1205 */:
                if (this.f287a.f284a != null) {
                    this.f287a.f284a.onEvent(c.e, System.currentTimeMillis());
                    break;
                }
                break;
            case 1301:
                if (this.f287a.f284a != null) {
                    this.f287a.f284a.onResult(1301, (String) message.obj);
                    break;
                }
                break;
            case 1401:
                if (this.f287a.f284a != null) {
                    this.f287a.f284a.onError(((Integer) message.obj).intValue(), a.a(((Integer) message.obj).intValue()));
                    break;
                }
                break;
        }
    }
}
