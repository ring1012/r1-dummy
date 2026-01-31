package com.baidu.b.a;

import android.content.pm.PackageManager;
import java.util.Hashtable;

/* loaded from: classes.dex */
class l implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f47a;
    final /* synthetic */ boolean b;
    final /* synthetic */ String c;
    final /* synthetic */ String d;
    final /* synthetic */ Hashtable e;
    final /* synthetic */ b f;

    l(b bVar, int i, boolean z, String str, String str2, Hashtable hashtable) {
        this.f = bVar;
        this.f47a = i;
        this.b = z;
        this.c = str;
        this.d = str2;
        this.e = hashtable;
    }

    @Override // java.lang.Runnable
    public void run() throws PackageManager.NameNotFoundException {
        if (d.f40a) {
            d.a("status = " + this.f47a + "; forced = " + this.b + "checkAK = " + this.f.b(this.c));
        }
        if (this.f47a != 601 && !this.b && this.f47a != -1 && !this.f.b(this.c)) {
            if (602 != this.f47a) {
                if (d.f40a) {
                    d.a("authenticate else  ");
                }
                this.f.a((String) null, this.c);
                return;
            } else {
                if (d.f40a) {
                    d.a("authenticate wait  ");
                }
                b.d.b();
                this.f.a((String) null, this.c);
                return;
            }
        }
        if (d.f40a) {
            d.a("authenticate sendAuthRequest");
        }
        String[] strArrB = e.b(b.f39a);
        if (d.f40a) {
            d.a("authStrings.length:" + strArrB.length);
        }
        if (strArrB == null || strArrB.length <= 1) {
            this.f.a(this.b, this.d, (Hashtable<String, String>) this.e, this.c);
            return;
        }
        if (d.f40a) {
            d.a("more sha1 auth");
        }
        this.f.a(this.b, this.d, (Hashtable<String, String>) this.e, strArrB, this.c);
    }
}
