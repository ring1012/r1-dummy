package com.google.android.exoplayer2;

/* loaded from: classes.dex */
public abstract class Timeline {
    public static final Timeline EMPTY = new Timeline() { // from class: com.google.android.exoplayer2.Timeline.1
        @Override // com.google.android.exoplayer2.Timeline
        public int getWindowCount() {
            return 0;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Window getWindow(int windowIndex, Window window, boolean setIds, long defaultPositionProjectionUs) {
            throw new IndexOutOfBoundsException();
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getPeriodCount() {
            return 0;
        }

        @Override // com.google.android.exoplayer2.Timeline
        public Period getPeriod(int periodIndex, Period period, boolean setIds) {
            throw new IndexOutOfBoundsException();
        }

        @Override // com.google.android.exoplayer2.Timeline
        public int getIndexOfPeriod(Object uid) {
            return -1;
        }
    };

    public abstract int getIndexOfPeriod(Object obj);

    public abstract Period getPeriod(int i, Period period, boolean z);

    public abstract int getPeriodCount();

    public abstract Window getWindow(int i, Window window, boolean z, long j);

    public abstract int getWindowCount();

    public final boolean isEmpty() {
        return getWindowCount() == 0;
    }

    public final Window getWindow(int windowIndex, Window window) {
        return getWindow(windowIndex, window, false);
    }

    public Window getWindow(int windowIndex, Window window, boolean setIds) {
        return getWindow(windowIndex, window, setIds, 0L);
    }

    public final Period getPeriod(int periodIndex, Period period) {
        return getPeriod(periodIndex, period, false);
    }

    public static final class Window {
        public long defaultPositionUs;
        public long durationUs;
        public int firstPeriodIndex;
        public Object id;
        public boolean isDynamic;
        public boolean isSeekable;
        public int lastPeriodIndex;
        public long positionInFirstPeriodUs;
        public long presentationStartTimeMs;
        public long windowStartTimeMs;

        public Window set(Object id, long presentationStartTimeMs, long windowStartTimeMs, boolean isSeekable, boolean isDynamic, long defaultPositionUs, long durationUs, int firstPeriodIndex, int lastPeriodIndex, long positionInFirstPeriodUs) {
            this.id = id;
            this.presentationStartTimeMs = presentationStartTimeMs;
            this.windowStartTimeMs = windowStartTimeMs;
            this.isSeekable = isSeekable;
            this.isDynamic = isDynamic;
            this.defaultPositionUs = defaultPositionUs;
            this.durationUs = durationUs;
            this.firstPeriodIndex = firstPeriodIndex;
            this.lastPeriodIndex = lastPeriodIndex;
            this.positionInFirstPeriodUs = positionInFirstPeriodUs;
            return this;
        }

        public long getDefaultPositionMs() {
            return C.usToMs(this.defaultPositionUs);
        }

        public long getDefaultPositionUs() {
            return this.defaultPositionUs;
        }

        public long getDurationMs() {
            return C.usToMs(this.durationUs);
        }

        public long getDurationUs() {
            return this.durationUs;
        }

        public long getPositionInFirstPeriodMs() {
            return C.usToMs(this.positionInFirstPeriodUs);
        }

        public long getPositionInFirstPeriodUs() {
            return this.positionInFirstPeriodUs;
        }
    }

    public static final class Period {
        public long durationUs;
        public Object id;
        public boolean isAd;
        private long positionInWindowUs;
        public Object uid;
        public int windowIndex;

        public Period set(Object id, Object uid, int windowIndex, long durationUs, long positionInWindowUs, boolean isAd) {
            this.id = id;
            this.uid = uid;
            this.windowIndex = windowIndex;
            this.durationUs = durationUs;
            this.positionInWindowUs = positionInWindowUs;
            this.isAd = isAd;
            return this;
        }

        public long getDurationMs() {
            return C.usToMs(this.durationUs);
        }

        public long getDurationUs() {
            return this.durationUs;
        }

        public long getPositionInWindowMs() {
            return C.usToMs(this.positionInWindowUs);
        }

        public long getPositionInWindowUs() {
            return this.positionInWindowUs;
        }
    }
}
