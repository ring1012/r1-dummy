package com.baidu.location.a;

import android.location.Location;
import android.os.Environment;
import android.os.Handler;
import cn.yunzhisheng.asr.JniUscClient;
import com.baidu.location.Jni;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.unisound.client.ErrorCode;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import nluparser.scheme.ASR;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class d {
    private Handler I;
    private static d j = null;
    public static String f = "0";
    private int k = 1;
    private double l = 0.699999988079071d;
    private String m = "3G|4G";
    private int n = 1;
    private int o = 307200;
    private int p = 15;
    private int q = 1;
    private double r = 3.5d;
    private double s = 3.0d;
    private double t = 0.5d;
    private int u = 300;
    private int v = 60;
    private int w = 0;
    private int x = 60;
    private int y = 0;
    private long z = 0;
    private a A = null;
    private boolean B = false;
    private boolean C = false;
    private int D = 0;
    private float E = 0.0f;
    private float F = 0.0f;
    private long G = 0;
    private int H = cn.yunzhisheng.asr.a.U;

    /* renamed from: a, reason: collision with root package name */
    long f62a = 0;
    Location b = null;
    Location c = null;
    StringBuilder d = null;
    long e = 0;
    private byte[] J = new byte[4];
    private byte[] K = null;
    private int L = 0;
    private List<Byte> M = null;
    private boolean N = false;
    int g = 0;
    double h = 116.22345545d;
    double i = 40.245667323d;

    class a extends com.baidu.location.d.e {

        /* renamed from: a, reason: collision with root package name */
        String f63a = null;

        public a() {
            this.k = new HashMap();
        }

        @Override // com.baidu.location.d.e
        public void a() {
            this.h = "http://loc.map.baidu.com/cc.php";
            String strA = Jni.a(this.f63a);
            this.f63a = null;
            this.k.put("q", strA);
        }

        public void a(String str) {
            this.f63a = str;
            e();
        }

        @Override // com.baidu.location.d.e
        public void a(boolean z) throws JSONException {
            if (z && this.j != null) {
                try {
                    JSONObject jSONObject = new JSONObject(this.j);
                    jSONObject.put("prod", com.baidu.location.d.b.d);
                    jSONObject.put("uptime", System.currentTimeMillis());
                    d.this.e(jSONObject.toString());
                } catch (Exception e) {
                }
            }
            if (this.k != null) {
                this.k.clear();
            }
        }
    }

    private d() {
        this.I = null;
        this.I = new Handler();
    }

    public static d a() {
        if (j == null) {
            j = new d();
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(File file, String str) throws IOException {
        String string = UUID.randomUUID().toString();
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Charset", com.unisound.b.f.b);
            httpURLConnection.setRequestProperty("connection", JniUscClient.s);
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + string);
            if (file != null && file.exists()) {
                OutputStream outputStream = httpURLConnection.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("--");
                stringBuffer.append(string);
                stringBuffer.append(IOUtils.LINE_SEPARATOR_WINDOWS);
                stringBuffer.append("Content-Disposition: form-data; name=\"location_dat\"; filename=\"" + file.getName() + "\"" + IOUtils.LINE_SEPARATOR_WINDOWS);
                stringBuffer.append("Content-Type: application/octet-stream; charset=utf-8" + IOUtils.LINE_SEPARATOR_WINDOWS);
                stringBuffer.append(IOUtils.LINE_SEPARATOR_WINDOWS);
                dataOutputStream.write(stringBuffer.toString().getBytes());
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bArr = new byte[1024];
                while (true) {
                    int i = fileInputStream.read(bArr);
                    if (i == -1) {
                        break;
                    }
                    dataOutputStream.write(bArr, 0, i);
                }
                fileInputStream.close();
                dataOutputStream.write(IOUtils.LINE_SEPARATOR_WINDOWS.getBytes());
                dataOutputStream.write(("--" + string + "--" + IOUtils.LINE_SEPARATOR_WINDOWS).getBytes());
                dataOutputStream.flush();
                dataOutputStream.close();
                int responseCode = httpURLConnection.getResponseCode();
                outputStream.close();
                httpURLConnection.disconnect();
                this.y += ErrorCode.VPR_CLIENT_PARAM_ERROR;
                c(this.y);
                if (responseCode == 200) {
                    return "1";
                }
            }
        } catch (MalformedURLException e) {
        } catch (IOException e2) {
        }
        return "0";
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0036  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean a(java.lang.String r5, android.content.Context r6) {
        /*
            r4 = this;
            r1 = 0
            java.lang.String r0 = "activity"
            java.lang.Object r0 = r6.getSystemService(r0)     // Catch: java.lang.Exception -> L34
            android.app.ActivityManager r0 = (android.app.ActivityManager) r0     // Catch: java.lang.Exception -> L34
            java.util.List r0 = r0.getRunningAppProcesses()     // Catch: java.lang.Exception -> L34
            if (r0 == 0) goto L35
            java.util.Iterator r2 = r0.iterator()     // Catch: java.lang.Exception -> L34
        L13:
            boolean r0 = r2.hasNext()     // Catch: java.lang.Exception -> L34
            if (r0 == 0) goto L35
            java.lang.Object r0 = r2.next()     // Catch: java.lang.Exception -> L34
            android.app.ActivityManager$RunningAppProcessInfo r0 = (android.app.ActivityManager.RunningAppProcessInfo) r0     // Catch: java.lang.Exception -> L34
            java.lang.String r3 = r0.processName     // Catch: java.lang.Exception -> L34
            boolean r3 = r3.equals(r5)     // Catch: java.lang.Exception -> L34
            if (r3 == 0) goto L36
            int r0 = r0.importance     // Catch: java.lang.Exception -> L34
            r3 = 200(0xc8, float:2.8E-43)
            if (r0 == r3) goto L31
            r3 = 100
            if (r0 != r3) goto L36
        L31:
            r0 = 1
        L32:
            r1 = r0
            goto L13
        L34:
            r0 = move-exception
        L35:
            return r1
        L36:
            r0 = r1
            goto L32
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.a.d.a(java.lang.String, android.content.Context):boolean");
    }

    private byte[] a(int i) {
        return new byte[]{(byte) (i & 255), (byte) ((65280 & i) >> 8), (byte) ((16711680 & i) >> 16), (byte) (((-16777216) & i) >> 24)};
    }

    private byte[] a(String str) {
        int i = 0;
        if (str == null) {
            return null;
        }
        byte[] bytes = str.getBytes();
        byte bNextInt = (byte) new Random().nextInt(255);
        byte bNextInt2 = (byte) new Random().nextInt(255);
        byte[] bArr = new byte[bytes.length + 2];
        int length = bytes.length;
        int i2 = 0;
        while (i < length) {
            bArr[i2] = (byte) (bytes[i] ^ bNextInt);
            i++;
            i2++;
        }
        int i3 = i2 + 1;
        bArr[i2] = bNextInt;
        int i4 = i3 + 1;
        bArr[i3] = bNextInt2;
        return bArr;
    }

    private String b(String str) {
        Calendar calendar = Calendar.getInstance();
        return String.format(str, Integer.valueOf(calendar.get(1)), Integer.valueOf(calendar.get(2) + 1), Integer.valueOf(calendar.get(5)));
    }

    private void b(int i) {
        byte[] bArrA = a(i);
        for (int i2 = 0; i2 < 4; i2++) {
            this.M.add(Byte.valueOf(bArrA[i2]));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(Location location) throws IOException {
        c(location);
        h();
    }

    private void c() throws IOException {
        if (this.N) {
            return;
        }
        this.N = true;
        d(com.baidu.location.d.b.d);
        j();
        d();
    }

    private void c(int i) throws IOException {
        if (i == 0) {
            return;
        }
        try {
            File file = new File(com.baidu.location.d.i.f111a + "/grtcf.dat");
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
            randomAccessFile2.seek(8L);
            byte[] bytes2 = (b("%d_%02d_%02d") + ":" + i).getBytes();
            randomAccessFile2.writeInt(bytes2.length);
            randomAccessFile2.write(bytes2);
            randomAccessFile2.close();
        } catch (Exception e) {
        }
    }

    private void c(Location location) {
        if (System.currentTimeMillis() - this.f62a < this.H || location == null) {
            return;
        }
        if (location != null && location.hasSpeed() && location.getSpeed() > this.E) {
            this.E = location.getSpeed();
        }
        try {
            if (this.M == null) {
                this.M = new ArrayList();
                i();
                d(location);
            } else {
                e(location);
            }
        } catch (Exception e) {
        }
        this.L++;
    }

    private void c(String str) {
        if (str != null) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("on")) {
                    this.k = jSONObject.getInt("on");
                }
                if (jSONObject.has("bash")) {
                    this.l = jSONObject.getDouble("bash");
                }
                if (jSONObject.has(ASR.NET)) {
                    this.m = jSONObject.getString(ASR.NET);
                }
                if (jSONObject.has("tcon")) {
                    this.n = jSONObject.getInt("tcon");
                }
                if (jSONObject.has("tcsh")) {
                    this.o = jSONObject.getInt("tcsh");
                }
                if (jSONObject.has("per")) {
                    this.p = jSONObject.getInt("per");
                }
                if (jSONObject.has("chdron")) {
                    this.q = jSONObject.getInt("chdron");
                }
                if (jSONObject.has("spsh")) {
                    this.r = jSONObject.getDouble("spsh");
                }
                if (jSONObject.has("acsh")) {
                    this.s = jSONObject.getDouble("acsh");
                }
                if (jSONObject.has("stspsh")) {
                    this.t = jSONObject.getDouble("stspsh");
                }
                if (jSONObject.has("drstsh")) {
                    this.u = jSONObject.getInt("drstsh");
                }
                if (jSONObject.has("stper")) {
                    this.v = jSONObject.getInt("stper");
                }
                if (jSONObject.has("nondron")) {
                    this.w = jSONObject.getInt("nondron");
                }
                if (jSONObject.has("nondrper")) {
                    this.x = jSONObject.getInt("nondrper");
                }
                if (jSONObject.has("uptime")) {
                    this.z = jSONObject.getLong("uptime");
                }
                k();
            } catch (JSONException e) {
            }
        }
    }

    private void d() {
        String[] strArrSplit = (0 == 0 ? "7.3.2" : null).split("\\.");
        int length = strArrSplit.length;
        this.J[0] = 0;
        this.J[1] = 0;
        this.J[2] = 0;
        this.J[3] = 0;
        if (length >= 4) {
            length = 4;
        }
        for (int i = 0; i < length; i++) {
            try {
                this.J[i] = (byte) (Integer.valueOf(strArrSplit[i]).intValue() & 255);
            } catch (Exception e) {
            }
        }
        this.K = a(com.baidu.location.d.b.d + ":" + com.baidu.location.d.b.a().b);
    }

    private void d(Location location) {
        this.e = System.currentTimeMillis();
        b((int) (location.getTime() / 1000));
        b((int) (location.getLongitude() * 1000000.0d));
        b((int) (location.getLatitude() * 1000000.0d));
        char c = location.hasBearing() ? (char) 0 : (char) 1;
        char c2 = location.hasSpeed() ? (char) 0 : (char) 1;
        if (c > 0) {
            this.M.add((byte) 32);
        } else {
            this.M.add(Byte.valueOf((byte) (((byte) (((int) (location.getBearing() / 15.0f)) & 255)) & (-33))));
        }
        if (c2 > 0) {
            this.M.add((byte) -128);
        } else {
            this.M.add(Byte.valueOf((byte) (((byte) (((int) ((location.getSpeed() * 3.6d) / 4.0d)) & 255)) & 127)));
        }
        this.b = location;
    }

    private void d(String str) throws IOException {
        int i = 1;
        try {
            File file = new File(com.baidu.location.d.i.f111a + "/grtcf.dat");
            if (file.exists()) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(2L);
                int i2 = randomAccessFile.readInt();
                randomAccessFile.seek(8L);
                int i3 = randomAccessFile.readInt();
                byte[] bArr = new byte[i3];
                randomAccessFile.read(bArr, 0, i3);
                String str2 = new String(bArr);
                if (str2.contains(b("%d_%02d_%02d")) && str2.contains(":")) {
                    try {
                        String[] strArrSplit = str2.split(":");
                        if (strArrSplit.length > 1) {
                            this.y = Integer.valueOf(strArrSplit[1]).intValue();
                        }
                    } catch (Exception e) {
                    }
                }
                while (true) {
                    if (i > i2) {
                        break;
                    }
                    randomAccessFile.seek(i * 2048);
                    int i4 = randomAccessFile.readInt();
                    byte[] bArr2 = new byte[i4];
                    randomAccessFile.read(bArr2, 0, i4);
                    String str3 = new String(bArr2);
                    if (str != null && str3.contains(str)) {
                        c(str3);
                        break;
                    }
                    i++;
                }
                randomAccessFile.close();
            }
        } catch (Exception e2) {
        }
    }

    private void e(Location location) {
        if (location == null) {
            return;
        }
        int longitude = (int) ((location.getLongitude() - this.b.getLongitude()) * 1000000.0d);
        int latitude = (int) ((location.getLatitude() - this.b.getLatitude()) * 1000000.0d);
        char c = location.hasBearing() ? (char) 0 : (char) 1;
        char c2 = location.hasSpeed() ? (char) 0 : (char) 1;
        char c3 = longitude > 0 ? (char) 0 : (char) 1;
        int iAbs = Math.abs(longitude);
        char c4 = latitude > 0 ? (char) 0 : (char) 1;
        int iAbs2 = Math.abs(latitude);
        if (this.L > 1) {
            this.c = null;
            this.c = this.b;
        }
        this.b = location;
        if (this.b != null && this.c != null && this.b.getTime() > this.c.getTime() && this.b.getTime() - this.c.getTime() < DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS) {
            long time = this.b.getTime() - this.c.getTime();
            float[] fArr = new float[2];
            Location.distanceBetween(this.b.getAltitude(), this.b.getLongitude(), this.c.getLatitude(), this.c.getLongitude(), fArr);
            double speed = (2.0f * (fArr[0] - (this.c.getSpeed() * time))) / (time * time);
            if (speed > this.F) {
                this.F = (float) speed;
            }
        }
        this.M.add(Byte.valueOf((byte) (iAbs & 255)));
        this.M.add(Byte.valueOf((byte) ((65280 & iAbs) >> 8)));
        this.M.add(Byte.valueOf((byte) (iAbs2 & 255)));
        this.M.add(Byte.valueOf((byte) ((65280 & iAbs2) >> 8)));
        if (c > 0) {
            byte b = c4 > 0 ? (byte) 96 : (byte) 32;
            if (c3 > 0) {
                b = (byte) (b | (-128));
            }
            this.M.add(Byte.valueOf(b));
        } else {
            byte bearing = (byte) (((byte) (((int) (location.getBearing() / 15.0f)) & 255)) & 31);
            if (c4 > 0) {
                bearing = (byte) (bearing | 64);
            }
            if (c3 > 0) {
                bearing = (byte) (bearing | (-128));
            }
            this.M.add(Byte.valueOf(bearing));
        }
        if (c2 > 0) {
            this.M.add((byte) -128);
        } else {
            this.M.add(Byte.valueOf((byte) (((byte) (((int) ((location.getSpeed() * 3.6d) / 4.0d)) & 255)) & 127)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(String str) throws IOException {
        try {
            File file = new File(com.baidu.location.d.i.f111a + "/grtcf.dat");
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
            randomAccessFile2.seek(2L);
            int i = randomAccessFile2.readInt();
            int i2 = 1;
            while (i2 <= i) {
                randomAccessFile2.seek(i2 * 2048);
                int i3 = randomAccessFile2.readInt();
                byte[] bArr = new byte[i3];
                randomAccessFile2.read(bArr, 0, i3);
                if (new String(bArr).contains(com.baidu.location.d.b.d)) {
                    break;
                } else {
                    i2++;
                }
            }
            if (i2 >= i) {
                randomAccessFile2.seek(2L);
                randomAccessFile2.writeInt(i2);
            }
            randomAccessFile2.seek(i2 * 2048);
            byte[] bytes2 = str.getBytes();
            randomAccessFile2.writeInt(bytes2.length);
            randomAccessFile2.write(bytes2);
            randomAccessFile2.close();
        } catch (Exception e) {
        }
    }

    private boolean e() throws Throwable {
        RandomAccessFile randomAccessFile;
        FileChannel channel;
        FileLock fileLockTryLock = null;
        FileChannel fileChannel = null;
        FileLock fileLock = null;
        boolean z = false;
        try {
            File file = new File(com.baidu.location.d.j.g() + File.separator + "gflk.dat");
            if (!file.exists()) {
                file.createNewFile();
            }
            if (0 == 0) {
                randomAccessFile = new RandomAccessFile(file, "rw");
                try {
                    channel = randomAccessFile.getChannel();
                } catch (Exception e) {
                    if (0 != 0) {
                        try {
                            fileLock.release();
                        } catch (Exception e2) {
                        }
                    }
                    if (0 != 0) {
                        fileChannel.close();
                    }
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                    return z;
                } catch (Throwable th) {
                    th = th;
                    channel = null;
                }
                try {
                    fileLockTryLock = channel.tryLock();
                } catch (Exception e3) {
                    z = true;
                } catch (Throwable th2) {
                    th = th2;
                    if (0 != 0) {
                        try {
                            fileLock.release();
                        } catch (Exception e4) {
                            throw th;
                        }
                    }
                    if (channel != null) {
                        channel.close();
                    }
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                    throw th;
                }
            } else {
                randomAccessFile = null;
                channel = null;
            }
            if (fileLockTryLock != null) {
                try {
                    fileLockTryLock.release();
                } catch (Exception e5) {
                }
            }
            if (channel != null) {
                channel.close();
            }
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        } catch (Exception e6) {
            randomAccessFile = null;
        } catch (Throwable th3) {
            th = th3;
            randomAccessFile = null;
            channel = null;
        }
        return z;
    }

    private boolean f() {
        if (this.B) {
            if (!this.C) {
                if (this.E >= this.t) {
                    return true;
                }
                this.C = true;
                this.D = 0;
                this.D += this.p;
                return true;
            }
            if (this.E >= this.t) {
                this.D = 0;
                this.C = false;
                return true;
            }
            this.D += this.p;
            if (this.D <= this.u || System.currentTimeMillis() - this.G > this.v * 1000) {
                return true;
            }
        } else {
            if (this.E >= this.r || this.F >= this.s) {
                this.B = true;
                return true;
            }
            if (this.w == 1 && System.currentTimeMillis() - this.G > this.x * 1000) {
                return true;
            }
        }
        return false;
    }

    private void g() {
        this.M = null;
        this.e = 0L;
        this.L = 0;
        this.b = null;
        this.c = null;
        this.E = 0.0f;
        this.F = 0.0f;
    }

    private void h() throws IOException {
        if (this.e == 0 || System.currentTimeMillis() - this.e < this.p * 1000) {
            return;
        }
        if (com.baidu.location.f.b().getSharedPreferences("loc_navi_mode", 4).getBoolean("is_navi_on", false)) {
            g();
            return;
        }
        if (this.n == 1 && !f()) {
            g();
            return;
        }
        if (com.baidu.location.d.b.d.equals("com.ubercab.driver")) {
            if (e()) {
                g();
                return;
            }
        } else if (!a(com.baidu.location.d.b.d, com.baidu.location.f.b())) {
            g();
            return;
        }
        if (this.M != null) {
            int size = this.M.size();
            this.M.set(0, Byte.valueOf((byte) (size & 255)));
            this.M.set(1, Byte.valueOf((byte) ((65280 & size) >> 8)));
            this.M.set(3, Byte.valueOf((byte) (this.L & 255)));
            byte[] bArr = new byte[size];
            for (int i = 0; i < size; i++) {
                bArr[i] = this.M.get(i).byteValue();
            }
            if (Environment.getExternalStorageState().equals("mounted")) {
                File file = new File(Environment.getExternalStorageDirectory(), "baidu/tempdata");
                if (!file.exists()) {
                    file.mkdirs();
                }
                if (file.exists()) {
                    File file2 = new File(file, "intime.dat");
                    if (file2.exists()) {
                        file2.delete();
                    }
                    try {
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
                        bufferedOutputStream.write(bArr);
                        bufferedOutputStream.flush();
                        bufferedOutputStream.close();
                        new f(this).start();
                    } catch (Exception e) {
                    }
                }
            }
            g();
            this.G = System.currentTimeMillis();
        }
    }

    private void i() {
        this.M.add((byte) 0);
        this.M.add((byte) 0);
        if (f.equals("0")) {
            this.M.add((byte) -82);
        } else {
            this.M.add((byte) -66);
        }
        this.M.add((byte) 0);
        this.M.add(Byte.valueOf(this.J[0]));
        this.M.add(Byte.valueOf(this.J[1]));
        this.M.add(Byte.valueOf(this.J[2]));
        this.M.add(Byte.valueOf(this.J[3]));
        int length = this.K.length;
        this.M.add(Byte.valueOf((byte) ((length + 1) & 255)));
        for (int i = 0; i < length; i++) {
            this.M.add(Byte.valueOf(this.K[i]));
        }
    }

    private void j() {
        if (System.currentTimeMillis() - this.z > 86400000) {
            if (this.A == null) {
                this.A = new a();
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(com.baidu.location.d.b.a().a(false));
            stringBuffer.append(com.baidu.location.a.a.a().c());
            this.A.a(stringBuffer.toString());
        }
        k();
    }

    private void k() {
    }

    public void a(Location location) throws IOException {
        if (!this.N) {
            c();
        }
        if (this.k == 1 && this.m.contains(com.baidu.location.b.c.a(com.baidu.location.b.b.a().e()))) {
            if (this.n != 1 || this.y <= this.o) {
                this.I.post(new e(this, location));
            }
        }
    }

    public void b() {
        if (this.N) {
            this.N = false;
            g();
        }
    }
}
