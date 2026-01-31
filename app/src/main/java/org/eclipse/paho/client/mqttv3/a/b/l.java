package org.eclipse.paho.client.mqttv3.a.b;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/* loaded from: classes.dex */
public class l extends b {
    public l(byte b, byte[] bArr) {
        super((byte) 7);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        this.q = dataInputStream.readUnsignedShort();
        dataInputStream.close();
    }

    public l(int i) {
        super((byte) 7);
        this.q = i;
    }

    public l(o oVar) {
        super((byte) 7);
        this.q = oVar.i();
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte[] c() {
        return k();
    }
}
