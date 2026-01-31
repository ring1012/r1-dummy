package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.support.v4.view.LayoutInflaterCompatHC;
import android.view.LayoutInflater;

@RequiresApi(21)
@TargetApi(21)
/* loaded from: classes.dex */
class LayoutInflaterCompatLollipop {
    LayoutInflaterCompatLollipop() {
    }

    static void setFactory(LayoutInflater inflater, LayoutInflaterFactory factory) {
        inflater.setFactory2(factory != null ? new LayoutInflaterCompatHC.FactoryWrapperHC(factory) : null);
    }
}
