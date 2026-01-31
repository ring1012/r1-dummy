package com.google.android.exoplayer2.source.hls;

import android.os.Handler;
import android.text.TextUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.CompositeSequenceableLoader;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.Allocator;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;

/* loaded from: classes.dex */
public final class HlsMediaPeriod implements MediaPeriod, HlsSampleStreamWrapper.Callback, HlsPlaylistTracker.PlaylistEventListener {
    private final Allocator allocator;
    private MediaPeriod.Callback callback;
    private final HlsDataSourceFactory dataSourceFactory;
    private HlsSampleStreamWrapper[] enabledSampleStreamWrappers;
    private final AdaptiveMediaSourceEventListener.EventDispatcher eventDispatcher;
    private final int minLoadableRetryCount;
    private int pendingPrepareCount;
    private final HlsPlaylistTracker playlistTracker;
    private final long preparePositionUs;
    private HlsSampleStreamWrapper[] sampleStreamWrappers;
    private boolean seenFirstTrackSelection;
    private CompositeSequenceableLoader sequenceableLoader;
    private TrackGroupArray trackGroups;
    private final IdentityHashMap<SampleStream, Integer> streamWrapperIndices = new IdentityHashMap<>();
    private final TimestampAdjusterProvider timestampAdjusterProvider = new TimestampAdjusterProvider();
    private final Handler continueLoadingHandler = new Handler();

    public HlsMediaPeriod(HlsPlaylistTracker playlistTracker, HlsDataSourceFactory dataSourceFactory, int minLoadableRetryCount, AdaptiveMediaSourceEventListener.EventDispatcher eventDispatcher, Allocator allocator, long positionUs) {
        this.playlistTracker = playlistTracker;
        this.dataSourceFactory = dataSourceFactory;
        this.minLoadableRetryCount = minLoadableRetryCount;
        this.eventDispatcher = eventDispatcher;
        this.allocator = allocator;
        this.preparePositionUs = positionUs;
    }

    public void release() {
        this.playlistTracker.removeListener(this);
        this.continueLoadingHandler.removeCallbacksAndMessages(null);
        if (this.sampleStreamWrappers != null) {
            for (HlsSampleStreamWrapper sampleStreamWrapper : this.sampleStreamWrappers) {
                sampleStreamWrapper.release();
            }
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void prepare(MediaPeriod.Callback callback) {
        this.playlistTracker.addListener(this);
        this.callback = callback;
        buildAndPrepareSampleStreamWrappers();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void maybeThrowPrepareError() throws IOException {
        if (this.sampleStreamWrappers != null) {
            for (HlsSampleStreamWrapper sampleStreamWrapper : this.sampleStreamWrappers) {
                sampleStreamWrapper.maybeThrowPrepareError();
            }
        }
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public TrackGroupArray getTrackGroups() {
        return this.trackGroups;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long selectTracks(TrackSelection[] selections, boolean[] mayRetainStreamFlags, SampleStream[] streams, boolean[] streamResetFlags, long positionUs) {
        int[] streamChildIndices = new int[selections.length];
        int[] selectionChildIndices = new int[selections.length];
        for (int i = 0; i < selections.length; i++) {
            streamChildIndices[i] = streams[i] == null ? -1 : this.streamWrapperIndices.get(streams[i]).intValue();
            selectionChildIndices[i] = -1;
            if (selections[i] != null) {
                TrackGroup trackGroup = selections[i].getTrackGroup();
                int j = 0;
                while (true) {
                    if (j >= this.sampleStreamWrappers.length) {
                        break;
                    }
                    if (this.sampleStreamWrappers[j].getTrackGroups().indexOf(trackGroup) == -1) {
                        j++;
                    } else {
                        selectionChildIndices[i] = j;
                        break;
                    }
                }
            }
        }
        boolean selectedNewTracks = false;
        this.streamWrapperIndices.clear();
        SampleStream[] newStreams = new SampleStream[selections.length];
        SampleStream[] childStreams = new SampleStream[selections.length];
        TrackSelection[] childSelections = new TrackSelection[selections.length];
        ArrayList<HlsSampleStreamWrapper> enabledSampleStreamWrapperList = new ArrayList<>(this.sampleStreamWrappers.length);
        int i2 = 0;
        while (i2 < this.sampleStreamWrappers.length) {
            for (int j2 = 0; j2 < selections.length; j2++) {
                childStreams[j2] = streamChildIndices[j2] == i2 ? streams[j2] : null;
                childSelections[j2] = selectionChildIndices[j2] == i2 ? selections[j2] : null;
            }
            selectedNewTracks |= this.sampleStreamWrappers[i2].selectTracks(childSelections, mayRetainStreamFlags, childStreams, streamResetFlags, !this.seenFirstTrackSelection);
            boolean wrapperEnabled = false;
            for (int j3 = 0; j3 < selections.length; j3++) {
                if (selectionChildIndices[j3] == i2) {
                    Assertions.checkState(childStreams[j3] != null);
                    newStreams[j3] = childStreams[j3];
                    wrapperEnabled = true;
                    this.streamWrapperIndices.put(childStreams[j3], Integer.valueOf(i2));
                } else if (streamChildIndices[j3] == i2) {
                    Assertions.checkState(childStreams[j3] == null);
                }
            }
            if (wrapperEnabled) {
                enabledSampleStreamWrapperList.add(this.sampleStreamWrappers[i2]);
            }
            i2++;
        }
        System.arraycopy(newStreams, 0, streams, 0, newStreams.length);
        this.enabledSampleStreamWrappers = new HlsSampleStreamWrapper[enabledSampleStreamWrapperList.size()];
        enabledSampleStreamWrapperList.toArray(this.enabledSampleStreamWrappers);
        if (this.enabledSampleStreamWrappers.length > 0) {
            this.enabledSampleStreamWrappers[0].setIsTimestampMaster(true);
            for (int i3 = 1; i3 < this.enabledSampleStreamWrappers.length; i3++) {
                this.enabledSampleStreamWrappers[i3].setIsTimestampMaster(false);
            }
        }
        this.sequenceableLoader = new CompositeSequenceableLoader(this.enabledSampleStreamWrappers);
        if (this.seenFirstTrackSelection && selectedNewTracks) {
            seekToUs(positionUs);
            for (int i4 = 0; i4 < selections.length; i4++) {
                if (streams[i4] != null) {
                    streamResetFlags[i4] = true;
                }
            }
        }
        this.seenFirstTrackSelection = true;
        return positionUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public void discardBuffer(long positionUs) {
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public boolean continueLoading(long positionUs) {
        return this.sequenceableLoader.continueLoading(positionUs);
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod, com.google.android.exoplayer2.source.SequenceableLoader
    public long getNextLoadPositionUs() {
        return this.sequenceableLoader.getNextLoadPositionUs();
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long readDiscontinuity() {
        return C.TIME_UNSET;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long getBufferedPositionUs() {
        long bufferedPositionUs = Long.MAX_VALUE;
        for (HlsSampleStreamWrapper sampleStreamWrapper : this.enabledSampleStreamWrappers) {
            long rendererBufferedPositionUs = sampleStreamWrapper.getBufferedPositionUs();
            if (rendererBufferedPositionUs != Long.MIN_VALUE) {
                bufferedPositionUs = Math.min(bufferedPositionUs, rendererBufferedPositionUs);
            }
        }
        if (bufferedPositionUs == Long.MAX_VALUE) {
            return Long.MIN_VALUE;
        }
        return bufferedPositionUs;
    }

    @Override // com.google.android.exoplayer2.source.MediaPeriod
    public long seekToUs(long positionUs) {
        this.timestampAdjusterProvider.reset();
        for (HlsSampleStreamWrapper sampleStreamWrapper : this.enabledSampleStreamWrappers) {
            sampleStreamWrapper.seekTo(positionUs);
        }
        return positionUs;
    }

    @Override // com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper.Callback
    public void onPrepared() {
        int i = 0;
        int i2 = this.pendingPrepareCount - 1;
        this.pendingPrepareCount = i2;
        if (i2 <= 0) {
            int totalTrackGroupCount = 0;
            for (HlsSampleStreamWrapper hlsSampleStreamWrapper : this.sampleStreamWrappers) {
                totalTrackGroupCount += hlsSampleStreamWrapper.getTrackGroups().length;
            }
            TrackGroup[] trackGroupArray = new TrackGroup[totalTrackGroupCount];
            int trackGroupIndex = 0;
            HlsSampleStreamWrapper[] hlsSampleStreamWrapperArr = this.sampleStreamWrappers;
            int length = hlsSampleStreamWrapperArr.length;
            while (i < length) {
                HlsSampleStreamWrapper sampleStreamWrapper = hlsSampleStreamWrapperArr[i];
                int wrapperTrackGroupCount = sampleStreamWrapper.getTrackGroups().length;
                int j = 0;
                int trackGroupIndex2 = trackGroupIndex;
                while (j < wrapperTrackGroupCount) {
                    trackGroupArray[trackGroupIndex2] = sampleStreamWrapper.getTrackGroups().get(j);
                    j++;
                    trackGroupIndex2++;
                }
                i++;
                trackGroupIndex = trackGroupIndex2;
            }
            this.trackGroups = new TrackGroupArray(trackGroupArray);
            this.callback.onPrepared(this);
        }
    }

    @Override // com.google.android.exoplayer2.source.hls.HlsSampleStreamWrapper.Callback
    public void onPlaylistRefreshRequired(HlsMasterPlaylist.HlsUrl url) {
        this.playlistTracker.refreshPlaylist(url);
    }

    @Override // com.google.android.exoplayer2.source.SequenceableLoader.Callback
    public void onContinueLoadingRequested(HlsSampleStreamWrapper sampleStreamWrapper) {
        if (this.trackGroups != null) {
            this.callback.onContinueLoadingRequested(this);
        }
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.PlaylistEventListener
    public void onPlaylistChanged() {
        continuePreparingOrLoading();
    }

    @Override // com.google.android.exoplayer2.source.hls.playlist.HlsPlaylistTracker.PlaylistEventListener
    public void onPlaylistBlacklisted(HlsMasterPlaylist.HlsUrl url, long blacklistMs) {
        for (HlsSampleStreamWrapper streamWrapper : this.sampleStreamWrappers) {
            streamWrapper.onPlaylistBlacklisted(url, blacklistMs);
        }
        continuePreparingOrLoading();
    }

    private void buildAndPrepareSampleStreamWrappers() {
        int currentWrapperIndex;
        HlsMasterPlaylist masterPlaylist = this.playlistTracker.getMasterPlaylist();
        List<HlsMasterPlaylist.HlsUrl> selectedVariants = new ArrayList<>(masterPlaylist.variants);
        ArrayList<HlsMasterPlaylist.HlsUrl> definiteVideoVariants = new ArrayList<>();
        ArrayList<HlsMasterPlaylist.HlsUrl> definiteAudioOnlyVariants = new ArrayList<>();
        for (int i = 0; i < selectedVariants.size(); i++) {
            HlsMasterPlaylist.HlsUrl variant = selectedVariants.get(i);
            if (variant.format.height > 0 || variantHasExplicitCodecWithPrefix(variant, "avc")) {
                definiteVideoVariants.add(variant);
            } else if (variantHasExplicitCodecWithPrefix(variant, "mp4a")) {
                definiteAudioOnlyVariants.add(variant);
            }
        }
        if (!definiteVideoVariants.isEmpty()) {
            selectedVariants = definiteVideoVariants;
        } else if (definiteAudioOnlyVariants.size() < selectedVariants.size()) {
            selectedVariants.removeAll(definiteAudioOnlyVariants);
        }
        List<HlsMasterPlaylist.HlsUrl> audioRenditions = masterPlaylist.audios;
        List<HlsMasterPlaylist.HlsUrl> subtitleRenditions = masterPlaylist.subtitles;
        this.sampleStreamWrappers = new HlsSampleStreamWrapper[audioRenditions.size() + 1 + subtitleRenditions.size()];
        this.pendingPrepareCount = this.sampleStreamWrappers.length;
        Assertions.checkArgument(!selectedVariants.isEmpty());
        HlsMasterPlaylist.HlsUrl[] variants = new HlsMasterPlaylist.HlsUrl[selectedVariants.size()];
        selectedVariants.toArray(variants);
        HlsSampleStreamWrapper sampleStreamWrapper = buildSampleStreamWrapper(0, variants, masterPlaylist.muxedAudioFormat, masterPlaylist.muxedCaptionFormats);
        int currentWrapperIndex2 = 0 + 1;
        this.sampleStreamWrappers[0] = sampleStreamWrapper;
        sampleStreamWrapper.setIsTimestampMaster(true);
        sampleStreamWrapper.continuePreparing();
        int i2 = 0;
        while (true) {
            currentWrapperIndex = currentWrapperIndex2;
            if (i2 >= audioRenditions.size()) {
                break;
            }
            HlsSampleStreamWrapper sampleStreamWrapper2 = buildSampleStreamWrapper(1, new HlsMasterPlaylist.HlsUrl[]{audioRenditions.get(i2)}, null, Collections.emptyList());
            currentWrapperIndex2 = currentWrapperIndex + 1;
            this.sampleStreamWrappers[currentWrapperIndex] = sampleStreamWrapper2;
            sampleStreamWrapper2.continuePreparing();
            i2++;
        }
        int i3 = 0;
        while (i3 < subtitleRenditions.size()) {
            HlsMasterPlaylist.HlsUrl url = subtitleRenditions.get(i3);
            HlsSampleStreamWrapper sampleStreamWrapper3 = buildSampleStreamWrapper(3, new HlsMasterPlaylist.HlsUrl[]{url}, null, Collections.emptyList());
            sampleStreamWrapper3.prepareSingleTrack(url.format);
            this.sampleStreamWrappers[currentWrapperIndex] = sampleStreamWrapper3;
            i3++;
            currentWrapperIndex++;
        }
    }

    private HlsSampleStreamWrapper buildSampleStreamWrapper(int trackType, HlsMasterPlaylist.HlsUrl[] variants, Format muxedAudioFormat, List<Format> muxedCaptionFormats) {
        HlsChunkSource defaultChunkSource = new HlsChunkSource(this.playlistTracker, variants, this.dataSourceFactory, this.timestampAdjusterProvider, muxedCaptionFormats);
        return new HlsSampleStreamWrapper(trackType, this, defaultChunkSource, this.allocator, this.preparePositionUs, muxedAudioFormat, this.minLoadableRetryCount, this.eventDispatcher);
    }

    private void continuePreparingOrLoading() {
        if (this.trackGroups != null) {
            this.callback.onContinueLoadingRequested(this);
            return;
        }
        for (HlsSampleStreamWrapper wrapper : this.sampleStreamWrappers) {
            wrapper.continuePreparing();
        }
    }

    private static boolean variantHasExplicitCodecWithPrefix(HlsMasterPlaylist.HlsUrl variant, String prefix) {
        String codecs = variant.format.codecs;
        if (TextUtils.isEmpty(codecs)) {
            return false;
        }
        String[] codecArray = codecs.split("(\\s*,\\s*)|(\\s*$)");
        for (String codec : codecArray) {
            if (codec.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
