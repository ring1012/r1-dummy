package retrofit2;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
import retrofit2.CallAdapter;

/* loaded from: classes.dex */
class Platform {
    private static final Platform PLATFORM = findPlatform();

    Platform() {
    }

    static Platform get() {
        return PLATFORM;
    }

    private static Platform findPlatform() throws ClassNotFoundException {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return new Android();
            }
        } catch (ClassNotFoundException e) {
        }
        try {
            Class.forName("java.util.Optional");
            return new Java8();
        } catch (ClassNotFoundException e2) {
            return new Platform();
        }
    }

    @Nullable
    Executor defaultCallbackExecutor() {
        return null;
    }

    CallAdapter.Factory defaultCallAdapterFactory(@Nullable Executor callbackExecutor) {
        return callbackExecutor != null ? new ExecutorCallAdapterFactory(callbackExecutor) : DefaultCallAdapterFactory.INSTANCE;
    }

    boolean isDefaultMethod(Method method) {
        return false;
    }

    @Nullable
    Object invokeDefaultMethod(Method method, Class<?> declaringClass, Object object, @Nullable Object... args) throws Throwable {
        throw new UnsupportedOperationException();
    }

    @IgnoreJRERequirement
    static class Java8 extends Platform {
        Java8() {
        }

        @Override // retrofit2.Platform
        boolean isDefaultMethod(Method method) {
            return method.isDefault();
        }

        @Override // retrofit2.Platform
        Object invokeDefaultMethod(Method method, Class<?> declaringClass, Object object, @Nullable Object... args) throws Throwable {
            Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Integer.TYPE);
            constructor.setAccessible(true);
            return constructor.newInstance(declaringClass, -1).unreflectSpecial(method, declaringClass).bindTo(object).invokeWithArguments(args);
        }
    }

    static class Android extends Platform {
        Android() {
        }

        @Override // retrofit2.Platform
        public Executor defaultCallbackExecutor() {
            return new MainThreadExecutor();
        }

        @Override // retrofit2.Platform
        CallAdapter.Factory defaultCallAdapterFactory(@Nullable Executor callbackExecutor) {
            if (callbackExecutor == null) {
                throw new AssertionError();
            }
            return new ExecutorCallAdapterFactory(callbackExecutor);
        }

        static class MainThreadExecutor implements Executor {
            private final Handler handler = new Handler(Looper.getMainLooper());

            MainThreadExecutor() {
            }

            @Override // java.util.concurrent.Executor
            public void execute(Runnable r) {
                this.handler.post(r);
            }
        }
    }
}
