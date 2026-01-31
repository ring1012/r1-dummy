package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;

/* loaded from: classes.dex */
final class OggPageHeader {
    public static final int EMPTY_PAGE_HEADER_SIZE = 27;
    public static final int MAX_PAGE_PAYLOAD = 65025;
    public static final int MAX_PAGE_SIZE = 65307;
    public static final int MAX_SEGMENT_COUNT = 255;
    private static final int TYPE_OGGS = Util.getIntegerCodeForString("OggS");
    public int bodySize;
    public long granulePosition;
    public int headerSize;
    public long pageChecksum;
    public int pageSegmentCount;
    public long pageSequenceNumber;
    public int revision;
    public long streamSerialNumber;
    public int type;
    public final int[] laces = new int[255];
    private final ParsableByteArray scratch = new ParsableByteArray(255);

    OggPageHeader() {
    }

    public void reset() {
        this.revision = 0;
        this.type = 0;
        this.granulePosition = 0L;
        this.streamSerialNumber = 0L;
        this.pageSequenceNumber = 0L;
        this.pageChecksum = 0L;
        this.pageSegmentCount = 0;
        this.headerSize = 0;
        this.bodySize = 0;
    }

    public boolean populate(ExtractorInput input, boolean quiet) throws InterruptedException, IOException {
        this.scratch.reset();
        reset();
        boolean hasEnoughBytes = input.getLength() == -1 || input.getLength() - input.getPeekPosition() >= 27;
        if (!hasEnoughBytes || !input.peekFully(this.scratch.data, 0, 27, true)) {
            if (quiet) {
                return false;
            }
            throw new EOFException();
        }
        if (this.scratch.readUnsignedInt() != TYPE_OGGS) {
            if (quiet) {
                return false;
            }
            throw new ParserException("expected OggS capture pattern at begin of page");
        }
        this.revision = this.scratch.readUnsignedByte();
        if (this.revision != 0) {
            if (quiet) {
                return false;
            }
            throw new ParserException("unsupported bit stream revision");
        }
        this.type = this.scratch.readUnsignedByte();
        this.granulePosition = this.scratch.readLittleEndianLong();
        this.streamSerialNumber = this.scratch.readLittleEndianUnsignedInt();
        this.pageSequenceNumber = this.scratch.readLittleEndianUnsignedInt();
        this.pageChecksum = this.scratch.readLittleEndianUnsignedInt();
        this.pageSegmentCount = this.scratch.readUnsignedByte();
        this.headerSize = this.pageSegmentCount + 27;
        this.scratch.reset();
        input.peekFully(this.scratch.data, 0, this.pageSegmentCount);
        for (int i = 0; i < this.pageSegmentCount; i++) {
            this.laces[i] = this.scratch.readUnsignedByte();
            this.bodySize += this.laces[i];
        }
        return true;
    }
}
