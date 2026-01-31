package com.google.android.exoplayer2.text.webvtt;

import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import cn.yunzhisheng.common.PinyinConverter;
import com.google.android.exoplayer2.text.webvtt.WebvttCue;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
final class WebvttCueParser {
    private static final char CHAR_AMPERSAND = '&';
    private static final char CHAR_GREATER_THAN = '>';
    private static final char CHAR_LESS_THAN = '<';
    private static final char CHAR_SEMI_COLON = ';';
    private static final char CHAR_SLASH = '/';
    private static final char CHAR_SPACE = ' ';
    public static final Pattern CUE_HEADER_PATTERN = Pattern.compile("^(\\S+)\\s+-->\\s+(\\S+)(.*)?$");
    private static final Pattern CUE_SETTING_PATTERN = Pattern.compile("(\\S+?):(\\S+)");
    private static final String ENTITY_AMPERSAND = "amp";
    private static final String ENTITY_GREATER_THAN = "gt";
    private static final String ENTITY_LESS_THAN = "lt";
    private static final String ENTITY_NON_BREAK_SPACE = "nbsp";
    private static final int STYLE_BOLD = 1;
    private static final int STYLE_ITALIC = 2;
    private static final String TAG = "WebvttCueParser";
    private static final String TAG_BOLD = "b";
    private static final String TAG_CLASS = "c";
    private static final String TAG_ITALIC = "i";
    private static final String TAG_LANG = "lang";
    private static final String TAG_UNDERLINE = "u";
    private static final String TAG_VOICE = "v";
    private final StringBuilder textBuilder = new StringBuilder();

    boolean parseCue(ParsableByteArray webvttData, WebvttCue.Builder builder, List<WebvttCssStyle> styles) {
        String firstLine = webvttData.readLine();
        Matcher cueHeaderMatcher = CUE_HEADER_PATTERN.matcher(firstLine);
        if (cueHeaderMatcher.matches()) {
            return parseCue(null, cueHeaderMatcher, webvttData, builder, this.textBuilder, styles);
        }
        String secondLine = webvttData.readLine();
        Matcher cueHeaderMatcher2 = CUE_HEADER_PATTERN.matcher(secondLine);
        if (cueHeaderMatcher2.matches()) {
            return parseCue(firstLine.trim(), cueHeaderMatcher2, webvttData, builder, this.textBuilder, styles);
        }
        return false;
    }

    static void parseCueSettingsList(String cueSettingsList, WebvttCue.Builder builder) {
        Matcher cueSettingMatcher = CUE_SETTING_PATTERN.matcher(cueSettingsList);
        while (cueSettingMatcher.find()) {
            String name = cueSettingMatcher.group(1);
            String value = cueSettingMatcher.group(2);
            try {
                if ("line".equals(name)) {
                    parseLineAttribute(value, builder);
                } else if ("align".equals(name)) {
                    builder.setTextAlignment(parseTextAlignment(value));
                } else if ("position".equals(name)) {
                    parsePositionAttribute(value, builder);
                } else if ("size".equals(name)) {
                    builder.setWidth(WebvttParserUtil.parsePercentage(value));
                } else {
                    Log.w(TAG, "Unknown cue setting " + name + ":" + value);
                }
            } catch (NumberFormatException e) {
                Log.w(TAG, "Skipping bad cue setting: " + cueSettingMatcher.group());
            }
        }
    }

    static void parseCueText(String id, String markup, WebvttCue.Builder builder, List<WebvttCssStyle> styles) {
        int entityEndIndex;
        SpannableStringBuilder spannedText = new SpannableStringBuilder();
        Stack<StartTag> startTagStack = new Stack<>();
        List<StyleMatch> scratchStyleMatches = new ArrayList<>();
        int pos = 0;
        while (pos < markup.length()) {
            char curr = markup.charAt(pos);
            switch (curr) {
                case MotionEventCompat.AXIS_GENERIC_7 /* 38 */:
                    int semiColonEndIndex = markup.indexOf(59, pos + 1);
                    int spaceEndIndex = markup.indexOf(32, pos + 1);
                    if (semiColonEndIndex == -1) {
                        entityEndIndex = spaceEndIndex;
                    } else {
                        entityEndIndex = spaceEndIndex == -1 ? semiColonEndIndex : Math.min(semiColonEndIndex, spaceEndIndex);
                    }
                    if (entityEndIndex != -1) {
                        applyEntity(markup.substring(pos + 1, entityEndIndex), spannedText);
                        if (entityEndIndex == spaceEndIndex) {
                            spannedText.append(PinyinConverter.PINYIN_SEPARATOR);
                        }
                        pos = entityEndIndex + 1;
                        break;
                    } else {
                        spannedText.append(curr);
                        pos++;
                        break;
                    }
                case '<':
                    if (pos + 1 >= markup.length()) {
                        pos++;
                        break;
                    } else {
                        int ltPos = pos;
                        boolean isClosingTag = markup.charAt(ltPos + 1) == '/';
                        pos = findEndOfTag(markup, ltPos + 1);
                        boolean isVoidTag = markup.charAt(pos + (-2)) == '/';
                        String fullTagExpression = markup.substring(ltPos + (isClosingTag ? 2 : 1), isVoidTag ? pos - 2 : pos - 1);
                        String tagName = getTagName(fullTagExpression);
                        if (tagName != null && isSupportedTag(tagName)) {
                            if (isClosingTag) {
                                while (!startTagStack.isEmpty()) {
                                    StartTag startTag = startTagStack.pop();
                                    applySpansForTag(id, startTag, spannedText, styles, scratchStyleMatches);
                                    if (startTag.name.equals(tagName)) {
                                        break;
                                    }
                                }
                                break;
                            } else if (!isVoidTag) {
                                startTagStack.push(StartTag.buildStartTag(fullTagExpression, spannedText.length()));
                                break;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                default:
                    spannedText.append(curr);
                    pos++;
                    break;
            }
        }
        while (!startTagStack.isEmpty()) {
            applySpansForTag(id, startTagStack.pop(), spannedText, styles, scratchStyleMatches);
        }
        applySpansForTag(id, StartTag.buildWholeCueVirtualTag(), spannedText, styles, scratchStyleMatches);
        builder.setText(spannedText);
    }

    private static boolean parseCue(String id, Matcher cueHeaderMatcher, ParsableByteArray webvttData, WebvttCue.Builder builder, StringBuilder textBuilder, List<WebvttCssStyle> styles) {
        try {
            builder.setStartTime(WebvttParserUtil.parseTimestampUs(cueHeaderMatcher.group(1))).setEndTime(WebvttParserUtil.parseTimestampUs(cueHeaderMatcher.group(2)));
            parseCueSettingsList(cueHeaderMatcher.group(3), builder);
            textBuilder.setLength(0);
            while (true) {
                String line = webvttData.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                if (textBuilder.length() > 0) {
                    textBuilder.append("\n");
                }
                textBuilder.append(line.trim());
            }
            parseCueText(id, textBuilder.toString(), builder, styles);
            return true;
        } catch (NumberFormatException e) {
            Log.w(TAG, "Skipping cue with bad header: " + cueHeaderMatcher.group());
            return false;
        }
    }

    private static void parseLineAttribute(String s, WebvttCue.Builder builder) throws NumberFormatException {
        int commaIndex = s.indexOf(44);
        if (commaIndex != -1) {
            builder.setLineAnchor(parsePositionAnchor(s.substring(commaIndex + 1)));
            s = s.substring(0, commaIndex);
        } else {
            builder.setLineAnchor(Integer.MIN_VALUE);
        }
        if (s.endsWith("%")) {
            builder.setLine(WebvttParserUtil.parsePercentage(s)).setLineType(0);
            return;
        }
        int lineNumber = Integer.parseInt(s);
        if (lineNumber < 0) {
            lineNumber--;
        }
        builder.setLine(lineNumber).setLineType(1);
    }

    private static void parsePositionAttribute(String s, WebvttCue.Builder builder) throws NumberFormatException {
        int commaIndex = s.indexOf(44);
        if (commaIndex != -1) {
            builder.setPositionAnchor(parsePositionAnchor(s.substring(commaIndex + 1)));
            s = s.substring(0, commaIndex);
        } else {
            builder.setPositionAnchor(Integer.MIN_VALUE);
        }
        builder.setPosition(WebvttParserUtil.parsePercentage(s));
    }

    private static int parsePositionAnchor(String s) {
        switch (s) {
            case "start":
                return 0;
            case "center":
            case "middle":
                return 1;
            case "end":
                return 2;
            default:
                Log.w(TAG, "Invalid anchor value: " + s);
                return Integer.MIN_VALUE;
        }
    }

    private static Layout.Alignment parseTextAlignment(String s) {
        switch (s) {
            case "start":
            case "left":
                return Layout.Alignment.ALIGN_NORMAL;
            case "center":
            case "middle":
                return Layout.Alignment.ALIGN_CENTER;
            case "end":
            case "right":
                return Layout.Alignment.ALIGN_OPPOSITE;
            default:
                Log.w(TAG, "Invalid alignment value: " + s);
                return null;
        }
    }

    private static int findEndOfTag(String markup, int startPos) {
        int index = markup.indexOf(62, startPos);
        return index == -1 ? markup.length() : index + 1;
    }

    private static void applyEntity(String entity, SpannableStringBuilder spannedText) {
        switch (entity) {
            case "lt":
                spannedText.append(CHAR_LESS_THAN);
                break;
            case "gt":
                spannedText.append(CHAR_GREATER_THAN);
                break;
            case "nbsp":
                spannedText.append(CHAR_SPACE);
                break;
            case "amp":
                spannedText.append(CHAR_AMPERSAND);
                break;
            default:
                Log.w(TAG, "ignoring unsupported entity: '&" + entity + ";'");
                break;
        }
    }

    private static boolean isSupportedTag(String tagName) {
        switch (tagName) {
            case "b":
            case "c":
            case "i":
            case "lang":
            case "u":
            case "v":
                return true;
            default:
                return false;
        }
    }

    private static void applySpansForTag(String cueId, StartTag startTag, SpannableStringBuilder text, List<WebvttCssStyle> styles, List<StyleMatch> scratchStyleMatches) {
        int start;
        int end;
        start = startTag.position;
        end = text.length();
        switch (startTag.name) {
            case "b":
                text.setSpan(new StyleSpan(1), start, end, 33);
                break;
            case "i":
                text.setSpan(new StyleSpan(2), start, end, 33);
                break;
            case "u":
                text.setSpan(new UnderlineSpan(), start, end, 33);
                break;
            case "c":
            case "lang":
            case "v":
            case "":
                break;
            default:
                return;
        }
        scratchStyleMatches.clear();
        getApplicableStyles(styles, cueId, startTag, scratchStyleMatches);
        int styleMatchesCount = scratchStyleMatches.size();
        for (int i = 0; i < styleMatchesCount; i++) {
            applyStyleToText(text, scratchStyleMatches.get(i).style, start, end);
        }
    }

    private static void applyStyleToText(SpannableStringBuilder spannedText, WebvttCssStyle style, int start, int end) {
        if (style != null) {
            if (style.getStyle() != -1) {
                spannedText.setSpan(new StyleSpan(style.getStyle()), start, end, 33);
            }
            if (style.isLinethrough()) {
                spannedText.setSpan(new StrikethroughSpan(), start, end, 33);
            }
            if (style.isUnderline()) {
                spannedText.setSpan(new UnderlineSpan(), start, end, 33);
            }
            if (style.hasFontColor()) {
                spannedText.setSpan(new ForegroundColorSpan(style.getFontColor()), start, end, 33);
            }
            if (style.hasBackgroundColor()) {
                spannedText.setSpan(new BackgroundColorSpan(style.getBackgroundColor()), start, end, 33);
            }
            if (style.getFontFamily() != null) {
                spannedText.setSpan(new TypefaceSpan(style.getFontFamily()), start, end, 33);
            }
            if (style.getTextAlign() != null) {
                spannedText.setSpan(new AlignmentSpan.Standard(style.getTextAlign()), start, end, 33);
            }
            switch (style.getFontSizeUnit()) {
                case 1:
                    spannedText.setSpan(new AbsoluteSizeSpan((int) style.getFontSize(), true), start, end, 33);
                    break;
                case 2:
                    spannedText.setSpan(new RelativeSizeSpan(style.getFontSize()), start, end, 33);
                    break;
                case 3:
                    spannedText.setSpan(new RelativeSizeSpan(style.getFontSize() / 100.0f), start, end, 33);
                    break;
            }
        }
    }

    private static String getTagName(String tagExpression) {
        String tagExpression2 = tagExpression.trim();
        if (tagExpression2.isEmpty()) {
            return null;
        }
        return tagExpression2.split("[ \\.]")[0];
    }

    private static void getApplicableStyles(List<WebvttCssStyle> declaredStyles, String id, StartTag tag, List<StyleMatch> output) {
        int styleCount = declaredStyles.size();
        for (int i = 0; i < styleCount; i++) {
            WebvttCssStyle style = declaredStyles.get(i);
            int score = style.getSpecificityScore(id, tag.name, tag.classes, tag.voice);
            if (score > 0) {
                output.add(new StyleMatch(score, style));
            }
        }
        Collections.sort(output);
    }

    private static final class StyleMatch implements Comparable<StyleMatch> {
        public final int score;
        public final WebvttCssStyle style;

        public StyleMatch(int score, WebvttCssStyle style) {
            this.score = score;
            this.style = style;
        }

        @Override // java.lang.Comparable
        public int compareTo(@NonNull StyleMatch another) {
            return this.score - another.score;
        }
    }

    private static final class StartTag {
        private static final String[] NO_CLASSES = new String[0];
        public final String[] classes;
        public final String name;
        public final int position;
        public final String voice;

        private StartTag(String name, int position, String voice, String[] classes) {
            this.position = position;
            this.name = name;
            this.voice = voice;
            this.classes = classes;
        }

        public static StartTag buildStartTag(String fullTagExpression, int position) {
            String voice;
            String[] classes;
            String fullTagExpression2 = fullTagExpression.trim();
            if (fullTagExpression2.isEmpty()) {
                return null;
            }
            int voiceStartIndex = fullTagExpression2.indexOf(PinyinConverter.PINYIN_SEPARATOR);
            if (voiceStartIndex == -1) {
                voice = "";
            } else {
                voice = fullTagExpression2.substring(voiceStartIndex).trim();
                fullTagExpression2 = fullTagExpression2.substring(0, voiceStartIndex);
            }
            String[] nameAndClasses = fullTagExpression2.split("\\.");
            String name = nameAndClasses[0];
            if (nameAndClasses.length > 1) {
                classes = (String[]) Arrays.copyOfRange(nameAndClasses, 1, nameAndClasses.length);
            } else {
                classes = NO_CLASSES;
            }
            return new StartTag(name, position, voice, classes);
        }

        public static StartTag buildWholeCueVirtualTag() {
            return new StartTag("", 0, "", new String[0]);
        }
    }
}
