package com.unisound.passport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* loaded from: classes.dex */
public class d {
    private static final int t = 10000;
    i b;
    h c;
    private ConnectivityManager o;
    private boolean p;
    private Handler q;
    private long r;
    private Looper s;
    private Context u;
    private String v;
    private String w;
    private static String d = "push.passport.hivoice.cn";
    private static int e = 1883;
    private static int f = 8883;
    private static MqttClientPersistence g = null;
    private static boolean h = false;
    private static short i = 30;
    private static int[] j = {0};
    private static boolean k = false;
    private static boolean l = true;
    private static String m = "passportuser";
    private static String n = "PZs5J8tc9qL12ocy";
    public static String MQTT_TOPIC = "sensors/temperature";

    /* renamed from: a, reason: collision with root package name */
    PassportListener f284a = null;
    private BroadcastReceiver x = new f(this);

    protected d(Context context, String str, String str2) {
        b.b("PassportInterface");
        this.u = context;
        this.o = (ConnectivityManager) this.u.getSystemService("connectivity");
        this.s = this.u.getMainLooper();
        this.q = new g(this, this.s);
        this.r = System.currentTimeMillis();
        this.v = str;
        this.w = str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        b.b(str);
    }

    private void a(boolean z) {
        this.p = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean l() {
        NetworkInfo activeNetworkInfo = this.o.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return false;
        }
        return activeNetworkInfo.isConnected();
    }

    private void m() {
        if (this.c == null) {
            a("Connecting...");
            if (this.v.equals("") || this.w.equals("")) {
                a(1401, 1501);
            } else {
                try {
                    this.c = new h(this, d, new String[]{"passport/" + this.v + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.w});
                } catch (MqttException e2) {
                    b.e("connect error occur!");
                    a(1401, 1502);
                    a("MqttException: " + (e2.getMessage() != null ? e2.getMessage() : "NULL"));
                    if (l()) {
                        scheduleReconnect(this.r);
                    }
                }
            }
        }
        a(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n() {
        if (this.b != null) {
            this.b.e();
            this.b = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void o() {
        if (this.p && this.c == null) {
            a("Reconnecting...");
            m();
        }
    }

    protected void a(int i2) {
        Message message = new Message();
        message.what = i2;
        this.q.sendMessage(message);
    }

    protected void a(int i2, int i3) {
        Message message = new Message();
        message.what = i2;
        message.obj = Integer.valueOf(i3);
        this.q.sendMessage(message);
    }

    protected synchronized void closeConnection() {
        if (this.p) {
            a(false);
            this.u.unregisterReceiver(this.x);
            n();
            if (this.c != null) {
                this.c.a();
                this.c = null;
            }
        } else {
            b.d("Attempt to stop connection not active.");
            this.q.sendEmptyMessage(1204);
        }
    }

    protected Object getOption(int i2) {
        return null;
    }

    protected void init() {
    }

    protected synchronized void openConnection() {
        a("Starting service...");
        if (this.p) {
            b.d("Attempt to start connection that is already active");
            this.q.sendEmptyMessage(c.e);
        } else {
            m();
            this.u.registerReceiver(this.x, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }

    public void publishToTopic(String str, String str2) {
        if (this.c != null) {
            try {
                this.c.a(str, str2);
            } catch (MqttException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void scheduleReconnect(long j2) {
        a("Rescheduling connection in 10000ms.");
        if (this.b == null) {
            this.b = new i(new e(this), this.s);
        }
        this.b.a(10000);
        this.b.c();
    }

    protected void setOption(int i2, Object obj) {
    }

    protected void setPassportListener(PassportListener passportListener) {
        this.f284a = passportListener;
    }
}
