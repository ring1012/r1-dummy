package com.google.android.exoplayer2.extractor.mp4;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.mp4.Atom;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/* loaded from: classes.dex */
public final class Mp4Extractor implements Extractor, SeekMap {
    private static final long RELOAD_MINIMUM_SEEK_DISTANCE = 262144;
    private static final int STATE_READING_ATOM_HEADER = 0;
    private static final int STATE_READING_ATOM_PAYLOAD = 1;
    private static final int STATE_READING_SAMPLE = 2;
    private ParsableByteArray atomData;
    private int atomHeaderBytesRead;
    private long atomSize;
    private int atomType;
    private long durationUs;
    private ExtractorOutput extractorOutput;
    private boolean isQuickTime;
    private int parserState;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private Mp4Track[] tracks;
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() { // from class: com.google.android.exoplayer2.extractor.mp4.Mp4Extractor.1
        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
        public Extractor[] createExtractors() {
            return new Extractor[]{new Mp4Extractor()};
        }
    };
    private static final int BRAND_QUICKTIME = Util.getIntegerCodeForString("qt  ");
    private final ParsableByteArray atomHeader = new ParsableByteArray(16);
    private final Stack<Atom.ContainerAtom> containerAtoms = new Stack<>();
    private final ParsableByteArray nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
    private final ParsableByteArray nalLength = new ParsableByteArray(4);

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        return Sniffer.sniffUnfragmented(input);
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long position, long timeUs) {
        this.containerAtoms.clear();
        this.atomHeaderBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        if (position == 0) {
            enterReadingAtomHeaderState();
        } else if (this.tracks != null) {
            updateSampleIndices(timeUs);
        }
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        while (true) {
            switch (this.parserState) {
                case 0:
                    if (!readAtomHeader(input)) {
                        return -1;
                    }
                    break;
                case 1:
                    if (!readAtomPayload(input, seekPosition)) {
                        break;
                    } else {
                        return 1;
                    }
                case 2:
                    return readSample(input, seekPosition);
                default:
                    throw new IllegalStateException();
            }
        }
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public boolean isSeekable() {
        return true;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public long getDurationUs() {
        return this.durationUs;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public long getPosition(long timeUs) {
        long earliestSamplePosition = Long.MAX_VALUE;
        for (Mp4Track track : this.tracks) {
            TrackSampleTable sampleTable = track.sampleTable;
            int sampleIndex = sampleTable.getIndexOfEarlierOrEqualSynchronizationSample(timeUs);
            if (sampleIndex == -1) {
                sampleIndex = sampleTable.getIndexOfLaterOrEqualSynchronizationSample(timeUs);
            }
            long offset = sampleTable.offsets[sampleIndex];
            if (offset < earliestSamplePosition) {
                earliestSamplePosition = offset;
            }
        }
        return earliestSamplePosition;
    }

    private void enterReadingAtomHeaderState() {
        this.parserState = 0;
        this.atomHeaderBytesRead = 0;
    }

    private boolean readAtomHeader(ExtractorInput input) throws InterruptedException, IOException {
        if (this.atomHeaderBytesRead == 0) {
            if (!input.readFully(this.atomHeader.data, 0, 8, true)) {
                return false;
            }
            this.atomHeaderBytesRead = 8;
            this.atomHeader.setPosition(0);
            this.atomSize = this.atomHeader.readUnsignedInt();
            this.atomType = this.atomHeader.readInt();
        }
        if (this.atomSize == 1) {
            input.readFully(this.atomHeader.data, 8, 8);
            this.atomHeaderBytesRead += 8;
            this.atomSize = this.atomHeader.readUnsignedLongToLong();
        }
        if (shouldParseContainerAtom(this.atomType)) {
            long endPosition = (input.getPosition() + this.atomSize) - this.atomHeaderBytesRead;
            this.containerAtoms.add(new Atom.ContainerAtom(this.atomType, endPosition));
            if (this.atomSize == this.atomHeaderBytesRead) {
                processAtomEnded(endPosition);
            } else {
                enterReadingAtomHeaderState();
            }
        } else if (shouldParseLeafAtom(this.atomType)) {
            Assertions.checkState(this.atomHeaderBytesRead == 8);
            Assertions.checkState(this.atomSize <= 2147483647L);
            this.atomData = new ParsableByteArray((int) this.atomSize);
            System.arraycopy(this.atomHeader.data, 0, this.atomData.data, 0, 8);
            this.parserState = 1;
        } else {
            this.atomData = null;
            this.parserState = 1;
        }
        return true;
    }

    private boolean readAtomPayload(ExtractorInput input, PositionHolder positionHolder) throws InterruptedException, IOException {
        long atomPayloadSize = this.atomSize - this.atomHeaderBytesRead;
        long atomEndPosition = input.getPosition() + atomPayloadSize;
        boolean seekRequired = false;
        if (this.atomData != null) {
            input.readFully(this.atomData.data, this.atomHeaderBytesRead, (int) atomPayloadSize);
            if (this.atomType == Atom.TYPE_ftyp) {
                this.isQuickTime = processFtypAtom(this.atomData);
            } else if (!this.containerAtoms.isEmpty()) {
                this.containerAtoms.peek().add(new Atom.LeafAtom(this.atomType, this.atomData));
            }
        } else if (atomPayloadSize < RELOAD_MINIMUM_SEEK_DISTANCE) {
            input.skipFully((int) atomPayloadSize);
        } else {
            positionHolder.position = input.getPosition() + atomPayloadSize;
            seekRequired = true;
        }
        processAtomEnded(atomEndPosition);
        return seekRequired && this.parserState != 2;
    }

    private void processAtomEnded(long atomEndPosition) throws ParserException {
        while (!this.containerAtoms.isEmpty() && this.containerAtoms.peek().endPosition == atomEndPosition) {
            Atom.ContainerAtom containerAtom = this.containerAtoms.pop();
            if (containerAtom.type == Atom.TYPE_moov) {
                processMoovAtom(containerAtom);
                this.containerAtoms.clear();
                this.parserState = 2;
            } else if (!this.containerAtoms.isEmpty()) {
                this.containerAtoms.peek().add(containerAtom);
            }
        }
        if (this.parserState != 2) {
            enterReadingAtomHeaderState();
        }
    }

    private static boolean processFtypAtom(ParsableByteArray atomData) {
        atomData.setPosition(8);
        int majorBrand = atomData.readInt();
        if (majorBrand == BRAND_QUICKTIME) {
            return true;
        }
        atomData.skipBytes(4);
        while (atomData.bytesLeft() > 0) {
            if (atomData.readInt() == BRAND_QUICKTIME) {
                return true;
            }
        }
        return false;
    }

    private void processMoovAtom(Atom.ContainerAtom moov) throws ParserException {
        Track track;
        long durationUs = C.TIME_UNSET;
        List<Mp4Track> tracks = new ArrayList<>();
        long earliestSampleOffset = Long.MAX_VALUE;
        Metadata metadata = null;
        GaplessInfoHolder gaplessInfoHolder = new GaplessInfoHolder();
        Atom.LeafAtom udta = moov.getLeafAtomOfType(Atom.TYPE_udta);
        if (udta != null && (metadata = AtomParsers.parseUdta(udta, this.isQuickTime)) != null) {
            gaplessInfoHolder.setFromMetadata(metadata);
        }
        for (int i = 0; i < moov.containerChildren.size(); i++) {
            Atom.ContainerAtom atom = moov.containerChildren.get(i);
            if (atom.type == Atom.TYPE_trak && (track = AtomParsers.parseTrak(atom, moov.getLeafAtomOfType(Atom.TYPE_mvhd), C.TIME_UNSET, null, this.isQuickTime)) != null) {
                Atom.ContainerAtom stblAtom = atom.getContainerAtomOfType(Atom.TYPE_mdia).getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl);
                TrackSampleTable trackSampleTable = AtomParsers.parseStbl(track, stblAtom, gaplessInfoHolder);
                if (trackSampleTable.sampleCount != 0) {
                    Mp4Track mp4Track = new Mp4Track(track, trackSampleTable, this.extractorOutput.track(i, track.type));
                    int maxInputSize = trackSampleTable.maximumSize + 30;
                    Format format = track.format.copyWithMaxInputSize(maxInputSize);
                    if (track.type == 1) {
                        if (gaplessInfoHolder.hasGaplessInfo()) {
                            format = format.copyWithGaplessInfo(gaplessInfoHolder.encoderDelay, gaplessInfoHolder.encoderPadding);
                        }
                        if (metadata != null) {
                            format = format.copyWithMetadata(metadata);
                        }
                    }
                    mp4Track.trackOutput.format(format);
                    durationUs = Math.max(durationUs, track.durationUs);
                    tracks.add(mp4Track);
                    long firstSampleOffset = trackSampleTable.offsets[0];
                    if (firstSampleOffset < earliestSampleOffset) {
                        earliestSampleOffset = firstSampleOffset;
                    }
                }
            }
        }
        this.durationUs = durationUs;
        this.tracks = (Mp4Track[]) tracks.toArray(new Mp4Track[tracks.size()]);
        this.extractorOutput.endTracks();
        this.extractorOutput.seekMap(this);
    }

    private int readSample(ExtractorInput input, PositionHolder positionHolder) throws InterruptedException, IOException {
        int trackIndex = getTrackIndexOfEarliestCurrentSample();
        if (trackIndex == -1) {
            return -1;
        }
        Mp4Track track = this.tracks[trackIndex];
        TrackOutput trackOutput = track.trackOutput;
        int sampleIndex = track.sampleIndex;
        long position = track.sampleTable.offsets[sampleIndex];
        int sampleSize = track.sampleTable.sizes[sampleIndex];
        if (track.track.sampleTransformation == 1) {
            position += 8;
            sampleSize -= 8;
        }
        long skipAmount = (position - input.getPosition()) + this.sampleBytesWritten;
        if (skipAmount < 0 || skipAmount >= RELOAD_MINIMUM_SEEK_DISTANCE) {
            positionHolder.position = position;
            return 1;
        }
        input.skipFully((int) skipAmount);
        if (track.track.nalUnitLengthFieldLength != 0) {
            byte[] nalLengthData = this.nalLength.data;
            nalLengthData[0] = 0;
            nalLengthData[1] = 0;
            nalLengthData[2] = 0;
            int nalUnitLengthFieldLength = track.track.nalUnitLengthFieldLength;
            int nalUnitLengthFieldLengthDiff = 4 - track.track.nalUnitLengthFieldLength;
            while (this.sampleBytesWritten < sampleSize) {
                if (this.sampleCurrentNalBytesRemaining == 0) {
                    input.readFully(this.nalLength.data, nalUnitLengthFieldLengthDiff, nalUnitLengthFieldLength);
                    this.nalLength.setPosition(0);
                    this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
                    this.nalStartCode.setPosition(0);
                    trackOutput.sampleData(this.nalStartCode, 4);
                    this.sampleBytesWritten += 4;
                    sampleSize += nalUnitLengthFieldLengthDiff;
                } else {
                    int writtenBytes = trackOutput.sampleData(input, this.sampleCurrentNalBytesRemaining, false);
                    this.sampleBytesWritten += writtenBytes;
                    this.sampleCurrentNalBytesRemaining -= writtenBytes;
                }
            }
        } else {
            while (this.sampleBytesWritten < sampleSize) {
                int writtenBytes2 = trackOutput.sampleData(input, sampleSize - this.sampleBytesWritten, false);
                this.sampleBytesWritten += writtenBytes2;
                this.sampleCurrentNalBytesRemaining -= writtenBytes2;
            }
        }
        trackOutput.sampleMetadata(track.sampleTable.timestampsUs[sampleIndex], track.sampleTable.flags[sampleIndex], sampleSize, 0, null);
        track.sampleIndex++;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        return 0;
    }

    private int getTrackIndexOfEarliestCurrentSample() {
        int earliestSampleTrackIndex = -1;
        long earliestSampleOffset = Long.MAX_VALUE;
        for (int trackIndex = 0; trackIndex < this.tracks.length; trackIndex++) {
            Mp4Track track = this.tracks[trackIndex];
            int sampleIndex = track.sampleIndex;
            if (sampleIndex != track.sampleTable.sampleCount) {
                long trackSampleOffset = track.sampleTable.offsets[sampleIndex];
                if (trackSampleOffset < earliestSampleOffset) {
                    earliestSampleOffset = trackSampleOffset;
                    earliestSampleTrackIndex = trackIndex;
                }
            }
        }
        return earliestSampleTrackIndex;
    }

    private void updateSampleIndices(long timeUs) {
        for (Mp4Track track : this.tracks) {
            TrackSampleTable sampleTable = track.sampleTable;
            int sampleIndex = sampleTable.getIndexOfEarlierOrEqualSynchronizationSample(timeUs);
            if (sampleIndex == -1) {
                sampleIndex = sampleTable.getIndexOfLaterOrEqualSynchronizationSample(timeUs);
            }
            track.sampleIndex = sampleIndex;
        }
    }

    private static boolean shouldParseLeafAtom(int atom) {
        return atom == Atom.TYPE_mdhd || atom == Atom.TYPE_mvhd || atom == Atom.TYPE_hdlr || atom == Atom.TYPE_stsd || atom == Atom.TYPE_stts || atom == Atom.TYPE_stss || atom == Atom.TYPE_ctts || atom == Atom.TYPE_elst || atom == Atom.TYPE_stsc || atom == Atom.TYPE_stsz || atom == Atom.TYPE_stz2 || atom == Atom.TYPE_stco || atom == Atom.TYPE_co64 || atom == Atom.TYPE_tkhd || atom == Atom.TYPE_ftyp || atom == Atom.TYPE_udta;
    }

    private static boolean shouldParseContainerAtom(int atom) {
        return atom == Atom.TYPE_moov || atom == Atom.TYPE_trak || atom == Atom.TYPE_mdia || atom == Atom.TYPE_minf || atom == Atom.TYPE_stbl || atom == Atom.TYPE_edts;
    }

    private static final class Mp4Track {
        public int sampleIndex;
        public final TrackSampleTable sampleTable;
        public final Track track;
        public final TrackOutput trackOutput;

        public Mp4Track(Track track, TrackSampleTable sampleTable, TrackOutput trackOutput) {
            this.track = track;
            this.sampleTable = sampleTable;
            this.trackOutput = trackOutput;
        }
    }
}
