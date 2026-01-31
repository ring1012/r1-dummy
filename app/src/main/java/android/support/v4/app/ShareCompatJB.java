package android.support.v4.app;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.text.Html;

@RequiresApi(16)
@TargetApi(16)
/* loaded from: classes.dex */
class ShareCompatJB {
    ShareCompatJB() {
    }

    public static String escapeHtml(CharSequence html) {
        return Html.escapeHtml(html);
    }
}
