package com.google.android.exoplayer2.extractor.ts;

import android.util.Log;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.ParsableNalUnitBitArray;
import java.util.Collections;

/* loaded from: classes.dex */
public final class H265Reader implements ElementaryStreamReader {
    private static final int BLA_W_LP = 16;
    private static final int CRA_NUT = 21;
    private static final int PPS_NUT = 34;
    private static final int PREFIX_SEI_NUT = 39;
    private static final int RASL_R = 9;
    private static final int SPS_NUT = 33;
    private static final int SUFFIX_SEI_NUT = 40;
    private static final String TAG = "H265Reader";
    private static final int VPS_NUT = 32;
    private String formatId;
    private boolean hasOutputFormat;
    private TrackOutput output;
    private long pesTimeUs;
    private SampleReader sampleReader;
    private final SeiReader seiReader;
    private long totalBytesWritten;
    private final boolean[] prefixFlags = new boolean[3];
    private final NalUnitTargetBuffer vps = new NalUnitTargetBuffer(32, 128);
    private final NalUnitTargetBuffer sps = new NalUnitTargetBuffer(33, 128);
    private final NalUnitTargetBuffer pps = new NalUnitTargetBuffer(34, 128);
    private final NalUnitTargetBuffer prefixSei = new NalUnitTargetBuffer(39, 128);
    private final NalUnitTargetBuffer suffixSei = new NalUnitTargetBuffer(40, 128);
    private final ParsableByteArray seiWrapper = new ParsableByteArray();

    public H265Reader(SeiReader seiReader) {
        this.seiReader = seiReader;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void seek() {
        NalUnitUtil.clearPrefixFlags(this.prefixFlags);
        this.vps.reset();
        this.sps.reset();
        this.pps.reset();
        this.prefixSei.reset();
        this.suffixSei.reset();
        this.sampleReader.reset();
        this.totalBytesWritten = 0L;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void createTracks(ExtractorOutput extractorOutput, TsPayloadReader.TrackIdGenerator idGenerator) {
        idGenerator.generateNewId();
        this.formatId = idGenerator.getFormatId();
        this.output = extractorOutput.track(idGenerator.getTrackId(), 2);
        this.sampleReader = new SampleReader(this.output);
        this.seiReader.createTracks(extractorOutput, idGenerator);
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void packetStarted(long pesTimeUs, boolean dataAlignmentIndicator) {
        this.pesTimeUs = pesTimeUs;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void consume(ParsableByteArray data) throws Throwable {
        while (data.bytesLeft() > 0) {
            int offset = data.getPosition();
            int limit = data.limit();
            byte[] dataArray = data.data;
            this.totalBytesWritten += data.bytesLeft();
            this.output.sampleData(data, data.bytesLeft());
            while (offset < limit) {
                int nalUnitOffset = NalUnitUtil.findNalUnit(dataArray, offset, limit, this.prefixFlags);
                if (nalUnitOffset == limit) {
                    nalUnitData(dataArray, offset, limit);
                    return;
                }
                int nalUnitType = NalUnitUtil.getH265NalUnitType(dataArray, nalUnitOffset);
                int lengthToNalUnit = nalUnitOffset - offset;
                if (lengthToNalUnit > 0) {
                    nalUnitData(dataArray, offset, nalUnitOffset);
                }
                int bytesWrittenPastPosition = limit - nalUnitOffset;
                long absolutePosition = this.totalBytesWritten - bytesWrittenPastPosition;
                endNalUnit(absolutePosition, bytesWrittenPastPosition, lengthToNalUnit < 0 ? -lengthToNalUnit : 0, this.pesTimeUs);
                startNalUnit(absolutePosition, bytesWrittenPastPosition, nalUnitType, this.pesTimeUs);
                offset = nalUnitOffset + 3;
            }
        }
    }

    @Override // com.google.android.exoplayer2.extractor.ts.ElementaryStreamReader
    public void packetFinished() {
    }

    private void startNalUnit(long position, int offset, int nalUnitType, long pesTimeUs) {
        if (this.hasOutputFormat) {
            this.sampleReader.startNalUnit(position, offset, nalUnitType, pesTimeUs);
        } else {
            this.vps.startNalUnit(nalUnitType);
            this.sps.startNalUnit(nalUnitType);
            this.pps.startNalUnit(nalUnitType);
        }
        this.prefixSei.startNalUnit(nalUnitType);
        this.suffixSei.startNalUnit(nalUnitType);
    }

    private void nalUnitData(byte[] dataArray, int offset, int limit) {
        if (this.hasOutputFormat) {
            this.sampleReader.readNalUnitData(dataArray, offset, limit);
        } else {
            this.vps.appendToNalUnit(dataArray, offset, limit);
            this.sps.appendToNalUnit(dataArray, offset, limit);
            this.pps.appendToNalUnit(dataArray, offset, limit);
        }
        this.prefixSei.appendToNalUnit(dataArray, offset, limit);
        this.suffixSei.appendToNalUnit(dataArray, offset, limit);
    }

    private void endNalUnit(long position, int offset, int discardPadding, long pesTimeUs) throws Throwable {
        if (this.hasOutputFormat) {
            this.sampleReader.endNalUnit(position, offset);
        } else {
            this.vps.endNalUnit(discardPadding);
            this.sps.endNalUnit(discardPadding);
            this.pps.endNalUnit(discardPadding);
            if (this.vps.isCompleted() && this.sps.isCompleted() && this.pps.isCompleted()) {
                this.output.format(parseMediaFormat(this.formatId, this.vps, this.sps, this.pps));
                this.hasOutputFormat = true;
            }
        }
        if (this.prefixSei.endNalUnit(discardPadding)) {
            int unescapedLength = NalUnitUtil.unescapeStream(this.prefixSei.nalData, this.prefixSei.nalLength);
            this.seiWrapper.reset(this.prefixSei.nalData, unescapedLength);
            this.seiWrapper.skipBytes(5);
            this.seiReader.consume(pesTimeUs, this.seiWrapper);
        }
        if (this.suffixSei.endNalUnit(discardPadding)) {
            int unescapedLength2 = NalUnitUtil.unescapeStream(this.suffixSei.nalData, this.suffixSei.nalLength);
            this.seiWrapper.reset(this.suffixSei.nalData, unescapedLength2);
            this.seiWrapper.skipBytes(5);
            this.seiReader.consume(pesTimeUs, this.seiWrapper);
        }
    }

    private static Format parseMediaFormat(String formatId, NalUnitTargetBuffer vps, NalUnitTargetBuffer sps, NalUnitTargetBuffer pps) {
        byte[] csd = new byte[vps.nalLength + sps.nalLength + pps.nalLength];
        System.arraycopy(vps.nalData, 0, csd, 0, vps.nalLength);
        System.arraycopy(sps.nalData, 0, csd, vps.nalLength, sps.nalLength);
        System.arraycopy(pps.nalData, 0, csd, vps.nalLength + sps.nalLength, pps.nalLength);
        ParsableNalUnitBitArray bitArray = new ParsableNalUnitBitArray(sps.nalData, 0, sps.nalLength);
        bitArray.skipBits(44);
        int maxSubLayersMinus1 = bitArray.readBits(3);
        bitArray.skipBits(1);
        bitArray.skipBits(88);
        bitArray.skipBits(8);
        int toSkip = 0;
        for (int i = 0; i < maxSubLayersMinus1; i++) {
            if (bitArray.readBit()) {
                toSkip += 89;
            }
            if (bitArray.readBit()) {
                toSkip += 8;
            }
        }
        bitArray.skipBits(toSkip);
        if (maxSubLayersMinus1 > 0) {
            bitArray.skipBits((8 - maxSubLayersMinus1) * 2);
        }
        bitArray.readUnsignedExpGolombCodedInt();
        int chromaFormatIdc = bitArray.readUnsignedExpGolombCodedInt();
        if (chromaFormatIdc == 3) {
            bitArray.skipBits(1);
        }
        int picWidthInLumaSamples = bitArray.readUnsignedExpGolombCodedInt();
        int picHeightInLumaSamples = bitArray.readUnsignedExpGolombCodedInt();
        if (bitArray.readBit()) {
            int confWinLeftOffset = bitArray.readUnsignedExpGolombCodedInt();
            int confWinRightOffset = bitArray.readUnsignedExpGolombCodedInt();
            int confWinTopOffset = bitArray.readUnsignedExpGolombCodedInt();
            int confWinBottomOffset = bitArray.readUnsignedExpGolombCodedInt();
            int subWidthC = (chromaFormatIdc == 1 || chromaFormatIdc == 2) ? 2 : 1;
            int subHeightC = chromaFormatIdc == 1 ? 2 : 1;
            picWidthInLumaSamples -= (confWinLeftOffset + confWinRightOffset) * subWidthC;
            picHeightInLumaSamples -= (confWinTopOffset + confWinBottomOffset) * subHeightC;
        }
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.readUnsignedExpGolombCodedInt();
        int log2MaxPicOrderCntLsbMinus4 = bitArray.readUnsignedExpGolombCodedInt();
        for (int i2 = bitArray.readBit() ? 0 : maxSubLayersMinus1; i2 <= maxSubLayersMinus1; i2++) {
            bitArray.readUnsignedExpGolombCodedInt();
            bitArray.readUnsignedExpGolombCodedInt();
            bitArray.readUnsignedExpGolombCodedInt();
        }
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.readUnsignedExpGolombCodedInt();
        bitArray.readUnsignedExpGolombCodedInt();
        boolean scalingListEnabled = bitArray.readBit();
        if (scalingListEnabled && bitArray.readBit()) {
            skipScalingList(bitArray);
        }
        bitArray.skipBits(2);
        if (bitArray.readBit()) {
            bitArray.skipBits(8);
            bitArray.readUnsignedExpGolombCodedInt();
            bitArray.readUnsignedExpGolombCodedInt();
            bitArray.skipBits(1);
        }
        skipShortTermRefPicSets(bitArray);
        if (bitArray.readBit()) {
            for (int i3 = 0; i3 < bitArray.readUnsignedExpGolombCodedInt(); i3++) {
                int ltRefPicPocLsbSpsLength = log2MaxPicOrderCntLsbMinus4 + 4;
                bitArray.skipBits(ltRefPicPocLsbSpsLength + 1);
            }
        }
        bitArray.skipBits(2);
        float pixelWidthHeightRatio = 1.0f;
        if (bitArray.readBit() && bitArray.readBit()) {
            int aspectRatioIdc = bitArray.readBits(8);
            if (aspectRatioIdc == 255) {
                int sarWidth = bitArray.readBits(16);
                int sarHeight = bitArray.readBits(16);
                if (sarWidth != 0 && sarHeight != 0) {
                    pixelWidthHeightRatio = sarWidth / sarHeight;
                }
            } else if (aspectRatioIdc < NalUnitUtil.ASPECT_RATIO_IDC_VALUES.length) {
                pixelWidthHeightRatio = NalUnitUtil.ASPECT_RATIO_IDC_VALUES[aspectRatioIdc];
            } else {
                Log.w(TAG, "Unexpected aspect_ratio_idc value: " + aspectRatioIdc);
            }
        }
        return Format.createVideoSampleFormat(formatId, MimeTypes.VIDEO_H265, null, -1, -1, picWidthInLumaSamples, picHeightInLumaSamples, -1.0f, Collections.singletonList(csd), -1, pixelWidthHeightRatio, null);
    }

    private static void skipScalingList(ParsableNalUnitBitArray bitArray) {
        int sizeId = 0;
        while (sizeId < 4) {
            for (int matrixId = 0; matrixId < 6; matrixId += sizeId == 3 ? 3 : 1) {
                if (!bitArray.readBit()) {
                    bitArray.readUnsignedExpGolombCodedInt();
                } else {
                    int coefNum = Math.min(64, 1 << ((sizeId << 1) + 4));
                    if (sizeId > 1) {
                        bitArray.readSignedExpGolombCodedInt();
                    }
                    for (int i = 0; i < coefNum; i++) {
                        bitArray.readSignedExpGolombCodedInt();
                    }
                }
            }
            sizeId++;
        }
    }

    private static void skipShortTermRefPicSets(ParsableNalUnitBitArray bitArray) {
        int numShortTermRefPicSets = bitArray.readUnsignedExpGolombCodedInt();
        boolean interRefPicSetPredictionFlag = false;
        int previousNumDeltaPocs = 0;
        for (int stRpsIdx = 0; stRpsIdx < numShortTermRefPicSets; stRpsIdx++) {
            if (stRpsIdx != 0) {
                interRefPicSetPredictionFlag = bitArray.readBit();
            }
            if (interRefPicSetPredictionFlag) {
                bitArray.skipBits(1);
                bitArray.readUnsignedExpGolombCodedInt();
                for (int j = 0; j <= previousNumDeltaPocs; j++) {
                    if (bitArray.readBit()) {
                        bitArray.skipBits(1);
                    }
                }
            } else {
                int numNegativePics = bitArray.readUnsignedExpGolombCodedInt();
                int numPositivePics = bitArray.readUnsignedExpGolombCodedInt();
                previousNumDeltaPocs = numNegativePics + numPositivePics;
                for (int i = 0; i < numNegativePics; i++) {
                    bitArray.readUnsignedExpGolombCodedInt();
                    bitArray.skipBits(1);
                }
                for (int i2 = 0; i2 < numPositivePics; i2++) {
                    bitArray.readUnsignedExpGolombCodedInt();
                    bitArray.skipBits(1);
                }
            }
        }
    }

    private static final class SampleReader {
        private static final int FIRST_SLICE_FLAG_OFFSET = 2;
        private boolean isFirstParameterSet;
        private boolean isFirstSlice;
        private boolean lookingForFirstSliceFlag;
        private int nalUnitBytesRead;
        private boolean nalUnitHasKeyframeData;
        private long nalUnitStartPosition;
        private long nalUnitTimeUs;
        private final TrackOutput output;
        private boolean readingSample;
        private boolean sampleIsKeyframe;
        private long samplePosition;
        private long sampleTimeUs;
        private boolean writingParameterSets;

        public SampleReader(TrackOutput output) {
            this.output = output;
        }

        public void reset() {
            this.lookingForFirstSliceFlag = false;
            this.isFirstSlice = false;
            this.isFirstParameterSet = false;
            this.readingSample = false;
            this.writingParameterSets = false;
        }

        public void startNalUnit(long position, int offset, int nalUnitType, long pesTimeUs) {
            this.isFirstSlice = false;
            this.isFirstParameterSet = false;
            this.nalUnitTimeUs = pesTimeUs;
            this.nalUnitBytesRead = 0;
            this.nalUnitStartPosition = position;
            if (nalUnitType >= 32) {
                if (!this.writingParameterSets && this.readingSample) {
                    outputSample(offset);
                    this.readingSample = false;
                }
                if (nalUnitType <= 34) {
                    this.isFirstParameterSet = !this.writingParameterSets;
                    this.writingParameterSets = true;
                }
            }
            this.nalUnitHasKeyframeData = nalUnitType >= 16 && nalUnitType <= 21;
            this.lookingForFirstSliceFlag = this.nalUnitHasKeyframeData || nalUnitType <= 9;
        }

        public void readNalUnitData(byte[] data, int offset, int limit) {
            if (this.lookingForFirstSliceFlag) {
                int headerOffset = (offset + 2) - this.nalUnitBytesRead;
                if (headerOffset < limit) {
                    this.isFirstSlice = (data[headerOffset] & 128) != 0;
                    this.lookingForFirstSliceFlag = false;
                } else {
                    this.nalUnitBytesRead += limit - offset;
                }
            }
        }

        public void endNalUnit(long position, int offset) {
            if (this.writingParameterSets && this.isFirstSlice) {
                this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
                this.writingParameterSets = false;
            } else if (this.isFirstParameterSet || this.isFirstSlice) {
                if (this.readingSample) {
                    int nalUnitLength = (int) (position - this.nalUnitStartPosition);
                    outputSample(offset + nalUnitLength);
                }
                this.samplePosition = this.nalUnitStartPosition;
                this.sampleTimeUs = this.nalUnitTimeUs;
                this.readingSample = true;
                this.sampleIsKeyframe = this.nalUnitHasKeyframeData;
            }
        }

        private void outputSample(int offset) {
            int flags = this.sampleIsKeyframe ? 1 : 0;
            int size = (int) (this.nalUnitStartPosition - this.samplePosition);
            this.output.sampleMetadata(this.sampleTimeUs, flags, size, offset, null);
        }
    }
}
