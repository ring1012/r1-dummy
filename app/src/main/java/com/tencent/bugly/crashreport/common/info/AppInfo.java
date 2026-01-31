package com.tencent.bugly.crashreport.common.info;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import com.tencent.bugly.proguard.x;
import com.tencent.bugly.proguard.z;
import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class AppInfo {

    /* renamed from: a, reason: collision with root package name */
    private static ActivityManager f139a;

    static {
        "@buglyAllChannel@".split(",");
        "@buglyAllChannelPriority@".split(",");
    }

    public static String a(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return context.getPackageName();
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return "fail";
        }
    }

    public static PackageInfo b(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(a(context), 0);
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    public static boolean a(Context context, String str) {
        if (context == null || str == null || str.trim().length() <= 0) {
            return false;
        }
        try {
            String[] strArr = context.getPackageManager().getPackageInfo(context.getPackageName(), 4096).requestedPermissions;
            if (strArr == null) {
                return false;
            }
            for (String str2 : strArr) {
                if (str.equals(str2)) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            if (x.a(th)) {
                return false;
            }
            th.printStackTrace();
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x0056 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String a(int r5) throws java.lang.Throwable {
        /*
            r0 = 0
            r2 = 0
            java.io.FileReader r1 = new java.io.FileReader     // Catch: java.lang.Throwable -> L3b java.lang.Throwable -> L52
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L3b java.lang.Throwable -> L52
            java.lang.String r4 = "/proc/"
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L3b java.lang.Throwable -> L52
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch: java.lang.Throwable -> L3b java.lang.Throwable -> L52
            java.lang.String r4 = "/cmdline"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L3b java.lang.Throwable -> L52
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L3b java.lang.Throwable -> L52
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L3b java.lang.Throwable -> L52
            r2 = 512(0x200, float:7.175E-43)
            char[] r2 = new char[r2]     // Catch: java.lang.Throwable -> L5e java.lang.Throwable -> L60
            r1.read(r2)     // Catch: java.lang.Throwable -> L5e java.lang.Throwable -> L60
        L23:
            int r3 = r2.length     // Catch: java.lang.Throwable -> L5e java.lang.Throwable -> L60
            if (r0 >= r3) goto L2d
            char r3 = r2[r0]     // Catch: java.lang.Throwable -> L5e java.lang.Throwable -> L60
            if (r3 == 0) goto L2d
            int r0 = r0 + 1
            goto L23
        L2d:
            java.lang.String r3 = new java.lang.String     // Catch: java.lang.Throwable -> L5e java.lang.Throwable -> L60
            r3.<init>(r2)     // Catch: java.lang.Throwable -> L5e java.lang.Throwable -> L60
            r2 = 0
            java.lang.String r0 = r3.substring(r2, r0)     // Catch: java.lang.Throwable -> L5e java.lang.Throwable -> L60
            r1.close()     // Catch: java.lang.Throwable -> L5a
        L3a:
            return r0
        L3b:
            r0 = move-exception
            r1 = r2
        L3d:
            boolean r2 = com.tencent.bugly.proguard.x.a(r0)     // Catch: java.lang.Throwable -> L5e
            if (r2 != 0) goto L46
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L5e
        L46:
            java.lang.String r0 = java.lang.String.valueOf(r5)     // Catch: java.lang.Throwable -> L5e
            if (r1 == 0) goto L3a
            r1.close()     // Catch: java.lang.Throwable -> L50
            goto L3a
        L50:
            r1 = move-exception
            goto L3a
        L52:
            r0 = move-exception
            r1 = r2
        L54:
            if (r1 == 0) goto L59
            r1.close()     // Catch: java.lang.Throwable -> L5c
        L59:
            throw r0
        L5a:
            r1 = move-exception
            goto L3a
        L5c:
            r1 = move-exception
            goto L59
        L5e:
            r0 = move-exception
            goto L54
        L60:
            r0 = move-exception
            goto L3d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.crashreport.common.info.AppInfo.a(int):java.lang.String");
    }

    public static String c(Context context) {
        CharSequence applicationLabel;
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            if (packageManager == null || applicationInfo == null || (applicationLabel = packageManager.getApplicationLabel(applicationInfo)) == null) {
                return null;
            }
            return applicationLabel.toString();
        } catch (Throwable th) {
            if (x.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    public static Map<String, String> d(Context context) {
        HashMap map;
        if (context == null) {
            return null;
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo.metaData != null) {
                map = new HashMap();
                Object obj = applicationInfo.metaData.get("BUGLY_DISABLE");
                if (obj != null) {
                    map.put("BUGLY_DISABLE", obj.toString());
                }
                Object obj2 = applicationInfo.metaData.get("BUGLY_APPID");
                if (obj2 != null) {
                    map.put("BUGLY_APPID", obj2.toString());
                }
                Object obj3 = applicationInfo.metaData.get("BUGLY_APP_CHANNEL");
                if (obj3 != null) {
                    map.put("BUGLY_APP_CHANNEL", obj3.toString());
                }
                Object obj4 = applicationInfo.metaData.get("BUGLY_APP_VERSION");
                if (obj4 != null) {
                    map.put("BUGLY_APP_VERSION", obj4.toString());
                }
                Object obj5 = applicationInfo.metaData.get("BUGLY_ENABLE_DEBUG");
                if (obj5 != null) {
                    map.put("BUGLY_ENABLE_DEBUG", obj5.toString());
                }
                Object obj6 = applicationInfo.metaData.get("com.tencent.rdm.uuid");
                if (obj6 != null) {
                    map.put("com.tencent.rdm.uuid", obj6.toString());
                }
            } else {
                map = null;
            }
            return map;
        } catch (Throwable th) {
            if (x.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    public static List<String> a(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        try {
            String str = map.get("BUGLY_DISABLE");
            if (str == null || str.length() == 0) {
                return null;
            }
            String[] strArrSplit = str.split(",");
            for (int i = 0; i < strArrSplit.length; i++) {
                strArrSplit[i] = strArrSplit[i].trim();
            }
            return Arrays.asList(strArrSplit);
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private static String a(byte[] bArr) {
        X509Certificate x509Certificate;
        StringBuilder sb = new StringBuilder();
        if (bArr != null && bArr.length > 0) {
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                if (certificateFactory != null && (x509Certificate = (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(bArr))) != null) {
                    sb.append("Issuer|");
                    Principal issuerDN = x509Certificate.getIssuerDN();
                    if (issuerDN != null) {
                        sb.append(issuerDN.toString());
                    } else {
                        sb.append("unknown");
                    }
                    sb.append("\n");
                    sb.append("SerialNumber|");
                    BigInteger serialNumber = x509Certificate.getSerialNumber();
                    if (issuerDN != null) {
                        sb.append(serialNumber.toString(16));
                    } else {
                        sb.append("unknown");
                    }
                    sb.append("\n");
                    sb.append("NotBefore|");
                    Date notBefore = x509Certificate.getNotBefore();
                    if (issuerDN != null) {
                        sb.append(notBefore.toString());
                    } else {
                        sb.append("unknown");
                    }
                    sb.append("\n");
                    sb.append("NotAfter|");
                    Date notAfter = x509Certificate.getNotAfter();
                    if (issuerDN != null) {
                        sb.append(notAfter.toString());
                    } else {
                        sb.append("unknown");
                    }
                    sb.append("\n");
                    sb.append("SHA1|");
                    String strA = z.a(MessageDigest.getInstance("SHA1").digest(x509Certificate.getEncoded()));
                    if (strA != null && strA.length() > 0) {
                        sb.append(strA.toString());
                    } else {
                        sb.append("unknown");
                    }
                    sb.append("\n");
                    sb.append("MD5|");
                    String strA2 = z.a(MessageDigest.getInstance("MD5").digest(x509Certificate.getEncoded()));
                    if (strA2 != null && strA2.length() > 0) {
                        sb.append(strA2.toString());
                    } else {
                        sb.append("unknown");
                    }
                }
                return null;
            } catch (CertificateException e) {
                if (!x.a(e)) {
                    e.printStackTrace();
                }
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
            }
        }
        if (sb.length() == 0) {
            return "unknown";
        }
        return sb.toString();
    }

    public static String e(Context context) throws PackageManager.NameNotFoundException {
        Signature[] signatureArr;
        String strA = a(context);
        if (strA == null) {
            return null;
        }
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(strA, 64);
            if (packageInfo == null || (signatureArr = packageInfo.signatures) == null || signatureArr.length == 0) {
                return null;
            }
            return a(packageInfo.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static boolean f(Context context) {
        if (context == null) {
            return false;
        }
        if (f139a == null) {
            f139a = (ActivityManager) context.getSystemService("activity");
        }
        try {
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            f139a.getMemoryInfo(memoryInfo);
            if (!memoryInfo.lowMemory) {
                return false;
            }
            x.c("Memory is low.", new Object[0]);
            return true;
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return false;
        }
    }
}
