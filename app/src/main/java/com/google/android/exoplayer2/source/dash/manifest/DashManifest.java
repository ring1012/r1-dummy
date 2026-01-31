package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import com.google.android.exoplayer2.C;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class DashManifest {
    public final long availabilityStartTime;
    public final long duration;
    public final boolean dynamic;
    public final Uri location;
    public final long minBufferTime;
    public final long minUpdatePeriod;
    private final List<Period> periods;
    public final long suggestedPresentationDelay;
    public final long timeShiftBufferDepth;
    public final UtcTimingElement utcTiming;

    public DashManifest(long availabilityStartTime, long duration, long minBufferTime, boolean dynamic, long minUpdatePeriod, long timeShiftBufferDepth, long suggestedPresentationDelay, UtcTimingElement utcTiming, Uri location, List<Period> periods) {
        this.availabilityStartTime = availabilityStartTime;
        this.duration = duration;
        this.minBufferTime = minBufferTime;
        this.dynamic = dynamic;
        this.minUpdatePeriod = minUpdatePeriod;
        this.timeShiftBufferDepth = timeShiftBufferDepth;
        this.suggestedPresentationDelay = suggestedPresentationDelay;
        this.utcTiming = utcTiming;
        this.location = location;
        this.periods = periods == null ? Collections.emptyList() : periods;
    }

    public final int getPeriodCount() {
        return this.periods.size();
    }

    public final Period getPeriod(int index) {
        return this.periods.get(index);
    }

    public final long getPeriodDurationMs(int index) {
        if (index == this.periods.size() - 1) {
            return this.duration == C.TIME_UNSET ? C.TIME_UNSET : this.duration - this.periods.get(index).startMs;
        }
        return this.periods.get(index + 1).startMs - this.periods.get(index).startMs;
    }

    public final long getPeriodDurationUs(int index) {
        return C.msToUs(getPeriodDurationMs(index));
    }

    public final DashManifest copy(List<RepresentationKey> representationKeys) {
        LinkedList<RepresentationKey> keys = new LinkedList<>(representationKeys);
        Collections.sort(keys);
        keys.add(new RepresentationKey(-1, -1, -1));
        ArrayList<Period> copyPeriods = new ArrayList<>();
        long shiftMs = 0;
        for (int periodIndex = 0; periodIndex < getPeriodCount(); periodIndex++) {
            if (keys.peek().periodIndex != periodIndex) {
                long periodDurationMs = getPeriodDurationMs(periodIndex);
                if (periodDurationMs != C.TIME_UNSET) {
                    shiftMs += periodDurationMs;
                }
            } else {
                Period period = getPeriod(periodIndex);
                ArrayList<AdaptationSet> copyAdaptationSets = copyAdaptationSets(period.adaptationSets, keys);
                copyPeriods.add(new Period(period.id, period.startMs - shiftMs, copyAdaptationSets));
            }
        }
        long newDuration = this.duration != C.TIME_UNSET ? this.duration - shiftMs : C.TIME_UNSET;
        return new DashManifest(this.availabilityStartTime, newDuration, this.minBufferTime, this.dynamic, this.minUpdatePeriod, this.timeShiftBufferDepth, this.suggestedPresentationDelay, this.utcTiming, this.location, copyPeriods);
    }

    private static ArrayList<AdaptationSet> copyAdaptationSets(List<AdaptationSet> adaptationSets, LinkedList<RepresentationKey> keys) {
        RepresentationKey key = keys.poll();
        int periodIndex = key.periodIndex;
        ArrayList<AdaptationSet> copyAdaptationSets = new ArrayList<>();
        do {
            int adaptationSetIndex = key.adaptationSetIndex;
            AdaptationSet adaptationSet = adaptationSets.get(adaptationSetIndex);
            List<Representation> representations = adaptationSet.representations;
            ArrayList<Representation> copyRepresentations = new ArrayList<>();
            do {
                Representation representation = representations.get(key.representationIndex);
                copyRepresentations.add(representation);
                key = keys.poll();
                if (key.periodIndex != periodIndex) {
                    break;
                }
            } while (key.adaptationSetIndex == adaptationSetIndex);
            copyAdaptationSets.add(new AdaptationSet(adaptationSet.id, adaptationSet.type, copyRepresentations, adaptationSet.accessibilityDescriptors));
        } while (key.periodIndex == periodIndex);
        keys.addFirst(key);
        return copyAdaptationSets;
    }
}
