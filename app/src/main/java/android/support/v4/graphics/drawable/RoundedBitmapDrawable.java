package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;

@RequiresApi(9)
@TargetApi(9)
/* loaded from: classes.dex */
public abstract class RoundedBitmapDrawable extends Drawable {
    private static final int DEFAULT_PAINT_FLAGS = 3;
    final Bitmap mBitmap;
    private int mBitmapHeight;
    private final BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private float mCornerRadius;
    private boolean mIsCircular;
    private int mTargetDensity;
    private int mGravity = 119;
    private final Paint mPaint = new Paint(3);
    private final Matrix mShaderMatrix = new Matrix();
    final Rect mDstRect = new Rect();
    private final RectF mDstRectF = new RectF();
    private boolean mApplyGravity = true;

    public final Paint getPaint() {
        return this.mPaint;
    }

    public final Bitmap getBitmap() {
        return this.mBitmap;
    }

    private void computeBitmapSize() {
        this.mBitmapWidth = this.mBitmap.getScaledWidth(this.mTargetDensity);
        this.mBitmapHeight = this.mBitmap.getScaledHeight(this.mTargetDensity);
    }

    public void setTargetDensity(Canvas canvas) {
        setTargetDensity(canvas.getDensity());
    }

    public void setTargetDensity(DisplayMetrics metrics) {
        setTargetDensity(metrics.densityDpi);
    }

    public void setTargetDensity(int density) {
        if (this.mTargetDensity != density) {
            if (density == 0) {
                density = 160;
            }
            this.mTargetDensity = density;
            if (this.mBitmap != null) {
                computeBitmapSize();
            }
            invalidateSelf();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            this.mGravity = gravity;
            this.mApplyGravity = true;
            invalidateSelf();
        }
    }

    public void setMipMap(boolean mipMap) {
        throw new UnsupportedOperationException();
    }

    public boolean hasMipMap() {
        throw new UnsupportedOperationException();
    }

    public void setAntiAlias(boolean aa) {
        this.mPaint.setAntiAlias(aa);
        invalidateSelf();
    }

    public boolean hasAntiAlias() {
        return this.mPaint.isAntiAlias();
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean filter) {
        this.mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean dither) {
        this.mPaint.setDither(dither);
        invalidateSelf();
    }

    void gravityCompatApply(int gravity, int bitmapWidth, int bitmapHeight, Rect bounds, Rect outRect) {
        throw new UnsupportedOperationException();
    }

    void updateDstRect() {
        if (this.mApplyGravity) {
            if (this.mIsCircular) {
                int minDimen = Math.min(this.mBitmapWidth, this.mBitmapHeight);
                gravityCompatApply(this.mGravity, minDimen, minDimen, getBounds(), this.mDstRect);
                int minDrawDimen = Math.min(this.mDstRect.width(), this.mDstRect.height());
                int insetX = Math.max(0, (this.mDstRect.width() - minDrawDimen) / 2);
                int insetY = Math.max(0, (this.mDstRect.height() - minDrawDimen) / 2);
                this.mDstRect.inset(insetX, insetY);
                this.mCornerRadius = 0.5f * minDrawDimen;
            } else {
                gravityCompatApply(this.mGravity, this.mBitmapWidth, this.mBitmapHeight, getBounds(), this.mDstRect);
            }
            this.mDstRectF.set(this.mDstRect);
            if (this.mBitmapShader != null) {
                this.mShaderMatrix.setTranslate(this.mDstRectF.left, this.mDstRectF.top);
                this.mShaderMatrix.preScale(this.mDstRectF.width() / this.mBitmap.getWidth(), this.mDstRectF.height() / this.mBitmap.getHeight());
                this.mBitmapShader.setLocalMatrix(this.mShaderMatrix);
                this.mPaint.setShader(this.mBitmapShader);
            }
            this.mApplyGravity = false;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            updateDstRect();
            if (this.mPaint.getShader() == null) {
                canvas.drawBitmap(bitmap, (Rect) null, this.mDstRect, this.mPaint);
            } else {
                canvas.drawRoundRect(this.mDstRectF, this.mCornerRadius, this.mCornerRadius, this.mPaint);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        int oldAlpha = this.mPaint.getAlpha();
        if (alpha != oldAlpha) {
            this.mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mPaint.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }

    public void setCircular(boolean circular) {
        this.mIsCircular = circular;
        this.mApplyGravity = true;
        if (circular) {
            updateCircularCornerRadius();
            this.mPaint.setShader(this.mBitmapShader);
            invalidateSelf();
            return;
        }
        setCornerRadius(0.0f);
    }

    private void updateCircularCornerRadius() {
        int minCircularSize = Math.min(this.mBitmapHeight, this.mBitmapWidth);
        this.mCornerRadius = minCircularSize / 2;
    }

    public boolean isCircular() {
        return this.mIsCircular;
    }

    public void setCornerRadius(float cornerRadius) {
        if (this.mCornerRadius != cornerRadius) {
            this.mIsCircular = false;
            if (isGreaterThanZero(cornerRadius)) {
                this.mPaint.setShader(this.mBitmapShader);
            } else {
                this.mPaint.setShader(null);
            }
            this.mCornerRadius = cornerRadius;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (this.mIsCircular) {
            updateCircularCornerRadius();
        }
        this.mApplyGravity = true;
    }

    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        Bitmap bm;
        return (this.mGravity != 119 || this.mIsCircular || (bm = this.mBitmap) == null || bm.hasAlpha() || this.mPaint.getAlpha() < 255 || isGreaterThanZero(this.mCornerRadius)) ? -3 : -1;
    }

    RoundedBitmapDrawable(Resources res, Bitmap bitmap) {
        this.mTargetDensity = 160;
        if (res != null) {
            this.mTargetDensity = res.getDisplayMetrics().densityDpi;
        }
        this.mBitmap = bitmap;
        if (this.mBitmap != null) {
            computeBitmapSize();
            this.mBitmapShader = new BitmapShader(this.mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        } else {
            this.mBitmapHeight = -1;
            this.mBitmapWidth = -1;
            this.mBitmapShader = null;
        }
    }

    private static boolean isGreaterThanZero(float toCompare) {
        return toCompare > 0.05f;
    }
}
