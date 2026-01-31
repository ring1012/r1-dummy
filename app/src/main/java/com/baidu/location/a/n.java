package com.baidu.location.a;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import com.baidu.location.Jni;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class n extends com.baidu.location.d.e {
    private static n p = null;

    /* renamed from: a, reason: collision with root package name */
    String f77a = null;
    String b = null;
    String c = null;
    String d = null;
    int e = 1;
    Handler f;

    private n() {
        this.f = null;
        this.f = new Handler();
    }

    public static void a(File file, File file2) throws Throwable {
        BufferedOutputStream bufferedOutputStream;
        BufferedInputStream bufferedInputStream = null;
        try {
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(new FileInputStream(file));
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
            } catch (Throwable th) {
                th = th;
                bufferedOutputStream = null;
                bufferedInputStream = bufferedInputStream2;
            }
            try {
                byte[] bArr = new byte[5120];
                while (true) {
                    int i = bufferedInputStream2.read(bArr);
                    if (i == -1) {
                        break;
                    } else {
                        bufferedOutputStream.write(bArr, 0, i);
                    }
                }
                bufferedOutputStream.flush();
                file.delete();
                if (bufferedInputStream2 != null) {
                    bufferedInputStream2.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (Throwable th2) {
                th = th2;
                bufferedInputStream = bufferedInputStream2;
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            bufferedOutputStream = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.getType() == 0) {
                String strA = com.baidu.location.b.c.a(com.baidu.location.b.b.a().e());
                if (!strA.equals("3G")) {
                    if (strA.equals("4G")) {
                    }
                }
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean a(String str, String str2) throws IOException {
        File file = new File(com.baidu.location.d.j.h() + File.separator + "tmp");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[4096];
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            while (true) {
                int i = bufferedInputStream.read(bArr);
                if (i <= 0) {
                    break;
                }
                fileOutputStream.write(bArr, 0, i);
            }
            httpURLConnection.disconnect();
            fileOutputStream.close();
            bufferedInputStream.close();
            if (file.length() < 10240) {
                file.delete();
                return false;
            }
            file.renameTo(new File(com.baidu.location.d.j.h() + File.separator + str2));
            return true;
        } catch (Exception e) {
            file.delete();
            return false;
        }
    }

    public static n b() {
        if (p == null) {
            p = new n();
        }
        return p;
    }

    private Handler f() {
        return this.f;
    }

    private void g() throws IOException {
        try {
            File file = new File(com.baidu.location.d.j.h() + "/grtcfrsa.dat");
            if (!file.exists()) {
                File file2 = new File(com.baidu.location.d.i.f111a);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                if (!file.createNewFile()) {
                    return;
                }
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(2L);
                randomAccessFile.writeInt(0);
                randomAccessFile.seek(8L);
                byte[] bytes = "1980_01_01:0".getBytes();
                randomAccessFile.writeInt(bytes.length);
                randomAccessFile.write(bytes);
                randomAccessFile.seek(200L);
                randomAccessFile.writeBoolean(false);
                randomAccessFile.seek(800L);
                randomAccessFile.writeBoolean(false);
                randomAccessFile.close();
            }
            RandomAccessFile randomAccessFile2 = new RandomAccessFile(file, "rw");
            randomAccessFile2.seek(200L);
            randomAccessFile2.writeBoolean(true);
            if (this.e == 1) {
                randomAccessFile2.writeBoolean(true);
            } else {
                randomAccessFile2.writeBoolean(false);
            }
            if (this.d != null) {
                byte[] bytes2 = this.d.getBytes();
                randomAccessFile2.writeInt(bytes2.length);
                randomAccessFile2.write(bytes2);
            } else if (Math.abs(com.baidu.location.f.a() - 7.32f) < 1.0E-8f) {
                randomAccessFile2.writeInt(0);
            }
            randomAccessFile2.close();
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        if (this.f77a == null) {
            return;
        }
        new r(this).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean i() {
        if (this.c == null || new File(com.baidu.location.d.j.h() + File.separator + this.c).exists()) {
            return true;
        }
        return a("http://" + this.f77a + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.c, this.c);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() throws Throwable {
        if (this.b == null) {
            return;
        }
        File file = new File(com.baidu.location.d.j.h() + File.separator + this.b);
        if (file.exists() || !a("http://" + this.f77a + MqttTopic.TOPIC_LEVEL_SEPARATOR + this.b, this.b)) {
            return;
        }
        String strA = com.baidu.location.d.j.a(file, "SHA-256");
        if (this.d == null || strA == null || !com.baidu.location.d.j.b(strA, this.d, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiP7BS5IjEOzrKGR9/Ww9oSDhdX1ir26VOsYjT1T6tk2XumRpkHRwZbrucDcNnvSB4QsqiEJnvTSRi7YMbh2H9sLMkcvHlMV5jAErNvnuskWfcvf7T2mq7EUZI/Hf4oVZhHV0hQJRFVdTcjWI6q2uaaKM3VMh+roDesiE7CR2biQIDAQAB")) {
            return;
        }
        File file2 = new File(com.baidu.location.d.j.h() + File.separator + com.baidu.location.f.d);
        if (file2.exists()) {
            file2.delete();
        }
        try {
            a(file, file2);
        } catch (Exception e) {
            file2.delete();
        }
    }

    @Override // com.baidu.location.d.e
    @SuppressLint({"NewApi"})
    public void a() {
        String str;
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer(128);
        stringBuffer.append("&sdk=");
        stringBuffer.append(7.32f);
        stringBuffer.append("&fw=");
        stringBuffer.append(com.baidu.location.f.a());
        stringBuffer.append("&suit=");
        stringBuffer.append(2);
        if (com.baidu.location.d.b.a().b == null) {
            stringBuffer.append("&im=");
            stringBuffer.append(com.baidu.location.d.b.a().f102a);
        } else {
            stringBuffer.append("&cu=");
            stringBuffer.append(com.baidu.location.d.b.a().b);
        }
        stringBuffer.append("&mb=");
        stringBuffer.append(Build.MODEL);
        stringBuffer.append("&sv=");
        String strSubstring = Build.VERSION.RELEASE;
        if (strSubstring != null && strSubstring.length() > 10) {
            strSubstring = strSubstring.substring(0, 10);
        }
        stringBuffer.append(strSubstring);
        try {
            if (Build.VERSION.SDK_INT > 20) {
                String[] strArr = Build.SUPPORTED_ABIS;
                str = null;
                while (i < strArr.length) {
                    str = i == 0 ? strArr[i] + ";" : str + strArr[i] + ";";
                    i++;
                }
            } else {
                str = Build.CPU_ABI2;
            }
        } catch (Error e) {
            str = null;
        } catch (Exception e2) {
            str = null;
        }
        if (str != null) {
            stringBuffer.append("&cpuabi=");
            stringBuffer.append(str);
        }
        stringBuffer.append("&pack=");
        stringBuffer.append(com.baidu.location.d.b.d);
        this.h = com.baidu.location.d.j.d() + "?&it=" + Jni.b(stringBuffer.toString());
    }

    @Override // com.baidu.location.d.e
    public void a(boolean z) throws IOException {
        if (z) {
            try {
                JSONObject jSONObject = new JSONObject(this.j);
                if ("up".equals(jSONObject.getString("res"))) {
                    this.f77a = jSONObject.getString("upath");
                    if (jSONObject.has("u1")) {
                        this.b = jSONObject.getString("u1");
                    }
                    if (jSONObject.has("u2")) {
                        this.c = jSONObject.getString("u2");
                    }
                    if (jSONObject.has("u1_rsa")) {
                        this.d = jSONObject.getString("u1_rsa");
                    }
                    f().post(new q(this));
                }
                if (jSONObject.has("ison")) {
                    this.e = jSONObject.getInt("ison");
                }
                g();
            } catch (Exception e) {
            }
        }
        com.baidu.location.d.c.a().a(System.currentTimeMillis());
    }

    public void c() {
        if (System.currentTimeMillis() - com.baidu.location.d.c.a().b() > 86400000) {
            f().postDelayed(new o(this), 10000L);
            f().postDelayed(new p(this), DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
        }
    }
}
