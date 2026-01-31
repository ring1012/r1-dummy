package org.eclipse.paho.client.mqttv3.a;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.SocketFactory;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class t implements q {

    /* renamed from: a, reason: collision with root package name */
    private static final String f502a;
    static Class c;
    private static final Logger d;
    protected Socket b;
    private SocketFactory e;
    private String f;
    private int g;
    private int h;

    static {
        Class<?> cls = c;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.a.t");
                c = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        f502a = cls.getName();
        d = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, f502a);
    }

    public t(SocketFactory socketFactory, String str, int i, String str2) {
        d.setResourceName(str2);
        this.e = socketFactory;
        this.f = str;
        this.g = i;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.q
    public void a() throws MqttException, IOException {
        try {
            d.fine(f502a, "start", "252", new Object[]{this.f, new Integer(this.g), new Long(this.h * 1000)});
            InetSocketAddress inetSocketAddress = new InetSocketAddress(this.f, this.g);
            this.b = this.e.createSocket();
            this.b.connect(inetSocketAddress, this.h * 1000);
        } catch (ConnectException e) {
            d.fine(f502a, "start", "250", null, e);
            throw new MqttException(32103, e);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.a.q
    public InputStream b() {
        return this.b.getInputStream();
    }

    public void b(int i) {
        this.h = i;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.q
    public OutputStream c() {
        return this.b.getOutputStream();
    }

    @Override // org.eclipse.paho.client.mqttv3.a.q
    public void d() throws IOException {
        if (this.b != null) {
            this.b.close();
        }
    }
}
