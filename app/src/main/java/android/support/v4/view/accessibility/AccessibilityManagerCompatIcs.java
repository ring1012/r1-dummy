package android.support.v4.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityManager;
import java.util.List;

@RequiresApi(14)
@TargetApi(14)
/* loaded from: classes.dex */
class AccessibilityManagerCompatIcs {

    interface AccessibilityStateChangeListenerBridge {
        void onAccessibilityStateChanged(boolean z);
    }

    AccessibilityManagerCompatIcs() {
    }

    public static class AccessibilityStateChangeListenerWrapper implements AccessibilityManager.AccessibilityStateChangeListener {
        Object mListener;
        AccessibilityStateChangeListenerBridge mListenerBridge;

        public AccessibilityStateChangeListenerWrapper(Object listener, AccessibilityStateChangeListenerBridge listenerBridge) {
            this.mListener = listener;
            this.mListenerBridge = listenerBridge;
        }

        public int hashCode() {
            if (this.mListener == null) {
                return 0;
            }
            return this.mListener.hashCode();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            AccessibilityStateChangeListenerWrapper other = (AccessibilityStateChangeListenerWrapper) o;
            return this.mListener == null ? other.mListener == null : this.mListener.equals(other.mListener);
        }

        @Override // android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener
        public void onAccessibilityStateChanged(boolean enabled) {
            this.mListenerBridge.onAccessibilityStateChanged(enabled);
        }
    }

    public static boolean addAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListenerWrapper listener) {
        return manager.addAccessibilityStateChangeListener(listener);
    }

    public static boolean removeAccessibilityStateChangeListener(AccessibilityManager manager, AccessibilityStateChangeListenerWrapper listener) {
        return manager.removeAccessibilityStateChangeListener(listener);
    }

    public static List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager manager, int feedbackTypeFlags) {
        return manager.getEnabledAccessibilityServiceList(feedbackTypeFlags);
    }

    public static List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager manager) {
        return manager.getInstalledAccessibilityServiceList();
    }

    public static boolean isTouchExplorationEnabled(AccessibilityManager manager) {
        return manager.isTouchExplorationEnabled();
    }
}
