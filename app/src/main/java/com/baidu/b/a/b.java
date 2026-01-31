package com.baidu.b.a;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import com.unisound.vui.handler.session.memo.utils.MemoConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class b {

    /* renamed from: a, reason: collision with root package name */
    private static Context f39a;
    private static o d = null;
    private static int e = 0;
    private static Hashtable<String, c> f = new Hashtable<>();
    private static b g;
    private f b = null;
    private h c = null;
    private final Handler h = new k(this, Looper.getMainLooper());

    private b(Context context) {
        f39a = context;
        if (d != null && !d.isAlive()) {
            d = null;
        }
        d.b("BaiduApiAuth SDK Version:1.0.20");
        e();
    }

    private int a(String str) throws JSONException {
        int i = -1;
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("status")) {
                jSONObject.put("status", -1);
            }
            i = jSONObject.getInt("status");
            if (jSONObject.has("current") && i == 0) {
                long j = jSONObject.getLong("current");
                long jCurrentTimeMillis = System.currentTimeMillis();
                if ((jCurrentTimeMillis - j) / 3600000.0d >= 24.0d) {
                    i = 601;
                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MemoConstants.DATE_FORMATE_YMD);
                    if (!simpleDateFormat.format(Long.valueOf(jCurrentTimeMillis)).equals(simpleDateFormat.format(Long.valueOf(j)))) {
                        i = 601;
                    }
                }
            }
            if (jSONObject.has("current") && i == 602) {
                if ((System.currentTimeMillis() - jSONObject.getLong("current")) / 1000 > 180.0d) {
                    return 601;
                }
            }
            return i;
        } catch (JSONException e2) {
            int i2 = i;
            e2.printStackTrace();
            return i2;
        }
    }

    public static b a(Context context) {
        if (g == null) {
            synchronized (b.class) {
                if (g == null) {
                    g = new b(context);
                }
            }
        } else if (context != null) {
            f39a = context;
        } else if (d.f40a) {
            d.c("input context is null");
            new RuntimeException("here").printStackTrace();
        }
        return g;
    }

    private String a(int i) throws Throwable {
        InputStreamReader inputStreamReader;
        FileInputStream fileInputStream;
        BufferedReader bufferedReader;
        Throwable th;
        String line = null;
        try {
            fileInputStream = new FileInputStream(new File("/proc/" + i + "/cmdline"));
            try {
                inputStreamReader = new InputStreamReader(fileInputStream);
                try {
                    bufferedReader = new BufferedReader(inputStreamReader);
                } catch (FileNotFoundException e2) {
                    bufferedReader = null;
                } catch (IOException e3) {
                    bufferedReader = null;
                } catch (Throwable th2) {
                    bufferedReader = null;
                    th = th2;
                }
                try {
                    line = bufferedReader.readLine();
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (inputStreamReader != null) {
                        inputStreamReader.close();
                    }
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (FileNotFoundException e4) {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (inputStreamReader != null) {
                        inputStreamReader.close();
                    }
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    return line;
                } catch (IOException e5) {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (inputStreamReader != null) {
                        inputStreamReader.close();
                    }
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    return line;
                } catch (Throwable th3) {
                    th = th3;
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (inputStreamReader != null) {
                        inputStreamReader.close();
                    }
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    throw th;
                }
            } catch (FileNotFoundException e6) {
                bufferedReader = null;
                inputStreamReader = null;
            } catch (IOException e7) {
                bufferedReader = null;
                inputStreamReader = null;
            } catch (Throwable th4) {
                inputStreamReader = null;
                th = th4;
                bufferedReader = null;
            }
        } catch (FileNotFoundException e8) {
            bufferedReader = null;
            inputStreamReader = null;
            fileInputStream = null;
        } catch (IOException e9) {
            bufferedReader = null;
            inputStreamReader = null;
            fileInputStream = null;
        } catch (Throwable th5) {
            inputStreamReader = null;
            fileInputStream = null;
            bufferedReader = null;
            th = th5;
        }
        return line;
    }

    private String a(Context context, String str) throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo;
        c cVar;
        String string = "";
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
        } catch (PackageManager.NameNotFoundException e2) {
            c cVar2 = f.get(str);
            if (cVar2 != null) {
                cVar2.a(101, a.a(101, "无法在AndroidManifest.xml中获取com.baidu.android.lbs.API_KEY的值"));
            }
        }
        if (applicationInfo.metaData == null) {
            c cVar3 = f.get(str);
            if (cVar3 != null) {
                cVar3.a(101, a.a(101, "AndroidManifest.xml的application中没有meta-data标签"));
            }
            return "";
        }
        string = applicationInfo.metaData.getString("com.baidu.lbsapi.API_KEY");
        if ((string == null || string.equals("")) && (cVar = f.get(str)) != null) {
            cVar.a(101, a.a(101, "无法在AndroidManifest.xml中获取com.baidu.android.lbs.API_KEY的值"));
        }
        return string;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0029 A[Catch: JSONException -> 0x009f, all -> 0x00bf, TryCatch #0 {JSONException -> 0x009f, blocks: (B:7:0x000e, B:9:0x001b, B:10:0x0021, B:12:0x0029, B:13:0x0032, B:15:0x0041, B:16:0x0046), top: B:33:0x000e, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0041 A[Catch: JSONException -> 0x009f, all -> 0x00bf, TryCatch #0 {JSONException -> 0x009f, blocks: (B:7:0x000e, B:9:0x001b, B:10:0x0021, B:12:0x0029, B:13:0x0032, B:15:0x0041, B:16:0x0046), top: B:33:0x000e, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0075 A[Catch: all -> 0x00bf, TryCatch #1 {, blocks: (B:5:0x0004, B:6:0x0008, B:7:0x000e, B:9:0x001b, B:10:0x0021, B:12:0x0029, B:13:0x0032, B:15:0x0041, B:16:0x0046, B:17:0x0066, B:19:0x0075, B:20:0x008d, B:22:0x0091, B:24:0x009a, B:28:0x00a0), top: B:35:0x0004, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0091 A[Catch: all -> 0x00bf, TryCatch #1 {, blocks: (B:5:0x0004, B:6:0x0008, B:7:0x000e, B:9:0x001b, B:10:0x0021, B:12:0x0029, B:13:0x0032, B:15:0x0041, B:16:0x0046, B:17:0x0066, B:19:0x0075, B:20:0x008d, B:22:0x0091, B:24:0x009a, B:28:0x00a0), top: B:35:0x0004, inners: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x001b A[Catch: JSONException -> 0x009f, all -> 0x00bf, TryCatch #0 {JSONException -> 0x009f, blocks: (B:7:0x000e, B:9:0x001b, B:10:0x0021, B:12:0x0029, B:13:0x0032, B:15:0x0041, B:16:0x0046), top: B:33:0x000e, outer: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void a(java.lang.String r7, java.lang.String r8) {
        /*
            r6 = this;
            r1 = -1
            monitor-enter(r6)
            if (r7 != 0) goto L8
            java.lang.String r7 = r6.f()     // Catch: java.lang.Throwable -> Lbf
        L8:
            android.os.Handler r0 = r6.h     // Catch: java.lang.Throwable -> Lbf
            android.os.Message r2 = r0.obtainMessage()     // Catch: java.lang.Throwable -> Lbf
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            r0.<init>(r7)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            java.lang.String r3 = "status"
            boolean r3 = r0.has(r3)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            if (r3 != 0) goto L21
            java.lang.String r3 = "status"
            r4 = -1
            r0.put(r3, r4)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
        L21:
            java.lang.String r3 = "current"
            boolean r3 = r0.has(r3)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            if (r3 != 0) goto L32
            java.lang.String r3 = "current"
            long r4 = java.lang.System.currentTimeMillis()     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            r0.put(r3, r4)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
        L32:
            java.lang.String r3 = r0.toString()     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            r6.c(r3)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            java.lang.String r3 = "current"
            boolean r3 = r0.has(r3)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            if (r3 == 0) goto L46
            java.lang.String r3 = "current"
            r0.remove(r3)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
        L46:
            java.lang.String r3 = "status"
            int r1 = r0.getInt(r3)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            r2.what = r1     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            java.lang.String r0 = r0.toString()     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            r2.obj = r0     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            android.os.Bundle r0 = new android.os.Bundle     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            r0.<init>()     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            java.lang.String r3 = "listenerKey"
            r0.putString(r3, r8)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            r2.setData(r0)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            android.os.Handler r0 = r6.h     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
            r0.sendMessage(r2)     // Catch: org.json.JSONException -> L9f java.lang.Throwable -> Lbf
        L66:
            com.baidu.b.a.o r0 = com.baidu.b.a.b.d     // Catch: java.lang.Throwable -> Lbf
            r0.c()     // Catch: java.lang.Throwable -> Lbf
            int r0 = com.baidu.b.a.b.e     // Catch: java.lang.Throwable -> Lbf
            int r0 = r0 + (-1)
            com.baidu.b.a.b.e = r0     // Catch: java.lang.Throwable -> Lbf
            boolean r0 = com.baidu.b.a.d.f40a     // Catch: java.lang.Throwable -> Lbf
            if (r0 == 0) goto L8d
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lbf
            r0.<init>()     // Catch: java.lang.Throwable -> Lbf
            java.lang.String r1 = "httpRequest called mAuthCounter-- = "
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> Lbf
            int r1 = com.baidu.b.a.b.e     // Catch: java.lang.Throwable -> Lbf
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Throwable -> Lbf
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Throwable -> Lbf
            com.baidu.b.a.d.a(r0)     // Catch: java.lang.Throwable -> Lbf
        L8d:
            int r0 = com.baidu.b.a.b.e     // Catch: java.lang.Throwable -> Lbf
            if (r0 != 0) goto L9d
            com.baidu.b.a.o r0 = com.baidu.b.a.b.d     // Catch: java.lang.Throwable -> Lbf
            r0.a()     // Catch: java.lang.Throwable -> Lbf
            com.baidu.b.a.o r0 = com.baidu.b.a.b.d     // Catch: java.lang.Throwable -> Lbf
            if (r0 == 0) goto L9d
            r0 = 0
            com.baidu.b.a.b.d = r0     // Catch: java.lang.Throwable -> Lbf
        L9d:
            monitor-exit(r6)
            return
        L9f:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Throwable -> Lbf
            r2.what = r1     // Catch: java.lang.Throwable -> Lbf
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch: java.lang.Throwable -> Lbf
            r0.<init>()     // Catch: java.lang.Throwable -> Lbf
            r2.obj = r0     // Catch: java.lang.Throwable -> Lbf
            android.os.Bundle r0 = new android.os.Bundle     // Catch: java.lang.Throwable -> Lbf
            r0.<init>()     // Catch: java.lang.Throwable -> Lbf
            java.lang.String r1 = "listenerKey"
            r0.putString(r1, r8)     // Catch: java.lang.Throwable -> Lbf
            r2.setData(r0)     // Catch: java.lang.Throwable -> Lbf
            android.os.Handler r0 = r6.h     // Catch: java.lang.Throwable -> Lbf
            r0.sendMessage(r2)     // Catch: java.lang.Throwable -> Lbf
            goto L66
        Lbf:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.b.a.b.a(java.lang.String, java.lang.String):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z, String str, Hashtable<String, String> hashtable, String str2) throws PackageManager.NameNotFoundException {
        String strA = a(f39a, str2);
        if (strA == null || strA.equals("")) {
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("url", "https://api.map.baidu.com/sdkcs/verify");
        d.a("url:https://api.map.baidu.com/sdkcs/verify");
        map.put("output", "json");
        map.put("ak", strA);
        d.a("ak:" + strA);
        map.put("mcode", e.a(f39a));
        map.put("from", "lbs_yunsdk");
        if (hashtable != null && hashtable.size() > 0) {
            for (Map.Entry<String, String> entry : hashtable.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    map.put(key, value);
                }
            }
        }
        String strA2 = "";
        try {
            strA2 = com.baidu.a.a.a.b.a.a(f39a);
        } catch (Exception e2) {
        }
        d.a("cuid:" + strA2);
        if (TextUtils.isEmpty(strA2)) {
            map.put("cuid", "");
        } else {
            map.put("cuid", strA2);
        }
        map.put("pcn", f39a.getPackageName());
        map.put("version", "1.0.20");
        String strC = "";
        try {
            strC = e.c(f39a);
        } catch (Exception e3) {
        }
        if (TextUtils.isEmpty(strC)) {
            map.put("macaddr", "");
        } else {
            map.put("macaddr", strC);
        }
        String strA3 = "";
        try {
            strA3 = e.a();
        } catch (Exception e4) {
        }
        if (TextUtils.isEmpty(strA3)) {
            map.put("language", "");
        } else {
            map.put("language", strA3);
        }
        if (z) {
            map.put("force", z ? "1" : "0");
        }
        if (str == null) {
            map.put("from_service", "");
        } else {
            map.put("from_service", str);
        }
        this.b = new f(f39a);
        this.b.a(map, new m(this, str2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z, String str, Hashtable<String, String> hashtable, String[] strArr, String str2) throws PackageManager.NameNotFoundException {
        String strA = a(f39a, str2);
        if (strA == null || strA.equals("")) {
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("url", "https://api.map.baidu.com/sdkcs/verify");
        map.put("output", "json");
        map.put("ak", strA);
        map.put("from", "lbs_yunsdk");
        if (hashtable != null && hashtable.size() > 0) {
            for (Map.Entry<String, String> entry : hashtable.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                    map.put(key, value);
                }
            }
        }
        String strA2 = "";
        try {
            strA2 = com.baidu.a.a.a.b.a.a(f39a);
        } catch (Exception e2) {
        }
        if (TextUtils.isEmpty(strA2)) {
            map.put("cuid", "");
        } else {
            map.put("cuid", strA2);
        }
        map.put("pcn", f39a.getPackageName());
        map.put("version", "1.0.20");
        String strC = "";
        try {
            strC = e.c(f39a);
        } catch (Exception e3) {
        }
        if (TextUtils.isEmpty(strC)) {
            map.put("macaddr", "");
        } else {
            map.put("macaddr", strC);
        }
        String strA3 = "";
        try {
            strA3 = e.a();
        } catch (Exception e4) {
        }
        if (TextUtils.isEmpty(strA3)) {
            map.put("language", "");
        } else {
            map.put("language", strA3);
        }
        if (z) {
            map.put("force", z ? "1" : "0");
        }
        if (str == null) {
            map.put("from_service", "");
        } else {
            map.put("from_service", str);
        }
        this.c = new h(f39a);
        this.c.a(map, strArr, new n(this, str2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b(String str) throws JSONException, PackageManager.NameNotFoundException {
        String string;
        JSONObject jSONObject;
        String strA = a(f39a, str);
        try {
            jSONObject = new JSONObject(f());
        } catch (JSONException e2) {
            e2.printStackTrace();
            string = "";
        }
        if (!jSONObject.has("ak")) {
            return true;
        }
        string = jSONObject.getString("ak");
        return (strA == null || string == null || strA.equals(string)) ? false : true;
    }

    private String c(Context context) throws Throwable {
        int iMyPid = Process.myPid();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.pid == iMyPid) {
                    return runningAppProcessInfo.processName;
                }
            }
        }
        String strA = null;
        try {
            strA = a(iMyPid);
        } catch (IOException e2) {
        }
        return strA == null ? f39a.getPackageName() : strA;
    }

    private void c(String str) {
        f39a.getSharedPreferences("authStatus_" + c(f39a), 0).edit().putString("status", str).commit();
    }

    private void e() {
        synchronized (b.class) {
            if (d == null) {
                d = new o("auth");
                d.start();
                while (d.f50a == null) {
                    try {
                        if (d.f40a) {
                            d.a("wait for create auth thread.");
                        }
                        Thread.sleep(3L);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    private String f() {
        return f39a.getSharedPreferences("authStatus_" + c(f39a), 0).getString("status", "{\"status\":601}");
    }

    public int a(boolean z, String str, Hashtable<String, String> hashtable, c cVar) {
        int iA;
        synchronized (b.class) {
            String str2 = System.currentTimeMillis() + "";
            if (cVar != null) {
                f.put(str2, cVar);
            }
            String strA = a(f39a, str2);
            if (strA == null || strA.equals("")) {
                iA = 101;
            } else {
                e++;
                if (d.f40a) {
                    d.a(" mAuthCounter  ++ = " + e);
                }
                String strF = f();
                if (d.f40a) {
                    d.a("getAuthMessage from cache:" + strF);
                }
                iA = a(strF);
                if (iA == 601) {
                    try {
                        c(new JSONObject().put("status", 602).toString());
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                }
                e();
                if (d.f40a) {
                    d.a("mThreadLooper.mHandler = " + d.f50a);
                }
                if (d == null || d.f50a == null) {
                    iA = -1;
                } else {
                    d.f50a.post(new l(this, iA, z, str2, str, hashtable));
                }
            }
        }
        return iA;
    }

    public String a() {
        return f39a == null ? "" : e.a(f39a);
    }

    public String b(Context context) {
        return context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getString("com.baidu.lbsapi.API_KEY");
    }
}
