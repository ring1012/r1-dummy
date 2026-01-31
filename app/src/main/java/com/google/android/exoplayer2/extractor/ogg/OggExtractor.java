package com.google.android.exoplayer2.extractor.ogg;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.io.IOException;

/* loaded from: classes.dex */
public class OggExtractor implements Extractor {
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() { // from class: com.google.android.exoplayer2.extractor.ogg.OggExtractor.1
        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
        public Extractor[] createExtractors() {
            return new Extractor[]{new OggExtractor()};
        }
    };
    private static final int MAX_VERIFICATION_BYTES = 8;
    private StreamReader streamReader;

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        try {
            OggPageHeader header = new OggPageHeader();
            if (!header.populate(input, true) || (header.type & 2) != 2) {
                return false;
            }
            int length = Math.min(header.bodySize, 8);
            ParsableByteArray scratch = new ParsableByteArray(length);
            input.peekFully(scratch.data, 0, length);
            if (FlacReader.verifyBitstreamType(resetPosition(scratch))) {
                this.streamReader = new FlacReader();
            } else if (VorbisReader.verifyBitstreamType(resetPosition(scratch))) {
                this.streamReader = new VorbisReader();
            } else {
                if (!OpusReader.verifyBitstreamType(resetPosition(scratch))) {
                    return false;
                }
                this.streamReader = new OpusReader();
            }
            return true;
        } catch (ParserException e) {
            return false;
        }
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput output) {
        TrackOutput trackOutput = output.track(0, 1);
        output.endTracks();
        this.streamReader.init(output, trackOutput);
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long position, long timeUs) {
        this.streamReader.seek(position, timeUs);
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        return this.streamReader.read(input, seekPosition);
    }

    StreamReader getStreamReader() {
        return this.streamReader;
    }

    private static ParsableByteArray resetPosition(ParsableByteArray scratch) {
        scratch.setPosition(0);
        return scratch;
    }
}
