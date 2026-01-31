package android.support.v4.content.res;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnyRes;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleableRes;
import android.util.TypedValue;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public class TypedArrayUtils {
    public static boolean getBoolean(TypedArray a2, @StyleableRes int index, @StyleableRes int fallbackIndex, boolean defaultValue) {
        boolean val = a2.getBoolean(fallbackIndex, defaultValue);
        return a2.getBoolean(index, val);
    }

    public static Drawable getDrawable(TypedArray a2, @StyleableRes int index, @StyleableRes int fallbackIndex) {
        Drawable val = a2.getDrawable(index);
        if (val == null) {
            return a2.getDrawable(fallbackIndex);
        }
        return val;
    }

    public static int getInt(TypedArray a2, @StyleableRes int index, @StyleableRes int fallbackIndex, int defaultValue) {
        int val = a2.getInt(fallbackIndex, defaultValue);
        return a2.getInt(index, val);
    }

    @AnyRes
    public static int getResourceId(TypedArray a2, @StyleableRes int index, @StyleableRes int fallbackIndex, @AnyRes int defaultValue) {
        int val = a2.getResourceId(fallbackIndex, defaultValue);
        return a2.getResourceId(index, val);
    }

    public static String getString(TypedArray a2, @StyleableRes int index, @StyleableRes int fallbackIndex) {
        String val = a2.getString(index);
        if (val == null) {
            return a2.getString(fallbackIndex);
        }
        return val;
    }

    public static CharSequence getText(TypedArray a2, @StyleableRes int index, @StyleableRes int fallbackIndex) {
        CharSequence val = a2.getText(index);
        if (val == null) {
            return a2.getText(fallbackIndex);
        }
        return val;
    }

    public static CharSequence[] getTextArray(TypedArray a2, @StyleableRes int index, @StyleableRes int fallbackIndex) {
        CharSequence[] val = a2.getTextArray(index);
        if (val == null) {
            return a2.getTextArray(fallbackIndex);
        }
        return val;
    }

    public static int getAttr(Context context, int attr, int fallbackAttr) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(attr, value, true);
        return value.resourceId != 0 ? attr : fallbackAttr;
    }
}
