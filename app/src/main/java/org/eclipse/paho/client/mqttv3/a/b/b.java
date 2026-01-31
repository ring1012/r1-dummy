package org.eclipse.paho.client.mqttv3.a.b;

/* loaded from: classes.dex */
public abstract class b extends u {
    public b(byte b) {
        super(b);
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    protected byte a() {
        return (byte) 0;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.b.u
    public String toString() {
        return new StringBuffer(String.valueOf(super.toString())).append(" msgId ").append(this.q).toString();
    }
}
