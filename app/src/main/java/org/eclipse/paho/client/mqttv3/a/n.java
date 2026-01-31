package org.eclipse.paho.client.mqttv3.a;

import cn.yunzhisheng.asr.JniUscClient;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.paho.client.mqttv3.MqttException;

/* loaded from: classes.dex */
public class n implements q {

    /* renamed from: a, reason: collision with root package name */
    static Class f497a;
    private Class b;
    private String c;
    private Object d;

    public n(String str) {
        this.c = str;
    }

    @Override // org.eclipse.paho.client.mqttv3.a.q
    public void a() throws MqttException, ClassNotFoundException {
        if (!l.a("com.ibm.mqttdirect.modules.local.bindings.localListener")) {
            throw l.a(32103);
        }
        try {
            this.b = Class.forName("com.ibm.mqttdirect.modules.local.bindings.localListener");
            Class cls = this.b;
            Class<?>[] clsArr = new Class[1];
            Class<?> cls2 = f497a;
            if (cls2 == null) {
                try {
                    cls2 = Class.forName("java.lang.String");
                    f497a = cls2;
                } catch (ClassNotFoundException e) {
                    throw new NoClassDefFoundError(e.getMessage());
                }
            }
            clsArr[0] = cls2;
            this.d = cls.getMethod("connect", clsArr).invoke(null, this.c);
        } catch (Exception e2) {
        }
        if (this.d == null) {
            throw l.a(32103);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.a.q
    public InputStream b() {
        try {
            return (InputStream) this.b.getMethod("getClientInputStream", new Class[0]).invoke(this.d, new Object[0]);
        } catch (Exception e) {
            return null;
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.a.q
    public OutputStream c() {
        try {
            return (OutputStream) this.b.getMethod("getClientOutputStream", new Class[0]).invoke(this.d, new Object[0]);
        } catch (Exception e) {
            return null;
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.a.q
    public void d() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (this.d != null) {
            try {
                this.b.getMethod(JniUscClient.s, new Class[0]).invoke(this.d, new Object[0]);
            } catch (Exception e) {
            }
        }
    }
}
