package com.baidu.location.a;

import android.location.Location;
import java.io.IOException;

/* loaded from: classes.dex */
class e implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Location f64a;
    final /* synthetic */ d b;

    e(d dVar, Location location) {
        this.b = dVar;
        this.f64a = location;
    }

    @Override // java.lang.Runnable
    public void run() throws IOException {
        this.b.b(this.f64a);
    }
}
