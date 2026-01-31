package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

/* loaded from: classes.dex */
public interface TrackOutput {
    void format(Format format);

    int sampleData(ExtractorInput extractorInput, int i, boolean z) throws InterruptedException, IOException;

    void sampleData(ParsableByteArray parsableByteArray, int i);

    void sampleMetadata(long j, int i, int i2, int i3, byte[] bArr);
}
