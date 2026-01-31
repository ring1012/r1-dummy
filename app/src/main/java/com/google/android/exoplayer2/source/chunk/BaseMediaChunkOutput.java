package com.google.android.exoplayer2.source.chunk;

import android.util.Log;
import com.google.android.exoplayer2.extractor.DefaultTrackOutput;
import com.google.android.exoplayer2.extractor.DummyTrackOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper;

/* loaded from: classes.dex */
final class BaseMediaChunkOutput implements ChunkExtractorWrapper.TrackOutputProvider {
    private static final String TAG = "BaseMediaChunkOutput";
    private final DefaultTrackOutput[] trackOutputs;
    private final int[] trackTypes;

    public BaseMediaChunkOutput(int[] trackTypes, DefaultTrackOutput[] trackOutputs) {
        this.trackTypes = trackTypes;
        this.trackOutputs = trackOutputs;
    }

    @Override // com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper.TrackOutputProvider
    public TrackOutput track(int id, int type) {
        for (int i = 0; i < this.trackTypes.length; i++) {
            if (type == this.trackTypes[i]) {
                return this.trackOutputs[i];
            }
        }
        Log.e(TAG, "Unmatched track of type: " + type);
        return new DummyTrackOutput();
    }

    public int[] getWriteIndices() {
        int[] writeIndices = new int[this.trackOutputs.length];
        for (int i = 0; i < this.trackOutputs.length; i++) {
            if (this.trackOutputs[i] != null) {
                writeIndices[i] = this.trackOutputs[i].getWriteIndex();
            }
        }
        return writeIndices;
    }

    public void setSampleOffsetUs(long sampleOffsetUs) {
        for (DefaultTrackOutput trackOutput : this.trackOutputs) {
            if (trackOutput != null) {
                trackOutput.setSampleOffsetUs(sampleOffsetUs);
            }
        }
    }
}
