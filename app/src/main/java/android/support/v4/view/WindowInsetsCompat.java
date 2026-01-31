package android.support.v4.view;

import android.graphics.Rect;
import android.os.Build;

/* loaded from: classes.dex */
public class WindowInsetsCompat {
    private static final WindowInsetsCompatImpl IMPL;
    private final Object mInsets;

    private interface WindowInsetsCompatImpl {
        WindowInsetsCompat consumeStableInsets(Object obj);

        WindowInsetsCompat consumeSystemWindowInsets(Object obj);

        Object getSourceWindowInsets(Object obj);

        int getStableInsetBottom(Object obj);

        int getStableInsetLeft(Object obj);

        int getStableInsetRight(Object obj);

        int getStableInsetTop(Object obj);

        int getSystemWindowInsetBottom(Object obj);

        int getSystemWindowInsetLeft(Object obj);

        int getSystemWindowInsetRight(Object obj);

        int getSystemWindowInsetTop(Object obj);

        boolean hasInsets(Object obj);

        boolean hasStableInsets(Object obj);

        boolean hasSystemWindowInsets(Object obj);

        boolean isConsumed(Object obj);

        boolean isRound(Object obj);

        WindowInsetsCompat replaceSystemWindowInsets(Object obj, int i, int i2, int i3, int i4);

        WindowInsetsCompat replaceSystemWindowInsets(Object obj, Rect rect);
    }

    private static class WindowInsetsCompatBaseImpl implements WindowInsetsCompatImpl {
        WindowInsetsCompatBaseImpl() {
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getSystemWindowInsetLeft(Object insets) {
            return 0;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getSystemWindowInsetTop(Object insets) {
            return 0;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getSystemWindowInsetRight(Object insets) {
            return 0;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getSystemWindowInsetBottom(Object insets) {
            return 0;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public boolean hasSystemWindowInsets(Object insets) {
            return false;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public boolean hasInsets(Object insets) {
            return false;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public boolean isConsumed(Object insets) {
            return false;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public boolean isRound(Object insets) {
            return false;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public WindowInsetsCompat consumeSystemWindowInsets(Object insets) {
            return null;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public WindowInsetsCompat replaceSystemWindowInsets(Object insets, int left, int top, int right, int bottom) {
            return null;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public WindowInsetsCompat replaceSystemWindowInsets(Object insets, Rect systemWindowInsets) {
            return null;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getStableInsetTop(Object insets) {
            return 0;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getStableInsetLeft(Object insets) {
            return 0;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getStableInsetRight(Object insets) {
            return 0;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getStableInsetBottom(Object insets) {
            return 0;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public boolean hasStableInsets(Object insets) {
            return false;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public WindowInsetsCompat consumeStableInsets(Object insets) {
            return null;
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public Object getSourceWindowInsets(Object src) {
            return null;
        }
    }

    private static class WindowInsetsCompatApi20Impl extends WindowInsetsCompatBaseImpl {
        WindowInsetsCompatApi20Impl() {
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public WindowInsetsCompat consumeSystemWindowInsets(Object insets) {
            return new WindowInsetsCompat(WindowInsetsCompatApi20.consumeSystemWindowInsets(insets));
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getSystemWindowInsetBottom(Object insets) {
            return WindowInsetsCompatApi20.getSystemWindowInsetBottom(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getSystemWindowInsetLeft(Object insets) {
            return WindowInsetsCompatApi20.getSystemWindowInsetLeft(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getSystemWindowInsetRight(Object insets) {
            return WindowInsetsCompatApi20.getSystemWindowInsetRight(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getSystemWindowInsetTop(Object insets) {
            return WindowInsetsCompatApi20.getSystemWindowInsetTop(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public boolean hasInsets(Object insets) {
            return WindowInsetsCompatApi20.hasInsets(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public boolean hasSystemWindowInsets(Object insets) {
            return WindowInsetsCompatApi20.hasSystemWindowInsets(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public boolean isRound(Object insets) {
            return WindowInsetsCompatApi20.isRound(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public WindowInsetsCompat replaceSystemWindowInsets(Object insets, int left, int top, int right, int bottom) {
            return new WindowInsetsCompat(WindowInsetsCompatApi20.replaceSystemWindowInsets(insets, left, top, right, bottom));
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public Object getSourceWindowInsets(Object src) {
            return WindowInsetsCompatApi20.getSourceWindowInsets(src);
        }
    }

    private static class WindowInsetsCompatApi21Impl extends WindowInsetsCompatApi20Impl {
        WindowInsetsCompatApi21Impl() {
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public WindowInsetsCompat consumeStableInsets(Object insets) {
            return new WindowInsetsCompat(WindowInsetsCompatApi21.consumeStableInsets(insets));
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getStableInsetBottom(Object insets) {
            return WindowInsetsCompatApi21.getStableInsetBottom(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getStableInsetLeft(Object insets) {
            return WindowInsetsCompatApi21.getStableInsetLeft(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getStableInsetRight(Object insets) {
            return WindowInsetsCompatApi21.getStableInsetRight(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public int getStableInsetTop(Object insets) {
            return WindowInsetsCompatApi21.getStableInsetTop(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public boolean hasStableInsets(Object insets) {
            return WindowInsetsCompatApi21.hasStableInsets(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public boolean isConsumed(Object insets) {
            return WindowInsetsCompatApi21.isConsumed(insets);
        }

        @Override // android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatBaseImpl, android.support.v4.view.WindowInsetsCompat.WindowInsetsCompatImpl
        public WindowInsetsCompat replaceSystemWindowInsets(Object insets, Rect systemWindowInsets) {
            return new WindowInsetsCompat(WindowInsetsCompatApi21.replaceSystemWindowInsets(insets, systemWindowInsets));
        }
    }

    static {
        int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            IMPL = new WindowInsetsCompatApi21Impl();
        } else if (version >= 20) {
            IMPL = new WindowInsetsCompatApi20Impl();
        } else {
            IMPL = new WindowInsetsCompatBaseImpl();
        }
    }

    WindowInsetsCompat(Object insets) {
        this.mInsets = insets;
    }

    public WindowInsetsCompat(WindowInsetsCompat src) {
        this.mInsets = src == null ? null : IMPL.getSourceWindowInsets(src.mInsets);
    }

    public int getSystemWindowInsetLeft() {
        return IMPL.getSystemWindowInsetLeft(this.mInsets);
    }

    public int getSystemWindowInsetTop() {
        return IMPL.getSystemWindowInsetTop(this.mInsets);
    }

    public int getSystemWindowInsetRight() {
        return IMPL.getSystemWindowInsetRight(this.mInsets);
    }

    public int getSystemWindowInsetBottom() {
        return IMPL.getSystemWindowInsetBottom(this.mInsets);
    }

    public boolean hasSystemWindowInsets() {
        return IMPL.hasSystemWindowInsets(this.mInsets);
    }

    public boolean hasInsets() {
        return IMPL.hasInsets(this.mInsets);
    }

    public boolean isConsumed() {
        return IMPL.isConsumed(this.mInsets);
    }

    public boolean isRound() {
        return IMPL.isRound(this.mInsets);
    }

    public WindowInsetsCompat consumeSystemWindowInsets() {
        return IMPL.consumeSystemWindowInsets(this.mInsets);
    }

    public WindowInsetsCompat replaceSystemWindowInsets(int left, int top, int right, int bottom) {
        return IMPL.replaceSystemWindowInsets(this.mInsets, left, top, right, bottom);
    }

    public WindowInsetsCompat replaceSystemWindowInsets(Rect systemWindowInsets) {
        return IMPL.replaceSystemWindowInsets(this.mInsets, systemWindowInsets);
    }

    public int getStableInsetTop() {
        return IMPL.getStableInsetTop(this.mInsets);
    }

    public int getStableInsetLeft() {
        return IMPL.getStableInsetLeft(this.mInsets);
    }

    public int getStableInsetRight() {
        return IMPL.getStableInsetRight(this.mInsets);
    }

    public int getStableInsetBottom() {
        return IMPL.getStableInsetBottom(this.mInsets);
    }

    public boolean hasStableInsets() {
        return IMPL.hasStableInsets(this.mInsets);
    }

    public WindowInsetsCompat consumeStableInsets() {
        return IMPL.consumeStableInsets(this.mInsets);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WindowInsetsCompat other = (WindowInsetsCompat) o;
        return this.mInsets == null ? other.mInsets == null : this.mInsets.equals(other.mInsets);
    }

    public int hashCode() {
        if (this.mInsets == null) {
            return 0;
        }
        return this.mInsets.hashCode();
    }

    static WindowInsetsCompat wrap(Object insets) {
        if (insets == null) {
            return null;
        }
        return new WindowInsetsCompat(insets);
    }

    static Object unwrap(WindowInsetsCompat insets) {
        if (insets == null) {
            return null;
        }
        return insets.mInsets;
    }
}
