package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;

@RequiresApi(14)
@TargetApi(14)
/* loaded from: classes.dex */
class MotionEventCompatICS {
    MotionEventCompatICS() {
    }

    public static int getButtonState(MotionEvent event) {
        return event.getButtonState();
    }
}
