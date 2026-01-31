package android.support.v4.view;

import android.view.MenuItem;

/* loaded from: classes.dex */
public final class MenuCompat {
    @Deprecated
    public static void setShowAsAction(MenuItem item, int actionEnum) {
        MenuItemCompat.setShowAsAction(item, actionEnum);
    }

    private MenuCompat() {
    }
}
