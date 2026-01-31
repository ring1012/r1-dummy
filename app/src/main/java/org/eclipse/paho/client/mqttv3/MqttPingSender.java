package org.eclipse.paho.client.mqttv3;

import org.eclipse.paho.client.mqttv3.a.a;

/* loaded from: classes.dex */
public interface MqttPingSender {
    void init(a aVar);

    void schedule(long j);

    void start();

    void stop();
}
