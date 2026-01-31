package com.google.android.exoplayer2.extractor.ts;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

/* loaded from: classes.dex */
public final class AdtsExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() { // from class: com.google.android.exoplayer2.extractor.ts.AdtsExtractor.1
        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
        public Extractor[] createExtractors() {
            return new Extractor[]{new AdtsExtractor()};
        }
    };
    private static final int ID3_TAG = Util.getIntegerCodeForString("ID3");
    private static final int MAX_PACKET_SIZE = 200;
    private static final int MAX_SNIFF_BYTES = 8192;
    private final long firstSampleTimestampUs;
    private final ParsableByteArray packetBuffer;
    private AdtsReader reader;
    private boolean startedPacket;

    public AdtsExtractor() {
        this(0L);
    }

    public AdtsExtractor(long firstSampleTimestampUs) {
        this.firstSampleTimestampUs = firstSampleTimestampUs;
        this.packetBuffer = new ParsableByteArray(200);
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        ParsableByteArray scratch = new ParsableByteArray(10);
        ParsableBitArray scratchBits = new ParsableBitArray(scratch.data);
        int startPosition = 0;
        while (true) {
            input.peekFully(scratch.data, 0, 10);
            scratch.setPosition(0);
            if (scratch.readUnsignedInt24() != ID3_TAG) {
                break;
            }
            scratch.skipBytes(3);
            int length = scratch.readSynchSafeInt();
            startPosition += length + 10;
            input.advancePeekPosition(length);
        }
        input.resetPeekPosition();
        input.advancePeekPosition(startPosition);
        int headerPosition = startPosition;
        int validFramesSize = 0;
        int validFramesCount = 0;
        while (true) {
            input.peekFully(scratch.data, 0, 2);
            scratch.setPosition(0);
            int syncBytes = scratch.readUnsignedShort();
            if ((65526 & syncBytes) != 65520) {
                validFramesCount = 0;
                validFramesSize = 0;
                input.resetPeekPosition();
                headerPosition++;
                if (headerPosition - startPosition >= 8192) {
                    return false;
                }
                input.advancePeekPosition(headerPosition);
            } else {
                validFramesCount++;
                if (validFramesCount >= 4 && validFramesSize > 188) {
                    return true;
                }
                input.peekFully(scratch.data, 0, 4);
                scratchBits.setPosition(14);
                int frameSize = scratchBits.readBits(13);
                if (frameSize <= 6) {
                    return false;
                }
                input.advancePeekPosition(frameSize - 6);
                validFramesSize += frameSize;
            }
        }
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.reader = new AdtsReader(true);
        this.reader.createTracks(output, new TsPayloadReader.TrackIdGenerator(0, 1));
        output.endTracks();
        output.seekMap(new SeekMap.Unseekable(C.TIME_UNSET));
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long position, long timeUs) {
        this.startedPacket = false;
        this.reader.seek();
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        int bytesRead = input.read(this.packetBuffer.data, 0, 200);
        if (bytesRead == -1) {
            return -1;
        }
        this.packetBuffer.setPosition(0);
        this.packetBuffer.setLimit(bytesRead);
        if (!this.startedPacket) {
            this.reader.packetStarted(this.firstSampleTimestampUs, true);
            this.startedPacket = true;
        }
        this.reader.consume(this.packetBuffer);
        return 0;
    }
}
