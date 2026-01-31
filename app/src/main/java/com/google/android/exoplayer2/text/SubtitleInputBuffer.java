package com.google.android.exoplayer2.text;

import android.support.annotation.NonNull;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;

/* loaded from: classes.dex */
public final class SubtitleInputBuffer extends DecoderInputBuffer implements Comparable<SubtitleInputBuffer> {
    public long subsampleOffsetUs;

    public SubtitleInputBuffer() {
        super(1);
    }

    @Override // java.lang.Comparable
    public int compareTo(@NonNull SubtitleInputBuffer other) {
        long delta = this.timeUs - other.timeUs;
        if (delta == 0) {
            return 0;
        }
        return delta > 0 ? 1 : -1;
    }
}
