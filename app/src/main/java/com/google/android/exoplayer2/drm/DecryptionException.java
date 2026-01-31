package com.google.android.exoplayer2.drm;

/* loaded from: classes.dex */
public class DecryptionException extends Exception {
    private final int errorCode;

    public DecryptionException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}
