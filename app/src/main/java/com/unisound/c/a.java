package com.unisound.c;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.unisound.sdk.ca;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class a {
    private static Context B = null;
    private static final String F = "DeviceInfoUtil";

    /* renamed from: a, reason: collision with root package name */
    public static final String f227a = "PN";
    public static final String b = "OS";
    public static final String c = "CR";
    public static final String d = "NT";
    public static final String e = "MD";
    public static final String f = "SV";
    public static final String g = "SID";
    public static final String h = "RPT";
    public static final String i = "EC";
    public static final String j = "NPT";
    public static final String k = "IP";
    public static final int l = 0;
    public static final int m = 1;
    public static final int n = 2;
    public static final int o = 3;
    public static final int p = 4;
    public static TelephonyManager u = null;
    public static NetworkInfo v = null;
    public static ConnectivityManager w = null;
    private static final String x = "000000000000000";
    private static boolean y = false;
    public static String q = "";
    public static String r = "";
    public static String s = "";
    public static String t = "";
    private static String z = "";
    private static String A = "4bd9354d1cf247c93db388257567d0e2";
    private static String C = "";
    private static boolean D = false;
    private static boolean E = true;
    private static String G = "00";

    public static String a() {
        return Build.MODEL;
    }

    public static String a(Context context, String str) {
        return a(context, str, C);
    }

    private static String a(Context context, String str, String str2) throws Throwable {
        String strD = d(str);
        f("DeviceInfoUtil getUDID deviceSn = " + str2);
        if (strD != null && !strD.equals("")) {
            f("DeviceInfoUtil getUDID from sdcard= " + strD);
            if (!TextUtils.isEmpty(str2) && !b(str, strD, str2)) {
                return b(context, str, str2);
            }
            c(context, str, strD);
            return strD;
        }
        String strB = b(context, str);
        if (strB == null || strB.equals("")) {
            String strB2 = b(context, str, str2);
            f("DeviceInfoUtil first getUDID= " + strB2);
            return strB2;
        }
        f("DeviceInfoUtil getUDID from sharedPreferences= " + strB);
        if (!TextUtils.isEmpty(str2) && !b(str, strB, str2)) {
            return b(context, str, str2);
        }
        a(A, str, strB);
        return strB;
    }

    public static String a(String str) {
        if (TextUtils.isEmpty(z) || D) {
            z = a(B, str);
            if (D) {
                D = false;
            }
        }
        return z;
    }

    public static String a(String str, Context context) {
        String strB = "";
        try {
            strB = b(a(str, a(str), context), "v3.1");
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        h("DeviceUtil tokenJson is " + strB);
        return strB;
    }

    private static String a(String str, String str2) {
        h("build udid by deviceSn, appkey = " + str + ", OS = " + G + ", deviceSn = " + str2);
        int iHashCode = str.hashCode();
        h("build udid by deviceSn, appkeyHashCode = " + iHashCode);
        StringBuilder sb = new StringBuilder();
        sb.append(iHashCode).append(G).append(str2);
        String string = sb.toString();
        h("build udid by deviceSn, deviceContent = " + string);
        String strReplaceAll = (TextUtils.isEmpty(string) ? "" : Base64.encodeToString(string.getBytes(), 8)).replaceAll("=", "").replaceAll("\\s", "");
        h("build udid by deviceSn, udid = " + strReplaceAll);
        return strReplaceAll;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x005a, code lost:
    
        r0 = r3.getString("token");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String a(java.lang.String r5, java.lang.String r6, android.content.Context r7) throws org.json.JSONException {
        /*
            r1 = 0
            java.lang.String r0 = com.unisound.c.a.A
            android.content.SharedPreferences r0 = r7.getSharedPreferences(r0, r1)
            java.lang.String r2 = "deviceInfo"
            java.lang.String r3 = ""
            java.lang.String r2 = r0.getString(r2, r3)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "DeviceUtil getTokenInfo deviceContent = "
            java.lang.StringBuilder r0 = r0.append(r3)
            java.lang.StringBuilder r0 = r0.append(r2)
            java.lang.String r0 = r0.toString()
            h(r0)
            java.lang.String r0 = ""
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch: java.lang.Exception -> L7a
            if (r3 != 0) goto L60
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch: java.lang.Exception -> L7a
            r3.<init>(r2)     // Catch: java.lang.Exception -> L7a
            java.lang.String r2 = "deviceInfo"
            org.json.JSONArray r2 = r3.getJSONArray(r2)     // Catch: java.lang.Exception -> L7a
        L38:
            int r3 = r2.length()     // Catch: java.lang.Exception -> L7a
            if (r1 >= r3) goto L60
            org.json.JSONObject r3 = r2.getJSONObject(r1)     // Catch: java.lang.Exception -> L7a
            java.lang.String r4 = "appkey"
            java.lang.Object r4 = r3.get(r4)     // Catch: java.lang.Exception -> L7a
            boolean r4 = r5.equals(r4)     // Catch: java.lang.Exception -> L7a
            if (r4 == 0) goto L77
            java.lang.String r4 = "udid"
            java.lang.Object r4 = r3.get(r4)     // Catch: java.lang.Exception -> L7a
            boolean r4 = r6.equals(r4)     // Catch: java.lang.Exception -> L7a
            if (r4 == 0) goto L77
            java.lang.String r1 = "token"
            java.lang.String r0 = r3.getString(r1)     // Catch: java.lang.Exception -> L7a
        L60:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "getTokenInfo token = "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r0)
            java.lang.String r1 = r1.toString()
            h(r1)
            return r0
        L77:
            int r1 = r1 + 1
            goto L38
        L7a:
            r1 = move-exception
            goto L60
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.c.a.a(java.lang.String, java.lang.String, android.content.Context):java.lang.String");
    }

    private static String a(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        for (byte b2 : bArr) {
            String hexString = Integer.toHexString(b2 & 255);
            if (hexString.length() == 1) {
                sb.append("0");
            }
            sb.append(hexString.toUpperCase());
        }
        return sb.toString();
    }

    public static void a(Context context) {
        B = context;
        if (y) {
            return;
        }
        w = (ConnectivityManager) context.getSystemService("connectivity");
        u = (TelephonyManager) context.getSystemService("phone");
        if (w != null) {
            v = w.getNetworkInfo(0);
        }
        s = context.getPackageName();
        r = c(context);
        q = b(context);
        t = Build.MODEL;
        y = true;
    }

    private static void a(String str, String str2, String str3) throws Throwable {
        RandomAccessFile randomAccessFile;
        Throwable th;
        String string;
        boolean z2 = false;
        RandomAccessFile randomAccessFile2 = null;
        String strI = "";
        try {
            String strJ = j();
            File file = new File(strJ);
            if (!file.exists()) {
                file.mkdirs();
            }
            File file2 = new File(strJ + File.separator + str);
            if (file2.exists()) {
                strI = i();
                file2.delete();
            }
            file2.createNewFile();
            if (strI.contains("deviceInfo")) {
                JSONObject jSONObject = new JSONObject(strI);
                JSONArray jSONArray = jSONObject.getJSONArray("deviceInfo");
                int i2 = 0;
                while (true) {
                    if (i2 >= jSONArray.length()) {
                        break;
                    }
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
                    if (str2.equals(jSONObject2.get(ca.c))) {
                        jSONObject2.put("udid", str3);
                        z2 = true;
                        break;
                    }
                    i2++;
                }
                if (!z2) {
                    JSONObject jSONObject3 = new JSONObject();
                    jSONObject3.put(ca.c, str2);
                    jSONObject3.put("udid", str3);
                    jSONArray.put(jSONObject3);
                    jSONObject.put("deviceInfo", jSONArray);
                }
                string = jSONObject.toString();
            } else {
                JSONObject jSONObject4 = new JSONObject();
                JSONArray jSONArray2 = new JSONArray();
                JSONObject jSONObject5 = new JSONObject();
                jSONObject5.put(ca.c, str2);
                jSONObject5.put("udid", str3);
                jSONArray2.put(jSONObject5);
                jSONObject4.put("deviceInfo", jSONArray2);
                string = jSONObject4.toString();
            }
            f("DeviceInfoUtil setUDIDToSdcard udidContent = " + string);
            randomAccessFile = new RandomAccessFile(strJ + File.separator + str, "rw");
            try {
                randomAccessFile.write(string.getBytes());
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Exception e3) {
                randomAccessFile2 = randomAccessFile;
                if (randomAccessFile2 != null) {
                    try {
                        randomAccessFile2.close();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Exception e6) {
        } catch (Throwable th3) {
            randomAccessFile = null;
            th = th3;
        }
    }

    public static void a(boolean z2) {
        D = z2;
    }

    public static int b() {
        NetworkInfo networkInfo = w.getNetworkInfo(1);
        if (networkInfo == null || !networkInfo.isAvailable()) {
            return c();
        }
        return 1;
    }

    public static int b(String str) {
        if (str == null) {
            return -1;
        }
        if (str.length() > 24) {
            C = "";
            return -1;
        }
        C = str;
        f("DeviceInfoUtil setDeviceSn = " + C);
        return 0;
    }

    public static String b(Context context) {
        String deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (deviceId != null && !"".equals(deviceId) && !deviceId.equals(x)) {
            return deviceId;
        }
        String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
        if (string != null && !"".equals(string) && !string.equals(x)) {
            return string;
        }
        WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        return connectionInfo != null ? connectionInfo.getMacAddress() : x;
    }

    private static String b(Context context, String str) throws JSONException {
        String string = context.getSharedPreferences(A, 0).getString("deviceInfo", "");
        h("DeviceInfoUtil getUDIDFromSb deviceContent = " + string);
        try {
            if (TextUtils.isEmpty(string)) {
                return "";
            }
            JSONArray jSONArray = new JSONObject(string).getJSONArray("deviceInfo");
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                if (str.equals(jSONObject.get(ca.c))) {
                    return jSONObject.getString("udid");
                }
            }
            return "";
        } catch (Exception e2) {
            return "";
        }
    }

    private static String b(Context context, String str, String str2) throws Throwable {
        String strA = !TextUtils.isEmpty(str2) ? a(str, str2) : l();
        c(context, str, strA);
        a(A, str, strA);
        f("DeviceInfoUtil createNewUDID UDID= " + strA);
        return strA;
    }

    private static String b(String str, String str2) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("deviceToken", str);
        jSONObject.put("activateVersion", str2);
        return jSONObject.toString();
    }

    public static void b(boolean z2) {
        E = z2;
    }

    private static boolean b(String str, String str2, String str3) {
        boolean zEquals = str2.equals(a(str, str3));
        f("DeviceInfoUtil isUdidMatchDeviceSn = " + zEquals);
        return zEquals;
    }

    public static int c() {
        if (v == null || !v.isAvailable()) {
            return 0;
        }
        switch (u.getNetworkType()) {
            case 1:
            case 2:
            case 4:
                return 3;
            case 3:
            case 5:
            case 6:
            case 8:
                return 2;
            case 7:
            default:
                return 4;
        }
    }

    public static String c(Context context) {
        String networkOperator = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperator();
        return (networkOperator == null || "".equals(networkOperator)) ? "0" : networkOperator;
    }

    private static void c(Context context, String str, String str2) throws JSONException {
        String string;
        boolean z2 = false;
        SharedPreferences sharedPreferences = context.getSharedPreferences(A, 0);
        String string2 = sharedPreferences.getString("deviceInfo", "");
        try {
            if (TextUtils.isEmpty(string2)) {
                JSONObject jSONObject = new JSONObject();
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put(ca.c, str);
                jSONObject2.put("udid", str2);
                jSONObject2.put("token", "");
                JSONArray jSONArray = new JSONArray();
                jSONArray.put(jSONObject2);
                jSONObject.put("deviceInfo", jSONArray);
                string = jSONObject.toString();
            } else {
                JSONObject jSONObject3 = new JSONObject(string2);
                JSONArray jSONArray2 = jSONObject3.getJSONArray("deviceInfo");
                int i2 = 0;
                while (true) {
                    if (i2 >= jSONArray2.length()) {
                        break;
                    }
                    JSONObject jSONObject4 = jSONArray2.getJSONObject(i2);
                    if (str.equals(jSONObject4.get(ca.c))) {
                        if (!str2.equals(jSONObject4.get("udid"))) {
                            jSONObject4.put("udid", str2);
                            jSONObject4.put("token", "");
                        }
                        z2 = true;
                    } else {
                        i2++;
                    }
                }
                if (!z2) {
                    JSONObject jSONObject5 = new JSONObject();
                    jSONObject5.put(ca.c, str);
                    jSONObject5.put("udid", str2);
                    jSONObject5.put("token", "");
                    jSONArray2.put(jSONObject5);
                    jSONObject3.put("deviceInfo", jSONArray2);
                }
                string = jSONObject3.toString();
            }
            h("DeviceInfoUtil setUDIDToSb deviceContent = " + string);
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.putString("deviceInfo", string);
            editorEdit.commit();
        } catch (Exception e2) {
        }
    }

    public static void c(String str) {
        G = str;
        f("DeviceInfoUtil setOS = " + G);
    }

    public static String d() {
        return Build.PRODUCT;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0028, code lost:
    
        r0 = r3.getString("udid");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String d(java.lang.String r5) throws org.json.JSONException {
        /*
            java.lang.String r0 = ""
            java.lang.String r1 = i()     // Catch: java.lang.Exception -> L52
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch: java.lang.Exception -> L52
            r2.<init>(r1)     // Catch: java.lang.Exception -> L52
            java.lang.String r1 = "deviceInfo"
            org.json.JSONArray r2 = r2.getJSONArray(r1)     // Catch: java.lang.Exception -> L52
            r1 = 0
        L12:
            int r3 = r2.length()     // Catch: java.lang.Exception -> L52
            if (r1 >= r3) goto L2e
            org.json.JSONObject r3 = r2.getJSONObject(r1)     // Catch: java.lang.Exception -> L52
            java.lang.String r4 = "appkey"
            java.lang.Object r4 = r3.get(r4)     // Catch: java.lang.Exception -> L52
            boolean r4 = r5.equals(r4)     // Catch: java.lang.Exception -> L52
            if (r4 == 0) goto L4f
            java.lang.String r1 = "udid"
            java.lang.String r0 = r3.getString(r1)     // Catch: java.lang.Exception -> L52
        L2e:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "DeviceInfoUtil getUDIDFromSdcard : appkey = "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r5)
            java.lang.String r2 = ", udid = "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r0)
            java.lang.String r1 = r1.toString()
            f(r1)
            return r0
        L4f:
            int r1 = r1 + 1
            goto L12
        L52:
            r1 = move-exception
            goto L2e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.c.a.d(java.lang.String):java.lang.String");
    }

    public static boolean d(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
    }

    public static String e() {
        return Build.MODEL;
    }

    public static String e(Context context) {
        WifiInfo connectionInfo;
        try {
            connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        } catch (SecurityException e2) {
            g(e2.getMessage() + " add permission android.permission.ACCESS_WIFI_STATE");
        }
        String bssid = connectionInfo != null ? connectionInfo.getBSSID() : "";
        if (bssid == null) {
            bssid = "";
        }
        f("DeviceUtil getBSSID= " + bssid);
        return bssid;
    }

    private static String e(String str) {
        try {
            return a(MessageDigest.getInstance("SHA-1").digest(str.getBytes("UTF-8")));
        } catch (Exception e2) {
            return null;
        }
    }

    public static String f() {
        return Build.MANUFACTURER;
    }

    public static String f(Context context) {
        String str;
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e2) {
            e2.printStackTrace();
            str = "";
        }
        if (str == null) {
            str = "";
        }
        f("DeviceUtil getAppVersion= " + str);
        return str;
    }

    private static void f(String str) {
        if (E) {
            Log.i(F, str);
        }
    }

    public static String g() {
        return Build.VERSION.RELEASE;
    }

    public static String g(Context context) {
        String str;
        try {
            str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (Exception e2) {
            e2.printStackTrace();
            str = "";
        }
        if (str == null) {
            str = "";
        }
        f("DeviceUtil getAppPackageName= " + str);
        return str;
    }

    private static void g(String str) {
        if (E) {
            Log.i(F, str);
        }
    }

    public static String h() {
        String str = j() + File.separator + m();
        if (!new File(str).exists()) {
            return "";
        }
        try {
            return new RandomAccessFile(str, "rw").readLine();
        } catch (Exception e2) {
            g(e2.getMessage() + "readSN error");
            return "";
        }
    }

    public static String h(Context context) {
        if (context == null) {
            return x;
        }
        WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        if (connectionInfo != null) {
            return connectionInfo.getMacAddress();
        }
        f("DeviceUtil getMac= 000000000000000");
        return x;
    }

    private static void h(String str) {
        if (E) {
            Log.i(F, str);
        }
    }

    private static String i() throws IOException {
        BufferedReader bufferedReader;
        FileReader fileReader;
        String string;
        StringBuilder sb = new StringBuilder();
        try {
            fileReader = new FileReader(j() + File.separator + A);
            try {
                bufferedReader = new BufferedReader(fileReader);
                while (true) {
                    try {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line);
                    } catch (Exception e2) {
                        string = "";
                    }
                }
                string = sb.toString();
                try {
                    f("DeviceInfoUtil udidContent = " + string);
                } catch (Exception e3) {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    if (fileReader != null) {
                        fileReader.close();
                    }
                    return string;
                }
            } catch (Exception e5) {
                bufferedReader = null;
                string = "";
            }
        } catch (Exception e6) {
            bufferedReader = null;
            fileReader = null;
            string = "";
        }
        return string;
    }

    public static String i(Context context) {
        WifiInfo connectionInfo;
        String ssid;
        try {
            connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
        } catch (SecurityException e2) {
            g(e2.getMessage() + " add permission android.permission.ACCESS_WIFI_STATE");
        }
        String strSubstring = (connectionInfo == null || (ssid = connectionInfo.getSSID()) == null || ssid.equals("")) ? "" : ssid.substring(1, ssid.length() - 1);
        if (strSubstring == null) {
            strSubstring = "";
        }
        f("DeviceUtil getWifiSSID= " + strSubstring);
        return strSubstring;
    }

    private static String j() {
        return Environment.getExternalStorageState().equals("mounted") ? Environment.getExternalStorageDirectory().getPath() + File.separator + "unisound/sdk" : "/mnt/sdcard/unisound/sdk";
    }

    public static String j(Context context) {
        String subscriberId = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        String str = subscriberId != null ? (subscriberId.startsWith("46000") || subscriberId.startsWith("46002")) ? "中国移动" : subscriberId.startsWith("46001") ? "中国联通" : subscriberId.startsWith("46003") ? "中国电信" : "未找到对应运营商" : "没有手机卡";
        f("DeviceUtil operator= " + str);
        return str;
    }

    private static String k() throws IOException {
        InputStream inputStream;
        String strTrim = "00000000000000000000000000000000";
        try {
            Process processExec = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            if (processExec != null && (inputStream = processExec.getInputStream()) != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                while (true) {
                    if (line == null) {
                        break;
                    }
                    if (line.indexOf("Serial") <= -1) {
                        line = bufferedReader.readLine();
                    } else if (line.length() > 1 && line.contains(":")) {
                        strTrim = line.substring(line.indexOf(":") + 1, line.length()).trim();
                        break;
                    }
                }
                bufferedReader.close();
                inputStream.close();
            }
        } catch (IOException e2) {
        }
        return strTrim;
    }

    private static String k(Context context) throws IOException {
        if (A.equals("")) {
            String strB = b(context);
            q = strB;
            A = e(strB + k());
        }
        return A;
    }

    private static String l() {
        String string = UUID.randomUUID().toString();
        h("DeviceInfoUtil buildUdidFromUUId udid = " + string);
        return string;
    }

    private static String m() {
        return "SN";
    }
}
