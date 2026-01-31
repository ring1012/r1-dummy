package com.google.android.exoplayer2.text.cea;

import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.MotionEventCompat;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import cn.yunzhisheng.asr.JniUscClient;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.text.SubtitleInputBuffer;
import com.google.android.exoplayer2.text.SubtitleOutputBuffer;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;
import com.kaolafm.sdk.client.BuildConfig;
import com.unisound.b.g;
import com.unisound.common.ad;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public final class Cea608Decoder extends CeaDecoder {
    private static final int CC_FIELD_FLAG = 1;
    private static final byte CC_IMPLICIT_DATA_HEADER = -4;
    private static final int CC_MODE_PAINT_ON = 3;
    private static final int CC_MODE_POP_ON = 2;
    private static final int CC_MODE_ROLL_UP = 1;
    private static final int CC_MODE_UNKNOWN = 0;
    private static final int CC_TYPE_FLAG = 2;
    private static final int CC_VALID_608_ID = 4;
    private static final int CC_VALID_FLAG = 4;
    private static final byte CTRL_BACKSPACE = 33;
    private static final byte CTRL_CARRIAGE_RETURN = 45;
    private static final byte CTRL_DELETE_TO_END_OF_ROW = 36;
    private static final byte CTRL_END_OF_CAPTION = 47;
    private static final byte CTRL_ERASE_DISPLAYED_MEMORY = 44;
    private static final byte CTRL_ERASE_NON_DISPLAYED_MEMORY = 46;
    private static final byte CTRL_RESUME_CAPTION_LOADING = 32;
    private static final byte CTRL_RESUME_DIRECT_CAPTIONING = 41;
    private static final byte CTRL_ROLL_UP_CAPTIONS_2_ROWS = 37;
    private static final byte CTRL_ROLL_UP_CAPTIONS_3_ROWS = 38;
    private static final byte CTRL_ROLL_UP_CAPTIONS_4_ROWS = 39;
    private static final int DEFAULT_CAPTIONS_ROW_COUNT = 4;
    private static final int NTSC_CC_FIELD_1 = 0;
    private static final int NTSC_CC_FIELD_2 = 1;
    private int captionMode;
    private int captionRowCount;
    private List<Cue> cues;
    private List<Cue> lastCues;
    private final int packetLength;
    private byte repeatableControlCc1;
    private byte repeatableControlCc2;
    private boolean repeatableControlSet;
    private final int selectedField;
    private static final int[] ROW_INDICES = {11, 1, 3, 12, 14, 5, 7, 9};
    private static final int[] COLUMN_INDICES = {0, 4, 8, 12, 16, 20, 24, 28};
    private static final int[] COLORS = {-1, -16711936, -16776961, -16711681, SupportMenu.CATEGORY_MASK, InputDeviceCompat.SOURCE_ANY, -65281};
    private static final int[] BASIC_CHARACTER_SET = {32, 33, 34, 35, 36, 37, 38, 39, 40, 41, ad.g, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 233, 93, 237, 243, 250, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, ad.r, ad.s, ad.t, ad.u, 115, 116, 117, 118, 119, g.g, 121, 122, 231, 247, 209, 241, 9632};
    private static final int[] SPECIAL_CHARACTER_SET = {174, 176, PsExtractor.PRIVATE_STREAM_1, 191, 8482, 162, 163, 9834, 224, 32, 232, 226, 234, 238, 244, 251};
    private static final int[] SPECIAL_ES_FR_CHARACTER_SET = {193, 201, ad.e, 218, 220, 252, 8216, 161, 42, 39, 8212, 169, 8480, 8226, 8220, 8221, PsExtractor.AUDIO_STREAM, 194, 199, 200, 202, 203, 235, JniUscClient.aj, JniUscClient.al, 239, ad.f, 217, 249, 219, 171, 187};
    private static final int[] SPECIAL_PT_DE_CHARACTER_SET = {195, 227, 205, 204, 236, ad.d, 242, 213, 245, BuildConfig.VERSION_CODE, 125, 92, 94, 95, 124, 126, 196, 228, 214, 246, 223, 165, 164, 9474, 197, 229, 216, 248, 9484, 9488, 9492, 9496};
    private final ParsableByteArray ccData = new ParsableByteArray();
    private final LinkedList<CueBuilder> cueBuilders = new LinkedList<>();
    private CueBuilder currentCueBuilder = new CueBuilder(0, 4);

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.decoder.Decoder
    public /* bridge */ /* synthetic */ SubtitleInputBuffer dequeueInputBuffer() throws SubtitleDecoderException {
        return super.dequeueInputBuffer();
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.decoder.Decoder
    public /* bridge */ /* synthetic */ SubtitleOutputBuffer dequeueOutputBuffer() throws SubtitleDecoderException {
        return super.dequeueOutputBuffer();
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder
    public /* bridge */ /* synthetic */ void queueInputBuffer(SubtitleInputBuffer subtitleInputBuffer) throws SubtitleDecoderException {
        super.queueInputBuffer(subtitleInputBuffer);
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.text.SubtitleDecoder
    public /* bridge */ /* synthetic */ void setPositionUs(long j) {
        super.setPositionUs(j);
    }

    public Cea608Decoder(String mimeType, int accessibilityChannel) {
        this.packetLength = MimeTypes.APPLICATION_MP4CEA608.equals(mimeType) ? 2 : 3;
        switch (accessibilityChannel) {
            case 3:
            case 4:
                this.selectedField = 2;
                break;
            default:
                this.selectedField = 1;
                break;
        }
        setCaptionMode(0);
        resetCueBuilders();
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.decoder.Decoder
    public String getName() {
        return "Cea608Decoder";
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.decoder.Decoder
    public void flush() {
        super.flush();
        this.cues = null;
        this.lastCues = null;
        setCaptionMode(0);
        resetCueBuilders();
        this.captionRowCount = 4;
        this.repeatableControlSet = false;
        this.repeatableControlCc1 = (byte) 0;
        this.repeatableControlCc2 = (byte) 0;
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder, com.google.android.exoplayer2.decoder.Decoder
    public void release() {
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder
    protected boolean isNewSubtitleDataAvailable() {
        return this.cues != this.lastCues;
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder
    protected Subtitle createSubtitle() {
        this.lastCues = this.cues;
        return new CeaSubtitle(this.cues);
    }

    @Override // com.google.android.exoplayer2.text.cea.CeaDecoder
    protected void decode(SubtitleInputBuffer inputBuffer) {
        this.ccData.reset(inputBuffer.data.array(), inputBuffer.data.limit());
        boolean captionDataProcessed = false;
        boolean isRepeatableControl = false;
        while (this.ccData.bytesLeft() >= this.packetLength) {
            byte ccDataHeader = this.packetLength == 2 ? CC_IMPLICIT_DATA_HEADER : (byte) this.ccData.readUnsignedByte();
            byte ccData1 = (byte) (this.ccData.readUnsignedByte() & 127);
            byte ccData2 = (byte) (this.ccData.readUnsignedByte() & 127);
            if ((ccDataHeader & 6) == 4 && (this.selectedField != 1 || (ccDataHeader & 1) == 0)) {
                if (this.selectedField != 2 || (ccDataHeader & 1) == 1) {
                    if (ccData1 != 0 || ccData2 != 0) {
                        captionDataProcessed = true;
                        if ((ccData1 & 247) == 17 && (ccData2 & 240) == 48) {
                            this.currentCueBuilder.append(getSpecialChar(ccData2));
                        } else if ((ccData1 & 246) == 18 && (ccData2 & 224) == 32) {
                            this.currentCueBuilder.backspace();
                            if ((ccData1 & 1) == 0) {
                                this.currentCueBuilder.append(getExtendedEsFrChar(ccData2));
                            } else {
                                this.currentCueBuilder.append(getExtendedPtDeChar(ccData2));
                            }
                        } else if ((ccData1 & 224) == 0) {
                            isRepeatableControl = handleCtrl(ccData1, ccData2);
                        } else {
                            this.currentCueBuilder.append(getChar(ccData1));
                            if ((ccData2 & 224) != 0) {
                                this.currentCueBuilder.append(getChar(ccData2));
                            }
                        }
                    }
                }
            }
        }
        if (captionDataProcessed) {
            if (!isRepeatableControl) {
                this.repeatableControlSet = false;
            }
            if (this.captionMode == 1 || this.captionMode == 3) {
                this.cues = getDisplayCues();
            }
        }
    }

    private boolean handleCtrl(byte cc1, byte cc2) {
        boolean isRepeatableControl = isRepeatable(cc1);
        if (isRepeatableControl) {
            if (this.repeatableControlSet && this.repeatableControlCc1 == cc1 && this.repeatableControlCc2 == cc2) {
                this.repeatableControlSet = false;
                return true;
            }
            this.repeatableControlSet = true;
            this.repeatableControlCc1 = cc1;
            this.repeatableControlCc2 = cc2;
        }
        if (isMidrowCtrlCode(cc1, cc2)) {
            handleMidrowCtrl(cc2);
            return isRepeatableControl;
        }
        if (isPreambleAddressCode(cc1, cc2)) {
            handlePreambleAddressCode(cc1, cc2);
            return isRepeatableControl;
        }
        if (isTabCtrlCode(cc1, cc2)) {
            this.currentCueBuilder.setTab(cc2 - 32);
            return isRepeatableControl;
        }
        if (isMiscCode(cc1, cc2)) {
            handleMiscCode(cc2);
            return isRepeatableControl;
        }
        return isRepeatableControl;
    }

    private void handleMidrowCtrl(byte cc2) {
        boolean isUnderlined = (cc2 & 1) == 1;
        this.currentCueBuilder.setUnderline(isUnderlined);
        int attribute = (cc2 >> 1) & 15;
        if (attribute == 7) {
            this.currentCueBuilder.setMidrowStyle(new StyleSpan(2), 2);
            this.currentCueBuilder.setMidrowStyle(new ForegroundColorSpan(-1), 1);
        } else {
            this.currentCueBuilder.setMidrowStyle(new ForegroundColorSpan(COLORS[attribute]), 1);
        }
    }

    private void handlePreambleAddressCode(byte cc1, byte cc2) {
        int row = ROW_INDICES[cc1 & 7];
        boolean nextRowDown = (cc2 & CTRL_RESUME_CAPTION_LOADING) != 0;
        if (nextRowDown) {
            row++;
        }
        if (row != this.currentCueBuilder.getRow()) {
            if (this.captionMode != 1 && !this.currentCueBuilder.isEmpty()) {
                this.currentCueBuilder = new CueBuilder(this.captionMode, this.captionRowCount);
                this.cueBuilders.add(this.currentCueBuilder);
            }
            this.currentCueBuilder.setRow(row);
        }
        if ((cc2 & 1) == 1) {
            this.currentCueBuilder.setPreambleStyle(new UnderlineSpan());
        }
        int attribute = (cc2 >> 1) & 15;
        if (attribute <= 7) {
            if (attribute == 7) {
                this.currentCueBuilder.setPreambleStyle(new StyleSpan(2));
                this.currentCueBuilder.setPreambleStyle(new ForegroundColorSpan(-1));
                return;
            } else {
                this.currentCueBuilder.setPreambleStyle(new ForegroundColorSpan(COLORS[attribute]));
                return;
            }
        }
        this.currentCueBuilder.setIndent(COLUMN_INDICES[attribute & 7]);
    }

    private void handleMiscCode(byte cc2) {
        switch (cc2) {
            case 32:
                setCaptionMode(2);
                break;
            case 33:
            case 34:
            case 35:
            case 36:
            case MotionEventCompat.AXIS_GENERIC_9 /* 40 */:
            default:
                if (this.captionMode != 0) {
                    switch (cc2) {
                        case 33:
                            this.currentCueBuilder.backspace();
                            break;
                        case MotionEventCompat.AXIS_GENERIC_13 /* 44 */:
                            this.cues = null;
                            if (this.captionMode == 1 || this.captionMode == 3) {
                                resetCueBuilders();
                                break;
                            }
                            break;
                        case MotionEventCompat.AXIS_GENERIC_14 /* 45 */:
                            if (this.captionMode == 1 && !this.currentCueBuilder.isEmpty()) {
                                this.currentCueBuilder.rollUp();
                                break;
                            }
                            break;
                        case MotionEventCompat.AXIS_GENERIC_15 /* 46 */:
                            resetCueBuilders();
                            break;
                        case MotionEventCompat.AXIS_GENERIC_16 /* 47 */:
                            this.cues = getDisplayCues();
                            resetCueBuilders();
                            break;
                    }
                }
                break;
            case MotionEventCompat.AXIS_GENERIC_6 /* 37 */:
                this.captionRowCount = 2;
                setCaptionMode(1);
                break;
            case MotionEventCompat.AXIS_GENERIC_7 /* 38 */:
                this.captionRowCount = 3;
                setCaptionMode(1);
                break;
            case MotionEventCompat.AXIS_GENERIC_8 /* 39 */:
                this.captionRowCount = 4;
                setCaptionMode(1);
                break;
            case MotionEventCompat.AXIS_GENERIC_10 /* 41 */:
                setCaptionMode(3);
                break;
        }
    }

    private List<Cue> getDisplayCues() {
        List<Cue> displayCues = new ArrayList<>();
        for (int i = 0; i < this.cueBuilders.size(); i++) {
            Cue cue = this.cueBuilders.get(i).build();
            if (cue != null) {
                displayCues.add(cue);
            }
        }
        return displayCues;
    }

    private void setCaptionMode(int captionMode) {
        if (this.captionMode != captionMode) {
            int oldCaptionMode = this.captionMode;
            this.captionMode = captionMode;
            resetCueBuilders();
            if (oldCaptionMode == 3 || captionMode == 1 || captionMode == 0) {
                this.cues = null;
            }
        }
    }

    private void resetCueBuilders() {
        this.currentCueBuilder.reset(this.captionMode, this.captionRowCount);
        this.cueBuilders.clear();
        this.cueBuilders.add(this.currentCueBuilder);
    }

    private static char getChar(byte ccData) {
        int index = (ccData & 127) - 32;
        return (char) BASIC_CHARACTER_SET[index];
    }

    private static char getSpecialChar(byte ccData) {
        int index = ccData & 15;
        return (char) SPECIAL_CHARACTER_SET[index];
    }

    private static char getExtendedEsFrChar(byte ccData) {
        int index = ccData & 31;
        return (char) SPECIAL_ES_FR_CHARACTER_SET[index];
    }

    private static char getExtendedPtDeChar(byte ccData) {
        int index = ccData & 31;
        return (char) SPECIAL_PT_DE_CHARACTER_SET[index];
    }

    private static boolean isMidrowCtrlCode(byte cc1, byte cc2) {
        return (cc1 & 247) == 17 && (cc2 & 240) == 32;
    }

    private static boolean isPreambleAddressCode(byte cc1, byte cc2) {
        return (cc1 & 240) == 16 && (cc2 & 192) == 64;
    }

    private static boolean isTabCtrlCode(byte cc1, byte cc2) {
        return (cc1 & 247) == 23 && cc2 >= 33 && cc2 <= 35;
    }

    private static boolean isMiscCode(byte cc1, byte cc2) {
        return (cc1 & 247) == 20 && (cc2 & 240) == 32;
    }

    private static boolean isRepeatable(byte cc1) {
        return (cc1 & 240) == 16;
    }

    private static class CueBuilder {
        private static final int BASE_ROW = 15;
        private static final int POSITION_UNSET = -1;
        private static final int SCREEN_CHARWIDTH = 32;
        private int captionMode;
        private int captionRowCount;
        private int indent;
        private int row;
        private int tabOffset;
        private int underlineStartPosition;
        private final List<CharacterStyle> preambleStyles = new ArrayList();
        private final List<CueStyle> midrowStyles = new ArrayList();
        private final List<SpannableString> rolledUpCaptions = new LinkedList();
        private final SpannableStringBuilder captionStringBuilder = new SpannableStringBuilder();

        public CueBuilder(int captionMode, int captionRowCount) {
            reset(captionMode, captionRowCount);
        }

        public void reset(int captionMode, int captionRowCount) {
            this.preambleStyles.clear();
            this.midrowStyles.clear();
            this.rolledUpCaptions.clear();
            this.captionStringBuilder.clear();
            this.row = 15;
            this.indent = 0;
            this.tabOffset = 0;
            this.captionMode = captionMode;
            this.captionRowCount = captionRowCount;
            this.underlineStartPosition = -1;
        }

        public boolean isEmpty() {
            return this.preambleStyles.isEmpty() && this.midrowStyles.isEmpty() && this.rolledUpCaptions.isEmpty() && this.captionStringBuilder.length() == 0;
        }

        public void backspace() {
            int length = this.captionStringBuilder.length();
            if (length > 0) {
                this.captionStringBuilder.delete(length - 1, length);
            }
        }

        public int getRow() {
            return this.row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public void rollUp() {
            this.rolledUpCaptions.add(buildSpannableString());
            this.captionStringBuilder.clear();
            this.preambleStyles.clear();
            this.midrowStyles.clear();
            this.underlineStartPosition = -1;
            int numRows = Math.min(this.captionRowCount, this.row);
            while (this.rolledUpCaptions.size() >= numRows) {
                this.rolledUpCaptions.remove(0);
            }
        }

        public void setIndent(int indent) {
            this.indent = indent;
        }

        public void setTab(int tabs) {
            this.tabOffset = tabs;
        }

        public void setPreambleStyle(CharacterStyle style) {
            this.preambleStyles.add(style);
        }

        public void setMidrowStyle(CharacterStyle style, int nextStyleIncrement) {
            this.midrowStyles.add(new CueStyle(style, this.captionStringBuilder.length(), nextStyleIncrement));
        }

        public void setUnderline(boolean enabled) {
            if (enabled) {
                this.underlineStartPosition = this.captionStringBuilder.length();
            } else if (this.underlineStartPosition != -1) {
                this.captionStringBuilder.setSpan(new UnderlineSpan(), this.underlineStartPosition, this.captionStringBuilder.length(), 33);
                this.underlineStartPosition = -1;
            }
        }

        public void append(char text) {
            this.captionStringBuilder.append(text);
        }

        public SpannableString buildSpannableString() {
            int length = this.captionStringBuilder.length();
            for (int i = 0; i < this.preambleStyles.size(); i++) {
                this.captionStringBuilder.setSpan(this.preambleStyles.get(i), 0, length, 33);
            }
            int i2 = 0;
            while (i2 < this.midrowStyles.size()) {
                CueStyle cueStyle = this.midrowStyles.get(i2);
                int end = i2 < this.midrowStyles.size() - cueStyle.nextStyleIncrement ? this.midrowStyles.get(cueStyle.nextStyleIncrement + i2).start : length;
                this.captionStringBuilder.setSpan(cueStyle.style, cueStyle.start, end, 33);
                i2++;
            }
            if (this.underlineStartPosition != -1) {
                this.captionStringBuilder.setSpan(new UnderlineSpan(), this.underlineStartPosition, length, 33);
            }
            return new SpannableString(this.captionStringBuilder);
        }

        public Cue build() {
            float position;
            int positionAnchor;
            int lineAnchor;
            int line;
            SpannableStringBuilder cueString = new SpannableStringBuilder();
            for (int i = 0; i < this.rolledUpCaptions.size(); i++) {
                cueString.append((CharSequence) this.rolledUpCaptions.get(i));
                cueString.append('\n');
            }
            cueString.append((CharSequence) buildSpannableString());
            if (cueString.length() == 0) {
                return null;
            }
            int startPadding = this.indent + this.tabOffset;
            int endPadding = (32 - startPadding) - cueString.length();
            int startEndPaddingDelta = startPadding - endPadding;
            if (this.captionMode == 2 && Math.abs(startEndPaddingDelta) < 3) {
                position = 0.5f;
                positionAnchor = 1;
            } else if (this.captionMode == 2 && startEndPaddingDelta > 0) {
                float position2 = (32 - endPadding) / 32.0f;
                position = (0.8f * position2) + 0.1f;
                positionAnchor = 2;
            } else {
                float position3 = startPadding / 32.0f;
                position = (0.8f * position3) + 0.1f;
                positionAnchor = 0;
            }
            if (this.captionMode == 1 || this.row > 7) {
                lineAnchor = 2;
                int line2 = this.row - 15;
                line = line2 - 2;
            } else {
                lineAnchor = 0;
                line = this.row;
            }
            return new Cue(cueString, Layout.Alignment.ALIGN_NORMAL, line, 1, lineAnchor, position, positionAnchor, Float.MIN_VALUE);
        }

        public String toString() {
            return this.captionStringBuilder.toString();
        }

        private static class CueStyle {
            public final int nextStyleIncrement;
            public final int start;
            public final CharacterStyle style;

            public CueStyle(CharacterStyle style, int start, int nextStyleIncrement) {
                this.style = style;
                this.start = start;
                this.nextStyleIncrement = nextStyleIncrement;
            }
        }
    }
}
