package cn.kuwo.autosdk;

import cn.kuwo.autosdk.api.SearchStatus;
import java.util.List;

/* loaded from: classes.dex */
class l implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f15a;
    private final /* synthetic */ SearchStatus b;
    private final /* synthetic */ boolean c;
    private final /* synthetic */ List d;
    private final /* synthetic */ boolean e;

    l(k kVar, SearchStatus searchStatus, boolean z, List list, boolean z2) {
        this.f15a = kVar;
        this.b = searchStatus;
        this.c = z;
        this.d = list;
        this.e = z2;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f15a.d.searchFinshed(this.b, this.c, this.d, this.e);
    }
}
