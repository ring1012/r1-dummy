package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ListPopupWindow;

@RequiresApi(19)
@TargetApi(19)
/* loaded from: classes.dex */
class ListPopupWindowCompatKitKat {
    ListPopupWindowCompatKitKat() {
    }

    public static View.OnTouchListener createDragToOpenListener(Object listPopupWindow, View src) {
        return ((ListPopupWindow) listPopupWindow).createDragToOpenListener(src);
    }
}
