package org.eclipse.paho.client.mqttv3.a;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttToken;

/* loaded from: classes.dex */
class c implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    Thread f488a = null;
    org.eclipse.paho.client.mqttv3.a.b.e b;
    long c;
    MqttToken d;
    final a e;

    c(a aVar, org.eclipse.paho.client.mqttv3.a.b.e eVar, long j, MqttToken mqttToken) {
        this.e = aVar;
        this.b = eVar;
        this.c = j;
        this.d = mqttToken;
    }

    void a() {
        this.f488a = new Thread(this, new StringBuffer("MQTT Disc: ").append(this.e.k().getClientId()).toString());
        this.f488a.start();
    }

    @Override // java.lang.Runnable
    public void run() {
        a.q().fine(a.r(), "disconnectBG:run", "221");
        a.d(this.e).c(this.c);
        try {
            this.e.a(this.b, this.d);
            this.d.internalTok.k();
        } catch (MqttException e) {
        } catch (Throwable th) {
            this.d.internalTok.a(null, null);
            this.e.a(this.d, (MqttException) null);
            throw th;
        }
        this.d.internalTok.a(null, null);
        this.e.a(this.d, (MqttException) null);
    }
}
