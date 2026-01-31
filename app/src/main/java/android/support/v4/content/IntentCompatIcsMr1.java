package android.support.v4.content;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.annotation.RequiresApi;

@RequiresApi(15)
@TargetApi(15)
/* loaded from: classes.dex */
class IntentCompatIcsMr1 {
    IntentCompatIcsMr1() {
    }

    public static Intent makeMainSelectorActivity(String selectorAction, String selectorCategory) {
        return Intent.makeMainSelectorActivity(selectorAction, selectorCategory);
    }
}
