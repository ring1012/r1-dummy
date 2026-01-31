package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.EOFException;
import java.io.IOException;

/* loaded from: classes.dex */
public final class DummyTrackOutput implements TrackOutput {
    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public void format(Format format) {
    }

    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public int sampleData(ExtractorInput input, int length, boolean allowEndOfInput) throws InterruptedException, IOException {
        int bytesSkipped = input.skip(length);
        if (bytesSkipped == -1) {
            if (allowEndOfInput) {
                return -1;
            }
            throw new EOFException();
        }
        return bytesSkipped;
    }

    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public void sampleData(ParsableByteArray data, int length) {
        data.skipBytes(length);
    }

    @Override // com.google.android.exoplayer2.extractor.TrackOutput
    public void sampleMetadata(long timeUs, int flags, int size, int offset, byte[] encryptionKey) {
    }
}
