package com.baidu.location.b;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.SystemClock;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import cn.yunzhisheng.common.PinyinConverter;
import com.baidu.location.d.j;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public class b {
    private TelephonyManager d = null;
    private Object e = null;
    private com.baidu.location.b.a f = new com.baidu.location.b.a();
    private com.baidu.location.b.a g = null;
    private List<com.baidu.location.b.a> h = null;
    private a i = null;
    private boolean j = false;
    private boolean q = false;
    private static b c = null;
    private static Method k = null;
    private static Method l = null;
    private static Method m = null;
    private static Method n = null;
    private static Method o = null;
    private static Class<?> p = null;

    /* renamed from: a, reason: collision with root package name */
    public static int f87a = 0;
    public static int b = 0;

    private class a extends PhoneStateListener {
        public a() {
        }

        @Override // android.telephony.PhoneStateListener
        public void onCellLocationChanged(CellLocation cellLocation) {
            if (cellLocation == null) {
                return;
            }
            try {
                b.this.k();
            } catch (Exception e) {
            }
        }

        @Override // android.telephony.PhoneStateListener
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            if (b.this.f != null) {
                if (b.this.f.i == 'g') {
                    b.this.f.h = signalStrength.getGsmSignalStrength();
                } else if (b.this.f.i == 'c') {
                    b.this.f.h = signalStrength.getCdmaDbm();
                }
            }
        }
    }

    private b() {
    }

    private int a(int i) {
        if (i == Integer.MAX_VALUE) {
            return -1;
        }
        return i;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v0 */
    /* JADX WARN: Type inference failed for: r7v1 */
    /* JADX WARN: Type inference failed for: r7v18 */
    /* JADX WARN: Type inference failed for: r7v19 */
    /* JADX WARN: Type inference failed for: r7v20 */
    private CellLocation a(List<?> list) throws ClassNotFoundException {
        ?? r7;
        CdmaCellLocation cdmaCellLocation;
        GsmCellLocation gsmCellLocation;
        if (list == null || list.isEmpty()) {
            return null;
        }
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        GsmCellLocation gsmCellLocation2 = null;
        int i = 0;
        boolean z = false;
        CdmaCellLocation cdmaCellLocation2 = null;
        while (true) {
            if (i >= list.size()) {
                r7 = z;
                cdmaCellLocation = cdmaCellLocation2;
                gsmCellLocation = gsmCellLocation2;
                break;
            }
            Object obj = list.get(i);
            if (obj != null) {
                try {
                    Class<?> clsLoadClass = systemClassLoader.loadClass("android.telephony.CellInfoGsm");
                    Class<?> clsLoadClass2 = systemClassLoader.loadClass("android.telephony.CellInfoWcdma");
                    Class<?> clsLoadClass3 = systemClassLoader.loadClass("android.telephony.CellInfoLte");
                    Class<?> clsLoadClass4 = systemClassLoader.loadClass("android.telephony.CellInfoCdma");
                    char c2 = clsLoadClass.isInstance(obj) ? (char) 1 : clsLoadClass2.isInstance(obj) ? (char) 2 : clsLoadClass3.isInstance(obj) ? (char) 3 : clsLoadClass4.isInstance(obj) ? (char) 4 : (char) 0;
                    if (c2 > 0) {
                        Object objCast = null;
                        if (c2 == 1) {
                            try {
                                objCast = clsLoadClass.cast(obj);
                            } catch (Exception e) {
                                z = c2 == true ? 1 : 0;
                            }
                        } else if (c2 == 2) {
                            objCast = clsLoadClass2.cast(obj);
                        } else if (c2 == 3) {
                            objCast = clsLoadClass3.cast(obj);
                        } else if (c2 == 4) {
                            objCast = clsLoadClass4.cast(obj);
                        }
                        Object objA = j.a(objCast, "getCellIdentity", new Object[0]);
                        if (objA == null) {
                            z = c2 == true ? 1 : 0;
                        } else if (c2 == 4) {
                            cdmaCellLocation = new CdmaCellLocation();
                            try {
                                cdmaCellLocation.setCellLocationData(j.b(objA, "getBasestationId", new Object[0]), j.b(objA, "getLatitude", new Object[0]), j.b(objA, "getLongitude", new Object[0]), j.b(objA, "getSystemId", new Object[0]), j.b(objA, "getNetworkId", new Object[0]));
                                gsmCellLocation = gsmCellLocation2;
                                r7 = c2;
                                break;
                            } catch (Exception e2) {
                                cdmaCellLocation2 = cdmaCellLocation;
                                z = c2 == true ? 1 : 0;
                            }
                        } else if (c2 == 3) {
                            int iB = j.b(objA, "getTac", new Object[0]);
                            int iB2 = j.b(objA, "getCi", new Object[0]);
                            GsmCellLocation gsmCellLocation3 = new GsmCellLocation();
                            try {
                                gsmCellLocation3.setLacAndCid(iB, iB2);
                                CdmaCellLocation cdmaCellLocation3 = cdmaCellLocation2;
                                gsmCellLocation = gsmCellLocation3;
                                cdmaCellLocation = cdmaCellLocation3;
                                r7 = c2;
                                break;
                            } catch (Exception e3) {
                                gsmCellLocation2 = gsmCellLocation3;
                                z = c2 == true ? 1 : 0;
                            }
                        } else {
                            int iB3 = j.b(objA, "getLac", new Object[0]);
                            int iB4 = j.b(objA, "getCid", new Object[0]);
                            GsmCellLocation gsmCellLocation4 = new GsmCellLocation();
                            try {
                                gsmCellLocation4.setLacAndCid(iB3, iB4);
                                CdmaCellLocation cdmaCellLocation4 = cdmaCellLocation2;
                                gsmCellLocation = gsmCellLocation4;
                                cdmaCellLocation = cdmaCellLocation4;
                                r7 = c2;
                                break;
                            } catch (Exception e4) {
                                gsmCellLocation2 = gsmCellLocation4;
                                z = c2 == true ? 1 : 0;
                            }
                        }
                    } else {
                        z = c2 == true ? 1 : 0;
                    }
                } catch (Exception e5) {
                }
            }
            i++;
            z = z;
        }
        return r7 != 4 ? gsmCellLocation : cdmaCellLocation;
    }

    @SuppressLint({"NewApi"})
    private com.baidu.location.b.a a(CellInfo cellInfo) {
        boolean z = false;
        int i = -1;
        int iIntValue = Integer.valueOf(Build.VERSION.SDK_INT).intValue();
        if (iIntValue < 17) {
            return null;
        }
        com.baidu.location.b.a aVar = new com.baidu.location.b.a();
        if (cellInfo instanceof CellInfoGsm) {
            CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
            aVar.c = a(cellIdentity.getMcc());
            aVar.d = a(cellIdentity.getMnc());
            aVar.f86a = a(cellIdentity.getLac());
            aVar.b = a(cellIdentity.getCid());
            aVar.i = 'g';
            aVar.h = ((CellInfoGsm) cellInfo).getCellSignalStrength().getAsuLevel();
            z = true;
        } else if (cellInfo instanceof CellInfoCdma) {
            CellIdentityCdma cellIdentity2 = ((CellInfoCdma) cellInfo).getCellIdentity();
            aVar.e = cellIdentity2.getLatitude();
            aVar.f = cellIdentity2.getLongitude();
            aVar.d = a(cellIdentity2.getSystemId());
            aVar.f86a = a(cellIdentity2.getNetworkId());
            aVar.b = a(cellIdentity2.getBasestationId());
            aVar.i = 'c';
            aVar.h = ((CellInfoCdma) cellInfo).getCellSignalStrength().getCdmaDbm();
            if (this.f == null || this.f.c <= 0) {
                try {
                    String networkOperator = this.d.getNetworkOperator();
                    if (networkOperator != null && networkOperator.length() > 0 && networkOperator.length() >= 3) {
                        int iIntValue2 = Integer.valueOf(networkOperator.substring(0, 3)).intValue();
                        if (iIntValue2 < 0) {
                            iIntValue2 = -1;
                        }
                        i = iIntValue2;
                    }
                } catch (Exception e) {
                }
                if (i > 0) {
                    aVar.c = i;
                }
            } else {
                aVar.c = this.f.c;
            }
            z = true;
        } else if (cellInfo instanceof CellInfoLte) {
            CellIdentityLte cellIdentity3 = ((CellInfoLte) cellInfo).getCellIdentity();
            aVar.c = a(cellIdentity3.getMcc());
            aVar.d = a(cellIdentity3.getMnc());
            aVar.f86a = a(cellIdentity3.getTac());
            aVar.b = a(cellIdentity3.getCi());
            aVar.i = 'g';
            aVar.h = ((CellInfoLte) cellInfo).getCellSignalStrength().getAsuLevel();
            z = true;
        }
        if (iIntValue >= 18 && !z) {
            try {
                if (cellInfo instanceof CellInfoWcdma) {
                    CellIdentityWcdma cellIdentity4 = ((CellInfoWcdma) cellInfo).getCellIdentity();
                    aVar.c = a(cellIdentity4.getMcc());
                    aVar.d = a(cellIdentity4.getMnc());
                    aVar.f86a = a(cellIdentity4.getLac());
                    aVar.b = a(cellIdentity4.getCid());
                    aVar.i = 'g';
                    aVar.h = ((CellInfoWcdma) cellInfo).getCellSignalStrength().getAsuLevel();
                }
            } catch (Exception e2) {
            }
        }
        try {
            aVar.g = System.currentTimeMillis() - ((SystemClock.elapsedRealtimeNanos() - cellInfo.getTimeStamp()) / C.MICROS_PER_SECOND);
        } catch (Error e3) {
            aVar.g = System.currentTimeMillis();
        }
        return aVar;
    }

    private com.baidu.location.b.a a(CellLocation cellLocation) {
        return a(cellLocation, false);
    }

    private com.baidu.location.b.a a(CellLocation cellLocation, boolean z) throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        int i = 0;
        if (cellLocation == null || this.d == null) {
            return null;
        }
        com.baidu.location.b.a aVar = new com.baidu.location.b.a();
        if (z) {
            aVar.f();
        }
        aVar.g = System.currentTimeMillis();
        try {
            String networkOperator = this.d.getNetworkOperator();
            if (networkOperator != null && networkOperator.length() > 0) {
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
                    while (i < charArray.length && Character.isDigit(charArray[i])) {
                        i++;
                    }
                }
                int iIntValue2 = Integer.valueOf(strSubstring.substring(0, i)).intValue();
                if (iIntValue2 < 0) {
                    iIntValue2 = this.f.d;
                }
                aVar.d = iIntValue2;
            }
            f87a = this.d.getSimState();
        } catch (Exception e) {
            b = 1;
        }
        if (cellLocation instanceof GsmCellLocation) {
            aVar.f86a = ((GsmCellLocation) cellLocation).getLac();
            aVar.b = ((GsmCellLocation) cellLocation).getCid();
            aVar.i = 'g';
        } else if (cellLocation instanceof CdmaCellLocation) {
            aVar.i = 'c';
            if (Integer.valueOf(Build.VERSION.SDK_INT).intValue() < 5) {
                return aVar;
            }
            if (p == null) {
                try {
                    p = Class.forName("android.telephony.cdma.CdmaCellLocation");
                    k = p.getMethod("getBaseStationId", new Class[0]);
                    l = p.getMethod("getNetworkId", new Class[0]);
                    m = p.getMethod("getSystemId", new Class[0]);
                    n = p.getMethod("getBaseStationLatitude", new Class[0]);
                    o = p.getMethod("getBaseStationLongitude", new Class[0]);
                } catch (Exception e2) {
                    p = null;
                    b = 2;
                    return aVar;
                }
            }
            if (p != null && p.isInstance(cellLocation)) {
                try {
                    int iIntValue3 = ((Integer) m.invoke(cellLocation, new Object[0])).intValue();
                    if (iIntValue3 < 0) {
                        iIntValue3 = this.f.d;
                    }
                    aVar.d = iIntValue3;
                    aVar.b = ((Integer) k.invoke(cellLocation, new Object[0])).intValue();
                    aVar.f86a = ((Integer) l.invoke(cellLocation, new Object[0])).intValue();
                    Object objInvoke = n.invoke(cellLocation, new Object[0]);
                    if (((Integer) objInvoke).intValue() < Integer.MAX_VALUE) {
                        aVar.e = ((Integer) objInvoke).intValue();
                    }
                    Object objInvoke2 = o.invoke(cellLocation, new Object[0]);
                    if (((Integer) objInvoke2).intValue() < Integer.MAX_VALUE) {
                        aVar.f = ((Integer) objInvoke2).intValue();
                    }
                } catch (Exception e3) {
                    b = 3;
                    return aVar;
                }
            }
        }
        c(aVar);
        return aVar;
    }

    public static synchronized b a() {
        if (c == null) {
            c = new b();
        }
        return c;
    }

    private void c(com.baidu.location.b.a aVar) throws IOException {
        if (aVar.b()) {
            if (this.f == null || !this.f.a(aVar)) {
                this.f = aVar;
                if (!aVar.b()) {
                    if (this.h != null) {
                        this.h.clear();
                        return;
                    }
                    return;
                }
                int size = this.h.size();
                com.baidu.location.b.a aVar2 = size == 0 ? null : this.h.get(size - 1);
                if (aVar2 != null && aVar2.b == this.f.b && aVar2.f86a == this.f.f86a) {
                    return;
                }
                this.h.add(this.f);
                if (this.h.size() > 3) {
                    this.h.remove(0);
                }
                j();
                this.q = false;
            }
        }
    }

    @SuppressLint({"NewApi"})
    private String d(com.baidu.location.b.a aVar) {
        com.baidu.location.b.a aVarA;
        StringBuilder sb = new StringBuilder();
        if (Integer.valueOf(Build.VERSION.SDK_INT).intValue() >= 17) {
            try {
                List<CellInfo> allCellInfo = this.d.getAllCellInfo();
                if (allCellInfo != null && allCellInfo.size() > 0) {
                    sb.append("&nc=");
                    for (CellInfo cellInfo : allCellInfo) {
                        if (!cellInfo.isRegistered() && (aVarA = a(cellInfo)) != null && aVarA.f86a != -1 && aVarA.b != -1) {
                            if (aVar.f86a != aVarA.f86a) {
                                sb.append(aVarA.f86a + PinyinConverter.PINYIN_EXCLUDE + aVarA.b + PinyinConverter.PINYIN_EXCLUDE + aVarA.h + ";");
                            } else {
                                sb.append(PinyinConverter.PINYIN_EXCLUDE + aVarA.b + PinyinConverter.PINYIN_EXCLUDE + aVarA.h + ";");
                            }
                        }
                    }
                }
            } catch (Throwable th) {
            }
        }
        return sb.toString();
    }

    private void i() throws IOException {
        String strG = j.g();
        if (strG == null) {
            return;
        }
        File file = new File(strG + File.separator + "lcvif.dat");
        if (file.exists()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(0L);
                if (System.currentTimeMillis() - randomAccessFile.readLong() > ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS) {
                    randomAccessFile.close();
                    file.delete();
                    return;
                }
                randomAccessFile.readInt();
                for (int i = 0; i < 3; i++) {
                    long j = randomAccessFile.readLong();
                    int i2 = randomAccessFile.readInt();
                    int i3 = randomAccessFile.readInt();
                    int i4 = randomAccessFile.readInt();
                    int i5 = randomAccessFile.readInt();
                    int i6 = randomAccessFile.readInt();
                    char c2 = i6 == 1 ? 'g' : (char) 0;
                    if (i6 == 2) {
                        c2 = 'c';
                    }
                    if (j != 0) {
                        com.baidu.location.b.a aVar = new com.baidu.location.b.a(i4, i5, i2, i3, 0, c2);
                        aVar.g = j;
                        if (aVar.b()) {
                            this.q = true;
                            this.h.add(aVar);
                        }
                    }
                }
                randomAccessFile.close();
            } catch (Exception e) {
                file.delete();
            }
        }
    }

    private void j() throws IOException {
        if (this.h == null && this.g == null) {
            return;
        }
        if (this.h == null && this.g != null) {
            this.h = new LinkedList();
            this.h.add(this.g);
        }
        String strG = j.g();
        if (strG != null) {
            File file = new File(strG + File.separator + "lcvif.dat");
            int size = this.h.size();
            try {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(0L);
                randomAccessFile.writeLong(this.h.get(size - 1).g);
                randomAccessFile.writeInt(size);
                for (int i = 0; i < 3 - size; i++) {
                    randomAccessFile.writeLong(0L);
                    randomAccessFile.writeInt(-1);
                    randomAccessFile.writeInt(-1);
                    randomAccessFile.writeInt(-1);
                    randomAccessFile.writeInt(-1);
                    randomAccessFile.writeInt(2);
                }
                for (int i2 = 0; i2 < size; i2++) {
                    randomAccessFile.writeLong(this.h.get(i2).g);
                    randomAccessFile.writeInt(this.h.get(i2).c);
                    randomAccessFile.writeInt(this.h.get(i2).d);
                    randomAccessFile.writeInt(this.h.get(i2).f86a);
                    randomAccessFile.writeInt(this.h.get(i2).b);
                    if (this.h.get(i2).i == 'g') {
                        randomAccessFile.writeInt(1);
                    } else if (this.h.get(i2).i == 'c') {
                        randomAccessFile.writeInt(2);
                    } else {
                        randomAccessFile.writeInt(3);
                    }
                }
                randomAccessFile.close();
            } catch (Exception e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() throws IllegalAccessException, IOException, IllegalArgumentException, InvocationTargetException {
        CellLocation cellLocation;
        CellLocation cellLocationL;
        com.baidu.location.b.a aVarN = n();
        if (aVarN != null) {
            c(aVarN);
        }
        if (aVarN == null || !aVarN.b()) {
            try {
                cellLocation = this.d.getCellLocation();
            } catch (Throwable th) {
                cellLocation = null;
            }
            com.baidu.location.b.a aVarA = cellLocation != null ? a(cellLocation) : null;
            if ((aVarA == null || !aVarA.b()) && (cellLocationL = l()) != null) {
                a(cellLocationL, true);
            }
        }
    }

    private CellLocation l() {
        CellLocation cellLocation;
        Object objA;
        List<?> list;
        Object obj = this.e;
        if (obj == null) {
            return null;
        }
        try {
            Class<?> clsM = m();
            if (clsM.isInstance(obj)) {
                Object objCast = clsM.cast(obj);
                try {
                    objA = j.a(objCast, "getCellLocation", new Object[0]);
                } catch (NoSuchMethodException e) {
                    objA = null;
                } catch (Exception e2) {
                    objA = null;
                }
                if (objA == null) {
                    try {
                        objA = j.a(objCast, "getCellLocation", 1);
                    } catch (NoSuchMethodException e3) {
                    } catch (Exception e4) {
                    }
                }
                if (objA == null) {
                    try {
                        objA = j.a(objCast, "getCellLocationGemini", 1);
                    } catch (NoSuchMethodException e5) {
                    } catch (Exception e6) {
                    }
                }
                if (objA == null) {
                    try {
                        list = (List) j.a(objCast, "getAllCellInfo", new Object[0]);
                    } catch (NoSuchMethodException e7) {
                        list = null;
                    } catch (Exception e8) {
                        list = null;
                    }
                    objA = a(list);
                    if (objA != null) {
                    }
                }
            } else {
                objA = null;
            }
            cellLocation = objA != null ? (CellLocation) objA : null;
        } catch (Exception e9) {
            cellLocation = null;
        }
        return cellLocation;
    }

    private Class<?> m() {
        String str;
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        switch (o()) {
            case 0:
                str = "android.telephony.TelephonyManager";
                break;
            case 1:
                str = "android.telephony.MSimTelephonyManager";
                break;
            case 2:
                str = "android.telephony.TelephonyManager2";
                break;
            default:
                str = null;
                break;
        }
        try {
            return systemClassLoader.loadClass(str);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressLint({"NewApi"})
    private com.baidu.location.b.a n() {
        com.baidu.location.b.a aVarA;
        if (Integer.valueOf(Build.VERSION.SDK_INT).intValue() < 17) {
            return null;
        }
        try {
            List<CellInfo> allCellInfo = this.d.getAllCellInfo();
            if (allCellInfo == null || allCellInfo.size() <= 0) {
                return null;
            }
            com.baidu.location.b.a aVar = null;
            for (CellInfo cellInfo : allCellInfo) {
                if (cellInfo.isRegistered()) {
                    boolean z = aVar != null;
                    aVarA = a(cellInfo);
                    if (aVarA == null) {
                        continue;
                    } else {
                        if (!aVarA.b()) {
                            aVarA = null;
                        } else if (z) {
                            aVar.j = aVarA.h();
                            return aVar;
                        }
                        if (aVar != null) {
                        }
                        aVar = aVarA;
                    }
                }
                aVarA = aVar;
                aVar = aVarA;
            }
            return aVar;
        } catch (Throwable th) {
            return null;
        }
    }

    private int o() throws ClassNotFoundException {
        int i = 0;
        try {
            Class.forName("android.telephony.MSimTelephonyManager");
            i = 1;
        } catch (Exception e) {
        }
        if (i != 0) {
            return i;
        }
        try {
            Class.forName("android.telephony.TelephonyManager2");
            return 2;
        } catch (Exception e2) {
            return i;
        }
    }

    public String a(com.baidu.location.b.a aVar) {
        String strD;
        int iIntValue;
        try {
            strD = d(aVar);
            iIntValue = Integer.valueOf(Build.VERSION.SDK_INT).intValue();
        } catch (Throwable th) {
            th.printStackTrace();
            strD = "";
        }
        if ((strD != null && !strD.equals("") && !strD.equals("&nc=")) || iIntValue >= 17) {
            return strD;
        }
        List neighboringCellInfo = this.d.getNeighboringCellInfo();
        if (neighboringCellInfo != null && !neighboringCellInfo.isEmpty()) {
            String str = "&nc=";
            Iterator it = neighboringCellInfo.iterator();
            int i = 0;
            while (true) {
                if (!it.hasNext()) {
                    strD = str;
                    break;
                }
                NeighboringCellInfo neighboringCellInfo2 = (NeighboringCellInfo) it.next();
                int lac = neighboringCellInfo2.getLac();
                strD = (lac == -1 || neighboringCellInfo2.getCid() == -1) ? str : aVar.f86a != lac ? str + lac + PinyinConverter.PINYIN_EXCLUDE + neighboringCellInfo2.getCid() + PinyinConverter.PINYIN_EXCLUDE + neighboringCellInfo2.getRssi() + ";" : str + PinyinConverter.PINYIN_EXCLUDE + neighboringCellInfo2.getCid() + PinyinConverter.PINYIN_EXCLUDE + neighboringCellInfo2.getRssi() + ";";
                int i2 = i + 1;
                if (i2 >= 8) {
                    break;
                }
                i = i2;
                str = strD;
            }
        }
        if (strD == null || !strD.equals("&nc=")) {
            return strD;
        }
        return null;
    }

    public String b(com.baidu.location.b.a aVar) {
        StringBuffer stringBuffer = new StringBuffer(128);
        stringBuffer.append("&nw=");
        stringBuffer.append(aVar.i);
        stringBuffer.append(String.format(Locale.CHINA, "&cl=%d|%d|%d|%d&cl_s=%d", Integer.valueOf(aVar.c), Integer.valueOf(aVar.d), Integer.valueOf(aVar.f86a), Integer.valueOf(aVar.b), Integer.valueOf(aVar.h)));
        if (aVar.e < Integer.MAX_VALUE && aVar.f < Integer.MAX_VALUE) {
            stringBuffer.append(String.format(Locale.CHINA, "&cdmall=%.6f|%.6f", Double.valueOf(aVar.f / 14400.0d), Double.valueOf(aVar.e / 14400.0d)));
        }
        stringBuffer.append("&cl_t=");
        stringBuffer.append(aVar.g);
        if (this.h != null && this.h.size() > 0) {
            int size = this.h.size();
            stringBuffer.append("&clt=");
            for (int i = 0; i < size; i++) {
                com.baidu.location.b.a aVar2 = this.h.get(i);
                if (aVar2.c != aVar.c) {
                    stringBuffer.append(aVar2.c);
                }
                stringBuffer.append(PinyinConverter.PINYIN_EXCLUDE);
                if (aVar2.d != aVar.d) {
                    stringBuffer.append(aVar2.d);
                }
                stringBuffer.append(PinyinConverter.PINYIN_EXCLUDE);
                if (aVar2.f86a != aVar.f86a) {
                    stringBuffer.append(aVar2.f86a);
                }
                stringBuffer.append(PinyinConverter.PINYIN_EXCLUDE);
                if (aVar2.b != aVar.b) {
                    stringBuffer.append(aVar2.b);
                }
                stringBuffer.append(PinyinConverter.PINYIN_EXCLUDE);
                stringBuffer.append((System.currentTimeMillis() - aVar2.g) / 1000);
                stringBuffer.append(";");
            }
        }
        if (f87a > 100) {
            f87a = 0;
        }
        stringBuffer.append("&cs=" + ((b << 8) + f87a));
        if (aVar.j != null) {
            stringBuffer.append(aVar.j);
        }
        return stringBuffer.toString();
    }

    public synchronized void b() {
        if (!this.j && com.baidu.location.f.f) {
            this.d = (TelephonyManager) com.baidu.location.f.b().getSystemService("phone");
            this.h = new LinkedList();
            this.i = new a();
            i();
            if (this.d != null && this.i != null) {
                try {
                    this.d.listen(this.i, 272);
                } catch (Exception e) {
                }
                try {
                    switch (o()) {
                        case 0:
                            this.e = j.a(com.baidu.location.f.b(), "phone2");
                            break;
                        case 1:
                            this.e = j.a(com.baidu.location.f.b(), "phone_msim");
                            break;
                        case 2:
                            this.e = j.a(com.baidu.location.f.b(), "phone2");
                            break;
                    }
                } catch (Throwable th) {
                    this.e = null;
                }
                this.j = true;
            }
        }
    }

    public synchronized void c() {
        if (this.j) {
            if (this.i != null && this.d != null) {
                this.d.listen(this.i, 0);
            }
            this.i = null;
            this.d = null;
            this.h.clear();
            this.h = null;
            j();
            this.j = false;
        }
    }

    public boolean d() {
        return this.q;
    }

    public int e() {
        if (this.d == null) {
            return 0;
        }
        try {
            return this.d.getNetworkType();
        } catch (Exception e) {
            return 0;
        }
    }

    public com.baidu.location.b.a f() {
        if ((this.f == null || !this.f.a() || !this.f.b()) && this.d != null) {
            try {
                k();
            } catch (Exception e) {
            }
        }
        if (this.f.e()) {
            this.g = null;
            this.g = new com.baidu.location.b.a(this.f.f86a, this.f.b, this.f.c, this.f.d, this.f.h, this.f.i);
        }
        if (this.f.d() && this.g != null && this.f.i == 'g') {
            this.f.d = this.g.d;
            this.f.c = this.g.c;
        }
        return this.f;
    }

    public String g() {
        int simState = -1;
        try {
            if (this.d != null) {
                simState = this.d.getSimState();
            }
        } catch (Exception e) {
        }
        return "&sim=" + simState;
    }

    public int h() {
        String subscriberId;
        try {
            subscriberId = ((TelephonyManager) com.baidu.location.f.b().getSystemService("phone")).getSubscriberId();
        } catch (Exception e) {
            subscriberId = null;
        }
        if (subscriberId != null) {
            if (subscriberId.startsWith("46000") || subscriberId.startsWith("46002") || subscriberId.startsWith("46007")) {
                return 1;
            }
            if (subscriberId.startsWith("46001")) {
                return 2;
            }
            if (subscriberId.startsWith("46003")) {
                return 3;
            }
        }
        return 0;
    }
}
