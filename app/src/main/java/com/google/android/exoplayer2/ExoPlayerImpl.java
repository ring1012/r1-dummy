package com.google.android.exoplayer2;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerImplInternal;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

/* loaded from: classes.dex */
final class ExoPlayerImpl implements ExoPlayer {
    private static final String TAG = "ExoPlayerImpl";
    private final TrackSelectionArray emptyTrackSelections;
    private final Handler eventHandler;
    private final ExoPlayerImplInternal internalPlayer;
    private boolean isLoading;
    private final CopyOnWriteArraySet<ExoPlayer.EventListener> listeners;
    private Object manifest;
    private int maskingPeriodIndex;
    private int maskingWindowIndex;
    private long maskingWindowPositionMs;
    private int pendingPrepareAcks;
    private int pendingSeekAcks;
    private final Timeline.Period period;
    private boolean playWhenReady;
    private ExoPlayerImplInternal.PlaybackInfo playbackInfo;
    private PlaybackParameters playbackParameters;
    private int playbackState;
    private final Renderer[] renderers;
    private Timeline timeline;
    private TrackGroupArray trackGroups;
    private TrackSelectionArray trackSelections;
    private final TrackSelector trackSelector;
    private boolean tracksSelected;
    private final Timeline.Window window;

    @SuppressLint({"HandlerLeak"})
    public ExoPlayerImpl(Renderer[] renderers, TrackSelector trackSelector, LoadControl loadControl) {
        Log.i(TAG, "Init ExoPlayerLib/2.4.0 [" + Util.DEVICE_DEBUG_INFO + "]");
        Assertions.checkState(renderers.length > 0);
        this.renderers = (Renderer[]) Assertions.checkNotNull(renderers);
        this.trackSelector = (TrackSelector) Assertions.checkNotNull(trackSelector);
        this.playWhenReady = false;
        this.playbackState = 1;
        this.listeners = new CopyOnWriteArraySet<>();
        this.emptyTrackSelections = new TrackSelectionArray(new TrackSelection[renderers.length]);
        this.timeline = Timeline.EMPTY;
        this.window = new Timeline.Window();
        this.period = new Timeline.Period();
        this.trackGroups = TrackGroupArray.EMPTY;
        this.trackSelections = this.emptyTrackSelections;
        this.playbackParameters = PlaybackParameters.DEFAULT;
        this.eventHandler = new Handler() { // from class: com.google.android.exoplayer2.ExoPlayerImpl.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                ExoPlayerImpl.this.handleEvent(msg);
            }
        };
        this.playbackInfo = new ExoPlayerImplInternal.PlaybackInfo(0, 0L);
        this.internalPlayer = new ExoPlayerImplInternal(renderers, trackSelector, loadControl, this.playWhenReady, this.eventHandler, this.playbackInfo, this);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void addListener(ExoPlayer.EventListener listener) {
        this.listeners.add(listener);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void removeListener(ExoPlayer.EventListener listener) {
        this.listeners.remove(listener);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public int getPlaybackState() {
        return this.playbackState;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void prepare(MediaSource mediaSource) {
        prepare(mediaSource, true, true);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void prepare(MediaSource mediaSource, boolean resetPosition, boolean resetState) {
        if (resetState) {
            if (!this.timeline.isEmpty() || this.manifest != null) {
                this.timeline = Timeline.EMPTY;
                this.manifest = null;
                Iterator<ExoPlayer.EventListener> it = this.listeners.iterator();
                while (it.hasNext()) {
                    ExoPlayer.EventListener listener = it.next();
                    listener.onTimelineChanged(this.timeline, this.manifest);
                }
            }
            if (this.tracksSelected) {
                this.tracksSelected = false;
                this.trackGroups = TrackGroupArray.EMPTY;
                this.trackSelections = this.emptyTrackSelections;
                this.trackSelector.onSelectionActivated(null);
                Iterator<ExoPlayer.EventListener> it2 = this.listeners.iterator();
                while (it2.hasNext()) {
                    ExoPlayer.EventListener listener2 = it2.next();
                    listener2.onTracksChanged(this.trackGroups, this.trackSelections);
                }
            }
        }
        this.pendingPrepareAcks++;
        this.internalPlayer.prepare(mediaSource, resetPosition);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void setPlayWhenReady(boolean playWhenReady) {
        if (this.playWhenReady != playWhenReady) {
            this.playWhenReady = playWhenReady;
            this.internalPlayer.setPlayWhenReady(playWhenReady);
            Iterator<ExoPlayer.EventListener> it = this.listeners.iterator();
            while (it.hasNext()) {
                ExoPlayer.EventListener listener = it.next();
                listener.onPlayerStateChanged(playWhenReady, this.playbackState);
            }
        }
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public boolean getPlayWhenReady() {
        return this.playWhenReady;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public boolean isLoading() {
        return this.isLoading;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void seekToDefaultPosition() {
        seekToDefaultPosition(getCurrentWindowIndex());
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void seekToDefaultPosition(int windowIndex) {
        seekTo(windowIndex, C.TIME_UNSET);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void seekTo(long positionMs) {
        seekTo(getCurrentWindowIndex(), positionMs);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void seekTo(int windowIndex, long positionMs) {
        if (windowIndex < 0 || (!this.timeline.isEmpty() && windowIndex >= this.timeline.getWindowCount())) {
            throw new IllegalSeekPositionException(this.timeline, windowIndex, positionMs);
        }
        this.pendingSeekAcks++;
        this.maskingWindowIndex = windowIndex;
        if (this.timeline.isEmpty()) {
            this.maskingPeriodIndex = 0;
        } else {
            this.timeline.getWindow(windowIndex, this.window);
            long resolvedPositionMs = positionMs == C.TIME_UNSET ? this.window.getDefaultPositionUs() : positionMs;
            int periodIndex = this.window.firstPeriodIndex;
            long periodPositionUs = this.window.getPositionInFirstPeriodUs() + C.msToUs(resolvedPositionMs);
            long periodDurationUs = this.timeline.getPeriod(periodIndex, this.period).getDurationUs();
            while (periodDurationUs != C.TIME_UNSET && periodPositionUs >= periodDurationUs && periodIndex < this.window.lastPeriodIndex) {
                periodPositionUs -= periodDurationUs;
                periodIndex++;
                periodDurationUs = this.timeline.getPeriod(periodIndex, this.period).getDurationUs();
            }
            this.maskingPeriodIndex = periodIndex;
        }
        if (positionMs == C.TIME_UNSET) {
            this.maskingWindowPositionMs = 0L;
            this.internalPlayer.seekTo(this.timeline, windowIndex, C.TIME_UNSET);
            return;
        }
        this.maskingWindowPositionMs = positionMs;
        this.internalPlayer.seekTo(this.timeline, windowIndex, C.msToUs(positionMs));
        Iterator<ExoPlayer.EventListener> it = this.listeners.iterator();
        while (it.hasNext()) {
            ExoPlayer.EventListener listener = it.next();
            listener.onPositionDiscontinuity();
        }
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void setPlaybackParameters(@Nullable PlaybackParameters playbackParameters) {
        if (playbackParameters == null) {
            playbackParameters = PlaybackParameters.DEFAULT;
        }
        this.internalPlayer.setPlaybackParameters(playbackParameters);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public PlaybackParameters getPlaybackParameters() {
        return this.playbackParameters;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void stop() {
        this.internalPlayer.stop();
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void release() {
        this.internalPlayer.release();
        this.eventHandler.removeCallbacksAndMessages(null);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void sendMessages(ExoPlayer.ExoPlayerMessage... messages) {
        this.internalPlayer.sendMessages(messages);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public void blockingSendMessages(ExoPlayer.ExoPlayerMessage... messages) {
        this.internalPlayer.blockingSendMessages(messages);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public int getCurrentPeriodIndex() {
        return (this.timeline.isEmpty() || this.pendingSeekAcks > 0) ? this.maskingPeriodIndex : this.playbackInfo.periodIndex;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public int getCurrentWindowIndex() {
        return (this.timeline.isEmpty() || this.pendingSeekAcks > 0) ? this.maskingWindowIndex : this.timeline.getPeriod(this.playbackInfo.periodIndex, this.period).windowIndex;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public long getDuration() {
        return this.timeline.isEmpty() ? C.TIME_UNSET : this.timeline.getWindow(getCurrentWindowIndex(), this.window).getDurationMs();
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public long getCurrentPosition() {
        if (this.timeline.isEmpty() || this.pendingSeekAcks > 0) {
            return this.maskingWindowPositionMs;
        }
        this.timeline.getPeriod(this.playbackInfo.periodIndex, this.period);
        return this.period.getPositionInWindowMs() + C.usToMs(this.playbackInfo.positionUs);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public long getBufferedPosition() {
        if (this.timeline.isEmpty() || this.pendingSeekAcks > 0) {
            return this.maskingWindowPositionMs;
        }
        this.timeline.getPeriod(this.playbackInfo.periodIndex, this.period);
        return this.period.getPositionInWindowMs() + C.usToMs(this.playbackInfo.bufferedPositionUs);
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public int getBufferedPercentage() {
        int i;
        if (this.timeline.isEmpty()) {
            return 0;
        }
        long bufferedPosition = getBufferedPosition();
        long duration = getDuration();
        if (bufferedPosition == C.TIME_UNSET || duration == C.TIME_UNSET) {
            i = 0;
        } else {
            i = (int) (duration != 0 ? (100 * bufferedPosition) / duration : 100L);
        }
        return i;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public boolean isCurrentWindowDynamic() {
        return !this.timeline.isEmpty() && this.timeline.getWindow(getCurrentWindowIndex(), this.window).isDynamic;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public boolean isCurrentWindowSeekable() {
        return !this.timeline.isEmpty() && this.timeline.getWindow(getCurrentWindowIndex(), this.window).isSeekable;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public int getRendererCount() {
        return this.renderers.length;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public int getRendererType(int index) {
        return this.renderers[index].getTrackType();
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public TrackGroupArray getCurrentTrackGroups() {
        return this.trackGroups;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public TrackSelectionArray getCurrentTrackSelections() {
        return this.trackSelections;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public Timeline getCurrentTimeline() {
        return this.timeline;
    }

    @Override // com.google.android.exoplayer2.ExoPlayer
    public Object getCurrentManifest() {
        return this.manifest;
    }

    void handleEvent(Message msg) {
        switch (msg.what) {
            case 0:
                this.pendingPrepareAcks--;
                return;
            case 1:
                this.playbackState = msg.arg1;
                Iterator<ExoPlayer.EventListener> it = this.listeners.iterator();
                while (it.hasNext()) {
                    ExoPlayer.EventListener listener = it.next();
                    listener.onPlayerStateChanged(this.playWhenReady, this.playbackState);
                }
                return;
            case 2:
                this.isLoading = msg.arg1 != 0;
                Iterator<ExoPlayer.EventListener> it2 = this.listeners.iterator();
                while (it2.hasNext()) {
                    ExoPlayer.EventListener listener2 = it2.next();
                    listener2.onLoadingChanged(this.isLoading);
                }
                return;
            case 3:
                if (this.pendingPrepareAcks == 0) {
                    TrackSelectorResult trackSelectorResult = (TrackSelectorResult) msg.obj;
                    this.tracksSelected = true;
                    this.trackGroups = trackSelectorResult.groups;
                    this.trackSelections = trackSelectorResult.selections;
                    this.trackSelector.onSelectionActivated(trackSelectorResult.info);
                    Iterator<ExoPlayer.EventListener> it3 = this.listeners.iterator();
                    while (it3.hasNext()) {
                        ExoPlayer.EventListener listener3 = it3.next();
                        listener3.onTracksChanged(this.trackGroups, this.trackSelections);
                    }
                    return;
                }
                return;
            case 4:
                int i = this.pendingSeekAcks - 1;
                this.pendingSeekAcks = i;
                if (i == 0) {
                    this.playbackInfo = (ExoPlayerImplInternal.PlaybackInfo) msg.obj;
                    if (msg.arg1 != 0) {
                        Iterator<ExoPlayer.EventListener> it4 = this.listeners.iterator();
                        while (it4.hasNext()) {
                            ExoPlayer.EventListener listener4 = it4.next();
                            listener4.onPositionDiscontinuity();
                        }
                        return;
                    }
                    return;
                }
                return;
            case 5:
                if (this.pendingSeekAcks == 0) {
                    this.playbackInfo = (ExoPlayerImplInternal.PlaybackInfo) msg.obj;
                    Iterator<ExoPlayer.EventListener> it5 = this.listeners.iterator();
                    while (it5.hasNext()) {
                        ExoPlayer.EventListener listener5 = it5.next();
                        listener5.onPositionDiscontinuity();
                    }
                    return;
                }
                return;
            case 6:
                ExoPlayerImplInternal.SourceInfo sourceInfo = (ExoPlayerImplInternal.SourceInfo) msg.obj;
                this.pendingSeekAcks -= sourceInfo.seekAcks;
                if (this.pendingPrepareAcks == 0) {
                    this.timeline = sourceInfo.timeline;
                    this.manifest = sourceInfo.manifest;
                    this.playbackInfo = sourceInfo.playbackInfo;
                    Iterator<ExoPlayer.EventListener> it6 = this.listeners.iterator();
                    while (it6.hasNext()) {
                        ExoPlayer.EventListener listener6 = it6.next();
                        listener6.onTimelineChanged(this.timeline, this.manifest);
                    }
                    return;
                }
                return;
            case 7:
                PlaybackParameters playbackParameters = (PlaybackParameters) msg.obj;
                if (!this.playbackParameters.equals(playbackParameters)) {
                    this.playbackParameters = playbackParameters;
                    Iterator<ExoPlayer.EventListener> it7 = this.listeners.iterator();
                    while (it7.hasNext()) {
                        ExoPlayer.EventListener listener7 = it7.next();
                        listener7.onPlaybackParametersChanged(playbackParameters);
                    }
                    return;
                }
                return;
            case 8:
                ExoPlaybackException exception = (ExoPlaybackException) msg.obj;
                Iterator<ExoPlayer.EventListener> it8 = this.listeners.iterator();
                while (it8.hasNext()) {
                    ExoPlayer.EventListener listener8 = it8.next();
                    listener8.onPlayerError(exception);
                }
                return;
            default:
                throw new IllegalStateException();
        }
    }
}
