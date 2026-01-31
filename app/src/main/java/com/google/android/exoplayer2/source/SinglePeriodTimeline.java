package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.util.Assertions;

/* loaded from: classes.dex */
public final class SinglePeriodTimeline extends Timeline {
    private static final Object ID = new Object();
    private final boolean isDynamic;
    private final boolean isSeekable;
    private final long periodDurationUs;
    private final long windowDefaultStartPositionUs;
    private final long windowDurationUs;
    private final long windowPositionInPeriodUs;

    public SinglePeriodTimeline(long durationUs, boolean isSeekable) {
        this(durationUs, durationUs, 0L, 0L, isSeekable, false);
    }

    public SinglePeriodTimeline(long periodDurationUs, long windowDurationUs, long windowPositionInPeriodUs, long windowDefaultStartPositionUs, boolean isSeekable, boolean isDynamic) {
        this.periodDurationUs = periodDurationUs;
        this.windowDurationUs = windowDurationUs;
        this.windowPositionInPeriodUs = windowPositionInPeriodUs;
        this.windowDefaultStartPositionUs = windowDefaultStartPositionUs;
        this.isSeekable = isSeekable;
        this.isDynamic = isDynamic;
    }

    @Override // com.google.android.exoplayer2.Timeline
    public int getWindowCount() {
        return 1;
    }

    @Override // com.google.android.exoplayer2.Timeline
    public Timeline.Window getWindow(int windowIndex, Timeline.Window window, boolean setIds, long defaultPositionProjectionUs) {
        Assertions.checkIndex(windowIndex, 0, 1);
        Object id = setIds ? ID : null;
        long windowDefaultStartPositionUs = this.windowDefaultStartPositionUs;
        if (this.isDynamic) {
            windowDefaultStartPositionUs += defaultPositionProjectionUs;
            if (windowDefaultStartPositionUs > this.windowDurationUs) {
                windowDefaultStartPositionUs = C.TIME_UNSET;
            }
        }
        return window.set(id, C.TIME_UNSET, C.TIME_UNSET, this.isSeekable, this.isDynamic, windowDefaultStartPositionUs, this.windowDurationUs, 0, 0, this.windowPositionInPeriodUs);
    }

    @Override // com.google.android.exoplayer2.Timeline
    public int getPeriodCount() {
        return 1;
    }

    @Override // com.google.android.exoplayer2.Timeline
    public Timeline.Period getPeriod(int periodIndex, Timeline.Period period, boolean setIds) {
        Assertions.checkIndex(periodIndex, 0, 1);
        Object id = setIds ? ID : null;
        return period.set(id, id, 0, this.periodDurationUs, -this.windowPositionInPeriodUs, false);
    }

    @Override // com.google.android.exoplayer2.Timeline
    public int getIndexOfPeriod(Object uid) {
        return ID.equals(uid) ? 0 : -1;
    }
}
