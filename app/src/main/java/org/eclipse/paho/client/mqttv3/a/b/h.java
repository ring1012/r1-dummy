package org.eclipse.paho.client.mqttv3.a.b;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistable;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

/* loaded from: classes.dex */
public abstract class h extends u implements MqttPersistable {
    public h(byte b) {
        super(b);
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPersistable
    public byte[] getHeaderBytes() throws MqttPersistenceException {
        try {
            return j();
        } catch (MqttException e) {
            throw new MqttPersistenceException(e.getCause());
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPersistable
    public int getHeaderLength() {
        return getHeaderBytes().length;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPersistable
    public int getHeaderOffset() {
        return 0;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPersistable
    public byte[] getPayloadBytes() throws MqttPersistenceException {
        try {
            return f();
        } catch (MqttException e) {
            throw new MqttPersistenceException(e.getCause());
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPersistable
    public int getPayloadLength() {
        return 0;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPersistable
    public int getPayloadOffset() {
        return 0;
    }
}
