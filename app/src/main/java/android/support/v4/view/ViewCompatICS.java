package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

@RequiresApi(14)
@TargetApi(14)
/* loaded from: classes.dex */
class ViewCompatICS {
    ViewCompatICS() {
    }

    public static boolean canScrollHorizontally(View v, int direction) {
        return v.canScrollHorizontally(direction);
    }

    public static boolean canScrollVertically(View v, int direction) {
        return v.canScrollVertically(direction);
    }

    public static void setAccessibilityDelegate(View v, @Nullable Object delegate) {
        v.setAccessibilityDelegate((View.AccessibilityDelegate) delegate);
    }

    public static void onPopulateAccessibilityEvent(View v, AccessibilityEvent event) {
        v.onPopulateAccessibilityEvent(event);
    }

    public static void onInitializeAccessibilityEvent(View v, AccessibilityEvent event) {
        v.onInitializeAccessibilityEvent(event);
    }

    public static void onInitializeAccessibilityNodeInfo(View v, Object info) {
        v.onInitializeAccessibilityNodeInfo((AccessibilityNodeInfo) info);
    }

    public static void setFitsSystemWindows(View view, boolean fitSystemWindows) {
        view.setFitsSystemWindows(fitSystemWindows);
    }
}
