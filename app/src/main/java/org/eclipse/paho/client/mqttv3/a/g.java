package org.eclipse.paho.client.mqttv3.a;

import java.io.InputStream;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class g implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    static Class f492a;
    private static final String b;
    private static final Logger c;
    private e f;
    private a g;
    private org.eclipse.paho.client.mqttv3.a.b.f h;
    private i i;
    private volatile boolean k;
    private boolean d = false;
    private Object e = new Object();
    private Thread j = null;

    static {
        Class<?> cls = f492a;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.a.g");
                f492a = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        b = cls.getName();
        c = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, b);
    }

    public g(a aVar, e eVar, i iVar, InputStream inputStream) {
        this.f = null;
        this.g = null;
        this.i = null;
        this.h = new org.eclipse.paho.client.mqttv3.a.b.f(eVar, inputStream);
        this.g = aVar;
        this.f = eVar;
        this.i = iVar;
        c.setResourceName(aVar.k().getClientId());
    }

    public void a() {
        synchronized (this.e) {
            c.fine(b, "stop", "850");
            if (this.d) {
                this.d = false;
                this.k = false;
                if (!Thread.currentThread().equals(this.j)) {
                    try {
                        this.j.join();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        this.j = null;
        c.fine(b, "stop", "851");
    }

    public void a(String str) {
        c.fine(b, "start", "855");
        synchronized (this.e) {
            if (!this.d) {
                this.d = true;
                this.j = new Thread(this, str);
                this.j.start();
            }
        }
    }

    public boolean b() {
        return this.d;
    }

    public boolean c() {
        return this.k;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x000b, code lost:
    
        org.eclipse.paho.client.mqttv3.a.g.c.fine(org.eclipse.paho.client.mqttv3.a.g.b, "run", "854");
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0016, code lost:
    
        return;
     */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            r8 = this;
            r0 = 0
            r7 = 0
            r1 = r0
        L3:
            boolean r0 = r8.d
            if (r0 == 0) goto Lb
            org.eclipse.paho.client.mqttv3.a.b.f r0 = r8.h
            if (r0 != 0) goto L17
        Lb:
            org.eclipse.paho.client.mqttv3.logging.Logger r0 = org.eclipse.paho.client.mqttv3.a.g.c
            java.lang.String r1 = org.eclipse.paho.client.mqttv3.a.g.b
            java.lang.String r2 = "run"
            java.lang.String r3 = "854"
            r0.fine(r1, r2, r3)
            return
        L17:
            org.eclipse.paho.client.mqttv3.logging.Logger r0 = org.eclipse.paho.client.mqttv3.a.g.c     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            java.lang.String r2 = org.eclipse.paho.client.mqttv3.a.g.b     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            java.lang.String r3 = "run"
            java.lang.String r4 = "852"
            r0.fine(r2, r3, r4)     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            org.eclipse.paho.client.mqttv3.a.b.f r0 = r8.h     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            int r0 = r0.available()     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            if (r0 <= 0) goto L50
            r0 = 1
        L2b:
            r8.k = r0     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            org.eclipse.paho.client.mqttv3.a.b.f r0 = r8.h     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            org.eclipse.paho.client.mqttv3.a.b.u r0 = r0.a()     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            r2 = 0
            r8.k = r2     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            boolean r2 = r0 instanceof org.eclipse.paho.client.mqttv3.a.b.b     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            if (r2 == 0) goto L99
            org.eclipse.paho.client.mqttv3.a.i r2 = r8.i     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            org.eclipse.paho.client.mqttv3.MqttToken r1 = r2.a(r0)     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            if (r1 == 0) goto L6d
            monitor-enter(r1)     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            org.eclipse.paho.client.mqttv3.a.e r2 = r8.f     // Catch: java.lang.Throwable -> L52
            org.eclipse.paho.client.mqttv3.a.b.b r0 = (org.eclipse.paho.client.mqttv3.a.b.b) r0     // Catch: java.lang.Throwable -> L52
            r2.a(r0)     // Catch: java.lang.Throwable -> L52
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L52
            r0 = r1
        L4c:
            r8.k = r7
            r1 = r0
            goto L3
        L50:
            r0 = r7
            goto L2b
        L52:
            r0 = move-exception
            monitor-exit(r1)     // Catch: java.lang.Throwable -> L52
            throw r0     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
        L55:
            r5 = move-exception
            r6 = r1
            org.eclipse.paho.client.mqttv3.logging.Logger r0 = org.eclipse.paho.client.mqttv3.a.g.c     // Catch: java.lang.Throwable -> La0
            java.lang.String r1 = org.eclipse.paho.client.mqttv3.a.g.b     // Catch: java.lang.Throwable -> La0
            java.lang.String r2 = "run"
            java.lang.String r3 = "856"
            r4 = 0
            r0.fine(r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> La0
            r0 = 0
            r8.d = r0     // Catch: java.lang.Throwable -> La0
            org.eclipse.paho.client.mqttv3.a.a r0 = r8.g     // Catch: java.lang.Throwable -> La0
            r0.a(r6, r5)     // Catch: java.lang.Throwable -> La0
            r0 = r6
            goto L4c
        L6d:
            org.eclipse.paho.client.mqttv3.MqttException r0 = new org.eclipse.paho.client.mqttv3.MqttException     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            r2 = 6
            r0.<init>(r2)     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            throw r0     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
        L74:
            r0 = move-exception
            org.eclipse.paho.client.mqttv3.logging.Logger r2 = org.eclipse.paho.client.mqttv3.a.g.c     // Catch: java.lang.Throwable -> La0
            java.lang.String r3 = org.eclipse.paho.client.mqttv3.a.g.b     // Catch: java.lang.Throwable -> La0
            java.lang.String r4 = "run"
            java.lang.String r5 = "853"
            r2.fine(r3, r4, r5)     // Catch: java.lang.Throwable -> La0
            r2 = 0
            r8.d = r2     // Catch: java.lang.Throwable -> La0
            org.eclipse.paho.client.mqttv3.a.a r2 = r8.g     // Catch: java.lang.Throwable -> La0
            boolean r2 = r2.f()     // Catch: java.lang.Throwable -> La0
            if (r2 != 0) goto La4
            org.eclipse.paho.client.mqttv3.a.a r2 = r8.g     // Catch: java.lang.Throwable -> La0
            org.eclipse.paho.client.mqttv3.MqttException r3 = new org.eclipse.paho.client.mqttv3.MqttException     // Catch: java.lang.Throwable -> La0
            r4 = 32109(0x7d6d, float:4.4994E-41)
            r3.<init>(r4, r0)     // Catch: java.lang.Throwable -> La0
            r2.a(r1, r3)     // Catch: java.lang.Throwable -> La0
            r0 = r1
            goto L4c
        L99:
            org.eclipse.paho.client.mqttv3.a.e r2 = r8.f     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            r2.b(r0)     // Catch: org.eclipse.paho.client.mqttv3.MqttException -> L55 java.io.IOException -> L74 java.lang.Throwable -> La0
            r0 = r1
            goto L4c
        La0:
            r0 = move-exception
            r8.k = r7
            throw r0
        La4:
            r0 = r1
            goto L4c
        */
        throw new UnsupportedOperationException("Method not decompiled: org.eclipse.paho.client.mqttv3.a.g.run():void");
    }
}
