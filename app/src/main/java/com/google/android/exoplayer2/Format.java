package com.google.android.exoplayer2;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaFormat;
import android.os.Parcel;
import android.os.Parcelable;
import cn.yunzhisheng.asr.JniUscClient;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.ColorInfo;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class Format implements Parcelable {
    public static final Parcelable.Creator<Format> CREATOR = new Parcelable.Creator<Format>() { // from class: com.google.android.exoplayer2.Format.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Format createFromParcel(Parcel in) {
            return new Format(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Format[] newArray(int size) {
            return new Format[size];
        }
    };
    public static final int NO_VALUE = -1;
    public static final long OFFSET_SAMPLE_RELATIVE = Long.MAX_VALUE;
    public final int accessibilityChannel;
    public final int bitrate;
    public final int channelCount;
    public final String codecs;
    public final ColorInfo colorInfo;
    public final String containerMimeType;
    public final DrmInitData drmInitData;
    public final int encoderDelay;
    public final int encoderPadding;
    public final float frameRate;
    private int hashCode;
    public final int height;
    public final String id;
    public final List<byte[]> initializationData;
    public final String language;
    public final int maxInputSize;
    public final Metadata metadata;
    public final int pcmEncoding;
    public final float pixelWidthHeightRatio;
    public final byte[] projectionData;
    public final int rotationDegrees;
    public final String sampleMimeType;
    public final int sampleRate;
    public final int selectionFlags;
    public final int stereoMode;
    public final long subsampleOffsetUs;
    public final int width;

    public static Format createVideoContainerFormat(String id, String containerMimeType, String sampleMimeType, String codecs, int bitrate, int width, int height, float frameRate, List<byte[]> initializationData, int selectionFlags) {
        return new Format(id, containerMimeType, sampleMimeType, codecs, bitrate, -1, width, height, frameRate, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, selectionFlags, null, -1, Long.MAX_VALUE, initializationData, null, null);
    }

    public static Format createVideoSampleFormat(String id, String sampleMimeType, String codecs, int bitrate, int maxInputSize, int width, int height, float frameRate, List<byte[]> initializationData, DrmInitData drmInitData) {
        return createVideoSampleFormat(id, sampleMimeType, codecs, bitrate, maxInputSize, width, height, frameRate, initializationData, -1, -1.0f, drmInitData);
    }

    public static Format createVideoSampleFormat(String id, String sampleMimeType, String codecs, int bitrate, int maxInputSize, int width, int height, float frameRate, List<byte[]> initializationData, int rotationDegrees, float pixelWidthHeightRatio, DrmInitData drmInitData) {
        return createVideoSampleFormat(id, sampleMimeType, codecs, bitrate, maxInputSize, width, height, frameRate, initializationData, rotationDegrees, pixelWidthHeightRatio, null, -1, null, drmInitData);
    }

    public static Format createVideoSampleFormat(String id, String sampleMimeType, String codecs, int bitrate, int maxInputSize, int width, int height, float frameRate, List<byte[]> initializationData, int rotationDegrees, float pixelWidthHeightRatio, byte[] projectionData, int stereoMode, ColorInfo colorInfo, DrmInitData drmInitData) {
        return new Format(id, null, sampleMimeType, codecs, bitrate, maxInputSize, width, height, frameRate, rotationDegrees, pixelWidthHeightRatio, projectionData, stereoMode, colorInfo, -1, -1, -1, -1, -1, 0, null, -1, Long.MAX_VALUE, initializationData, drmInitData, null);
    }

    public static Format createAudioContainerFormat(String id, String containerMimeType, String sampleMimeType, String codecs, int bitrate, int channelCount, int sampleRate, List<byte[]> initializationData, int selectionFlags, String language) {
        return new Format(id, containerMimeType, sampleMimeType, codecs, bitrate, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, channelCount, sampleRate, -1, -1, -1, selectionFlags, language, -1, Long.MAX_VALUE, initializationData, null, null);
    }

    public static Format createAudioSampleFormat(String id, String sampleMimeType, String codecs, int bitrate, int maxInputSize, int channelCount, int sampleRate, List<byte[]> initializationData, DrmInitData drmInitData, int selectionFlags, String language) {
        return createAudioSampleFormat(id, sampleMimeType, codecs, bitrate, maxInputSize, channelCount, sampleRate, -1, initializationData, drmInitData, selectionFlags, language);
    }

    public static Format createAudioSampleFormat(String id, String sampleMimeType, String codecs, int bitrate, int maxInputSize, int channelCount, int sampleRate, int pcmEncoding, List<byte[]> initializationData, DrmInitData drmInitData, int selectionFlags, String language) {
        return createAudioSampleFormat(id, sampleMimeType, codecs, bitrate, maxInputSize, channelCount, sampleRate, pcmEncoding, -1, -1, initializationData, drmInitData, selectionFlags, language, null);
    }

    public static Format createAudioSampleFormat(String id, String sampleMimeType, String codecs, int bitrate, int maxInputSize, int channelCount, int sampleRate, int pcmEncoding, int encoderDelay, int encoderPadding, List<byte[]> initializationData, DrmInitData drmInitData, int selectionFlags, String language, Metadata metadata) {
        return new Format(id, null, sampleMimeType, codecs, bitrate, maxInputSize, -1, -1, -1.0f, -1, -1.0f, null, -1, null, channelCount, sampleRate, pcmEncoding, encoderDelay, encoderPadding, selectionFlags, language, -1, Long.MAX_VALUE, initializationData, drmInitData, metadata);
    }

    public static Format createTextContainerFormat(String id, String containerMimeType, String sampleMimeType, String codecs, int bitrate, int selectionFlags, String language) {
        return createTextContainerFormat(id, containerMimeType, sampleMimeType, codecs, bitrate, selectionFlags, language, -1);
    }

    public static Format createTextContainerFormat(String id, String containerMimeType, String sampleMimeType, String codecs, int bitrate, int selectionFlags, String language, int accessibilityChannel) {
        return new Format(id, containerMimeType, sampleMimeType, codecs, bitrate, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, selectionFlags, language, accessibilityChannel, Long.MAX_VALUE, null, null, null);
    }

    public static Format createTextSampleFormat(String id, String sampleMimeType, String codecs, int bitrate, int selectionFlags, String language, DrmInitData drmInitData) {
        return createTextSampleFormat(id, sampleMimeType, codecs, bitrate, selectionFlags, language, -1, drmInitData, Long.MAX_VALUE, Collections.emptyList());
    }

    public static Format createTextSampleFormat(String id, String sampleMimeType, String codecs, int bitrate, int selectionFlags, String language, int accessibilityChannel, DrmInitData drmInitData) {
        return createTextSampleFormat(id, sampleMimeType, codecs, bitrate, selectionFlags, language, accessibilityChannel, drmInitData, Long.MAX_VALUE, Collections.emptyList());
    }

    public static Format createTextSampleFormat(String id, String sampleMimeType, String codecs, int bitrate, int selectionFlags, String language, DrmInitData drmInitData, long subsampleOffsetUs) {
        return createTextSampleFormat(id, sampleMimeType, codecs, bitrate, selectionFlags, language, -1, drmInitData, subsampleOffsetUs, Collections.emptyList());
    }

    public static Format createTextSampleFormat(String id, String sampleMimeType, String codecs, int bitrate, int selectionFlags, String language, int accessibilityChannel, DrmInitData drmInitData, long subsampleOffsetUs, List<byte[]> initializationData) {
        return new Format(id, null, sampleMimeType, codecs, bitrate, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, selectionFlags, language, accessibilityChannel, subsampleOffsetUs, initializationData, drmInitData, null);
    }

    public static Format createImageSampleFormat(String id, String sampleMimeType, String codecs, int bitrate, List<byte[]> initializationData, String language, DrmInitData drmInitData) {
        return new Format(id, null, sampleMimeType, codecs, bitrate, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, 0, language, -1, Long.MAX_VALUE, initializationData, drmInitData, null);
    }

    public static Format createContainerFormat(String id, String containerMimeType, String sampleMimeType, String codecs, int bitrate, int selectionFlags, String language) {
        return new Format(id, containerMimeType, sampleMimeType, codecs, bitrate, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, selectionFlags, language, -1, Long.MAX_VALUE, null, null, null);
    }

    public static Format createSampleFormat(String id, String sampleMimeType, long subsampleOffsetUs) {
        return new Format(id, null, sampleMimeType, null, -1, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, 0, null, -1, subsampleOffsetUs, null, null, null);
    }

    public static Format createSampleFormat(String id, String sampleMimeType, String codecs, int bitrate, DrmInitData drmInitData) {
        return new Format(id, null, sampleMimeType, codecs, bitrate, -1, -1, -1, -1.0f, -1, -1.0f, null, -1, null, -1, -1, -1, -1, -1, 0, null, -1, Long.MAX_VALUE, null, drmInitData, null);
    }

    Format(String id, String containerMimeType, String sampleMimeType, String codecs, int bitrate, int maxInputSize, int width, int height, float frameRate, int rotationDegrees, float pixelWidthHeightRatio, byte[] projectionData, int stereoMode, ColorInfo colorInfo, int channelCount, int sampleRate, int pcmEncoding, int encoderDelay, int encoderPadding, int selectionFlags, String language, int accessibilityChannel, long subsampleOffsetUs, List<byte[]> initializationData, DrmInitData drmInitData, Metadata metadata) {
        this.id = id;
        this.containerMimeType = containerMimeType;
        this.sampleMimeType = sampleMimeType;
        this.codecs = codecs;
        this.bitrate = bitrate;
        this.maxInputSize = maxInputSize;
        this.width = width;
        this.height = height;
        this.frameRate = frameRate;
        this.rotationDegrees = rotationDegrees;
        this.pixelWidthHeightRatio = pixelWidthHeightRatio;
        this.projectionData = projectionData;
        this.stereoMode = stereoMode;
        this.colorInfo = colorInfo;
        this.channelCount = channelCount;
        this.sampleRate = sampleRate;
        this.pcmEncoding = pcmEncoding;
        this.encoderDelay = encoderDelay;
        this.encoderPadding = encoderPadding;
        this.selectionFlags = selectionFlags;
        this.language = language;
        this.accessibilityChannel = accessibilityChannel;
        this.subsampleOffsetUs = subsampleOffsetUs;
        this.initializationData = initializationData == null ? Collections.emptyList() : initializationData;
        this.drmInitData = drmInitData;
        this.metadata = metadata;
    }

    Format(Parcel in) {
        this.id = in.readString();
        this.containerMimeType = in.readString();
        this.sampleMimeType = in.readString();
        this.codecs = in.readString();
        this.bitrate = in.readInt();
        this.maxInputSize = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
        this.frameRate = in.readFloat();
        this.rotationDegrees = in.readInt();
        this.pixelWidthHeightRatio = in.readFloat();
        boolean hasProjectionData = in.readInt() != 0;
        this.projectionData = hasProjectionData ? in.createByteArray() : null;
        this.stereoMode = in.readInt();
        this.colorInfo = (ColorInfo) in.readParcelable(ColorInfo.class.getClassLoader());
        this.channelCount = in.readInt();
        this.sampleRate = in.readInt();
        this.pcmEncoding = in.readInt();
        this.encoderDelay = in.readInt();
        this.encoderPadding = in.readInt();
        this.selectionFlags = in.readInt();
        this.language = in.readString();
        this.accessibilityChannel = in.readInt();
        this.subsampleOffsetUs = in.readLong();
        int initializationDataSize = in.readInt();
        this.initializationData = new ArrayList(initializationDataSize);
        for (int i = 0; i < initializationDataSize; i++) {
            this.initializationData.add(in.createByteArray());
        }
        this.drmInitData = (DrmInitData) in.readParcelable(DrmInitData.class.getClassLoader());
        this.metadata = (Metadata) in.readParcelable(Metadata.class.getClassLoader());
    }

    public Format copyWithMaxInputSize(int maxInputSize) {
        return new Format(this.id, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }

    public Format copyWithSubsampleOffsetUs(long subsampleOffsetUs) {
        return new Format(this.id, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }

    public Format copyWithContainerInfo(String id, String codecs, int bitrate, int width, int height, int selectionFlags, String language) {
        return new Format(id, this.containerMimeType, this.sampleMimeType, codecs, bitrate, this.maxInputSize, width, height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, selectionFlags, language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }

    public Format copyWithManifestFormatInfo(Format manifestFormat) {
        if (this != manifestFormat) {
            String id = manifestFormat.id;
            String codecs = this.codecs == null ? manifestFormat.codecs : this.codecs;
            int bitrate = this.bitrate == -1 ? manifestFormat.bitrate : this.bitrate;
            float frameRate = this.frameRate == -1.0f ? manifestFormat.frameRate : this.frameRate;
            int selectionFlags = this.selectionFlags | manifestFormat.selectionFlags;
            String language = this.language == null ? manifestFormat.language : this.language;
            DrmInitData drmInitData = manifestFormat.drmInitData != null ? manifestFormat.drmInitData : this.drmInitData;
            return new Format(id, this.containerMimeType, this.sampleMimeType, codecs, bitrate, this.maxInputSize, this.width, this.height, frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, selectionFlags, language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, drmInitData, this.metadata);
        }
        return this;
    }

    public Format copyWithGaplessInfo(int encoderDelay, int encoderPadding) {
        return new Format(this.id, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, encoderDelay, encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, this.metadata);
    }

    public Format copyWithDrmInitData(DrmInitData drmInitData) {
        return new Format(this.id, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, drmInitData, this.metadata);
    }

    public Format copyWithMetadata(Metadata metadata) {
        return new Format(this.id, this.containerMimeType, this.sampleMimeType, this.codecs, this.bitrate, this.maxInputSize, this.width, this.height, this.frameRate, this.rotationDegrees, this.pixelWidthHeightRatio, this.projectionData, this.stereoMode, this.colorInfo, this.channelCount, this.sampleRate, this.pcmEncoding, this.encoderDelay, this.encoderPadding, this.selectionFlags, this.language, this.accessibilityChannel, this.subsampleOffsetUs, this.initializationData, this.drmInitData, metadata);
    }

    public int getPixelCount() {
        if (this.width == -1 || this.height == -1) {
            return -1;
        }
        return this.width * this.height;
    }

    @SuppressLint({"InlinedApi"})
    @TargetApi(16)
    public final MediaFormat getFrameworkMediaFormatV16() {
        MediaFormat format = new MediaFormat();
        format.setString("mime", this.sampleMimeType);
        maybeSetStringV16(format, "language", this.language);
        maybeSetIntegerV16(format, "max-input-size", this.maxInputSize);
        maybeSetIntegerV16(format, "width", this.width);
        maybeSetIntegerV16(format, "height", this.height);
        maybeSetFloatV16(format, "frame-rate", this.frameRate);
        maybeSetIntegerV16(format, "rotation-degrees", this.rotationDegrees);
        maybeSetIntegerV16(format, "channel-count", this.channelCount);
        maybeSetIntegerV16(format, "sample-rate", this.sampleRate);
        maybeSetIntegerV16(format, "encoder-delay", this.encoderDelay);
        maybeSetIntegerV16(format, "encoder-padding", this.encoderPadding);
        for (int i = 0; i < this.initializationData.size(); i++) {
            format.setByteBuffer("csd-" + i, ByteBuffer.wrap(this.initializationData.get(i)));
        }
        maybeSetColorInfoV24(format, this.colorInfo);
        return format;
    }

    public String toString() {
        return "Format(" + this.id + ", " + this.containerMimeType + ", " + this.sampleMimeType + ", " + this.bitrate + ", " + this.language + ", [" + this.width + ", " + this.height + ", " + this.frameRate + "], [" + this.channelCount + ", " + this.sampleRate + "])";
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = (this.id == null ? 0 : this.id.hashCode()) + 527;
            this.hashCode = (((((((((((((((((((((((result * 31) + (this.containerMimeType == null ? 0 : this.containerMimeType.hashCode())) * 31) + (this.sampleMimeType == null ? 0 : this.sampleMimeType.hashCode())) * 31) + (this.codecs == null ? 0 : this.codecs.hashCode())) * 31) + this.bitrate) * 31) + this.width) * 31) + this.height) * 31) + this.channelCount) * 31) + this.sampleRate) * 31) + (this.language == null ? 0 : this.language.hashCode())) * 31) + this.accessibilityChannel) * 31) + (this.drmInitData == null ? 0 : this.drmInitData.hashCode())) * 31) + (this.metadata != null ? this.metadata.hashCode() : 0);
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Format other = (Format) obj;
        if (this.bitrate != other.bitrate || this.maxInputSize != other.maxInputSize || this.width != other.width || this.height != other.height || this.frameRate != other.frameRate || this.rotationDegrees != other.rotationDegrees || this.pixelWidthHeightRatio != other.pixelWidthHeightRatio || this.stereoMode != other.stereoMode || this.channelCount != other.channelCount || this.sampleRate != other.sampleRate || this.pcmEncoding != other.pcmEncoding || this.encoderDelay != other.encoderDelay || this.encoderPadding != other.encoderPadding || this.subsampleOffsetUs != other.subsampleOffsetUs || this.selectionFlags != other.selectionFlags || !Util.areEqual(this.id, other.id) || !Util.areEqual(this.language, other.language) || this.accessibilityChannel != other.accessibilityChannel || !Util.areEqual(this.containerMimeType, other.containerMimeType) || !Util.areEqual(this.sampleMimeType, other.sampleMimeType) || !Util.areEqual(this.codecs, other.codecs) || !Util.areEqual(this.drmInitData, other.drmInitData) || !Util.areEqual(this.metadata, other.metadata) || !Util.areEqual(this.colorInfo, other.colorInfo) || !Arrays.equals(this.projectionData, other.projectionData) || this.initializationData.size() != other.initializationData.size()) {
            return false;
        }
        for (int i = 0; i < this.initializationData.size(); i++) {
            if (!Arrays.equals(this.initializationData.get(i), other.initializationData.get(i))) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(24)
    private static void maybeSetColorInfoV24(MediaFormat format, ColorInfo colorInfo) {
        if (colorInfo != null) {
            maybeSetIntegerV16(format, "color-transfer", colorInfo.colorTransfer);
            maybeSetIntegerV16(format, "color-standard", colorInfo.colorSpace);
            maybeSetIntegerV16(format, "color-range", colorInfo.colorRange);
            maybeSetByteBufferV16(format, "hdr-static-info", colorInfo.hdrStaticInfo);
        }
    }

    @TargetApi(16)
    private static void maybeSetStringV16(MediaFormat format, String key, String value) {
        if (value != null) {
            format.setString(key, value);
        }
    }

    @TargetApi(16)
    private static void maybeSetIntegerV16(MediaFormat format, String key, int value) {
        if (value != -1) {
            format.setInteger(key, value);
        }
    }

    @TargetApi(16)
    private static void maybeSetFloatV16(MediaFormat format, String key, float value) {
        if (value != -1.0f) {
            format.setFloat(key, value);
        }
    }

    @TargetApi(16)
    private static void maybeSetByteBufferV16(MediaFormat format, String key, byte[] value) {
        if (value != null) {
            format.setByteBuffer(key, ByteBuffer.wrap(value));
        }
    }

    public static String toLogString(Format format) {
        if (format == null) {
            return JniUscClient.az;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("id=").append(format.id).append(", mimeType=").append(format.sampleMimeType);
        if (format.bitrate != -1) {
            builder.append(", bitrate=").append(format.bitrate);
        }
        if (format.width != -1 && format.height != -1) {
            builder.append(", res=").append(format.width).append("x").append(format.height);
        }
        if (format.frameRate != -1.0f) {
            builder.append(", fps=").append(format.frameRate);
        }
        if (format.channelCount != -1) {
            builder.append(", channels=").append(format.channelCount);
        }
        if (format.sampleRate != -1) {
            builder.append(", sample_rate=").append(format.sampleRate);
        }
        if (format.language != null) {
            builder.append(", language=").append(format.language);
        }
        return builder.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.containerMimeType);
        dest.writeString(this.sampleMimeType);
        dest.writeString(this.codecs);
        dest.writeInt(this.bitrate);
        dest.writeInt(this.maxInputSize);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeFloat(this.frameRate);
        dest.writeInt(this.rotationDegrees);
        dest.writeFloat(this.pixelWidthHeightRatio);
        dest.writeInt(this.projectionData != null ? 1 : 0);
        if (this.projectionData != null) {
            dest.writeByteArray(this.projectionData);
        }
        dest.writeInt(this.stereoMode);
        dest.writeParcelable(this.colorInfo, flags);
        dest.writeInt(this.channelCount);
        dest.writeInt(this.sampleRate);
        dest.writeInt(this.pcmEncoding);
        dest.writeInt(this.encoderDelay);
        dest.writeInt(this.encoderPadding);
        dest.writeInt(this.selectionFlags);
        dest.writeString(this.language);
        dest.writeInt(this.accessibilityChannel);
        dest.writeLong(this.subsampleOffsetUs);
        int initializationDataSize = this.initializationData.size();
        dest.writeInt(initializationDataSize);
        for (int i = 0; i < initializationDataSize; i++) {
            dest.writeByteArray(this.initializationData.get(i));
        }
        dest.writeParcelable(this.drmInitData, 0);
        dest.writeParcelable(this.metadata, 0);
    }
}
