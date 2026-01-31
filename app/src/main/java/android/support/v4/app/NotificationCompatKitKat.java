package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompatBase;
import android.support.v4.app.RemoteInputCompatBase;
import android.util.SparseArray;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(19)
@TargetApi(19)
/* loaded from: classes.dex */
class NotificationCompatKitKat {
    NotificationCompatKitKat() {
    }

    public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions {
        private Notification.Builder b;
        private List<Bundle> mActionExtrasList = new ArrayList();
        private RemoteViews mBigContentView;
        private RemoteViews mContentView;
        private Bundle mExtras;

        public Builder(Context context, Notification n, CharSequence contentTitle, CharSequence contentText, CharSequence contentInfo, RemoteViews tickerView, int number, PendingIntent contentIntent, PendingIntent fullScreenIntent, Bitmap largeIcon, int progressMax, int progress, boolean progressIndeterminate, boolean showWhen, boolean useChronometer, int priority, CharSequence subText, boolean localOnly, ArrayList<String> people, Bundle extras, String groupKey, boolean groupSummary, String sortKey, RemoteViews contentView, RemoteViews bigContentView) {
            this.b = new Notification.Builder(context).setWhen(n.when).setShowWhen(showWhen).setSmallIcon(n.icon, n.iconLevel).setContent(n.contentView).setTicker(n.tickerText, tickerView).setSound(n.sound, n.audioStreamType).setVibrate(n.vibrate).setLights(n.ledARGB, n.ledOnMS, n.ledOffMS).setOngoing((n.flags & 2) != 0).setOnlyAlertOnce((n.flags & 8) != 0).setAutoCancel((n.flags & 16) != 0).setDefaults(n.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(n.deleteIntent).setFullScreenIntent(fullScreenIntent, (n.flags & 128) != 0).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(useChronometer).setPriority(priority).setProgress(progressMax, progress, progressIndeterminate);
            this.mExtras = new Bundle();
            if (extras != null) {
                this.mExtras.putAll(extras);
            }
            if (people != null && !people.isEmpty()) {
                this.mExtras.putStringArray(NotificationCompat.EXTRA_PEOPLE, (String[]) people.toArray(new String[people.size()]));
            }
            if (localOnly) {
                this.mExtras.putBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY, true);
            }
            if (groupKey != null) {
                this.mExtras.putString(NotificationCompatExtras.EXTRA_GROUP_KEY, groupKey);
                if (groupSummary) {
                    this.mExtras.putBoolean(NotificationCompatExtras.EXTRA_GROUP_SUMMARY, true);
                } else {
                    this.mExtras.putBoolean(NotificationManagerCompat.EXTRA_USE_SIDE_CHANNEL, true);
                }
            }
            if (sortKey != null) {
                this.mExtras.putString(NotificationCompatExtras.EXTRA_SORT_KEY, sortKey);
            }
            this.mContentView = contentView;
            this.mBigContentView = bigContentView;
        }

        @Override // android.support.v4.app.NotificationBuilderWithActions
        public void addAction(NotificationCompatBase.Action action) {
            this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.b, action));
        }

        @Override // android.support.v4.app.NotificationBuilderWithBuilderAccessor
        public Notification.Builder getBuilder() {
            return this.b;
        }

        @Override // android.support.v4.app.NotificationBuilderWithBuilderAccessor
        public Notification build() {
            SparseArray<Bundle> actionExtrasMap = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (actionExtrasMap != null) {
                this.mExtras.putSparseParcelableArray(NotificationCompatExtras.EXTRA_ACTION_EXTRAS, actionExtrasMap);
            }
            this.b.setExtras(this.mExtras);
            Notification notification = this.b.build();
            if (this.mContentView != null) {
                notification.contentView = this.mContentView;
            }
            if (this.mBigContentView != null) {
                notification.bigContentView = this.mBigContentView;
            }
            return notification;
        }
    }

    public static Bundle getExtras(Notification notif) {
        return notif.extras;
    }

    public static int getActionCount(Notification notif) {
        if (notif.actions != null) {
            return notif.actions.length;
        }
        return 0;
    }

    public static NotificationCompatBase.Action getAction(Notification notif, int actionIndex, NotificationCompatBase.Action.Factory factory, RemoteInputCompatBase.RemoteInput.Factory remoteInputFactory) {
        Notification.Action action = notif.actions[actionIndex];
        Bundle actionExtras = null;
        SparseArray<Bundle> actionExtrasMap = notif.extras.getSparseParcelableArray(NotificationCompatExtras.EXTRA_ACTION_EXTRAS);
        if (actionExtrasMap != null) {
            Bundle actionExtras2 = actionExtrasMap.get(actionIndex);
            actionExtras = actionExtras2;
        }
        return NotificationCompatJellybean.readAction(factory, remoteInputFactory, action.icon, action.title, action.actionIntent, actionExtras);
    }

    public static boolean getLocalOnly(Notification notif) {
        return notif.extras.getBoolean(NotificationCompatExtras.EXTRA_LOCAL_ONLY);
    }

    public static String getGroup(Notification notif) {
        return notif.extras.getString(NotificationCompatExtras.EXTRA_GROUP_KEY);
    }

    public static boolean isGroupSummary(Notification notif) {
        return notif.extras.getBoolean(NotificationCompatExtras.EXTRA_GROUP_SUMMARY);
    }

    public static String getSortKey(Notification notif) {
        return notif.extras.getString(NotificationCompatExtras.EXTRA_SORT_KEY);
    }
}
