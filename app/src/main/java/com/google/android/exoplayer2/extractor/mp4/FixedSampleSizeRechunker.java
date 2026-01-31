package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
final class FixedSampleSizeRechunker {
    private static final int MAX_SAMPLE_SIZE = 8192;

    FixedSampleSizeRechunker() {
    }

    public static final class Results {
        public final int[] flags;
        public final int maximumSize;
        public final long[] offsets;
        public final int[] sizes;
        public final long[] timestamps;

        private Results(long[] offsets, int[] sizes, int maximumSize, long[] timestamps, int[] flags) {
            this.offsets = offsets;
            this.sizes = sizes;
            this.maximumSize = maximumSize;
            this.timestamps = timestamps;
            this.flags = flags;
        }
    }

    public static Results rechunk(int fixedSampleSize, long[] chunkOffsets, int[] chunkSampleCounts, long timestampDeltaInTimeUnits) {
        int maxSampleCount = 8192 / fixedSampleSize;
        int rechunkedSampleCount = 0;
        for (int chunkSampleCount : chunkSampleCounts) {
            rechunkedSampleCount += Util.ceilDivide(chunkSampleCount, maxSampleCount);
        }
        long[] offsets = new long[rechunkedSampleCount];
        int[] sizes = new int[rechunkedSampleCount];
        int maximumSize = 0;
        long[] timestamps = new long[rechunkedSampleCount];
        int[] flags = new int[rechunkedSampleCount];
        int originalSampleIndex = 0;
        int newSampleIndex = 0;
        for (int chunkIndex = 0; chunkIndex < chunkSampleCounts.length; chunkIndex++) {
            int chunkSamplesRemaining = chunkSampleCounts[chunkIndex];
            long sampleOffset = chunkOffsets[chunkIndex];
            while (chunkSamplesRemaining > 0) {
                int bufferSampleCount = Math.min(maxSampleCount, chunkSamplesRemaining);
                offsets[newSampleIndex] = sampleOffset;
                sizes[newSampleIndex] = fixedSampleSize * bufferSampleCount;
                maximumSize = Math.max(maximumSize, sizes[newSampleIndex]);
                timestamps[newSampleIndex] = originalSampleIndex * timestampDeltaInTimeUnits;
                flags[newSampleIndex] = 1;
                sampleOffset += sizes[newSampleIndex];
                originalSampleIndex += bufferSampleCount;
                chunkSamplesRemaining -= bufferSampleCount;
                newSampleIndex++;
            }
        }
        return new Results(offsets, sizes, maximumSize, timestamps, flags);
    }
}
