package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@RequiresApi(16)
@TargetApi(16)
/* loaded from: classes.dex */
class ContextCompatJellybean {
    ContextCompatJellybean() {
    }

    public static void startActivities(Context context, Intent[] intents, Bundle options) {
        context.startActivities(intents, options);
    }

    public static void startActivity(Context context, Intent intent, Bundle options) {
        context.startActivity(intent, options);
    }
}
