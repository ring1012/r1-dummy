package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
@TargetApi(19)
/* loaded from: classes.dex */
class ActivityManagerCompatKitKat {
    ActivityManagerCompatKitKat() {
    }

    public static boolean isLowRamDevice(ActivityManager am) {
        return am.isLowRamDevice();
    }
}
