package android.support.v4.view.accessibility;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityManager;

@RequiresApi(19)
@TargetApi(19)
/* loaded from: classes.dex */
class AccessibilityManagerCompatKitKat {

    interface TouchExplorationStateChangeListenerBridge {
        void onTouchExplorationStateChanged(boolean z);
    }

    AccessibilityManagerCompatKitKat() {
    }

    public static class TouchExplorationStateChangeListenerWrapper implements AccessibilityManager.TouchExplorationStateChangeListener {
        final Object mListener;
        final TouchExplorationStateChangeListenerBridge mListenerBridge;

        public TouchExplorationStateChangeListenerWrapper(Object listener, TouchExplorationStateChangeListenerBridge listenerBridge) {
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
            TouchExplorationStateChangeListenerWrapper other = (TouchExplorationStateChangeListenerWrapper) o;
            return this.mListener == null ? other.mListener == null : this.mListener.equals(other.mListener);
        }

        @Override // android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener
        public void onTouchExplorationStateChanged(boolean enabled) {
            this.mListenerBridge.onTouchExplorationStateChanged(enabled);
        }
    }

    public static Object newTouchExplorationStateChangeListener(final TouchExplorationStateChangeListenerBridge bridge) {
        return new AccessibilityManager.TouchExplorationStateChangeListener() { // from class: android.support.v4.view.accessibility.AccessibilityManagerCompatKitKat.1
            @Override // android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener
            public void onTouchExplorationStateChanged(boolean enabled) {
                bridge.onTouchExplorationStateChanged(enabled);
            }
        };
    }

    public static boolean addTouchExplorationStateChangeListener(AccessibilityManager manager, Object listener) {
        return manager.addTouchExplorationStateChangeListener((AccessibilityManager.TouchExplorationStateChangeListener) listener);
    }

    public static boolean removeTouchExplorationStateChangeListener(AccessibilityManager manager, Object listener) {
        return manager.removeTouchExplorationStateChangeListener((AccessibilityManager.TouchExplorationStateChangeListener) listener);
    }
}
