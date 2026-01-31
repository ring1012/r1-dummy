package com.google.android.exoplayer2;

/* loaded from: classes.dex */
public interface RendererCapabilities {
    public static final int ADAPTIVE_NOT_SEAMLESS = 4;
    public static final int ADAPTIVE_NOT_SUPPORTED = 0;
    public static final int ADAPTIVE_SEAMLESS = 8;
    public static final int ADAPTIVE_SUPPORT_MASK = 12;
    public static final int FORMAT_EXCEEDS_CAPABILITIES = 2;
    public static final int FORMAT_HANDLED = 3;
    public static final int FORMAT_SUPPORT_MASK = 3;
    public static final int FORMAT_UNSUPPORTED_SUBTYPE = 1;
    public static final int FORMAT_UNSUPPORTED_TYPE = 0;
    public static final int TUNNELING_NOT_SUPPORTED = 0;
    public static final int TUNNELING_SUPPORTED = 16;
    public static final int TUNNELING_SUPPORT_MASK = 16;

    int getTrackType();

    int supportsFormat(Format format) throws ExoPlaybackException;

    int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException;
}
