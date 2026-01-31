package com.google.android.exoplayer2.extractor;

import com.google.android.exoplayer2.extractor.flv.FlvExtractor;
import com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer2.extractor.ogg.OggExtractor;
import com.google.android.exoplayer2.extractor.ts.Ac3Extractor;
import com.google.android.exoplayer2.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.extractor.wav.WavExtractor;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public final class DefaultExtractorsFactory implements ExtractorsFactory {
    private static final Constructor<? extends Extractor> FLAC_EXTRACTOR_CONSTRUCTOR;
    private int fragmentedMp4Flags;
    private int matroskaFlags;
    private int mp3Flags;
    private int tsFlags;

    static {
        Constructor<? extends Extractor> flacExtractorConstructor = null;
        try {
            flacExtractorConstructor = Class.forName("com.google.android.exoplayer2.ext.flac.FlacExtractor").asSubclass(Extractor.class).getConstructor(new Class[0]);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e2) {
        }
        FLAC_EXTRACTOR_CONSTRUCTOR = flacExtractorConstructor;
    }

    public synchronized DefaultExtractorsFactory setMatroskaExtractorFlags(int flags) {
        this.matroskaFlags = flags;
        return this;
    }

    public synchronized DefaultExtractorsFactory setFragmentedMp4ExtractorFlags(int flags) {
        this.fragmentedMp4Flags = flags;
        return this;
    }

    public synchronized DefaultExtractorsFactory setMp3ExtractorFlags(int flags) {
        this.mp3Flags = flags;
        return this;
    }

    public synchronized DefaultExtractorsFactory setTsExtractorFlags(int flags) {
        this.tsFlags = flags;
        return this;
    }

    @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
    public synchronized Extractor[] createExtractors() {
        Extractor[] extractors;
        synchronized (this) {
            extractors = new Extractor[FLAC_EXTRACTOR_CONSTRUCTOR != null ? 12 : 11];
            extractors[0] = new MatroskaExtractor(this.matroskaFlags);
            extractors[1] = new FragmentedMp4Extractor(this.fragmentedMp4Flags);
            extractors[2] = new Mp4Extractor();
            extractors[3] = new Mp3Extractor(this.mp3Flags);
            extractors[4] = new AdtsExtractor();
            extractors[5] = new Ac3Extractor();
            extractors[6] = new TsExtractor(this.tsFlags);
            extractors[7] = new FlvExtractor();
            extractors[8] = new OggExtractor();
            extractors[9] = new PsExtractor();
            extractors[10] = new WavExtractor();
            if (FLAC_EXTRACTOR_CONSTRUCTOR != null) {
                try {
                    extractors[11] = FLAC_EXTRACTOR_CONSTRUCTOR.newInstance(new Object[0]);
                } catch (Exception e) {
                    throw new IllegalStateException("Unexpected error creating FLAC extractor", e);
                }
            }
        }
        return extractors;
    }
}
