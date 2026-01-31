package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
@TargetApi(23)
/* loaded from: classes.dex */
class AppOpsManagerCompat23 {
    AppOpsManagerCompat23() {
    }

    public static String permissionToOp(String permission) {
        return AppOpsManager.permissionToOp(permission);
    }

    public static int noteOp(Context context, String op, int uid, String packageName) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        return appOpsManager.noteOp(op, uid, packageName);
    }

    public static int noteProxyOp(Context context, String op, String proxiedPackageName) {
        AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        return appOpsManager.noteProxyOp(op, proxiedPackageName);
    }
}
