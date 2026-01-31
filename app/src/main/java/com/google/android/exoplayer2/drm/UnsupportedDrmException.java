package com.google.android.exoplayer2.drm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public final class UnsupportedDrmException extends Exception {
    public static final int REASON_INSTANTIATION_ERROR = 2;
    public static final int REASON_UNSUPPORTED_SCHEME = 1;
    public final int reason;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Reason {
    }

    public UnsupportedDrmException(int reason) {
        this.reason = reason;
    }

    public UnsupportedDrmException(int reason, Exception cause) {
        super(cause);
        this.reason = reason;
    }
}
