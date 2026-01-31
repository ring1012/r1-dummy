package org.eclipse.paho.client.mqttv3;

import org.eclipse.paho.client.mqttv3.a.u;

/* loaded from: classes.dex */
public class MqttToken implements IMqttToken {
    public u internalTok;

    public MqttToken() {
        this.internalTok = null;
    }

    public MqttToken(String str) {
        this.internalTok = null;
        this.internalTok = new u(str);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public IMqttActionListener getActionCallback() {
        return this.internalTok.g();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public IMqttAsyncClient getClient() {
        return this.internalTok.m();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public MqttException getException() {
        return this.internalTok.c();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public int[] getGrantedQos() {
        return this.internalTok.u();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public int getMessageId() {
        return this.internalTok.a();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public org.eclipse.paho.client.mqttv3.a.b.u getResponse() {
        return this.internalTok.w();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public boolean getSessionPresent() {
        return this.internalTok.v();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public String[] getTopics() {
        return this.internalTok.q();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public Object getUserContext() {
        return this.internalTok.r();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public boolean isComplete() {
        return this.internalTok.d();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public void setActionCallback(IMqttActionListener iMqttActionListener) {
        this.internalTok.a(iMqttActionListener);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public void setUserContext(Object obj) {
        this.internalTok.a(obj);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public void waitForCompletion() {
        this.internalTok.a(-1L);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttToken
    public void waitForCompletion(long j) {
        this.internalTok.a(j);
    }
}
