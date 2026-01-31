package com.android.volley;

/* loaded from: classes.dex */
public class NetworkError extends VolleyError {
    public NetworkError() {
    }

    public NetworkError(Throwable cause) {
        super(cause);
    }

    public NetworkError(NetworkResponse networkResponse) {
        super(networkResponse);
    }
}
