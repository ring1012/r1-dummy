package com.google.android.exoplayer2.source;

import android.util.Pair;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/* loaded from: classes.dex */
public final class ConcatenatingMediaSource implements MediaSource {
    private final boolean[] duplicateFlags;
    private MediaSource.Listener listener;
    private final Object[] manifests;
    private final MediaSource[] mediaSources;
    private final Map<MediaPeriod, Integer> sourceIndexByMediaPeriod = new HashMap();
    private ConcatenatedTimeline timeline;
    private final Timeline[] timelines;

    public ConcatenatingMediaSource(MediaSource... mediaSources) {
        this.mediaSources = mediaSources;
        this.timelines = new Timeline[mediaSources.length];
        this.manifests = new Object[mediaSources.length];
        this.duplicateFlags = buildDuplicateFlags(mediaSources);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void prepareSource(ExoPlayer player, boolean isTopLevelSource, MediaSource.Listener listener) {
        this.listener = listener;
        for (int i = 0; i < this.mediaSources.length; i++) {
            if (!this.duplicateFlags[i]) {
                final int index = i;
                this.mediaSources[i].prepareSource(player, false, new MediaSource.Listener() { // from class: com.google.android.exoplayer2.source.ConcatenatingMediaSource.1
                    @Override // com.google.android.exoplayer2.source.MediaSource.Listener
                    public void onSourceInfoRefreshed(Timeline timeline, Object manifest) {
                        ConcatenatingMediaSource.this.handleSourceInfoRefreshed(index, timeline, manifest);
                    }
                });
            }
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void maybeThrowSourceInfoRefreshError() throws IOException {
        for (int i = 0; i < this.mediaSources.length; i++) {
            if (!this.duplicateFlags[i]) {
                this.mediaSources[i].maybeThrowSourceInfoRefreshError();
            }
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public MediaPeriod createPeriod(int index, Allocator allocator, long positionUs) {
        int sourceIndex = this.timeline.getSourceIndexForPeriod(index);
        int periodIndexInSource = index - this.timeline.getFirstPeriodIndexInSource(sourceIndex);
        MediaPeriod mediaPeriod = this.mediaSources[sourceIndex].createPeriod(periodIndexInSource, allocator, positionUs);
        this.sourceIndexByMediaPeriod.put(mediaPeriod, Integer.valueOf(sourceIndex));
        return mediaPeriod;
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releasePeriod(MediaPeriod mediaPeriod) {
        int sourceIndex = this.sourceIndexByMediaPeriod.get(mediaPeriod).intValue();
        this.sourceIndexByMediaPeriod.remove(mediaPeriod);
        this.mediaSources[sourceIndex].releasePeriod(mediaPeriod);
    }

    @Override // com.google.android.exoplayer2.source.MediaSource
    public void releaseSource() {
        for (int i = 0; i < this.mediaSources.length; i++) {
            if (!this.duplicateFlags[i]) {
                this.mediaSources[i].releaseSource();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSourceInfoRefreshed(int sourceFirstIndex, Timeline sourceTimeline, Object sourceManifest) {
        this.timelines[sourceFirstIndex] = sourceTimeline;
        this.manifests[sourceFirstIndex] = sourceManifest;
        for (int i = sourceFirstIndex + 1; i < this.mediaSources.length; i++) {
            if (this.mediaSources[i] == this.mediaSources[sourceFirstIndex]) {
                this.timelines[i] = sourceTimeline;
                this.manifests[i] = sourceManifest;
            }
        }
        for (Timeline timeline : this.timelines) {
            if (timeline == null) {
                return;
            }
        }
        this.timeline = new ConcatenatedTimeline((Timeline[]) this.timelines.clone());
        this.listener.onSourceInfoRefreshed(this.timeline, this.manifests.clone());
    }

    private static boolean[] buildDuplicateFlags(MediaSource[] mediaSources) {
        boolean[] duplicateFlags = new boolean[mediaSources.length];
        IdentityHashMap<MediaSource, Void> sources = new IdentityHashMap<>(mediaSources.length);
        for (int i = 0; i < mediaSources.length; i++) {
            MediaSource source = mediaSources[i];
            if (!sources.containsKey(source)) {
                sources.put(source, null);
            } else {
                duplicateFlags[i] = true;
            }
        }
        return duplicateFlags;
    }

    private static final class ConcatenatedTimeline extends Timeline {
        private final int[] sourcePeriodOffsets;
        private final int[] sourceWindowOffsets;
        private final Timeline[] timelines;

        public ConcatenatedTimeline(Timeline[] timelines) {
            int[] sourcePeriodOffsets = new int[timelines.length];
            int[] sourceWindowOffsets = new int[timelines.length];
            long periodCount = 0;
            int windowCount = 0;
            for (int i = 0; i < timelines.length; i++) {
                Timeline timeline = timelines[i];
                periodCount += timeline.getPeriodCount();
                Assertions.checkState(periodCount <= 2147483647L, "ConcatenatingMediaSource children contain too many periods");
                sourcePeriodOffsets[i] = (int) periodCount;
                windowCount += timeline.getWindowCount();
                sourceWindowOffsets[i] = windowCount;
            }
            this.timelines = timelines;
            this.sourcePeriodOffsets = sourcePeriodOffsets;
            this.sourceWindowOffsets = sourceWindowOffsets;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getWindowCount() {
            return this.sourceWindowOffsets[this.sourceWindowOffsets.length - 1];
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Timeline.Window getWindow(int windowIndex, Timeline.Window window, boolean setIds, long defaultPositionProjectionUs) {
            int sourceIndex = getSourceIndexForWindow(windowIndex);
            int firstWindowIndexInSource = getFirstWindowIndexInSource(sourceIndex);
            int firstPeriodIndexInSource = getFirstPeriodIndexInSource(sourceIndex);
            this.timelines[sourceIndex].getWindow(windowIndex - firstWindowIndexInSource, window, setIds, defaultPositionProjectionUs);
            window.firstPeriodIndex += firstPeriodIndexInSource;
            window.lastPeriodIndex += firstPeriodIndexInSource;
            return window;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getPeriodCount() {
            return this.sourcePeriodOffsets[this.sourcePeriodOffsets.length - 1];
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Timeline.Period getPeriod(int periodIndex, Timeline.Period period, boolean setIds) {
            int sourceIndex = getSourceIndexForPeriod(periodIndex);
            int firstWindowIndexInSource = getFirstWindowIndexInSource(sourceIndex);
            int firstPeriodIndexInSource = getFirstPeriodIndexInSource(sourceIndex);
            this.timelines[sourceIndex].getPeriod(periodIndex - firstPeriodIndexInSource, period, setIds);
            period.windowIndex += firstWindowIndexInSource;
            if (setIds) {
                period.uid = Pair.create(Integer.valueOf(sourceIndex), period.uid);
            }
            return period;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getIndexOfPeriod(Object uid) {
            if (!(uid instanceof Pair)) {
                return -1;
            }
            Pair<?, ?> sourceIndexAndPeriodId = (Pair) uid;
            if (!(sourceIndexAndPeriodId.first instanceof Integer)) {
                return -1;
            }
            int sourceIndex = ((Integer) sourceIndexAndPeriodId.first).intValue();
            Object periodId = sourceIndexAndPeriodId.second;
            if (sourceIndex < 0 || sourceIndex >= this.timelines.length) {
                return -1;
            }
            int periodIndexInSource = this.timelines[sourceIndex].getIndexOfPeriod(periodId);
            return periodIndexInSource == -1 ? -1 : getFirstPeriodIndexInSource(sourceIndex) + periodIndexInSource;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getSourceIndexForPeriod(int periodIndex) {
            return Util.binarySearchFloor(this.sourcePeriodOffsets, periodIndex, true, false) + 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getFirstPeriodIndexInSource(int sourceIndex) {
            if (sourceIndex == 0) {
                return 0;
            }
            return this.sourcePeriodOffsets[sourceIndex - 1];
        }

        private int getSourceIndexForWindow(int windowIndex) {
            return Util.binarySearchFloor(this.sourceWindowOffsets, windowIndex, true, false) + 1;
        }

        private int getFirstWindowIndexInSource(int sourceIndex) {
            if (sourceIndex == 0) {
                return 0;
            }
            return this.sourceWindowOffsets[sourceIndex - 1];
        }
    }
}
