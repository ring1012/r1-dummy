package com.google.android.exoplayer2.text;

import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public class Cue {
    public static final int ANCHOR_TYPE_END = 2;
    public static final int ANCHOR_TYPE_MIDDLE = 1;
    public static final int ANCHOR_TYPE_START = 0;
    public static final float DIMEN_UNSET = Float.MIN_VALUE;
    public static final int LINE_TYPE_FRACTION = 0;
    public static final int LINE_TYPE_NUMBER = 1;
    public static final int TYPE_UNSET = Integer.MIN_VALUE;
    public final Bitmap bitmap;
    public final float bitmapHeight;
    public final float line;
    public final int lineAnchor;
    public final int lineType;
    public final float position;
    public final int positionAnchor;
    public final float size;
    public final CharSequence text;
    public final Layout.Alignment textAlignment;
    public final int windowColor;
    public final boolean windowColorSet;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AnchorType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LineType {
    }

    public Cue(Bitmap bitmap, float horizontalPosition, int horizontalPositionAnchor, float verticalPosition, int verticalPositionAnchor, float width, float height) {
        this(null, null, bitmap, verticalPosition, 0, verticalPositionAnchor, horizontalPosition, horizontalPositionAnchor, width, height, false, ViewCompat.MEASURED_STATE_MASK);
    }

    public Cue(CharSequence text) {
        this(text, null, Float.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE, Integer.MIN_VALUE, Float.MIN_VALUE);
    }

    public Cue(CharSequence text, Layout.Alignment textAlignment, float line, int lineType, int lineAnchor, float position, int positionAnchor, float size) {
        this(text, textAlignment, line, lineType, lineAnchor, position, positionAnchor, size, false, ViewCompat.MEASURED_STATE_MASK);
    }

    public Cue(CharSequence text, Layout.Alignment textAlignment, float line, int lineType, int lineAnchor, float position, int positionAnchor, float size, boolean windowColorSet, int windowColor) {
        this(text, textAlignment, null, line, lineType, lineAnchor, position, positionAnchor, size, Float.MIN_VALUE, windowColorSet, windowColor);
    }

    private Cue(CharSequence text, Layout.Alignment textAlignment, Bitmap bitmap, float line, int lineType, int lineAnchor, float position, int positionAnchor, float size, float bitmapHeight, boolean windowColorSet, int windowColor) {
        this.text = text;
        this.textAlignment = textAlignment;
        this.bitmap = bitmap;
        this.line = line;
        this.lineType = lineType;
        this.lineAnchor = lineAnchor;
        this.position = position;
        this.positionAnchor = positionAnchor;
        this.size = size;
        this.bitmapHeight = bitmapHeight;
        this.windowColorSet = windowColorSet;
        this.windowColor = windowColor;
    }
}
