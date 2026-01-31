package com.unisound.client;

/* loaded from: classes.dex */
public interface TextUnderstanderListener {
    void onError(int i, String str);

    void onEvent(int i);

    void onResult(int i, String str);
}
