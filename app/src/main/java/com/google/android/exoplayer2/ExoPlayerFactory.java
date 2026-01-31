package com.google.android.exoplayer2;

import android.content.Context;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.trackselection.TrackSelector;

/* loaded from: classes.dex */
public final class ExoPlayerFactory {
    private ExoPlayerFactory() {
    }

    @Deprecated
    public static SimpleExoPlayer newSimpleInstance(Context context, TrackSelector trackSelector, LoadControl loadControl) {
        RenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        return newSimpleInstance(renderersFactory, trackSelector, loadControl);
    }

    @Deprecated
    public static SimpleExoPlayer newSimpleInstance(Context context, TrackSelector trackSelector, LoadControl loadControl, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        RenderersFactory renderersFactory = new DefaultRenderersFactory(context, drmSessionManager);
        return newSimpleInstance(renderersFactory, trackSelector, loadControl);
    }

    @Deprecated
    public static SimpleExoPlayer newSimpleInstance(Context context, TrackSelector trackSelector, LoadControl loadControl, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, int extensionRendererMode) {
        RenderersFactory renderersFactory = new DefaultRenderersFactory(context, drmSessionManager, extensionRendererMode);
        return newSimpleInstance(renderersFactory, trackSelector, loadControl);
    }

    @Deprecated
    public static SimpleExoPlayer newSimpleInstance(Context context, TrackSelector trackSelector, LoadControl loadControl, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, int extensionRendererMode, long allowedVideoJoiningTimeMs) {
        RenderersFactory renderersFactory = new DefaultRenderersFactory(context, drmSessionManager, extensionRendererMode, allowedVideoJoiningTimeMs);
        return newSimpleInstance(renderersFactory, trackSelector, loadControl);
    }

    public static SimpleExoPlayer newSimpleInstance(Context context, TrackSelector trackSelector) {
        return newSimpleInstance(new DefaultRenderersFactory(context), trackSelector);
    }

    public static SimpleExoPlayer newSimpleInstance(RenderersFactory renderersFactory, TrackSelector trackSelector) {
        return newSimpleInstance(renderersFactory, trackSelector, new DefaultLoadControl());
    }

    public static SimpleExoPlayer newSimpleInstance(RenderersFactory renderersFactory, TrackSelector trackSelector, LoadControl loadControl) {
        return new SimpleExoPlayer(renderersFactory, trackSelector, loadControl);
    }

    public static ExoPlayer newInstance(Renderer[] renderers, TrackSelector trackSelector) {
        return newInstance(renderers, trackSelector, new DefaultLoadControl());
    }

    public static ExoPlayer newInstance(Renderer[] renderers, TrackSelector trackSelector, LoadControl loadControl) {
        return new ExoPlayerImpl(renderers, trackSelector, loadControl);
    }
}
