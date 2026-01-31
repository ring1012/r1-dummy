package org.eclipse.paho.client.mqttv3;

/* loaded from: classes.dex */
public interface IMqttAsyncClient {
    void close();

    IMqttToken connect();

    IMqttToken connect(Object obj, IMqttActionListener iMqttActionListener);

    IMqttToken connect(MqttConnectOptions mqttConnectOptions);

    IMqttToken connect(MqttConnectOptions mqttConnectOptions, Object obj, IMqttActionListener iMqttActionListener);

    IMqttToken disconnect();

    IMqttToken disconnect(long j);

    IMqttToken disconnect(long j, Object obj, IMqttActionListener iMqttActionListener);

    IMqttToken disconnect(Object obj, IMqttActionListener iMqttActionListener);

    void disconnectForcibly();

    void disconnectForcibly(long j);

    void disconnectForcibly(long j, long j2);

    String getClientId();

    IMqttDeliveryToken[] getPendingDeliveryTokens();

    String getServerURI();

    boolean isConnected();

    IMqttDeliveryToken publish(String str, MqttMessage mqttMessage);

    IMqttDeliveryToken publish(String str, MqttMessage mqttMessage, Object obj, IMqttActionListener iMqttActionListener);

    IMqttDeliveryToken publish(String str, byte[] bArr, int i, boolean z);

    IMqttDeliveryToken publish(String str, byte[] bArr, int i, boolean z, Object obj, IMqttActionListener iMqttActionListener);

    void setCallback(MqttCallback mqttCallback);

    IMqttToken subscribe(String str, int i);

    IMqttToken subscribe(String str, int i, Object obj, IMqttActionListener iMqttActionListener);

    IMqttToken subscribe(String[] strArr, int[] iArr);

    IMqttToken subscribe(String[] strArr, int[] iArr, Object obj, IMqttActionListener iMqttActionListener);

    IMqttToken unsubscribe(String str);

    IMqttToken unsubscribe(String str, Object obj, IMqttActionListener iMqttActionListener);

    IMqttToken unsubscribe(String[] strArr);

    IMqttToken unsubscribe(String[] strArr, Object obj, IMqttActionListener iMqttActionListener);
}
