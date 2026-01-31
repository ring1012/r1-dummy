package android.support.v4.widget;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.widget.CompoundButton;

@RequiresApi(23)
@TargetApi(23)
/* loaded from: classes.dex */
class CompoundButtonCompatApi23 {
    CompoundButtonCompatApi23() {
    }

    static Drawable getButtonDrawable(CompoundButton button) {
        return button.getButtonDrawable();
    }
}
