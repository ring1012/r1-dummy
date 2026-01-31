package android.support.v4.content.res;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

@RequiresApi(13)
@TargetApi(13)
/* loaded from: classes.dex */
class ConfigurationHelperHoneycombMr2 {
    ConfigurationHelperHoneycombMr2() {
    }

    static int getScreenHeightDp(@NonNull Resources resources) {
        return resources.getConfiguration().screenHeightDp;
    }

    static int getScreenWidthDp(@NonNull Resources resources) {
        return resources.getConfiguration().screenWidthDp;
    }

    static int getSmallestScreenWidthDp(@NonNull Resources resources) {
        return resources.getConfiguration().smallestScreenWidthDp;
    }
}
