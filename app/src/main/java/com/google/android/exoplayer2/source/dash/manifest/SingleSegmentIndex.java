package com.google.android.exoplayer2.source.dash.manifest;

import com.google.android.exoplayer2.source.dash.DashSegmentIndex;

/* loaded from: classes.dex */
final class SingleSegmentIndex implements DashSegmentIndex {
    private final RangedUri uri;

    public SingleSegmentIndex(RangedUri uri) {
        this.uri = uri;
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public int getSegmentNum(long timeUs, long periodDurationUs) {
        return 0;
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public long getTimeUs(int segmentNum) {
        return 0L;
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public long getDurationUs(int segmentNum, long periodDurationUs) {
        return periodDurationUs;
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public RangedUri getSegmentUrl(int segmentNum) {
        return this.uri;
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public int getFirstSegmentNum() {
        return 0;
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public int getSegmentCount(long periodDurationUs) {
        return 1;
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public boolean isExplicit() {
        return true;
    }
}
