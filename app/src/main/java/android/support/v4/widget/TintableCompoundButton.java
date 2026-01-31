package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;

/* loaded from: classes.dex */
public interface TintableCompoundButton {
    @Nullable
    ColorStateList getSupportButtonTintList();

    @Nullable
    PorterDuff.Mode getSupportButtonTintMode();

    void setSupportButtonTintList(@Nullable ColorStateList colorStateList);

    void setSupportButtonTintMode(@Nullable PorterDuff.Mode mode);
}
