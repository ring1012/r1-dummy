package com.google.android.exoplayer2;

import android.support.annotation.Nullable;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;

/* loaded from: classes.dex */
public interface ExoPlayer {
    public static final int STATE_BUFFERING = 2;
    public static final int STATE_ENDED = 4;
    public static final int STATE_IDLE = 1;
    public static final int STATE_READY = 3;

    public interface EventListener {
        void onLoadingChanged(boolean z);

        void onPlaybackParametersChanged(PlaybackParameters playbackParameters);

        void onPlayerError(ExoPlaybackException exoPlaybackException);

        void onPlayerStateChanged(boolean z, int i);

        void onPositionDiscontinuity();

        void onTimelineChanged(Timeline timeline, Object obj);

        void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray);
    }

    public interface ExoPlayerComponent {
        void handleMessage(int i, Object obj) throws ExoPlaybackException;
    }

    void addListener(EventListener eventListener);

    void blockingSendMessages(ExoPlayerMessage... exoPlayerMessageArr);

    int getBufferedPercentage();

    long getBufferedPosition();

    Object getCurrentManifest();

    int getCurrentPeriodIndex();

    long getCurrentPosition();

    Timeline getCurrentTimeline();

    TrackGroupArray getCurrentTrackGroups();

    TrackSelectionArray getCurrentTrackSelections();

    int getCurrentWindowIndex();

    long getDuration();

    boolean getPlayWhenReady();

    PlaybackParameters getPlaybackParameters();

    int getPlaybackState();

    int getRendererCount();

    int getRendererType(int i);

    boolean isCurrentWindowDynamic();

    boolean isCurrentWindowSeekable();

    boolean isLoading();

    void prepare(MediaSource mediaSource);

    void prepare(MediaSource mediaSource, boolean z, boolean z2);

    void release();

    void removeListener(EventListener eventListener);

    void seekTo(int i, long j);

    void seekTo(long j);

    void seekToDefaultPosition();

    void seekToDefaultPosition(int i);

    void sendMessages(ExoPlayerMessage... exoPlayerMessageArr);

    void setPlayWhenReady(boolean z);

    void setPlaybackParameters(@Nullable PlaybackParameters playbackParameters);

    void stop();

    public static final class ExoPlayerMessage {
        public final Object message;
        public final int messageType;
        public final ExoPlayerComponent target;

        public ExoPlayerMessage(ExoPlayerComponent target, int messageType, Object message) {
            this.target = target;
            this.messageType = messageType;
            this.message = message;
        }
    }
}
