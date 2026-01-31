package org.eclipse.paho.client.mqttv3.a.b;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/* loaded from: classes.dex */
public class s extends b {
    public s(byte b, byte[] bArr) {
        super(u.l);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        this.q = dataInputStream.readUnsignedShort();
        dataInputStream.close();
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte[] c() {
        return new byte[0];
    }
}
