package com.tencent.bugly.crashreport.common.info;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import cn.yunzhisheng.asr.JniUscClient;
import cn.yunzhisheng.common.PinyinConverter;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import com.unisound.client.SpeechConstants;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class b {

    /* renamed from: a, reason: collision with root package name */
    private static String f142a = null;
    private static String b = null;

    public static String a() {
        try {
            return Build.MODEL;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return "fail";
        }
    }

    public static String b() {
        try {
            return Build.VERSION.RELEASE;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return "fail";
        }
    }

    public static int c() {
        try {
            return Build.VERSION.SDK_INT;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return -1;
        }
    }

    public static String a(Context context) {
        String deviceId;
        if (context == null) {
            return null;
        }
        if (!AppInfo.a(context, SpeechConstants.PERMISSION_READ_PHONE_STATE)) {
            x.d("no READ_PHONE_STATE permission to get IMEI", new Object[0]);
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                deviceId = telephonyManager.getDeviceId();
                if (deviceId != null) {
                    try {
                        deviceId = deviceId.toLowerCase();
                    } catch (Throwable th) {
                        x.a("Failed to get IMEI.", new Object[0]);
                        return deviceId;
                    }
                }
            } else {
                deviceId = null;
            }
        } catch (Throwable th2) {
            deviceId = null;
        }
        return deviceId;
    }

    public static String b(Context context) {
        String subscriberId;
        if (context == null) {
            return null;
        }
        if (!AppInfo.a(context, SpeechConstants.PERMISSION_READ_PHONE_STATE)) {
            x.d("no READ_PHONE_STATE permission to get IMSI", new Object[0]);
            return null;
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager != null) {
                subscriberId = telephonyManager.getSubscriberId();
                if (subscriberId != null) {
                    try {
                        subscriberId = subscriberId.toLowerCase();
                    } catch (Throwable th) {
                        x.a("Failed to get IMSI.", new Object[0]);
                        return subscriberId;
                    }
                }
            } else {
                subscriberId = null;
            }
        } catch (Throwable th2) {
            subscriberId = null;
        }
        return subscriberId;
    }

    public static String c(Context context) {
        String str = "fail";
        if (context == null) {
            return "fail";
        }
        try {
            String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
            if (string == null) {
                return JniUscClient.az;
            }
            try {
                return string.toLowerCase();
            } catch (Throwable th) {
                str = string;
                th = th;
                if (!x.a(th)) {
                    x.a("Failed to get Android ID.", new Object[0]);
                    return str;
                }
                return str;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0025 A[Catch: Throwable -> 0x0072, TryCatch #0 {Throwable -> 0x0072, blocks: (B:11:0x001d, B:13:0x0025, B:15:0x003c, B:17:0x0044, B:19:0x004c), top: B:30:0x001d }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0056  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String d(android.content.Context r8) {
        /*
            r6 = 1
            r5 = 0
            java.lang.String r1 = "fail"
            if (r8 != 0) goto L7
        L6:
            return r1
        L7:
            java.lang.String r0 = "wifi"
            java.lang.Object r0 = r8.getSystemService(r0)     // Catch: java.lang.Throwable -> L66
            android.net.wifi.WifiManager r0 = (android.net.wifi.WifiManager) r0     // Catch: java.lang.Throwable -> L66
            if (r0 == 0) goto L70
            android.net.wifi.WifiInfo r0 = r0.getConnectionInfo()     // Catch: java.lang.Throwable -> L66
            if (r0 == 0) goto L70
            java.lang.String r0 = r0.getMacAddress()     // Catch: java.lang.Throwable -> L66
            if (r0 == 0) goto L25
            java.lang.String r1 = "02:00:00:00:00:00"
            boolean r1 = r0.equals(r1)     // Catch: java.lang.Throwable -> L72
            if (r1 == 0) goto L54
        L25:
            java.lang.String r1 = "wifi.interface"
            java.lang.String r1 = com.tencent.bugly.proguard.z.a(r8, r1)     // Catch: java.lang.Throwable -> L72
            java.lang.String r2 = "MAC interface: %s"
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> L72
            r4 = 0
            r3[r4] = r1     // Catch: java.lang.Throwable -> L72
            com.tencent.bugly.proguard.x.c(r2, r3)     // Catch: java.lang.Throwable -> L72
            java.net.NetworkInterface r1 = java.net.NetworkInterface.getByName(r1)     // Catch: java.lang.Throwable -> L72
            if (r1 != 0) goto L42
            java.lang.String r1 = "wlan0"
            java.net.NetworkInterface r1 = java.net.NetworkInterface.getByName(r1)     // Catch: java.lang.Throwable -> L72
        L42:
            if (r1 != 0) goto L4a
            java.lang.String r1 = "eth0"
            java.net.NetworkInterface r1 = java.net.NetworkInterface.getByName(r1)     // Catch: java.lang.Throwable -> L72
        L4a:
            if (r1 == 0) goto L54
            byte[] r1 = r1.getHardwareAddress()     // Catch: java.lang.Throwable -> L72
            java.lang.String r0 = com.tencent.bugly.proguard.z.d(r1)     // Catch: java.lang.Throwable -> L72
        L54:
            if (r0 != 0) goto L58
            java.lang.String r0 = "null"
        L58:
            java.lang.String r1 = "MAC address: %s"
            java.lang.Object[] r2 = new java.lang.Object[r6]
            r2[r5] = r0
            com.tencent.bugly.proguard.x.c(r1, r2)
            java.lang.String r1 = r0.toLowerCase()
            goto L6
        L66:
            r0 = move-exception
        L67:
            boolean r2 = com.tencent.bugly.proguard.x.a(r0)
            if (r2 != 0) goto L70
            r0.printStackTrace()
        L70:
            r0 = r1
            goto L54
        L72:
            r1 = move-exception
            r7 = r1
            r1 = r0
            r0 = r7
            goto L67
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.d(android.content.Context):java.lang.String");
    }

    /* JADX WARN: Unreachable blocks removed: 1, instructions: 1 */
    public static String e(Context context) {
        String simSerialNumber;
        if (context == null) {
            return "fail";
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
            if (telephonyManager == null) {
                simSerialNumber = "fail";
            } else {
                simSerialNumber = telephonyManager.getSimSerialNumber();
                if (simSerialNumber == null) {
                    simSerialNumber = JniUscClient.az;
                }
            }
        } catch (Throwable th) {
            simSerialNumber = "fail";
            x.a("Failed to get SIM serial number.", new Object[0]);
        }
        return simSerialNumber;
    }

    public static String d() {
        try {
            return Build.SERIAL;
        } catch (Throwable th) {
            x.a("Failed to get hardware serial number.", new Object[0]);
            return "fail";
        }
    }

    private static boolean p() {
        try {
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
        }
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:78:0x0098 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:85:0x009d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String q() throws java.lang.Throwable {
        /*
            r6 = 2
            r1 = 0
            java.lang.String r0 = "/system/build.prop"
            java.io.FileReader r3 = new java.io.FileReader     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L93
            r3.<init>(r0)     // Catch: java.lang.Throwable -> L65 java.lang.Throwable -> L93
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> Lb7 java.lang.Throwable -> Lbc
            r0 = 2048(0x800, float:2.87E-42)
            r2.<init>(r3, r0)     // Catch: java.lang.Throwable -> Lb7 java.lang.Throwable -> Lbc
        L10:
            java.lang.String r0 = r2.readLine()     // Catch: java.lang.Throwable -> Lba java.lang.Throwable -> Lbf
            if (r0 == 0) goto Lc1
            java.lang.String r4 = "="
            r5 = 2
            java.lang.String[] r0 = r0.split(r4, r5)     // Catch: java.lang.Throwable -> Lba java.lang.Throwable -> Lbf
            int r4 = r0.length     // Catch: java.lang.Throwable -> Lba java.lang.Throwable -> Lbf
            if (r4 != r6) goto L10
            r4 = 0
            r4 = r0[r4]     // Catch: java.lang.Throwable -> Lba java.lang.Throwable -> Lbf
            java.lang.String r5 = "ro.product.cpu.abilist"
            boolean r4 = r4.equals(r5)     // Catch: java.lang.Throwable -> Lba java.lang.Throwable -> Lbf
            if (r4 == 0) goto L40
            r4 = 1
            r0 = r0[r4]     // Catch: java.lang.Throwable -> Lba java.lang.Throwable -> Lbf
        L2e:
            if (r0 == 0) goto L39
            java.lang.String r4 = ","
            java.lang.String[] r0 = r0.split(r4)     // Catch: java.lang.Throwable -> Lba java.lang.Throwable -> Lbf
            r4 = 0
            r0 = r0[r4]     // Catch: java.lang.Throwable -> Lba java.lang.Throwable -> Lbf
        L39:
            r2.close()     // Catch: java.io.IOException -> L4f
        L3c:
            r3.close()     // Catch: java.io.IOException -> L5a
        L3f:
            return r0
        L40:
            r4 = 0
            r4 = r0[r4]     // Catch: java.lang.Throwable -> Lba java.lang.Throwable -> Lbf
            java.lang.String r5 = "ro.product.cpu.abi"
            boolean r4 = r4.equals(r5)     // Catch: java.lang.Throwable -> Lba java.lang.Throwable -> Lbf
            if (r4 == 0) goto L10
            r4 = 1
            r0 = r0[r4]     // Catch: java.lang.Throwable -> Lba java.lang.Throwable -> Lbf
            goto L2e
        L4f:
            r1 = move-exception
            boolean r2 = com.tencent.bugly.proguard.x.a(r1)
            if (r2 != 0) goto L3c
            r1.printStackTrace()
            goto L3c
        L5a:
            r1 = move-exception
            boolean r2 = com.tencent.bugly.proguard.x.a(r1)
            if (r2 != 0) goto L3f
            r1.printStackTrace()
            goto L3f
        L65:
            r0 = move-exception
            r2 = r1
            r3 = r1
        L68:
            boolean r4 = com.tencent.bugly.proguard.x.a(r0)     // Catch: java.lang.Throwable -> Lba
            if (r4 != 0) goto L71
            r0.printStackTrace()     // Catch: java.lang.Throwable -> Lba
        L71:
            if (r2 == 0) goto L76
            r2.close()     // Catch: java.io.IOException -> L7d
        L76:
            if (r3 == 0) goto L7b
            r3.close()     // Catch: java.io.IOException -> L88
        L7b:
            r0 = r1
            goto L3f
        L7d:
            r0 = move-exception
            boolean r2 = com.tencent.bugly.proguard.x.a(r0)
            if (r2 != 0) goto L76
            r0.printStackTrace()
            goto L76
        L88:
            r0 = move-exception
            boolean r2 = com.tencent.bugly.proguard.x.a(r0)
            if (r2 != 0) goto L7b
            r0.printStackTrace()
            goto L7b
        L93:
            r0 = move-exception
            r2 = r1
            r3 = r1
        L96:
            if (r2 == 0) goto L9b
            r2.close()     // Catch: java.io.IOException -> La1
        L9b:
            if (r3 == 0) goto La0
            r3.close()     // Catch: java.io.IOException -> Lac
        La0:
            throw r0
        La1:
            r1 = move-exception
            boolean r2 = com.tencent.bugly.proguard.x.a(r1)
            if (r2 != 0) goto L9b
            r1.printStackTrace()
            goto L9b
        Lac:
            r1 = move-exception
            boolean r2 = com.tencent.bugly.proguard.x.a(r1)
            if (r2 != 0) goto La0
            r1.printStackTrace()
            goto La0
        Lb7:
            r0 = move-exception
            r2 = r1
            goto L96
        Lba:
            r0 = move-exception
            goto L96
        Lbc:
            r0 = move-exception
            r2 = r1
            goto L68
        Lbf:
            r0 = move-exception
            goto L68
        Lc1:
            r0 = r1
            goto L2e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.q():java.lang.String");
    }

    public static String a(boolean z) {
        String strQ = null;
        if (z) {
            try {
                strQ = q();
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                return "fail";
            }
        }
        if (strQ == null) {
            strQ = System.getProperty("os.arch");
        }
        return strQ;
    }

    public static long e() {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return statFs.getBlockCount() * statFs.getBlockSize();
        } catch (Throwable th) {
            if (x.a(th)) {
                return -1L;
            }
            th.printStackTrace();
            return -1L;
        }
    }

    public static long f() {
        try {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
            return statFs.getAvailableBlocks() * statFs.getBlockSize();
        } catch (Throwable th) {
            if (x.a(th)) {
                return -1L;
            }
            th.printStackTrace();
            return -1L;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:75:0x00aa A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x00a5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long g() throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 212
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.g():long");
    }

    /* JADX WARN: Removed duplicated region for block: B:112:0x013b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0136 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long h() throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 358
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.h():long");
    }

    public static long i() {
        if (!p()) {
            return 0L;
        }
        try {
            return new StatFs(Environment.getExternalStorageDirectory().getPath()).getBlockSize() * r0.getBlockCount();
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return -2L;
        }
    }

    public static long j() {
        if (!p()) {
            return 0L;
        }
        try {
            return new StatFs(Environment.getExternalStorageDirectory().getPath()).getBlockSize() * r0.getAvailableBlocks();
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return -2L;
        }
    }

    public static String k() {
        try {
            return Locale.getDefault().getCountry();
        } catch (Throwable th) {
            if (x.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    public static String l() {
        try {
            return Build.BRAND;
        } catch (Throwable th) {
            if (x.a(th)) {
                return "fail";
            }
            th.printStackTrace();
            return "fail";
        }
    }

    public static String f(Context context) {
        String str;
        TelephonyManager telephonyManager;
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return null;
            }
            if (activeNetworkInfo.getType() == 1) {
                return "WIFI";
            }
            if (activeNetworkInfo.getType() != 0 || (telephonyManager = (TelephonyManager) context.getSystemService("phone")) == null) {
                str = "unknown";
            } else {
                int networkType = telephonyManager.getNetworkType();
                switch (networkType) {
                    case 1:
                        return "GPRS";
                    case 2:
                        return "EDGE";
                    case 3:
                        return "UMTS";
                    case 4:
                        return "CDMA";
                    case 5:
                        return "EVDO_0";
                    case 6:
                        return "EVDO_A";
                    case 7:
                        return "1xRTT";
                    case 8:
                        return "HSDPA";
                    case 9:
                        return "HSUPA";
                    case 10:
                        return "HSPA";
                    case 11:
                        return "iDen";
                    case 12:
                        return "EVDO_B";
                    case 13:
                        return "LTE";
                    case 14:
                        return "eHRPD";
                    case 15:
                        return "HSPA+";
                    default:
                        str = "MOBILE(" + networkType + ")";
                        break;
                }
            }
            return str;
        } catch (Exception e) {
            if (x.a(e)) {
                return "unknown";
            }
            e.printStackTrace();
            return "unknown";
        }
    }

    public static String g(Context context) throws Throwable {
        String strA = z.a(context, "ro.miui.ui.version.name");
        if (!z.a(strA) && !strA.equals("fail")) {
            return "XiaoMi/MIUI/" + strA;
        }
        String strA2 = z.a(context, "ro.build.version.emui");
        if (!z.a(strA2) && !strA2.equals("fail")) {
            return "HuaWei/EMOTION/" + strA2;
        }
        String strA3 = z.a(context, "ro.lenovo.series");
        if (!z.a(strA3) && !strA3.equals("fail")) {
            return "Lenovo/VIBE/" + z.a(context, "ro.build.version.incremental");
        }
        String strA4 = z.a(context, "ro.build.nubia.rom.name");
        if (!z.a(strA4) && !strA4.equals("fail")) {
            return "Zte/NUBIA/" + strA4 + "_" + z.a(context, "ro.build.nubia.rom.code");
        }
        String strA5 = z.a(context, "ro.meizu.product.model");
        if (!z.a(strA5) && !strA5.equals("fail")) {
            return "Meizu/FLYME/" + z.a(context, "ro.build.display.id");
        }
        String strA6 = z.a(context, "ro.build.version.opporom");
        if (!z.a(strA6) && !strA6.equals("fail")) {
            return "Oppo/COLOROS/" + strA6;
        }
        String strA7 = z.a(context, "ro.vivo.os.build.display.id");
        if (!z.a(strA7) && !strA7.equals("fail")) {
            return "vivo/FUNTOUCH/" + strA7;
        }
        String strA8 = z.a(context, "ro.aa.romver");
        if (!z.a(strA8) && !strA8.equals("fail")) {
            return "htc/" + strA8 + MqttTopic.TOPIC_LEVEL_SEPARATOR + z.a(context, "ro.build.description");
        }
        String strA9 = z.a(context, "ro.lewa.version");
        if (!z.a(strA9) && !strA9.equals("fail")) {
            return "tcl/" + strA9 + MqttTopic.TOPIC_LEVEL_SEPARATOR + z.a(context, "ro.build.display.id");
        }
        String strA10 = z.a(context, "ro.gn.gnromvernumber");
        if (!z.a(strA10) && !strA10.equals("fail")) {
            return "amigo/" + strA10 + MqttTopic.TOPIC_LEVEL_SEPARATOR + z.a(context, "ro.build.display.id");
        }
        String strA11 = z.a(context, "ro.build.tyd.kbstyle_version");
        if (!z.a(strA11) && !strA11.equals("fail")) {
            return "dido/" + strA11;
        }
        return z.a(context, "ro.build.fingerprint") + MqttTopic.TOPIC_LEVEL_SEPARATOR + z.a(context, "ro.build.rom.id");
    }

    public static String h(Context context) {
        return z.a(context, "ro.board.platform");
    }

    public static boolean i(Context context) throws Throwable {
        boolean zExists;
        try {
            zExists = new File("/system/app/Superuser.apk").exists();
        } catch (Throwable th) {
            if (!x.b(th)) {
                th.printStackTrace();
            }
            zExists = false;
        }
        Boolean bool = null;
        ArrayList<String> arrayListA = z.a(context, new String[]{"/system/bin/sh", "-c", "type su"});
        if (arrayListA != null && arrayListA.size() > 0) {
            Iterator<String> it = arrayListA.iterator();
            while (it.hasNext()) {
                String next = it.next();
                x.c(next, new Object[0]);
                bool = next.contains("not found") ? false : bool;
            }
            if (bool == null) {
                bool = true;
            }
        }
        return (Build.TAGS != null && Build.TAGS.contains("test-keys")) || zExists || Boolean.valueOf(bool == null ? false : bool.booleanValue()).booleanValue();
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0069 A[Catch: all -> 0x00ab, Throwable -> 0x00bb, TRY_LEAVE, TryCatch #1 {Throwable -> 0x00bb, blocks: (B:19:0x0057, B:21:0x0069), top: B:65:0x0057 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x0084 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0091 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x009f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String m() throws java.lang.Throwable {
        /*
            r0 = 0
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L8d java.lang.Throwable -> L9a
            r3.<init>()     // Catch: java.lang.Throwable -> L8d java.lang.Throwable -> L9a
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> L8d java.lang.Throwable -> L9a
            java.lang.String r2 = "/sys/block/mmcblk0/device/type"
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L8d java.lang.Throwable -> L9a
            boolean r1 = r1.exists()     // Catch: java.lang.Throwable -> L8d java.lang.Throwable -> L9a
            if (r1 == 0) goto Lc0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L8d java.lang.Throwable -> L9a
            java.io.FileReader r2 = new java.io.FileReader     // Catch: java.lang.Throwable -> L8d java.lang.Throwable -> L9a
            java.lang.String r4 = "/sys/block/mmcblk0/device/type"
            r2.<init>(r4)     // Catch: java.lang.Throwable -> L8d java.lang.Throwable -> L9a
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L8d java.lang.Throwable -> L9a
            java.lang.String r2 = r1.readLine()     // Catch: java.lang.Throwable -> La8 java.lang.Throwable -> Lb6
            if (r2 == 0) goto L28
            r3.append(r2)     // Catch: java.lang.Throwable -> La8 java.lang.Throwable -> Lb6
        L28:
            r1.close()     // Catch: java.lang.Throwable -> La8 java.lang.Throwable -> Lb6
            r2 = r1
        L2c:
            java.lang.String r1 = ","
            r3.append(r1)     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lb8
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lb8
            java.lang.String r4 = "/sys/block/mmcblk0/device/name"
            r1.<init>(r4)     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lb8
            boolean r1 = r1.exists()     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lb8
            if (r1 == 0) goto L57
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lb8
            java.io.FileReader r4 = new java.io.FileReader     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lb8
            java.lang.String r5 = "/sys/block/mmcblk0/device/name"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lb8
            r1.<init>(r4)     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lb8
            java.lang.String r2 = r1.readLine()     // Catch: java.lang.Throwable -> Lad java.lang.Throwable -> Lb6
            if (r2 == 0) goto L53
            r3.append(r2)     // Catch: java.lang.Throwable -> Lad java.lang.Throwable -> Lb6
        L53:
            r1.close()     // Catch: java.lang.Throwable -> Lad java.lang.Throwable -> Lb6
            r2 = r1
        L57:
            java.lang.String r1 = ","
            r3.append(r1)     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lbb
            java.io.File r1 = new java.io.File     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lbb
            java.lang.String r4 = "/sys/block/mmcblk0/device/cid"
            r1.<init>(r4)     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lbb
            boolean r1 = r1.exists()     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lbb
            if (r1 == 0) goto Lbe
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lbb
            java.io.FileReader r4 = new java.io.FileReader     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lbb
            java.lang.String r5 = "/sys/block/mmcblk0/device/cid"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lbb
            r1.<init>(r4)     // Catch: java.lang.Throwable -> Lab java.lang.Throwable -> Lbb
            java.lang.String r2 = r1.readLine()     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Lb6
            if (r2 == 0) goto L7e
            r3.append(r2)     // Catch: java.lang.Throwable -> Lb0 java.lang.Throwable -> Lb6
        L7e:
            java.lang.String r0 = r3.toString()     // Catch: java.lang.Throwable -> Lb3 java.lang.Throwable -> Lb6
            if (r1 == 0) goto L87
            r1.close()     // Catch: java.io.IOException -> L88
        L87:
            return r0
        L88:
            r1 = move-exception
            com.tencent.bugly.proguard.x.a(r1)
            goto L87
        L8d:
            r1 = move-exception
            r1 = r0
        L8f:
            if (r1 == 0) goto L87
            r1.close()     // Catch: java.io.IOException -> L95
            goto L87
        L95:
            r1 = move-exception
            com.tencent.bugly.proguard.x.a(r1)
            goto L87
        L9a:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L9d:
            if (r2 == 0) goto La2
            r2.close()     // Catch: java.io.IOException -> La3
        La2:
            throw r0
        La3:
            r1 = move-exception
            com.tencent.bugly.proguard.x.a(r1)
            goto La2
        La8:
            r0 = move-exception
            r2 = r1
            goto L9d
        Lab:
            r0 = move-exception
            goto L9d
        Lad:
            r0 = move-exception
            r2 = r1
            goto L9d
        Lb0:
            r0 = move-exception
            r2 = r1
            goto L9d
        Lb3:
            r0 = move-exception
            r2 = r1
            goto L9d
        Lb6:
            r2 = move-exception
            goto L8f
        Lb8:
            r1 = move-exception
            r1 = r2
            goto L8f
        Lbb:
            r1 = move-exception
            r1 = r2
            goto L8f
        Lbe:
            r1 = r2
            goto L7e
        Lc0:
            r2 = r0
            goto L2c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.m():java.lang.String");
    }

    public static String j(Context context) throws Throwable {
        StringBuilder sb = new StringBuilder();
        String strA = z.a(context, "ro.genymotion.version");
        if (strA != null) {
            sb.append("ro.genymotion.version");
            sb.append(PinyinConverter.PINYIN_EXCLUDE);
            sb.append(strA);
            sb.append("\n");
        }
        String strA2 = z.a(context, "androVM.vbox_dpi");
        if (strA2 != null) {
            sb.append("androVM.vbox_dpi");
            sb.append(PinyinConverter.PINYIN_EXCLUDE);
            sb.append(strA2);
            sb.append("\n");
        }
        String strA3 = z.a(context, "qemu.sf.fake_camera");
        if (strA3 != null) {
            sb.append("qemu.sf.fake_camera");
            sb.append(PinyinConverter.PINYIN_EXCLUDE);
            sb.append(strA3);
        }
        return sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:48:0x00a6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String k(android.content.Context r5) throws java.lang.Throwable {
        /*
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r0 = com.tencent.bugly.crashreport.common.info.b.f142a
            if (r0 != 0) goto L11
            java.lang.String r0 = "ro.secure"
            java.lang.String r0 = com.tencent.bugly.proguard.z.a(r5, r0)
            com.tencent.bugly.crashreport.common.info.b.f142a = r0
        L11:
            java.lang.String r0 = com.tencent.bugly.crashreport.common.info.b.f142a
            if (r0 == 0) goto L29
            java.lang.String r0 = "ro.secure"
            r3.append(r0)
            java.lang.String r0 = "|"
            r3.append(r0)
            java.lang.String r0 = com.tencent.bugly.crashreport.common.info.b.f142a
            r3.append(r0)
            java.lang.String r0 = "\n"
            r3.append(r0)
        L29:
            java.lang.String r0 = com.tencent.bugly.crashreport.common.info.b.b
            if (r0 != 0) goto L35
            java.lang.String r0 = "ro.debuggable"
            java.lang.String r0 = com.tencent.bugly.proguard.z.a(r5, r0)
            com.tencent.bugly.crashreport.common.info.b.b = r0
        L35:
            java.lang.String r0 = com.tencent.bugly.crashreport.common.info.b.b
            if (r0 == 0) goto L4d
            java.lang.String r0 = "ro.debuggable"
            r3.append(r0)
            java.lang.String r0 = "|"
            r3.append(r0)
            java.lang.String r0 = com.tencent.bugly.crashreport.common.info.b.b
            r3.append(r0)
            java.lang.String r0 = "\n"
            r3.append(r0)
        L4d:
            r2 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> La2
            java.io.FileReader r0 = new java.io.FileReader     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> La2
            java.lang.String r4 = "/proc/self/status"
            r0.<init>(r4)     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> La2
            r1.<init>(r0)     // Catch: java.lang.Throwable -> L8e java.lang.Throwable -> La2
        L5a:
            java.lang.String r0 = r1.readLine()     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lb1
            if (r0 == 0) goto L68
            java.lang.String r2 = "TracerPid:"
            boolean r2 = r0.startsWith(r2)     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lb1
            if (r2 == 0) goto L5a
        L68:
            if (r0 == 0) goto L81
            r2 = 10
            java.lang.String r0 = r0.substring(r2)     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lb1
            java.lang.String r0 = r0.trim()     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lb1
            java.lang.String r2 = "tracer_pid"
            r3.append(r2)     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lb1
            java.lang.String r2 = "|"
            r3.append(r2)     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lb1
            r3.append(r0)     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lb1
        L81:
            java.lang.String r0 = r3.toString()     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lb1
            r1.close()     // Catch: java.io.IOException -> L89
        L88:
            return r0
        L89:
            r1 = move-exception
            com.tencent.bugly.proguard.x.a(r1)
            goto L88
        L8e:
            r0 = move-exception
            r1 = r2
        L90:
            com.tencent.bugly.proguard.x.a(r0)     // Catch: java.lang.Throwable -> Laf
            if (r1 == 0) goto L98
            r1.close()     // Catch: java.io.IOException -> L9d
        L98:
            java.lang.String r0 = r3.toString()
            goto L88
        L9d:
            r0 = move-exception
            com.tencent.bugly.proguard.x.a(r0)
            goto L98
        La2:
            r0 = move-exception
            r1 = r2
        La4:
            if (r1 == 0) goto La9
            r1.close()     // Catch: java.io.IOException -> Laa
        La9:
            throw r0
        Laa:
            r1 = move-exception
            com.tencent.bugly.proguard.x.a(r1)
            goto La9
        Laf:
            r0 = move-exception
            goto La4
        Lb1:
            r0 = move-exception
            goto L90
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.k(android.content.Context):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0048 A[Catch: all -> 0x00bb, Throwable -> 0x00d8, TRY_LEAVE, TryCatch #7 {all -> 0x00bb, blocks: (B:3:0x0006, B:5:0x0013, B:11:0x0036, B:13:0x0048, B:19:0x006b, B:21:0x007d), top: B:66:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x007d A[Catch: all -> 0x00bb, Throwable -> 0x00db, TRY_LEAVE, TryCatch #1 {Throwable -> 0x00db, blocks: (B:19:0x006b, B:21:0x007d), top: B:60:0x006b }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00de  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00a1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x006b A[EXC_TOP_SPLITTER, PHI: r1
  0x006b: PHI (r1v5 java.io.BufferedReader) = (r1v4 java.io.BufferedReader), (r1v12 java.io.BufferedReader) binds: [B:12:0x0046, B:18:0x006a] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00b2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x00be A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String n() throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 224
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.n():java.lang.String");
    }

    public static String l(Context context) throws Throwable {
        StringBuilder sb = new StringBuilder();
        String strA = z.a(context, "gsm.sim.state");
        if (strA != null) {
            sb.append("gsm.sim.state");
            sb.append(PinyinConverter.PINYIN_EXCLUDE);
            sb.append(strA);
        }
        sb.append("\n");
        String strA2 = z.a(context, "gsm.sim.state2");
        if (strA2 != null) {
            sb.append("gsm.sim.state2");
            sb.append(PinyinConverter.PINYIN_EXCLUDE);
            sb.append(strA2);
        }
        return sb.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0049 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long o() throws java.lang.Throwable {
        /*
            r0 = 0
            r3 = 0
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L45
            java.io.FileReader r1 = new java.io.FileReader     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L45
            java.lang.String r4 = "/proc/uptime"
            r1.<init>(r4)     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L45
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L45
            java.lang.String r1 = r2.readLine()     // Catch: java.lang.Throwable -> L52 java.lang.Throwable -> L54
            if (r1 == 0) goto L2b
            java.lang.String r3 = " "
            java.lang.String[] r1 = r1.split(r3)     // Catch: java.lang.Throwable -> L52 java.lang.Throwable -> L54
            r3 = 0
            r1 = r1[r3]     // Catch: java.lang.Throwable -> L52 java.lang.Throwable -> L54
            long r4 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L52 java.lang.Throwable -> L54
            r6 = 1000(0x3e8, double:4.94E-321)
            long r4 = r4 / r6
            float r3 = (float) r4     // Catch: java.lang.Throwable -> L52 java.lang.Throwable -> L54
            float r0 = java.lang.Float.parseFloat(r1)     // Catch: java.lang.Throwable -> L52 java.lang.Throwable -> L54
            float r0 = r3 - r0
        L2b:
            r2.close()     // Catch: java.io.IOException -> L30
        L2e:
            long r0 = (long) r0
            return r0
        L30:
            r1 = move-exception
            com.tencent.bugly.proguard.x.a(r1)
            goto L2e
        L35:
            r1 = move-exception
            r2 = r3
        L37:
            com.tencent.bugly.proguard.x.a(r1)     // Catch: java.lang.Throwable -> L52
            if (r2 == 0) goto L2e
            r2.close()     // Catch: java.io.IOException -> L40
            goto L2e
        L40:
            r1 = move-exception
            com.tencent.bugly.proguard.x.a(r1)
            goto L2e
        L45:
            r0 = move-exception
            r2 = r3
        L47:
            if (r2 == 0) goto L4c
            r2.close()     // Catch: java.io.IOException -> L4d
        L4c:
            throw r0
        L4d:
            r1 = move-exception
            com.tencent.bugly.proguard.x.a(r1)
            goto L4c
        L52:
            r0 = move-exception
            goto L47
        L54:
            r1 = move-exception
            goto L37
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.b.o():long");
    }
}
