package com.unisound.sdk;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import cn.yunzhisheng.asrfix.JniAsrFix;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import nluparser.scheme.ASR;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ab {
    public static final String f = "ml";
    public static final int i = 0;
    public static final int j = -100;
    public static final int k = -200;
    public static final int l = -300;
    public static final int n = 1000;
    public static final int o = 1001;

    /* renamed from: a, reason: collision with root package name */
    public String f293a;
    private static String q = "assetsModelsMD5";
    public static int m = 20;
    private static g t = new g();
    public String[] b = {"tri", "l", "wid", "am", "digit", "wseg", "stat"};
    public String c = "am";
    public String d = ASR.NET;
    private String p = ".dat";
    public String e = "main";
    public boolean g = false;
    private boolean r = false;
    private List<String> s = new ArrayList();
    public boolean h = false;
    private ac u = null;

    public ab() {
        t.a(this);
    }

    private void a(Map<String, String> map) throws Throwable {
        RandomAccessFile randomAccessFile = null;
        try {
            try {
                JSONObject jSONObject = new JSONObject();
                for (String str : map.keySet()) {
                    jSONObject.put(str, map.get(str));
                }
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(this.f293a + q, "rw");
                try {
                    randomAccessFile2.write(jSONObject.toString().getBytes());
                    if (randomAccessFile2 != null) {
                        try {
                            randomAccessFile2.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e2) {
                    e = e2;
                    randomAccessFile = randomAccessFile2;
                    e.printStackTrace();
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    randomAccessFile = randomAccessFile2;
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private boolean a(AssetManager assetManager, String str) throws IOException {
        boolean z;
        File file;
        this.r = false;
        try {
            file = new File(str);
        } catch (Exception e) {
            e.printStackTrace();
            z = true;
        }
        if (!file.exists()) {
            return true;
        }
        InputStream inputStreamOpen = assetManager.open("version/data");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamOpen));
        String line = bufferedReader.readLine();
        inputStreamOpen.close();
        BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file));
        String line2 = bufferedReader2.readLine();
        z = (line2 == null || line2.equals(line)) ? false : true;
        bufferedReader.close();
        bufferedReader2.close();
        FileReader fileReader = new FileReader(new File(this.f293a + q));
        BufferedReader bufferedReader3 = new BufferedReader(fileReader);
        String line3 = bufferedReader3.readLine();
        JSONObject jSONObject = line3 != null ? new JSONObject(line3) : null;
        for (String str2 : this.b) {
            String strK = k(this.f293a + str2 + ".dat");
            String string = jSONObject != null ? jSONObject.getString(str2) : "";
            if (TextUtils.isEmpty(string) || TextUtils.isEmpty(strK)) {
                this.r = true;
                this.s.add(str2);
                z = true;
            } else if (!string.equalsIgnoreCase(strK)) {
                this.r = true;
                this.s.add(str2);
                z = true;
            }
        }
        bufferedReader3.close();
        fileReader.close();
        com.unisound.common.y.c("ModelData isUpdateModel = " + z);
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x008d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean a(android.content.res.AssetManager r9, java.lang.String r10, java.lang.String r11) throws java.lang.Throwable {
        /*
            r0 = 1
            r1 = 0
            r3 = 0
            java.io.File r4 = new java.io.File     // Catch: java.lang.Throwable -> La1 java.lang.Exception -> La4
            r4.<init>(r11)     // Catch: java.lang.Throwable -> La1 java.lang.Exception -> La4
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> La1 java.lang.Exception -> La4
            r2.<init>(r4)     // Catch: java.lang.Throwable -> La1 java.lang.Exception -> La4
            r3 = 10240(0x2800, float:1.4349E-41)
            byte[] r4 = new byte[r3]     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            int r3 = com.unisound.sdk.ab.m     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            if (r3 != r0) goto L52
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            r3.<init>()     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            java.lang.StringBuilder r3 = r3.append(r10)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            java.lang.String r5 = "/data"
            java.lang.StringBuilder r3 = r3.append(r5)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            java.io.InputStream r3 = r9.open(r3)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
        L2c:
            r5 = 0
            int r6 = r4.length     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            int r5 = r3.read(r4, r5, r6)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            if (r5 > 0) goto L3d
            r3.close()     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
        L37:
            if (r2 == 0) goto L3c
            r2.close()     // Catch: java.io.IOException -> L91
        L3c:
            return r0
        L3d:
            r6 = 0
            r2.write(r4, r6, r5)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            goto L2c
        L42:
            r0 = move-exception
        L43:
            java.lang.String r3 = "init asr model error"
            com.unisound.common.y.a(r3)     // Catch: java.lang.Throwable -> L8a
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L8a
            if (r2 == 0) goto L50
            r2.close()     // Catch: java.io.IOException -> L96
        L50:
            r0 = r1
            goto L3c
        L52:
            java.lang.String[] r5 = r9.list(r10)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            r3 = r1
        L57:
            int r6 = r5.length     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            if (r3 >= r6) goto L37
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            r6.<init>()     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            java.lang.StringBuilder r6 = r6.append(r10)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            java.lang.String r7 = "/"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            r7 = r5[r3]     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            java.lang.String r6 = r6.toString()     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            java.io.InputStream r6 = r9.open(r6)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
        L77:
            r7 = 0
            int r8 = r4.length     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            int r7 = r6.read(r4, r7, r8)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            if (r7 > 0) goto L85
            r6.close()     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            int r3 = r3 + 1
            goto L57
        L85:
            r8 = 0
            r2.write(r4, r8, r7)     // Catch: java.lang.Exception -> L42 java.lang.Throwable -> L8a
            goto L77
        L8a:
            r0 = move-exception
        L8b:
            if (r2 == 0) goto L90
            r2.close()     // Catch: java.io.IOException -> L9c
        L90:
            throw r0
        L91:
            r1 = move-exception
            r1.printStackTrace()
            goto L3c
        L96:
            r0 = move-exception
            r0.printStackTrace()
            r0 = r1
            goto L3c
        L9c:
            r1 = move-exception
            r1.printStackTrace()
            goto L90
        La1:
            r0 = move-exception
            r2 = r3
            goto L8b
        La4:
            r0 = move-exception
            r2 = r3
            goto L43
        */
        throw new UnsupportedOperationException("Method not decompiled: com.unisound.sdk.ab.a(android.content.res.AssetManager, java.lang.String, java.lang.String):boolean");
    }

    public static boolean a(AssetManager assetManager, String str, String str2, boolean z, boolean z2) {
        if (!z && new File(str2).exists()) {
            if (!z2 || JniAsrFix.a(str2)) {
                return true;
            }
            com.unisound.common.y.a("reset model file " + str2);
        }
        return a(assetManager, str, str2);
    }

    private String b(AssetManager assetManager, String str) {
        try {
            return com.unisound.common.aa.a(assetManager.open(str + "/data"));
        } catch (Exception e) {
            com.unisound.common.y.a("getMd5FromAssets from assets error!");
            e.printStackTrace();
            return "";
        }
    }

    public static boolean d(String str) {
        return JniAsrFix.a(str);
    }

    private boolean i(String str) {
        File file = new File(str);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    private static void j(String str) {
        com.unisound.common.y.a(str);
    }

    private String k(String str) {
        try {
            return com.unisound.common.aa.a(new File(str));
        } catch (Exception e) {
            com.unisound.common.y.a("getMd5FromAssets from file error!");
            e.printStackTrace();
            return "";
        }
    }

    public int a(int i2) {
        return t.a(i2);
    }

    public int a(String str, String str2) {
        return t.a(str, str2);
    }

    public int a(String str, String str2, String str3) {
        return t.a(str, str2, str3);
    }

    public int a(String str, String str2, String str3, String str4) {
        File file = new File(str4);
        if (file.exists()) {
            file.delete();
        } else {
            File file2 = new File(file.getParent());
            if (!file2.exists()) {
                file2.mkdirs();
            }
        }
        return t.a() ? t.a(str, str2, str3, str4) : t.a(str, str2, str3, this.f293a, str4);
    }

    public int a(boolean z) {
        return z ? t.b(1) : t.b(0);
    }

    public String a() {
        return this.f293a + this.d + ".dat";
    }

    public String a(String str, List<String> list) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<" + str + ">").append("\n");
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            stringBuffer.append(it.next()).append("\n");
        }
        stringBuffer.append("</" + str + ">");
        com.unisound.common.y.c("ModelData : ", "getVocabString --> vocab = ", stringBuffer.toString());
        return stringBuffer.toString();
    }

    public void a(ac acVar) {
        this.u = acVar;
    }

    public void a(String str) {
        this.f293a = str + MqttTopic.TOPIC_LEVEL_SEPARATOR;
    }

    public boolean a(Context context) {
        return a(context, false);
    }

    public boolean a(Context context, String str) throws IOException {
        try {
            InputStream inputStreamOpen = context.getAssets().open(str);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamOpen));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    inputStreamOpen.close();
                    return true;
                }
                String[] strArrSplit = line.split("=");
                if (strArrSplit.length == 2) {
                    String strTrim = strArrSplit[0].trim();
                    String strTrim2 = strArrSplit[1].trim();
                    if ("models".equals(strTrim)) {
                        this.b = strTrim2.split(",");
                    } else if ("am".equals(strTrim)) {
                        this.c = strTrim2;
                    } else if ("custom".equals(strTrim)) {
                        this.d = strTrim2;
                    } else if ("domain".equals(strTrim)) {
                        this.e = strTrim2;
                    }
                }
            }
        } catch (Exception e) {
            com.unisound.common.y.a("model list error");
            e.printStackTrace();
            return false;
        }
    }

    public boolean a(Context context, boolean z) {
        synchronized (this) {
            if (this.g) {
                return true;
            }
            AssetManager assets = context.getAssets();
            File file = new File(this.f293a);
            if (!file.exists()) {
                file.mkdirs();
            }
            boolean zA = a(assets, this.f293a + "version");
            if (zA) {
                com.unisound.common.y.a("init asr models..");
                HashMap map = new HashMap();
                if (this.r) {
                    for (String str : this.s) {
                        com.unisound.common.y.c("ModelData , partCopy : model = " + str);
                        if (!a(assets, str, this.f293a + str + ".dat", zA, this.h)) {
                            return false;
                        }
                    }
                }
                for (String str2 : this.b) {
                    if (!this.r && !a(assets, str2, this.f293a + str2 + ".dat", zA, this.h)) {
                        return false;
                    }
                    map.put(str2, b(assets, str2));
                }
                com.unisound.common.y.a("init asr models ok");
                a(assets, "version", this.f293a + "version");
                a(map);
                this.s.clear();
            } else {
                com.unisound.common.y.a("init not overwrite models..");
            }
            if (!t.a() && z && t.a(this.f293a)) {
                this.u.a(1000);
            }
            this.g = true;
            return true;
        }
    }

    public String b(String str) {
        return this.f293a + str + "_partialFile";
    }

    public boolean b() {
        String str = this.f293a + this.d + this.p;
        if (JniAsrFix.a(str)) {
            return true;
        }
        i(str);
        return false;
    }

    public boolean b(Context context, String str) throws IOException {
        try {
            for (String str2 : context.getAssets().list("")) {
                if (str2.equals(str)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean c() {
        boolean zA;
        synchronized (this) {
            zA = JniAsrFix.a(this.f293a + this.d + this.p);
        }
        return zA;
    }

    public boolean c(String str) {
        return false;
    }

    public int d() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            com.unisound.common.y.a("setAMFile error");
        }
        synchronized (this) {
            File file = new File(this.f293a + this.c + this.p);
            if (!file.exists()) {
                return 0;
            }
            return (int) file.length();
        }
    }

    public int e(String str) {
        int iCompileDecodeNet = JniAsrFix.compileDecodeNet(this.f293a, str);
        if (iCompileDecodeNet == 0) {
            return 0;
        }
        j("setUserData DecodeNet error:" + iCompileDecodeNet);
        return l;
    }

    public boolean e() {
        return JniAsrFix.a(this.f293a + this.c);
    }

    public String f(String str) {
        String str2 = "#JSGF V1.0 utf-8 cn;\ngrammar " + str + ";\npublic <" + str + "> =( \"<s>\" (\n<NAME>\n) \"</s>\");";
        com.unisound.common.y.c("ModelData : ", "getJsgf --> jsgf = ", str2);
        return str2;
    }

    public void f() {
        t.b();
    }

    public String g() {
        return t.c();
    }

    public String g(String str) {
        return this.f293a + "jsgf_model/" + str + ".dat";
    }

    public int h(String str) {
        return t.b(str);
    }

    public g h() {
        return t;
    }
}
