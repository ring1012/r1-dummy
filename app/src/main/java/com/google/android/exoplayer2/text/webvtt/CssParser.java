package com.google.android.exoplayer2.text.webvtt;

import android.text.TextUtils;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.google.android.exoplayer2.util.ColorParser;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
final class CssParser {
    private static final String BLOCK_END = "}";
    private static final String BLOCK_START = "{";
    private static final String PROPERTY_BGCOLOR = "background-color";
    private static final String PROPERTY_FONT_FAMILY = "font-family";
    private static final String PROPERTY_FONT_STYLE = "font-style";
    private static final String PROPERTY_FONT_WEIGHT = "font-weight";
    private static final String PROPERTY_TEXT_DECORATION = "text-decoration";
    private static final String VALUE_BOLD = "bold";
    private static final String VALUE_ITALIC = "italic";
    private static final String VALUE_UNDERLINE = "underline";
    private static final Pattern VOICE_NAME_PATTERN = Pattern.compile("\\[voice=\"([^\"]*)\"\\]");
    private final ParsableByteArray styleInput = new ParsableByteArray();
    private final StringBuilder stringBuilder = new StringBuilder();

    public WebvttCssStyle parseBlock(ParsableByteArray input) {
        this.stringBuilder.setLength(0);
        int initialInputPosition = input.getPosition();
        skipStyleBlock(input);
        this.styleInput.reset(input.data, input.getPosition());
        this.styleInput.setPosition(initialInputPosition);
        String selector = parseSelector(this.styleInput, this.stringBuilder);
        if (selector == null || !BLOCK_START.equals(parseNextToken(this.styleInput, this.stringBuilder))) {
            return null;
        }
        WebvttCssStyle style = new WebvttCssStyle();
        applySelectorToStyle(style, selector);
        String token = null;
        boolean blockEndFound = false;
        while (!blockEndFound) {
            int position = this.styleInput.getPosition();
            token = parseNextToken(this.styleInput, this.stringBuilder);
            blockEndFound = token == null || BLOCK_END.equals(token);
            if (!blockEndFound) {
                this.styleInput.setPosition(position);
                parseStyleDeclaration(this.styleInput, style, this.stringBuilder);
            }
        }
        if (BLOCK_END.equals(token)) {
            return style;
        }
        return null;
    }

    private static String parseSelector(ParsableByteArray input, StringBuilder stringBuilder) {
        skipWhitespaceAndComments(input);
        if (input.bytesLeft() < 5) {
            return null;
        }
        String cueSelector = input.readString(5);
        if (!"::cue".equals(cueSelector)) {
            return null;
        }
        int position = input.getPosition();
        String token = parseNextToken(input, stringBuilder);
        if (token == null) {
            return null;
        }
        if (BLOCK_START.equals(token)) {
            input.setPosition(position);
            return "";
        }
        String target = null;
        if ("(".equals(token)) {
            target = readCueTarget(input);
        }
        String token2 = parseNextToken(input, stringBuilder);
        if (!")".equals(token2) || token2 == null) {
            return null;
        }
        return target;
    }

    private static String readCueTarget(ParsableByteArray input) {
        int position = input.getPosition();
        int limit = input.limit();
        boolean cueTargetEndFound = false;
        int position2 = position;
        while (position2 < limit && !cueTargetEndFound) {
            int position3 = position2 + 1;
            char c = (char) input.data[position2];
            cueTargetEndFound = c == ')';
            position2 = position3;
        }
        return input.readString((position2 - 1) - input.getPosition()).trim();
    }

    private static void parseStyleDeclaration(ParsableByteArray input, WebvttCssStyle style, StringBuilder stringBuilder) {
        skipWhitespaceAndComments(input);
        String property = parseIdentifier(input, stringBuilder);
        if (!"".equals(property) && ":".equals(parseNextToken(input, stringBuilder))) {
            skipWhitespaceAndComments(input);
            String value = parsePropertyValue(input, stringBuilder);
            if (value != null && !"".equals(value)) {
                int position = input.getPosition();
                String token = parseNextToken(input, stringBuilder);
                if (!";".equals(token)) {
                    if (BLOCK_END.equals(token)) {
                        input.setPosition(position);
                    } else {
                        return;
                    }
                }
                if (TtmlNode.ATTR_TTS_COLOR.equals(property)) {
                    style.setFontColor(ColorParser.parseCssColor(value));
                    return;
                }
                if (PROPERTY_BGCOLOR.equals(property)) {
                    style.setBackgroundColor(ColorParser.parseCssColor(value));
                    return;
                }
                if (PROPERTY_TEXT_DECORATION.equals(property)) {
                    if ("underline".equals(value)) {
                        style.setUnderline(true);
                    }
                } else {
                    if (PROPERTY_FONT_FAMILY.equals(property)) {
                        style.setFontFamily(value);
                        return;
                    }
                    if (PROPERTY_FONT_WEIGHT.equals(property)) {
                        if ("bold".equals(value)) {
                            style.setBold(true);
                        }
                    } else if (PROPERTY_FONT_STYLE.equals(property) && "italic".equals(value)) {
                        style.setItalic(true);
                    }
                }
            }
        }
    }

    static void skipWhitespaceAndComments(ParsableByteArray input) {
        boolean skipping = true;
        while (input.bytesLeft() > 0 && skipping) {
            skipping = maybeSkipWhitespace(input) || maybeSkipComment(input);
        }
    }

    static String parseNextToken(ParsableByteArray input, StringBuilder stringBuilder) {
        skipWhitespaceAndComments(input);
        if (input.bytesLeft() == 0) {
            return null;
        }
        String identifier = parseIdentifier(input, stringBuilder);
        return "".equals(identifier) ? "" + ((char) input.readUnsignedByte()) : identifier;
    }

    private static boolean maybeSkipWhitespace(ParsableByteArray input) {
        switch (peekCharAtPosition(input, input.getPosition())) {
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
                input.skipBytes(1);
                return true;
            default:
                return false;
        }
    }

    static void skipStyleBlock(ParsableByteArray input) {
        String line;
        do {
            line = input.readLine();
        } while (!TextUtils.isEmpty(line));
    }

    private static char peekCharAtPosition(ParsableByteArray input, int position) {
        return (char) input.data[position];
    }

    private static String parsePropertyValue(ParsableByteArray input, StringBuilder stringBuilder) {
        StringBuilder expressionBuilder = new StringBuilder();
        boolean expressionEndFound = false;
        while (!expressionEndFound) {
            int position = input.getPosition();
            String token = parseNextToken(input, stringBuilder);
            if (token == null) {
                return null;
            }
            if (BLOCK_END.equals(token) || ";".equals(token)) {
                input.setPosition(position);
                expressionEndFound = true;
            } else {
                expressionBuilder.append(token);
            }
        }
        return expressionBuilder.toString();
    }

    private static boolean maybeSkipComment(ParsableByteArray input) {
        int position = input.getPosition();
        int limit = input.limit();
        byte[] data = input.data;
        if (position + 2 <= limit) {
            int position2 = position + 1;
            if (data[position] == 47) {
                int position3 = position2 + 1;
                if (data[position2] == 42) {
                    while (true) {
                        int position4 = position3;
                        if (position4 + 1 < limit) {
                            position3 = position4 + 1;
                            char skippedChar = (char) data[position4];
                            if (skippedChar == '*' && ((char) data[position3]) == '/') {
                                position3++;
                                limit = position3;
                            }
                        } else {
                            input.skipBytes(limit - input.getPosition());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static String parseIdentifier(ParsableByteArray input, StringBuilder stringBuilder) {
        stringBuilder.setLength(0);
        int position = input.getPosition();
        int limit = input.limit();
        boolean identifierEndFound = false;
        while (position < limit && !identifierEndFound) {
            char c = (char) input.data[position];
            if ((c >= 'A' && c <= 'Z') || ((c >= 'a' && c <= 'z') || ((c >= '0' && c <= '9') || c == '#' || c == '-' || c == '.' || c == '_'))) {
                position++;
                stringBuilder.append(c);
            } else {
                identifierEndFound = true;
            }
        }
        input.skipBytes(position - input.getPosition());
        return stringBuilder.toString();
    }

    private void applySelectorToStyle(WebvttCssStyle style, String selector) {
        if (!"".equals(selector)) {
            int voiceStartIndex = selector.indexOf(91);
            if (voiceStartIndex != -1) {
                Matcher matcher = VOICE_NAME_PATTERN.matcher(selector.substring(voiceStartIndex));
                if (matcher.matches()) {
                    style.setTargetVoice(matcher.group(1));
                }
                selector = selector.substring(0, voiceStartIndex);
            }
            String[] classDivision = selector.split("\\.");
            String tagAndIdDivision = classDivision[0];
            int idPrefixIndex = tagAndIdDivision.indexOf(35);
            if (idPrefixIndex != -1) {
                style.setTargetTagName(tagAndIdDivision.substring(0, idPrefixIndex));
                style.setTargetId(tagAndIdDivision.substring(idPrefixIndex + 1));
            } else {
                style.setTargetTagName(tagAndIdDivision);
            }
            if (classDivision.length > 1) {
                style.setTargetClasses((String[]) Arrays.copyOfRange(classDivision, 1, classDivision.length));
            }
        }
    }
}
