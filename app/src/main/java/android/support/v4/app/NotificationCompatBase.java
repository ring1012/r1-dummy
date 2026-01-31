package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.app.RemoteInputCompatBase;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(9)
@TargetApi(9)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: classes.dex */
public class NotificationCompatBase {
    private static Method sSetLatestEventInfo;

    public static abstract class Action {

        public interface Factory {
            Action build(int i, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle, RemoteInputCompatBase.RemoteInput[] remoteInputArr, boolean z);

            Action[] newArray(int i);
        }

        public abstract PendingIntent getActionIntent();

        public abstract boolean getAllowGeneratedReplies();

        public abstract Bundle getExtras();

        public abstract int getIcon();

        public abstract RemoteInputCompatBase.RemoteInput[] getRemoteInputs();

        public abstract CharSequence getTitle();
    }

    public static abstract class UnreadConversation {

        public interface Factory {
            UnreadConversation build(String[] strArr, RemoteInputCompatBase.RemoteInput remoteInput, PendingIntent pendingIntent, PendingIntent pendingIntent2, String[] strArr2, long j);
        }

        abstract long getLatestTimestamp();

        abstract String[] getMessages();

        abstract String getParticipant();

        abstract String[] getParticipants();

        abstract PendingIntent getReadPendingIntent();

        abstract RemoteInputCompatBase.RemoteInput getRemoteInput();

        abstract PendingIntent getReplyPendingIntent();
    }

    public static Notification add(Notification notification, Context context, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent, PendingIntent fullScreenIntent) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (sSetLatestEventInfo == null) {
            try {
                sSetLatestEventInfo = Notification.class.getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            sSetLatestEventInfo.invoke(notification, context, contentTitle, contentText, contentIntent);
            notification.fullScreenIntent = fullScreenIntent;
            return notification;
        } catch (IllegalAccessException | InvocationTargetException e2) {
            throw new RuntimeException(e2);
        }
    }
}
