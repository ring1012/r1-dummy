package com.google.android.exoplayer2.ui;

import android.support.annotation.Nullable;

/* loaded from: classes.dex */
public interface TimeBar {

    public interface OnScrubListener {
        void onScrubMove(TimeBar timeBar, long j);

        void onScrubStart(TimeBar timeBar);

        void onScrubStop(TimeBar timeBar, long j, boolean z);
    }

    void setAdBreakTimesMs(@Nullable long[] jArr, int i);

    void setBufferedPosition(long j);

    void setDuration(long j);

    void setEnabled(boolean z);

    void setKeyCountIncrement(int i);

    void setKeyTimeIncrement(long j);

    void setListener(OnScrubListener onScrubListener);

    void setPosition(long j);
}
