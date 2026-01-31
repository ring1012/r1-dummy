package org.eclipse.paho.client.mqttv3.b;

import java.util.Enumeration;
import java.util.Hashtable;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttPersistable;

/* loaded from: classes.dex */
public class a implements MqttClientPersistence {

    /* renamed from: a, reason: collision with root package name */
    private Hashtable f504a;

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public void clear() {
        this.f504a.clear();
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public void close() {
        this.f504a.clear();
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public boolean containsKey(String str) {
        return this.f504a.containsKey(str);
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public MqttPersistable get(String str) {
        return (MqttPersistable) this.f504a.get(str);
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public Enumeration keys() {
        return this.f504a.keys();
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public void open(String str, String str2) {
        this.f504a = new Hashtable();
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public void put(String str, MqttPersistable mqttPersistable) {
        this.f504a.put(str, mqttPersistable);
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttClientPersistence
    public void remove(String str) {
        this.f504a.remove(str);
    }
}
