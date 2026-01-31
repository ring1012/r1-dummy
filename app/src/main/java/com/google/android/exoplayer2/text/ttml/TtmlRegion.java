package com.google.android.exoplayer2.text.ttml;

/* loaded from: classes.dex */
final class TtmlRegion {
    public final float line;
    public final int lineType;
    public final float position;
    public final float width;

    public TtmlRegion() {
        this(Float.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE);
    }

    public TtmlRegion(float position, float line, int lineType, float width) {
        this.position = position;
        this.line = line;
        this.lineType = lineType;
        this.width = width;
    }
}
