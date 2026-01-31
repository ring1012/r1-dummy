package android.support.v4.text;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

@RequiresApi(23)
@TargetApi(23)
/* loaded from: classes.dex */
class ICUCompatApi23 {
    private static final String TAG = "ICUCompatIcs";
    private static Method sAddLikelySubtagsMethod;

    ICUCompatApi23() {
    }

    static {
        try {
            Class<?> clazz = Class.forName("libcore.icu.ICU");
            sAddLikelySubtagsMethod = clazz.getMethod("addLikelySubtags", Locale.class);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String maximizeAndGetScript(Locale locale) {
        try {
            Object[] args = {locale};
            return ((Locale) sAddLikelySubtagsMethod.invoke(null, args)).getScript();
        } catch (IllegalAccessException e) {
            Log.w(TAG, e);
            return locale.getScript();
        } catch (InvocationTargetException e2) {
            Log.w(TAG, e2);
            return locale.getScript();
        }
    }
}
