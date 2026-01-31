package com.google.android.exoplayer2.extractor.mp4;

import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.audio.Ac3Util;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.GaplessInfoHolder;
import com.google.android.exoplayer2.extractor.mp4.Atom;
import com.google.android.exoplayer2.extractor.mp4.FixedSampleSizeRechunker;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
final class AtomParsers {
    private static final String TAG = "AtomParsers";
    private static final int TYPE_vide = Util.getIntegerCodeForString("vide");
    private static final int TYPE_soun = Util.getIntegerCodeForString("soun");
    private static final int TYPE_text = Util.getIntegerCodeForString("text");
    private static final int TYPE_sbtl = Util.getIntegerCodeForString("sbtl");
    private static final int TYPE_subt = Util.getIntegerCodeForString("subt");
    private static final int TYPE_clcp = Util.getIntegerCodeForString("clcp");
    private static final int TYPE_cenc = Util.getIntegerCodeForString("cenc");
    private static final int TYPE_meta = Util.getIntegerCodeForString("meta");

    private interface SampleSizeBox {
        int getSampleCount();

        boolean isFixedSampleSize();

        int readNextSampleSize();
    }

    public static Track parseTrak(Atom.ContainerAtom trak, Atom.LeafAtom mvhd, long duration, DrmInitData drmInitData, boolean isQuickTime) throws ParserException {
        long durationUs;
        Atom.ContainerAtom mdia = trak.getContainerAtomOfType(Atom.TYPE_mdia);
        int trackType = parseHdlr(mdia.getLeafAtomOfType(Atom.TYPE_hdlr).data);
        if (trackType == -1) {
            return null;
        }
        TkhdData tkhdData = parseTkhd(trak.getLeafAtomOfType(Atom.TYPE_tkhd).data);
        if (duration == C.TIME_UNSET) {
            duration = tkhdData.duration;
        }
        long movieTimescale = parseMvhd(mvhd.data);
        if (duration == C.TIME_UNSET) {
            durationUs = C.TIME_UNSET;
        } else {
            durationUs = Util.scaleLargeTimestamp(duration, C.MICROS_PER_SECOND, movieTimescale);
        }
        Atom.ContainerAtom stbl = mdia.getContainerAtomOfType(Atom.TYPE_minf).getContainerAtomOfType(Atom.TYPE_stbl);
        Pair<Long, String> mdhdData = parseMdhd(mdia.getLeafAtomOfType(Atom.TYPE_mdhd).data);
        StsdData stsdData = parseStsd(stbl.getLeafAtomOfType(Atom.TYPE_stsd).data, tkhdData.id, tkhdData.rotationDegrees, (String) mdhdData.second, drmInitData, isQuickTime);
        Pair<long[], long[]> edtsData = parseEdts(trak.getContainerAtomOfType(Atom.TYPE_edts));
        if (stsdData.format == null) {
            return null;
        }
        return new Track(tkhdData.id, trackType, ((Long) mdhdData.first).longValue(), movieTimescale, durationUs, stsdData.format, stsdData.requiredSampleTransformation, stsdData.trackEncryptionBoxes, stsdData.nalUnitLengthFieldLength, (long[]) edtsData.first, (long[]) edtsData.second);
    }

    public static TrackSampleTable parseStbl(Track track, Atom.ContainerAtom stblAtom, GaplessInfoHolder gaplessInfoHolder) throws ParserException {
        SampleSizeBox sampleSizeBox;
        long[] offsets;
        int[] sizes;
        long[] timestamps;
        int[] flags;
        Atom.LeafAtom stszAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_stsz);
        if (stszAtom != null) {
            sampleSizeBox = new StszSampleSizeBox(stszAtom);
        } else {
            Atom.LeafAtom stz2Atom = stblAtom.getLeafAtomOfType(Atom.TYPE_stz2);
            if (stz2Atom == null) {
                throw new ParserException("Track has no sample table size information");
            }
            sampleSizeBox = new Stz2SampleSizeBox(stz2Atom);
        }
        int sampleCount = sampleSizeBox.getSampleCount();
        if (sampleCount == 0) {
            return new TrackSampleTable(new long[0], new int[0], 0, new long[0], new int[0]);
        }
        boolean chunkOffsetsAreLongs = false;
        Atom.LeafAtom chunkOffsetsAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_stco);
        if (chunkOffsetsAtom == null) {
            chunkOffsetsAreLongs = true;
            chunkOffsetsAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_co64);
        }
        ParsableByteArray chunkOffsets = chunkOffsetsAtom.data;
        ParsableByteArray stsc = stblAtom.getLeafAtomOfType(Atom.TYPE_stsc).data;
        ParsableByteArray stts = stblAtom.getLeafAtomOfType(Atom.TYPE_stts).data;
        Atom.LeafAtom stssAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_stss);
        ParsableByteArray stss = stssAtom != null ? stssAtom.data : null;
        Atom.LeafAtom cttsAtom = stblAtom.getLeafAtomOfType(Atom.TYPE_ctts);
        ParsableByteArray ctts = cttsAtom != null ? cttsAtom.data : null;
        ChunkIterator chunkIterator = new ChunkIterator(stsc, chunkOffsets, chunkOffsetsAreLongs);
        stts.setPosition(12);
        int remainingTimestampDeltaChanges = stts.readUnsignedIntToInt() - 1;
        int remainingSamplesAtTimestampDelta = stts.readUnsignedIntToInt();
        int timestampDeltaInTimeUnits = stts.readUnsignedIntToInt();
        int remainingSamplesAtTimestampOffset = 0;
        int remainingTimestampOffsetChanges = 0;
        int timestampOffset = 0;
        if (ctts != null) {
            ctts.setPosition(12);
            remainingTimestampOffsetChanges = ctts.readUnsignedIntToInt();
        }
        int nextSynchronizationSampleIndex = -1;
        int remainingSynchronizationSamples = 0;
        if (stss != null) {
            stss.setPosition(12);
            remainingSynchronizationSamples = stss.readUnsignedIntToInt();
            if (remainingSynchronizationSamples > 0) {
                nextSynchronizationSampleIndex = stss.readUnsignedIntToInt() - 1;
            } else {
                stss = null;
            }
        }
        boolean isRechunkable = sampleSizeBox.isFixedSampleSize() && MimeTypes.AUDIO_RAW.equals(track.format.sampleMimeType) && remainingTimestampDeltaChanges == 0 && remainingTimestampOffsetChanges == 0 && remainingSynchronizationSamples == 0;
        int maximumSize = 0;
        long timestampTimeUnits = 0;
        if (!isRechunkable) {
            offsets = new long[sampleCount];
            sizes = new int[sampleCount];
            timestamps = new long[sampleCount];
            flags = new int[sampleCount];
            long offset = 0;
            int remainingSamplesInChunk = 0;
            for (int i = 0; i < sampleCount; i++) {
                while (remainingSamplesInChunk == 0) {
                    Assertions.checkState(chunkIterator.moveNext());
                    offset = chunkIterator.offset;
                    remainingSamplesInChunk = chunkIterator.numSamples;
                }
                if (ctts != null) {
                    while (remainingSamplesAtTimestampOffset == 0 && remainingTimestampOffsetChanges > 0) {
                        remainingSamplesAtTimestampOffset = ctts.readUnsignedIntToInt();
                        timestampOffset = ctts.readInt();
                        remainingTimestampOffsetChanges--;
                    }
                    remainingSamplesAtTimestampOffset--;
                }
                offsets[i] = offset;
                sizes[i] = sampleSizeBox.readNextSampleSize();
                if (sizes[i] > maximumSize) {
                    maximumSize = sizes[i];
                }
                timestamps[i] = timestampOffset + timestampTimeUnits;
                flags[i] = stss == null ? 1 : 0;
                if (i == nextSynchronizationSampleIndex) {
                    flags[i] = 1;
                    remainingSynchronizationSamples--;
                    if (remainingSynchronizationSamples > 0) {
                        nextSynchronizationSampleIndex = stss.readUnsignedIntToInt() - 1;
                    }
                }
                timestampTimeUnits += timestampDeltaInTimeUnits;
                remainingSamplesAtTimestampDelta--;
                if (remainingSamplesAtTimestampDelta == 0 && remainingTimestampDeltaChanges > 0) {
                    remainingSamplesAtTimestampDelta = stts.readUnsignedIntToInt();
                    timestampDeltaInTimeUnits = stts.readUnsignedIntToInt();
                    remainingTimestampDeltaChanges--;
                }
                offset += sizes[i];
                remainingSamplesInChunk--;
            }
            Assertions.checkArgument(remainingSamplesAtTimestampOffset == 0);
            while (remainingTimestampOffsetChanges > 0) {
                Assertions.checkArgument(ctts.readUnsignedIntToInt() == 0);
                ctts.readInt();
                remainingTimestampOffsetChanges--;
            }
            if (remainingSynchronizationSamples != 0 || remainingSamplesAtTimestampDelta != 0 || remainingSamplesInChunk != 0 || remainingTimestampDeltaChanges != 0) {
                Log.w(TAG, "Inconsistent stbl box for track " + track.id + ": remainingSynchronizationSamples " + remainingSynchronizationSamples + ", remainingSamplesAtTimestampDelta " + remainingSamplesAtTimestampDelta + ", remainingSamplesInChunk " + remainingSamplesInChunk + ", remainingTimestampDeltaChanges " + remainingTimestampDeltaChanges);
            }
        } else {
            long[] chunkOffsetsBytes = new long[chunkIterator.length];
            int[] chunkSampleCounts = new int[chunkIterator.length];
            while (chunkIterator.moveNext()) {
                chunkOffsetsBytes[chunkIterator.index] = chunkIterator.offset;
                chunkSampleCounts[chunkIterator.index] = chunkIterator.numSamples;
            }
            int fixedSampleSize = sampleSizeBox.readNextSampleSize();
            FixedSampleSizeRechunker.Results rechunkedResults = FixedSampleSizeRechunker.rechunk(fixedSampleSize, chunkOffsetsBytes, chunkSampleCounts, timestampDeltaInTimeUnits);
            offsets = rechunkedResults.offsets;
            sizes = rechunkedResults.sizes;
            maximumSize = rechunkedResults.maximumSize;
            timestamps = rechunkedResults.timestamps;
            flags = rechunkedResults.flags;
        }
        if (track.editListDurations == null || gaplessInfoHolder.hasGaplessInfo()) {
            Util.scaleLargeTimestampsInPlace(timestamps, C.MICROS_PER_SECOND, track.timescale);
            return new TrackSampleTable(offsets, sizes, maximumSize, timestamps, flags);
        }
        if (track.editListDurations.length == 1 && track.type == 1 && timestamps.length >= 2) {
            long editStartTime = track.editListMediaTimes[0];
            long editEndTime = editStartTime + Util.scaleLargeTimestamp(track.editListDurations[0], track.timescale, track.movieTimescale);
            long lastSampleEndTime = timestampTimeUnits;
            if (timestamps[0] <= editStartTime && editStartTime < timestamps[1] && timestamps[timestamps.length - 1] < editEndTime && editEndTime <= lastSampleEndTime) {
                long paddingTimeUnits = lastSampleEndTime - editEndTime;
                long encoderDelay = Util.scaleLargeTimestamp(editStartTime - timestamps[0], track.format.sampleRate, track.timescale);
                long encoderPadding = Util.scaleLargeTimestamp(paddingTimeUnits, track.format.sampleRate, track.timescale);
                if ((encoderDelay != 0 || encoderPadding != 0) && encoderDelay <= 2147483647L && encoderPadding <= 2147483647L) {
                    gaplessInfoHolder.encoderDelay = (int) encoderDelay;
                    gaplessInfoHolder.encoderPadding = (int) encoderPadding;
                    Util.scaleLargeTimestampsInPlace(timestamps, C.MICROS_PER_SECOND, track.timescale);
                    return new TrackSampleTable(offsets, sizes, maximumSize, timestamps, flags);
                }
            }
        }
        if (track.editListDurations.length == 1 && track.editListDurations[0] == 0) {
            for (int i2 = 0; i2 < timestamps.length; i2++) {
                timestamps[i2] = Util.scaleLargeTimestamp(timestamps[i2] - track.editListMediaTimes[0], C.MICROS_PER_SECOND, track.timescale);
            }
            return new TrackSampleTable(offsets, sizes, maximumSize, timestamps, flags);
        }
        boolean omitClippedSample = track.type == 1;
        int editedSampleCount = 0;
        int nextSampleIndex = 0;
        boolean copyMetadata = false;
        for (int i3 = 0; i3 < track.editListDurations.length; i3++) {
            long mediaTime = track.editListMediaTimes[i3];
            if (mediaTime != -1) {
                long duration = Util.scaleLargeTimestamp(track.editListDurations[i3], track.timescale, track.movieTimescale);
                int startIndex = Util.binarySearchCeil(timestamps, mediaTime, true, true);
                int endIndex = Util.binarySearchCeil(timestamps, mediaTime + duration, omitClippedSample, false);
                editedSampleCount += endIndex - startIndex;
                copyMetadata |= nextSampleIndex != startIndex;
                nextSampleIndex = endIndex;
            }
        }
        boolean copyMetadata2 = copyMetadata | (editedSampleCount != sampleCount);
        long[] editedOffsets = copyMetadata2 ? new long[editedSampleCount] : offsets;
        int[] editedSizes = copyMetadata2 ? new int[editedSampleCount] : sizes;
        int editedMaximumSize = copyMetadata2 ? 0 : maximumSize;
        int[] editedFlags = copyMetadata2 ? new int[editedSampleCount] : flags;
        long[] editedTimestamps = new long[editedSampleCount];
        long pts = 0;
        int sampleIndex = 0;
        for (int i4 = 0; i4 < track.editListDurations.length; i4++) {
            long mediaTime2 = track.editListMediaTimes[i4];
            long duration2 = track.editListDurations[i4];
            if (mediaTime2 != -1) {
                long endMediaTime = mediaTime2 + Util.scaleLargeTimestamp(duration2, track.timescale, track.movieTimescale);
                int startIndex2 = Util.binarySearchCeil(timestamps, mediaTime2, true, true);
                int endIndex2 = Util.binarySearchCeil(timestamps, endMediaTime, omitClippedSample, false);
                if (copyMetadata2) {
                    int count = endIndex2 - startIndex2;
                    System.arraycopy(offsets, startIndex2, editedOffsets, sampleIndex, count);
                    System.arraycopy(sizes, startIndex2, editedSizes, sampleIndex, count);
                    System.arraycopy(flags, startIndex2, editedFlags, sampleIndex, count);
                }
                for (int j = startIndex2; j < endIndex2; j++) {
                    long ptsUs = Util.scaleLargeTimestamp(pts, C.MICROS_PER_SECOND, track.movieTimescale);
                    long timeInSegmentUs = Util.scaleLargeTimestamp(timestamps[j] - mediaTime2, C.MICROS_PER_SECOND, track.timescale);
                    editedTimestamps[sampleIndex] = ptsUs + timeInSegmentUs;
                    if (copyMetadata2 && editedSizes[sampleIndex] > editedMaximumSize) {
                        editedMaximumSize = sizes[j];
                    }
                    sampleIndex++;
                }
            }
            pts += duration2;
        }
        boolean hasSyncSample = false;
        for (int i5 = 0; i5 < editedFlags.length && !hasSyncSample; i5++) {
            hasSyncSample |= (editedFlags[i5] & 1) != 0;
        }
        if (!hasSyncSample) {
            throw new ParserException("The edited sample sequence does not contain a sync sample.");
        }
        return new TrackSampleTable(editedOffsets, editedSizes, editedMaximumSize, editedTimestamps, editedFlags);
    }

    public static Metadata parseUdta(Atom.LeafAtom udtaAtom, boolean isQuickTime) {
        if (isQuickTime) {
            return null;
        }
        ParsableByteArray udtaData = udtaAtom.data;
        udtaData.setPosition(8);
        while (udtaData.bytesLeft() >= 8) {
            int atomPosition = udtaData.getPosition();
            int atomSize = udtaData.readInt();
            int atomType = udtaData.readInt();
            if (atomType == Atom.TYPE_meta) {
                udtaData.setPosition(atomPosition);
                return parseMetaAtom(udtaData, atomPosition + atomSize);
            }
            udtaData.skipBytes(atomSize - 8);
        }
        return null;
    }

    private static Metadata parseMetaAtom(ParsableByteArray meta, int limit) {
        meta.skipBytes(12);
        while (meta.getPosition() < limit) {
            int atomPosition = meta.getPosition();
            int atomSize = meta.readInt();
            int atomType = meta.readInt();
            if (atomType == Atom.TYPE_ilst) {
                meta.setPosition(atomPosition);
                return parseIlst(meta, atomPosition + atomSize);
            }
            meta.skipBytes(atomSize - 8);
        }
        return null;
    }

    private static Metadata parseIlst(ParsableByteArray ilst, int limit) {
        ilst.skipBytes(8);
        ArrayList<Metadata.Entry> entries = new ArrayList<>();
        while (ilst.getPosition() < limit) {
            Metadata.Entry entry = MetadataUtil.parseIlstElement(ilst);
            if (entry != null) {
                entries.add(entry);
            }
        }
        if (entries.isEmpty()) {
            return null;
        }
        return new Metadata(entries);
    }

    private static long parseMvhd(ParsableByteArray mvhd) {
        mvhd.setPosition(8);
        int fullAtom = mvhd.readInt();
        int version = Atom.parseFullAtomVersion(fullAtom);
        mvhd.skipBytes(version != 0 ? 16 : 8);
        return mvhd.readUnsignedInt();
    }

    private static TkhdData parseTkhd(ParsableByteArray tkhd) {
        long duration;
        int rotationDegrees;
        tkhd.setPosition(8);
        int fullAtom = tkhd.readInt();
        int version = Atom.parseFullAtomVersion(fullAtom);
        tkhd.skipBytes(version == 0 ? 8 : 16);
        int trackId = tkhd.readInt();
        tkhd.skipBytes(4);
        boolean durationUnknown = true;
        int durationPosition = tkhd.getPosition();
        int durationByteCount = version == 0 ? 4 : 8;
        int i = 0;
        while (true) {
            if (i >= durationByteCount) {
                break;
            }
            if (tkhd.data[durationPosition + i] == -1) {
                i++;
            } else {
                durationUnknown = false;
                break;
            }
        }
        if (durationUnknown) {
            tkhd.skipBytes(durationByteCount);
            duration = C.TIME_UNSET;
        } else {
            duration = version == 0 ? tkhd.readUnsignedInt() : tkhd.readUnsignedLongToLong();
            if (duration == 0) {
                duration = C.TIME_UNSET;
            }
        }
        tkhd.skipBytes(16);
        int a00 = tkhd.readInt();
        int a01 = tkhd.readInt();
        tkhd.skipBytes(4);
        int a10 = tkhd.readInt();
        int a11 = tkhd.readInt();
        if (a00 == 0 && a01 == 65536 && a10 == (-65536) && a11 == 0) {
            rotationDegrees = 90;
        } else if (a00 == 0 && a01 == (-65536) && a10 == 65536 && a11 == 0) {
            rotationDegrees = 270;
        } else if (a00 == (-65536) && a01 == 0 && a10 == 0 && a11 == (-65536)) {
            rotationDegrees = 180;
        } else {
            rotationDegrees = 0;
        }
        return new TkhdData(trackId, duration, rotationDegrees);
    }

    private static int parseHdlr(ParsableByteArray hdlr) {
        hdlr.setPosition(16);
        int trackType = hdlr.readInt();
        if (trackType == TYPE_soun) {
            return 1;
        }
        if (trackType == TYPE_vide) {
            return 2;
        }
        if (trackType == TYPE_text || trackType == TYPE_sbtl || trackType == TYPE_subt || trackType == TYPE_clcp) {
            return 3;
        }
        if (trackType == TYPE_meta) {
            return 4;
        }
        return -1;
    }

    private static Pair<Long, String> parseMdhd(ParsableByteArray mdhd) {
        mdhd.setPosition(8);
        int fullAtom = mdhd.readInt();
        int version = Atom.parseFullAtomVersion(fullAtom);
        mdhd.skipBytes(version == 0 ? 8 : 16);
        long timescale = mdhd.readUnsignedInt();
        mdhd.skipBytes(version == 0 ? 4 : 8);
        int languageCode = mdhd.readUnsignedShort();
        String language = "" + ((char) (((languageCode >> 10) & 31) + 96)) + ((char) (((languageCode >> 5) & 31) + 96)) + ((char) ((languageCode & 31) + 96));
        return Pair.create(Long.valueOf(timescale), language);
    }

    private static StsdData parseStsd(ParsableByteArray stsd, int trackId, int rotationDegrees, String language, DrmInitData drmInitData, boolean isQuickTime) throws ParserException {
        stsd.setPosition(12);
        int numberOfEntries = stsd.readInt();
        StsdData out = new StsdData(numberOfEntries);
        for (int i = 0; i < numberOfEntries; i++) {
            int childStartPosition = stsd.getPosition();
            int childAtomSize = stsd.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType = stsd.readInt();
            if (childAtomType == Atom.TYPE_avc1 || childAtomType == Atom.TYPE_avc3 || childAtomType == Atom.TYPE_encv || childAtomType == Atom.TYPE_mp4v || childAtomType == Atom.TYPE_hvc1 || childAtomType == Atom.TYPE_hev1 || childAtomType == Atom.TYPE_s263 || childAtomType == Atom.TYPE_vp08 || childAtomType == Atom.TYPE_vp09) {
                parseVideoSampleEntry(stsd, childAtomType, childStartPosition, childAtomSize, trackId, rotationDegrees, drmInitData, out, i);
            } else if (childAtomType == Atom.TYPE_mp4a || childAtomType == Atom.TYPE_enca || childAtomType == Atom.TYPE_ac_3 || childAtomType == Atom.TYPE_ec_3 || childAtomType == Atom.TYPE_dtsc || childAtomType == Atom.TYPE_dtse || childAtomType == Atom.TYPE_dtsh || childAtomType == Atom.TYPE_dtsl || childAtomType == Atom.TYPE_samr || childAtomType == Atom.TYPE_sawb || childAtomType == Atom.TYPE_lpcm || childAtomType == Atom.TYPE_sowt || childAtomType == Atom.TYPE__mp3 || childAtomType == Atom.TYPE_alac) {
                parseAudioSampleEntry(stsd, childAtomType, childStartPosition, childAtomSize, trackId, language, isQuickTime, drmInitData, out, i);
            } else if (childAtomType == Atom.TYPE_TTML || childAtomType == Atom.TYPE_tx3g || childAtomType == Atom.TYPE_wvtt || childAtomType == Atom.TYPE_stpp || childAtomType == Atom.TYPE_c608) {
                parseTextSampleEntry(stsd, childAtomType, childStartPosition, childAtomSize, trackId, language, drmInitData, out);
            } else if (childAtomType == Atom.TYPE_camm) {
                out.format = Format.createSampleFormat(Integer.toString(trackId), MimeTypes.APPLICATION_CAMERA_MOTION, null, -1, drmInitData);
            }
            stsd.setPosition(childStartPosition + childAtomSize);
        }
        return out;
    }

    private static void parseTextSampleEntry(ParsableByteArray parent, int atomType, int position, int atomSize, int trackId, String language, DrmInitData drmInitData, StsdData out) throws ParserException {
        String mimeType;
        parent.setPosition(position + 8 + 8);
        List<byte[]> initializationData = null;
        long subsampleOffsetUs = Long.MAX_VALUE;
        if (atomType == Atom.TYPE_TTML) {
            mimeType = MimeTypes.APPLICATION_TTML;
        } else if (atomType == Atom.TYPE_tx3g) {
            mimeType = MimeTypes.APPLICATION_TX3G;
            int sampleDescriptionLength = (atomSize - 8) - 8;
            byte[] sampleDescriptionData = new byte[sampleDescriptionLength];
            parent.readBytes(sampleDescriptionData, 0, sampleDescriptionLength);
            initializationData = Collections.singletonList(sampleDescriptionData);
        } else if (atomType == Atom.TYPE_wvtt) {
            mimeType = MimeTypes.APPLICATION_MP4VTT;
        } else if (atomType == Atom.TYPE_stpp) {
            mimeType = MimeTypes.APPLICATION_TTML;
            subsampleOffsetUs = 0;
        } else if (atomType == Atom.TYPE_c608) {
            mimeType = MimeTypes.APPLICATION_MP4CEA608;
            out.requiredSampleTransformation = 1;
        } else {
            throw new IllegalStateException();
        }
        out.format = Format.createTextSampleFormat(Integer.toString(trackId), mimeType, null, -1, 0, language, -1, drmInitData, subsampleOffsetUs, initializationData);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x006b A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0171  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void parseVideoSampleEntry(com.google.android.exoplayer2.util.ParsableByteArray r30, int r31, int r32, int r33, int r34, int r35, com.google.android.exoplayer2.drm.DrmInitData r36, com.google.android.exoplayer2.extractor.mp4.AtomParsers.StsdData r37, int r38) throws com.google.android.exoplayer2.ParserException {
        /*
            Method dump skipped, instructions count: 406
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.extractor.mp4.AtomParsers.parseVideoSampleEntry(com.google.android.exoplayer2.util.ParsableByteArray, int, int, int, int, int, com.google.android.exoplayer2.drm.DrmInitData, com.google.android.exoplayer2.extractor.mp4.AtomParsers$StsdData, int):void");
    }

    private static Pair<long[], long[]> parseEdts(Atom.ContainerAtom edtsAtom) {
        Atom.LeafAtom elst;
        if (edtsAtom == null || (elst = edtsAtom.getLeafAtomOfType(Atom.TYPE_elst)) == null) {
            return Pair.create(null, null);
        }
        ParsableByteArray elstData = elst.data;
        elstData.setPosition(8);
        int fullAtom = elstData.readInt();
        int version = Atom.parseFullAtomVersion(fullAtom);
        int entryCount = elstData.readUnsignedIntToInt();
        long[] editListDurations = new long[entryCount];
        long[] editListMediaTimes = new long[entryCount];
        for (int i = 0; i < entryCount; i++) {
            editListDurations[i] = version == 1 ? elstData.readUnsignedLongToLong() : elstData.readUnsignedInt();
            editListMediaTimes[i] = version == 1 ? elstData.readLong() : elstData.readInt();
            int mediaRateInteger = elstData.readShort();
            if (mediaRateInteger != 1) {
                throw new IllegalArgumentException("Unsupported media rate.");
            }
            elstData.skipBytes(2);
        }
        return Pair.create(editListDurations, editListMediaTimes);
    }

    private static float parsePaspFromParent(ParsableByteArray parent, int position) {
        parent.setPosition(position + 8);
        int hSpacing = parent.readUnsignedIntToInt();
        int vSpacing = parent.readUnsignedIntToInt();
        return hSpacing / vSpacing;
    }

    private static void parseAudioSampleEntry(ParsableByteArray parent, int atomType, int position, int size, int trackId, String language, boolean isQuickTime, DrmInitData drmInitData, StsdData out, int entryIndex) {
        int channelCount;
        int sampleRate;
        parent.setPosition(position + 8 + 8);
        int quickTimeSoundDescriptionVersion = 0;
        if (isQuickTime) {
            quickTimeSoundDescriptionVersion = parent.readUnsignedShort();
            parent.skipBytes(6);
        } else {
            parent.skipBytes(8);
        }
        if (quickTimeSoundDescriptionVersion == 0 || quickTimeSoundDescriptionVersion == 1) {
            channelCount = parent.readUnsignedShort();
            parent.skipBytes(6);
            sampleRate = parent.readUnsignedFixedPoint1616();
            if (quickTimeSoundDescriptionVersion == 1) {
                parent.skipBytes(16);
            }
        } else if (quickTimeSoundDescriptionVersion == 2) {
            parent.skipBytes(16);
            sampleRate = (int) Math.round(parent.readDouble());
            channelCount = parent.readUnsignedIntToInt();
            parent.skipBytes(20);
        } else {
            return;
        }
        int childPosition = parent.getPosition();
        if (atomType == Atom.TYPE_enca) {
            atomType = parseSampleEntryEncryptionData(parent, position, size, out, entryIndex);
            parent.setPosition(childPosition);
        }
        String mimeType = null;
        if (atomType == Atom.TYPE_ac_3) {
            mimeType = MimeTypes.AUDIO_AC3;
        } else if (atomType == Atom.TYPE_ec_3) {
            mimeType = MimeTypes.AUDIO_E_AC3;
        } else if (atomType == Atom.TYPE_dtsc) {
            mimeType = MimeTypes.AUDIO_DTS;
        } else if (atomType == Atom.TYPE_dtsh || atomType == Atom.TYPE_dtsl) {
            mimeType = MimeTypes.AUDIO_DTS_HD;
        } else if (atomType == Atom.TYPE_dtse) {
            mimeType = MimeTypes.AUDIO_DTS_EXPRESS;
        } else if (atomType == Atom.TYPE_samr) {
            mimeType = MimeTypes.AUDIO_AMR_NB;
        } else if (atomType == Atom.TYPE_sawb) {
            mimeType = MimeTypes.AUDIO_AMR_WB;
        } else if (atomType == Atom.TYPE_lpcm || atomType == Atom.TYPE_sowt) {
            mimeType = MimeTypes.AUDIO_RAW;
        } else if (atomType == Atom.TYPE__mp3) {
            mimeType = MimeTypes.AUDIO_MPEG;
        } else if (atomType == Atom.TYPE_alac) {
            mimeType = MimeTypes.AUDIO_ALAC;
        }
        byte[] initializationData = null;
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_esds || (isQuickTime && childAtomType == Atom.TYPE_wave)) {
                int esdsAtomPosition = childAtomType == Atom.TYPE_esds ? childPosition : findEsdsPosition(parent, childPosition, childAtomSize);
                if (esdsAtomPosition != -1) {
                    Pair<String, byte[]> mimeTypeAndInitializationData = parseEsdsFromParent(parent, esdsAtomPosition);
                    mimeType = (String) mimeTypeAndInitializationData.first;
                    initializationData = (byte[]) mimeTypeAndInitializationData.second;
                    if (MimeTypes.AUDIO_AAC.equals(mimeType)) {
                        Pair<Integer, Integer> audioSpecificConfig = CodecSpecificDataUtil.parseAacAudioSpecificConfig(initializationData);
                        sampleRate = ((Integer) audioSpecificConfig.first).intValue();
                        channelCount = ((Integer) audioSpecificConfig.second).intValue();
                    }
                }
            } else if (childAtomType == Atom.TYPE_dac3) {
                parent.setPosition(childPosition + 8);
                out.format = Ac3Util.parseAc3AnnexFFormat(parent, Integer.toString(trackId), language, drmInitData);
            } else if (childAtomType == Atom.TYPE_dec3) {
                parent.setPosition(childPosition + 8);
                out.format = Ac3Util.parseEAc3AnnexFFormat(parent, Integer.toString(trackId), language, drmInitData);
            } else if (childAtomType == Atom.TYPE_ddts) {
                out.format = Format.createAudioSampleFormat(Integer.toString(trackId), mimeType, null, -1, -1, channelCount, sampleRate, null, drmInitData, 0, language);
            } else if (childAtomType == Atom.TYPE_alac) {
                initializationData = new byte[childAtomSize];
                parent.setPosition(childPosition);
                parent.readBytes(initializationData, 0, childAtomSize);
            }
            childPosition += childAtomSize;
        }
        if (out.format == null && mimeType != null) {
            int pcmEncoding = MimeTypes.AUDIO_RAW.equals(mimeType) ? 2 : -1;
            out.format = Format.createAudioSampleFormat(Integer.toString(trackId), mimeType, null, -1, -1, channelCount, sampleRate, pcmEncoding, initializationData == null ? null : Collections.singletonList(initializationData), drmInitData, 0, language);
        }
    }

    private static int findEsdsPosition(ParsableByteArray parent, int position, int size) {
        int childAtomPosition = parent.getPosition();
        while (childAtomPosition - position < size) {
            parent.setPosition(childAtomPosition);
            int childAtomSize = parent.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childType = parent.readInt();
            if (childType != Atom.TYPE_esds) {
                childAtomPosition += childAtomSize;
            } else {
                return childAtomPosition;
            }
        }
        return -1;
    }

    private static Pair<String, byte[]> parseEsdsFromParent(ParsableByteArray parent, int position) {
        String mimeType;
        parent.setPosition(position + 8 + 4);
        parent.skipBytes(1);
        parseExpandableClassSize(parent);
        parent.skipBytes(2);
        int flags = parent.readUnsignedByte();
        if ((flags & 128) != 0) {
            parent.skipBytes(2);
        }
        if ((flags & 64) != 0) {
            parent.skipBytes(parent.readUnsignedShort());
        }
        if ((flags & 32) != 0) {
            parent.skipBytes(2);
        }
        parent.skipBytes(1);
        parseExpandableClassSize(parent);
        int objectTypeIndication = parent.readUnsignedByte();
        switch (objectTypeIndication) {
            case 32:
                mimeType = MimeTypes.VIDEO_MP4V;
                break;
            case 33:
                mimeType = MimeTypes.VIDEO_H264;
                break;
            case 35:
                mimeType = MimeTypes.VIDEO_H265;
                break;
            case 64:
            case 102:
            case 103:
            case 104:
                mimeType = MimeTypes.AUDIO_AAC;
                break;
            case 107:
                return Pair.create(MimeTypes.AUDIO_MPEG, null);
            case 165:
                mimeType = MimeTypes.AUDIO_AC3;
                break;
            case 166:
                mimeType = MimeTypes.AUDIO_E_AC3;
                break;
            case 169:
            case 172:
                return Pair.create(MimeTypes.AUDIO_DTS, null);
            case 170:
            case 171:
                return Pair.create(MimeTypes.AUDIO_DTS_HD, null);
            default:
                mimeType = null;
                break;
        }
        parent.skipBytes(12);
        parent.skipBytes(1);
        int initializationDataSize = parseExpandableClassSize(parent);
        byte[] initializationData = new byte[initializationDataSize];
        parent.readBytes(initializationData, 0, initializationDataSize);
        return Pair.create(mimeType, initializationData);
    }

    private static int parseSampleEntryEncryptionData(ParsableByteArray parent, int position, int size, StsdData out, int entryIndex) {
        Pair<Integer, TrackEncryptionBox> result;
        int childPosition = parent.getPosition();
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            Assertions.checkArgument(childAtomSize > 0, "childAtomSize should be positive");
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_sinf && (result = parseSinfFromParent(parent, childPosition, childAtomSize)) != null) {
                out.trackEncryptionBoxes[entryIndex] = (TrackEncryptionBox) result.second;
                return ((Integer) result.first).intValue();
            }
            childPosition += childAtomSize;
        }
        return 0;
    }

    private static Pair<Integer, TrackEncryptionBox> parseSinfFromParent(ParsableByteArray parent, int position, int size) {
        int childPosition = position + 8;
        boolean isCencScheme = false;
        TrackEncryptionBox trackEncryptionBox = null;
        Integer dataFormat = null;
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_frma) {
                dataFormat = Integer.valueOf(parent.readInt());
            } else if (childAtomType == Atom.TYPE_schm) {
                parent.skipBytes(4);
                isCencScheme = parent.readInt() == TYPE_cenc;
            } else if (childAtomType == Atom.TYPE_schi) {
                trackEncryptionBox = parseSchiFromParent(parent, childPosition, childAtomSize);
            }
            childPosition += childAtomSize;
        }
        if (isCencScheme) {
            Assertions.checkArgument(dataFormat != null, "frma atom is mandatory");
            Assertions.checkArgument(trackEncryptionBox != null, "schi->tenc atom is mandatory");
            return Pair.create(dataFormat, trackEncryptionBox);
        }
        return null;
    }

    private static TrackEncryptionBox parseSchiFromParent(ParsableByteArray parent, int position, int size) {
        int childPosition = position + 8;
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_tenc) {
                parent.skipBytes(6);
                boolean defaultIsEncrypted = parent.readUnsignedByte() == 1;
                int defaultInitVectorSize = parent.readUnsignedByte();
                byte[] defaultKeyId = new byte[16];
                parent.readBytes(defaultKeyId, 0, defaultKeyId.length);
                return new TrackEncryptionBox(defaultIsEncrypted, defaultInitVectorSize, defaultKeyId);
            }
            childPosition += childAtomSize;
        }
        return null;
    }

    private static byte[] parseProjFromParent(ParsableByteArray parent, int position, int size) {
        int childPosition = position + 8;
        while (childPosition - position < size) {
            parent.setPosition(childPosition);
            int childAtomSize = parent.readInt();
            int childAtomType = parent.readInt();
            if (childAtomType == Atom.TYPE_proj) {
                return Arrays.copyOfRange(parent.data, childPosition, childPosition + childAtomSize);
            }
            childPosition += childAtomSize;
        }
        return null;
    }

    private static int parseExpandableClassSize(ParsableByteArray data) {
        int currentByte = data.readUnsignedByte();
        int size = currentByte & 127;
        while ((currentByte & 128) == 128) {
            currentByte = data.readUnsignedByte();
            size = (size << 7) | (currentByte & 127);
        }
        return size;
    }

    private AtomParsers() {
    }

    private static final class ChunkIterator {
        private final ParsableByteArray chunkOffsets;
        private final boolean chunkOffsetsAreLongs;
        public int index;
        public final int length;
        private int nextSamplesPerChunkChangeIndex;
        public int numSamples;
        public long offset;
        private int remainingSamplesPerChunkChanges;
        private final ParsableByteArray stsc;

        public ChunkIterator(ParsableByteArray stsc, ParsableByteArray chunkOffsets, boolean chunkOffsetsAreLongs) {
            this.stsc = stsc;
            this.chunkOffsets = chunkOffsets;
            this.chunkOffsetsAreLongs = chunkOffsetsAreLongs;
            chunkOffsets.setPosition(12);
            this.length = chunkOffsets.readUnsignedIntToInt();
            stsc.setPosition(12);
            this.remainingSamplesPerChunkChanges = stsc.readUnsignedIntToInt();
            Assertions.checkState(stsc.readInt() == 1, "first_chunk must be 1");
            this.index = -1;
        }

        public boolean moveNext() {
            int i = this.index + 1;
            this.index = i;
            if (i == this.length) {
                return false;
            }
            this.offset = this.chunkOffsetsAreLongs ? this.chunkOffsets.readUnsignedLongToLong() : this.chunkOffsets.readUnsignedInt();
            if (this.index == this.nextSamplesPerChunkChangeIndex) {
                this.numSamples = this.stsc.readUnsignedIntToInt();
                this.stsc.skipBytes(4);
                int i2 = this.remainingSamplesPerChunkChanges - 1;
                this.remainingSamplesPerChunkChanges = i2;
                this.nextSamplesPerChunkChangeIndex = i2 > 0 ? this.stsc.readUnsignedIntToInt() - 1 : -1;
            }
            return true;
        }
    }

    private static final class TkhdData {
        private final long duration;
        private final int id;
        private final int rotationDegrees;

        public TkhdData(int id, long duration, int rotationDegrees) {
            this.id = id;
            this.duration = duration;
            this.rotationDegrees = rotationDegrees;
        }
    }

    private static final class StsdData {
        public static final int STSD_HEADER_SIZE = 8;
        public Format format;
        public int nalUnitLengthFieldLength;
        public int requiredSampleTransformation = 0;
        public final TrackEncryptionBox[] trackEncryptionBoxes;

        public StsdData(int numberOfEntries) {
            this.trackEncryptionBoxes = new TrackEncryptionBox[numberOfEntries];
        }
    }

    static final class StszSampleSizeBox implements SampleSizeBox {
        private final ParsableByteArray data;
        private final int fixedSampleSize;
        private final int sampleCount;

        public StszSampleSizeBox(Atom.LeafAtom stszAtom) {
            this.data = stszAtom.data;
            this.data.setPosition(12);
            this.fixedSampleSize = this.data.readUnsignedIntToInt();
            this.sampleCount = this.data.readUnsignedIntToInt();
        }

        @Override // com.google.android.exoplayer2.extractor.mp4.AtomParsers.SampleSizeBox
        public int getSampleCount() {
            return this.sampleCount;
        }

        @Override // com.google.android.exoplayer2.extractor.mp4.AtomParsers.SampleSizeBox
        public int readNextSampleSize() {
            return this.fixedSampleSize == 0 ? this.data.readUnsignedIntToInt() : this.fixedSampleSize;
        }

        @Override // com.google.android.exoplayer2.extractor.mp4.AtomParsers.SampleSizeBox
        public boolean isFixedSampleSize() {
            return this.fixedSampleSize != 0;
        }
    }

    static final class Stz2SampleSizeBox implements SampleSizeBox {
        private int currentByte;
        private final ParsableByteArray data;
        private final int fieldSize;
        private final int sampleCount;
        private int sampleIndex;

        public Stz2SampleSizeBox(Atom.LeafAtom stz2Atom) {
            this.data = stz2Atom.data;
            this.data.setPosition(12);
            this.fieldSize = this.data.readUnsignedIntToInt() & 255;
            this.sampleCount = this.data.readUnsignedIntToInt();
        }

        @Override // com.google.android.exoplayer2.extractor.mp4.AtomParsers.SampleSizeBox
        public int getSampleCount() {
            return this.sampleCount;
        }

        @Override // com.google.android.exoplayer2.extractor.mp4.AtomParsers.SampleSizeBox
        public int readNextSampleSize() {
            if (this.fieldSize == 8) {
                return this.data.readUnsignedByte();
            }
            if (this.fieldSize == 16) {
                return this.data.readUnsignedShort();
            }
            int i = this.sampleIndex;
            this.sampleIndex = i + 1;
            if (i % 2 == 0) {
                this.currentByte = this.data.readUnsignedByte();
                return (this.currentByte & PsExtractor.VIDEO_STREAM_MASK) >> 4;
            }
            return this.currentByte & 15;
        }

        @Override // com.google.android.exoplayer2.extractor.mp4.AtomParsers.SampleSizeBox
        public boolean isFixedSampleSize() {
            return false;
        }
    }
}
