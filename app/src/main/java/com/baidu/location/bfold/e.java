package com.baidu.location.bfold;

import android.location.Location;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;

/* loaded from: classes.dex */
class e extends Handler {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f94a;

    e(d dVar) {
        this.f94a = dVar;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) throws IOException {
        if (com.baidu.location.f.f) {
            switch (message.what) {
                case 1:
                    this.f94a.e((Location) message.obj);
                    break;
                case 3:
                    this.f94a.a("&og=1", (Location) message.obj);
                    break;
                case 4:
                    this.f94a.a("&og=2", (Location) message.obj);
                    break;
            }
        }
    }
}
