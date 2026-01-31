package com.google.android.exoplayer2.extractor.ts;

import android.util.SparseArray;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class DefaultTsPayloadReaderFactory implements TsPayloadReader.Factory {
    private static final int DESCRIPTOR_TAG_CAPTION_SERVICE = 134;
    public static final int FLAG_ALLOW_NON_IDR_KEYFRAMES = 1;
    public static final int FLAG_DETECT_ACCESS_UNITS = 8;
    public static final int FLAG_IGNORE_AAC_STREAM = 2;
    public static final int FLAG_IGNORE_H264_STREAM = 4;
    public static final int FLAG_IGNORE_SPLICE_INFO_STREAM = 16;
    public static final int FLAG_OVERRIDE_CAPTION_DESCRIPTORS = 32;
    private final List<Format> closedCaptionFormats;
    private final int flags;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    public DefaultTsPayloadReaderFactory() {
        this(0);
    }

    public DefaultTsPayloadReaderFactory(int flags) {
        this(flags, Collections.emptyList());
    }

    public DefaultTsPayloadReaderFactory(int flags, List<Format> closedCaptionFormats) {
        this.flags = flags;
        if (!isSet(32) && closedCaptionFormats.isEmpty()) {
            closedCaptionFormats = Collections.singletonList(Format.createTextSampleFormat(null, MimeTypes.APPLICATION_CEA608, null, -1, 0, null, null));
        }
        this.closedCaptionFormats = closedCaptionFormats;
    }

    @Override // com.google.android.exoplayer2.extractor.ts.TsPayloadReader.Factory
    public SparseArray<TsPayloadReader> createInitialPayloadReaders() {
        return new SparseArray<>();
    }

    @Override // com.google.android.exoplayer2.extractor.ts.TsPayloadReader.Factory
    public TsPayloadReader createPayloadReader(int streamType, TsPayloadReader.EsInfo esInfo) {
        switch (streamType) {
            case 2:
                return new PesReader(new H262Reader());
            case 3:
            case 4:
                return new PesReader(new MpegAudioReader(esInfo.language));
            case 15:
                if (isSet(2)) {
                    return null;
                }
                return new PesReader(new AdtsReader(false, esInfo.language));
            case 21:
                return new PesReader(new Id3Reader());
            case 27:
                if (isSet(4)) {
                    return null;
                }
                return new PesReader(new H264Reader(buildSeiReader(esInfo), isSet(1), isSet(8)));
            case 36:
                return new PesReader(new H265Reader(buildSeiReader(esInfo)));
            case TsExtractor.TS_STREAM_TYPE_DVBSUBS /* 89 */:
                return new PesReader(new DvbSubtitleReader(esInfo.dvbSubtitleInfos));
            case TsExtractor.TS_STREAM_TYPE_AC3 /* 129 */:
            case TsExtractor.TS_STREAM_TYPE_E_AC3 /* 135 */:
                return new PesReader(new Ac3Reader(esInfo.language));
            case TsExtractor.TS_STREAM_TYPE_HDMV_DTS /* 130 */:
            case TsExtractor.TS_STREAM_TYPE_DTS /* 138 */:
                return new PesReader(new DtsReader(esInfo.language));
            case 134:
                if (isSet(16)) {
                    return null;
                }
                return new SectionReader(new SpliceInfoSectionReader());
            default:
                return null;
        }
    }

    private SeiReader buildSeiReader(TsPayloadReader.EsInfo esInfo) {
        String mimeType;
        int accessibilityChannel;
        if (isSet(32)) {
            return new SeiReader(this.closedCaptionFormats);
        }
        ParsableByteArray scratchDescriptorData = new ParsableByteArray(esInfo.descriptorBytes);
        List<Format> closedCaptionFormats = this.closedCaptionFormats;
        while (scratchDescriptorData.bytesLeft() > 0) {
            int descriptorTag = scratchDescriptorData.readUnsignedByte();
            int descriptorLength = scratchDescriptorData.readUnsignedByte();
            int nextDescriptorPosition = scratchDescriptorData.getPosition() + descriptorLength;
            if (descriptorTag == 134) {
                closedCaptionFormats = new ArrayList<>();
                int numberOfServices = scratchDescriptorData.readUnsignedByte() & 31;
                for (int i = 0; i < numberOfServices; i++) {
                    String language = scratchDescriptorData.readString(3);
                    int captionTypeByte = scratchDescriptorData.readUnsignedByte();
                    boolean isDigital = (captionTypeByte & 128) != 0;
                    if (isDigital) {
                        mimeType = MimeTypes.APPLICATION_CEA708;
                        accessibilityChannel = captionTypeByte & 63;
                    } else {
                        mimeType = MimeTypes.APPLICATION_CEA608;
                        accessibilityChannel = 1;
                    }
                    closedCaptionFormats.add(Format.createTextSampleFormat((String) null, mimeType, (String) null, -1, 0, language, accessibilityChannel, (DrmInitData) null));
                    scratchDescriptorData.skipBytes(2);
                }
            }
            scratchDescriptorData.setPosition(nextDescriptorPosition);
        }
        return new SeiReader(closedCaptionFormats);
    }

    private boolean isSet(int flag) {
        return (this.flags & flag) != 0;
    }
}
