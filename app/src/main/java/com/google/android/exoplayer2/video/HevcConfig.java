package com.google.android.exoplayer2.video;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class HevcConfig {
    public final List<byte[]> initializationData;
    public final int nalUnitLengthFieldLength;

    public static HevcConfig parse(ParsableByteArray data) throws ParserException {
        try {
            data.skipBytes(21);
            int lengthSizeMinusOne = data.readUnsignedByte() & 3;
            int numberOfArrays = data.readUnsignedByte();
            int csdLength = 0;
            int csdStartPosition = data.getPosition();
            for (int i = 0; i < numberOfArrays; i++) {
                data.skipBytes(1);
                int numberOfNalUnits = data.readUnsignedShort();
                for (int j = 0; j < numberOfNalUnits; j++) {
                    int nalUnitLength = data.readUnsignedShort();
                    csdLength += nalUnitLength + 4;
                    data.skipBytes(nalUnitLength);
                }
            }
            data.setPosition(csdStartPosition);
            byte[] buffer = new byte[csdLength];
            int bufferPosition = 0;
            for (int i2 = 0; i2 < numberOfArrays; i2++) {
                data.skipBytes(1);
                int numberOfNalUnits2 = data.readUnsignedShort();
                for (int j2 = 0; j2 < numberOfNalUnits2; j2++) {
                    int nalUnitLength2 = data.readUnsignedShort();
                    System.arraycopy(NalUnitUtil.NAL_START_CODE, 0, buffer, bufferPosition, NalUnitUtil.NAL_START_CODE.length);
                    int bufferPosition2 = bufferPosition + NalUnitUtil.NAL_START_CODE.length;
                    System.arraycopy(data.data, data.getPosition(), buffer, bufferPosition2, nalUnitLength2);
                    bufferPosition = bufferPosition2 + nalUnitLength2;
                    data.skipBytes(nalUnitLength2);
                }
            }
            List<byte[]> initializationData = csdLength == 0 ? null : Collections.singletonList(buffer);
            return new HevcConfig(initializationData, lengthSizeMinusOne + 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ParserException("Error parsing HEVC config", e);
        }
    }

    private HevcConfig(List<byte[]> initializationData, int nalUnitLengthFieldLength) {
        this.initializationData = initializationData;
        this.nalUnitLengthFieldLength = nalUnitLengthFieldLength;
    }
}
