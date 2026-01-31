package org.eclipse.paho.client.mqttv3;

import org.eclipse.paho.client.mqttv3.a.o;

/* loaded from: classes.dex */
public class MqttException extends Exception {
    public static final short REASON_CODE_BROKER_UNAVAILABLE = 3;
    public static final short REASON_CODE_CLIENT_ALREADY_DISCONNECTED = 32101;
    public static final short REASON_CODE_CLIENT_CLOSED = 32111;
    public static final short REASON_CODE_CLIENT_CONNECTED = 32100;
    public static final short REASON_CODE_CLIENT_DISCONNECTING = 32102;
    public static final short REASON_CODE_CLIENT_DISCONNECT_PROHIBITED = 32107;
    public static final short REASON_CODE_CLIENT_EXCEPTION = 0;
    public static final short REASON_CODE_CLIENT_NOT_CONNECTED = 32104;
    public static final short REASON_CODE_CLIENT_TIMEOUT = 32000;
    public static final short REASON_CODE_CONNECTION_LOST = 32109;
    public static final short REASON_CODE_CONNECT_IN_PROGRESS = 32110;
    public static final short REASON_CODE_FAILED_AUTHENTICATION = 4;
    public static final short REASON_CODE_INVALID_CLIENT_ID = 2;
    public static final short REASON_CODE_INVALID_MESSAGE = 32108;
    public static final short REASON_CODE_INVALID_PROTOCOL_VERSION = 1;
    public static final short REASON_CODE_MAX_INFLIGHT = 32202;
    public static final short REASON_CODE_NOT_AUTHORIZED = 5;
    public static final short REASON_CODE_NO_MESSAGE_IDS_AVAILABLE = 32001;
    public static final short REASON_CODE_SERVER_CONNECT_ERROR = 32103;
    public static final short REASON_CODE_SOCKET_FACTORY_MISMATCH = 32105;
    public static final short REASON_CODE_SSL_CONFIG_ERROR = 32106;
    public static final short REASON_CODE_SUBSCRIBE_FAILED = 128;
    public static final short REASON_CODE_TOKEN_INUSE = 32201;
    public static final short REASON_CODE_UNEXPECTED_ERROR = 6;
    public static final short REASON_CODE_WRITE_TIMEOUT = 32002;

    /* renamed from: a, reason: collision with root package name */
    private static final long f461a = 300;
    private int b;
    private Throwable c;

    public MqttException(int i) {
        this.b = i;
    }

    public MqttException(int i, Throwable th) {
        this.b = i;
        this.c = th;
    }

    public MqttException(Throwable th) {
        this.b = 0;
        this.c = th;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.c;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return o.a(this.b);
    }

    public int getReasonCode() {
        return this.b;
    }

    @Override // java.lang.Throwable
    public String toString() {
        String string = new StringBuffer(String.valueOf(getMessage())).append(" (").append(this.b).append(")").toString();
        return this.c != null ? new StringBuffer(String.valueOf(string)).append(" - ").append(this.c.toString()).toString() : string;
    }
}
