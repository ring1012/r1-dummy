package android.support.v4.app;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public final class BundleCompat {
    private BundleCompat() {
    }

    public static IBinder getBinder(Bundle bundle, String key) {
        return Build.VERSION.SDK_INT >= 18 ? BundleCompatJellybeanMR2.getBinder(bundle, key) : BundleCompatGingerbread.getBinder(bundle, key);
    }

    public static void putBinder(Bundle bundle, String key, IBinder binder) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (Build.VERSION.SDK_INT >= 18) {
            BundleCompatJellybeanMR2.putBinder(bundle, key, binder);
        } else {
            BundleCompatGingerbread.putBinder(bundle, key, binder);
        }
    }
}
