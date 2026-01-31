package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.PopupMenu;

@RequiresApi(19)
@TargetApi(19)
/* loaded from: classes.dex */
class PopupMenuCompatKitKat {
    PopupMenuCompatKitKat() {
    }

    public static View.OnTouchListener getDragToOpenListener(Object popupMenu) {
        return ((PopupMenu) popupMenu).getDragToOpenListener();
    }
}
