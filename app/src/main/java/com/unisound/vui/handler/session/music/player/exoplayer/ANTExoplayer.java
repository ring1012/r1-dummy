package com.unisound.vui.handler.session.music.player.exoplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.unisound.vui.common.media.EventLogger;
import com.unisound.vui.common.media.OkHttpDataSourceFactory;
import com.unisound.vui.handler.session.music.player.MusicPlayer;
import com.unisound.vui.util.LogMgr;
import okhttp3.OkHttpClient;

/* loaded from: classes.dex */
public class ANTExoplayer implements MusicPlayer, ExoPlayer.EventListener {
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final String TAG = "ANTExoplayer";
    private MusicPlayer.Callback callback;
    private Context context;
    private EventLogger eventLogger;
    private boolean isPrepared;
    private final Handler mainHandler;
    private DataSource.Factory mediaDataSourceFactory;
    private int playState;
    private boolean playWhenReady;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private String userAgent;

    private ANTExoplayer(Context context) {
        this.context = context;
        this.mediaDataSourceFactory = buildDataSourceFactory(true);
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(context);
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
        this.trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        this.eventLogger = new EventLogger(this.trackSelector);
        this.player = ExoPlayerFactory.newSimpleInstance(renderersFactory, this.trackSelector);
        this.player.addListener(this);
        this.mainHandler = new Handler();
    }

    private void prepare(String url, boolean playWhenReady) {
        LogMgr.d(TAG, "prepare url : " + url + ";playWhenReady : " + playWhenReady);
        this.player.setPlayWhenReady(playWhenReady);
        MediaSource mediaSource = buildMediaSource(Uri.parse(url), null);
        this.player.seekTo(0L);
        this.player.prepare(mediaSource);
        this.isPrepared = false;
        this.playWhenReady = playWhenReady;
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public boolean isPrepared() {
        return this.isPrepared;
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public void play(String url, boolean playWhenReady) {
        LogMgr.d(TAG, "play url : " + url + ";playState : " + this.playState + ";playWhenReady : " + playWhenReady);
        if (this.playState == 2 || this.playState == 5) {
            this.player.setPlayWhenReady(true);
            this.playState = 3;
            onStateChanged();
            return;
        }
        prepare(url, playWhenReady);
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public void pause() {
        LogMgr.d(TAG, "pause");
        this.player.setPlayWhenReady(false);
        this.playState = 2;
        onStateChanged();
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public void stop() {
        LogMgr.d(TAG, "stop");
        this.player.stop();
        this.playState = 1;
        onStateChanged();
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public void release() {
        LogMgr.d(TAG, "release");
        this.player.release();
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public void seekTo(long position) {
        LogMgr.d(TAG, "seekTo");
        this.player.seekTo(position);
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public long getDuration() {
        LogMgr.d(TAG, "seekTo");
        return this.player.getDuration();
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public boolean getPlayWhenReady() {
        return this.playWhenReady;
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public long getCurrentPosition() {
        LogMgr.d(TAG, "getCurrentPosition");
        return this.player.getCurrentPosition();
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public int getBufferPercent() {
        return this.player.getBufferedPercentage();
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public boolean isPlaying() {
        boolean isPlaying = this.playState == 3;
        LogMgr.d(TAG, "isPlaying:" + isPlaying);
        return isPlaying;
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public void registerCallback(MusicPlayer.Callback callback) {
        this.callback = callback;
    }

    @Override // com.unisound.vui.handler.session.music.player.MusicPlayer
    public void setRenderer(MusicPlayer.Renderer renderer) {
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        LogMgr.d(TAG, "onTimelineChanged:");
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        LogMgr.d(TAG, "onTracksChanged:");
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onPlayerStateChanged(boolean playWhenReady, int state) {
        LogMgr.d(TAG, "onPlayerStateChanged playWhenReady:" + playWhenReady + ";state:" + state);
        switch (state) {
            case 2:
                this.playState = 6;
                break;
            case 3:
                if (this.playState == 6) {
                    this.isPrepared = true;
                    this.playState = 5;
                    if (playWhenReady) {
                        onStateChanged();
                        this.playState = 3;
                        break;
                    }
                } else {
                    return;
                }
                break;
            case 4:
                this.playState = 4;
                break;
        }
        onStateChanged();
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onPlayerError(ExoPlaybackException error) {
        LogMgr.d(TAG, "onPositionDiscontinuity:");
        if (this.callback != null) {
            this.callback.onPlayerError(error.getMessage());
        }
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onPositionDiscontinuity() {
        LogMgr.d(TAG, "onPositionDiscontinuity:");
    }

    @Override // com.google.android.exoplayer2.ExoPlayer.EventListener
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        LogMgr.d(TAG, "onPlaybackParametersChanged:");
    }

    private void onStateChanged() {
        if (this.callback != null) {
            this.callback.onPlayStateChanged(this.playState);
        }
    }

    public Handler getMainHandler() {
        return this.mainHandler;
    }

    public static final class ExoplayerFactory implements MusicPlayer.PlayerFactory<ANTExoplayer> {
        private Context context;

        public ExoplayerFactory(Context context) {
            this.context = context;
        }

        @Override // com.unisound.vui.handler.session.music.player.MusicPlayer.PlayerFactory
        public ANTExoplayer newInstance() {
            return new ANTExoplayer(this.context);
        }
    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri) : Util.inferContentType("." + overrideExtension);
        switch (type) {
            case 0:
                return new DashMediaSource(uri, buildDataSourceFactory(false), new DefaultDashChunkSource.Factory(this.mediaDataSourceFactory), this.mainHandler, this.eventLogger);
            case 1:
                return new SsMediaSource(uri, buildDataSourceFactory(false), new DefaultSsChunkSource.Factory(this.mediaDataSourceFactory), this.mainHandler, this.eventLogger);
            case 2:
                return new HlsMediaSource(uri, this.mediaDataSourceFactory, this.mainHandler, this.eventLogger);
            case 3:
                return new ExtractorMediaSource(uri, this.mediaDataSourceFactory, new DefaultExtractorsFactory(), this.mainHandler, this.eventLogger);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this.context, bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        this.userAgent = Util.getUserAgent(this.context, TAG);
        return new OkHttpDataSourceFactory(new OkHttpClient(), this.userAgent, bandwidthMeter);
    }
}
