package com.unisound.vui;

import com.unisound.client.SpeechUnderstanderListener;

/* loaded from: classes.dex */
public interface b extends SpeechUnderstanderListener {
    @Override // com.unisound.client.SpeechUnderstanderListener
    void onError(int i, String str);

    @Override // com.unisound.client.SpeechUnderstanderListener
    void onEvent(int i, int i2);

    @Override // com.unisound.client.SpeechUnderstanderListener
    void onResult(int i, String str);
}
