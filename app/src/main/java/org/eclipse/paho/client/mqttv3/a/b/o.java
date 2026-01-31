package org.eclipse.paho.client.mqttv3.a.b;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/* loaded from: classes.dex */
public class o extends h {

    /* renamed from: a, reason: collision with root package name */
    private MqttMessage f480a;
    private String s;
    private byte[] t;

    public o(byte b, byte[] bArr) throws IOException {
        super((byte) 3);
        this.t = null;
        this.f480a = new p();
        this.f480a.setQos((b >> 1) & 3);
        if ((b & 1) == 1) {
            this.f480a.setRetained(true);
        }
        if ((b & 8) == 8) {
            ((p) this.f480a).b(true);
        }
        a aVar = new a(new ByteArrayInputStream(bArr));
        DataInputStream dataInputStream = new DataInputStream(aVar);
        this.s = b(dataInputStream);
        if (this.f480a.getQos() > 0) {
            this.q = dataInputStream.readUnsignedShort();
        }
        byte[] bArr2 = new byte[bArr.length - aVar.a()];
        dataInputStream.readFully(bArr2);
        dataInputStream.close();
        this.f480a.setPayload(bArr2);
    }

    public o(String str, MqttMessage mqttMessage) {
        super((byte) 3);
        this.t = null;
        this.s = str;
        this.f480a = mqttMessage;
    }

    protected static byte[] a(MqttMessage mqttMessage) {
        return mqttMessage.getPayload();
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte a() {
        byte qos = (byte) (this.f480a.getQos() << 1);
        if (this.f480a.isRetained()) {
            qos = (byte) (qos | 1);
        }
        return (this.f480a.isDuplicate() || this.r) ? (byte) (qos | 8) : qos;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public void a(int i) {
        super.a(i);
        if (this.f480a instanceof p) {
            ((p) this.f480a).a(i);
        }
    }

    public String b() {
        return this.s;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte[] c() throws MqttException, IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            a(dataOutputStream, this.s);
            if (this.f480a.getQos() > 0) {
                dataOutputStream.writeShort(this.q);
            }
            dataOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new MqttException(e);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public boolean d() {
        return true;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public byte[] f() {
        if (this.t == null) {
            this.t = a(this.f480a);
        }
        return this.t;
    }

    public MqttMessage g() {
        return this.f480a;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.h, org.eclipse.paho.client.mqttv3.MqttPersistable
    public int getPayloadLength() {
        try {
            return f().length;
        } catch (MqttException e) {
            return 0;
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public String toString() {
        String str;
        StringBuffer stringBuffer = new StringBuffer();
        byte[] payload = this.f480a.getPayload();
        int iMin = Math.min(payload.length, 20);
        for (int i = 0; i < iMin; i++) {
            String hexString = Integer.toHexString(payload[i]);
            if (hexString.length() == 1) {
                hexString = new StringBuffer("0").append(hexString).toString();
            }
            stringBuffer.append(hexString);
        }
        try {
            str = new String(payload, 0, iMin, "UTF-8");
        } catch (Exception e) {
            str = "?";
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append(super.toString());
        stringBuffer2.append(" qos:").append(this.f480a.getQos());
        if (this.f480a.getQos() > 0) {
            stringBuffer2.append(" msgId:").append(this.q);
        }
        stringBuffer2.append(" retained:").append(this.f480a.isRetained());
        stringBuffer2.append(" dup:").append(this.r);
        stringBuffer2.append(" topic:\"").append(this.s).append("\"");
        stringBuffer2.append(" payload:[hex:").append(stringBuffer);
        stringBuffer2.append(" utf8:\"").append(str).append("\"");
        stringBuffer2.append(" length:").append(payload.length).append("]");
        return stringBuffer2.toString();
    }
}
