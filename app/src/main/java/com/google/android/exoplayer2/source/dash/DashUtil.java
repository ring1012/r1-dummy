package com.google.android.exoplayer2.source.dash;

import android.net.Uri;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.mkv.MatroskaExtractor;
import com.google.android.exoplayer2.extractor.mp4.FragmentedMp4Extractor;
import com.google.android.exoplayer2.source.chunk.ChunkExtractorWrapper;
import com.google.android.exoplayer2.source.chunk.InitializationChunk;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.DashManifestParser;
import com.google.android.exoplayer2.source.dash.manifest.Period;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSourceInputStream;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/* loaded from: classes.dex */
public final class DashUtil {
    public static DashManifest loadManifest(DataSource dataSource, String manifestUri) throws IOException {
        DataSourceInputStream inputStream = new DataSourceInputStream(dataSource, new DataSpec(Uri.parse(manifestUri), 2));
        try {
            inputStream.open();
            DashManifestParser parser = new DashManifestParser();
            return parser.parse(dataSource.getUri(), (InputStream) inputStream);
        } finally {
            inputStream.close();
        }
    }

    public static DrmInitData loadDrmInitData(DataSource dataSource, DashManifest dashManifest) throws InterruptedException, IOException {
        if (dashManifest.getPeriodCount() < 1) {
            return null;
        }
        Period period = dashManifest.getPeriod(0);
        int adaptationSetIndex = period.getAdaptationSetIndex(2);
        if (adaptationSetIndex == -1 && (adaptationSetIndex = period.getAdaptationSetIndex(1)) == -1) {
            return null;
        }
        AdaptationSet adaptationSet = period.adaptationSets.get(adaptationSetIndex);
        if (adaptationSet.representations.isEmpty()) {
            return null;
        }
        Representation representation = adaptationSet.representations.get(0);
        DrmInitData drmInitData = representation.format.drmInitData;
        if (drmInitData == null) {
            Format sampleFormat = loadSampleFormat(dataSource, representation);
            if (sampleFormat != null) {
                drmInitData = sampleFormat.drmInitData;
            }
            if (drmInitData == null) {
                return null;
            }
            return drmInitData;
        }
        return drmInitData;
    }

    public static DrmInitData loadDrmInitData(DataSource dataSource, Period period) throws InterruptedException, IOException {
        Representation representation = getFirstRepresentation(period, 2);
        if (representation == null && (representation = getFirstRepresentation(period, 1)) == null) {
            return null;
        }
        DrmInitData drmInitData = representation.format.drmInitData;
        if (drmInitData != null) {
            return drmInitData;
        }
        Format sampleFormat = loadSampleFormat(dataSource, representation);
        if (sampleFormat != null) {
            return sampleFormat.drmInitData;
        }
        return null;
    }

    public static Format loadSampleFormat(DataSource dataSource, Representation representation) throws InterruptedException, IOException {
        ChunkExtractorWrapper extractorWrapper = loadInitializationData(dataSource, representation, false);
        if (extractorWrapper == null) {
            return null;
        }
        return extractorWrapper.getSampleFormats()[0];
    }

    public static ChunkIndex loadChunkIndex(DataSource dataSource, Representation representation) throws InterruptedException, IOException {
        ChunkExtractorWrapper extractorWrapper = loadInitializationData(dataSource, representation, true);
        if (extractorWrapper == null) {
            return null;
        }
        return (ChunkIndex) extractorWrapper.getSeekMap();
    }

    private static ChunkExtractorWrapper loadInitializationData(DataSource dataSource, Representation representation, boolean loadIndex) throws InterruptedException, IOException {
        RangedUri requestUri;
        RangedUri initializationUri = representation.getInitializationUri();
        if (initializationUri == null) {
            return null;
        }
        ChunkExtractorWrapper extractorWrapper = newWrappedExtractor(representation.format);
        if (loadIndex) {
            RangedUri indexUri = representation.getIndexUri();
            if (indexUri == null) {
                return null;
            }
            requestUri = initializationUri.attemptMerge(indexUri, representation.baseUrl);
            if (requestUri == null) {
                loadInitializationData(dataSource, representation, extractorWrapper, initializationUri);
                requestUri = indexUri;
            }
        } else {
            requestUri = initializationUri;
        }
        loadInitializationData(dataSource, representation, extractorWrapper, requestUri);
        return extractorWrapper;
    }

    private static void loadInitializationData(DataSource dataSource, Representation representation, ChunkExtractorWrapper extractorWrapper, RangedUri requestUri) throws InterruptedException, IOException {
        DataSpec dataSpec = new DataSpec(requestUri.resolveUri(representation.baseUrl), requestUri.start, requestUri.length, representation.getCacheKey());
        InitializationChunk initializationChunk = new InitializationChunk(dataSource, dataSpec, representation.format, 0, null, extractorWrapper);
        initializationChunk.load();
    }

    private static ChunkExtractorWrapper newWrappedExtractor(Format format) {
        String mimeType = format.containerMimeType;
        boolean isWebm = mimeType.startsWith(MimeTypes.VIDEO_WEBM) || mimeType.startsWith(MimeTypes.AUDIO_WEBM);
        Extractor extractor = isWebm ? new MatroskaExtractor() : new FragmentedMp4Extractor();
        return new ChunkExtractorWrapper(extractor, format);
    }

    private static Representation getFirstRepresentation(Period period, int type) {
        int index = period.getAdaptationSetIndex(type);
        if (index == -1) {
            return null;
        }
        List<Representation> representations = period.adaptationSets.get(index).representations;
        return representations.isEmpty() ? null : representations.get(0);
    }

    private DashUtil() {
    }
}
