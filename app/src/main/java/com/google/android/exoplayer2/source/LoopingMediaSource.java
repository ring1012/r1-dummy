package com.google.android.exoplayer2.source;

import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;
import com.unisound.vui.priority.PriorityMap;
import java.io.IOException;

/* loaded from: classes.dex */
public final class LoopingMediaSource implements MediaSource {
    public static final int MAX_EXPOSED_PERIODS = 157680000;
    private static final String TAG = "LoopingMediaSource";
    private int childPeriodCount;
    private final MediaSource childSource;
    private final int loopCount;

    public LoopingMediaSource(MediaSource childSource) {
        this(childSource, PriorityMap.PRIORITY_MAX);
    }

    public LoopingMediaSource(MediaSource childSource, int loopCount) {
        Assertions.checkArgument(loopCount > 0);
        this.childSource = childSource;
        this.loopCount = loopCount;
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void prepareSource(ExoPlayer player, boolean isTopLevelSource, final MediaSource.Listener listener) {
        this.childSource.prepareSource(player, false, new MediaSource.Listener() { // from class: com.google.android.exoplayer2.source.LoopingMediaSource.1
            @Override // com.google.android.exoplayer2.source.MediaSource.Listener
            public void onSourceInfoRefreshed(Timeline timeline, Object manifest) {
                LoopingMediaSource.this.childPeriodCount = timeline.getPeriodCount();
                listener.onSourceInfoRefreshed(new LoopingTimeline(timeline, LoopingMediaSource.this.loopCount), manifest);
            }
        });
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        this.childSource.maybeThrowSourceInfoRefreshError();
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public MediaPeriod createPeriod(int index, Allocator allocator, long positionUs) {
        return this.childSource.createPeriod(index % this.childPeriodCount, allocator, positionUs);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releasePeriod(MediaPeriod mediaPeriod) {
        this.childSource.releasePeriod(mediaPeriod);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releaseSource() {
        this.childSource.releaseSource();
    }

    private static final class LoopingTimeline extends Timeline {
        private final int childPeriodCount;
        private final Timeline childTimeline;
        private final int childWindowCount;
        private final int loopCount;

        public LoopingTimeline(Timeline childTimeline, int loopCount) {
            this.childTimeline = childTimeline;
            this.childPeriodCount = childTimeline.getPeriodCount();
            this.childWindowCount = childTimeline.getWindowCount();
            int maxLoopCount = LoopingMediaSource.MAX_EXPOSED_PERIODS / this.childPeriodCount;
            if (loopCount > maxLoopCount) {
                if (loopCount != Integer.MAX_VALUE) {
                    Log.w(LoopingMediaSource.TAG, "Capped loops to avoid overflow: " + loopCount + " -> " + maxLoopCount);
                }
                this.loopCount = maxLoopCount;
                return;
            }
            this.loopCount = loopCount;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getWindowCount() {
            return this.childWindowCount * this.loopCount;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Timeline.Window getWindow(int windowIndex, Timeline.Window window, boolean setIds, long defaultPositionProjectionUs) {
            this.childTimeline.getWindow(windowIndex % this.childWindowCount, window, setIds, defaultPositionProjectionUs);
            int periodIndexOffset = (windowIndex / this.childWindowCount) * this.childPeriodCount;
            window.firstPeriodIndex += periodIndexOffset;
            window.lastPeriodIndex += periodIndexOffset;
            return window;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getPeriodCount() {
            return this.childPeriodCount * this.loopCount;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Timeline.Period getPeriod(int periodIndex, Timeline.Period period, boolean setIds) {
            this.childTimeline.getPeriod(periodIndex % this.childPeriodCount, period, setIds);
            int loopCount = periodIndex / this.childPeriodCount;
            period.windowIndex += this.childWindowCount * loopCount;
            if (setIds) {
                period.uid = Pair.create(Integer.valueOf(loopCount), period.uid);
            }
            return period;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getIndexOfPeriod(Object uid) {
            if (!(uid instanceof Pair)) {
                return -1;
            }
            Pair<?, ?> loopCountAndChildUid = (Pair) uid;
            if (!(loopCountAndChildUid.first instanceof Integer)) {
                return -1;
            }
            int loopCount = ((Integer) loopCountAndChildUid.first).intValue();
            int periodIndexOffset = loopCount * this.childPeriodCount;
            return this.childTimeline.getIndexOfPeriod(loopCountAndChildUid.second) + periodIndexOffset;
        }
    }
}
