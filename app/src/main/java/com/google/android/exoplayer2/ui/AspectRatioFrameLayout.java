package com.google.android.exoplayer2.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public final class AspectRatioFrameLayout extends FrameLayout {
    private static final float MAX_ASPECT_RATIO_DEFORMATION_FRACTION = 0.01f;
    public static final int RESIZE_MODE_FILL = 3;
    public static final int RESIZE_MODE_FIT = 0;
    public static final int RESIZE_MODE_FIXED_HEIGHT = 2;
    public static final int RESIZE_MODE_FIXED_WIDTH = 1;
    private int resizeMode;
    private float videoAspectRatio;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ResizeMode {
    }

    public AspectRatioFrameLayout(Context context) {
        this(context, null);
    }

    public AspectRatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.resizeMode = 0;
        if (attrs != null) {
            TypedArray a2 = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AspectRatioFrameLayout, 0, 0);
            try {
                this.resizeMode = a2.getInt(R.styleable.AspectRatioFrameLayout_resize_mode, 0);
            } finally {
                a2.recycle();
            }
        }
    }

    public void setAspectRatio(float widthHeightRatio) {
        if (this.videoAspectRatio != widthHeightRatio) {
            this.videoAspectRatio = widthHeightRatio;
            requestLayout();
        }
    }

    public void setResizeMode(int resizeMode) {
        if (this.resizeMode != resizeMode) {
            this.resizeMode = resizeMode;
            requestLayout();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.resizeMode != 3 && this.videoAspectRatio > 0.0f) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            float viewAspectRatio = width / height;
            float aspectDeformation = (this.videoAspectRatio / viewAspectRatio) - 1.0f;
            if (Math.abs(aspectDeformation) > MAX_ASPECT_RATIO_DEFORMATION_FRACTION) {
                switch (this.resizeMode) {
                    case 1:
                        height = (int) (width / this.videoAspectRatio);
                        break;
                    case 2:
                        width = (int) (height * this.videoAspectRatio);
                        break;
                    default:
                        if (aspectDeformation > 0.0f) {
                            height = (int) (width / this.videoAspectRatio);
                            break;
                        } else {
                            width = (int) (height * this.videoAspectRatio);
                            break;
                        }
                }
                super.onMeasure(View.MeasureSpec.makeMeasureSpec(width, 1073741824), View.MeasureSpec.makeMeasureSpec(height, 1073741824));
            }
        }
    }
}
