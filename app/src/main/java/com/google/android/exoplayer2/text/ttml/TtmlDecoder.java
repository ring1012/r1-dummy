package com.google.android.exoplayer2.text.ttml;

import android.util.Log;
import android.util.Pair;
import cn.yunzhisheng.common.PinyinConverter;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;
import com.google.android.exoplayer2.text.SubtitleDecoderException;
import com.google.android.exoplayer2.util.XmlPullParserUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public final class TtmlDecoder extends SimpleSubtitleDecoder {
    private static final String ATTR_BEGIN = "begin";
    private static final String ATTR_DURATION = "dur";
    private static final String ATTR_END = "end";
    private static final String ATTR_REGION = "region";
    private static final String ATTR_STYLE = "style";
    private static final int DEFAULT_FRAME_RATE = 30;
    private static final String TAG = "TtmlDecoder";
    private static final String TTP = "http://www.w3.org/ns/ttml#parameter";
    private final XmlPullParserFactory xmlParserFactory;
    private static final Pattern CLOCK_TIME = Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");
    private static final Pattern OFFSET_TIME = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");
    private static final Pattern FONT_SIZE = Pattern.compile("^(([0-9]*.)?[0-9]+)(px|em|%)$");
    private static final Pattern PERCENTAGE_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)% (\\d+\\.?\\d*?)%$");
    private static final FrameAndTickRate DEFAULT_FRAME_AND_TICK_RATE = new FrameAndTickRate(30.0f, 1, 1);

    public TtmlDecoder() {
        super(TAG);
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
            this.xmlParserFactory.setNamespaceAware(true);
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.text.SimpleSubtitleDecoder
    public TtmlSubtitle decode(byte[] bytes, int length, boolean reset) throws XmlPullParserException, SubtitleDecoderException, NumberFormatException, IOException {
        try {
            XmlPullParser xmlParser = this.xmlParserFactory.newPullParser();
            Map<String, TtmlStyle> globalStyles = new HashMap<>();
            Map<String, TtmlRegion> regionMap = new HashMap<>();
            regionMap.put("", new TtmlRegion());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes, 0, length);
            xmlParser.setInput(inputStream, null);
            TtmlSubtitle ttmlSubtitle = null;
            LinkedList<TtmlNode> nodeStack = new LinkedList<>();
            int unsupportedNodeDepth = 0;
            FrameAndTickRate frameAndTickRate = DEFAULT_FRAME_AND_TICK_RATE;
            for (int eventType = xmlParser.getEventType(); eventType != 1; eventType = xmlParser.getEventType()) {
                TtmlNode parent = nodeStack.peekLast();
                if (unsupportedNodeDepth == 0) {
                    String name = xmlParser.getName();
                    if (eventType == 2) {
                        if (TtmlNode.TAG_TT.equals(name)) {
                            frameAndTickRate = parseFrameAndTickRates(xmlParser);
                        }
                        if (!isSupportedTag(name)) {
                            Log.i(TAG, "Ignoring unsupported tag: " + xmlParser.getName());
                            unsupportedNodeDepth++;
                        } else if (TtmlNode.TAG_HEAD.equals(name)) {
                            parseHeader(xmlParser, globalStyles, regionMap);
                        } else {
                            try {
                                TtmlNode node = parseNode(xmlParser, parent, regionMap, frameAndTickRate);
                                nodeStack.addLast(node);
                                if (parent != null) {
                                    parent.addChild(node);
                                }
                            } catch (SubtitleDecoderException e) {
                                Log.w(TAG, "Suppressing parser error", e);
                                unsupportedNodeDepth++;
                            }
                        }
                    } else if (eventType == 4) {
                        parent.addChild(TtmlNode.buildTextNode(xmlParser.getText()));
                    } else if (eventType == 3) {
                        if (xmlParser.getName().equals(TtmlNode.TAG_TT)) {
                            ttmlSubtitle = new TtmlSubtitle(nodeStack.getLast(), globalStyles, regionMap);
                        }
                        nodeStack.removeLast();
                    }
                } else if (eventType == 2) {
                    unsupportedNodeDepth++;
                } else if (eventType == 3) {
                    unsupportedNodeDepth--;
                }
                xmlParser.next();
            }
            return ttmlSubtitle;
        } catch (IOException e2) {
            throw new IllegalStateException("Unexpected error when reading input.", e2);
        } catch (XmlPullParserException xppe) {
            throw new SubtitleDecoderException("Unable to decode source", xppe);
        }
    }

    private FrameAndTickRate parseFrameAndTickRates(XmlPullParser xmlParser) throws SubtitleDecoderException, NumberFormatException {
        int frameRate = 30;
        String frameRateString = xmlParser.getAttributeValue(TTP, "frameRate");
        if (frameRateString != null) {
            frameRate = Integer.parseInt(frameRateString);
        }
        float frameRateMultiplier = 1.0f;
        String frameRateMultiplierString = xmlParser.getAttributeValue(TTP, "frameRateMultiplier");
        if (frameRateMultiplierString != null) {
            String[] parts = frameRateMultiplierString.split(PinyinConverter.PINYIN_SEPARATOR);
            if (parts.length != 2) {
                throw new SubtitleDecoderException("frameRateMultiplier doesn't have 2 parts");
            }
            float numerator = Integer.parseInt(parts[0]);
            float denominator = Integer.parseInt(parts[1]);
            frameRateMultiplier = numerator / denominator;
        }
        int subFrameRate = DEFAULT_FRAME_AND_TICK_RATE.subFrameRate;
        String subFrameRateString = xmlParser.getAttributeValue(TTP, "subFrameRate");
        if (subFrameRateString != null) {
            subFrameRate = Integer.parseInt(subFrameRateString);
        }
        int tickRate = DEFAULT_FRAME_AND_TICK_RATE.tickRate;
        String tickRateString = xmlParser.getAttributeValue(TTP, "tickRate");
        if (tickRateString != null) {
            tickRate = Integer.parseInt(tickRateString);
        }
        return new FrameAndTickRate(frameRate * frameRateMultiplier, subFrameRate, tickRate);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Map<String, TtmlStyle> parseHeader(XmlPullParser xmlParser, Map<String, TtmlStyle> globalStyles, Map<String, TtmlRegion> map) throws XmlPullParserException, IOException {
        Pair<String, TtmlRegion> ttmlRegionInfo;
        do {
            xmlParser.next();
            if (XmlPullParserUtil.isStartTag(xmlParser, "style")) {
                String parentStyleId = XmlPullParserUtil.getAttributeValue(xmlParser, "style");
                TtmlStyle style = parseStyleAttributes(xmlParser, new TtmlStyle());
                if (parentStyleId != null) {
                    for (String id : parseStyleIds(parentStyleId)) {
                        style.chain(globalStyles.get(id));
                    }
                }
                if (style.getId() != null) {
                    globalStyles.put(style.getId(), style);
                }
            } else if (XmlPullParserUtil.isStartTag(xmlParser, "region") && (ttmlRegionInfo = parseRegionAttributes(xmlParser)) != null) {
                map.put(ttmlRegionInfo.first, ttmlRegionInfo.second);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlParser, TtmlNode.TAG_HEAD));
        return globalStyles;
    }

    private Pair<String, TtmlRegion> parseRegionAttributes(XmlPullParser xmlParser) {
        String regionId = XmlPullParserUtil.getAttributeValue(xmlParser, TtmlNode.ATTR_ID);
        String regionOrigin = XmlPullParserUtil.getAttributeValue(xmlParser, TtmlNode.ATTR_TTS_ORIGIN);
        String regionExtent = XmlPullParserUtil.getAttributeValue(xmlParser, TtmlNode.ATTR_TTS_EXTENT);
        if (regionOrigin == null || regionId == null) {
            return null;
        }
        float position = Float.MIN_VALUE;
        float line = Float.MIN_VALUE;
        Matcher originMatcher = PERCENTAGE_COORDINATES.matcher(regionOrigin);
        if (originMatcher.matches()) {
            try {
                position = Float.parseFloat(originMatcher.group(1)) / 100.0f;
                line = Float.parseFloat(originMatcher.group(2)) / 100.0f;
            } catch (NumberFormatException e) {
                Log.w(TAG, "Ignoring region with malformed origin: '" + regionOrigin + "'", e);
                position = Float.MIN_VALUE;
            }
        }
        float width = Float.MIN_VALUE;
        if (regionExtent != null) {
            Matcher extentMatcher = PERCENTAGE_COORDINATES.matcher(regionExtent);
            if (extentMatcher.matches()) {
                try {
                    width = Float.parseFloat(extentMatcher.group(1)) / 100.0f;
                } catch (NumberFormatException e2) {
                    Log.w(TAG, "Ignoring malformed region extent: '" + regionExtent + "'", e2);
                }
            }
        }
        if (position != Float.MIN_VALUE) {
            return new Pair<>(regionId, new TtmlRegion(position, line, 0, width));
        }
        return null;
    }

    private String[] parseStyleIds(String parentStyleIds) {
        return parentStyleIds.split("\\s+");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:58:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:6:0x001b  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01cb  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.google.android.exoplayer2.text.ttml.TtmlStyle parseStyleAttributes(org.xmlpull.v1.XmlPullParser r13, com.google.android.exoplayer2.text.ttml.TtmlStyle r14) {
        /*
            Method dump skipped, instructions count: 672
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.text.ttml.TtmlDecoder.parseStyleAttributes(org.xmlpull.v1.XmlPullParser, com.google.android.exoplayer2.text.ttml.TtmlStyle):com.google.android.exoplayer2.text.ttml.TtmlStyle");
    }

    private TtmlStyle createIfNull(TtmlStyle style) {
        return style == null ? new TtmlStyle() : style;
    }

    private TtmlNode parseNode(XmlPullParser parser, TtmlNode parent, Map<String, TtmlRegion> regionMap, FrameAndTickRate frameAndTickRate) throws SubtitleDecoderException, NumberFormatException {
        String value;
        long duration = C.TIME_UNSET;
        long startTime = C.TIME_UNSET;
        long endTime = C.TIME_UNSET;
        String regionId = "";
        String[] styleIds = null;
        int attributeCount = parser.getAttributeCount();
        TtmlStyle style = parseStyleAttributes(parser, null);
        for (int i = 0; i < attributeCount; i++) {
            String attr = parser.getAttributeName(i);
            value = parser.getAttributeValue(i);
            switch (attr) {
                case "begin":
                    startTime = parseTimeExpression(value, frameAndTickRate);
                    break;
                case "end":
                    endTime = parseTimeExpression(value, frameAndTickRate);
                    break;
                case "dur":
                    duration = parseTimeExpression(value, frameAndTickRate);
                    break;
                case "style":
                    String[] ids = parseStyleIds(value);
                    if (ids.length > 0) {
                        styleIds = ids;
                        break;
                    } else {
                        break;
                    }
                case "region":
                    if (regionMap.containsKey(value)) {
                        regionId = value;
                        break;
                    } else {
                        break;
                    }
            }
        }
        if (parent != null && parent.startTimeUs != C.TIME_UNSET) {
            if (startTime != C.TIME_UNSET) {
                startTime += parent.startTimeUs;
            }
            if (endTime != C.TIME_UNSET) {
                endTime += parent.startTimeUs;
            }
        }
        if (endTime == C.TIME_UNSET) {
            if (duration != C.TIME_UNSET) {
                endTime = startTime + duration;
            } else if (parent != null && parent.endTimeUs != C.TIME_UNSET) {
                endTime = parent.endTimeUs;
            }
        }
        return TtmlNode.buildNode(parser.getName(), startTime, endTime, style, styleIds, regionId);
    }

    private static boolean isSupportedTag(String tag) {
        return tag.equals(TtmlNode.TAG_TT) || tag.equals(TtmlNode.TAG_HEAD) || tag.equals(TtmlNode.TAG_BODY) || tag.equals(TtmlNode.TAG_DIV) || tag.equals(TtmlNode.TAG_P) || tag.equals(TtmlNode.TAG_SPAN) || tag.equals(TtmlNode.TAG_BR) || tag.equals("style") || tag.equals(TtmlNode.TAG_STYLING) || tag.equals(TtmlNode.TAG_LAYOUT) || tag.equals("region") || tag.equals(TtmlNode.TAG_METADATA) || tag.equals(TtmlNode.TAG_SMPTE_IMAGE) || tag.equals(TtmlNode.TAG_SMPTE_DATA) || tag.equals(TtmlNode.TAG_SMPTE_INFORMATION);
    }

    private static void parseFontSize(String expression, TtmlStyle out) throws SubtitleDecoderException {
        Matcher matcher;
        String unit;
        String[] expressions = expression.split("\\s+");
        if (expressions.length == 1) {
            matcher = FONT_SIZE.matcher(expression);
        } else if (expressions.length == 2) {
            matcher = FONT_SIZE.matcher(expressions[1]);
            Log.w(TAG, "Multiple values in fontSize attribute. Picking the second value for vertical font size and ignoring the first.");
        } else {
            throw new SubtitleDecoderException("Invalid number of entries for fontSize: " + expressions.length + ".");
        }
        if (matcher.matches()) {
            unit = matcher.group(3);
            switch (unit) {
                case "px":
                    out.setFontSizeUnit(1);
                    break;
                case "em":
                    out.setFontSizeUnit(2);
                    break;
                case "%":
                    out.setFontSizeUnit(3);
                    break;
                default:
                    throw new SubtitleDecoderException("Invalid unit for fontSize: '" + unit + "'.");
            }
            out.setFontSize(Float.valueOf(matcher.group(1)).floatValue());
            return;
        }
        throw new SubtitleDecoderException("Invalid expression for fontSize: '" + expression + "'.");
    }

    private static long parseTimeExpression(String time, FrameAndTickRate frameAndTickRate) throws SubtitleDecoderException, NumberFormatException {
        double offsetSeconds;
        Matcher matcher = CLOCK_TIME.matcher(time);
        if (matcher.matches()) {
            String hours = matcher.group(1);
            double durationSeconds = Long.parseLong(hours) * 3600;
            String minutes = matcher.group(2);
            String seconds = matcher.group(3);
            double durationSeconds2 = durationSeconds + (Long.parseLong(minutes) * 60) + Long.parseLong(seconds);
            String fraction = matcher.group(4);
            double durationSeconds3 = durationSeconds2 + (fraction != null ? Double.parseDouble(fraction) : 0.0d);
            String frames = matcher.group(5);
            double durationSeconds4 = durationSeconds3 + (frames != null ? Long.parseLong(frames) / frameAndTickRate.effectiveFrameRate : 0.0d);
            String subframes = matcher.group(6);
            return (long) (1000000.0d * (durationSeconds4 + (subframes != null ? (Long.parseLong(subframes) / frameAndTickRate.subFrameRate) / frameAndTickRate.effectiveFrameRate : 0.0d)));
        }
        Matcher matcher2 = OFFSET_TIME.matcher(time);
        if (matcher2.matches()) {
            String timeValue = matcher2.group(1);
            offsetSeconds = Double.parseDouble(timeValue);
            String unit = matcher2.group(2);
            switch (unit) {
                case "h":
                    offsetSeconds *= 3600.0d;
                    break;
                case "m":
                    offsetSeconds *= 60.0d;
                    break;
                case "ms":
                    offsetSeconds /= 1000.0d;
                    break;
                case "f":
                    offsetSeconds /= frameAndTickRate.effectiveFrameRate;
                    break;
                case "t":
                    offsetSeconds /= frameAndTickRate.tickRate;
                    break;
            }
            return (long) (1000000.0d * offsetSeconds);
        }
        throw new SubtitleDecoderException("Malformed time expression: " + time);
    }

    private static final class FrameAndTickRate {
        final float effectiveFrameRate;
        final int subFrameRate;
        final int tickRate;

        FrameAndTickRate(float effectiveFrameRate, int subFrameRate, int tickRate) {
            this.effectiveFrameRate = effectiveFrameRate;
            this.subFrameRate = subFrameRate;
            this.tickRate = tickRate;
        }
    }
}
