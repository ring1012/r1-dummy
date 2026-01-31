package com.tencent.bugly.crashreport.crash;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class a implements Comparable<a> {

    /* renamed from: a, reason: collision with root package name */
    public long f149a = -1;
    public long b = -1;
    public String c = null;
    public boolean d = false;
    public boolean e = false;
    public int f = 0;

    @Override // java.lang.Comparable
    public final /* bridge */ /* synthetic */ int compareTo(a aVar) {
        a aVar2 = aVar;
        if (aVar2 != null) {
            long j = this.b - aVar2.b;
            if (j <= 0) {
                return j < 0 ? -1 : 0;
            }
        }
        return 1;
    }
}
