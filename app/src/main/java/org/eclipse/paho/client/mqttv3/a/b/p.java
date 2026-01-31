package org.eclipse.paho.client.mqttv3.a.b;

import org.eclipse.paho.client.mqttv3.MqttMessage;

/* loaded from: classes.dex */
public class p extends MqttMessage {

    /* renamed from: a, reason: collision with root package name */
    private int f481a;

    public void a(int i) {
        this.f481a = i;
    }

    public int b() {
        return this.f481a;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttMessage
    public void b(boolean z) {
        super.b(z);
    }
}
