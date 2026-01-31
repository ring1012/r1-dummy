package org.eclipse.paho.client.mqttv3.a;

import java.io.IOException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class s extends t {

    /* renamed from: a, reason: collision with root package name */
    static Class f501a;
    private static final String d;
    private static final Logger e;
    private String[] f;
    private int g;

    static {
        Class<?> cls = f501a;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.a.s");
                f501a = cls;
            } catch (ClassNotFoundException e2) {
                throw new NoClassDefFoundError(e2.getMessage());
            }
        }
        d = cls.getName();
        e = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, d);
    }

    public s(SSLSocketFactory sSLSocketFactory, String str, int i, String str2) {
        super(sSLSocketFactory, str, i, str2);
        e.setResourceName(str2);
    }

    @Override // org.eclipse.paho.client.mqttv3.a.t, org.eclipse.paho.client.mqttv3.a.q
    public void a() throws MqttException, IOException {
        super.a();
        a(this.f);
        int soTimeout = this.b.getSoTimeout();
        if (soTimeout == 0) {
            this.b.setSoTimeout(this.g * 1000);
        }
        ((SSLSocket) this.b).startHandshake();
        this.b.setSoTimeout(soTimeout);
    }

    public void a(int i) {
        super.b(i);
        this.g = i;
    }

    public void a(String[] strArr) {
        this.f = strArr;
        if (this.b == null || strArr == null) {
            return;
        }
        if (e.isLoggable(5)) {
            String string = "";
            int i = 0;
            while (i < strArr.length) {
                if (i > 0) {
                    string = new StringBuffer(String.valueOf(string)).append(",").toString();
                }
                String string2 = new StringBuffer(String.valueOf(string)).append(strArr[i]).toString();
                i++;
                string = string2;
            }
            e.fine(d, "setEnabledCiphers", "260", new Object[]{string});
        }
        ((SSLSocket) this.b).setEnabledCipherSuites(strArr);
    }

    public String[] e() {
        return this.f;
    }
}
