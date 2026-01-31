package android.support.v4.animation;

import android.support.annotation.RestrictTo;
import android.view.View;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public interface ValueAnimatorCompat {
    void addListener(AnimatorListenerCompat animatorListenerCompat);

    void addUpdateListener(AnimatorUpdateListenerCompat animatorUpdateListenerCompat);

    void cancel();

    float getAnimatedFraction();

    void setDuration(long j);

    void setTarget(View view);

    void start();
}
