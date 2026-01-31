package com.google.android.exoplayer2.drm;

import android.annotation.TargetApi;
import android.media.MediaCrypto;
import com.google.android.exoplayer2.util.Assertions;

@TargetApi(16)
/* loaded from: classes.dex */
public final class FrameworkMediaCrypto implements ExoMediaCrypto {
    private final MediaCrypto mediaCrypto;

    FrameworkMediaCrypto(MediaCrypto mediaCrypto) {
        this.mediaCrypto = (MediaCrypto) Assertions.checkNotNull(mediaCrypto);
    }

    public MediaCrypto getWrappedMediaCrypto() {
        return this.mediaCrypto;
    }

    @Override // com.google.android.exoplayer2.drm.ExoMediaCrypto
    public boolean requiresSecureDecoderComponent(String mimeType) {
        return this.mediaCrypto.requiresSecureDecoderComponent(mimeType);
    }
}
