package android.support.v4.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

@RequiresApi(9)
@TargetApi(9)
/* loaded from: classes.dex */
class LayoutInflaterCompatBase {
    LayoutInflaterCompatBase() {
    }

    static class FactoryWrapper implements LayoutInflater.Factory {
        final LayoutInflaterFactory mDelegateFactory;

        FactoryWrapper(LayoutInflaterFactory delegateFactory) {
            this.mDelegateFactory = delegateFactory;
        }

        @Override // android.view.LayoutInflater.Factory
        public View onCreateView(String name, Context context, AttributeSet attrs) {
            return this.mDelegateFactory.onCreateView(null, name, context, attrs);
        }

        public String toString() {
            return getClass().getName() + "{" + this.mDelegateFactory + "}";
        }
    }

    static void setFactory(LayoutInflater inflater, LayoutInflaterFactory factory) {
        inflater.setFactory(factory != null ? new FactoryWrapper(factory) : null);
    }

    static LayoutInflaterFactory getFactory(LayoutInflater inflater) {
        LayoutInflater.Factory factory = inflater.getFactory();
        if (factory instanceof FactoryWrapper) {
            return ((FactoryWrapper) factory).mDelegateFactory;
        }
        return null;
    }
}
