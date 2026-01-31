package com.google.android.exoplayer2.text;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.text.cea.Cea608Decoder;
import com.google.android.exoplayer2.text.cea.Cea708Decoder;
import com.google.android.exoplayer2.text.dvb.DvbDecoder;
import com.google.android.exoplayer2.text.subrip.SubripDecoder;
import com.google.android.exoplayer2.text.ttml.TtmlDecoder;
import com.google.android.exoplayer2.text.tx3g.Tx3gDecoder;
import com.google.android.exoplayer2.text.webvtt.Mp4WebvttDecoder;
import com.google.android.exoplayer2.text.webvtt.WebvttDecoder;
import com.google.android.exoplayer2.util.MimeTypes;

/* loaded from: classes.dex */
public interface SubtitleDecoderFactory {
    public static final SubtitleDecoderFactory DEFAULT = new SubtitleDecoderFactory() { // from class: com.google.android.exoplayer2.text.SubtitleDecoderFactory.1
        @Override // com.google.android.exoplayer2.text.SubtitleDecoderFactory
        public boolean supportsFormat(Format format) {
            String mimeType = format.sampleMimeType;
            return MimeTypes.TEXT_VTT.equals(mimeType) || MimeTypes.APPLICATION_TTML.equals(mimeType) || MimeTypes.APPLICATION_MP4VTT.equals(mimeType) || MimeTypes.APPLICATION_SUBRIP.equals(mimeType) || MimeTypes.APPLICATION_TX3G.equals(mimeType) || MimeTypes.APPLICATION_CEA608.equals(mimeType) || MimeTypes.APPLICATION_MP4CEA608.equals(mimeType) || MimeTypes.APPLICATION_CEA708.equals(mimeType) || MimeTypes.APPLICATION_DVBSUBS.equals(mimeType);
        }

        @Override // com.google.android.exoplayer2.text.SubtitleDecoderFactory
        public SubtitleDecoder createDecoder(Format format) {
            switch (format.sampleMimeType) {
                case "text/vtt":
                    return new WebvttDecoder();
                case "application/x-mp4-vtt":
                    return new Mp4WebvttDecoder();
                case "application/ttml+xml":
                    return new TtmlDecoder();
                case "application/x-subrip":
                    return new SubripDecoder();
                case "application/x-quicktime-tx3g":
                    return new Tx3gDecoder(format.initializationData);
                case "application/cea-608":
                case "application/x-mp4-cea-608":
                    return new Cea608Decoder(format.sampleMimeType, format.accessibilityChannel);
                case "application/cea-708":
                    return new Cea708Decoder(format.accessibilityChannel);
                case "application/dvbsubs":
                    return new DvbDecoder(format.initializationData);
                default:
                    throw new IllegalArgumentException("Attempted to create decoder for unsupported format");
            }
        }
    };

    SubtitleDecoder createDecoder(Format format);

    boolean supportsFormat(Format format);
}
