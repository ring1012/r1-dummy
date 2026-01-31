package com.baidu.b.a;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
class k extends Handler {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ b f46a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    k(b bVar, Looper looper) {
        super(looper);
        this.f46a = bVar;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (d.f40a) {
            d.a("handleMessage !!");
        }
        c cVar = (c) b.f.get(message.getData().getString("listenerKey"));
        if (d.f40a) {
            d.a("handleMessage listener = " + cVar);
        }
        if (cVar != null) {
            cVar.a(message.what, message.obj.toString());
        }
    }
}
