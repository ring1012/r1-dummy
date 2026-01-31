package com.google.android.exoplayer2.extractor.mkv;

import android.util.SparseArray;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.PositionHolder;
import com.google.android.exoplayer2.extractor.SeekMap;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.util.LongArray;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.NalUnitUtil;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.AvcConfig;
import com.google.android.exoplayer2.video.ColorInfo;
import com.google.android.exoplayer2.video.HevcConfig;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/* loaded from: classes.dex */
public final class MatroskaExtractor implements Extractor {
    private static final int BLOCK_STATE_DATA = 2;
    private static final int BLOCK_STATE_HEADER = 1;
    private static final int BLOCK_STATE_START = 0;
    private static final String CODEC_ID_AAC = "A_AAC";
    private static final String CODEC_ID_AC3 = "A_AC3";
    private static final String CODEC_ID_ACM = "A_MS/ACM";
    private static final String CODEC_ID_DTS = "A_DTS";
    private static final String CODEC_ID_DTS_EXPRESS = "A_DTS/EXPRESS";
    private static final String CODEC_ID_DTS_LOSSLESS = "A_DTS/LOSSLESS";
    private static final String CODEC_ID_DVBSUB = "S_DVBSUB";
    private static final String CODEC_ID_E_AC3 = "A_EAC3";
    private static final String CODEC_ID_FLAC = "A_FLAC";
    private static final String CODEC_ID_FOURCC = "V_MS/VFW/FOURCC";
    private static final String CODEC_ID_H264 = "V_MPEG4/ISO/AVC";
    private static final String CODEC_ID_H265 = "V_MPEGH/ISO/HEVC";
    private static final String CODEC_ID_MP2 = "A_MPEG/L2";
    private static final String CODEC_ID_MP3 = "A_MPEG/L3";
    private static final String CODEC_ID_MPEG2 = "V_MPEG2";
    private static final String CODEC_ID_MPEG4_AP = "V_MPEG4/ISO/AP";
    private static final String CODEC_ID_MPEG4_ASP = "V_MPEG4/ISO/ASP";
    private static final String CODEC_ID_MPEG4_SP = "V_MPEG4/ISO/SP";
    private static final String CODEC_ID_OPUS = "A_OPUS";
    private static final String CODEC_ID_PCM_INT_LIT = "A_PCM/INT/LIT";
    private static final String CODEC_ID_PGS = "S_HDMV/PGS";
    private static final String CODEC_ID_SUBRIP = "S_TEXT/UTF8";
    private static final String CODEC_ID_THEORA = "V_THEORA";
    private static final String CODEC_ID_TRUEHD = "A_TRUEHD";
    private static final String CODEC_ID_VOBSUB = "S_VOBSUB";
    private static final String CODEC_ID_VORBIS = "A_VORBIS";
    private static final String CODEC_ID_VP8 = "V_VP8";
    private static final String CODEC_ID_VP9 = "V_VP9";
    private static final String DOC_TYPE_MATROSKA = "matroska";
    private static final String DOC_TYPE_WEBM = "webm";
    private static final int ENCRYPTION_IV_SIZE = 8;
    public static final int FLAG_DISABLE_SEEK_FOR_CUES = 1;
    private static final int FOURCC_COMPRESSION_VC1 = 826496599;
    private static final int ID_AUDIO = 225;
    private static final int ID_AUDIO_BIT_DEPTH = 25188;
    private static final int ID_BLOCK = 161;
    private static final int ID_BLOCK_DURATION = 155;
    private static final int ID_BLOCK_GROUP = 160;
    private static final int ID_CHANNELS = 159;
    private static final int ID_CLUSTER = 524531317;
    private static final int ID_CODEC_DELAY = 22186;
    private static final int ID_CODEC_ID = 134;
    private static final int ID_CODEC_PRIVATE = 25506;
    private static final int ID_COLOUR = 21936;
    private static final int ID_COLOUR_PRIMARIES = 21947;
    private static final int ID_COLOUR_RANGE = 21945;
    private static final int ID_COLOUR_TRANSFER = 21946;
    private static final int ID_CONTENT_COMPRESSION = 20532;
    private static final int ID_CONTENT_COMPRESSION_ALGORITHM = 16980;
    private static final int ID_CONTENT_COMPRESSION_SETTINGS = 16981;
    private static final int ID_CONTENT_ENCODING = 25152;
    private static final int ID_CONTENT_ENCODINGS = 28032;
    private static final int ID_CONTENT_ENCODING_ORDER = 20529;
    private static final int ID_CONTENT_ENCODING_SCOPE = 20530;
    private static final int ID_CONTENT_ENCRYPTION = 20533;
    private static final int ID_CONTENT_ENCRYPTION_AES_SETTINGS = 18407;
    private static final int ID_CONTENT_ENCRYPTION_AES_SETTINGS_CIPHER_MODE = 18408;
    private static final int ID_CONTENT_ENCRYPTION_ALGORITHM = 18401;
    private static final int ID_CONTENT_ENCRYPTION_KEY_ID = 18402;
    private static final int ID_CUES = 475249515;
    private static final int ID_CUE_CLUSTER_POSITION = 241;
    private static final int ID_CUE_POINT = 187;
    private static final int ID_CUE_TIME = 179;
    private static final int ID_CUE_TRACK_POSITIONS = 183;
    private static final int ID_DEFAULT_DURATION = 2352003;
    private static final int ID_DISPLAY_HEIGHT = 21690;
    private static final int ID_DISPLAY_UNIT = 21682;
    private static final int ID_DISPLAY_WIDTH = 21680;
    private static final int ID_DOC_TYPE = 17026;
    private static final int ID_DOC_TYPE_READ_VERSION = 17029;
    private static final int ID_DURATION = 17545;
    private static final int ID_EBML = 440786851;
    private static final int ID_EBML_READ_VERSION = 17143;
    private static final int ID_FLAG_DEFAULT = 136;
    private static final int ID_FLAG_FORCED = 21930;
    private static final int ID_INFO = 357149030;
    private static final int ID_LANGUAGE = 2274716;
    private static final int ID_LUMNINANCE_MAX = 21977;
    private static final int ID_LUMNINANCE_MIN = 21978;
    private static final int ID_MASTERING_METADATA = 21968;
    private static final int ID_MAX_CLL = 21948;
    private static final int ID_MAX_FALL = 21949;
    private static final int ID_PIXEL_HEIGHT = 186;
    private static final int ID_PIXEL_WIDTH = 176;
    private static final int ID_PRIMARY_B_CHROMATICITY_X = 21973;
    private static final int ID_PRIMARY_B_CHROMATICITY_Y = 21974;
    private static final int ID_PRIMARY_G_CHROMATICITY_X = 21971;
    private static final int ID_PRIMARY_G_CHROMATICITY_Y = 21972;
    private static final int ID_PRIMARY_R_CHROMATICITY_X = 21969;
    private static final int ID_PRIMARY_R_CHROMATICITY_Y = 21970;
    private static final int ID_PROJECTION = 30320;
    private static final int ID_PROJECTION_PRIVATE = 30322;
    private static final int ID_REFERENCE_BLOCK = 251;
    private static final int ID_SAMPLING_FREQUENCY = 181;
    private static final int ID_SEEK = 19899;
    private static final int ID_SEEK_HEAD = 290298740;
    private static final int ID_SEEK_ID = 21419;
    private static final int ID_SEEK_POSITION = 21420;
    private static final int ID_SEEK_PRE_ROLL = 22203;
    private static final int ID_SEGMENT = 408125543;
    private static final int ID_SEGMENT_INFO = 357149030;
    private static final int ID_SIMPLE_BLOCK = 163;
    private static final int ID_STEREO_MODE = 21432;
    private static final int ID_TIMECODE_SCALE = 2807729;
    private static final int ID_TIME_CODE = 231;
    private static final int ID_TRACKS = 374648427;
    private static final int ID_TRACK_ENTRY = 174;
    private static final int ID_TRACK_NUMBER = 215;
    private static final int ID_TRACK_TYPE = 131;
    private static final int ID_VIDEO = 224;
    private static final int ID_WHITE_POINT_CHROMATICITY_X = 21975;
    private static final int ID_WHITE_POINT_CHROMATICITY_Y = 21976;
    private static final int LACING_EBML = 3;
    private static final int LACING_FIXED_SIZE = 2;
    private static final int LACING_NONE = 0;
    private static final int LACING_XIPH = 1;
    private static final int OPUS_MAX_INPUT_SIZE = 5760;
    private static final int SUBRIP_PREFIX_END_TIMECODE_OFFSET = 19;
    private static final int SUBRIP_TIMECODE_LENGTH = 12;
    private static final int TRACK_TYPE_AUDIO = 2;
    private static final int UNSET_ENTRY_ID = -1;
    private static final int VORBIS_MAX_INPUT_SIZE = 8192;
    private static final int WAVE_FORMAT_EXTENSIBLE = 65534;
    private static final int WAVE_FORMAT_PCM = 1;
    private static final int WAVE_FORMAT_SIZE = 18;
    private long blockDurationUs;
    private int blockFlags;
    private int blockLacingSampleCount;
    private int blockLacingSampleIndex;
    private int[] blockLacingSampleSizes;
    private int blockState;
    private long blockTimeUs;
    private int blockTrackNumber;
    private int blockTrackNumberLength;
    private long clusterTimecodeUs;
    private LongArray cueClusterPositions;
    private LongArray cueTimesUs;
    private long cuesContentPosition;
    private Track currentTrack;
    private long durationTimecode;
    private long durationUs;
    private final ParsableByteArray encryptionInitializationVector;
    private final ParsableByteArray encryptionSubsampleData;
    private ByteBuffer encryptionSubsampleDataBuffer;
    private ExtractorOutput extractorOutput;
    private final ParsableByteArray nalLength;
    private final ParsableByteArray nalStartCode;
    private final EbmlReader reader;
    private int sampleBytesRead;
    private int sampleBytesWritten;
    private int sampleCurrentNalBytesRemaining;
    private boolean sampleEncodingHandled;
    private boolean sampleInitializationVectorRead;
    private int samplePartitionCount;
    private boolean samplePartitionCountRead;
    private boolean sampleRead;
    private boolean sampleSeenReferenceBlock;
    private byte sampleSignalByte;
    private boolean sampleSignalByteRead;
    private final ParsableByteArray sampleStrippedBytes;
    private final ParsableByteArray scratch;
    private int seekEntryId;
    private final ParsableByteArray seekEntryIdBytes;
    private long seekEntryPosition;
    private boolean seekForCues;
    private final boolean seekForCuesEnabled;
    private long seekPositionAfterBuildingCues;
    private boolean seenClusterPositionForCurrentCuePoint;
    private long segmentContentPosition;
    private long segmentContentSize;
    private boolean sentSeekMap;
    private final ParsableByteArray subripSample;
    private long timecodeScale;
    private final SparseArray<Track> tracks;
    private final VarintReader varintReader;
    private final ParsableByteArray vorbisNumPageSamples;
    public static final ExtractorsFactory FACTORY = new ExtractorsFactory() { // from class: com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor.1
        @Override // com.google.android.exoplayer2.extractor.ExtractorsFactory
        public Extractor[] createExtractors() {
            return new Extractor[]{new MatroskaExtractor()};
        }
    };
    private static final byte[] SUBRIP_PREFIX = {49, 10, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 32, 45, 45, 62, 32, 48, 48, 58, 48, 48, 58, 48, 48, 44, 48, 48, 48, 10};
    private static final byte[] SUBRIP_TIMECODE_EMPTY = {32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32};
    private static final UUID WAVE_SUBFORMAT_PCM = new UUID(72057594037932032L, -9223371306706625679L);

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    public MatroskaExtractor() {
        this(0);
    }

    public MatroskaExtractor(int flags) {
        this(new DefaultEbmlReader(), flags);
    }

    MatroskaExtractor(EbmlReader reader, int flags) {
        this.segmentContentPosition = -1L;
        this.timecodeScale = C.TIME_UNSET;
        this.durationTimecode = C.TIME_UNSET;
        this.durationUs = C.TIME_UNSET;
        this.cuesContentPosition = -1L;
        this.seekPositionAfterBuildingCues = -1L;
        this.clusterTimecodeUs = C.TIME_UNSET;
        this.reader = reader;
        this.reader.init(new InnerEbmlReaderOutput());
        this.seekForCuesEnabled = (flags & 1) == 0;
        this.varintReader = new VarintReader();
        this.tracks = new SparseArray<>();
        this.scratch = new ParsableByteArray(4);
        this.vorbisNumPageSamples = new ParsableByteArray(ByteBuffer.allocate(4).putInt(-1).array());
        this.seekEntryIdBytes = new ParsableByteArray(4);
        this.nalStartCode = new ParsableByteArray(NalUnitUtil.NAL_START_CODE);
        this.nalLength = new ParsableByteArray(4);
        this.sampleStrippedBytes = new ParsableByteArray();
        this.subripSample = new ParsableByteArray();
        this.encryptionInitializationVector = new ParsableByteArray(8);
        this.encryptionSubsampleData = new ParsableByteArray();
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public boolean sniff(ExtractorInput input) throws InterruptedException, IOException {
        return new Sniffer().sniff(input);
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void init(ExtractorOutput output) {
        this.extractorOutput = output;
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void seek(long position, long timeUs) {
        this.clusterTimecodeUs = C.TIME_UNSET;
        this.blockState = 0;
        this.reader.reset();
        this.varintReader.reset();
        resetSample();
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public void release() {
    }

    @Override // com.google.android.exoplayer2.extractor.Extractor
    public int read(ExtractorInput input, PositionHolder seekPosition) throws InterruptedException, IOException {
        this.sampleRead = false;
        boolean continueReading = true;
        while (continueReading && !this.sampleRead) {
            continueReading = this.reader.read(input);
            if (continueReading && maybeSeekForCues(seekPosition, input.getPosition())) {
                return 1;
            }
        }
        return !continueReading ? -1 : 0;
    }

    int getElementType(int id) {
        switch (id) {
            case ID_TRACK_TYPE /* 131 */:
            case ID_FLAG_DEFAULT /* 136 */:
            case ID_BLOCK_DURATION /* 155 */:
            case ID_CHANNELS /* 159 */:
            case ID_PIXEL_WIDTH /* 176 */:
            case ID_CUE_TIME /* 179 */:
            case ID_PIXEL_HEIGHT /* 186 */:
            case ID_TRACK_NUMBER /* 215 */:
            case ID_TIME_CODE /* 231 */:
            case ID_CUE_CLUSTER_POSITION /* 241 */:
            case ID_REFERENCE_BLOCK /* 251 */:
            case ID_CONTENT_COMPRESSION_ALGORITHM /* 16980 */:
            case ID_DOC_TYPE_READ_VERSION /* 17029 */:
            case ID_EBML_READ_VERSION /* 17143 */:
            case ID_CONTENT_ENCRYPTION_ALGORITHM /* 18401 */:
            case ID_CONTENT_ENCRYPTION_AES_SETTINGS_CIPHER_MODE /* 18408 */:
            case ID_CONTENT_ENCODING_ORDER /* 20529 */:
            case ID_CONTENT_ENCODING_SCOPE /* 20530 */:
            case ID_SEEK_POSITION /* 21420 */:
            case ID_STEREO_MODE /* 21432 */:
            case ID_DISPLAY_WIDTH /* 21680 */:
            case ID_DISPLAY_UNIT /* 21682 */:
            case ID_DISPLAY_HEIGHT /* 21690 */:
            case ID_FLAG_FORCED /* 21930 */:
            case ID_COLOUR_RANGE /* 21945 */:
            case ID_COLOUR_TRANSFER /* 21946 */:
            case ID_COLOUR_PRIMARIES /* 21947 */:
            case ID_MAX_CLL /* 21948 */:
            case ID_MAX_FALL /* 21949 */:
            case ID_CODEC_DELAY /* 22186 */:
            case ID_SEEK_PRE_ROLL /* 22203 */:
            case ID_AUDIO_BIT_DEPTH /* 25188 */:
            case ID_DEFAULT_DURATION /* 2352003 */:
            case ID_TIMECODE_SCALE /* 2807729 */:
                return 2;
            case 134:
            case ID_DOC_TYPE /* 17026 */:
            case ID_LANGUAGE /* 2274716 */:
                return 3;
            case ID_BLOCK_GROUP /* 160 */:
            case ID_TRACK_ENTRY /* 174 */:
            case ID_CUE_TRACK_POSITIONS /* 183 */:
            case ID_CUE_POINT /* 187 */:
            case 224:
            case 225:
            case ID_CONTENT_ENCRYPTION_AES_SETTINGS /* 18407 */:
            case ID_SEEK /* 19899 */:
            case ID_CONTENT_COMPRESSION /* 20532 */:
            case ID_CONTENT_ENCRYPTION /* 20533 */:
            case ID_COLOUR /* 21936 */:
            case ID_MASTERING_METADATA /* 21968 */:
            case ID_CONTENT_ENCODING /* 25152 */:
            case ID_CONTENT_ENCODINGS /* 28032 */:
            case ID_PROJECTION /* 30320 */:
            case ID_SEEK_HEAD /* 290298740 */:
            case 357149030:
            case ID_TRACKS /* 374648427 */:
            case ID_SEGMENT /* 408125543 */:
            case ID_EBML /* 440786851 */:
            case ID_CUES /* 475249515 */:
            case ID_CLUSTER /* 524531317 */:
                return 1;
            case ID_BLOCK /* 161 */:
            case ID_SIMPLE_BLOCK /* 163 */:
            case ID_CONTENT_COMPRESSION_SETTINGS /* 16981 */:
            case ID_CONTENT_ENCRYPTION_KEY_ID /* 18402 */:
            case ID_SEEK_ID /* 21419 */:
            case ID_CODEC_PRIVATE /* 25506 */:
            case ID_PROJECTION_PRIVATE /* 30322 */:
                return 4;
            case ID_SAMPLING_FREQUENCY /* 181 */:
            case ID_DURATION /* 17545 */:
            case ID_PRIMARY_R_CHROMATICITY_X /* 21969 */:
            case ID_PRIMARY_R_CHROMATICITY_Y /* 21970 */:
            case ID_PRIMARY_G_CHROMATICITY_X /* 21971 */:
            case ID_PRIMARY_G_CHROMATICITY_Y /* 21972 */:
            case ID_PRIMARY_B_CHROMATICITY_X /* 21973 */:
            case ID_PRIMARY_B_CHROMATICITY_Y /* 21974 */:
            case ID_WHITE_POINT_CHROMATICITY_X /* 21975 */:
            case ID_WHITE_POINT_CHROMATICITY_Y /* 21976 */:
            case ID_LUMNINANCE_MAX /* 21977 */:
            case ID_LUMNINANCE_MIN /* 21978 */:
                return 5;
            default:
                return 0;
        }
    }

    boolean isLevel1Element(int id) {
        return id == 357149030 || id == ID_CLUSTER || id == ID_CUES || id == ID_TRACKS;
    }

    void startMasterElement(int id, long contentPosition, long contentSize) throws ParserException {
        switch (id) {
            case ID_BLOCK_GROUP /* 160 */:
                this.sampleSeenReferenceBlock = false;
                return;
            case ID_TRACK_ENTRY /* 174 */:
                this.currentTrack = new Track();
                return;
            case ID_CUE_POINT /* 187 */:
                this.seenClusterPositionForCurrentCuePoint = false;
                return;
            case ID_SEEK /* 19899 */:
                this.seekEntryId = -1;
                this.seekEntryPosition = -1L;
                return;
            case ID_CONTENT_ENCRYPTION /* 20533 */:
                this.currentTrack.hasContentEncryption = true;
                return;
            case ID_MASTERING_METADATA /* 21968 */:
                this.currentTrack.hasColorInfo = true;
                return;
            case ID_CONTENT_ENCODING /* 25152 */:
            default:
                return;
            case ID_SEGMENT /* 408125543 */:
                if (this.segmentContentPosition != -1 && this.segmentContentPosition != contentPosition) {
                    throw new ParserException("Multiple Segment elements not supported");
                }
                this.segmentContentPosition = contentPosition;
                this.segmentContentSize = contentSize;
                return;
            case ID_CUES /* 475249515 */:
                this.cueTimesUs = new LongArray();
                this.cueClusterPositions = new LongArray();
                return;
            case ID_CLUSTER /* 524531317 */:
                if (!this.sentSeekMap) {
                    if (this.seekForCuesEnabled && this.cuesContentPosition != -1) {
                        this.seekForCues = true;
                        return;
                    } else {
                        this.extractorOutput.seekMap(new SeekMap.Unseekable(this.durationUs));
                        this.sentSeekMap = true;
                        return;
                    }
                }
                return;
        }
    }

    void endMasterElement(int id) throws ParserException {
        switch (id) {
            case ID_BLOCK_GROUP /* 160 */:
                if (this.blockState == 2) {
                    if (!this.sampleSeenReferenceBlock) {
                        this.blockFlags |= 1;
                    }
                    commitSampleToOutput(this.tracks.get(this.blockTrackNumber), this.blockTimeUs);
                    this.blockState = 0;
                    return;
                }
                return;
            case ID_TRACK_ENTRY /* 174 */:
                if (isCodecSupported(this.currentTrack.codecId)) {
                    this.currentTrack.initializeOutput(this.extractorOutput, this.currentTrack.number);
                    this.tracks.put(this.currentTrack.number, this.currentTrack);
                }
                this.currentTrack = null;
                return;
            case ID_SEEK /* 19899 */:
                if (this.seekEntryId == -1 || this.seekEntryPosition == -1) {
                    throw new ParserException("Mandatory element SeekID or SeekPosition not found");
                }
                if (this.seekEntryId == ID_CUES) {
                    this.cuesContentPosition = this.seekEntryPosition;
                    return;
                }
                return;
            case ID_CONTENT_ENCODING /* 25152 */:
                if (this.currentTrack.hasContentEncryption) {
                    if (this.currentTrack.encryptionKeyId == null) {
                        throw new ParserException("Encrypted Track found but ContentEncKeyID was not found");
                    }
                    this.currentTrack.drmInitData = new DrmInitData(new DrmInitData.SchemeData(C.UUID_NIL, MimeTypes.VIDEO_WEBM, this.currentTrack.encryptionKeyId));
                    return;
                }
                return;
            case ID_CONTENT_ENCODINGS /* 28032 */:
                if (this.currentTrack.hasContentEncryption && this.currentTrack.sampleStrippedBytes != null) {
                    throw new ParserException("Combining encryption and compression is not supported");
                }
                return;
            case 357149030:
                if (this.timecodeScale == C.TIME_UNSET) {
                    this.timecodeScale = C.MICROS_PER_SECOND;
                }
                if (this.durationTimecode != C.TIME_UNSET) {
                    this.durationUs = scaleTimecodeToUs(this.durationTimecode);
                    return;
                }
                return;
            case ID_TRACKS /* 374648427 */:
                if (this.tracks.size() == 0) {
                    throw new ParserException("No valid tracks were found");
                }
                this.extractorOutput.endTracks();
                return;
            case ID_CUES /* 475249515 */:
                if (!this.sentSeekMap) {
                    this.extractorOutput.seekMap(buildSeekMap());
                    this.sentSeekMap = true;
                    return;
                }
                return;
            default:
                return;
        }
    }

    void integerElement(int id, long value) throws ParserException {
        switch (id) {
            case ID_TRACK_TYPE /* 131 */:
                this.currentTrack.type = (int) value;
                return;
            case ID_FLAG_DEFAULT /* 136 */:
                this.currentTrack.flagForced = value == 1;
                return;
            case ID_BLOCK_DURATION /* 155 */:
                this.blockDurationUs = scaleTimecodeToUs(value);
                return;
            case ID_CHANNELS /* 159 */:
                this.currentTrack.channelCount = (int) value;
                return;
            case ID_PIXEL_WIDTH /* 176 */:
                this.currentTrack.width = (int) value;
                return;
            case ID_CUE_TIME /* 179 */:
                this.cueTimesUs.add(scaleTimecodeToUs(value));
                return;
            case ID_PIXEL_HEIGHT /* 186 */:
                this.currentTrack.height = (int) value;
                return;
            case ID_TRACK_NUMBER /* 215 */:
                this.currentTrack.number = (int) value;
                return;
            case ID_TIME_CODE /* 231 */:
                this.clusterTimecodeUs = scaleTimecodeToUs(value);
                return;
            case ID_CUE_CLUSTER_POSITION /* 241 */:
                if (!this.seenClusterPositionForCurrentCuePoint) {
                    this.cueClusterPositions.add(value);
                    this.seenClusterPositionForCurrentCuePoint = true;
                    return;
                }
                return;
            case ID_REFERENCE_BLOCK /* 251 */:
                this.sampleSeenReferenceBlock = true;
                return;
            case ID_CONTENT_COMPRESSION_ALGORITHM /* 16980 */:
                if (value != 3) {
                    throw new ParserException("ContentCompAlgo " + value + " not supported");
                }
                return;
            case ID_DOC_TYPE_READ_VERSION /* 17029 */:
                if (value < 1 || value > 2) {
                    throw new ParserException("DocTypeReadVersion " + value + " not supported");
                }
                return;
            case ID_EBML_READ_VERSION /* 17143 */:
                if (value != 1) {
                    throw new ParserException("EBMLReadVersion " + value + " not supported");
                }
                return;
            case ID_CONTENT_ENCRYPTION_ALGORITHM /* 18401 */:
                if (value != 5) {
                    throw new ParserException("ContentEncAlgo " + value + " not supported");
                }
                return;
            case ID_CONTENT_ENCRYPTION_AES_SETTINGS_CIPHER_MODE /* 18408 */:
                if (value != 1) {
                    throw new ParserException("AESSettingsCipherMode " + value + " not supported");
                }
                return;
            case ID_CONTENT_ENCODING_ORDER /* 20529 */:
                if (value != 0) {
                    throw new ParserException("ContentEncodingOrder " + value + " not supported");
                }
                return;
            case ID_CONTENT_ENCODING_SCOPE /* 20530 */:
                if (value != 1) {
                    throw new ParserException("ContentEncodingScope " + value + " not supported");
                }
                return;
            case ID_SEEK_POSITION /* 21420 */:
                this.seekEntryPosition = this.segmentContentPosition + value;
                return;
            case ID_STEREO_MODE /* 21432 */:
                int layout = (int) value;
                switch (layout) {
                    case 0:
                        this.currentTrack.stereoMode = 0;
                        return;
                    case 1:
                        this.currentTrack.stereoMode = 2;
                        return;
                    case 3:
                        this.currentTrack.stereoMode = 1;
                        return;
                    case 15:
                        this.currentTrack.stereoMode = 3;
                        return;
                    default:
                        return;
                }
            case ID_DISPLAY_WIDTH /* 21680 */:
                this.currentTrack.displayWidth = (int) value;
                return;
            case ID_DISPLAY_UNIT /* 21682 */:
                this.currentTrack.displayUnit = (int) value;
                return;
            case ID_DISPLAY_HEIGHT /* 21690 */:
                this.currentTrack.displayHeight = (int) value;
                return;
            case ID_FLAG_FORCED /* 21930 */:
                this.currentTrack.flagDefault = value == 1;
                return;
            case ID_COLOUR_RANGE /* 21945 */:
                switch ((int) value) {
                    case 1:
                        this.currentTrack.colorRange = 2;
                        return;
                    case 2:
                        this.currentTrack.colorRange = 1;
                        return;
                    default:
                        return;
                }
            case ID_COLOUR_TRANSFER /* 21946 */:
                switch ((int) value) {
                    case 1:
                    case 6:
                    case 7:
                        this.currentTrack.colorTransfer = 3;
                        return;
                    case 16:
                        this.currentTrack.colorTransfer = 6;
                        return;
                    case 18:
                        this.currentTrack.colorTransfer = 7;
                        return;
                    default:
                        return;
                }
            case ID_COLOUR_PRIMARIES /* 21947 */:
                this.currentTrack.hasColorInfo = true;
                switch ((int) value) {
                    case 1:
                        this.currentTrack.colorSpace = 1;
                        return;
                    case 2:
                    case 3:
                    case 8:
                    default:
                        return;
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        this.currentTrack.colorSpace = 2;
                        return;
                    case 9:
                        this.currentTrack.colorSpace = 6;
                        return;
                }
            case ID_MAX_CLL /* 21948 */:
                this.currentTrack.maxContentLuminance = (int) value;
                return;
            case ID_MAX_FALL /* 21949 */:
                this.currentTrack.maxFrameAverageLuminance = (int) value;
                return;
            case ID_CODEC_DELAY /* 22186 */:
                this.currentTrack.codecDelayNs = value;
                return;
            case ID_SEEK_PRE_ROLL /* 22203 */:
                this.currentTrack.seekPreRollNs = value;
                return;
            case ID_AUDIO_BIT_DEPTH /* 25188 */:
                this.currentTrack.audioBitDepth = (int) value;
                return;
            case ID_DEFAULT_DURATION /* 2352003 */:
                this.currentTrack.defaultSampleDurationNs = (int) value;
                return;
            case ID_TIMECODE_SCALE /* 2807729 */:
                this.timecodeScale = value;
                return;
            default:
                return;
        }
    }

    void floatElement(int id, double value) {
        switch (id) {
            case ID_SAMPLING_FREQUENCY /* 181 */:
                this.currentTrack.sampleRate = (int) value;
                break;
            case ID_DURATION /* 17545 */:
                this.durationTimecode = (long) value;
                break;
            case ID_PRIMARY_R_CHROMATICITY_X /* 21969 */:
                this.currentTrack.primaryRChromaticityX = (float) value;
                break;
            case ID_PRIMARY_R_CHROMATICITY_Y /* 21970 */:
                this.currentTrack.primaryRChromaticityY = (float) value;
                break;
            case ID_PRIMARY_G_CHROMATICITY_X /* 21971 */:
                this.currentTrack.primaryGChromaticityX = (float) value;
                break;
            case ID_PRIMARY_G_CHROMATICITY_Y /* 21972 */:
                this.currentTrack.primaryGChromaticityY = (float) value;
                break;
            case ID_PRIMARY_B_CHROMATICITY_X /* 21973 */:
                this.currentTrack.primaryBChromaticityX = (float) value;
                break;
            case ID_PRIMARY_B_CHROMATICITY_Y /* 21974 */:
                this.currentTrack.primaryBChromaticityY = (float) value;
                break;
            case ID_WHITE_POINT_CHROMATICITY_X /* 21975 */:
                this.currentTrack.whitePointChromaticityX = (float) value;
                break;
            case ID_WHITE_POINT_CHROMATICITY_Y /* 21976 */:
                this.currentTrack.whitePointChromaticityY = (float) value;
                break;
            case ID_LUMNINANCE_MAX /* 21977 */:
                this.currentTrack.maxMasteringLuminance = (float) value;
                break;
            case ID_LUMNINANCE_MIN /* 21978 */:
                this.currentTrack.minMasteringLuminance = (float) value;
                break;
        }
    }

    void stringElement(int id, String value) throws ParserException {
        switch (id) {
            case 134:
                this.currentTrack.codecId = value;
                return;
            case ID_DOC_TYPE /* 17026 */:
                if (!DOC_TYPE_WEBM.equals(value) && !DOC_TYPE_MATROSKA.equals(value)) {
                    throw new ParserException("DocType " + value + " not supported");
                }
                return;
            case ID_LANGUAGE /* 2274716 */:
                this.currentTrack.language = value;
                return;
            default:
                return;
        }
    }

    void binaryElement(int id, int contentSize, ExtractorInput input) throws InterruptedException, IOException {
        int byteValue;
        switch (id) {
            case ID_BLOCK /* 161 */:
            case ID_SIMPLE_BLOCK /* 163 */:
                if (this.blockState == 0) {
                    this.blockTrackNumber = (int) this.varintReader.readUnsignedVarint(input, false, true, 8);
                    this.blockTrackNumberLength = this.varintReader.getLastLength();
                    this.blockDurationUs = C.TIME_UNSET;
                    this.blockState = 1;
                    this.scratch.reset();
                }
                Track track = this.tracks.get(this.blockTrackNumber);
                if (track == null) {
                    input.skipFully(contentSize - this.blockTrackNumberLength);
                    this.blockState = 0;
                    return;
                }
                if (this.blockState == 1) {
                    readScratch(input, 3);
                    int lacing = (this.scratch.data[2] & 6) >> 1;
                    if (lacing == 0) {
                        this.blockLacingSampleCount = 1;
                        this.blockLacingSampleSizes = ensureArrayCapacity(this.blockLacingSampleSizes, 1);
                        this.blockLacingSampleSizes[0] = (contentSize - this.blockTrackNumberLength) - 3;
                    } else {
                        if (id != ID_SIMPLE_BLOCK) {
                            throw new ParserException("Lacing only supported in SimpleBlocks.");
                        }
                        readScratch(input, 4);
                        this.blockLacingSampleCount = (this.scratch.data[3] & 255) + 1;
                        this.blockLacingSampleSizes = ensureArrayCapacity(this.blockLacingSampleSizes, this.blockLacingSampleCount);
                        if (lacing == 2) {
                            int blockLacingSampleSize = ((contentSize - this.blockTrackNumberLength) - 4) / this.blockLacingSampleCount;
                            Arrays.fill(this.blockLacingSampleSizes, 0, this.blockLacingSampleCount, blockLacingSampleSize);
                        } else if (lacing == 1) {
                            int totalSamplesSize = 0;
                            int headerSize = 4;
                            for (int sampleIndex = 0; sampleIndex < this.blockLacingSampleCount - 1; sampleIndex++) {
                                this.blockLacingSampleSizes[sampleIndex] = 0;
                                do {
                                    headerSize++;
                                    readScratch(input, headerSize);
                                    byteValue = this.scratch.data[headerSize - 1] & 255;
                                    int[] iArr = this.blockLacingSampleSizes;
                                    iArr[sampleIndex] = iArr[sampleIndex] + byteValue;
                                } while (byteValue == 255);
                                totalSamplesSize += this.blockLacingSampleSizes[sampleIndex];
                            }
                            this.blockLacingSampleSizes[this.blockLacingSampleCount - 1] = ((contentSize - this.blockTrackNumberLength) - headerSize) - totalSamplesSize;
                        } else if (lacing == 3) {
                            int totalSamplesSize2 = 0;
                            int headerSize2 = 4;
                            for (int sampleIndex2 = 0; sampleIndex2 < this.blockLacingSampleCount - 1; sampleIndex2++) {
                                this.blockLacingSampleSizes[sampleIndex2] = 0;
                                headerSize2++;
                                readScratch(input, headerSize2);
                                if (this.scratch.data[headerSize2 - 1] == 0) {
                                    throw new ParserException("No valid varint length mask found");
                                }
                                long readValue = 0;
                                int i = 0;
                                while (true) {
                                    if (i < 8) {
                                        int lengthMask = 1 << (7 - i);
                                        if ((this.scratch.data[headerSize2 - 1] & lengthMask) == 0) {
                                            i++;
                                        } else {
                                            int readPosition = headerSize2 - 1;
                                            headerSize2 += i;
                                            readScratch(input, headerSize2);
                                            readValue = this.scratch.data[readPosition] & 255 & (lengthMask ^ (-1));
                                            for (int readPosition2 = readPosition + 1; readPosition2 < headerSize2; readPosition2++) {
                                                readValue = (readValue << 8) | (this.scratch.data[readPosition2] & 255);
                                            }
                                            if (sampleIndex2 > 0) {
                                                readValue -= (1 << ((i * 7) + 6)) - 1;
                                            }
                                        }
                                    }
                                }
                                if (readValue < -2147483648L || readValue > 2147483647L) {
                                    throw new ParserException("EBML lacing sample size out of range.");
                                }
                                int intReadValue = (int) readValue;
                                int[] iArr2 = this.blockLacingSampleSizes;
                                if (sampleIndex2 != 0) {
                                    intReadValue += this.blockLacingSampleSizes[sampleIndex2 - 1];
                                }
                                iArr2[sampleIndex2] = intReadValue;
                                totalSamplesSize2 += this.blockLacingSampleSizes[sampleIndex2];
                            }
                            this.blockLacingSampleSizes[this.blockLacingSampleCount - 1] = ((contentSize - this.blockTrackNumberLength) - headerSize2) - totalSamplesSize2;
                        } else {
                            throw new ParserException("Unexpected lacing value: " + lacing);
                        }
                    }
                    int timecode = (this.scratch.data[0] << 8) | (this.scratch.data[1] & 255);
                    this.blockTimeUs = this.clusterTimecodeUs + scaleTimecodeToUs(timecode);
                    boolean isInvisible = (this.scratch.data[2] & 8) == 8;
                    boolean isKeyframe = track.type == 2 || (id == ID_SIMPLE_BLOCK && (this.scratch.data[2] & 128) == 128);
                    this.blockFlags = (isInvisible ? Integer.MIN_VALUE : 0) | (isKeyframe ? 1 : 0);
                    this.blockState = 2;
                    this.blockLacingSampleIndex = 0;
                }
                if (id == ID_SIMPLE_BLOCK) {
                    while (this.blockLacingSampleIndex < this.blockLacingSampleCount) {
                        writeSampleData(input, track, this.blockLacingSampleSizes[this.blockLacingSampleIndex]);
                        long sampleTimeUs = this.blockTimeUs + ((this.blockLacingSampleIndex * track.defaultSampleDurationNs) / 1000);
                        commitSampleToOutput(track, sampleTimeUs);
                        this.blockLacingSampleIndex++;
                    }
                    this.blockState = 0;
                    return;
                }
                writeSampleData(input, track, this.blockLacingSampleSizes[0]);
                return;
            case ID_CONTENT_COMPRESSION_SETTINGS /* 16981 */:
                this.currentTrack.sampleStrippedBytes = new byte[contentSize];
                input.readFully(this.currentTrack.sampleStrippedBytes, 0, contentSize);
                return;
            case ID_CONTENT_ENCRYPTION_KEY_ID /* 18402 */:
                this.currentTrack.encryptionKeyId = new byte[contentSize];
                input.readFully(this.currentTrack.encryptionKeyId, 0, contentSize);
                return;
            case ID_SEEK_ID /* 21419 */:
                Arrays.fill(this.seekEntryIdBytes.data, (byte) 0);
                input.readFully(this.seekEntryIdBytes.data, 4 - contentSize, contentSize);
                this.seekEntryIdBytes.setPosition(0);
                this.seekEntryId = (int) this.seekEntryIdBytes.readUnsignedInt();
                return;
            case ID_CODEC_PRIVATE /* 25506 */:
                this.currentTrack.codecPrivate = new byte[contentSize];
                input.readFully(this.currentTrack.codecPrivate, 0, contentSize);
                return;
            case ID_PROJECTION_PRIVATE /* 30322 */:
                this.currentTrack.projectionData = new byte[contentSize];
                input.readFully(this.currentTrack.projectionData, 0, contentSize);
                return;
            default:
                throw new ParserException("Unexpected id: " + id);
        }
    }

    private void commitSampleToOutput(Track track, long timeUs) {
        if (CODEC_ID_SUBRIP.equals(track.codecId)) {
            writeSubripSample(track);
        }
        track.output.sampleMetadata(timeUs, this.blockFlags, this.sampleBytesWritten, 0, track.encryptionKeyId);
        this.sampleRead = true;
        resetSample();
    }

    private void resetSample() {
        this.sampleBytesRead = 0;
        this.sampleBytesWritten = 0;
        this.sampleCurrentNalBytesRemaining = 0;
        this.sampleEncodingHandled = false;
        this.sampleSignalByteRead = false;
        this.samplePartitionCountRead = false;
        this.samplePartitionCount = 0;
        this.sampleSignalByte = (byte) 0;
        this.sampleInitializationVectorRead = false;
        this.sampleStrippedBytes.reset();
    }

    private void readScratch(ExtractorInput input, int requiredLength) throws InterruptedException, IOException {
        if (this.scratch.limit() < requiredLength) {
            if (this.scratch.capacity() < requiredLength) {
                this.scratch.reset(Arrays.copyOf(this.scratch.data, Math.max(this.scratch.data.length * 2, requiredLength)), this.scratch.limit());
            }
            input.readFully(this.scratch.data, this.scratch.limit(), requiredLength - this.scratch.limit());
            this.scratch.setLimit(requiredLength);
        }
    }

    private void writeSampleData(ExtractorInput input, Track track, int size) throws InterruptedException, IOException {
        if (CODEC_ID_SUBRIP.equals(track.codecId)) {
            int sizeWithPrefix = SUBRIP_PREFIX.length + size;
            if (this.subripSample.capacity() < sizeWithPrefix) {
                this.subripSample.data = Arrays.copyOf(SUBRIP_PREFIX, sizeWithPrefix + size);
            }
            input.readFully(this.subripSample.data, SUBRIP_PREFIX.length, size);
            this.subripSample.setPosition(0);
            this.subripSample.setLimit(sizeWithPrefix);
            return;
        }
        TrackOutput output = track.output;
        if (!this.sampleEncodingHandled) {
            if (track.hasContentEncryption) {
                this.blockFlags &= -1073741825;
                if (!this.sampleSignalByteRead) {
                    input.readFully(this.scratch.data, 0, 1);
                    this.sampleBytesRead++;
                    if ((this.scratch.data[0] & 128) == 128) {
                        throw new ParserException("Extension bit is set in signal byte");
                    }
                    this.sampleSignalByte = this.scratch.data[0];
                    this.sampleSignalByteRead = true;
                }
                boolean isEncrypted = (this.sampleSignalByte & 1) == 1;
                if (isEncrypted) {
                    boolean hasSubsampleEncryption = (this.sampleSignalByte & 2) == 2;
                    this.blockFlags |= 1073741824;
                    if (!this.sampleInitializationVectorRead) {
                        input.readFully(this.encryptionInitializationVector.data, 0, 8);
                        this.sampleBytesRead += 8;
                        this.sampleInitializationVectorRead = true;
                        this.scratch.data[0] = (byte) ((hasSubsampleEncryption ? 128 : 0) | 8);
                        this.scratch.setPosition(0);
                        output.sampleData(this.scratch, 1);
                        this.sampleBytesWritten++;
                        this.encryptionInitializationVector.setPosition(0);
                        output.sampleData(this.encryptionInitializationVector, 8);
                        this.sampleBytesWritten += 8;
                    }
                    if (hasSubsampleEncryption) {
                        if (!this.samplePartitionCountRead) {
                            input.readFully(this.scratch.data, 0, 1);
                            this.sampleBytesRead++;
                            this.scratch.setPosition(0);
                            this.samplePartitionCount = this.scratch.readUnsignedByte();
                            this.samplePartitionCountRead = true;
                        }
                        int samplePartitionDataSize = this.samplePartitionCount * 4;
                        this.scratch.reset(samplePartitionDataSize);
                        input.readFully(this.scratch.data, 0, samplePartitionDataSize);
                        this.sampleBytesRead += samplePartitionDataSize;
                        short subsampleCount = (short) ((this.samplePartitionCount / 2) + 1);
                        int subsampleDataSize = (subsampleCount * 6) + 2;
                        if (this.encryptionSubsampleDataBuffer == null || this.encryptionSubsampleDataBuffer.capacity() < subsampleDataSize) {
                            this.encryptionSubsampleDataBuffer = ByteBuffer.allocate(subsampleDataSize);
                        }
                        this.encryptionSubsampleDataBuffer.position(0);
                        this.encryptionSubsampleDataBuffer.putShort(subsampleCount);
                        int partitionOffset = 0;
                        for (int i = 0; i < this.samplePartitionCount; i++) {
                            int previousPartitionOffset = partitionOffset;
                            partitionOffset = this.scratch.readUnsignedIntToInt();
                            if (i % 2 == 0) {
                                this.encryptionSubsampleDataBuffer.putShort((short) (partitionOffset - previousPartitionOffset));
                            } else {
                                this.encryptionSubsampleDataBuffer.putInt(partitionOffset - previousPartitionOffset);
                            }
                        }
                        int finalPartitionSize = (size - this.sampleBytesRead) - partitionOffset;
                        if (this.samplePartitionCount % 2 == 1) {
                            this.encryptionSubsampleDataBuffer.putInt(finalPartitionSize);
                        } else {
                            this.encryptionSubsampleDataBuffer.putShort((short) finalPartitionSize);
                            this.encryptionSubsampleDataBuffer.putInt(0);
                        }
                        this.encryptionSubsampleData.reset(this.encryptionSubsampleDataBuffer.array(), subsampleDataSize);
                        output.sampleData(this.encryptionSubsampleData, subsampleDataSize);
                        this.sampleBytesWritten += subsampleDataSize;
                    }
                }
            } else if (track.sampleStrippedBytes != null) {
                this.sampleStrippedBytes.reset(track.sampleStrippedBytes, track.sampleStrippedBytes.length);
            }
            this.sampleEncodingHandled = true;
        }
        int size2 = size + this.sampleStrippedBytes.limit();
        if (CODEC_ID_H264.equals(track.codecId) || CODEC_ID_H265.equals(track.codecId)) {
            byte[] nalLengthData = this.nalLength.data;
            nalLengthData[0] = 0;
            nalLengthData[1] = 0;
            nalLengthData[2] = 0;
            int nalUnitLengthFieldLength = track.nalUnitLengthFieldLength;
            int nalUnitLengthFieldLengthDiff = 4 - track.nalUnitLengthFieldLength;
            while (this.sampleBytesRead < size2) {
                if (this.sampleCurrentNalBytesRemaining == 0) {
                    readToTarget(input, nalLengthData, nalUnitLengthFieldLengthDiff, nalUnitLengthFieldLength);
                    this.nalLength.setPosition(0);
                    this.sampleCurrentNalBytesRemaining = this.nalLength.readUnsignedIntToInt();
                    this.nalStartCode.setPosition(0);
                    output.sampleData(this.nalStartCode, 4);
                    this.sampleBytesWritten += 4;
                } else {
                    this.sampleCurrentNalBytesRemaining -= readToOutput(input, output, this.sampleCurrentNalBytesRemaining);
                }
            }
        } else {
            while (this.sampleBytesRead < size2) {
                readToOutput(input, output, size2 - this.sampleBytesRead);
            }
        }
        if (CODEC_ID_VORBIS.equals(track.codecId)) {
            this.vorbisNumPageSamples.setPosition(0);
            output.sampleData(this.vorbisNumPageSamples, 4);
            this.sampleBytesWritten += 4;
        }
    }

    private void writeSubripSample(Track track) {
        setSubripSampleEndTimecode(this.subripSample.data, this.blockDurationUs);
        track.output.sampleData(this.subripSample, this.subripSample.limit());
        this.sampleBytesWritten += this.subripSample.limit();
    }

    private static void setSubripSampleEndTimecode(byte[] subripSampleData, long timeUs) {
        byte[] timeCodeData;
        if (timeUs == C.TIME_UNSET) {
            timeCodeData = SUBRIP_TIMECODE_EMPTY;
        } else {
            int hours = (int) (timeUs / 3600000000L);
            long timeUs2 = timeUs - (hours * 3600000000L);
            int minutes = (int) (timeUs2 / 60000000);
            long timeUs3 = timeUs2 - (60000000 * minutes);
            int seconds = (int) (timeUs3 / C.MICROS_PER_SECOND);
            int milliseconds = (int) ((timeUs3 - (1000000 * seconds)) / 1000);
            timeCodeData = Util.getUtf8Bytes(String.format(Locale.US, "%02d:%02d:%02d,%03d", Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds), Integer.valueOf(milliseconds)));
        }
        System.arraycopy(timeCodeData, 0, subripSampleData, 19, 12);
    }

    private void readToTarget(ExtractorInput input, byte[] target, int offset, int length) throws InterruptedException, IOException {
        int pendingStrippedBytes = Math.min(length, this.sampleStrippedBytes.bytesLeft());
        input.readFully(target, offset + pendingStrippedBytes, length - pendingStrippedBytes);
        if (pendingStrippedBytes > 0) {
            this.sampleStrippedBytes.readBytes(target, offset, pendingStrippedBytes);
        }
        this.sampleBytesRead += length;
    }

    private int readToOutput(ExtractorInput input, TrackOutput output, int length) throws InterruptedException, IOException {
        int bytesRead;
        int strippedBytesLeft = this.sampleStrippedBytes.bytesLeft();
        if (strippedBytesLeft > 0) {
            bytesRead = Math.min(length, strippedBytesLeft);
            output.sampleData(this.sampleStrippedBytes, bytesRead);
        } else {
            bytesRead = output.sampleData(input, length, false);
        }
        this.sampleBytesRead += bytesRead;
        this.sampleBytesWritten += bytesRead;
        return bytesRead;
    }

    private SeekMap buildSeekMap() {
        if (this.segmentContentPosition == -1 || this.durationUs == C.TIME_UNSET || this.cueTimesUs == null || this.cueTimesUs.size() == 0 || this.cueClusterPositions == null || this.cueClusterPositions.size() != this.cueTimesUs.size()) {
            this.cueTimesUs = null;
            this.cueClusterPositions = null;
            return new SeekMap.Unseekable(this.durationUs);
        }
        int cuePointsSize = this.cueTimesUs.size();
        int[] sizes = new int[cuePointsSize];
        long[] offsets = new long[cuePointsSize];
        long[] durationsUs = new long[cuePointsSize];
        long[] timesUs = new long[cuePointsSize];
        for (int i = 0; i < cuePointsSize; i++) {
            timesUs[i] = this.cueTimesUs.get(i);
            offsets[i] = this.segmentContentPosition + this.cueClusterPositions.get(i);
        }
        for (int i2 = 0; i2 < cuePointsSize - 1; i2++) {
            sizes[i2] = (int) (offsets[i2 + 1] - offsets[i2]);
            durationsUs[i2] = timesUs[i2 + 1] - timesUs[i2];
        }
        sizes[cuePointsSize - 1] = (int) ((this.segmentContentPosition + this.segmentContentSize) - offsets[cuePointsSize - 1]);
        durationsUs[cuePointsSize - 1] = this.durationUs - timesUs[cuePointsSize - 1];
        this.cueTimesUs = null;
        this.cueClusterPositions = null;
        return new ChunkIndex(sizes, offsets, durationsUs, timesUs);
    }

    private boolean maybeSeekForCues(PositionHolder seekPosition, long currentPosition) {
        if (this.seekForCues) {
            this.seekPositionAfterBuildingCues = currentPosition;
            seekPosition.position = this.cuesContentPosition;
            this.seekForCues = false;
            return true;
        }
        if (!this.sentSeekMap || this.seekPositionAfterBuildingCues == -1) {
            return false;
        }
        seekPosition.position = this.seekPositionAfterBuildingCues;
        this.seekPositionAfterBuildingCues = -1L;
        return true;
    }

    private long scaleTimecodeToUs(long unscaledTimecode) throws ParserException {
        if (this.timecodeScale == C.TIME_UNSET) {
            throw new ParserException("Can't scale timecode prior to timecodeScale being set.");
        }
        return Util.scaleLargeTimestamp(unscaledTimecode, this.timecodeScale, 1000L);
    }

    private static boolean isCodecSupported(String codecId) {
        return CODEC_ID_VP8.equals(codecId) || CODEC_ID_VP9.equals(codecId) || CODEC_ID_MPEG2.equals(codecId) || CODEC_ID_MPEG4_SP.equals(codecId) || CODEC_ID_MPEG4_ASP.equals(codecId) || CODEC_ID_MPEG4_AP.equals(codecId) || CODEC_ID_H264.equals(codecId) || CODEC_ID_H265.equals(codecId) || CODEC_ID_FOURCC.equals(codecId) || CODEC_ID_THEORA.equals(codecId) || CODEC_ID_OPUS.equals(codecId) || CODEC_ID_VORBIS.equals(codecId) || CODEC_ID_AAC.equals(codecId) || CODEC_ID_MP2.equals(codecId) || CODEC_ID_MP3.equals(codecId) || CODEC_ID_AC3.equals(codecId) || CODEC_ID_E_AC3.equals(codecId) || CODEC_ID_TRUEHD.equals(codecId) || CODEC_ID_DTS.equals(codecId) || CODEC_ID_DTS_EXPRESS.equals(codecId) || CODEC_ID_DTS_LOSSLESS.equals(codecId) || CODEC_ID_FLAC.equals(codecId) || CODEC_ID_ACM.equals(codecId) || CODEC_ID_PCM_INT_LIT.equals(codecId) || CODEC_ID_SUBRIP.equals(codecId) || CODEC_ID_VOBSUB.equals(codecId) || CODEC_ID_PGS.equals(codecId) || CODEC_ID_DVBSUB.equals(codecId);
    }

    private static int[] ensureArrayCapacity(int[] array, int length) {
        if (array == null) {
            return new int[length];
        }
        return array.length < length ? new int[Math.max(array.length * 2, length)] : array;
    }

    private final class InnerEbmlReaderOutput implements EbmlReaderOutput {
        private InnerEbmlReaderOutput() {
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlReaderOutput
        public int getElementType(int id) {
            return MatroskaExtractor.this.getElementType(id);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlReaderOutput
        public boolean isLevel1Element(int id) {
            return MatroskaExtractor.this.isLevel1Element(id);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlReaderOutput
        public void startMasterElement(int id, long contentPosition, long contentSize) throws ParserException {
            MatroskaExtractor.this.startMasterElement(id, contentPosition, contentSize);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlReaderOutput
        public void endMasterElement(int id) throws ParserException {
            MatroskaExtractor.this.endMasterElement(id);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlReaderOutput
        public void integerElement(int id, long value) throws ParserException {
            MatroskaExtractor.this.integerElement(id, value);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlReaderOutput
        public void floatElement(int id, double value) throws ParserException {
            MatroskaExtractor.this.floatElement(id, value);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlReaderOutput
        public void stringElement(int id, String value) throws ParserException {
            MatroskaExtractor.this.stringElement(id, value);
        }

        @Override // com.google.android.exoplayer2.extractor.mkv.EbmlReaderOutput
        public void binaryElement(int id, int contentsSize, ExtractorInput input) throws InterruptedException, IOException {
            MatroskaExtractor.this.binaryElement(id, contentsSize, input);
        }
    }

    private static final class Track {
        private static final int DEFAULT_MAX_CLL = 1000;
        private static final int DEFAULT_MAX_FALL = 200;
        private static final int DISPLAY_UNIT_PIXELS = 0;
        private static final int MAX_CHROMATICITY = 50000;
        public int audioBitDepth;
        public int channelCount;
        public long codecDelayNs;
        public String codecId;
        public byte[] codecPrivate;
        public int colorRange;
        public int colorSpace;
        public int colorTransfer;
        public int defaultSampleDurationNs;
        public int displayHeight;
        public int displayUnit;
        public int displayWidth;
        public DrmInitData drmInitData;
        public byte[] encryptionKeyId;
        public boolean flagDefault;
        public boolean flagForced;
        public boolean hasColorInfo;
        public boolean hasContentEncryption;
        public int height;
        private String language;
        public int maxContentLuminance;
        public int maxFrameAverageLuminance;
        public float maxMasteringLuminance;
        public float minMasteringLuminance;
        public int nalUnitLengthFieldLength;
        public int number;
        public TrackOutput output;
        public float primaryBChromaticityX;
        public float primaryBChromaticityY;
        public float primaryGChromaticityX;
        public float primaryGChromaticityY;
        public float primaryRChromaticityX;
        public float primaryRChromaticityY;
        public byte[] projectionData;
        public int sampleRate;
        public byte[] sampleStrippedBytes;
        public long seekPreRollNs;
        public int stereoMode;
        public int type;
        public float whitePointChromaticityX;
        public float whitePointChromaticityY;
        public int width;

        private Track() {
            this.width = -1;
            this.height = -1;
            this.displayWidth = -1;
            this.displayHeight = -1;
            this.displayUnit = 0;
            this.projectionData = null;
            this.stereoMode = -1;
            this.hasColorInfo = false;
            this.colorSpace = -1;
            this.colorTransfer = -1;
            this.colorRange = -1;
            this.maxContentLuminance = 1000;
            this.maxFrameAverageLuminance = 200;
            this.primaryRChromaticityX = -1.0f;
            this.primaryRChromaticityY = -1.0f;
            this.primaryGChromaticityX = -1.0f;
            this.primaryGChromaticityY = -1.0f;
            this.primaryBChromaticityX = -1.0f;
            this.primaryBChromaticityY = -1.0f;
            this.whitePointChromaticityX = -1.0f;
            this.whitePointChromaticityY = -1.0f;
            this.maxMasteringLuminance = -1.0f;
            this.minMasteringLuminance = -1.0f;
            this.channelCount = 1;
            this.audioBitDepth = -1;
            this.sampleRate = 8000;
            this.codecDelayNs = 0L;
            this.seekPreRollNs = 0L;
            this.flagDefault = true;
            this.language = "eng";
        }

        public void initializeOutput(ExtractorOutput output, int trackId) throws ParserException {
            int maxInputSize;
            int pcmEncoding;
            List<byte[]> initializationData;
            String mimeType;
            int type;
            Format format;
            maxInputSize = -1;
            pcmEncoding = -1;
            initializationData = null;
            switch (this.codecId) {
                case "V_VP8":
                    mimeType = MimeTypes.VIDEO_VP8;
                    break;
                case "V_VP9":
                    mimeType = MimeTypes.VIDEO_VP9;
                    break;
                case "V_MPEG2":
                    mimeType = MimeTypes.VIDEO_MPEG2;
                    break;
                case "V_MPEG4/ISO/SP":
                case "V_MPEG4/ISO/ASP":
                case "V_MPEG4/ISO/AP":
                    mimeType = MimeTypes.VIDEO_MP4V;
                    if (this.codecPrivate != null) {
                        initializationData = Collections.singletonList(this.codecPrivate);
                        break;
                    } else {
                        initializationData = null;
                        break;
                    }
                case "V_MPEG4/ISO/AVC":
                    mimeType = MimeTypes.VIDEO_H264;
                    AvcConfig avcConfig = AvcConfig.parse(new ParsableByteArray(this.codecPrivate));
                    initializationData = avcConfig.initializationData;
                    this.nalUnitLengthFieldLength = avcConfig.nalUnitLengthFieldLength;
                    break;
                case "V_MPEGH/ISO/HEVC":
                    mimeType = MimeTypes.VIDEO_H265;
                    HevcConfig hevcConfig = HevcConfig.parse(new ParsableByteArray(this.codecPrivate));
                    initializationData = hevcConfig.initializationData;
                    this.nalUnitLengthFieldLength = hevcConfig.nalUnitLengthFieldLength;
                    break;
                case "V_MS/VFW/FOURCC":
                    initializationData = parseFourCcVc1Private(new ParsableByteArray(this.codecPrivate));
                    if (initializationData != null) {
                        mimeType = MimeTypes.VIDEO_VC1;
                        break;
                    } else {
                        mimeType = MimeTypes.VIDEO_UNKNOWN;
                        break;
                    }
                case "V_THEORA":
                    mimeType = MimeTypes.VIDEO_UNKNOWN;
                    break;
                case "A_VORBIS":
                    mimeType = MimeTypes.AUDIO_VORBIS;
                    maxInputSize = 8192;
                    initializationData = parseVorbisCodecPrivate(this.codecPrivate);
                    break;
                case "A_OPUS":
                    mimeType = MimeTypes.AUDIO_OPUS;
                    maxInputSize = MatroskaExtractor.OPUS_MAX_INPUT_SIZE;
                    initializationData = new ArrayList<>(3);
                    initializationData.add(this.codecPrivate);
                    initializationData.add(ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(this.codecDelayNs).array());
                    initializationData.add(ByteBuffer.allocate(8).order(ByteOrder.nativeOrder()).putLong(this.seekPreRollNs).array());
                    break;
                case "A_AAC":
                    mimeType = MimeTypes.AUDIO_AAC;
                    initializationData = Collections.singletonList(this.codecPrivate);
                    break;
                case "A_MPEG/L2":
                    mimeType = MimeTypes.AUDIO_MPEG_L2;
                    maxInputSize = 4096;
                    break;
                case "A_MPEG/L3":
                    mimeType = MimeTypes.AUDIO_MPEG;
                    maxInputSize = 4096;
                    break;
                case "A_AC3":
                    mimeType = MimeTypes.AUDIO_AC3;
                    break;
                case "A_EAC3":
                    mimeType = MimeTypes.AUDIO_E_AC3;
                    break;
                case "A_TRUEHD":
                    mimeType = MimeTypes.AUDIO_TRUEHD;
                    break;
                case "A_DTS":
                case "A_DTS/EXPRESS":
                    mimeType = MimeTypes.AUDIO_DTS;
                    break;
                case "A_DTS/LOSSLESS":
                    mimeType = MimeTypes.AUDIO_DTS_HD;
                    break;
                case "A_FLAC":
                    mimeType = MimeTypes.AUDIO_FLAC;
                    initializationData = Collections.singletonList(this.codecPrivate);
                    break;
                case "A_MS/ACM":
                    mimeType = MimeTypes.AUDIO_RAW;
                    if (!parseMsAcmCodecPrivate(new ParsableByteArray(this.codecPrivate))) {
                        throw new ParserException("Non-PCM MS/ACM is unsupported");
                    }
                    pcmEncoding = Util.getPcmEncoding(this.audioBitDepth);
                    if (pcmEncoding == 0) {
                        throw new ParserException("Unsupported PCM bit depth: " + this.audioBitDepth);
                    }
                    break;
                case "A_PCM/INT/LIT":
                    mimeType = MimeTypes.AUDIO_RAW;
                    pcmEncoding = Util.getPcmEncoding(this.audioBitDepth);
                    if (pcmEncoding == 0) {
                        throw new ParserException("Unsupported PCM bit depth: " + this.audioBitDepth);
                    }
                    break;
                case "S_TEXT/UTF8":
                    mimeType = MimeTypes.APPLICATION_SUBRIP;
                    break;
                case "S_VOBSUB":
                    mimeType = MimeTypes.APPLICATION_VOBSUB;
                    initializationData = Collections.singletonList(this.codecPrivate);
                    break;
                case "S_HDMV/PGS":
                    mimeType = MimeTypes.APPLICATION_PGS;
                    break;
                case "S_DVBSUB":
                    mimeType = MimeTypes.APPLICATION_DVBSUBS;
                    initializationData = Collections.singletonList(new byte[]{this.codecPrivate[0], this.codecPrivate[1], this.codecPrivate[2], this.codecPrivate[3]});
                    break;
                default:
                    throw new ParserException("Unrecognized codec identifier.");
            }
            int selectionFlags = 0 | (this.flagDefault ? 1 : 0) | (this.flagForced ? 2 : 0);
            if (MimeTypes.isAudio(mimeType)) {
                type = 1;
                format = Format.createAudioSampleFormat(Integer.toString(trackId), mimeType, null, -1, maxInputSize, this.channelCount, this.sampleRate, pcmEncoding, initializationData, this.drmInitData, selectionFlags, this.language);
            } else if (MimeTypes.isVideo(mimeType)) {
                type = 2;
                if (this.displayUnit == 0) {
                    this.displayWidth = this.displayWidth == -1 ? this.width : this.displayWidth;
                    this.displayHeight = this.displayHeight == -1 ? this.height : this.displayHeight;
                }
                float pixelWidthHeightRatio = -1.0f;
                if (this.displayWidth != -1 && this.displayHeight != -1) {
                    pixelWidthHeightRatio = (this.height * this.displayWidth) / (this.width * this.displayHeight);
                }
                ColorInfo colorInfo = null;
                if (this.hasColorInfo) {
                    byte[] hdrStaticInfo = getHdrStaticInfo();
                    colorInfo = new ColorInfo(this.colorSpace, this.colorRange, this.colorTransfer, hdrStaticInfo);
                }
                format = Format.createVideoSampleFormat(Integer.toString(trackId), mimeType, null, -1, maxInputSize, this.width, this.height, -1.0f, initializationData, -1, pixelWidthHeightRatio, this.projectionData, this.stereoMode, colorInfo, this.drmInitData);
            } else if (MimeTypes.APPLICATION_SUBRIP.equals(mimeType)) {
                type = 3;
                format = Format.createTextSampleFormat(Integer.toString(trackId), mimeType, null, -1, selectionFlags, this.language, this.drmInitData);
            } else if (MimeTypes.APPLICATION_VOBSUB.equals(mimeType) || MimeTypes.APPLICATION_PGS.equals(mimeType) || MimeTypes.APPLICATION_DVBSUBS.equals(mimeType)) {
                type = 3;
                format = Format.createImageSampleFormat(Integer.toString(trackId), mimeType, null, -1, initializationData, this.language, this.drmInitData);
            } else {
                throw new ParserException("Unexpected MIME type.");
            }
            this.output = output.track(this.number, type);
            this.output.format(format);
        }

        private byte[] getHdrStaticInfo() {
            if (this.primaryRChromaticityX == -1.0f || this.primaryRChromaticityY == -1.0f || this.primaryGChromaticityX == -1.0f || this.primaryGChromaticityY == -1.0f || this.primaryBChromaticityX == -1.0f || this.primaryBChromaticityY == -1.0f || this.whitePointChromaticityX == -1.0f || this.whitePointChromaticityY == -1.0f || this.maxMasteringLuminance == -1.0f || this.minMasteringLuminance == -1.0f) {
                return null;
            }
            byte[] hdrStaticInfoData = new byte[25];
            ByteBuffer hdrStaticInfo = ByteBuffer.wrap(hdrStaticInfoData);
            hdrStaticInfo.put((byte) 0);
            hdrStaticInfo.putShort((short) ((this.primaryRChromaticityX * 50000.0f) + 0.5f));
            hdrStaticInfo.putShort((short) ((this.primaryRChromaticityY * 50000.0f) + 0.5f));
            hdrStaticInfo.putShort((short) ((this.primaryGChromaticityX * 50000.0f) + 0.5f));
            hdrStaticInfo.putShort((short) ((this.primaryGChromaticityY * 50000.0f) + 0.5f));
            hdrStaticInfo.putShort((short) ((this.primaryBChromaticityX * 50000.0f) + 0.5f));
            hdrStaticInfo.putShort((short) ((this.primaryBChromaticityY * 50000.0f) + 0.5f));
            hdrStaticInfo.putShort((short) ((this.whitePointChromaticityX * 50000.0f) + 0.5f));
            hdrStaticInfo.putShort((short) ((this.whitePointChromaticityY * 50000.0f) + 0.5f));
            hdrStaticInfo.putShort((short) (this.maxMasteringLuminance + 0.5f));
            hdrStaticInfo.putShort((short) (this.minMasteringLuminance + 0.5f));
            hdrStaticInfo.putShort((short) this.maxContentLuminance);
            hdrStaticInfo.putShort((short) this.maxFrameAverageLuminance);
            return hdrStaticInfoData;
        }

        private static List<byte[]> parseFourCcVc1Private(ParsableByteArray buffer) throws ParserException {
            try {
                buffer.skipBytes(16);
                long compression = buffer.readLittleEndianUnsignedInt();
                if (compression != 826496599) {
                    return null;
                }
                int startOffset = buffer.getPosition() + 20;
                byte[] bufferData = buffer.data;
                for (int offset = startOffset; offset < bufferData.length - 4; offset++) {
                    if (bufferData[offset] == 0 && bufferData[offset + 1] == 0 && bufferData[offset + 2] == 1 && bufferData[offset + 3] == 15) {
                        byte[] initializationData = Arrays.copyOfRange(bufferData, offset, bufferData.length);
                        return Collections.singletonList(initializationData);
                    }
                }
                throw new ParserException("Failed to find FourCC VC1 initialization data");
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParserException("Error parsing FourCC VC1 codec private");
            }
        }

        private static List<byte[]> parseVorbisCodecPrivate(byte[] codecPrivate) throws ParserException {
            try {
                if (codecPrivate[0] != 2) {
                    throw new ParserException("Error parsing vorbis codec private");
                }
                int vorbisInfoLength = 0;
                int offset = 1;
                while (codecPrivate[offset] == -1) {
                    vorbisInfoLength += 255;
                    offset++;
                }
                int vorbisInfoLength2 = vorbisInfoLength + codecPrivate[offset];
                int vorbisSkipLength = 0;
                int offset2 = offset + 1;
                while (codecPrivate[offset2] == -1) {
                    vorbisSkipLength += 255;
                    offset2++;
                }
                int offset3 = offset2 + 1;
                int vorbisSkipLength2 = vorbisSkipLength + codecPrivate[offset2];
                if (codecPrivate[offset3] != 1) {
                    throw new ParserException("Error parsing vorbis codec private");
                }
                byte[] vorbisInfo = new byte[vorbisInfoLength2];
                System.arraycopy(codecPrivate, offset3, vorbisInfo, 0, vorbisInfoLength2);
                int offset4 = offset3 + vorbisInfoLength2;
                if (codecPrivate[offset4] != 3) {
                    throw new ParserException("Error parsing vorbis codec private");
                }
                int offset5 = offset4 + vorbisSkipLength2;
                if (codecPrivate[offset5] != 5) {
                    throw new ParserException("Error parsing vorbis codec private");
                }
                byte[] vorbisBooks = new byte[codecPrivate.length - offset5];
                System.arraycopy(codecPrivate, offset5, vorbisBooks, 0, codecPrivate.length - offset5);
                List<byte[]> initializationData = new ArrayList<>(2);
                initializationData.add(vorbisInfo);
                initializationData.add(vorbisBooks);
                return initializationData;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParserException("Error parsing vorbis codec private");
            }
        }

        private static boolean parseMsAcmCodecPrivate(ParsableByteArray buffer) throws ParserException {
            try {
                int formatTag = buffer.readLittleEndianUnsignedShort();
                if (formatTag == 1) {
                    return true;
                }
                if (formatTag != MatroskaExtractor.WAVE_FORMAT_EXTENSIBLE) {
                    return false;
                }
                buffer.setPosition(24);
                if (buffer.readLong() == MatroskaExtractor.WAVE_SUBFORMAT_PCM.getMostSignificantBits()) {
                    if (buffer.readLong() == MatroskaExtractor.WAVE_SUBFORMAT_PCM.getLeastSignificantBits()) {
                        return true;
                    }
                }
                return false;
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParserException("Error parsing MS/ACM codec private");
            }
        }
    }
}
