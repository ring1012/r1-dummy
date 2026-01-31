package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class TrackGroup {
    private final Format[] formats;
    private int hashCode;
    public final int length;

    public TrackGroup(Format... formats) {
        Assertions.checkState(formats.length > 0);
        this.formats = formats;
        this.length = formats.length;
    }

    public Format getFormat(int index) {
        return this.formats[index];
    }

    public int indexOf(Format format) {
        for (int i = 0; i < this.formats.length; i++) {
            if (format == this.formats[i]) {
                return i;
            }
        }
        return -1;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int result = Arrays.hashCode(this.formats) + 527;
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
        TrackGroup other = (TrackGroup) obj;
        return this.length == other.length && Arrays.equals(this.formats, other.formats);
    }
}
