package com.google.android.exoplayer2.extractor.wav;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.IOException;

/* loaded from: classes.dex */
public final class WavExtractor implements Extractor, SeekMap {
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() { // from class: com.google.android.exoplayer2.extractor.wav.WavExtractor.1
        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
        public Extractor[] createExtractors() {
            return new Extractor[]{new WavExtractor()};
        }
    };
    private static final int MAX_INPUT_SIZE = 32768;
    private int bytesPerFrame;
    private ExtractorOutput extractorOutput;
    private int pendingBytes;
    private TrackOutput trackOutput;
    private WavHeader wavHeader;

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        return WavHeaderReader.peek(input) != null;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
        this.trackOutput = output.track(0, 1);
        this.wavHeader = null;
        output.endTracks();
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long position, long timeUs) {
        this.pendingBytes = 0;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        if (this.wavHeader == null) {
            this.wavHeader = WavHeaderReader.peek(input);
            if (this.wavHeader == null) {
                throw new ParserException("Unsupported or unrecognized wav header.");
            }
            Format format = Format.createAudioSampleFormat(null, MimeTypes.AUDIO_RAW, null, this.wavHeader.getBitrate(), 32768, this.wavHeader.getNumChannels(), this.wavHeader.getSampleRateHz(), this.wavHeader.getEncoding(), null, null, 0, null);
            this.trackOutput.format(format);
            this.bytesPerFrame = this.wavHeader.getBytesPerFrame();
        }
        if (!this.wavHeader.hasDataBounds()) {
            WavHeaderReader.skipToData(input, this.wavHeader);
            this.extractorOutput.seekMap(this);
        }
        int bytesAppended = this.trackOutput.sampleData(input, 32768 - this.pendingBytes, true);
        if (bytesAppended != -1) {
            this.pendingBytes += bytesAppended;
        }
        int pendingFrames = this.pendingBytes / this.bytesPerFrame;
        if (pendingFrames > 0) {
            long timeUs = this.wavHeader.getTimeUs(input.getPosition() - this.pendingBytes);
            int size = pendingFrames * this.bytesPerFrame;
            this.pendingBytes -= size;
            this.trackOutput.sampleMetadata(timeUs, 1, size, this.pendingBytes, null);
        }
        return bytesAppended == -1 ? -1 : 0;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public long getDurationUs() {
        return this.wavHeader.getDurationUs();
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public boolean isSeekable() {
        return true;
    }

    @Override // com.google.android.exoplayer2.extractor.SeekMap
    public long getPosition(long timeUs) {
        return this.wavHeader.getPosition(timeUs);
    }
}
