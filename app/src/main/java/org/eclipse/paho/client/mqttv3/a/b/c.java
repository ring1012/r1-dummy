package org.eclipse.paho.client.mqttv3.a.b;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/* loaded from: classes.dex */
public class c extends b {

    /* renamed from: a, reason: collision with root package name */
    public static final String f473a = "Con";
    private int s;
    private boolean t;

    public c(byte b, byte[] bArr) {
        super((byte) 2);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(bArr));
        this.t = (dataInputStream.readUnsignedByte() & 1) == 1;
        this.s = dataInputStream.readUnsignedByte();
        dataInputStream.close();
    }

    public boolean a_() {
        return this.t;
    }

    public int b() {
        return this.s;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte[] c() {
        return new byte[0];
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public boolean d() {
        return false;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public String e() {
        return "Con";
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.b, org.eclipse.paho.client.mqttv3.a.b.u
    public String toString() {
        return new StringBuffer(String.valueOf(super.toString())).append(" session present:").append(this.t).append(" return code: ").append(this.s).toString();
    }
}
