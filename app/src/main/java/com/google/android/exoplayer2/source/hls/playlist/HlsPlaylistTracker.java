package com.google.android.exoplayer2.source.hls.playlist;

import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import com.google.android.exoplayer2.source.hls.HlsDataSourceFactory;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import com.google.android.exoplayer2.util.UriUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

/* loaded from: classes.dex */
public final class HlsPlaylistTracker implements Loader.Callback<ParsingLoadable<HlsPlaylist>> {
    private static final long PRIMARY_URL_KEEPALIVE_MS = 15000;
    private final HlsDataSourceFactory dataSourceFactory;
    private final AdaptiveMediaSourceEventListener.EventDispatcher eventDispatcher;
    private final Uri initialPlaylistUri;
    private boolean isLive;
    private HlsMasterPlaylist masterPlaylist;
    private final int minRetryCount;
    private HlsMasterPlaylist.HlsUrl primaryHlsUrl;
    private final PrimaryPlaylistListener primaryPlaylistListener;
    private HlsMediaPlaylist primaryUrlSnapshot;
    private final List<PlaylistEventListener> listeners = new ArrayList();
    private final Loader initialPlaylistLoader = new Loader("HlsPlaylistTracker:MasterPlaylist");
    private final HlsPlaylistParser playlistParser = new HlsPlaylistParser();
    private final IdentityHashMap<HlsMasterPlaylist.HlsUrl, MediaPlaylistBundle> playlistBundles = new IdentityHashMap<>();
    private final Handler playlistRefreshHandler = new Handler();

    public interface PlaylistEventListener {
        void onPlaylistBlacklisted(HlsMasterPlaylist.HlsUrl hlsUrl, long j);

        void onPlaylistChanged();
    }

    public interface PrimaryPlaylistListener {
        void onPrimaryPlaylistRefreshed(HlsMediaPlaylist hlsMediaPlaylist);
    }

    public HlsPlaylistTracker(Uri initialPlaylistUri, HlsDataSourceFactory dataSourceFactory, AdaptiveMediaSourceEventListener.EventDispatcher eventDispatcher, int minRetryCount, PrimaryPlaylistListener primaryPlaylistListener) {
        this.initialPlaylistUri = initialPlaylistUri;
        this.dataSourceFactory = dataSourceFactory;
        this.eventDispatcher = eventDispatcher;
        this.minRetryCount = minRetryCount;
        this.primaryPlaylistListener = primaryPlaylistListener;
    }

    public void addListener(PlaylistEventListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(PlaylistEventListener listener) {
        this.listeners.remove(listener);
    }

    public void start() {
        ParsingLoadable<HlsPlaylist> masterPlaylistLoadable = new ParsingLoadable<>(this.dataSourceFactory.createDataSource(4), this.initialPlaylistUri, 4, this.playlistParser);
        this.initialPlaylistLoader.startLoading(masterPlaylistLoadable, this, this.minRetryCount);
    }

    public HlsMasterPlaylist getMasterPlaylist() {
        return this.masterPlaylist;
    }

    public HlsMediaPlaylist getPlaylistSnapshot(HlsMasterPlaylist.HlsUrl url) {
        HlsMediaPlaylist snapshot = this.playlistBundles.get(url).getPlaylistSnapshot();
        if (snapshot != null) {
            maybeSetPrimaryUrl(url);
        }
        return snapshot;
    }

    public boolean isSnapshotValid(HlsMasterPlaylist.HlsUrl url) {
        return this.playlistBundles.get(url).isSnapshotValid();
    }

    public void release() {
        this.initialPlaylistLoader.release();
        for (MediaPlaylistBundle bundle : this.playlistBundles.values()) {
            bundle.release();
        }
        this.playlistRefreshHandler.removeCallbacksAndMessages(null);
        this.playlistBundles.clear();
    }

    public void maybeThrowPlaylistRefreshError() throws IOException {
        this.initialPlaylistLoader.maybeThrowError();
        if (this.primaryHlsUrl == null) {
            return;
        }
        this.playlistBundles.get(this.primaryHlsUrl).mediaPlaylistLoader.maybeThrowError();
    }

    public void refreshPlaylist(HlsMasterPlaylist.HlsUrl url) {
        this.playlistBundles.get(url).loadPlaylist();
    }

    public boolean isLive() {
        return this.isLive;
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCompleted(ParsingLoadable<HlsPlaylist> loadable, long elapsedRealtimeMs, long loadDurationMs) {
        HlsMasterPlaylist masterPlaylist;
        HlsPlaylist result = loadable.getResult();
        boolean isMediaPlaylist = result instanceof HlsMediaPlaylist;
        if (isMediaPlaylist) {
            masterPlaylist = HlsMasterPlaylist.createSingleVariantMasterPlaylist(result.baseUri);
        } else {
            masterPlaylist = (HlsMasterPlaylist) result;
        }
        this.masterPlaylist = masterPlaylist;
        this.primaryHlsUrl = masterPlaylist.variants.get(0);
        ArrayList<HlsMasterPlaylist.HlsUrl> urls = new ArrayList<>();
        urls.addAll(masterPlaylist.variants);
        urls.addAll(masterPlaylist.audios);
        urls.addAll(masterPlaylist.subtitles);
        createBundles(urls);
        MediaPlaylistBundle primaryBundle = this.playlistBundles.get(this.primaryHlsUrl);
        if (!isMediaPlaylist) {
            primaryBundle.loadPlaylist();
        } else {
            primaryBundle.processLoadedPlaylist((HlsMediaPlaylist) result);
        }
        this.eventDispatcher.loadCompleted(loadable.dataSpec, 4, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public void onLoadCanceled(ParsingLoadable<HlsPlaylist> loadable, long elapsedRealtimeMs, long loadDurationMs, boolean released) {
        this.eventDispatcher.loadCanceled(loadable.dataSpec, 4, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.Callback
    public int onLoadError(ParsingLoadable<HlsPlaylist> loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
        boolean isFatal = error instanceof ParserException;
        this.eventDispatcher.loadError(loadable.dataSpec, 4, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded(), error, isFatal);
        return isFatal ? 3 : 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean maybeSelectNewPrimaryUrl() {
        List<HlsMasterPlaylist.HlsUrl> variants = this.masterPlaylist.variants;
        int variantsSize = variants.size();
        long currentTimeMs = SystemClock.elapsedRealtime();
        for (int i = 0; i < variantsSize; i++) {
            MediaPlaylistBundle bundle = this.playlistBundles.get(variants.get(i));
            if (currentTimeMs > bundle.blacklistUntilMs) {
                this.primaryHlsUrl = bundle.playlistUrl;
                bundle.loadPlaylist();
                return true;
            }
        }
        return false;
    }

    private void maybeSetPrimaryUrl(HlsMasterPlaylist.HlsUrl url) {
        if (this.masterPlaylist.variants.contains(url)) {
            if (this.primaryUrlSnapshot == null || !this.primaryUrlSnapshot.hasEndTag) {
                MediaPlaylistBundle currentPrimaryBundle = this.playlistBundles.get(this.primaryHlsUrl);
                long primarySnapshotAccessAgeMs = currentPrimaryBundle.lastSnapshotAccessTimeMs - SystemClock.elapsedRealtime();
                if (primarySnapshotAccessAgeMs > PRIMARY_URL_KEEPALIVE_MS) {
                    this.primaryHlsUrl = url;
                    this.playlistBundles.get(this.primaryHlsUrl).loadPlaylist();
                }
            }
        }
    }

    private void createBundles(List<HlsMasterPlaylist.HlsUrl> urls) {
        int listSize = urls.size();
        long currentTimeMs = SystemClock.elapsedRealtime();
        for (int i = 0; i < listSize; i++) {
            HlsMasterPlaylist.HlsUrl url = urls.get(i);
            MediaPlaylistBundle bundle = new MediaPlaylistBundle(url, currentTimeMs);
            this.playlistBundles.put(url, bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean onPlaylistUpdated(HlsMasterPlaylist.HlsUrl url, HlsMediaPlaylist newSnapshot) {
        if (url == this.primaryHlsUrl) {
            if (this.primaryUrlSnapshot == null) {
                this.isLive = !newSnapshot.hasEndTag;
            }
            this.primaryUrlSnapshot = newSnapshot;
            this.primaryPlaylistListener.onPrimaryPlaylistRefreshed(newSnapshot);
        }
        int listenersSize = this.listeners.size();
        for (int i = 0; i < listenersSize; i++) {
            this.listeners.get(i).onPlaylistChanged();
        }
        return url == this.primaryHlsUrl && !newSnapshot.hasEndTag;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPlaylistBlacklisting(HlsMasterPlaylist.HlsUrl url, long blacklistMs) {
        int listenersSize = this.listeners.size();
        for (int i = 0; i < listenersSize; i++) {
            this.listeners.get(i).onPlaylistBlacklisted(url, blacklistMs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public HlsMediaPlaylist getLatestPlaylistSnapshot(HlsMediaPlaylist oldPlaylist, HlsMediaPlaylist loadedPlaylist) {
        if (!loadedPlaylist.isNewerThan(oldPlaylist)) {
            if (loadedPlaylist.hasEndTag) {
                return oldPlaylist.copyWithEndTag();
            }
            return oldPlaylist;
        }
        long startTimeUs = getLoadedPlaylistStartTimeUs(oldPlaylist, loadedPlaylist);
        int discontinuitySequence = getLoadedPlaylistDiscontinuitySequence(oldPlaylist, loadedPlaylist);
        return loadedPlaylist.copyWith(startTimeUs, discontinuitySequence);
    }

    private long getLoadedPlaylistStartTimeUs(HlsMediaPlaylist oldPlaylist, HlsMediaPlaylist loadedPlaylist) {
        if (loadedPlaylist.hasProgramDateTime) {
            return loadedPlaylist.startTimeUs;
        }
        long primarySnapshotStartTimeUs = this.primaryUrlSnapshot != null ? this.primaryUrlSnapshot.startTimeUs : 0L;
        if (oldPlaylist != null) {
            int oldPlaylistSize = oldPlaylist.segments.size();
            HlsMediaPlaylist.Segment firstOldOverlappingSegment = getFirstOldOverlappingSegment(oldPlaylist, loadedPlaylist);
            if (firstOldOverlappingSegment != null) {
                return oldPlaylist.startTimeUs + firstOldOverlappingSegment.relativeStartTimeUs;
            }
            if (oldPlaylistSize == loadedPlaylist.mediaSequence - oldPlaylist.mediaSequence) {
                return oldPlaylist.getEndTimeUs();
            }
            return primarySnapshotStartTimeUs;
        }
        return primarySnapshotStartTimeUs;
    }

    private int getLoadedPlaylistDiscontinuitySequence(HlsMediaPlaylist oldPlaylist, HlsMediaPlaylist loadedPlaylist) {
        HlsMediaPlaylist.Segment firstOldOverlappingSegment;
        if (loadedPlaylist.hasDiscontinuitySequence) {
            return loadedPlaylist.discontinuitySequence;
        }
        int primaryUrlDiscontinuitySequence = this.primaryUrlSnapshot != null ? this.primaryUrlSnapshot.discontinuitySequence : 0;
        if (oldPlaylist != null && (firstOldOverlappingSegment = getFirstOldOverlappingSegment(oldPlaylist, loadedPlaylist)) != null) {
            return (oldPlaylist.discontinuitySequence + firstOldOverlappingSegment.relativeDiscontinuitySequence) - loadedPlaylist.segments.get(0).relativeDiscontinuitySequence;
        }
        return primaryUrlDiscontinuitySequence;
    }

    private static HlsMediaPlaylist.Segment getFirstOldOverlappingSegment(HlsMediaPlaylist oldPlaylist, HlsMediaPlaylist loadedPlaylist) {
        int mediaSequenceOffset = loadedPlaylist.mediaSequence - oldPlaylist.mediaSequence;
        List<HlsMediaPlaylist.Segment> oldSegments = oldPlaylist.segments;
        if (mediaSequenceOffset < oldSegments.size()) {
            return oldSegments.get(mediaSequenceOffset);
        }
        return null;
    }

    private final class MediaPlaylistBundle implements Loader.Callback<ParsingLoadable<HlsPlaylist>>, Runnable {
        private long blacklistUntilMs;
        private long lastSnapshotAccessTimeMs;
        private long lastSnapshotLoadMs;
        private final ParsingLoadable<HlsPlaylist> mediaPlaylistLoadable;
        private final Loader mediaPlaylistLoader = new Loader("HlsPlaylistTracker:MediaPlaylist");
        private boolean pendingRefresh;
        private HlsMediaPlaylist playlistSnapshot;
        private final HlsMasterPlaylist.HlsUrl playlistUrl;

        public MediaPlaylistBundle(HlsMasterPlaylist.HlsUrl playlistUrl, long initialLastSnapshotAccessTimeMs) {
            this.playlistUrl = playlistUrl;
            this.lastSnapshotAccessTimeMs = initialLastSnapshotAccessTimeMs;
            this.mediaPlaylistLoadable = new ParsingLoadable<>(HlsPlaylistTracker.this.dataSourceFactory.createDataSource(4), UriUtil.resolveToUri(HlsPlaylistTracker.this.masterPlaylist.baseUri, playlistUrl.url), 4, HlsPlaylistTracker.this.playlistParser);
        }

        public HlsMediaPlaylist getPlaylistSnapshot() {
            this.lastSnapshotAccessTimeMs = SystemClock.elapsedRealtime();
            return this.playlistSnapshot;
        }

        public boolean isSnapshotValid() {
            if (this.playlistSnapshot == null) {
                return false;
            }
            long currentTimeMs = SystemClock.elapsedRealtime();
            long snapshotValidityDurationMs = Math.max(30000L, C.usToMs(this.playlistSnapshot.durationUs));
            return this.playlistSnapshot.hasEndTag || this.playlistSnapshot.playlistType == 2 || this.playlistSnapshot.playlistType == 1 || this.lastSnapshotLoadMs + snapshotValidityDurationMs > currentTimeMs;
        }

        public void release() {
            this.mediaPlaylistLoader.release();
        }

        public void loadPlaylist() {
            this.blacklistUntilMs = 0L;
            if (!this.pendingRefresh && !this.mediaPlaylistLoader.isLoading()) {
                this.mediaPlaylistLoader.startLoading(this.mediaPlaylistLoadable, this, HlsPlaylistTracker.this.minRetryCount);
            }
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public void onLoadCompleted(ParsingLoadable<HlsPlaylist> loadable, long elapsedRealtimeMs, long loadDurationMs) {
            HlsPlaylist result = loadable.getResult();
            if (result instanceof HlsMediaPlaylist) {
                processLoadedPlaylist((HlsMediaPlaylist) result);
                HlsPlaylistTracker.this.eventDispatcher.loadCompleted(loadable.dataSpec, 4, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
            } else {
                onLoadError(loadable, elapsedRealtimeMs, loadDurationMs, (IOException) new ParserException("Loaded playlist has unexpected type."));
            }
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public void onLoadCanceled(ParsingLoadable<HlsPlaylist> loadable, long elapsedRealtimeMs, long loadDurationMs, boolean released) {
            HlsPlaylistTracker.this.eventDispatcher.loadCanceled(loadable.dataSpec, 4, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.Callback
        public int onLoadError(ParsingLoadable<HlsPlaylist> loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
            boolean isFatal = error instanceof ParserException;
            HlsPlaylistTracker.this.eventDispatcher.loadError(loadable.dataSpec, 4, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded(), error, isFatal);
            if (isFatal) {
                return 3;
            }
            boolean shouldRetry = true;
            if (ChunkedTrackBlacklistUtil.shouldBlacklist(error)) {
                this.blacklistUntilMs = SystemClock.elapsedRealtime() + ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS;
                HlsPlaylistTracker.this.notifyPlaylistBlacklisting(this.playlistUrl, ChunkedTrackBlacklistUtil.DEFAULT_TRACK_BLACKLIST_MS);
                shouldRetry = HlsPlaylistTracker.this.primaryHlsUrl == this.playlistUrl && !HlsPlaylistTracker.this.maybeSelectNewPrimaryUrl();
            }
            return shouldRetry ? 0 : 2;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.pendingRefresh = false;
            loadPlaylist();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void processLoadedPlaylist(HlsMediaPlaylist loadedPlaylist) {
            HlsMediaPlaylist oldPlaylist = this.playlistSnapshot;
            this.lastSnapshotLoadMs = SystemClock.elapsedRealtime();
            this.playlistSnapshot = HlsPlaylistTracker.this.getLatestPlaylistSnapshot(oldPlaylist, loadedPlaylist);
            long refreshDelayUs = C.TIME_UNSET;
            if (this.playlistSnapshot != oldPlaylist) {
                if (HlsPlaylistTracker.this.onPlaylistUpdated(this.playlistUrl, this.playlistSnapshot)) {
                    refreshDelayUs = this.playlistSnapshot.targetDurationUs;
                }
            } else if (!this.playlistSnapshot.hasEndTag) {
                refreshDelayUs = this.playlistSnapshot.targetDurationUs / 2;
            }
            if (refreshDelayUs != C.TIME_UNSET) {
                this.pendingRefresh = HlsPlaylistTracker.this.playlistRefreshHandler.postDelayed(this, C.usToMs(refreshDelayUs));
            }
        }
    }
}
