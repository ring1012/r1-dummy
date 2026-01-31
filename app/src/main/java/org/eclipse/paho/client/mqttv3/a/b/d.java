package org.eclipse.paho.client.mqttv3.a.b;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/* loaded from: classes.dex */
public class d extends u {

    /* renamed from: a, reason: collision with root package name */
    public static final String f474a = "Con";
    private String s;
    private boolean t;
    private MqttMessage u;
    private String v;
    private char[] w;
    private int x;
    private String y;
    private int z;

    public d(byte b, byte[] bArr) throws IOException {
        super((byte) 1);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        b(dataInputStream);
        dataInputStream.readByte();
        dataInputStream.readByte();
        this.x = dataInputStream.readUnsignedShort();
        this.s = b(dataInputStream);
        dataInputStream.close();
    }

    public d(String str, int i, boolean z, int i2, String str2, char[] cArr, MqttMessage mqttMessage, String str3) {
        super((byte) 1);
        this.s = str;
        this.t = z;
        this.x = i2;
        this.v = str2;
        this.w = cArr;
        this.u = mqttMessage;
        this.y = str3;
        this.z = i;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte a() {
        return (byte) 0;
    }

    public boolean b() {
        return this.t;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte[] c() throws MqttException, IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            if (this.z == 3) {
                a(dataOutputStream, "MQIsdp");
            } else if (this.z == 4) {
                a(dataOutputStream, "MQTT");
            }
            dataOutputStream.write(this.z);
            byte qos = this.t ? (byte) 2 : (byte) 0;
            if (this.u != null) {
                qos = (byte) (((byte) (qos | 4)) | (this.u.getQos() << 3));
                if (this.u.isRetained()) {
                    qos = (byte) (qos | 32);
                }
            }
            if (this.v != null) {
                qos = (byte) (qos | 128);
                if (this.w != null) {
                    qos = (byte) (qos | 64);
                }
            }
            dataOutputStream.write(qos);
            dataOutputStream.writeShort(this.x);
            dataOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new MqttException(e);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public boolean d() {
        return false;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public String e() {
        return "Con";
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public byte[] f() throws MqttException, IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            a(dataOutputStream, this.s);
            if (this.u != null) {
                a(dataOutputStream, this.y);
                dataOutputStream.writeShort(this.u.getPayload().length);
                dataOutputStream.write(this.u.getPayload());
            }
            if (this.v != null) {
                a(dataOutputStream, this.v);
                if (this.w != null) {
                    a(dataOutputStream, new String(this.w));
                }
            }
            dataOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new MqttException(e);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public String toString() {
        return new StringBuffer(String.valueOf(super.toString())).append(" clientId ").append(this.s).append(" keepAliveInterval ").append(this.x).toString();
    }
}
