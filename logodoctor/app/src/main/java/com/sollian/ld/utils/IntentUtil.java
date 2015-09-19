package com.sollian.ld.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by sollian on 2015/9/6.
 */
public class IntentUtil {
    public static final int REQUEST_CODE_SIGN_UP = 0x0001;
    public static final int REQUEST_CODE_MODIFY_PWD = 0x0002;

    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
    }

    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
    }
}
