package cn.kuwo.autosdk;

/* loaded from: classes.dex */
class c implements Runnable {
    private static /* synthetic */ int[] d;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ b f9a;
    private final /* synthetic */ e b;
    private final /* synthetic */ int c;

    c(b bVar, e eVar, int i) {
        this.f9a = bVar;
        this.b = eVar;
        this.c = i;
    }

    static /* synthetic */ int[] a() {
        int[] iArr = d;
        if (iArr == null) {
            iArr = new int[e.a().length];
            try {
                iArr[e.NOTIFY_FAILED.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[e.NOTIFY_FINISH.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[e.NOTIFY_START.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            d = iArr;
        }
        return iArr;
    }

    @Override // java.lang.Runnable
    public void run() {
        synchronized (this.f9a) {
            if (this.f9a.p) {
                return;
            }
            switch (a()[this.b.ordinal()]) {
                case 1:
                    this.f9a.x.a(this.f9a, this.c, this.f9a.z);
                    break;
                case 2:
                    this.f9a.x.b(this.f9a, this.f9a.z);
                    break;
                case 3:
                    this.f9a.x.a(this.f9a, this.f9a.z);
                    break;
            }
        }
    }
}
