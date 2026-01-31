package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

/* loaded from: classes.dex */
final class OggPacket {
    private boolean populated;
    private int segmentCount;
    private final OggPageHeader pageHeader = new OggPageHeader();
    private final ParsableByteArray packetArray = new ParsableByteArray(new byte[OggPageHeader.MAX_PAGE_PAYLOAD], 0);
    private int currentSegmentIndex = -1;

    OggPacket() {
    }

    public void reset() {
        this.pageHeader.reset();
        this.packetArray.reset();
        this.currentSegmentIndex = -1;
        this.populated = false;
    }

    public boolean populate(ExtractorInput input) throws InterruptedException, IOException {
        Assertions.checkState(input != null);
        if (this.populated) {
            this.populated = false;
            this.packetArray.reset();
        }
        while (!this.populated) {
            if (this.currentSegmentIndex < 0) {
                if (!this.pageHeader.populate(input, true)) {
                    return false;
                }
                int segmentIndex = 0;
                int bytesToSkip = this.pageHeader.headerSize;
                if ((this.pageHeader.type & 1) == 1 && this.packetArray.limit() == 0) {
                    bytesToSkip += calculatePacketSize(0);
                    segmentIndex = 0 + this.segmentCount;
                }
                input.skipFully(bytesToSkip);
                this.currentSegmentIndex = segmentIndex;
            }
            int size = calculatePacketSize(this.currentSegmentIndex);
            int segmentIndex2 = this.currentSegmentIndex + this.segmentCount;
            if (size > 0) {
                input.readFully(this.packetArray.data, this.packetArray.limit(), size);
                this.packetArray.setLimit(this.packetArray.limit() + size);
                this.populated = this.pageHeader.laces[segmentIndex2 + (-1)] != 255;
            }
            if (segmentIndex2 == this.pageHeader.pageSegmentCount) {
                segmentIndex2 = -1;
            }
            this.currentSegmentIndex = segmentIndex2;
        }
        return true;
    }

    public OggPageHeader getPageHeader() {
        return this.pageHeader;
    }

    public ParsableByteArray getPayload() {
        return this.packetArray;
    }

    private int calculatePacketSize(int startSegmentIndex) {
        this.segmentCount = 0;
        int size = 0;
        while (this.segmentCount + startSegmentIndex < this.pageHeader.pageSegmentCount) {
            int[] iArr = this.pageHeader.laces;
            int i = this.segmentCount;
            this.segmentCount = i + 1;
            int segmentLength = iArr[i + startSegmentIndex];
            size += segmentLength;
            if (segmentLength != 255) {
                break;
            }
        }
        return size;
    }
}
