package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableWrapperGingerbread;

@RequiresApi(11)
@TargetApi(11)
/* loaded from: classes.dex */
class DrawableWrapperHoneycomb extends DrawableWrapperGingerbread {
    DrawableWrapperHoneycomb(Drawable drawable) {
        super(drawable);
    }

    DrawableWrapperHoneycomb(DrawableWrapperGingerbread.DrawableWrapperState state, Resources resources) {
        super(state, resources);
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        this.mDrawable.jumpToCurrentState();
    }

    @Override // android.support.v4.graphics.drawable.DrawableWrapperGingerbread
    @NonNull
    DrawableWrapperGingerbread.DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateHoneycomb(this.mState, null);
    }

    private static class DrawableWrapperStateHoneycomb extends DrawableWrapperGingerbread.DrawableWrapperState {
        DrawableWrapperStateHoneycomb(@Nullable DrawableWrapperGingerbread.DrawableWrapperState orig, @Nullable Resources res) {
            super(orig, res);
        }

        @Override // android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState, android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(@Nullable Resources res) {
            return new DrawableWrapperHoneycomb(this, res);
        }
    }
}
