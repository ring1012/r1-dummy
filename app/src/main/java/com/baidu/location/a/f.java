package com.baidu.location.a;

import android.os.Environment;
import java.io.File;
import java.io.IOException;

/* loaded from: classes.dex */
class f extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ d f65a;

    f(d dVar) {
        this.f65a = dVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IOException {
        this.f65a.a(new File(Environment.getExternalStorageDirectory() + "/baidu/tempdata", "intime.dat"), "http://itsdata.map.baidu.com/long-conn-gps/sdk.php");
    }
}
