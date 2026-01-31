package com.google.android.exoplayer2.text.dvb;

import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.List;

/* loaded from: classes.dex */
public final class DvbDecoder extends SimpleSubtitleDecoder {
    private final DvbParser parser;

    public DvbDecoder(List<byte[]> initializationData) {
        super("DvbDecoder");
        ParsableByteArray data = new ParsableByteArray(initializationData.get(0));
        int subtitleCompositionPage = data.readUnsignedShort();
        int subtitleAncillaryPage = data.readUnsignedShort();
        this.parser = new DvbParser(subtitleCompositionPage, subtitleAncillaryPage);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.text.SimpleSubtitleDecoder
    public DvbSubtitle decode(byte[] data, int length, boolean reset) {
        if (reset) {
            this.parser.reset();
        }
        return new DvbSubtitle(this.parser.decode(data, length));
    }
}
