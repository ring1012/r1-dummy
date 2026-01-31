package com.google.android.exoplayer2.mediacodec;

import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;

/* loaded from: classes.dex */
public interface MediaCodecSelector {
    public static final MediaCodecSelector DEFAULT = new MediaCodecSelector() { // from class: com.google.android.exoplayer2.mediacodec.MediaCodecSelector.1
        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecSelector
        public MediaCodecInfo getDecoderInfo(String mimeType, boolean requiresSecureDecoder) throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getDecoderInfo(mimeType, requiresSecureDecoder);
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecSelector
        public MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException {
            return MediaCodecUtil.getPassthroughDecoderInfo();
        }
    };

    MediaCodecInfo getDecoderInfo(String str, boolean z) throws MediaCodecUtil.DecoderQueryException;

    MediaCodecInfo getPassthroughDecoderInfo() throws MediaCodecUtil.DecoderQueryException;
}
