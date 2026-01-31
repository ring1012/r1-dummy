package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableWrapperGingerbread;

@RequiresApi(19)
@TargetApi(19)
/* loaded from: classes.dex */
class DrawableWrapperKitKat extends DrawableWrapperHoneycomb {
    DrawableWrapperKitKat(Drawable drawable) {
        super(drawable);
    }

    DrawableWrapperKitKat(DrawableWrapperGingerbread.DrawableWrapperState state, Resources resources) {
        super(state, resources);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean mirrored) {
        this.mDrawable.setAutoMirrored(mirrored);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        return this.mDrawable.isAutoMirrored();
    }

    @Override // android.support.v4.graphics.drawable.DrawableWrapperHoneycomb, android.support.v4.graphics.drawable.DrawableWrapperGingerbread
    @NonNull
    DrawableWrapperGingerbread.DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateKitKat(this.mState, null);
    }

    private static class DrawableWrapperStateKitKat extends DrawableWrapperGingerbread.DrawableWrapperState {
        DrawableWrapperStateKitKat(@Nullable DrawableWrapperGingerbread.DrawableWrapperState orig, @Nullable Resources res) {
            super(orig, res);
        }

        @Override // android.support.v4.graphics.drawable.DrawableWrapperGingerbread.DrawableWrapperState, android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(@Nullable Resources res) {
            return new DrawableWrapperKitKat(this, res);
        }
    }
}
