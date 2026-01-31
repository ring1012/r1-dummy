package com.google.android.exoplayer2;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.util.MediaClock;
import java.io.IOException;

/* loaded from: classes.dex */
public interface Renderer extends ExoPlayer.ExoPlayerComponent {
    public static final int STATE_DISABLED = 0;
    public static final int STATE_ENABLED = 1;
    public static final int STATE_STARTED = 2;

    void disable();

    void enable(RendererConfiguration rendererConfiguration, Format[] formatArr, SampleStream sampleStream, long j, boolean z, long j2) throws ExoPlaybackException;

    RendererCapabilities getCapabilities();

    MediaClock getMediaClock();

    int getState();

    SampleStream getStream();

    int getTrackType();

    boolean hasReadStreamToEnd();

    boolean isCurrentStreamFinal();

    boolean isEnded();

    boolean isReady();

    void maybeThrowStreamError() throws IOException;

    void render(long j, long j2) throws ExoPlaybackException;

    void replaceStream(Format[] formatArr, SampleStream sampleStream, long j) throws ExoPlaybackException;

    void resetPosition(long j) throws ExoPlaybackException;

    void setCurrentStreamFinal();

    void setIndex(int i);

    void start() throws ExoPlaybackException;

    void stop() throws ExoPlaybackException;
}
