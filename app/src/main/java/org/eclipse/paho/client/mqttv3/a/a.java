package org.eclipse.paho.client.mqttv3.a;

import cn.yunzhisheng.asr.JniUscClient;
import com.unisound.vui.handler.session.music.syncloud.MusicStateMgr;
import java.util.Enumeration;
import java.util.Properties;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    public static String f468a = "${project.version}";
    public static String b = "L${build.level}";
    static Class c = null;
    private static final String d;
    private static final Logger e;
    private static final byte f = 0;
    private static final byte g = 1;
    private static final byte h = 2;
    private static final byte i = 3;
    private static final byte j = 4;
    private IMqttAsyncClient k;
    private int l;
    private q[] m;
    private g n;
    private h o;
    private f p;
    private e q;
    private MqttConnectOptions r;
    private MqttClientPersistence s;
    private MqttPingSender t;
    private i u;
    private byte w;
    private boolean v = false;
    private Object x = new Object();
    private boolean y = false;

    static {
        Class<?> cls = c;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.a.a");
                c = cls;
            } catch (ClassNotFoundException e2) {
                throw new NoClassDefFoundError(e2.getMessage());
            }
        }
        d = cls.getName();
        e = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, d);
    }

    public a(IMqttAsyncClient iMqttAsyncClient, MqttClientPersistence mqttClientPersistence, MqttPingSender mqttPingSender) {
        this.w = (byte) 3;
        this.w = (byte) 3;
        this.k = iMqttAsyncClient;
        this.s = mqttClientPersistence;
        this.t = mqttPingSender;
        this.t.init(this);
        this.u = new i(k().getClientId());
        this.p = new f(this);
        this.q = new e(mqttClientPersistence, this.u, this.p, this, mqttPingSender);
        this.p.a(this.q);
        e.setResourceName(k().getClientId());
    }

    static i a(a aVar) {
        return aVar.u;
    }

    private void a(Exception exc) {
        e.fine(d, "handleRunException", "804", null, exc);
        a((MqttToken) null, !(exc instanceof MqttException) ? new MqttException(32109, exc) : (MqttException) exc);
    }

    static void a(a aVar, g gVar) {
        aVar.n = gVar;
    }

    static void a(a aVar, h hVar) {
        aVar.o = hVar;
    }

    private MqttToken b(MqttToken mqttToken, MqttException mqttException) {
        e.fine(d, "handleOldTokens", "222");
        MqttToken mqttToken2 = null;
        if (mqttToken != null) {
            try {
                if (this.u.a(mqttToken.internalTok.s()) == null) {
                    this.u.a(mqttToken, mqttToken.internalTok.s());
                }
            } catch (Exception e2) {
                return null;
            }
        }
        Enumeration enumerationElements = this.q.a(mqttException).elements();
        while (enumerationElements.hasMoreElements()) {
            try {
                MqttToken mqttToken3 = (MqttToken) enumerationElements.nextElement();
                if (mqttToken3.internalTok.s().equals(org.eclipse.paho.client.mqttv3.a.b.e.f475a) || mqttToken3.internalTok.s().equals("Con")) {
                    mqttToken2 = mqttToken3;
                } else {
                    this.p.b(mqttToken3);
                }
            } catch (Exception e3) {
                return mqttToken2;
            }
        }
        return mqttToken2;
    }

    static q[] b(a aVar) {
        return aVar.m;
    }

    static int c(a aVar) {
        return aVar.l;
    }

    static e d(a aVar) {
        return aVar.q;
    }

    static g e(a aVar) {
        return aVar.n;
    }

    static h f(a aVar) {
        return aVar.o;
    }

    static f g(a aVar) {
        return aVar.p;
    }

    static Logger q() {
        return e;
    }

    static String r() {
        return d;
    }

    protected MqttTopic a(String str) {
        return new MqttTopic(str, this);
    }

    g a() {
        return this.n;
    }

    public void a(int i2) {
        this.l = i2;
    }

    public void a(long j2, long j3) {
        this.q.c(j2);
        MqttToken mqttToken = new MqttToken(this.k.getClientId());
        try {
            a(new org.eclipse.paho.client.mqttv3.a.b.e(), mqttToken);
            mqttToken.waitForCompletion(j3);
        } catch (Exception e2) {
        } catch (Throwable th) {
            mqttToken.internalTok.a(null, null);
            a(mqttToken, (MqttException) null);
            throw th;
        }
        mqttToken.internalTok.a(null, null);
        a(mqttToken, (MqttException) null);
    }

    public void a(MqttCallback mqttCallback) {
        this.p.a(mqttCallback);
    }

    public void a(MqttConnectOptions mqttConnectOptions, MqttToken mqttToken) {
        synchronized (this.x) {
            if (!e() || this.y) {
                e.fine(d, "connect", "207", new Object[]{new Byte(this.w)});
                if (g() || this.y) {
                    throw new MqttException(32111);
                }
                if (d()) {
                    throw new MqttException(32110);
                }
                if (!f()) {
                    throw l.a(32100);
                }
                throw new MqttException(32102);
            }
            e.fine(d, "connect", "214");
            this.w = (byte) 1;
            this.r = mqttConnectOptions;
            org.eclipse.paho.client.mqttv3.a.b.d dVar = new org.eclipse.paho.client.mqttv3.a.b.d(this.k.getClientId(), mqttConnectOptions.getMqttVersion(), mqttConnectOptions.isCleanSession(), mqttConnectOptions.getKeepAliveInterval(), mqttConnectOptions.getUserName(), mqttConnectOptions.getPassword(), mqttConnectOptions.getWillMessage(), mqttConnectOptions.getWillDestination());
            this.q.a(mqttConnectOptions.getKeepAliveInterval());
            this.q.a(mqttConnectOptions.isCleanSession());
            this.u.a();
            new b(this, this, mqttToken, dVar).a();
        }
    }

    public void a(MqttToken mqttToken, MqttException mqttException) {
        q qVar;
        synchronized (this.x) {
            if (this.v || this.y) {
                return;
            }
            this.v = true;
            e.fine(d, "shutdownConnection", "216");
            boolean z = c() || f();
            this.w = (byte) 2;
            if (mqttToken != null && !mqttToken.isComplete()) {
                mqttToken.internalTok.a(mqttException);
            }
            if (this.p != null) {
                this.p.a();
            }
            try {
                if (this.m != null && (qVar = this.m[this.l]) != null) {
                    qVar.d();
                }
            } catch (Exception e2) {
            }
            if (this.n != null) {
                this.n.a();
            }
            this.u.a(new MqttException(32102));
            MqttToken mqttTokenB = b(mqttToken, mqttException);
            try {
                this.q.b(mqttException);
            } catch (Exception e3) {
            }
            if (this.o != null) {
                this.o.a();
            }
            if (this.t != null) {
                this.t.stop();
            }
            try {
                if (this.s != null) {
                    this.s.close();
                }
            } catch (Exception e4) {
            }
            synchronized (this.x) {
                e.fine(d, "shutdownConnection", "217");
                this.w = (byte) 3;
                this.v = false;
            }
            if ((mqttTokenB != null) & (this.p != null)) {
                this.p.b(mqttTokenB);
            }
            if (z && this.p != null) {
                this.p.a(mqttException);
            }
            synchronized (this.x) {
                if (this.y) {
                    try {
                        b();
                    } catch (Exception e5) {
                    }
                }
            }
        }
    }

    public void a(org.eclipse.paho.client.mqttv3.a.b.c cVar, MqttException mqttException) throws MqttException {
        int iB = cVar.b();
        synchronized (this.x) {
            if (iB != 0) {
                e.fine(d, "connectComplete", "204", new Object[]{new Integer(iB)});
                throw mqttException;
            }
            e.fine(d, "connectComplete", "215");
            this.w = f;
        }
    }

    public void a(org.eclipse.paho.client.mqttv3.a.b.e eVar, long j2, MqttToken mqttToken) {
        synchronized (this.x) {
            if (g()) {
                e.fine(d, "disconnect", "223");
                throw l.a(32111);
            }
            if (e()) {
                e.fine(d, "disconnect", "211");
                throw l.a(32101);
            }
            if (f()) {
                e.fine(d, "disconnect", "219");
                throw l.a(32102);
            }
            if (Thread.currentThread() == this.p.d()) {
                e.fine(d, "disconnect", "210");
                throw l.a(32107);
            }
            e.fine(d, "disconnect", "218");
            this.w = (byte) 2;
            new c(this, eVar, j2, mqttToken).a();
        }
    }

    protected void a(org.eclipse.paho.client.mqttv3.a.b.o oVar) {
        this.q.b(oVar);
    }

    void a(org.eclipse.paho.client.mqttv3.a.b.u uVar, MqttToken mqttToken) throws MqttException {
        e.fine(d, "internalSend", MusicStateMgr.PAGE_SIZE, new Object[]{uVar.e(), uVar, mqttToken});
        if (mqttToken.getClient() != null) {
            e.fine(d, "internalSend", "213", new Object[]{uVar.e(), uVar, mqttToken});
            throw new MqttException(32201);
        }
        mqttToken.internalTok.a(k());
        try {
            this.q.a(uVar, mqttToken);
        } catch (MqttException e2) {
            if (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.o) {
                this.q.a((org.eclipse.paho.client.mqttv3.a.b.o) uVar);
            }
            throw e2;
        }
    }

    public void a(q[] qVarArr) {
        this.m = qVarArr;
    }

    public void b() {
        synchronized (this.x) {
            if (!g()) {
                if (!e()) {
                    e.fine(d, JniUscClient.s, "224");
                    if (d()) {
                        throw new MqttException(32110);
                    }
                    if (c()) {
                        throw l.a(32100);
                    }
                    if (f()) {
                        this.y = true;
                        return;
                    }
                }
                this.w = (byte) 4;
                this.q.i();
                this.q = null;
                this.p = null;
                this.s = null;
                this.o = null;
                this.t = null;
                this.n = null;
                this.m = null;
                this.r = null;
                this.u = null;
            }
        }
    }

    public void b(org.eclipse.paho.client.mqttv3.a.b.u uVar, MqttToken mqttToken) throws MqttException {
        if (c() || ((!c() && (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.d)) || (f() && (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.e)))) {
            a(uVar, mqttToken);
        } else {
            e.fine(d, "sendNoWait", "208");
            throw l.a(32104);
        }
    }

    public boolean c() {
        boolean z;
        synchronized (this.x) {
            z = this.w == 0;
        }
        return z;
    }

    public boolean d() {
        boolean z;
        synchronized (this.x) {
            z = this.w == 1;
        }
        return z;
    }

    public boolean e() {
        boolean z;
        synchronized (this.x) {
            z = this.w == 3;
        }
        return z;
    }

    public boolean f() {
        boolean z;
        synchronized (this.x) {
            z = this.w == 2;
        }
        return z;
    }

    public boolean g() {
        boolean z;
        synchronized (this.x) {
            z = this.w == 4;
        }
        return z;
    }

    public int h() {
        return this.l;
    }

    public q[] i() {
        return this.m;
    }

    public MqttDeliveryToken[] j() {
        return this.u.b();
    }

    public IMqttAsyncClient k() {
        return this.k;
    }

    public long l() {
        return this.q.a();
    }

    public e m() {
        return this.q;
    }

    public MqttConnectOptions n() {
        return this.r;
    }

    public Properties o() {
        Properties properties = new Properties();
        properties.put("conState", new Integer(this.w));
        properties.put("serverURI", k().getServerURI());
        properties.put("callback", this.p);
        properties.put("stoppingComms", new Boolean(this.v));
        return properties;
    }

    public MqttToken p() {
        try {
            return this.q.d();
        } catch (MqttException e2) {
            a(e2);
            return null;
        } catch (Exception e3) {
            a(e3);
            return null;
        }
    }
}
