package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.PointerIcon;
import android.view.View;

@RequiresApi(24)
@TargetApi(24)
/* loaded from: classes.dex */
class ViewCompatApi24 {
    ViewCompatApi24() {
    }

    public static void setPointerIcon(View view, Object pointerIcon) {
        view.setPointerIcon((PointerIcon) pointerIcon);
    }
}
