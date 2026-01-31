package com.google.android.exoplayer2.source.hls;

import android.net.Uri;
import android.os.SystemClock;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.chunk.Chunk;
import com.google.android.exoplayer2.source.chunk.ChunkedTrackBlacklistUtil;
import com.google.android.exoplayer2.source.chunk.DataChunk;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.trackselection.BaseTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
class HlsChunkSource {
    private final DataSource encryptionDataSource;
    private byte[] encryptionIv;
    private String encryptionIvString;
    private byte[] encryptionKey;
    private Uri encryptionKeyUri;
    private IOException fatalError;
    private boolean isTimestampMaster;
    private final DataSource mediaDataSource;
    private final List<Format> muxedCaptionFormats;
    private final HlsPlaylistTracker playlistTracker;
    private byte[] scratchSpace;
    private final TimestampAdjusterProvider timestampAdjusterProvider;
    private final TrackGroup trackGroup;
    private TrackSelection trackSelection;
    private final HlsMasterPlaylist.HlsUrl[] variants;

    public static final class HlsChunkHolder {
        public Chunk chunk;
        public boolean endOfStream;
        public HlsMasterPlaylist.HlsUrl playlist;

        public HlsChunkHolder() {
            clear();
        }

        public void clear() {
            this.chunk = null;
            this.endOfStream = false;
            this.playlist = null;
        }
    }

    public HlsChunkSource(HlsPlaylistTracker playlistTracker, HlsMasterPlaylist.HlsUrl[] variants, HlsDataSourceFactory dataSourceFactory, TimestampAdjusterProvider timestampAdjusterProvider, List<Format> muxedCaptionFormats) {
        this.playlistTracker = playlistTracker;
        this.variants = variants;
        this.timestampAdjusterProvider = timestampAdjusterProvider;
        this.muxedCaptionFormats = muxedCaptionFormats;
        Format[] variantFormats = new Format[variants.length];
        int[] initialTrackSelection = new int[variants.length];
        for (int i = 0; i < variants.length; i++) {
            variantFormats[i] = variants[i].format;
            initialTrackSelection[i] = i;
        }
        this.mediaDataSource = dataSourceFactory.createDataSource(1);
        this.encryptionDataSource = dataSourceFactory.createDataSource(3);
        this.trackGroup = new TrackGroup(variantFormats);
        this.trackSelection = new InitializationTrackSelection(this.trackGroup, initialTrackSelection);
    }

    public void maybeThrowError() throws IOException {
        if (this.fatalError != null) {
            throw this.fatalError;
        }
    }

    public TrackGroup getTrackGroup() {
        return this.trackGroup;
    }

    public void selectTracks(TrackSelection trackSelection) {
        this.trackSelection = trackSelection;
    }

    public void reset() {
        this.fatalError = null;
    }

    public void setIsTimestampMaster(boolean isTimestampMaster) {
        this.isTimestampMaster = isTimestampMaster;
    }

    public void getNextChunk(HlsMediaChunk previous, long playbackPositionUs, HlsChunkHolder out) {
        int chunkMediaSequence;
        int oldVariantIndex = previous == null ? -1 : this.trackGroup.indexOf(previous.trackFormat);
        long bufferedDurationUs = previous == null ? 0L : Math.max(0L, previous.startTimeUs - playbackPositionUs);
        this.trackSelection.updateSelectedTrack(bufferedDurationUs);
        int selectedVariantIndex = this.trackSelection.getSelectedIndexInTrackGroup();
        boolean switchingVariant = oldVariantIndex != selectedVariantIndex;
        HlsMasterPlaylist.HlsUrl selectedUrl = this.variants[selectedVariantIndex];
        if (!this.playlistTracker.isSnapshotValid(selectedUrl)) {
            out.playlist = selectedUrl;
            return;
        }
        HlsMediaPlaylist mediaPlaylist = this.playlistTracker.getPlaylistSnapshot(selectedUrl);
        if (previous == null || switchingVariant) {
            long targetPositionUs = previous == null ? playbackPositionUs : previous.startTimeUs;
            if (!mediaPlaylist.hasEndTag && targetPositionUs > mediaPlaylist.getEndTimeUs()) {
                chunkMediaSequence = mediaPlaylist.mediaSequence + mediaPlaylist.segments.size();
            } else {
                chunkMediaSequence = Util.binarySearchFloor((List<? extends Comparable<? super Long>>) mediaPlaylist.segments, Long.valueOf(targetPositionUs - mediaPlaylist.startTimeUs), true, !this.playlistTracker.isLive() || previous == null) + mediaPlaylist.mediaSequence;
                if (chunkMediaSequence < mediaPlaylist.mediaSequence && previous != null) {
                    selectedVariantIndex = oldVariantIndex;
                    selectedUrl = this.variants[selectedVariantIndex];
                    mediaPlaylist = this.playlistTracker.getPlaylistSnapshot(selectedUrl);
                    chunkMediaSequence = previous.getNextChunkIndex();
                }
            }
        } else {
            chunkMediaSequence = previous.getNextChunkIndex();
        }
        if (chunkMediaSequence < mediaPlaylist.mediaSequence) {
            this.fatalError = new BehindLiveWindowException();
            return;
        }
        int chunkIndex = chunkMediaSequence - mediaPlaylist.mediaSequence;
        if (chunkIndex >= mediaPlaylist.segments.size()) {
            if (mediaPlaylist.hasEndTag) {
                out.endOfStream = true;
                return;
            } else {
                out.playlist = selectedUrl;
                return;
            }
        }
        HlsMediaPlaylist.Segment segment = mediaPlaylist.segments.get(chunkIndex);
        if (segment.isEncrypted) {
            Uri keyUri = UriUtil.resolveToUri(mediaPlaylist.baseUri, segment.encryptionKeyUri);
            if (!keyUri.equals(this.encryptionKeyUri)) {
                out.chunk = newEncryptionKeyChunk(keyUri, segment.encryptionIV, selectedVariantIndex, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData());
                return;
            } else if (!Util.areEqual(segment.encryptionIV, this.encryptionIvString)) {
                setEncryptionData(keyUri, segment.encryptionIV, this.encryptionKey);
            }
        } else {
            clearEncryptionData();
        }
        DataSpec initDataSpec = null;
        HlsMediaPlaylist.Segment initSegment = mediaPlaylist.initializationSegment;
        if (initSegment != null) {
            Uri initSegmentUri = UriUtil.resolveToUri(mediaPlaylist.baseUri, initSegment.url);
            initDataSpec = new DataSpec(initSegmentUri, initSegment.byterangeOffset, initSegment.byterangeLength, null);
        }
        long startTimeUs = mediaPlaylist.startTimeUs + segment.relativeStartTimeUs;
        int discontinuitySequence = mediaPlaylist.discontinuitySequence + segment.relativeDiscontinuitySequence;
        TimestampAdjuster timestampAdjuster = this.timestampAdjusterProvider.getAdjuster(discontinuitySequence);
        Uri chunkUri = UriUtil.resolveToUri(mediaPlaylist.baseUri, segment.url);
        DataSpec dataSpec = new DataSpec(chunkUri, segment.byterangeOffset, segment.byterangeLength, null);
        out.chunk = new HlsMediaChunk(this.mediaDataSource, dataSpec, initDataSpec, selectedUrl, this.muxedCaptionFormats, this.trackSelection.getSelectionReason(), this.trackSelection.getSelectionData(), startTimeUs, startTimeUs + segment.durationUs, chunkMediaSequence, discontinuitySequence, this.isTimestampMaster, timestampAdjuster, previous, this.encryptionKey, this.encryptionIv);
    }

    public void onChunkLoadCompleted(Chunk chunk) {
        if (chunk instanceof EncryptionKeyChunk) {
            EncryptionKeyChunk encryptionKeyChunk = (EncryptionKeyChunk) chunk;
            this.scratchSpace = encryptionKeyChunk.getDataHolder();
            setEncryptionData(encryptionKeyChunk.dataSpec.uri, encryptionKeyChunk.iv, encryptionKeyChunk.getResult());
        }
    }

    public boolean onChunkLoadError(Chunk chunk, boolean cancelable, IOException error) {
        return cancelable && ChunkedTrackBlacklistUtil.maybeBlacklistTrack(this.trackSelection, this.trackSelection.indexOf(this.trackGroup.indexOf(chunk.trackFormat)), error);
    }

    public void onPlaylistBlacklisted(HlsMasterPlaylist.HlsUrl url, long blacklistMs) {
        int trackSelectionIndex;
        int trackGroupIndex = this.trackGroup.indexOf(url.format);
        if (trackGroupIndex != -1 && (trackSelectionIndex = this.trackSelection.indexOf(trackGroupIndex)) != -1) {
            this.trackSelection.blacklist(trackSelectionIndex, blacklistMs);
        }
    }

    private EncryptionKeyChunk newEncryptionKeyChunk(Uri keyUri, String iv, int variantIndex, int trackSelectionReason, Object trackSelectionData) {
        DataSpec dataSpec = new DataSpec(keyUri, 0L, -1L, null, 1);
        return new EncryptionKeyChunk(this.encryptionDataSource, dataSpec, this.variants[variantIndex].format, trackSelectionReason, trackSelectionData, this.scratchSpace, iv);
    }

    private void setEncryptionData(Uri keyUri, String iv, byte[] secretKey) {
        String trimmedIv;
        if (iv.toLowerCase(Locale.getDefault()).startsWith("0x")) {
            trimmedIv = iv.substring(2);
        } else {
            trimmedIv = iv;
        }
        byte[] ivData = new BigInteger(trimmedIv, 16).toByteArray();
        byte[] ivDataWithPadding = new byte[16];
        int offset = ivData.length > 16 ? ivData.length - 16 : 0;
        System.arraycopy(ivData, offset, ivDataWithPadding, (ivDataWithPadding.length - ivData.length) + offset, ivData.length - offset);
        this.encryptionKeyUri = keyUri;
        this.encryptionKey = secretKey;
        this.encryptionIvString = iv;
        this.encryptionIv = ivDataWithPadding;
    }

    private void clearEncryptionData() {
        this.encryptionKeyUri = null;
        this.encryptionKey = null;
        this.encryptionIvString = null;
        this.encryptionIv = null;
    }

    private static final class InitializationTrackSelection extends BaseTrackSelection {
        private int selectedIndex;

        public InitializationTrackSelection(TrackGroup group, int[] tracks) {
            super(group, tracks);
            this.selectedIndex = indexOf(group.getFormat(0));
        }

        @Override // com.google.android.exoplayer2.trackselection.TrackSelection
        public void updateSelectedTrack(long bufferedDurationUs) {
            long nowMs = SystemClock.elapsedRealtime();
            if (isBlacklisted(this.selectedIndex, nowMs)) {
                for (int i = this.length - 1; i >= 0; i--) {
                    if (!isBlacklisted(i, nowMs)) {
                        this.selectedIndex = i;
                        return;
                    }
                }
                throw new IllegalStateException();
            }
        }

        @Override // com.google.android.exoplayer2.trackselection.TrackSelection
        public int getSelectedIndex() {
            return this.selectedIndex;
        }

        @Override // com.google.android.exoplayer2.trackselection.TrackSelection
        public int getSelectionReason() {
            return 0;
        }

        @Override // com.google.android.exoplayer2.trackselection.TrackSelection
        public Object getSelectionData() {
            return null;
        }
    }

    private static final class EncryptionKeyChunk extends DataChunk {
        public final String iv;
        private byte[] result;

        public EncryptionKeyChunk(DataSource dataSource, DataSpec dataSpec, Format trackFormat, int trackSelectionReason, Object trackSelectionData, byte[] scratchSpace, String iv) {
            super(dataSource, dataSpec, 3, trackFormat, trackSelectionReason, trackSelectionData, scratchSpace);
            this.iv = iv;
        }

        @Override // com.google.android.exoplayer2.source.chunk.DataChunk
        protected void consume(byte[] data, int limit) throws IOException {
            this.result = Arrays.copyOf(data, limit);
        }

        public byte[] getResult() {
            return this.result;
        }
    }
}
