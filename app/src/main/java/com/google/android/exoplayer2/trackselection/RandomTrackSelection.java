package com.google.android.exoplayer2.trackselection;

import android.os.SystemClock;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import java.util.Random;

/* loaded from: classes.dex */
public final class RandomTrackSelection extends BaseTrackSelection {
    private final Random random;
    private int selectedIndex;

    public static final class Factory implements TrackSelection.Factory {
        private final Random random;

        public Factory() {
            this.random = new Random();
        }

        public Factory(int seed) {
            this.random = new Random(seed);
        }

        @Override // com.google.android.exoplayer2.trackselection.TrackSelection.Factory
        public RandomTrackSelection createTrackSelection(TrackGroup group, int... tracks) {
            return new RandomTrackSelection(group, tracks, this.random);
        }
    }

    public RandomTrackSelection(TrackGroup group, int... tracks) {
        super(group, tracks);
        this.random = new Random();
        this.selectedIndex = this.random.nextInt(this.length);
    }

    public RandomTrackSelection(TrackGroup group, int[] tracks, long seed) {
        this(group, tracks, new Random(seed));
    }

    public RandomTrackSelection(TrackGroup group, int[] tracks, Random random) {
        super(group, tracks);
        this.random = random;
        this.selectedIndex = random.nextInt(this.length);
    }

    @Override // com.google.android.exoplayer2.trackselection.TrackSelection
    public void updateSelectedTrack(long bufferedDurationUs) {
        long nowMs = SystemClock.elapsedRealtime();
        int nonBlacklistedFormatCount = 0;
        for (int i = 0; i < this.length; i++) {
            if (!isBlacklisted(i, nowMs)) {
                nonBlacklistedFormatCount++;
            }
        }
        this.selectedIndex = this.random.nextInt(nonBlacklistedFormatCount);
        if (nonBlacklistedFormatCount != this.length) {
            int nonBlacklistedFormatCount2 = 0;
            for (int i2 = 0; i2 < this.length; i2++) {
                if (!isBlacklisted(i2, nowMs)) {
                    int nonBlacklistedFormatCount3 = nonBlacklistedFormatCount2 + 1;
                    if (this.selectedIndex == nonBlacklistedFormatCount2) {
                        this.selectedIndex = i2;
                        return;
                    }
                    nonBlacklistedFormatCount2 = nonBlacklistedFormatCount3;
                }
            }
        }
    }

    @Override // com.google.android.exoplayer2.trackselection.TrackSelection
    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    @Override // com.google.android.exoplayer2.trackselection.TrackSelection
    public int getSelectionReason() {
        return 3;
    }

    @Override // com.google.android.exoplayer2.trackselection.TrackSelection
    public Object getSelectionData() {
        return null;
    }
}
