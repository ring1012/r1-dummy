package android.support.v4.database;

import android.text.TextUtils;

/* loaded from: classes.dex */
public final class DatabaseUtilsCompat {
    private DatabaseUtilsCompat() {
    }

    public static String concatenateWhere(String a2, String b) {
        if (TextUtils.isEmpty(a2)) {
            return b;
        }
        return TextUtils.isEmpty(b) ? a2 : "(" + a2 + ") AND (" + b + ")";
    }

    public static String[] appendSelectionArgs(String[] originalValues, String[] newValues) {
        if (originalValues == null || originalValues.length == 0) {
            return newValues;
        }
        String[] result = new String[originalValues.length + newValues.length];
        System.arraycopy(originalValues, 0, result, 0, originalValues.length);
        System.arraycopy(newValues, 0, result, originalValues.length, newValues.length);
        return result;
    }
}
