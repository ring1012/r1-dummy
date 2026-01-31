package org.eclipse.paho.client.mqttv3.a;

import java.util.Vector;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class f implements Runnable {
    static Class b = null;
    private static final String c;
    private static final Logger d;
    private static final int e = 10;
    private MqttCallback f;
    private a g;
    private Thread l;
    private e o;

    /* renamed from: a, reason: collision with root package name */
    public boolean f491a = false;
    private boolean j = false;
    private Object k = new Object();
    private Object m = new Object();
    private Object n = new Object();
    private Vector h = new Vector(10);
    private Vector i = new Vector(10);

    static {
        Class<?> cls = b;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.a.f");
                b = cls;
            } catch (ClassNotFoundException e2) {
                throw new NoClassDefFoundError(e2.getMessage());
            }
        }
        c = cls.getName();
        d = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, c);
    }

    f(a aVar) {
        this.g = aVar;
        d.setResourceName(aVar.k().getClientId());
    }

    private void b(org.eclipse.paho.client.mqttv3.a.b.o oVar) throws MqttException {
        if (this.f != null) {
            String strB = oVar.b();
            d.fine(c, "handleMessage", "713", new Object[]{new Integer(oVar.i()), strB});
            this.f.messageArrived(strB, oVar.g());
            if (oVar.g().getQos() == 1) {
                this.g.a(new org.eclipse.paho.client.mqttv3.a.b.k(oVar), new MqttToken(this.g.k().getClientId()));
            } else if (oVar.g().getQos() == 2) {
                this.g.a(oVar);
                this.g.a(new org.eclipse.paho.client.mqttv3.a.b.l(oVar), new MqttToken(this.g.k().getClientId()));
            }
        }
    }

    private void c(MqttToken mqttToken) {
        synchronized (mqttToken) {
            d.fine(c, "handleActionComplete", "705", new Object[]{mqttToken.internalTok.s()});
            mqttToken.internalTok.j();
            if (!mqttToken.internalTok.t()) {
                if (this.f != null && (mqttToken instanceof MqttDeliveryToken) && mqttToken.isComplete()) {
                    this.f.deliveryComplete((MqttDeliveryToken) mqttToken);
                }
                a(mqttToken);
            }
            if (mqttToken.isComplete() && ((mqttToken instanceof MqttDeliveryToken) || (mqttToken.getActionCallback() instanceof IMqttActionListener))) {
                mqttToken.internalTok.a(true);
            }
            if (mqttToken.isComplete()) {
                this.o.a(mqttToken);
            }
        }
    }

    public void a() {
        synchronized (this.k) {
            if (this.f491a) {
                d.fine(c, "stop", "700");
                this.f491a = false;
                if (!Thread.currentThread().equals(this.l)) {
                    try {
                        synchronized (this.m) {
                            d.fine(c, "stop", "701");
                            this.m.notifyAll();
                        }
                        this.l.join();
                    } catch (InterruptedException e2) {
                    }
                }
            }
            this.l = null;
            d.fine(c, "stop", "703");
        }
    }

    public void a(String str) {
        synchronized (this.k) {
            if (!this.f491a) {
                this.h.clear();
                this.i.clear();
                this.f491a = true;
                this.j = false;
                this.l = new Thread(this, str);
                this.l.start();
            }
        }
    }

    public void a(MqttCallback mqttCallback) {
        this.f = mqttCallback;
    }

    public void a(MqttException mqttException) {
        try {
            if (this.f == null || mqttException == null) {
                return;
            }
            d.fine(c, "connectionLost", "708", new Object[]{mqttException});
            this.f.connectionLost(mqttException);
        } catch (Throwable th) {
            d.fine(c, "connectionLost", "720", new Object[]{th});
        }
    }

    public void a(MqttToken mqttToken) {
        IMqttActionListener actionCallback;
        if (mqttToken == null || (actionCallback = mqttToken.getActionCallback()) == null) {
            return;
        }
        if (mqttToken.getException() == null) {
            d.fine(c, "fireActionEvent", "716", new Object[]{mqttToken.internalTok.s()});
            actionCallback.onSuccess(mqttToken);
        } else {
            d.fine(c, "fireActionEvent", "716", new Object[]{mqttToken.internalTok.s()});
            actionCallback.onFailure(mqttToken, mqttToken.getException());
        }
    }

    public void a(org.eclipse.paho.client.mqttv3.a.b.o oVar) {
        if (this.f != null) {
            synchronized (this.n) {
                while (this.f491a && !this.j && this.h.size() >= 10) {
                    try {
                        d.fine(c, "messageArrived", "709");
                        this.n.wait(200L);
                    } catch (InterruptedException e2) {
                    }
                }
            }
            if (this.j) {
                return;
            }
            this.h.addElement(oVar);
            synchronized (this.m) {
                d.fine(c, "messageArrived", "710");
                this.m.notifyAll();
            }
        }
    }

    public void a(e eVar) {
        this.o = eVar;
    }

    public void b() {
        this.j = true;
        synchronized (this.n) {
            d.fine(c, "quiesce", "711");
            this.n.notifyAll();
        }
    }

    public void b(MqttToken mqttToken) {
        if (this.f491a) {
            this.i.addElement(mqttToken);
            synchronized (this.m) {
                d.fine(c, "asyncOperationComplete", "715", new Object[]{mqttToken.internalTok.s()});
                this.m.notifyAll();
            }
            return;
        }
        try {
            c(mqttToken);
        } catch (Throwable th) {
            d.fine(c, "asyncOperationComplete", "719", null, th);
            this.g.a((MqttToken) null, new MqttException(th));
        }
    }

    public boolean c() {
        return this.j && this.i.size() == 0 && this.h.size() == 0;
    }

    protected Thread d() {
        return this.l;
    }

    @Override // java.lang.Runnable
    public void run() {
        MqttToken mqttToken;
        org.eclipse.paho.client.mqttv3.a.b.o oVar;
        while (this.f491a) {
            try {
                try {
                    try {
                        synchronized (this.m) {
                            if (this.f491a && this.h.isEmpty() && this.i.isEmpty()) {
                                d.fine(c, "run", "704");
                                this.m.wait();
                            }
                        }
                    } catch (InterruptedException e2) {
                    }
                    if (this.f491a) {
                        synchronized (this.i) {
                            if (this.i.isEmpty()) {
                                mqttToken = null;
                            } else {
                                mqttToken = (MqttToken) this.i.elementAt(0);
                                this.i.removeElementAt(0);
                            }
                        }
                        if (mqttToken != null) {
                            c(mqttToken);
                        }
                        synchronized (this.h) {
                            if (this.h.isEmpty()) {
                                oVar = null;
                            } else {
                                oVar = (org.eclipse.paho.client.mqttv3.a.b.o) this.h.elementAt(0);
                                this.h.removeElementAt(0);
                            }
                        }
                        if (oVar != null) {
                            b(oVar);
                        }
                    }
                    if (this.j) {
                        this.o.f();
                    }
                } catch (Throwable th) {
                    d.fine(c, "run", "714", null, th);
                    this.f491a = false;
                    this.g.a((MqttToken) null, new MqttException(th));
                }
                synchronized (this.n) {
                    d.fine(c, "run", "706");
                    this.n.notifyAll();
                }
            } catch (Throwable th2) {
                synchronized (this.n) {
                    d.fine(c, "run", "706");
                    this.n.notifyAll();
                    throw th2;
                }
            }
        }
    }
}
