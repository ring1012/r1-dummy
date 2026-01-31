package com.baidu.a.a.a.b;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.system.ErrnoException;
import android.system.Os;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import cn.yunzhisheng.common.PinyinConverter;
import com.unisound.b.f;
import com.unisound.common.x;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class c {

    /* renamed from: a, reason: collision with root package name */
    private static final String f35a = new String(com.baidu.a.a.a.a.b.a(new byte[]{77, 122, 65, 121, 77, 84, 73, 120, 77, 68, 73, 61})) + new String(com.baidu.a.a.a.a.b.a(new byte[]{90, 71, 108, 106, 100, 87, 82, 112, 89, 87, 73, 61}));
    private static b e;
    private final Context b;
    private int c = 0;
    private PublicKey d;

    /* JADX INFO: Access modifiers changed from: private */
    static class a {

        /* renamed from: a, reason: collision with root package name */
        public ApplicationInfo f36a;
        public int b;
        public boolean c;
        public boolean d;

        private a() {
            this.b = 0;
            this.c = false;
            this.d = false;
        }

        /* synthetic */ a(d dVar) {
            this();
        }
    }

    private static class b {

        /* renamed from: a, reason: collision with root package name */
        public String f37a;
        public String b;
        public int c;

        private b() {
            this.c = 2;
        }

        /* synthetic */ b(d dVar) {
            this();
        }

        public static b a(String str) throws JSONException {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                String string = jSONObject.getString("deviceid");
                String string2 = jSONObject.getString(x.b);
                int i = jSONObject.getInt("ver");
                if (TextUtils.isEmpty(string) || string2 == null) {
                    return null;
                }
                b bVar = new b();
                bVar.f37a = string;
                bVar.b = string2;
                bVar.c = i;
                return bVar;
            } catch (JSONException e) {
                c.b(e);
                return null;
            }
        }

        public String a() {
            try {
                return new JSONObject().put("deviceid", this.f37a).put(x.b, this.b).put("ver", this.c).toString();
            } catch (JSONException e) {
                c.b(e);
                return null;
            }
        }

        public String b() {
            String str = this.b;
            if (TextUtils.isEmpty(str)) {
                str = "0";
            }
            return this.f37a + PinyinConverter.PINYIN_EXCLUDE + new StringBuffer(str).reverse().toString();
        }
    }

    /* renamed from: com.baidu.a.a.a.b.c$c, reason: collision with other inner class name */
    static class C0003c {
        static boolean a(String str, int i) throws ErrnoException {
            try {
                Os.chmod(str, i);
                return true;
            } catch (ErrnoException e) {
                c.b(e);
                return false;
            }
        }
    }

    private c(Context context) throws Throwable {
        this.b = context.getApplicationContext();
        a();
    }

    public static String a(Context context) {
        return c(context).b();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:41:0x003d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.io.FileReader] */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v11 */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v8 */
    /* JADX WARN: Type inference failed for: r0v9 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.lang.String a(java.io.File r6) throws java.lang.Throwable {
        /*
            r0 = 0
            java.io.FileReader r2 = new java.io.FileReader     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L48
            r2.<init>(r6)     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L48
            r1 = 8192(0x2000, float:1.14794E-41)
            char[] r1 = new char[r1]     // Catch: java.lang.Exception -> L1a java.lang.Throwable -> L46
            java.io.CharArrayWriter r3 = new java.io.CharArrayWriter     // Catch: java.lang.Exception -> L1a java.lang.Throwable -> L46
            r3.<init>()     // Catch: java.lang.Exception -> L1a java.lang.Throwable -> L46
        Lf:
            int r4 = r2.read(r1)     // Catch: java.lang.Exception -> L1a java.lang.Throwable -> L46
            if (r4 <= 0) goto L24
            r5 = 0
            r3.write(r1, r5, r4)     // Catch: java.lang.Exception -> L1a java.lang.Throwable -> L46
            goto Lf
        L1a:
            r1 = move-exception
        L1b:
            b(r1)     // Catch: java.lang.Throwable -> L46
            if (r2 == 0) goto L23
            r2.close()     // Catch: java.lang.Exception -> L33
        L23:
            return r0
        L24:
            java.lang.String r0 = r3.toString()     // Catch: java.lang.Exception -> L1a java.lang.Throwable -> L46
            if (r2 == 0) goto L23
            r2.close()     // Catch: java.lang.Exception -> L2e
            goto L23
        L2e:
            r1 = move-exception
            b(r1)
            goto L23
        L33:
            r1 = move-exception
            b(r1)
            goto L23
        L38:
            r1 = move-exception
            r2 = r0
            r0 = r1
        L3b:
            if (r2 == 0) goto L40
            r2.close()     // Catch: java.lang.Exception -> L41
        L40:
            throw r0
        L41:
            r1 = move-exception
            b(r1)
            goto L40
        L46:
            r0 = move-exception
            goto L3b
        L48:
            r1 = move-exception
            r2 = r0
            goto L1b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.a.a.a.b.c.a(java.io.File):java.lang.String");
    }

    private static String a(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("Argument b ( byte array ) is null! ");
        }
        String str = "";
        for (byte b2 : bArr) {
            String hexString = Integer.toHexString(b2 & 255);
            str = hexString.length() == 1 ? str + "0" + hexString : str + hexString;
        }
        return str.toLowerCase();
    }

    private List<a> a(Intent intent, boolean z) throws JSONException, PackageManager.NameNotFoundException {
        ArrayList arrayList = new ArrayList();
        PackageManager packageManager = this.b.getPackageManager();
        List<ResolveInfo> listQueryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent, 0);
        if (listQueryBroadcastReceivers != null) {
            for (ResolveInfo resolveInfo : listQueryBroadcastReceivers) {
                if (resolveInfo.activityInfo != null && resolveInfo.activityInfo.applicationInfo != null) {
                    try {
                        Bundle bundle = packageManager.getReceiverInfo(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name), 128).metaData;
                        if (bundle != null) {
                            String string = bundle.getString("galaxy_data");
                            if (!TextUtils.isEmpty(string)) {
                                byte[] bArrA = com.baidu.a.a.a.a.b.a(string.getBytes(f.b));
                                JSONObject jSONObject = new JSONObject(new String(bArrA));
                                a aVar = new a(null);
                                aVar.b = jSONObject.getInt("priority");
                                aVar.f36a = resolveInfo.activityInfo.applicationInfo;
                                if (this.b.getPackageName().equals(resolveInfo.activityInfo.applicationInfo.packageName)) {
                                    aVar.d = true;
                                }
                                if (z) {
                                    String string2 = bundle.getString("galaxy_sf");
                                    if (!TextUtils.isEmpty(string2)) {
                                        PackageInfo packageInfo = packageManager.getPackageInfo(resolveInfo.activityInfo.applicationInfo.packageName, 64);
                                        JSONArray jSONArray = jSONObject.getJSONArray("sigs");
                                        String[] strArr = new String[jSONArray.length()];
                                        for (int i = 0; i < strArr.length; i++) {
                                            strArr[i] = jSONArray.getString(i);
                                        }
                                        if (a(strArr, a(packageInfo.signatures))) {
                                            byte[] bArrA2 = a(com.baidu.a.a.a.a.b.a(string2.getBytes()), this.d);
                                            if (bArrA2 != null && Arrays.equals(bArrA2, com.baidu.a.a.a.a.d.a(bArrA))) {
                                                aVar.c = true;
                                            }
                                        }
                                    }
                                }
                                arrayList.add(aVar);
                            }
                        }
                    } catch (Exception e2) {
                    }
                }
            }
        }
        Collections.sort(arrayList, new d(this));
        return arrayList;
    }

    private void a() throws Throwable {
        ByteArrayInputStream byteArrayInputStream;
        ByteArrayInputStream byteArrayInputStream2 = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(com.baidu.a.a.a.b.b.a());
            try {
                this.d = CertificateFactory.getInstance("X.509").generateCertificate(byteArrayInputStream).getPublicKey();
                if (byteArrayInputStream != null) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Exception e2) {
                        b(e2);
                    }
                }
            } catch (Exception e3) {
                if (byteArrayInputStream != null) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Exception e4) {
                        b(e4);
                    }
                }
            } catch (Throwable th) {
                byteArrayInputStream2 = byteArrayInputStream;
                th = th;
                if (byteArrayInputStream2 != null) {
                    try {
                        byteArrayInputStream2.close();
                    } catch (Exception e5) {
                        b(e5);
                    }
                }
                throw th;
            }
        } catch (Exception e6) {
            byteArrayInputStream = null;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0056 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    @android.annotation.SuppressLint({"NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean a(java.lang.String r7) throws java.lang.Throwable {
        /*
            r6 = this;
            r2 = 1
            r1 = 0
            r3 = 0
            int r0 = android.os.Build.VERSION.SDK_INT
            r4 = 24
            if (r0 < r4) goto L3b
            r0 = r1
        La:
            android.content.Context r4 = r6.b     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L53
            java.lang.String r5 = "libcuid.so"
            java.io.FileOutputStream r3 = r4.openFileOutput(r5, r0)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L53
            byte[] r4 = r7.getBytes()     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L62
            r3.write(r4)     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L62
            r3.flush()     // Catch: java.lang.Throwable -> L53 java.lang.Exception -> L62
            if (r3 == 0) goto L21
            r3.close()     // Catch: java.lang.Exception -> L3d
        L21:
            if (r0 != 0) goto L3a
            r0 = 436(0x1b4, float:6.11E-43)
            java.io.File r1 = new java.io.File
            android.content.Context r2 = r6.b
            java.io.File r2 = r2.getFilesDir()
            java.lang.String r3 = "libcuid.so"
            r1.<init>(r2, r3)
            java.lang.String r1 = r1.getAbsolutePath()
            boolean r2 = com.baidu.a.a.a.b.c.C0003c.a(r1, r0)
        L3a:
            return r2
        L3b:
            r0 = r2
            goto La
        L3d:
            r1 = move-exception
            b(r1)
            goto L21
        L42:
            r0 = move-exception
            r2 = r3
        L44:
            b(r0)     // Catch: java.lang.Throwable -> L5f
            if (r2 == 0) goto L4c
            r2.close()     // Catch: java.lang.Exception -> L4e
        L4c:
            r2 = r1
            goto L3a
        L4e:
            r0 = move-exception
            b(r0)
            goto L4c
        L53:
            r0 = move-exception
        L54:
            if (r3 == 0) goto L59
            r3.close()     // Catch: java.lang.Exception -> L5a
        L59:
            throw r0
        L5a:
            r1 = move-exception
            b(r1)
            goto L59
        L5f:
            r0 = move-exception
            r3 = r2
            goto L54
        L62:
            r0 = move-exception
            r2 = r3
            goto L44
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.a.a.a.b.c.a(java.lang.String):boolean");
    }

    private boolean a(String str, String str2) {
        try {
            return Settings.System.putString(this.b.getContentResolver(), str, str2);
        } catch (Exception e2) {
            b(e2);
            return false;
        }
    }

    private boolean a(String[] strArr, String[] strArr2) {
        if (strArr == null || strArr2 == null || strArr.length != strArr2.length) {
            return false;
        }
        HashSet hashSet = new HashSet();
        for (String str : strArr) {
            hashSet.add(str);
        }
        HashSet hashSet2 = new HashSet();
        for (String str2 : strArr2) {
            hashSet2.add(str2);
        }
        return hashSet.equals(hashSet2);
    }

    private static byte[] a(byte[] bArr, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(2, publicKey);
        return cipher.doFinal(bArr);
    }

    private String[] a(Signature[] signatureArr) {
        String[] strArr = new String[signatureArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = a(com.baidu.a.a.a.a.d.a(signatureArr[i].toByteArray()));
        }
        return strArr;
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x026e A[PHI: r3
  0x026e: PHI (r3v6 com.baidu.a.a.a.b.c$b) = (r3v5 com.baidu.a.a.a.b.c$b), (r3v5 com.baidu.a.a.a.b.c$b), (r3v20 com.baidu.a.a.a.b.c$b) binds: [B:13:0x004e, B:15:0x0061, B:107:0x026e] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.baidu.a.a.a.b.c.b b() throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 634
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.a.a.a.b.c.b():com.baidu.a.a.a.b.c$b");
    }

    public static String b(Context context) {
        String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
        return TextUtils.isEmpty(string) ? "" : string;
    }

    private String b(String str) {
        try {
            return Settings.System.getString(this.b.getContentResolver(), str);
        } catch (Exception e2) {
            b(e2);
            return null;
        }
    }

    private static void b(String str, String str2) {
        File file;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        File file2 = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig");
        File file3 = new File(file2, ".cuid");
        try {
            if (file2.exists() && !file2.isDirectory()) {
                Random random = new Random();
                File parentFile = file2.getParentFile();
                String name = file2.getName();
                do {
                    file = new File(parentFile, name + random.nextInt() + ".tmp");
                } while (file.exists());
                file2.renameTo(file);
                file.delete();
            }
            file2.mkdirs();
            FileWriter fileWriter = new FileWriter(file3, false);
            fileWriter.write(com.baidu.a.a.a.a.b.a(com.baidu.a.a.a.a.a.a(f35a, f35a, (str + "=" + str2).getBytes()), f.b));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e2) {
        } catch (Exception e3) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(Throwable th) {
    }

    private static b c(Context context) {
        if (e == null) {
            synchronized (b.class) {
                if (e == null) {
                    SystemClock.uptimeMillis();
                    e = new c(context).b();
                    SystemClock.uptimeMillis();
                }
            }
        }
        return e;
    }

    private boolean c() {
        return c("android.permission.WRITE_SETTINGS");
    }

    private boolean c(String str) {
        return this.b.checkPermission(str, Process.myPid(), Process.myUid()) == 0;
    }

    private b d() {
        d dVar = null;
        String strB = b("com.baidu.deviceid");
        String strB2 = b("bd_setting_i");
        if (TextUtils.isEmpty(strB2)) {
            strB2 = h("");
            if (!TextUtils.isEmpty(strB2)) {
                a("bd_setting_i", strB2);
            }
        }
        if (TextUtils.isEmpty(strB)) {
            strB = b(com.baidu.a.a.a.a.c.a(("com.baidu" + strB2 + b(this.b)).getBytes(), true));
        }
        if (TextUtils.isEmpty(strB)) {
            return null;
        }
        b bVar = new b(dVar);
        bVar.f37a = strB;
        bVar.b = strB2;
        return bVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x00c3  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00a0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.baidu.a.a.a.b.c.b d(java.lang.String r9) throws java.io.IOException {
        /*
            r8 = this;
            r2 = 0
            r3 = 0
            r4 = 1
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 23
            if (r0 >= r1) goto L14
            r5 = r4
        La:
            if (r5 == 0) goto L16
            boolean r0 = android.text.TextUtils.isEmpty(r9)
            if (r0 == 0) goto L16
            r0 = r2
        L13:
            return r0
        L14:
            r5 = r3
            goto La
        L16:
            java.lang.String r0 = ""
            java.io.File r1 = new java.io.File
            java.io.File r6 = android.os.Environment.getExternalStorageDirectory()
            java.lang.String r7 = "baidu/.cuid"
            r1.<init>(r6, r7)
            boolean r6 = r1.exists()
            if (r6 == 0) goto L5a
        L29:
            java.io.FileReader r4 = new java.io.FileReader     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            r4.<init>(r1)     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            r1.<init>(r4)     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            r4.<init>()     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
        L38:
            java.lang.String r6 = r1.readLine()     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            if (r6 == 0) goto L67
            r4.append(r6)     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            java.lang.String r6 = "\r\n"
            r4.append(r6)     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            goto L38
        L47:
            r1 = move-exception
            r1 = r9
        L49:
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 != 0) goto Lb6
            com.baidu.a.a.a.b.c$b r3 = new com.baidu.a.a.a.b.c$b
            r3.<init>(r2)
            r3.f37a = r0
            r3.b = r1
            r0 = r3
            goto L13
        L5a:
            java.io.File r1 = new java.io.File
            java.io.File r3 = android.os.Environment.getExternalStorageDirectory()
            java.lang.String r6 = "backups/.SystemConfig/.cuid"
            r1.<init>(r3, r6)
            r3 = r4
            goto L29
        L67:
            r1.close()     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            java.lang.String r1 = new java.lang.String     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            java.lang.String r6 = com.baidu.a.a.a.b.c.f35a     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            java.lang.String r7 = com.baidu.a.a.a.b.c.f35a     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            java.lang.String r4 = r4.toString()     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            byte[] r4 = r4.getBytes()     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            byte[] r4 = com.baidu.a.a.a.a.b.a(r4)     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            byte[] r4 = com.baidu.a.a.a.a.a.b(r6, r7, r4)     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            r1.<init>(r4)     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            java.lang.String r4 = "="
            java.lang.String[] r1 = r1.split(r4)     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            if (r1 == 0) goto Lc3
            int r4 = r1.length     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            r6 = 2
            if (r4 != r6) goto Lc3
            if (r5 == 0) goto La6
            r4 = 0
            r4 = r1[r4]     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            boolean r4 = r9.equals(r4)     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            if (r4 == 0) goto La6
            r4 = 1
            r0 = r1[r4]     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            r1 = r9
        L9e:
            if (r3 != 0) goto L49
            b(r1, r0)     // Catch: java.io.FileNotFoundException -> La4 java.lang.Exception -> Lbc java.io.IOException -> Lc1
            goto L49
        La4:
            r3 = move-exception
            goto L49
        La6:
            if (r5 != 0) goto Lc3
            boolean r4 = android.text.TextUtils.isEmpty(r9)     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            if (r4 == 0) goto Lb1
            r4 = 1
            r9 = r1[r4]     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
        Lb1:
            r4 = 1
            r0 = r1[r4]     // Catch: java.io.FileNotFoundException -> L47 java.lang.Exception -> Lb9 java.io.IOException -> Lbe
            r1 = r9
            goto L9e
        Lb6:
            r0 = r2
            goto L13
        Lb9:
            r1 = move-exception
            r1 = r9
            goto L49
        Lbc:
            r3 = move-exception
            goto L49
        Lbe:
            r1 = move-exception
            r1 = r9
            goto L49
        Lc1:
            r3 = move-exception
            goto L49
        Lc3:
            r1 = r9
            goto L9e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.a.a.a.b.c.d(java.lang.String):com.baidu.a.a.a.b.c$b");
    }

    private b e() throws Throwable {
        File file = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig/.cuid2");
        if (file.exists()) {
            String strA = a(file);
            if (!TextUtils.isEmpty(strA)) {
                try {
                    return b.a(new String(com.baidu.a.a.a.a.a.b(f35a, f35a, com.baidu.a.a.a.a.b.a(strA.getBytes()))));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String e(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return com.baidu.a.a.a.a.b.a(com.baidu.a.a.a.a.a.a(f35a, f35a, str.getBytes()), f.b);
        } catch (UnsupportedEncodingException e2) {
            b(e2);
            return "";
        } catch (Exception e3) {
            b(e3);
            return "";
        }
    }

    private static String f(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return new String(com.baidu.a.a.a.a.a.b(f35a, f35a, com.baidu.a.a.a.a.b.a(str.getBytes())));
        } catch (Exception e2) {
            b(e2);
            return "";
        }
    }

    private static void g(String str) {
        File file;
        File file2 = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig");
        File file3 = new File(file2, ".cuid2");
        try {
            if (file2.exists() && !file2.isDirectory()) {
                Random random = new Random();
                File parentFile = file2.getParentFile();
                String name = file2.getName();
                do {
                    file = new File(parentFile, name + random.nextInt() + ".tmp");
                } while (file.exists());
                file2.renameTo(file);
                file.delete();
            }
            file2.mkdirs();
            FileWriter fileWriter = new FileWriter(file3, false);
            fileWriter.write(str);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e2) {
        } catch (Exception e3) {
        }
    }

    private String h(String str) {
        TelephonyManager telephonyManager;
        try {
            telephonyManager = (TelephonyManager) this.b.getSystemService("phone");
        } catch (Exception e2) {
            Log.e("DeviceId", "Read IMEI failed", e2);
        }
        String deviceId = telephonyManager != null ? telephonyManager.getDeviceId() : null;
        String strI = i(deviceId);
        return TextUtils.isEmpty(strI) ? str : strI;
    }

    private static String i(String str) {
        return (str == null || !str.contains(":")) ? str : "";
    }
}
