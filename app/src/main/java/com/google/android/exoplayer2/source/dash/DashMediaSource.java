package com.google.android.exoplayer2.source.dash;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser;
import com.google.android.exoplayer2.source.dash.manifest.Period;
import com.google.android.exoplayer2.source.dash.manifest.UtcTimingElement;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.LoaderErrorThrower;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/* loaded from: classes.dex */
public final class DashMediaSource implements MediaSource {
    public static final long DEFAULT_LIVE_PRESENTATION_DELAY_FIXED_MS = 30000;
    public static final long DEFAULT_LIVE_PRESENTATION_DELAY_PREFER_MANIFEST_MS = -1;
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    private static final long MIN_LIVE_DEFAULT_START_POSITION_US = 5000000;
    private static final int NOTIFY_MANIFEST_INTERVAL_MS = 5000;
    private static final String TAG = "DashMediaSource";
    private final DashChunkSource.Factory chunkSourceFactory;
    private DataSource dataSource;
    private long elapsedRealtimeOffsetMs;
    private final AdaptiveMediaSourceEventListener.EventDispatcher eventDispatcher;
    private int firstPeriodId;
    private Handler handler;
    private final long livePresentationDelayMs;
    private Loader loader;
    private LoaderErrorThrower loaderErrorThrower;
    private DashManifest manifest;
    private final ManifestCallback manifestCallback;
    private final DataSource.Factory manifestDataSourceFactory;
    private long manifestLoadEndTimestamp;
    private long manifestLoadStartTimestamp;
    private final ParsingLoadable.Parser<? extends DashManifest> manifestParser;
    private Uri manifestUri;
    private final Object manifestUriLock;
    private final int minLoadableRetryCount;
    private final SparseArray<DashMediaPeriod> periodsById;
    private final Runnable refreshManifestRunnable;
    private final boolean sideloadedManifest;
    private final Runnable simulateManifestRefreshRunnable;
    private MediaSource.Listener sourceListener;

    public DashMediaSource(DashManifest manifest, DashChunkSource.Factory chunkSourceFactory, Handler eventHandler, AdaptiveMediaSourceEventListener eventListener) {
        this(manifest, chunkSourceFactory, 3, eventHandler, eventListener);
    }

    public DashMediaSource(DashManifest manifest, DashChunkSource.Factory chunkSourceFactory, int minLoadableRetryCount, Handler eventHandler, AdaptiveMediaSourceEventListener eventListener) {
        this(manifest, null, null, null, chunkSourceFactory, minLoadableRetryCount, -1L, eventHandler, eventListener);
    }

    public DashMediaSource(Uri manifestUri, DataSource.Factory manifestDataSourceFactory, DashChunkSource.Factory chunkSourceFactory, Handler eventHandler, AdaptiveMediaSourceEventListener eventListener) {
        this(manifestUri, manifestDataSourceFactory, chunkSourceFactory, 3, -1L, eventHandler, eventListener);
    }

    public DashMediaSource(Uri manifestUri, DataSource.Factory manifestDataSourceFactory, DashChunkSource.Factory chunkSourceFactory, int minLoadableRetryCount, long livePresentationDelayMs, Handler eventHandler, AdaptiveMediaSourceEventListener eventListener) {
        this(manifestUri, manifestDataSourceFactory, new DashManifestParser(), chunkSourceFactory, minLoadableRetryCount, livePresentationDelayMs, eventHandler, eventListener);
    }

    public DashMediaSource(Uri manifestUri, DataSource.Factory manifestDataSourceFactory, ParsingLoadable.Parser<? extends DashManifest> manifestParser, DashChunkSource.Factory chunkSourceFactory, int minLoadableRetryCount, long livePresentationDelayMs, Handler eventHandler, AdaptiveMediaSourceEventListener eventListener) {
        this(null, manifestUri, manifestDataSourceFactory, manifestParser, chunkSourceFactory, minLoadableRetryCount, livePresentationDelayMs, eventHandler, eventListener);
    }

    private DashMediaSource(DashManifest manifest, Uri manifestUri, DataSource.Factory manifestDataSourceFactory, ParsingLoadable.Parser<? extends DashManifest> manifestParser, DashChunkSource.Factory chunkSourceFactory, int minLoadableRetryCount, long livePresentationDelayMs, Handler eventHandler, AdaptiveMediaSourceEventListener eventListener) {
        this.manifest = manifest;
        this.manifestUri = manifestUri;
        this.manifestDataSourceFactory = manifestDataSourceFactory;
        this.manifestParser = manifestParser;
        this.chunkSourceFactory = chunkSourceFactory;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.livePresentationDelayMs = livePresentationDelayMs;
        this.sideloadedManifest = manifest != null;
        this.eventDispatcher = new AdaptiveMediaSourceEventListener.EventDispatcher(eventHandler, eventListener);
        this.manifestUriLock = new Object();
        this.periodsById = new SparseArray<>();
        if (this.sideloadedManifest) {
            Assertions.checkState(manifest.dynamic ? false : true);
            this.manifestCallback = null;
            this.refreshManifestRunnable = null;
            this.simulateManifestRefreshRunnable = null;
            return;
        }
        this.manifestCallback = new ManifestCallback();
        this.refreshManifestRunnable = new Runnable() { // from class: com.google.android.exoplayer2.source.dash.DashMediaSource.1
            @Override // java.lang.Runnable
            public void run() {
                DashMediaSource.this.startLoadingManifest();
            }
        };
        this.simulateManifestRefreshRunnable = new Runnable() { // from class: com.google.android.exoplayer2.source.dash.DashMediaSource.2
            @Override // java.lang.Runnable
            public void run() {
                DashMediaSource.this.processManifest(false);
            }
        };
    }

    public void replaceManifestUri(Uri manifestUri) {
        synchronized (this.manifestUriLock) {
            this.manifestUri = manifestUri;
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void prepareSource(ExoPlayer player, boolean isTopLevelSource, MediaSource.Listener listener) {
        this.sourceListener = listener;
        if (this.sideloadedManifest) {
            this.loaderErrorThrower = new LoaderErrorThrower.Dummy();
            processManifest(false);
            return;
        }
        this.dataSource = this.manifestDataSourceFactory.createDataSource();
        this.loader = new Loader("Loader:DashMediaSource");
        this.loaderErrorThrower = this.loader;
        this.handler = new Handler();
        startLoadingManifest();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.loaderErrorThrower.maybeThrowError();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public MediaPeriod createPeriod(int periodIndex, Allocator allocator, long positionUs) {
        AdaptiveMediaSourceEventListener.EventDispatcher periodEventDispatcher = this.eventDispatcher.copyWithMediaTimeOffsetMs(this.manifest.getPeriod(periodIndex).startMs);
        DashMediaPeriod mediaPeriod = new DashMediaPeriod(this.firstPeriodId + periodIndex, this.manifest, periodIndex, this.chunkSourceFactory, this.minLoadableRetryCount, periodEventDispatcher, this.elapsedRealtimeOffsetMs, this.loaderErrorThrower, allocator);
        this.periodsById.put(mediaPeriod.id, mediaPeriod);
        return mediaPeriod;
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releasePeriod(MediaPeriod mediaPeriod) {
        DashMediaPeriod dashMediaPeriod = (DashMediaPeriod) mediaPeriod;
        dashMediaPeriod.release();
        this.periodsById.remove(dashMediaPeriod.id);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releaseSource() {
        this.dataSource = null;
        this.loaderErrorThrower = null;
        if (this.loader != null) {
            this.loader.release();
            this.loader = null;
        }
        this.manifestLoadStartTimestamp = 0L;
        this.manifestLoadEndTimestamp = 0L;
        this.manifest = null;
        if (this.handler != null) {
            this.handler.removeCallbacksAndMessages(null);
            this.handler = null;
        }
        this.elapsedRealtimeOffsetMs = 0L;
        this.periodsById.clear();
    }

    void onManifestLoadCompleted(ParsingLoadable<DashManifest> loadable, long elapsedRealtimeMs, long loadDurationMs) {
        this.eventDispatcher.loadCompleted(loadable.dataSpec, loadable.type, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
        DashManifest newManifest = loadable.getResult();
        int periodCount = this.manifest == null ? 0 : this.manifest.getPeriodCount();
        int removedPeriodCount = 0;
        long newFirstPeriodStartTimeMs = newManifest.getPeriod(0).startMs;
        while (removedPeriodCount < periodCount && this.manifest.getPeriod(removedPeriodCount).startMs < newFirstPeriodStartTimeMs) {
            removedPeriodCount++;
        }
        if (periodCount - removedPeriodCount > newManifest.getPeriodCount()) {
            Log.w(TAG, "Out of sync manifest");
            scheduleManifestRefresh();
            return;
        }
        this.manifest = newManifest;
        this.manifestLoadStartTimestamp = elapsedRealtimeMs - loadDurationMs;
        this.manifestLoadEndTimestamp = elapsedRealtimeMs;
        if (this.manifest.location != null) {
            synchronized (this.manifestUriLock) {
                if (loadable.dataSpec.uri == this.manifestUri) {
                    this.manifestUri = this.manifest.location;
                }
            }
        }
        if (periodCount == 0) {
            if (this.manifest.utcTiming != null) {
                resolveUtcTimingElement(this.manifest.utcTiming);
                return;
            } else {
                processManifest(true);
                return;
            }
        }
        this.firstPeriodId += removedPeriodCount;
        processManifest(true);
    }

    int onManifestLoadError(ParsingLoadable<DashManifest> loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
        boolean isFatal = error instanceof ParserException;
        this.eventDispatcher.loadError(loadable.dataSpec, loadable.type, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded(), error, isFatal);
        return isFatal ? 3 : 0;
    }

    void onUtcTimestampLoadCompleted(ParsingLoadable<Long> loadable, long elapsedRealtimeMs, long loadDurationMs) {
        this.eventDispatcher.loadCompleted(loadable.dataSpec, loadable.type, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
        onUtcTimestampResolved(loadable.getResult().longValue() - elapsedRealtimeMs);
    }

    int onUtcTimestampLoadError(ParsingLoadable<Long> loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
        this.eventDispatcher.loadError(loadable.dataSpec, loadable.type, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded(), error, true);
        onUtcTimestampResolutionError(error);
        return 2;
    }

    void onLoadCanceled(ParsingLoadable<?> loadable, long elapsedRealtimeMs, long loadDurationMs) {
        this.eventDispatcher.loadCanceled(loadable.dataSpec, loadable.type, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLoadingManifest() {
        Uri manifestUri;
        synchronized (this.manifestUriLock) {
            manifestUri = this.manifestUri;
        }
        startLoading(new ParsingLoadable(this.dataSource, manifestUri, 4, this.manifestParser), this.manifestCallback, this.minLoadableRetryCount);
    }

    private void resolveUtcTimingElement(UtcTimingElement timingElement) {
        String scheme = timingElement.schemeIdUri;
        if (Util.areEqual(scheme, "urn:mpeg:dash:utc:direct:2012")) {
            resolveUtcTimingElementDirect(timingElement);
            return;
        }
        if (Util.areEqual(scheme, "urn:mpeg:dash:utc:http-iso:2014")) {
            resolveUtcTimingElementHttp(timingElement, new Iso8601Parser());
        } else if (Util.areEqual(scheme, "urn:mpeg:dash:utc:http-xsdate:2012") || Util.areEqual(scheme, "urn:mpeg:dash:utc:http-xsdate:2014")) {
            resolveUtcTimingElementHttp(timingElement, new XsDateTimeParser());
        } else {
            onUtcTimestampResolutionError(new IOException("Unsupported UTC timing scheme"));
        }
    }

    private void resolveUtcTimingElementDirect(UtcTimingElement timingElement) {
        try {
            long utcTimestamp = Util.parseXsDateTime(timingElement.value);
            onUtcTimestampResolved(utcTimestamp - this.manifestLoadEndTimestamp);
        } catch (ParserException e) {
            onUtcTimestampResolutionError(e);
        }
    }

    private void resolveUtcTimingElementHttp(UtcTimingElement timingElement, ParsingLoadable.Parser<Long> parser) {
        startLoading(new ParsingLoadable(this.dataSource, Uri.parse(timingElement.value), 5, parser), new UtcTimestampCallback(), 1);
    }

    private void onUtcTimestampResolved(long elapsedRealtimeOffsetMs) {
        this.elapsedRealtimeOffsetMs = elapsedRealtimeOffsetMs;
        processManifest(true);
    }

    private void onUtcTimestampResolutionError(IOException error) {
        Log.e(TAG, "Failed to resolve UtcTiming element.", error);
        processManifest(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processManifest(boolean scheduleRefresh) {
        for (int i = 0; i < this.periodsById.size(); i++) {
            int id = this.periodsById.keyAt(i);
            if (id >= this.firstPeriodId) {
                this.periodsById.valueAt(i).updateManifest(this.manifest, id - this.firstPeriodId);
            }
        }
        boolean windowChangingImplicitly = false;
        int lastPeriodIndex = this.manifest.getPeriodCount() - 1;
        PeriodSeekInfo firstPeriodSeekInfo = PeriodSeekInfo.createPeriodSeekInfo(this.manifest.getPeriod(0), this.manifest.getPeriodDurationUs(0));
        PeriodSeekInfo lastPeriodSeekInfo = PeriodSeekInfo.createPeriodSeekInfo(this.manifest.getPeriod(lastPeriodIndex), this.manifest.getPeriodDurationUs(lastPeriodIndex));
        long currentStartTimeUs = firstPeriodSeekInfo.availableStartTimeUs;
        long currentEndTimeUs = lastPeriodSeekInfo.availableEndTimeUs;
        if (this.manifest.dynamic && !lastPeriodSeekInfo.isIndexExplicit) {
            long liveStreamDurationUs = getNowUnixTimeUs() - C.msToUs(this.manifest.availabilityStartTime);
            long liveStreamEndPositionInLastPeriodUs = liveStreamDurationUs - C.msToUs(this.manifest.getPeriod(lastPeriodIndex).startMs);
            currentEndTimeUs = Math.min(liveStreamEndPositionInLastPeriodUs, currentEndTimeUs);
            if (this.manifest.timeShiftBufferDepth != C.TIME_UNSET) {
                long timeShiftBufferDepthUs = C.msToUs(this.manifest.timeShiftBufferDepth);
                long offsetInPeriodUs = currentEndTimeUs - timeShiftBufferDepthUs;
                int periodIndex = lastPeriodIndex;
                while (offsetInPeriodUs < 0 && periodIndex > 0) {
                    periodIndex--;
                    offsetInPeriodUs += this.manifest.getPeriodDurationUs(periodIndex);
                }
                if (periodIndex == 0) {
                    currentStartTimeUs = Math.max(currentStartTimeUs, offsetInPeriodUs);
                } else {
                    currentStartTimeUs = this.manifest.getPeriodDurationUs(0);
                }
            }
            windowChangingImplicitly = true;
        }
        long windowDurationUs = currentEndTimeUs - currentStartTimeUs;
        for (int i2 = 0; i2 < this.manifest.getPeriodCount() - 1; i2++) {
            windowDurationUs += this.manifest.getPeriodDurationUs(i2);
        }
        long windowDefaultStartPositionUs = 0;
        if (this.manifest.dynamic) {
            long presentationDelayForManifestMs = this.livePresentationDelayMs;
            if (presentationDelayForManifestMs == -1) {
                presentationDelayForManifestMs = this.manifest.suggestedPresentationDelay != C.TIME_UNSET ? this.manifest.suggestedPresentationDelay : 30000L;
            }
            windowDefaultStartPositionUs = windowDurationUs - C.msToUs(presentationDelayForManifestMs);
            if (windowDefaultStartPositionUs < MIN_LIVE_DEFAULT_START_POSITION_US) {
                windowDefaultStartPositionUs = Math.min(MIN_LIVE_DEFAULT_START_POSITION_US, windowDurationUs / 2);
            }
        }
        long windowStartTimeMs = this.manifest.availabilityStartTime + this.manifest.getPeriod(0).startMs + C.usToMs(currentStartTimeUs);
        DashTimeline timeline = new DashTimeline(this.manifest.availabilityStartTime, windowStartTimeMs, this.firstPeriodId, currentStartTimeUs, windowDurationUs, windowDefaultStartPositionUs, this.manifest);
        this.sourceListener.onSourceInfoRefreshed(timeline, this.manifest);
        if (!this.sideloadedManifest) {
            this.handler.removeCallbacks(this.simulateManifestRefreshRunnable);
            if (windowChangingImplicitly) {
                this.handler.postDelayed(this.simulateManifestRefreshRunnable, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
            }
            if (scheduleRefresh) {
                scheduleManifestRefresh();
            }
        }
    }

    private void scheduleManifestRefresh() {
        if (this.manifest.dynamic) {
            long minUpdatePeriod = this.manifest.minUpdatePeriod;
            if (minUpdatePeriod == 0) {
                minUpdatePeriod = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
            }
            long nextLoadTimestamp = this.manifestLoadStartTimestamp + minUpdatePeriod;
            long delayUntilNextLoad = Math.max(0L, nextLoadTimestamp - SystemClock.elapsedRealtime());
            this.handler.postDelayed(this.refreshManifestRunnable, delayUntilNextLoad);
        }
    }

    private <T> void startLoading(ParsingLoadable<T> loadable, Loader.Callback<ParsingLoadable<T>> callback, int minRetryCount) {
        long elapsedRealtimeMs = this.loader.startLoading(loadable, callback, minRetryCount);
        this.eventDispatcher.loadStarted(loadable.dataSpec, loadable.type, elapsedRealtimeMs);
    }

    private long getNowUnixTimeUs() {
        return this.elapsedRealtimeOffsetMs != 0 ? C.msToUs(SystemClock.elapsedRealtime() + this.elapsedRealtimeOffsetMs) : C.msToUs(System.currentTimeMillis());
    }

    private static final class PeriodSeekInfo {
        public final long availableEndTimeUs;
        public final long availableStartTimeUs;
        public final boolean isIndexExplicit;

        public static PeriodSeekInfo createPeriodSeekInfo(Period period, long durationUs) {
            int adaptationSetCount = period.adaptationSets.size();
            long availableStartTimeUs = 0;
            long availableEndTimeUs = Long.MAX_VALUE;
            boolean isIndexExplicit = false;
            boolean seenEmptyIndex = false;
            for (int i = 0; i < adaptationSetCount; i++) {
                DashSegmentIndex index = period.adaptationSets.get(i).representations.get(0).getIndex();
                if (index == null) {
                    return new PeriodSeekInfo(true, 0L, durationUs);
                }
                isIndexExplicit |= index.isExplicit();
                int segmentCount = index.getSegmentCount(durationUs);
                if (segmentCount == 0) {
                    seenEmptyIndex = true;
                    availableStartTimeUs = 0;
                    availableEndTimeUs = 0;
                } else if (!seenEmptyIndex) {
                    int firstSegmentNum = index.getFirstSegmentNum();
                    long adaptationSetAvailableStartTimeUs = index.getTimeUs(firstSegmentNum);
                    availableStartTimeUs = Math.max(availableStartTimeUs, adaptationSetAvailableStartTimeUs);
                    if (segmentCount != -1) {
                        int lastSegmentNum = (firstSegmentNum + segmentCount) - 1;
                        long adaptationSetAvailableEndTimeUs = index.getTimeUs(lastSegmentNum) + index.getDurationUs(lastSegmentNum, durationUs);
                        availableEndTimeUs = Math.min(availableEndTimeUs, adaptationSetAvailableEndTimeUs);
                    }
                }
            }
            return new PeriodSeekInfo(isIndexExplicit, availableStartTimeUs, availableEndTimeUs);
        }

        private PeriodSeekInfo(boolean isIndexExplicit, long availableStartTimeUs, long availableEndTimeUs) {
            this.isIndexExplicit = isIndexExplicit;
            this.availableStartTimeUs = availableStartTimeUs;
            this.availableEndTimeUs = availableEndTimeUs;
        }
    }

    private static final class DashTimeline extends Timeline {
        private final int firstPeriodId;
        private final DashManifest manifest;
        private final long offsetInFirstPeriodUs;
        private final long presentationStartTimeMs;
        private final long windowDefaultStartPositionUs;
        private final long windowDurationUs;
        private final long windowStartTimeMs;

        public DashTimeline(long presentationStartTimeMs, long windowStartTimeMs, int firstPeriodId, long offsetInFirstPeriodUs, long windowDurationUs, long windowDefaultStartPositionUs, DashManifest manifest) {
            this.presentationStartTimeMs = presentationStartTimeMs;
            this.windowStartTimeMs = windowStartTimeMs;
            this.firstPeriodId = firstPeriodId;
            this.offsetInFirstPeriodUs = offsetInFirstPeriodUs;
            this.windowDurationUs = windowDurationUs;
            this.windowDefaultStartPositionUs = windowDefaultStartPositionUs;
            this.manifest = manifest;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getPeriodCount() {
            return this.manifest.getPeriodCount();
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Timeline.Period getPeriod(int periodIndex, Timeline.Period period, boolean setIdentifiers) {
            Assertions.checkIndex(periodIndex, 0, this.manifest.getPeriodCount());
            String id = setIdentifiers ? this.manifest.getPeriod(periodIndex).id : null;
            Integer uid = setIdentifiers ? Integer.valueOf(this.firstPeriodId + Assertions.checkIndex(periodIndex, 0, this.manifest.getPeriodCount())) : null;
            return period.set(id, uid, 0, this.manifest.getPeriodDurationUs(periodIndex), C.msToUs(this.manifest.getPeriod(periodIndex).startMs - this.manifest.getPeriod(0).startMs) - this.offsetInFirstPeriodUs, false);
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getWindowCount() {
            return 1;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Timeline.Window getWindow(int windowIndex, Timeline.Window window, boolean setIdentifier, long defaultPositionProjectionUs) {
            Assertions.checkIndex(windowIndex, 0, 1);
            long windowDefaultStartPositionUs = getAdjustedWindowDefaultStartPositionUs(defaultPositionProjectionUs);
            return window.set(null, this.presentationStartTimeMs, this.windowStartTimeMs, true, this.manifest.dynamic, windowDefaultStartPositionUs, this.windowDurationUs, 0, this.manifest.getPeriodCount() - 1, this.offsetInFirstPeriodUs);
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getIndexOfPeriod(Object uid) {
            int periodId;
            if ((uid instanceof Integer) && (periodId = ((Integer) uid).intValue()) >= this.firstPeriodId && periodId < this.firstPeriodId + getPeriodCount()) {
                return periodId - this.firstPeriodId;
            }
            return -1;
        }

        private long getAdjustedWindowDefaultStartPositionUs(long defaultPositionProjectionUs) {
            long windowDefaultStartPositionUs = this.windowDefaultStartPositionUs;
            if (!this.manifest.dynamic) {
                return windowDefaultStartPositionUs;
            }
            if (defaultPositionProjectionUs > 0) {
                windowDefaultStartPositionUs += defaultPositionProjectionUs;
                if (windowDefaultStartPositionUs > this.windowDurationUs) {
                    return C.TIME_UNSET;
                }
            }
            int periodIndex = 0;
            long defaultStartPositionInPeriodUs = this.offsetInFirstPeriodUs + windowDefaultStartPositionUs;
            long periodDurationUs = this.manifest.getPeriodDurationUs(0);
            while (periodIndex < this.manifest.getPeriodCount() - 1 && defaultStartPositionInPeriodUs >= periodDurationUs) {
                defaultStartPositionInPeriodUs -= periodDurationUs;
                periodIndex++;
                periodDurationUs = this.manifest.getPeriodDurationUs(periodIndex);
            }
            Period period = this.manifest.getPeriod(periodIndex);
            int videoAdaptationSetIndex = period.getAdaptationSetIndex(2);
            if (videoAdaptationSetIndex == -1) {
                return windowDefaultStartPositionUs;
            }
            DashSegmentIndex snapIndex = period.adaptationSets.get(videoAdaptationSetIndex).representations.get(0).getIndex();
            if (snapIndex == null || snapIndex.getSegmentCount(periodDurationUs) == 0) {
                return windowDefaultStartPositionUs;
            }
            int segmentNum = snapIndex.getSegmentNum(defaultStartPositionInPeriodUs, periodDurationUs);
            return (snapIndex.getTimeUs(segmentNum) + windowDefaultStartPositionUs) - defaultStartPositionInPeriodUs;
        }
    }

    private final class ManifestCallback implements Loader.Callback<ParsingLoadable<DashManifest>> {
        private ManifestCallback() {
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public void onLoadCompleted(ParsingLoadable<DashManifest> loadable, long elapsedRealtimeMs, long loadDurationMs) {
            DashMediaSource.this.onManifestLoadCompleted(loadable, elapsedRealtimeMs, loadDurationMs);
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public void onLoadCanceled(ParsingLoadable<DashManifest> loadable, long elapsedRealtimeMs, long loadDurationMs, boolean released) {
            DashMediaSource.this.onLoadCanceled(loadable, elapsedRealtimeMs, loadDurationMs);
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public int onLoadError(ParsingLoadable<DashManifest> loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
            return DashMediaSource.this.onManifestLoadError(loadable, elapsedRealtimeMs, loadDurationMs, error);
        }
    }

    private final class UtcTimestampCallback implements Loader.Callback<ParsingLoadable<Long>> {
        private UtcTimestampCallback() {
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public void onLoadCompleted(ParsingLoadable<Long> loadable, long elapsedRealtimeMs, long loadDurationMs) {
            DashMediaSource.this.onUtcTimestampLoadCompleted(loadable, elapsedRealtimeMs, loadDurationMs);
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public void onLoadCanceled(ParsingLoadable<Long> loadable, long elapsedRealtimeMs, long loadDurationMs, boolean released) {
            DashMediaSource.this.onLoadCanceled(loadable, elapsedRealtimeMs, loadDurationMs);
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public int onLoadError(ParsingLoadable<Long> loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
            return DashMediaSource.this.onUtcTimestampLoadError(loadable, elapsedRealtimeMs, loadDurationMs, error);
        }
    }

    private static final class XsDateTimeParser implements ParsingLoadable.Parser<Long> {
        private XsDateTimeParser() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.android.exoplayer2.upstream.ParsingLoadable.Parser
        public Long parse(Uri uri, InputStream inputStream) throws IOException {
            String firstLine = new BufferedReader(new InputStreamReader(inputStream)).readLine();
            return Long.valueOf(Util.parseXsDateTime(firstLine));
        }
    }

    private static final class Iso8601Parser implements ParsingLoadable.Parser<Long> {
        private Iso8601Parser() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.android.exoplayer2.upstream.ParsingLoadable.Parser
        public Long parse(Uri uri, InputStream inputStream) throws IOException {
            String firstLine = new BufferedReader(new InputStreamReader(inputStream)).readLine();
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                return Long.valueOf(format.parse(firstLine).getTime());
            } catch (ParseException e) {
                throw new ParserException(e);
            }
        }
    }
}
