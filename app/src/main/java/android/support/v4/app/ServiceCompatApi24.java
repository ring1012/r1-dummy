package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Service;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
@TargetApi(24)
/* loaded from: classes.dex */
class ServiceCompatApi24 {
    ServiceCompatApi24() {
    }

    public static void stopForeground(Service service, int flags) {
        service.stopForeground(flags);
    }
}
