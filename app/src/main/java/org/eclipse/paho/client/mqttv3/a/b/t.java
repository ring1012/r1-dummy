package org.eclipse.paho.client.mqttv3.a.b;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.eclipse.paho.client.mqttv3.MqttException;

/* loaded from: classes.dex */
public class t extends u {

    /* renamed from: a, reason: collision with root package name */
    private String[] f484a;
    private int s;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public t(byte b, byte[] bArr) {
        super((byte) 10);
        boolean z = false;
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        this.q = dataInputStream.readUnsignedShort();
        this.s = 0;
        this.f484a = new String[10];
        while (!z) {
            try {
                this.f484a[this.s] = b(dataInputStream);
            } catch (Exception e) {
                z = true;
            }
        }
        dataInputStream.close();
    }

    public t(String[] strArr) {
        super((byte) 10);
        this.f484a = strArr;
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
    public byte[] f() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        for (int i = 0; i < this.f484a.length; i++) {
            a(dataOutputStream, this.f484a[i]);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(super.toString());
        stringBuffer.append(" names:[");
        for (int i = 0; i < this.s; i++) {
            if (i > 0) {
                stringBuffer.append(", ");
            }
            stringBuffer.append(new StringBuffer("\"").append(this.f484a[i]).append("\"").toString());
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
