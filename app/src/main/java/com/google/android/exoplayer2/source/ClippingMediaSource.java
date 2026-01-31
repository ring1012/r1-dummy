package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class ClippingMediaSource implements MediaSource, MediaSource.Listener {
    private ClippingTimeline clippingTimeline;
    private final long endUs;
    private final ArrayList<ClippingMediaPeriod> mediaPeriods;
    private final MediaSource mediaSource;
    private MediaSource.Listener sourceListener;
    private final long startUs;

    public ClippingMediaSource(MediaSource mediaSource, long startPositionUs, long endPositionUs) {
        Assertions.checkArgument(startPositionUs >= 0);
        this.mediaSource = (MediaSource) Assertions.checkNotNull(mediaSource);
        this.startUs = startPositionUs;
        this.endUs = endPositionUs;
        this.mediaPeriods = new ArrayList<>();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void prepareSource(ExoPlayer player, boolean isTopLevelSource, MediaSource.Listener listener) {
        this.sourceListener = listener;
        this.mediaSource.prepareSource(player, false, this);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.mediaSource.maybeThrowSourceInfoRefreshError();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public MediaPeriod createPeriod(int index, Allocator allocator, long positionUs) {
        ClippingMediaPeriod mediaPeriod = new ClippingMediaPeriod(this.mediaSource.createPeriod(index, allocator, this.startUs + positionUs));
        this.mediaPeriods.add(mediaPeriod);
        mediaPeriod.setClipping(this.clippingTimeline.startUs, this.clippingTimeline.endUs);
        return mediaPeriod;
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releasePeriod(MediaPeriod mediaPeriod) {
        Assertions.checkState(this.mediaPeriods.remove(mediaPeriod));
        this.mediaSource.releasePeriod(((ClippingMediaPeriod) mediaPeriod).mediaPeriod);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releaseSource() {
        this.mediaSource.releaseSource();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource.Listener
    public void onSourceInfoRefreshed(Timeline timeline, Object manifest) {
        this.clippingTimeline = new ClippingTimeline(timeline, this.startUs, this.endUs);
        this.sourceListener.onSourceInfoRefreshed(this.clippingTimeline, manifest);
        long startUs = this.clippingTimeline.startUs;
        long endUs = this.clippingTimeline.endUs == C.TIME_UNSET ? Long.MIN_VALUE : this.clippingTimeline.endUs;
        int count = this.mediaPeriods.size();
        for (int i = 0; i < count; i++) {
            this.mediaPeriods.get(i).setClipping(startUs, endUs);
        }
    }

    private static final class ClippingTimeline extends Timeline {
        private final long endUs;
        private final long startUs;
        private final Timeline timeline;

        public ClippingTimeline(Timeline timeline, long startUs, long endUs) {
            Assertions.checkArgument(timeline.getWindowCount() == 1);
            Assertions.checkArgument(timeline.getPeriodCount() == 1);
            Timeline.Window window = timeline.getWindow(0, new Timeline.Window(), false);
            Assertions.checkArgument(!window.isDynamic);
            long resolvedEndUs = endUs == Long.MIN_VALUE ? window.durationUs : endUs;
            if (window.durationUs != C.TIME_UNSET) {
                Assertions.checkArgument(startUs == 0 || window.isSeekable);
                Assertions.checkArgument(resolvedEndUs <= window.durationUs);
                Assertions.checkArgument(startUs <= resolvedEndUs);
            }
            Timeline.Period period = timeline.getPeriod(0, new Timeline.Period());
            Assertions.checkArgument(period.getPositionInWindowUs() == 0);
            this.timeline = timeline;
            this.startUs = startUs;
            this.endUs = resolvedEndUs;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getWindowCount() {
            return 1;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Timeline.Window getWindow(int windowIndex, Timeline.Window window, boolean setIds, long defaultPositionProjectionUs) {
            Timeline.Window window2 = this.timeline.getWindow(0, window, setIds, defaultPositionProjectionUs);
            window2.durationUs = this.endUs != C.TIME_UNSET ? this.endUs - this.startUs : -9223372036854775807L;
            if (window2.defaultPositionUs != C.TIME_UNSET) {
                window2.defaultPositionUs = Math.max(window2.defaultPositionUs, this.startUs);
                window2.defaultPositionUs = this.endUs == C.TIME_UNSET ? window2.defaultPositionUs : Math.min(window2.defaultPositionUs, this.endUs);
                window2.defaultPositionUs -= this.startUs;
            }
            long startMs = C.usToMs(this.startUs);
            if (window2.presentationStartTimeMs != C.TIME_UNSET) {
                window2.presentationStartTimeMs += startMs;
            }
            if (window2.windowStartTimeMs != C.TIME_UNSET) {
                window2.windowStartTimeMs += startMs;
            }
            return window2;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getPeriodCount() {
            return 1;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Timeline.Period getPeriod(int periodIndex, Timeline.Period period, boolean setIds) {
            long j = C.TIME_UNSET;
            Timeline.Period period2 = this.timeline.getPeriod(0, period, setIds);
            if (this.endUs != C.TIME_UNSET) {
                j = this.endUs - this.startUs;
            }
            period2.durationUs = j;
            return period2;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getIndexOfPeriod(Object uid) {
            return this.timeline.getIndexOfPeriod(uid);
        }
    }
}
