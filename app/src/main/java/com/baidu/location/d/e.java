package com.baidu.location.d;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import cn.yunzhisheng.asr.JniUscClient;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class e {
    public String h = null;
    public int i = 3;
    public String j = null;
    public Map<String, Object> k = null;
    public String l = null;
    public byte[] m = null;
    public String n = null;
    public static int g = a.g;

    /* renamed from: a, reason: collision with root package name */
    private static String f107a = "10.0.0.172";
    private static int b = 80;
    protected static int o = 0;

    private static int a(Context context, NetworkInfo networkInfo) {
        String lowerCase;
        if (networkInfo != null && networkInfo.getExtraInfo() != null && (lowerCase = networkInfo.getExtraInfo().toLowerCase()) != null) {
            if (lowerCase.startsWith("cmwap") || lowerCase.startsWith("uniwap") || lowerCase.startsWith("3gwap")) {
                String defaultHost = Proxy.getDefaultHost();
                if (defaultHost == null || defaultHost.equals("") || defaultHost.equals(JniUscClient.az)) {
                    defaultHost = "10.0.0.172";
                }
                f107a = defaultHost;
                return a.d;
            }
            if (lowerCase.startsWith("ctwap")) {
                String defaultHost2 = Proxy.getDefaultHost();
                if (defaultHost2 == null || defaultHost2.equals("") || defaultHost2.equals(JniUscClient.az)) {
                    defaultHost2 = "10.0.0.200";
                }
                f107a = defaultHost2;
                return a.d;
            }
            if (lowerCase.startsWith("cmnet") || lowerCase.startsWith("uninet") || lowerCase.startsWith("ctnet") || lowerCase.startsWith("3gnet")) {
                return a.e;
            }
        }
        String defaultHost3 = Proxy.getDefaultHost();
        if (defaultHost3 != null && defaultHost3.length() > 0) {
            if ("10.0.0.172".equals(defaultHost3.trim())) {
                f107a = "10.0.0.172";
                return a.d;
            }
            if ("10.0.0.200".equals(defaultHost3.trim())) {
                f107a = "10.0.0.200";
                return a.d;
            }
        }
        return a.e;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        g = c();
    }

    private int c() {
        NetworkInfo activeNetworkInfo;
        int iA;
        Context contextB = com.baidu.location.f.b();
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) contextB.getSystemService("connectivity");
            if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isAvailable()) {
                iA = a.g;
            } else if (activeNetworkInfo.getType() == 1) {
                String defaultHost = Proxy.getDefaultHost();
                iA = (defaultHost == null || defaultHost.length() <= 0) ? a.f : a.h;
            } else {
                iA = a(contextB, activeNetworkInfo);
            }
            return iA;
        } catch (Exception e) {
            return a.g;
        }
    }

    public abstract void a();

    public abstract void a(boolean z);

    public void a(boolean z, String str) {
        new g(this, str, z).start();
    }

    public void b(String str) {
        new h(this, str).start();
    }

    public void d() {
        new f(this).start();
    }

    public void e() {
        a(false, "loc.map.baidu.com");
    }
}
