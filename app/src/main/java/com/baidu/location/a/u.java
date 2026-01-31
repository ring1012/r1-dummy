package com.baidu.location.a;

import android.location.Location;
import com.baidu.location.Jni;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class u {
    private int B;

    /* renamed from: a, reason: collision with root package name */
    long f84a = 0;
    private a z;
    private static ArrayList<String> b = new ArrayList<>();
    private static ArrayList<String> c = new ArrayList<>();
    private static ArrayList<String> d = new ArrayList<>();
    private static String e = com.baidu.location.d.i.f111a + "/yo.dat";
    private static final String f = com.baidu.location.d.i.f111a + "/yoh.dat";
    private static final String g = com.baidu.location.d.i.f111a + "/yom.dat";
    private static final String h = com.baidu.location.d.i.f111a + "/yol.dat";
    private static final String i = com.baidu.location.d.i.f111a + "/yor.dat";
    private static File j = null;
    private static int k = 8;
    private static int l = 8;
    private static int m = 16;
    private static int n = 1024;
    private static double o = 0.0d;
    private static double p = 0.1d;
    private static double q = 30.0d;
    private static double r = 100.0d;
    private static int s = 0;
    private static int t = 64;
    private static int u = 128;
    private static Location v = null;
    private static Location w = null;
    private static Location x = null;
    private static com.baidu.location.b.f y = null;
    private static u A = null;

    private class a extends com.baidu.location.d.e {

        /* renamed from: a, reason: collision with root package name */
        boolean f85a = false;
        int b = 0;
        int c = 0;
        private ArrayList<String> e = null;
        private boolean f = true;

        public a() {
            this.k = new HashMap();
        }

        @Override // com.baidu.location.d.e
        public void a() {
            this.h = com.baidu.location.d.j.c();
            if (this.b != 1) {
                this.h = com.baidu.location.d.j.e();
            }
            this.i = 2;
            if (this.e != null) {
                for (int i = 0; i < this.e.size(); i++) {
                    if (this.b == 1) {
                        this.k.put("cldc[" + i + "]", this.e.get(i));
                    } else {
                        this.k.put("cltr[" + i + "]", this.e.get(i));
                    }
                }
                this.k.put("trtm", String.format(Locale.CHINA, "%d", Long.valueOf(System.currentTimeMillis())));
                if (this.b != 1) {
                    this.k.put("qt", "cltrg");
                }
            }
        }

        @Override // com.baidu.location.d.e
        public void a(boolean z) {
            if (z && this.j != null) {
                if (this.e != null) {
                    this.e.clear();
                }
                try {
                    JSONObject jSONObject = new JSONObject(this.j);
                    if (jSONObject.has("ison") && jSONObject.getInt("ison") == 0) {
                        this.f = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (this.k != null) {
                this.k.clear();
            }
            this.f85a = false;
        }

        public void b() {
            if (this.f85a) {
                return;
            }
            if (o > 4 && this.c < o) {
                this.c++;
                return;
            }
            this.c = 0;
            this.f85a = true;
            this.b = 0;
            if (this.e == null || this.e.size() < 1) {
                if (this.e == null) {
                    this.e = new ArrayList<>();
                }
                this.b = 0;
                int length = 0;
                while (true) {
                    String strB = this.b < 2 ? u.b() : null;
                    if (strB == null && this.b != 1 && this.f) {
                        this.b = 2;
                        try {
                            strB = g.b();
                        } catch (Exception e) {
                            strB = null;
                        }
                    } else {
                        this.b = 1;
                    }
                    if (strB == null) {
                        break;
                    }
                    if (!strB.contains("err!")) {
                        this.e.add(strB);
                        length += strB.length();
                        if (length >= com.baidu.location.d.a.i) {
                            break;
                        }
                    }
                }
            }
            if (this.e == null || this.e.size() < 1) {
                this.e = null;
                this.f85a = false;
            } else if (this.b != 1) {
                b(com.baidu.location.d.j.e());
            } else {
                b(com.baidu.location.d.j.f);
            }
        }
    }

    private u() {
        this.z = null;
        this.B = 0;
        this.z = new a();
        this.B = 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0028 A[Catch: Exception -> 0x00bb, all -> 0x00c2, TryCatch #1 {Exception -> 0x00bb, blocks: (B:11:0x000e, B:13:0x0012, B:15:0x0023, B:16:0x0028, B:18:0x003b, B:19:0x0040, B:24:0x0066, B:27:0x0073, B:30:0x0086, B:32:0x0090, B:33:0x009b, B:34:0x00a0, B:23:0x0061), top: B:43:0x000e, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:8:0x000b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static synchronized int a(java.util.List<java.lang.String> r14, int r15) {
        /*
            java.lang.Class<com.baidu.location.a.u> r7 = com.baidu.location.a.u.class
            monitor-enter(r7)
            if (r14 == 0) goto Lb
            r0 = 256(0x100, float:3.59E-43)
            if (r15 > r0) goto Lb
            if (r15 >= 0) goto Le
        Lb:
            r0 = -1
        Lc:
            monitor-exit(r7)
            return r0
        Le:
            java.io.File r0 = com.baidu.location.a.u.j     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            if (r0 != 0) goto L28
            java.io.File r0 = new java.io.File     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            java.lang.String r1 = com.baidu.location.a.u.e     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r0.<init>(r1)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            com.baidu.location.a.u.j = r0     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            java.io.File r0 = com.baidu.location.a.u.j     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            boolean r0 = r0.exists()     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            if (r0 != 0) goto L28
            r0 = 0
            com.baidu.location.a.u.j = r0     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r0 = -2
            goto Lc
        L28:
            java.io.RandomAccessFile r8 = new java.io.RandomAccessFile     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            java.io.File r0 = com.baidu.location.a.u.j     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            java.lang.String r1 = "rw"
            r8.<init>(r0, r1)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            long r0 = r8.length()     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r2 = 1
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 >= 0) goto L40
            r8.close()     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r0 = -3
            goto Lc
        L40:
            long r0 = (long) r15     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r8.seek(r0)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            int r0 = r8.readInt()     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            int r1 = r8.readInt()     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            int r2 = r8.readInt()     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            int r3 = r8.readInt()     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            long r4 = r8.readLong()     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            boolean r6 = a(r0, r1, r2, r3, r4)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            if (r6 == 0) goto L61
            r6 = 1
            if (r1 >= r6) goto L66
        L61:
            r8.close()     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r0 = -4
            goto Lc
        L66:
            int r6 = com.baidu.location.a.u.n     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            byte[] r9 = new byte[r6]     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            int r6 = com.baidu.location.a.u.k     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r13 = r6
            r6 = r1
            r1 = r13
        L6f:
            if (r1 <= 0) goto La0
            if (r6 <= 0) goto La0
            int r10 = r0 + r6
            int r10 = r10 + (-1)
            int r10 = r10 % r2
            int r10 = r10 * r3
            long r10 = (long) r10     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            long r10 = r10 + r4
            r8.seek(r10)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            int r10 = r8.readInt()     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            if (r10 <= 0) goto L9b
            if (r10 >= r3) goto L9b
            r11 = 0
            r8.read(r9, r11, r10)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            int r11 = r10 + (-1)
            r11 = r9[r11]     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            if (r11 != 0) goto L9b
            java.lang.String r11 = new java.lang.String     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r12 = 0
            int r10 = r10 + (-1)
            r11.<init>(r9, r12, r10)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r14.add(r11)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
        L9b:
            int r1 = r1 + (-1)
            int r6 = r6 + (-1)
            goto L6f
        La0:
            long r10 = (long) r15     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r8.seek(r10)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r8.writeInt(r0)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r8.writeInt(r6)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r8.writeInt(r2)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r8.writeInt(r3)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r8.writeLong(r4)     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            r8.close()     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            int r0 = com.baidu.location.a.u.k     // Catch: java.lang.Exception -> Lbb java.lang.Throwable -> Lc2
            int r0 = r0 - r1
            goto Lc
        Lbb:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Throwable -> Lc2
            r0 = -5
            goto Lc
        Lc2:
            r0 = move-exception
            monitor-exit(r7)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.a.u.a(java.util.List, int):int");
    }

    public static synchronized u a() {
        if (A == null) {
            A = new u();
        }
        return A;
    }

    public static String a(int i2) throws IOException {
        String str;
        ArrayList<String> arrayList;
        String str2;
        String str3 = null;
        if (i2 == 1) {
            str = f;
            arrayList = b;
        } else if (i2 == 2) {
            str = g;
            arrayList = c;
        } else if (i2 == 3) {
            str = h;
            arrayList = d;
        } else {
            if (i2 != 4) {
                return null;
            }
            str = i;
            arrayList = d;
        }
        if (arrayList == null) {
            return null;
        }
        if (arrayList.size() < 1) {
            a(str, arrayList);
        }
        synchronized (u.class) {
            int size = arrayList.size();
            if (size > 0) {
                try {
                    str2 = arrayList.get(size - 1);
                } catch (Exception e2) {
                }
                try {
                    arrayList.remove(size - 1);
                } catch (Exception e3) {
                    str3 = str2;
                    str2 = str3;
                    return str2;
                }
            } else {
                str2 = null;
            }
        }
        return str2;
    }

    public static void a(int i2, boolean z) throws IOException {
        String str;
        ArrayList<String> arrayList;
        int i3;
        boolean z2;
        int i4;
        int i5;
        if (i2 == 1) {
            String str2 = f;
            if (z) {
                return;
            }
            str = str2;
            arrayList = b;
        } else if (i2 == 2) {
            String str3 = g;
            if (z) {
                str = str3;
                arrayList = b;
            } else {
                str = str3;
                arrayList = c;
            }
        } else if (i2 == 3) {
            String str4 = h;
            if (z) {
                str = str4;
                arrayList = c;
            } else {
                str = str4;
                arrayList = d;
            }
        } else {
            if (i2 != 4) {
                return;
            }
            String str5 = i;
            if (!z) {
                return;
            }
            str = str5;
            arrayList = d;
        }
        File file = new File(str);
        if (!file.exists()) {
            a(str);
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(4L);
            int i6 = randomAccessFile.readInt();
            int i7 = randomAccessFile.readInt();
            int i8 = randomAccessFile.readInt();
            int i9 = randomAccessFile.readInt();
            int i10 = randomAccessFile.readInt();
            int size = arrayList.size();
            while (true) {
                i3 = i10;
                if (size <= l) {
                    z2 = false;
                    break;
                }
                i10 = z ? i3 + 1 : i3;
                if (i8 >= i6) {
                    if (!z) {
                        z2 = true;
                        i3 = i10;
                        break;
                    }
                    randomAccessFile.seek((i9 * i7) + 128);
                    byte[] bytes = (arrayList.get(0) + (char) 0).getBytes();
                    randomAccessFile.writeInt(bytes.length);
                    randomAccessFile.write(bytes, 0, bytes.length);
                    arrayList.remove(0);
                    i4 = i9 + 1;
                    if (i4 > i8) {
                        i4 = 0;
                    }
                    i5 = i8;
                } else {
                    randomAccessFile.seek((i7 * i8) + 128);
                    byte[] bytes2 = (arrayList.get(0) + (char) 0).getBytes();
                    randomAccessFile.writeInt(bytes2.length);
                    randomAccessFile.write(bytes2, 0, bytes2.length);
                    arrayList.remove(0);
                    int i11 = i9;
                    i5 = i8 + 1;
                    i4 = i11;
                }
                size--;
                i8 = i5;
                i9 = i4;
            }
            randomAccessFile.seek(12L);
            randomAccessFile.writeInt(i8);
            randomAccessFile.writeInt(i9);
            randomAccessFile.writeInt(i3);
            randomAccessFile.close();
            if (!z2 || i2 >= 4) {
                return;
            }
            a(i2 + 1, true);
        } catch (Exception e2) {
        }
    }

    public static void a(com.baidu.location.b.a aVar, com.baidu.location.b.f fVar, Location location, String str) {
        String strA;
        if ((com.baidu.location.d.j.u == 3 && !a(location, fVar) && !a(location, false)) || aVar == null || aVar.c()) {
            return;
        }
        if (aVar != null && aVar.a()) {
            if (!a(location, fVar)) {
                fVar = null;
            }
            String strA2 = com.baidu.location.d.j.a(aVar, fVar, location, str, 1);
            if (strA2 != null) {
                c(Jni.a(strA2));
                w = location;
                v = location;
                if (fVar != null) {
                    y = fVar;
                    return;
                }
                return;
            }
            return;
        }
        if (fVar != null && fVar.j() && a(location, fVar)) {
            if (!a(location) && !com.baidu.location.b.b.a().d()) {
                str = "&cfr=1" + str;
            } else if (!a(location) && com.baidu.location.b.b.a().d()) {
                str = "&cfr=3" + str;
            } else if (com.baidu.location.b.b.a().d()) {
                str = "&cfr=2" + str;
            }
            String strA3 = com.baidu.location.d.j.a(aVar, fVar, location, str, 2);
            if (strA3 != null) {
                d(Jni.a(strA3));
                x = location;
                v = location;
                if (fVar != null) {
                    y = fVar;
                    return;
                }
                return;
            }
            return;
        }
        if (!a(location) && !com.baidu.location.b.b.a().d()) {
            str = "&cfr=1" + str;
        } else if (!a(location) && com.baidu.location.b.b.a().d()) {
            str = "&cfr=3" + str;
        } else if (com.baidu.location.b.b.a().d()) {
            str = "&cfr=2" + str;
        }
        com.baidu.location.b.f fVar2 = a(location, fVar) ? fVar : null;
        if ((aVar == null && fVar2 == null) || (strA = com.baidu.location.d.j.a(aVar, fVar2, location, str, 3)) == null) {
            return;
        }
        e(Jni.a(strA));
        v = location;
        if (fVar2 != null) {
            y = fVar2;
        }
    }

    public static void a(String str) throws IOException {
        try {
            File file = new File(str);
            if (file.exists()) {
                return;
            }
            File file2 = new File(com.baidu.location.d.i.f111a);
            if (!file2.exists()) {
                file2.mkdirs();
            }
            if (!file.createNewFile()) {
                file = null;
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(0L);
            randomAccessFile.writeInt(32);
            randomAccessFile.writeInt(2048);
            randomAccessFile.writeInt(1040);
            randomAccessFile.writeInt(0);
            randomAccessFile.writeInt(0);
            randomAccessFile.writeInt(0);
            randomAccessFile.close();
        } catch (Exception e2) {
        }
    }

    private static boolean a(int i2, int i3, int i4, int i5, long j2) {
        return i2 >= 0 && i2 < i4 && i3 >= 0 && i3 <= i4 && i4 >= 0 && i4 <= 1024 && i5 >= 128 && i5 <= 1024;
    }

    private static boolean a(Location location) {
        if (location == null) {
            return false;
        }
        if (w == null || v == null) {
            w = location;
            return true;
        }
        double dDistanceTo = location.distanceTo(w);
        return ((double) location.distanceTo(v)) > ((dDistanceTo * ((double) com.baidu.location.d.j.S)) + ((((double) com.baidu.location.d.j.R) * dDistanceTo) * dDistanceTo)) + ((double) com.baidu.location.d.j.T);
    }

    private static boolean a(Location location, com.baidu.location.b.f fVar) {
        if (location == null || fVar == null || fVar.f95a == null || fVar.f95a.isEmpty() || fVar.b(y)) {
            return false;
        }
        if (x != null) {
            return true;
        }
        x = location;
        return true;
    }

    public static boolean a(Location location, boolean z) {
        return com.baidu.location.b.d.a(v, location, z);
    }

    public static boolean a(String str, List<String> list) throws IOException {
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(8L);
            int i2 = randomAccessFile.readInt();
            int i3 = randomAccessFile.readInt();
            int i4 = randomAccessFile.readInt();
            byte[] bArr = new byte[n];
            int i5 = i3;
            int i6 = l + 1;
            boolean z = false;
            while (i6 > 0 && i5 > 0) {
                if (i5 < i4) {
                    i4 = 0;
                }
                try {
                    randomAccessFile.seek(((i5 - 1) * i2) + 128);
                    int i7 = randomAccessFile.readInt();
                    if (i7 > 0 && i7 < i2) {
                        randomAccessFile.read(bArr, 0, i7);
                        if (bArr[i7 - 1] == 0) {
                            list.add(0, new String(bArr, 0, i7 - 1));
                            z = true;
                        }
                    }
                    i6--;
                    i5--;
                } catch (Exception e2) {
                    return z;
                }
            }
            randomAccessFile.seek(12L);
            randomAccessFile.writeInt(i5);
            randomAccessFile.writeInt(i4);
            randomAccessFile.close();
            return z;
        } catch (Exception e3) {
            return false;
        }
    }

    public static String b() {
        return d();
    }

    public static synchronized void b(String str) {
        ArrayList<String> arrayList;
        if (!str.contains("err!")) {
            int i2 = com.baidu.location.d.j.p;
            if (i2 == 1) {
                arrayList = b;
            } else if (i2 == 2) {
                arrayList = c;
            } else if (i2 == 3) {
                arrayList = d;
            }
            if (arrayList != null) {
                if (arrayList.size() <= m) {
                    arrayList.add(str);
                }
                if (arrayList.size() >= m) {
                    a(i2, false);
                }
                while (arrayList.size() > m) {
                    arrayList.remove(0);
                }
            }
        }
    }

    private static void c(String str) {
        b(str);
    }

    public static String d() throws IOException {
        String strA = null;
        for (int i2 = 1; i2 < 5; i2++) {
            strA = a(i2);
            if (strA != null) {
                return strA;
            }
        }
        a(d, t);
        if (d.size() > 0) {
            strA = d.get(0);
            d.remove(0);
        }
        if (strA != null) {
            return strA;
        }
        a(d, s);
        if (d.size() > 0) {
            strA = d.get(0);
            d.remove(0);
        }
        if (strA != null) {
            return strA;
        }
        a(d, u);
        if (d.size() <= 0) {
            return strA;
        }
        String str = d.get(0);
        d.remove(0);
        return str;
    }

    private static void d(String str) {
        b(str);
    }

    public static void e() {
        l = 0;
        a(1, false);
        a(2, false);
        a(3, false);
        l = 8;
    }

    private static void e(String str) {
        b(str);
    }

    public static String f() throws IOException {
        String str = null;
        File file = new File(g);
        if (file.exists()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                randomAccessFile.seek(20L);
                int i2 = randomAccessFile.readInt();
                if (i2 > 128) {
                    String str2 = "&p1=" + i2;
                    randomAccessFile.seek(20L);
                    randomAccessFile.writeInt(0);
                    randomAccessFile.close();
                    return str2;
                }
                randomAccessFile.close();
            } catch (Exception e2) {
            }
        }
        File file2 = new File(h);
        if (file2.exists()) {
            try {
                RandomAccessFile randomAccessFile2 = new RandomAccessFile(file2, "rw");
                randomAccessFile2.seek(20L);
                int i3 = randomAccessFile2.readInt();
                if (i3 > 256) {
                    String str3 = "&p2=" + i3;
                    randomAccessFile2.seek(20L);
                    randomAccessFile2.writeInt(0);
                    randomAccessFile2.close();
                    return str3;
                }
                randomAccessFile2.close();
            } catch (Exception e3) {
            }
        }
        File file3 = new File(i);
        if (!file3.exists()) {
            return null;
        }
        try {
            RandomAccessFile randomAccessFile3 = new RandomAccessFile(file3, "rw");
            randomAccessFile3.seek(20L);
            int i4 = randomAccessFile3.readInt();
            if (i4 > 512) {
                str = "&p3=" + i4;
                randomAccessFile3.seek(20L);
                randomAccessFile3.writeInt(0);
                randomAccessFile3.close();
            } else {
                randomAccessFile3.close();
            }
            return str;
        } catch (Exception e4) {
            return str;
        }
    }

    public void c() {
        if (com.baidu.location.b.g.i()) {
            this.z.b();
        }
    }
}
