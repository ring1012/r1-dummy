package com.google.android.exoplayer2.source;

import android.net.Uri;
import android.os.Handler;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;

/* loaded from: classes.dex */
public final class ExtractorMediaSource implements MediaSource, MediaSource.Listener {
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT_LIVE = 6;
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT_ON_DEMAND = 3;
    public static final int MIN_RETRY_COUNT_DEFAULT_FOR_MEDIA = -1;
    private final String customCacheKey;
    private final DataSource.Factory dataSourceFactory;
    private final Handler eventHandler;
    private final EventListener eventListener;
    private final ExtractorsFactory extractorsFactory;
    private final int minLoadableRetryCount;
    private final Timeline.Period period;
    private MediaSource.Listener sourceListener;
    private Timeline timeline;
    private boolean timelineHasDuration;
    private final Uri uri;

    public interface EventListener {
        void onLoadError(IOException iOException);
    }

    public ExtractorMediaSource(Uri uri, DataSource.Factory dataSourceFactory, ExtractorsFactory extractorsFactory, Handler eventHandler, EventListener eventListener) {
        this(uri, dataSourceFactory, extractorsFactory, -1, eventHandler, eventListener, null);
    }

    public ExtractorMediaSource(Uri uri, DataSource.Factory dataSourceFactory, ExtractorsFactory extractorsFactory, Handler eventHandler, EventListener eventListener, String customCacheKey) {
        this(uri, dataSourceFactory, extractorsFactory, -1, eventHandler, eventListener, customCacheKey);
    }

    public ExtractorMediaSource(Uri uri, DataSource.Factory dataSourceFactory, ExtractorsFactory extractorsFactory, int minLoadableRetryCount, Handler eventHandler, EventListener eventListener, String customCacheKey) {
        this.uri = uri;
        this.dataSourceFactory = dataSourceFactory;
        this.extractorsFactory = extractorsFactory;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        this.customCacheKey = customCacheKey;
        this.period = new Timeline.Period();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void prepareSource(ExoPlayer player, boolean isTopLevelSource, MediaSource.Listener listener) {
        this.sourceListener = listener;
        this.timeline = new SinglePeriodTimeline(C.TIME_UNSET, false);
        listener.onSourceInfoRefreshed(this.timeline, null);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public MediaPeriod createPeriod(int index, Allocator allocator, long positionUs) {
        Assertions.checkArgument(index == 0);
        return new ExtractorMediaPeriod(this.uri, this.dataSourceFactory.createDataSource(), this.extractorsFactory.createExtractors(), this.minLoadableRetryCount, this.eventHandler, this.eventListener, this, allocator, this.customCacheKey);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releasePeriod(MediaPeriod mediaPeriod) {
        ((ExtractorMediaPeriod) mediaPeriod).release();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releaseSource() {
        this.sourceListener = null;
    }

    @Override // com.google.android.exoplayer2.source.MediaSource.Listener
    public void onSourceInfoRefreshed(Timeline newTimeline, Object manifest) {
        long newTimelineDurationUs = newTimeline.getPeriod(0, this.period).getDurationUs();
        boolean newTimelineHasDuration = newTimelineDurationUs != C.TIME_UNSET;
        if (!this.timelineHasDuration || newTimelineHasDuration) {
            this.timeline = newTimeline;
            this.timelineHasDuration = newTimelineHasDuration;
            this.sourceListener.onSourceInfoRefreshed(this.timeline, null);
        }
    }
}
