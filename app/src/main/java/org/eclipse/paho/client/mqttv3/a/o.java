package org.eclipse.paho.client.mqttv3.a;

/* loaded from: classes.dex */
public abstract class o {

    /* renamed from: a, reason: collision with root package name */
    private static o f498a = null;

    public static final String a(int i) {
        if (f498a == null) {
            if (l.a("java.util.ResourceBundle")) {
                try {
                    f498a = (o) Class.forName("org.eclipse.paho.client.mqttv3.a.r").newInstance();
                } catch (Exception e) {
                    return "";
                }
            } else if (l.a("org.eclipse.paho.client.mqttv3.internal.MIDPCatalog")) {
                try {
                    f498a = (o) Class.forName("org.eclipse.paho.client.mqttv3.internal.MIDPCatalog").newInstance();
                } catch (Exception e2) {
                    return "";
                }
            }
        }
        return f498a.b(i);
    }

    protected abstract String b(int i);
}
