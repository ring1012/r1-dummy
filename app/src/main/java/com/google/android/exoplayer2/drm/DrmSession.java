package com.google.android.exoplayer2.drm;

import android.annotation.TargetApi;
import com.google.android.exoplayer2.drm.ExoMediaCrypto;
import java.util.Map;

@TargetApi(16)
/* loaded from: classes.dex */
public interface DrmSession<T extends ExoMediaCrypto> {
    public static final int STATE_CLOSED = 1;
    public static final int STATE_ERROR = 0;
    public static final int STATE_OPENED = 3;
    public static final int STATE_OPENED_WITH_KEYS = 4;
    public static final int STATE_OPENING = 2;

    DrmSessionException getError();

    T getMediaCrypto();

    byte[] getOfflineLicenseKeySetId();

    int getState();

    Map<String, String> queryKeyStatus();

    boolean requiresSecureDecoderComponent(String str);

    public static class DrmSessionException extends Exception {
        public DrmSessionException(Exception e) {
            super(e);
        }
    }
}
