package org.eclipse.paho.client.mqttv3;

import cn.yunzhisheng.asr.JniUscClient;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Hashtable;
import java.util.Properties;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.eclipse.paho.client.mqttv3.a.a;
import org.eclipse.paho.client.mqttv3.a.b.e;
import org.eclipse.paho.client.mqttv3.a.b.o;
import org.eclipse.paho.client.mqttv3.a.b.r;
import org.eclipse.paho.client.mqttv3.a.j;
import org.eclipse.paho.client.mqttv3.a.l;
import org.eclipse.paho.client.mqttv3.a.n;
import org.eclipse.paho.client.mqttv3.a.q;
import org.eclipse.paho.client.mqttv3.a.s;
import org.eclipse.paho.client.mqttv3.a.t;
import org.eclipse.paho.client.mqttv3.b.b;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class MqttAsyncClient implements IMqttAsyncClient {
    static Class b = null;
    private static final String c;
    private static final Logger d;
    private static final String e = "paho";
    private static final long f = 30000;
    private static final long g = 10000;
    private static final char h = 55296;
    private static final char i = 56319;

    /* renamed from: a, reason: collision with root package name */
    protected a f458a;
    private String j;
    private String k;
    private Hashtable l;
    private MqttClientPersistence m;

    static {
        Class<?> cls = b;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.MqttAsyncClient");
                b = cls;
            } catch (ClassNotFoundException e2) {
                throw new NoClassDefFoundError(e2.getMessage());
            }
        }
        c = cls.getName();
        d = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, c);
    }

    public MqttAsyncClient(String str, String str2) {
        this(str, str2, new b());
    }

    public MqttAsyncClient(String str, String str2, MqttClientPersistence mqttClientPersistence) {
        this(str, str2, mqttClientPersistence, new TimerPingSender());
    }

    public MqttAsyncClient(String str, String str2, MqttClientPersistence mqttClientPersistence, MqttPingSender mqttPingSender) {
        d.setResourceName(str2);
        if (str2 == null) {
            throw new IllegalArgumentException("Null clientId");
        }
        int i2 = 0;
        int i3 = 0;
        while (i2 < str2.length() - 1) {
            if (a(str2.charAt(i2))) {
                i2++;
            }
            i3++;
            i2++;
        }
        if (i3 > 65535) {
            throw new IllegalArgumentException("ClientId longer than 65535 characters");
        }
        MqttConnectOptions.a(str);
        this.k = str;
        this.j = str2;
        this.m = mqttClientPersistence;
        if (this.m == null) {
            this.m = new org.eclipse.paho.client.mqttv3.b.a();
        }
        d.fine(c, "MqttAsyncClient", "101", new Object[]{str2, str, mqttClientPersistence});
        this.m.open(str2, str);
        this.f458a = new a(this, this.m, mqttPingSender);
        this.m.close();
        this.l = new Hashtable();
    }

    private int a(String str, int i2) {
        int iLastIndexOf = str.lastIndexOf(58);
        return iLastIndexOf == -1 ? i2 : Integer.parseInt(str.substring(iLastIndexOf + 1));
    }

    protected static boolean a(char c2) {
        return c2 >= 55296 && c2 <= 56319;
    }

    private String b(String str) {
        int iLastIndexOf = str.lastIndexOf(47);
        int iLastIndexOf2 = str.lastIndexOf(58);
        if (iLastIndexOf2 == -1) {
            iLastIndexOf2 = str.length();
        }
        return str.substring(iLastIndexOf + 1, iLastIndexOf2);
    }

    private q b(String str, MqttConnectOptions mqttConnectOptions) throws NoSuchAlgorithmException, UnrecoverableKeyException, MqttException, IOException, KeyStoreException, CertificateException, KeyManagementException {
        org.eclipse.paho.client.mqttv3.a.a.a aVar;
        String[] strArrQ;
        d.fine(c, "createNetworkModule", "115", new Object[]{str});
        SocketFactory socketFactory = mqttConnectOptions.getSocketFactory();
        switch (MqttConnectOptions.a(str)) {
            case 0:
                String strSubstring = str.substring(6);
                String strB = b(strSubstring);
                int iA = a(strSubstring, 1883);
                if (socketFactory == null) {
                    socketFactory = SocketFactory.getDefault();
                } else if (socketFactory instanceof SSLSocketFactory) {
                    throw l.a(32105);
                }
                t tVar = new t(socketFactory, strB, iA, this.j);
                tVar.b(mqttConnectOptions.getConnectionTimeout());
                return tVar;
            case 1:
                String strSubstring2 = str.substring(6);
                String strB2 = b(strSubstring2);
                int iA2 = a(strSubstring2, 8883);
                if (socketFactory == null) {
                    org.eclipse.paho.client.mqttv3.a.a.a aVar2 = new org.eclipse.paho.client.mqttv3.a.a.a();
                    Properties sSLProperties = mqttConnectOptions.getSSLProperties();
                    if (sSLProperties != null) {
                        aVar2.a(sSLProperties, (String) null);
                    }
                    aVar = aVar2;
                    socketFactory = aVar2.s(null);
                } else {
                    if (!(socketFactory instanceof SSLSocketFactory)) {
                        throw l.a(32105);
                    }
                    aVar = null;
                }
                s sVar = new s((SSLSocketFactory) socketFactory, strB2, iA2, this.j);
                sVar.a(mqttConnectOptions.getConnectionTimeout());
                if (aVar == null || (strArrQ = aVar.q(null)) == null) {
                    return sVar;
                }
                sVar.a(strArrQ);
                return sVar;
            case 2:
                return new n(str.substring(8));
            default:
                return null;
        }
    }

    public static String generateClientId() {
        return new StringBuffer(e).append(System.nanoTime()).toString();
    }

    protected MqttTopic a(String str) {
        MqttTopic.validate(str, false);
        MqttTopic mqttTopic = (MqttTopic) this.l.get(str);
        if (mqttTopic != null) {
            return mqttTopic;
        }
        MqttTopic mqttTopic2 = new MqttTopic(str, this.f458a);
        this.l.put(str, mqttTopic2);
        return mqttTopic2;
    }

    protected q[] a(String str, MqttConnectOptions mqttConnectOptions) {
        d.fine(c, "createNetworkModules", "116", new Object[]{str});
        String[] serverURIs = mqttConnectOptions.getServerURIs();
        if (serverURIs == null || serverURIs.length == 0) {
            serverURIs = new String[]{str};
        }
        q[] qVarArr = new q[serverURIs.length];
        for (int i2 = 0; i2 < serverURIs.length; i2++) {
            qVarArr[i2] = b(serverURIs[i2], mqttConnectOptions);
        }
        d.fine(c, "createNetworkModules", "108");
        return qVarArr;
    }

    public IMqttToken checkPing(Object obj, IMqttActionListener iMqttActionListener) {
        d.fine(c, "ping", "117");
        MqttToken mqttTokenP = this.f458a.p();
        d.fine(c, "ping", "118");
        return mqttTokenP;
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public void close() {
        d.fine(c, JniUscClient.s, "113");
        this.f458a.b();
        d.fine(c, JniUscClient.s, "114");
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken connect() {
        return connect(null, null);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken connect(Object obj, IMqttActionListener iMqttActionListener) {
        return connect(new MqttConnectOptions(), obj, iMqttActionListener);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken connect(MqttConnectOptions mqttConnectOptions) {
        return connect(mqttConnectOptions, null, null);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken connect(MqttConnectOptions mqttConnectOptions, Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        if (this.f458a.c()) {
            throw l.a(32100);
        }
        if (this.f458a.d()) {
            throw new MqttException(32110);
        }
        if (this.f458a.f()) {
            throw new MqttException(32102);
        }
        if (this.f458a.g()) {
            throw new MqttException(32111);
        }
        Logger logger = d;
        String str = c;
        Object[] objArr = new Object[8];
        objArr[0] = Boolean.valueOf(mqttConnectOptions.isCleanSession());
        objArr[1] = new Integer(mqttConnectOptions.getConnectionTimeout());
        objArr[2] = new Integer(mqttConnectOptions.getKeepAliveInterval());
        objArr[3] = mqttConnectOptions.getUserName();
        objArr[4] = mqttConnectOptions.getPassword() == null ? "[null]" : "[notnull]";
        objArr[5] = mqttConnectOptions.getWillMessage() == null ? "[null]" : "[notnull]";
        objArr[6] = obj;
        objArr[7] = iMqttActionListener;
        logger.fine(str, "connect", "103", objArr);
        this.f458a.a(a(this.k, mqttConnectOptions));
        MqttToken mqttToken = new MqttToken(getClientId());
        j jVar = new j(this, this.m, this.f458a, mqttConnectOptions, mqttToken, obj, iMqttActionListener);
        mqttToken.setActionCallback(jVar);
        mqttToken.setUserContext(this);
        this.f458a.a(0);
        jVar.a();
        return mqttToken;
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken disconnect() {
        return disconnect(null, null);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken disconnect(long j) {
        return disconnect(j, null, null);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken disconnect(long j, Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        d.fine(c, "disconnect", "104", new Object[]{new Long(j), obj, iMqttActionListener});
        MqttToken mqttToken = new MqttToken(getClientId());
        mqttToken.setActionCallback(iMqttActionListener);
        mqttToken.setUserContext(obj);
        try {
            this.f458a.a(new e(), j, mqttToken);
            d.fine(c, "disconnect", "108");
            return mqttToken;
        } catch (MqttException e2) {
            d.fine(c, "disconnect", "105", null, e2);
            throw e2;
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken disconnect(Object obj, IMqttActionListener iMqttActionListener) {
        return disconnect(30000L, obj, iMqttActionListener);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public void disconnectForcibly() {
        disconnectForcibly(30000L, g);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public void disconnectForcibly(long j) {
        disconnectForcibly(30000L, j);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public void disconnectForcibly(long j, long j2) {
        this.f458a.a(j, j2);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public String getClientId() {
        return this.j;
    }

    public org.eclipse.paho.client.mqttv3.c.a getDebug() {
        return new org.eclipse.paho.client.mqttv3.c.a(this.j, this.f458a);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttDeliveryToken[] getPendingDeliveryTokens() {
        return this.f458a.j();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public String getServerURI() {
        return this.k;
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public boolean isConnected() {
        return this.f458a.c();
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttDeliveryToken publish(String str, MqttMessage mqttMessage) {
        return publish(str, mqttMessage, (Object) null, (IMqttActionListener) null);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttDeliveryToken publish(String str, MqttMessage mqttMessage, Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        d.fine(c, "publish", "111", new Object[]{str, obj, iMqttActionListener});
        MqttTopic.validate(str, false);
        MqttDeliveryToken mqttDeliveryToken = new MqttDeliveryToken(getClientId());
        mqttDeliveryToken.setActionCallback(iMqttActionListener);
        mqttDeliveryToken.setUserContext(obj);
        mqttDeliveryToken.a(mqttMessage);
        mqttDeliveryToken.internalTok.a(new String[]{str});
        this.f458a.b(new o(str, mqttMessage), mqttDeliveryToken);
        d.fine(c, "publish", "112");
        return mqttDeliveryToken;
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttDeliveryToken publish(String str, byte[] bArr, int i2, boolean z) {
        return publish(str, bArr, i2, z, null, null);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttDeliveryToken publish(String str, byte[] bArr, int i2, boolean z, Object obj, IMqttActionListener iMqttActionListener) {
        MqttMessage mqttMessage = new MqttMessage(bArr);
        mqttMessage.setQos(i2);
        mqttMessage.setRetained(z);
        return publish(str, mqttMessage, obj, iMqttActionListener);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public void setCallback(MqttCallback mqttCallback) {
        this.f458a.a(mqttCallback);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken subscribe(String str, int i2) {
        return subscribe(new String[]{str}, new int[]{i2}, (Object) null, (IMqttActionListener) null);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken subscribe(String str, int i2, Object obj, IMqttActionListener iMqttActionListener) {
        return subscribe(new String[]{str}, new int[]{i2}, obj, iMqttActionListener);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken subscribe(String[] strArr, int[] iArr) {
        return subscribe(strArr, iArr, (Object) null, (IMqttActionListener) null);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken subscribe(String[] strArr, int[] iArr, Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        if (strArr.length != iArr.length) {
            throw new IllegalArgumentException();
        }
        String string = "";
        int i2 = 0;
        while (i2 < strArr.length) {
            if (i2 > 0) {
                string = new StringBuffer(String.valueOf(string)).append(", ").toString();
            }
            String string2 = new StringBuffer(String.valueOf(string)).append("topic=").append(strArr[i2]).append(" qos=").append(iArr[i2]).toString();
            MqttTopic.validate(strArr[i2], true);
            i2++;
            string = string2;
        }
        d.fine(c, "subscribe", "106", new Object[]{string, obj, iMqttActionListener});
        MqttToken mqttToken = new MqttToken(getClientId());
        mqttToken.setActionCallback(iMqttActionListener);
        mqttToken.setUserContext(obj);
        mqttToken.internalTok.a(strArr);
        this.f458a.b(new r(strArr, iArr), mqttToken);
        d.fine(c, "subscribe", "109");
        return mqttToken;
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken unsubscribe(String str) {
        return unsubscribe(new String[]{str}, (Object) null, (IMqttActionListener) null);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken unsubscribe(String str, Object obj, IMqttActionListener iMqttActionListener) {
        return unsubscribe(new String[]{str}, obj, iMqttActionListener);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken unsubscribe(String[] strArr) {
        return unsubscribe(strArr, (Object) null, (IMqttActionListener) null);
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken unsubscribe(String[] strArr, Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        String string = "";
        int i2 = 0;
        while (i2 < strArr.length) {
            if (i2 > 0) {
                string = new StringBuffer(String.valueOf(string)).append(", ").toString();
            }
            String string2 = new StringBuffer(String.valueOf(string)).append(strArr[i2]).toString();
            MqttTopic.validate(strArr[i2], true);
            i2++;
            string = string2;
        }
        d.fine(c, "unsubscribe", "107", new Object[]{string, obj, iMqttActionListener});
        MqttToken mqttToken = new MqttToken(getClientId());
        mqttToken.setActionCallback(iMqttActionListener);
        mqttToken.setUserContext(obj);
        mqttToken.internalTok.a(strArr);
        this.f458a.b(new org.eclipse.paho.client.mqttv3.a.b.t(strArr), mqttToken);
        d.fine(c, "unsubscribe", "110");
        return mqttToken;
    }
}
