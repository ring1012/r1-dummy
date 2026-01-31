package com.baidu.location;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/* loaded from: classes.dex */
public class f extends Service {
    public static String d = "repll.jar";
    public static Context e = null;
    public static boolean f = false;
    public static boolean g = false;

    /* renamed from: a, reason: collision with root package name */
    e f113a = null;
    e b = null;
    e c = null;

    public static float a() {
        return 7.32f;
    }

    private boolean a(File file) throws IOException {
        int i;
        boolean z = false;
        try {
            File file2 = new File(com.baidu.location.d.j.h() + "/grtcfrsa.dat");
            if (file2.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file2, "rw");
                randomAccessFile.seek(200L);
                if (randomAccessFile.readBoolean() && randomAccessFile.readBoolean() && (i = randomAccessFile.readInt()) != 0) {
                    byte[] bArr = new byte[i];
                    randomAccessFile.read(bArr, 0, i);
                    String str = new String(bArr);
                    String strA = com.baidu.location.d.j.a(file, "SHA-256");
                    if (str != null && strA != null && com.baidu.location.d.j.b(strA, str, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiP7BS5IjEOzrKGR9/Ww9oSDhdX1ir26VOsYjT1T6tk2XumRpkHRwZbrucDcNnvSB4QsqiEJnvTSRi7YMbh2H9sLMkcvHlMV5jAErNvnuskWfcvf7T2mq7EUZI/Hf4oVZhHV0hQJRFVdTcjWI6q2uaaKM3VMh+roDesiE7CR2biQIDAQAB")) {
                        z = true;
                    }
                }
                randomAccessFile.close();
            }
        } catch (Exception e2) {
        }
        return z;
    }

    public static Context b() {
        return e;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.c.onBind(intent);
    }

    @Override // android.app.Service
    @SuppressLint({"NewApi"})
    public void onCreate() {
        e = getApplicationContext();
        System.currentTimeMillis();
        this.b = new com.baidu.location.c.a();
        try {
            File file = new File(com.baidu.location.d.j.h() + File.separator + d);
            File file2 = new File(com.baidu.location.d.j.h() + File.separator + "app.jar");
            if (file.exists()) {
                if (file2.exists()) {
                    file2.delete();
                }
                file.renameTo(file2);
            }
            if (file2.exists() && a(new File(com.baidu.location.d.j.h() + File.separator + "app.jar"))) {
                this.f113a = (e) new DexClassLoader(com.baidu.location.d.j.h() + File.separator + "app.jar", com.baidu.location.d.j.h(), null, getClassLoader()).loadClass("com.baidu.serverLoc.LocationService").newInstance();
            }
        } catch (Exception e2) {
            this.f113a = null;
        }
        if (this.f113a == null || this.f113a.a() < this.b.a()) {
            this.c = this.b;
            this.f113a = null;
        } else {
            this.c = this.f113a;
            this.b = null;
        }
        f = true;
        this.c.a(this);
    }

    @Override // android.app.Service
    public void onDestroy() {
        f = false;
        this.c.onDestroy();
        if (g) {
            stopForeground(true);
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            try {
                int intExtra = intent.getIntExtra("command", 0);
                if (intExtra == 1) {
                    startForeground(intent.getIntExtra(TtmlNode.ATTR_ID, 0), (Notification) intent.getParcelableExtra("notification"));
                    g = true;
                } else if (intExtra == 2) {
                    stopForeground(intent.getBooleanExtra("removenotify", true));
                    g = false;
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return this.c.onStartCommand(intent, i, i2);
    }

    @Override // android.app.Service
    public void onTaskRemoved(Intent intent) {
        this.c.onTaskRemoved(intent);
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        return this.c.a(intent);
    }
}
