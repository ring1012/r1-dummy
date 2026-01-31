package org.eclipse.paho.client.mqttv3.a;

import org.eclipse.paho.client.mqttv3.MqttPersistable;

/* loaded from: classes.dex */
public class p implements MqttPersistable {

    /* renamed from: a, reason: collision with root package name */
    private String f499a;
    private byte[] b;
    private int c;
    private int d;
    private byte[] e;
    private int f;
    private int g;

    public p(String str, byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4) {
        this.f499a = null;
        this.b = null;
        this.c = 0;
        this.d = 0;
        this.e = null;
        this.f = 0;
        this.g = 0;
        this.f499a = str;
        this.b = bArr;
        this.c = i;
        this.d = i2;
        this.e = bArr2;
        this.f = i3;
        this.g = i4;
    }

    public String a() {
        return this.f499a;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPersistable
    public byte[] getHeaderBytes() {
        return this.b;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPersistable
    public int getHeaderLength() {
        return this.d;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPersistable
    public int getHeaderOffset() {
        return this.c;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPersistable
    public byte[] getPayloadBytes() {
        return this.e;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPersistable
    public int getPayloadLength() {
        if (this.e == null) {
            return 0;
        }
        return this.g;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPersistable
    public int getPayloadOffset() {
        return this.f;
    }
}
