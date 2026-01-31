package com.baidu.location.a;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import cn.yunzhisheng.common.PinyinConverter;
import com.baidu.location.Jni;
import com.unisound.vui.priority.PriorityMap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class c {
    private static Method g = null;
    private static Method h = null;
    private static Method i = null;
    private static Method j = null;
    private static Method k = null;
    private static Class<?> l = null;

    /* renamed from: a, reason: collision with root package name */
    String f58a;
    String b;
    private Context d;
    private TelephonyManager e;
    private WifiManager m;
    private String o;
    private com.baidu.location.h p;
    private b q;
    private String s;
    private String t;
    private a f = new a();
    private d n = null;
    private String r = null;
    C0006c c = new C0006c();

    private class a {

        /* renamed from: a, reason: collision with root package name */
        public int f59a;
        public int b;
        public int c;
        public int d;
        public int e;
        public int f;
        public char g;

        private a() {
            this.f59a = -1;
            this.b = -1;
            this.c = -1;
            this.d = -1;
            this.e = PriorityMap.PRIORITY_MAX;
            this.f = PriorityMap.PRIORITY_MAX;
            this.g = (char) 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean b() {
            return this.f59a > -1 && this.b > 0;
        }

        public String a() {
            if (!b()) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer(128);
            stringBuffer.append("&nw=");
            stringBuffer.append(this.g);
            stringBuffer.append(String.format(Locale.CHINA, "&cl=%d|%d|%d|%d", Integer.valueOf(this.c), Integer.valueOf(this.d), Integer.valueOf(this.f59a), Integer.valueOf(this.b)));
            if (this.e < Integer.MAX_VALUE && this.f < Integer.MAX_VALUE) {
                stringBuffer.append(String.format(Locale.CHINA, "&cdmall=%.6f|%.6f", Double.valueOf(this.f / 14400.0d), Double.valueOf(this.e / 14400.0d)));
            }
            return stringBuffer.toString();
        }
    }

    public interface b {
        void a(com.baidu.location.c cVar);
    }

    /* renamed from: com.baidu.location.a.c$c, reason: collision with other inner class name */
    class C0006c extends com.baidu.location.d.e {

        /* renamed from: a, reason: collision with root package name */
        String f60a = null;

        C0006c() {
            this.k = new HashMap();
        }

        @Override // com.baidu.location.d.e
        public void a() {
            this.h = com.baidu.location.d.j.c();
            if (c.this.s != null && c.this.t != null) {
                this.f60a += String.format(Locale.CHINA, "&ki=%s&sn=%s", c.this.s, c.this.t);
            }
            String strD = Jni.d(this.f60a);
            this.f60a = null;
            this.k.put("bloc", strD);
            this.k.put("trtm", String.format(Locale.CHINA, "%d", Long.valueOf(System.currentTimeMillis())));
        }

        public void a(String str) {
            this.f60a = str;
            b(com.baidu.location.d.j.f);
        }

        @Override // com.baidu.location.d.e
        public void a(boolean z) {
            com.baidu.location.c cVar;
            if (z && this.j != null) {
                try {
                    try {
                        cVar = new com.baidu.location.c(this.j);
                    } catch (Exception e) {
                        cVar = new com.baidu.location.c();
                        cVar.d(63);
                    }
                    if (cVar != null && cVar.h() == 161) {
                        cVar.d(c.this.p.f117a);
                        cVar.c(Jni.b(c.this.f58a + ";" + c.this.b + ";" + cVar.c()));
                        c.this.q.a(cVar);
                    }
                } catch (Exception e2) {
                }
            }
            if (this.k != null) {
                this.k.clear();
            }
        }
    }

    protected class d {

        /* renamed from: a, reason: collision with root package name */
        public List<ScanResult> f61a;
        private long c;

        public d(List<ScanResult> list) {
            this.f61a = null;
            this.c = 0L;
            this.f61a = list;
            this.c = System.currentTimeMillis();
            c();
        }

        private String b() {
            WifiInfo connectionInfo = c.this.m.getConnectionInfo();
            if (connectionInfo == null) {
                return null;
            }
            try {
                String bssid = connectionInfo.getBSSID();
                String strReplace = bssid != null ? bssid.replace(":", "") : null;
                if (strReplace == null || strReplace.length() == 12) {
                    return new String(strReplace);
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }

        private void c() {
            boolean z;
            if (a() < 1) {
                return;
            }
            boolean z2 = true;
            for (int size = this.f61a.size() - 1; size >= 1 && z2; size--) {
                int i = 0;
                z2 = false;
                while (i < size) {
                    if (this.f61a.get(i).level < this.f61a.get(i + 1).level) {
                        ScanResult scanResult = this.f61a.get(i + 1);
                        this.f61a.set(i + 1, this.f61a.get(i));
                        this.f61a.set(i, scanResult);
                        z = true;
                    } else {
                        z = z2;
                    }
                    i++;
                    z2 = z;
                }
            }
        }

        public int a() {
            if (this.f61a == null) {
                return 0;
            }
            return this.f61a.size();
        }

        public String a(int i) {
            int i2;
            if (a() < 2) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer(512);
            int size = this.f61a.size();
            boolean z = true;
            int i3 = 0;
            String strB = b();
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            while (i6 < size) {
                if (this.f61a.get(i6).level == 0) {
                    i2 = i3;
                } else {
                    i4++;
                    if (z) {
                        stringBuffer.append("&wf=");
                        z = false;
                    } else {
                        stringBuffer.append(PinyinConverter.PINYIN_EXCLUDE);
                    }
                    String strReplace = this.f61a.get(i6).BSSID.replace(":", "");
                    stringBuffer.append(strReplace);
                    if (strB != null && strReplace.equals(strB)) {
                        i5 = i4;
                    }
                    int i7 = this.f61a.get(i6).level;
                    if (i7 < 0) {
                        i7 = -i7;
                    }
                    stringBuffer.append(String.format(Locale.CHINA, ";%d;", Integer.valueOf(i7)));
                    i2 = i3 + 1;
                    if (i2 > i) {
                        break;
                    }
                }
                i6++;
                i3 = i2;
            }
            if (i5 > 0) {
                stringBuffer.append("&wf_n=");
                stringBuffer.append(i5);
            }
            if (z) {
                return null;
            }
            return stringBuffer.toString();
        }
    }

    public c(Context context, com.baidu.location.h hVar, b bVar) {
        String deviceId;
        this.d = null;
        this.e = null;
        this.m = null;
        this.o = null;
        this.s = null;
        this.t = null;
        this.f58a = null;
        this.b = null;
        this.d = context.getApplicationContext();
        this.p = hVar;
        this.q = bVar;
        this.f58a = this.d.getPackageName();
        this.b = null;
        try {
            this.e = (TelephonyManager) this.d.getSystemService("phone");
            deviceId = this.e.getDeviceId();
        } catch (Exception e) {
            deviceId = null;
        }
        try {
            this.b = com.baidu.a.a.a.b.a.a(this.d);
        } catch (Exception e2) {
            this.b = null;
        }
        if (this.b != null) {
            this.o = "&prod=" + this.p.f + ":" + this.f58a + "|&cu=" + this.b + "&coor=" + hVar.a();
        } else {
            this.o = "&prod=" + this.p.f + ":" + this.f58a + "|&im=" + deviceId + "&coor=" + hVar.a();
        }
        StringBuffer stringBuffer = new StringBuffer(256);
        stringBuffer.append("&fw=");
        stringBuffer.append("7.32");
        stringBuffer.append("&lt=1");
        stringBuffer.append("&mb=");
        stringBuffer.append(Build.MODEL);
        stringBuffer.append("&resid=");
        stringBuffer.append("12");
        if (hVar.b() != null) {
        }
        if (hVar.b() != null && hVar.b().equals("all")) {
            this.o += "&addr=allj";
        }
        if (hVar.o || hVar.p) {
            this.o += "&sema=";
            if (hVar.o) {
                this.o += "aptag|";
            }
            if (hVar.p) {
                this.o += "aptagd|";
            }
            this.s = j.b(this.d);
            this.t = j.c(this.d);
        }
        stringBuffer.append("&first=1");
        stringBuffer.append("&os=A");
        stringBuffer.append(Build.VERSION.SDK);
        this.o += stringBuffer.toString();
        this.m = (WifiManager) this.d.getApplicationContext().getSystemService("wifi");
        String strA = a();
        strA = TextUtils.isEmpty(strA) ? strA : strA.replace(":", "");
        if (!TextUtils.isEmpty(strA) && !strA.equals("020000000000")) {
            this.o += "&mac=" + strA;
        }
        b();
    }

    private String a(int i2) {
        String strA;
        String strA2;
        if (i2 < 3) {
            i2 = 3;
        }
        try {
            a(this.e.getCellLocation());
            strA = this.f.a();
        } catch (Throwable th) {
            strA = null;
        }
        try {
            this.n = null;
            this.n = new d(this.m.getScanResults());
            strA2 = this.n.a(i2);
        } catch (Exception e) {
            strA2 = null;
        }
        if (strA == null && strA2 == null) {
            this.r = null;
            return null;
        }
        if (strA2 != null) {
            strA = strA + strA2;
        }
        if (strA == null) {
            return null;
        }
        this.r = strA;
        if (this.o != null) {
            this.r += this.o;
        }
        return strA + this.o;
    }

    private void a(CellLocation cellLocation) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        int i2 = 0;
        if (cellLocation == null || this.e == null) {
            return;
        }
        a aVar = new a();
        String networkOperator = this.e.getNetworkOperator();
        if (networkOperator != null && networkOperator.length() > 0) {
            try {
                if (networkOperator.length() >= 3) {
                    int iIntValue = Integer.valueOf(networkOperator.substring(0, 3)).intValue();
                    if (iIntValue < 0) {
                        iIntValue = this.f.c;
                    }
                    aVar.c = iIntValue;
                }
                String strSubstring = networkOperator.substring(3);
                if (strSubstring != null) {
                    char[] charArray = strSubstring.toCharArray();
                    while (i2 < charArray.length && Character.isDigit(charArray[i2])) {
                        i2++;
                    }
                }
                int iIntValue2 = Integer.valueOf(strSubstring.substring(0, i2)).intValue();
                if (iIntValue2 < 0) {
                    iIntValue2 = this.f.d;
                }
                aVar.d = iIntValue2;
            } catch (Exception e) {
            }
        }
        if (cellLocation instanceof GsmCellLocation) {
            aVar.f59a = ((GsmCellLocation) cellLocation).getLac();
            aVar.b = ((GsmCellLocation) cellLocation).getCid();
            aVar.g = 'g';
        } else if (cellLocation instanceof CdmaCellLocation) {
            aVar.g = 'c';
            if (l == null) {
                try {
                    l = Class.forName("android.telephony.cdma.CdmaCellLocation");
                    g = l.getMethod("getBaseStationId", new Class[0]);
                    h = l.getMethod("getNetworkId", new Class[0]);
                    i = l.getMethod("getSystemId", new Class[0]);
                    j = l.getMethod("getBaseStationLatitude", new Class[0]);
                    k = l.getMethod("getBaseStationLongitude", new Class[0]);
                } catch (Exception e2) {
                    l = null;
                    return;
                }
            }
            if (l != null && l.isInstance(cellLocation)) {
                try {
                    int iIntValue3 = ((Integer) i.invoke(cellLocation, new Object[0])).intValue();
                    if (iIntValue3 < 0) {
                        iIntValue3 = this.f.d;
                    }
                    aVar.d = iIntValue3;
                    aVar.b = ((Integer) g.invoke(cellLocation, new Object[0])).intValue();
                    aVar.f59a = ((Integer) h.invoke(cellLocation, new Object[0])).intValue();
                    Object objInvoke = j.invoke(cellLocation, new Object[0]);
                    if (((Integer) objInvoke).intValue() < Integer.MAX_VALUE) {
                        aVar.e = ((Integer) objInvoke).intValue();
                    }
                    Object objInvoke2 = k.invoke(cellLocation, new Object[0]);
                    if (((Integer) objInvoke2).intValue() < Integer.MAX_VALUE) {
                        aVar.f = ((Integer) objInvoke2).intValue();
                    }
                } catch (Exception e3) {
                    return;
                }
            }
        }
        if (aVar.b()) {
            this.f = aVar;
        } else {
            this.f = null;
        }
    }

    public String a() {
        try {
            WifiInfo connectionInfo = this.m.getConnectionInfo();
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

    public String b() {
        try {
            return a(15);
        } catch (Exception e) {
            return null;
        }
    }

    public void c() {
        if (this.r == null || this.f == null || 0 != 0) {
            return;
        }
        this.c.a(this.r);
    }
}
