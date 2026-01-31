package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
public final class ChunkIndex implements SeekMap {
    private final long durationUs;
    public final long[] durationsUs;
    public final int length;
    public final long[] offsets;
    public final int[] sizes;
    public final long[] timesUs;

    public ChunkIndex(int[] sizes, long[] offsets, long[] durationsUs, long[] timesUs) {
        this.sizes = sizes;
        this.offsets = offsets;
        this.durationsUs = durationsUs;
        this.timesUs = timesUs;
        this.length = sizes.length;
        if (this.length > 0) {
            this.durationUs = durationsUs[this.length - 1] + timesUs[this.length - 1];
        } else {
            this.durationUs = 0L;
        }
    }

    public int getChunkIndex(long timeUs) {
        return Util.binarySearchFloor(this.timesUs, timeUs, true, true);
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public boolean isSeekable() {
        return true;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public long getDurationUs() {
        return this.durationUs;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public long getPosition(long timeUs) {
        return this.offsets[getChunkIndex(timeUs)];
    }
}
