package org.eclipse.paho.client.mqttv3.a;

import cn.yunzhisheng.asr.JniUscClient;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttToken;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class i {

    /* renamed from: a, reason: collision with root package name */
    static Class f494a;
    private static final String b;
    private static final Logger c;
    private Hashtable d;
    private String e;
    private MqttException f = null;

    static {
        Class<?> cls = f494a;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.a.i");
                f494a = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        b = cls.getName();
        c = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, b);
    }

    public i(String str) {
        c.setResourceName(str);
        this.d = new Hashtable();
        this.e = str;
        c.fine(b, "<Init>", "308");
    }

    protected MqttDeliveryToken a(org.eclipse.paho.client.mqttv3.a.b.o oVar) {
        MqttDeliveryToken mqttDeliveryToken;
        synchronized (this.d) {
            String string = new Integer(oVar.i()).toString();
            if (this.d.containsKey(string)) {
                mqttDeliveryToken = (MqttDeliveryToken) this.d.get(string);
                c.fine(b, "restoreToken", "302", new Object[]{string, oVar, mqttDeliveryToken});
            } else {
                mqttDeliveryToken = new MqttDeliveryToken(this.e);
                mqttDeliveryToken.internalTok.a(string);
                this.d.put(string, mqttDeliveryToken);
                c.fine(b, "restoreToken", "303", new Object[]{string, oVar, mqttDeliveryToken});
            }
        }
        return mqttDeliveryToken;
    }

    public MqttToken a(String str) {
        return (MqttToken) this.d.get(str);
    }

    public MqttToken a(org.eclipse.paho.client.mqttv3.a.b.u uVar) {
        return (MqttToken) this.d.get(uVar.e());
    }

    public void a() {
        synchronized (this.d) {
            c.fine(b, JniUscClient.r, "310");
            this.f = null;
        }
    }

    protected void a(MqttException mqttException) {
        synchronized (this.d) {
            c.fine(b, "quiesce", "309", new Object[]{mqttException});
            this.f = mqttException;
        }
    }

    protected void a(MqttToken mqttToken, String str) {
        synchronized (this.d) {
            c.fine(b, "saveToken", "307", new Object[]{str, mqttToken.toString()});
            mqttToken.internalTok.a(str);
            this.d.put(str, mqttToken);
        }
    }

    protected void a(MqttToken mqttToken, org.eclipse.paho.client.mqttv3.a.b.u uVar) {
        synchronized (this.d) {
            if (this.f != null) {
                throw this.f;
            }
            String strE = uVar.e();
            c.fine(b, "saveToken", "300", new Object[]{strE, uVar});
            a(mqttToken, strE);
        }
    }

    public MqttToken b(String str) {
        c.fine(b, "removeToken", "306", new Object[]{str});
        if (str != null) {
            return (MqttToken) this.d.remove(str);
        }
        return null;
    }

    public MqttToken b(org.eclipse.paho.client.mqttv3.a.b.u uVar) {
        if (uVar != null) {
            return b(uVar.e());
        }
        return null;
    }

    public MqttDeliveryToken[] b() {
        MqttDeliveryToken[] mqttDeliveryTokenArr;
        synchronized (this.d) {
            c.fine(b, "getOutstandingDelTokens", "311");
            Vector vector = new Vector();
            Enumeration enumerationElements = this.d.elements();
            while (enumerationElements.hasMoreElements()) {
                MqttToken mqttToken = (MqttToken) enumerationElements.nextElement();
                if (mqttToken != null && (mqttToken instanceof MqttDeliveryToken) && !mqttToken.internalTok.t()) {
                    vector.addElement(mqttToken);
                }
            }
            mqttDeliveryTokenArr = (MqttDeliveryToken[]) vector.toArray(new MqttDeliveryToken[vector.size()]);
        }
        return mqttDeliveryTokenArr;
    }

    public Vector c() {
        Vector vector;
        synchronized (this.d) {
            c.fine(b, "getOutstandingTokens", "312");
            vector = new Vector();
            Enumeration enumerationElements = this.d.elements();
            while (enumerationElements.hasMoreElements()) {
                MqttToken mqttToken = (MqttToken) enumerationElements.nextElement();
                if (mqttToken != null) {
                    vector.addElement(mqttToken);
                }
            }
        }
        return vector;
    }

    public void d() {
        c.fine(b, "clear", "305", new Object[]{new Integer(this.d.size())});
        synchronized (this.d) {
            this.d.clear();
        }
    }

    public int e() {
        int size;
        synchronized (this.d) {
            size = this.d.size();
        }
        return size;
    }

    public String toString() {
        String string;
        String property = System.getProperty("line.separator", "\n");
        StringBuffer stringBuffer = new StringBuffer();
        synchronized (this.d) {
            Enumeration enumerationElements = this.d.elements();
            while (enumerationElements.hasMoreElements()) {
                stringBuffer.append(new StringBuffer("{").append(((MqttToken) enumerationElements.nextElement()).internalTok).append("}").append(property).toString());
            }
            string = stringBuffer.toString();
        }
        return string;
    }
}
