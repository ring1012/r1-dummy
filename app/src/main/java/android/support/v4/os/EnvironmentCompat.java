package android.support.v4.os;

import android.os.Build;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;

/* loaded from: classes.dex */
public final class EnvironmentCompat {
    public static final String MEDIA_UNKNOWN = "unknown";
    private static final String TAG = "EnvironmentCompat";

    public static String getStorageState(File path) throws IOException {
        int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return EnvironmentCompatKitKat.getStorageState(path);
        }
        try {
            String canonicalPath = path.getCanonicalPath();
            String canonicalExternal = Environment.getExternalStorageDirectory().getCanonicalPath();
            if (canonicalPath.startsWith(canonicalExternal)) {
                return Environment.getExternalStorageState();
            }
        } catch (IOException e) {
            Log.w(TAG, "Failed to resolve canonical path: " + e);
        }
        return "unknown";
    }

    private EnvironmentCompat() {
    }
}
