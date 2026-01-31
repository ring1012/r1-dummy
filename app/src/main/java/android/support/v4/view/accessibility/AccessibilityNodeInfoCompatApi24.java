package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

@RequiresApi(24)
@TargetApi(24)
/* loaded from: classes.dex */
class AccessibilityNodeInfoCompatApi24 {
    AccessibilityNodeInfoCompatApi24() {
    }

    public static Object getActionSetProgress() {
        return AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS;
    }

    public static int getDrawingOrder(Object info) {
        return ((AccessibilityNodeInfo) info).getDrawingOrder();
    }

    public static void setDrawingOrder(Object info, int drawingOrderInParent) {
        ((AccessibilityNodeInfo) info).setDrawingOrder(drawingOrderInParent);
    }

    public static boolean isImportantForAccessibility(Object info) {
        return ((AccessibilityNodeInfo) info).isImportantForAccessibility();
    }

    public static void setImportantForAccessibility(Object info, boolean importantForAccessibility) {
        ((AccessibilityNodeInfo) info).setImportantForAccessibility(importantForAccessibility);
    }
}
