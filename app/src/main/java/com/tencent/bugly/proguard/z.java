package com.tencent.bugly.proguard;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import cn.yunzhisheng.asr.JniUscClient;
import com.tencent.bugly.crashreport.common.info.AppInfo;
import com.tencent.bugly.crashreport.common.info.PlugInBean;
import com.unisound.vui.handler.session.memo.utils.MemoConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public class z {

    /* renamed from: a, reason: collision with root package name */
    private static Map<String, String> f212a = null;
    private static boolean b = false;

    public static String a(Throwable th) {
        if (th == null) {
            return "";
        }
        try {
            StringWriter stringWriter = new StringWriter();
            th.printStackTrace(new PrintWriter(stringWriter));
            return stringWriter.getBuffer().toString();
        } catch (Throwable th2) {
            if (!x.a(th2)) {
                th2.printStackTrace();
            }
            return "fail";
        }
    }

    public static String a() {
        return a(System.currentTimeMillis());
    }

    public static String a(long j) {
        try {
            return new SimpleDateFormat(MemoConstants.DATE_FORMATE_YMDHMS, Locale.US).format(new Date(j));
        } catch (Exception e) {
            return new Date().toString();
        }
    }

    public static String a(Date date) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(MemoConstants.DATE_FORMATE_YMDHMS, Locale.US).format(date);
        } catch (Exception e) {
            return new Date().toString();
        }
    }

    private static byte[] a(byte[] bArr, int i, String str) {
        if (bArr != null && i != -1) {
            x.c("rqdp{  enD:} %d %d", Integer.valueOf(bArr.length), Integer.valueOf(i));
            try {
                ag agVarA = a.a(i);
                if (agVarA == null) {
                    return null;
                }
                agVarA.a(str);
                return agVarA.b(bArr);
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                return null;
            }
        }
        return bArr;
    }

    private static byte[] b(byte[] bArr, int i, String str) {
        if (bArr != null && i != -1) {
            try {
                ag agVarA = a.a(i);
                if (agVarA == null) {
                    return null;
                }
                agVarA.a(str);
                return agVarA.a(bArr);
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                x.d("encrytype %d %s", Integer.valueOf(i), str);
                return null;
            }
        }
        return bArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:22:0x005b A[Catch: all -> 0x007b, TRY_LEAVE, TryCatch #11 {all -> 0x007b, blocks: (B:13:0x0038, B:15:0x0049, B:17:0x004f, B:29:0x0070, B:31:0x0076, B:40:0x008e, B:20:0x0055, B:22:0x005b), top: B:82:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0065 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0060 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0083 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x007e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Object[]] */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.util.zip.ZipOutputStream] */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] a(java.io.File r9, java.lang.String r10, java.lang.String r11) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 232
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.z.a(java.io.File, java.lang.String, java.lang.String):byte[]");
    }

    public static byte[] a(byte[] bArr, int i) {
        if (bArr != null && i != -1) {
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(bArr.length);
            objArr[1] = i == 2 ? "Gzip" : "zip";
            x.c("[Util] Zip %d bytes data with type %s", objArr);
            try {
                ab abVarA = aa.a(i);
                if (abVarA == null) {
                    return null;
                }
                return abVarA.a(bArr);
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                return null;
            }
        }
        return bArr;
    }

    public static byte[] b(byte[] bArr, int i) {
        if (bArr != null && i != -1) {
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(bArr.length);
            objArr[1] = i == 2 ? "Gzip" : "zip";
            x.c("[Util] Unzip %d bytes data with type %s", objArr);
            try {
                ab abVarA = aa.a(i);
                if (abVarA == null) {
                    return null;
                }
                return abVarA.b(bArr);
            } catch (Throwable th) {
                if (!x.a(th)) {
                    th.printStackTrace();
                }
                return null;
            }
        }
        return bArr;
    }

    public static byte[] a(byte[] bArr, int i, int i2, String str) {
        if (bArr == null) {
            return null;
        }
        try {
            return a(a(bArr, 2), 1, str);
        } catch (Throwable th) {
            if (x.a(th)) {
                return null;
            }
            th.printStackTrace();
            return null;
        }
    }

    public static byte[] b(byte[] bArr, int i, int i2, String str) {
        try {
            return b(b(bArr, 1, str), 2);
        } catch (Exception e) {
            if (!x.a(e)) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static long b() {
        try {
            return (((System.currentTimeMillis() + TimeZone.getDefault().getRawOffset()) / 86400000) * 86400000) - TimeZone.getDefault().getRawOffset();
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return -1L;
        }
    }

    public static String a(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b2 : bArr) {
            String hexString = Integer.toHexString(b2 & 255);
            if (hexString.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString().toUpperCase();
    }

    public static String b(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "NULL";
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(bArr);
            return a(messageDigest.digest());
        } catch (Throwable th) {
            if (!x.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:82:0x00e7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x00ec A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean a(java.io.File r6, java.io.File r7, int r8) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 272
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.z.a(java.io.File, java.io.File, int):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:56:0x0090 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0095 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.ArrayList<java.lang.String> a(android.content.Context r6, java.lang.String[] r7) throws java.lang.Throwable {
        /*
            r1 = 0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            com.tencent.bugly.crashreport.common.info.a r2 = com.tencent.bugly.crashreport.common.info.a.a(r6)
            boolean r2 = r2.J()
            if (r2 == 0) goto L20
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.lang.String r1 = new java.lang.String
            java.lang.String r2 = "unknown(low memory)"
            r1.<init>(r2)
            r0.add(r1)
        L1f:
            return r0
        L20:
            java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch: java.lang.Throwable -> L8c java.lang.Throwable -> La8
            java.lang.Process r4 = r2.exec(r7)     // Catch: java.lang.Throwable -> L8c java.lang.Throwable -> La8
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L8c java.lang.Throwable -> La8
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L8c java.lang.Throwable -> La8
            java.io.InputStream r5 = r4.getInputStream()     // Catch: java.lang.Throwable -> L8c java.lang.Throwable -> La8
            r2.<init>(r5)     // Catch: java.lang.Throwable -> L8c java.lang.Throwable -> La8
            r3.<init>(r2)     // Catch: java.lang.Throwable -> L8c java.lang.Throwable -> La8
        L36:
            java.lang.String r2 = r3.readLine()     // Catch: java.lang.Throwable -> L40 java.lang.Throwable -> La3
            if (r2 == 0) goto L57
            r0.add(r2)     // Catch: java.lang.Throwable -> L40 java.lang.Throwable -> La3
            goto L36
        L40:
            r0 = move-exception
            r2 = r1
        L42:
            boolean r4 = com.tencent.bugly.proguard.x.a(r0)     // Catch: java.lang.Throwable -> La5
            if (r4 != 0) goto L4b
            r0.printStackTrace()     // Catch: java.lang.Throwable -> La5
        L4b:
            if (r3 == 0) goto L50
            r3.close()     // Catch: java.io.IOException -> L82
        L50:
            if (r2 == 0) goto L55
            r2.close()     // Catch: java.io.IOException -> L87
        L55:
            r0 = r1
            goto L1f
        L57:
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L40 java.lang.Throwable -> La3
            java.io.InputStreamReader r5 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L40 java.lang.Throwable -> La3
            java.io.InputStream r4 = r4.getErrorStream()     // Catch: java.lang.Throwable -> L40 java.lang.Throwable -> La3
            r5.<init>(r4)     // Catch: java.lang.Throwable -> L40 java.lang.Throwable -> La3
            r2.<init>(r5)     // Catch: java.lang.Throwable -> L40 java.lang.Throwable -> La3
        L65:
            java.lang.String r4 = r2.readLine()     // Catch: java.lang.Throwable -> L6f java.lang.Throwable -> La5
            if (r4 == 0) goto L71
            r0.add(r4)     // Catch: java.lang.Throwable -> L6f java.lang.Throwable -> La5
            goto L65
        L6f:
            r0 = move-exception
            goto L42
        L71:
            r3.close()     // Catch: java.io.IOException -> L7d
        L74:
            r2.close()     // Catch: java.io.IOException -> L78
            goto L1f
        L78:
            r1 = move-exception
            r1.printStackTrace()
            goto L1f
        L7d:
            r1 = move-exception
            r1.printStackTrace()
            goto L74
        L82:
            r0 = move-exception
            r0.printStackTrace()
            goto L50
        L87:
            r0 = move-exception
            r0.printStackTrace()
            goto L55
        L8c:
            r0 = move-exception
            r3 = r1
        L8e:
            if (r3 == 0) goto L93
            r3.close()     // Catch: java.io.IOException -> L99
        L93:
            if (r1 == 0) goto L98
            r1.close()     // Catch: java.io.IOException -> L9e
        L98:
            throw r0
        L99:
            r2 = move-exception
            r2.printStackTrace()
            goto L93
        L9e:
            r1 = move-exception
            r1.printStackTrace()
            goto L98
        La3:
            r0 = move-exception
            goto L8e
        La5:
            r0 = move-exception
            r1 = r2
            goto L8e
        La8:
            r0 = move-exception
            r2 = r1
            r3 = r1
            goto L42
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.z.a(android.content.Context, java.lang.String[]):java.util.ArrayList");
    }

    public static String a(Context context, String str) throws Throwable {
        if (str == null || str.trim().equals("")) {
            return "";
        }
        if (f212a == null) {
            f212a = new HashMap();
            ArrayList<String> arrayListA = a(context, new String[]{"/system/bin/sh", "-c", "getprop"});
            if (arrayListA != null && arrayListA.size() > 0) {
                x.b(z.class, "Successfully get 'getprop' list.", new Object[0]);
                Pattern patternCompile = Pattern.compile("\\[(.+)\\]: \\[(.*)\\]");
                Iterator<String> it = arrayListA.iterator();
                while (it.hasNext()) {
                    Matcher matcher = patternCompile.matcher(it.next());
                    if (matcher.find()) {
                        f212a.put(matcher.group(1), matcher.group(2));
                    }
                }
                x.b(z.class, "System properties number: %d.", Integer.valueOf(f212a.size()));
            }
        }
        if (f212a.containsKey(str)) {
            return f212a.get(str);
        }
        return "fail";
    }

    public static void b(long j) throws InterruptedException {
        try {
            Thread.sleep(j);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean a(String str) {
        return str == null || str.trim().length() <= 0;
    }

    public static void b(String str) {
        if (str != null) {
            File file = new File(str);
            if (file.isFile() && file.exists() && file.canWrite()) {
                file.delete();
            }
        }
    }

    public static byte[] c(long j) {
        try {
            return new StringBuilder().append(j).toString().getBytes(com.unisound.b.f.b);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long c(byte[] bArr) {
        if (bArr == null) {
            return -1L;
        }
        try {
            return Long.parseLong(new String(bArr, com.unisound.b.f.b));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return -1L;
        }
    }

    public static Context a(Context context) {
        Context applicationContext;
        return (context == null || (applicationContext = context.getApplicationContext()) == null) ? context : applicationContext;
    }

    public static String b(Throwable th) {
        if (th == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        printWriter.flush();
        return stringWriter.toString();
    }

    public static void a(Class<?> cls, String str, Object obj, Object obj2) throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            declaredField.set(null, obj);
        } catch (Exception e) {
        }
    }

    public static Object a(String str, String str2, Object obj, Class<?>[] clsArr, Object[] objArr) throws NoSuchMethodException, SecurityException {
        try {
            Method declaredMethod = Class.forName(str).getDeclaredMethod(str2, clsArr);
            declaredMethod.setAccessible(true);
            return declaredMethod.invoke(null, objArr);
        } catch (Exception e) {
            return null;
        }
    }

    public static void a(Parcel parcel, Map<String, PlugInBean> map) {
        if (map == null || map.size() <= 0) {
            parcel.writeBundle(null);
            return;
        }
        int size = map.size();
        ArrayList arrayList = new ArrayList(size);
        ArrayList arrayList2 = new ArrayList(size);
        for (Map.Entry<String, PlugInBean> entry : map.entrySet()) {
            arrayList.add(entry.getKey());
            arrayList2.add(entry.getValue());
        }
        Bundle bundle = new Bundle();
        bundle.putInt("pluginNum", arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            bundle.putString("pluginKey" + i, (String) arrayList.get(i));
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            bundle.putString("pluginVal" + i2 + "plugInId", ((PlugInBean) arrayList2.get(i2)).f140a);
            bundle.putString("pluginVal" + i2 + "plugInUUID", ((PlugInBean) arrayList2.get(i2)).c);
            bundle.putString("pluginVal" + i2 + "plugInVersion", ((PlugInBean) arrayList2.get(i2)).b);
        }
        parcel.writeBundle(bundle);
    }

    public static Map<String, PlugInBean> a(Parcel parcel) {
        HashMap map;
        Bundle bundle = parcel.readBundle();
        if (bundle == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        int iIntValue = ((Integer) bundle.get("pluginNum")).intValue();
        for (int i = 0; i < iIntValue; i++) {
            arrayList.add(bundle.getString("pluginKey" + i));
        }
        for (int i2 = 0; i2 < iIntValue; i2++) {
            arrayList2.add(new PlugInBean(bundle.getString("pluginVal" + i2 + "plugInId"), bundle.getString("pluginVal" + i2 + "plugInVersion"), bundle.getString("pluginVal" + i2 + "plugInUUID")));
        }
        if (arrayList.size() == arrayList2.size()) {
            HashMap map2 = new HashMap(arrayList.size());
            for (int i3 = 0; i3 < arrayList.size(); i3++) {
                map2.put(arrayList.get(i3), PlugInBean.class.cast(arrayList2.get(i3)));
            }
            map = map2;
        } else {
            x.e("map plugin parcel error!", new Object[0]);
            map = null;
        }
        return map;
    }

    public static void b(Parcel parcel, Map<String, String> map) {
        if (map == null || map.size() <= 0) {
            parcel.writeBundle(null);
            return;
        }
        int size = map.size();
        ArrayList<String> arrayList = new ArrayList<>(size);
        ArrayList<String> arrayList2 = new ArrayList<>(size);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            arrayList.add(entry.getKey());
            arrayList2.add(entry.getValue());
        }
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("keys", arrayList);
        bundle.putStringArrayList("values", arrayList2);
        parcel.writeBundle(bundle);
    }

    public static Map<String, String> b(Parcel parcel) {
        HashMap map;
        Bundle bundle = parcel.readBundle();
        if (bundle == null) {
            return null;
        }
        ArrayList<String> stringArrayList = bundle.getStringArrayList("keys");
        ArrayList<String> stringArrayList2 = bundle.getStringArrayList("values");
        if (stringArrayList != null && stringArrayList2 != null && stringArrayList.size() == stringArrayList2.size()) {
            HashMap map2 = new HashMap(stringArrayList.size());
            for (int i = 0; i < stringArrayList.size(); i++) {
                map2.put(stringArrayList.get(i), stringArrayList2.get(i));
            }
            map = map2;
        } else {
            x.e("map parcel error!", new Object[0]);
            map = null;
        }
        return map;
    }

    public static byte[] a(Parcelable parcelable) {
        Parcel parcelObtain = Parcel.obtain();
        parcelable.writeToParcel(parcelObtain, 0);
        byte[] bArrMarshall = parcelObtain.marshall();
        parcelObtain.recycle();
        return bArrMarshall;
    }

    public static <T> T a(byte[] bArr, Parcelable.Creator<T> creator) {
        T tCreateFromParcel;
        Parcel parcelObtain = Parcel.obtain();
        parcelObtain.unmarshall(bArr, 0, bArr.length);
        parcelObtain.setDataPosition(0);
        try {
            try {
                tCreateFromParcel = creator.createFromParcel(parcelObtain);
                if (parcelObtain != null) {
                    parcelObtain.recycle();
                }
            } catch (Throwable th) {
                th.printStackTrace();
                if (parcelObtain != null) {
                    parcelObtain.recycle();
                }
                tCreateFromParcel = null;
            }
            return tCreateFromParcel;
        } catch (Throwable th2) {
            if (parcelObtain != null) {
                parcelObtain.recycle();
            }
            throw th2;
        }
    }

    public static String a(Context context, int i, String str) throws Throwable {
        if (!AppInfo.a(context, "android.permission.READ_LOGS")) {
            x.d("no read_log permission!", new Object[0]);
            return null;
        }
        String[] strArr = str == null ? new String[]{"logcat", "-d", "-v", "threadtime"} : new String[]{"logcat", "-d", "-v", "threadtime", "-s", str};
        Process process = null;
        StringBuilder sb = new StringBuilder();
        try {
            try {
                Process processExec = Runtime.getRuntime().exec(strArr);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
                    while (true) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        sb.append(line).append("\n");
                        if (i > 0 && sb.length() > i) {
                            sb.delete(0, sb.length() - i);
                        }
                    }
                    String string = sb.toString();
                    if (processExec == null) {
                        return string;
                    }
                    try {
                        processExec.getOutputStream().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        processExec.getInputStream().close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        processExec.getErrorStream().close();
                        return string;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        return string;
                    }
                } catch (Throwable th) {
                    th = th;
                    process = processExec;
                    if (!x.a(th)) {
                        th.printStackTrace();
                    }
                    String string2 = sb.append("\n[error:" + th.toString() + "]").toString();
                    if (process == null) {
                        return string2;
                    }
                    try {
                        process.getOutputStream().close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                    try {
                        process.getInputStream().close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                    try {
                        process.getErrorStream().close();
                        return string2;
                    } catch (IOException e6) {
                        e6.printStackTrace();
                        return string2;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public static Map<String, String> a(int i, boolean z) {
        HashMap map = new HashMap(12);
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        if (allStackTraces == null) {
            return null;
        }
        Thread.currentThread().getId();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
            sb.setLength(0);
            if (entry.getValue() != null && entry.getValue().length != 0) {
                StackTraceElement[] value = entry.getValue();
                int length = value.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    StackTraceElement stackTraceElement = value[i2];
                    if (i > 0 && sb.length() >= i) {
                        sb.append("\n[Stack over limit size :" + i + " , has been cutted !]");
                        break;
                    }
                    sb.append(stackTraceElement.toString()).append("\n");
                    i2++;
                }
                map.put(entry.getKey().getName() + "(" + entry.getKey().getId() + ")", sb.toString());
            }
        }
        return map;
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x001f, code lost:
    
        r0 = null;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8, types: [java.io.DataInputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static synchronized byte[] a(int r7) {
        /*
            r1 = 0
            java.lang.Class<com.tencent.bugly.proguard.z> r3 = com.tencent.bugly.proguard.z.class
            monitor-enter(r3)
            r0 = 16
            byte[] r0 = new byte[r0]     // Catch: java.lang.Exception -> L21 java.lang.Throwable -> L4c
            java.io.DataInputStream r2 = new java.io.DataInputStream     // Catch: java.lang.Exception -> L21 java.lang.Throwable -> L4c
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Exception -> L21 java.lang.Throwable -> L4c
            java.io.File r5 = new java.io.File     // Catch: java.lang.Exception -> L21 java.lang.Throwable -> L4c
            java.lang.String r6 = "/dev/urandom"
            r5.<init>(r6)     // Catch: java.lang.Exception -> L21 java.lang.Throwable -> L4c
            r4.<init>(r5)     // Catch: java.lang.Exception -> L21 java.lang.Throwable -> L4c
            r2.<init>(r4)     // Catch: java.lang.Exception -> L21 java.lang.Throwable -> L4c
            r2.readFully(r0)     // Catch: java.lang.Throwable -> L63 java.lang.Exception -> L65
            r2.close()     // Catch: java.lang.Exception -> L54 java.lang.Throwable -> L60
        L1f:
            monitor-exit(r3)
            return r0
        L21:
            r0 = move-exception
            r2 = r1
        L23:
            java.lang.String r4 = "Failed to read from /dev/urandom : %s"
            r5 = 1
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> L63
            r6 = 0
            r5[r6] = r0     // Catch: java.lang.Throwable -> L63
            com.tencent.bugly.proguard.x.e(r4, r5)     // Catch: java.lang.Throwable -> L63
            if (r2 == 0) goto L33
            r2.close()     // Catch: java.lang.Exception -> L54 java.lang.Throwable -> L60
        L33:
            java.lang.String r0 = "AES"
            javax.crypto.KeyGenerator r0 = javax.crypto.KeyGenerator.getInstance(r0)     // Catch: java.lang.Exception -> L54 java.lang.Throwable -> L60
            r2 = 128(0x80, float:1.794E-43)
            java.security.SecureRandom r4 = new java.security.SecureRandom     // Catch: java.lang.Exception -> L54 java.lang.Throwable -> L60
            r4.<init>()     // Catch: java.lang.Exception -> L54 java.lang.Throwable -> L60
            r0.init(r2, r4)     // Catch: java.lang.Exception -> L54 java.lang.Throwable -> L60
            javax.crypto.SecretKey r0 = r0.generateKey()     // Catch: java.lang.Exception -> L54 java.lang.Throwable -> L60
            byte[] r0 = r0.getEncoded()     // Catch: java.lang.Exception -> L54 java.lang.Throwable -> L60
            goto L1f
        L4c:
            r0 = move-exception
            r2 = r1
        L4e:
            if (r2 == 0) goto L53
            r2.close()     // Catch: java.lang.Exception -> L54 java.lang.Throwable -> L60
        L53:
            throw r0     // Catch: java.lang.Exception -> L54 java.lang.Throwable -> L60
        L54:
            r0 = move-exception
            boolean r2 = com.tencent.bugly.proguard.x.b(r0)     // Catch: java.lang.Throwable -> L60
            if (r2 != 0) goto L5e
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L60
        L5e:
            r0 = r1
            goto L1f
        L60:
            r0 = move-exception
            monitor-exit(r3)
            throw r0
        L63:
            r0 = move-exception
            goto L4e
        L65:
            r0 = move-exception
            goto L23
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.z.a(int):byte[]");
    }

    @TargetApi(19)
    public static byte[] a(int i, byte[] bArr, byte[] bArr2) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            if (Build.VERSION.SDK_INT < 21 || b) {
                cipher.init(i, secretKeySpec, new IvParameterSpec(bArr2));
            } else {
                try {
                    cipher.init(i, secretKeySpec, new GCMParameterSpec(cipher.getBlockSize() << 3, bArr2));
                } catch (InvalidAlgorithmParameterException e) {
                    b = true;
                    throw e;
                }
            }
            return cipher.doFinal(bArr);
        } catch (Exception e2) {
            if (!x.b(e2)) {
                e2.printStackTrace();
            }
            return null;
        }
    }

    public static byte[] b(int i, byte[] bArr, byte[] bArr2) throws InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            PublicKey publicKeyGeneratePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bArr2));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, publicKeyGeneratePublic);
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            if (!x.b(e)) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static boolean a(Context context, String str, long j) {
        x.c("[Util] try to lock file:%s (pid=%d | tid=%d)", str, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        try {
            File file = new File(context.getFilesDir() + File.separator + str);
            if (file.exists()) {
                if (System.currentTimeMillis() - file.lastModified() < j) {
                    return false;
                }
                x.c("[Util] lock file(%s) is expired, unlock it", str);
                b(context, str);
            }
            if (file.createNewFile()) {
                x.c("[Util] successfully locked file:%s (pid=%d | tid=%d)", str, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
                return true;
            }
            x.c("[Util] Failed to locked file:%s (pid=%d | tid=%d)", str, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            return false;
        } catch (Throwable th) {
            x.a(th);
            return false;
        }
    }

    public static boolean b(Context context, String str) {
        x.c("[Util] try to unlock file:%s (pid=%d | tid=%d)", str, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
        try {
            File file = new File(context.getFilesDir() + File.separator + str);
            if (!file.exists()) {
                return true;
            }
            if (!file.delete()) {
                return false;
            }
            x.c("[Util] successfully unlocked file:%s (pid=%d | tid=%d)", str, Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()));
            return true;
        } catch (Throwable th) {
            x.a(th);
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x0056 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String a(java.io.File r6) throws java.lang.Throwable {
        /*
            r0 = 0
            if (r6 == 0) goto Lf
            boolean r1 = r6.exists()
            if (r1 == 0) goto Lf
            boolean r1 = r6.canRead()
            if (r1 != 0) goto L10
        Lf:
            return r0
        L10:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L61
            r1.<init>()     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L61
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L61
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L61
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L61
            r4.<init>(r6)     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L61
            java.lang.String r5 = "utf-8"
            r3.<init>(r4, r5)     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L61
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L51 java.lang.Throwable -> L61
        L26:
            java.lang.String r3 = r2.readLine()     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L5f
            if (r3 == 0) goto L44
            r1.append(r3)     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L5f
            java.lang.String r3 = "\n"
            r1.append(r3)     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L5f
            goto L26
        L35:
            r1 = move-exception
        L36:
            com.tencent.bugly.proguard.x.a(r1)     // Catch: java.lang.Throwable -> L5f
            if (r2 == 0) goto Lf
            r2.close()     // Catch: java.lang.Exception -> L3f
            goto Lf
        L3f:
            r1 = move-exception
            com.tencent.bugly.proguard.x.a(r1)
            goto Lf
        L44:
            java.lang.String r0 = r1.toString()     // Catch: java.lang.Throwable -> L35 java.lang.Throwable -> L5f
            r2.close()     // Catch: java.lang.Exception -> L4c
            goto Lf
        L4c:
            r1 = move-exception
            com.tencent.bugly.proguard.x.a(r1)
            goto Lf
        L51:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L54:
            if (r2 == 0) goto L59
            r2.close()     // Catch: java.lang.Exception -> L5a
        L59:
            throw r0
        L5a:
            r1 = move-exception
            com.tencent.bugly.proguard.x.a(r1)
            goto L59
        L5f:
            r0 = move-exception
            goto L54
        L61:
            r1 = move-exception
            r2 = r0
            goto L36
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.z.a(java.io.File):java.lang.String");
    }

    private static BufferedReader b(File file) {
        if (file == null || !file.exists() || !file.canRead()) {
            return null;
        }
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(file), com.unisound.b.f.b));
        } catch (Throwable th) {
            x.a(th);
            return null;
        }
    }

    public static BufferedReader a(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            File file = new File(str, str2);
            if (file.exists() && file.canRead()) {
                return b(file);
            }
            return null;
        } catch (NullPointerException e) {
            x.a(e);
            return null;
        }
    }

    public static Thread a(Runnable runnable, String str) {
        try {
            Thread thread = new Thread(runnable);
            thread.setName(str);
            thread.start();
            return thread;
        } catch (Throwable th) {
            x.e("[Util] Failed to start a thread to execute task with message: %s", th.getMessage());
            return null;
        }
    }

    public static boolean a(Runnable runnable) {
        if (runnable != null) {
            w wVarA = w.a();
            if (wVarA != null) {
                return wVarA.a(runnable);
            }
            if (a(runnable, runnable.getClass().getName().split("\\.")[r0.length - 1]) != null) {
                return true;
            }
        }
        return false;
    }

    public static boolean c(String str) {
        if (str == null || str.trim().length() <= 0) {
            return false;
        }
        if (str.length() > 255) {
            x.a("URL(%s)'s length is larger than 255.", str);
            return false;
        }
        if (!str.toLowerCase().startsWith("http")) {
            x.a("URL(%s) is not start with \"http\".", str);
            return false;
        }
        if (str.toLowerCase().contains("qq.com")) {
            return true;
        }
        x.a("URL(%s) does not contain \"qq.com\".", str);
        return false;
    }

    public static SharedPreferences a(String str, Context context) {
        if (context != null) {
            return context.getSharedPreferences(str, 0);
        }
        return null;
    }

    public static String b(String str, String str2) {
        return (com.tencent.bugly.crashreport.common.info.a.b() == null || com.tencent.bugly.crashreport.common.info.a.b().E == null) ? "" : com.tencent.bugly.crashreport.common.info.a.b().E.getString(str, str2);
    }

    public static String d(byte[] bArr) {
        if (bArr == null) {
            return JniUscClient.az;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bArr.length; i++) {
            if (i != 0) {
                stringBuffer.append(':');
            }
            String hexString = Integer.toHexString(bArr[i] & 255);
            if (hexString.length() == 1) {
                hexString = "0" + hexString;
            }
            stringBuffer.append(hexString);
        }
        return stringBuffer.toString().toUpperCase();
    }
}
