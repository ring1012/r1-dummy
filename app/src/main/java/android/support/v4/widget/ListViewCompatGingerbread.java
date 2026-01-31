package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ListView;

@RequiresApi(9)
@TargetApi(9)
/* loaded from: classes.dex */
class ListViewCompatGingerbread {
    ListViewCompatGingerbread() {
    }

    static void scrollListBy(ListView listView, int y) {
        View firstView;
        int firstPosition = listView.getFirstVisiblePosition();
        if (firstPosition != -1 && (firstView = listView.getChildAt(0)) != null) {
            int newTop = firstView.getTop() - y;
            listView.setSelectionFromTop(firstPosition, newTop);
        }
    }
}
