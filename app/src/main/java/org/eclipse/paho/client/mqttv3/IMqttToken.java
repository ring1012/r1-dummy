package org.eclipse.paho.client.mqttv3;

import org.eclipse.paho.client.mqttv3.a.b.u;

/* loaded from: classes.dex */
public interface IMqttToken {
    IMqttActionListener getActionCallback();

    IMqttAsyncClient getClient();

    MqttException getException();

    int[] getGrantedQos();

    int getMessageId();

    u getResponse();

    boolean getSessionPresent();

    String[] getTopics();

    Object getUserContext();

    boolean isComplete();

    void setActionCallback(IMqttActionListener iMqttActionListener);

    void setUserContext(Object obj);

    void waitForCompletion();

    void waitForCompletion(long j);
}
