package com.google.android.exoplayer2;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MediaClock;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class BaseRenderer implements Renderer, RendererCapabilities {
    private RendererConfiguration configuration;
    private int index;
    private boolean readEndOfStream = true;
    private int state;
    private SampleStream stream;
    private boolean streamIsFinal;
    private long streamOffsetUs;
    private final int trackType;

    public BaseRenderer(int trackType) {
        this.trackType = trackType;
    }

    @Override // com.google.android.exoplayer2.Renderer, com.google.android.exoplayer2.RendererCapabilities
    public final int getTrackType() {
        return this.trackType;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final RendererCapabilities getCapabilities() {
        return this;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final void setIndex(int index) {
        this.index = index;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public MediaClock getMediaClock() {
        return null;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final int getState() {
        return this.state;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final void enable(RendererConfiguration configuration, Format[] formats, SampleStream stream, long positionUs, boolean joining, long offsetUs) throws ExoPlaybackException {
        Assertions.checkState(this.state == 0);
        this.configuration = configuration;
        this.state = 1;
        onEnabled(joining);
        replaceStream(formats, stream, offsetUs);
        onPositionReset(positionUs, joining);
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final void start() throws ExoPlaybackException {
        Assertions.checkState(this.state == 1);
        this.state = 2;
        onStarted();
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final void replaceStream(Format[] formats, SampleStream stream, long offsetUs) throws ExoPlaybackException {
        Assertions.checkState(!this.streamIsFinal);
        this.stream = stream;
        this.readEndOfStream = false;
        this.streamOffsetUs = offsetUs;
        onStreamChanged(formats);
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final SampleStream getStream() {
        return this.stream;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final boolean hasReadStreamToEnd() {
        return this.readEndOfStream;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final void setCurrentStreamFinal() {
        this.streamIsFinal = true;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final boolean isCurrentStreamFinal() {
        return this.streamIsFinal;
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final void maybeThrowStreamError() throws IOException {
        this.stream.maybeThrowError();
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final void resetPosition(long positionUs) throws ExoPlaybackException {
        this.streamIsFinal = false;
        this.readEndOfStream = false;
        onPositionReset(positionUs, false);
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final void stop() throws ExoPlaybackException {
        Assertions.checkState(this.state == 2);
        this.state = 1;
        onStopped();
    }

    @Override // com.google.android.exoplayer2.Renderer
    public final void disable() {
        Assertions.checkState(this.state == 1);
        this.state = 0;
        onDisabled();
        this.stream = null;
        this.streamIsFinal = false;
    }

    @Override // com.google.android.exoplayer2.RendererCapabilities
    public int supportsMixedMimeTypeAdaptation() throws ExoPlaybackException {
        return 0;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.ExoPlayerComponent
    public void handleMessage(int what, Object object) throws ExoPlaybackException {
    }

    protected void onEnabled(boolean joining) throws ExoPlaybackException {
    }

    protected void onStreamChanged(Format[] formats) throws ExoPlaybackException {
    }

    protected void onPositionReset(long positionUs, boolean joining) throws ExoPlaybackException {
    }

    protected void onStarted() throws ExoPlaybackException {
    }

    protected void onStopped() throws ExoPlaybackException {
    }

    protected void onDisabled() {
    }

    protected final RendererConfiguration getConfiguration() {
        return this.configuration;
    }

    protected final int getIndex() {
        return this.index;
    }

    protected final int readSource(FormatHolder formatHolder, DecoderInputBuffer buffer, boolean formatRequired) {
        int result = this.stream.readData(formatHolder, buffer, formatRequired);
        if (result == -4) {
            if (buffer.isEndOfStream()) {
                this.readEndOfStream = true;
                return this.streamIsFinal ? -4 : -3;
            }
            buffer.timeUs += this.streamOffsetUs;
        } else if (result == -5) {
            Format format = formatHolder.format;
            if (format.subsampleOffsetUs != Long.MAX_VALUE) {
                formatHolder.format = format.copyWithSubsampleOffsetUs(format.subsampleOffsetUs + this.streamOffsetUs);
            }
        }
        return result;
    }

    protected void skipSource(long positionUs) {
        this.stream.skipData(positionUs - this.streamOffsetUs);
    }

    protected final boolean isSourceReady() {
        return this.readEndOfStream ? this.streamIsFinal : this.stream.isReady();
    }
}
