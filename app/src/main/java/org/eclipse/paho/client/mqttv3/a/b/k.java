package org.eclipse.paho.client.mqttv3.a.b;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/* loaded from: classes.dex */
public class k extends b {
    public k(byte b, byte[] bArr) {
        super((byte) 4);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        this.q = dataInputStream.readUnsignedShort();
        dataInputStream.close();
    }

    public k(o oVar) {
        super((byte) 4);
        this.q = oVar.i();
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte[] c() {
        return k();
    }
}
