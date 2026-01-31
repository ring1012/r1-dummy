package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Uri;
import android.support.annotation.RequiresApi;

@RequiresApi(22)
@TargetApi(22)
/* loaded from: classes.dex */
class ActivityCompatApi22 {
    ActivityCompatApi22() {
    }

    public static Uri getReferrer(Activity activity) {
        return activity.getReferrer();
    }
}
