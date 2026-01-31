package org.eclipse.paho.client.mqttv3.a;

import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttToken;

/* loaded from: classes.dex */
class b implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    a f471a;
    Thread b;
    MqttToken c;
    org.eclipse.paho.client.mqttv3.a.b.d d;
    final a e;

    b(a aVar, a aVar2, MqttToken mqttToken, org.eclipse.paho.client.mqttv3.a.b.d dVar) {
        this.e = aVar;
        this.f471a = null;
        this.b = null;
        this.f471a = aVar2;
        this.c = mqttToken;
        this.d = dVar;
        this.b = new Thread(this, new StringBuffer("MQTT Con: ").append(aVar.k().getClientId()).toString());
    }

    void a() {
        this.b.start();
    }

    @Override // java.lang.Runnable
    public void run() {
        MqttException mqttExceptionA = null;
        a.q().fine(a.r(), "connectBG:run", "220");
        try {
            for (MqttDeliveryToken mqttDeliveryToken : a.a(this.e).b()) {
                mqttDeliveryToken.internalTok.a((MqttException) null);
            }
            a.a(this.e).a(this.c, this.d);
            q qVar = a.b(this.e)[a.c(this.e)];
            qVar.a();
            a.a(this.e, new g(this.f471a, a.d(this.e), a.a(this.e), qVar.b()));
            a.e(this.e).a(new StringBuffer("MQTT Rec: ").append(this.e.k().getClientId()).toString());
            a.a(this.e, new h(this.f471a, a.d(this.e), a.a(this.e), qVar.c()));
            a.f(this.e).a(new StringBuffer("MQTT Snd: ").append(this.e.k().getClientId()).toString());
            a.g(this.e).a(new StringBuffer("MQTT Call: ").append(this.e.k().getClientId()).toString());
            this.e.a(this.d, this.c);
        } catch (MqttException e) {
            a.q().fine(a.r(), "connectBG:run", "212", null, e);
            mqttExceptionA = e;
        } catch (Exception e2) {
            a.q().fine(a.r(), "connectBG:run", "209", null, e2);
            mqttExceptionA = l.a(e2);
        }
        if (mqttExceptionA != null) {
            this.e.a(this.c, mqttExceptionA);
        }
    }
}
