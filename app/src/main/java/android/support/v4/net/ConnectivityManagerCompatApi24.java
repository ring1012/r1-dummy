package android.support.v4.net;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
@TargetApi(24)
/* loaded from: classes.dex */
class ConnectivityManagerCompatApi24 {
    ConnectivityManagerCompatApi24() {
    }

    public static int getRestrictBackgroundStatus(ConnectivityManager cm) {
        return cm.getRestrictBackgroundStatus();
    }
}
