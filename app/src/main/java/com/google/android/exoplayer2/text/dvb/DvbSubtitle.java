package com.google.android.exoplayer2.text.dvb;

import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;
import java.util.List;

/* loaded from: classes.dex */
final class DvbSubtitle implements Subtitle {
    private final List<Cue> cues;

    public DvbSubtitle(List<Cue> cues) {
        this.cues = cues;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public int getNextEventTimeIndex(long timeUs) {
        return -1;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public int getEventTimeCount() {
        return 1;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public long getEventTime(int index) {
        return 0L;
    }

    @Override // com.google.android.exoplayer2.text.Subtitle
    public List<Cue> getCues(long timeUs) {
        return this.cues;
    }
}
