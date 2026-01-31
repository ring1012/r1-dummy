package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ogg.StreamReader;
import com.google.android.exoplayer2.extractor.ogg.VorbisUtil;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes.dex */
final class VorbisReader extends StreamReader {
    private VorbisUtil.CommentHeader commentHeader;
    private int previousPacketBlockSize;
    private boolean seenFirstAudioPacket;
    private VorbisUtil.VorbisIdHeader vorbisIdHeader;
    private VorbisSetup vorbisSetup;

    VorbisReader() {
    }

    public static boolean verifyBitstreamType(ParsableByteArray data) {
        try {
            return VorbisUtil.verifyVorbisHeaderCapturePattern(1, data, true);
        } catch (ParserException e) {
            return false;
        }
    }

    @Override // com.google.android.exoplayer2.extractor.ogg.StreamReader
    protected void reset(boolean headerData) {
        super.reset(headerData);
        if (headerData) {
            this.vorbisSetup = null;
            this.vorbisIdHeader = null;
            this.commentHeader = null;
        }
        this.previousPacketBlockSize = 0;
        this.seenFirstAudioPacket = false;
    }

    @Override // com.google.android.exoplayer2.extractor.ogg.StreamReader
    protected void onSeekEnd(long currentGranule) {
        super.onSeekEnd(currentGranule);
        this.seenFirstAudioPacket = currentGranule != 0;
        this.previousPacketBlockSize = this.vorbisIdHeader != null ? this.vorbisIdHeader.blockSize0 : 0;
    }

    @Override // com.google.android.exoplayer2.extractor.ogg.StreamReader
    protected long preparePayload(ParsableByteArray packet) {
        if ((packet.data[0] & 1) == 1) {
            return -1L;
        }
        int packetBlockSize = decodeBlockSize(packet.data[0], this.vorbisSetup);
        int samplesInPacket = this.seenFirstAudioPacket ? (this.previousPacketBlockSize + packetBlockSize) / 4 : 0;
        appendNumberOfSamples(packet, samplesInPacket);
        this.seenFirstAudioPacket = true;
        this.previousPacketBlockSize = packetBlockSize;
        return samplesInPacket;
    }

    @Override // com.google.android.exoplayer2.extractor.ogg.StreamReader
    protected boolean readHeaders(ParsableByteArray packet, long position, StreamReader.SetupData setupData) throws InterruptedException, IOException {
        if (this.vorbisSetup != null) {
            return false;
        }
        this.vorbisSetup = readSetupHeaders(packet);
        if (this.vorbisSetup == null) {
            return true;
        }
        ArrayList<byte[]> codecInitialisationData = new ArrayList<>();
        codecInitialisationData.add(this.vorbisSetup.idHeader.data);
        codecInitialisationData.add(this.vorbisSetup.setupHeaderData);
        setupData.format = Format.createAudioSampleFormat(null, MimeTypes.AUDIO_VORBIS, null, this.vorbisSetup.idHeader.bitrateNominal, OggPageHeader.MAX_PAGE_PAYLOAD, this.vorbisSetup.idHeader.channels, (int) this.vorbisSetup.idHeader.sampleRate, codecInitialisationData, null, 0, null);
        return true;
    }

    VorbisSetup readSetupHeaders(ParsableByteArray scratch) throws IOException {
        if (this.vorbisIdHeader == null) {
            this.vorbisIdHeader = VorbisUtil.readVorbisIdentificationHeader(scratch);
            return null;
        }
        if (this.commentHeader == null) {
            this.commentHeader = VorbisUtil.readVorbisCommentHeader(scratch);
            return null;
        }
        byte[] setupHeaderData = new byte[scratch.limit()];
        System.arraycopy(scratch.data, 0, setupHeaderData, 0, scratch.limit());
        VorbisUtil.Mode[] modes = VorbisUtil.readVorbisModes(scratch, this.vorbisIdHeader.channels);
        int iLogModes = VorbisUtil.iLog(modes.length - 1);
        return new VorbisSetup(this.vorbisIdHeader, this.commentHeader, setupHeaderData, modes, iLogModes);
    }

    static int readBits(byte src, int length, int leastSignificantBitIndex) {
        return (src >> leastSignificantBitIndex) & (255 >>> (8 - length));
    }

    static void appendNumberOfSamples(ParsableByteArray buffer, long packetSampleCount) {
        buffer.setLimit(buffer.limit() + 4);
        buffer.data[buffer.limit() - 4] = (byte) (packetSampleCount & 255);
        buffer.data[buffer.limit() - 3] = (byte) ((packetSampleCount >>> 8) & 255);
        buffer.data[buffer.limit() - 2] = (byte) ((packetSampleCount >>> 16) & 255);
        buffer.data[buffer.limit() - 1] = (byte) ((packetSampleCount >>> 24) & 255);
    }

    private static int decodeBlockSize(byte firstByteOfAudioPacket, VorbisSetup vorbisSetup) {
        int modeNumber = readBits(firstByteOfAudioPacket, vorbisSetup.iLogModes, 1);
        if (!vorbisSetup.modes[modeNumber].blockFlag) {
            int currentBlockSize = vorbisSetup.idHeader.blockSize0;
            return currentBlockSize;
        }
        int currentBlockSize2 = vorbisSetup.idHeader.blockSize1;
        return currentBlockSize2;
    }

    static final class VorbisSetup {
        public final VorbisUtil.CommentHeader commentHeader;
        public final int iLogModes;
        public final VorbisUtil.VorbisIdHeader idHeader;
        public final VorbisUtil.Mode[] modes;
        public final byte[] setupHeaderData;

        public VorbisSetup(VorbisUtil.VorbisIdHeader idHeader, VorbisUtil.CommentHeader commentHeader, byte[] setupHeaderData, VorbisUtil.Mode[] modes, int iLogModes) {
            this.idHeader = idHeader;
            this.commentHeader = commentHeader;
            this.setupHeaderData = setupHeaderData;
            this.modes = modes;
            this.iLogModes = iLogModes;
        }
    }
}
