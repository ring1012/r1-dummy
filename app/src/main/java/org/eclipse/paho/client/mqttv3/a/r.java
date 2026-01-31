package org.eclipse.paho.client.mqttv3.a;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* loaded from: classes.dex */
public class r extends o {

    /* renamed from: a, reason: collision with root package name */
    private ResourceBundle f500a = ResourceBundle.getBundle("org.eclipse.paho.client.mqttv3.internal.nls.messages");

    @Override // org.eclipse.paho.client.mqttv3.a.o
    protected String b(int i) {
        try {
            return this.f500a.getString(Integer.toString(i));
        } catch (MissingResourceException e) {
            return "MqttException";
        }
    }
}
