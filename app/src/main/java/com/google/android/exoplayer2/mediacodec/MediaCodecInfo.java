package com.google.android.exoplayer2.mediacodec;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.media.MediaCodecInfo;
import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;

@TargetApi(16)
/* loaded from: classes.dex */
public final class MediaCodecInfo {
    public static final String TAG = "MediaCodecInfo";
    public final boolean adaptive;
    private final MediaCodecInfo.CodecCapabilities capabilities;
    private final String mimeType;
    public final String name;
    public final boolean tunneling;

    public static MediaCodecInfo newPassthroughInstance(String name) {
        return new MediaCodecInfo(name, null, null);
    }

    public static MediaCodecInfo newInstance(String name, String mimeType, MediaCodecInfo.CodecCapabilities capabilities) {
        return new MediaCodecInfo(name, mimeType, capabilities);
    }

    private MediaCodecInfo(String name, String mimeType, MediaCodecInfo.CodecCapabilities capabilities) {
        this.name = (String) Assertions.checkNotNull(name);
        this.mimeType = mimeType;
        this.capabilities = capabilities;
        this.adaptive = capabilities != null && isAdaptive(capabilities);
        this.tunneling = capabilities != null && isTunneling(capabilities);
    }

    public MediaCodecInfo.CodecProfileLevel[] getProfileLevels() {
        return (this.capabilities == null || this.capabilities.profileLevels == null) ? new MediaCodecInfo.CodecProfileLevel[0] : this.capabilities.profileLevels;
    }

    public boolean isCodecSupported(String codec) {
        if (codec == null || this.mimeType == null) {
            return true;
        }
        String codecMimeType = MimeTypes.getMediaMimeType(codec);
        if (codecMimeType == null) {
            return true;
        }
        if (!this.mimeType.equals(codecMimeType)) {
            logNoSupport("codec.mime " + codec + ", " + codecMimeType);
            return false;
        }
        Pair<Integer, Integer> codecProfileAndLevel = MediaCodecUtil.getCodecProfileAndLevel(codec);
        if (codecProfileAndLevel == null) {
            return true;
        }
        for (MediaCodecInfo.CodecProfileLevel capabilities : getProfileLevels()) {
            if (capabilities.profile == ((Integer) codecProfileAndLevel.first).intValue() && capabilities.level >= ((Integer) codecProfileAndLevel.second).intValue()) {
                return true;
            }
        }
        logNoSupport("codec.profileLevel, " + codec + ", " + codecMimeType);
        return false;
    }

    @TargetApi(21)
    public boolean isVideoSizeAndRateSupportedV21(int width, int height, double frameRate) {
        if (this.capabilities == null) {
            logNoSupport("sizeAndRate.caps");
            return false;
        }
        MediaCodecInfo.VideoCapabilities videoCapabilities = this.capabilities.getVideoCapabilities();
        if (videoCapabilities == null) {
            logNoSupport("sizeAndRate.vCaps");
            return false;
        }
        if (!areSizeAndRateSupported(videoCapabilities, width, height, frameRate)) {
            if (width >= height || !areSizeAndRateSupported(videoCapabilities, height, width, frameRate)) {
                logNoSupport("sizeAndRate.support, " + width + "x" + height + "x" + frameRate);
                return false;
            }
            logAssumedSupport("sizeAndRate.rotated, " + width + "x" + height + "x" + frameRate);
        }
        return true;
    }

    @TargetApi(21)
    public Point alignVideoSizeV21(int width, int height) {
        if (this.capabilities == null) {
            logNoSupport("align.caps");
            return null;
        }
        MediaCodecInfo.VideoCapabilities videoCapabilities = this.capabilities.getVideoCapabilities();
        if (videoCapabilities == null) {
            logNoSupport("align.vCaps");
            return null;
        }
        int widthAlignment = videoCapabilities.getWidthAlignment();
        int heightAlignment = videoCapabilities.getHeightAlignment();
        return new Point(Util.ceilDivide(width, widthAlignment) * widthAlignment, Util.ceilDivide(height, heightAlignment) * heightAlignment);
    }

    @TargetApi(21)
    public boolean isAudioSampleRateSupportedV21(int sampleRate) {
        if (this.capabilities == null) {
            logNoSupport("sampleRate.caps");
            return false;
        }
        MediaCodecInfo.AudioCapabilities audioCapabilities = this.capabilities.getAudioCapabilities();
        if (audioCapabilities == null) {
            logNoSupport("sampleRate.aCaps");
            return false;
        }
        if (!audioCapabilities.isSampleRateSupported(sampleRate)) {
            logNoSupport("sampleRate.support, " + sampleRate);
            return false;
        }
        return true;
    }

    @TargetApi(21)
    public boolean isAudioChannelCountSupportedV21(int channelCount) {
        if (this.capabilities == null) {
            logNoSupport("channelCount.caps");
            return false;
        }
        MediaCodecInfo.AudioCapabilities audioCapabilities = this.capabilities.getAudioCapabilities();
        if (audioCapabilities == null) {
            logNoSupport("channelCount.aCaps");
            return false;
        }
        if (audioCapabilities.getMaxInputChannelCount() < channelCount) {
            logNoSupport("channelCount.support, " + channelCount);
            return false;
        }
        return true;
    }

    private void logNoSupport(String message) {
        Log.d(TAG, "NoSupport [" + message + "] [" + this.name + ", " + this.mimeType + "] [" + Util.DEVICE_DEBUG_INFO + "]");
    }

    private void logAssumedSupport(String message) {
        Log.d(TAG, "AssumedSupport [" + message + "] [" + this.name + ", " + this.mimeType + "] [" + Util.DEVICE_DEBUG_INFO + "]");
    }

    private static boolean isAdaptive(MediaCodecInfo.CodecCapabilities capabilities) {
        return Util.SDK_INT >= 19 && isAdaptiveV19(capabilities);
    }

    @TargetApi(19)
    private static boolean isAdaptiveV19(MediaCodecInfo.CodecCapabilities capabilities) {
        return capabilities.isFeatureSupported("adaptive-playback");
    }

    @TargetApi(21)
    private static boolean areSizeAndRateSupported(MediaCodecInfo.VideoCapabilities capabilities, int width, int height, double frameRate) {
        return (frameRate == -1.0d || frameRate <= 0.0d) ? capabilities.isSizeSupported(width, height) : capabilities.areSizeAndRateSupported(width, height, frameRate);
    }

    private static boolean isTunneling(MediaCodecInfo.CodecCapabilities capabilities) {
        return Util.SDK_INT >= 21 && isTunnelingV21(capabilities);
    }

    @TargetApi(21)
    private static boolean isTunnelingV21(MediaCodecInfo.CodecCapabilities capabilities) {
        return capabilities.isFeatureSupported("tunneled-playback");
    }
}
