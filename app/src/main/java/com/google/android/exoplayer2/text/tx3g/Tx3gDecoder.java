package com.google.android.exoplayer2.text.tx3g;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.android.exoplayer2.util.Util;
import java.nio.charset.Charset;
import java.util.List;

/* loaded from: classes.dex */
public final class Tx3gDecoder extends SimpleSubtitleDecoder {
    private static final char BOM_UTF16_BE = 65279;
    private static final char BOM_UTF16_LE = 65534;
    private static final int DEFAULT_COLOR = -1;
    private static final int DEFAULT_FONT_FACE = 0;
    private static final String DEFAULT_FONT_FAMILY = "sans-serif";
    private static final float DEFAULT_VERTICAL_PLACEMENT = 0.85f;
    private static final int FONT_FACE_BOLD = 1;
    private static final int FONT_FACE_ITALIC = 2;
    private static final int FONT_FACE_UNDERLINE = 4;
    private static final int SIZE_ATOM_HEADER = 8;
    private static final int SIZE_BOM_UTF16 = 2;
    private static final int SIZE_SHORT = 2;
    private static final int SIZE_STYLE_RECORD = 12;
    private static final int SPAN_PRIORITY_HIGH = 0;
    private static final int SPAN_PRIORITY_LOW = 16711680;
    private static final String TX3G_SERIF = "Serif";
    private static final int TYPE_STYL = Util.getIntegerCodeForString("styl");
    private static final int TYPE_TBOX = Util.getIntegerCodeForString("tbox");
    private int calculatedVideoTrackHeight;
    private boolean customVerticalPlacement;
    private int defaultColorRgba;
    private int defaultFontFace;
    private String defaultFontFamily;
    private float defaultVerticalPlacement;
    private final ParsableByteArray parsableByteArray;

    public Tx3gDecoder(List<byte[]> initializationData) {
        super("Tx3gDecoder");
        this.parsableByteArray = new ParsableByteArray();
        decodeInitializationData(initializationData);
    }

    private void decodeInitializationData(List<byte[]> initializationData) {
        if (initializationData != null && initializationData.size() == 1 && (initializationData.get(0).length == 48 || initializationData.get(0).length == 53)) {
            byte[] initializationBytes = initializationData.get(0);
            this.defaultFontFace = initializationBytes[24];
            this.defaultColorRgba = ((initializationBytes[26] & 255) << 24) | ((initializationBytes[27] & 255) << 16) | ((initializationBytes[28] & 255) << 8) | (initializationBytes[29] & 255);
            String fontFamily = new String(initializationBytes, 43, initializationBytes.length - 43);
            this.defaultFontFamily = TX3G_SERIF.equals(fontFamily) ? C.SERIF_NAME : "sans-serif";
            this.calculatedVideoTrackHeight = initializationBytes[25] * 20;
            this.customVerticalPlacement = (initializationBytes[0] & 32) != 0;
            if (this.customVerticalPlacement) {
                int requestedVerticalPlacement = ((initializationBytes[10] & 255) << 8) | (initializationBytes[11] & 255);
                this.defaultVerticalPlacement = requestedVerticalPlacement / this.calculatedVideoTrackHeight;
                this.defaultVerticalPlacement = Util.constrainValue(this.defaultVerticalPlacement, 0.0f, 0.95f);
                return;
            }
            this.defaultVerticalPlacement = DEFAULT_VERTICAL_PLACEMENT;
            return;
        }
        this.defaultFontFace = 0;
        this.defaultColorRgba = -1;
        this.defaultFontFamily = "sans-serif";
        this.customVerticalPlacement = false;
        this.defaultVerticalPlacement = DEFAULT_VERTICAL_PLACEMENT;
    }

    @Override // com.google.android.exoplayer2.text.SimpleSubtitleDecoder
    protected Subtitle decode(byte[] bytes, int length, boolean reset) throws SubtitleDecoderException {
        this.parsableByteArray.reset(bytes, length);
        String cueTextString = readSubtitleText(this.parsableByteArray);
        if (cueTextString.isEmpty()) {
            return Tx3gSubtitle.EMPTY;
        }
        SpannableStringBuilder cueText = new SpannableStringBuilder(cueTextString);
        attachFontFace(cueText, this.defaultFontFace, 0, 0, cueText.length(), SPAN_PRIORITY_LOW);
        attachColor(cueText, this.defaultColorRgba, -1, 0, cueText.length(), SPAN_PRIORITY_LOW);
        attachFontFamily(cueText, this.defaultFontFamily, "sans-serif", 0, cueText.length(), SPAN_PRIORITY_LOW);
        float verticalPlacement = this.defaultVerticalPlacement;
        while (this.parsableByteArray.bytesLeft() >= 8) {
            int position = this.parsableByteArray.getPosition();
            int atomSize = this.parsableByteArray.readInt();
            int atomType = this.parsableByteArray.readInt();
            if (atomType == TYPE_STYL) {
                assertTrue(this.parsableByteArray.bytesLeft() >= 2);
                int styleRecordCount = this.parsableByteArray.readUnsignedShort();
                for (int i = 0; i < styleRecordCount; i++) {
                    applyStyleRecord(this.parsableByteArray, cueText);
                }
            } else if (atomType == TYPE_TBOX && this.customVerticalPlacement) {
                assertTrue(this.parsableByteArray.bytesLeft() >= 2);
                int requestedVerticalPlacement = this.parsableByteArray.readUnsignedShort();
                float verticalPlacement2 = requestedVerticalPlacement / this.calculatedVideoTrackHeight;
                verticalPlacement = Util.constrainValue(verticalPlacement2, 0.0f, 0.95f);
            }
            this.parsableByteArray.setPosition(position + atomSize);
        }
        return new Tx3gSubtitle(new Cue(cueText, null, verticalPlacement, 0, 0, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE));
    }

    private static String readSubtitleText(ParsableByteArray parsableByteArray) throws SubtitleDecoderException {
        char firstChar;
        assertTrue(parsableByteArray.bytesLeft() >= 2);
        int textLength = parsableByteArray.readUnsignedShort();
        if (textLength == 0) {
            return "";
        }
        if (parsableByteArray.bytesLeft() >= 2 && ((firstChar = parsableByteArray.peekChar()) == 65279 || firstChar == 65534)) {
            return parsableByteArray.readString(textLength, Charset.forName(C.UTF16_NAME));
        }
        return parsableByteArray.readString(textLength, Charset.forName("UTF-8"));
    }

    private void applyStyleRecord(ParsableByteArray parsableByteArray, SpannableStringBuilder cueText) throws SubtitleDecoderException {
        assertTrue(parsableByteArray.bytesLeft() >= 12);
        int start = parsableByteArray.readUnsignedShort();
        int end = parsableByteArray.readUnsignedShort();
        parsableByteArray.skipBytes(2);
        int fontFace = parsableByteArray.readUnsignedByte();
        parsableByteArray.skipBytes(1);
        int colorRgba = parsableByteArray.readInt();
        attachFontFace(cueText, fontFace, this.defaultFontFace, start, end, 0);
        attachColor(cueText, colorRgba, this.defaultColorRgba, start, end, 0);
    }

    private static void attachFontFace(SpannableStringBuilder cueText, int fontFace, int defaultFontFace, int start, int end, int spanPriority) {
        if (fontFace != defaultFontFace) {
            int flags = spanPriority | 33;
            boolean isBold = (fontFace & 1) != 0;
            boolean isItalic = (fontFace & 2) != 0;
            if (isBold) {
                if (isItalic) {
                    cueText.setSpan(new StyleSpan(3), start, end, flags);
                } else {
                    cueText.setSpan(new StyleSpan(1), start, end, flags);
                }
            } else if (isItalic) {
                cueText.setSpan(new StyleSpan(2), start, end, flags);
            }
            boolean isUnderlined = (fontFace & 4) != 0;
            if (isUnderlined) {
                cueText.setSpan(new UnderlineSpan(), start, end, flags);
            }
            if (!isUnderlined && !isBold && !isItalic) {
                cueText.setSpan(new StyleSpan(0), start, end, flags);
            }
        }
    }

    private static void attachColor(SpannableStringBuilder cueText, int colorRgba, int defaultColorRgba, int start, int end, int spanPriority) {
        if (colorRgba != defaultColorRgba) {
            int colorArgb = ((colorRgba & 255) << 24) | (colorRgba >>> 8);
            cueText.setSpan(new ForegroundColorSpan(colorArgb), start, end, spanPriority | 33);
        }
    }

    private static void attachFontFamily(SpannableStringBuilder cueText, String fontFamily, String defaultFontFamily, int start, int end, int spanPriority) {
        if (fontFamily != defaultFontFamily) {
            cueText.setSpan(new TypefaceSpan(fontFamily), start, end, spanPriority | 33);
        }
    }

    private static void assertTrue(boolean checkValue) throws SubtitleDecoderException {
        if (!checkValue) {
            throw new SubtitleDecoderException("Unexpected subtitle format.");
        }
    }
}
