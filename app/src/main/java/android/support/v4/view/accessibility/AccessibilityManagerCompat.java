package android.support.v4.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.support.v4.view.accessibility.AccessibilityManagerCompatIcs;
import android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat;
import android.view.accessibility.AccessibilityManager;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class AccessibilityManagerCompat {
    private static final AccessibilityManagerVersionImpl IMPL;

    interface AccessibilityManagerVersionImpl {
        boolean addAccessibilityStateChangeListener(AccessibilityManager accessibilityManager, AccessibilityStateChangeListener accessibilityStateChangeListener);

        boolean addTouchExplorationStateChangeListener(AccessibilityManager accessibilityManager, TouchExplorationStateChangeListener touchExplorationStateChangeListener);

        List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager accessibilityManager, int i);

        List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager accessibilityManager);

        boolean isTouchExplorationEnabled(AccessibilityManager accessibilityManager);

        AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper newAccessibilityStateChangeListener(AccessibilityStateChangeListener accessibilityStateChangeListener);

        AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper newTouchExplorationStateChangeListener(TouchExplorationStateChangeListener touchExplorationStateChangeListener);

        boolean removeAccessibilityStateChangeListener(AccessibilityManager accessibilityManager, AccessibilityStateChangeListener accessibilityStateChangeListener);

        boolean removeTouchExplorationStateChangeListener(AccessibilityManager accessibilityManager, TouchExplorationStateChangeListener touchExplorationStateChangeListener);
    }

    public interface AccessibilityStateChangeListener {
        void onAccessibilityStateChanged(boolean z);
    }

    @Deprecated
    public static abstract class AccessibilityStateChangeListenerCompat implements AccessibilityStateChangeListener {
    }

    public interface TouchExplorationStateChangeListener {
        void onTouchExplorationStateChanged(boolean z);
    }

    static class AccessibilityManagerStubImpl implements AccessibilityManagerVersionImpl {
        AccessibilityManagerStubImpl() {
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper newAccessibilityStateChangeListener(AccessibilityStateChangeListener listener) {
            return null;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public boolean addAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListener listener) {
            return false;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListener listener) {
            return false;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
            return Collections.emptyList();
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
            return Collections.emptyList();
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public boolean isTouchExplorationEnabled(AccessibilityManager manager) {
            return false;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper newTouchExplorationStateChangeListener(TouchExplorationStateChangeListener listener) {
            return null;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public boolean addTouchExplorationStateChangeListener(AccessibilityManager manager, TouchExplorationStateChangeListener listener) {
            return false;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public boolean removeTouchExplorationStateChangeListener(AccessibilityManager manager, TouchExplorationStateChangeListener listener) {
            return false;
        }
    }

    static class AccessibilityManagerIcsImpl extends AccessibilityManagerStubImpl {
        AccessibilityManagerIcsImpl() {
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerStubImpl, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper newAccessibilityStateChangeListener(final AccessibilityStateChangeListener listener) {
            return new AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper(listener, new AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge() { // from class: android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerIcsImpl.1
                @Override // android.support.v4.view.accessibility.AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge
                public void onAccessibilityStateChanged(boolean enabled) {
                    listener.onAccessibilityStateChanged(enabled);
                }
            });
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerStubImpl, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public boolean addAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListener listener) {
            return AccessibilityManagerCompatIcs.addAccessibilityStateChangeListener(manager, newAccessibilityStateChangeListener(listener));
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerStubImpl, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListener listener) {
            return AccessibilityManagerCompatIcs.removeAccessibilityStateChangeListener(manager, newAccessibilityStateChangeListener(listener));
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerStubImpl, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
            return AccessibilityManagerCompatIcs.getEnabledAccessibilityServiceList(manager, feedbackTypeFlags);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerStubImpl, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
            return AccessibilityManagerCompatIcs.getInstalledAccessibilityServiceList(manager);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerStubImpl, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public boolean isTouchExplorationEnabled(AccessibilityManager manager) {
            return AccessibilityManagerCompatIcs.isTouchExplorationEnabled(manager);
        }
    }

    static class AccessibilityManagerKitKatImpl extends AccessibilityManagerIcsImpl {
        AccessibilityManagerKitKatImpl() {
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerStubImpl, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper newTouchExplorationStateChangeListener(final TouchExplorationStateChangeListener listener) {
            return new AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper(listener, new AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge() { // from class: android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerKitKatImpl.1
                @Override // android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge
                public void onTouchExplorationStateChanged(boolean enabled) {
                    listener.onTouchExplorationStateChanged(enabled);
                }
            });
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerStubImpl, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public boolean addTouchExplorationStateChangeListener(AccessibilityManager manager, TouchExplorationStateChangeListener listener) {
            return AccessibilityManagerCompatKitKat.addTouchExplorationStateChangeListener(manager, newTouchExplorationStateChangeListener(listener));
        }

        @Override // android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerStubImpl, android.support.v4.view.accessibility.AccessibilityManagerCompat.AccessibilityManagerVersionImpl
        public boolean removeTouchExplorationStateChangeListener(AccessibilityManager manager, TouchExplorationStateChangeListener listener) {
            return AccessibilityManagerCompatKitKat.removeTouchExplorationStateChangeListener(manager, newTouchExplorationStateChangeListener(listener));
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 19) {
            IMPL = new AccessibilityManagerKitKatImpl();
        } else if (Build.VERSION.SDK_INT >= 14) {
            IMPL = new AccessibilityManagerIcsImpl();
        } else {
            IMPL = new AccessibilityManagerStubImpl();
        }
    }

    public static boolean addAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListener listener) {
        return IMPL.addAccessibilityStateChangeListener(manager, listener);
    }

    public static boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListener listener) {
        return IMPL.removeAccessibilityStateChangeListener(manager, listener);
    }

    public static List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
        return IMPL.getInstalledAccessibilityServiceList(manager);
    }

    public static List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
        return IMPL.getEnabledAccessibilityServiceList(manager, feedbackTypeFlags);
    }

    public static boolean isTouchExplorationEnabled(AccessibilityManager manager) {
        return IMPL.isTouchExplorationEnabled(manager);
    }

    public static boolean addTouchExplorationStateChangeListener(AccessibilityManager manager, TouchExplorationStateChangeListener listener) {
        return IMPL.addTouchExplorationStateChangeListener(manager, listener);
    }

    public static boolean removeTouchExplorationStateChangeListener(AccessibilityManager manager, TouchExplorationStateChangeListener listener) {
        return IMPL.removeTouchExplorationStateChangeListener(manager, listener);
    }

    private AccessibilityManagerCompat() {
    }
}
