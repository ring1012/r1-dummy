package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;

/* loaded from: classes.dex */
public final class SpliceInfoSectionReader implements SectionPayloadReader {
    private boolean formatDeclared;
    private TrackOutput output;
    private TimestampAdjuster timestampAdjuster;

    @Override // com.google.android.exoplayer2.extractor.ts.SectionPayloadReader
    public void init(TimestampAdjuster timestampAdjuster, ExtractorOutput extractorOutput, TsPayloadReader.TrackIdGenerator idGenerator) {
        this.timestampAdjuster = timestampAdjuster;
        idGenerator.generateNewId();
        this.output = extractorOutput.track(idGenerator.getTrackId(), 4);
        this.output.format(Format.createSampleFormat(idGenerator.getFormatId(), MimeTypes.APPLICATION_SCTE35, null, -1, null));
    }

    @Override // com.google.android.exoplayer2.extractor.ts.SectionPayloadReader
    public void consume(ParsableByteArray sectionData) {
        if (!this.formatDeclared) {
            if (this.timestampAdjuster.getTimestampOffsetUs() != C.TIME_UNSET) {
                this.output.format(Format.createSampleFormat(null, MimeTypes.APPLICATION_SCTE35, this.timestampAdjuster.getTimestampOffsetUs()));
                this.formatDeclared = true;
            } else {
                return;
            }
        }
        int sampleSize = sectionData.bytesLeft();
        this.output.sampleData(sectionData, sampleSize);
        this.output.sampleMetadata(this.timestampAdjuster.getLastAdjustedTimestampUs(), 1, sampleSize, 0, null);
    }
}
