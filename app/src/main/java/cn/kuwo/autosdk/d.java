package cn.kuwo.autosdk;

/* loaded from: classes.dex */
class d implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    int f10a;
    int b;
    byte[] c;
    int d;
    final /* synthetic */ b e;

    d(b bVar) {
        this.e = bVar;
    }

    public d a(int i, int i2, byte[] bArr, int i3) {
        this.f10a = i;
        this.b = i2;
        this.c = bArr;
        this.d = i3;
        return this;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.e.p) {
            return;
        }
        synchronized (this.e) {
            this.e.x.a(this.e, this.f10a, this.b, this.c, this.d);
        }
        this.c = null;
    }
}
