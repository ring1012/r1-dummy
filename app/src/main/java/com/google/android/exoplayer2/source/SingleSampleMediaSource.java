package com.google.android.exoplayer2.source;

import android.net.Uri;
import android.os.Handler;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;

/* loaded from: classes.dex */
public final class SingleSampleMediaSource implements MediaSource {
    public static final int DEFAULT_MIN_LOADABLE_RETRY_COUNT = 3;
    private final DataSource.Factory dataSourceFactory;
    private final Handler eventHandler;
    private final EventListener eventListener;
    private final int eventSourceId;
    private final Format format;
    private final int minLoadableRetryCount;
    private final Timeline timeline;
    private final Uri uri;

    public interface EventListener {
        void onLoadError(int i, IOException iOException);
    }

    public SingleSampleMediaSource(Uri uri, DataSource.Factory dataSourceFactory, Format format, long durationUs) {
        this(uri, dataSourceFactory, format, durationUs, 3);
    }

    public SingleSampleMediaSource(Uri uri, DataSource.Factory dataSourceFactory, Format format, long durationUs, int minLoadableRetryCount) {
        this(uri, dataSourceFactory, format, durationUs, minLoadableRetryCount, null, null, 0);
    }

    public SingleSampleMediaSource(Uri uri, DataSource.Factory dataSourceFactory, Format format, long durationUs, int minLoadableRetryCount, Handler eventHandler, EventListener eventListener, int eventSourceId) {
        this.uri = uri;
        this.dataSourceFactory = dataSourceFactory;
        this.format = format;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        this.eventSourceId = eventSourceId;
        this.timeline = new SinglePeriodTimeline(durationUs, true);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void prepareSource(ExoPlayer player, boolean isTopLevelSource, MediaSource.Listener listener) {
        listener.onSourceInfoRefreshed(this.timeline, null);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public MediaPeriod createPeriod(int index, Allocator allocator, long positionUs) {
        Assertions.checkArgument(index == 0);
        return new SingleSampleMediaPeriod(this.uri, this.dataSourceFactory, this.format, this.minLoadableRetryCount, this.eventHandler, this.eventListener, this.eventSourceId);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releasePeriod(MediaPeriod mediaPeriod) {
        ((SingleSampleMediaPeriod) mediaPeriod).release();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releaseSource() {
    }
}
