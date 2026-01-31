package com.google.android.exoplayer2.text.ttml;

import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.util.Util;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
final class TtmlSubtitle implements Subtitle {
    private final long[] eventTimesUs;
    private final Map<String, TtmlStyle> globalStyles;
    private final Map<String, TtmlRegion> regionMap;
    private final TtmlNode root;

    public TtmlSubtitle(TtmlNode root, Map<String, TtmlStyle> globalStyles, Map<String, TtmlRegion> regionMap) {
        this.root = root;
        this.regionMap = regionMap;
        this.globalStyles = globalStyles != null ? Collections.unmodifiableMap(globalStyles) : Collections.emptyMap();
        this.eventTimesUs = root.getEventTimesUs();
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public int getNextEventTimeIndex(long timeUs) {
        int index = Util.binarySearchCeil(this.eventTimesUs, timeUs, false, false);
        if (index < this.eventTimesUs.length) {
            return index;
        }
        return -1;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public int getEventTimeCount() {
        return this.eventTimesUs.length;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public long getEventTime(int index) {
        return this.eventTimesUs[index];
    }

    TtmlNode getRoot() {
        return this.root;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public List<Cue> getCues(long timeUs) {
        return this.root.getCues(timeUs, this.globalStyles, this.regionMap);
    }

    Map<String, TtmlStyle> getGlobalStyles() {
        return this.globalStyles;
    }
}
