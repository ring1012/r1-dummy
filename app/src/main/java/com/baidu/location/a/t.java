package com.baidu.location.a;

import android.os.HandlerThread;

/* loaded from: classes.dex */
public class t {

    /* renamed from: a, reason: collision with root package name */
    private static HandlerThread f83a = null;

    public static synchronized HandlerThread a() {
        if (f83a == null) {
            f83a = new HandlerThread("ServiceStartArguments", 10);
            f83a.start();
        }
        return f83a;
    }
}
