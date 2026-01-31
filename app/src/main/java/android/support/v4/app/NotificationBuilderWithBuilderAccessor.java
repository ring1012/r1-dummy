package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;

@RequiresApi(11)
@TargetApi(11)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public interface NotificationBuilderWithBuilderAccessor {
    Notification build();

    Notification.Builder getBuilder();
}
