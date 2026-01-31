package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@RequiresApi(16)
@TargetApi(16)
/* loaded from: classes.dex */
class ActivityCompatJB {
    ActivityCompatJB() {
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode, Bundle options) {
        activity.startActivityForResult(intent, requestCode, options);
    }

    public static void startIntentSenderForResult(Activity activity, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        activity.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }

    public static void finishAffinity(Activity activity) {
        activity.finishAffinity();
    }
}
