package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.util.ParsableByteArray;

/* loaded from: classes.dex */
public final class MpegAudioReader implements ElementaryStreamReader {
    private static final int HEADER_SIZE = 4;
    private static final int STATE_FINDING_HEADER = 0;
    private static final int STATE_READING_FRAME = 2;
    private static final int STATE_READING_HEADER = 1;
    private String formatId;
    private int frameBytesRead;
    private long frameDurationUs;
    private int frameSize;
    private boolean hasOutputFormat;
    private final MpegAudioHeader header;
    private final ParsableByteArray headerScratch;
    private final String language;
    private boolean lastByteWasFF;
    private TrackOutput output;
    private int state;
    private long timeUs;

    public MpegAudioReader() {
        this(null);
    }

    public MpegAudioReader(String language) {
        this.state = 0;
        this.headerScratch = new ParsableByteArray(4);
        this.headerScratch.data[0] = -1;
        this.header = new MpegAudioHeader();
        this.language = language;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void seek() {
        this.state = 0;
        this.frameBytesRead = 0;
        this.lastByteWasFF = false;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput extractorOutput, TsPayloadReader.TrackIdGenerator idGenerator) {
        idGenerator.generateNewId();
        this.formatId = idGenerator.getFormatId();
        this.output = extractorOutput.track(idGenerator.getTrackId(), 1);
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void packetStarted(long pesTimeUs, boolean dataAlignmentIndicator) {
        this.timeUs = pesTimeUs;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void consume(ParsableByteArray data) {
        while (data.bytesLeft() > 0) {
            switch (this.state) {
                case 0:
                    findHeader(data);
                    break;
                case 1:
                    readHeaderRemainder(data);
                    break;
                case 2:
                    readFrameRemainder(data);
                    break;
            }
        }
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void packetFinished() {
    }

    private void findHeader(ParsableByteArray source) {
        byte[] data = source.data;
        int startOffset = source.getPosition();
        int endOffset = source.limit();
        for (int i = startOffset; i < endOffset; i++) {
            boolean byteIsFF = (data[i] & 255) == 255;
            boolean found = this.lastByteWasFF && (data[i] & 224) == 224;
            this.lastByteWasFF = byteIsFF;
            if (found) {
                source.setPosition(i + 1);
                this.lastByteWasFF = false;
                this.headerScratch.data[1] = data[i];
                this.frameBytesRead = 2;
                this.state = 1;
                return;
            }
        }
        source.setPosition(endOffset);
    }

    private void readHeaderRemainder(ParsableByteArray source) {
        int bytesToRead = Math.min(source.bytesLeft(), 4 - this.frameBytesRead);
        source.readBytes(this.headerScratch.data, this.frameBytesRead, bytesToRead);
        this.frameBytesRead += bytesToRead;
        if (this.frameBytesRead >= 4) {
            this.headerScratch.setPosition(0);
            boolean parsedHeader = MpegAudioHeader.populateHeader(this.headerScratch.readInt(), this.header);
            if (!parsedHeader) {
                this.frameBytesRead = 0;
                this.state = 1;
                return;
            }
            this.frameSize = this.header.frameSize;
            if (!this.hasOutputFormat) {
                this.frameDurationUs = (C.MICROS_PER_SECOND * this.header.samplesPerFrame) / this.header.sampleRate;
                Format format = Format.createAudioSampleFormat(this.formatId, this.header.mimeType, null, -1, 4096, this.header.channels, this.header.sampleRate, null, null, 0, this.language);
                this.output.format(format);
                this.hasOutputFormat = true;
            }
            this.headerScratch.setPosition(0);
            this.output.sampleData(this.headerScratch, 4);
            this.state = 2;
        }
    }

    private void readFrameRemainder(ParsableByteArray source) {
        int bytesToRead = Math.min(source.bytesLeft(), this.frameSize - this.frameBytesRead);
        this.output.sampleData(source, bytesToRead);
        this.frameBytesRead += bytesToRead;
        if (this.frameBytesRead >= this.frameSize) {
            this.output.sampleMetadata(this.timeUs, 1, this.frameSize, 0, null);
            this.timeUs += this.frameDurationUs;
            this.frameBytesRead = 0;
            this.state = 0;
        }
    }
}
