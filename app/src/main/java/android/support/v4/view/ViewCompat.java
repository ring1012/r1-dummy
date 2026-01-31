package android.support.v4.view;

import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.os.BuildCompat;
import android.support.v4.view.ViewCompatLollipop;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import com.unisound.sdk.ct;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

/* loaded from: classes.dex */
public class ViewCompat {
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    private static final long FAKE_FRAME_TIME = 10;
    static final ViewCompatImpl IMPL;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
    public static final int LAYER_TYPE_HARDWARE = 2;
    public static final int LAYER_TYPE_NONE = 0;
    public static final int LAYER_TYPE_SOFTWARE = 1;
    public static final int LAYOUT_DIRECTION_INHERIT = 2;
    public static final int LAYOUT_DIRECTION_LOCALE = 3;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
    public static final int MEASURED_SIZE_MASK = 16777215;
    public static final int MEASURED_STATE_MASK = -16777216;
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;

    @Deprecated
    public static final int OVER_SCROLL_ALWAYS = 0;

    @Deprecated
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;

    @Deprecated
    public static final int OVER_SCROLL_NEVER = 2;
    public static final int SCROLL_AXIS_HORIZONTAL = 1;
    public static final int SCROLL_AXIS_NONE = 0;
    public static final int SCROLL_AXIS_VERTICAL = 2;
    public static final int SCROLL_INDICATOR_BOTTOM = 2;
    public static final int SCROLL_INDICATOR_END = 32;
    public static final int SCROLL_INDICATOR_LEFT = 4;
    public static final int SCROLL_INDICATOR_RIGHT = 8;
    public static final int SCROLL_INDICATOR_START = 16;
    public static final int SCROLL_INDICATOR_TOP = 1;
    private static final String TAG = "ViewCompat";

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface FocusDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface FocusRealDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface FocusRelativeDirection {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollIndicators {
    }

    interface ViewCompatImpl {
        ViewPropertyAnimatorCompat animate(View view);

        boolean canScrollHorizontally(View view, int i);

        boolean canScrollVertically(View view, int i);

        int combineMeasuredStates(int i, int i2);

        WindowInsetsCompat dispatchApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat);

        void dispatchFinishTemporaryDetach(View view);

        boolean dispatchNestedFling(View view, float f, float f2, boolean z);

        boolean dispatchNestedPreFling(View view, float f, float f2);

        boolean dispatchNestedPreScroll(View view, int i, int i2, int[] iArr, int[] iArr2);

        boolean dispatchNestedScroll(View view, int i, int i2, int i3, int i4, int[] iArr);

        void dispatchStartTemporaryDetach(View view);

        int getAccessibilityLiveRegion(View view);

        AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view);

        float getAlpha(View view);

        ColorStateList getBackgroundTintList(View view);

        PorterDuff.Mode getBackgroundTintMode(View view);

        Rect getClipBounds(View view);

        Display getDisplay(View view);

        float getElevation(View view);

        boolean getFitsSystemWindows(View view);

        int getImportantForAccessibility(View view);

        int getLabelFor(View view);

        int getLayerType(View view);

        int getLayoutDirection(View view);

        @Nullable
        Matrix getMatrix(View view);

        int getMeasuredHeightAndState(View view);

        int getMeasuredState(View view);

        int getMeasuredWidthAndState(View view);

        int getMinimumHeight(View view);

        int getMinimumWidth(View view);

        int getPaddingEnd(View view);

        int getPaddingStart(View view);

        ViewParent getParentForAccessibility(View view);

        float getPivotX(View view);

        float getPivotY(View view);

        float getRotation(View view);

        float getRotationX(View view);

        float getRotationY(View view);

        float getScaleX(View view);

        float getScaleY(View view);

        int getScrollIndicators(View view);

        String getTransitionName(View view);

        float getTranslationX(View view);

        float getTranslationY(View view);

        float getTranslationZ(View view);

        int getWindowSystemUiVisibility(View view);

        float getX(View view);

        float getY(View view);

        float getZ(View view);

        boolean hasAccessibilityDelegate(View view);

        boolean hasNestedScrollingParent(View view);

        boolean hasOnClickListeners(View view);

        boolean hasOverlappingRendering(View view);

        boolean hasTransientState(View view);

        boolean isAttachedToWindow(View view);

        boolean isImportantForAccessibility(View view);

        boolean isInLayout(View view);

        boolean isLaidOut(View view);

        boolean isLayoutDirectionResolved(View view);

        boolean isNestedScrollingEnabled(View view);

        boolean isPaddingRelative(View view);

        void jumpDrawablesToCurrentState(View view);

        void offsetLeftAndRight(View view, int i);

        void offsetTopAndBottom(View view, int i);

        WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat);

        void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent);

        void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat);

        void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent);

        boolean performAccessibilityAction(View view, int i, Bundle bundle);

        void postInvalidateOnAnimation(View view);

        void postInvalidateOnAnimation(View view, int i, int i2, int i3, int i4);

        void postOnAnimation(View view, Runnable runnable);

        void postOnAnimationDelayed(View view, Runnable runnable, long j);

        void requestApplyInsets(View view);

        int resolveSizeAndState(int i, int i2, int i3);

        void setAccessibilityDelegate(View view, @Nullable AccessibilityDelegateCompat accessibilityDelegateCompat);

        void setAccessibilityLiveRegion(View view, int i);

        void setActivated(View view, boolean z);

        void setAlpha(View view, float f);

        void setBackground(View view, Drawable drawable);

        void setBackgroundTintList(View view, ColorStateList colorStateList);

        void setBackgroundTintMode(View view, PorterDuff.Mode mode);

        void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean z);

        void setClipBounds(View view, Rect rect);

        void setElevation(View view, float f);

        void setFitsSystemWindows(View view, boolean z);

        void setHasTransientState(View view, boolean z);

        void setImportantForAccessibility(View view, int i);

        void setLabelFor(View view, int i);

        void setLayerPaint(View view, Paint paint);

        void setLayerType(View view, int i, Paint paint);

        void setLayoutDirection(View view, int i);

        void setNestedScrollingEnabled(View view, boolean z);

        void setOnApplyWindowInsetsListener(View view, OnApplyWindowInsetsListener onApplyWindowInsetsListener);

        void setPaddingRelative(View view, int i, int i2, int i3, int i4);

        void setPivotX(View view, float f);

        void setPivotY(View view, float f);

        void setPointerIcon(View view, PointerIconCompat pointerIconCompat);

        void setRotation(View view, float f);

        void setRotationX(View view, float f);

        void setRotationY(View view, float f);

        void setSaveFromParentEnabled(View view, boolean z);

        void setScaleX(View view, float f);

        void setScaleY(View view, float f);

        void setScrollIndicators(View view, int i);

        void setScrollIndicators(View view, int i, int i2);

        void setTransitionName(View view, String str);

        void setTranslationX(View view, float f);

        void setTranslationY(View view, float f);

        void setTranslationZ(View view, float f);

        void setX(View view, float f);

        void setY(View view, float f);

        void setZ(View view, float f);

        boolean startNestedScroll(View view, int i);

        void stopNestedScroll(View view);
    }

    static class BaseViewCompatImpl implements ViewCompatImpl {
        private static Method sChildrenDrawingOrderMethod;
        private Method mDispatchFinishTemporaryDetach;
        private Method mDispatchStartTemporaryDetach;
        private boolean mTempDetachBound;
        WeakHashMap<View, ViewPropertyAnimatorCompat> mViewPropertyAnimatorCompatMap = null;

        BaseViewCompatImpl() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean canScrollHorizontally(View view, int direction) {
            return (view instanceof ScrollingView) && canScrollingViewScrollHorizontally((ScrollingView) view, direction);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean canScrollVertically(View view, int direction) {
            return (view instanceof ScrollingView) && canScrollingViewScrollVertically((ScrollingView) view, direction);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setAccessibilityDelegate(View v, AccessibilityDelegateCompat delegate) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean hasAccessibilityDelegate(View v) {
            return false;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void onPopulateAccessibilityEvent(View v, AccessibilityEvent event) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void onInitializeAccessibilityEvent(View v, AccessibilityEvent event) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void onInitializeAccessibilityNodeInfo(View v, AccessibilityNodeInfoCompat info) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean hasTransientState(View view) {
            return false;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setHasTransientState(View view, boolean hasTransientState) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void postInvalidateOnAnimation(View view) {
            view.invalidate();
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
            view.invalidate(left, top, right, bottom);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void postOnAnimation(View view, Runnable action) {
            view.postDelayed(action, getFrameTime());
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
            view.postDelayed(action, getFrameTime() + delayMillis);
        }

        long getFrameTime() {
            return ViewCompat.FAKE_FRAME_TIME;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getImportantForAccessibility(View view) {
            return 0;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setImportantForAccessibility(View view, int mode) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isImportantForAccessibility(View view) {
            return true;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean performAccessibilityAction(View view, int action, Bundle arguments) {
            return false;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
            return null;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getAlpha(View view) {
            return 1.0f;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setLayerType(View view, int layerType, Paint paint) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getLayerType(View view) {
            return 0;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getLabelFor(View view) {
            return 0;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setLabelFor(View view, int id) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setLayerPaint(View view, Paint p) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getLayoutDirection(View view) {
            return 0;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setLayoutDirection(View view, int layoutDirection) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public ViewParent getParentForAccessibility(View view) {
            return view.getParent();
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
            return View.resolveSize(size, measureSpec);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getMeasuredWidthAndState(View view) {
            return view.getMeasuredWidth();
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getMeasuredHeightAndState(View view) {
            return view.getMeasuredHeight();
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getMeasuredState(View view) {
            return 0;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getAccessibilityLiveRegion(View view) {
            return 0;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setAccessibilityLiveRegion(View view, int mode) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getPaddingStart(View view) {
            return view.getPaddingLeft();
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getPaddingEnd(View view) {
            return view.getPaddingRight();
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setPaddingRelative(View view, int start, int top, int end, int bottom) {
            view.setPadding(start, top, end, bottom);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void dispatchStartTemporaryDetach(View view) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (!this.mTempDetachBound) {
                bindTempDetach();
            }
            if (this.mDispatchStartTemporaryDetach != null) {
                try {
                    this.mDispatchStartTemporaryDetach.invoke(view, new Object[0]);
                    return;
                } catch (Exception e) {
                    Log.d(ViewCompat.TAG, "Error calling dispatchStartTemporaryDetach", e);
                    return;
                }
            }
            view.onStartTemporaryDetach();
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void dispatchFinishTemporaryDetach(View view) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (!this.mTempDetachBound) {
                bindTempDetach();
            }
            if (this.mDispatchFinishTemporaryDetach != null) {
                try {
                    this.mDispatchFinishTemporaryDetach.invoke(view, new Object[0]);
                    return;
                } catch (Exception e) {
                    Log.d(ViewCompat.TAG, "Error calling dispatchFinishTemporaryDetach", e);
                    return;
                }
            }
            view.onFinishTemporaryDetach();
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean hasOverlappingRendering(View view) {
            return true;
        }

        private void bindTempDetach() {
            try {
                this.mDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", new Class[0]);
                this.mDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", new Class[0]);
            } catch (NoSuchMethodException e) {
                Log.e(ViewCompat.TAG, "Couldn't find method", e);
            }
            this.mTempDetachBound = true;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getTranslationX(View view) {
            return 0.0f;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getTranslationY(View view) {
            return 0.0f;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getX(View view) {
            return view.getLeft();
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getY(View view) {
            return view.getTop();
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getRotation(View view) {
            return 0.0f;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getRotationX(View view) {
            return 0.0f;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getRotationY(View view) {
            return 0.0f;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getScaleX(View view) {
            return 0.0f;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getScaleY(View view) {
            return 0.0f;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public Matrix getMatrix(View view) {
            return null;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getMinimumWidth(View view) {
            return ViewCompatBase.getMinimumWidth(view);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getMinimumHeight(View view) {
            return ViewCompatBase.getMinimumHeight(view);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public ViewPropertyAnimatorCompat animate(View view) {
            return new ViewPropertyAnimatorCompat(view);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setRotation(View view, float value) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setTranslationX(View view, float value) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setTranslationY(View view, float value) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setAlpha(View view, float value) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setRotationX(View view, float value) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setRotationY(View view, float value) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setScaleX(View view, float value) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setScaleY(View view, float value) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setX(View view, float value) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setY(View view, float value) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setPivotX(View view, float value) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setPivotY(View view, float value) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getPivotX(View view) {
            return 0.0f;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getPivotY(View view) {
            return 0.0f;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setTransitionName(View view, String transitionName) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public String getTransitionName(View view) {
            return null;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getWindowSystemUiVisibility(View view) {
            return 0;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void requestApplyInsets(View view) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setElevation(View view, float elevation) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getElevation(View view) {
            return 0.0f;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setTranslationZ(View view, float translationZ) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getTranslationZ(View view) {
            return 0.0f;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setClipBounds(View view, Rect clipBounds) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public Rect getClipBounds(View view) {
            return null;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean enabled) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            if (sChildrenDrawingOrderMethod == null) {
                try {
                    sChildrenDrawingOrderMethod = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", Boolean.TYPE);
                } catch (NoSuchMethodException e) {
                    Log.e(ViewCompat.TAG, "Unable to find childrenDrawingOrderEnabled", e);
                }
                sChildrenDrawingOrderMethod.setAccessible(true);
            }
            try {
                sChildrenDrawingOrderMethod.invoke(viewGroup, Boolean.valueOf(enabled));
            } catch (IllegalAccessException e2) {
                Log.e(ViewCompat.TAG, "Unable to invoke childrenDrawingOrderEnabled", e2);
            } catch (IllegalArgumentException e3) {
                Log.e(ViewCompat.TAG, "Unable to invoke childrenDrawingOrderEnabled", e3);
            } catch (InvocationTargetException e4) {
                Log.e(ViewCompat.TAG, "Unable to invoke childrenDrawingOrderEnabled", e4);
            }
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean getFitsSystemWindows(View view) {
            return false;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setFitsSystemWindows(View view, boolean fitSystemWindows) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void jumpDrawablesToCurrentState(View view) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setOnApplyWindowInsetsListener(View view, OnApplyWindowInsetsListener listener) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
            return insets;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public WindowInsetsCompat dispatchApplyWindowInsets(View v, WindowInsetsCompat insets) {
            return insets;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setSaveFromParentEnabled(View v, boolean enabled) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setActivated(View view, boolean activated) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isPaddingRelative(View view) {
            return false;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setNestedScrollingEnabled(View view, boolean enabled) {
            if (view instanceof NestedScrollingChild) {
                ((NestedScrollingChild) view).setNestedScrollingEnabled(enabled);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isNestedScrollingEnabled(View view) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).isNestedScrollingEnabled();
            }
            return false;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setBackground(View view, Drawable background) {
            view.setBackgroundDrawable(background);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public ColorStateList getBackgroundTintList(View view) {
            return ViewCompatBase.getBackgroundTintList(view);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setBackgroundTintList(View view, ColorStateList tintList) {
            ViewCompatBase.setBackgroundTintList(view, tintList);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
            ViewCompatBase.setBackgroundTintMode(view, mode);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public PorterDuff.Mode getBackgroundTintMode(View view) {
            return ViewCompatBase.getBackgroundTintMode(view);
        }

        private boolean canScrollingViewScrollHorizontally(ScrollingView view, int direction) {
            int offset = view.computeHorizontalScrollOffset();
            int range = view.computeHorizontalScrollRange() - view.computeHorizontalScrollExtent();
            if (range == 0) {
                return false;
            }
            return direction < 0 ? offset > 0 : offset < range + (-1);
        }

        private boolean canScrollingViewScrollVertically(ScrollingView view, int direction) {
            int offset = view.computeVerticalScrollOffset();
            int range = view.computeVerticalScrollRange() - view.computeVerticalScrollExtent();
            if (range == 0) {
                return false;
            }
            return direction < 0 ? offset > 0 : offset < range + (-1);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean startNestedScroll(View view, int axes) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).startNestedScroll(axes);
            }
            return false;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void stopNestedScroll(View view) {
            if (view instanceof NestedScrollingChild) {
                ((NestedScrollingChild) view).stopNestedScroll();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean hasNestedScrollingParent(View view) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).hasNestedScrollingParent();
            }
            return false;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean dispatchNestedScroll(View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
            }
            return false;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean dispatchNestedPreScroll(View view, int dx, int dy, int[] consumed, int[] offsetInWindow) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
            }
            return false;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean dispatchNestedFling(View view, float velocityX, float velocityY, boolean consumed) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).dispatchNestedFling(velocityX, velocityY, consumed);
            }
            return false;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean dispatchNestedPreFling(View view, float velocityX, float velocityY) {
            if (view instanceof NestedScrollingChild) {
                return ((NestedScrollingChild) view).dispatchNestedPreFling(velocityX, velocityY);
            }
            return false;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isInLayout(View view) {
            return false;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isLaidOut(View view) {
            return ViewCompatBase.isLaidOut(view);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isLayoutDirectionResolved(View view) {
            return false;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int combineMeasuredStates(int curState, int newState) {
            return curState | newState;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getZ(View view) {
            return getTranslationZ(view) + getElevation(view);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setZ(View view, float z) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isAttachedToWindow(View view) {
            return ViewCompatBase.isAttachedToWindow(view);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean hasOnClickListeners(View view) {
            return false;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getScrollIndicators(View view) {
            return 0;
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setScrollIndicators(View view, int indicators) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setScrollIndicators(View view, int indicators, int mask) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void offsetLeftAndRight(View view, int offset) {
            ViewCompatBase.offsetLeftAndRight(view, offset);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void offsetTopAndBottom(View view, int offset) {
            ViewCompatBase.offsetTopAndBottom(view, offset);
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setPointerIcon(View view, PointerIconCompat pointerIcon) {
        }

        @Override // android.support.v4.view.ViewCompat.ViewCompatImpl
        public Display getDisplay(View view) {
            return ViewCompatBase.getDisplay(view);
        }
    }

    static class HCViewCompatImpl extends BaseViewCompatImpl {
        HCViewCompatImpl() {
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl
        long getFrameTime() {
            return ViewCompatHC.getFrameTime();
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getAlpha(View view) {
            return ViewCompatHC.getAlpha(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setLayerType(View view, int layerType, Paint paint) {
            ViewCompatHC.setLayerType(view, layerType, paint);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getLayerType(View view) {
            return ViewCompatHC.getLayerType(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setLayerPaint(View view, Paint paint) {
            setLayerType(view, getLayerType(view), paint);
            view.invalidate();
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
            return ViewCompatHC.resolveSizeAndState(size, measureSpec, childMeasuredState);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getMeasuredWidthAndState(View view) {
            return ViewCompatHC.getMeasuredWidthAndState(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getMeasuredHeightAndState(View view) {
            return ViewCompatHC.getMeasuredHeightAndState(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getMeasuredState(View view) {
            return ViewCompatHC.getMeasuredState(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getTranslationX(View view) {
            return ViewCompatHC.getTranslationX(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getTranslationY(View view) {
            return ViewCompatHC.getTranslationY(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public Matrix getMatrix(View view) {
            return ViewCompatHC.getMatrix(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setTranslationX(View view, float value) {
            ViewCompatHC.setTranslationX(view, value);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setTranslationY(View view, float value) {
            ViewCompatHC.setTranslationY(view, value);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setAlpha(View view, float value) {
            ViewCompatHC.setAlpha(view, value);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setX(View view, float value) {
            ViewCompatHC.setX(view, value);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setY(View view, float value) {
            ViewCompatHC.setY(view, value);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setRotation(View view, float value) {
            ViewCompatHC.setRotation(view, value);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setRotationX(View view, float value) {
            ViewCompatHC.setRotationX(view, value);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setRotationY(View view, float value) {
            ViewCompatHC.setRotationY(view, value);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setScaleX(View view, float value) {
            ViewCompatHC.setScaleX(view, value);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setScaleY(View view, float value) {
            ViewCompatHC.setScaleY(view, value);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setPivotX(View view, float value) {
            ViewCompatHC.setPivotX(view, value);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setPivotY(View view, float value) {
            ViewCompatHC.setPivotY(view, value);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getX(View view) {
            return ViewCompatHC.getX(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getY(View view) {
            return ViewCompatHC.getY(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getRotation(View view) {
            return ViewCompatHC.getRotation(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getRotationX(View view) {
            return ViewCompatHC.getRotationX(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getRotationY(View view) {
            return ViewCompatHC.getRotationY(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getScaleX(View view) {
            return ViewCompatHC.getScaleX(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getScaleY(View view) {
            return ViewCompatHC.getScaleY(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getPivotX(View view) {
            return ViewCompatHC.getPivotX(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getPivotY(View view) {
            return ViewCompatHC.getPivotY(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void jumpDrawablesToCurrentState(View view) {
            ViewCompatHC.jumpDrawablesToCurrentState(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setSaveFromParentEnabled(View view, boolean enabled) {
            ViewCompatHC.setSaveFromParentEnabled(view, enabled);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setActivated(View view, boolean activated) {
            ViewCompatHC.setActivated(view, activated);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int combineMeasuredStates(int curState, int newState) {
            return ViewCompatHC.combineMeasuredStates(curState, newState);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void offsetLeftAndRight(View view, int offset) {
            ViewCompatHC.offsetLeftAndRight(view, offset);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void offsetTopAndBottom(View view, int offset) {
            ViewCompatHC.offsetTopAndBottom(view, offset);
        }
    }

    static class ICSViewCompatImpl extends HCViewCompatImpl {
        static boolean accessibilityDelegateCheckFailed = false;
        static Field mAccessibilityDelegateField;

        ICSViewCompatImpl() {
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean canScrollHorizontally(View v, int direction) {
            return ViewCompatICS.canScrollHorizontally(v, direction);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean canScrollVertically(View v, int direction) {
            return ViewCompatICS.canScrollVertically(v, direction);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void onPopulateAccessibilityEvent(View v, AccessibilityEvent event) {
            ViewCompatICS.onPopulateAccessibilityEvent(v, event);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void onInitializeAccessibilityEvent(View v, AccessibilityEvent event) {
            ViewCompatICS.onInitializeAccessibilityEvent(v, event);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void onInitializeAccessibilityNodeInfo(View v, AccessibilityNodeInfoCompat info) {
            ViewCompatICS.onInitializeAccessibilityNodeInfo(v, info.getInfo());
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setAccessibilityDelegate(View v, @Nullable AccessibilityDelegateCompat delegate) {
            ViewCompatICS.setAccessibilityDelegate(v, delegate == null ? null : delegate.getBridge());
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean hasAccessibilityDelegate(View v) {
            if (accessibilityDelegateCheckFailed) {
                return false;
            }
            if (mAccessibilityDelegateField == null) {
                try {
                    mAccessibilityDelegateField = View.class.getDeclaredField("mAccessibilityDelegate");
                    mAccessibilityDelegateField.setAccessible(true);
                } catch (Throwable th) {
                    accessibilityDelegateCheckFailed = true;
                    return false;
                }
            }
            try {
                return mAccessibilityDelegateField.get(v) != null;
            } catch (Throwable th2) {
                accessibilityDelegateCheckFailed = true;
                return false;
            }
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public ViewPropertyAnimatorCompat animate(View view) {
            if (this.mViewPropertyAnimatorCompatMap == null) {
                this.mViewPropertyAnimatorCompatMap = new WeakHashMap<>();
            }
            ViewPropertyAnimatorCompat vpa = this.mViewPropertyAnimatorCompatMap.get(view);
            if (vpa == null) {
                ViewPropertyAnimatorCompat vpa2 = new ViewPropertyAnimatorCompat(view);
                this.mViewPropertyAnimatorCompatMap.put(view, vpa2);
                return vpa2;
            }
            return vpa;
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setFitsSystemWindows(View view, boolean fitSystemWindows) {
            ViewCompatICS.setFitsSystemWindows(view, fitSystemWindows);
        }
    }

    static class ICSMr1ViewCompatImpl extends ICSViewCompatImpl {
        ICSMr1ViewCompatImpl() {
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean hasOnClickListeners(View view) {
            return ViewCompatICSMr1.hasOnClickListeners(view);
        }
    }

    static class JBViewCompatImpl extends ICSMr1ViewCompatImpl {
        JBViewCompatImpl() {
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean hasTransientState(View view) {
            return ViewCompatJB.hasTransientState(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setHasTransientState(View view, boolean hasTransientState) {
            ViewCompatJB.setHasTransientState(view, hasTransientState);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void postInvalidateOnAnimation(View view) {
            ViewCompatJB.postInvalidateOnAnimation(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
            ViewCompatJB.postInvalidateOnAnimation(view, left, top, right, bottom);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void postOnAnimation(View view, Runnable action) {
            ViewCompatJB.postOnAnimation(view, action);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
            ViewCompatJB.postOnAnimationDelayed(view, action, delayMillis);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getImportantForAccessibility(View view) {
            return ViewCompatJB.getImportantForAccessibility(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setImportantForAccessibility(View view, int mode) {
            if (mode == 4) {
                mode = 2;
            }
            ViewCompatJB.setImportantForAccessibility(view, mode);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean performAccessibilityAction(View view, int action, Bundle arguments) {
            return ViewCompatJB.performAccessibilityAction(view, action, arguments);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
            Object compat = ViewCompatJB.getAccessibilityNodeProvider(view);
            if (compat != null) {
                return new AccessibilityNodeProviderCompat(compat);
            }
            return null;
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public ViewParent getParentForAccessibility(View view) {
            return ViewCompatJB.getParentForAccessibility(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getMinimumWidth(View view) {
            return ViewCompatJB.getMinimumWidth(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getMinimumHeight(View view) {
            return ViewCompatJB.getMinimumHeight(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void requestApplyInsets(View view) {
            ViewCompatJB.requestApplyInsets(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean getFitsSystemWindows(View view) {
            return ViewCompatJB.getFitsSystemWindows(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean hasOverlappingRendering(View view) {
            return ViewCompatJB.hasOverlappingRendering(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setBackground(View view, Drawable background) {
            ViewCompatJB.setBackground(view, background);
        }
    }

    static class JbMr1ViewCompatImpl extends JBViewCompatImpl {
        JbMr1ViewCompatImpl() {
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getLabelFor(View view) {
            return ViewCompatJellybeanMr1.getLabelFor(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setLabelFor(View view, int id) {
            ViewCompatJellybeanMr1.setLabelFor(view, id);
        }

        @Override // android.support.v4.view.ViewCompat.HCViewCompatImpl, android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setLayerPaint(View view, Paint paint) {
            ViewCompatJellybeanMr1.setLayerPaint(view, paint);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getLayoutDirection(View view) {
            return ViewCompatJellybeanMr1.getLayoutDirection(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setLayoutDirection(View view, int layoutDirection) {
            ViewCompatJellybeanMr1.setLayoutDirection(view, layoutDirection);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getPaddingStart(View view) {
            return ViewCompatJellybeanMr1.getPaddingStart(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getPaddingEnd(View view) {
            return ViewCompatJellybeanMr1.getPaddingEnd(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setPaddingRelative(View view, int start, int top, int end, int bottom) {
            ViewCompatJellybeanMr1.setPaddingRelative(view, start, top, end, bottom);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getWindowSystemUiVisibility(View view) {
            return ViewCompatJellybeanMr1.getWindowSystemUiVisibility(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isPaddingRelative(View view) {
            return ViewCompatJellybeanMr1.isPaddingRelative(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public Display getDisplay(View view) {
            return ViewCompatJellybeanMr1.getDisplay(view);
        }
    }

    static class JbMr2ViewCompatImpl extends JbMr1ViewCompatImpl {
        JbMr2ViewCompatImpl() {
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setClipBounds(View view, Rect clipBounds) {
            ViewCompatJellybeanMr2.setClipBounds(view, clipBounds);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public Rect getClipBounds(View view) {
            return ViewCompatJellybeanMr2.getClipBounds(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isInLayout(View view) {
            return ViewCompatJellybeanMr2.isInLayout(view);
        }
    }

    static class KitKatViewCompatImpl extends JbMr2ViewCompatImpl {
        KitKatViewCompatImpl() {
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getAccessibilityLiveRegion(View view) {
            return ViewCompatKitKat.getAccessibilityLiveRegion(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setAccessibilityLiveRegion(View view, int mode) {
            ViewCompatKitKat.setAccessibilityLiveRegion(view, mode);
        }

        @Override // android.support.v4.view.ViewCompat.JBViewCompatImpl, android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setImportantForAccessibility(View view, int mode) {
            ViewCompatJB.setImportantForAccessibility(view, mode);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isLaidOut(View view) {
            return ViewCompatKitKat.isLaidOut(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isLayoutDirectionResolved(View view) {
            return ViewCompatKitKat.isLayoutDirectionResolved(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isAttachedToWindow(View view) {
            return ViewCompatKitKat.isAttachedToWindow(view);
        }
    }

    static class LollipopViewCompatImpl extends KitKatViewCompatImpl {
        LollipopViewCompatImpl() {
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setTransitionName(View view, String transitionName) {
            ViewCompatLollipop.setTransitionName(view, transitionName);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public String getTransitionName(View view) {
            return ViewCompatLollipop.getTransitionName(view);
        }

        @Override // android.support.v4.view.ViewCompat.JBViewCompatImpl, android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void requestApplyInsets(View view) {
            ViewCompatLollipop.requestApplyInsets(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setElevation(View view, float elevation) {
            ViewCompatLollipop.setElevation(view, elevation);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getElevation(View view) {
            return ViewCompatLollipop.getElevation(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setTranslationZ(View view, float translationZ) {
            ViewCompatLollipop.setTranslationZ(view, translationZ);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getTranslationZ(View view) {
            return ViewCompatLollipop.getTranslationZ(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setOnApplyWindowInsetsListener(View view, final OnApplyWindowInsetsListener listener) {
            if (listener == null) {
                ViewCompatLollipop.setOnApplyWindowInsetsListener(view, null);
            } else {
                ViewCompatLollipop.OnApplyWindowInsetsListenerBridge bridge = new ViewCompatLollipop.OnApplyWindowInsetsListenerBridge() { // from class: android.support.v4.view.ViewCompat.LollipopViewCompatImpl.1
                    @Override // android.support.v4.view.ViewCompatLollipop.OnApplyWindowInsetsListenerBridge
                    public Object onApplyWindowInsets(View v, Object insets) {
                        WindowInsetsCompat compatInsets = WindowInsetsCompat.wrap(insets);
                        return WindowInsetsCompat.unwrap(listener.onApplyWindowInsets(v, compatInsets));
                    }
                };
                ViewCompatLollipop.setOnApplyWindowInsetsListener(view, bridge);
            }
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setNestedScrollingEnabled(View view, boolean enabled) {
            ViewCompatLollipop.setNestedScrollingEnabled(view, enabled);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isNestedScrollingEnabled(View view) {
            return ViewCompatLollipop.isNestedScrollingEnabled(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean startNestedScroll(View view, int axes) {
            return ViewCompatLollipop.startNestedScroll(view, axes);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void stopNestedScroll(View view) {
            ViewCompatLollipop.stopNestedScroll(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean hasNestedScrollingParent(View view) {
            return ViewCompatLollipop.hasNestedScrollingParent(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean dispatchNestedScroll(View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
            return ViewCompatLollipop.dispatchNestedScroll(view, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean dispatchNestedPreScroll(View view, int dx, int dy, int[] consumed, int[] offsetInWindow) {
            return ViewCompatLollipop.dispatchNestedPreScroll(view, dx, dy, consumed, offsetInWindow);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean dispatchNestedFling(View view, float velocityX, float velocityY, boolean consumed) {
            return ViewCompatLollipop.dispatchNestedFling(view, velocityX, velocityY, consumed);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean dispatchNestedPreFling(View view, float velocityX, float velocityY) {
            return ViewCompatLollipop.dispatchNestedPreFling(view, velocityX, velocityY);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public boolean isImportantForAccessibility(View view) {
            return ViewCompatLollipop.isImportantForAccessibility(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public ColorStateList getBackgroundTintList(View view) {
            return ViewCompatLollipop.getBackgroundTintList(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setBackgroundTintList(View view, ColorStateList tintList) {
            ViewCompatLollipop.setBackgroundTintList(view, tintList);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
            ViewCompatLollipop.setBackgroundTintMode(view, mode);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public PorterDuff.Mode getBackgroundTintMode(View view) {
            return ViewCompatLollipop.getBackgroundTintMode(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
            return WindowInsetsCompat.wrap(ViewCompatLollipop.onApplyWindowInsets(v, WindowInsetsCompat.unwrap(insets)));
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public WindowInsetsCompat dispatchApplyWindowInsets(View v, WindowInsetsCompat insets) {
            return WindowInsetsCompat.wrap(ViewCompatLollipop.dispatchApplyWindowInsets(v, WindowInsetsCompat.unwrap(insets)));
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public float getZ(View view) {
            return ViewCompatLollipop.getZ(view);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setZ(View view, float z) {
            ViewCompatLollipop.setZ(view, z);
        }

        @Override // android.support.v4.view.ViewCompat.HCViewCompatImpl, android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void offsetLeftAndRight(View view, int offset) {
            ViewCompatLollipop.offsetLeftAndRight(view, offset);
        }

        @Override // android.support.v4.view.ViewCompat.HCViewCompatImpl, android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void offsetTopAndBottom(View view, int offset) {
            ViewCompatLollipop.offsetTopAndBottom(view, offset);
        }
    }

    static class MarshmallowViewCompatImpl extends LollipopViewCompatImpl {
        MarshmallowViewCompatImpl() {
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setScrollIndicators(View view, int indicators) {
            ViewCompatMarshmallow.setScrollIndicators(view, indicators);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setScrollIndicators(View view, int indicators, int mask) {
            ViewCompatMarshmallow.setScrollIndicators(view, indicators, mask);
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public int getScrollIndicators(View view) {
            return ViewCompatMarshmallow.getScrollIndicators(view);
        }

        @Override // android.support.v4.view.ViewCompat.LollipopViewCompatImpl, android.support.v4.view.ViewCompat.HCViewCompatImpl, android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void offsetLeftAndRight(View view, int offset) {
            ViewCompatMarshmallow.offsetLeftAndRight(view, offset);
        }

        @Override // android.support.v4.view.ViewCompat.LollipopViewCompatImpl, android.support.v4.view.ViewCompat.HCViewCompatImpl, android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void offsetTopAndBottom(View view, int offset) {
            ViewCompatMarshmallow.offsetTopAndBottom(view, offset);
        }
    }

    static class Api24ViewCompatImpl extends MarshmallowViewCompatImpl {
        Api24ViewCompatImpl() {
        }

        @Override // android.support.v4.view.ViewCompat.BaseViewCompatImpl, android.support.v4.view.ViewCompat.ViewCompatImpl
        public void setPointerIcon(View view, PointerIconCompat pointerIconCompat) {
            ViewCompatApi24.setPointerIcon(view, pointerIconCompat != null ? pointerIconCompat.getPointerIcon() : null);
        }
    }

    static {
        int version = Build.VERSION.SDK_INT;
        if (BuildCompat.isAtLeastN()) {
            IMPL = new Api24ViewCompatImpl();
            return;
        }
        if (version >= 23) {
            IMPL = new MarshmallowViewCompatImpl();
            return;
        }
        if (version >= 21) {
            IMPL = new LollipopViewCompatImpl();
            return;
        }
        if (version >= 19) {
            IMPL = new KitKatViewCompatImpl();
            return;
        }
        if (version >= 18) {
            IMPL = new JbMr2ViewCompatImpl();
            return;
        }
        if (version >= 17) {
            IMPL = new JbMr1ViewCompatImpl();
            return;
        }
        if (version >= 16) {
            IMPL = new JBViewCompatImpl();
            return;
        }
        if (version >= 15) {
            IMPL = new ICSMr1ViewCompatImpl();
            return;
        }
        if (version >= 14) {
            IMPL = new ICSViewCompatImpl();
        } else if (version >= 11) {
            IMPL = new HCViewCompatImpl();
        } else {
            IMPL = new BaseViewCompatImpl();
        }
    }

    public static boolean canScrollHorizontally(View v, int direction) {
        return IMPL.canScrollHorizontally(v, direction);
    }

    public static boolean canScrollVertically(View v, int direction) {
        return IMPL.canScrollVertically(v, direction);
    }

    @Deprecated
    public static int getOverScrollMode(View v) {
        return v.getOverScrollMode();
    }

    @Deprecated
    public static void setOverScrollMode(View v, int overScrollMode) {
        v.setOverScrollMode(overScrollMode);
    }

    public static void onPopulateAccessibilityEvent(View v, AccessibilityEvent event) {
        IMPL.onPopulateAccessibilityEvent(v, event);
    }

    public static void onInitializeAccessibilityEvent(View v, AccessibilityEvent event) {
        IMPL.onInitializeAccessibilityEvent(v, event);
    }

    public static void onInitializeAccessibilityNodeInfo(View v, AccessibilityNodeInfoCompat info) {
        IMPL.onInitializeAccessibilityNodeInfo(v, info);
    }

    public static void setAccessibilityDelegate(View v, AccessibilityDelegateCompat delegate) {
        IMPL.setAccessibilityDelegate(v, delegate);
    }

    public static boolean hasAccessibilityDelegate(View v) {
        return IMPL.hasAccessibilityDelegate(v);
    }

    public static boolean hasTransientState(View view) {
        return IMPL.hasTransientState(view);
    }

    public static void setHasTransientState(View view, boolean hasTransientState) {
        IMPL.setHasTransientState(view, hasTransientState);
    }

    public static void postInvalidateOnAnimation(View view) {
        IMPL.postInvalidateOnAnimation(view);
    }

    public static void postInvalidateOnAnimation(View view, int left, int top, int right, int bottom) {
        IMPL.postInvalidateOnAnimation(view, left, top, right, bottom);
    }

    public static void postOnAnimation(View view, Runnable action) {
        IMPL.postOnAnimation(view, action);
    }

    public static void postOnAnimationDelayed(View view, Runnable action, long delayMillis) {
        IMPL.postOnAnimationDelayed(view, action, delayMillis);
    }

    public static int getImportantForAccessibility(View view) {
        return IMPL.getImportantForAccessibility(view);
    }

    public static void setImportantForAccessibility(View view, int mode) {
        IMPL.setImportantForAccessibility(view, mode);
    }

    public static boolean isImportantForAccessibility(View view) {
        return IMPL.isImportantForAccessibility(view);
    }

    public static boolean performAccessibilityAction(View view, int action, Bundle arguments) {
        return IMPL.performAccessibilityAction(view, action, arguments);
    }

    public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        return IMPL.getAccessibilityNodeProvider(view);
    }

    public static float getAlpha(View view) {
        return IMPL.getAlpha(view);
    }

    public static void setLayerType(View view, int layerType, Paint paint) {
        IMPL.setLayerType(view, layerType, paint);
    }

    public static int getLayerType(View view) {
        return IMPL.getLayerType(view);
    }

    public static int getLabelFor(View view) {
        return IMPL.getLabelFor(view);
    }

    public static void setLabelFor(View view, @IdRes int labeledId) {
        IMPL.setLabelFor(view, labeledId);
    }

    public static void setLayerPaint(View view, Paint paint) {
        IMPL.setLayerPaint(view, paint);
    }

    public static int getLayoutDirection(View view) {
        return IMPL.getLayoutDirection(view);
    }

    public static void setLayoutDirection(View view, int layoutDirection) {
        IMPL.setLayoutDirection(view, layoutDirection);
    }

    public static ViewParent getParentForAccessibility(View view) {
        return IMPL.getParentForAccessibility(view);
    }

    @Deprecated
    public static boolean isOpaque(View view) {
        return view.isOpaque();
    }

    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        return IMPL.resolveSizeAndState(size, measureSpec, childMeasuredState);
    }

    public static int getMeasuredWidthAndState(View view) {
        return IMPL.getMeasuredWidthAndState(view);
    }

    public static int getMeasuredHeightAndState(View view) {
        return IMPL.getMeasuredHeightAndState(view);
    }

    public static int getMeasuredState(View view) {
        return IMPL.getMeasuredState(view);
    }

    public static int combineMeasuredStates(int curState, int newState) {
        return IMPL.combineMeasuredStates(curState, newState);
    }

    public static int getAccessibilityLiveRegion(View view) {
        return IMPL.getAccessibilityLiveRegion(view);
    }

    public static void setAccessibilityLiveRegion(View view, int mode) {
        IMPL.setAccessibilityLiveRegion(view, mode);
    }

    public static int getPaddingStart(View view) {
        return IMPL.getPaddingStart(view);
    }

    public static int getPaddingEnd(View view) {
        return IMPL.getPaddingEnd(view);
    }

    public static void setPaddingRelative(View view, int start, int top, int end, int bottom) {
        IMPL.setPaddingRelative(view, start, top, end, bottom);
    }

    public static void dispatchStartTemporaryDetach(View view) {
        IMPL.dispatchStartTemporaryDetach(view);
    }

    public static void dispatchFinishTemporaryDetach(View view) {
        IMPL.dispatchFinishTemporaryDetach(view);
    }

    public static float getTranslationX(View view) {
        return IMPL.getTranslationX(view);
    }

    public static float getTranslationY(View view) {
        return IMPL.getTranslationY(view);
    }

    @Nullable
    public static Matrix getMatrix(View view) {
        return IMPL.getMatrix(view);
    }

    public static int getMinimumWidth(View view) {
        return IMPL.getMinimumWidth(view);
    }

    public static int getMinimumHeight(View view) {
        return IMPL.getMinimumHeight(view);
    }

    public static ViewPropertyAnimatorCompat animate(View view) {
        return IMPL.animate(view);
    }

    public static void setTranslationX(View view, float value) {
        IMPL.setTranslationX(view, value);
    }

    public static void setTranslationY(View view, float value) {
        IMPL.setTranslationY(view, value);
    }

    public static void setAlpha(View view, @FloatRange(from = 0.0d, to = ct.d) float value) {
        IMPL.setAlpha(view, value);
    }

    public static void setX(View view, float value) {
        IMPL.setX(view, value);
    }

    public static void setY(View view, float value) {
        IMPL.setY(view, value);
    }

    public static void setRotation(View view, float value) {
        IMPL.setRotation(view, value);
    }

    public static void setRotationX(View view, float value) {
        IMPL.setRotationX(view, value);
    }

    public static void setRotationY(View view, float value) {
        IMPL.setRotationY(view, value);
    }

    public static void setScaleX(View view, float value) {
        IMPL.setScaleX(view, value);
    }

    public static void setScaleY(View view, float value) {
        IMPL.setScaleY(view, value);
    }

    public static float getPivotX(View view) {
        return IMPL.getPivotX(view);
    }

    public static void setPivotX(View view, float value) {
        IMPL.setPivotX(view, value);
    }

    public static float getPivotY(View view) {
        return IMPL.getPivotY(view);
    }

    public static void setPivotY(View view, float value) {
        IMPL.setPivotY(view, value);
    }

    public static float getRotation(View view) {
        return IMPL.getRotation(view);
    }

    public static float getRotationX(View view) {
        return IMPL.getRotationX(view);
    }

    public static float getRotationY(View view) {
        return IMPL.getRotationY(view);
    }

    public static float getScaleX(View view) {
        return IMPL.getScaleX(view);
    }

    public static float getScaleY(View view) {
        return IMPL.getScaleY(view);
    }

    public static float getX(View view) {
        return IMPL.getX(view);
    }

    public static float getY(View view) {
        return IMPL.getY(view);
    }

    public static void setElevation(View view, float elevation) {
        IMPL.setElevation(view, elevation);
    }

    public static float getElevation(View view) {
        return IMPL.getElevation(view);
    }

    public static void setTranslationZ(View view, float translationZ) {
        IMPL.setTranslationZ(view, translationZ);
    }

    public static float getTranslationZ(View view) {
        return IMPL.getTranslationZ(view);
    }

    public static void setTransitionName(View view, String transitionName) {
        IMPL.setTransitionName(view, transitionName);
    }

    public static String getTransitionName(View view) {
        return IMPL.getTransitionName(view);
    }

    public static int getWindowSystemUiVisibility(View view) {
        return IMPL.getWindowSystemUiVisibility(view);
    }

    public static void requestApplyInsets(View view) {
        IMPL.requestApplyInsets(view);
    }

    public static void setChildrenDrawingOrderEnabled(ViewGroup viewGroup, boolean enabled) {
        IMPL.setChildrenDrawingOrderEnabled(viewGroup, enabled);
    }

    public static boolean getFitsSystemWindows(View v) {
        return IMPL.getFitsSystemWindows(v);
    }

    public static void setFitsSystemWindows(View view, boolean fitSystemWindows) {
        IMPL.setFitsSystemWindows(view, fitSystemWindows);
    }

    public static void jumpDrawablesToCurrentState(View v) {
        IMPL.jumpDrawablesToCurrentState(v);
    }

    public static void setOnApplyWindowInsetsListener(View v, OnApplyWindowInsetsListener listener) {
        IMPL.setOnApplyWindowInsetsListener(v, listener);
    }

    public static WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets) {
        return IMPL.onApplyWindowInsets(view, insets);
    }

    public static WindowInsetsCompat dispatchApplyWindowInsets(View view, WindowInsetsCompat insets) {
        return IMPL.dispatchApplyWindowInsets(view, insets);
    }

    public static void setSaveFromParentEnabled(View v, boolean enabled) {
        IMPL.setSaveFromParentEnabled(v, enabled);
    }

    public static void setActivated(View view, boolean activated) {
        IMPL.setActivated(view, activated);
    }

    public static boolean hasOverlappingRendering(View view) {
        return IMPL.hasOverlappingRendering(view);
    }

    public static boolean isPaddingRelative(View view) {
        return IMPL.isPaddingRelative(view);
    }

    public static void setBackground(View view, Drawable background) {
        IMPL.setBackground(view, background);
    }

    public static ColorStateList getBackgroundTintList(View view) {
        return IMPL.getBackgroundTintList(view);
    }

    public static void setBackgroundTintList(View view, ColorStateList tintList) {
        IMPL.setBackgroundTintList(view, tintList);
    }

    public static PorterDuff.Mode getBackgroundTintMode(View view) {
        return IMPL.getBackgroundTintMode(view);
    }

    public static void setBackgroundTintMode(View view, PorterDuff.Mode mode) {
        IMPL.setBackgroundTintMode(view, mode);
    }

    public static void setNestedScrollingEnabled(View view, boolean enabled) {
        IMPL.setNestedScrollingEnabled(view, enabled);
    }

    public static boolean isNestedScrollingEnabled(View view) {
        return IMPL.isNestedScrollingEnabled(view);
    }

    public static boolean startNestedScroll(View view, int axes) {
        return IMPL.startNestedScroll(view, axes);
    }

    public static void stopNestedScroll(View view) {
        IMPL.stopNestedScroll(view);
    }

    public static boolean hasNestedScrollingParent(View view) {
        return IMPL.hasNestedScrollingParent(view);
    }

    public static boolean dispatchNestedScroll(View view, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return IMPL.dispatchNestedScroll(view, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    public static boolean dispatchNestedPreScroll(View view, int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return IMPL.dispatchNestedPreScroll(view, dx, dy, consumed, offsetInWindow);
    }

    public static boolean dispatchNestedFling(View view, float velocityX, float velocityY, boolean consumed) {
        return IMPL.dispatchNestedFling(view, velocityX, velocityY, consumed);
    }

    public static boolean dispatchNestedPreFling(View view, float velocityX, float velocityY) {
        return IMPL.dispatchNestedPreFling(view, velocityX, velocityY);
    }

    public static boolean isInLayout(View view) {
        return IMPL.isInLayout(view);
    }

    public static boolean isLaidOut(View view) {
        return IMPL.isLaidOut(view);
    }

    public static boolean isLayoutDirectionResolved(View view) {
        return IMPL.isLayoutDirectionResolved(view);
    }

    public static float getZ(View view) {
        return IMPL.getZ(view);
    }

    public static void setZ(View view, float z) {
        IMPL.setZ(view, z);
    }

    public static void offsetTopAndBottom(View view, int offset) {
        IMPL.offsetTopAndBottom(view, offset);
    }

    public static void offsetLeftAndRight(View view, int offset) {
        IMPL.offsetLeftAndRight(view, offset);
    }

    public static void setClipBounds(View view, Rect clipBounds) {
        IMPL.setClipBounds(view, clipBounds);
    }

    public static Rect getClipBounds(View view) {
        return IMPL.getClipBounds(view);
    }

    public static boolean isAttachedToWindow(View view) {
        return IMPL.isAttachedToWindow(view);
    }

    public static boolean hasOnClickListeners(View view) {
        return IMPL.hasOnClickListeners(view);
    }

    public static void setScrollIndicators(@NonNull View view, int indicators) {
        IMPL.setScrollIndicators(view, indicators);
    }

    public static void setScrollIndicators(@NonNull View view, int indicators, int mask) {
        IMPL.setScrollIndicators(view, indicators, mask);
    }

    public static int getScrollIndicators(@NonNull View view) {
        return IMPL.getScrollIndicators(view);
    }

    public static void setPointerIcon(@NonNull View view, PointerIconCompat pointerIcon) {
        IMPL.setPointerIcon(view, pointerIcon);
    }

    public static Display getDisplay(@NonNull View view) {
        return IMPL.getDisplay(view);
    }

    protected ViewCompat() {
    }
}
