package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
@TargetApi(24)
/* loaded from: classes.dex */
class NotificationManagerCompatApi24 {
    NotificationManagerCompatApi24() {
    }

    public static boolean areNotificationsEnabled(NotificationManager notificationManager) {
        return notificationManager.areNotificationsEnabled();
    }

    public static int getImportance(NotificationManager notificationManager) {
        return notificationManager.getImportance();
    }
}
