package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@RequiresApi(17)
@TargetApi(17)
/* loaded from: classes.dex */
class MarginLayoutParamsCompatJellybeanMr1 {
    MarginLayoutParamsCompatJellybeanMr1() {
    }

    public static int getMarginStart(ViewGroup.MarginLayoutParams lp) {
        return lp.getMarginStart();
    }

    public static int getMarginEnd(ViewGroup.MarginLayoutParams lp) {
        return lp.getMarginEnd();
    }

    public static void setMarginStart(ViewGroup.MarginLayoutParams lp, int marginStart) {
        lp.setMarginStart(marginStart);
    }

    public static void setMarginEnd(ViewGroup.MarginLayoutParams lp, int marginEnd) {
        lp.setMarginEnd(marginEnd);
    }

    public static boolean isMarginRelative(ViewGroup.MarginLayoutParams lp) {
        return lp.isMarginRelative();
    }

    public static int getLayoutDirection(ViewGroup.MarginLayoutParams lp) {
        return lp.getLayoutDirection();
    }

    public static void setLayoutDirection(ViewGroup.MarginLayoutParams lp, int layoutDirection) {
        lp.setLayoutDirection(layoutDirection);
    }

    public static void resolveLayoutDirection(ViewGroup.MarginLayoutParams lp, int layoutDirection) {
        lp.resolveLayoutDirection(layoutDirection);
    }
}
