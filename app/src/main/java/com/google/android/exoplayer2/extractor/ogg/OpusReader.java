package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ogg.StreamReader;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
final class OpusReader extends StreamReader {
    private static final int DEFAULT_SEEK_PRE_ROLL_SAMPLES = 3840;
    private static final int OPUS_CODE = Util.getIntegerCodeForString("Opus");
    private static final byte[] OPUS_SIGNATURE = {79, 112, 117, 115, 72, 101, 97, 100};
    private static final int SAMPLE_RATE = 48000;
    private boolean headerRead;

    OpusReader() {
    }

    public static boolean verifyBitstreamType(ParsableByteArray data) {
        if (data.bytesLeft() < OPUS_SIGNATURE.length) {
            return false;
        }
        byte[] header = new byte[OPUS_SIGNATURE.length];
        data.readBytes(header, 0, OPUS_SIGNATURE.length);
        return Arrays.equals(header, OPUS_SIGNATURE);
    }

    @Override // com.google.android.exoplayer2.extractor.ogg.StreamReader
    protected void reset(boolean headerData) {
        super.reset(headerData);
        if (headerData) {
            this.headerRead = false;
        }
    }

    @Override // com.google.android.exoplayer2.extractor.ogg.StreamReader
    protected long preparePayload(ParsableByteArray packet) {
        return convertTimeToGranule(getPacketDurationUs(packet.data));
    }

    @Override // com.google.android.exoplayer2.extractor.ogg.StreamReader
    protected boolean readHeaders(ParsableByteArray packet, long position, StreamReader.SetupData setupData) throws InterruptedException, IOException {
        if (!this.headerRead) {
            byte[] metadata = Arrays.copyOf(packet.data, packet.limit());
            int channelCount = metadata[9] & 255;
            int preskip = ((metadata[11] & 255) << 8) | (metadata[10] & 255);
            List<byte[]> initializationData = new ArrayList<>(3);
            initializationData.add(metadata);
            putNativeOrderLong(initializationData, preskip);
            putNativeOrderLong(initializationData, DEFAULT_SEEK_PRE_ROLL_SAMPLES);
            setupData.format = Format.createAudioSampleFormat(null, MimeTypes.AUDIO_OPUS, null, -1, -1, channelCount, SAMPLE_RATE, initializationData, null, 0, null);
            this.headerRead = true;
            return true;
        }
        boolean headerPacket = packet.readInt() == OPUS_CODE;
        packet.setPosition(0);
        return headerPacket;
    }

    private void putNativeOrderLong(List<byte[]> initializationData, int samples) {
        long ns = (samples * C.NANOS_PER_SECOND) / 48000;
        byte[] array = ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(ns).array();
        initializationData.add(array);
    }

    private long getPacketDurationUs(byte[] packet) {
        int frames;
        int length;
        int toc = packet[0] & 255;
        switch (toc & 3) {
            case 0:
                frames = 1;
                break;
            case 1:
            case 2:
                frames = 2;
                break;
            default:
                frames = packet[1] & 63;
                break;
        }
        int config = toc >> 3;
        int length2 = config & 3;
        if (config >= 16) {
            length = 2500 << length2;
        } else if (config >= 12) {
            length = 10000 << (length2 & 1);
        } else if (length2 == 3) {
            length = 60000;
        } else {
            length = 10000 << length2;
        }
        return frames * length;
    }
}
