package com.google.android.exoplayer2.text.cea;

import android.util.Log;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;

/* loaded from: classes.dex */
public final class CeaUtil {
    private static final int COUNTRY_CODE = 181;
    private static final int PAYLOAD_TYPE_CC = 4;
    private static final int PROVIDER_CODE = 49;
    private static final String TAG = "CeaUtil";
    private static final int USER_DATA_TYPE_CODE = 3;
    private static final int USER_ID = 1195456820;

    public static void consume(long presentationTimeUs, ParsableByteArray seiBuffer, TrackOutput[] outputs) {
        while (seiBuffer.bytesLeft() > 1) {
            int payloadType = readNon255TerminatedValue(seiBuffer);
            int payloadSize = readNon255TerminatedValue(seiBuffer);
            if (payloadSize == -1 || payloadSize > seiBuffer.bytesLeft()) {
                Log.w(TAG, "Skipping remainder of malformed SEI NAL unit.");
                seiBuffer.setPosition(seiBuffer.limit());
            } else if (isSeiMessageCea608(payloadType, payloadSize, seiBuffer)) {
                seiBuffer.skipBytes(8);
                int ccCount = seiBuffer.readUnsignedByte() & 31;
                seiBuffer.skipBytes(1);
                int sampleLength = ccCount * 3;
                int sampleStartPosition = seiBuffer.getPosition();
                for (TrackOutput output : outputs) {
                    seiBuffer.setPosition(sampleStartPosition);
                    output.sampleData(seiBuffer, sampleLength);
                    output.sampleMetadata(presentationTimeUs, 1, sampleLength, 0, null);
                }
                seiBuffer.skipBytes(payloadSize - ((ccCount * 3) + 10));
            } else {
                seiBuffer.skipBytes(payloadSize);
            }
        }
    }

    private static int readNon255TerminatedValue(ParsableByteArray buffer) {
        int value = 0;
        while (buffer.bytesLeft() != 0) {
            int b = buffer.readUnsignedByte();
            value += b;
            if (b != 255) {
                return value;
            }
        }
        return -1;
    }

    private static boolean isSeiMessageCea608(int payloadType, int payloadLength, ParsableByteArray payload) {
        if (payloadType != 4 || payloadLength < 8) {
            return false;
        }
        int startPosition = payload.getPosition();
        int countryCode = payload.readUnsignedByte();
        int providerCode = payload.readUnsignedShort();
        int userIdentifier = payload.readInt();
        int userDataTypeCode = payload.readUnsignedByte();
        payload.setPosition(startPosition);
        return countryCode == COUNTRY_CODE && providerCode == PROVIDER_CODE && userIdentifier == USER_ID && userDataTypeCode == 3;
    }

    private CeaUtil() {
    }
}
