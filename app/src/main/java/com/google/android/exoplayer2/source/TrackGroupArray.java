package com.google.android.exoplayer2.source;

import java.util.Arrays;

/* loaded from: classes.dex */
public final class TrackGroupArray {
    public static final TrackGroupArray EMPTY = new TrackGroupArray(new TrackGroup[0]);
    private int hashCode;
    public final int length;
    private final TrackGroup[] trackGroups;

    public TrackGroupArray(TrackGroup... trackGroups) {
        this.trackGroups = trackGroups;
        this.length = trackGroups.length;
    }

    public TrackGroup get(int index) {
        return this.trackGroups[index];
    }

    public int indexOf(TrackGroup group) {
        for (int i = 0; i < this.length; i++) {
            if (this.trackGroups[i] == group) {
                return i;
            }
        }
        return -1;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = Arrays.hashCode(this.trackGroups);
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
        TrackGroupArray other = (TrackGroupArray) obj;
        return this.length == other.length && Arrays.equals(this.trackGroups, other.trackGroups);
    }
}
