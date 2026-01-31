package com.unisound.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
public class ab extends Handler {

    /* renamed from: a, reason: collision with root package name */
    private ac f234a;

    public ab() {
    }

    public ab(Looper looper) {
        super(looper);
    }

    protected boolean a(Message message) {
        return true;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (a(message) || this.f234a == null) {
            return;
        }
        this.f234a.a(message);
    }

    public void removeSendMessage() {
        removeCallbacksAndMessages(null);
    }

    public void sendMessage(int i) {
        sendEmptyMessage(i);
    }

    public void sendMessage(int i, Object obj) {
        obtainMessage(i, obj).sendToTarget();
    }

    public void setMessageLisenter(ac acVar) {
        this.f234a = acVar;
    }
}
