package org.eclipse.paho.client.mqttv3;

import java.util.Enumeration;

/* loaded from: classes.dex */
public interface MqttClientPersistence {
    void clear();

    void close();

    boolean containsKey(String str);

    MqttPersistable get(String str);

    Enumeration keys();

    void open(String str, String str2);

    void put(String str, MqttPersistable mqttPersistable);

    void remove(String str);
}
