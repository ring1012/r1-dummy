package com.google.android.exoplayer2.source.dash;

import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;

/* loaded from: classes.dex */
public final class DashWrappingSegmentIndex implements DashSegmentIndex {
    private final ChunkIndex chunkIndex;

    public DashWrappingSegmentIndex(ChunkIndex chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public int getFirstSegmentNum() {
        return 0;
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public int getSegmentCount(long periodDurationUs) {
        return this.chunkIndex.length;
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public long getTimeUs(int segmentNum) {
        return this.chunkIndex.timesUs[segmentNum];
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public long getDurationUs(int segmentNum, long periodDurationUs) {
        return this.chunkIndex.durationsUs[segmentNum];
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public RangedUri getSegmentUrl(int segmentNum) {
        return new RangedUri(null, this.chunkIndex.offsets[segmentNum], this.chunkIndex.sizes[segmentNum]);
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public int getSegmentNum(long timeUs, long periodDurationUs) {
        return this.chunkIndex.getChunkIndex(timeUs);
    }

    @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
    public boolean isExplicit() {
        return true;
    }
}
