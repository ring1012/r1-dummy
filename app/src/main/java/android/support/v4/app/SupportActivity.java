package android.support.v4.app;

import android.app.Activity;
import android.support.annotation.RestrictTo;
import android.support.v4.util.SimpleArrayMap;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public class SupportActivity extends Activity {
    private SimpleArrayMap<Class<? extends ExtraData>, ExtraData> mExtraDataMap = new SimpleArrayMap<>();

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public static class ExtraData {
    }

    /* JADX WARN: Multi-variable type inference failed */
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public void putExtraData(ExtraData extraData) {
        this.mExtraDataMap.put(extraData.getClass(), extraData);
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public <T extends ExtraData> T getExtraData(Class<T> extraDataClass) {
        return (T) this.mExtraDataMap.get(extraDataClass);
    }
}
