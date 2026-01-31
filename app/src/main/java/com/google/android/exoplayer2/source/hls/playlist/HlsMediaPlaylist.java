package com.google.android.exoplayer2.source.hls.playlist;

import android.support.annotation.NonNull;
import com.google.android.exoplayer2.C;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class HlsMediaPlaylist extends HlsPlaylist {
    public static final int PLAYLIST_TYPE_EVENT = 2;
    public static final int PLAYLIST_TYPE_UNKNOWN = 0;
    public static final int PLAYLIST_TYPE_VOD = 1;
    public final int discontinuitySequence;
    public final long durationUs;
    public final boolean hasDiscontinuitySequence;
    public final boolean hasEndTag;
    public final boolean hasProgramDateTime;
    public final Segment initializationSegment;
    public final int mediaSequence;
    public final int playlistType;
    public final List<Segment> segments;
    public final long startOffsetUs;
    public final long startTimeUs;
    public final long targetDurationUs;
    public final int version;

    @Retention(RetentionPolicy.SOURCE)
    public @interface PlaylistType {
    }

    public static final class Segment implements Comparable<Long> {
        public final long byterangeLength;
        public final long byterangeOffset;
        public final long durationUs;
        public final String encryptionIV;
        public final String encryptionKeyUri;
        public final boolean isEncrypted;
        public final int relativeDiscontinuitySequence;
        public final long relativeStartTimeUs;
        public final String url;

        public Segment(String uri, long byterangeOffset, long byterangeLength) {
            this(uri, 0L, -1, C.TIME_UNSET, false, null, null, byterangeOffset, byterangeLength);
        }

        public Segment(String uri, long durationUs, int relativeDiscontinuitySequence, long relativeStartTimeUs, boolean isEncrypted, String encryptionKeyUri, String encryptionIV, long byterangeOffset, long byterangeLength) {
            this.url = uri;
            this.durationUs = durationUs;
            this.relativeDiscontinuitySequence = relativeDiscontinuitySequence;
            this.relativeStartTimeUs = relativeStartTimeUs;
            this.isEncrypted = isEncrypted;
            this.encryptionKeyUri = encryptionKeyUri;
            this.encryptionIV = encryptionIV;
            this.byterangeOffset = byterangeOffset;
            this.byterangeLength = byterangeLength;
        }

        @Override // java.lang.Comparable
        public int compareTo(@NonNull Long relativeStartTimeUs) {
            if (this.relativeStartTimeUs > relativeStartTimeUs.longValue()) {
                return 1;
            }
            return this.relativeStartTimeUs < relativeStartTimeUs.longValue() ? -1 : 0;
        }
    }

    public HlsMediaPlaylist(int playlistType, String baseUri, long startOffsetUs, long startTimeUs, boolean hasDiscontinuitySequence, int discontinuitySequence, int mediaSequence, int version, long targetDurationUs, boolean hasEndTag, boolean hasProgramDateTime, Segment initializationSegment, List<Segment> segments) {
        super(baseUri);
        this.playlistType = playlistType;
        this.startTimeUs = startTimeUs;
        this.hasDiscontinuitySequence = hasDiscontinuitySequence;
        this.discontinuitySequence = discontinuitySequence;
        this.mediaSequence = mediaSequence;
        this.version = version;
        this.targetDurationUs = targetDurationUs;
        this.hasEndTag = hasEndTag;
        this.hasProgramDateTime = hasProgramDateTime;
        this.initializationSegment = initializationSegment;
        this.segments = Collections.unmodifiableList(segments);
        if (!segments.isEmpty()) {
            Segment last = segments.get(segments.size() - 1);
            this.durationUs = last.relativeStartTimeUs + last.durationUs;
        } else {
            this.durationUs = 0L;
        }
        if (startOffsetUs == C.TIME_UNSET) {
            startOffsetUs = C.TIME_UNSET;
        } else if (startOffsetUs < 0) {
            startOffsetUs += this.durationUs;
        }
        this.startOffsetUs = startOffsetUs;
    }

    public boolean isNewerThan(HlsMediaPlaylist other) {
        if (other == null || this.mediaSequence > other.mediaSequence) {
            return true;
        }
        if (this.mediaSequence < other.mediaSequence) {
            return false;
        }
        int segmentCount = this.segments.size();
        int otherSegmentCount = other.segments.size();
        return segmentCount > otherSegmentCount || (segmentCount == otherSegmentCount && this.hasEndTag && !other.hasEndTag);
    }

    public long getEndTimeUs() {
        return this.startTimeUs + this.durationUs;
    }

    public HlsMediaPlaylist copyWith(long startTimeUs, int discontinuitySequence) {
        return new HlsMediaPlaylist(this.playlistType, this.baseUri, this.startOffsetUs, startTimeUs, true, discontinuitySequence, this.mediaSequence, this.version, this.targetDurationUs, this.hasEndTag, this.hasProgramDateTime, this.initializationSegment, this.segments);
    }

    public HlsMediaPlaylist copyWithEndTag() {
        return this.hasEndTag ? this : new HlsMediaPlaylist(this.playlistType, this.baseUri, this.startOffsetUs, this.startTimeUs, this.hasDiscontinuitySequence, this.discontinuitySequence, this.mediaSequence, this.version, this.targetDurationUs, true, this.hasProgramDateTime, this.initializationSegment, this.segments);
    }
}
