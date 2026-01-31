package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.os.Build;

/* loaded from: classes.dex */
public class AccessibilityWindowInfoCompat {
    private static final AccessibilityWindowInfoImpl IMPL;
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
    public static final int TYPE_APPLICATION = 1;
    public static final int TYPE_INPUT_METHOD = 2;
    public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;
    public static final int TYPE_SYSTEM = 3;
    private static final int UNDEFINED = -1;
    private Object mInfo;

    private interface AccessibilityWindowInfoImpl {
        Object getAnchor(Object obj);

        void getBoundsInScreen(Object obj, Rect rect);

        Object getChild(Object obj, int i);

        int getChildCount(Object obj);

        int getId(Object obj);

        int getLayer(Object obj);

        Object getParent(Object obj);

        Object getRoot(Object obj);

        CharSequence getTitle(Object obj);

        int getType(Object obj);

        boolean isAccessibilityFocused(Object obj);

        boolean isActive(Object obj);

        boolean isFocused(Object obj);

        Object obtain();

        Object obtain(Object obj);

        void recycle(Object obj);
    }

    private static class AccessibilityWindowInfoStubImpl implements AccessibilityWindowInfoImpl {
        AccessibilityWindowInfoStubImpl() {
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public Object obtain() {
            return null;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public Object obtain(Object info) {
            return null;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public int getType(Object info) {
            return -1;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public int getLayer(Object info) {
            return -1;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public Object getRoot(Object info) {
            return null;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public Object getParent(Object info) {
            return null;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public int getId(Object info) {
            return -1;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public void getBoundsInScreen(Object info, Rect outBounds) {
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public boolean isActive(Object info) {
            return true;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public boolean isFocused(Object info) {
            return true;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public boolean isAccessibilityFocused(Object info) {
            return true;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public int getChildCount(Object info) {
            return 0;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public Object getChild(Object info, int index) {
            return null;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public void recycle(Object info) {
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public CharSequence getTitle(Object info) {
            return null;
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public Object getAnchor(Object info) {
            return null;
        }
    }

    private static class AccessibilityWindowInfoApi21Impl extends AccessibilityWindowInfoStubImpl {
        AccessibilityWindowInfoApi21Impl() {
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public Object obtain() {
            return AccessibilityWindowInfoCompatApi21.obtain();
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public Object obtain(Object info) {
            return AccessibilityWindowInfoCompatApi21.obtain(info);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public int getType(Object info) {
            return AccessibilityWindowInfoCompatApi21.getType(info);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public int getLayer(Object info) {
            return AccessibilityWindowInfoCompatApi21.getLayer(info);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public Object getRoot(Object info) {
            return AccessibilityWindowInfoCompatApi21.getRoot(info);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public Object getParent(Object info) {
            return AccessibilityWindowInfoCompatApi21.getParent(info);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public int getId(Object info) {
            return AccessibilityWindowInfoCompatApi21.getId(info);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public void getBoundsInScreen(Object info, Rect outBounds) {
            AccessibilityWindowInfoCompatApi21.getBoundsInScreen(info, outBounds);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public boolean isActive(Object info) {
            return AccessibilityWindowInfoCompatApi21.isActive(info);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public boolean isFocused(Object info) {
            return AccessibilityWindowInfoCompatApi21.isFocused(info);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public boolean isAccessibilityFocused(Object info) {
            return AccessibilityWindowInfoCompatApi21.isAccessibilityFocused(info);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public int getChildCount(Object info) {
            return AccessibilityWindowInfoCompatApi21.getChildCount(info);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public Object getChild(Object info, int index) {
            return AccessibilityWindowInfoCompatApi21.getChild(info, index);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public void recycle(Object info) {
            AccessibilityWindowInfoCompatApi21.recycle(info);
        }
    }

    private static class AccessibilityWindowInfoApi24Impl extends AccessibilityWindowInfoApi21Impl {
        AccessibilityWindowInfoApi24Impl() {
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public CharSequence getTitle(Object info) {
            return AccessibilityWindowInfoCompatApi24.getTitle(info);
        }

        @Override // android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoStubImpl, android.support.v4.view.accessibility.AccessibilityWindowInfoCompat.AccessibilityWindowInfoImpl
        public Object getAnchor(Object info) {
            return AccessibilityWindowInfoCompatApi24.getAnchor(info);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 24) {
            IMPL = new AccessibilityWindowInfoApi24Impl();
        } else if (Build.VERSION.SDK_INT >= 21) {
            IMPL = new AccessibilityWindowInfoApi21Impl();
        } else {
            IMPL = new AccessibilityWindowInfoStubImpl();
        }
    }

    static AccessibilityWindowInfoCompat wrapNonNullInstance(Object object) {
        if (object != null) {
            return new AccessibilityWindowInfoCompat(object);
        }
        return null;
    }

    private AccessibilityWindowInfoCompat(Object info) {
        this.mInfo = info;
    }

    public int getType() {
        return IMPL.getType(this.mInfo);
    }

    public int getLayer() {
        return IMPL.getLayer(this.mInfo);
    }

    public AccessibilityNodeInfoCompat getRoot() {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.getRoot(this.mInfo));
    }

    public AccessibilityWindowInfoCompat getParent() {
        return wrapNonNullInstance(IMPL.getParent(this.mInfo));
    }

    public int getId() {
        return IMPL.getId(this.mInfo);
    }

    public void getBoundsInScreen(Rect outBounds) {
        IMPL.getBoundsInScreen(this.mInfo, outBounds);
    }

    public boolean isActive() {
        return IMPL.isActive(this.mInfo);
    }

    public boolean isFocused() {
        return IMPL.isFocused(this.mInfo);
    }

    public boolean isAccessibilityFocused() {
        return IMPL.isAccessibilityFocused(this.mInfo);
    }

    public int getChildCount() {
        return IMPL.getChildCount(this.mInfo);
    }

    public AccessibilityWindowInfoCompat getChild(int index) {
        return wrapNonNullInstance(IMPL.getChild(this.mInfo, index));
    }

    public CharSequence getTitle() {
        return IMPL.getTitle(this.mInfo);
    }

    public AccessibilityNodeInfoCompat getAnchor() {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(IMPL.getAnchor(this.mInfo));
    }

    public static AccessibilityWindowInfoCompat obtain() {
        return wrapNonNullInstance(IMPL.obtain());
    }

    public static AccessibilityWindowInfoCompat obtain(AccessibilityWindowInfoCompat info) {
        if (info == null) {
            return null;
        }
        return wrapNonNullInstance(IMPL.obtain(info.mInfo));
    }

    public void recycle() {
        IMPL.recycle(this.mInfo);
    }

    public int hashCode() {
        if (this.mInfo == null) {
            return 0;
        }
        return this.mInfo.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            AccessibilityWindowInfoCompat other = (AccessibilityWindowInfoCompat) obj;
            return this.mInfo == null ? other.mInfo == null : this.mInfo.equals(other.mInfo);
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        Rect bounds = new Rect();
        getBoundsInScreen(bounds);
        builder.append("AccessibilityWindowInfo[");
        builder.append("id=").append(getId());
        builder.append(", type=").append(typeToString(getType()));
        builder.append(", layer=").append(getLayer());
        builder.append(", bounds=").append(bounds);
        builder.append(", focused=").append(isFocused());
        builder.append(", active=").append(isActive());
        builder.append(", hasParent=").append(getParent() != null);
        builder.append(", hasChildren=").append(getChildCount() > 0);
        builder.append(']');
        return builder.toString();
    }

    private static String typeToString(int type) {
        switch (type) {
            case 1:
                return "TYPE_APPLICATION";
            case 2:
                return "TYPE_INPUT_METHOD";
            case 3:
                return "TYPE_SYSTEM";
            case 4:
                return "TYPE_ACCESSIBILITY_OVERLAY";
            default:
                return "<UNKNOWN>";
        }
    }
}
