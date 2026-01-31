package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

/* loaded from: classes.dex */
public final class FlvExtractor implements Extractor, SeekMap {
    private static final int FLV_HEADER_SIZE = 9;
    private static final int FLV_TAG_HEADER_SIZE = 11;
    private static final int STATE_READING_FLV_HEADER = 1;
    private static final int STATE_READING_TAG_DATA = 4;
    private static final int STATE_READING_TAG_HEADER = 3;
    private static final int STATE_SKIPPING_TO_TAG_HEADER = 2;
    private static final int TAG_TYPE_AUDIO = 8;
    private static final int TAG_TYPE_SCRIPT_DATA = 18;
    private static final int TAG_TYPE_VIDEO = 9;
    private AudioTagPayloadReader audioReader;
    private int bytesToNextTagHeader;
    private ExtractorOutput extractorOutput;
    private ScriptTagPayloadReader metadataReader;
    public int tagDataSize;
    public long tagTimestampUs;
    public int tagType;
    private VideoTagPayloadReader videoReader;
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() { // from class: com.google.android.exoplayer2.extractor.flv.FlvExtractor.1
        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
        public Extractor[] createExtractors() {
            return new Extractor[]{new FlvExtractor()};
        }
    };
    private static final int FLV_TAG = Util.getIntegerCodeForString("FLV");
    private final ParsableByteArray scratch = new ParsableByteArray(4);
    private final ParsableByteArray headerBuffer = new ParsableByteArray(9);
    private final ParsableByteArray tagHeaderBuffer = new ParsableByteArray(11);
    private final ParsableByteArray tagData = new ParsableByteArray();
    private int parserState = 1;

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        input.peekFully(this.scratch.data, 0, 3);
        this.scratch.setPosition(0);
        if (this.scratch.readUnsignedInt24() != FLV_TAG) {
            return false;
        }
        input.peekFully(this.scratch.data, 0, 2);
        this.scratch.setPosition(0);
        if ((this.scratch.readUnsignedShort() & 250) != 0) {
            return false;
        }
        input.peekFully(this.scratch.data, 0, 4);
        this.scratch.setPosition(0);
        int dataOffset = this.scratch.readInt();
        input.resetPeekPosition();
        input.advancePeekPosition(dataOffset);
        input.peekFully(this.scratch.data, 0, 4);
        this.scratch.setPosition(0);
        return this.scratch.readInt() == 0;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long position, long timeUs) {
        this.parserState = 1;
        this.bytesToNextTagHeader = 0;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        while (true) {
            switch (this.parserState) {
                case 1:
                    if (!readFlvHeader(input)) {
                        return -1;
                    }
                    break;
                case 2:
                    skipToTagHeader(input);
                    break;
                case 3:
                    if (!readTagHeader(input)) {
                        return -1;
                    }
                    break;
                case 4:
                    if (!readTagData(input)) {
                        break;
                    } else {
                        return 0;
                    }
            }
        }
    }

    private boolean readFlvHeader(ExtractorInput input) throws InterruptedException, IOException {
        if (!input.readFully(this.headerBuffer.data, 0, 9, true)) {
            return false;
        }
        this.headerBuffer.setPosition(0);
        this.headerBuffer.skipBytes(4);
        int flags = this.headerBuffer.readUnsignedByte();
        boolean hasAudio = (flags & 4) != 0;
        boolean hasVideo = (flags & 1) != 0;
        if (hasAudio && this.audioReader == null) {
            this.audioReader = new AudioTagPayloadReader(this.extractorOutput.track(8, 1));
        }
        if (hasVideo && this.videoReader == null) {
            this.videoReader = new VideoTagPayloadReader(this.extractorOutput.track(9, 2));
        }
        if (this.metadataReader == null) {
            this.metadataReader = new ScriptTagPayloadReader(null);
        }
        this.extractorOutput.endTracks();
        this.extractorOutput.seekMap(this);
        this.bytesToNextTagHeader = (this.headerBuffer.readInt() - 9) + 4;
        this.parserState = 2;
        return true;
    }

    private void skipToTagHeader(ExtractorInput input) throws InterruptedException, IOException {
        input.skipFully(this.bytesToNextTagHeader);
        this.bytesToNextTagHeader = 0;
        this.parserState = 3;
    }

    private boolean readTagHeader(ExtractorInput input) throws InterruptedException, IOException {
        if (!input.readFully(this.tagHeaderBuffer.data, 0, 11, true)) {
            return false;
        }
        this.tagHeaderBuffer.setPosition(0);
        this.tagType = this.tagHeaderBuffer.readUnsignedByte();
        this.tagDataSize = this.tagHeaderBuffer.readUnsignedInt24();
        this.tagTimestampUs = this.tagHeaderBuffer.readUnsignedInt24();
        this.tagTimestampUs = ((this.tagHeaderBuffer.readUnsignedByte() << 24) | this.tagTimestampUs) * 1000;
        this.tagHeaderBuffer.skipBytes(3);
        this.parserState = 4;
        return true;
    }

    private boolean readTagData(ExtractorInput input) throws InterruptedException, IOException {
        boolean wasConsumed = true;
        if (this.tagType == 8 && this.audioReader != null) {
            this.audioReader.consume(prepareTagData(input), this.tagTimestampUs);
        } else if (this.tagType == 9 && this.videoReader != null) {
            this.videoReader.consume(prepareTagData(input), this.tagTimestampUs);
        } else if (this.tagType == 18 && this.metadataReader != null) {
            this.metadataReader.consume(prepareTagData(input), this.tagTimestampUs);
        } else {
            input.skipFully(this.tagDataSize);
            wasConsumed = false;
        }
        this.bytesToNextTagHeader = 4;
        this.parserState = 2;
        return wasConsumed;
    }

    private ParsableByteArray prepareTagData(ExtractorInput input) throws InterruptedException, IOException {
        if (this.tagDataSize > this.tagData.capacity()) {
            this.tagData.reset(new byte[Math.max(this.tagData.capacity() * 2, this.tagDataSize)], 0);
        } else {
            this.tagData.setPosition(0);
        }
        this.tagData.setLimit(this.tagDataSize);
        input.readFully(this.tagData.data, 0, this.tagDataSize);
        return this.tagData;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public boolean isSeekable() {
        return false;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public long getDurationUs() {
        return this.metadataReader.getDurationUs();
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public long getPosition(long timeUs) {
        return 0L;
    }
}
