package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.util.Pair;
import android.view.View;

/* loaded from: classes.dex */
public class ActivityOptionsCompat {
    public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
    public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";

    public static ActivityOptionsCompat makeCustomAnimation(Context context, int enterResId, int exitResId) {
        if (Build.VERSION.SDK_INT >= 24) {
            return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeCustomAnimation(context, enterResId, exitResId));
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeCustomAnimation(context, enterResId, exitResId));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeCustomAnimation(context, enterResId, exitResId));
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeCustomAnimation(context, enterResId, exitResId));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeScaleUpAnimation(View source, int startX, int startY, int startWidth, int startHeight) {
        if (Build.VERSION.SDK_INT >= 24) {
            return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight));
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight));
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeScaleUpAnimation(source, startX, startY, startWidth, startHeight));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeClipRevealAnimation(View source, int startX, int startY, int width, int height) {
        if (Build.VERSION.SDK_INT >= 24) {
            return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeClipRevealAnimation(source, startX, startY, width, height));
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeClipRevealAnimation(source, startX, startY, width, height));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View source, Bitmap thumbnail, int startX, int startY) {
        if (Build.VERSION.SDK_INT >= 24) {
            return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY));
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY));
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity activity, View sharedElement, String sharedElementName) {
        if (Build.VERSION.SDK_INT >= 24) {
            return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeSceneTransitionAnimation(activity, sharedElement, sharedElementName));
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeSceneTransitionAnimation(activity, sharedElement, sharedElementName));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeSceneTransitionAnimation(activity, sharedElement, sharedElementName));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity activity, Pair<View, String>... sharedElements) {
        if (Build.VERSION.SDK_INT >= 21) {
            View[] views = null;
            String[] names = null;
            if (sharedElements != null) {
                views = new View[sharedElements.length];
                names = new String[sharedElements.length];
                for (int i = 0; i < sharedElements.length; i++) {
                    views[i] = sharedElements[i].first;
                    names[i] = sharedElements[i].second;
                }
            }
            if (Build.VERSION.SDK_INT >= 24) {
                return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeSceneTransitionAnimation(activity, views, names));
            }
            if (Build.VERSION.SDK_INT >= 23) {
                return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeSceneTransitionAnimation(activity, views, names));
            }
            return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeSceneTransitionAnimation(activity, views, names));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeTaskLaunchBehind() {
        if (Build.VERSION.SDK_INT >= 24) {
            return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeTaskLaunchBehind());
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeTaskLaunchBehind());
        }
        if (Build.VERSION.SDK_INT >= 21) {
            return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeTaskLaunchBehind());
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeBasic() {
        if (Build.VERSION.SDK_INT >= 24) {
            return new ActivityOptionsImpl24(ActivityOptionsCompat24.makeBasic());
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return new ActivityOptionsImpl23(ActivityOptionsCompat23.makeBasic());
        }
        return new ActivityOptionsCompat();
    }

    @RequiresApi(16)
    @TargetApi(16)
    private static class ActivityOptionsImplJB extends ActivityOptionsCompat {
        private final ActivityOptionsCompatJB mImpl;

        ActivityOptionsImplJB(ActivityOptionsCompatJB impl) {
            this.mImpl = impl;
        }

        @Override // android.support.v4.app.ActivityOptionsCompat
        public Bundle toBundle() {
            return this.mImpl.toBundle();
        }

        @Override // android.support.v4.app.ActivityOptionsCompat
        public void update(ActivityOptionsCompat otherOptions) {
            if (otherOptions instanceof ActivityOptionsImplJB) {
                ActivityOptionsImplJB otherImpl = (ActivityOptionsImplJB) otherOptions;
                this.mImpl.update(otherImpl.mImpl);
            }
        }
    }

    @RequiresApi(21)
    @TargetApi(21)
    private static class ActivityOptionsImpl21 extends ActivityOptionsCompat {
        private final ActivityOptionsCompat21 mImpl;

        ActivityOptionsImpl21(ActivityOptionsCompat21 impl) {
            this.mImpl = impl;
        }

        @Override // android.support.v4.app.ActivityOptionsCompat
        public Bundle toBundle() {
            return this.mImpl.toBundle();
        }

        @Override // android.support.v4.app.ActivityOptionsCompat
        public void update(ActivityOptionsCompat otherOptions) {
            if (otherOptions instanceof ActivityOptionsImpl21) {
                ActivityOptionsImpl21 otherImpl = (ActivityOptionsImpl21) otherOptions;
                this.mImpl.update(otherImpl.mImpl);
            }
        }
    }

    @RequiresApi(23)
    @TargetApi(23)
    private static class ActivityOptionsImpl23 extends ActivityOptionsCompat {
        private final ActivityOptionsCompat23 mImpl;

        ActivityOptionsImpl23(ActivityOptionsCompat23 impl) {
            this.mImpl = impl;
        }

        @Override // android.support.v4.app.ActivityOptionsCompat
        public Bundle toBundle() {
            return this.mImpl.toBundle();
        }

        @Override // android.support.v4.app.ActivityOptionsCompat
        public void update(ActivityOptionsCompat otherOptions) {
            if (otherOptions instanceof ActivityOptionsImpl23) {
                ActivityOptionsImpl23 otherImpl = (ActivityOptionsImpl23) otherOptions;
                this.mImpl.update(otherImpl.mImpl);
            }
        }

        @Override // android.support.v4.app.ActivityOptionsCompat
        public void requestUsageTimeReport(PendingIntent receiver) {
            this.mImpl.requestUsageTimeReport(receiver);
        }
    }

    @RequiresApi(24)
    @TargetApi(24)
    private static class ActivityOptionsImpl24 extends ActivityOptionsCompat {
        private final ActivityOptionsCompat24 mImpl;

        ActivityOptionsImpl24(ActivityOptionsCompat24 impl) {
            this.mImpl = impl;
        }

        @Override // android.support.v4.app.ActivityOptionsCompat
        public Bundle toBundle() {
            return this.mImpl.toBundle();
        }

        @Override // android.support.v4.app.ActivityOptionsCompat
        public void update(ActivityOptionsCompat otherOptions) {
            if (otherOptions instanceof ActivityOptionsImpl24) {
                ActivityOptionsImpl24 otherImpl = (ActivityOptionsImpl24) otherOptions;
                this.mImpl.update(otherImpl.mImpl);
            }
        }

        @Override // android.support.v4.app.ActivityOptionsCompat
        public ActivityOptionsCompat setLaunchBounds(@Nullable Rect screenSpacePixelRect) {
            return new ActivityOptionsImpl24(this.mImpl.setLaunchBounds(screenSpacePixelRect));
        }

        @Override // android.support.v4.app.ActivityOptionsCompat
        public Rect getLaunchBounds() {
            return this.mImpl.getLaunchBounds();
        }

        @Override // android.support.v4.app.ActivityOptionsCompat
        public void requestUsageTimeReport(PendingIntent receiver) {
            this.mImpl.requestUsageTimeReport(receiver);
        }
    }

    protected ActivityOptionsCompat() {
    }

    public ActivityOptionsCompat setLaunchBounds(@Nullable Rect screenSpacePixelRect) {
        return null;
    }

    @Nullable
    public Rect getLaunchBounds() {
        return null;
    }

    public Bundle toBundle() {
        return null;
    }

    public void update(ActivityOptionsCompat otherOptions) {
    }

    public void requestUsageTimeReport(PendingIntent receiver) {
    }
}
