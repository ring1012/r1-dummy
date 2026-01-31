package org.eclipse.paho.client.mqttv3;

/* loaded from: classes.dex */
public class MqttDeliveryToken extends MqttToken implements IMqttDeliveryToken {
    public MqttDeliveryToken() {
    }

    public MqttDeliveryToken(String str) {
        super(str);
    }

    protected void a(MqttMessage mqttMessage) {
        this.internalTok.a(mqttMessage);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
    public MqttMessage getMessage() {
        return this.internalTok.o();
    }
}
