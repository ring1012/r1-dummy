package com.unisound.vui;

import com.unisound.client.SpeechSynthesizerListener;

/* loaded from: classes.dex */
public interface d extends SpeechSynthesizerListener {
    @Override // com.unisound.client.SpeechSynthesizerListener
    void onError(int i, String str);

    @Override // com.unisound.client.SpeechSynthesizerListener
    void onEvent(int i);
}
