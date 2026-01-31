package com.baidu.location.b;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import com.baidu.location.a.k;
import com.baidu.location.a.s;
import com.baidu.location.a.u;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/* loaded from: classes.dex */
public class g {
    private WifiManager c = null;
    private a d = null;
    private f e = null;
    private long f = 0;
    private long g = 0;
    private boolean h = false;
    private Handler i = new Handler();
    private long j = 0;
    private long k = 0;
    private static g b = null;

    /* renamed from: a, reason: collision with root package name */
    public static long f96a = 0;

    private class a extends BroadcastReceiver {
        private long b;
        private boolean c;

        private a() {
            this.b = 0L;
            this.c = false;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (context == null) {
                return;
            }
            String action = intent.getAction();
            if (action.equals("android.net.wifi.SCAN_RESULTS")) {
                g.f96a = System.currentTimeMillis() / 1000;
                g.this.r();
                k.c().h();
                if (System.currentTimeMillis() - s.b() <= DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS) {
                    u.a(s.c(), g.this.n(), s.d(), s.a());
                    return;
                }
                return;
            }
            if (action.equals("android.net.wifi.STATE_CHANGE") && ((NetworkInfo) intent.getParcelableExtra("networkInfo")).getState().equals(NetworkInfo.State.CONNECTED) && System.currentTimeMillis() - this.b >= DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS) {
                this.b = System.currentTimeMillis();
                if (this.c) {
                    return;
                }
                this.c = true;
            }
        }
    }

    private g() {
    }

    public static synchronized g a() {
        if (b == null) {
            b = new g();
        }
        return b;
    }

    private String a(long j) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(String.valueOf((int) (j & 255)));
        stringBuffer.append(FilenameUtils.EXTENSION_SEPARATOR);
        stringBuffer.append(String.valueOf((int) ((j >> 8) & 255)));
        stringBuffer.append(FilenameUtils.EXTENSION_SEPARATOR);
        stringBuffer.append(String.valueOf((int) ((j >> 16) & 255)));
        stringBuffer.append(FilenameUtils.EXTENSION_SEPARATOR);
        stringBuffer.append(String.valueOf((int) ((j >> 24) & 255)));
        return stringBuffer.toString();
    }

    public static boolean a(f fVar, f fVar2) {
        return a(fVar, fVar2, 0.7f);
    }

    public static boolean a(f fVar, f fVar2, float f) {
        int i;
        long j;
        int i2;
        if (fVar == null || fVar2 == null) {
            return false;
        }
        List<ScanResult> list = fVar.f95a;
        List<ScanResult> list2 = fVar2.f95a;
        if (list == list2) {
            return true;
        }
        if (list == null || list2 == null) {
            return false;
        }
        LinkedList linkedList = new LinkedList();
        int size = list.size();
        int size2 = list2.size();
        if (size == 0 && size2 == 0) {
            return true;
        }
        if (size == 0 || size2 == 0) {
            return false;
        }
        int i3 = 0;
        long j2 = 0;
        int i4 = 0;
        while (i4 < size) {
            String str = list.get(i4).BSSID;
            if (str == null) {
                long j3 = j2;
                i2 = i3;
                j = j3;
            } else {
                int i5 = 0;
                while (true) {
                    if (i5 >= size2) {
                        i = i3;
                        break;
                    }
                    if (str.equals(list2.get(i5).BSSID)) {
                        i = i3 + 1;
                        j2 += (list.get(i4).level - list2.get(i5).level) * (list.get(i4).level - list2.get(i5).level);
                        break;
                    }
                    i5++;
                }
                if (i5 == size2) {
                    linkedList.add(list.get(i4));
                    j = ((list.get(i4).level + 100) * (list.get(i4).level + 100)) + j2;
                    i2 = i;
                } else {
                    j = j2;
                    i2 = i;
                }
            }
            i4++;
            i3 = i2;
            j2 = j;
        }
        double dSqrt = Math.sqrt(j2) / size;
        return ((float) i3) >= ((float) size) * f;
    }

    public static boolean i() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) com.baidu.location.f.b().getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.getType() == 1) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r() {
        if (this.c == null) {
            return;
        }
        try {
            List<ScanResult> scanResults = this.c.getScanResults();
            if (scanResults != null) {
                f fVar = new f(scanResults, System.currentTimeMillis());
                if (this.e == null || !fVar.a(this.e)) {
                    this.e = fVar;
                }
            }
        } catch (Exception e) {
        }
    }

    public void b() {
        this.j = 0L;
    }

    public synchronized void c() {
        if (!this.h && com.baidu.location.f.f) {
            this.c = (WifiManager) com.baidu.location.f.b().getApplicationContext().getSystemService("wifi");
            this.d = new a();
            try {
                com.baidu.location.f.b().registerReceiver(this.d, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
            } catch (Exception e) {
            }
            this.h = true;
        }
    }

    public synchronized void d() {
        if (this.h) {
            try {
                com.baidu.location.f.b().unregisterReceiver(this.d);
                f96a = 0L;
            } catch (Exception e) {
            }
            this.d = null;
            this.c = null;
            this.h = false;
        }
    }

    public boolean e() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.g > 0 && jCurrentTimeMillis - this.g <= DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS) {
            return false;
        }
        this.g = jCurrentTimeMillis;
        b();
        return f();
    }

    public boolean f() {
        if (this.c == null) {
            return false;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.f > 0) {
            if (jCurrentTimeMillis - this.f <= this.j + DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS || jCurrentTimeMillis - (f96a * 1000) <= this.j + DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS) {
                return false;
            }
            if (i() && jCurrentTimeMillis - this.f <= 10000 + this.j) {
                return false;
            }
        }
        return h();
    }

    @SuppressLint({"NewApi"})
    public String g() {
        if (this.c == null) {
            return "";
        }
        try {
            if (!this.c.isWifiEnabled()) {
                if (Build.VERSION.SDK_INT <= 17) {
                    return "";
                }
                if (!this.c.isScanAlwaysAvailable()) {
                    return "";
                }
            }
            return "&wifio=1";
        } catch (Exception e) {
            return "";
        } catch (NoSuchMethodError e2) {
            return "";
        }
    }

    @SuppressLint({"NewApi"})
    public boolean h() {
        long jCurrentTimeMillis = System.currentTimeMillis() - this.k;
        if (jCurrentTimeMillis >= 0 && jCurrentTimeMillis <= 2000) {
            return false;
        }
        this.k = System.currentTimeMillis();
        try {
            if (!this.c.isWifiEnabled() && (Build.VERSION.SDK_INT <= 17 || !this.c.isScanAlwaysAvailable())) {
                return false;
            }
            this.c.startScan();
            this.f = System.currentTimeMillis();
            return true;
        } catch (Exception e) {
            return false;
        } catch (NoSuchMethodError e2) {
            return false;
        }
    }

    @SuppressLint({"NewApi"})
    public boolean j() {
        f fVar;
        try {
            if ((!this.c.isWifiEnabled() && (Build.VERSION.SDK_INT <= 17 || !this.c.isScanAlwaysAvailable())) || i() || (fVar = new f(this.c.getScanResults(), 0L)) == null) {
                return false;
            }
            return fVar.e();
        } catch (Exception e) {
            return false;
        } catch (NoSuchMethodError e2) {
            return false;
        }
    }

    public WifiInfo k() {
        if (this.c == null) {
            return null;
        }
        try {
            WifiInfo connectionInfo = this.c.getConnectionInfo();
            if (connectionInfo == null || connectionInfo.getBSSID() == null || connectionInfo.getRssi() <= -100) {
                return null;
            }
            String bssid = connectionInfo.getBSSID();
            if (bssid != null) {
                String strReplace = bssid.replace(":", "");
                if ("000000000000".equals(strReplace)) {
                    return null;
                }
                if ("".equals(strReplace)) {
                    return null;
                }
            }
            return connectionInfo;
        } catch (Error e) {
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    public String l() {
        StringBuffer stringBuffer = new StringBuffer();
        WifiInfo wifiInfoK = a().k();
        if (wifiInfoK == null || wifiInfoK.getBSSID() == null) {
            return null;
        }
        String strReplace = wifiInfoK.getBSSID().replace(":", "");
        int rssi = wifiInfoK.getRssi();
        String strM = a().m();
        if (rssi < 0) {
            rssi = -rssi;
        }
        if (strReplace == null || rssi >= 100) {
            return null;
        }
        stringBuffer.append("&wf=");
        stringBuffer.append(strReplace);
        stringBuffer.append(";");
        stringBuffer.append("" + rssi + ";");
        String ssid = wifiInfoK.getSSID();
        if (ssid != null && (ssid.contains("&") || ssid.contains(";"))) {
            ssid = ssid.replace("&", "_");
        }
        stringBuffer.append(ssid);
        stringBuffer.append("&wf_n=1");
        if (strM != null) {
            stringBuffer.append("&wf_gw=");
            stringBuffer.append(strM);
        }
        return stringBuffer.toString();
    }

    public String m() {
        DhcpInfo dhcpInfo;
        if (this.c == null || (dhcpInfo = this.c.getDhcpInfo()) == null) {
            return null;
        }
        return a(dhcpInfo.gateway);
    }

    public f n() {
        return (this.e == null || !this.e.h()) ? p() : this.e;
    }

    public f o() {
        return (this.e == null || !this.e.i()) ? p() : this.e;
    }

    public f p() {
        if (this.c != null) {
            try {
                return new f(this.c.getScanResults(), this.f);
            } catch (Exception e) {
            }
        }
        return new f(null, 0L);
    }

    public String q() {
        try {
            WifiInfo connectionInfo = this.c.getConnectionInfo();
            if (connectionInfo != null) {
                return connectionInfo.getMacAddress();
            }
            return null;
        } catch (Error e) {
            return null;
        } catch (Exception e2) {
            return null;
        }
    }
}
