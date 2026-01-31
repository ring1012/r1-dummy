package com.google.android.exoplayer2.mediacodec;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.MessageDispatchManager;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseIntArray;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.unisound.ant.device.sessionlayer.DialogProfile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({"InlinedApi"})
@TargetApi(16)
/* loaded from: classes.dex */
public final class MediaCodecUtil {
    private static final SparseIntArray AVC_LEVEL_NUMBER_TO_CONST;
    private static final String CODEC_ID_AVC1 = "avc1";
    private static final String CODEC_ID_AVC2 = "avc2";
    private static final String CODEC_ID_HEV1 = "hev1";
    private static final String CODEC_ID_HVC1 = "hvc1";
    private static final Map<String, Integer> HEVC_CODEC_STRING_TO_PROFILE_LEVEL;
    private static final String TAG = "MediaCodecUtil";
    private static final MediaCodecInfo PASSTHROUGH_DECODER_INFO = MediaCodecInfo.newPassthroughInstance("OMX.google.raw.decoder");
    private static final Pattern PROFILE_PATTERN = Pattern.compile("^\\D?(\\d+)$");
    private static final HashMap<CodecKey, List<MediaCodecInfo>> decoderInfosCache = new HashMap<>();
    private static int maxH264DecodableFrameSize = -1;
    private static final SparseIntArray AVC_PROFILE_NUMBER_TO_CONST = new SparseIntArray();

    private interface MediaCodecListCompat {
        int getCodecCount();

        android.media.MediaCodecInfo getCodecInfoAt(int i);

        boolean isSecurePlaybackSupported(String str, MediaCodecInfo.CodecCapabilities codecCapabilities);

        boolean secureDecodersExplicit();
    }

    public static class DecoderQueryException extends Exception {
        private DecoderQueryException(Throwable cause) {
            super("Failed to query underlying media codecs", cause);
        }
    }

    static {
        AVC_PROFILE_NUMBER_TO_CONST.put(66, 1);
        AVC_PROFILE_NUMBER_TO_CONST.put(77, 2);
        AVC_PROFILE_NUMBER_TO_CONST.put(88, 4);
        AVC_PROFILE_NUMBER_TO_CONST.put(100, 8);
        AVC_LEVEL_NUMBER_TO_CONST = new SparseIntArray();
        AVC_LEVEL_NUMBER_TO_CONST.put(10, 1);
        AVC_LEVEL_NUMBER_TO_CONST.put(11, 4);
        AVC_LEVEL_NUMBER_TO_CONST.put(12, 8);
        AVC_LEVEL_NUMBER_TO_CONST.put(13, 16);
        AVC_LEVEL_NUMBER_TO_CONST.put(20, 32);
        AVC_LEVEL_NUMBER_TO_CONST.put(21, 64);
        AVC_LEVEL_NUMBER_TO_CONST.put(22, 128);
        AVC_LEVEL_NUMBER_TO_CONST.put(30, 256);
        AVC_LEVEL_NUMBER_TO_CONST.put(31, 512);
        AVC_LEVEL_NUMBER_TO_CONST.put(32, 1024);
        AVC_LEVEL_NUMBER_TO_CONST.put(40, 2048);
        AVC_LEVEL_NUMBER_TO_CONST.put(41, 4096);
        AVC_LEVEL_NUMBER_TO_CONST.put(42, 8192);
        AVC_LEVEL_NUMBER_TO_CONST.put(50, 16384);
        AVC_LEVEL_NUMBER_TO_CONST.put(51, 32768);
        AVC_LEVEL_NUMBER_TO_CONST.put(52, 65536);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL = new HashMap();
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L30", 1);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L60", 4);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L63", 16);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L90", 64);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L93", 256);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L120", 1024);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L123", 4096);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L150", 16384);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L153", 65536);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L156", 262144);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L180", 1048576);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L183", Integer.valueOf(AccessibilityEventCompat.TYPE_WINDOWS_CHANGED));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L186", 16777216);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H30", 2);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H60", 8);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H63", 32);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H90", 128);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H93", 512);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H120", 2048);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H123", 8192);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H150", 32768);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H153", 131072);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H156", 524288);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H180", 2097152);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H183", 8388608);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H186", Integer.valueOf(MessageDispatchManager.MSG_TYPE_USER_DATA));
    }

    private MediaCodecUtil() {
    }

    public static void warmDecoderInfoCache(String mimeType, boolean secure) {
        try {
            getDecoderInfos(mimeType, secure);
        } catch (DecoderQueryException e) {
            Log.e(TAG, "Codec warming failed", e);
        }
    }

    public static MediaCodecInfo getPassthroughDecoderInfo() {
        return PASSTHROUGH_DECODER_INFO;
    }

    public static MediaCodecInfo getDecoderInfo(String mimeType, boolean secure) throws DecoderQueryException {
        List<MediaCodecInfo> decoderInfos = getDecoderInfos(mimeType, secure);
        if (decoderInfos.isEmpty()) {
            return null;
        }
        return decoderInfos.get(0);
    }

    public static synchronized List<MediaCodecInfo> getDecoderInfos(String mimeType, boolean secure) throws DecoderQueryException {
        List<MediaCodecInfo> decoderInfos;
        CodecKey key = new CodecKey(mimeType, secure);
        List<MediaCodecInfo> decoderInfos2 = decoderInfosCache.get(key);
        if (decoderInfos2 != null) {
            decoderInfos = decoderInfos2;
        } else {
            MediaCodecListCompat mediaCodecList = Util.SDK_INT >= 21 ? new MediaCodecListCompatV21(secure) : new MediaCodecListCompatV16();
            List<MediaCodecInfo> decoderInfos3 = getDecoderInfosInternal(key, mediaCodecList);
            if (secure && decoderInfos3.isEmpty() && 21 <= Util.SDK_INT && Util.SDK_INT <= 23) {
                MediaCodecListCompat mediaCodecList2 = new MediaCodecListCompatV16();
                decoderInfos3 = getDecoderInfosInternal(key, mediaCodecList2);
                if (!decoderInfos3.isEmpty()) {
                    Log.w(TAG, "MediaCodecList API didn't list secure decoder for: " + mimeType + ". Assuming: " + decoderInfos3.get(0).name);
                }
            }
            List<MediaCodecInfo> decoderInfos4 = Collections.unmodifiableList(decoderInfos3);
            decoderInfosCache.put(key, decoderInfos4);
            decoderInfos = decoderInfos4;
        }
        return decoderInfos;
    }

    private static List<MediaCodecInfo> getDecoderInfosInternal(CodecKey key, MediaCodecListCompat mediaCodecList) throws Exception {
        try {
            List<MediaCodecInfo> decoderInfos = new ArrayList<>();
            String mimeType = key.mimeType;
            int numberOfCodecs = mediaCodecList.getCodecCount();
            boolean secureDecodersExplicit = mediaCodecList.secureDecodersExplicit();
            loop0: for (int i = 0; i < numberOfCodecs; i++) {
                android.media.MediaCodecInfo codecInfo = mediaCodecList.getCodecInfoAt(i);
                String codecName = codecInfo.getName();
                if (isCodecUsableDecoder(codecInfo, codecName, secureDecodersExplicit)) {
                    for (String supportedType : codecInfo.getSupportedTypes()) {
                        if (supportedType.equalsIgnoreCase(mimeType)) {
                            try {
                                MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(supportedType);
                                boolean secure = mediaCodecList.isSecurePlaybackSupported(mimeType, capabilities);
                                if ((secureDecodersExplicit && key.secure == secure) || (!secureDecodersExplicit && !key.secure)) {
                                    decoderInfos.add(MediaCodecInfo.newInstance(codecName, mimeType, capabilities));
                                } else if (!secureDecodersExplicit && secure) {
                                    decoderInfos.add(MediaCodecInfo.newInstance(codecName + ".secure", mimeType, capabilities));
                                    break loop0;
                                }
                            } catch (Exception e) {
                                if (Util.SDK_INT <= 23 && !decoderInfos.isEmpty()) {
                                    Log.e(TAG, "Skipping codec " + codecName + " (failed to query capabilities)");
                                } else {
                                    Log.e(TAG, "Failed to query codec " + codecName + " (" + supportedType + ")");
                                    throw e;
                                }
                            }
                        }
                    }
                }
            }
            return decoderInfos;
        } catch (Exception e2) {
            throw new DecoderQueryException(e2);
        }
    }

    private static boolean isCodecUsableDecoder(android.media.MediaCodecInfo info, String name, boolean secureDecodersExplicit) {
        if (info.isEncoder()) {
            return false;
        }
        if (!secureDecodersExplicit && name.endsWith(".secure")) {
            return false;
        }
        if (Util.SDK_INT < 21 && ("CIPAACDecoder".equals(name) || "CIPMP3Decoder".equals(name) || "CIPVorbisDecoder".equals(name) || "CIPAMRNBDecoder".equals(name) || "AACDecoder".equals(name) || "MP3Decoder".equals(name))) {
            return false;
        }
        if (Util.SDK_INT < 18 && "OMX.SEC.MP3.Decoder".equals(name)) {
            return false;
        }
        if (Util.SDK_INT < 18 && "OMX.MTK.AUDIO.DECODER.AAC".equals(name) && "a70".equals(Util.DEVICE)) {
            return false;
        }
        if (Util.SDK_INT == 16 && "OMX.qcom.audio.decoder.mp3".equals(name) && ("dlxu".equals(Util.DEVICE) || "protou".equals(Util.DEVICE) || "ville".equals(Util.DEVICE) || "villeplus".equals(Util.DEVICE) || "villec2".equals(Util.DEVICE) || Util.DEVICE.startsWith("gee") || "C6602".equals(Util.DEVICE) || "C6603".equals(Util.DEVICE) || "C6606".equals(Util.DEVICE) || "C6616".equals(Util.DEVICE) || "L36h".equals(Util.DEVICE) || "SO-02E".equals(Util.DEVICE))) {
            return false;
        }
        if (Util.SDK_INT == 16 && "OMX.qcom.audio.decoder.aac".equals(name) && ("C1504".equals(Util.DEVICE) || "C1505".equals(Util.DEVICE) || "C1604".equals(Util.DEVICE) || "C1605".equals(Util.DEVICE))) {
            return false;
        }
        if (Util.SDK_INT <= 19 && "OMX.SEC.vp8.dec".equals(name) && "samsung".equals(Util.MANUFACTURER) && (Util.DEVICE.startsWith("d2") || Util.DEVICE.startsWith("serrano") || Util.DEVICE.startsWith("jflte") || Util.DEVICE.startsWith("santos") || Util.DEVICE.startsWith("t0"))) {
            return false;
        }
        return (Util.SDK_INT <= 19 && Util.DEVICE.startsWith("jflte") && "OMX.qcom.video.decoder.vp8".equals(name)) ? false : true;
    }

    public static int maxH264DecodableFrameSize() throws DecoderQueryException {
        if (maxH264DecodableFrameSize == -1) {
            int result = 0;
            MediaCodecInfo decoderInfo = getDecoderInfo(MimeTypes.VIDEO_H264, false);
            if (decoderInfo != null) {
                for (MediaCodecInfo.CodecProfileLevel profileLevel : decoderInfo.getProfileLevels()) {
                    result = Math.max(avcLevelToMaxFrameSize(profileLevel.level), result);
                }
                result = Math.max(result, Util.SDK_INT >= 21 ? 345600 : 172800);
            }
            maxH264DecodableFrameSize = result;
        }
        return maxH264DecodableFrameSize;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0015  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.util.Pair<java.lang.Integer, java.lang.Integer> getCodecProfileAndLevel(java.lang.String r6) {
        /*
            r1 = 0
            r2 = 0
            if (r6 != 0) goto L5
        L4:
            return r1
        L5:
            java.lang.String r3 = "\\."
            java.lang.String[] r0 = r6.split(r3)
            r4 = r0[r2]
            r3 = -1
            int r5 = r4.hashCode()
            switch(r5) {
                case 3006243: goto L32;
                case 3006244: goto L3c;
                case 3199032: goto L1f;
                case 3214780: goto L28;
                default: goto L15;
            }
        L15:
            r2 = r3
        L16:
            switch(r2) {
                case 0: goto L1a;
                case 1: goto L1a;
                case 2: goto L46;
                case 3: goto L46;
                default: goto L19;
            }
        L19:
            goto L4
        L1a:
            android.util.Pair r1 = getHevcProfileAndLevel(r6, r0)
            goto L4
        L1f:
            java.lang.String r5 = "hev1"
            boolean r4 = r4.equals(r5)
            if (r4 == 0) goto L15
            goto L16
        L28:
            java.lang.String r2 = "hvc1"
            boolean r2 = r4.equals(r2)
            if (r2 == 0) goto L15
            r2 = 1
            goto L16
        L32:
            java.lang.String r2 = "avc1"
            boolean r2 = r4.equals(r2)
            if (r2 == 0) goto L15
            r2 = 2
            goto L16
        L3c:
            java.lang.String r2 = "avc2"
            boolean r2 = r4.equals(r2)
            if (r2 == 0) goto L15
            r2 = 3
            goto L16
        L46:
            android.util.Pair r1 = getAvcProfileAndLevel(r6, r0)
            goto L4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.mediacodec.MediaCodecUtil.getCodecProfileAndLevel(java.lang.String):android.util.Pair");
    }

    private static Pair<Integer, Integer> getHevcProfileAndLevel(String codec, String[] parts) {
        int profile;
        if (parts.length < 4) {
            Log.w(TAG, "Ignoring malformed HEVC codec string: " + codec);
            return null;
        }
        Matcher matcher = PROFILE_PATTERN.matcher(parts[1]);
        if (!matcher.matches()) {
            Log.w(TAG, "Ignoring malformed HEVC codec string: " + codec);
            return null;
        }
        String profileString = matcher.group(1);
        if ("1".equals(profileString)) {
            profile = 1;
        } else if (DialogProfile.DIALOG_FINISH.equals(profileString)) {
            profile = 2;
        } else {
            Log.w(TAG, "Unknown HEVC profile string: " + profileString);
            return null;
        }
        Integer level = HEVC_CODEC_STRING_TO_PROFILE_LEVEL.get(parts[3]);
        if (level == null) {
            Log.w(TAG, "Unknown HEVC level string: " + matcher.group(1));
            return null;
        }
        return new Pair<>(Integer.valueOf(profile), level);
    }

    private static Pair<Integer, Integer> getAvcProfileAndLevel(String codec, String[] codecsParts) {
        Integer profileInteger;
        Integer levelInteger;
        if (codecsParts.length < 2) {
            Log.w(TAG, "Ignoring malformed AVC codec string: " + codec);
            return null;
        }
        try {
            if (codecsParts[1].length() == 6) {
                profileInteger = Integer.valueOf(Integer.parseInt(codecsParts[1].substring(0, 2), 16));
                levelInteger = Integer.valueOf(Integer.parseInt(codecsParts[1].substring(4), 16));
            } else if (codecsParts.length >= 3) {
                profileInteger = Integer.valueOf(Integer.parseInt(codecsParts[1]));
                levelInteger = Integer.valueOf(Integer.parseInt(codecsParts[2]));
            } else {
                Log.w(TAG, "Ignoring malformed AVC codec string: " + codec);
                return null;
            }
            Integer profile = Integer.valueOf(AVC_PROFILE_NUMBER_TO_CONST.get(profileInteger.intValue()));
            if (profile == null) {
                Log.w(TAG, "Unknown AVC profile: " + profileInteger);
                return null;
            }
            Integer level = Integer.valueOf(AVC_LEVEL_NUMBER_TO_CONST.get(levelInteger.intValue()));
            if (level == null) {
                Log.w(TAG, "Unknown AVC level: " + levelInteger);
                return null;
            }
            return new Pair<>(profile, level);
        } catch (NumberFormatException e) {
            Log.w(TAG, "Ignoring malformed AVC codec string: " + codec);
            return null;
        }
    }

    private static int avcLevelToMaxFrameSize(int avcLevel) {
        switch (avcLevel) {
            case 1:
            case 2:
                return 25344;
            case 8:
                return 101376;
            case 16:
                return 101376;
            case 32:
                return 101376;
            case 64:
                return 202752;
            case 128:
                return 414720;
            case 256:
                return 414720;
            case 512:
                return 921600;
            case 1024:
                return 1310720;
            case 2048:
                return 2097152;
            case 4096:
                return 2097152;
            case 8192:
                return 2228224;
            case 16384:
                return 5652480;
            case 32768:
                return 9437184;
            default:
                return -1;
        }
    }

    @TargetApi(21)
    private static final class MediaCodecListCompatV21 implements MediaCodecListCompat {
        private final int codecKind;
        private android.media.MediaCodecInfo[] mediaCodecInfos;

        public MediaCodecListCompatV21(boolean includeSecure) {
            this.codecKind = includeSecure ? 1 : 0;
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public int getCodecCount() {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos.length;
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public android.media.MediaCodecInfo getCodecInfoAt(int index) {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos[index];
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public boolean secureDecodersExplicit() {
            return true;
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public boolean isSecurePlaybackSupported(String mimeType, MediaCodecInfo.CodecCapabilities capabilities) {
            return capabilities.isFeatureSupported("secure-playback");
        }

        private void ensureMediaCodecInfosInitialized() {
            if (this.mediaCodecInfos == null) {
                this.mediaCodecInfos = new MediaCodecList(this.codecKind).getCodecInfos();
            }
        }
    }

    private static final class MediaCodecListCompatV16 implements MediaCodecListCompat {
        private MediaCodecListCompatV16() {
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public int getCodecCount() {
            return MediaCodecList.getCodecCount();
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public android.media.MediaCodecInfo getCodecInfoAt(int index) {
            return MediaCodecList.getCodecInfoAt(index);
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public boolean secureDecodersExplicit() {
            return false;
        }

        @Override // com.google.android.exoplayer2.mediacodec.MediaCodecUtil.MediaCodecListCompat
        public boolean isSecurePlaybackSupported(String mimeType, MediaCodecInfo.CodecCapabilities capabilities) {
            return MimeTypes.VIDEO_H264.equals(mimeType);
        }
    }

    private static final class CodecKey {
        public final String mimeType;
        public final boolean secure;

        public CodecKey(String mimeType, boolean secure) {
            this.mimeType = mimeType;
            this.secure = secure;
        }

        public int hashCode() {
            int result = (this.mimeType == null ? 0 : this.mimeType.hashCode()) + 31;
            return (result * 31) + (this.secure ? 1231 : 1237);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || obj.getClass() != CodecKey.class) {
                return false;
            }
            CodecKey other = (CodecKey) obj;
            return TextUtils.equals(this.mimeType, other.mimeType) && this.secure == other.secure;
        }
    }
}
