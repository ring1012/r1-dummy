package com.google.android.exoplayer2.extractor.mp4;

/* loaded from: classes.dex */
final class DefaultSampleValues {
    public final int duration;
    public final int flags;
    public final int sampleDescriptionIndex;
    public final int size;

    public DefaultSampleValues(int sampleDescriptionIndex, int duration, int size, int flags) {
        this.sampleDescriptionIndex = sampleDescriptionIndex;
        this.duration = duration;
        this.size = size;
        this.flags = flags;
    }
}
