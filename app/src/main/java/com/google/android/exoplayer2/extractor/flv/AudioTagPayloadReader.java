package com.google.android.exoplayer2.extractor.flv;

import android.util.Pair;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.flv.TagPayloadReader;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.unisound.sdk.bw;
import java.util.Collections;

/* loaded from: classes.dex */
final class AudioTagPayloadReader extends TagPayloadReader {
    private static final int AAC_PACKET_TYPE_AAC_RAW = 1;
    private static final int AAC_PACKET_TYPE_SEQUENCE_HEADER = 0;
    private static final int AUDIO_FORMAT_AAC = 10;
    private static final int AUDIO_FORMAT_ALAW = 7;
    private static final int AUDIO_FORMAT_MP3 = 2;
    private static final int AUDIO_FORMAT_ULAW = 8;
    private static final int[] AUDIO_SAMPLING_RATE_TABLE = {5512, 11025, bw.k, 44100};
    private int audioFormat;
    private boolean hasOutputFormat;
    private boolean hasParsedAudioDataHeader;

    public AudioTagPayloadReader(TrackOutput output) {
        super(output);
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    public void seek() {
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected boolean parseHeader(ParsableByteArray data) throws TagPayloadReader.UnsupportedFormatException {
        if (!this.hasParsedAudioDataHeader) {
            int header = data.readUnsignedByte();
            this.audioFormat = (header >> 4) & 15;
            if (this.audioFormat == 2) {
                int sampleRateIndex = (header >> 2) & 3;
                int sampleRate = AUDIO_SAMPLING_RATE_TABLE[sampleRateIndex];
                Format format = Format.createAudioSampleFormat(null, MimeTypes.AUDIO_MPEG, null, -1, -1, 1, sampleRate, null, null, 0, null);
                this.output.format(format);
                this.hasOutputFormat = true;
            } else if (this.audioFormat == 7 || this.audioFormat == 8) {
                String type = this.audioFormat == 7 ? MimeTypes.AUDIO_ALAW : MimeTypes.AUDIO_ULAW;
                int pcmEncoding = (header & 1) == 1 ? 2 : 3;
                Format format2 = Format.createAudioSampleFormat(null, type, null, -1, -1, 1, 8000, pcmEncoding, null, null, 0, null);
                this.output.format(format2);
                this.hasOutputFormat = true;
            } else if (this.audioFormat != 10) {
                throw new TagPayloadReader.UnsupportedFormatException("Audio format not supported: " + this.audioFormat);
            }
            this.hasParsedAudioDataHeader = true;
            return true;
        }
        data.skipBytes(1);
        return true;
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected void parsePayload(ParsableByteArray data, long timeUs) {
        if (this.audioFormat == 2) {
            int sampleSize = data.bytesLeft();
            this.output.sampleData(data, sampleSize);
            this.output.sampleMetadata(timeUs, 1, sampleSize, 0, null);
            return;
        }
        int packetType = data.readUnsignedByte();
        if (packetType == 0 && !this.hasOutputFormat) {
            byte[] audioSpecificConfig = new byte[data.bytesLeft()];
            data.readBytes(audioSpecificConfig, 0, audioSpecificConfig.length);
            Pair<Integer, Integer> audioParams = CodecSpecificDataUtil.parseAacAudioSpecificConfig(audioSpecificConfig);
            Format format = Format.createAudioSampleFormat(null, MimeTypes.AUDIO_AAC, null, -1, -1, ((Integer) audioParams.second).intValue(), ((Integer) audioParams.first).intValue(), Collections.singletonList(audioSpecificConfig), null, 0, null);
            this.output.format(format);
            this.hasOutputFormat = true;
            return;
        }
        if (this.audioFormat != 10 || packetType == 1) {
            int sampleSize2 = data.bytesLeft();
            this.output.sampleData(data, sampleSize2);
            this.output.sampleMetadata(timeUs, 1, sampleSize2, 0, null);
        }
    }
}
