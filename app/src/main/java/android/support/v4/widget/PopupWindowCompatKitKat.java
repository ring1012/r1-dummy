package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.PopupWindow;

@RequiresApi(19)
@TargetApi(19)
/* loaded from: classes.dex */
class PopupWindowCompatKitKat {
    PopupWindowCompatKitKat() {
    }

    public static void showAsDropDown(PopupWindow popup, View anchor, int xoff, int yoff, int gravity) {
        popup.showAsDropDown(anchor, xoff, yoff, gravity);
    }
}
