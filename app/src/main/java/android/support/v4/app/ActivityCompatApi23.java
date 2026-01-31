package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompatApi21;
import android.view.View;
import java.util.List;
import java.util.Map;

@RequiresApi(23)
@TargetApi(23)
/* loaded from: classes.dex */
class ActivityCompatApi23 {

    public interface OnSharedElementsReadyListenerBridge {
        void onSharedElementsReady();
    }

    public interface RequestPermissionsRequestCodeValidator {
        void validateRequestPermissionsRequestCode(int i);
    }

    public static abstract class SharedElementCallback23 extends ActivityCompatApi21.SharedElementCallback21 {
        public abstract void onSharedElementsArrived(List<String> list, List<View> list2, OnSharedElementsReadyListenerBridge onSharedElementsReadyListenerBridge);
    }

    ActivityCompatApi23() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (activity instanceof RequestPermissionsRequestCodeValidator) {
            ((RequestPermissionsRequestCodeValidator) activity).validateRequestPermissionsRequestCode(requestCode);
        }
        activity.requestPermissions(permissions, requestCode);
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        return activity.shouldShowRequestPermissionRationale(permission);
    }

    public static void setEnterSharedElementCallback(Activity activity, SharedElementCallback23 callback) {
        activity.setEnterSharedElementCallback(createCallback(callback));
    }

    public static void setExitSharedElementCallback(Activity activity, SharedElementCallback23 callback) {
        activity.setExitSharedElementCallback(createCallback(callback));
    }

    private static android.app.SharedElementCallback createCallback(SharedElementCallback23 callback) {
        if (callback == null) {
            return null;
        }
        android.app.SharedElementCallback newListener = new SharedElementCallbackImpl(callback);
        return newListener;
    }

    private static class SharedElementCallbackImpl extends android.app.SharedElementCallback {
        private SharedElementCallback23 mCallback;

        public SharedElementCallbackImpl(SharedElementCallback23 callback) {
            this.mCallback = callback;
        }

        @Override // android.app.SharedElementCallback
        public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            this.mCallback.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @Override // android.app.SharedElementCallback
        public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
            this.mCallback.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
        }

        @Override // android.app.SharedElementCallback
        public void onRejectSharedElements(List<View> rejectedSharedElements) {
            this.mCallback.onRejectSharedElements(rejectedSharedElements);
        }

        @Override // android.app.SharedElementCallback
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            this.mCallback.onMapSharedElements(names, sharedElements);
        }

        @Override // android.app.SharedElementCallback
        public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
            return this.mCallback.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
        }

        @Override // android.app.SharedElementCallback
        public View onCreateSnapshotView(Context context, Parcelable snapshot) {
            return this.mCallback.onCreateSnapshotView(context, snapshot);
        }

        @Override // android.app.SharedElementCallback
        public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, final SharedElementCallback.OnSharedElementsReadyListener listener) {
            this.mCallback.onSharedElementsArrived(sharedElementNames, sharedElements, new OnSharedElementsReadyListenerBridge() { // from class: android.support.v4.app.ActivityCompatApi23.SharedElementCallbackImpl.1
                @Override // android.support.v4.app.ActivityCompatApi23.OnSharedElementsReadyListenerBridge
                public void onSharedElementsReady() {
                    listener.onSharedElementsReady();
                }
            });
        }
    }
}
