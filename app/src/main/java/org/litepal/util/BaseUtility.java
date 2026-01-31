package org.litepal.util;

import android.text.TextUtils;
import java.util.Collection;
import java.util.Locale;
import org.litepal.exceptions.DataSupportException;
import org.litepal.parser.LitePalAttr;
import org.litepal.util.Const;

/* loaded from: classes.dex */
public class BaseUtility {
    private BaseUtility() {
    }

    public static String changeCase(String string) {
        if (string != null) {
            LitePalAttr litePalAttr = LitePalAttr.getInstance();
            String cases = litePalAttr.getCases();
            if (!Const.LitePal.CASES_KEEP.equals(cases)) {
                if (Const.LitePal.CASES_UPPER.equals(cases)) {
                    return string.toUpperCase(Locale.US);
                }
                return string.toLowerCase(Locale.US);
            }
            return string;
        }
        return null;
    }

    public static boolean containsIgnoreCases(Collection<String> collection, String string) {
        if (collection == null) {
            return false;
        }
        if (string == null) {
            return collection.contains(null);
        }
        for (String element : collection) {
            if (string.equalsIgnoreCase(element)) {
                return true;
            }
        }
        return false;
    }

    public static String capitalize(String string) {
        if (!TextUtils.isEmpty(string)) {
            return String.valueOf(string.substring(0, 1).toUpperCase(Locale.US)) + string.substring(1);
        }
        if (string == null) {
            return null;
        }
        return "";
    }

    public static int count(String string, String mark) {
        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(mark)) {
            int count = 0;
            int index = string.indexOf(mark);
            while (index != -1) {
                count++;
                string = string.substring(mark.length() + index);
                index = string.indexOf(mark);
            }
            return count;
        }
        return 0;
    }

    public static void checkConditionsCorrect(String... conditions) {
        int conditionsSize;
        if (conditions != null && (conditionsSize = conditions.length) > 0) {
            String whereClause = conditions[0];
            int placeHolderSize = count(whereClause, "?");
            if (conditionsSize != placeHolderSize + 1) {
                throw new DataSupportException(DataSupportException.UPDATE_CONDITIONS_EXCEPTION);
            }
        }
    }

    public static boolean isFieldTypeSupported(String fieldType) {
        return "boolean".equals(fieldType) || "java.lang.Boolean".equals(fieldType) || "float".equals(fieldType) || "java.lang.Float".equals(fieldType) || "double".equals(fieldType) || "java.lang.Double".equals(fieldType) || "int".equals(fieldType) || "java.lang.Integer".equals(fieldType) || "long".equals(fieldType) || "java.lang.Long".equals(fieldType) || "short".equals(fieldType) || "java.lang.Short".equals(fieldType) || "char".equals(fieldType) || "java.lang.Character".equals(fieldType) || "[B".equals(fieldType) || "[Ljava.lang.Byte;".equals(fieldType) || "java.lang.String".equals(fieldType) || "java.util.Date".equals(fieldType);
    }
}
