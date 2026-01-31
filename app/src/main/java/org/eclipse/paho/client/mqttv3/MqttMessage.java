package org.eclipse.paho.client.mqttv3;

/* loaded from: classes.dex */
public class MqttMessage {
    private byte[] b;

    /* renamed from: a, reason: collision with root package name */
    private boolean f462a = true;
    private int c = 1;
    private boolean d = false;
    private boolean e = false;

    public MqttMessage() {
        setPayload(new byte[0]);
    }

    public MqttMessage(byte[] bArr) {
        setPayload(bArr);
    }

    public static void validateQos(int i) {
        if (i < 0 || i > 2) {
            throw new IllegalArgumentException();
        }
    }

    protected void a() {
        if (!this.f462a) {
            throw new IllegalStateException();
        }
    }

    protected void a(boolean z) {
        this.f462a = z;
    }

    protected void b(boolean z) {
        this.e = z;
    }

    public void clearPayload() {
        a();
        this.b = new byte[0];
    }

    public byte[] getPayload() {
        return this.b;
    }

    public int getQos() {
        return this.c;
    }

    public boolean isDuplicate() {
        return this.e;
    }

    public boolean isRetained() {
        return this.d;
    }

    public void setPayload(byte[] bArr) {
        a();
        if (bArr == null) {
            throw new NullPointerException();
        }
        this.b = bArr;
    }

    public void setQos(int i) {
        a();
        validateQos(i);
        this.c = i;
    }

    public void setRetained(boolean z) {
        a();
        this.d = z;
    }

    public String toString() {
        return new String(this.b);
    }
}
