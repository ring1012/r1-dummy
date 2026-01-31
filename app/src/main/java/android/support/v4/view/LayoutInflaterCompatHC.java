package android.support.v4.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.support.v4.view.LayoutInflaterCompatBase;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import java.lang.reflect.Field;

@RequiresApi(11)
@TargetApi(11)
/* loaded from: classes.dex */
class LayoutInflaterCompatHC {
    private static final String TAG = "LayoutInflaterCompatHC";
    private static boolean sCheckedField;
    private static Field sLayoutInflaterFactory2Field;

    LayoutInflaterCompatHC() {
    }

    static class FactoryWrapperHC extends LayoutInflaterCompatBase.FactoryWrapper implements LayoutInflater.Factory2 {
        FactoryWrapperHC(LayoutInflaterFactory delegateFactory) {
            super(delegateFactory);
        }

        @Override // android.view.LayoutInflater.Factory2
        public View onCreateView(View parent, String name, Context context, AttributeSet attributeSet) {
            return this.mDelegateFactory.onCreateView(parent, name, context, attributeSet);
        }
    }

    static void setFactory(LayoutInflater inflater, LayoutInflaterFactory factory) throws IllegalAccessException, IllegalArgumentException {
        LayoutInflater.Factory2 factory2 = factory != null ? new FactoryWrapperHC(factory) : null;
        inflater.setFactory2(factory2);
        LayoutInflater.Factory f = inflater.getFactory();
        if (f instanceof LayoutInflater.Factory2) {
            forceSetFactory2(inflater, (LayoutInflater.Factory2) f);
        } else {
            forceSetFactory2(inflater, factory2);
        }
    }

    static void forceSetFactory2(LayoutInflater inflater, LayoutInflater.Factory2 factory) throws IllegalAccessException, IllegalArgumentException {
        if (!sCheckedField) {
            try {
                sLayoutInflaterFactory2Field = LayoutInflater.class.getDeclaredField("mFactory2");
                sLayoutInflaterFactory2Field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "forceSetFactory2 Could not find field 'mFactory2' on class " + LayoutInflater.class.getName() + "; inflation may have unexpected results.", e);
            }
            sCheckedField = true;
        }
        if (sLayoutInflaterFactory2Field != null) {
            try {
                sLayoutInflaterFactory2Field.set(inflater, factory);
            } catch (IllegalAccessException e2) {
                Log.e(TAG, "forceSetFactory2 could not set the Factory2 on LayoutInflater " + inflater + "; inflation may have unexpected results.", e2);
            }
        }
    }
}
