package android.support.multidex;

import android.app.Application;
import android.content.Context;

/* loaded from: classes.dex */
public class MultiDexApplication extends Application {
    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
