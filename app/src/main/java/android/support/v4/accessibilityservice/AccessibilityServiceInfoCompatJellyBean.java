package android.support.v4.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.RequiresApi;

@RequiresApi(16)
@TargetApi(16)
/* loaded from: classes.dex */
class AccessibilityServiceInfoCompatJellyBean {
    AccessibilityServiceInfoCompatJellyBean() {
    }

    public static String loadDescription(AccessibilityServiceInfo info, PackageManager pm) {
        return info.loadDescription(pm);
    }
}
