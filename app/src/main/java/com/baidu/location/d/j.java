package com.baidu.location.d;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.provider.Settings;
import cn.yunzhisheng.asr.JniUscClient;
import cn.yunzhisheng.common.PinyinConverter;
import com.baidu.location.Jni;
import com.unisound.client.SpeechConstants;
import com.unisound.vui.priority.PriorityMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;

/* loaded from: classes.dex */
public class j {

    /* renamed from: a, reason: collision with root package name */
    public static boolean f112a = false;
    public static boolean b = false;
    public static boolean c = false;
    public static int d = 0;
    private static String av = "http://loc.map.baidu.com/sdk.php";
    public static String e = "http://loc.map.baidu.com/sdk_ep.php";
    private static String aw = "http://loc.map.baidu.com/user_err.php";
    private static String ax = "http://loc.map.baidu.com/oqur.php";
    private static String ay = "http://loc.map.baidu.com/tcu.php";
    private static String az = "http://loc.map.baidu.com/rtbu.php";
    private static String aA = "http://loc.map.baidu.com/iofd.php";
    private static String aB = "http://loc.map.baidu.com/wloc";
    public static String f = "https://loc.map.baidu.com/sdk.php";
    public static String g = JniUscClient.aA;
    public static boolean h = false;
    public static boolean i = false;
    public static boolean j = false;
    public static boolean k = false;
    public static boolean l = false;
    public static String m = "gcj02";
    public static String n = "";
    public static boolean o = true;
    public static int p = 3;
    public static double q = 0.0d;
    public static double r = 0.0d;
    public static double s = 0.0d;
    public static double t = 0.0d;
    public static int u = 0;
    public static byte[] v = null;
    public static boolean w = false;
    public static int x = 0;
    public static float y = 1.1f;
    public static float z = 2.2f;
    public static float A = 2.3f;
    public static float B = 3.8f;
    public static int C = 3;
    public static int D = 10;
    public static int E = 2;
    public static int F = 7;
    public static int G = 20;
    public static int H = 70;
    public static int I = com.unisound.b.g.g;
    public static float J = 2.0f;
    public static float K = 10.0f;
    public static float L = 50.0f;
    public static float M = 200.0f;
    public static int N = 16;
    public static float O = 0.9f;
    public static int P = 10000;
    public static float Q = 0.5f;
    public static float R = 0.0f;
    public static float S = 0.1f;
    public static int T = 30;
    public static int U = 100;
    public static int V = 0;
    public static int W = 0;
    public static int X = 0;
    public static int Y = 420000;
    public static boolean Z = true;
    public static boolean aa = true;
    public static int ab = 20;
    public static int ac = 300;
    public static int ad = 1000;
    public static int ae = PriorityMap.PRIORITY_MAX;
    public static long af = 900000;
    public static long ag = 420000;
    public static long ah = 180000;
    public static long ai = 0;
    public static long aj = 15;
    public static long ak = 300000;
    public static int al = 1000;
    public static int am = 0;
    public static int an = 30000;
    public static int ao = 30000;
    public static float ap = 10.0f;
    public static float aq = 6.0f;
    public static float ar = 10.0f;
    public static int as = 60;
    public static int at = 70;
    public static int au = 6;

    public static int a(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), "airplane_mode_on", 0);
        } catch (Exception e2) {
            return 2;
        }
    }

    public static int a(String str, String str2, String str3) {
        int iIndexOf;
        int length;
        int iIndexOf2;
        String strSubstring;
        if (str == null || str.equals("") || (iIndexOf = str.indexOf(str2)) == -1 || (iIndexOf2 = str.indexOf(str3, (length = iIndexOf + str2.length()))) == -1 || (strSubstring = str.substring(length, iIndexOf2)) == null || strSubstring.equals("")) {
            return Integer.MIN_VALUE;
        }
        try {
            return Integer.parseInt(strSubstring);
        } catch (NumberFormatException e2) {
            return Integer.MIN_VALUE;
        }
    }

    public static Object a(Context context, String str) {
        if (context == null) {
            return null;
        }
        try {
            return context.getApplicationContext().getSystemService(str);
        } catch (Throwable th) {
            return null;
        }
    }

    public static Object a(Object obj, String str, Object... objArr) {
        Class<?> cls = obj.getClass();
        Class<?>[] clsArr = new Class[objArr.length];
        int length = objArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            clsArr[i2] = objArr[i2].getClass();
            if (clsArr[i2] == Integer.class) {
                clsArr[i2] = Integer.TYPE;
            }
        }
        Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
        if (!declaredMethod.isAccessible()) {
            declaredMethod.setAccessible(true);
        }
        return declaredMethod.invoke(obj, objArr);
    }

    public static String a() {
        Calendar calendar = Calendar.getInstance();
        int i2 = calendar.get(5);
        return String.format(Locale.CHINA, "%d-%02d-%02d %02d:%02d:%02d", Integer.valueOf(calendar.get(1)), Integer.valueOf(calendar.get(2) + 1), Integer.valueOf(i2), Integer.valueOf(calendar.get(11)), Integer.valueOf(calendar.get(12)), Integer.valueOf(calendar.get(13)));
    }

    public static String a(com.baidu.location.bfold.a aVar, com.baidu.location.bfold.f fVar, Location location, String str, int i2) {
        return a(aVar, fVar, location, str, i2, false);
    }

    public static String a(com.baidu.location.bfold.a aVar, com.baidu.location.bfold.f fVar, Location location, String str, int i2, boolean z2) {
        String strA;
        String strB;
        StringBuffer stringBuffer = new StringBuffer(1024);
        if (aVar != null && (strB = com.baidu.location.b.b.a().b(aVar)) != null) {
            stringBuffer.append(strB);
        }
        if (fVar != null) {
            String strB2 = i2 == 0 ? z2 ? fVar.b() : fVar.c() : fVar.d();
            if (strB2 != null) {
                stringBuffer.append(strB2);
            }
        }
        if (location != null) {
            String strB3 = (d == 0 || i2 == 0) ? com.baidu.location.b.d.b(location) : com.baidu.location.b.d.c(location);
            if (strB3 != null) {
                stringBuffer.append(strB3);
            }
        }
        String strA2 = b.a().a(i2 == 0);
        if (strA2 != null) {
            stringBuffer.append(strA2);
        }
        if (str != null) {
            stringBuffer.append(str);
        }
        if (i2 == 0) {
        }
        if (aVar != null && (strA = com.baidu.location.b.b.a().a(aVar)) != null && strA.length() + stringBuffer.length() < 750) {
            stringBuffer.append(strA);
        }
        String string = stringBuffer.toString();
        try {
            if (location == null || fVar == null) {
                p = 3;
            } else {
                float speed = location.getSpeed();
                int i3 = d;
                int iF = fVar.f();
                int iA = fVar.a();
                boolean zG = fVar.g();
                if (speed < aq && ((i3 == 1 || i3 == 0) && (iF < as || zG))) {
                    p = 1;
                } else if (speed >= ar || (!(i3 == 1 || i3 == 0 || i3 == 3) || (iF >= at && iA <= au))) {
                    p = 3;
                } else {
                    p = 2;
                }
            }
        } catch (Exception e2) {
            p = 3;
        }
        return string;
    }

    public static String a(File file, String str) throws NoSuchAlgorithmException, IOException {
        if (!file.isFile()) {
            return null;
        }
        byte[] bArr = new byte[1024];
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(str);
            FileInputStream fileInputStream = new FileInputStream(file);
            while (true) {
                int i2 = fileInputStream.read(bArr, 0, 1024);
                if (i2 == -1) {
                    fileInputStream.close();
                    return new BigInteger(1, messageDigest.digest()).toString(16);
                }
                messageDigest.update(bArr, 0, i2);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String a(String str) {
        return Jni.b(n + ";" + str);
    }

    public static boolean a(com.baidu.location.c cVar) {
        int iH = cVar.h();
        return (iH > 100 && iH < 200) || iH == 62;
    }

    public static int b(Context context) {
        if (Build.VERSION.SDK_INT < 19) {
            return -2;
        }
        try {
            return Settings.Secure.getInt(context.getContentResolver(), "location_mode", -1);
        } catch (Exception e2) {
            return -1;
        }
    }

    private static int b(Context context, String str) {
        boolean z2;
        try {
            z2 = context.checkPermission(str, Process.myPid(), Process.myUid()) == 0;
        } catch (Exception e2) {
            z2 = true;
        }
        return !z2 ? 0 : 1;
    }

    public static int b(Object obj, String str, Object... objArr) {
        Class<?> cls = obj.getClass();
        Class<?>[] clsArr = new Class[objArr.length];
        int length = objArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            clsArr[i2] = objArr[i2].getClass();
            if (clsArr[i2] == Integer.class) {
                clsArr[i2] = Integer.TYPE;
            }
        }
        Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
        if (!declaredMethod.isAccessible()) {
            declaredMethod.setAccessible(true);
        }
        return ((Integer) declaredMethod.invoke(obj, objArr)).intValue();
    }

    public static String b() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddressNextElement = inetAddresses.nextElement();
                    if (!inetAddressNextElement.isLoopbackAddress() && (inetAddressNextElement instanceof Inet4Address)) {
                        byte[] address = inetAddressNextElement.getAddress();
                        String str = "";
                        int i2 = 0;
                        while (true) {
                            int i3 = i2;
                            String str2 = str;
                            if (i3 >= address.length) {
                                return str2;
                            }
                            String hexString = Integer.toHexString(address[i3] & 255);
                            if (hexString.length() == 1) {
                                hexString = '0' + hexString;
                            }
                            str = str2 + hexString;
                            i2 = i3 + 1;
                        }
                    }
                }
            }
        } catch (Exception e2) {
        }
        return null;
    }

    public static boolean b(String str, String str2, String str3) throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        try {
            PublicKey publicKeyGeneratePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(com.baidu.a.a.a.a.b.a(str3.getBytes())));
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(publicKeyGeneratePublic);
            signature.update(str.getBytes());
            return signature.verify(com.baidu.a.a.a.a.b.a(str2.getBytes()));
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static String c() {
        return av;
    }

    public static String c(Context context) {
        return "&per=" + b(context, "android.permission.ACCESS_COARSE_LOCATION") + PinyinConverter.PINYIN_EXCLUDE + b(context, SpeechConstants.PERMISSION_ACCESS_FINE_LOCATION) + PinyinConverter.PINYIN_EXCLUDE + b(context, SpeechConstants.PERMISSION_READ_PHONE_STATE);
    }

    public static String d() {
        return ay;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0032  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String d(android.content.Context r3) {
        /*
            r1 = -1
            if (r3 == 0) goto L32
            java.lang.String r0 = "connectivity"
            java.lang.Object r0 = r3.getSystemService(r0)     // Catch: java.lang.Exception -> L30
            android.net.ConnectivityManager r0 = (android.net.ConnectivityManager) r0     // Catch: java.lang.Exception -> L30
            android.net.NetworkInfo r0 = r0.getActiveNetworkInfo()     // Catch: java.lang.Exception -> L30
            if (r0 == 0) goto L32
            boolean r2 = r0.isAvailable()     // Catch: java.lang.Exception -> L30
            if (r2 == 0) goto L32
            int r0 = r0.getType()     // Catch: java.lang.Exception -> L30
        L1b:
            r1 = r0
        L1c:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "&netc="
            java.lang.StringBuilder r0 = r0.append(r2)
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            return r0
        L30:
            r0 = move-exception
            goto L1c
        L32:
            r0 = r1
            goto L1b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.d.j.d(android.content.Context):java.lang.String");
    }

    public static String e() {
        return "https://daup.map.baidu.com/cltr/rcvr";
    }

    public static String f() {
        try {
            if (!Environment.getExternalStorageState().equals("mounted")) {
                return null;
            }
            String path = Environment.getExternalStorageDirectory().getPath();
            File file = new File(path + "/baidu/tempdata");
            if (file.exists()) {
                return path;
            }
            file.mkdirs();
            return path;
        } catch (Exception e2) {
            return null;
        }
    }

    public static String g() {
        String strF = f();
        if (strF == null) {
            return null;
        }
        return strF + "/baidu/tempdata";
    }

    public static String h() {
        try {
            File file = new File(com.baidu.location.f.b().getFilesDir() + File.separator + "lldt");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        } catch (Exception e2) {
            return null;
        }
    }
}
