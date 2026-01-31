package com.google.android.exoplayer2.text.cea;

import android.support.annotation.NonNull;
import android.text.Layout;
import com.google.android.exoplayer2.text.Cue;

/* loaded from: classes.dex */
final class Cea708Cue extends Cue implements Comparable<Cea708Cue> {
    public static final int PRIORITY_UNSET = -1;
    public final int priority;

    public Cea708Cue(CharSequence text, Layout.Alignment textAlignment, float line, int lineType, int lineAnchor, float position, int positionAnchor, float size, boolean windowColorSet, int windowColor, int priority) {
        super(text, textAlignment, line, lineType, lineAnchor, position, positionAnchor, size, windowColorSet, windowColor);
        this.priority = priority;
    }

    @Override // java.lang.Comparable
    public int compareTo(@NonNull Cea708Cue other) {
        if (other.priority < this.priority) {
            return -1;
        }
        if (other.priority > this.priority) {
            return 1;
        }
        return 0;
    }
}
