package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/* loaded from: classes.dex */
public final class ResourcesCompat {
    @Nullable
    public static Drawable getDrawable(@NonNull Resources res, @DrawableRes int id, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        return Build.VERSION.SDK_INT >= 21 ? ResourcesCompatApi21.getDrawable(res, id, theme) : res.getDrawable(id);
    }

    @Nullable
    public static Drawable getDrawableForDensity(@NonNull Resources res, @DrawableRes int id, int density, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return ResourcesCompatApi21.getDrawableForDensity(res, id, density, theme);
        }
        if (Build.VERSION.SDK_INT >= 15) {
            return ResourcesCompatIcsMr1.getDrawableForDensity(res, id, density);
        }
        return res.getDrawable(id);
    }

    @ColorInt
    public static int getColor(@NonNull Resources res, @ColorRes int id, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        return Build.VERSION.SDK_INT >= 23 ? ResourcesCompatApi23.getColor(res, id, theme) : res.getColor(id);
    }

    @Nullable
    public static ColorStateList getColorStateList(@NonNull Resources res, @ColorRes int id, @Nullable Resources.Theme theme) throws Resources.NotFoundException {
        return Build.VERSION.SDK_INT >= 23 ? ResourcesCompatApi23.getColorStateList(res, id, theme) : res.getColorStateList(id);
    }

    private ResourcesCompat() {
    }
}
