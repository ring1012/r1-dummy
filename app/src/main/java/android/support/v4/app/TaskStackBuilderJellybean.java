package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@RequiresApi(16)
@TargetApi(16)
/* loaded from: classes.dex */
class TaskStackBuilderJellybean {
    TaskStackBuilderJellybean() {
    }

    public static PendingIntent getActivitiesPendingIntent(Context context, int requestCode, Intent[] intents, int flags, Bundle options) {
        return PendingIntent.getActivities(context, requestCode, intents, flags, options);
    }
}
