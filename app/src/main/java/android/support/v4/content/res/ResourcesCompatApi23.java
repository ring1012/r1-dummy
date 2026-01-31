package android.support.v4.content.res;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
@TargetApi(23)
/* loaded from: classes.dex */
class ResourcesCompatApi23 {
    ResourcesCompatApi23() {
    }

    public static int getColor(Resources res, int id, Resources.Theme theme) throws Resources.NotFoundException {
        return res.getColor(id, theme);
    }

    public static ColorStateList getColorStateList(Resources res, int id, Resources.Theme theme) throws Resources.NotFoundException {
        return res.getColorStateList(id, theme);
    }
}
