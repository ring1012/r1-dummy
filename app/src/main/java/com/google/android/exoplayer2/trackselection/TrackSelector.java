package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.source.TrackGroupArray;

/* loaded from: classes.dex */
public abstract class TrackSelector {
    private InvalidationListener listener;

    public interface InvalidationListener {
        void onTrackSelectionsInvalidated();
    }

    public abstract void onSelectionActivated(Object obj);

    public abstract TrackSelectorResult selectTracks(RendererCapabilities[] rendererCapabilitiesArr, TrackGroupArray trackGroupArray) throws ExoPlaybackException;

    public final void init(InvalidationListener listener) {
        this.listener = listener;
    }

    protected final void invalidate() {
        if (this.listener != null) {
            this.listener.onTrackSelectionsInvalidated();
        }
    }
}
