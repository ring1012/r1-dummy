package org.eclipse.paho.client.mqttv3.a.b;

import cn.yunzhisheng.common.PinyinConverter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/* loaded from: classes.dex */
public class q extends b {

    /* renamed from: a, reason: collision with root package name */
    private int[] f482a;

    public q(byte b, byte[] bArr) {
        super((byte) 9);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        this.q = dataInputStream.readUnsignedShort();
        int i = 0;
        this.f482a = new int[bArr.length - 2];
        for (int i2 = dataInputStream.read(); i2 != -1; i2 = dataInputStream.read()) {
            this.f482a[i] = i2;
            i++;
        }
        dataInputStream.close();
    }

    public int[] b() {
        return this.f482a;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte[] c() {
        return new byte[0];
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.b, org.eclipse.paho.client.mqttv3.a.b.u
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(super.toString()).append(" granted Qos");
        for (int i = 0; i < this.f482a.length; i++) {
            stringBuffer.append(PinyinConverter.PINYIN_SEPARATOR).append(this.f482a[i]);
        }
        return stringBuffer.toString();
    }
}
