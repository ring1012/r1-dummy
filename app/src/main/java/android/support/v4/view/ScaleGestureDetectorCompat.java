package android.support.v4.view;

import android.os.Build;

/* loaded from: classes.dex */
public final class ScaleGestureDetectorCompat {
    static final ScaleGestureDetectorImpl IMPL;

    interface ScaleGestureDetectorImpl {
        boolean isQuickScaleEnabled(Object obj);

        void setQuickScaleEnabled(Object obj, boolean z);
    }

    private static class BaseScaleGestureDetectorImpl implements ScaleGestureDetectorImpl {
        BaseScaleGestureDetectorImpl() {
        }

        @Override // android.support.v4.view.ScaleGestureDetectorCompat.ScaleGestureDetectorImpl
        public void setQuickScaleEnabled(Object o, boolean enabled) {
        }

        @Override // android.support.v4.view.ScaleGestureDetectorCompat.ScaleGestureDetectorImpl
        public boolean isQuickScaleEnabled(Object o) {
            return false;
        }
    }

    private static class ScaleGestureDetectorCompatKitKatImpl implements ScaleGestureDetectorImpl {
        ScaleGestureDetectorCompatKitKatImpl() {
        }

        @Override // android.support.v4.view.ScaleGestureDetectorCompat.ScaleGestureDetectorImpl
        public void setQuickScaleEnabled(Object o, boolean enabled) {
            ScaleGestureDetectorCompatKitKat.setQuickScaleEnabled(o, enabled);
        }

        @Override // android.support.v4.view.ScaleGestureDetectorCompat.ScaleGestureDetectorImpl
        public boolean isQuickScaleEnabled(Object o) {
            return ScaleGestureDetectorCompatKitKat.isQuickScaleEnabled(o);
        }
    }

    static {
        int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            IMPL = new ScaleGestureDetectorCompatKitKatImpl();
        } else {
            IMPL = new BaseScaleGestureDetectorImpl();
        }
    }

    private ScaleGestureDetectorCompat() {
    }

    public static void setQuickScaleEnabled(Object scaleGestureDetector, boolean enabled) {
        IMPL.setQuickScaleEnabled(scaleGestureDetector, enabled);
    }

    public static boolean isQuickScaleEnabled(Object scaleGestureDetector) {
        return IMPL.isQuickScaleEnabled(scaleGestureDetector);
    }
}
