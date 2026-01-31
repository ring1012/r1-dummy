package com.baidu.b.a;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/* loaded from: classes.dex */
public class j {

    /* renamed from: a, reason: collision with root package name */
    private Context f45a;
    private String b = null;
    private HashMap<String, String> c = null;
    private String d = null;

    public j(Context context) {
        this.f45a = context;
    }

    private String a(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return null;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
                return null;
            }
            String extraInfo = activeNetworkInfo.getExtraInfo();
            return (extraInfo == null || !(extraInfo.trim().toLowerCase().equals("cmwap") || extraInfo.trim().toLowerCase().equals("uniwap") || extraInfo.trim().toLowerCase().equals("3gwap") || extraInfo.trim().toLowerCase().equals("ctwap"))) ? "wifi" : extraInfo.trim().toLowerCase().equals("ctwap") ? "ctwap" : "cmwap";
        } catch (Exception e) {
            if (d.f40a) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:107:0x0207  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0216  */
    /* JADX WARN: Removed duplicated region for block: B:145:0x0275 A[PHI: r3
  0x0275: PHI (r3v6 int) = (r3v3 int), (r3v4 int), (r3v7 int) binds: [B:88:0x01df, B:74:0x01a3, B:56:0x015d] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:149:0x01f6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:162:0x00d3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00d8 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0131 A[Catch: MalformedURLException -> 0x0135, all -> 0x0230, Exception -> 0x023c, IOException -> 0x0247, TryCatch #13 {all -> 0x0230, blocks: (B:8:0x0031, B:38:0x0115, B:84:0x01b9, B:86:0x01bd, B:87:0x01c0, B:70:0x017d, B:72:0x0181, B:73:0x0184, B:40:0x011d, B:26:0x00c5, B:28:0x00cd, B:46:0x0129, B:48:0x0131, B:49:0x0134), top: B:157:0x002d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(javax.net.ssl.HttpsURLConnection r13) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 641
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.b.a.j.a(javax.net.ssl.HttpsURLConnection):void");
    }

    private static String b(HashMap<String, String> map) {
        boolean z;
        StringBuilder sb = new StringBuilder();
        boolean z2 = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (z2) {
                z = false;
            } else {
                sb.append("&");
                z = z2;
            }
            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            z2 = z;
        }
        return sb.toString();
    }

    private HttpsURLConnection b() {
        try {
            URL url = new URL(this.b);
            d.a("https URL: " + this.b);
            String strA = a(this.f45a);
            if (strA == null || strA.equals("")) {
                d.c("Current network is not available.");
                this.d = a.a(-10, "Current network is not available.");
                return null;
            }
            d.a("checkNetwork = " + strA);
            HttpsURLConnection httpsURLConnection = strA.equals("cmwap") ? (HttpsURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.172", 80))) : strA.equals("ctwap") ? (HttpsURLConnection) url.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.200", 80))) : (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setConnectTimeout(50000);
            httpsURLConnection.setReadTimeout(50000);
            return httpsURLConnection;
        } catch (MalformedURLException e) {
            if (d.f40a) {
                e.printStackTrace();
                d.a(e.getMessage());
            }
            this.d = a.a(-11, "Auth server could not be parsed as a URL.");
            return null;
        } catch (Exception e2) {
            if (d.f40a) {
                e2.printStackTrace();
                d.a(e2.getMessage());
            }
            this.d = a.a(-11, "Init httpsurlconnection failed.");
            return null;
        }
    }

    private HashMap<String, String> c(HashMap<String, String> map) {
        HashMap<String, String> map2 = new HashMap<>();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String string = it.next().toString();
            map2.put(string, map.get(string));
        }
        return map2;
    }

    protected String a(HashMap<String, String> map) throws Throwable {
        this.c = c(map);
        this.b = this.c.get("url");
        HttpsURLConnection httpsURLConnectionB = b();
        if (httpsURLConnectionB == null) {
            d.c("syncConnect failed,httpsURLConnection is null");
            return this.d;
        }
        a(httpsURLConnectionB);
        return this.d;
    }

    protected boolean a() {
        d.a("checkNetwork start");
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.f45a.getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.isAvailable()) {
                    d.a("checkNetwork end");
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            if (d.f40a) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
