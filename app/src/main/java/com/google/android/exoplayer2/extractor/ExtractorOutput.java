package com.google.android.exoplayer2.extractor;

/* loaded from: classes.dex */
public interface ExtractorOutput {
    void endTracks();

    void seekMap(SeekMap seekMap);

    TrackOutput track(int i, int i2);
}
