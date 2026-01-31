package org.eclipse.paho.client.mqttv3.a.b;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class f extends InputStream {

    /* renamed from: a, reason: collision with root package name */
    static Class f476a;
    private static final String b;
    private static final Logger c;
    private org.eclipse.paho.client.mqttv3.a.e d;
    private DataInputStream e;

    static {
        Class<?> cls = f476a;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.a.b.f");
                f476a = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        b = cls.getName();
        c = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, b);
    }

    public f(org.eclipse.paho.client.mqttv3.a.e eVar, InputStream inputStream) {
        this.d = null;
        this.d = eVar;
        this.e = new DataInputStream(inputStream);
    }

    private void a(byte[] bArr, int i, int i2) throws IOException {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException();
        }
        int i3 = 0;
        while (i3 < i2) {
            int i4 = this.e.read(bArr, i + i3, i2 - i3);
            this.d.b(i4);
            if (i4 < 0) {
                throw new EOFException();
            }
            i3 += i4;
        }
    }

    public u a() throws MqttException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte b2 = this.e.readByte();
        this.d.b(1);
        byte b3 = (byte) ((b2 >>> 4) & 15);
        if (b3 < 1 || b3 > 14) {
            throw org.eclipse.paho.client.mqttv3.a.l.a(32108);
        }
        long jB = u.a(this.e).b();
        byteArrayOutputStream.write(b2);
        byteArrayOutputStream.write(u.a(jB));
        byte[] bArr = new byte[(int) (jB + byteArrayOutputStream.size())];
        a(bArr, byteArrayOutputStream.size(), bArr.length - byteArrayOutputStream.size());
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        System.arraycopy(byteArray, 0, bArr, 0, byteArray.length);
        u uVarA = u.a(bArr);
        c.fine(b, "readMqttWireMessage", "501", new Object[]{uVarA});
        return uVarA;
    }

    @Override // java.io.InputStream
    public int available() {
        return this.e.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.e.close();
    }

    @Override // java.io.InputStream
    public int read() {
        return this.e.read();
    }
}
