package android.support.v4.widget;

import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.ListView;

/* loaded from: classes.dex */
public final class ListViewCompat {
    public static void scrollListBy(@NonNull ListView listView, int y) {
        if (Build.VERSION.SDK_INT >= 19) {
            ListViewCompatKitKat.scrollListBy(listView, y);
        } else {
            ListViewCompatGingerbread.scrollListBy(listView, y);
        }
    }

    private ListViewCompat() {
    }
}
