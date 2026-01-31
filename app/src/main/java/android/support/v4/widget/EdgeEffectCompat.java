package android.support.v4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;

/* loaded from: classes.dex */
public final class EdgeEffectCompat {
    private static final EdgeEffectImpl IMPL;
    private Object mEdgeEffect;

    interface EdgeEffectImpl {
        boolean draw(Object obj, Canvas canvas);

        void finish(Object obj);

        boolean isFinished(Object obj);

        Object newEdgeEffect(Context context);

        boolean onAbsorb(Object obj, int i);

        boolean onPull(Object obj, float f);

        boolean onPull(Object obj, float f, float f2);

        boolean onRelease(Object obj);

        void setSize(Object obj, int i, int i2);
    }

    static {
        if (Build.VERSION.SDK_INT >= 21) {
            IMPL = new EdgeEffectLollipopImpl();
        } else if (Build.VERSION.SDK_INT >= 14) {
            IMPL = new EdgeEffectIcsImpl();
        } else {
            IMPL = new BaseEdgeEffectImpl();
        }
    }

    static class BaseEdgeEffectImpl implements EdgeEffectImpl {
        BaseEdgeEffectImpl() {
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public Object newEdgeEffect(Context context) {
            return null;
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public void setSize(Object edgeEffect, int width, int height) {
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean isFinished(Object edgeEffect) {
            return true;
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public void finish(Object edgeEffect) {
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean onPull(Object edgeEffect, float deltaDistance) {
            return false;
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean onRelease(Object edgeEffect) {
            return false;
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean onAbsorb(Object edgeEffect, int velocity) {
            return false;
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean draw(Object edgeEffect, Canvas canvas) {
            return false;
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean onPull(Object edgeEffect, float deltaDistance, float displacement) {
            return false;
        }
    }

    static class EdgeEffectIcsImpl implements EdgeEffectImpl {
        EdgeEffectIcsImpl() {
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public Object newEdgeEffect(Context context) {
            return EdgeEffectCompatIcs.newEdgeEffect(context);
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public void setSize(Object edgeEffect, int width, int height) {
            EdgeEffectCompatIcs.setSize(edgeEffect, width, height);
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean isFinished(Object edgeEffect) {
            return EdgeEffectCompatIcs.isFinished(edgeEffect);
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public void finish(Object edgeEffect) {
            EdgeEffectCompatIcs.finish(edgeEffect);
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean onPull(Object edgeEffect, float deltaDistance) {
            return EdgeEffectCompatIcs.onPull(edgeEffect, deltaDistance);
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean onRelease(Object edgeEffect) {
            return EdgeEffectCompatIcs.onRelease(edgeEffect);
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean onAbsorb(Object edgeEffect, int velocity) {
            return EdgeEffectCompatIcs.onAbsorb(edgeEffect, velocity);
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean draw(Object edgeEffect, Canvas canvas) {
            return EdgeEffectCompatIcs.draw(edgeEffect, canvas);
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean onPull(Object edgeEffect, float deltaDistance, float displacement) {
            return EdgeEffectCompatIcs.onPull(edgeEffect, deltaDistance);
        }
    }

    static class EdgeEffectLollipopImpl extends EdgeEffectIcsImpl {
        EdgeEffectLollipopImpl() {
        }

        @Override // android.support.v4.widget.EdgeEffectCompat.EdgeEffectIcsImpl, android.support.v4.widget.EdgeEffectCompat.EdgeEffectImpl
        public boolean onPull(Object edgeEffect, float deltaDistance, float displacement) {
            return EdgeEffectCompatLollipop.onPull(edgeEffect, deltaDistance, displacement);
        }
    }

    public EdgeEffectCompat(Context context) {
        this.mEdgeEffect = IMPL.newEdgeEffect(context);
    }

    public void setSize(int width, int height) {
        IMPL.setSize(this.mEdgeEffect, width, height);
    }

    public boolean isFinished() {
        return IMPL.isFinished(this.mEdgeEffect);
    }

    public void finish() {
        IMPL.finish(this.mEdgeEffect);
    }

    @Deprecated
    public boolean onPull(float deltaDistance) {
        return IMPL.onPull(this.mEdgeEffect, deltaDistance);
    }

    public boolean onPull(float deltaDistance, float displacement) {
        return IMPL.onPull(this.mEdgeEffect, deltaDistance, displacement);
    }

    public boolean onRelease() {
        return IMPL.onRelease(this.mEdgeEffect);
    }

    public boolean onAbsorb(int velocity) {
        return IMPL.onAbsorb(this.mEdgeEffect, velocity);
    }

    public boolean draw(Canvas canvas) {
        return IMPL.draw(this.mEdgeEffect, canvas);
    }
}
