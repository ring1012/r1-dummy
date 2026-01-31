package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import java.io.File;

@RequiresApi(19)
@TargetApi(19)
/* loaded from: classes.dex */
class ContextCompatKitKat {
    ContextCompatKitKat() {
    }

    public static File[] getExternalCacheDirs(Context context) {
        return context.getExternalCacheDirs();
    }

    public static File[] getExternalFilesDirs(Context context, String type) {
        return context.getExternalFilesDirs(type);
    }

    public static File[] getObbDirs(Context context) {
        return context.getObbDirs();
    }
}
