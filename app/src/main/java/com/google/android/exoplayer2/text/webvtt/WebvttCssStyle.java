package com.google.android.exoplayer2.text.webvtt;

import android.text.Layout;
import com.google.android.exoplayer2.util.Util;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
final class WebvttCssStyle {
    public static final int FONT_SIZE_UNIT_EM = 2;
    public static final int FONT_SIZE_UNIT_PERCENT = 3;
    public static final int FONT_SIZE_UNIT_PIXEL = 1;
    private static final int OFF = 0;
    private static final int ON = 1;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_BOLD_ITALIC = 3;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_NORMAL = 0;
    public static final int UNSPECIFIED = -1;
    private int backgroundColor;
    private int bold;
    private int fontColor;
    private String fontFamily;
    private float fontSize;
    private int fontSizeUnit;
    private boolean hasBackgroundColor;
    private boolean hasFontColor;
    private int italic;
    private int linethrough;
    private List<String> targetClasses;
    private String targetId;
    private String targetTag;
    private String targetVoice;
    private Layout.Alignment textAlign;
    private int underline;

    @Retention(RetentionPolicy.SOURCE)
    public @interface FontSizeUnit {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface StyleFlags {
    }

    public WebvttCssStyle() {
        reset();
    }

    public void reset() {
        this.targetId = "";
        this.targetTag = "";
        this.targetClasses = Collections.emptyList();
        this.targetVoice = "";
        this.fontFamily = null;
        this.hasFontColor = false;
        this.hasBackgroundColor = false;
        this.linethrough = -1;
        this.underline = -1;
        this.bold = -1;
        this.italic = -1;
        this.fontSizeUnit = -1;
        this.textAlign = null;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public void setTargetTagName(String targetTag) {
        this.targetTag = targetTag;
    }

    public void setTargetClasses(String[] targetClasses) {
        this.targetClasses = Arrays.asList(targetClasses);
    }

    public void setTargetVoice(String targetVoice) {
        this.targetVoice = targetVoice;
    }

    public int getSpecificityScore(String id, String tag, String[] classes, String voice) {
        if (this.targetId.isEmpty() && this.targetTag.isEmpty() && this.targetClasses.isEmpty() && this.targetVoice.isEmpty()) {
            return tag.isEmpty() ? 1 : 0;
        }
        int score = updateScoreForMatch(updateScoreForMatch(updateScoreForMatch(0, this.targetId, id, 1073741824), this.targetTag, tag, 2), this.targetVoice, voice, 4);
        if (score == -1 || !Arrays.asList(classes).containsAll(this.targetClasses)) {
            return 0;
        }
        return score + (this.targetClasses.size() * 4);
    }

    public int getStyle() {
        if (this.bold == -1 && this.italic == -1) {
            return -1;
        }
        return (this.bold == 1 ? 1 : 0) | (this.italic == 1 ? 2 : 0);
    }

    public boolean isLinethrough() {
        return this.linethrough == 1;
    }

    public WebvttCssStyle setLinethrough(boolean linethrough) {
        this.linethrough = linethrough ? 1 : 0;
        return this;
    }

    public boolean isUnderline() {
        return this.underline == 1;
    }

    public WebvttCssStyle setUnderline(boolean underline) {
        this.underline = underline ? 1 : 0;
        return this;
    }

    public WebvttCssStyle setBold(boolean bold) {
        this.bold = bold ? 1 : 0;
        return this;
    }

    public WebvttCssStyle setItalic(boolean italic) {
        this.italic = italic ? 1 : 0;
        return this;
    }

    public String getFontFamily() {
        return this.fontFamily;
    }

    public WebvttCssStyle setFontFamily(String fontFamily) {
        this.fontFamily = Util.toLowerInvariant(fontFamily);
        return this;
    }

    public int getFontColor() {
        if (!this.hasFontColor) {
            throw new IllegalStateException("Font color not defined");
        }
        return this.fontColor;
    }

    public WebvttCssStyle setFontColor(int color) {
        this.fontColor = color;
        this.hasFontColor = true;
        return this;
    }

    public boolean hasFontColor() {
        return this.hasFontColor;
    }

    public int getBackgroundColor() {
        if (!this.hasBackgroundColor) {
            throw new IllegalStateException("Background color not defined.");
        }
        return this.backgroundColor;
    }

    public WebvttCssStyle setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.hasBackgroundColor = true;
        return this;
    }

    public boolean hasBackgroundColor() {
        return this.hasBackgroundColor;
    }

    public Layout.Alignment getTextAlign() {
        return this.textAlign;
    }

    public WebvttCssStyle setTextAlign(Layout.Alignment textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    public WebvttCssStyle setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public WebvttCssStyle setFontSizeUnit(short unit) {
        this.fontSizeUnit = unit;
        return this;
    }

    public int getFontSizeUnit() {
        return this.fontSizeUnit;
    }

    public float getFontSize() {
        return this.fontSize;
    }

    public void cascadeFrom(WebvttCssStyle style) {
        if (style.hasFontColor) {
            setFontColor(style.fontColor);
        }
        if (style.bold != -1) {
            this.bold = style.bold;
        }
        if (style.italic != -1) {
            this.italic = style.italic;
        }
        if (style.fontFamily != null) {
            this.fontFamily = style.fontFamily;
        }
        if (this.linethrough == -1) {
            this.linethrough = style.linethrough;
        }
        if (this.underline == -1) {
            this.underline = style.underline;
        }
        if (this.textAlign == null) {
            this.textAlign = style.textAlign;
        }
        if (this.fontSizeUnit == -1) {
            this.fontSizeUnit = style.fontSizeUnit;
            this.fontSize = style.fontSize;
        }
        if (style.hasBackgroundColor) {
            setBackgroundColor(style.backgroundColor);
        }
    }

    private static int updateScoreForMatch(int currentScore, String target, String actual, int score) {
        if (target.isEmpty() || currentScore == -1) {
            return currentScore;
        }
        if (target.equals(actual)) {
            return currentScore + score;
        }
        return -1;
    }
}
