package com.unisound.passport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
class h implements IMqttActionListener, MqttCallback {

    /* renamed from: a, reason: collision with root package name */
    IMqttAsyncClient f288a;
    String b;
    final /* synthetic */ d c;

    public h(d dVar, String str, String[] strArr) {
        String str2;
        int i;
        this.c = dVar;
        this.f288a = null;
        this.b = "";
        if (d.l) {
            b.e("Doing an SSL Connect");
            str2 = "ssl://";
            i = d.f;
        } else {
            str2 = "tcp://";
            i = d.e;
        }
        String str3 = str2 + d.d + ":" + i;
        b.b("URL: " + str3);
        this.b = com.unisound.passport.a.c.a();
        this.f288a = new MqttAsyncClient(str3, this.b, d.g);
        this.f288a.setCallback(this);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        if (d.l) {
            mqttConnectOptions.setSocketFactory(a("passw0rd"));
        }
        mqttConnectOptions.setCleanSession(d.h);
        mqttConnectOptions.setKeepAliveInterval(d.i);
        mqttConnectOptions.setUserName(d.m);
        mqttConnectOptions.setPassword(d.n.toCharArray());
        this.f288a.connect(mqttConnectOptions, null, this).waitForCompletion();
        for (String str4 : strArr) {
            dVar.a("topics=" + str4);
        }
        a(strArr);
        dVar.a("Connection established to " + d.d + " on topic " + strArr);
        dVar.r = System.currentTimeMillis();
        dVar.q.sendEmptyMessage(1202);
    }

    private SSLSocketFactory a(String str) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException, KeyManagementException, MqttSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance("BKS");
            keyStore.load(this.c.u.getAssets().open("bks/ssl"), str.toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            SSLContext sSLContext = SSLContext.getInstance("SSL");
            sSLContext.init(null, trustManagers, null);
            return sSLContext.getSocketFactory();
        } catch (FileNotFoundException e) {
            b.e("MqttSecurityException FileNotFoundException:" + e.getMessage());
            throw new MqttSecurityException(e);
        } catch (IOException e2) {
            b.e("MqttSecurityException IOException:" + e2.getMessage());
            throw new MqttSecurityException(e2);
        } catch (KeyManagementException e3) {
            b.e("MqttSecurityException KeyManagementException:" + e3.getMessage());
            throw new MqttSecurityException(e3);
        } catch (KeyStoreException e4) {
            b.e("MqttSecurityException KeyStoreException:" + e4.getMessage());
            throw new MqttSecurityException(e4);
        } catch (NoSuchAlgorithmException e5) {
            b.e("MqttSecurityException NoSuchAlgorithmException:" + e5.getMessage());
            throw new MqttSecurityException(e5);
        } catch (CertificateException e6) {
            b.e("MqttSecurityException CertificateException:" + e6.getMessage());
            throw new MqttSecurityException(e6);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, String str2) {
        if (this.f288a == null || !this.f288a.isConnected()) {
            this.c.a("No connection to public to");
        } else {
            b.c("publish ==> topickName = " + str + " , message = " + str2);
            this.f288a.publish(str, str2.getBytes(), d.j[0], d.k);
        }
    }

    private void a(String[] strArr) {
        if (this.f288a == null || !this.f288a.isConnected()) {
            this.c.a("Connection errorNo connection");
        } else {
            this.f288a.subscribe(strArr, d.j);
        }
    }

    public void a() {
        try {
            if (this.f288a == null || !this.f288a.isConnected()) {
                return;
            }
            this.f288a.disconnect();
            this.c.q.sendEmptyMessage(1203);
            b.c("connection close");
        } catch (Exception e) {
            b.e("connection close error");
            this.c.a(1401, 1503);
            this.c.a("MqttException" + (e.getMessage() != null ? e.getMessage() : " NULL") + e.getMessage());
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttCallback
    public void connectionLost(Throwable th) {
        this.c.a("Loss of connectionconnection downed");
        this.c.q.sendEmptyMessage(1201);
        this.c.c = null;
        if (this.c.l()) {
            this.c.o();
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttCallback
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttCallback
    public void messageArrived(String str, MqttMessage mqttMessage) throws JSONException {
        String str2 = new String(mqttMessage.getPayload());
        this.c.a("Got message: " + str2);
        this.c.q.obtainMessage(1301, str2).sendToTarget();
        if (str2.contains("messageId")) {
            String string = new JSONObject(str2).getString("messageId");
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("messageId", string);
            String string2 = jSONObject.toString();
            this.c.a("ack message: " + string2);
            a("passport/ack", string2);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
    public void onFailure(IMqttToken iMqttToken, Throwable th) {
        this.c.a("---------------onFailure-----------------" + th.getMessage());
    }

    @Override // org.eclipse.paho.client.mqttv3.IMqttActionListener
    public void onSuccess(IMqttToken iMqttToken) {
        this.c.a("---------------onSuccess-----------------");
    }
}
