package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RequiresApi(9)
@TargetApi(9)
/* loaded from: classes.dex */
class DrawableCompatBase {
    DrawableCompatBase() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void setTint(Drawable drawable, int tint) {
        if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTint(tint);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void setTintList(Drawable drawable, ColorStateList tint) {
        if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTintList(tint);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void setTintMode(Drawable drawable, PorterDuff.Mode tintMode) {
        if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTintMode(tintMode);
        }
    }

    public static Drawable wrapForTinting(Drawable drawable) {
        if (!(drawable instanceof TintAwareDrawable)) {
            return new DrawableWrapperGingerbread(drawable);
        }
        return drawable;
    }

    public static void inflate(Drawable drawable, Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme t) throws XmlPullParserException, IOException {
        drawable.inflate(res, parser, attrs);
    }
}
