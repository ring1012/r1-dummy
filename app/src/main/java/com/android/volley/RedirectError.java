package com.android.volley;

/* loaded from: classes.dex */
public class RedirectError extends VolleyError {
    public RedirectError() {
    }

    public RedirectError(Throwable cause) {
        super(cause);
    }

    public RedirectError(NetworkResponse response) {
        super(response);
    }
}
