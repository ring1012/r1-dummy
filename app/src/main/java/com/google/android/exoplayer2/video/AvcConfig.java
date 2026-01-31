package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class AvcConfig {
    public final int height;
    public final List<byte[]> initializationData;
    public final int nalUnitLengthFieldLength;
    public final float pixelWidthAspectRatio;
    public final int width;

    public static AvcConfig parse(ParsableByteArray data) throws ParserException {
        try {
            data.skipBytes(4);
            int nalUnitLengthFieldLength = (data.readUnsignedByte() & 3) + 1;
            if (nalUnitLengthFieldLength == 3) {
                throw new IllegalStateException();
            }
            List<byte[]> initializationData = new ArrayList<>();
            int numSequenceParameterSets = data.readUnsignedByte() & 31;
            for (int j = 0; j < numSequenceParameterSets; j++) {
                initializationData.add(buildNalUnitForChild(data));
            }
            int numPictureParameterSets = data.readUnsignedByte();
            for (int j2 = 0; j2 < numPictureParameterSets; j2++) {
                initializationData.add(buildNalUnitForChild(data));
            }
            int width = -1;
            int height = -1;
            float pixelWidthAspectRatio = 1.0f;
            if (numSequenceParameterSets > 0) {
                byte[] sps = initializationData.get(0);
                NalUnitUtil.SpsData spsData = NalUnitUtil.parseSpsNalUnit(initializationData.get(0), nalUnitLengthFieldLength, sps.length);
                width = spsData.width;
                height = spsData.height;
                pixelWidthAspectRatio = spsData.pixelWidthAspectRatio;
            }
            return new AvcConfig(initializationData, nalUnitLengthFieldLength, width, height, pixelWidthAspectRatio);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ParserException("Error parsing AVC config", e);
        }
    }

    private AvcConfig(List<byte[]> initializationData, int nalUnitLengthFieldLength, int width, int height, float pixelWidthAspectRatio) {
        this.initializationData = initializationData;
        this.nalUnitLengthFieldLength = nalUnitLengthFieldLength;
        this.width = width;
        this.height = height;
        this.pixelWidthAspectRatio = pixelWidthAspectRatio;
    }

    private static byte[] buildNalUnitForChild(ParsableByteArray data) {
        int length = data.readUnsignedShort();
        int offset = data.getPosition();
        data.skipBytes(length);
        return CodecSpecificDataUtil.buildNalUnit(data.data, offset, length);
    }
}
