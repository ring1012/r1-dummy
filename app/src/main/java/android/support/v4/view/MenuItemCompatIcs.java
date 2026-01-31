package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;

@RequiresApi(14)
@TargetApi(14)
/* loaded from: classes.dex */
class MenuItemCompatIcs {

    interface SupportActionExpandProxy {
        boolean onMenuItemActionCollapse(MenuItem menuItem);

        boolean onMenuItemActionExpand(MenuItem menuItem);
    }

    MenuItemCompatIcs() {
    }

    public static boolean expandActionView(MenuItem item) {
        return item.expandActionView();
    }

    public static boolean collapseActionView(MenuItem item) {
        return item.collapseActionView();
    }

    public static boolean isActionViewExpanded(MenuItem item) {
        return item.isActionViewExpanded();
    }

    public static MenuItem setOnActionExpandListener(MenuItem item, SupportActionExpandProxy listener) {
        return item.setOnActionExpandListener(new OnActionExpandListenerWrapper(listener));
    }

    static class OnActionExpandListenerWrapper implements MenuItem.OnActionExpandListener {
        private SupportActionExpandProxy mWrapped;

        public OnActionExpandListenerWrapper(SupportActionExpandProxy wrapped) {
            this.mWrapped = wrapped;
        }

        @Override // android.view.MenuItem.OnActionExpandListener
        public boolean onMenuItemActionExpand(MenuItem item) {
            return this.mWrapped.onMenuItemActionExpand(item);
        }

        @Override // android.view.MenuItem.OnActionExpandListener
        public boolean onMenuItemActionCollapse(MenuItem item) {
            return this.mWrapped.onMenuItemActionCollapse(item);
        }
    }
}
