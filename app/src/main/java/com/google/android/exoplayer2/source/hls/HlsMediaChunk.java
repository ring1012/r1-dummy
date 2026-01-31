package com.google.android.exoplayer2.source.hls;

import android.text.TextUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.DefaultExtractorInput;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.extractor.ts.Ac3Extractor;
import com.google.android.exoplayer2.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.metadata.id3.PrivFrame;
import com.google.android.exoplayer2.source.chunk.MediaChunk;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
final class HlsMediaChunk extends MediaChunk {
    private static final String AAC_FILE_EXTENSION = ".aac";
    private static final String AC3_FILE_EXTENSION = ".ac3";
    private static final String EC3_FILE_EXTENSION = ".ec3";
    private static final String M4_FILE_EXTENSION_PREFIX = ".m4";
    private static final String MP3_FILE_EXTENSION = ".mp3";
    private static final String MP4_FILE_EXTENSION = ".mp4";
    private static final String PRIV_TIMESTAMP_FRAME_OWNER = "com.apple.streaming.transportStreamTimestamp";
    private static final AtomicInteger UID_SOURCE = new AtomicInteger();
    private static final String VTT_FILE_EXTENSION = ".vtt";
    private static final String WEBVTT_FILE_EXTENSION = ".webvtt";
    private int bytesLoaded;
    public final int discontinuitySequenceNumber;
    private Extractor extractor;
    private HlsSampleStreamWrapper extractorOutput;
    public final HlsMasterPlaylist.HlsUrl hlsUrl;
    private final ParsableByteArray id3Data;
    private final Id3Decoder id3Decoder;
    private final DataSource initDataSource;
    private final DataSpec initDataSpec;
    private boolean initLoadCompleted;
    private int initSegmentBytesLoaded;
    private final boolean isEncrypted;
    private final boolean isMasterTimestampSource;
    private final boolean isPackedAudio;
    private final String lastPathSegment;
    private volatile boolean loadCanceled;
    private volatile boolean loadCompleted;
    private final List<Format> muxedCaptionFormats;
    private final boolean needNewExtractor;
    private final Extractor previousExtractor;
    private final boolean shouldSpliceIn;
    private final TimestampAdjuster timestampAdjuster;
    public final int uid;

    public HlsMediaChunk(DataSource dataSource, DataSpec dataSpec, DataSpec initDataSpec, HlsMasterPlaylist.HlsUrl hlsUrl, List<Format> muxedCaptionFormats, int trackSelectionReason, Object trackSelectionData, long startTimeUs, long endTimeUs, int chunkIndex, int discontinuitySequenceNumber, boolean isMasterTimestampSource, TimestampAdjuster timestampAdjuster, HlsMediaChunk previousChunk, byte[] encryptionKey, byte[] encryptionIv) {
        super(buildDataSource(dataSource, encryptionKey, encryptionIv), dataSpec, hlsUrl.format, trackSelectionReason, trackSelectionData, startTimeUs, endTimeUs, chunkIndex);
        this.discontinuitySequenceNumber = discontinuitySequenceNumber;
        this.initDataSpec = initDataSpec;
        this.hlsUrl = hlsUrl;
        this.muxedCaptionFormats = muxedCaptionFormats;
        this.isMasterTimestampSource = isMasterTimestampSource;
        this.timestampAdjuster = timestampAdjuster;
        this.isEncrypted = this.dataSource instanceof Aes128DataSource;
        this.lastPathSegment = dataSpec.uri.getLastPathSegment();
        this.isPackedAudio = this.lastPathSegment.endsWith(AAC_FILE_EXTENSION) || this.lastPathSegment.endsWith(AC3_FILE_EXTENSION) || this.lastPathSegment.endsWith(EC3_FILE_EXTENSION) || this.lastPathSegment.endsWith(MP3_FILE_EXTENSION);
        if (previousChunk != null) {
            this.id3Decoder = previousChunk.id3Decoder;
            this.id3Data = previousChunk.id3Data;
            this.previousExtractor = previousChunk.extractor;
            this.shouldSpliceIn = previousChunk.hlsUrl != hlsUrl;
            this.needNewExtractor = previousChunk.discontinuitySequenceNumber != discontinuitySequenceNumber || this.shouldSpliceIn;
        } else {
            this.id3Decoder = this.isPackedAudio ? new Id3Decoder() : null;
            this.id3Data = this.isPackedAudio ? new ParsableByteArray(10) : null;
            this.previousExtractor = null;
            this.shouldSpliceIn = false;
            this.needNewExtractor = true;
        }
        this.initDataSource = dataSource;
        this.uid = UID_SOURCE.getAndIncrement();
    }

    public void init(HlsSampleStreamWrapper output) {
        this.extractorOutput = output;
        output.init(this.uid, this.shouldSpliceIn);
    }

    @Override // com.google.android.exoplayer2.source.chunk.MediaChunk
    public boolean isLoadCompleted() {
        return this.loadCompleted;
    }

    @Override // com.google.android.exoplayer2.source.chunk.Chunk
    public long bytesLoaded() {
        return this.bytesLoaded;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public void cancelLoad() {
        this.loadCanceled = true;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public boolean isLoadCanceled() {
        return this.loadCanceled;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Loadable
    public void load() throws InterruptedException, IOException {
        if (this.extractor == null && !this.isPackedAudio) {
            this.extractor = createExtractor();
        }
        maybeLoadInitData();
        if (!this.loadCanceled) {
            loadMedia();
        }
    }

    private void maybeLoadInitData() throws InterruptedException, IOException {
        if (this.previousExtractor != this.extractor && !this.initLoadCompleted && this.initDataSpec != null) {
            DataSpec initSegmentDataSpec = Util.getRemainderDataSpec(this.initDataSpec, this.initSegmentBytesLoaded);
            try {
                ExtractorInput input = new DefaultExtractorInput(this.initDataSource, initSegmentDataSpec.absoluteStreamPosition, this.initDataSource.open(initSegmentDataSpec));
                int result = 0;
                while (result == 0) {
                    try {
                        if (this.loadCanceled) {
                            break;
                        } else {
                            result = this.extractor.read(input, null);
                        }
                    } finally {
                        this.initSegmentBytesLoaded = (int) (input.getPosition() - this.initDataSpec.absoluteStreamPosition);
                    }
                }
                Util.closeQuietly(this.dataSource);
                this.initLoadCompleted = true;
            } catch (Throwable th) {
                Util.closeQuietly(this.dataSource);
                throw th;
            }
        }
    }

    private void loadMedia() throws InterruptedException, IOException {
        DataSpec loadDataSpec;
        boolean skipLoadedBytes;
        if (this.isEncrypted) {
            loadDataSpec = this.dataSpec;
            skipLoadedBytes = this.bytesLoaded != 0;
        } else {
            loadDataSpec = Util.getRemainderDataSpec(this.dataSpec, this.bytesLoaded);
            skipLoadedBytes = false;
        }
        if (!this.isMasterTimestampSource) {
            this.timestampAdjuster.waitUntilInitialized();
        } else if (this.timestampAdjuster.getFirstSampleTimestampUs() == Long.MAX_VALUE) {
            this.timestampAdjuster.setFirstSampleTimestampUs(this.startTimeUs);
        }
        try {
            ExtractorInput input = new DefaultExtractorInput(this.dataSource, loadDataSpec.absoluteStreamPosition, this.dataSource.open(loadDataSpec));
            if (this.extractor == null) {
                long id3Timestamp = peekId3PrivTimestamp(input);
                this.extractor = buildPackedAudioExtractor(id3Timestamp != C.TIME_UNSET ? this.timestampAdjuster.adjustTsTimestamp(id3Timestamp) : this.startTimeUs);
            }
            if (skipLoadedBytes) {
                input.skipFully(this.bytesLoaded);
            }
            int result = 0;
            while (result == 0) {
                try {
                    if (this.loadCanceled) {
                        break;
                    } else {
                        result = this.extractor.read(input, null);
                    }
                } finally {
                    this.bytesLoaded = (int) (input.getPosition() - this.dataSpec.absoluteStreamPosition);
                }
            }
            Util.closeQuietly(this.dataSource);
            this.loadCompleted = true;
        } catch (Throwable th) {
            Util.closeQuietly(this.dataSource);
            throw th;
        }
    }

    private long peekId3PrivTimestamp(ExtractorInput input) throws InterruptedException, IOException {
        Metadata metadata;
        input.resetPeekPosition();
        if (!input.peekFully(this.id3Data.data, 0, 10, true)) {
            return C.TIME_UNSET;
        }
        this.id3Data.reset(10);
        int id = this.id3Data.readUnsignedInt24();
        if (id != Id3Decoder.ID3_TAG) {
            return C.TIME_UNSET;
        }
        this.id3Data.skipBytes(3);
        int id3Size = this.id3Data.readSynchSafeInt();
        int requiredCapacity = id3Size + 10;
        if (requiredCapacity > this.id3Data.capacity()) {
            byte[] data = this.id3Data.data;
            this.id3Data.reset(requiredCapacity);
            System.arraycopy(data, 0, this.id3Data.data, 0, 10);
        }
        if (!input.peekFully(this.id3Data.data, 10, id3Size, true) || (metadata = this.id3Decoder.decode(this.id3Data.data, id3Size)) == null) {
            return C.TIME_UNSET;
        }
        int metadataLength = metadata.length();
        for (int i = 0; i < metadataLength; i++) {
            Metadata.Entry frame = metadata.get(i);
            if (frame instanceof PrivFrame) {
                PrivFrame privFrame = (PrivFrame) frame;
                if (PRIV_TIMESTAMP_FRAME_OWNER.equals(privFrame.owner)) {
                    System.arraycopy(privFrame.privateData, 0, this.id3Data.data, 0, 8);
                    this.id3Data.reset(8);
                    return this.id3Data.readLong();
                }
            }
        }
        return C.TIME_UNSET;
    }

    private static DataSource buildDataSource(DataSource dataSource, byte[] encryptionKey, byte[] encryptionIv) {
        return (encryptionKey == null || encryptionIv == null) ? dataSource : new Aes128DataSource(dataSource, encryptionKey, encryptionIv);
    }

    private Extractor createExtractor() {
        Extractor extractor;
        boolean usingNewExtractor = true;
        if (MimeTypes.TEXT_VTT.equals(this.hlsUrl.format.sampleMimeType) || this.lastPathSegment.endsWith(WEBVTT_FILE_EXTENSION) || this.lastPathSegment.endsWith(VTT_FILE_EXTENSION)) {
            extractor = new WebvttExtractor(this.trackFormat.language, this.timestampAdjuster);
        } else if (!this.needNewExtractor) {
            usingNewExtractor = false;
            extractor = this.previousExtractor;
        } else if (this.lastPathSegment.endsWith(MP4_FILE_EXTENSION) || this.lastPathSegment.startsWith(M4_FILE_EXTENSION_PREFIX, this.lastPathSegment.length() - 4)) {
            extractor = new FragmentedMp4Extractor(0, this.timestampAdjuster);
        } else {
            int esReaderFactoryFlags = 16;
            if (!this.muxedCaptionFormats.isEmpty()) {
                esReaderFactoryFlags = 16 | 32;
            }
            String codecs = this.trackFormat.codecs;
            if (!TextUtils.isEmpty(codecs)) {
                if (!MimeTypes.AUDIO_AAC.equals(MimeTypes.getAudioMediaMimeType(codecs))) {
                    esReaderFactoryFlags |= 2;
                }
                if (!MimeTypes.VIDEO_H264.equals(MimeTypes.getVideoMediaMimeType(codecs))) {
                    esReaderFactoryFlags |= 4;
                }
            }
            extractor = new TsExtractor(2, this.timestampAdjuster, new DefaultTsPayloadReaderFactory(esReaderFactoryFlags, this.muxedCaptionFormats));
        }
        if (usingNewExtractor) {
            extractor.init(this.extractorOutput);
        }
        return extractor;
    }

    private Extractor buildPackedAudioExtractor(long startTimeUs) {
        Extractor extractor;
        if (this.lastPathSegment.endsWith(AAC_FILE_EXTENSION)) {
            extractor = new AdtsExtractor(startTimeUs);
        } else if (this.lastPathSegment.endsWith(AC3_FILE_EXTENSION) || this.lastPathSegment.endsWith(EC3_FILE_EXTENSION)) {
            extractor = new Ac3Extractor(startTimeUs);
        } else if (this.lastPathSegment.endsWith(MP3_FILE_EXTENSION)) {
            extractor = new Mp3Extractor(0, startTimeUs);
        } else {
            throw new IllegalArgumentException("Unkown extension for audio file: " + this.lastPathSegment);
        }
        extractor.init(this.extractorOutput);
        return extractor;
    }
}
