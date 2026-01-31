package android.support.v4.widget;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.widget.TextView;

/* loaded from: classes.dex */
public final class TextViewCompat {
    static final TextViewCompatImpl IMPL;

    interface TextViewCompatImpl {
        Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView);

        int getMaxLines(TextView textView);

        int getMinLines(TextView textView);

        void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4);

        void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int i, @DrawableRes int i2, @DrawableRes int i3, @DrawableRes int i4);

        void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4);

        void setTextAppearance(@NonNull TextView textView, @StyleRes int i);
    }

    private TextViewCompat() {
    }

    static class BaseTextViewCompatImpl implements TextViewCompatImpl {
        BaseTextViewCompatImpl() {
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
            textView.setCompoundDrawables(start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
            textView.setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int start, @DrawableRes int top, @DrawableRes int end, @DrawableRes int bottom) {
            textView.setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public int getMaxLines(TextView textView) {
            return TextViewCompatGingerbread.getMaxLines(textView);
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public int getMinLines(TextView textView) {
            return TextViewCompatGingerbread.getMinLines(textView);
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setTextAppearance(TextView textView, @StyleRes int resId) {
            TextViewCompatGingerbread.setTextAppearance(textView, resId);
        }

        @Override // android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
            return TextViewCompatGingerbread.getCompoundDrawablesRelative(textView);
        }
    }

    static class JbTextViewCompatImpl extends BaseTextViewCompatImpl {
        JbTextViewCompatImpl() {
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public int getMaxLines(TextView textView) {
            return TextViewCompatJb.getMaxLines(textView);
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public int getMinLines(TextView textView) {
            return TextViewCompatJb.getMinLines(textView);
        }
    }

    static class JbMr1TextViewCompatImpl extends JbTextViewCompatImpl {
        JbMr1TextViewCompatImpl() {
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
            TextViewCompatJbMr1.setCompoundDrawablesRelative(textView, start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
            TextViewCompatJbMr1.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int start, @DrawableRes int top, @DrawableRes int end, @DrawableRes int bottom) {
            TextViewCompatJbMr1.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
            return TextViewCompatJbMr1.getCompoundDrawablesRelative(textView);
        }
    }

    static class JbMr2TextViewCompatImpl extends JbMr1TextViewCompatImpl {
        JbMr2TextViewCompatImpl() {
        }

        @Override // android.support.v4.widget.TextViewCompat.JbMr1TextViewCompatImpl, android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
            TextViewCompatJbMr2.setCompoundDrawablesRelative(textView, start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.JbMr1TextViewCompatImpl, android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
            TextViewCompatJbMr2.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.JbMr1TextViewCompatImpl, android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int start, @DrawableRes int top, @DrawableRes int end, @DrawableRes int bottom) {
            TextViewCompatJbMr2.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
        }

        @Override // android.support.v4.widget.TextViewCompat.JbMr1TextViewCompatImpl, android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
            return TextViewCompatJbMr2.getCompoundDrawablesRelative(textView);
        }
    }

    static class Api23TextViewCompatImpl extends JbMr2TextViewCompatImpl {
        Api23TextViewCompatImpl() {
        }

        @Override // android.support.v4.widget.TextViewCompat.BaseTextViewCompatImpl, android.support.v4.widget.TextViewCompat.TextViewCompatImpl
        public void setTextAppearance(@NonNull TextView textView, @StyleRes int resId) {
            TextViewCompatApi23.setTextAppearance(textView, resId);
        }
    }

    static {
        int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            IMPL = new Api23TextViewCompatImpl();
            return;
        }
        if (version >= 18) {
            IMPL = new JbMr2TextViewCompatImpl();
            return;
        }
        if (version >= 17) {
            IMPL = new JbMr1TextViewCompatImpl();
        } else if (version >= 16) {
            IMPL = new JbTextViewCompatImpl();
        } else {
            IMPL = new BaseTextViewCompatImpl();
        }
    }

    public static void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        IMPL.setCompoundDrawablesRelative(textView, start, top, end, bottom);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int start, @DrawableRes int top, @DrawableRes int end, @DrawableRes int bottom) {
        IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, start, top, end, bottom);
    }

    public static int getMaxLines(@NonNull TextView textView) {
        return IMPL.getMaxLines(textView);
    }

    public static int getMinLines(@NonNull TextView textView) {
        return IMPL.getMinLines(textView);
    }

    public static void setTextAppearance(@NonNull TextView textView, @StyleRes int resId) {
        IMPL.setTextAppearance(textView, resId);
    }

    public static Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
        return IMPL.getCompoundDrawablesRelative(textView);
    }
}
