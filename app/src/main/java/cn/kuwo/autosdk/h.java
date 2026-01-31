package cn.kuwo.autosdk;

import android.os.Handler;
import android.os.Looper;
import cn.kuwo.autosdk.api.OnSearchListener;
import cn.kuwo.autosdk.api.SearchMode;

/* loaded from: classes.dex */
public class h implements g {

    /* renamed from: a, reason: collision with root package name */
    private volatile k f12a = null;
    private String b = "";
    private int c = 0;
    private Handler d = new i(Looper.getMainLooper());

    @Override // cn.kuwo.autosdk.g
    public void a(String str, SearchMode searchMode, OnSearchListener onSearchListener) {
        this.b = str;
        this.c = 0;
        if (this.f12a != null) {
            this.f12a.f14a = true;
            this.f12a = null;
        }
        this.f12a = new k(this.b, this.c, searchMode);
        this.f12a.a(this.d, onSearchListener);
        r.a(s.NET, this.f12a);
    }
}
