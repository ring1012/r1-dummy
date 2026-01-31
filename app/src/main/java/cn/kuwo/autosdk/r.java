package cn.kuwo.autosdk;

/* loaded from: classes.dex */
public final class r {

    /* renamed from: a, reason: collision with root package name */
    private static volatile int f19a = 0;
    private static t[] b = new t[5];

    public static void a(s sVar, Runnable runnable) {
        if (sVar == s.NET) {
        }
        c().a(runnable, 0);
    }

    private static t c() {
        t tVar;
        t tVar2 = null;
        if (f19a == 0) {
            return new t(tVar2);
        }
        synchronized (b) {
            if (f19a == 0) {
                tVar = new t(null);
            } else {
                f19a--;
                tVar = b[f19a];
                b[f19a] = null;
            }
        }
        return tVar;
    }
}
