package org.eclipse.paho.client.mqttv3.a.b;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/* loaded from: classes.dex */
public class r extends u {

    /* renamed from: a, reason: collision with root package name */
    private String[] f483a;
    private int[] s;
    private int t;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public r(byte b, byte[] bArr) {
        super((byte) 8);
        boolean z = false;
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        this.q = dataInputStream.readUnsignedShort();
        this.t = 0;
        this.f483a = new String[10];
        this.s = new int[10];
        while (!z) {
            try {
                this.f483a[this.t] = b(dataInputStream);
                int[] iArr = this.s;
                int i = this.t;
                this.t = i + 1;
                iArr[i] = dataInputStream.readByte();
            } catch (Exception e) {
                z = true;
            }
        }
        dataInputStream.close();
    }

    public r(String[] strArr, int[] iArr) {
        super((byte) 8);
        this.f483a = strArr;
        this.s = iArr;
        if (strArr.length != iArr.length) {
            throw new IllegalArgumentException();
        }
        for (int i : iArr) {
            MqttMessage.validateQos(i);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte a() {
        return (byte) ((this.r ? 8 : 0) | 2);
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public boolean b_() {
        return true;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte[] c() throws MqttException, IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeShort(this.q);
            dataOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new MqttException(e);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public byte[] f() throws MqttException, IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            for (int i = 0; i < this.f483a.length; i++) {
                a(dataOutputStream, this.f483a[i]);
                dataOutputStream.writeByte(this.s[i]);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new MqttException(e);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(super.toString());
        stringBuffer.append(" names:[");
        for (int i = 0; i < this.t; i++) {
            if (i > 0) {
                stringBuffer.append(", ");
            }
            stringBuffer.append("\"").append(this.f483a[i]).append("\"");
        }
        stringBuffer.append("] qos:[");
        for (int i2 = 0; i2 < this.t; i2++) {
            if (i2 > 0) {
                stringBuffer.append(", ");
            }
            stringBuffer.append(this.s[i2]);
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
