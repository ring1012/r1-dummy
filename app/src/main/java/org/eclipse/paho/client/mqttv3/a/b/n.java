package org.eclipse.paho.client.mqttv3.a.b;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/* loaded from: classes.dex */
public class n extends h {
    public n(byte b, byte[] bArr) {
        super((byte) 6);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        this.q = dataInputStream.readUnsignedShort();
        dataInputStream.close();
    }

    public n(m mVar) {
        super((byte) 6);
        a(mVar.i());
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte a() {
        return (byte) ((this.r ? 8 : 0) | 2);
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte[] c() {
        return k();
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public String toString() {
        return new StringBuffer(String.valueOf(super.toString())).append(" msgId ").append(this.q).toString();
    }
}
