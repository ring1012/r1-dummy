package cn.kuwo.autosdk;

import android.os.Handler;
import android.text.TextUtils;
import cn.kuwo.autosdk.api.OnSearchListener;
import cn.kuwo.autosdk.api.SearchMode;
import cn.kuwo.autosdk.api.SearchStatus;
import cn.kuwo.autosdk.bean.Constants;
import java.util.List;

/* loaded from: classes.dex */
public final class k implements Runnable {
    private String b;
    private int c;
    private SearchMode e;
    private Handler f;

    /* renamed from: a, reason: collision with root package name */
    public volatile boolean f14a = false;
    private OnSearchListener d = null;

    public k(String str, int i, SearchMode searchMode) {
        this.b = null;
        this.c = 0;
        this.e = SearchMode.ALL;
        this.e = searchMode;
        this.b = str;
        this.c = i;
    }

    private void a() {
        j jVar = new j(this.e);
        if (this.f14a) {
            return;
        }
        int i = 3;
        int[] iArr = new int[1];
        boolean z = false;
        List list = null;
        while (list == null) {
            int i2 = i - 1;
            if (i == 0) {
                break;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            List listA = jVar.a(this.b, this.c, iArr);
            boolean z2 = System.currentTimeMillis() - jCurrentTimeMillis > Constants.SEARCH_TIMEOUT;
            if (this.f14a) {
                return;
            }
            list = listA;
            z = z2;
            i = i2;
        }
        if (this.f14a) {
            return;
        }
        if (list != null) {
            if (this.d != null) {
                a(SearchStatus.SUCCESS, this.c == 0, list, false);
            }
        } else if (iArr[0] == 1) {
            if (this.d != null) {
                a(SearchStatus.FAILED, this.c == 0, null, true);
            }
        } else if (this.d != null) {
            a(SearchStatus.FAILED, this.c == 0, null, z);
        }
    }

    private void a(SearchStatus searchStatus, boolean z, List list, boolean z2) {
        if (this.f != null) {
            this.f.post(new l(this, searchStatus, z, list, z2));
        }
    }

    public void a(Handler handler, OnSearchListener onSearchListener) {
        this.f = handler;
        this.d = onSearchListener;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f14a || !TextUtils.isEmpty(this.b)) {
            if (this.f14a) {
                return;
            }
            a();
        } else if (this.d != null) {
            a(SearchStatus.FAILED, this.c == 0, null, false);
        }
    }
}
