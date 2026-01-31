package com.google.android.exoplayer2.ui;

import android.widget.TextView;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.unisound.ant.device.bean.MusicData;
import com.unisound.ant.device.devicelayer.DeviceStatus;

/* loaded from: classes.dex */
public final class DebugTextViewHelper implements Runnable, ExoPlayer.EventListener {
    private static final int REFRESH_INTERVAL_MS = 1000;
    private final SimpleExoPlayer player;
    private boolean started;
    private final TextView textView;

    public DebugTextViewHelper(SimpleExoPlayer player, TextView textView) {
        this.player = player;
        this.textView = textView;
    }

    public void start() {
        if (!this.started) {
            this.started = true;
            this.player.addListener(this);
            updateAndPost();
        }
    }

    public void stop() {
        if (this.started) {
            this.started = false;
            this.player.removeListener(this);
            this.textView.removeCallbacks(this);
        }
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        updateAndPost();
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onPositionDiscontinuity() {
        updateAndPost();
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onTracksChanged(TrackGroupArray tracks, TrackSelectionArray selections) {
    }

    @Override // java.lang.Runnable
    public void run() {
        updateAndPost();
    }

    private void updateAndPost() {
        this.textView.setText(getPlayerStateString() + getPlayerWindowIndexString() + getVideoString() + getAudioString());
        this.textView.removeCallbacks(this);
        this.textView.postDelayed(this, 1000L);
    }

    private String getPlayerStateString() {
        String text = "playWhenReady:" + this.player.getPlayWhenReady() + " playbackState:";
        switch (this.player.getPlaybackState()) {
            case 1:
                return text + DeviceStatus.DEVICE_STATUS_IDLE;
            case 2:
                return text + MusicData.PLAY_STATUS_BUFFERING;
            case 3:
                return text + "ready";
            case 4:
                return text + "ended";
            default:
                return text + "unknown";
        }
    }

    private String getPlayerWindowIndexString() {
        return " window:" + this.player.getCurrentWindowIndex();
    }

    private String getVideoString() {
        Format format = this.player.getVideoFormat();
        if (format == null) {
            return "";
        }
        return "\n" + format.sampleMimeType + "(id:" + format.id + " r:" + format.width + "x" + format.height + getDecoderCountersBufferCountString(this.player.getVideoDecoderCounters()) + ")";
    }

    private String getAudioString() {
        Format format = this.player.getAudioFormat();
        if (format == null) {
            return "";
        }
        return "\n" + format.sampleMimeType + "(id:" + format.id + " hz:" + format.sampleRate + " ch:" + format.channelCount + getDecoderCountersBufferCountString(this.player.getAudioDecoderCounters()) + ")";
    }

    private static String getDecoderCountersBufferCountString(DecoderCounters counters) {
        if (counters == null) {
            return "";
        }
        counters.ensureUpdated();
        return " rb:" + counters.renderedOutputBufferCount + " sb:" + counters.skippedOutputBufferCount + " db:" + counters.droppedOutputBufferCount + " mcdb:" + counters.maxConsecutiveDroppedOutputBufferCount;
    }
}
