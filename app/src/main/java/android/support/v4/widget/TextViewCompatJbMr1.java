package android.support.v4.widget;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

@RequiresApi(17)
@TargetApi(17)
/* loaded from: classes.dex */
class TextViewCompatJbMr1 {
    TextViewCompatJbMr1() {
    }

    public static void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        boolean rtl = textView.getLayoutDirection() == 1;
        Drawable drawable = rtl ? end : start;
        if (!rtl) {
            start = end;
        }
        textView.setCompoundDrawables(drawable, top, start, bottom);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        boolean rtl = textView.getLayoutDirection() == 1;
        Drawable drawable = rtl ? end : start;
        if (!rtl) {
            start = end;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, top, start, bottom);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, int start, int top, int end, int bottom) {
        boolean rtl = textView.getLayoutDirection() == 1;
        int i = rtl ? end : start;
        if (!rtl) {
            start = end;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(i, top, start, bottom);
    }

    public static Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
        boolean rtl = textView.getLayoutDirection() == 1;
        Drawable[] compounds = textView.getCompoundDrawables();
        if (rtl) {
            Drawable start = compounds[2];
            Drawable end = compounds[0];
            compounds[0] = start;
            compounds[2] = end;
        }
        return compounds;
    }
}
