package com.unisound.common;

import android.content.Context;
import android.os.Process;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

/* loaded from: classes.dex */
public class ag {

    /* renamed from: a, reason: collision with root package name */
    private final Context f238a;

    public ag(Context context) {
        this.f238a = context.getApplicationContext();
    }

    private boolean b(String str) {
        return ContextCompat.checkSelfPermission(this.f238a, str) == -1;
    }

    public int a(String str) {
        return PermissionChecker.checkPermission(this.f238a, str, Process.myPid(), Process.myUid(), this.f238a.getPackageName());
    }

    public boolean a(String... strArr) {
        for (String str : strArr) {
            if (b(str)) {
                return true;
            }
        }
        return false;
    }
}
