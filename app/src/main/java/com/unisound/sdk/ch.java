package com.unisound.sdk;

import android.os.AsyncTask;

/* loaded from: classes.dex */
class ch extends AsyncTask<Object, Object, Object> {

    /* renamed from: a, reason: collision with root package name */
    cg f331a;
    final /* synthetic */ cf b;

    public ch(cf cfVar, cg cgVar) {
        this.b = cfVar;
        this.f331a = cgVar;
    }

    public void a() {
        this.f331a = null;
    }

    @Override // android.os.AsyncTask
    protected Object doInBackground(Object... objArr) {
        com.unisound.common.y.b("USCAsyncTask doInBackground: ", objArr[0]);
        return this.f331a.a(objArr);
    }

    @Override // android.os.AsyncTask
    protected void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        this.b.d = false;
        this.f331a.a(obj);
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        this.b.d = true;
        super.onPreExecute();
    }
}
