package com.google.android.exoplayer2.text.webvtt;

import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.webvtt.WebvttCue;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class Mp4WebvttDecoder extends SimpleSubtitleDecoder {
    private static final int BOX_HEADER_SIZE = 8;
    private static final int TYPE_payl = Util.getIntegerCodeForString("payl");
    private static final int TYPE_sttg = Util.getIntegerCodeForString("sttg");
    private static final int TYPE_vttc = Util.getIntegerCodeForString("vttc");
    private final WebvttCue.Builder builder;
    private final ParsableByteArray sampleData;

    public Mp4WebvttDecoder() {
        super("Mp4WebvttDecoder");
        this.sampleData = new ParsableByteArray();
        this.builder = new WebvttCue.Builder();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.text.SimpleSubtitleDecoder
    public Mp4WebvttSubtitle decode(byte[] bytes, int length, boolean reset) throws SubtitleDecoderException {
        this.sampleData.reset(bytes, length);
        List<Cue> resultingCueList = new ArrayList<>();
        while (this.sampleData.bytesLeft() > 0) {
            if (this.sampleData.bytesLeft() < 8) {
                throw new SubtitleDecoderException("Incomplete Mp4Webvtt Top Level box header found.");
            }
            int boxSize = this.sampleData.readInt();
            int boxType = this.sampleData.readInt();
            if (boxType == TYPE_vttc) {
                resultingCueList.add(parseVttCueBox(this.sampleData, this.builder, boxSize - 8));
            } else {
                this.sampleData.skipBytes(boxSize - 8);
            }
        }
        return new Mp4WebvttSubtitle(resultingCueList);
    }

    private static Cue parseVttCueBox(ParsableByteArray sampleData, WebvttCue.Builder builder, int remainingCueBoxBytes) throws SubtitleDecoderException {
        builder.reset();
        while (remainingCueBoxBytes > 0) {
            if (remainingCueBoxBytes < 8) {
                throw new SubtitleDecoderException("Incomplete vtt cue box header found.");
            }
            int boxSize = sampleData.readInt();
            int boxType = sampleData.readInt();
            int payloadLength = boxSize - 8;
            String boxPayload = new String(sampleData.data, sampleData.getPosition(), payloadLength);
            sampleData.skipBytes(payloadLength);
            remainingCueBoxBytes = (remainingCueBoxBytes - 8) - payloadLength;
            if (boxType == TYPE_sttg) {
                WebvttCueParser.parseCueSettingsList(boxPayload, builder);
            } else if (boxType == TYPE_payl) {
                WebvttCueParser.parseCueText(null, boxPayload.trim(), builder, Collections.emptyList());
            }
        }
        return builder.build();
    }
}
