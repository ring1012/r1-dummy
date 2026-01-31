package a.a.a;

import java.io.BufferedInputStream;

/* loaded from: classes.dex */
class d {

    /* renamed from: a, reason: collision with root package name */
    static Class f6a;

    d() {
    }

    static BufferedInputStream a(String str) {
        Class clsB;
        if (f6a == null) {
            clsB = b("a.a.a.d");
            f6a = clsB;
        } else {
            clsB = f6a;
        }
        return new BufferedInputStream(clsB.getResourceAsStream(str));
    }

    static Class b(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }
}
