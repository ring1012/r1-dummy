package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;

@RequiresApi(19)
@TargetApi(19)
/* loaded from: classes.dex */
class ViewCompatKitKat {
    ViewCompatKitKat() {
    }

    public static int getAccessibilityLiveRegion(View view) {
        return view.getAccessibilityLiveRegion();
    }

    public static void setAccessibilityLiveRegion(View view, int mode) {
        view.setAccessibilityLiveRegion(mode);
    }

    public static boolean isLaidOut(View view) {
        return view.isLaidOut();
    }

    public static boolean isAttachedToWindow(View view) {
        return view.isAttachedToWindow();
    }

    public static boolean isLayoutDirectionResolved(View view) {
        return view.isLayoutDirectionResolved();
    }
}
