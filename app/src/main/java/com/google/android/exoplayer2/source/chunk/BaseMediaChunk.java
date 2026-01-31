package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;

/* loaded from: classes.dex */
public abstract class BaseMediaChunk extends MediaChunk {
    private int[] firstSampleIndices;
    private BaseMediaChunkOutput output;

    public BaseMediaChunk(DataSource dataSource, DataSpec dataSpec, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long startTimeUs, long endTimeUs, int chunkIndex) {
        super(dataSource, dataSpec, trackFormat, trackSelectionReason, trackSelectionData, startTimeUs, endTimeUs, chunkIndex);
    }

    public void init(BaseMediaChunkOutput output) {
        this.output = output;
        this.firstSampleIndices = output.getWriteIndices();
    }

    public final int getFirstSampleIndex(int trackIndex) {
        return this.firstSampleIndices[trackIndex];
    }

    protected final BaseMediaChunkOutput getOutput() {
        return this.output;
    }
}
