package android.support.v4.view;

import android.os.Build;
import android.view.ViewConfiguration;

/* loaded from: classes.dex */
public final class ViewConfigurationCompat {
    static final ViewConfigurationVersionImpl IMPL;

    interface ViewConfigurationVersionImpl {
        boolean hasPermanentMenuKey(ViewConfiguration viewConfiguration);
    }

    static class BaseViewConfigurationVersionImpl implements ViewConfigurationVersionImpl {
        BaseViewConfigurationVersionImpl() {
        }

        @Override // android.support.v4.view.ViewConfigurationCompat.ViewConfigurationVersionImpl
        public boolean hasPermanentMenuKey(ViewConfiguration config) {
            return true;
        }
    }

    static class HoneycombViewConfigurationVersionImpl extends BaseViewConfigurationVersionImpl {
        HoneycombViewConfigurationVersionImpl() {
        }

        @Override // android.support.v4.view.ViewConfigurationCompat.BaseViewConfigurationVersionImpl, android.support.v4.view.ViewConfigurationCompat.ViewConfigurationVersionImpl
        public boolean hasPermanentMenuKey(ViewConfiguration config) {
            return false;
        }
    }

    static class IcsViewConfigurationVersionImpl extends HoneycombViewConfigurationVersionImpl {
        IcsViewConfigurationVersionImpl() {
        }

        @Override // android.support.v4.view.ViewConfigurationCompat.HoneycombViewConfigurationVersionImpl, android.support.v4.view.ViewConfigurationCompat.BaseViewConfigurationVersionImpl, android.support.v4.view.ViewConfigurationCompat.ViewConfigurationVersionImpl
        public boolean hasPermanentMenuKey(ViewConfiguration config) {
            return ViewConfigurationCompatICS.hasPermanentMenuKey(config);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 14) {
            IMPL = new IcsViewConfigurationVersionImpl();
        } else if (Build.VERSION.SDK_INT >= 11) {
            IMPL = new HoneycombViewConfigurationVersionImpl();
        } else {
            IMPL = new BaseViewConfigurationVersionImpl();
        }
    }

    @Deprecated
    public static int getScaledPagingTouchSlop(ViewConfiguration config) {
        return config.getScaledPagingTouchSlop();
    }

    public static boolean hasPermanentMenuKey(ViewConfiguration config) {
        return IMPL.hasPermanentMenuKey(config);
    }

    private ViewConfigurationCompat() {
    }
}
