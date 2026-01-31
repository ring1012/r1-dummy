package org.eclipse.paho.client.mqttv3.a;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

/* loaded from: classes.dex */
public class l {
    private l() {
    }

    public static MqttException a(int i) {
        return (i == 4 || i == 5) ? new MqttSecurityException(i) : new MqttException(i);
    }

    public static MqttException a(Throwable th) {
        return th.getClass().getName().equals("java.security.GeneralSecurityException") ? new MqttSecurityException(th) : new MqttException(th);
    }

    public static boolean a(String str) throws ClassNotFoundException {
        try {
            Class.forName(str);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
