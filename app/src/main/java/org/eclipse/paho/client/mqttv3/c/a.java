package org.eclipse.paho.client.mqttv3.c;

import cn.yunzhisheng.common.PinyinConverter;
import java.util.Enumeration;
import java.util.Properties;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    static Class f507a = null;
    private static final String b;
    private static final Logger c;
    private static final String d = "==============";
    private static final String e;
    private String f;
    private org.eclipse.paho.client.mqttv3.a.a g;

    static {
        Class<?> cls = f507a;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.a.a");
                f507a = cls;
            } catch (ClassNotFoundException e2) {
                throw new NoClassDefFoundError(e2.getMessage());
            }
        }
        b = cls.getName();
        c = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, b);
        e = System.getProperty("line.separator", "\n");
    }

    public a(String str, org.eclipse.paho.client.mqttv3.a.a aVar) {
        this.f = str;
        this.g = aVar;
        c.setResourceName(str);
    }

    public static String a(String str, int i, char c2) {
        if (str.length() >= i) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(i);
        stringBuffer.append(str);
        int length = i - str.length();
        while (true) {
            length--;
            if (length < 0) {
                return stringBuffer.toString();
            }
            stringBuffer.append(c2);
        }
    }

    public static String a(Properties properties, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        Enumeration<?> enumerationPropertyNames = properties.propertyNames();
        stringBuffer.append(new StringBuffer(String.valueOf(e)).append(d).append(PinyinConverter.PINYIN_SEPARATOR).append(str).append(PinyinConverter.PINYIN_SEPARATOR).append(d).append(e).toString());
        while (enumerationPropertyNames.hasMoreElements()) {
            String str2 = (String) enumerationPropertyNames.nextElement();
            stringBuffer.append(new StringBuffer(String.valueOf(a(str2, 28, ' '))).append(":  ").append(properties.get(str2)).append(e).toString());
        }
        stringBuffer.append(new StringBuffer("==========================================").append(e).toString());
        return stringBuffer.toString();
    }

    public void a() {
        g();
        h();
        f();
        b();
    }

    public void b() {
        d();
        e();
        c();
    }

    protected void c() {
        c.dumpTrace();
    }

    protected void d() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(new StringBuffer(String.valueOf(e)).append(d).append(" Version Info ").append(d).append(e).toString());
        stringBuffer.append(new StringBuffer(String.valueOf(a("Version", 20, ' '))).append(":  ").append(org.eclipse.paho.client.mqttv3.a.a.f468a).append(e).toString());
        stringBuffer.append(new StringBuffer(String.valueOf(a("Build Level", 20, ' '))).append(":  ").append(org.eclipse.paho.client.mqttv3.a.a.b).append(e).toString());
        stringBuffer.append(new StringBuffer("==========================================").append(e).toString());
        c.fine(b, "dumpVersion", stringBuffer.toString());
    }

    public void e() {
        c.fine(b, "dumpSystemProperties", a(System.getProperties(), "SystemProperties").toString());
    }

    public void f() {
        if (this.g == null || this.g.m() == null) {
            return;
        }
        c.fine(b, "dumpClientState", a(this.g.m().j(), new StringBuffer(String.valueOf(this.f)).append(" : ClientState").toString()).toString());
    }

    public void g() {
        if (this.g != null) {
            c.fine(b, "dumpClientComms", a(this.g.o(), new StringBuffer(String.valueOf(this.f)).append(" : ClientComms").toString()).toString());
        }
    }

    public void h() {
        if (this.g != null) {
            c.fine(b, "dumpConOptions", a(this.g.n().getDebug(), new StringBuffer(String.valueOf(this.f)).append(" : Connect Options").toString()).toString());
        }
    }
}
