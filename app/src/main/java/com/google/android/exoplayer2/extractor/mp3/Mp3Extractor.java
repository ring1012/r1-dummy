package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.EOFException;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public final class Mp3Extractor implements Extractor {
    public static final int FLAG_DISABLE_ID3_METADATA = 2;
    public static final int FLAG_ENABLE_CONSTANT_BITRATE_SEEKING = 1;
    private static final int HEADER_MASK = -128000;
    private static final int MAX_SNIFF_BYTES = 4096;
    private static final int MAX_SYNC_BYTES = 131072;
    private static final int SCRATCH_LENGTH = 10;
    private long basisTimeUs;
    private ExtractorOutput extractorOutput;
    private final int flags;
    private final long forcedFirstSampleTimestampUs;
    private final GaplessInfoHolder gaplessInfoHolder;
    private Metadata metadata;
    private int sampleBytesRemaining;
    private long samplesRead;
    private final ParsableByteArray scratch;
    private Seeker seeker;
    private final MpegAudioHeader synchronizedHeader;
    private int synchronizedHeaderData;
    private TrackOutput trackOutput;
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() { // from class: com.google.android.exoplayer2.extractor.mp3.Mp3Extractor.1
        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
        public Extractor[] createExtractors() {
            return new Extractor[]{new Mp3Extractor()};
        }
    };
    private static final int XING_HEADER = Util.getIntegerCodeForString("Xing");
    private static final int INFO_HEADER = Util.getIntegerCodeForString("Info");
    private static final int VBRI_HEADER = Util.getIntegerCodeForString("VBRI");

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    interface Seeker extends SeekMap {
        long getTimeUs(long j);
    }

    public Mp3Extractor() {
        this(0);
    }

    public Mp3Extractor(int flags) {
        this(flags, C.TIME_UNSET);
    }

    public Mp3Extractor(int flags, long forcedFirstSampleTimestampUs) {
        this.flags = flags;
        this.forcedFirstSampleTimestampUs = forcedFirstSampleTimestampUs;
        this.scratch = new ParsableByteArray(10);
        this.synchronizedHeader = new MpegAudioHeader();
        this.gaplessInfoHolder = new GaplessInfoHolder();
        this.basisTimeUs = C.TIME_UNSET;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        return synchronize(input, true);
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
        this.trackOutput = this.extractorOutput.track(0, 1);
        this.extractorOutput.endTracks();
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long position, long timeUs) {
        this.synchronizedHeaderData = 0;
        this.basisTimeUs = C.TIME_UNSET;
        this.samplesRead = 0L;
        this.sampleBytesRemaining = 0;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        if (this.synchronizedHeaderData == 0) {
            try {
                synchronize(input, false);
            } catch (EOFException e) {
                return -1;
            }
        }
        if (this.seeker == null) {
            this.seeker = setupSeeker(input);
            this.extractorOutput.seekMap(this.seeker);
            this.trackOutput.format(Format.createAudioSampleFormat(null, this.synchronizedHeader.mimeType, null, -1, 4096, this.synchronizedHeader.channels, this.synchronizedHeader.sampleRate, -1, this.gaplessInfoHolder.encoderDelay, this.gaplessInfoHolder.encoderPadding, null, null, 0, null, (this.flags & 2) != 0 ? null : this.metadata));
        }
        return readSample(input);
    }

    private int readSample(ExtractorInput extractorInput) throws InterruptedException, IOException {
        if (this.sampleBytesRemaining == 0) {
            extractorInput.resetPeekPosition();
            if (!extractorInput.peekFully(this.scratch.data, 0, 4, true)) {
                return -1;
            }
            this.scratch.setPosition(0);
            int sampleHeaderData = this.scratch.readInt();
            if ((HEADER_MASK & sampleHeaderData) != (this.synchronizedHeaderData & HEADER_MASK) || MpegAudioHeader.getFrameSize(sampleHeaderData) == -1) {
                extractorInput.skipFully(1);
                this.synchronizedHeaderData = 0;
                return 0;
            }
            MpegAudioHeader.populateHeader(sampleHeaderData, this.synchronizedHeader);
            if (this.basisTimeUs == C.TIME_UNSET) {
                this.basisTimeUs = this.seeker.getTimeUs(extractorInput.getPosition());
                if (this.forcedFirstSampleTimestampUs != C.TIME_UNSET) {
                    long embeddedFirstSampleTimestampUs = this.seeker.getTimeUs(0L);
                    this.basisTimeUs += this.forcedFirstSampleTimestampUs - embeddedFirstSampleTimestampUs;
                }
            }
            this.sampleBytesRemaining = this.synchronizedHeader.frameSize;
        }
        int bytesAppended = this.trackOutput.sampleData(extractorInput, this.sampleBytesRemaining, true);
        if (bytesAppended == -1) {
            return -1;
        }
        this.sampleBytesRemaining -= bytesAppended;
        if (this.sampleBytesRemaining > 0) {
            return 0;
        }
        long timeUs = this.basisTimeUs + ((this.samplesRead * C.MICROS_PER_SECOND) / this.synchronizedHeader.sampleRate);
        this.trackOutput.sampleMetadata(timeUs, 1, this.synchronizedHeader.frameSize, 0, null);
        this.samplesRead += this.synchronizedHeader.samplesPerFrame;
        this.sampleBytesRemaining = 0;
        return 0;
    }

    private boolean synchronize(ExtractorInput input, boolean sniffing) throws InterruptedException, IOException {
        int frameSize;
        int validFrameCount = 0;
        int candidateSynchronizedHeaderData = 0;
        int peekedId3Bytes = 0;
        int searchedBytes = 0;
        int searchLimitBytes = sniffing ? 4096 : 131072;
        input.resetPeekPosition();
        if (input.getPosition() == 0) {
            peekId3Data(input);
            peekedId3Bytes = (int) input.getPeekPosition();
            if (!sniffing) {
                input.skipFully(peekedId3Bytes);
            }
        }
        while (true) {
            if (!input.peekFully(this.scratch.data, 0, 4, validFrameCount > 0)) {
                break;
            }
            this.scratch.setPosition(0);
            int headerData = this.scratch.readInt();
            if ((candidateSynchronizedHeaderData != 0 && (HEADER_MASK & headerData) != (HEADER_MASK & candidateSynchronizedHeaderData)) || (frameSize = MpegAudioHeader.getFrameSize(headerData)) == -1) {
                int searchedBytes2 = searchedBytes + 1;
                if (searchedBytes == searchLimitBytes) {
                    if (!sniffing) {
                        throw new ParserException("Searched too many bytes.");
                    }
                    return false;
                }
                validFrameCount = 0;
                candidateSynchronizedHeaderData = 0;
                if (sniffing) {
                    input.resetPeekPosition();
                    input.advancePeekPosition(peekedId3Bytes + searchedBytes2);
                    searchedBytes = searchedBytes2;
                } else {
                    input.skipFully(1);
                    searchedBytes = searchedBytes2;
                }
            } else {
                validFrameCount++;
                if (validFrameCount == 1) {
                    MpegAudioHeader.populateHeader(headerData, this.synchronizedHeader);
                    candidateSynchronizedHeaderData = headerData;
                } else if (validFrameCount == 4) {
                    break;
                }
                input.advancePeekPosition(frameSize - 4);
            }
        }
        if (sniffing) {
            input.skipFully(peekedId3Bytes + searchedBytes);
        } else {
            input.resetPeekPosition();
        }
        this.synchronizedHeaderData = candidateSynchronizedHeaderData;
        return true;
    }

    private void peekId3Data(ExtractorInput input) throws InterruptedException, IOException {
        int peekedId3Bytes = 0;
        while (true) {
            input.peekFully(this.scratch.data, 0, 10);
            this.scratch.setPosition(0);
            if (this.scratch.readUnsignedInt24() == Id3Decoder.ID3_TAG) {
                this.scratch.skipBytes(3);
                int framesLength = this.scratch.readSynchSafeInt();
                int tagLength = framesLength + 10;
                if (this.metadata == null) {
                    byte[] id3Data = new byte[tagLength];
                    System.arraycopy(this.scratch.data, 0, id3Data, 0, 10);
                    input.peekFully(id3Data, 10, framesLength);
                    Id3Decoder.FramePredicate id3FramePredicate = (this.flags & 2) != 0 ? GaplessInfoHolder.GAPLESS_INFO_ID3_FRAME_PREDICATE : null;
                    this.metadata = new Id3Decoder(id3FramePredicate).decode(id3Data, tagLength);
                    if (this.metadata != null) {
                        this.gaplessInfoHolder.setFromMetadata(this.metadata);
                    }
                } else {
                    input.advancePeekPosition(framesLength);
                }
                peekedId3Bytes += tagLength;
            } else {
                input.resetPeekPosition();
                input.advancePeekPosition(peekedId3Bytes);
                return;
            }
        }
    }

    private Seeker setupSeeker(ExtractorInput input) throws InterruptedException, IOException {
        int xingBase = 21;
        ParsableByteArray frame = new ParsableByteArray(this.synchronizedHeader.frameSize);
        input.peekFully(frame.data, 0, this.synchronizedHeader.frameSize);
        long position = input.getPosition();
        long length = input.getLength();
        int headerData = 0;
        Seeker seeker = null;
        if ((this.synchronizedHeader.version & 1) != 0) {
            if (this.synchronizedHeader.channels != 1) {
                xingBase = 36;
            }
        } else if (this.synchronizedHeader.channels == 1) {
            xingBase = 13;
        }
        if (frame.limit() >= xingBase + 4) {
            frame.setPosition(xingBase);
            headerData = frame.readInt();
        }
        if (headerData == XING_HEADER || headerData == INFO_HEADER) {
            seeker = XingSeeker.create(this.synchronizedHeader, frame, position, length);
            if (seeker != null && !this.gaplessInfoHolder.hasGaplessInfo()) {
                input.resetPeekPosition();
                input.advancePeekPosition(xingBase + 141);
                input.peekFully(this.scratch.data, 0, 3);
                this.scratch.setPosition(0);
                this.gaplessInfoHolder.setFromXingHeaderValue(this.scratch.readUnsignedInt24());
            }
            input.skipFully(this.synchronizedHeader.frameSize);
        } else if (frame.limit() >= 40) {
            frame.setPosition(36);
            int headerData2 = frame.readInt();
            if (headerData2 == VBRI_HEADER) {
                seeker = VbriSeeker.create(this.synchronizedHeader, frame, position, length);
                input.skipFully(this.synchronizedHeader.frameSize);
            }
        }
        if (seeker == null || (!seeker.isSeekable() && (this.flags & 1) != 0)) {
            input.resetPeekPosition();
            input.peekFully(this.scratch.data, 0, 4);
            this.scratch.setPosition(0);
            MpegAudioHeader.populateHeader(this.scratch.readInt(), this.synchronizedHeader);
            return new ConstantBitrateSeeker(input.getPosition(), this.synchronizedHeader.bitrate, length);
        }
        return seeker;
    }
}
