package cn.kuwo.autosdk;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.util.Random;

/* loaded from: classes.dex */
public final class n {

    /* renamed from: a, reason: collision with root package name */
    public static String f17a;
    public static String b;
    public static String c = "kwplayerhd";
    public static String d = "ar";
    public static String e = "1.2.0.0";
    public static String f = String.valueOf(c) + "_" + d + "_" + e;
    public static String g = "";

    private static String a() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        int iNextInt = random.nextInt(5);
        if (iNextInt == 0) {
            iNextInt = 1;
        }
        int i = iNextInt * 10000;
        sb.append(i + random.nextInt(i));
        int iNextInt2 = (random.nextInt(5) + 5) * 100000;
        sb.append(iNextInt2 + random.nextInt(iNextInt2));
        return sb.toString();
    }

    public static void a(Context context, String str) {
        String deviceId;
        g = String.valueOf(f) + "_" + str + ".apk";
        try {
            f17a = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            if (TextUtils.isEmpty(f17a)) {
                f17a = "";
            }
        } catch (Throwable th) {
            th.printStackTrace();
            f17a = "";
        }
        try {
            deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        } catch (Throwable th2) {
            th2.printStackTrace();
            deviceId = "";
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = !TextUtils.isEmpty(f17a) ? String.valueOf(f17a.replace(":", "")) + "000" : a();
        }
        b = deviceId;
    }
}
