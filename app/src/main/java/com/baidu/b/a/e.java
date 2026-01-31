package com.baidu.b.a;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Base64;
import com.unisound.client.SpeechConstants;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Locale;

/* loaded from: classes.dex */
class e {

    static class a {
        public static String a(byte[] bArr) {
            char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            StringBuilder sb = new StringBuilder(bArr.length * 2);
            for (int i = 0; i < bArr.length; i++) {
                sb.append(cArr[(bArr[i] & 240) >> 4]);
                sb.append(cArr[bArr[i] & 15]);
            }
            return sb.toString();
        }
    }

    static String a() {
        return Locale.getDefault().getLanguage();
    }

    protected static String a(Context context) {
        String packageName = context.getPackageName();
        return a(context, packageName) + ";" + packageName;
    }

    private static String a(Context context, String str) {
        String strA;
        try {
            strA = a((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(context.getPackageManager().getPackageInfo(str, 64).signatures[0].toByteArray())));
        } catch (PackageManager.NameNotFoundException e) {
            strA = "";
        } catch (CertificateException e2) {
            strA = "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strA.length(); i++) {
            stringBuffer.append(strA.charAt(i));
            if (i > 0 && i % 2 == 1 && i < strA.length() - 1) {
                stringBuffer.append(":");
            }
        }
        return stringBuffer.toString();
    }

    static String a(X509Certificate x509Certificate) {
        try {
            return a.a(a(x509Certificate.getEncoded()));
        } catch (CertificateEncodingException e) {
            return null;
        }
    }

    static byte[] a(byte[] bArr) {
        try {
            return MessageDigest.getInstance("SHA1").digest(bArr);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    protected static String[] b(Context context) {
        String packageName = context.getPackageName();
        String[] strArrB = b(context, packageName);
        if (strArrB == null || strArrB.length <= 0) {
            return null;
        }
        String[] strArr = new String[strArrB.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = strArrB[i] + ";" + packageName;
            if (d.f40a) {
                d.a("mcode" + strArr[i]);
            }
        }
        return strArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x003f  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0049  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String[] b(android.content.Context r8, java.lang.String r9) {
        /*
            r2 = 0
            r3 = 0
            android.content.pm.PackageManager r0 = r8.getPackageManager()     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L79 java.security.cert.CertificateException -> L7d
            r1 = 64
            android.content.pm.PackageInfo r0 = r0.getPackageInfo(r9, r1)     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L79 java.security.cert.CertificateException -> L7d
            android.content.pm.Signature[] r5 = r0.signatures     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L79 java.security.cert.CertificateException -> L7d
            if (r5 == 0) goto L8f
            int r0 = r5.length     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L79 java.security.cert.CertificateException -> L7d
            if (r0 <= 0) goto L8f
            int r0 = r5.length     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L79 java.security.cert.CertificateException -> L7d
            java.lang.String[] r1 = new java.lang.String[r0]     // Catch: android.content.pm.PackageManager.NameNotFoundException -> L79 java.security.cert.CertificateException -> L7d
            r4 = r3
        L17:
            int r0 = r5.length     // Catch: java.security.cert.CertificateException -> L8b android.content.pm.PackageManager.NameNotFoundException -> L8d
            if (r4 >= r0) goto L3b
            java.lang.String r0 = "X.509"
            java.security.cert.CertificateFactory r0 = java.security.cert.CertificateFactory.getInstance(r0)     // Catch: java.security.cert.CertificateException -> L8b android.content.pm.PackageManager.NameNotFoundException -> L8d
            java.io.ByteArrayInputStream r6 = new java.io.ByteArrayInputStream     // Catch: java.security.cert.CertificateException -> L8b android.content.pm.PackageManager.NameNotFoundException -> L8d
            r7 = r5[r4]     // Catch: java.security.cert.CertificateException -> L8b android.content.pm.PackageManager.NameNotFoundException -> L8d
            byte[] r7 = r7.toByteArray()     // Catch: java.security.cert.CertificateException -> L8b android.content.pm.PackageManager.NameNotFoundException -> L8d
            r6.<init>(r7)     // Catch: java.security.cert.CertificateException -> L8b android.content.pm.PackageManager.NameNotFoundException -> L8d
            java.security.cert.Certificate r0 = r0.generateCertificate(r6)     // Catch: java.security.cert.CertificateException -> L8b android.content.pm.PackageManager.NameNotFoundException -> L8d
            java.security.cert.X509Certificate r0 = (java.security.cert.X509Certificate) r0     // Catch: java.security.cert.CertificateException -> L8b android.content.pm.PackageManager.NameNotFoundException -> L8d
            java.lang.String r0 = a(r0)     // Catch: java.security.cert.CertificateException -> L8b android.content.pm.PackageManager.NameNotFoundException -> L8d
            r1[r4] = r0     // Catch: java.security.cert.CertificateException -> L8b android.content.pm.PackageManager.NameNotFoundException -> L8d
            int r0 = r4 + 1
            r4 = r0
            goto L17
        L3b:
            r0 = r1
        L3c:
            r4 = r0
        L3d:
            if (r4 == 0) goto L8a
            int r0 = r4.length
            if (r0 <= 0) goto L8a
            int r0 = r4.length
            java.lang.String[] r2 = new java.lang.String[r0]
            r0 = r3
        L46:
            int r1 = r4.length
            if (r0 >= r1) goto L8a
            java.lang.StringBuffer r5 = new java.lang.StringBuffer
            r5.<init>()
            r1 = r3
        L4f:
            r6 = r4[r0]
            int r6 = r6.length()
            if (r1 >= r6) goto L81
            r6 = r4[r0]
            char r6 = r6.charAt(r1)
            r5.append(r6)
            if (r1 <= 0) goto L76
            int r6 = r1 % 2
            r7 = 1
            if (r6 != r7) goto L76
            r6 = r4[r0]
            int r6 = r6.length()
            int r6 = r6 + (-1)
            if (r1 >= r6) goto L76
            java.lang.String r6 = ":"
            r5.append(r6)
        L76:
            int r1 = r1 + 1
            goto L4f
        L79:
            r0 = move-exception
            r1 = r2
        L7b:
            r4 = r1
            goto L3d
        L7d:
            r0 = move-exception
            r1 = r2
        L7f:
            r4 = r1
            goto L3d
        L81:
            java.lang.String r1 = r5.toString()
            r2[r0] = r1
            int r0 = r0 + 1
            goto L46
        L8a:
            return r2
        L8b:
            r0 = move-exception
            goto L7f
        L8d:
            r0 = move-exception
            goto L7b
        L8f:
            r0 = r2
            goto L3c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.b.a.e.b(android.content.Context, java.lang.String):java.lang.String[]");
    }

    static String c(Context context) {
        String string = null;
        if ((0 == 0 || "".equals(null)) && (string = context.getSharedPreferences("mac", 0).getString("macaddr", null)) == null) {
            String strD = d(context);
            if (strD != null) {
                string = Base64.encodeToString(strD.getBytes(), 0);
                if (!TextUtils.isEmpty(string)) {
                    context.getSharedPreferences("mac", 0).edit().putString("macaddr", string).commit();
                }
            } else {
                string = "";
            }
        }
        if (d.f40a) {
            d.a("getMacID mac_adress: " + string);
        }
        return string;
    }

    private static boolean c(Context context, String str) {
        boolean z = context.checkCallingOrSelfPermission(str) != -1;
        if (d.f40a) {
            d.a("hasPermission " + z + " | " + str);
        }
        return z;
    }

    static String d(Context context) {
        String macAddress;
        Exception e;
        try {
            if (!c(context, SpeechConstants.PERMISSION_ACCESS_WIFI_STATE)) {
                if (d.f40a) {
                    d.a("You need the android.Manifest.permission.ACCESS_WIFI_STATE permission. Open AndroidManifest.xml and just before the final </manifest> tag add:android.permission.ACCESS_WIFI_STATE");
                }
                return null;
            }
            WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
            macAddress = connectionInfo.getMacAddress();
            try {
                if (!TextUtils.isEmpty(macAddress)) {
                    Base64.encode(macAddress.getBytes(), 0);
                }
                if (!d.f40a) {
                    return macAddress;
                }
                d.a(String.format("ssid=%s mac=%s", connectionInfo.getSSID(), connectionInfo.getMacAddress()));
                return macAddress;
            } catch (Exception e2) {
                e = e2;
                if (!d.f40a) {
                    return macAddress;
                }
                d.a(e.toString());
                return macAddress;
            }
        } catch (Exception e3) {
            macAddress = null;
            e = e3;
        }
    }
}
