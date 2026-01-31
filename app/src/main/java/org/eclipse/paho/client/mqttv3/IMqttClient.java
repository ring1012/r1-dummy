package org.eclipse.paho.client.mqttv3;

/* loaded from: classes.dex */
public interface IMqttClient {
    void close();

    void connect();

    void connect(MqttConnectOptions mqttConnectOptions);

    IMqttToken connectWithResult(MqttConnectOptions mqttConnectOptions);

    void disconnect();

    void disconnect(long j);

    void disconnectForcibly();

    void disconnectForcibly(long j);

    void disconnectForcibly(long j, long j2);

    String getClientId();

    IMqttDeliveryToken[] getPendingDeliveryTokens();

    String getServerURI();

    MqttTopic getTopic(String str);

    boolean isConnected();

    void publish(String str, MqttMessage mqttMessage);

    void publish(String str, byte[] bArr, int i, boolean z);

    void setCallback(MqttCallback mqttCallback);

    void subscribe(String str);

    void subscribe(String str, int i);

    void subscribe(String[] strArr);

    void subscribe(String[] strArr, int[] iArr);

    void unsubscribe(String str);

    void unsubscribe(String[] strArr);
}
