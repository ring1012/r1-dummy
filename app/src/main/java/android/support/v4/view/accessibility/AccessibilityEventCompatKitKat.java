package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;

@RequiresApi(19)
@TargetApi(19)
/* loaded from: classes.dex */
class AccessibilityEventCompatKitKat {
    AccessibilityEventCompatKitKat() {
    }

    public static void setContentChangeTypes(AccessibilityEvent event, int changeTypes) {
        event.setContentChangeTypes(changeTypes);
    }

    public static int getContentChangeTypes(AccessibilityEvent event) {
        return event.getContentChangeTypes();
    }
}
