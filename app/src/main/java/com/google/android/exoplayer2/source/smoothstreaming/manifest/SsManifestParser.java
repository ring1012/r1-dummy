package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.CodecSpecificDataUtil;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class SsManifestParser implements ParsingLoadable.Parser<SsManifest> {
    private final XmlPullParserFactory xmlParserFactory;

    public SsManifestParser() {
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.exoplayer2.upstream.ParsingLoadable.Parser
    public SsManifest parse(Uri uri, InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser xmlParser = this.xmlParserFactory.newPullParser();
            xmlParser.setInput(inputStream, null);
            SmoothStreamingMediaParser smoothStreamingMediaParser = new SmoothStreamingMediaParser(null, uri.toString());
            return (SsManifest) smoothStreamingMediaParser.parse(xmlParser);
        } catch (XmlPullParserException e) {
            throw new ParserException(e);
        }
    }

    public static class MissingFieldException extends ParserException {
        public MissingFieldException(String fieldName) {
            super("Missing required field: " + fieldName);
        }
    }

    private static abstract class ElementParser {
        private final String baseUri;
        private final List<Pair<String, Object>> normalizedAttributes = new LinkedList();
        private final ElementParser parent;
        private final String tag;

        protected abstract Object build();

        public ElementParser(ElementParser parent, String baseUri, String tag) {
            this.parent = parent;
            this.baseUri = baseUri;
            this.tag = tag;
        }

        public final Object parse(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
            boolean foundStartTag = false;
            int skippingElementDepth = 0;
            while (true) {
                int eventType = xmlParser.getEventType();
                switch (eventType) {
                    case 1:
                        return null;
                    case 2:
                        String tagName = xmlParser.getName();
                        if (this.tag.equals(tagName)) {
                            foundStartTag = true;
                            parseStartTag(xmlParser);
                            break;
                        } else if (!foundStartTag) {
                            break;
                        } else if (skippingElementDepth > 0) {
                            skippingElementDepth++;
                            break;
                        } else if (handleChildInline(tagName)) {
                            parseStartTag(xmlParser);
                            break;
                        } else {
                            ElementParser childElementParser = newChildParser(this, tagName, this.baseUri);
                            if (childElementParser == null) {
                                skippingElementDepth = 1;
                                break;
                            } else {
                                addChild(childElementParser.parse(xmlParser));
                                break;
                            }
                        }
                    case 3:
                        if (!foundStartTag) {
                            continue;
                        } else if (skippingElementDepth > 0) {
                            skippingElementDepth--;
                            break;
                        } else {
                            String tagName2 = xmlParser.getName();
                            parseEndTag(xmlParser);
                            if (!handleChildInline(tagName2)) {
                                return build();
                            }
                            break;
                        }
                    case 4:
                        if (!foundStartTag || skippingElementDepth != 0) {
                            break;
                        } else {
                            parseText(xmlParser);
                            break;
                        }
                        break;
                }
                xmlParser.next();
            }
        }

        private ElementParser newChildParser(ElementParser parent, String name, String baseUri) {
            if (QualityLevelParser.TAG.equals(name)) {
                return new QualityLevelParser(parent, baseUri);
            }
            if (ProtectionParser.TAG.equals(name)) {
                return new ProtectionParser(parent, baseUri);
            }
            if (StreamIndexParser.TAG.equals(name)) {
                return new StreamIndexParser(parent, baseUri);
            }
            return null;
        }

        protected final void putNormalizedAttribute(String key, Object value) {
            this.normalizedAttributes.add(Pair.create(key, value));
        }

        protected final Object getNormalizedAttribute(String key) {
            for (int i = 0; i < this.normalizedAttributes.size(); i++) {
                Pair<String, Object> pair = this.normalizedAttributes.get(i);
                if (((String) pair.first).equals(key)) {
                    return pair.second;
                }
            }
            if (this.parent == null) {
                return null;
            }
            return this.parent.getNormalizedAttribute(key);
        }

        protected boolean handleChildInline(String tagName) {
            return false;
        }

        protected void parseStartTag(XmlPullParser xmlParser) throws ParserException {
        }

        protected void parseText(XmlPullParser xmlParser) {
        }

        protected void parseEndTag(XmlPullParser xmlParser) {
        }

        protected void addChild(Object parsedChild) {
        }

        protected final String parseRequiredString(XmlPullParser parser, String key) throws MissingFieldException {
            String value = parser.getAttributeValue(null, key);
            if (value != null) {
                return value;
            }
            throw new MissingFieldException(key);
        }

        protected final int parseInt(XmlPullParser parser, String key, int defaultValue) throws ParserException, NumberFormatException {
            String value = parser.getAttributeValue(null, key);
            if (value != null) {
                try {
                    int defaultValue2 = Integer.parseInt(value);
                    return defaultValue2;
                } catch (NumberFormatException e) {
                    throw new ParserException(e);
                }
            }
            return defaultValue;
        }

        protected final int parseRequiredInt(XmlPullParser parser, String key) throws ParserException {
            String value = parser.getAttributeValue(null, key);
            if (value != null) {
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new ParserException(e);
                }
            }
            throw new MissingFieldException(key);
        }

        protected final long parseLong(XmlPullParser parser, String key, long defaultValue) throws ParserException, NumberFormatException {
            String value = parser.getAttributeValue(null, key);
            if (value != null) {
                try {
                    long defaultValue2 = Long.parseLong(value);
                    return defaultValue2;
                } catch (NumberFormatException e) {
                    throw new ParserException(e);
                }
            }
            return defaultValue;
        }

        protected final long parseRequiredLong(XmlPullParser parser, String key) throws ParserException {
            String value = parser.getAttributeValue(null, key);
            if (value != null) {
                try {
                    return Long.parseLong(value);
                } catch (NumberFormatException e) {
                    throw new ParserException(e);
                }
            }
            throw new MissingFieldException(key);
        }

        protected final boolean parseBoolean(XmlPullParser parser, String key, boolean defaultValue) {
            String value = parser.getAttributeValue(null, key);
            if (value != null) {
                boolean defaultValue2 = Boolean.parseBoolean(value);
                return defaultValue2;
            }
            return defaultValue;
        }
    }

    private static class SmoothStreamingMediaParser extends ElementParser {
        private static final String KEY_DURATION = "Duration";
        private static final String KEY_DVR_WINDOW_LENGTH = "DVRWindowLength";
        private static final String KEY_IS_LIVE = "IsLive";
        private static final String KEY_LOOKAHEAD_COUNT = "LookaheadCount";
        private static final String KEY_MAJOR_VERSION = "MajorVersion";
        private static final String KEY_MINOR_VERSION = "MinorVersion";
        private static final String KEY_TIME_SCALE = "TimeScale";
        public static final String TAG = "SmoothStreamingMedia";
        private long duration;
        private long dvrWindowLength;
        private boolean isLive;
        private int lookAheadCount;
        private int majorVersion;
        private int minorVersion;
        private SsManifest.ProtectionElement protectionElement;
        private final List<SsManifest.StreamElement> streamElements;
        private long timescale;

        public SmoothStreamingMediaParser(ElementParser parent, String baseUri) {
            super(parent, baseUri, TAG);
            this.lookAheadCount = -1;
            this.protectionElement = null;
            this.streamElements = new LinkedList();
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseStartTag(XmlPullParser parser) throws ParserException {
            this.majorVersion = parseRequiredInt(parser, KEY_MAJOR_VERSION);
            this.minorVersion = parseRequiredInt(parser, KEY_MINOR_VERSION);
            this.timescale = parseLong(parser, KEY_TIME_SCALE, 10000000L);
            this.duration = parseRequiredLong(parser, KEY_DURATION);
            this.dvrWindowLength = parseLong(parser, KEY_DVR_WINDOW_LENGTH, 0L);
            this.lookAheadCount = parseInt(parser, KEY_LOOKAHEAD_COUNT, -1);
            this.isLive = parseBoolean(parser, KEY_IS_LIVE, false);
            putNormalizedAttribute(KEY_TIME_SCALE, Long.valueOf(this.timescale));
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void addChild(Object child) {
            if (child instanceof SsManifest.StreamElement) {
                this.streamElements.add((SsManifest.StreamElement) child);
            } else if (child instanceof SsManifest.ProtectionElement) {
                Assertions.checkState(this.protectionElement == null);
                this.protectionElement = (SsManifest.ProtectionElement) child;
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public Object build() {
            SsManifest.StreamElement[] streamElementArray = new SsManifest.StreamElement[this.streamElements.size()];
            this.streamElements.toArray(streamElementArray);
            if (this.protectionElement != null) {
                DrmInitData drmInitData = new DrmInitData(new DrmInitData.SchemeData(this.protectionElement.uuid, MimeTypes.VIDEO_MP4, this.protectionElement.data));
                for (SsManifest.StreamElement streamElement : streamElementArray) {
                    for (int i = 0; i < streamElement.formats.length; i++) {
                        streamElement.formats[i] = streamElement.formats[i].copyWithDrmInitData(drmInitData);
                    }
                }
            }
            return new SsManifest(this.majorVersion, this.minorVersion, this.timescale, this.duration, this.dvrWindowLength, this.lookAheadCount, this.isLive, this.protectionElement, streamElementArray);
        }
    }

    private static class ProtectionParser extends ElementParser {
        public static final String KEY_SYSTEM_ID = "SystemID";
        public static final String TAG = "Protection";
        public static final String TAG_PROTECTION_HEADER = "ProtectionHeader";
        private boolean inProtectionHeader;
        private byte[] initData;
        private UUID uuid;

        public ProtectionParser(ElementParser parent, String baseUri) {
            super(parent, baseUri, TAG);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public boolean handleChildInline(String tag) {
            return TAG_PROTECTION_HEADER.equals(tag);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseStartTag(XmlPullParser parser) {
            if (TAG_PROTECTION_HEADER.equals(parser.getName())) {
                this.inProtectionHeader = true;
                String uuidString = parser.getAttributeValue(null, KEY_SYSTEM_ID);
                this.uuid = UUID.fromString(stripCurlyBraces(uuidString));
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseText(XmlPullParser parser) {
            if (this.inProtectionHeader) {
                this.initData = Base64.decode(parser.getText(), 0);
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseEndTag(XmlPullParser parser) {
            if (TAG_PROTECTION_HEADER.equals(parser.getName())) {
                this.inProtectionHeader = false;
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public Object build() {
            return new SsManifest.ProtectionElement(this.uuid, PsshAtomUtil.buildPsshAtom(this.uuid, this.initData));
        }

        private static String stripCurlyBraces(String uuidString) {
            if (uuidString.charAt(0) == '{' && uuidString.charAt(uuidString.length() - 1) == '}') {
                return uuidString.substring(1, uuidString.length() - 1);
            }
            return uuidString;
        }
    }

    private static class StreamIndexParser extends ElementParser {
        private static final String KEY_DISPLAY_HEIGHT = "DisplayHeight";
        private static final String KEY_DISPLAY_WIDTH = "DisplayWidth";
        private static final String KEY_FRAGMENT_DURATION = "d";
        private static final String KEY_FRAGMENT_REPEAT_COUNT = "r";
        private static final String KEY_FRAGMENT_START_TIME = "t";
        private static final String KEY_LANGUAGE = "Language";
        private static final String KEY_MAX_HEIGHT = "MaxHeight";
        private static final String KEY_MAX_WIDTH = "MaxWidth";
        private static final String KEY_NAME = "Name";
        private static final String KEY_SUB_TYPE = "Subtype";
        private static final String KEY_TIME_SCALE = "TimeScale";
        private static final String KEY_TYPE = "Type";
        private static final String KEY_TYPE_AUDIO = "audio";
        private static final String KEY_TYPE_TEXT = "text";
        private static final String KEY_TYPE_VIDEO = "video";
        private static final String KEY_URL = "Url";
        public static final String TAG = "StreamIndex";
        private static final String TAG_STREAM_FRAGMENT = "c";
        private final String baseUri;
        private int displayHeight;
        private int displayWidth;
        private final List<Format> formats;
        private String language;
        private long lastChunkDuration;
        private int maxHeight;
        private int maxWidth;
        private String name;
        private ArrayList<Long> startTimes;
        private String subType;
        private long timescale;
        private int type;
        private String url;

        public StreamIndexParser(ElementParser parent, String baseUri) {
            super(parent, baseUri, TAG);
            this.baseUri = baseUri;
            this.formats = new LinkedList();
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public boolean handleChildInline(String tag) {
            return TAG_STREAM_FRAGMENT.equals(tag);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseStartTag(XmlPullParser parser) throws ParserException {
            if (TAG_STREAM_FRAGMENT.equals(parser.getName())) {
                parseStreamFragmentStartTag(parser);
            } else {
                parseStreamElementStartTag(parser);
            }
        }

        private void parseStreamFragmentStartTag(XmlPullParser parser) throws ParserException {
            int chunkIndex = this.startTimes.size();
            long startTime = parseLong(parser, KEY_FRAGMENT_START_TIME, C.TIME_UNSET);
            if (startTime == C.TIME_UNSET) {
                if (chunkIndex == 0) {
                    startTime = 0;
                } else if (this.lastChunkDuration != -1) {
                    startTime = this.startTimes.get(chunkIndex - 1).longValue() + this.lastChunkDuration;
                } else {
                    throw new ParserException("Unable to infer start time");
                }
            }
            int chunkIndex2 = chunkIndex + 1;
            this.startTimes.add(Long.valueOf(startTime));
            this.lastChunkDuration = parseLong(parser, KEY_FRAGMENT_DURATION, C.TIME_UNSET);
            long repeatCount = parseLong(parser, KEY_FRAGMENT_REPEAT_COUNT, 1L);
            if (repeatCount > 1 && this.lastChunkDuration == C.TIME_UNSET) {
                throw new ParserException("Repeated chunk with unspecified duration");
            }
            for (int i = 1; i < repeatCount; i++) {
                chunkIndex2++;
                this.startTimes.add(Long.valueOf((this.lastChunkDuration * i) + startTime));
            }
        }

        private void parseStreamElementStartTag(XmlPullParser parser) throws ParserException {
            this.type = parseType(parser);
            putNormalizedAttribute(KEY_TYPE, Integer.valueOf(this.type));
            if (this.type == 3) {
                this.subType = parseRequiredString(parser, KEY_SUB_TYPE);
            } else {
                this.subType = parser.getAttributeValue(null, KEY_SUB_TYPE);
            }
            this.name = parser.getAttributeValue(null, KEY_NAME);
            this.url = parseRequiredString(parser, KEY_URL);
            this.maxWidth = parseInt(parser, KEY_MAX_WIDTH, -1);
            this.maxHeight = parseInt(parser, KEY_MAX_HEIGHT, -1);
            this.displayWidth = parseInt(parser, KEY_DISPLAY_WIDTH, -1);
            this.displayHeight = parseInt(parser, KEY_DISPLAY_HEIGHT, -1);
            this.language = parser.getAttributeValue(null, KEY_LANGUAGE);
            putNormalizedAttribute(KEY_LANGUAGE, this.language);
            this.timescale = parseInt(parser, KEY_TIME_SCALE, -1);
            if (this.timescale == -1) {
                this.timescale = ((Long) getNormalizedAttribute(KEY_TIME_SCALE)).longValue();
            }
            this.startTimes = new ArrayList<>();
        }

        private int parseType(XmlPullParser parser) throws ParserException {
            String value = parser.getAttributeValue(null, KEY_TYPE);
            if (value != null) {
                if ("audio".equalsIgnoreCase(value)) {
                    return 1;
                }
                if ("video".equalsIgnoreCase(value)) {
                    return 2;
                }
                if ("text".equalsIgnoreCase(value)) {
                    return 3;
                }
                throw new ParserException("Invalid key value[" + value + "]");
            }
            throw new MissingFieldException(KEY_TYPE);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void addChild(Object child) {
            if (child instanceof Format) {
                this.formats.add((Format) child);
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public Object build() {
            Format[] formatArray = new Format[this.formats.size()];
            this.formats.toArray(formatArray);
            return new SsManifest.StreamElement(this.baseUri, this.url, this.type, this.subType, this.timescale, this.name, this.maxWidth, this.maxHeight, this.displayWidth, this.displayHeight, this.language, formatArray, this.startTimes, this.lastChunkDuration);
        }
    }

    private static class QualityLevelParser extends ElementParser {
        private static final String KEY_BITRATE = "Bitrate";
        private static final String KEY_CHANNELS = "Channels";
        private static final String KEY_CODEC_PRIVATE_DATA = "CodecPrivateData";
        private static final String KEY_FOUR_CC = "FourCC";
        private static final String KEY_INDEX = "Index";
        private static final String KEY_LANGUAGE = "Language";
        private static final String KEY_MAX_HEIGHT = "MaxHeight";
        private static final String KEY_MAX_WIDTH = "MaxWidth";
        private static final String KEY_SAMPLING_RATE = "SamplingRate";
        private static final String KEY_TYPE = "Type";
        public static final String TAG = "QualityLevel";
        private Format format;

        public QualityLevelParser(ElementParser parent, String baseUri) {
            super(parent, baseUri, TAG);
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public void parseStartTag(XmlPullParser parser) throws ParserException {
            int type = ((Integer) getNormalizedAttribute(KEY_TYPE)).intValue();
            String id = parser.getAttributeValue(null, KEY_INDEX);
            int bitrate = parseRequiredInt(parser, KEY_BITRATE);
            String sampleMimeType = fourCCToMimeType(parseRequiredString(parser, KEY_FOUR_CC));
            if (type == 2) {
                int width = parseRequiredInt(parser, KEY_MAX_WIDTH);
                int height = parseRequiredInt(parser, KEY_MAX_HEIGHT);
                this.format = Format.createVideoContainerFormat(id, MimeTypes.VIDEO_MP4, sampleMimeType, null, bitrate, width, height, -1.0f, buildCodecSpecificData(parser.getAttributeValue(null, KEY_CODEC_PRIVATE_DATA)), 0);
                return;
            }
            if (type == 1) {
                if (sampleMimeType == null) {
                    sampleMimeType = MimeTypes.AUDIO_AAC;
                }
                int channels = parseRequiredInt(parser, KEY_CHANNELS);
                int samplingRate = parseRequiredInt(parser, KEY_SAMPLING_RATE);
                List<byte[]> codecSpecificData = buildCodecSpecificData(parser.getAttributeValue(null, KEY_CODEC_PRIVATE_DATA));
                if (codecSpecificData.isEmpty() && MimeTypes.AUDIO_AAC.equals(sampleMimeType)) {
                    codecSpecificData = Collections.singletonList(CodecSpecificDataUtil.buildAacLcAudioSpecificConfig(samplingRate, channels));
                }
                String language = (String) getNormalizedAttribute(KEY_LANGUAGE);
                this.format = Format.createAudioContainerFormat(id, MimeTypes.AUDIO_MP4, sampleMimeType, null, bitrate, channels, samplingRate, codecSpecificData, 0, language);
                return;
            }
            if (type == 3) {
                String language2 = (String) getNormalizedAttribute(KEY_LANGUAGE);
                this.format = Format.createTextContainerFormat(id, MimeTypes.APPLICATION_MP4, sampleMimeType, null, bitrate, 0, language2);
            } else {
                this.format = Format.createContainerFormat(id, MimeTypes.APPLICATION_MP4, sampleMimeType, null, bitrate, 0, null);
            }
        }

        @Override // com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser.ElementParser
        public Object build() {
            return this.format;
        }

        private static List<byte[]> buildCodecSpecificData(String codecSpecificDataString) {
            ArrayList<byte[]> csd = new ArrayList<>();
            if (!TextUtils.isEmpty(codecSpecificDataString)) {
                byte[] codecPrivateData = Util.getBytesFromHexString(codecSpecificDataString);
                byte[][] split = CodecSpecificDataUtil.splitNalUnits(codecPrivateData);
                if (split == null) {
                    csd.add(codecPrivateData);
                } else {
                    Collections.addAll(csd, split);
                }
            }
            return csd;
        }

        private static String fourCCToMimeType(String fourCC) {
            if (fourCC.equalsIgnoreCase("H264") || fourCC.equalsIgnoreCase("X264") || fourCC.equalsIgnoreCase("AVC1") || fourCC.equalsIgnoreCase("DAVC")) {
                return MimeTypes.VIDEO_H264;
            }
            if (fourCC.equalsIgnoreCase("AAC") || fourCC.equalsIgnoreCase("AACL") || fourCC.equalsIgnoreCase("AACH") || fourCC.equalsIgnoreCase("AACP")) {
                return MimeTypes.AUDIO_AAC;
            }
            if (fourCC.equalsIgnoreCase("TTML")) {
                return MimeTypes.APPLICATION_TTML;
            }
            if (fourCC.equalsIgnoreCase("ac-3") || fourCC.equalsIgnoreCase("dac3")) {
                return MimeTypes.AUDIO_AC3;
            }
            if (fourCC.equalsIgnoreCase("ec-3") || fourCC.equalsIgnoreCase("dec3")) {
                return MimeTypes.AUDIO_E_AC3;
            }
            if (fourCC.equalsIgnoreCase("dtsc")) {
                return MimeTypes.AUDIO_DTS;
            }
            if (fourCC.equalsIgnoreCase("dtsh") || fourCC.equalsIgnoreCase("dtsl")) {
                return MimeTypes.AUDIO_DTS_HD;
            }
            if (fourCC.equalsIgnoreCase("dtse")) {
                return MimeTypes.AUDIO_DTS_EXPRESS;
            }
            if (fourCC.equalsIgnoreCase("opus")) {
                return MimeTypes.AUDIO_OPUS;
            }
            return null;
        }
    }
}
