package org.eclipse.paho.client.mqttv3.a;

import java.io.IOException;
import java.io.OutputStream;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class h implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    static Class f493a;
    private static final String b;
    private static final Logger c;
    private e f;
    private org.eclipse.paho.client.mqttv3.a.b.g g;
    private a h;
    private i i;
    private boolean d = false;
    private Object e = new Object();
    private Thread j = null;

    static {
        Class<?> cls = f493a;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.a.h");
                f493a = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        b = cls.getName();
        c = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, b);
    }

    public h(a aVar, e eVar, i iVar, OutputStream outputStream) {
        this.f = null;
        this.h = null;
        this.i = null;
        this.g = new org.eclipse.paho.client.mqttv3.a.b.g(eVar, outputStream);
        this.h = aVar;
        this.f = eVar;
        this.i = iVar;
        c.setResourceName(aVar.k().getClientId());
    }

    private void a(org.eclipse.paho.client.mqttv3.a.b.u uVar, Exception exc) {
        c.fine(b, "handleRunException", "804", null, exc);
        MqttException mqttException = !(exc instanceof MqttException) ? new MqttException(32109, exc) : (MqttException) exc;
        this.d = false;
        this.h.a((MqttToken) null, mqttException);
    }

    public void a() {
        synchronized (this.e) {
            c.fine(b, "stop", "800");
            if (this.d) {
                this.d = false;
                if (!Thread.currentThread().equals(this.j)) {
                    try {
                        this.f.h();
                        this.j.join();
                    } catch (InterruptedException e) {
                    }
                }
            }
            this.j = null;
            c.fine(b, "stop", "801");
        }
    }

    public void a(String str) {
        synchronized (this.e) {
            if (!this.d) {
                this.d = true;
                this.j = new Thread(this, str);
                this.j.start();
            }
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        org.eclipse.paho.client.mqttv3.a.b.u uVarE = null;
        while (this.d && this.g != null) {
            try {
                uVarE = this.f.e();
                if (uVarE != null) {
                    c.fine(b, "run", "802", new Object[]{uVarE.e(), uVarE});
                    if (uVarE instanceof org.eclipse.paho.client.mqttv3.a.b.b) {
                        this.g.a(uVarE);
                        this.g.flush();
                    } else {
                        MqttToken mqttTokenA = this.i.a(uVarE);
                        if (mqttTokenA != null) {
                            synchronized (mqttTokenA) {
                                this.g.a(uVarE);
                                try {
                                    this.g.flush();
                                } catch (IOException e) {
                                    if (!(uVarE instanceof org.eclipse.paho.client.mqttv3.a.b.e)) {
                                        throw e;
                                    }
                                }
                                this.f.a(uVarE);
                            }
                        } else {
                            continue;
                        }
                    }
                } else {
                    c.fine(b, "run", "803");
                    this.d = false;
                }
            } catch (MqttException e2) {
                a(uVarE, e2);
            } catch (Exception e3) {
                a(uVarE, e3);
            }
        }
        c.fine(b, "run", "805");
    }
}
