package com.unisound.common;

import cn.yunzhisheng.common.PinyinConverter;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public final class l implements Closeable {

    /* renamed from: a, reason: collision with root package name */
    static final String f264a = "journal";
    static final String b = "journal.tmp";
    static final String c = "libcore.io.DiskLruCache";
    static final String d = "1";
    static final long e = -1;
    private static final String f = "CLEAN";
    private static final String g = "DIRTY";
    private static final String h = "REMOVE";
    private static final String i = "READ";
    private static final Charset j = Charset.forName("UTF-8");
    private static final int k = 8192;
    private final File l;
    private final File m;
    private final File n;
    private final int o;
    private final long p;
    private final int q;
    private Writer s;
    private int u;
    private long r = 0;
    private final LinkedHashMap<String, p> t = new LinkedHashMap<>(0, 0.75f, true);
    private long v = 0;
    private final ExecutorService w = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue());
    private final Callable<Void> x = new m(this);

    private l(File file, int i2, int i3, long j2) {
        this.l = file;
        this.o = i2;
        this.m = new File(file, f264a);
        this.n = new File(file, b);
        this.q = i3;
        this.p = j2;
    }

    public static l a(File file, int i2, int i3, long j2) throws IOException {
        if (j2 <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (i3 <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }
        l lVar = new l(file, i2, i3, j2);
        if (lVar.m.exists()) {
            try {
                lVar.i();
                lVar.j();
                lVar.s = new BufferedWriter(new FileWriter(lVar.m, true), 8192);
                return lVar;
            } catch (IOException e2) {
                lVar.f();
            }
        }
        file.mkdirs();
        l lVar2 = new l(file, i2, i3, j2);
        lVar2.k();
        return lVar2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized n a(String str, long j2) {
        p pVar;
        n nVar;
        m();
        e(str);
        p pVar2 = this.t.get(str);
        if (j2 == -1 || (pVar2 != null && pVar2.f == j2)) {
            if (pVar2 == null) {
                p pVar3 = new p(this, str, null);
                this.t.put(str, pVar3);
                pVar = pVar3;
            } else if (pVar2.e != null) {
                nVar = null;
            } else {
                pVar = pVar2;
            }
            nVar = new n(this, pVar, null);
            pVar.e = nVar;
            this.s.write("DIRTY " + str + '\n');
            this.s.flush();
        } else {
            nVar = null;
        }
        return nVar;
    }

    public static String a(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder(80);
        while (true) {
            int i2 = inputStream.read();
            if (i2 == -1) {
                throw new EOFException();
            }
            if (i2 == 10) {
                int length = sb.length();
                if (length > 0 && sb.charAt(length - 1) == '\r') {
                    sb.setLength(length - 1);
                }
                return sb.toString();
            }
            sb.append((char) i2);
        }
    }

    public static String a(Reader reader) throws IOException {
        try {
            StringWriter stringWriter = new StringWriter();
            char[] cArr = new char[1024];
            while (true) {
                int i2 = reader.read(cArr);
                if (i2 == -1) {
                    return stringWriter.toString();
                }
                stringWriter.write(cArr, 0, i2);
            }
        } finally {
            reader.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(n nVar, boolean z) {
        synchronized (this) {
            p pVar = nVar.b;
            if (pVar.e != nVar) {
                throw new IllegalStateException();
            }
            if (z && !pVar.d) {
                for (int i2 = 0; i2 < this.q; i2++) {
                    if (!pVar.b(i2).exists()) {
                        nVar.b();
                        throw new IllegalStateException("edit didn't create file " + i2);
                    }
                }
            }
            for (int i3 = 0; i3 < this.q; i3++) {
                File fileB = pVar.b(i3);
                if (!z) {
                    b(fileB);
                } else if (fileB.exists()) {
                    File fileA = pVar.a(i3);
                    fileB.renameTo(fileA);
                    long j2 = pVar.c[i3];
                    long length = fileA.length();
                    pVar.c[i3] = length;
                    this.r = (this.r - j2) + length;
                }
            }
            this.u++;
            pVar.e = null;
            if (pVar.d || z) {
                pVar.d = true;
                this.s.write("CLEAN " + pVar.b + pVar.a() + '\n');
                if (z) {
                    long j3 = this.v;
                    this.v = 1 + j3;
                    pVar.f = j3;
                }
            } else {
                this.t.remove(pVar.b);
                this.s.write("REMOVE " + pVar.b + '\n');
            }
            if (this.r > this.p || l()) {
                this.w.submit(this.x);
            }
        }
    }

    public static void a(Closeable closeable) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e2) {
                throw e2;
            } catch (Exception e3) {
            }
        }
    }

    public static void a(File file) throws IOException {
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles == null) {
            throw new IllegalArgumentException("not a directory: " + file);
        }
        for (File file2 : fileArrListFiles) {
            if (file2.isDirectory()) {
                a(file2);
            }
            if (!file2.delete()) {
                throw new IOException("failed to delete file: " + file2);
            }
        }
    }

    private static <T> T[] a(T[] tArr, int i2, int i3) {
        int length = tArr.length;
        if (i2 > i3) {
            throw new IllegalArgumentException();
        }
        if (i2 < 0 || i2 > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i4 = i3 - i2;
        int iMin = Math.min(i4, length - i2);
        T[] tArr2 = (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), i4));
        System.arraycopy(tArr, i2, tArr2, 0, iMin);
        return tArr2;
    }

    private static void b(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String c(InputStream inputStream) {
        return a((Reader) new InputStreamReader(inputStream, j));
    }

    private void d(String str) throws IOException {
        p pVar;
        m mVar = null;
        String[] strArrSplit = str.split(PinyinConverter.PINYIN_SEPARATOR);
        if (strArrSplit.length < 2) {
            throw new IOException("unexpected journal line: " + str);
        }
        String str2 = strArrSplit[1];
        if (strArrSplit[0].equals(h) && strArrSplit.length == 2) {
            this.t.remove(str2);
            return;
        }
        p pVar2 = this.t.get(str2);
        if (pVar2 == null) {
            p pVar3 = new p(this, str2, mVar);
            this.t.put(str2, pVar3);
            pVar = pVar3;
        } else {
            pVar = pVar2;
        }
        if (strArrSplit[0].equals(f) && strArrSplit.length == this.q + 2) {
            pVar.d = true;
            pVar.e = null;
            pVar.a((String[]) a(strArrSplit, 2, strArrSplit.length));
        } else if (strArrSplit[0].equals(g) && strArrSplit.length == 2) {
            pVar.e = new n(this, pVar, mVar);
        } else if (!strArrSplit[0].equals(i) || strArrSplit.length != 2) {
            throw new IOException("unexpected journal line: " + str);
        }
    }

    private void e(String str) {
        if (str.contains(PinyinConverter.PINYIN_SEPARATOR) || str.contains("\n") || str.contains("\r")) {
            throw new IllegalArgumentException("keys must not contain spaces or newlines: \"" + str + "\"");
        }
    }

    private void i() throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(this.m), 8192);
        try {
            String strA = a((InputStream) bufferedInputStream);
            String strA2 = a((InputStream) bufferedInputStream);
            String strA3 = a((InputStream) bufferedInputStream);
            String strA4 = a((InputStream) bufferedInputStream);
            String strA5 = a((InputStream) bufferedInputStream);
            if (!c.equals(strA) || !"1".equals(strA2) || !Integer.toString(this.o).equals(strA3) || !Integer.toString(this.q).equals(strA4) || !"".equals(strA5)) {
                throw new IOException("unexpected journal header: [" + strA + ", " + strA2 + ", " + strA4 + ", " + strA5 + "]");
            }
            while (true) {
                try {
                    d(a((InputStream) bufferedInputStream));
                } catch (EOFException e2) {
                    return;
                }
            }
        } finally {
            a((Closeable) bufferedInputStream);
        }
    }

    private void j() throws IOException {
        b(this.n);
        Iterator<p> it = this.t.values().iterator();
        while (it.hasNext()) {
            p next = it.next();
            if (next.e == null) {
                for (int i2 = 0; i2 < this.q; i2++) {
                    this.r += next.c[i2];
                }
            } else {
                next.e = null;
                for (int i3 = 0; i3 < this.q; i3++) {
                    b(next.a(i3));
                    b(next.b(i3));
                }
                it.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void k() {
        if (this.s != null) {
            this.s.close();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.n), 8192);
        bufferedWriter.write(c);
        bufferedWriter.write("\n");
        bufferedWriter.write("1");
        bufferedWriter.write("\n");
        bufferedWriter.write(Integer.toString(this.o));
        bufferedWriter.write("\n");
        bufferedWriter.write(Integer.toString(this.q));
        bufferedWriter.write("\n");
        bufferedWriter.write("\n");
        for (p pVar : this.t.values()) {
            if (pVar.e != null) {
                bufferedWriter.write("DIRTY " + pVar.b + '\n');
            } else {
                bufferedWriter.write("CLEAN " + pVar.b + pVar.a() + '\n');
            }
        }
        bufferedWriter.close();
        this.n.renameTo(this.m);
        this.s = new BufferedWriter(new FileWriter(this.m, true), 8192);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean l() {
        return this.u >= 2000 && this.u >= this.t.size();
    }

    private void m() {
        if (this.s == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n() {
        while (this.r > this.p) {
            c(this.t.entrySet().iterator().next().getKey());
        }
    }

    public synchronized q a(String str) {
        q qVar = null;
        synchronized (this) {
            m();
            e(str);
            p pVar = this.t.get(str);
            if (pVar != null && pVar.d) {
                InputStream[] inputStreamArr = new InputStream[this.q];
                for (int i2 = 0; i2 < this.q; i2++) {
                    try {
                        inputStreamArr[i2] = new FileInputStream(pVar.a(i2));
                    } catch (FileNotFoundException e2) {
                    }
                }
                this.u++;
                this.s.append((CharSequence) ("READ " + str + '\n'));
                if (l()) {
                    this.w.submit(this.x);
                }
                qVar = new q(this, str, pVar.f, inputStreamArr, null);
            }
        }
        return qVar;
    }

    public File a() {
        return this.l;
    }

    public long b() {
        return this.p;
    }

    public n b(String str) {
        return a(str, -1L);
    }

    public synchronized long c() {
        return this.r;
    }

    public synchronized boolean c(String str) {
        boolean z;
        synchronized (this) {
            m();
            e(str);
            p pVar = this.t.get(str);
            if (pVar == null || pVar.e != null) {
                z = false;
            } else {
                for (int i2 = 0; i2 < this.q; i2++) {
                    File fileA = pVar.a(i2);
                    if (!fileA.delete()) {
                        throw new IOException("failed to delete " + fileA);
                    }
                    this.r -= pVar.c[i2];
                    pVar.c[i2] = 0;
                }
                this.u++;
                this.s.append((CharSequence) ("REMOVE " + str + '\n'));
                this.t.remove(str);
                if (l()) {
                    this.w.submit(this.x);
                }
                z = true;
            }
        }
        return z;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() {
        if (this.s != null) {
            Iterator it = new ArrayList(this.t.values()).iterator();
            while (it.hasNext()) {
                p pVar = (p) it.next();
                if (pVar.e != null) {
                    pVar.e.b();
                }
            }
            n();
            this.s.close();
            this.s = null;
        }
    }

    public boolean d() {
        return this.s == null;
    }

    public synchronized void e() {
        m();
        n();
        this.s.flush();
    }

    public void f() throws IOException {
        close();
        a(this.l);
    }

    public ArrayList<String> g() {
        Iterator<String> it = this.t.keySet().iterator();
        ArrayList<String> arrayList = new ArrayList<>();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList;
    }
}
