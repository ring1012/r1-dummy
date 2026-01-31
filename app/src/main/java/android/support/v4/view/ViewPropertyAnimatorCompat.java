package android.support.v4.view;

import android.os.Build;
import android.view.View;
import android.view.animation.Interpolator;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/* loaded from: classes.dex */
public final class ViewPropertyAnimatorCompat {
    static final ViewPropertyAnimatorCompatImpl IMPL;
    static final int LISTENER_TAG_ID = 2113929216;
    private static final String TAG = "ViewAnimatorCompat";
    private WeakReference<View> mView;
    Runnable mStartAction = null;
    Runnable mEndAction = null;
    int mOldLayerType = -1;

    interface ViewPropertyAnimatorCompatImpl {
        void alpha(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void alphaBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void cancel(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view);

        long getDuration(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view);

        Interpolator getInterpolator(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view);

        long getStartDelay(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view);

        void rotation(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void rotationBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void rotationX(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void rotationXBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void rotationY(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void rotationYBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void scaleX(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void scaleXBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void scaleY(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void scaleYBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void setDuration(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, long j);

        void setInterpolator(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Interpolator interpolator);

        void setListener(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener);

        void setStartDelay(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, long j);

        void setUpdateListener(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, ViewPropertyAnimatorUpdateListener viewPropertyAnimatorUpdateListener);

        void start(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view);

        void translationX(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void translationXBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void translationY(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void translationYBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void translationZ(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void translationZBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void withEndAction(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Runnable runnable);

        void withLayer(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view);

        void withStartAction(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, Runnable runnable);

        void x(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void xBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void y(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void yBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void z(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);

        void zBy(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat, View view, float f);
    }

    ViewPropertyAnimatorCompat(View view) {
        this.mView = new WeakReference<>(view);
    }

    static class BaseViewPropertyAnimatorCompatImpl implements ViewPropertyAnimatorCompatImpl {
        WeakHashMap<View, Runnable> mStarterMap = null;

        BaseViewPropertyAnimatorCompatImpl() {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setDuration(ViewPropertyAnimatorCompat vpa, View view, long value) {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void alpha(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationX(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationY(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withEndAction(ViewPropertyAnimatorCompat vpa, View view, Runnable runnable) {
            vpa.mEndAction = runnable;
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public long getDuration(ViewPropertyAnimatorCompat vpa, View view) {
            return 0L;
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setInterpolator(ViewPropertyAnimatorCompat vpa, View view, Interpolator value) {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public Interpolator getInterpolator(ViewPropertyAnimatorCompat vpa, View view) {
            return null;
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setStartDelay(ViewPropertyAnimatorCompat vpa, View view, long value) {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public long getStartDelay(ViewPropertyAnimatorCompat vpa, View view) {
            return 0L;
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void alphaBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotation(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationX(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationXBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationY(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationYBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleX(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleXBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleY(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleYBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void cancel(ViewPropertyAnimatorCompat vpa, View view) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void x(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void xBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void y(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void yBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void z(ViewPropertyAnimatorCompat vpa, View view, float value) {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void zBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationXBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationYBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationZ(ViewPropertyAnimatorCompat vpa, View view, float value) {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationZBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void start(ViewPropertyAnimatorCompat vpa, View view) {
            removeStartMessage(view);
            startAnimation(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withLayer(ViewPropertyAnimatorCompat vpa, View view) {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withStartAction(ViewPropertyAnimatorCompat vpa, View view, Runnable runnable) {
            vpa.mStartAction = runnable;
            postStartMessage(vpa, view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setListener(ViewPropertyAnimatorCompat vpa, View view, ViewPropertyAnimatorListener listener) {
            view.setTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID, listener);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setUpdateListener(ViewPropertyAnimatorCompat vpa, View view, ViewPropertyAnimatorUpdateListener listener) {
        }

        void startAnimation(ViewPropertyAnimatorCompat vpa, View view) {
            Object listenerTag = view.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
            ViewPropertyAnimatorListener listener = null;
            if (listenerTag instanceof ViewPropertyAnimatorListener) {
                listener = (ViewPropertyAnimatorListener) listenerTag;
            }
            Runnable startAction = vpa.mStartAction;
            Runnable endAction = vpa.mEndAction;
            vpa.mStartAction = null;
            vpa.mEndAction = null;
            if (startAction != null) {
                startAction.run();
            }
            if (listener != null) {
                listener.onAnimationStart(view);
                listener.onAnimationEnd(view);
            }
            if (endAction != null) {
                endAction.run();
            }
            if (this.mStarterMap != null) {
                this.mStarterMap.remove(view);
            }
        }

        class Starter implements Runnable {
            WeakReference<View> mViewRef;
            ViewPropertyAnimatorCompat mVpa;

            Starter(ViewPropertyAnimatorCompat vpa, View view) {
                this.mViewRef = new WeakReference<>(view);
                this.mVpa = vpa;
            }

            @Override // java.lang.Runnable
            public void run() {
                View view = this.mViewRef.get();
                if (view != null) {
                    BaseViewPropertyAnimatorCompatImpl.this.startAnimation(this.mVpa, view);
                }
            }
        }

        private void removeStartMessage(View view) {
            Runnable starter;
            if (this.mStarterMap != null && (starter = this.mStarterMap.get(view)) != null) {
                view.removeCallbacks(starter);
            }
        }

        private void postStartMessage(ViewPropertyAnimatorCompat vpa, View view) {
            Runnable starter = null;
            if (this.mStarterMap != null) {
                Runnable starter2 = this.mStarterMap.get(view);
                starter = starter2;
            }
            if (starter == null) {
                starter = new Starter(vpa, view);
                if (this.mStarterMap == null) {
                    this.mStarterMap = new WeakHashMap<>();
                }
                this.mStarterMap.put(view, starter);
            }
            view.removeCallbacks(starter);
            view.post(starter);
        }
    }

    static class ICSViewPropertyAnimatorCompatImpl extends BaseViewPropertyAnimatorCompatImpl {
        WeakHashMap<View, Integer> mLayerMap = null;

        ICSViewPropertyAnimatorCompatImpl() {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setDuration(ViewPropertyAnimatorCompat vpa, View view, long value) {
            ViewPropertyAnimatorCompatICS.setDuration(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void alpha(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.alpha(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationX(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.translationX(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationY(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.translationY(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public long getDuration(ViewPropertyAnimatorCompat vpa, View view) {
            return ViewPropertyAnimatorCompatICS.getDuration(view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setInterpolator(ViewPropertyAnimatorCompat vpa, View view, Interpolator value) {
            ViewPropertyAnimatorCompatICS.setInterpolator(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setStartDelay(ViewPropertyAnimatorCompat vpa, View view, long value) {
            ViewPropertyAnimatorCompatICS.setStartDelay(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public long getStartDelay(ViewPropertyAnimatorCompat vpa, View view) {
            return ViewPropertyAnimatorCompatICS.getStartDelay(view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void alphaBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.alphaBy(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotation(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.rotation(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.rotationBy(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationX(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.rotationX(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationXBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.rotationXBy(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationY(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.rotationY(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void rotationYBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.rotationYBy(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleX(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.scaleX(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleXBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.scaleXBy(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleY(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.scaleY(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void scaleYBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.scaleYBy(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void cancel(ViewPropertyAnimatorCompat vpa, View view) {
            ViewPropertyAnimatorCompatICS.cancel(view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void x(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.x(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void xBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.xBy(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void y(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.y(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void yBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.yBy(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationXBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.translationXBy(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationYBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatICS.translationYBy(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void start(ViewPropertyAnimatorCompat vpa, View view) {
            ViewPropertyAnimatorCompatICS.start(view);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setListener(ViewPropertyAnimatorCompat vpa, View view, ViewPropertyAnimatorListener listener) {
            view.setTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID, listener);
            ViewPropertyAnimatorCompatICS.setListener(view, new MyVpaListener(vpa));
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withEndAction(ViewPropertyAnimatorCompat vpa, View view, Runnable runnable) {
            ViewPropertyAnimatorCompatICS.setListener(view, new MyVpaListener(vpa));
            vpa.mEndAction = runnable;
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withStartAction(ViewPropertyAnimatorCompat vpa, View view, Runnable runnable) {
            ViewPropertyAnimatorCompatICS.setListener(view, new MyVpaListener(vpa));
            vpa.mStartAction = runnable;
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withLayer(ViewPropertyAnimatorCompat vpa, View view) {
            vpa.mOldLayerType = ViewCompat.getLayerType(view);
            ViewPropertyAnimatorCompatICS.setListener(view, new MyVpaListener(vpa));
        }

        static class MyVpaListener implements ViewPropertyAnimatorListener {
            boolean mAnimEndCalled;
            ViewPropertyAnimatorCompat mVpa;

            MyVpaListener(ViewPropertyAnimatorCompat vpa) {
                this.mVpa = vpa;
            }

            @Override // android.support.v4.view.ViewPropertyAnimatorListener
            public void onAnimationStart(View view) {
                this.mAnimEndCalled = false;
                if (this.mVpa.mOldLayerType >= 0) {
                    ViewCompat.setLayerType(view, 2, null);
                }
                if (this.mVpa.mStartAction != null) {
                    Runnable startAction = this.mVpa.mStartAction;
                    this.mVpa.mStartAction = null;
                    startAction.run();
                }
                Object listenerTag = view.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
                ViewPropertyAnimatorListener listener = null;
                if (listenerTag instanceof ViewPropertyAnimatorListener) {
                    listener = (ViewPropertyAnimatorListener) listenerTag;
                }
                if (listener != null) {
                    listener.onAnimationStart(view);
                }
            }

            @Override // android.support.v4.view.ViewPropertyAnimatorListener
            public void onAnimationEnd(View view) {
                if (this.mVpa.mOldLayerType >= 0) {
                    ViewCompat.setLayerType(view, this.mVpa.mOldLayerType, null);
                    this.mVpa.mOldLayerType = -1;
                }
                if (Build.VERSION.SDK_INT >= 16 || !this.mAnimEndCalled) {
                    if (this.mVpa.mEndAction != null) {
                        Runnable endAction = this.mVpa.mEndAction;
                        this.mVpa.mEndAction = null;
                        endAction.run();
                    }
                    Object listenerTag = view.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
                    ViewPropertyAnimatorListener listener = null;
                    if (listenerTag instanceof ViewPropertyAnimatorListener) {
                        listener = (ViewPropertyAnimatorListener) listenerTag;
                    }
                    if (listener != null) {
                        listener.onAnimationEnd(view);
                    }
                    this.mAnimEndCalled = true;
                }
            }

            @Override // android.support.v4.view.ViewPropertyAnimatorListener
            public void onAnimationCancel(View view) {
                Object listenerTag = view.getTag(ViewPropertyAnimatorCompat.LISTENER_TAG_ID);
                ViewPropertyAnimatorListener listener = null;
                if (listenerTag instanceof ViewPropertyAnimatorListener) {
                    listener = (ViewPropertyAnimatorListener) listenerTag;
                }
                if (listener != null) {
                    listener.onAnimationCancel(view);
                }
            }
        }
    }

    static class JBViewPropertyAnimatorCompatImpl extends ICSViewPropertyAnimatorCompatImpl {
        JBViewPropertyAnimatorCompatImpl() {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setListener(ViewPropertyAnimatorCompat vpa, View view, ViewPropertyAnimatorListener listener) {
            ViewPropertyAnimatorCompatJB.setListener(view, listener);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withStartAction(ViewPropertyAnimatorCompat vpa, View view, Runnable runnable) {
            ViewPropertyAnimatorCompatJB.withStartAction(view, runnable);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withEndAction(ViewPropertyAnimatorCompat vpa, View view, Runnable runnable) {
            ViewPropertyAnimatorCompatJB.withEndAction(view, runnable);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.ICSViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void withLayer(ViewPropertyAnimatorCompat vpa, View view) {
            ViewPropertyAnimatorCompatJB.withLayer(view);
        }
    }

    static class JBMr2ViewPropertyAnimatorCompatImpl extends JBViewPropertyAnimatorCompatImpl {
        JBMr2ViewPropertyAnimatorCompatImpl() {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public Interpolator getInterpolator(ViewPropertyAnimatorCompat vpa, View view) {
            return ViewPropertyAnimatorCompatJellybeanMr2.getInterpolator(view);
        }
    }

    static class KitKatViewPropertyAnimatorCompatImpl extends JBMr2ViewPropertyAnimatorCompatImpl {
        KitKatViewPropertyAnimatorCompatImpl() {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void setUpdateListener(ViewPropertyAnimatorCompat vpa, View view, ViewPropertyAnimatorUpdateListener listener) {
            ViewPropertyAnimatorCompatKK.setUpdateListener(view, listener);
        }
    }

    static class LollipopViewPropertyAnimatorCompatImpl extends KitKatViewPropertyAnimatorCompatImpl {
        LollipopViewPropertyAnimatorCompatImpl() {
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationZ(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatLollipop.translationZ(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void translationZBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatLollipop.translationZBy(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void z(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatLollipop.z(view, value);
        }

        @Override // android.support.v4.view.ViewPropertyAnimatorCompat.BaseViewPropertyAnimatorCompatImpl, android.support.v4.view.ViewPropertyAnimatorCompat.ViewPropertyAnimatorCompatImpl
        public void zBy(ViewPropertyAnimatorCompat vpa, View view, float value) {
            ViewPropertyAnimatorCompatLollipop.zBy(view, value);
        }
    }

    static {
        int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            IMPL = new LollipopViewPropertyAnimatorCompatImpl();
            return;
        }
        if (version >= 19) {
            IMPL = new KitKatViewPropertyAnimatorCompatImpl();
            return;
        }
        if (version >= 18) {
            IMPL = new JBMr2ViewPropertyAnimatorCompatImpl();
            return;
        }
        if (version >= 16) {
            IMPL = new JBViewPropertyAnimatorCompatImpl();
        } else if (version >= 14) {
            IMPL = new ICSViewPropertyAnimatorCompatImpl();
        } else {
            IMPL = new BaseViewPropertyAnimatorCompatImpl();
        }
    }

    public ViewPropertyAnimatorCompat setDuration(long value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.setDuration(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat alpha(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.alpha(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat alphaBy(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.alphaBy(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationX(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.translationX(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationY(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.translationY(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat withEndAction(Runnable runnable) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.withEndAction(this, view, runnable);
        }
        return this;
    }

    public long getDuration() {
        View view = this.mView.get();
        if (view != null) {
            return IMPL.getDuration(this, view);
        }
        return 0L;
    }

    public ViewPropertyAnimatorCompat setInterpolator(Interpolator value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.setInterpolator(this, view, value);
        }
        return this;
    }

    public Interpolator getInterpolator() {
        View view = this.mView.get();
        if (view != null) {
            return IMPL.getInterpolator(this, view);
        }
        return null;
    }

    public ViewPropertyAnimatorCompat setStartDelay(long value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.setStartDelay(this, view, value);
        }
        return this;
    }

    public long getStartDelay() {
        View view = this.mView.get();
        if (view != null) {
            return IMPL.getStartDelay(this, view);
        }
        return 0L;
    }

    public ViewPropertyAnimatorCompat rotation(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.rotation(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationBy(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.rotationBy(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationX(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.rotationX(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationXBy(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.rotationXBy(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationY(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.rotationY(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationYBy(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.rotationYBy(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleX(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.scaleX(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleXBy(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.scaleXBy(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleY(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.scaleY(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleYBy(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.scaleYBy(this, view, value);
        }
        return this;
    }

    public void cancel() {
        View view = this.mView.get();
        if (view != null) {
            IMPL.cancel(this, view);
        }
    }

    public ViewPropertyAnimatorCompat x(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.x(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat xBy(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.xBy(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat y(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.y(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat yBy(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.yBy(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationXBy(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.translationXBy(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationYBy(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.translationYBy(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationZBy(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.translationZBy(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationZ(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.translationZ(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat z(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.z(this, view, value);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat zBy(float value) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.zBy(this, view, value);
        }
        return this;
    }

    public void start() {
        View view = this.mView.get();
        if (view != null) {
            IMPL.start(this, view);
        }
    }

    public ViewPropertyAnimatorCompat withLayer() {
        View view = this.mView.get();
        if (view != null) {
            IMPL.withLayer(this, view);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat withStartAction(Runnable runnable) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.withStartAction(this, view, runnable);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setListener(ViewPropertyAnimatorListener listener) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.setListener(this, view, listener);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setUpdateListener(ViewPropertyAnimatorUpdateListener listener) {
        View view = this.mView.get();
        if (view != null) {
            IMPL.setUpdateListener(this, view, listener);
        }
        return this;
    }
}
