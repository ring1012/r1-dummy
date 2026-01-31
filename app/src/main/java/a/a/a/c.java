package a.a.a;

/* loaded from: classes.dex */
public class c {
    private static String[] a(char c) {
        return a.a().a(c);
    }

    public static String[] a(char c, a.a.a.a.b bVar) {
        return b(c, bVar);
    }

    private static String[] b(char c, a.a.a.a.b bVar) {
        String[] strArrA = a(c);
        if (strArrA == null) {
            return null;
        }
        for (int i = 0; i < strArrA.length; i++) {
            strArrA[i] = b.a(strArrA[i], bVar);
        }
        return strArrA;
    }
}
