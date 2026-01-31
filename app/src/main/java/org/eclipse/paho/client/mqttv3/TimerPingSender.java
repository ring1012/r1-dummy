package org.eclipse.paho.client.mqttv3;

import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.paho.client.mqttv3.a.a;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

/* loaded from: classes.dex */
public class TimerPingSender implements MqttPingSender {

    /* renamed from: a, reason: collision with root package name */
    static Class f466a;
    private static final String b;
    private static final Logger c;
    private a d;
    private Timer e;

    class PingTask extends TimerTask {
        private static final String b = "PingTask.run";

        /* renamed from: a, reason: collision with root package name */
        final TimerPingSender f467a;

        private PingTask(TimerPingSender timerPingSender) {
            this.f467a = timerPingSender;
        }

        PingTask(TimerPingSender timerPingSender, PingTask pingTask) {
            this(timerPingSender);
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            TimerPingSender.a().fine(TimerPingSender.b(), b, "660", new Object[]{new Long(System.currentTimeMillis())});
            TimerPingSender.a(this.f467a).p();
        }
    }

    static {
        Class<?> cls = f466a;
        if (cls == null) {
            try {
                cls = Class.forName("org.eclipse.paho.client.mqttv3.TimerPingSender");
                f466a = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        b = cls.getName();
        c = LoggerFactory.getLogger(LoggerFactory.MQTT_CLIENT_MSG_CAT, b);
    }

    static a a(TimerPingSender timerPingSender) {
        return timerPingSender.d;
    }

    static Logger a() {
        return c;
    }

    static String b() {
        return b;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPingSender
    public void init(a aVar) {
        if (aVar == null) {
            throw new IllegalArgumentException("ClientComms cannot be null.");
        }
        this.d = aVar;
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPingSender
    public void schedule(long j) {
        this.e.schedule(new PingTask(this, null), j);
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPingSender
    public void start() {
        String clientId = this.d.k().getClientId();
        c.fine(b, "start", "659", new Object[]{clientId});
        this.e = new Timer(new StringBuffer("MQTT Ping: ").append(clientId).toString());
        this.e.schedule(new PingTask(this, null), this.d.l());
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttPingSender
    public void stop() {
        c.fine(b, "stop", "661", null);
        if (this.e != null) {
            this.e.cancel();
        }
    }
}
