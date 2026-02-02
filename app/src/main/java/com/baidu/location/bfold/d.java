package com.baidu.location.bfold;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import cn.yunzhisheng.common.PinyinConverter;
import com.baidu.location.Jni;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import com.unisound.client.ErrorCode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class d {
    private int A;
    private int B;
    private Context d;
    private Location f;
    private GpsStatus i;
    private a j;
    private boolean k;
    private static d c = null;
    private static int m = 0;
    private static int n = 0;
    private static String u = null;
    private static double C = 100.0d;
    private static String D = "";

    /* renamed from: a, reason: collision with root package name */
    private final long f89a = 1000;
    private final long b = 9000;
    private LocationManager e = null;
    private c g = null;
    private C0007d h = null;
    private b l = null;
    private long o = 0;
    private boolean p = false;
    private boolean q = false;
    private String r = null;
    private boolean s = false;
    private long t = 0;
    private Handler v = null;
    private final int w = 1;
    private final int x = 2;
    private final int y = 3;
    private final int z = 4;
    private long E = 0;
    private ArrayList<ArrayList<Float>> F = new ArrayList<>();

    @TargetApi(24)
    private class a extends GnssStatus.Callback {
        private a() {
        }

        /* synthetic */ a(d dVar, e eVar) {
            this();
        }
    }

    private class b implements GpsStatus.Listener {

        /* renamed from: a, reason: collision with root package name */
        long f91a;
        private long c;
        private final int d;
        private boolean e;
        private List<String> f;
        private String g;
        private String h;
        private String i;
        private long j;

        private b() {
            this.f91a = 0L;
            this.c = 0L;
            this.d = ErrorCode.VPR_CLIENT_PARAM_ERROR;
            this.e = false;
            this.f = new ArrayList();
            this.g = null;
            this.h = null;
            this.i = null;
            this.j = 0L;
        }

        /* synthetic */ b(d dVar, e eVar) {
            this();
        }

        @Override // android.location.GpsStatus.Listener
        public void onGpsStatusChanged(int i) {
            int i2 = 0;
            if (d.this.e == null) {
            }
            switch (i) {
                case 2:
                    d.this.d((Location) null);
                    d.this.b(false);
                    int unused = d.m = 0;
                    int unused2 = d.n = 0;
                    break;
                case 4:
                    if (d.this.q) {
                        try {
                            if (d.this.i == null) {
                                d.this.i = d.this.e.getGpsStatus(null);
                            } else {
                                d.this.e.getGpsStatus(d.this.i);
                            }
                            d.this.A = 0;
                            d.this.B = 0;
                            double snr = 0.0d;
                            d.this.F.clear();
                            int i3 = 0;
                            for (GpsSatellite gpsSatellite : d.this.i.getSatellites()) {
                                ArrayList arrayList = new ArrayList();
                                if (gpsSatellite.usedInFix()) {
                                    i3++;
                                    if (gpsSatellite.getPrn() <= 65) {
                                        i2++;
                                        snr += gpsSatellite.getSnr();
                                        arrayList.add(Float.valueOf(0.0f));
                                        arrayList.add(Float.valueOf(gpsSatellite.getSnr()));
                                        arrayList.add(Float.valueOf(gpsSatellite.getAzimuth()));
                                        arrayList.add(Float.valueOf(gpsSatellite.getElevation()));
                                        arrayList.add(Float.valueOf(1.0f));
                                    }
                                    d.this.F.add(arrayList);
                                    if (gpsSatellite.getSnr() >= j.G) {
                                        d.f(d.this);
                                    }
                                }
                            }
                            if (i2 > 0) {
                                int unused3 = d.n = i2;
                                double unused4 = d.C = snr / i2;
                            }
                            if (i3 > 0) {
                                this.j = System.currentTimeMillis();
                                int unused5 = d.m = i3;
                                break;
                            } else if (System.currentTimeMillis() - this.j > 100) {
                                this.j = System.currentTimeMillis();
                                int unused6 = d.m = i3;
                                break;
                            }
                        } catch (Exception e) {
                            return;
                        }
                    }
                    break;
            }
        }
    }

    private class c implements LocationListener {
        private c() {
        }

        /* synthetic */ c(d dVar, e eVar) {
            this();
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            d.this.t = System.currentTimeMillis();
            d.this.b(true);
            d.this.d(location);
            d.this.p = false;
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
            d.this.d((Location) null);
            d.this.b(false);
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
            switch (i) {
                case 0:
                    d.this.d((Location) null);
                    d.this.b(false);
                    break;
                case 1:
                    d.this.o = System.currentTimeMillis();
                    d.this.p = true;
                    d.this.b(false);
                    break;
                case 2:
                    d.this.p = false;
                    break;
            }
        }
    }

    /* renamed from: com.baidu.location.b.d$d, reason: collision with other inner class name */
    private class C0007d implements LocationListener {
        private long b;

        private C0007d() {
            this.b = 0L;
        }

        /* synthetic */ C0007d(d dVar, e eVar) {
            this();
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (!d.this.q && location != null && location.getProvider() == "gps" && System.currentTimeMillis() - this.b >= 10000 && u.a(location, false)) {
                this.b = System.currentTimeMillis();
                d.this.v.sendMessage(d.this.v.obtainMessage(4, location));
            }
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    private d() throws ClassNotFoundException {
        this.k = false;
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Class.forName("android.location.GnssStatus");
                this.k = true;
            } catch (ClassNotFoundException e) {
                this.k = false;
            }
        }
    }

    public static synchronized d a() {
        if (c == null) {
            c = new d();
        }
        return c;
    }

    public static String a(Location location) {
        if (location == null) {
            return null;
        }
        float speed = (float) (location.getSpeed() * 3.6d);
        if (!location.hasSpeed()) {
            speed = -1.0f;
        }
        return String.format(Locale.CHINA, "&ll=%.5f|%.5f&s=%.1f&d=%.1f&ll_r=%d&ll_n=%d&ll_h=%.2f&ll_t=%d&ll_sn=%d|%d&ll_snr=%.1f", Double.valueOf(location.getLongitude()), Double.valueOf(location.getLatitude()), Float.valueOf(speed), Float.valueOf(location.hasBearing() ? location.getBearing() : -1.0f), Integer.valueOf((int) (location.hasAccuracy() ? location.getAccuracy() : -1.0f)), Integer.valueOf(m), Double.valueOf(location.hasAltitude() ? location.getAltitude() : 555.0d), Long.valueOf(location.getTime() / 1000), Integer.valueOf(m), Integer.valueOf(n), Double.valueOf(C));
    }

    private void a(double d, double d2, float f) {
        int i = 0;
        if (d >= 73.146973d && d <= 135.252686d && d2 <= 54.258807d && d2 >= 14.604847d && f <= 18.0f) {
            int i2 = (int) ((d - j.s) * 1000.0d);
            int i3 = (int) ((j.t - d2) * 1000.0d);
            if (i2 <= 0 || i2 >= 50 || i3 <= 0 || i3 >= 50) {
                String str = String.format(Locale.CHINA, "&ll=%.5f|%.5f", Double.valueOf(d), Double.valueOf(d2)) + "&im=" + com.baidu.location.d.b.a().b();
                j.q = d;
                j.r = d2;
            } else {
                int i4 = i2 + (i3 * 50);
                int i5 = i4 >> 2;
                int i6 = i4 & 3;
                if (j.w) {
                    i = (j.v[i5] >> (i6 * 2)) & 3;
                }
            }
        }
        if (j.u != i) {
            j.u = i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, Location location) {
        if (location == null) {
            return;
        }
        String str2 = str + com.baidu.location.a.a.a().c();
        boolean zE = g.a().e();
        s.a(new com.baidu.location.bfold.a(com.baidu.location.b.b.a().f()));
        s.a(System.currentTimeMillis());
        s.a(new Location(location));
        s.a(str2);
        if (zE) {
            return;
        }
        u.a(s.c(), null, s.d(), str2);
    }

    public static boolean a(Location location, Location location2, boolean z) {
        if (location == location2) {
            return false;
        }
        if (location == null || location2 == null) {
            return true;
        }
        float speed = location2.getSpeed();
        if (z && ((j.u == 3 || !com.baidu.location.d.d.a().a(location2.getLongitude(), location2.getLatitude())) && speed < 5.0f)) {
            return true;
        }
        float fDistanceTo = location2.distanceTo(location);
        return speed > j.K ? fDistanceTo > j.M : speed > j.J ? fDistanceTo > j.L : fDistanceTo > 5.0f;
    }

    public static String b(Location location) {
        String strA = a(location);
        return strA != null ? strA + "&g_tp=0" : strA;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(boolean z) {
        this.s = z;
        if (!z || !i()) {
        }
    }

    public static String c(Location location) {
        String strA = a(location);
        return strA != null ? strA + u : strA;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(Location location) {
        this.v.sendMessage(this.v.obtainMessage(1, location));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(Location location) throws IOException {
        Location location2;
        if (location == null) {
            this.f = null;
            return;
        }
        int i = m;
        if (i == 0) {
            try {
                i = location.getExtras().getInt("satellites");
            } catch (Exception e) {
            }
        }
        if (i != 0 || j.l) {
            this.f = location;
            int i2 = m;
            if (this.f == null) {
                this.r = null;
                location2 = null;
            } else {
                Location location3 = new Location(this.f);
                long jCurrentTimeMillis = System.currentTimeMillis();
                this.f.setTime(jCurrentTimeMillis);
                float speed = (float) (this.f.getSpeed() * 3.6d);
                if (!this.f.hasSpeed()) {
                    speed = -1.0f;
                }
                if (i2 == 0) {
                    try {
                        i2 = this.f.getExtras().getInt("satellites");
                    } catch (Exception e2) {
                    }
                }
                this.r = String.format(Locale.CHINA, "&ll=%.5f|%.5f&s=%.1f&d=%.1f&ll_n=%d&ll_t=%d", Double.valueOf(this.f.getLongitude()), Double.valueOf(this.f.getLatitude()), Float.valueOf(speed), Float.valueOf(this.f.getBearing()), Integer.valueOf(i2), Long.valueOf(jCurrentTimeMillis));
                a(this.f.getLongitude(), this.f.getLatitude(), speed);
                location2 = location3;
            }
            try {
                com.baidu.location.a.g.a().a(this.f);
            } catch (Exception e3) {
            }
            if (location2 != null) {
                com.baidu.location.a.d.a().a(location2);
            }
            if (!i() || this.f == null) {
                return;
            }
            D = j();
            com.baidu.location.a.a.a().a(f());
            if (m <= 2 || !u.a(this.f, true)) {
                return;
            }
            boolean zE = g.a().e();
            s.a(new com.baidu.location.bfold.a(com.baidu.location.b.b.a().f()));
            s.a(System.currentTimeMillis());
            s.a(new Location(this.f));
            s.a(com.baidu.location.a.a.a().c());
            if (zE) {
                return;
            }
            u.a(s.c(), null, s.d(), com.baidu.location.a.a.a().c());
        }
    }

    static /* synthetic */ int f(d dVar) {
        int i = dVar.B;
        dVar.B = i + 1;
        return i;
    }

    private String j() {
        StringBuilder sb = new StringBuilder();
        if (this.F.size() > 32 || this.F.size() == 0) {
            return sb.toString();
        }
        Iterator<ArrayList<Float>> it = this.F.iterator();
        boolean z = true;
        while (it.hasNext()) {
            ArrayList<Float> next = it.next();
            if (next.size() == 5) {
                if (z) {
                    z = false;
                } else {
                    sb.append(PinyinConverter.PINYIN_EXCLUDE);
                }
                sb.append(String.format("%.1f;", next.get(0)));
                sb.append(String.format("%.1f;", next.get(2)));
                sb.append(String.format("%.0f;", next.get(2)));
                sb.append(String.format("%.0f;", next.get(3)));
                sb.append(String.format("%.0f", next.get(4)));
            }
        }
        return sb.toString();
    }

    public void a(boolean z) {
        if (z) {
            c();
        } else {
            d();
        }
    }

    public synchronized void b() {
        if (com.baidu.location.f.f) {
            this.d = com.baidu.location.f.b();
            try {
                this.e = (LocationManager) this.d.getSystemService("location");
                if (this.k) {
                    this.j = new a(this, null);
                    this.e.registerGnssStatusCallback(this.j);
                } else {
                    this.l = new b(this, null);
                    this.e.addGpsStatusListener(this.l);
                }
                this.h = new C0007d(this, null);
                this.e.requestLocationUpdates("passive", 9000L, 0.0f, this.h);
            } catch (Exception e) {
            }
            this.v = new e(this);
        }
    }

    public void c() {
        Log.d(com.baidu.location.d.a.f101a, "start gps...");
        if (this.q) {
            return;
        }
        try {
            this.g = new c(this, null);
            try {
                this.e.sendExtraCommand("gps", "force_xtra_injection", new Bundle());
            } catch (Exception e) {
            }
            this.e.requestLocationUpdates("gps", 1000L, 0.0f, this.g);
            this.E = System.currentTimeMillis();
            this.q = true;
        } catch (Exception e2) {
        }
    }

    public void d() {
        if (this.q) {
            if (this.e != null) {
                try {
                    if (this.g != null) {
                        this.e.removeUpdates(this.g);
                    }
                } catch (Exception e) {
                }
            }
            j.d = 0;
            j.u = 0;
            this.g = null;
            this.q = false;
            b(false);
        }
    }

    public synchronized void e() {
        d();
        if (this.e != null) {
            try {
                if (this.l != null) {
                    this.e.removeGpsStatusListener(this.l);
                }
                if (this.k && this.j != null) {
                    this.e.unregisterGnssStatusCallback(this.j);
                }
                this.e.removeUpdates(this.h);
            } catch (Exception e) {
            }
            this.l = null;
            this.e = null;
        }
    }

    public String f() {
        double[] dArr;
        boolean z;
        if (this.f == null) {
            return null;
        }
        String str = "{\"result\":{\"time\":\"" + j.a() + "\",\"error\":\"61\"},\"content\":{\"point\":{\"x\":\"%f\",\"y\":\"%f\"},\"radius\":\"%d\",\"d\":\"%f\",\"s\":\"%f\",\"n\":\"%d\"";
        int accuracy = (int) (this.f.hasAccuracy() ? this.f.getAccuracy() : 10.0f);
        float speed = (float) (this.f.getSpeed() * 3.6d);
        if (!this.f.hasSpeed()) {
            speed = -1.0f;
        }
        double[] dArr2 = new double[2];
        if (com.baidu.location.d.d.a().a(this.f.getLongitude(), this.f.getLatitude())) {
            double[] dArrA = Jni.a(this.f.getLongitude(), this.f.getLatitude(), "gps2gcj");
            if (dArrA[0] > 0.0d || dArrA[1] > 0.0d) {
                dArr = dArrA;
                z = true;
            } else {
                dArrA[0] = this.f.getLongitude();
                dArrA[1] = this.f.getLatitude();
                dArr = dArrA;
                z = true;
            }
        } else {
            dArr2[0] = this.f.getLongitude();
            dArr2[1] = this.f.getLatitude();
            dArr = dArr2;
            z = false;
        }
        String str2 = String.format(Locale.CHINA, str, Double.valueOf(dArr[0]), Double.valueOf(dArr[1]), Integer.valueOf(accuracy), Float.valueOf(this.f.getBearing()), Float.valueOf(speed), Integer.valueOf(m));
        if (!z) {
            str2 = str2 + ",\"in_cn\":\"0\"";
        }
        return this.f.hasAltitude() ? str2 + String.format(Locale.CHINA, ",\"h\":%.2f}}", Double.valueOf(this.f.getAltitude())) : str2 + "}}";
    }

    public Location g() {
        if (this.f != null && Math.abs(System.currentTimeMillis() - this.f.getTime()) <= ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS) {
            return this.f;
        }
        return null;
    }

    public boolean h() {
        try {
            if (this.f != null && this.f.getLatitude() != 0.0d && this.f.getLongitude() != 0.0d) {
                if (m > 2) {
                    return true;
                }
                if (this.f.getExtras().getInt("satellites", 3) > 2) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return (this.f == null || this.f.getLatitude() == 0.0d || this.f.getLongitude() == 0.0d) ? false : true;
        }
    }

    public boolean i() {
        if (!h() || System.currentTimeMillis() - this.t > 10000) {
            return false;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (!this.p || jCurrentTimeMillis - this.o >= 3000) {
            return this.s;
        }
        return true;
    }
}
