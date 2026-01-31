package org.eclipse.paho.client.mqttv3.a.b;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class g extends OutputStream {

    /* renamed from: a, reason: collision with root package name */
    static Class f477a;
    private static final String b;
    private static final Logger c;
    private org.eclipse.paho.client.mqttv3.a.e d;
    private BufferedOutputStream e;

    static {
        Class<?> cls = f477a;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.a.b.g");
                f477a = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        b = cls.getName();
        c = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, b);
    }

    public g(org.eclipse.paho.client.mqttv3.a.e eVar, OutputStream outputStream) {
        this.d = null;
        this.d = eVar;
        this.e = new BufferedOutputStream(outputStream);
    }

    public void a(u uVar) {
        byte[] bArrJ = uVar.j();
        byte[] bArrF = uVar.f();
        this.e.write(bArrJ, 0, bArrJ.length);
        this.d.a(bArrJ.length);
        int i = 0;
        while (i < bArrF.length) {
            int iMin = Math.min(1024, bArrF.length - i);
            this.e.write(bArrF, i, iMin);
            i += 1024;
            this.d.a(iMin);
        }
        c.fine(b, "write", "500", new Object[]{uVar});
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.e.close();
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() {
        this.e.flush();
    }

    @Override // java.io.OutputStream
    public void write(int i) throws IOException {
        this.e.write(i);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) {
        this.e.write(bArr);
        this.d.a(bArr.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.e.write(bArr, i, i2);
        this.d.a(i2);
    }
}
