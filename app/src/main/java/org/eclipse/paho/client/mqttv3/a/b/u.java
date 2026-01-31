package org.eclipse.paho.client.mqttv3.a.b;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistable;

/* loaded from: classes.dex */
public abstract class u {

    /* renamed from: a, reason: collision with root package name */
    private static final String[] f485a = {"reserved", "CONNECT", "CONNACK", "PUBLISH", "PUBACK", "PUBREC", "PUBREL", "PUBCOMP", "SUBSCRIBE", "SUBACK", "UNSUBSCRIBE", "UNSUBACK", "PINGREQ", "PINGRESP", "DISCONNECT"};
    public static final byte b = 1;
    public static final byte c = 2;
    public static final byte d = 3;
    public static final byte e = 4;
    public static final byte f = 5;
    public static final byte g = 6;
    public static final byte h = 7;
    public static final byte i = 8;
    public static final byte j = 9;
    public static final byte k = 10;
    public static final byte l = 11;
    public static final byte m = 12;
    public static final byte n = 13;
    public static final byte o = 14;
    protected static final String p = "UTF-8";
    private byte s;
    protected boolean r = false;
    protected int q = 0;

    public u(byte b2) {
        this.s = b2;
    }

    private static u a(InputStream inputStream) throws MqttException, IOException {
        try {
            DataInputStream dataInputStream = new DataInputStream(new a(inputStream));
            int unsignedByte = dataInputStream.readUnsignedByte();
            byte b2 = (byte) (unsignedByte >> 4);
            byte b3 = (byte) (unsignedByte & 15);
            long jB = (a(dataInputStream).b() + r0.a()) - r0.a();
            byte[] bArr = new byte[0];
            if (jB > 0) {
                bArr = new byte[(int) jB];
                dataInputStream.readFully(bArr, 0, bArr.length);
            }
            byte[] bArr2 = bArr;
            if (b2 == 1) {
                return new d(b3, bArr2);
            }
            if (b2 == 3) {
                return new o(b3, bArr2);
            }
            if (b2 == 4) {
                return new k(b3, bArr2);
            }
            if (b2 == 7) {
                return new l(b3, bArr2);
            }
            if (b2 == 2) {
                return new c(b3, bArr2);
            }
            if (b2 == 12) {
                return new i(b3, bArr2);
            }
            if (b2 == 13) {
                return new j(b3, bArr2);
            }
            if (b2 == 8) {
                return new r(b3, bArr2);
            }
            if (b2 == 9) {
                return new q(b3, bArr2);
            }
            if (b2 == 10) {
                return new t(b3, bArr2);
            }
            if (b2 == 11) {
                return new s(b3, bArr2);
            }
            if (b2 == 6) {
                return new n(b3, bArr2);
            }
            if (b2 == 5) {
                return new m(b3, bArr2);
            }
            if (b2 == 14) {
                return new e(b3, bArr2);
            }
            throw org.eclipse.paho.client.mqttv3.a.l.a(6);
        } catch (IOException e2) {
            throw new MqttException(e2);
        }
    }

    public static u a(MqttPersistable mqttPersistable) {
        byte[] payloadBytes = mqttPersistable.getPayloadBytes();
        if (payloadBytes == null) {
            payloadBytes = new byte[0];
        }
        return a(new v(mqttPersistable.getHeaderBytes(), mqttPersistable.getHeaderOffset(), mqttPersistable.getHeaderLength(), payloadBytes, mqttPersistable.getPayloadOffset(), mqttPersistable.getPayloadLength()));
    }

    public static u a(byte[] bArr) {
        return a(new ByteArrayInputStream(bArr));
    }

    protected static w a(DataInputStream dataInputStream) throws IOException {
        long j2 = 0;
        int i2 = 1;
        int i3 = 0;
        do {
            i3++;
            j2 += (r4 & 127) * i2;
            i2 *= 128;
        } while ((dataInputStream.readByte() & 128) != 0);
        return new w(j2, i3);
    }

    protected static byte[] a(long j2) {
        int i2 = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        do {
            byte b2 = (byte) (j2 % 128);
            j2 /= 128;
            if (j2 > 0) {
                b2 = (byte) (b2 | 128);
            }
            byteArrayOutputStream.write(b2);
            i2++;
            if (j2 <= 0) {
                break;
            }
        } while (i2 < 4);
        return byteArrayOutputStream.toByteArray();
    }

    protected abstract byte a();

    public void a(int i2) {
        this.q = i2;
    }

    protected void a(DataOutputStream dataOutputStream, String str) throws MqttException, IOException {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            byte length = (byte) ((bytes.length >>> 8) & 255);
            byte length2 = (byte) ((bytes.length >>> 0) & 255);
            dataOutputStream.write(length);
            dataOutputStream.write(length2);
            dataOutputStream.write(bytes);
        } catch (UnsupportedEncodingException e2) {
            throw new MqttException(e2);
        } catch (IOException e3) {
            throw new MqttException(e3);
        }
    }

    public void a(boolean z) {
        this.r = z;
    }

    protected String b(DataInputStream dataInputStream) throws MqttException, IOException {
        try {
            byte[] bArr = new byte[dataInputStream.readUnsignedShort()];
            dataInputStream.readFully(bArr);
            return new String(bArr, "UTF-8");
        } catch (IOException e2) {
            throw new MqttException(e2);
        }
    }

    public boolean b_() {
        return false;
    }

    protected abstract byte[] c();

    public boolean d() {
        return true;
    }

    public String e() {
        return new Integer(i()).toString();
    }

    public byte[] f() {
        return new byte[0];
    }

    public byte h() {
        return this.s;
    }

    public int i() {
        return this.q;
    }

    public byte[] j() throws MqttException, IOException {
        try {
            int iH = ((h() & 15) << 4) ^ (a() & 15);
            byte[] bArrC = c();
            int length = bArrC.length + f().length;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeByte(iH);
            dataOutputStream.write(a(length));
            dataOutputStream.write(bArrC);
            dataOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e2) {
            throw new MqttException(e2);
        }
    }

    protected byte[] k() throws MqttException, IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeShort(this.q);
            dataOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e2) {
            throw new MqttException(e2);
        }
    }

    public String toString() {
        return f485a[this.s];
    }
}
