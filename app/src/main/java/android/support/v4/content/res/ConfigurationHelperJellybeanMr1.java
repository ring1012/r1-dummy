package android.support.v4.content.res;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

@RequiresApi(17)
@TargetApi(17)
/* loaded from: classes.dex */
class ConfigurationHelperJellybeanMr1 {
    ConfigurationHelperJellybeanMr1() {
    }

    static int getDensityDpi(@NonNull Resources resources) {
        return resources.getConfiguration().densityDpi;
    }
}
