package com.google.android.exoplayer2.extractor.rawcc;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

/* loaded from: classes.dex */
public final class RawCcExtractor implements Extractor {
    private static final int HEADER_ID = Util.getIntegerCodeForString("RCC\u0001");
    private static final int HEADER_SIZE = 8;
    private static final int SCRATCH_SIZE = 9;
    private static final int STATE_READING_HEADER = 0;
    private static final int STATE_READING_SAMPLES = 2;
    private static final int STATE_READING_TIMESTAMP_AND_COUNT = 1;
    private static final int TIMESTAMP_SIZE_V0 = 4;
    private static final int TIMESTAMP_SIZE_V1 = 8;
    private final Format format;
    private int remainingSampleCount;
    private int sampleBytesWritten;
    private long timestampUs;
    private TrackOutput trackOutput;
    private int version;
    private final ParsableByteArray dataScratch = new ParsableByteArray(9);
    private int parserState = 0;

    public RawCcExtractor(Format format) {
        this.format = format;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput output) {
        output.seekMap(new SeekMap.Unseekable(C.TIME_UNSET));
        this.trackOutput = output.track(0, 3);
        output.endTracks();
        this.trackOutput.format(this.format);
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        this.dataScratch.reset();
        input.peekFully(this.dataScratch.data, 0, 8);
        return this.dataScratch.readInt() == HEADER_ID;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        while (true) {
            switch (this.parserState) {
                case 0:
                    if (!parseHeader(input)) {
                        return -1;
                    }
                    this.parserState = 1;
                    break;
                case 1:
                    if (parseTimestampAndSampleCount(input)) {
                        this.parserState = 2;
                        break;
                    } else {
                        this.parserState = 0;
                        return -1;
                    }
                case 2:
                    parseSamples(input);
                    this.parserState = 1;
                    return 0;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long position, long timeUs) {
        this.parserState = 0;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    private boolean parseHeader(ExtractorInput input) throws InterruptedException, IOException {
        this.dataScratch.reset();
        if (!input.readFully(this.dataScratch.data, 0, 8, true)) {
            return false;
        }
        if (this.dataScratch.readInt() != HEADER_ID) {
            throw new IOException("Input not RawCC");
        }
        this.version = this.dataScratch.readUnsignedByte();
        return true;
    }

    private boolean parseTimestampAndSampleCount(ExtractorInput input) throws InterruptedException, IOException {
        this.dataScratch.reset();
        if (this.version == 0) {
            if (!input.readFully(this.dataScratch.data, 0, 5, true)) {
                return false;
            }
            this.timestampUs = (this.dataScratch.readUnsignedInt() * 1000) / 45;
        } else if (this.version == 1) {
            if (!input.readFully(this.dataScratch.data, 0, 9, true)) {
                return false;
            }
            this.timestampUs = this.dataScratch.readLong();
        } else {
            throw new ParserException("Unsupported version number: " + this.version);
        }
        this.remainingSampleCount = this.dataScratch.readUnsignedByte();
        this.sampleBytesWritten = 0;
        return true;
    }

    private void parseSamples(ExtractorInput input) throws InterruptedException, IOException {
        while (this.remainingSampleCount > 0) {
            this.dataScratch.reset();
            input.readFully(this.dataScratch.data, 0, 3);
            this.trackOutput.sampleData(this.dataScratch, 3);
            this.sampleBytesWritten += 3;
            this.remainingSampleCount--;
        }
        if (this.sampleBytesWritten > 0) {
            this.trackOutput.sampleMetadata(this.timestampUs, 1, this.sampleBytesWritten, 0, null);
        }
    }
}
