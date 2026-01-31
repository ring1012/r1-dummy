package com.google.android.exoplayer2.extractor.ts;

import android.util.Log;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;

/* loaded from: classes.dex */
public final class PesReader implements TsPayloadReader {
    private static final int HEADER_SIZE = 9;
    private static final int MAX_HEADER_EXTENSION_SIZE = 10;
    private static final int PES_SCRATCH_SIZE = 10;
    private static final int STATE_FINDING_HEADER = 0;
    private static final int STATE_READING_BODY = 3;
    private static final int STATE_READING_HEADER = 1;
    private static final int STATE_READING_HEADER_EXTENSION = 2;
    private static final String TAG = "PesReader";
    private int bytesRead;
    private boolean dataAlignmentIndicator;
    private boolean dtsFlag;
    private int extendedHeaderLength;
    private int payloadSize;
    private boolean ptsFlag;
    private final ElementaryStreamReader reader;
    private boolean seenFirstDts;
    private long timeUs;
    private TimestampAdjuster timestampAdjuster;
    private final ParsableBitArray pesScratch = new ParsableBitArray(new byte[10]);
    private int state = 0;

    public PesReader(ElementaryStreamReader reader) {
        this.reader = reader;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.TsPayloadReader
    public void init(TimestampAdjuster timestampAdjuster, ExtractorOutput extractorOutput, TsPayloadReader.TrackIdGenerator idGenerator) {
        this.timestampAdjuster = timestampAdjuster;
        this.reader.createTracks(extractorOutput, idGenerator);
    }

    @Override // com.google.android.exoplayer2.extractor.ts.TsPayloadReader
    public final void seek() {
        this.state = 0;
        this.bytesRead = 0;
        this.seenFirstDts = false;
        this.reader.seek();
    }

    @Override // com.google.android.exoplayer2.extractor.ts.TsPayloadReader
    public final void consume(ParsableByteArray data, boolean payloadUnitStartIndicator) {
        if (payloadUnitStartIndicator) {
            switch (this.state) {
                case 2:
                    Log.w(TAG, "Unexpected start indicator reading extended header");
                    break;
                case 3:
                    if (this.payloadSize != -1) {
                        Log.w(TAG, "Unexpected start indicator: expected " + this.payloadSize + " more bytes");
                    }
                    this.reader.packetFinished();
                    break;
            }
            setState(1);
        }
        while (data.bytesLeft() > 0) {
            switch (this.state) {
                case 0:
                    data.skipBytes(data.bytesLeft());
                    break;
                case 1:
                    if (!continueRead(data, this.pesScratch.data, 9)) {
                        break;
                    } else {
                        setState(parseHeader() ? 2 : 0);
                        break;
                    }
                case 2:
                    if (!continueRead(data, this.pesScratch.data, Math.min(10, this.extendedHeaderLength)) || !continueRead(data, null, this.extendedHeaderLength)) {
                        break;
                    } else {
                        parseHeaderExtension();
                        this.reader.packetStarted(this.timeUs, this.dataAlignmentIndicator);
                        setState(3);
                        break;
                    }
                    break;
                case 3:
                    int readLength = data.bytesLeft();
                    int padding = this.payloadSize == -1 ? 0 : readLength - this.payloadSize;
                    if (padding > 0) {
                        readLength -= padding;
                        data.setLimit(data.getPosition() + readLength);
                    }
                    this.reader.consume(data);
                    if (this.payloadSize == -1) {
                        break;
                    } else {
                        this.payloadSize -= readLength;
                        if (this.payloadSize != 0) {
                            break;
                        } else {
                            this.reader.packetFinished();
                            setState(1);
                            break;
                        }
                    }
            }
        }
    }

    private void setState(int state) {
        this.state = state;
        this.bytesRead = 0;
    }

    private boolean continueRead(ParsableByteArray source, byte[] target, int targetLength) {
        int bytesToRead = Math.min(source.bytesLeft(), targetLength - this.bytesRead);
        if (bytesToRead <= 0) {
            return true;
        }
        if (target == null) {
            source.skipBytes(bytesToRead);
        } else {
            source.readBytes(target, this.bytesRead, bytesToRead);
        }
        this.bytesRead += bytesToRead;
        return this.bytesRead == targetLength;
    }

    private boolean parseHeader() {
        this.pesScratch.setPosition(0);
        int startCodePrefix = this.pesScratch.readBits(24);
        if (startCodePrefix != 1) {
            Log.w(TAG, "Unexpected start code prefix: " + startCodePrefix);
            this.payloadSize = -1;
            return false;
        }
        this.pesScratch.skipBits(8);
        int packetLength = this.pesScratch.readBits(16);
        this.pesScratch.skipBits(5);
        this.dataAlignmentIndicator = this.pesScratch.readBit();
        this.pesScratch.skipBits(2);
        this.ptsFlag = this.pesScratch.readBit();
        this.dtsFlag = this.pesScratch.readBit();
        this.pesScratch.skipBits(6);
        this.extendedHeaderLength = this.pesScratch.readBits(8);
        if (packetLength == 0) {
            this.payloadSize = -1;
        } else {
            this.payloadSize = ((packetLength + 6) - 9) - this.extendedHeaderLength;
        }
        return true;
    }

    private void parseHeaderExtension() {
        this.pesScratch.setPosition(0);
        this.timeUs = C.TIME_UNSET;
        if (this.ptsFlag) {
            this.pesScratch.skipBits(4);
            long pts = this.pesScratch.readBits(3) << 30;
            this.pesScratch.skipBits(1);
            this.pesScratch.skipBits(1);
            long pts2 = pts | (this.pesScratch.readBits(15) << 15) | this.pesScratch.readBits(15);
            this.pesScratch.skipBits(1);
            if (!this.seenFirstDts && this.dtsFlag) {
                this.pesScratch.skipBits(4);
                long dts = this.pesScratch.readBits(3) << 30;
                this.pesScratch.skipBits(1);
                this.pesScratch.skipBits(1);
                this.pesScratch.skipBits(1);
                this.timestampAdjuster.adjustTsTimestamp(dts | (this.pesScratch.readBits(15) << 15) | this.pesScratch.readBits(15));
                this.seenFirstDts = true;
            }
            this.timeUs = this.timestampAdjuster.adjustTsTimestamp(pts2);
        }
    }
}
