package com.google.android.exoplayer2.source.smoothstreaming.manifest;

import android.net.Uri;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import java.util.List;
import java.util.UUID;

/* loaded from: classes.dex */
public class SsManifest {
    public static final int UNSET_LOOKAHEAD = -1;
    public final long durationUs;
    public final long dvrWindowLengthUs;
    public final boolean isLive;
    public final int lookAheadCount;
    public final int majorVersion;
    public final int minorVersion;
    public final ProtectionElement protectionElement;
    public final StreamElement[] streamElements;

    public SsManifest(int majorVersion, int minorVersion, long timescale, long duration, long dvrWindowLength, int lookAheadCount, boolean isLive, ProtectionElement protectionElement, StreamElement[] streamElements) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.lookAheadCount = lookAheadCount;
        this.isLive = isLive;
        this.protectionElement = protectionElement;
        this.streamElements = streamElements;
        this.dvrWindowLengthUs = dvrWindowLength == 0 ? C.TIME_UNSET : Util.scaleLargeTimestamp(dvrWindowLength, C.MICROS_PER_SECOND, timescale);
        this.durationUs = duration == 0 ? C.TIME_UNSET : Util.scaleLargeTimestamp(duration, C.MICROS_PER_SECOND, timescale);
    }

    public static class ProtectionElement {
        public final byte[] data;
        public final UUID uuid;

        public ProtectionElement(UUID uuid, byte[] data) {
            this.uuid = uuid;
            this.data = data;
        }
    }

    public static class StreamElement {
        private static final String URL_PLACEHOLDER_BITRATE_1 = "{bitrate}";
        private static final String URL_PLACEHOLDER_BITRATE_2 = "{Bitrate}";
        private static final String URL_PLACEHOLDER_START_TIME_1 = "{start time}";
        private static final String URL_PLACEHOLDER_START_TIME_2 = "{start_time}";
        private final String baseUri;
        public final int chunkCount;
        private final List<Long> chunkStartTimes;
        private final long[] chunkStartTimesUs;
        private final String chunkTemplate;
        public final int displayHeight;
        public final int displayWidth;
        public final Format[] formats;
        public final String language;
        private final long lastChunkDurationUs;
        public final int maxHeight;
        public final int maxWidth;
        public final String name;
        public final String subType;
        public final long timescale;
        public final int type;

        public StreamElement(String baseUri, String chunkTemplate, int type, String subType, long timescale, String name, int maxWidth, int maxHeight, int displayWidth, int displayHeight, String language, Format[] formats, List<Long> chunkStartTimes, long lastChunkDuration) {
            this.baseUri = baseUri;
            this.chunkTemplate = chunkTemplate;
            this.type = type;
            this.subType = subType;
            this.timescale = timescale;
            this.name = name;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
            this.displayWidth = displayWidth;
            this.displayHeight = displayHeight;
            this.language = language;
            this.formats = formats;
            this.chunkCount = chunkStartTimes.size();
            this.chunkStartTimes = chunkStartTimes;
            this.lastChunkDurationUs = Util.scaleLargeTimestamp(lastChunkDuration, C.MICROS_PER_SECOND, timescale);
            this.chunkStartTimesUs = Util.scaleLargeTimestamps(chunkStartTimes, C.MICROS_PER_SECOND, timescale);
        }

        public int getChunkIndex(long timeUs) {
            return Util.binarySearchFloor(this.chunkStartTimesUs, timeUs, true, true);
        }

        public long getStartTimeUs(int chunkIndex) {
            return this.chunkStartTimesUs[chunkIndex];
        }

        public long getChunkDurationUs(int chunkIndex) {
            return chunkIndex == this.chunkCount + (-1) ? this.lastChunkDurationUs : this.chunkStartTimesUs[chunkIndex + 1] - this.chunkStartTimesUs[chunkIndex];
        }

        public Uri buildRequestUri(int track, int chunkIndex) {
            Assertions.checkState(this.formats != null);
            Assertions.checkState(this.chunkStartTimes != null);
            Assertions.checkState(chunkIndex < this.chunkStartTimes.size());
            String bitrateString = Integer.toString(this.formats[track].bitrate);
            String startTimeString = this.chunkStartTimes.get(chunkIndex).toString();
            String chunkUrl = this.chunkTemplate.replace(URL_PLACEHOLDER_BITRATE_1, bitrateString).replace(URL_PLACEHOLDER_BITRATE_2, bitrateString).replace(URL_PLACEHOLDER_START_TIME_1, startTimeString).replace(URL_PLACEHOLDER_START_TIME_2, startTimeString);
            return UriUtil.resolveToUri(this.baseUri, chunkUrl);
        }
    }
}
