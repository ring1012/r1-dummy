package com.unisound.vui;

import com.unisound.client.TextUnderstanderListener;

/* loaded from: classes.dex */
public interface f extends TextUnderstanderListener {
    @Override // com.unisound.client.TextUnderstanderListener
    void onError(int i, String str);

    @Override // com.unisound.client.TextUnderstanderListener
    void onEvent(int i);

    @Override // com.unisound.client.TextUnderstanderListener
    void onResult(int i, String str);
}
