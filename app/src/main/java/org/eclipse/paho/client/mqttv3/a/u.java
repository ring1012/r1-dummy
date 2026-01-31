package org.eclipse.paho.client.mqttv3.a;

import com.tencent.bugly.Bugly;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class u {
    static Class b;
    private static final String c;
    private static final Logger d;
    private String m;
    private volatile boolean e = false;
    private boolean f = false;
    private boolean g = false;
    private Object h = new Object();
    private Object i = new Object();

    /* renamed from: a, reason: collision with root package name */
    protected MqttMessage f503a = null;
    private org.eclipse.paho.client.mqttv3.a.b.u j = null;
    private MqttException k = null;
    private String[] l = null;
    private IMqttAsyncClient n = null;
    private IMqttActionListener o = null;
    private Object p = null;
    private int q = 0;
    private boolean r = false;

    static {
        Class<?> cls = b;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.a.u");
                b = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        c = cls.getName();
        d = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, c);
    }

    public u(String str) {
        d.setResourceName(str);
    }

    public int a() {
        return this.q;
    }

    public void a(int i) {
        this.q = i;
    }

    public void a(long j) {
        d.fine(c, "waitForCompletion", "407", new Object[]{s(), new Long(j), this});
        if (b(j) != null || this.e) {
            b();
        } else {
            d.fine(c, "waitForCompletion", "406", new Object[]{s(), this});
            this.k = new MqttException(32000);
            throw this.k;
        }
    }

    public void a(Object obj) {
        this.p = obj;
    }

    public void a(String str) {
        this.m = str;
    }

    public void a(IMqttActionListener iMqttActionListener) {
        this.o = iMqttActionListener;
    }

    protected void a(IMqttAsyncClient iMqttAsyncClient) {
        this.n = iMqttAsyncClient;
    }

    public void a(MqttException mqttException) {
        synchronized (this.h) {
            this.k = mqttException;
        }
    }

    public void a(MqttMessage mqttMessage) {
        this.f503a = mqttMessage;
    }

    protected void a(org.eclipse.paho.client.mqttv3.a.b.u uVar, MqttException mqttException) {
        d.fine(c, "markComplete", "404", new Object[]{s(), uVar, mqttException});
        synchronized (this.h) {
            if (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.b) {
                this.f503a = null;
            }
            this.f = true;
            this.j = uVar;
            this.k = mqttException;
        }
    }

    public void a(boolean z) {
        this.r = z;
    }

    public void a(String[] strArr) {
        this.l = strArr;
    }

    protected org.eclipse.paho.client.mqttv3.a.b.u b(long j) {
        synchronized (this.h) {
            Logger logger = d;
            String str = c;
            Object[] objArr = new Object[7];
            objArr[0] = s();
            objArr[1] = new Long(j);
            objArr[2] = new Boolean(this.g);
            objArr[3] = new Boolean(this.e);
            objArr[4] = this.k == null ? Bugly.SDK_IS_DEV : "true";
            objArr[5] = this.j;
            objArr[6] = this;
            logger.fine(str, "waitForResponse", "400", objArr, this.k);
            while (!this.e) {
                if (this.k == null) {
                    try {
                        d.fine(c, "waitForResponse", "408", new Object[]{s(), new Long(j)});
                        if (j <= 0) {
                            this.h.wait();
                        } else {
                            this.h.wait(j);
                        }
                    } catch (InterruptedException e) {
                        this.k = new MqttException(e);
                    }
                }
                if (!this.e) {
                    if (this.k != null) {
                        d.fine(c, "waitForResponse", "401", null, this.k);
                        throw this.k;
                    }
                    if (j > 0) {
                        break;
                    }
                }
            }
        }
        d.fine(c, "waitForResponse", "402", new Object[]{s(), this.j});
        return this.j;
    }

    public boolean b() throws MqttException {
        if (c() != null) {
            throw c();
        }
        return true;
    }

    public MqttException c() {
        return this.k;
    }

    public boolean d() {
        return this.e;
    }

    protected boolean e() {
        return this.f;
    }

    protected boolean f() {
        return (m() == null || d()) ? false : true;
    }

    public IMqttActionListener g() {
        return this.o;
    }

    public void h() {
        a(-1L);
    }

    protected org.eclipse.paho.client.mqttv3.a.b.u i() {
        return b(-1L);
    }

    protected void j() {
        d.fine(c, "notifyComplete", "404", new Object[]{s(), this.j, this.k});
        synchronized (this.h) {
            if (this.k == null && this.f) {
                this.e = true;
                this.f = false;
            } else {
                this.f = false;
            }
            this.h.notifyAll();
        }
        synchronized (this.i) {
            this.g = true;
            this.i.notifyAll();
        }
    }

    public void k() {
        synchronized (this.i) {
            synchronized (this.h) {
                if (this.k != null) {
                    throw this.k;
                }
            }
            while (!this.g) {
                try {
                    d.fine(c, "waitUntilSent", "409", new Object[]{s()});
                    this.i.wait();
                } catch (InterruptedException e) {
                }
            }
            if (!this.g) {
                if (this.k != null) {
                    throw this.k;
                }
                throw l.a(6);
            }
        }
    }

    protected void l() {
        d.fine(c, "notifySent", "403", new Object[]{s()});
        synchronized (this.h) {
            this.j = null;
            this.e = false;
        }
        synchronized (this.i) {
            this.g = true;
            this.i.notifyAll();
        }
    }

    public IMqttAsyncClient m() {
        return this.n;
    }

    public void n() throws MqttException {
        if (f()) {
            throw new MqttException(32201);
        }
        d.fine(c, "reset", "410", new Object[]{s()});
        this.n = null;
        this.e = false;
        this.j = null;
        this.g = false;
        this.k = null;
        this.p = null;
    }

    public MqttMessage o() {
        return this.f503a;
    }

    public org.eclipse.paho.client.mqttv3.a.b.u p() {
        return this.j;
    }

    public String[] q() {
        return this.l;
    }

    public Object r() {
        return this.p;
    }

    public String s() {
        return this.m;
    }

    public boolean t() {
        return this.r;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("key=").append(s());
        stringBuffer.append(" ,topics=");
        if (q() != null) {
            for (int i = 0; i < q().length; i++) {
                stringBuffer.append(q()[i]).append(", ");
            }
        }
        stringBuffer.append(" ,usercontext=").append(r());
        stringBuffer.append(" ,isComplete=").append(d());
        stringBuffer.append(" ,isNotified=").append(t());
        stringBuffer.append(" ,exception=").append(c());
        stringBuffer.append(" ,actioncallback=").append(g());
        return stringBuffer.toString();
    }

    public int[] u() {
        return this.j instanceof org.eclipse.paho.client.mqttv3.a.b.q ? ((org.eclipse.paho.client.mqttv3.a.b.q) this.j).b() : new int[0];
    }

    public boolean v() {
        if (this.j instanceof org.eclipse.paho.client.mqttv3.a.b.c) {
            return ((org.eclipse.paho.client.mqttv3.a.b.c) this.j).a_();
        }
        return false;
    }

    public org.eclipse.paho.client.mqttv3.a.b.u w() {
        return this.j;
    }
}
