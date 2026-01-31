package org.eclipse.paho.client.mqttv3;

import cn.yunzhisheng.asr.JniUscClient;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.net.SocketFactory;
import nluparser.scheme.ASR;
import org.eclipse.paho.client.mqttv3.c.a;

/* loaded from: classes.dex */
public class MqttConnectOptions {
    public static final boolean CLEAN_SESSION_DEFAULT = true;
    public static final int CONNECTION_TIMEOUT_DEFAULT = 30;
    public static final int KEEP_ALIVE_INTERVAL_DEFAULT = 60;
    public static final int MQTT_VERSION_3_1 = 3;
    public static final int MQTT_VERSION_3_1_1 = 4;
    public static final int MQTT_VERSION_DEFAULT = 0;

    /* renamed from: a, reason: collision with root package name */
    protected static final int f460a = 0;
    protected static final int b = 1;
    protected static final int c = 2;
    private String g;
    private char[] h;
    private SocketFactory i;
    private int d = 60;
    private String e = null;
    private MqttMessage f = null;
    private Properties j = null;
    private boolean k = true;
    private int l = 30;
    private String[] m = null;
    private int n = 0;

    protected static int a(String str) {
        try {
            URI uri = new URI(str);
            if (!uri.getPath().equals("")) {
                throw new IllegalArgumentException(str);
            }
            if (uri.getScheme().equals("tcp")) {
                return 0;
            }
            if (uri.getScheme().equals("ssl")) {
                return 1;
            }
            if (uri.getScheme().equals(ASR.LOCAL)) {
                return 2;
            }
            throw new IllegalArgumentException(str);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(str);
        }
    }

    private void a(String str, Object obj) {
        if (str == null || obj == null) {
            throw new IllegalArgumentException();
        }
        MqttTopic.validate(str, false);
    }

    protected void a(String str, MqttMessage mqttMessage, int i, boolean z) {
        this.e = str;
        this.f = mqttMessage;
        this.f.setQos(i);
        this.f.setRetained(z);
        this.f.a(false);
    }

    public int getConnectionTimeout() {
        return this.l;
    }

    public Properties getDebug() {
        Properties properties = new Properties();
        properties.put("MqttVersion", new Integer(getMqttVersion()));
        properties.put("CleanSession", Boolean.valueOf(isCleanSession()));
        properties.put("ConTimeout", new Integer(getConnectionTimeout()));
        properties.put("KeepAliveInterval", new Integer(getKeepAliveInterval()));
        properties.put("UserName", getUserName() == null ? JniUscClient.az : getUserName());
        properties.put("WillDestination", getWillDestination() == null ? JniUscClient.az : getWillDestination());
        if (getSocketFactory() == null) {
            properties.put("SocketFactory", JniUscClient.az);
        } else {
            properties.put("SocketFactory", getSocketFactory());
        }
        if (getSSLProperties() == null) {
            properties.put("SSLProperties", JniUscClient.az);
        } else {
            properties.put("SSLProperties", getSSLProperties());
        }
        return properties;
    }

    public int getKeepAliveInterval() {
        return this.d;
    }

    public int getMqttVersion() {
        return this.n;
    }

    public char[] getPassword() {
        return this.h;
    }

    public Properties getSSLProperties() {
        return this.j;
    }

    public String[] getServerURIs() {
        return this.m;
    }

    public SocketFactory getSocketFactory() {
        return this.i;
    }

    public String getUserName() {
        return this.g;
    }

    public String getWillDestination() {
        return this.e;
    }

    public MqttMessage getWillMessage() {
        return this.f;
    }

    public boolean isCleanSession() {
        return this.k;
    }

    public void setCleanSession(boolean z) {
        this.k = z;
    }

    public void setConnectionTimeout(int i) {
        if (i < 0) {
            throw new IllegalArgumentException();
        }
        this.l = i;
    }

    public void setKeepAliveInterval(int i) {
        if (i < 0) {
            throw new IllegalArgumentException();
        }
        this.d = i;
    }

    public void setMqttVersion(int i) {
        if (i != 0 && i != 3 && i != 4) {
            throw new IllegalArgumentException();
        }
        this.n = i;
    }

    public void setPassword(char[] cArr) {
        this.h = cArr;
    }

    public void setSSLProperties(Properties properties) {
        this.j = properties;
    }

    public void setServerURIs(String[] strArr) {
        for (String str : strArr) {
            a(str);
        }
        this.m = strArr;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.i = socketFactory;
    }

    public void setUserName(String str) {
        if (str != null && str.trim().equals("")) {
            throw new IllegalArgumentException();
        }
        this.g = str;
    }

    public void setWill(String str, byte[] bArr, int i, boolean z) {
        a(str, bArr);
        a(str, new MqttMessage(bArr), i, z);
    }

    public void setWill(MqttTopic mqttTopic, byte[] bArr, int i, boolean z) {
        String name = mqttTopic.getName();
        a(name, bArr);
        a(name, new MqttMessage(bArr), i, z);
    }

    public String toString() {
        return a.a(getDebug(), "Connection options");
    }
}
