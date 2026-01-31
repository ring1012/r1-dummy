package com.google.android.exoplayer2.source.chunk;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.Util;
import com.unisound.vui.priority.PriorityMap;
import java.io.IOException;

/* loaded from: classes.dex */
public final class SingleSampleMediaChunk extends BaseMediaChunk {
    private volatile int bytesLoaded;
    private volatile boolean loadCanceled;
    private volatile boolean loadCompleted;
    private final Format sampleFormat;
    private final int trackType;

    public SingleSampleMediaChunk(DataSource dataSource, DataSpec dataSpec, Format trackFormat, int trackSelectionReason, Object trackSelectionData, long startTimeUs, long endTimeUs, int chunkIndex, int trackType, Format sampleFormat) {
        super(dataSource, dataSpec, trackFormat, trackSelectionReason, trackSelectionData, startTimeUs, endTimeUs, chunkIndex);
        this.trackType = trackType;
        this.sampleFormat = sampleFormat;
    }

    @Override // com.google.android.exoplayer2.source.chunk.MediaChunk
    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }

    @Override // com.google.android.exoplayer2.source.chunk.Chunk
    public long bytesLoaded() {
        return this.bytesLoaded;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public void cancelLoad() {
        this.loadCanceled = true;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public void load() throws InterruptedException, IOException {
        DataSpec loadDataSpec = Util.getRemainderDataSpec(this.dataSpec, this.bytesLoaded);
        try {
            long length = this.dataSource.open(loadDataSpec);
            if (length != -1) {
                length += this.bytesLoaded;
            }
            ExtractorInput extractorInput = new DefaultExtractorInput(this.dataSource, this.bytesLoaded, length);
            BaseMediaChunkOutput output = getOutput();
            output.setSampleOffsetUs(0L);
            TrackOutput trackOutput = output.track(0, this.trackType);
            trackOutput.format(this.sampleFormat);
            for (int result = 0; result != -1; result = trackOutput.sampleData(extractorInput, PriorityMap.PRIORITY_MAX, true)) {
                this.bytesLoaded += result;
            }
            int sampleSize = this.bytesLoaded;
            trackOutput.sampleMetadata(this.startTimeUs, 1, sampleSize, 0, null);
            Util.closeQuietly(this.dataSource);
            this.loadCompleted = true;
        } catch (Throwable th) {
            Util.closeQuietly(this.dataSource);
            throw th;
        }
    }
}
