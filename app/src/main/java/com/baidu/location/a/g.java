package com.baidu.location.a;

import android.location.Location;
import com.baidu.location.Jni;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;

/* loaded from: classes.dex */
public class g {

    /* renamed from: a, reason: collision with root package name */
    private static g f66a = null;
    private static String b = "Temp_in.dat";
    private static File c = new File(com.baidu.location.d.i.f111a, b);
    private static StringBuffer d = null;
    private static boolean e = true;
    private static int f = 0;
    private static int g = 0;
    private static long h = 0;
    private static long i = 0;
    private static long j = 0;
    private static double k = 0.0d;
    private static double l = 0.0d;
    private static int m = 0;
    private static int n = 0;
    private static int o = 0;
    private String p;
    private boolean q = true;

    private g(String str) {
        this.p = null;
        if (str == null) {
            str = "";
        } else if (str.length() > 100) {
            str = str.substring(0, 100);
        }
        this.p = str;
    }

    public static g a() {
        if (f66a == null) {
            f66a = new g(com.baidu.location.d.b.a().c());
        }
        return f66a;
    }

    private String a(int i2) throws IOException {
        if (!c.exists()) {
            return null;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(c, "rw");
            randomAccessFile.seek(0L);
            int i3 = randomAccessFile.readInt();
            if (!a(i3, randomAccessFile.readInt(), randomAccessFile.readInt())) {
                randomAccessFile.close();
                d();
                return null;
            }
            if (i2 == 0 || i2 == i3 + 1) {
                randomAccessFile.close();
                return null;
            }
            long j2 = 12 + 0 + ((i2 - 1) * 1024);
            randomAccessFile.seek(j2);
            int i4 = randomAccessFile.readInt();
            byte[] bArr = new byte[i4];
            randomAccessFile.seek(j2 + 4);
            for (int i5 = 0; i5 < i4; i5++) {
                bArr[i5] = randomAccessFile.readByte();
            }
            randomAccessFile.close();
            return new String(bArr);
        } catch (IOException e2) {
            return null;
        }
    }

    private static boolean a(int i2, int i3, int i4) {
        if (i2 < 0 || i2 > com.baidu.location.d.j.ad) {
            return false;
        }
        if (i3 < 0 || i3 > i2 + 1) {
            return false;
        }
        return i4 >= 1 && i4 <= i2 + 1 && i4 <= com.baidu.location.d.j.ad;
    }

    private boolean a(Location location, int i2, int i3) throws IOException {
        if (location == null || !com.baidu.location.d.j.Z || !this.q) {
            return false;
        }
        if (com.baidu.location.d.j.ab < 5) {
            com.baidu.location.d.j.ab = 5;
        } else if (com.baidu.location.d.j.ab > 1000) {
            com.baidu.location.d.j.ab = 1000;
        }
        if (com.baidu.location.d.j.ac < 5) {
            com.baidu.location.d.j.ac = 5;
        } else if (com.baidu.location.d.j.ac > 3600) {
            com.baidu.location.d.j.ac = 3600;
        }
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        long time = location.getTime() / 1000;
        if (e) {
            f = 1;
            d = new StringBuffer("");
            d.append(String.format(Locale.CHINA, "&nr=%s&traj=%d,%.5f,%.5f|", this.p, Long.valueOf(time), Double.valueOf(longitude), Double.valueOf(latitude)));
            g = d.length();
            h = time;
            k = longitude;
            l = latitude;
            i = (long) Math.floor((longitude * 100000.0d) + 0.5d);
            j = (long) Math.floor((latitude * 100000.0d) + 0.5d);
            e = false;
            return true;
        }
        float[] fArr = new float[1];
        Location.distanceBetween(latitude, longitude, l, k, fArr);
        long j2 = time - h;
        if (fArr[0] < com.baidu.location.d.j.ab && j2 < com.baidu.location.d.j.ac) {
            return false;
        }
        if (d == null) {
            f++;
            g = 0;
            d = new StringBuffer("");
            d.append(String.format(Locale.CHINA, "&nr=%s&traj=%d,%.5f,%.5f|", this.p, Long.valueOf(time), Double.valueOf(longitude), Double.valueOf(latitude)));
            g = d.length();
            h = time;
            k = longitude;
            l = latitude;
            i = (long) Math.floor((longitude * 100000.0d) + 0.5d);
            j = (long) Math.floor((latitude * 100000.0d) + 0.5d);
        } else {
            k = longitude;
            l = latitude;
            long jFloor = (long) Math.floor((longitude * 100000.0d) + 0.5d);
            long jFloor2 = (long) Math.floor((latitude * 100000.0d) + 0.5d);
            m = (int) (time - h);
            n = (int) (jFloor - i);
            o = (int) (jFloor2 - j);
            d.append(String.format(Locale.CHINA, "%d,%d,%d|", Integer.valueOf(m), Integer.valueOf(n), Integer.valueOf(o)));
            g = d.length();
            h = time;
            i = jFloor;
            j = jFloor2;
        }
        if (g + 15 > 750) {
            a(d.toString());
            d = null;
        }
        if (f >= com.baidu.location.d.j.ad) {
            this.q = false;
        }
        return true;
    }

    private boolean a(String str) throws IOException {
        if (str == null || !str.startsWith("&nr")) {
            return false;
        }
        if (!c.exists() && !d()) {
            return false;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(c, "rw");
            randomAccessFile.seek(0L);
            int i2 = randomAccessFile.readInt();
            int i3 = randomAccessFile.readInt();
            int i4 = randomAccessFile.readInt();
            if (!a(i2, i3, i4)) {
                randomAccessFile.close();
                d();
                return false;
            }
            if (com.baidu.location.d.j.aa) {
                if (i2 == com.baidu.location.d.j.ad) {
                    if (str.equals(a(i4 == 1 ? com.baidu.location.d.j.ad : i4 - 1))) {
                        randomAccessFile.close();
                        return false;
                    }
                } else if (i4 > 1 && str.equals(a(i4 - 1))) {
                    randomAccessFile.close();
                    return false;
                }
            }
            randomAccessFile.seek(((i4 - 1) * 1024) + 12 + 0);
            if (str.length() > 750) {
                randomAccessFile.close();
                return false;
            }
            String strA = Jni.a(str);
            int length = strA.length();
            if (length > 1020) {
                randomAccessFile.close();
                return false;
            }
            randomAccessFile.writeInt(length);
            randomAccessFile.writeBytes(strA);
            if (i2 == 0) {
                randomAccessFile.seek(0L);
                randomAccessFile.writeInt(1);
                randomAccessFile.writeInt(1);
                randomAccessFile.writeInt(2);
            } else if (i2 < com.baidu.location.d.j.ad - 1) {
                randomAccessFile.seek(0L);
                randomAccessFile.writeInt(i2 + 1);
                randomAccessFile.seek(8L);
                randomAccessFile.writeInt(i2 + 2);
            } else if (i2 == com.baidu.location.d.j.ad - 1) {
                randomAccessFile.seek(0L);
                randomAccessFile.writeInt(com.baidu.location.d.j.ad);
                if (i3 == 0 || i3 == 1) {
                    randomAccessFile.writeInt(2);
                }
                randomAccessFile.seek(8L);
                randomAccessFile.writeInt(1);
            } else if (i4 == i3) {
                int i5 = i4 == com.baidu.location.d.j.ad ? 1 : i4 + 1;
                int i6 = i5 == com.baidu.location.d.j.ad ? 1 : i5 + 1;
                randomAccessFile.seek(4L);
                randomAccessFile.writeInt(i6);
                randomAccessFile.writeInt(i5);
            } else {
                int i7 = i4 == com.baidu.location.d.j.ad ? 1 : i4 + 1;
                if (i7 == i3) {
                    int i8 = i7 == com.baidu.location.d.j.ad ? 1 : i7 + 1;
                    randomAccessFile.seek(4L);
                    randomAccessFile.writeInt(i8);
                }
                randomAccessFile.seek(8L);
                randomAccessFile.writeInt(i7);
            }
            randomAccessFile.close();
            return true;
        } catch (IOException e2) {
            return false;
        }
    }

    public static String b() throws IOException {
        if (c != null && c.exists()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(c, "rw");
                randomAccessFile.seek(0L);
                int i2 = randomAccessFile.readInt();
                int i3 = randomAccessFile.readInt();
                int i4 = randomAccessFile.readInt();
                if (!a(i2, i3, i4)) {
                    randomAccessFile.close();
                    d();
                    return null;
                }
                if (i3 == 0 || i3 == i4) {
                    randomAccessFile.close();
                    return null;
                }
                long j2 = 0 + ((i3 - 1) * 1024) + 12;
                randomAccessFile.seek(j2);
                int i5 = randomAccessFile.readInt();
                byte[] bArr = new byte[i5];
                randomAccessFile.seek(j2 + 4);
                for (int i6 = 0; i6 < i5; i6++) {
                    bArr[i6] = randomAccessFile.readByte();
                }
                String str = new String(bArr);
                int i7 = (i2 >= com.baidu.location.d.j.ad && i3 == com.baidu.location.d.j.ad) ? 1 : i3 + 1;
                randomAccessFile.seek(4L);
                randomAccessFile.writeInt(i7);
                randomAccessFile.close();
                return str;
            } catch (IOException e2) {
                return null;
            }
        }
        return null;
    }

    private static void c() {
        e = true;
        d = null;
        f = 0;
        g = 0;
        h = 0L;
        i = 0L;
        j = 0L;
        k = 0.0d;
        l = 0.0d;
        m = 0;
        n = 0;
        o = 0;
    }

    private static boolean d() throws IOException {
        if (c.exists()) {
            c.delete();
        }
        if (!c.getParentFile().exists()) {
            c.getParentFile().mkdirs();
        }
        try {
            c.createNewFile();
            RandomAccessFile randomAccessFile = new RandomAccessFile(c, "rw");
            randomAccessFile.seek(0L);
            randomAccessFile.writeInt(0);
            randomAccessFile.writeInt(0);
            randomAccessFile.writeInt(1);
            randomAccessFile.close();
            c();
            return c.exists();
        } catch (IOException e2) {
            return false;
        }
    }

    public boolean a(Location location) {
        return a(location, com.baidu.location.d.j.ab, com.baidu.location.d.j.ac);
    }
}
