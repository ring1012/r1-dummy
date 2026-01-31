package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.dash.DashSegmentIndex;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public abstract class Representation {
    public static final long REVISION_ID_DEFAULT = -1;
    public final String baseUrl;
    public final String contentId;
    public final Format format;
    public final List<SchemeValuePair> inbandEventStreams;
    private final RangedUri initializationUri;
    public final long presentationTimeOffsetUs;
    public final long revisionId;

    public abstract String getCacheKey();

    public abstract DashSegmentIndex getIndex();

    public abstract RangedUri getIndexUri();

    public static Representation newInstance(String contentId, long revisionId, Format format, String baseUrl, SegmentBase segmentBase) {
        return newInstance(contentId, revisionId, format, baseUrl, segmentBase, null);
    }

    public static Representation newInstance(String contentId, long revisionId, Format format, String baseUrl, SegmentBase segmentBase, List<SchemeValuePair> inbandEventStreams) {
        return newInstance(contentId, revisionId, format, baseUrl, segmentBase, inbandEventStreams, null);
    }

    public static Representation newInstance(String contentId, long revisionId, Format format, String baseUrl, SegmentBase segmentBase, List<SchemeValuePair> inbandEventStreams, String customCacheKey) {
        if (segmentBase instanceof SegmentBase.SingleSegmentBase) {
            return new SingleSegmentRepresentation(contentId, revisionId, format, baseUrl, (SegmentBase.SingleSegmentBase) segmentBase, inbandEventStreams, customCacheKey, -1L);
        }
        if (segmentBase instanceof SegmentBase.MultiSegmentBase) {
            return new MultiSegmentRepresentation(contentId, revisionId, format, baseUrl, (SegmentBase.MultiSegmentBase) segmentBase, inbandEventStreams);
        }
        throw new IllegalArgumentException("segmentBase must be of type SingleSegmentBase or MultiSegmentBase");
    }

    private Representation(String contentId, long revisionId, Format format, String baseUrl, SegmentBase segmentBase, List<SchemeValuePair> inbandEventStreams) {
        List<SchemeValuePair> listUnmodifiableList;
        this.contentId = contentId;
        this.revisionId = revisionId;
        this.format = format;
        this.baseUrl = baseUrl;
        if (inbandEventStreams == null) {
            listUnmodifiableList = Collections.emptyList();
        } else {
            listUnmodifiableList = Collections.unmodifiableList(inbandEventStreams);
        }
        this.inbandEventStreams = listUnmodifiableList;
        this.initializationUri = segmentBase.getInitialization(this);
        this.presentationTimeOffsetUs = segmentBase.getPresentationTimeOffsetUs();
    }

    public RangedUri getInitializationUri() {
        return this.initializationUri;
    }

    public static class SingleSegmentRepresentation extends Representation {
        private final String cacheKey;
        public final long contentLength;
        private final RangedUri indexUri;
        private final SingleSegmentIndex segmentIndex;
        public final Uri uri;

        public static SingleSegmentRepresentation newInstance(String contentId, long revisionId, Format format, String uri, long initializationStart, long initializationEnd, long indexStart, long indexEnd, List<SchemeValuePair> inbandEventStreams, String customCacheKey, long contentLength) {
            RangedUri rangedUri = new RangedUri(null, initializationStart, (initializationEnd - initializationStart) + 1);
            SegmentBase.SingleSegmentBase segmentBase = new SegmentBase.SingleSegmentBase(rangedUri, 1L, 0L, indexStart, 1 + (indexEnd - indexStart));
            return new SingleSegmentRepresentation(contentId, revisionId, format, uri, segmentBase, inbandEventStreams, customCacheKey, contentLength);
        }

        public SingleSegmentRepresentation(String contentId, long revisionId, Format format, String baseUrl, SegmentBase.SingleSegmentBase segmentBase, List<SchemeValuePair> inbandEventStreams, String customCacheKey, long contentLength) {
            super(contentId, revisionId, format, baseUrl, segmentBase, inbandEventStreams);
            this.uri = Uri.parse(baseUrl);
            this.indexUri = segmentBase.getIndex();
            this.cacheKey = customCacheKey == null ? contentId != null ? contentId + "." + format.id + "." + revisionId : null : customCacheKey;
            this.contentLength = contentLength;
            this.segmentIndex = this.indexUri != null ? null : new SingleSegmentIndex(new RangedUri(null, 0L, contentLength));
        }

        @Override // com.google.android.exoplayer2.source.dash.manifest.Representation
        public RangedUri getIndexUri() {
            return this.indexUri;
        }

        @Override // com.google.android.exoplayer2.source.dash.manifest.Representation
        public DashSegmentIndex getIndex() {
            return this.segmentIndex;
        }

        @Override // com.google.android.exoplayer2.source.dash.manifest.Representation
        public String getCacheKey() {
            return this.cacheKey;
        }
    }

    public static class MultiSegmentRepresentation extends Representation implements DashSegmentIndex {
        private final SegmentBase.MultiSegmentBase segmentBase;

        public MultiSegmentRepresentation(String contentId, long revisionId, Format format, String baseUrl, SegmentBase.MultiSegmentBase segmentBase, List<SchemeValuePair> inbandEventStreams) {
            super(contentId, revisionId, format, baseUrl, segmentBase, inbandEventStreams);
            this.segmentBase = segmentBase;
        }

        @Override // com.google.android.exoplayer2.source.dash.manifest.Representation
        public RangedUri getIndexUri() {
            return null;
        }

        @Override // com.google.android.exoplayer2.source.dash.manifest.Representation
        public DashSegmentIndex getIndex() {
            return this;
        }

        @Override // com.google.android.exoplayer2.source.dash.manifest.Representation
        public String getCacheKey() {
            return null;
        }

        @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
        public RangedUri getSegmentUrl(int segmentIndex) {
            return this.segmentBase.getSegmentUrl(this, segmentIndex);
        }

        @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
        public int getSegmentNum(long timeUs, long periodDurationUs) {
            return this.segmentBase.getSegmentNum(timeUs, periodDurationUs);
        }

        @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
        public long getTimeUs(int segmentIndex) {
            return this.segmentBase.getSegmentTimeUs(segmentIndex);
        }

        @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
        public long getDurationUs(int segmentIndex, long periodDurationUs) {
            return this.segmentBase.getSegmentDurationUs(segmentIndex, periodDurationUs);
        }

        @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
        public int getFirstSegmentNum() {
            return this.segmentBase.getFirstSegmentNum();
        }

        @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
        public int getSegmentCount(long periodDurationUs) {
            return this.segmentBase.getSegmentCount(periodDurationUs);
        }

        @Override // com.google.android.exoplayer2.source.dash.DashSegmentIndex
        public boolean isExplicit() {
            return this.segmentBase.isExplicit();
        }
    }
}
