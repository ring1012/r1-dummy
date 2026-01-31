package com.android.volley;

/* loaded from: classes.dex */
public interface RetryPolicy {
    int getCurrentRetryCount();

    int getCurrentTimeout();

    void retry(VolleyError volleyError) throws VolleyError;
}
