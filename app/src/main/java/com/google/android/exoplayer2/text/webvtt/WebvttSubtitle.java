package com.google.android.exoplayer2.text.webvtt;

import android.text.SpannableStringBuilder;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
final class WebvttSubtitle implements Subtitle {
    private final long[] cueTimesUs;
    private final List<WebvttCue> cues;
    private final int numCues;
    private final long[] sortedCueTimesUs;

    public WebvttSubtitle(List<WebvttCue> cues) {
        this.cues = cues;
        this.numCues = cues.size();
        this.cueTimesUs = new long[this.numCues * 2];
        for (int cueIndex = 0; cueIndex < this.numCues; cueIndex++) {
            WebvttCue cue = cues.get(cueIndex);
            int arrayIndex = cueIndex * 2;
            this.cueTimesUs[arrayIndex] = cue.startTime;
            this.cueTimesUs[arrayIndex + 1] = cue.endTime;
        }
        this.sortedCueTimesUs = Arrays.copyOf(this.cueTimesUs, this.cueTimesUs.length);
        Arrays.sort(this.sortedCueTimesUs);
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public int getNextEventTimeIndex(long timeUs) {
        int index = Util.binarySearchCeil(this.sortedCueTimesUs, timeUs, false, false);
        if (index < this.sortedCueTimesUs.length) {
            return index;
        }
        return -1;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public int getEventTimeCount() {
        return this.sortedCueTimesUs.length;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public long getEventTime(int index) {
        Assertions.checkArgument(index >= 0);
        Assertions.checkArgument(index < this.sortedCueTimesUs.length);
        return this.sortedCueTimesUs[index];
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public List<Cue> getCues(long timeUs) {
        ArrayList<Cue> list = null;
        WebvttCue firstNormalCue = null;
        SpannableStringBuilder normalCueTextBuilder = null;
        for (int i = 0; i < this.numCues; i++) {
            if (this.cueTimesUs[i * 2] <= timeUs && timeUs < this.cueTimesUs[(i * 2) + 1]) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                WebvttCue cue = this.cues.get(i);
                if (cue.isNormalCue()) {
                    if (firstNormalCue == null) {
                        firstNormalCue = cue;
                    } else if (normalCueTextBuilder == null) {
                        normalCueTextBuilder = new SpannableStringBuilder();
                        normalCueTextBuilder.append(firstNormalCue.text).append((CharSequence) "\n").append(cue.text);
                    } else {
                        normalCueTextBuilder.append((CharSequence) "\n").append(cue.text);
                    }
                } else {
                    list.add(cue);
                }
            }
        }
        if (normalCueTextBuilder != null) {
            list.add(new WebvttCue(normalCueTextBuilder));
        } else if (firstNormalCue != null) {
            list.add(firstNormalCue);
        }
        return list != null ? list : Collections.emptyList();
    }
}
