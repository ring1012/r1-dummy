package org.eclipse.paho.client.mqttv3.a;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttToken;

/* loaded from: classes.dex */
public class j implements IMqttActionListener {

    /* renamed from: a, reason: collision with root package name */
    private MqttClientPersistence f495a;
    private MqttAsyncClient b;
    private a c;
    private MqttConnectOptions d;
    private MqttToken e;
    private Object f;
    private IMqttActionListener g;
    private int h;

    public j(MqttAsyncClient mqttAsyncClient, MqttClientPersistence mqttClientPersistence, a aVar, MqttConnectOptions mqttConnectOptions, MqttToken mqttToken, Object obj, IMqttActionListener iMqttActionListener) {
        this.f495a = mqttClientPersistence;
        this.b = mqttAsyncClient;
        this.c = aVar;
        this.d = mqttConnectOptions;
        this.e = mqttToken;
        this.f = obj;
        this.g = iMqttActionListener;
        this.h = mqttConnectOptions.getMqttVersion();
    }

    public void a() {
        MqttToken mqttToken = new MqttToken(this.b.getClientId());
        mqttToken.setActionCallback(this);
        mqttToken.setUserContext(this);
        this.f495a.open(this.b.getClientId(), this.b.getServerURI());
        if (this.d.isCleanSession()) {
            this.f495a.clear();
        }
        if (this.d.getMqttVersion() == 0) {
            this.d.setMqttVersion(4);
        }
        try {
            this.c.a(this.d, mqttToken);
        } catch (MqttException e) {
            onFailure(mqttToken, e);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
    public void onFailure(IMqttToken iMqttToken, Throwable th) {
        int length = this.c.i().length;
        int iH = this.c.h();
        if (iH + 1 >= length && (this.h != 0 || this.d.getMqttVersion() != 4)) {
            if (this.h == 0) {
                this.d.setMqttVersion(0);
            }
            this.e.internalTok.a(null, th instanceof MqttException ? (MqttException) th : new MqttException(th));
            this.e.internalTok.j();
            if (this.g != null) {
                this.e.setUserContext(this.f);
                this.g.onFailure(this.e, th);
                return;
            }
            return;
        }
        if (this.h != 0) {
            this.c.a(iH + 1);
        } else if (this.d.getMqttVersion() == 4) {
            this.d.setMqttVersion(3);
        } else {
            this.d.setMqttVersion(4);
            this.c.a(iH + 1);
        }
        try {
            a();
        } catch (MqttPersistenceException e) {
            onFailure(iMqttToken, e);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
    public void onSuccess(IMqttToken iMqttToken) {
        if (this.h == 0) {
            this.d.setMqttVersion(0);
        }
        this.e.internalTok.a(iMqttToken.getResponse(), null);
        this.e.internalTok.j();
        if (this.g != null) {
            this.e.setUserContext(this.f);
            this.g.onSuccess(this.e);
        }
    }
}
