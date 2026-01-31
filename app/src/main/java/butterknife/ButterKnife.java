package butterknife;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.Property;
import android.view.View;
import butterknife.internal.ButterKnifeProcessor;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public final class ButterKnife {
    private static final String TAG = "ButterKnife";
    private static boolean debug = false;
    static final Map<Class<?>, ViewBinder<Object>> BINDERS = new LinkedHashMap();
    static final ViewBinder<Object> NOP_VIEW_BINDER = new ViewBinder<Object>() { // from class: butterknife.ButterKnife.1
        @Override // butterknife.ButterKnife.ViewBinder
        public void bind(Finder finder, Object target, Object source) {
        }

        @Override // butterknife.ButterKnife.ViewBinder
        public void unbind(Object target) {
        }
    };

    public interface Action<T extends View> {
        void apply(T t, int i);
    }

    public interface Setter<T extends View, V> {
        void set(T t, V v, int i);
    }

    public interface ViewBinder<T> {
        void bind(Finder finder, T t, Object obj);

        void unbind(T t);
    }

    private ButterKnife() {
        throw new AssertionError("No instances.");
    }

    public enum Finder {
        VIEW { // from class: butterknife.ButterKnife.Finder.1
            @Override // butterknife.ButterKnife.Finder
            protected View findView(Object source, int id) {
                return ((View) source).findViewById(id);
            }

            @Override // butterknife.ButterKnife.Finder
            public Context getContext(Object source) {
                return ((View) source).getContext();
            }
        },
        ACTIVITY { // from class: butterknife.ButterKnife.Finder.2
            @Override // butterknife.ButterKnife.Finder
            protected View findView(Object source, int id) {
                return ((Activity) source).findViewById(id);
            }

            @Override // butterknife.ButterKnife.Finder
            public Context getContext(Object source) {
                return (Activity) source;
            }
        },
        DIALOG { // from class: butterknife.ButterKnife.Finder.3
            @Override // butterknife.ButterKnife.Finder
            protected View findView(Object source, int id) {
                return ((Dialog) source).findViewById(id);
            }

            @Override // butterknife.ButterKnife.Finder
            public Context getContext(Object source) {
                return ((Dialog) source).getContext();
            }
        };

        protected abstract View findView(Object obj, int i);

        public abstract Context getContext(Object obj);

        private static <T> T[] filterNull(T[] tArr) {
            int i = 0;
            for (T t : tArr) {
                if (t != null) {
                    tArr[i] = t;
                    i++;
                }
            }
            return (T[]) Arrays.copyOfRange(tArr, 0, i);
        }

        public static <T> T[] arrayOf(T... tArr) {
            return (T[]) filterNull(tArr);
        }

        public static <T> List<T> listOf(T... views) {
            return new ImmutableList(filterNull(views));
        }

        public <T> T findRequiredView(Object obj, int i, String str) throws Resources.NotFoundException {
            T t = (T) findOptionalView(obj, i, str);
            if (t == null) {
                throw new IllegalStateException("Required view '" + getContext(obj).getResources().getResourceEntryName(i) + "' with ID " + i + " for " + str + " was not found. If this view is optional add '@Nullable' annotation.");
            }
            return t;
        }

        public <T> T findOptionalView(Object obj, int i, String str) {
            return (T) castView(findView(obj, i), i, str);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <T> T castView(View view, int id, String who) {
            return view;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public <T> T castParam(Object obj, String from, int fromPosition, String to, int toPosition) {
            return obj;
        }
    }

    public static void setDebug(boolean debug2) {
        debug = debug2;
    }

    public static void bind(Activity target) {
        bind(target, target, Finder.ACTIVITY);
    }

    public static void bind(View target) {
        bind(target, target, Finder.VIEW);
    }

    public static void bind(Dialog target) {
        bind(target, target, Finder.DIALOG);
    }

    public static void bind(Object target, Activity source) {
        bind(target, source, Finder.ACTIVITY);
    }

    public static void bind(Object target, View source) {
        bind(target, source, Finder.VIEW);
    }

    public static void bind(Object target, Dialog source) {
        bind(target, source, Finder.DIALOG);
    }

    public static void unbind(Object target) {
        Class<?> targetClass = target.getClass();
        try {
            if (debug) {
                Log.d(TAG, "Looking up view binder for " + targetClass.getName());
            }
            ViewBinder<Object> viewBinder = findViewBinderForClass(targetClass);
            if (viewBinder != null) {
                viewBinder.unbind(target);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to unbind views for " + targetClass.getName(), e);
        }
    }

    static void bind(Object target, Object source, Finder finder) {
        Class<?> targetClass = target.getClass();
        try {
            if (debug) {
                Log.d(TAG, "Looking up view binder for " + targetClass.getName());
            }
            ViewBinder<Object> viewBinder = findViewBinderForClass(targetClass);
            if (viewBinder != null) {
                viewBinder.bind(finder, target, source);
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to bind views for " + targetClass.getName(), e);
        }
    }

    private static ViewBinder<Object> findViewBinderForClass(Class<?> cls) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        ViewBinder<Object> viewBinder;
        ViewBinder<Object> viewBinder2 = BINDERS.get(cls);
        if (viewBinder2 != null) {
            if (debug) {
                Log.d(TAG, "HIT: Cached in view binder map.");
            }
            return viewBinder2;
        }
        String clsName = cls.getName();
        if (clsName.startsWith(ButterKnifeProcessor.ANDROID_PREFIX) || clsName.startsWith(ButterKnifeProcessor.JAVA_PREFIX)) {
            if (debug) {
                Log.d(TAG, "MISS: Reached framework class. Abandoning search.");
            }
            return NOP_VIEW_BINDER;
        }
        try {
            Class<?> viewBindingClass = Class.forName(clsName + ButterKnifeProcessor.SUFFIX);
            viewBinder = (ViewBinder) viewBindingClass.newInstance();
            if (debug) {
                Log.d(TAG, "HIT: Loaded view binder class.");
            }
        } catch (ClassNotFoundException e) {
            if (debug) {
                Log.d(TAG, "Not found. Trying superclass " + cls.getSuperclass().getName());
            }
            viewBinder = findViewBinderForClass(cls.getSuperclass());
        }
        BINDERS.put(cls, viewBinder);
        return viewBinder;
    }

    public static <T extends View> void apply(List<T> list, Action<? super T> action) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            action.apply(list.get(i), i);
        }
    }

    public static <T extends View, V> void apply(List<T> list, Setter<? super T, V> setter, V value) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            setter.set(list.get(i), value, i);
        }
    }

    @TargetApi(14)
    public static <T extends View, V> void apply(List<T> list, Property<? super T, V> setter, V value) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            setter.set(list.get(i), value);
        }
    }

    public static <T extends View> T findById(View view, int i) {
        return (T) view.findViewById(i);
    }

    public static <T extends View> T findById(Activity activity, int i) {
        return (T) activity.findViewById(i);
    }

    public static <T extends View> T findById(Dialog dialog, int i) {
        return (T) dialog.findViewById(i);
    }
}
