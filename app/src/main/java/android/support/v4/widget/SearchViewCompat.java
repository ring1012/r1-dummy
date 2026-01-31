package android.support.v4.widget;

import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SearchViewCompatHoneycomb;
import android.view.View;

/* loaded from: classes.dex */
public final class SearchViewCompat {
    private static final SearchViewCompatImpl IMPL;

    public interface OnCloseListener {
        boolean onClose();
    }

    public interface OnQueryTextListener {
        boolean onQueryTextChange(String str);

        boolean onQueryTextSubmit(String str);
    }

    interface SearchViewCompatImpl {
        CharSequence getQuery(View view);

        boolean isIconified(View view);

        boolean isQueryRefinementEnabled(View view);

        boolean isSubmitButtonEnabled(View view);

        Object newOnCloseListener(OnCloseListener onCloseListener);

        Object newOnQueryTextListener(OnQueryTextListener onQueryTextListener);

        View newSearchView(Context context);

        void setIconified(View view, boolean z);

        void setImeOptions(View view, int i);

        void setInputType(View view, int i);

        void setMaxWidth(View view, int i);

        void setOnCloseListener(View view, OnCloseListener onCloseListener);

        void setOnQueryTextListener(View view, OnQueryTextListener onQueryTextListener);

        void setQuery(View view, CharSequence charSequence, boolean z);

        void setQueryHint(View view, CharSequence charSequence);

        void setQueryRefinementEnabled(View view, boolean z);

        void setSearchableInfo(View view, ComponentName componentName);

        void setSubmitButtonEnabled(View view, boolean z);
    }

    static class SearchViewCompatStubImpl implements SearchViewCompatImpl {
        SearchViewCompatStubImpl() {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public View newSearchView(Context context) {
            return null;
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setSearchableInfo(View searchView, ComponentName searchableComponent) {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setImeOptions(View searchView, int imeOptions) {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setInputType(View searchView, int inputType) {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public Object newOnQueryTextListener(OnQueryTextListener listener) {
            return null;
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setOnQueryTextListener(View searchView, OnQueryTextListener listener) {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public Object newOnCloseListener(OnCloseListener listener) {
            return null;
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setOnCloseListener(View searchView, OnCloseListener listener) {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public CharSequence getQuery(View searchView) {
            return null;
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setQuery(View searchView, CharSequence query, boolean submit) {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setQueryHint(View searchView, CharSequence hint) {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setIconified(View searchView, boolean iconify) {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public boolean isIconified(View searchView) {
            return true;
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setSubmitButtonEnabled(View searchView, boolean enabled) {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public boolean isSubmitButtonEnabled(View searchView) {
            return false;
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setQueryRefinementEnabled(View searchView, boolean enable) {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public boolean isQueryRefinementEnabled(View searchView) {
            return false;
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setMaxWidth(View searchView, int maxpixels) {
        }
    }

    static class SearchViewCompatHoneycombImpl extends SearchViewCompatStubImpl {
        SearchViewCompatHoneycombImpl() {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public View newSearchView(Context context) {
            return SearchViewCompatHoneycomb.newSearchView(context);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setSearchableInfo(View searchView, ComponentName searchableComponent) {
            checkIfLegalArg(searchView);
            SearchViewCompatHoneycomb.setSearchableInfo(searchView, searchableComponent);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public Object newOnQueryTextListener(final OnQueryTextListener listener) {
            return SearchViewCompatHoneycomb.newOnQueryTextListener(new SearchViewCompatHoneycomb.OnQueryTextListenerCompatBridge() { // from class: android.support.v4.widget.SearchViewCompat.SearchViewCompatHoneycombImpl.1
                @Override // android.support.v4.widget.SearchViewCompatHoneycomb.OnQueryTextListenerCompatBridge
                public boolean onQueryTextSubmit(String query) {
                    return listener.onQueryTextSubmit(query);
                }

                @Override // android.support.v4.widget.SearchViewCompatHoneycomb.OnQueryTextListenerCompatBridge
                public boolean onQueryTextChange(String newText) {
                    return listener.onQueryTextChange(newText);
                }
            });
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setOnQueryTextListener(View searchView, OnQueryTextListener listener) {
            checkIfLegalArg(searchView);
            SearchViewCompatHoneycomb.setOnQueryTextListener(searchView, newOnQueryTextListener(listener));
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public Object newOnCloseListener(final OnCloseListener listener) {
            return SearchViewCompatHoneycomb.newOnCloseListener(new SearchViewCompatHoneycomb.OnCloseListenerCompatBridge() { // from class: android.support.v4.widget.SearchViewCompat.SearchViewCompatHoneycombImpl.2
                @Override // android.support.v4.widget.SearchViewCompatHoneycomb.OnCloseListenerCompatBridge
                public boolean onClose() {
                    return listener.onClose();
                }
            });
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setOnCloseListener(View searchView, OnCloseListener listener) {
            checkIfLegalArg(searchView);
            SearchViewCompatHoneycomb.setOnCloseListener(searchView, newOnCloseListener(listener));
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public CharSequence getQuery(View searchView) {
            checkIfLegalArg(searchView);
            return SearchViewCompatHoneycomb.getQuery(searchView);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setQuery(View searchView, CharSequence query, boolean submit) {
            checkIfLegalArg(searchView);
            SearchViewCompatHoneycomb.setQuery(searchView, query, submit);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setQueryHint(View searchView, CharSequence hint) {
            checkIfLegalArg(searchView);
            SearchViewCompatHoneycomb.setQueryHint(searchView, hint);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setIconified(View searchView, boolean iconify) {
            checkIfLegalArg(searchView);
            SearchViewCompatHoneycomb.setIconified(searchView, iconify);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public boolean isIconified(View searchView) {
            checkIfLegalArg(searchView);
            return SearchViewCompatHoneycomb.isIconified(searchView);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setSubmitButtonEnabled(View searchView, boolean enabled) {
            checkIfLegalArg(searchView);
            SearchViewCompatHoneycomb.setSubmitButtonEnabled(searchView, enabled);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public boolean isSubmitButtonEnabled(View searchView) {
            checkIfLegalArg(searchView);
            return SearchViewCompatHoneycomb.isSubmitButtonEnabled(searchView);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setQueryRefinementEnabled(View searchView, boolean enable) {
            checkIfLegalArg(searchView);
            SearchViewCompatHoneycomb.setQueryRefinementEnabled(searchView, enable);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public boolean isQueryRefinementEnabled(View searchView) {
            checkIfLegalArg(searchView);
            return SearchViewCompatHoneycomb.isQueryRefinementEnabled(searchView);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setMaxWidth(View searchView, int maxpixels) {
            checkIfLegalArg(searchView);
            SearchViewCompatHoneycomb.setMaxWidth(searchView, maxpixels);
        }

        protected void checkIfLegalArg(View searchView) {
            SearchViewCompatHoneycomb.checkIfLegalArg(searchView);
        }
    }

    static class SearchViewCompatIcsImpl extends SearchViewCompatHoneycombImpl {
        SearchViewCompatIcsImpl() {
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatHoneycombImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public View newSearchView(Context context) {
            return SearchViewCompatIcs.newSearchView(context);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setImeOptions(View searchView, int imeOptions) {
            checkIfLegalArg(searchView);
            SearchViewCompatIcs.setImeOptions(searchView, imeOptions);
        }

        @Override // android.support.v4.widget.SearchViewCompat.SearchViewCompatStubImpl, android.support.v4.widget.SearchViewCompat.SearchViewCompatImpl
        public void setInputType(View searchView, int inputType) {
            checkIfLegalArg(searchView);
            SearchViewCompatIcs.setInputType(searchView, inputType);
        }
    }

    static {
        if (Build.VERSION.SDK_INT >= 14) {
            IMPL = new SearchViewCompatIcsImpl();
        } else if (Build.VERSION.SDK_INT >= 11) {
            IMPL = new SearchViewCompatHoneycombImpl();
        } else {
            IMPL = new SearchViewCompatStubImpl();
        }
    }

    private SearchViewCompat(Context context) {
    }

    public static View newSearchView(Context context) {
        return IMPL.newSearchView(context);
    }

    public static void setSearchableInfo(View searchView, ComponentName searchableComponent) {
        IMPL.setSearchableInfo(searchView, searchableComponent);
    }

    public static void setImeOptions(View searchView, int imeOptions) {
        IMPL.setImeOptions(searchView, imeOptions);
    }

    public static void setInputType(View searchView, int inputType) {
        IMPL.setInputType(searchView, inputType);
    }

    public static void setOnQueryTextListener(View searchView, OnQueryTextListener listener) {
        IMPL.setOnQueryTextListener(searchView, listener);
    }

    @Deprecated
    public static abstract class OnQueryTextListenerCompat implements OnQueryTextListener {
        @Override // android.support.v4.widget.SearchViewCompat.OnQueryTextListener
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override // android.support.v4.widget.SearchViewCompat.OnQueryTextListener
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }

    public static void setOnCloseListener(View searchView, OnCloseListener listener) {
        IMPL.setOnCloseListener(searchView, listener);
    }

    @Deprecated
    public static abstract class OnCloseListenerCompat implements OnCloseListener {
        @Override // android.support.v4.widget.SearchViewCompat.OnCloseListener
        public boolean onClose() {
            return false;
        }
    }

    public static CharSequence getQuery(View searchView) {
        return IMPL.getQuery(searchView);
    }

    public static void setQuery(View searchView, CharSequence query, boolean submit) {
        IMPL.setQuery(searchView, query, submit);
    }

    public static void setQueryHint(View searchView, CharSequence hint) {
        IMPL.setQueryHint(searchView, hint);
    }

    public static void setIconified(View searchView, boolean iconify) {
        IMPL.setIconified(searchView, iconify);
    }

    public static boolean isIconified(View searchView) {
        return IMPL.isIconified(searchView);
    }

    public static void setSubmitButtonEnabled(View searchView, boolean enabled) {
        IMPL.setSubmitButtonEnabled(searchView, enabled);
    }

    public static boolean isSubmitButtonEnabled(View searchView) {
        return IMPL.isSubmitButtonEnabled(searchView);
    }

    public static void setQueryRefinementEnabled(View searchView, boolean enable) {
        IMPL.setQueryRefinementEnabled(searchView, enable);
    }

    public static boolean isQueryRefinementEnabled(View searchView) {
        return IMPL.isQueryRefinementEnabled(searchView);
    }

    public static void setMaxWidth(View searchView, int maxpixels) {
        IMPL.setMaxWidth(searchView, maxpixels);
    }
}
