package com.unisound.b;

import android.os.Message;
import java.util.Map;

/* loaded from: classes.dex */
class d extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ b f219a;
    private int b;

    public d(b bVar, int i) {
        this.f219a = bVar;
        this.b = 0;
        this.b = i;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws Throwable {
        i.d("ActivatorInterface HttpThreadSn start jsonStr= " + b.z);
        this.f219a.y();
        this.f219a.n(com.unisound.a.a.a(this.f219a.A, this.f219a.f));
        i.c("ActivatorInterface$HttpThreadSn init over");
        j.b(this.f219a.A);
        if (j.a()) {
            try {
                String str = "";
                Map mapB = null;
                if (this.b == 0) {
                    str = this.f219a.f217a;
                    mapB = this.f219a.A();
                } else if (this.b == 1) {
                    str = this.f219a.b;
                    mapB = this.f219a.B();
                }
                i.d("ActivatorInterface", "url = " + str);
                l lVarA = f.a(str, (Map<String, String>) mapB, 5000);
                String strA = lVarA.a();
                if (lVarA.b() != 0) {
                    this.f219a.a(lVarA.b() - lVarA.d());
                }
                i.d("result = " + strA);
                if (strA.equals("") || strA.equals("{}")) {
                    this.f219a.B.sendEmptyMessage(106);
                    i.d("activate HttpResponse result is null");
                } else {
                    Message message = new Message();
                    message.obj = strA;
                    message.what = 102;
                    this.f219a.B.sendMessage(message);
                }
            } catch (Exception e) {
                this.f219a.D = c.FINISH;
                e.printStackTrace();
                this.f219a.B.sendEmptyMessage(107);
                i.a("activate Exception exception :" + e.getMessage());
                return;
            }
        } else {
            this.f219a.B.sendEmptyMessage(109);
            i.a("activate No Network");
        }
        this.f219a.D = c.FINISH;
    }
}
