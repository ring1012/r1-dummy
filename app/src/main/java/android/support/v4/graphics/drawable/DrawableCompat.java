package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public final class DrawableCompat {
    static final DrawableImpl IMPL;

    interface DrawableImpl {
        void applyTheme(Drawable drawable, Resources.Theme theme);

        boolean canApplyTheme(Drawable drawable);

        void clearColorFilter(Drawable drawable);

        int getAlpha(Drawable drawable);

        ColorFilter getColorFilter(Drawable drawable);

        int getLayoutDirection(Drawable drawable);

        void inflate(Drawable drawable, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException;

        boolean isAutoMirrored(Drawable drawable);

        void jumpToCurrentState(Drawable drawable);

        void setAutoMirrored(Drawable drawable, boolean z);

        void setHotspot(Drawable drawable, float f, float f2);

        void setHotspotBounds(Drawable drawable, int i, int i2, int i3, int i4);

        boolean setLayoutDirection(Drawable drawable, int i);

        void setTint(Drawable drawable, int i);

        void setTintList(Drawable drawable, ColorStateList colorStateList);

        void setTintMode(Drawable drawable, PorterDuff.Mode mode);

        Drawable wrap(Drawable drawable);
    }

    static class BaseDrawableImpl implements DrawableImpl {
        BaseDrawableImpl() {
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void jumpToCurrentState(Drawable drawable) {
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void setAutoMirrored(Drawable drawable, boolean mirrored) {
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public boolean isAutoMirrored(Drawable drawable) {
            return false;
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void setHotspot(Drawable drawable, float x, float y) {
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void setHotspotBounds(Drawable drawable, int left, int top, int right, int bottom) {
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void setTint(Drawable drawable, int tint) {
            DrawableCompatBase.setTint(drawable, tint);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void setTintList(Drawable drawable, ColorStateList tint) {
            DrawableCompatBase.setTintList(drawable, tint);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void setTintMode(Drawable drawable, PorterDuff.Mode tintMode) {
            DrawableCompatBase.setTintMode(drawable, tintMode);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public Drawable wrap(Drawable drawable) {
            return DrawableCompatBase.wrapForTinting(drawable);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public boolean setLayoutDirection(Drawable drawable, int layoutDirection) {
            return false;
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public int getLayoutDirection(Drawable drawable) {
            return 0;
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public int getAlpha(Drawable drawable) {
            return 0;
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void applyTheme(Drawable drawable, Resources.Theme t) {
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public boolean canApplyTheme(Drawable drawable) {
            return false;
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public ColorFilter getColorFilter(Drawable drawable) {
            return null;
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void clearColorFilter(Drawable drawable) {
            drawable.clearColorFilter();
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void inflate(Drawable drawable, Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme t) throws XmlPullParserException, IOException {
            DrawableCompatBase.inflate(drawable, res, parser, attrs, t);
        }
    }

    static class HoneycombDrawableImpl extends BaseDrawableImpl {
        HoneycombDrawableImpl() {
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void jumpToCurrentState(Drawable drawable) {
            DrawableCompatHoneycomb.jumpToCurrentState(drawable);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public Drawable wrap(Drawable drawable) {
            return DrawableCompatHoneycomb.wrapForTinting(drawable);
        }
    }

    static class JellybeanMr1DrawableImpl extends HoneycombDrawableImpl {
        JellybeanMr1DrawableImpl() {
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public boolean setLayoutDirection(Drawable drawable, int layoutDirection) {
            return DrawableCompatJellybeanMr1.setLayoutDirection(drawable, layoutDirection);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public int getLayoutDirection(Drawable drawable) {
            int dir = DrawableCompatJellybeanMr1.getLayoutDirection(drawable);
            if (dir >= 0) {
                return dir;
            }
            return 0;
        }
    }

    static class KitKatDrawableImpl extends JellybeanMr1DrawableImpl {
        KitKatDrawableImpl() {
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void setAutoMirrored(Drawable drawable, boolean mirrored) {
            DrawableCompatKitKat.setAutoMirrored(drawable, mirrored);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public boolean isAutoMirrored(Drawable drawable) {
            return DrawableCompatKitKat.isAutoMirrored(drawable);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.HoneycombDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public Drawable wrap(Drawable drawable) {
            return DrawableCompatKitKat.wrapForTinting(drawable);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public int getAlpha(Drawable drawable) {
            return DrawableCompatKitKat.getAlpha(drawable);
        }
    }

    static class LollipopDrawableImpl extends KitKatDrawableImpl {
        LollipopDrawableImpl() {
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void setHotspot(Drawable drawable, float x, float y) {
            DrawableCompatLollipop.setHotspot(drawable, x, y);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void setHotspotBounds(Drawable drawable, int left, int top, int right, int bottom) {
            DrawableCompatLollipop.setHotspotBounds(drawable, left, top, right, bottom);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void setTint(Drawable drawable, int tint) {
            DrawableCompatLollipop.setTint(drawable, tint);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void setTintList(Drawable drawable, ColorStateList tint) {
            DrawableCompatLollipop.setTintList(drawable, tint);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void setTintMode(Drawable drawable, PorterDuff.Mode tintMode) {
            DrawableCompatLollipop.setTintMode(drawable, tintMode);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.KitKatDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.HoneycombDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public Drawable wrap(Drawable drawable) {
            return DrawableCompatLollipop.wrapForTinting(drawable);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void applyTheme(Drawable drawable, Resources.Theme t) {
            DrawableCompatLollipop.applyTheme(drawable, t);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public boolean canApplyTheme(Drawable drawable) {
            return DrawableCompatLollipop.canApplyTheme(drawable);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public ColorFilter getColorFilter(Drawable drawable) {
            return DrawableCompatLollipop.getColorFilter(drawable);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void clearColorFilter(Drawable drawable) {
            DrawableCompatLollipop.clearColorFilter(drawable);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void inflate(Drawable drawable, Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme t) throws XmlPullParserException, IOException {
            DrawableCompatLollipop.inflate(drawable, res, parser, attrs, t);
        }
    }

    static class MDrawableImpl extends LollipopDrawableImpl {
        MDrawableImpl() {
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.JellybeanMr1DrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public boolean setLayoutDirection(Drawable drawable, int layoutDirection) {
            return DrawableCompatApi23.setLayoutDirection(drawable, layoutDirection);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.JellybeanMr1DrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public int getLayoutDirection(Drawable drawable) {
            return DrawableCompatApi23.getLayoutDirection(drawable);
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.LollipopDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.KitKatDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.HoneycombDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public Drawable wrap(Drawable drawable) {
            return drawable;
        }

        @Override // android.support.v4.graphics.drawable.DrawableCompat.LollipopDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.BaseDrawableImpl, android.support.v4.graphics.drawable.DrawableCompat.DrawableImpl
        public void clearColorFilter(Drawable drawable) {
            drawable.clearColorFilter();
        }
    }

    static {
        int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            IMPL = new MDrawableImpl();
            return;
        }
        if (version >= 21) {
            IMPL = new LollipopDrawableImpl();
            return;
        }
        if (version >= 19) {
            IMPL = new KitKatDrawableImpl();
            return;
        }
        if (version >= 17) {
            IMPL = new JellybeanMr1DrawableImpl();
        } else if (version >= 11) {
            IMPL = new HoneycombDrawableImpl();
        } else {
            IMPL = new BaseDrawableImpl();
        }
    }

    public static void jumpToCurrentState(@NonNull Drawable drawable) {
        IMPL.jumpToCurrentState(drawable);
    }

    public static void setAutoMirrored(@NonNull Drawable drawable, boolean mirrored) {
        IMPL.setAutoMirrored(drawable, mirrored);
    }

    public static boolean isAutoMirrored(@NonNull Drawable drawable) {
        return IMPL.isAutoMirrored(drawable);
    }

    public static void setHotspot(@NonNull Drawable drawable, float x, float y) {
        IMPL.setHotspot(drawable, x, y);
    }

    public static void setHotspotBounds(@NonNull Drawable drawable, int left, int top, int right, int bottom) {
        IMPL.setHotspotBounds(drawable, left, top, right, bottom);
    }

    public static void setTint(@NonNull Drawable drawable, @ColorInt int tint) {
        IMPL.setTint(drawable, tint);
    }

    public static void setTintList(@NonNull Drawable drawable, @Nullable ColorStateList tint) {
        IMPL.setTintList(drawable, tint);
    }

    public static void setTintMode(@NonNull Drawable drawable, @Nullable PorterDuff.Mode tintMode) {
        IMPL.setTintMode(drawable, tintMode);
    }

    public static int getAlpha(@NonNull Drawable drawable) {
        return IMPL.getAlpha(drawable);
    }

    public static void applyTheme(@NonNull Drawable drawable, @NonNull Resources.Theme t) {
        IMPL.applyTheme(drawable, t);
    }

    public static boolean canApplyTheme(@NonNull Drawable drawable) {
        return IMPL.canApplyTheme(drawable);
    }

    public static ColorFilter getColorFilter(@NonNull Drawable drawable) {
        return IMPL.getColorFilter(drawable);
    }

    public static void clearColorFilter(@NonNull Drawable drawable) {
        IMPL.clearColorFilter(drawable);
    }

    public static void inflate(@NonNull Drawable drawable, @NonNull Resources res, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Resources.Theme theme) throws XmlPullParserException, IOException {
        IMPL.inflate(drawable, res, parser, attrs, theme);
    }

    public static Drawable wrap(@NonNull Drawable drawable) {
        return IMPL.wrap(drawable);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T extends Drawable> T unwrap(@NonNull Drawable drawable) {
        if (drawable instanceof DrawableWrapper) {
            return (T) ((DrawableWrapper) drawable).getWrappedDrawable();
        }
        return drawable;
    }

    public static boolean setLayoutDirection(@NonNull Drawable drawable, int layoutDirection) {
        return IMPL.setLayoutDirection(drawable, layoutDirection);
    }

    public static int getLayoutDirection(@NonNull Drawable drawable) {
        return IMPL.getLayoutDirection(drawable);
    }

    private DrawableCompat() {
    }
}
