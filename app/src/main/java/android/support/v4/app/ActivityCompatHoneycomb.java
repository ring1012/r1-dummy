package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import java.io.FileDescriptor;
import java.io.PrintWriter;

@RequiresApi(11)
@TargetApi(11)
/* loaded from: classes.dex */
class ActivityCompatHoneycomb {
    ActivityCompatHoneycomb() {
    }

    static void invalidateOptionsMenu(Activity activity) {
        activity.invalidateOptionsMenu();
    }

    static void dump(Activity activity, String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        activity.dump(prefix, fd, writer, args);
    }
}
