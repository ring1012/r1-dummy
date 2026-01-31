package com.google.android.exoplayer2.extractor.wav;

import android.util.Log;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;

/* loaded from: classes.dex */
final class WavHeaderReader {
    private static final String TAG = "WavHeaderReader";
    private static final int TYPE_PCM = 1;
    private static final int TYPE_WAVE_FORMAT_EXTENSIBLE = 65534;

    WavHeaderReader() {
    }

    public static WavHeader peek(ExtractorInput input) throws InterruptedException, IOException {
        Assertions.checkNotNull(input);
        ParsableByteArray scratch = new ParsableByteArray(16);
        ChunkHeader chunkHeader = ChunkHeader.peek(input, scratch);
        if (chunkHeader.id != Util.getIntegerCodeForString("RIFF")) {
            return null;
        }
        input.peekFully(scratch.data, 0, 4);
        scratch.setPosition(0);
        int riffFormat = scratch.readInt();
        if (riffFormat != Util.getIntegerCodeForString("WAVE")) {
            Log.e(TAG, "Unsupported RIFF format: " + riffFormat);
            return null;
        }
        ChunkHeader chunkHeader2 = ChunkHeader.peek(input, scratch);
        while (chunkHeader2.id != Util.getIntegerCodeForString("fmt ")) {
            input.advancePeekPosition((int) chunkHeader2.size);
            chunkHeader2 = ChunkHeader.peek(input, scratch);
        }
        Assertions.checkState(chunkHeader2.size >= 16);
        input.peekFully(scratch.data, 0, 16);
        scratch.setPosition(0);
        int type = scratch.readLittleEndianUnsignedShort();
        int numChannels = scratch.readLittleEndianUnsignedShort();
        int sampleRateHz = scratch.readLittleEndianUnsignedIntToInt();
        int averageBytesPerSecond = scratch.readLittleEndianUnsignedIntToInt();
        int blockAlignment = scratch.readLittleEndianUnsignedShort();
        int bitsPerSample = scratch.readLittleEndianUnsignedShort();
        int expectedBlockAlignment = (numChannels * bitsPerSample) / 8;
        if (blockAlignment != expectedBlockAlignment) {
            throw new ParserException("Expected block alignment: " + expectedBlockAlignment + "; got: " + blockAlignment);
        }
        int encoding = Util.getPcmEncoding(bitsPerSample);
        if (encoding == 0) {
            Log.e(TAG, "Unsupported WAV bit depth: " + bitsPerSample);
            return null;
        }
        if (type != 1 && type != TYPE_WAVE_FORMAT_EXTENSIBLE) {
            Log.e(TAG, "Unsupported WAV format type: " + type);
            return null;
        }
        input.advancePeekPosition(((int) chunkHeader2.size) - 16);
        return new WavHeader(numChannels, sampleRateHz, averageBytesPerSecond, blockAlignment, bitsPerSample, encoding);
    }

    public static void skipToData(ExtractorInput input, WavHeader wavHeader) throws InterruptedException, IOException {
        Assertions.checkNotNull(input);
        Assertions.checkNotNull(wavHeader);
        input.resetPeekPosition();
        ParsableByteArray scratch = new ParsableByteArray(8);
        ChunkHeader chunkHeader = ChunkHeader.peek(input, scratch);
        while (chunkHeader.id != Util.getIntegerCodeForString("data")) {
            Log.w(TAG, "Ignoring unknown WAV chunk: " + chunkHeader.id);
            long bytesToSkip = 8 + chunkHeader.size;
            if (chunkHeader.id == Util.getIntegerCodeForString("RIFF")) {
                bytesToSkip = 12;
            }
            if (bytesToSkip > 2147483647L) {
                throw new ParserException("Chunk is too large (~2GB+) to skip; id: " + chunkHeader.id);
            }
            input.skipFully((int) bytesToSkip);
            chunkHeader = ChunkHeader.peek(input, scratch);
        }
        input.skipFully(8);
        wavHeader.setDataBounds(input.getPosition(), chunkHeader.size);
    }

    private static final class ChunkHeader {
        public static final int SIZE_IN_BYTES = 8;
        public final int id;
        public final long size;

        private ChunkHeader(int id, long size) {
            this.id = id;
            this.size = size;
        }

        public static ChunkHeader peek(ExtractorInput input, ParsableByteArray scratch) throws InterruptedException, IOException {
            input.peekFully(scratch.data, 0, 8);
            scratch.setPosition(0);
            int id = scratch.readInt();
            long size = scratch.readLittleEndianUnsignedInt();
            return new ChunkHeader(id, size);
        }
    }
}
