package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.Allocator;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class MergingMediaSource implements MediaSource {
    private static final int PERIOD_COUNT_UNSET = -1;
    private MediaSource.Listener listener;
    private final MediaSource[] mediaSources;
    private IllegalMergeException mergeError;
    private final ArrayList<MediaSource> pendingTimelineSources;
    private Object primaryManifest;
    private Timeline primaryTimeline;
    private final Timeline.Window window = new Timeline.Window();
    private int periodCount = -1;

    public static final class IllegalMergeException extends IOException {
        public static final int REASON_PERIOD_COUNT_MISMATCH = 1;
        public static final int REASON_WINDOWS_ARE_DYNAMIC = 0;
        public final int reason;

        @Retention(RetentionPolicy.SOURCE)
        public @interface Reason {
        }

        public IllegalMergeException(int reason) {
            this.reason = reason;
        }
    }

    public MergingMediaSource(MediaSource... mediaSources) {
        this.mediaSources = mediaSources;
        this.pendingTimelineSources = new ArrayList<>(Arrays.asList(mediaSources));
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void prepareSource(ExoPlayer player, boolean isTopLevelSource, MediaSource.Listener listener) {
        this.listener = listener;
        for (int i = 0; i < this.mediaSources.length; i++) {
            final int sourceIndex = i;
            this.mediaSources[sourceIndex].prepareSource(player, false, new MediaSource.Listener() { // from class: com.google.android.exoplayer2.source.MergingMediaSource.1
                @Override // com.google.android.exoplayer2.source.MediaSource.Listener
                public void onSourceInfoRefreshed(Timeline timeline, Object manifest) {
                    MergingMediaSource.this.handleSourceInfoRefreshed(sourceIndex, timeline, manifest);
                }
            });
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        if (this.mergeError != null) {
            throw this.mergeError;
        }
        for (MediaSource mediaSource : this.mediaSources) {
            mediaSource.maybeThrowSourceInfoRefreshError();
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public MediaPeriod createPeriod(int index, Allocator allocator, long positionUs) {
        MediaPeriod[] periods = new MediaPeriod[this.mediaSources.length];
        for (int i = 0; i < periods.length; i++) {
            periods[i] = this.mediaSources[i].createPeriod(index, allocator, positionUs);
        }
        return new MergingMediaPeriod(periods);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releasePeriod(MediaPeriod mediaPeriod) {
        MergingMediaPeriod mergingPeriod = (MergingMediaPeriod) mediaPeriod;
        for (int i = 0; i < this.mediaSources.length; i++) {
            this.mediaSources[i].releasePeriod(mergingPeriod.periods[i]);
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releaseSource() {
        for (MediaSource mediaSource : this.mediaSources) {
            mediaSource.releaseSource();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSourceInfoRefreshed(int sourceIndex, Timeline timeline, Object manifest) {
        if (this.mergeError == null) {
            this.mergeError = checkTimelineMerges(timeline);
        }
        if (this.mergeError == null) {
            this.pendingTimelineSources.remove(this.mediaSources[sourceIndex]);
            if (sourceIndex == 0) {
                this.primaryTimeline = timeline;
                this.primaryManifest = manifest;
            }
            if (this.pendingTimelineSources.isEmpty()) {
                this.listener.onSourceInfoRefreshed(this.primaryTimeline, this.primaryManifest);
            }
        }
    }

    private IllegalMergeException checkTimelineMerges(Timeline timeline) {
        int windowCount = timeline.getWindowCount();
        for (int i = 0; i < windowCount; i++) {
            if (timeline.getWindow(i, this.window, false).isDynamic) {
                return new IllegalMergeException(0);
            }
        }
        if (this.periodCount == -1) {
            this.periodCount = timeline.getPeriodCount();
        } else if (timeline.getPeriodCount() != this.periodCount) {
            return new IllegalMergeException(1);
        }
        return null;
    }
}
