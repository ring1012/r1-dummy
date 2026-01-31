package com.unisound.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import cn.yunzhisheng.asr.JniUscClient;
import com.google.android.exoplayer2.C;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class y {
    public static final String A = "initVPR";
    public static final String B = "initNLU";
    public static final String C = "collectedInfo";
    public static final String D = "start";
    public static final String E = "recognition";
    public static final String F = "stop";
    public static final String G = "cancel";
    public static final String H = "getResult";
    public static final String I = "error";
    public static final String J = "success";
    public static final String K = "partial";
    public static final String L = "last";
    private static final String N = "USCLOG";

    /* renamed from: a, reason: collision with root package name */
    public static final int f273a = 0;
    public static final int b = 1;
    public static final int c = 2;
    public static final int d = 3;
    public static final int e = 4;
    public static final int f = 5;
    public static final int g = 0;
    public static final int h = 1;
    public static final int i = 2;
    public static final int j = 3;
    public static final String v = "USC";
    public static final String w = "USC_THREAD";
    public static final String y = "init";
    public static final String z = "create";
    private static ar M = null;
    public static boolean k = false;
    public static boolean l = false;
    public static boolean m = false;
    public static boolean n = false;
    public static boolean o = false;
    public static boolean p = false;
    public static boolean q = false;
    public static boolean r = false;
    public static String s = "";
    public static boolean t = true;
    public static boolean u = true;
    public static int x = 0;
    private static String O = "";
    private static String P = "";
    private static StringBuffer Q = new StringBuffer();
    private static boolean R = true;
    private static int S = 0;
    private static Object T = new Object();

    private static String a(long j2) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(j2));
    }

    public static String a(JniUscClient jniUscClient) {
        O = jniUscClient.c(54);
        return O;
    }

    private static String a(String str, String str2, String str3, int i2, String str4, Context context, String str5, String str6, long j2) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(x.f272a, str);
            jSONObject.put(x.b, str2);
            jSONObject.put(x.c, str3);
            jSONObject.put(x.e, b(j2));
            jSONObject.put(x.f, context.getPackageName());
            jSONObject.put("status", i2);
            jSONObject.put(x.h, str4);
            jSONObject.put(x.i, str5);
            jSONObject.put(x.j, str6);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static void a() {
        Q.delete(0, Q.length());
        O = "";
        P = "";
    }

    public static void a(Context context) {
        if (u) {
            new Thread(new z(context.getCacheDir().getAbsolutePath() + "/errorlog/")).start();
        }
    }

    public static void a(Context context, boolean z2, String str, String str2, int i2, String str3) {
        if (t) {
            try {
                String str4 = context.getCacheDir().getAbsolutePath() + "/javalog";
                String str5 = context.getCacheDir().getAbsolutePath() + "/clog";
                String str6 = context.getCacheDir().getAbsolutePath() + "/errorlog";
                String str7 = context.getCacheDir().getAbsolutePath() + "/errorlog/" + System.currentTimeMillis();
                String str8 = context.getCacheDir().getAbsolutePath() + "/javalogbk";
                String str9 = context.getCacheDir().getAbsolutePath() + "/clogbk";
                File file = new File(str4);
                File file2 = new File(str5);
                if (file.exists() && file.length() > C.MICROS_PER_SECOND) {
                    File file3 = new File(str8);
                    if (file3.exists()) {
                        file3.delete();
                    }
                    file.renameTo(file3);
                }
                if (file2.exists() && file2.length() > C.MICROS_PER_SECOND) {
                    File file4 = new File(str9);
                    if (file4.exists()) {
                        file4.delete();
                    }
                    file.renameTo(file4);
                }
                a(str4, new String(Q));
                c(str5, O);
                if (z2) {
                    File file5 = new File(str7);
                    if (!file5.getParentFile().exists()) {
                        file5.getParentFile().mkdirs();
                    }
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    String str10 = str2.equals("") ? com.unisound.c.a.q + jCurrentTimeMillis : str2;
                    c(str6);
                    c(str7, a(str, com.unisound.c.a.a(str), str10, i2, str3, context, P, O, jCurrentTimeMillis));
                }
            } catch (IOException e2) {
                a(e2.getMessage());
                e2.printStackTrace();
            }
        }
    }

    public static void a(ar arVar) {
        M = arVar;
    }

    public static void a(String str) {
        Log.e(v, str);
        b(str);
    }

    public static void a(String str, String str2) {
        FileWriter fileWriter = new FileWriter(str, true);
        new BufferedWriter(fileWriter);
        fileWriter.write(str2);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void a(String str, String str2, Throwable th) {
        if (k) {
            Log.e(str, str2, th);
        }
    }

    public static void a(String str, String str2, Map<String, String> map, String str3, String str4, String str5) {
        String str6;
        String str7;
        if (k) {
            String str8 = "Time=" + a(System.currentTimeMillis());
            if (str != null) {
                str8 = str8 + "&Action=" + str;
            }
            if (str2 != null) {
                str8 = str8 + "&Status=" + str2;
            }
            if (map != null) {
                String str9 = "{";
                Iterator<String> it = map.keySet().iterator();
                while (true) {
                    str7 = str9;
                    if (!it.hasNext()) {
                        break;
                    }
                    String next = it.next();
                    str9 = str7 + next + ":" + map.get(next) + MqttTopic.MULTI_LEVEL_WILDCARD;
                }
                str6 = str8 + "&Params=" + (str7 + "}");
            } else {
                str6 = str8;
            }
            if (str3 != null) {
                str6 = str6 + "&Result=" + str3;
            }
            if (str4 != null) {
                str6 = str6 + "&Errno=" + str4;
            }
            if (str5 != null) {
                str6 = str6 + "&Message=" + str5;
            }
            String str10 = str6 + "%";
            P = str10;
            Q.append(str10);
        }
    }

    public static void a(String str, Throwable th) {
        a(v, str, th);
    }

    public static void a(String str, String... strArr) {
        if (k) {
            String strH = h(strArr);
            Log.e(str, strH);
            b(strH);
        }
    }

    public static void a(Object... objArr) {
        if (k) {
            String strH = h(objArr);
            Log.v(v, strH);
            b(strH);
        }
    }

    private static String b(long j2) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(j2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String b(File file) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        if (!file.exists()) {
            return null;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                stringBuffer.append(line);
            }
            bufferedReader.close();
        } catch (IOException e2) {
            e2.getStackTrace();
        }
        return new String(stringBuffer);
    }

    private static void b(String str) {
        if (M != null) {
            M.a(5, 2, str);
        }
    }

    public static void b(String str, String str2) {
        FileWriter fileWriter = new FileWriter(str, true);
        new BufferedWriter(fileWriter);
        fileWriter.write(str2);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void b(Object... objArr) {
        if (k) {
            String strH = h(objArr);
            Log.i(v, strH);
            b(strH);
        }
    }

    private static void c(String str) {
        File[] fileArrListFiles = new File(str).listFiles();
        if (fileArrListFiles.length >= 10) {
            if (fileArrListFiles.length >= 10) {
                for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
                    for (int i3 = 0; i3 < fileArrListFiles.length - 1; i3++) {
                        if (fileArrListFiles[i3].lastModified() > fileArrListFiles[i3 + 1].lastModified()) {
                            File file = fileArrListFiles[i3];
                            fileArrListFiles[i3] = fileArrListFiles[i3 + 1];
                            fileArrListFiles[i3 + 1] = file;
                        }
                    }
                }
            }
            if (fileArrListFiles[0].exists()) {
                fileArrListFiles[0].delete();
            }
            c(str);
        }
    }

    public static void c(String str, String str2) {
        FileWriter fileWriter = new FileWriter(str, true);
        new BufferedWriter(fileWriter);
        fileWriter.write(str2);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void c(Object... objArr) {
        if (k) {
            String strH = h(objArr);
            Log.d(v, strH);
            b(strH);
        }
    }

    static /* synthetic */ int d() {
        int i2 = S;
        S = i2 + 1;
        return i2;
    }

    public static void d(Object... objArr) {
        if (l) {
            Log.d(w, h(objArr));
        }
    }

    public static void e(Object... objArr) {
        if (k) {
            String strH = h(objArr);
            Log.w(v, strH);
            b(strH);
        }
    }

    public static void f(Object... objArr) {
        if (m) {
            String strH = h(objArr);
            Log.d("USC_TIME_TRACE", strH + ": " + a(System.currentTimeMillis()));
            b(strH);
        }
    }

    public static void g(Object... objArr) {
        if (n) {
            String strH = h(objArr);
            Log.e(v, strH);
            b(strH);
        }
    }

    private static String h(Object... objArr) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Object obj : objArr) {
                if (!TextUtils.isEmpty(String.valueOf(obj))) {
                    sb.append(obj);
                }
            }
            return sb.toString();
        } catch (Exception e2) {
            Log.e(v, "getLog error");
            return "getLog error";
        }
    }
}
