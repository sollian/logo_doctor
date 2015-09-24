package com.sollian.ld.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.sollian.ld.R;

/**
 * Created by sollian on 2015/9/24.
 */
public class NotifyUtil {
    private static final int NOTIFY_ID = 0X1111;

    public static void showNotify(Context context, Intent intent, String title, String message) {
        PendingIntent pi = PendingIntent.getActivity(context, NOTIFY_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification();
        notification.icon = R.drawable.ic_launcher;
        notification.tickerText = context.getString(R.string.app_name);
        notification.when = System.currentTimeMillis();
        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(context, title, message, pi);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFY_ID, notification);
    }

    public static void cancelAll(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }
}
