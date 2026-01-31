package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public interface DrawableWrapper {
    Drawable getWrappedDrawable();

    void setWrappedDrawable(Drawable drawable);
}
