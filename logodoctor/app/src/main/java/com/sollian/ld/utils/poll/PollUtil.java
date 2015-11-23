package com.sollian.ld.utils.poll;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;

import com.sollian.ld.utils.SharePrefUtil;

/**
 * Created by sollian on 2015/9/25.
 */
public class PollUtil {
    private static final int INTERNAL = 15 * 1000;

    public static void startPoll(Context context) {
        if (TextUtils.isEmpty(new SharePrefUtil.RemindPref().getRemindIds())) {
            return;
        }
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, PollRemindService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long triggerAtTime = SystemClock.elapsedRealtime();

        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,
            INTERNAL, pendingIntent);
    }

    public static void stopPoll(Context context) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, PollRemindService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0,
            intent, PendingIntent.FLAG_CANCEL_CURRENT);
        manager.cancel(pendingIntent);
    }
}