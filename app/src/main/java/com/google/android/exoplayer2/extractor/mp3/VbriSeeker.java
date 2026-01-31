package com.google.android.exoplayer2.extractor.mp3;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.MpegAudioHeader;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
final class VbriSeeker implements Mp3Extractor.Seeker {
    private final long durationUs;
    private final long[] positions;
    private final long[] timesUs;

    public static VbriSeeker create(MpegAudioHeader mpegAudioHeader, ParsableByteArray frame, long position, long inputLength) {
        int segmentSize;
        frame.skipBytes(10);
        int numFrames = frame.readInt();
        if (numFrames <= 0) {
            return null;
        }
        int sampleRate = mpegAudioHeader.sampleRate;
        long durationUs = Util.scaleLargeTimestamp(numFrames, (sampleRate >= 32000 ? 1152 : 576) * C.MICROS_PER_SECOND, sampleRate);
        int entryCount = frame.readUnsignedShort();
        int scale = frame.readUnsignedShort();
        int entrySize = frame.readUnsignedShort();
        frame.skipBytes(2);
        long position2 = position + mpegAudioHeader.frameSize;
        long[] timesUs = new long[entryCount + 1];
        long[] positions = new long[entryCount + 1];
        timesUs[0] = 0;
        positions[0] = position2;
        for (int index = 1; index < timesUs.length; index++) {
            switch (entrySize) {
                case 1:
                    segmentSize = frame.readUnsignedByte();
                    break;
                case 2:
                    segmentSize = frame.readUnsignedShort();
                    break;
                case 3:
                    segmentSize = frame.readUnsignedInt24();
                    break;
                case 4:
                    segmentSize = frame.readUnsignedIntToInt();
                    break;
                default:
                    return null;
            }
            position2 += segmentSize * scale;
            timesUs[index] = (index * durationUs) / entryCount;
            positions[index] = inputLength == -1 ? position2 : Math.min(inputLength, position2);
        }
        return new VbriSeeker(timesUs, positions, durationUs);
    }

    private VbriSeeker(long[] timesUs, long[] positions, long durationUs) {
        this.timesUs = timesUs;
        this.positions = positions;
        this.durationUs = durationUs;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public boolean isSeekable() {
        return true;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public long getPosition(long timeUs) {
        return this.positions[Util.binarySearchFloor(this.timesUs, timeUs, true, true)];
    }

    @Override // com.google.android.exoplayer2.extractor.mp3.Mp3Extractor.Seeker
    public long getTimeUs(long position) {
        return this.timesUs[Util.binarySearchFloor(this.positions, position, true, true)];
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public long getDurationUs() {
        return this.durationUs;
    }
}
