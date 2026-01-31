package org.eclipse.paho.client.mqttv3.a;

import java.io.EOFException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistable;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class e {

    /* renamed from: a, reason: collision with root package name */
    static Class f490a = null;
    private static final String b;
    private static final Logger c;
    private static final String d = "s-";
    private static final String e = "sc-";
    private static final String f = "r-";
    private static final int g = 10;
    private static final int h = 1;
    private static final int i = 65535;
    private org.eclipse.paho.client.mqttv3.a.b.u C;
    private Hashtable G;
    private Hashtable H;
    private Hashtable I;
    private MqttPingSender J;
    private Hashtable k;
    private volatile Vector l;
    private volatile Vector m;
    private i n;
    private a o;
    private f p;
    private long q;
    private boolean r;
    private MqttClientPersistence s;
    private int u;
    private int v;
    private int j = 0;
    private int t = 10;
    private Object w = new Object();
    private Object x = new Object();
    private boolean y = false;
    private long z = 0;
    private long A = 0;
    private long B = 0;
    private Object D = new Object();
    private int E = 0;
    private boolean F = false;

    static {
        Class<?> cls = f490a;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.a.e");
                f490a = cls;
            } catch (ClassNotFoundException e2) {
                throw new NoClassDefFoundError(e2.getMessage());
            }
        }
        b = cls.getName();
        c = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, b);
    }

    protected e(MqttClientPersistence mqttClientPersistence, i iVar, f fVar, a aVar, MqttPingSender mqttPingSender) throws MqttException {
        this.o = null;
        this.p = null;
        this.u = 0;
        this.v = 0;
        this.G = null;
        this.H = null;
        this.I = null;
        this.J = null;
        c.setResourceName(aVar.k().getClientId());
        c.finer(b, "<Init>", "");
        this.k = new Hashtable();
        this.l = new Vector(this.t);
        this.m = new Vector();
        this.G = new Hashtable();
        this.H = new Hashtable();
        this.I = new Hashtable();
        this.C = new org.eclipse.paho.client.mqttv3.a.b.i();
        this.v = 0;
        this.u = 0;
        this.s = mqttClientPersistence;
        this.p = fVar;
        this.n = iVar;
        this.o = aVar;
        this.J = mqttPingSender;
        c();
    }

    private Vector a(Vector vector) {
        Vector vector2 = new Vector();
        if (vector.size() == 0) {
            return vector2;
        }
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i2 < vector.size()) {
            int i6 = ((org.eclipse.paho.client.mqttv3.a.b.u) vector.elementAt(i2)).i();
            if (i6 - i5 > i4) {
                i4 = i6 - i5;
                i3 = i2;
            }
            i2++;
            i5 = i6;
        }
        if (((org.eclipse.paho.client.mqttv3.a.b.u) vector.elementAt(0)).i() + (65535 - i5) > i4) {
            i3 = 0;
        }
        for (int i7 = i3; i7 < vector.size(); i7++) {
            vector2.addElement(vector.elementAt(i7));
        }
        for (int i8 = 0; i8 < i3; i8++) {
            vector2.addElement(vector.elementAt(i8));
        }
        return vector2;
    }

    private org.eclipse.paho.client.mqttv3.a.b.u a(String str, MqttPersistable mqttPersistable) throws MqttException {
        org.eclipse.paho.client.mqttv3.a.b.u uVarA;
        try {
            uVarA = org.eclipse.paho.client.mqttv3.a.b.u.a(mqttPersistable);
        } catch (MqttException e2) {
            c.fine(b, "restoreMessage", "602", new Object[]{str}, e2);
            if (!(e2.getCause() instanceof EOFException)) {
                throw e2;
            }
            if (str != null) {
                this.s.remove(str);
                uVarA = null;
            } else {
                uVarA = null;
            }
        }
        c.fine(b, "restoreMessage", "601", new Object[]{str, uVarA});
        return uVarA;
    }

    private void a(Vector vector, org.eclipse.paho.client.mqttv3.a.b.u uVar) {
        int i2 = uVar.i();
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 >= vector.size()) {
                vector.addElement(uVar);
                return;
            } else {
                if (((org.eclipse.paho.client.mqttv3.a.b.u) vector.elementAt(i4)).i() > i2) {
                    vector.insertElementAt(uVar, i4);
                    return;
                }
                i3 = i4 + 1;
            }
        }
    }

    private String c(org.eclipse.paho.client.mqttv3.a.b.u uVar) {
        return new StringBuffer(d).append(uVar.i()).toString();
    }

    private synchronized void c(int i2) {
        this.k.remove(new Integer(i2));
    }

    private String d(org.eclipse.paho.client.mqttv3.a.b.u uVar) {
        return new StringBuffer(e).append(uVar.i()).toString();
    }

    private String e(org.eclipse.paho.client.mqttv3.a.b.u uVar) {
        return new StringBuffer(f).append(uVar.i()).toString();
    }

    private void k() {
        this.l = new Vector(this.t);
        this.m = new Vector();
        Enumeration enumerationKeys = this.G.keys();
        while (enumerationKeys.hasMoreElements()) {
            Object objNextElement = enumerationKeys.nextElement();
            org.eclipse.paho.client.mqttv3.a.b.u uVar = (org.eclipse.paho.client.mqttv3.a.b.u) this.G.get(objNextElement);
            if (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.o) {
                c.fine(b, "restoreInflightMessages", "610", new Object[]{objNextElement});
                uVar.a(true);
                a(this.l, (org.eclipse.paho.client.mqttv3.a.b.o) uVar);
            } else if (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.n) {
                c.fine(b, "restoreInflightMessages", "611", new Object[]{objNextElement});
                a(this.m, (org.eclipse.paho.client.mqttv3.a.b.n) uVar);
            }
        }
        Enumeration enumerationKeys2 = this.H.keys();
        while (enumerationKeys2.hasMoreElements()) {
            Object objNextElement2 = enumerationKeys2.nextElement();
            org.eclipse.paho.client.mqttv3.a.b.o oVar = (org.eclipse.paho.client.mqttv3.a.b.o) this.H.get(objNextElement2);
            oVar.a(true);
            c.fine(b, "restoreInflightMessages", "612", new Object[]{objNextElement2});
            a(this.l, oVar);
        }
        this.m = a(this.m);
        this.l = a(this.l);
    }

    private void l() {
        synchronized (this.w) {
            this.u--;
            c.fine(b, "decrementInFlight", "646", new Object[]{new Integer(this.u)});
            if (!f()) {
                this.w.notifyAll();
            }
        }
    }

    private synchronized int m() {
        int i2 = this.j;
        int i3 = 0;
        do {
            this.j++;
            if (this.j > 65535) {
                this.j = 1;
            }
            if (this.j == i2 && (i3 = i3 + 1) == 2) {
                throw l.a(32001);
            }
        } while (this.k.containsKey(new Integer(this.j)));
        Integer num = new Integer(this.j);
        this.k.put(num, num);
        return this.j;
    }

    protected long a() {
        return this.q;
    }

    public Vector a(MqttException mqttException) {
        c.fine(b, "resolveOldTokens", "632", new Object[]{mqttException});
        if (mqttException == null) {
            mqttException = new MqttException(32102);
        }
        Vector vectorC = this.n.c();
        Enumeration enumerationElements = vectorC.elements();
        while (enumerationElements.hasMoreElements()) {
            MqttToken mqttToken = (MqttToken) enumerationElements.nextElement();
            synchronized (mqttToken) {
                if (!mqttToken.isComplete() && !mqttToken.internalTok.e() && mqttToken.getException() == null) {
                    mqttToken.internalTok.a(mqttException);
                }
            }
            if (!(mqttToken instanceof MqttDeliveryToken)) {
                this.n.b(mqttToken.internalTok.s());
            }
        }
        return vectorC;
    }

    public void a(int i2) {
        if (i2 > 0) {
            this.z = System.currentTimeMillis();
        }
        c.fine(b, "notifySentBytes", "631", new Object[]{new Integer(i2)});
    }

    protected void a(long j) {
        this.q = 1000 * j;
    }

    protected void a(MqttToken mqttToken) {
        org.eclipse.paho.client.mqttv3.a.b.u uVarP = mqttToken.internalTok.p();
        if (uVarP == null || !(uVarP instanceof org.eclipse.paho.client.mqttv3.a.b.b)) {
            return;
        }
        c.fine(b, "notifyComplete", "629", new Object[]{new Integer(uVarP.i()), mqttToken, uVarP});
        org.eclipse.paho.client.mqttv3.a.b.b bVar = (org.eclipse.paho.client.mqttv3.a.b.b) uVarP;
        if (bVar instanceof org.eclipse.paho.client.mqttv3.a.b.k) {
            this.s.remove(c(uVarP));
            this.H.remove(new Integer(bVar.i()));
            l();
            c(uVarP.i());
            this.n.b(uVarP);
            c.fine(b, "notifyComplete", "650", new Object[]{new Integer(bVar.i())});
        } else if (bVar instanceof org.eclipse.paho.client.mqttv3.a.b.l) {
            this.s.remove(c(uVarP));
            this.s.remove(d(uVarP));
            this.G.remove(new Integer(bVar.i()));
            this.v--;
            l();
            c(uVarP.i());
            this.n.b(uVarP);
            c.fine(b, "notifyComplete", "645", new Object[]{new Integer(bVar.i()), new Integer(this.v)});
        }
        f();
    }

    protected void a(org.eclipse.paho.client.mqttv3.a.b.b bVar) throws MqttException {
        this.A = System.currentTimeMillis();
        c.fine(b, "notifyReceivedAck", "627", new Object[]{new Integer(bVar.i()), bVar});
        MqttToken mqttTokenA = this.n.a(bVar);
        if (bVar instanceof org.eclipse.paho.client.mqttv3.a.b.m) {
            a(new org.eclipse.paho.client.mqttv3.a.b.n((org.eclipse.paho.client.mqttv3.a.b.m) bVar), mqttTokenA);
        } else if ((bVar instanceof org.eclipse.paho.client.mqttv3.a.b.k) || (bVar instanceof org.eclipse.paho.client.mqttv3.a.b.l)) {
            a(bVar, mqttTokenA, null);
        } else if (bVar instanceof org.eclipse.paho.client.mqttv3.a.b.j) {
            synchronized (this.D) {
                this.E = Math.max(0, this.E - 1);
                a(bVar, mqttTokenA, null);
                if (this.E == 0) {
                    this.n.b(bVar);
                }
            }
            c.fine(b, "notifyReceivedAck", "636", new Object[]{new Integer(this.E)});
        } else if (bVar instanceof org.eclipse.paho.client.mqttv3.a.b.c) {
            int iB = ((org.eclipse.paho.client.mqttv3.a.b.c) bVar).b();
            if (iB != 0) {
                throw l.a(iB);
            }
            synchronized (this.w) {
                if (this.r) {
                    b();
                    this.n.a(mqttTokenA, bVar);
                }
                this.v = 0;
                this.u = 0;
                k();
                g();
            }
            this.o.a((org.eclipse.paho.client.mqttv3.a.b.c) bVar, (MqttException) null);
            a(bVar, mqttTokenA, null);
            this.n.b(bVar);
            synchronized (this.w) {
                this.w.notifyAll();
            }
        } else {
            a(bVar, mqttTokenA, null);
            c(bVar.i());
            this.n.b(bVar);
        }
        f();
    }

    protected void a(org.eclipse.paho.client.mqttv3.a.b.o oVar) {
        synchronized (this.w) {
            c.fine(b, "undo", "618", new Object[]{new Integer(oVar.i()), new Integer(oVar.g().getQos())});
            if (oVar.g().getQos() == 1) {
                this.H.remove(new Integer(oVar.i()));
            } else {
                this.G.remove(new Integer(oVar.i()));
            }
            this.l.removeElement(oVar);
            this.s.remove(c(oVar));
            this.n.b(oVar);
            f();
        }
    }

    protected void a(org.eclipse.paho.client.mqttv3.a.b.u uVar) {
        this.z = System.currentTimeMillis();
        c.fine(b, "notifySent", "625", new Object[]{uVar.e()});
        MqttToken mqttTokenA = this.n.a(uVar);
        mqttTokenA.internalTok.l();
        if (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.i) {
            synchronized (this.D) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                synchronized (this.D) {
                    this.B = jCurrentTimeMillis;
                    this.E++;
                }
                c.fine(b, "notifySent", "635", new Object[]{new Integer(this.E)});
            }
            return;
        }
        if ((uVar instanceof org.eclipse.paho.client.mqttv3.a.b.o) && ((org.eclipse.paho.client.mqttv3.a.b.o) uVar).g().getQos() == 0) {
            mqttTokenA.internalTok.a(null, null);
            this.p.b(mqttTokenA);
            l();
            c(uVar.i());
            this.n.b(uVar);
            f();
        }
    }

    public void a(org.eclipse.paho.client.mqttv3.a.b.u uVar, MqttToken mqttToken) {
        if (uVar.d() && uVar.i() == 0) {
            uVar.a(m());
        }
        if (mqttToken != null) {
            try {
                mqttToken.internalTok.a(uVar.i());
            } catch (Exception e2) {
            }
        }
        if (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.o) {
            synchronized (this.w) {
                if (this.u >= this.t) {
                    c.fine(b, "send", "613", new Object[]{new Integer(this.u)});
                    throw new MqttException(32202);
                }
                MqttMessage mqttMessageG = ((org.eclipse.paho.client.mqttv3.a.b.o) uVar).g();
                c.fine(b, "send", "628", new Object[]{new Integer(uVar.i()), new Integer(mqttMessageG.getQos()), uVar});
                switch (mqttMessageG.getQos()) {
                    case 1:
                        this.H.put(new Integer(uVar.i()), uVar);
                        this.s.put(c(uVar), (org.eclipse.paho.client.mqttv3.a.b.o) uVar);
                        break;
                    case 2:
                        this.G.put(new Integer(uVar.i()), uVar);
                        this.s.put(c(uVar), (org.eclipse.paho.client.mqttv3.a.b.o) uVar);
                        break;
                }
                this.n.a(mqttToken, uVar);
                this.l.addElement(uVar);
                this.w.notifyAll();
            }
            return;
        }
        c.fine(b, "send", "615", new Object[]{new Integer(uVar.i()), uVar});
        if (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.d) {
            synchronized (this.w) {
                this.n.a(mqttToken, uVar);
                this.m.insertElementAt(uVar, 0);
                this.w.notifyAll();
            }
            return;
        }
        if (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.i) {
            this.C = uVar;
        } else if (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.n) {
            this.G.put(new Integer(uVar.i()), uVar);
            this.s.put(d(uVar), (org.eclipse.paho.client.mqttv3.a.b.n) uVar);
        } else if (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.l) {
            this.s.remove(e(uVar));
        }
        synchronized (this.w) {
            if (!(uVar instanceof org.eclipse.paho.client.mqttv3.a.b.b)) {
                this.n.a(mqttToken, uVar);
            }
            this.m.addElement(uVar);
            this.w.notifyAll();
        }
    }

    protected void a(org.eclipse.paho.client.mqttv3.a.b.u uVar, MqttToken mqttToken, MqttException mqttException) {
        mqttToken.internalTok.a(uVar, mqttException);
        if (uVar != null && (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.b) && !(uVar instanceof org.eclipse.paho.client.mqttv3.a.b.m)) {
            c.fine(b, "notifyResult", "648", new Object[]{mqttToken.internalTok.s(), uVar, mqttException});
            this.p.b(mqttToken);
        }
        if (uVar == null) {
            c.fine(b, "notifyResult", "649", new Object[]{mqttToken.internalTok.s(), mqttException});
            this.p.b(mqttToken);
        }
    }

    protected void a(boolean z) {
        this.r = z;
    }

    protected void b() {
        c.fine(b, "clearState", ">");
        this.s.clear();
        this.k.clear();
        this.l.clear();
        this.m.clear();
        this.G.clear();
        this.H.clear();
        this.I.clear();
        this.n.d();
    }

    public void b(int i2) {
        if (i2 > 0) {
            this.A = System.currentTimeMillis();
        }
        c.fine(b, "notifyReceivedBytes", "630", new Object[]{new Integer(i2)});
    }

    public void b(long j) {
        this.q = j;
    }

    public void b(MqttException mqttException) {
        c.fine(b, "disconnected", "633", new Object[]{mqttException});
        this.F = false;
        try {
            if (this.r) {
                b();
            }
            this.l.clear();
            this.m.clear();
            synchronized (this.D) {
                this.E = 0;
            }
        } catch (MqttException e2) {
        }
    }

    protected void b(org.eclipse.paho.client.mqttv3.a.b.o oVar) {
        c.fine(b, "deliveryComplete", "641", new Object[]{new Integer(oVar.i())});
        this.s.remove(e(oVar));
        this.I.remove(new Integer(oVar.i()));
    }

    protected void b(org.eclipse.paho.client.mqttv3.a.b.u uVar) {
        this.A = System.currentTimeMillis();
        c.fine(b, "notifyReceivedMsg", "651", new Object[]{new Integer(uVar.i()), uVar});
        if (this.y) {
        }
        if (!(uVar instanceof org.eclipse.paho.client.mqttv3.a.b.o)) {
            if (uVar instanceof org.eclipse.paho.client.mqttv3.a.b.n) {
                org.eclipse.paho.client.mqttv3.a.b.o oVar = (org.eclipse.paho.client.mqttv3.a.b.o) this.I.get(new Integer(uVar.i()));
                if (oVar == null) {
                    a(new org.eclipse.paho.client.mqttv3.a.b.l(uVar.i()), (MqttToken) null);
                    return;
                } else {
                    if (this.p != null) {
                        this.p.a(oVar);
                        return;
                    }
                    return;
                }
            }
            return;
        }
        org.eclipse.paho.client.mqttv3.a.b.o oVar2 = (org.eclipse.paho.client.mqttv3.a.b.o) uVar;
        switch (oVar2.g().getQos()) {
            case 0:
            case 1:
                if (this.p != null) {
                    this.p.a(oVar2);
                    break;
                }
                break;
            case 2:
                this.s.put(e(uVar), (org.eclipse.paho.client.mqttv3.a.b.o) uVar);
                this.I.put(new Integer(oVar2.i()), oVar2);
                a(new org.eclipse.paho.client.mqttv3.a.b.m(oVar2), (MqttToken) null);
                break;
        }
    }

    protected void c() throws MqttException {
        Enumeration enumerationKeys = this.s.keys();
        int i2 = this.j;
        Vector vector = new Vector();
        c.fine(b, "restoreState", "600");
        int i3 = i2;
        while (enumerationKeys.hasMoreElements()) {
            String str = (String) enumerationKeys.nextElement();
            org.eclipse.paho.client.mqttv3.a.b.u uVarA = a(str, this.s.get(str));
            if (uVarA != null) {
                if (str.startsWith(f)) {
                    c.fine(b, "restoreState", "604", new Object[]{str, uVarA});
                    this.I.put(new Integer(uVarA.i()), uVarA);
                } else if (str.startsWith(d)) {
                    org.eclipse.paho.client.mqttv3.a.b.o oVar = (org.eclipse.paho.client.mqttv3.a.b.o) uVarA;
                    int iMax = Math.max(oVar.i(), i3);
                    if (this.s.containsKey(d(oVar))) {
                        org.eclipse.paho.client.mqttv3.a.b.n nVar = (org.eclipse.paho.client.mqttv3.a.b.n) a(str, this.s.get(d(oVar)));
                        if (nVar != null) {
                            c.fine(b, "restoreState", "605", new Object[]{str, uVarA});
                            this.G.put(new Integer(nVar.i()), nVar);
                        } else {
                            c.fine(b, "restoreState", "606", new Object[]{str, uVarA});
                        }
                    } else {
                        oVar.a(true);
                        if (oVar.g().getQos() == 2) {
                            c.fine(b, "restoreState", "607", new Object[]{str, uVarA});
                            this.G.put(new Integer(oVar.i()), oVar);
                        } else {
                            c.fine(b, "restoreState", "608", new Object[]{str, uVarA});
                            this.H.put(new Integer(oVar.i()), oVar);
                        }
                    }
                    this.n.a(oVar).internalTok.a(this.o.k());
                    this.k.put(new Integer(oVar.i()), new Integer(oVar.i()));
                    i3 = iMax;
                } else if (str.startsWith(e) && !this.s.containsKey(c((org.eclipse.paho.client.mqttv3.a.b.n) uVarA))) {
                    vector.addElement(str);
                }
            }
        }
        Enumeration enumerationElements = vector.elements();
        while (enumerationElements.hasMoreElements()) {
            String str2 = (String) enumerationElements.nextElement();
            c.fine(b, "restoreState", "609", new Object[]{str2});
            this.s.remove(str2);
        }
        this.j = i3;
    }

    public void c(long j) {
        if (j > 0) {
            c.fine(b, "quiesce", "637", new Object[]{new Long(j)});
            synchronized (this.w) {
                this.y = true;
            }
            this.p.b();
            h();
            synchronized (this.x) {
                try {
                    int iE = this.n.e();
                    if (iE > 0 || this.m.size() > 0 || !this.p.c()) {
                        c.fine(b, "quiesce", "639", new Object[]{new Integer(this.u), new Integer(this.m.size()), new Integer(this.v), new Integer(iE)});
                        this.x.wait(j);
                    }
                } catch (InterruptedException e2) {
                }
            }
            synchronized (this.w) {
                this.l.clear();
                this.m.clear();
                this.y = false;
                this.u = 0;
            }
            c.fine(b, "quiesce", "640");
        }
    }

    public MqttToken d() {
        long jMax;
        c.fine(b, "checkForActivity", "616", new Object[0]);
        synchronized (this.x) {
            if (this.y) {
                return null;
            }
            MqttToken mqttToken = null;
            a();
            if (!this.F || this.q <= 0) {
                return null;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            synchronized (this.D) {
                if (this.E > 0 && jCurrentTimeMillis - this.A >= this.q + 100) {
                    c.severe(b, "checkForActivity", "619", new Object[]{new Long(this.q), new Long(this.z), new Long(this.A), new Long(jCurrentTimeMillis), new Long(this.B)});
                    throw l.a(32000);
                }
                if (this.E == 0 && jCurrentTimeMillis - this.z >= 2 * this.q) {
                    c.severe(b, "checkForActivity", "642", new Object[]{new Long(this.q), new Long(this.z), new Long(this.A), new Long(jCurrentTimeMillis), new Long(this.B)});
                    throw l.a(32002);
                }
                if ((this.E != 0 || jCurrentTimeMillis - this.A < this.q - 100) && jCurrentTimeMillis - this.z < this.q - 100) {
                    c.fine(b, "checkForActivity", "634", null);
                    jMax = Math.max(1L, a() - (jCurrentTimeMillis - this.z));
                } else {
                    c.fine(b, "checkForActivity", "620", new Object[]{new Long(this.q), new Long(this.z), new Long(this.A)});
                    MqttToken mqttToken2 = new MqttToken(this.o.k().getClientId());
                    this.n.a(mqttToken2, this.C);
                    this.m.insertElementAt(this.C, 0);
                    long jA = a();
                    h();
                    mqttToken = mqttToken2;
                    jMax = jA;
                }
            }
            c.fine(b, "checkForActivity", "624", new Object[]{new Long(jMax)});
            this.J.schedule(jMax);
            return mqttToken;
        }
    }

    protected org.eclipse.paho.client.mqttv3.a.b.u e() {
        synchronized (this.w) {
            org.eclipse.paho.client.mqttv3.a.b.u uVar = null;
            while (uVar == null) {
                if ((this.l.isEmpty() && this.m.isEmpty()) || (this.m.isEmpty() && this.u >= this.t)) {
                    try {
                        c.fine(b, "get", "644");
                        this.w.wait();
                        c.fine(b, "get", "647");
                    } catch (InterruptedException e2) {
                    }
                }
                if (!this.F && (this.m.isEmpty() || !(((org.eclipse.paho.client.mqttv3.a.b.u) this.m.elementAt(0)) instanceof org.eclipse.paho.client.mqttv3.a.b.d))) {
                    c.fine(b, "get", "621");
                    return null;
                }
                if (!this.m.isEmpty()) {
                    org.eclipse.paho.client.mqttv3.a.b.u uVar2 = (org.eclipse.paho.client.mqttv3.a.b.u) this.m.remove(0);
                    if (uVar2 instanceof org.eclipse.paho.client.mqttv3.a.b.n) {
                        this.v++;
                        c.fine(b, "get", "617", new Object[]{new Integer(this.v)});
                    }
                    f();
                    uVar = uVar2;
                } else if (!this.l.isEmpty()) {
                    if (this.u < this.t) {
                        org.eclipse.paho.client.mqttv3.a.b.u uVar3 = (org.eclipse.paho.client.mqttv3.a.b.u) this.l.elementAt(0);
                        this.l.removeElementAt(0);
                        this.u++;
                        c.fine(b, "get", "623", new Object[]{new Integer(this.u)});
                        uVar = uVar3;
                    } else {
                        c.fine(b, "get", "622");
                    }
                }
            }
            return uVar;
        }
    }

    protected boolean f() {
        int iE = this.n.e();
        if (!this.y || iE != 0 || this.m.size() != 0 || !this.p.c()) {
            return false;
        }
        c.fine(b, "checkQuiesceLock", "626", new Object[]{new Boolean(this.y), new Integer(this.u), new Integer(this.m.size()), new Integer(this.v), Boolean.valueOf(this.p.c()), new Integer(iE)});
        synchronized (this.x) {
            this.x.notifyAll();
        }
        return true;
    }

    public void g() {
        c.fine(b, "connected", "631");
        this.F = true;
        this.J.start();
    }

    public void h() {
        synchronized (this.w) {
            c.fine(b, "notifyQueueLock", "638");
            this.w.notifyAll();
        }
    }

    protected void i() {
        this.k.clear();
        this.l.clear();
        this.m.clear();
        this.G.clear();
        this.H.clear();
        this.I.clear();
        this.n.d();
        this.k = null;
        this.l = null;
        this.m = null;
        this.G = null;
        this.H = null;
        this.I = null;
        this.n = null;
        this.p = null;
        this.o = null;
        this.s = null;
        this.C = null;
    }

    public Properties j() {
        Properties properties = new Properties();
        properties.put("In use msgids", this.k);
        properties.put("pendingMessages", this.l);
        properties.put("pendingFlows", this.m);
        properties.put("maxInflight", new Integer(this.t));
        properties.put("nextMsgID", new Integer(this.j));
        properties.put("actualInFlight", new Integer(this.u));
        properties.put("inFlightPubRels", new Integer(this.v));
        properties.put("quiescing", Boolean.valueOf(this.y));
        properties.put("pingoutstanding", new Integer(this.E));
        properties.put("lastOutboundActivity", new Long(this.z));
        properties.put("lastInboundActivity", new Long(this.A));
        properties.put("outboundQoS2", this.G);
        properties.put("outboundQoS1", this.H);
        properties.put("inboundQoS2", this.I);
        properties.put("tokens", this.n);
        return properties;
    }
}
