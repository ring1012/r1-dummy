package android.support.v4.content;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.os.BuildCompat;
import android.util.Log;
import android.util.TypedValue;
import java.io.File;

/* loaded from: classes.dex */
public class ContextCompat {
    private static final String DIR_ANDROID = "Android";
    private static final String DIR_OBB = "obb";
    private static final String TAG = "ContextCompat";
    private static final Object sLock = new Object();
    private static TypedValue sTempValue;

    protected ContextCompat() {
    }

    public static boolean startActivities(Context context, Intent[] intents) {
        return startActivities(context, intents, null);
    }

    public static boolean startActivities(Context context, Intent[] intents, Bundle options) {
        int version = Build.VERSION.SDK_INT;
        if (version >= 16) {
            ContextCompatJellybean.startActivities(context, intents, options);
            return true;
        }
        if (version >= 11) {
            ContextCompatHoneycomb.startActivities(context, intents);
            return true;
        }
        return false;
    }

    public static void startActivity(Context context, Intent intent, @Nullable Bundle options) {
        if (Build.VERSION.SDK_INT >= 16) {
            ContextCompatJellybean.startActivity(context, intent, options);
        } else {
            context.startActivity(intent);
        }
    }

    public static File getDataDir(Context context) {
        if (BuildCompat.isAtLeastN()) {
            return ContextCompatApi24.getDataDir(context);
        }
        String dataDir = context.getApplicationInfo().dataDir;
        if (dataDir != null) {
            return new File(dataDir);
        }
        return null;
    }

    public static File[] getObbDirs(Context context) {
        File single;
        int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            return ContextCompatKitKat.getObbDirs(context);
        }
        if (version >= 11) {
            single = ContextCompatHoneycomb.getObbDir(context);
        } else {
            single = buildPath(Environment.getExternalStorageDirectory(), DIR_ANDROID, DIR_OBB, context.getPackageName());
        }
        return new File[]{single};
    }

    public static File[] getExternalFilesDirs(Context context, String type) {
        int version = Build.VERSION.SDK_INT;
        return version >= 19 ? ContextCompatKitKat.getExternalFilesDirs(context, type) : new File[]{context.getExternalFilesDir(type)};
    }

    public static File[] getExternalCacheDirs(Context context) {
        int version = Build.VERSION.SDK_INT;
        return version >= 19 ? ContextCompatKitKat.getExternalCacheDirs(context) : new File[]{context.getExternalCacheDir()};
    }

    private static File buildPath(File base, String... segments) {
        File cur;
        int length = segments.length;
        int i = 0;
        File cur2 = base;
        while (i < length) {
            String segment = segments[i];
            if (cur2 == null) {
                cur = new File(segment);
            } else {
                cur = segment != null ? new File(cur2, segment) : cur2;
            }
            i++;
            cur2 = cur;
        }
        return cur2;
    }

    public static final Drawable getDrawable(Context context, @DrawableRes int id) {
        int resolvedId;
        int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompatApi21.getDrawable(context, id);
        }
        if (version >= 16) {
            return context.getResources().getDrawable(id);
        }
        synchronized (sLock) {
            if (sTempValue == null) {
                sTempValue = new TypedValue();
            }
            context.getResources().getValue(id, sTempValue, true);
            resolvedId = sTempValue.resourceId;
        }
        return context.getResources().getDrawable(resolvedId);
    }

    public static final ColorStateList getColorStateList(Context context, @ColorRes int id) {
        int version = Build.VERSION.SDK_INT;
        return version >= 23 ? ContextCompatApi23.getColorStateList(context, id) : context.getResources().getColorStateList(id);
    }

    @ColorInt
    public static final int getColor(Context context, @ColorRes int id) {
        int version = Build.VERSION.SDK_INT;
        return version >= 23 ? ContextCompatApi23.getColor(context, id) : context.getResources().getColor(id);
    }

    public static int checkSelfPermission(@NonNull Context context, @NonNull String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }
        return context.checkPermission(permission, Process.myPid(), Process.myUid());
    }

    public static final File getNoBackupFilesDir(Context context) {
        int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompatApi21.getNoBackupFilesDir(context);
        }
        ApplicationInfo appInfo = context.getApplicationInfo();
        return createFilesDir(new File(appInfo.dataDir, "no_backup"));
    }

    public static File getCodeCacheDir(Context context) {
        int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompatApi21.getCodeCacheDir(context);
        }
        ApplicationInfo appInfo = context.getApplicationInfo();
        return createFilesDir(new File(appInfo.dataDir, "code_cache"));
    }

    private static synchronized File createFilesDir(File file) {
        if (!file.exists() && !file.mkdirs() && !file.exists()) {
            Log.w(TAG, "Unable to create files subdir " + file.getPath());
            file = null;
        }
        return file;
    }

    public static Context createDeviceProtectedStorageContext(Context context) {
        if (BuildCompat.isAtLeastN()) {
            return ContextCompatApi24.createDeviceProtectedStorageContext(context);
        }
        return null;
    }

    public static boolean isDeviceProtectedStorage(Context context) {
        if (BuildCompat.isAtLeastN()) {
            return ContextCompatApi24.isDeviceProtectedStorage(context);
        }
        return false;
    }
}
