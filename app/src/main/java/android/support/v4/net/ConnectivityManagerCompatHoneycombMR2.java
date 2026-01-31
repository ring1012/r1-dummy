package android.support.v4.net;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresApi;

@RequiresApi(13)
@TargetApi(13)
/* loaded from: classes.dex */
class ConnectivityManagerCompatHoneycombMR2 {
    ConnectivityManagerCompatHoneycombMR2() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return true;
        }
        int type = info.getType();
        switch (type) {
        }
        return true;
    }
}
