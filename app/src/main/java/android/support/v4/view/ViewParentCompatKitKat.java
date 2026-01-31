package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewParent;

@RequiresApi(19)
@TargetApi(19)
/* loaded from: classes.dex */
class ViewParentCompatKitKat {
    ViewParentCompatKitKat() {
    }

    public static void notifySubtreeAccessibilityStateChanged(ViewParent parent, View child, View source, int changeType) {
        parent.notifySubtreeAccessibilityStateChanged(child, source, changeType);
    }
}
