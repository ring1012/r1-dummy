package com.google.android.exoplayer2.extractor.ts;

import android.util.Log;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;

/* loaded from: classes.dex */
public final class Id3Reader implements ElementaryStreamReader {
    private static final int ID3_HEADER_SIZE = 10;
    private static final String TAG = "Id3Reader";
    private final ParsableByteArray id3Header = new ParsableByteArray(10);
    private TrackOutput output;
    private int sampleBytesRead;
    private int sampleSize;
    private long sampleTimeUs;
    private boolean writingSample;

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void seek() {
        this.writingSample = false;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput extractorOutput, TsPayloadReader.TrackIdGenerator idGenerator) {
        idGenerator.generateNewId();
        this.output = extractorOutput.track(idGenerator.getTrackId(), 4);
        this.output.format(Format.createSampleFormat(idGenerator.getFormatId(), MimeTypes.APPLICATION_ID3, null, -1, null));
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void packetStarted(long pesTimeUs, boolean dataAlignmentIndicator) {
        if (dataAlignmentIndicator) {
            this.writingSample = true;
            this.sampleTimeUs = pesTimeUs;
            this.sampleSize = 0;
            this.sampleBytesRead = 0;
        }
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void consume(ParsableByteArray data) {
        if (this.writingSample) {
            int bytesAvailable = data.bytesLeft();
            if (this.sampleBytesRead < 10) {
                int headerBytesAvailable = Math.min(bytesAvailable, 10 - this.sampleBytesRead);
                System.arraycopy(data.data, data.getPosition(), this.id3Header.data, this.sampleBytesRead, headerBytesAvailable);
                if (this.sampleBytesRead + headerBytesAvailable == 10) {
                    this.id3Header.setPosition(0);
                    if (73 != this.id3Header.readUnsignedByte() || 68 != this.id3Header.readUnsignedByte() || 51 != this.id3Header.readUnsignedByte()) {
                        Log.w(TAG, "Discarding invalid ID3 tag");
                        this.writingSample = false;
                        return;
                    } else {
                        this.id3Header.skipBytes(3);
                        this.sampleSize = this.id3Header.readSynchSafeInt() + 10;
                    }
                }
            }
            int bytesToWrite = Math.min(bytesAvailable, this.sampleSize - this.sampleBytesRead);
            this.output.sampleData(data, bytesToWrite);
            this.sampleBytesRead += bytesToWrite;
        }
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void packetFinished() {
        if (this.writingSample && this.sampleSize != 0 && this.sampleBytesRead == this.sampleSize) {
            this.output.sampleMetadata(this.sampleTimeUs, 1, this.sampleSize, 0, null);
            this.writingSample = false;
        }
    }
}
