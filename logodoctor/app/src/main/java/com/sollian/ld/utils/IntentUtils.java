package com.sollian.ld.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by sollian on 2015/9/6.
 */
public class IntentUtils {
    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }
}
