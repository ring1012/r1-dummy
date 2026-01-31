package org.eclipse.paho.client.mqttv3.a;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public class m {

    /* renamed from: a, reason: collision with root package name */
    private File f496a;
    private RandomAccessFile b;
    private Object c;

    public m(File file, String str) throws Exception {
        this.f496a = new File(file, str);
        if (l.a("java.nio.channels.FileLock")) {
            try {
                this.b = new RandomAccessFile(this.f496a, "rw");
                Object objInvoke = this.b.getClass().getMethod("getChannel", new Class[0]).invoke(this.b, new Object[0]);
                this.c = objInvoke.getClass().getMethod("tryLock", new Class[0]).invoke(objInvoke, new Object[0]);
            } catch (IllegalAccessException e) {
                this.c = null;
            } catch (IllegalArgumentException e2) {
                this.c = null;
            } catch (NoSuchMethodException e3) {
                this.c = null;
            }
            if (this.c == null) {
                a();
                throw new Exception("Problem obtaining file lock");
            }
        }
    }

    public void a() throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        try {
            if (this.c != null) {
                this.c.getClass().getMethod("release", new Class[0]).invoke(this.c, new Object[0]);
                this.c = null;
            }
        } catch (Exception e) {
        }
        if (this.b != null) {
            try {
                this.b.close();
            } catch (IOException e2) {
            }
            this.b = null;
        }
        if (this.f496a != null && this.f496a.exists()) {
            this.f496a.delete();
        }
        this.f496a = null;
    }
}
