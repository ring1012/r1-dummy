package com.google.android.exoplayer2.metadata;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.metadata.emsg.EventMessageDecoder;
import com.google.android.exoplayer2.metadata.id3.Id3Decoder;
import com.google.android.exoplayer2.metadata.scte35.SpliceInfoDecoder;
import com.google.android.exoplayer2.util.MimeTypes;

/* loaded from: classes.dex */
public interface MetadataDecoderFactory {
    public static final MetadataDecoderFactory DEFAULT = new MetadataDecoderFactory() { // from class: com.google.android.exoplayer2.metadata.MetadataDecoderFactory.1
        @Override // com.google.android.exoplayer2.metadata.MetadataDecoderFactory
        public boolean supportsFormat(Format format) {
            String mimeType = format.sampleMimeType;
            return MimeTypes.APPLICATION_ID3.equals(mimeType) || MimeTypes.APPLICATION_EMSG.equals(mimeType) || MimeTypes.APPLICATION_SCTE35.equals(mimeType);
        }

        @Override // com.google.android.exoplayer2.metadata.MetadataDecoderFactory
        public MetadataDecoder createDecoder(Format format) {
            switch (format.sampleMimeType) {
                case "application/id3":
                    return new Id3Decoder();
                case "application/x-emsg":
                    return new EventMessageDecoder();
                case "application/x-scte35":
                    return new SpliceInfoDecoder();
                default:
                    throw new IllegalArgumentException("Attempted to create decoder for unsupported format");
            }
        }
    };

    MetadataDecoder createDecoder(Format format);

    boolean supportsFormat(Format format);
}
