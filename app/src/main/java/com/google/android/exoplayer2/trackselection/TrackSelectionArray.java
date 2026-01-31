package com.google.android.exoplayer2.trackselection;

import java.util.Arrays;

/* loaded from: classes.dex */
public final class TrackSelectionArray {
    private int hashCode;
    public final int length;
    private final TrackSelection[] trackSelections;

    public TrackSelectionArray(TrackSelection... trackSelections) {
        this.trackSelections = trackSelections;
        this.length = trackSelections.length;
    }

    public TrackSelection get(int index) {
        return this.trackSelections[index];
    }

    public TrackSelection[] getAll() {
        return (TrackSelection[]) this.trackSelections.clone();
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = Arrays.hashCode(this.trackSelections) + 527;
            this.hashCode = result;
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
        TrackSelectionArray other = (TrackSelectionArray) obj;
        return Arrays.equals(this.trackSelections, other.trackSelections);
    }
}
