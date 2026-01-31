package com.google.android.exoplayer2.util;

import android.text.TextUtils;

/* loaded from: classes.dex */
public final class MimeTypes {
    public static final String APPLICATION_CAMERA_MOTION = "application/x-camera-motion";
    public static final String APPLICATION_CEA608 = "application/cea-608";
    public static final String APPLICATION_CEA708 = "application/cea-708";
    public static final String APPLICATION_DVBSUBS = "application/dvbsubs";
    public static final String APPLICATION_EMSG = "application/x-emsg";
    public static final String APPLICATION_ID3 = "application/id3";
    public static final String APPLICATION_M3U8 = "application/x-mpegURL";
    public static final String APPLICATION_MP4 = "application/mp4";
    public static final String APPLICATION_MP4CEA608 = "application/x-mp4-cea-608";
    public static final String APPLICATION_MP4VTT = "application/x-mp4-vtt";
    public static final String APPLICATION_PGS = "application/pgs";
    public static final String APPLICATION_RAWCC = "application/x-rawcc";
    public static final String APPLICATION_SCTE35 = "application/x-scte35";
    public static final String APPLICATION_SUBRIP = "application/x-subrip";
    public static final String APPLICATION_TTML = "application/ttml+xml";
    public static final String APPLICATION_TX3G = "application/x-quicktime-tx3g";
    public static final String APPLICATION_VOBSUB = "application/vobsub";
    public static final String APPLICATION_WEBM = "application/webm";
    public static final String AUDIO_AAC = "audio/mp4a-latm";
    public static final String AUDIO_AC3 = "audio/ac3";
    public static final String AUDIO_ALAC = "audio/alac";
    public static final String AUDIO_ALAW = "audio/g711-alaw";
    public static final String AUDIO_AMR_NB = "audio/3gpp";
    public static final String AUDIO_AMR_WB = "audio/amr-wb";
    public static final String AUDIO_DTS = "audio/vnd.dts";
    public static final String AUDIO_DTS_EXPRESS = "audio/vnd.dts.hd;profile=lbr";
    public static final String AUDIO_DTS_HD = "audio/vnd.dts.hd";
    public static final String AUDIO_E_AC3 = "audio/eac3";
    public static final String AUDIO_FLAC = "audio/x-flac";
    public static final String AUDIO_MP4 = "audio/mp4";
    public static final String AUDIO_MPEG = "audio/mpeg";
    public static final String AUDIO_MPEG_L1 = "audio/mpeg-L1";
    public static final String AUDIO_MPEG_L2 = "audio/mpeg-L2";
    public static final String AUDIO_OPUS = "audio/opus";
    public static final String AUDIO_RAW = "audio/raw";
    public static final String AUDIO_TRUEHD = "audio/true-hd";
    public static final String AUDIO_ULAW = "audio/g711-mlaw";
    public static final String AUDIO_VORBIS = "audio/vorbis";
    public static final String AUDIO_WEBM = "audio/webm";
    public static final String BASE_TYPE_APPLICATION = "application";
    public static final String BASE_TYPE_AUDIO = "audio";
    public static final String BASE_TYPE_TEXT = "text";
    public static final String BASE_TYPE_VIDEO = "video";
    public static final String TEXT_VTT = "text/vtt";
    public static final String VIDEO_H263 = "video/3gpp";
    public static final String VIDEO_H264 = "video/avc";
    public static final String VIDEO_H265 = "video/hevc";
    public static final String VIDEO_MP4 = "video/mp4";
    public static final String VIDEO_MP4V = "video/mp4v-es";
    public static final String VIDEO_MPEG2 = "video/mpeg2";
    public static final String VIDEO_UNKNOWN = "video/x-unknown";
    public static final String VIDEO_VC1 = "video/wvc1";
    public static final String VIDEO_VP8 = "video/x-vnd.on2.vp8";
    public static final String VIDEO_VP9 = "video/x-vnd.on2.vp9";
    public static final String VIDEO_WEBM = "video/webm";

    private MimeTypes() {
    }

    public static boolean isAudio(String mimeType) {
        return "audio".equals(getTopLevelType(mimeType));
    }

    public static boolean isVideo(String mimeType) {
        return BASE_TYPE_VIDEO.equals(getTopLevelType(mimeType));
    }

    public static boolean isText(String mimeType) {
        return "text".equals(getTopLevelType(mimeType));
    }

    public static boolean isApplication(String mimeType) {
        return BASE_TYPE_APPLICATION.equals(getTopLevelType(mimeType));
    }

    public static String getVideoMediaMimeType(String codecs) {
        if (codecs == null) {
            return null;
        }
        String[] codecList = codecs.split(",");
        for (String codec : codecList) {
            String mimeType = getMediaMimeType(codec);
            if (mimeType != null && isVideo(mimeType)) {
                return mimeType;
            }
        }
        return null;
    }

    public static String getAudioMediaMimeType(String codecs) {
        if (codecs == null) {
            return null;
        }
        String[] codecList = codecs.split(",");
        for (String codec : codecList) {
            String mimeType = getMediaMimeType(codec);
            if (mimeType != null && isAudio(mimeType)) {
                return mimeType;
            }
        }
        return null;
    }

    public static String getMediaMimeType(String codec) {
        if (codec == null) {
            return null;
        }
        String codec2 = codec.trim();
        if (codec2.startsWith("avc1") || codec2.startsWith("avc3")) {
            return VIDEO_H264;
        }
        if (codec2.startsWith("hev1") || codec2.startsWith("hvc1")) {
            return VIDEO_H265;
        }
        if (codec2.startsWith("vp9")) {
            return VIDEO_VP9;
        }
        if (codec2.startsWith("vp8")) {
            return VIDEO_VP8;
        }
        if (codec2.startsWith("mp4a")) {
            return AUDIO_AAC;
        }
        if (codec2.startsWith("ac-3") || codec2.startsWith("dac3")) {
            return AUDIO_AC3;
        }
        if (codec2.startsWith("ec-3") || codec2.startsWith("dec3")) {
            return AUDIO_E_AC3;
        }
        if (codec2.startsWith("dtsc") || codec2.startsWith("dtse")) {
            return AUDIO_DTS;
        }
        if (codec2.startsWith("dtsh") || codec2.startsWith("dtsl")) {
            return AUDIO_DTS_HD;
        }
        if (codec2.startsWith("opus")) {
            return AUDIO_OPUS;
        }
        if (codec2.startsWith("vorbis")) {
            return AUDIO_VORBIS;
        }
        return null;
    }

    public static int getTrackType(String mimeType) {
        if (TextUtils.isEmpty(mimeType)) {
            return -1;
        }
        if (isAudio(mimeType)) {
            return 1;
        }
        if (isVideo(mimeType)) {
            return 2;
        }
        if (isText(mimeType) || APPLICATION_CEA608.equals(mimeType) || APPLICATION_CEA708.equals(mimeType) || APPLICATION_MP4CEA608.equals(mimeType) || APPLICATION_SUBRIP.equals(mimeType) || APPLICATION_TTML.equals(mimeType) || APPLICATION_TX3G.equals(mimeType) || APPLICATION_MP4VTT.equals(mimeType) || APPLICATION_RAWCC.equals(mimeType) || APPLICATION_VOBSUB.equals(mimeType) || APPLICATION_PGS.equals(mimeType) || APPLICATION_DVBSUBS.equals(mimeType)) {
            return 3;
        }
        return (APPLICATION_ID3.equals(mimeType) || APPLICATION_EMSG.equals(mimeType) || APPLICATION_SCTE35.equals(mimeType) || APPLICATION_CAMERA_MOTION.equals(mimeType)) ? 4 : -1;
    }

    public static int getTrackTypeOfCodec(String codec) {
        return getTrackType(getMediaMimeType(codec));
    }

    private static String getTopLevelType(String mimeType) {
        if (mimeType == null) {
            return null;
        }
        int indexOfSlash = mimeType.indexOf(47);
        if (indexOfSlash == -1) {
            throw new IllegalArgumentException("Invalid mime type: " + mimeType);
        }
        return mimeType.substring(0, indexOfSlash);
    }
}
