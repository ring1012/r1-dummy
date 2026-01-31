package com.google.android.exoplayer2.extractor;

import java.io.IOException;

/* loaded from: classes.dex */
public interface Extractor {
    public static final int RESULT_CONTINUE = 0;
    public static final int RESULT_END_OF_INPUT = -1;
    public static final int RESULT_SEEK = 1;

    void init(ExtractorOutput extractorOutput);

    int read(ExtractorInput extractorInput, PositionHolder positionHolder) throws InterruptedException, IOException;

    void release();

    void seek(long j, long j2);

    boolean sniff(ExtractorInput extractorInput) throws InterruptedException, IOException;
}
