package a.a.a;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/* loaded from: classes.dex */
class a {

    /* renamed from: a, reason: collision with root package name */
    private Properties f0a;

    /* renamed from: a.a.a.a$1, reason: invalid class name */
    static class AnonymousClass1 {
    }

    /* renamed from: a.a.a.a$a, reason: collision with other inner class name */
    private static class C0000a {

        /* renamed from: a, reason: collision with root package name */
        static final a f2a = new a(null);
    }

    private a() throws IOException {
        this.f0a = null;
        c();
    }

    a(AnonymousClass1 anonymousClass1) {
        this();
    }

    static a a() {
        return C0000a.f2a;
    }

    private void a(Properties properties) {
        this.f0a = properties;
    }

    private boolean a(String str) {
        return str != null && !str.equals("(none0)") && str.startsWith("(") && str.endsWith(")");
    }

    private String b(char c) {
        String property = b().getProperty(Integer.toHexString(c).toUpperCase());
        if (a(property)) {
            return property;
        }
        return null;
    }

    private Properties b() {
        return this.f0a;
    }

    private void c() throws IOException {
        try {
            a(new Properties());
            b().load(d.a("/pinyindb/unicode_to_hanyu_pinyin.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    String[] a(char c) {
        String strB = b(c);
        if (strB == null) {
            return null;
        }
        int iIndexOf = strB.indexOf("(");
        return strB.substring(iIndexOf + "(".length(), strB.lastIndexOf(")")).split(",");
    }
}
