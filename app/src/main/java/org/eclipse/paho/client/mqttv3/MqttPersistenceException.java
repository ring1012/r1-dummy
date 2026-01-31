package org.eclipse.paho.client.mqttv3;

/* loaded from: classes.dex */
public class MqttPersistenceException extends MqttException {
    public static final short REASON_CODE_PERSISTENCE_IN_USE = 32200;

    /* renamed from: a, reason: collision with root package name */
    private static final long f463a = 300;

    public MqttPersistenceException() {
        super(0);
    }

    public MqttPersistenceException(int i) {
        super(i);
    }

    public MqttPersistenceException(int i, Throwable th) {
        super(i, th);
    }

    public MqttPersistenceException(Throwable th) {
        super(th);
    }
}
