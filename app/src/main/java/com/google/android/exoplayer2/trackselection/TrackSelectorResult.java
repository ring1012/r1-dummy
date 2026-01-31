package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.RendererConfiguration;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.util.Util;

/* loaded from: classes.dex */
public final class TrackSelectorResult {
    public final TrackGroupArray groups;
    public final Object info;
    public final RendererConfiguration[] rendererConfigurations;
    public final TrackSelectionArray selections;

    public TrackSelectorResult(TrackGroupArray groups, TrackSelectionArray selections, Object info, RendererConfiguration[] rendererConfigurations) {
        this.groups = groups;
        this.selections = selections;
        this.info = info;
        this.rendererConfigurations = rendererConfigurations;
    }

    public boolean isEquivalent(TrackSelectorResult other) {
        if (other == null) {
            return false;
        }
        for (int i = 0; i < this.selections.length; i++) {
            if (!isEquivalent(other, i)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEquivalent(TrackSelectorResult other, int index) {
        return other != null && Util.areEqual(this.selections.get(index), other.selections.get(index)) && Util.areEqual(this.rendererConfigurations[index], other.rendererConfigurations[index]);
    }
}
