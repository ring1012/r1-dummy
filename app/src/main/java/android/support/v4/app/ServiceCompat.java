package android.support.v4.app;

import android.app.Service;
import android.support.annotation.RestrictTo;
import android.support.v4.os.BuildCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes.dex */
public final class ServiceCompat {
    static final ServiceCompatImpl IMPL;
    public static final int START_STICKY = 1;
    public static final int STOP_FOREGROUND_DETACH = 2;
    public static final int STOP_FOREGROUND_REMOVE = 1;

    interface ServiceCompatImpl {
        void stopForeground(Service service, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface StopForegroundFlags {
    }

    private ServiceCompat() {
    }

    static class BaseServiceCompatImpl implements ServiceCompatImpl {
        BaseServiceCompatImpl() {
        }

        @Override // android.support.v4.app.ServiceCompat.ServiceCompatImpl
        public void stopForeground(Service service, int flags) {
            service.stopForeground((flags & 1) != 0);
        }
    }

    static class Api24ServiceCompatImpl implements ServiceCompatImpl {
        Api24ServiceCompatImpl() {
        }

        @Override // android.support.v4.app.ServiceCompat.ServiceCompatImpl
        public void stopForeground(Service service, int flags) {
            ServiceCompatApi24.stopForeground(service, flags);
        }
    }

    static {
        if (BuildCompat.isAtLeastN()) {
            IMPL = new Api24ServiceCompatImpl();
        } else {
            IMPL = new BaseServiceCompatImpl();
        }
    }

    public static void stopForeground(Service service, int flags) {
        IMPL.stopForeground(service, flags);
    }
}
