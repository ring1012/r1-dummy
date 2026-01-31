package com.unisound.vui.common.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class a extends Request<byte[]> {

    /* renamed from: a, reason: collision with root package name */
    public Map<String, String> f368a;
    private final Response.Listener<byte[]> b;
    private Map<String, String> c;

    public a(int i, String str, Response.Listener<byte[]> listener, Response.ErrorListener errorListener, HashMap<String, String> map) {
        super(i, str, errorListener);
        setShouldCache(false);
        this.b = listener;
        this.c = map;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.volley.Request
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public void deliverResponse(byte[] bArr) {
        this.b.onResponse(bArr);
    }

    @Override // com.android.volley.Request
    protected Map<String, String> getParams() {
        return this.c;
    }

    @Override // com.android.volley.Request
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        this.f368a = response.headers;
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }
}
