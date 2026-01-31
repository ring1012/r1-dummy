package com.google.android.exoplayer2.decoder;

/* loaded from: classes.dex */
public final class DecoderCounters {
    public int decoderInitCount;
    public int decoderReleaseCount;
    public int droppedOutputBufferCount;
    public int inputBufferCount;
    public int maxConsecutiveDroppedOutputBufferCount;
    public int renderedOutputBufferCount;
    public int skippedOutputBufferCount;

    public synchronized void ensureUpdated() {
    }

    public void merge(DecoderCounters other) {
        this.decoderInitCount += other.decoderInitCount;
        this.decoderReleaseCount += other.decoderReleaseCount;
        this.inputBufferCount += other.inputBufferCount;
        this.renderedOutputBufferCount += other.renderedOutputBufferCount;
        this.skippedOutputBufferCount += other.skippedOutputBufferCount;
        this.droppedOutputBufferCount += other.droppedOutputBufferCount;
        this.maxConsecutiveDroppedOutputBufferCount = Math.max(this.maxConsecutiveDroppedOutputBufferCount, other.maxConsecutiveDroppedOutputBufferCount);
    }
}
