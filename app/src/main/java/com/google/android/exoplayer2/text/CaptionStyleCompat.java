package com.google.android.exoplayer2.text;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.view.accessibility.CaptioningManager;
import com.google.android.exoplayer2.util.Util;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public final class CaptionStyleCompat {
    public static final CaptionStyleCompat DEFAULT = new CaptionStyleCompat(-1, ViewCompat.MEASURED_STATE_MASK, 0, 0, -1, null);
    public static final int EDGE_TYPE_DEPRESSED = 4;
    public static final int EDGE_TYPE_DROP_SHADOW = 2;
    public static final int EDGE_TYPE_NONE = 0;
    public static final int EDGE_TYPE_OUTLINE = 1;
    public static final int EDGE_TYPE_RAISED = 3;
    public static final int USE_TRACK_COLOR_SETTINGS = 1;
    public final int backgroundColor;
    public final int edgeColor;
    public final int edgeType;
    public final int foregroundColor;
    public final Typeface typeface;
    public final int windowColor;

    @Retention(RetentionPolicy.SOURCE)
    public @interface EdgeType {
    }

    @TargetApi(19)
    public static CaptionStyleCompat createFromCaptionStyle(CaptioningManager.CaptionStyle captionStyle) {
        return Util.SDK_INT >= 21 ? createFromCaptionStyleV21(captionStyle) : createFromCaptionStyleV19(captionStyle);
    }

    public CaptionStyleCompat(int foregroundColor, int backgroundColor, int windowColor, int edgeType, int edgeColor, Typeface typeface) {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.windowColor = windowColor;
        this.edgeType = edgeType;
        this.edgeColor = edgeColor;
        this.typeface = typeface;
    }

    @TargetApi(19)
    private static CaptionStyleCompat createFromCaptionStyleV19(CaptioningManager.CaptionStyle captionStyle) {
        return new CaptionStyleCompat(captionStyle.foregroundColor, captionStyle.backgroundColor, 0, captionStyle.edgeType, captionStyle.edgeColor, captionStyle.getTypeface());
    }

    @TargetApi(21)
    private static CaptionStyleCompat createFromCaptionStyleV21(CaptioningManager.CaptionStyle captionStyle) {
        return new CaptionStyleCompat(captionStyle.hasForegroundColor() ? captionStyle.foregroundColor : DEFAULT.foregroundColor, captionStyle.hasBackgroundColor() ? captionStyle.backgroundColor : DEFAULT.backgroundColor, captionStyle.hasWindowColor() ? captionStyle.windowColor : DEFAULT.windowColor, captionStyle.hasEdgeType() ? captionStyle.edgeType : DEFAULT.edgeType, captionStyle.hasEdgeColor() ? captionStyle.edgeColor : DEFAULT.edgeColor, captionStyle.getTypeface());
    }
}
