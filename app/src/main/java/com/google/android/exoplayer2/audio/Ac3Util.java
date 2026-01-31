package com.google.android.exoplayer2.audio;

import cn.yunzhisheng.asr.JniUscClient;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableBitArray;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.unisound.common.ad;
import com.unisound.sdk.bw;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public final class Ac3Util {
    private static final int AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT = 1536;
    private static final int AUDIO_SAMPLES_PER_AUDIO_BLOCK = 256;
    private static final int[] BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD = {1, 2, 3, 6};
    private static final int[] SAMPLE_RATE_BY_FSCOD = {48000, 44100, 32000};
    private static final int[] SAMPLE_RATE_BY_FSCOD2 = {24000, bw.k, 16000};
    private static final int[] CHANNEL_COUNT_BY_ACMOD = {2, 1, 2, 3, 3, 4, 4, 5};
    private static final int[] BITRATE_BY_HALF_FRMSIZECOD = {32, 40, 48, 56, 64, 80, 96, ad.s, 128, 160, PsExtractor.AUDIO_STREAM, 224, 256, JniUscClient.au, 384, 448, 512, 576, 640};
    private static final int[] SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1 = {69, 87, 104, 121, 139, 174, 208, 243, 278, 348, 417, 487, 557, 696, 835, 975, 1114, 1253, 1393};

    public static final class Ac3SyncFrameInfo {
        public final int channelCount;
        public final int frameSize;
        public final String mimeType;
        public final int sampleCount;
        public final int sampleRate;

        private Ac3SyncFrameInfo(String mimeType, int channelCount, int sampleRate, int frameSize, int sampleCount) {
            this.mimeType = mimeType;
            this.channelCount = channelCount;
            this.sampleRate = sampleRate;
            this.frameSize = frameSize;
            this.sampleCount = sampleCount;
        }
    }

    public static Format parseAc3AnnexFFormat(ParsableByteArray data, String trackId, String language, DrmInitData drmInitData) {
        int fscod = (data.readUnsignedByte() & PsExtractor.AUDIO_STREAM) >> 6;
        int sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
        int nextByte = data.readUnsignedByte();
        int channelCount = CHANNEL_COUNT_BY_ACMOD[(nextByte & 56) >> 3];
        if ((nextByte & 4) != 0) {
            channelCount++;
        }
        return Format.createAudioSampleFormat(trackId, MimeTypes.AUDIO_AC3, null, -1, -1, channelCount, sampleRate, null, drmInitData, 0, language);
    }

    public static Format parseEAc3AnnexFFormat(ParsableByteArray data, String trackId, String language, DrmInitData drmInitData) {
        data.skipBytes(2);
        int fscod = (data.readUnsignedByte() & PsExtractor.AUDIO_STREAM) >> 6;
        int sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
        int nextByte = data.readUnsignedByte();
        int channelCount = CHANNEL_COUNT_BY_ACMOD[(nextByte & 14) >> 1];
        if ((nextByte & 1) != 0) {
            channelCount++;
        }
        return Format.createAudioSampleFormat(trackId, MimeTypes.AUDIO_E_AC3, null, -1, -1, channelCount, sampleRate, null, drmInitData, 0, language);
    }

    public static Ac3SyncFrameInfo parseAc3SyncframeInfo(ParsableBitArray data) {
        String mimeType;
        int frameSize;
        int acmod;
        int sampleRate;
        int sampleCount;
        int audioBlocks;
        int initialPosition = data.getPosition();
        data.skipBits(40);
        boolean isEac3 = data.readBits(5) == 16;
        data.setPosition(initialPosition);
        if (isEac3) {
            mimeType = MimeTypes.AUDIO_E_AC3;
            data.skipBits(21);
            frameSize = (data.readBits(11) + 1) * 2;
            int fscod = data.readBits(2);
            if (fscod == 3) {
                sampleRate = SAMPLE_RATE_BY_FSCOD2[data.readBits(2)];
                audioBlocks = 6;
            } else {
                int numblkscod = data.readBits(2);
                audioBlocks = BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[numblkscod];
                sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
            }
            sampleCount = audioBlocks * 256;
            acmod = data.readBits(3);
        } else {
            mimeType = MimeTypes.AUDIO_AC3;
            data.skipBits(32);
            int fscod2 = data.readBits(2);
            int frmsizecod = data.readBits(6);
            frameSize = getAc3SyncframeSize(fscod2, frmsizecod);
            data.skipBits(8);
            acmod = data.readBits(3);
            if ((acmod & 1) != 0 && acmod != 1) {
                data.skipBits(2);
            }
            if ((acmod & 4) != 0) {
                data.skipBits(2);
            }
            if (acmod == 2) {
                data.skipBits(2);
            }
            sampleRate = SAMPLE_RATE_BY_FSCOD[fscod2];
            sampleCount = AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT;
        }
        boolean lfeon = data.readBit();
        int channelCount = CHANNEL_COUNT_BY_ACMOD[acmod] + (lfeon ? 1 : 0);
        return new Ac3SyncFrameInfo(mimeType, channelCount, sampleRate, frameSize, sampleCount);
    }

    public static int parseAc3SyncframeSize(byte[] data) {
        if (data.length < 5) {
            return -1;
        }
        int fscod = (data[4] & 192) >> 6;
        int frmsizecod = data[4] & 63;
        return getAc3SyncframeSize(fscod, frmsizecod);
    }

    public static int getAc3SyncframeAudioSampleCount() {
        return AC3_SYNCFRAME_AUDIO_SAMPLE_COUNT;
    }

    public static int parseEAc3SyncframeAudioSampleCount(ByteBuffer buffer) {
        int fscod = (buffer.get(buffer.position() + 4) & 192) >> 6;
        return (fscod == 3 ? 6 : BLOCKS_PER_SYNCFRAME_BY_NUMBLKSCOD[(buffer.get(buffer.position() + 4) & 48) >> 4]) * 256;
    }

    private static int getAc3SyncframeSize(int fscod, int frmsizecod) {
        int halfFrmsizecod = frmsizecod / 2;
        if (fscod < 0 || fscod >= SAMPLE_RATE_BY_FSCOD.length || frmsizecod < 0 || halfFrmsizecod >= SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1.length) {
            return -1;
        }
        int sampleRate = SAMPLE_RATE_BY_FSCOD[fscod];
        if (sampleRate == 44100) {
            return (SYNCFRAME_SIZE_WORDS_BY_HALF_FRMSIZECOD_44_1[halfFrmsizecod] + (frmsizecod % 2)) * 2;
        }
        int bitrate = BITRATE_BY_HALF_FRMSIZECOD[halfFrmsizecod];
        if (sampleRate == 32000) {
            return bitrate * 6;
        }
        return bitrate * 4;
    }

    private Ac3Util() {
    }
}
