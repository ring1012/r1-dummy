package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
public final class SectionReader implements TsPayloadReader {
    private static final int DEFAULT_SECTION_BUFFER_LENGTH = 32;
    private static final int MAX_SECTION_LENGTH = 4098;
    private static final int SECTION_HEADER_LENGTH = 3;
    private int bytesRead;
    private final SectionPayloadReader reader;
    private final ParsableByteArray sectionData = new ParsableByteArray(32);
    private boolean sectionSyntaxIndicator;
    private int totalSectionLength;
    private boolean waitingForPayloadStart;

    public SectionReader(SectionPayloadReader reader) {
        this.reader = reader;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.TsPayloadReader
    public void init(TimestampAdjuster timestampAdjuster, ExtractorOutput extractorOutput, TsPayloadReader.TrackIdGenerator idGenerator) {
        this.reader.init(timestampAdjuster, extractorOutput, idGenerator);
        this.waitingForPayloadStart = true;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.TsPayloadReader
    public void seek() {
        this.waitingForPayloadStart = true;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.TsPayloadReader
    public void consume(ParsableByteArray data, boolean payloadUnitStartIndicator) {
        int payloadStartPosition = -1;
        if (payloadUnitStartIndicator) {
            int payloadStartOffset = data.readUnsignedByte();
            payloadStartPosition = data.getPosition() + payloadStartOffset;
        }
        if (this.waitingForPayloadStart) {
            if (payloadUnitStartIndicator) {
                this.waitingForPayloadStart = false;
                data.setPosition(payloadStartPosition);
                this.bytesRead = 0;
            } else {
                return;
            }
        }
        while (data.bytesLeft() > 0) {
            if (this.bytesRead < 3) {
                if (this.bytesRead == 0) {
                    int tableId = data.readUnsignedByte();
                    data.setPosition(data.getPosition() - 1);
                    if (tableId == 255) {
                        this.waitingForPayloadStart = true;
                        return;
                    }
                }
                int headerBytesToRead = Math.min(data.bytesLeft(), 3 - this.bytesRead);
                data.readBytes(this.sectionData.data, this.bytesRead, headerBytesToRead);
                this.bytesRead += headerBytesToRead;
                if (this.bytesRead == 3) {
                    this.sectionData.reset(3);
                    this.sectionData.skipBytes(1);
                    int secondHeaderByte = this.sectionData.readUnsignedByte();
                    int thirdHeaderByte = this.sectionData.readUnsignedByte();
                    this.sectionSyntaxIndicator = (secondHeaderByte & 128) != 0;
                    this.totalSectionLength = (((secondHeaderByte & 15) << 8) | thirdHeaderByte) + 3;
                    if (this.sectionData.capacity() < this.totalSectionLength) {
                        byte[] bytes = this.sectionData.data;
                        this.sectionData.reset(Math.min(4098, Math.max(this.totalSectionLength, bytes.length * 2)));
                        System.arraycopy(bytes, 0, this.sectionData.data, 0, 3);
                    }
                }
            } else {
                int bodyBytesToRead = Math.min(data.bytesLeft(), this.totalSectionLength - this.bytesRead);
                data.readBytes(this.sectionData.data, this.bytesRead, bodyBytesToRead);
                this.bytesRead += bodyBytesToRead;
                if (this.bytesRead != this.totalSectionLength) {
                    continue;
                } else {
                    if (this.sectionSyntaxIndicator) {
                        if (Util.crc(this.sectionData.data, 0, this.totalSectionLength, -1) != 0) {
                            this.waitingForPayloadStart = true;
                            return;
                        }
                        this.sectionData.reset(this.totalSectionLength - 4);
                    } else {
                        this.sectionData.reset(this.totalSectionLength);
                    }
                    this.reader.consume(this.sectionData);
                    this.bytesRead = 0;
                }
            }
        }
    }
}
