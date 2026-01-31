package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Assertions;

/* loaded from: classes.dex */
public abstract class MediaChunk extends Chunk {
    public final int chunkIndex;

    public abstract boolean isLoadCompleted();

    public MediaChunk(DataSource dataSource, DataSpec dataSpec, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long startTimeUs, long endTimeUs, int chunkIndex) {
        super(dataSource, dataSpec, 1, trackFormat, trackSelectionReason, trackSelectionData, startTimeUs, endTimeUs);
        Assertions.checkNotNull(trackFormat);
        this.chunkIndex = chunkIndex;
    }

    public int getNextChunkIndex() {
        return this.chunkIndex + 1;
    }
}
