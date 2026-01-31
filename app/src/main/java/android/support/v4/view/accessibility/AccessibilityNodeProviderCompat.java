package android.support.v4.view.accessibility;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompatJellyBean;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class AccessibilityNodeProviderCompat {
    public static final int HOST_VIEW_ID = -1;
    private static final AccessibilityNodeProviderImpl IMPL;
    private final Object mProvider;

    interface AccessibilityNodeProviderImpl {
        Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat);
    }

    static class AccessibilityNodeProviderStubImpl implements AccessibilityNodeProviderImpl {
        AccessibilityNodeProviderStubImpl() {
        }

        @Override // android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderImpl
        public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat compat) {
            return null;
        }
    }

    private static class AccessibilityNodeProviderJellyBeanImpl extends AccessibilityNodeProviderStubImpl {
        AccessibilityNodeProviderJellyBeanImpl() {
        }

        @Override // android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderStubImpl, android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderImpl
        public Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompat compat) {
            return AccessibilityNodeProviderCompatJellyBean.newAccessibilityNodeProviderBridge(new AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge() { // from class: android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderJellyBeanImpl.1
                @Override // android.support.v4.view.accessibility.AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge
                public boolean performAction(int virtualViewId, int action, Bundle arguments) {
                    return compat.performAction(virtualViewId, action, arguments);
                }

                @Override // android.support.v4.view.accessibility.AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge
                public List<Object> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
                    List<AccessibilityNodeInfoCompat> compatInfos = compat.findAccessibilityNodeInfosByText(text, virtualViewId);
                    if (compatInfos == null) {
                        return null;
                    }
                    List<Object> infos = new ArrayList<>();
                    int infoCount = compatInfos.size();
                    for (int i = 0; i < infoCount; i++) {
                        AccessibilityNodeInfoCompat infoCompat = compatInfos.get(i);
                        infos.add(infoCompat.getInfo());
                    }
                    return infos;
                }

                @Override // android.support.v4.view.accessibility.AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge
                public Object createAccessibilityNodeInfo(int virtualViewId) {
                    AccessibilityNodeInfoCompat compatInfo = compat.createAccessibilityNodeInfo(virtualViewId);
                    if (compatInfo == null) {
                        return null;
                    }
                    return compatInfo.getInfo();
                }
            });
        }
    }

    private static class AccessibilityNodeProviderKitKatImpl extends AccessibilityNodeProviderStubImpl {
        AccessibilityNodeProviderKitKatImpl() {
        }

        @Override // android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderStubImpl, android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderImpl
        public Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompat compat) {
            return AccessibilityNodeProviderCompatKitKat.newAccessibilityNodeProviderBridge(new AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge() { // from class: android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderKitKatImpl.1
                @Override // android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge
                public boolean performAction(int virtualViewId, int action, Bundle arguments) {
                    return compat.performAction(virtualViewId, action, arguments);
                }

                @Override // android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge
                public List<Object> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
                    List<AccessibilityNodeInfoCompat> compatInfos = compat.findAccessibilityNodeInfosByText(text, virtualViewId);
                    if (compatInfos == null) {
                        return null;
                    }
                    List<Object> infos = new ArrayList<>();
                    int infoCount = compatInfos.size();
                    for (int i = 0; i < infoCount; i++) {
                        AccessibilityNodeInfoCompat infoCompat = compatInfos.get(i);
                        infos.add(infoCompat.getInfo());
                    }
                    return infos;
                }

                @Override // android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge
                public Object createAccessibilityNodeInfo(int virtualViewId) {
                    AccessibilityNodeInfoCompat compatInfo = compat.createAccessibilityNodeInfo(virtualViewId);
                    if (compatInfo == null) {
                        return null;
                    }
                    return compatInfo.getInfo();
                }

                @Override // android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge
                public Object findFocus(int focus) {
                    AccessibilityNodeInfoCompat compatInfo = compat.findFocus(focus);
                    if (compatInfo == null) {
                        return null;
                    }
                    return compatInfo.getInfo();
                }
            });
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 19) {
            IMPL = new AccessibilityNodeProviderKitKatImpl();
        } else if (Build.VERSION.SDK_INT >= 16) {
            IMPL = new AccessibilityNodeProviderJellyBeanImpl();
        } else {
            IMPL = new AccessibilityNodeProviderStubImpl();
        }
    }

    public AccessibilityNodeProviderCompat() {
        this.mProvider = IMPL.newAccessibilityNodeProviderBridge(this);
    }

    public AccessibilityNodeProviderCompat(Object provider) {
        this.mProvider = provider;
    }

    public Object getProvider() {
        return this.mProvider;
    }

    @Nullable
    public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int virtualViewId) {
        return null;
    }

    public boolean performAction(int virtualViewId, int action, Bundle arguments) {
        return false;
    }

    @Nullable
    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String text, int virtualViewId) {
        return null;
    }

    @Nullable
    public AccessibilityNodeInfoCompat findFocus(int focus) {
        return null;
    }
}
