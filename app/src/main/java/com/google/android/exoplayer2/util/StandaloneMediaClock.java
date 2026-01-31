package com.google.android.exoplayer2.util;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.PlaybackParameters;

/* loaded from: classes.dex */
public final class StandaloneMediaClock implements MediaClock {
    private long baseElapsedMs;
    private long baseUs;
    private PlaybackParameters playbackParameters = PlaybackParameters.DEFAULT;
    private boolean started;

    public void start() {
        if (!this.started) {
            this.baseElapsedMs = android.os.SystemClock.elapsedRealtime();
            this.started = true;
        }
    }

    public void stop() {
        if (this.started) {
            setPositionUs(getPositionUs());
            this.started = false;
        }
    }

    public void setPositionUs(long positionUs) {
        this.baseUs = positionUs;
        if (this.started) {
            this.baseElapsedMs = android.os.SystemClock.elapsedRealtime();
        }
    }

    public void synchronize(MediaClock clock) {
        setPositionUs(clock.getPositionUs());
        this.playbackParameters = clock.getPlaybackParameters();
    }

    @Override // com.google.android.exoplayer2.util.MediaClock
    public long getPositionUs() {
        long positionUs = this.baseUs;
        if (this.started) {
            long elapsedSinceBaseMs = android.os.SystemClock.elapsedRealtime() - this.baseElapsedMs;
            if (this.playbackParameters.speed == 1.0f) {
                return positionUs + C.msToUs(elapsedSinceBaseMs);
            }
            return positionUs + this.playbackParameters.getSpeedAdjustedDurationUs(elapsedSinceBaseMs);
        }
        return positionUs;
    }

    @Override // com.google.android.exoplayer2.util.MediaClock
    public PlaybackParameters setPlaybackParameters(PlaybackParameters playbackParameters) {
        if (this.started) {
            setPositionUs(getPositionUs());
        }
        this.playbackParameters = playbackParameters;
        return playbackParameters;
    }

    @Override // com.google.android.exoplayer2.util.MediaClock
    public PlaybackParameters getPlaybackParameters() {
        return this.playbackParameters;
    }
}
