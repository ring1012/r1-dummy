package com.google.android.exoplayer2.audio;

/* loaded from: classes.dex */
public abstract class AudioDecoderException extends Exception {
    public AudioDecoderException(String detailMessage) {
        super(detailMessage);
    }

    public AudioDecoderException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }
}
