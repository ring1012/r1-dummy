package android.support.v4.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;

@RequiresApi(16)
@TargetApi(16)
/* loaded from: classes.dex */
class ViewPropertyAnimatorCompatJB {
    ViewPropertyAnimatorCompatJB() {
    }

    public static void withStartAction(View view, Runnable runnable) {
        view.animate().withStartAction(runnable);
    }

    public static void withEndAction(View view, Runnable runnable) {
        view.animate().withEndAction(runnable);
    }

    public static void withLayer(View view) {
        view.animate().withLayer();
    }

    public static void setListener(final View view, final ViewPropertyAnimatorListener listener) {
        if (listener != null) {
            view.animate().setListener(new AnimatorListenerAdapter() { // from class: android.support.v4.view.ViewPropertyAnimatorCompatJB.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animation) {
                    listener.onAnimationCancel(view);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    listener.onAnimationEnd(view);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                    listener.onAnimationStart(view);
                }
            });
        } else {
            view.animate().setListener(null);
        }
    }
}
