package org.eclipse.paho.client.mqttv3;

import org.eclipse.paho.client.mqttv3.b.b;
import org.eclipse.paho.client.mqttv3.c.a;

/* loaded from: classes.dex */
public class MqttClient implements IMqttClient {

    /* renamed from: a, reason: collision with root package name */
    protected MqttAsyncClient f459a;
    protected long b;

    public MqttClient(String str, String str2) {
        this(str, str2, new b());
    }

    public MqttClient(String str, String str2, MqttClientPersistence mqttClientPersistence) {
        this.f459a = null;
        this.b = -1L;
        this.f459a = new MqttAsyncClient(str, str2, mqttClientPersistence);
    }

    public static String generateClientId() {
        return MqttAsyncClient.generateClientId();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void close() {
        this.f459a.close();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void connect() {
        connect(new MqttConnectOptions());
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void connect(MqttConnectOptions mqttConnectOptions) {
        this.f459a.connect(mqttConnectOptions, null, null).waitForCompletion(getTimeToWait());
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public IMqttToken connectWithResult(MqttConnectOptions mqttConnectOptions) throws MqttException {
        IMqttToken iMqttTokenConnect = this.f459a.connect(mqttConnectOptions, null, null);
        iMqttTokenConnect.waitForCompletion(getTimeToWait());
        return iMqttTokenConnect;
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void disconnect() {
        this.f459a.disconnect().waitForCompletion();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void disconnect(long j) {
        this.f459a.disconnect(j, null, null).waitForCompletion();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void disconnectForcibly() {
        this.f459a.disconnectForcibly();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void disconnectForcibly(long j) {
        this.f459a.disconnectForcibly(j);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void disconnectForcibly(long j, long j2) {
        this.f459a.disconnectForcibly(j, j2);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public String getClientId() {
        return this.f459a.getClientId();
    }

    public a getDebug() {
        return this.f459a.getDebug();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public IMqttDeliveryToken[] getPendingDeliveryTokens() {
        return this.f459a.getPendingDeliveryTokens();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public String getServerURI() {
        return this.f459a.getServerURI();
    }

    public long getTimeToWait() {
        return this.b;
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public MqttTopic getTopic(String str) {
        return this.f459a.a(str);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public boolean isConnected() {
        return this.f459a.isConnected();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void publish(String str, MqttMessage mqttMessage) {
        this.f459a.publish(str, mqttMessage, (Object) null, (IMqttActionListener) null).waitForCompletion(getTimeToWait());
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void publish(String str, byte[] bArr, int i, boolean z) {
        MqttMessage mqttMessage = new MqttMessage(bArr);
        mqttMessage.setQos(i);
        mqttMessage.setRetained(z);
        publish(str, mqttMessage);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void setCallback(MqttCallback mqttCallback) {
        this.f459a.setCallback(mqttCallback);
    }

    public void setTimeToWait(long j) {
        if (j < -1) {
            throw new IllegalArgumentException();
        }
        this.b = j;
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void subscribe(String str) throws MqttException {
        subscribe(new String[]{str}, new int[]{1});
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void subscribe(String str, int i) throws MqttException {
        subscribe(new String[]{str}, new int[]{i});
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void subscribe(String[] strArr) throws MqttException {
        int[] iArr = new int[strArr.length];
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = 1;
        }
        subscribe(strArr, iArr);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void subscribe(String[] strArr, int[] iArr) throws MqttException {
        IMqttToken iMqttTokenSubscribe = this.f459a.subscribe(strArr, iArr, (Object) null, (IMqttActionListener) null);
        iMqttTokenSubscribe.waitForCompletion(getTimeToWait());
        int[] grantedQos = iMqttTokenSubscribe.getGrantedQos();
        for (int i = 0; i < grantedQos.length; i++) {
            iArr[i] = grantedQos[i];
        }
        if (grantedQos.length == 1 && iArr[0] == 128) {
            throw new MqttException(128);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void unsubscribe(String str) {
        unsubscribe(new String[]{str});
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttClient
    public void unsubscribe(String[] strArr) {
        this.f459a.unsubscribe(strArr, (Object) null, (IMqttActionListener) null).waitForCompletion(getTimeToWait());
    }
}
