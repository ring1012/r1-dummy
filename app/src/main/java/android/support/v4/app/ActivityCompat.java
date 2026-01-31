package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompatApi21;
import android.support.v4.app.ActivityCompatApi23;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.ContextCompat;
import android.view.View;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class ActivityCompat extends ContextCompat {

    public interface OnRequestPermissionsResultCallback {
        void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr);
    }

    protected ActivityCompat() {
    }

    public static boolean invalidateOptionsMenu(Activity activity) {
        if (Build.VERSION.SDK_INT < 11) {
            return false;
        }
        ActivityCompatHoneycomb.invalidateOptionsMenu(activity);
        return true;
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode, @Nullable Bundle options) {
        if (Build.VERSION.SDK_INT >= 16) {
            ActivityCompatJB.startActivityForResult(activity, intent, requestCode, options);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void startIntentSenderForResult(Activity activity, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, @Nullable Bundle options) throws IntentSender.SendIntentException {
        if (Build.VERSION.SDK_INT >= 16) {
            ActivityCompatJB.startIntentSenderForResult(activity, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        } else {
            activity.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags);
        }
    }

    public static void finishAffinity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 16) {
            ActivityCompatJB.finishAffinity(activity);
        } else {
            activity.finish();
        }
    }

    public static void finishAfterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityCompatApi21.finishAfterTransition(activity);
        } else {
            activity.finish();
        }
    }

    @Nullable
    public static Uri getReferrer(Activity activity) {
        if (Build.VERSION.SDK_INT >= 22) {
            return ActivityCompatApi22.getReferrer(activity);
        }
        Intent intent = activity.getIntent();
        Uri referrer = (Uri) intent.getParcelableExtra("android.intent.extra.REFERRER");
        if (referrer == null) {
            String referrerName = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
            if (referrerName != null) {
                return Uri.parse(referrerName);
            }
            return null;
        }
        return referrer;
    }

    public static void setEnterSharedElementCallback(Activity activity, SharedElementCallback callback) {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompatApi23.setEnterSharedElementCallback(activity, createCallback23(callback));
        } else if (Build.VERSION.SDK_INT >= 21) {
            ActivityCompatApi21.setEnterSharedElementCallback(activity, createCallback(callback));
        }
    }

    public static void setExitSharedElementCallback(Activity activity, SharedElementCallback callback) {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompatApi23.setExitSharedElementCallback(activity, createCallback23(callback));
        } else if (Build.VERSION.SDK_INT >= 21) {
            ActivityCompatApi21.setExitSharedElementCallback(activity, createCallback(callback));
        }
    }

    public static void postponeEnterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityCompatApi21.postponeEnterTransition(activity);
        }
    }

    public static void startPostponedEnterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityCompatApi21.startPostponedEnterTransition(activity);
        }
    }

    public static void requestPermissions(@NonNull final Activity activity, @NonNull final String[] permissions, @IntRange(from = 0) final int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompatApi23.requestPermissions(activity, permissions, requestCode);
        } else if (activity instanceof OnRequestPermissionsResultCallback) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() { // from class: android.support.v4.app.ActivityCompat.1
                @Override // java.lang.Runnable
                public void run() {
                    int[] grantResults = new int[permissions.length];
                    PackageManager packageManager = activity.getPackageManager();
                    String packageName = activity.getPackageName();
                    int permissionCount = permissions.length;
                    for (int i = 0; i < permissionCount; i++) {
                        grantResults[i] = packageManager.checkPermission(permissions[i], packageName);
                    }
                    ((OnRequestPermissionsResultCallback) activity).onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            });
        }
    }

    public static boolean shouldShowRequestPermissionRationale(@NonNull Activity activity, @NonNull String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ActivityCompatApi23.shouldShowRequestPermissionRationale(activity, permission);
        }
        return false;
    }

    private static ActivityCompatApi21.SharedElementCallback21 createCallback(SharedElementCallback callback) {
        if (callback == null) {
            return null;
        }
        ActivityCompatApi21.SharedElementCallback21 newCallback = new SharedElementCallback21Impl(callback);
        return newCallback;
    }

    private static ActivityCompatApi23.SharedElementCallback23 createCallback23(SharedElementCallback callback) {
        if (callback == null) {
            return null;
        }
        ActivityCompatApi23.SharedElementCallback23 newCallback = new SharedElementCallback23Impl(callback);
        return newCallback;
    }

    private static class SharedElementCallback21Impl extends ActivityCompatApi21.SharedElementCallback21 {
        private SharedElementCallback mCallback;

        public SharedElementCallback21Impl(SharedElementCallback callback) {
            this.mCallback = callback;
        }

        @Override // android.support.v4.app.ActivityCompatApi21.SharedElementCallback21
        public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            this.mCallback.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @Override // android.support.v4.app.ActivityCompatApi21.SharedElementCallback21
        public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            this.mCallback.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @Override // android.support.v4.app.ActivityCompatApi21.SharedElementCallback21
        public void onRejectSharedElements(List<View> rejectedSharedElements) {
            this.mCallback.onRejectSharedElements(rejectedSharedElements);
        }

        @Override // android.support.v4.app.ActivityCompatApi21.SharedElementCallback21
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            this.mCallback.onMapSharedElements(names, sharedElements);
        }

        @Override // android.support.v4.app.ActivityCompatApi21.SharedElementCallback21
        public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
            return this.mCallback.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
        }

        @Override // android.support.v4.app.ActivityCompatApi21.SharedElementCallback21
        public View onCreateSnapshotView(Context context, Parcelable snapshot) {
            return this.mCallback.onCreateSnapshotView(context, snapshot);
        }
    }

    private static class SharedElementCallback23Impl extends ActivityCompatApi23.SharedElementCallback23 {
        private SharedElementCallback mCallback;

        public SharedElementCallback23Impl(SharedElementCallback callback) {
            this.mCallback = callback;
        }

        @Override // android.support.v4.app.ActivityCompatApi21.SharedElementCallback21
        public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            this.mCallback.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @Override // android.support.v4.app.ActivityCompatApi21.SharedElementCallback21
        public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            this.mCallback.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @Override // android.support.v4.app.ActivityCompatApi21.SharedElementCallback21
        public void onRejectSharedElements(List<View> rejectedSharedElements) {
            this.mCallback.onRejectSharedElements(rejectedSharedElements);
        }

        @Override // android.support.v4.app.ActivityCompatApi21.SharedElementCallback21
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            this.mCallback.onMapSharedElements(names, sharedElements);
        }

        @Override // android.support.v4.app.ActivityCompatApi21.SharedElementCallback21
        public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
            return this.mCallback.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
        }

        @Override // android.support.v4.app.ActivityCompatApi21.SharedElementCallback21
        public View onCreateSnapshotView(Context context, Parcelable snapshot) {
            return this.mCallback.onCreateSnapshotView(context, snapshot);
        }

        @Override // android.support.v4.app.ActivityCompatApi23.SharedElementCallback23
        public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, final ActivityCompatApi23.OnSharedElementsReadyListenerBridge listener) {
            this.mCallback.onSharedElementsArrived(sharedElementNames, sharedElements, new SharedElementCallback.OnSharedElementsReadyListener() { // from class: android.support.v4.app.ActivityCompat.SharedElementCallback23Impl.1
                @Override // android.support.v4.app.SharedElementCallback.OnSharedElementsReadyListener
                public void onSharedElementsReady() {
                    listener.onSharedElementsReady();
                }
            });
        }
    }
}
