package android.support.v4.content.res;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
@TargetApi(21)
/* loaded from: classes.dex */
class ResourcesCompatApi21 {
    ResourcesCompatApi21() {
    }

    public static Drawable getDrawable(Resources res, int id, Resources.Theme theme) throws Resources.NotFoundException {
        return res.getDrawable(id, theme);
    }

    public static Drawable getDrawableForDensity(Resources res, int id, int density, Resources.Theme theme) throws Resources.NotFoundException {
        return res.getDrawableForDensity(id, density, theme);
    }
}
