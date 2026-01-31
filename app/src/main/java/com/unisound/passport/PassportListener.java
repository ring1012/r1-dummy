package com.unisound.passport;

/* loaded from: classes.dex */
public interface PassportListener {
    void onError(int i, String str);

    void onEvent(int i, long j);

    void onResult(int i, String str);
}
