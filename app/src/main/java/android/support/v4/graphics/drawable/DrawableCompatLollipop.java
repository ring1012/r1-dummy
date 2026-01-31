package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.InsetDrawable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RequiresApi(21)
@TargetApi(21)
/* loaded from: classes.dex */
class DrawableCompatLollipop {
    DrawableCompatLollipop() {
    }

    public static void setHotspot(Drawable drawable, float x, float y) {
        drawable.setHotspot(x, y);
    }

    public static void setHotspotBounds(Drawable drawable, int left, int top, int right, int bottom) {
        drawable.setHotspotBounds(left, top, right, bottom);
    }

    public static void setTint(Drawable drawable, int tint) {
        drawable.setTint(tint);
    }

    public static void setTintList(Drawable drawable, ColorStateList tint) {
        drawable.setTintList(tint);
    }

    public static void setTintMode(Drawable drawable, PorterDuff.Mode tintMode) {
        drawable.setTintMode(tintMode);
    }

    public static Drawable wrapForTinting(Drawable drawable) {
        if (!(drawable instanceof TintAwareDrawable)) {
            return new DrawableWrapperLollipop(drawable);
        }
        return drawable;
    }

    public static void applyTheme(Drawable drawable, Resources.Theme t) {
        drawable.applyTheme(t);
    }

    public static boolean canApplyTheme(Drawable drawable) {
        return drawable.canApplyTheme();
    }

    public static ColorFilter getColorFilter(Drawable drawable) {
        return drawable.getColorFilter();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void clearColorFilter(Drawable drawable) {
        drawable.clearColorFilter();
        if (drawable instanceof InsetDrawable) {
            clearColorFilter(((InsetDrawable) drawable).getDrawable());
            return;
        }
        if (drawable instanceof DrawableWrapper) {
            clearColorFilter(((DrawableWrapper) drawable).getWrappedDrawable());
            return;
        }
        if (drawable instanceof DrawableContainer) {
            DrawableContainer container = (DrawableContainer) drawable;
            DrawableContainer.DrawableContainerState state = (DrawableContainer.DrawableContainerState) container.getConstantState();
            if (state != null) {
                int count = state.getChildCount();
                for (int i = 0; i < count; i++) {
                    Drawable child = state.getChild(i);
                    if (child != null) {
                        clearColorFilter(child);
                    }
                }
            }
        }
    }

    public static void inflate(Drawable drawable, Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme t) throws XmlPullParserException, IOException {
        drawable.inflate(res, parser, attrs, t);
    }
}
