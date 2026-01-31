package com.google.android.exoplayer2.source.smoothstreaming;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.SinglePeriodTimeline;
import com.google.android.exoplayer2.source.smoothstreaming.SsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class SsMediaSource implements MediaSource, Loader.Callback<ParsingLoadable<SsManifest>> {
    public static final long DEFAULT_LIVE_PRESENTATION_DELAY_MS = 30000;
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    private static final int MINIMUM_MANIFEST_REFRESH_PERIOD_MS = 5000;
    private static final long MIN_LIVE_DEFAULT_START_POSITION_US = 5000000;
    private final SsChunkSource.Factory chunkSourceFactory;
    private final AdaptiveMediaSourceEventListener.EventDispatcher eventDispatcher;
    private final long livePresentationDelayMs;
    private SsManifest manifest;
    private DataSource manifestDataSource;
    private final DataSource.Factory manifestDataSourceFactory;
    private long manifestLoadStartTimestamp;
    private Loader manifestLoader;
    private LoaderErrorThrower manifestLoaderErrorThrower;
    private final ParsingLoadable.Parser<? extends SsManifest> manifestParser;
    private Handler manifestRefreshHandler;
    private final Uri manifestUri;
    private final ArrayList<SsMediaPeriod> mediaPeriods;
    private final int minLoadableRetryCount;
    private MediaSource.Listener sourceListener;

    public SsMediaSource(SsManifest manifest, SsChunkSource.Factory chunkSourceFactory, Handler eventHandler, AdaptiveMediaSourceEventListener eventListener) {
        this(manifest, chunkSourceFactory, 3, eventHandler, eventListener);
    }

    public SsMediaSource(SsManifest manifest, SsChunkSource.Factory chunkSourceFactory, int minLoadableRetryCount, Handler eventHandler, AdaptiveMediaSourceEventListener eventListener) {
        this(manifest, null, null, null, chunkSourceFactory, minLoadableRetryCount, 30000L, eventHandler, eventListener);
    }

    public SsMediaSource(Uri manifestUri, DataSource.Factory manifestDataSourceFactory, SsChunkSource.Factory chunkSourceFactory, Handler eventHandler, AdaptiveMediaSourceEventListener eventListener) {
        this(manifestUri, manifestDataSourceFactory, chunkSourceFactory, 3, 30000L, eventHandler, eventListener);
    }

    public SsMediaSource(Uri manifestUri, DataSource.Factory manifestDataSourceFactory, SsChunkSource.Factory chunkSourceFactory, int minLoadableRetryCount, long livePresentationDelayMs, Handler eventHandler, AdaptiveMediaSourceEventListener eventListener) {
        this(manifestUri, manifestDataSourceFactory, new SsManifestParser(), chunkSourceFactory, minLoadableRetryCount, livePresentationDelayMs, eventHandler, eventListener);
    }

    public SsMediaSource(Uri manifestUri, DataSource.Factory manifestDataSourceFactory, ParsingLoadable.Parser<? extends SsManifest> manifestParser, SsChunkSource.Factory chunkSourceFactory, int minLoadableRetryCount, long livePresentationDelayMs, Handler eventHandler, AdaptiveMediaSourceEventListener eventListener) {
        this(null, manifestUri, manifestDataSourceFactory, manifestParser, chunkSourceFactory, minLoadableRetryCount, livePresentationDelayMs, eventHandler, eventListener);
    }

    private SsMediaSource(SsManifest manifest, Uri manifestUri, DataSource.Factory manifestDataSourceFactory, ParsingLoadable.Parser<? extends SsManifest> manifestParser, SsChunkSource.Factory chunkSourceFactory, int minLoadableRetryCount, long livePresentationDelayMs, Handler eventHandler, AdaptiveMediaSourceEventListener eventListener) {
        Assertions.checkState(manifest == null || !manifest.isLive);
        this.manifest = manifest;
        if (manifestUri == null) {
            manifestUri = null;
        } else if (!Util.toLowerInvariant(manifestUri.getLastPathSegment()).equals("manifest")) {
            manifestUri = Uri.withAppendedPath(manifestUri, "Manifest");
        }
        this.manifestUri = manifestUri;
        this.manifestDataSourceFactory = manifestDataSourceFactory;
        this.manifestParser = manifestParser;
        this.chunkSourceFactory = chunkSourceFactory;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.livePresentationDelayMs = livePresentationDelayMs;
        this.eventDispatcher = new AdaptiveMediaSourceEventListener.EventDispatcher(eventHandler, eventListener);
        this.mediaPeriods = new ArrayList<>();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void prepareSource(ExoPlayer player, boolean isTopLevelSource, MediaSource.Listener listener) {
        this.sourceListener = listener;
        if (this.manifest != null) {
            this.manifestLoaderErrorThrower = new LoaderErrorThrower.Dummy();
            processManifest();
            return;
        }
        this.manifestDataSource = this.manifestDataSourceFactory.createDataSource();
        this.manifestLoader = new Loader("Loader:Manifest");
        this.manifestLoaderErrorThrower = this.manifestLoader;
        this.manifestRefreshHandler = new Handler();
        startLoadingManifest();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.manifestLoaderErrorThrower.maybeThrowError();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public MediaPeriod createPeriod(int index, Allocator allocator, long positionUs) {
        Assertions.checkArgument(index == 0);
        SsMediaPeriod period = new SsMediaPeriod(this.manifest, this.chunkSourceFactory, this.minLoadableRetryCount, this.eventDispatcher, this.manifestLoaderErrorThrower, allocator);
        this.mediaPeriods.add(period);
        return period;
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releasePeriod(MediaPeriod period) {
        ((SsMediaPeriod) period).release();
        this.mediaPeriods.remove(period);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releaseSource() {
        this.sourceListener = null;
        this.manifest = null;
        this.manifestDataSource = null;
        this.manifestLoadStartTimestamp = 0L;
        if (this.manifestLoader != null) {
            this.manifestLoader.release();
            this.manifestLoader = null;
        }
        if (this.manifestRefreshHandler != null) {
            this.manifestRefreshHandler.removeCallbacksAndMessages(null);
            this.manifestRefreshHandler = null;
        }
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCompleted(ParsingLoadable<SsManifest> loadable, long elapsedRealtimeMs, long loadDurationMs) {
        this.eventDispatcher.loadCompleted(loadable.dataSpec, loadable.type, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
        this.manifest = loadable.getResult();
        this.manifestLoadStartTimestamp = elapsedRealtimeMs - loadDurationMs;
        processManifest();
        scheduleManifestRefresh();
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCanceled(ParsingLoadable<SsManifest> loadable, long elapsedRealtimeMs, long loadDurationMs, boolean released) {
        this.eventDispatcher.loadCompleted(loadable.dataSpec, loadable.type, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public int onLoadError(ParsingLoadable<SsManifest> loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
        boolean isFatal = error instanceof ParserException;
        this.eventDispatcher.loadError(loadable.dataSpec, loadable.type, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded(), error, isFatal);
        return isFatal ? 3 : 0;
    }

    private void processManifest() {
        Timeline timeline;
        for (int i = 0; i < this.mediaPeriods.size(); i++) {
            this.mediaPeriods.get(i).updateManifest(this.manifest);
        }
        if (this.manifest.isLive) {
            long startTimeUs = Long.MAX_VALUE;
            long endTimeUs = Long.MIN_VALUE;
            for (int i2 = 0; i2 < this.manifest.streamElements.length; i2++) {
                SsManifest.StreamElement element = this.manifest.streamElements[i2];
                if (element.chunkCount > 0) {
                    startTimeUs = Math.min(startTimeUs, element.getStartTimeUs(0));
                    endTimeUs = Math.max(endTimeUs, element.getStartTimeUs(element.chunkCount - 1) + element.getChunkDurationUs(element.chunkCount - 1));
                }
            }
            if (startTimeUs == Long.MAX_VALUE) {
                timeline = new SinglePeriodTimeline(C.TIME_UNSET, false);
            } else {
                if (this.manifest.dvrWindowLengthUs != C.TIME_UNSET && this.manifest.dvrWindowLengthUs > 0) {
                    startTimeUs = Math.max(startTimeUs, endTimeUs - this.manifest.dvrWindowLengthUs);
                }
                long durationUs = endTimeUs - startTimeUs;
                long defaultStartPositionUs = durationUs - C.msToUs(this.livePresentationDelayMs);
                if (defaultStartPositionUs < MIN_LIVE_DEFAULT_START_POSITION_US) {
                    defaultStartPositionUs = Math.min(MIN_LIVE_DEFAULT_START_POSITION_US, durationUs / 2);
                }
                timeline = new SinglePeriodTimeline(C.TIME_UNSET, durationUs, startTimeUs, defaultStartPositionUs, true, true);
            }
        } else {
            boolean isSeekable = this.manifest.durationUs != C.TIME_UNSET;
            timeline = new SinglePeriodTimeline(this.manifest.durationUs, isSeekable);
        }
        this.sourceListener.onSourceInfoRefreshed(timeline, this.manifest);
    }

    private void scheduleManifestRefresh() {
        if (this.manifest.isLive) {
            long nextLoadTimestamp = this.manifestLoadStartTimestamp + DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
            long delayUntilNextLoad = Math.max(0L, nextLoadTimestamp - SystemClock.elapsedRealtime());
            this.manifestRefreshHandler.postDelayed(new Runnable() { // from class: com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource.1
                @Override // java.lang.Runnable
                public void run() {
                    SsMediaSource.this.startLoadingManifest();
                }
            }, delayUntilNextLoad);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLoadingManifest() {
        ParsingLoadable<SsManifest> loadable = new ParsingLoadable<>(this.manifestDataSource, this.manifestUri, 4, this.manifestParser);
        long elapsedRealtimeMs = this.manifestLoader.startLoading(loadable, this, this.minLoadableRetryCount);
        this.eventDispatcher.loadStarted(loadable.dataSpec, loadable.type, elapsedRealtimeMs);
    }
}
