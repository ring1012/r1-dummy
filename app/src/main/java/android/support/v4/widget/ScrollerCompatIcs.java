package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.widget.OverScroller;

@RequiresApi(14)
@TargetApi(14)
/* loaded from: classes.dex */
class ScrollerCompatIcs {
    ScrollerCompatIcs() {
    }

    public static float getCurrVelocity(Object scroller) {
        return ((OverScroller) scroller).getCurrVelocity();
    }
}
