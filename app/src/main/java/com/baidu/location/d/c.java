package com.baidu.location.d;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/* loaded from: classes.dex */
public class c {
    static c c;

    /* renamed from: a, reason: collision with root package name */
    String f103a = "firll.dat";
    int b = 3164;
    int d = 0;
    int e = 20;
    int f = 40;
    int g = 60;
    int h = 80;
    int i = 100;

    private long a(int i) throws Throwable {
        RandomAccessFile randomAccessFile;
        String strH = j.h();
        if (strH == null) {
            return -1L;
        }
        String str = strH + File.separator + this.f103a;
        RandomAccessFile randomAccessFile2 = null;
        try {
            randomAccessFile = new RandomAccessFile(str, "rw");
        } catch (Exception e) {
        } catch (Throwable th) {
            th = th;
            randomAccessFile = null;
        }
        try {
            randomAccessFile.seek(i);
            int i2 = randomAccessFile.readInt();
            long j = randomAccessFile.readLong();
            if (i2 == randomAccessFile.readInt()) {
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e2) {
                    }
                }
                return j;
            }
            if (randomAccessFile == null) {
                return -1L;
            }
            try {
                randomAccessFile.close();
                return -1L;
            } catch (IOException e3) {
                return -1L;
            }
        } catch (Exception e4) {
            randomAccessFile2 = randomAccessFile;
            if (randomAccessFile2 == null) {
                return -1L;
            }
            try {
                randomAccessFile2.close();
                return -1L;
            } catch (IOException e5) {
                return -1L;
            }
        } catch (Throwable th2) {
            th = th2;
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e6) {
                }
            }
            throw th;
        }
    }

    public static c a() {
        if (c == null) {
            c = new c();
        }
        return c;
    }

    private void a(int i, long j) throws IOException {
        String strH = j.h();
        if (strH == null) {
            return;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(strH + File.separator + this.f103a, "rw");
            randomAccessFile.seek(i);
            randomAccessFile.writeInt(this.b);
            randomAccessFile.writeLong(j);
            randomAccessFile.writeInt(this.b);
            randomAccessFile.close();
        } catch (Exception e) {
        }
    }

    public void a(long j) throws IOException {
        a(this.d, j);
    }

    public long b() {
        return a(this.d);
    }
}
